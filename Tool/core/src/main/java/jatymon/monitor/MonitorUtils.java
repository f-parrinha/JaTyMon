package jatymon.monitor;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;
import java.util.LinkedList;
import java.util.List;

/**
 * Class {@code MonitorUtils} contains auxiliary methods for certain miscellaneous operations
 * @author Francisco Parrinha
 */
public class MonitorUtils {
    public static final String DEFAULT_ARG_NAME_EXPRESSION = "arg%s";

    public static List<ParameterSpec> getParameterSpecs(final List<TypeMirror> argsTypes) {
        int paramCount = 0;
        final List<ParameterSpec> parameters = new LinkedList<>();
        for (final TypeMirror argType : argsTypes) {
            parameters.add(ParameterSpec.builder(TypeName.get(argType), DEFAULT_ARG_NAME_EXPRESSION.formatted(paramCount++)).build());
        }
        return parameters;
    }

    public static List<String> getParametersNames(final List<ParameterSpec> parameters) {
        return parameters.stream().map(ParameterSpec::name).toList();
    }
}
