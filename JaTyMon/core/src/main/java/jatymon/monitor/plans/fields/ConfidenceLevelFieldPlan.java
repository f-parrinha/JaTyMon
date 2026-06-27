package jatymon.monitor.plans.fields;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;
import jatymon.monitor.MonitorFactory;

import javax.lang.model.element.Modifier;

public record ConfidenceLevelFieldPlan() implements FieldPlan {
    public static final String FIELD_NAME = "confidenceLevel";
    public static TypeName TYPE_NAME = TypeName.DOUBLE;

    @Override
    public CodeBlock getInitCode(final MonitorFactory.BuildContext ctx) {
        return CodeBlock.builder()
                .addStatement("this.$L = $L", FIELD_NAME, ctx.confidenceLevel())
                .build();
    }

    public CodeBlock getInitCode(final String confidenceLevelParam) {
        return CodeBlock.builder()
                .addStatement("this.$L = $L", FIELD_NAME, confidenceLevelParam)
                .build();
    }
    @Override
    public FieldSpec emit(final MonitorFactory.BuildContext ctx) {
        return FieldSpec.builder(TYPE_NAME, FIELD_NAME, Modifier.FINAL, Modifier.PRIVATE).build();
    }
}
