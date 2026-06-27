package jatymon.monitor.plans.fields;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.types.StatesEnumPlan;
import jatymon.typestate.ast.nodes.key.TKeyNode;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.Objects;


/**
 * Class {@code CurrentStateFieldPlan} is an internal representation for the current state according to the monitor's typestate
 *
 * @author Francisco Parrinha
 */
public record CurrentStateFieldPlan(String firstStateName) implements FieldPlan {
    public static final String FIELD_NAME = "currentState";


    public static CodeBlock getGetterCode(final TKeyNode key, final CodeBlock firstState) {
        return key == null ? CodeBlock.of("$L", FIELD_NAME) : CodeBlock.of("$L.getOrDefault($L, $L)", FIELD_NAME, key.getName(), firstState);
    }

    public static CodeBlock getAssignCode(final TKeyNode key, final CodeBlock val) {
        return key == null ? CodeBlock.of("$L = $L", FIELD_NAME, val) : CodeBlock.of("$L.put($L, $L)", FIELD_NAME, key.getName(), val);
    }

    @Override
    public CodeBlock getInitCode(final MonitorFactory.BuildContext ctx) {
        final TKeyNode key = ctx.typestateData().getKey();
        return key == null ? CodeBlock.builder()
                .addStatement("this.$L = $T.$L", FIELD_NAME, ctx.getClassName(StatesEnumPlan.ENUM_NAME), firstStateName)
                .build() : CodeBlock.builder()
                .addStatement("this.$L = new HashMap<>()", CurrentStateFieldPlan.FIELD_NAME)
                .build();
    }

    @Override
    public FieldSpec emit(final MonitorFactory.BuildContext ctx) {
        final TKeyNode key = ctx.typestateData().getKey();
        final ClassName statesEnum = ctx.getClassName(StatesEnumPlan.ENUM_NAME);
        if (key == null) {
            return FieldSpec.builder(statesEnum, FIELD_NAME, Modifier.PRIVATE).build();
        }

        final ParameterizedTypeName mapType = ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(String.class), statesEnum);
        return FieldSpec.builder(mapType, FIELD_NAME, Modifier.PRIVATE).build();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CurrentStateFieldPlan(String stateName)
                && firstStateName.equals(stateName);
    }

    @Override
    public String toString() {
        return "CurrentStateFieldPlan{firstStateName: %s}".formatted(firstStateName);
    }
}
