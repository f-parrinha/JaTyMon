package jatymon.monitor.plans.code.logging;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.CodePlan;
import jatymon.monitor.plans.fields.LoggerFieldPlan;
import jatymon.monitor.plans.fields.TypestateNameFieldPlan;

/**
 * Writes a new log call for internal exception logs.
 * @param exceptionCode exception code. Passed as string but interpreted as literal.
 */
public record LogInternalExceptionCodePlan(String exceptionCode) implements CodePlan {

    @Override
    public CodeBlock emit(final MonitorFactory.BuildContext ctx) {
        return CodeBlock.builder().add("$L.log(new $T($L, $L))",
                        LoggerFieldPlan.FIELD_NAME,
                        ClassName.get(InternalExceptionLog.class),
                        TypestateNameFieldPlan.FIELD_NAME,
                        exceptionCode)
                .build();
    }
}
