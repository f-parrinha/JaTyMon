package jatymon.monitor.plans.code.logging;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import jatymon.logging.logs.ratio.RatioLogFactory;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.CodePlan;
import jatymon.monitor.plans.fields.LoggerFieldPlan;
import jatymon.monitor.plans.fields.TypestateNameFieldPlan;

/**
 * Writes a new log call for invalid ratio logs.
 * @param actionId an ActionId instance. Either new or a name referring to an ActionId instance
 * @param ratio either a variable name or a double
 * @param confidenceInterval either a new Interval instance or a name referring to an Interval instance
 */
public record LogInvalidRatioCodePlan(String actionId, String ratio, String confidenceInterval) implements CodePlan {
    @Override
    public CodeBlock emit(final MonitorFactory.BuildContext ctx) {
        return CodeBlock.builder().add("$L.log($T.buildLog($L, $L, $L, $L))",
                        LoggerFieldPlan.FIELD_NAME,
                        ClassName.get(RatioLogFactory.class),
                        TypestateNameFieldPlan.FIELD_NAME,
                        actionId,
                        ratio,
                        confidenceInterval)
                .build();
    }
}
