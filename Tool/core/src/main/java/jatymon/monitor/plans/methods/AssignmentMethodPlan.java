package jatymon.monitor.plans.methods;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import jatymon.monitor.MonitorFactory;
import jatymon.typestate.ast.nodes.internalstate.assignments.TAssignExprNode;
import jatymon.typestate.ast.nodes.internalstate.assignments.TAssignNode;

import javax.lang.model.element.Modifier;


/**
 * Class {@code Assignment} is an internal representation of an assignment in the typestate
 * @author Francisco Parrinha
 */
public record AssignmentMethodPlan(String name, String valName, String arithExpr) implements MethodPlan {

    public static AssignmentMethodPlan fromTAssignNode(final TAssignNode node) {
        final TAssignExprNode assignExprNode = node.getAssignExpr();
        return new AssignmentMethodPlan(node.getName(), assignExprNode.getValName(), assignExprNode.getArithExpr().toCode());
    }

    @Override
    public MethodSpec emit(final MonitorFactory.BuildContext ctx) {
        return MethodSpec.methodBuilder(name)
                .returns(TypeName.VOID)
                .addModifiers(Modifier.PRIVATE)
                .addStatement("$L = $L", valName, arithExpr)
                .build();
    }
}
