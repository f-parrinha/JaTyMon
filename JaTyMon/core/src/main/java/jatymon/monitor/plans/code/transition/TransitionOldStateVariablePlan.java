package jatymon.monitor.plans.code.transition;

import com.palantir.javapoet.CodeBlock;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.CodePlan;
import jatymon.monitor.plans.fields.CurrentStateFieldPlan;
import jatymon.monitor.plans.types.StatesEnumPlan;


public record TransitionOldStateVariablePlan() implements CodePlan {
    public static final String VAR_NAME = "oldState";

    @Override
    public CodeBlock emit(final MonitorFactory.BuildContext ctx) {
        final CodeBlock firstState = CodeBlock.of("$T.$L", ctx.getClassName(StatesEnumPlan.ENUM_NAME), ctx.typestateData().getGraph().getStart().getName());
        return CodeBlock.builder()
                .addStatement("final $T $L = $L",
                        ctx.getClassName(StatesEnumPlan.ENUM_NAME),
                        VAR_NAME,
                        CurrentStateFieldPlan.getGetterCode(ctx.typestateData().getKey(), firstState))
                .build();
    }
}
