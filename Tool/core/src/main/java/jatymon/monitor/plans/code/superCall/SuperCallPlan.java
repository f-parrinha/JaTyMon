package jatymon.monitor.plans.code.superCall;

import com.palantir.javapoet.CodeBlock;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.CodePlan;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * Class {@code SuperCallPlan} represents the structure in the monitor responsible for calling {@code super.<method_name>(args...)}
 * and storing the value to be later used (if it is not void)
 */
public record SuperCallPlan(String methodName, TypeMirror returnType, List<String> args) implements CodePlan {

    @Override
    public CodeBlock emit(final MonitorFactory.BuildContext ctx) {
        final CodeBlock.Builder code = CodeBlock.builder();
        if (!returnType.getKind().equals(TypeKind.VOID)) {
            code.add("$L = ", SuperCallResultVariablePlan.VAR_NAME).build();
        }

        code.add("super.$L($L);\n", methodName, String.join(", ", args));
        return code.build();
    }

    public SuperCallResultVariablePlan getResultVariable() {
        return new SuperCallResultVariablePlan(returnType);
    }

    @Override
    public String toString() {
        return "SuperCallPlan[" +
                "actionName=" + methodName + ", " +
                "returnType=" + returnType + ", " +
                "args=" + args + ']';
    }
}
