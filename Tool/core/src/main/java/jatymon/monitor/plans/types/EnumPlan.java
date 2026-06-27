package jatymon.monitor.plans.types;

import com.palantir.javapoet.TypeSpec;
import jatymon.monitor.MonitorFactory;

import javax.lang.model.element.Modifier;
import java.util.Set;

/**
 * Class {@code EnumPlan} is an internal representation of an enumerator type
 * @author Francisco Parrinha
 */
public abstract class EnumPlan implements TypePlan {
    private final String name;
    private final Set<String> labels;

    public EnumPlan(final String name, final Set<String> labels) {
        this.name = name;
        this.labels = labels;
    }


    @Override
    public TypeSpec emit(MonitorFactory.BuildContext ctx) {
        final TypeSpec.Builder res = TypeSpec.enumBuilder(name).addModifiers(Modifier.PRIVATE);
        labels.forEach(res::addEnumConstant);
        return res.build();
    }
}
