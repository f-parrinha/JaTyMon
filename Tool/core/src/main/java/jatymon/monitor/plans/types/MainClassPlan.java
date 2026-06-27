package jatymon.monitor.plans.types;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeSpec;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.fields.FieldPlan;
import jatymon.monitor.plans.methods.MethodPlan;
import jatymon.runtime.Monitor;

import javax.lang.model.element.Modifier;
import java.util.Set;


/**
 * Class {@code MainClassPlan} is an internal representation of a monitor's main class. It contains all required field,
 *  method and type plans, defining the monitor's main class structure. The plan allows an easy creation of the corresponding
 *  JavaPoet instances for generating Java files.
 * @author Francisco Parrinha
 */
public record MainClassPlan(String name,
                            Set<TypePlan> types,
                            Set<FieldPlan> fields,
                            Set<MethodPlan> methods) implements TypePlan {

    @Override
    public TypeSpec emit(MonitorFactory.BuildContext ctx) {
        final TypeSpec.Builder builder = TypeSpec.classBuilder(name)
                .superclass(ctx.getSuperclass())
                .addSuperinterface(ClassName.get(Monitor.class))
                .addModifiers(Modifier.PUBLIC);
        types.forEach(t -> builder.addType(t.emit(ctx)));
        fields.forEach(f -> builder.addField(f.emit(ctx)));
        methods.forEach(m -> builder.addMethod(m.emit(ctx)));
        return builder.build();
    }
}
