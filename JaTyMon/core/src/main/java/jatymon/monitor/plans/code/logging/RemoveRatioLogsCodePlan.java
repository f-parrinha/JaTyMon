package jatymon.monitor.plans.code.logging;

import com.palantir.javapoet.CodeBlock;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.CodePlan;
import jatymon.monitor.plans.fields.LoggerFieldPlan;

public record RemoveRatioLogsCodePlan(String actionId) implements CodePlan {
    @Override
    public CodeBlock emit(final MonitorFactory.BuildContext ctx) {
        return CodeBlock.builder().add("$L.removeRatioLogs($L)", LoggerFieldPlan.FIELD_NAME, actionId).build();
    }
}
