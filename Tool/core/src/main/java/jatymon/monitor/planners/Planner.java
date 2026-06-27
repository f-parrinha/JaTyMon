package jatymon.monitor.planners;

import jatymon.monitor.MonitorFactory;

import java.util.Set;

/**
 * Interface {@code Planner} represents the blueprint for classes responsible for creating sets of plans that define the
 *  structure of a monitor.
 * @param <T>
 */
public interface Planner<T> {

    /**
     * Creates a set with all plans that the planner is responsible for
     * @param ctx monitor building context instance
     * @return set with monitor plans
     */
    Set<T> plan(final MonitorFactory.BuildContext ctx);
}
