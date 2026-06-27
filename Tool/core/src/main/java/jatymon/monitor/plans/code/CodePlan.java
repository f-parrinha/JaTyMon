package jatymon.monitor.plans.code;

import com.palantir.javapoet.CodeBlock;
import jatymon.monitor.plans.Plan;

/**
 * Interface {@code CodePlan} represents monitor plans representing custom code sections in the monitor.
 * @author Francisco Parrinha
 */
public interface CodePlan extends Plan<CodeBlock> {
}
