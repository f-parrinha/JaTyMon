package jatymon.diagnostics.processor;

import jatymon.diagnostics.ErrorDiagnostic;

public class TypestateAnnotationApplyToClassOnlyDiagnostic extends ErrorDiagnostic {
    public static final String MESSAGE = "@Typestate can only be applied to classes";
    public TypestateAnnotationApplyToClassOnlyDiagnostic() {
        super(MESSAGE);
    }
}
