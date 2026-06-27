package jatymon.monitor.plans.methods.constructors;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.logging.LogMonitorStartCodePlan;
import jatymon.monitor.plans.fields.FieldPlan;
import jatymon.monitor.plans.methods.MethodPlan;

import javax.lang.model.element.Modifier;
import java.util.Set;


/**
 * Class {@code PredicateMethodPlan} is an internal representation of the constructor of the monitor's main class
 * @author Francisco Parrinha
 */
public record SimpleConstructorPlan(Set<FieldPlan> fieldsToInit) implements MethodPlan {

    @Override
    public MethodSpec emit(final MonitorFactory.BuildContext ctx) {
        final CodeBlock.Builder body = CodeBlock.builder();
        fieldsToInit.forEach(f -> body.add(f.getInitCode(ctx)));

        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode(body.build())
                .addStatement(new LogMonitorStartCodePlan().emit(ctx))
                .build();
    }
}
