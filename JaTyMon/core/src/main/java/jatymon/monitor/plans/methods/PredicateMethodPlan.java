package jatymon.monitor.plans.methods;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import jatymon.monitor.MonitorFactory;
import jatymon.typestate.ast.nodes.internalstate.predicates.TPredNode;

import javax.lang.model.element.Modifier;


/**
 * Class {@code PredicateMethodPlan} is an internal representation of a predicate in the typestate
 * @author Francisco Parrinha
 */
public record PredicateMethodPlan(String name, String boolExpr) implements MethodPlan {

    public static PredicateMethodPlan fromTPredNode(final TPredNode node) {
        return new PredicateMethodPlan(node.getName(),  node.getBoolExpr().toCode());
    }

    @Override
    public MethodSpec emit(MonitorFactory.BuildContext ctx) {
        return MethodSpec.methodBuilder(name)
                .returns(TypeName.BOOLEAN)
                .addModifiers(Modifier.PRIVATE)
                .addStatement("return $L", boolExpr)
                .build();
    }
}
