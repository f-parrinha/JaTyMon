package jatymon.monitor.plans.methods.constructors;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.logging.LogMonitorStartCodePlan;
import jatymon.monitor.plans.fields.ConfidenceLevelFieldPlan;
import jatymon.monitor.plans.fields.FieldPlan;
import jatymon.monitor.plans.fields.LoggerFieldPlan;
import jatymon.monitor.plans.methods.MethodPlan;

import javax.lang.model.element.Modifier;
import java.util.Set;

public record ParameterizedConstructorPlan(Set<FieldPlan> fieldsToInit) implements MethodPlan {
    private static final String LOGGER_PARAM_NAME = "logger";
    private static final String CONFIDENCE_LEVEL_PARAM_NAME = "confidenceLevel";

    @Override
    public MethodSpec emit(final MonitorFactory.BuildContext ctx) {
        final CodeBlock.Builder body = CodeBlock.builder();
        fieldsToInit.forEach(f -> body.add(f instanceof LoggerFieldPlan logger ?
                logger.getInitCode(LOGGER_PARAM_NAME) : f instanceof ConfidenceLevelFieldPlan confidenceLevelFieldPlan ?
                confidenceLevelFieldPlan.getInitCode(CONFIDENCE_LEVEL_PARAM_NAME) :
                f.getInitCode(ctx)));

        final ParameterSpec loggerParam = ParameterSpec.builder(LoggerFieldPlan.CLASS_NAME, LOGGER_PARAM_NAME, Modifier.FINAL).build();
        final ParameterSpec confidenceLevelParam = ParameterSpec.builder(ConfidenceLevelFieldPlan.TYPE_NAME, CONFIDENCE_LEVEL_PARAM_NAME, Modifier.FINAL).build();
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode(body.build())
                .addParameter(loggerParam)
                .addParameter(confidenceLevelParam)
                .addStatement(new LogMonitorStartCodePlan().emit(ctx))
                .build();
    }
}
