package ast;

import common.Tests;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TypestateAstPerDiagnosticTests extends TypestateAstTester {

    @Test
    public void testDuplicateAssignment() throws IOException {
        success(Tests.PerDiagnostic.DUPLICATE_ASSIGNMENT);
    }

    @Test
    public void testDuplicateDecisionLabel() throws IOException {
        success(Tests.PerDiagnostic.DUPLICATE_DECISION_LABEL);
    }

    @Test
    public void testDuplicateField() throws IOException {
        success(Tests.PerDiagnostic.DUPLICATE_FIELD);
    }

    @Test
    public void testDuplicateImport() throws IOException {
        success(Tests.PerDiagnostic.DUPLICATE_IMPORT);
    }

    @Test
    public void testDuplicateMethod() throws IOException {
        success(Tests.PerDiagnostic.DUPLICATE_METHOD);
    }

    @Test
    public void testDuplicatePredicate() throws IOException {
        success(Tests.PerDiagnostic.DUPLICATE_PREDICATE);
    }

    @Test
    public void testDuplicateState() throws IOException {
        success(Tests.PerDiagnostic.DUPLICATE_STATE);
    }

    @Test
    public void testEnumerateAllDecisions() throws IOException {
        success(Tests.PerDiagnostic.ENUMERATE_ALL_DECISIONS);
    }

    @Test
    public void testExpectedDecisionState() throws IOException {
        success(Tests.PerDiagnostic.EXPECTED_DECISION_STATE);
    }

    @Test
    public void testExpectedMethod() throws IOException {
        success(Tests.PerDiagnostic.EXPECTED_METHOD);
    }

    @Test
    public void testExtFieldsAreImmutable() throws IOException {
        success(Tests.PerDiagnostic.EXT_FIELDS_ARE_IMMUTABLE);
    }

    @Test
    public void testImportCannotBeResolved() throws IOException {
        success(Tests.PerDiagnostic.IMPORT_CANNOT_BE_RESOLVED);
    }

    @Test
    public void testInvalidRatioSum() throws IOException {
        success(Tests.PerDiagnostic.INVALID_RATIO_SUM);
    }

    @Test
    public void testInvalidRatioValue() throws IOException {
        success(Tests.PerDiagnostic.INVALID_RATIO_VALUE);
    }

    @Test
    public void testNonReachableStates() throws IOException {
        success(Tests.PerDiagnostic.NON_REACHABLE_STATES);
    }

    @Test
    public void testNonProductiveStates() throws IOException {
        success(Tests.PerDiagnostic.NON_PRODUCTIVE_STATES);
    }

    @Test
    public void testPackageCannotBeResolved() throws IOException {
        success(Tests.PerDiagnostic.PACKAGE_CANNOT_BE_RESOLVED);
    }

    @Test
    public void testUndefinedAssignment() throws IOException {
        success(Tests.PerDiagnostic.UNDEFINED_ASSIGNMENT);
    }

    @Test
    public void testUndefinedField() throws IOException {
        success(Tests.PerDiagnostic.UNDEFINED_FIELD);
    }

    @Test
    public void testUndefinedPredicate() throws IOException {
        success(Tests.PerDiagnostic.UNDEFINED_PREDICATE);
    }

    @Test
    public void testUndefinedState() throws IOException {
        success(Tests.PerDiagnostic.UNDEFINED_STATE);
    }

    @Test
    public void testUnexpectedDecisionLabel() throws IOException {
        success(Tests.PerDiagnostic.UNEXPECTED_DECISION_LABEL);
    }

    @Test
    public void testUnexpectedDecisionState() throws IOException {
        success(Tests.PerDiagnostic.UNEXPECTED_DECISION_STATE);
    }

    @Test
    public void testUnexpectedEmptyProtocol() throws IOException {
        success(Tests.PerDiagnostic.UNEXPECTED_EMPTY_PROTOCOL);
    }

    @Test
    public void testUnexpectedEmptyState() throws IOException {
        success(Tests.PerDiagnostic.UNEXPECTED_EMPTY_STATE);
    }

    @Test
    public void testUnknownType() throws IOException {
        success(Tests.PerDiagnostic.UNKNOWN_TYPE);
    }
}
