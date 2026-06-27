package semantic;

import common.Tests;
import common.compilation.CompilationTester;
import jatymon.diagnostics.semantic.resolving.UnknownTypeDiagnostic;
import jatymon.diagnostics.semantic.typestate.ExpectedMethodDiagnostic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class SemanticGenericsTests extends CompilationTester {

    @Test
    public void testValidGeneric1() throws IOException {
        success(Tests.Generics.VALID_GENERIC_1);
    }

    @Test
    public void testValidGeneric2() throws IOException {
        success(Tests.Generics.VALID_GENERIC_2);
    }

    @Test
    public void testValidGeneric3() throws IOException {
        success(Tests.Generics.VALID_GENERIC_3);
    }

    @Test
    public void testValidGeneric4() throws IOException {
        success(Tests.Generics.VALID_GENERIC_4);
    }

    @Test
    public void testValidGeneric5() throws IOException {
        success(Tests.Generics.VALID_GENERIC_5);
    }

    @Test
    public void testValidGeneric6() throws IOException {
        success(Tests.Generics.VALID_GENERIC_6);
    }

    @Test
    public void testInvalidGeneric1() throws IOException {
        hasDiagnostic(List.of(Tests.Generics.INVALID_GENERIC_1), List.of(ExpectedMethodDiagnostic.class));
    }

    @Test
    public void testInvalidGeneric2() throws IOException {
        hasDiagnostic(List.of(Tests.Generics.INVALID_GENERIC_2), List.of(UnknownTypeDiagnostic.class));
    }

    @Test
    public void testInvalidGeneric3() throws IOException {
        hasDiagnostic(List.of(Tests.Generics.INVALID_GENERIC_3), List.of(UnknownTypeDiagnostic.class));
    }

    @Test
    public void testInvalidGeneric4() throws IOException {
        hasDiagnostic(List.of(Tests.Generics.INVALID_GENERIC_4), List.of(UnknownTypeDiagnostic.class));
    }

    @Test
    public void testInvalidGeneric5() throws IOException {
        hasDiagnostic(List.of(Tests.Generics.INVALID_GENERIC_5), List.of(UnknownTypeDiagnostic.class));
    }

    @Test
    public void testInvalidGeneric6() throws IOException {
        hasDiagnostic(List.of(Tests.Generics.INVALID_GENERIC_6), List.of(UnknownTypeDiagnostic.class));
    }
}
