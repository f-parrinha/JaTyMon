package jatymon.typestate.ast.nodes.internalstate;

public interface Expression {

    /**
     * Produces a string parseable by the Java parser.
     * @return readable String (code) by the Java parser
     */
    String toCode();
}
