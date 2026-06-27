package ast;

import common.Tests;
import jatymon.diagnostics.syntax.SyntaxErrorDiagnostic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class TypestateAstKeyTests extends TypestateAstTester {

    @Test
    public void testKeyBasic1() throws IOException {
        success(Tests.Key.KEY_BASIC_1);
    }

    @Test
    public void testKeyBasic2() throws IOException {
        hasDiagnostic(List.of(Tests.Key.KEY_BASIC_2), List.of(SyntaxErrorDiagnostic.class));
    }

    @Test
    public void testKeyBasic3() throws IOException {
        success(Tests.Key.KEY_BASIC_3);
    }

    @Test
    public void testKeyBasic5() throws IOException {
        success(Tests.Key.KEY_BASIC_5);
    }
}
