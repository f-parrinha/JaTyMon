package jatymon.monitor.plans.code.logging;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import jatymon.logging.logs.internal.MonitorStartLog;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.CodePlan;
import jatymon.monitor.plans.fields.LoggerFieldPlan;
import jatymon.monitor.plans.fields.ConfidenceLevelFieldPlan;
import jatymon.monitor.plans.fields.TypestateNameFieldPlan;

public record LogMonitorStartCodePlan() implements CodePlan {
    @Override
    public CodeBlock emit(final MonitorFactory.BuildContext ctx) {
        return CodeBlock.builder().add("$L.log(new $T($L, $L))",
                        LoggerFieldPlan.FIELD_NAME,
                        ClassName.get(MonitorStartLog.class),
                        TypestateNameFieldPlan.FIELD_NAME,
                        ConfidenceLevelFieldPlan.FIELD_NAME)
                .build();
    }
}
