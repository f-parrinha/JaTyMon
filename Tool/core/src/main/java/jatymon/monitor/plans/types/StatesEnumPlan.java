package jatymon.monitor.plans.types;

import com.palantir.javapoet.CodeBlock;

import java.util.Set;

/**
 * Class {@code StatesEnumPlan} is an internal representation of the enumerator containing all states of the monitor's
 *  typestate
 * @author Francisco Parrinha
 */
public class StatesEnumPlan extends EnumPlan {
    public static final String ENUM_NAME = "States";

    public StatesEnumPlan(final Set<String> labels) {
        super(ENUM_NAME, labels);
    }
}
