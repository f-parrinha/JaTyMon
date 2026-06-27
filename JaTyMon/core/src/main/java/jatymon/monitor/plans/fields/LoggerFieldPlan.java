package jatymon.monitor.plans.fields;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import jatymon.logging.JaTyMonLogger;
import jatymon.monitor.MonitorFactory;

import javax.lang.model.element.Modifier;


/**
 * Class {@code LoggerFieldPlan} is an internal representation for the {@code Logger} field in a monitor main class
 * @author Francisco Parrinha
 */
public record LoggerFieldPlan() implements FieldPlan {
    public static final String FIELD_NAME = "logger";
    public static final ClassName CLASS_NAME = ClassName.get(JaTyMonLogger.class);

    @Override
    public CodeBlock getInitCode(final MonitorFactory.BuildContext ctx) {
        return CodeBlock.builder()
                .addStatement("this.$L = new $T()", FIELD_NAME, CLASS_NAME)
                .build();
    }

    public CodeBlock getInitCode(final String constructorLoggerParam) {
        return CodeBlock.builder()
                .addStatement("this.$L = $L", FIELD_NAME, constructorLoggerParam)
                .build();
    }

    @Override
    public FieldSpec emit(final MonitorFactory.BuildContext ctx) {
        return FieldSpec.builder(CLASS_NAME, FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL).build();
    }
}
