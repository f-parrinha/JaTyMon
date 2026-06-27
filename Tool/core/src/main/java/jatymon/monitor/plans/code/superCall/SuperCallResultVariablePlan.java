package jatymon.monitor.plans.code.superCall;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import jatymon.common.globals.JavaTypes;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.CodePlan;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public record SuperCallResultVariablePlan(TypeMirror returnType) implements CodePlan {
    public static final String VAR_NAME = "superResult";

    @Override
    public CodeBlock emit(final MonitorFactory.BuildContext ctx) {
        return returnType.getKind().equals(TypeKind.VOID) ? CodeBlock.builder().build() :
                CodeBlock.builder().addStatement("$T $L = $L", ClassName.get(returnType), VAR_NAME, getStartValue(returnType)).build();
    }

    public CodeBlock getReturnStatement() {
        return returnType.getKind().equals(TypeKind.VOID) ? CodeBlock.builder().build() :
                CodeBlock.builder().addStatement("return $L", VAR_NAME).build();
    }

    private static String getStartValue(final TypeMirror typeMirror) {
        return switch (typeMirror.getKind()) {
            case BOOLEAN -> "false";
            case LONG -> "0L";
            case BYTE, SHORT, INT, CHAR, DOUBLE -> "0";
            case FLOAT -> "0f";
            case ARRAY, DECLARED, TYPEVAR, WILDCARD, UNION, INTERSECTION -> null;
            case VOID, NONE, NULL, ERROR, OTHER, MODULE, PACKAGE, EXECUTABLE ->
                    throw new IllegalArgumentException("Unsupported type kind: " + typeMirror.getKind());
        };
    }
}
