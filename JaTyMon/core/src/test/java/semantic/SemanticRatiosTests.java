package semantic;

import common.Tests;
import common.compilation.CompilationTester;
import jatymon.diagnostics.semantic.internalstate.InvalidRatioSumDiagnostic;
import jatymon.diagnostics.semantic.internalstate.InvalidRatioValueDiagnostic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class SemanticRatiosTests extends CompilationTester {

    @Test
    public void testRatioSimple() throws IOException {
        success(Tests.Ratios.RATIO_SIMPLE);
    }

    @Test
    public void testRatioSumLessThan1() throws IOException {
        hasDiagnostic(List.of(Tests.Ratios.RATIO_SUM_LESS_THAN_1), List.of(InvalidRatioSumDiagnostic.class));
    }

    @Test
    public void testRatioSumMoreThan1() throws IOException {
        hasDiagnostic(List.of(Tests.Ratios.RATIO_SUM_MORE_THAN_1), List.of(InvalidRatioSumDiagnostic.class));
    }

    @Test
    public void testRatioNegative() throws IOException {
        hasDiagnostic(List.of(Tests.Ratios.RATIO_NEGATIVE), List.of(InvalidRatioValueDiagnostic.class, InvalidRatioSumDiagnostic.class));
    }

    @Test
    public void testRatioNegative2() throws IOException {
        hasDiagnostic(List.of(Tests.Ratios.RATIO_NEGATIVE2), List.of(InvalidRatioValueDiagnostic.class, InvalidRatioValueDiagnostic.class, InvalidRatioValueDiagnostic.class)
        );
    }

    @Test
    public void testValueAndNone() throws IOException {
        success(Tests.Ratios.RATIO_VALUE_AND_NONE);
    }

    @Test
    public void testFloatingError() throws IOException {
        hasDiagnostic(List.of(Tests.Ratios.RATIO_FLOATING_ERROR), List.of(InvalidRatioSumDiagnostic.class));
    }

    @Test
    public void testExactBoundary() throws IOException {
        success(Tests.Ratios.RATIO_EXACT_BOUNDARY);
    }

    @Test
    public void testZeroSum() throws IOException {
        hasDiagnostic(List.of(Tests.Ratios.RATIO_ZERO_SUM), List.of(InvalidRatioSumDiagnostic.class));
    }

    @Test
    public void testOverFill() throws IOException {
        hasDiagnostic(List.of(Tests.Ratios.RATIO_OVER_FILL), List.of(InvalidRatioSumDiagnostic.class));
    }

    @Test
    public void testPositive() throws IOException {
        success(Tests.Ratios.RATIO_POSITIVE);
    }

    @Test
    public void testPositive2() throws IOException {
        hasDiagnostic(List.of(Tests.Ratios.RATIO_POSITIVE2), List.of(InvalidRatioValueDiagnostic.class, InvalidRatioValueDiagnostic.class));
    }

    @Test
    public void testPositive3() throws IOException {
        success(Tests.Ratios.RATIO_POSITIVE3);
    }
}