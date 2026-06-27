package jatymon.typestate.ast.nodes.state;

import java.util.Set;

public interface Decision {
    void setExpectedDecisionLabels(Set<String> expectedDecisionLabels);
}
