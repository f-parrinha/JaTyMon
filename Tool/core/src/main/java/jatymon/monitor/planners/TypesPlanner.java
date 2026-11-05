package jatymon.monitor.planners;

import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.types.StatesEnumPlan;
import jatymon.monitor.plans.types.TypePlan;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Class {code TypesPlanner} contains methods to create the different types that a monitor may use.
 * @author Francisco Parrinha
 */
public class TypesPlanner implements Planner<TypePlan> {
    private static final TypesPlanner INSTANCE = new TypesPlanner();

    public static TypesPlanner getInstance() {
        return INSTANCE;
    }

    @Override
    public Set<TypePlan> plan(final MonitorFactory.BuildContext ctx) {
        return Set.of(new StatesEnumPlan(ctx.typestateData()
                .getGraph()
                .getStateNames()
                .stream().
                sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new))));
    }
}
