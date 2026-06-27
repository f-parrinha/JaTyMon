package jatymon.monitor.plans.fields;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.Plan;


/**
 * Interface {@code FieldPlan} is an internal representation of a field to be added to the monitor
 * @author Francisco Parrinha
 */
public interface FieldPlan extends Plan<FieldSpec> {

    /**
     * Returns the code section for initializing the field
     * @param ctx monitor build context
     * @return JavaPoet {@code CodeBlock} for initializing the field
     */
    CodeBlock getInitCode(final MonitorFactory.BuildContext ctx);
}
