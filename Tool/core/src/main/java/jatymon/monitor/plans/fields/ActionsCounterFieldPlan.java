package jatymon.monitor.plans.fields;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import jatymon.monitor.MonitorFactory;
import jatymon.actions.ActionsCounter;

import javax.lang.model.element.Modifier;


/**
 * Class {@code ActionsCounterFieldPlan} is an internal representation for the {@code ActionsCounter} in a monitor main class
 * @author Francisco Parrinha
 */
public record ActionsCounterFieldPlan() implements FieldPlan {
    public static final String FIELD_NAME = "actionsCounter";
    private static final ClassName CLASS_NAME = ClassName.get(ActionsCounter.class);

    @Override
    public CodeBlock getInitCode(final MonitorFactory.BuildContext ctx) {
        return CodeBlock.builder()
                .addStatement("this.$L = new $T()", FIELD_NAME, CLASS_NAME)
                .build();
    }
    @Override
    public FieldSpec emit(final MonitorFactory.BuildContext ctx) {
        return FieldSpec.builder(CLASS_NAME, FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL).build();
    }
}
