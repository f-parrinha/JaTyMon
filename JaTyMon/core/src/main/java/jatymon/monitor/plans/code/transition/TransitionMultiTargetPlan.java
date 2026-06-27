package jatymon.monitor.plans.code.transition;

import com.palantir.javapoet.CodeBlock;
import jatymon.common.globals.JavaTypes;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.superCall.SuperCallResultVariablePlan;
import jatymon.monitor.plans.fields.CurrentStateFieldPlan;
import jatymon.monitor.plans.types.StatesEnumPlan;
import jatymon.typestate.ast.nodes.key.TKeyNode;
import jatymon.typestate.graph.nodes.GraphNode;
import jatymon.typestate.graph.transitions.GraphMultiTransition;

import java.util.Map;

/**
 * Class {@code TransitionMultiTargetPlan} represents the code structure for the actual transition mechanism
 *  (changing the value of the current state) for a {@code GraphMultiTransition}. This means it handles multiple output possibilities
 * @author Francisco Parrinha
 */
public class TransitionMultiTargetPlan extends TransitionTargetPlan {
    public TransitionMultiTargetPlan(final GraphMultiTransition transition) {
        super(transition);
    }

    @Override
    public CodeBlock emit(final MonitorFactory.BuildContext ctx) {
        final Map<String, GraphNode> outs = ((GraphMultiTransition) graphTransition).getOuts();
        final TKeyNode key = ctx.typestateData().getKey();
        final CodeBlock.Builder code = CodeBlock.builder();

        // Note: booleans require if-else statements.
        if (JavaTypes.isBoolean(graphTransition.getReturnType())) {
            final GraphNode trueOut = outs.get("true");
            final GraphNode falseOut = outs.get("false");
            code.beginControlFlow("if ($L)", SuperCallResultVariablePlan.VAR_NAME)
                    .addStatement(CurrentStateFieldPlan.getAssignCode(key, CodeBlock.of("$T.$L", ctx.getClassName(StatesEnumPlan.ENUM_NAME), trueOut.getName())))
                    .nextControlFlow("else")
                    .addStatement(CurrentStateFieldPlan.getAssignCode(key, CodeBlock.of("$T.$L" ,ctx.getClassName(StatesEnumPlan.ENUM_NAME), falseOut.getName())))
                    .endControlFlow();
        } else {
            code.beginControlFlow("switch ($L)", SuperCallResultVariablePlan.VAR_NAME);
            outs.forEach((label, out) -> code.add("case $L:\n", label)
                    .indent()
                    .addStatement(CurrentStateFieldPlan.getAssignCode(key, CodeBlock.of("$T.$L", ctx.getClassName(StatesEnumPlan.ENUM_NAME), out.getName())))
                    .addStatement("break")
                    .unindent());
            code.endControlFlow();
        }

        // Add post-assignments
        graphTransition.getPostAssignments().forEach(a -> code.addStatement("$L()", a));
        return code.build();
    }
}
