package jatymon.monitor;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.JavaFile;
import jatymon.resolving.Resolver;
import jatymon.monitor.planners.FieldsPlanner;
import jatymon.monitor.planners.MethodsPlanner;
import jatymon.monitor.planners.TypesPlanner;
import jatymon.monitor.plans.types.MainClassPlan;
import jatymon.monitor.plans.fields.FieldPlan;
import jatymon.monitor.plans.methods.MethodPlan;
import jatymon.monitor.plans.types.TypePlan;
import jatymon.typestate.TypestateData;

import java.util.*;

/**
 * Class {@code MonitorFactory} allows the creation of monitors given a fully parsed typestate and a confidence level
 *  for ratio monitoring.
 * TODO: Beware of changing the values States enum to short/int. We always need some strings for the logs.......
 *  Is it even worth it to change States enum to integer/short? There is almost no performance gains from this convertion.
 *  Switch statements are compared to a table. Each enum value has an ordinator and the switch statement uses these numbers
 *  to compare the current value. These are eventually cached by the JVM. ValidateRatio() is way more computationally heavy.
 * @author Francisco Parrinha
 */
public class MonitorFactory {
    public static final String MONITOR_NAME_EXPRESSION = "Monitored%s";


    /**
     * Generates a monitor given a fully parsed typestate and a confidence level
     * @param typestateData typestate information
     * @param confidenceLevel confidence level for ratio monitoring
     * @return new monitor instance
     */
    public static MonitorData build(final Resolver resolver, final TypestateData typestateData, final double confidenceLevel) {
        final String superclassName = typestateData.getClassName();
        final String packageName = typestateData.getPackageName();
        final String monitorName = MONITOR_NAME_EXPRESSION.formatted(superclassName);
        final BuildContext ctx = new BuildContext(typestateData, confidenceLevel, monitorName, resolver);

        // Plan monitor structure
        final Set<TypePlan> typePlans = TypesPlanner.getInstance().plan(ctx);
        final Set<FieldPlan> fieldPlans = FieldsPlanner.getInstance().plan(ctx);
        final Set<MethodPlan> methodPlans = MethodsPlanner.getInstance().plan(ctx, fieldPlans);
        final MainClassPlan monPlan = new MainClassPlan(monitorName, typePlans, fieldPlans, methodPlans);
        return new MonitorData(monitorName, monPlan, typestateData, JavaFile.builder(packageName, monPlan.emit(ctx)).build());
    }

    /**
     * Class {@code BuildContext} contains useful information and methods for the monitor building procedure
     *
     * @author Francisco Parrinha
     */
    public record BuildContext(TypestateData typestateData, double confidenceLevel, String monitorName, Resolver resolver) {

        /**
         * Returns a JavaPoet {@code ClassName} instance for a nested type (to the monitor main class) with the given simple name
         *
         * @param simpleName nested type simple name
         * @return JavaPoet {@code ClassName}
         */
        public ClassName getClassName(final String simpleName) {
            return ClassName.get(typestateData.getPackageName(), monitorName, simpleName);
        }

        /**
         * Returns a JavaPoet {@code ClassName} instance for the monitor's main class superclass
         *
         * @return JavaPoet {@code ClassName}
         */
        public ClassName getSuperclass() {
            return ClassName.get(typestateData.getPackageName(), typestateData.getClassName());
        }
    }
}
