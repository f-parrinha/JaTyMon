package jatymon.monitor.planners;

import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.fields.*;
import jatymon.monitor.plans.fields.ConfidenceLevelFieldPlan;
import jatymon.monitor.plans.fields.TypestateNameFieldPlan;
import jatymon.monitor.plans.fields.CurrentStateFieldPlan;
import jatymon.typestate.TypestateData;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class {code FieldsPlanner} contains methods to create the different fields that a monitor may use. Using LinkedHashSets
 *  *  to guarantee uniqueness in the created methods while maintaining order.
 * @author Francisco Parrinha
 */
public class FieldsPlanner implements Planner<FieldPlan> {
    private static final FieldsPlanner INSTANCE = new FieldsPlanner();

    public static FieldsPlanner getInstance() {
        return INSTANCE;
    }

    @Override
    public Set<FieldPlan> plan(final MonitorFactory.BuildContext ctx) {
        final TypestateData typestateData = ctx.typestateData();
        final String firstStateName = typestateData.getGraph().getStart().getName();

        final Set<FieldPlan> res = new LinkedHashSet<>();
        res.add(new TypestateNameFieldPlan());
        res.add(new ConfidenceLevelFieldPlan());
        res.add(new CurrentStateFieldPlan(firstStateName));
        res.add(new ActionsCounterFieldPlan());
        res.add(new LoggerFieldPlan());
        res.addAll(planVals(typestateData));
        return res;
    }

    /**
     * Returns a set of typestate {@code val} field monitor plans.
     * @param typestateData typestate information
     * @return set with {@code val} field plans
     */
    public Set<ValFieldPlan> planVals(final TypestateData typestateData) {
        final Set<ValFieldPlan> res = new LinkedHashSet<>();
        typestateData.getVals().forEach(v -> res.add(ValFieldPlan.fromTValMode(v)));
        return res;
    }
}
