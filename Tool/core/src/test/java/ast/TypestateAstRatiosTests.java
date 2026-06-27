package ast;

import common.Tests;
import jatymon.diagnostics.syntax.SyntaxErrorDiagnostic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class TypestateAstRatiosTests extends TypestateAstTester {

    @Test
    public void testRatioBadValue1() throws IOException {
        hasDiagnostic(Tests.Ratios.RATIO_BAD_VALUE_1, List.of(SyntaxErrorDiagnostic.class));
    }

    @Test
    public void testRatioBadValue2() throws IOException {
        hasDiagnostic(Tests.Ratios.RATIO_BAD_VALUE_2, List.of(SyntaxErrorDiagnostic.class));
    }

    @Test
    public void testRatioBadValue3() throws IOException {
        hasDiagnostic(Tests.Ratios.RATIO_BAD_VALUE_3, List.of(SyntaxErrorDiagnostic.class));
    }

    @Test
    public void testRatioNegativeAndPositive() throws IOException {
        hasDiagnostic(Tests.Ratios.RATIO_NEGATIVE_AND_POSITIVE, List.of(SyntaxErrorDiagnostic.class));
    }

    @Test
    public void testRatioNegativeAndPositive2() throws IOException {
        hasDiagnostic(Tests.Ratios.RATIO_NEGATIVE_AND_POSITIVE2, List.of(SyntaxErrorDiagnostic.class));
    }

    @Test
    public void testRatioSimple() throws IOException {
        success(Tests.Ratios.RATIO_SIMPLE);
    }

    @Test
    public void testRatioSumLessThan1() throws IOException {
        success(Tests.Ratios.RATIO_SUM_LESS_THAN_1);
    }

    @Test
    public void testRatioSumMoreThan1() throws IOException {
        success(Tests.Ratios.RATIO_SUM_MORE_THAN_1);
    }

    @Test
    public void testRatioNegative() throws IOException {
        success(Tests.Ratios.RATIO_NEGATIVE);
    }

    @Test
    public void testRatioNegative2() throws IOException {
        success(Tests.Ratios.RATIO_NEGATIVE2);
    }

    @Test
    public void testValueAndNone() throws IOException {
        success(Tests.Ratios.RATIO_VALUE_AND_NONE);
    }

    @Test
    public void testFloatingError() throws IOException {
        success(Tests.Ratios.RATIO_FLOATING_ERROR);
    }

    @Test
    public void testExactBoundary() throws IOException {
        success(Tests.Ratios.RATIO_EXACT_BOUNDARY);
    }

    @Test
    public void testZeroSum() throws IOException {
        success(Tests.Ratios.RATIO_ZERO_SUM);
    }

    @Test
    public void testOverFill() throws IOException {
        success(Tests.Ratios.RATIO_OVER_FILL);
    }

    @Test
    public void testPositive() throws IOException {
        success(Tests.Ratios.RATIO_POSITIVE);
    }

    @Test
    public void testPositive2() throws IOException {
        success(Tests.Ratios.RATIO_POSITIVE2);
    }

    @Test
    public void testPositive3() throws IOException {
        success(Tests.Ratios.RATIO_POSITIVE2);
    }
}
