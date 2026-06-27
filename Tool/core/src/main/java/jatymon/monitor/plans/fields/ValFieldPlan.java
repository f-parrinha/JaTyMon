package jatymon.monitor.plans.fields;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;
import jatymon.monitor.MonitorFactory;
import jatymon.typestate.ast.nodes.internalstate.fields.TValNode;

import javax.lang.model.element.Modifier;


/**
 * Class {@code ValFieldPlan} is an internal representation of a {@code val} field in the typestate
 * @author Francisco Parrinha
 */
public record ValFieldPlan(String name, String value) implements FieldPlan{

    /**
     * Creates a new plan from an AST {@code val} field node
     * @param node AST {@code val} field node
     * @return new plan
     */
    public static ValFieldPlan fromTValMode(final TValNode node) {
       return new ValFieldPlan(node.getName(), node.getOperand().getValue().toString());
    }

    @Override
    public CodeBlock getInitCode(final MonitorFactory.BuildContext ctx) {
        return CodeBlock.builder()
                .addStatement("this.$L = $L", name, value)
                .build();
    }

    @Override
    public FieldSpec emit(MonitorFactory.BuildContext ctx) {
        return FieldSpec.builder(TypeName.INT, name, Modifier.PRIVATE).build();
    }
}
