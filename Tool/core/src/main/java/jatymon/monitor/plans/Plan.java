package jatymon.monitor.plans;

import jatymon.monitor.MonitorFactory;

/**
 * Interface {@code Plan} is an internal representation of a monitor part: a method, a field, a type, etc...
 *  Each plan has the responsibility of being able of creating their own JavaPoet type to be added to the monitor
 * @param <T> JavaPoet output type
 */
public interface Plan<T> {

    /**
     * Returns the JavaPoet part to be added to the monitor representing the plan
     * @param ctx monitor build context
     * @return JavaPoet port representing the plan
     */
    T emit(final MonitorFactory.BuildContext ctx);
}
