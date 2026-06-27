package jatymon.monitor.plans.methods;

import com.palantir.javapoet.*;
import jatymon.actions.ActionType;
import jatymon.exceptions.discarding.MonitorDiscardException;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.MonitorUtils;
import jatymon.monitor.plans.code.logging.LogIllegalActionCodePlan;
import jatymon.monitor.plans.code.logging.LogInternalExceptionCodePlan;
import jatymon.monitor.plans.code.superCall.SuperCallResultVariablePlan;
import jatymon.monitor.plans.code.superCall.SuperCallPlan;
import jatymon.monitor.plans.code.transition.TransitionOldStateVariablePlan;
import jatymon.monitor.plans.fields.CurrentStateFieldPlan;
import jatymon.monitor.plans.code.transition.TransitionCasePlan;
import jatymon.monitor.plans.types.StatesEnumPlan;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;


/**
 * Class {@code TransitionMethodPlan} is an internal representation of a transition method, this is, a method that is used
 *  by the typestate
 * @author Francisco Parrinha
 */
public record TransitionMethodPlan(String name,
                                   TypeMirror returnType,
                                   List<TypeMirror> argsTypes,
                                   List<? extends TypeMirror> thrownTypes,
                                   ActionType actionType,
                                   Set<TransitionCasePlan> casePlans) implements MethodPlan {
    private static final String LOG_ILLEGAL_ACTIONS_VAR = "logIllegalActions";


    @Override
    public MethodSpec emit(final MonitorFactory.BuildContext ctx) {
        final List<ParameterSpec> parameters = MonitorUtils.getParameterSpecs(argsTypes);
        final SuperCallPlan superCallPlan = new SuperCallPlan(name, returnType, MonitorUtils.getParametersNames(parameters));
        final SuperCallResultVariablePlan superCallResVar = superCallPlan.getResultVariable();
        final TransitionOldStateVariablePlan oldStateVar = new TransitionOldStateVariablePlan();
        final CodeBlock.Builder body = CodeBlock.builder();

        // Init method (init return res, init old state var and start try-catch for internal exceptions)
        body.add(superCallResVar.emit(ctx))
                .add(oldStateVar.emit(ctx))
                .addStatement("boolean $L = false", LOG_ILLEGAL_ACTIONS_VAR)
                .beginControlFlow("try");

        // Build switch case for each transition possible depending on the input state
        body.add(buildTransitionCases(ctx, superCallPlan));

        // Close try-catch for internal exceptions
        body.nextControlFlow("catch(final $T e)", ClassName.get(MonitorDiscardException.class))
                .addStatement("$L = false", LOG_ILLEGAL_ACTIONS_VAR)
                .nextControlFlow("catch(final $T e)", ClassName.get(Exception.class))
                .addStatement(CurrentStateFieldPlan.getAssignCode(ctx.typestateData().getKey(), CodeBlock.of("$L", TransitionOldStateVariablePlan.VAR_NAME)))
                .addStatement(new LogInternalExceptionCodePlan("e.getMessage()").emit(ctx))
                .endControlFlow();

        // Log illegal actions
        body.beginControlFlow("if ($L)", LOG_ILLEGAL_ACTIONS_VAR)
                .addStatement(new LogIllegalActionCodePlan(name, actionType).emit(ctx))
                .endControlFlow();

        // Add return statement if required (the return value is the one given by the super() call) and catch internal exceptions
        body.add(superCallResVar.getReturnStatement());
        return MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameters(parameters)
                .addExceptions(thrownTypes.stream().map(ClassName::get).toList())
                .returns(ClassName.get(returnType))
                .addCode(body.build())
                .build();
    }

    /**
     * Builds the code for each possible transition case. Usually the transition is chosen with a switch case, however,
     *  if there is only one case possible (including the default one) we use an if-else statement.
     * @param ctx monitor building context object
     * @param superCallPlan plan for the super.method() call
     * @return JavaPoet code block containing all possible transition cases
     */
    private CodeBlock buildTransitionCases(final MonitorFactory.BuildContext ctx,
                                           final SuperCallPlan superCallPlan) {
        final CodeBlock firstState = CodeBlock.of("$T.$L", ctx.getClassName(StatesEnumPlan.ENUM_NAME), ctx.typestateData().getGraph().getStart().getName());
        final CodeBlock currentStateValue =  CurrentStateFieldPlan.getGetterCode(ctx.typestateData().getKey(), firstState);
        final CodeBlock.Builder cases = CodeBlock.builder();
        if (casePlans.size() == 1) {
            final TransitionCasePlan single = casePlans.iterator().next();
            cases.beginControlFlow("if ($L == $T.$L)", currentStateValue, ctx.getClassName(StatesEnumPlan.ENUM_NAME), single.inputState())
                    .add(single.emit(ctx, superCallPlan))
                    .nextControlFlow("else")
                    .addStatement("$L = true", LOG_ILLEGAL_ACTIONS_VAR)
                    .add(superCallPlan.emit(ctx))
                    .endControlFlow();
            return cases.build();
        }

        // Build switch case for multiple transitions
        cases.beginControlFlow("switch ($L)", currentStateValue);
        casePlans.forEach(c -> {
            cases.add("case $L:\n", c.inputState()).indent();
            cases.add(c.emit(ctx, superCallPlan));
            cases.addStatement("break").unindent();
        });
        cases.add("default:\n").indent()
                .addStatement("$L = true", LOG_ILLEGAL_ACTIONS_VAR)
                .add(superCallPlan.emit(ctx))
                .unindent()
                .endControlFlow();
        return cases.build();
    }
}
