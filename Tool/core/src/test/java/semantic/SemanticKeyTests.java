package semantic;

import common.Tests;
import common.compilation.CompilationTester;
import jatymon.diagnostics.semantic.internalstate.ExtFieldNotFoundInClassDiagnostic;
import jatymon.diagnostics.semantic.typestate.AmbiguousKeyDiagnostic;
import jatymon.diagnostics.semantic.typestate.KeyNotFoundInClassDiagnostic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class SemanticKeyTests extends CompilationTester {

    @Test
    public void testKeyBasic1() throws IOException {
        success(Tests.Key.KEY_BASIC_1);
    }

    @Test
    public void testKeyBasic3() throws IOException {
        hasDiagnostic(List.of(Tests.Key.KEY_BASIC_3), List.of(KeyNotFoundInClassDiagnostic.class));
    }

    @Test
    public void testKeyBasic4() throws IOException {
        hasDiagnostic(List.of(Tests.Key.KEY_BASIC_4), List.of(KeyNotFoundInClassDiagnostic.class));
    }

    @Test
    public void testKeyBasic5() throws IOException {
        hasDiagnostic(List.of(Tests.Key.KEY_BASIC_5), List.of(KeyNotFoundInClassDiagnostic.class,
                AmbiguousKeyDiagnostic.class,
                ExtFieldNotFoundInClassDiagnostic.class));
    }
}
