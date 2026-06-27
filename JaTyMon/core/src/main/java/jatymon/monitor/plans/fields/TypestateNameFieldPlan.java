package jatymon.monitor.plans.fields;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import jatymon.monitor.MonitorFactory;

import javax.lang.model.element.Modifier;

public class TypestateNameFieldPlan implements FieldPlan {
    public static final String FIELD_NAME = "TYPESTATE_NAME";

    @Override
    public CodeBlock getInitCode(MonitorFactory.BuildContext ctx) {
        return CodeBlock.builder().build();
    }

    @Override
    public FieldSpec emit(final MonitorFactory.BuildContext ctx) {
        return FieldSpec.builder(ClassName.get(String.class), FIELD_NAME, Modifier.PUBLIC , Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", ctx.typestateData().getName())
                .build();
    }
}
