package semantic;

import common.Tests;
import common.compilation.CompilationTester;
import jatymon.diagnostics.semantic.decisions.*;
import jatymon.diagnostics.semantic.internalstate.*;
import jatymon.diagnostics.semantic.typestate.*;
import jatymon.diagnostics.semantic.graph.NonProductiveStatesDiagnostic;
import jatymon.diagnostics.semantic.graph.NonReachableStatesDiagnostic;
import jatymon.diagnostics.semantic.resolving.DuplicateImportDiagnostic;
import jatymon.diagnostics.semantic.resolving.ImportCannotBeResolvedDiagnostic;
import jatymon.diagnostics.semantic.resolving.PackageCannotBeResolvedDiagnostic;
import jatymon.diagnostics.semantic.resolving.UnknownTypeDiagnostic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class SemanticPerDiagnosticTests extends CompilationTester {

    @Test
    public void testDuplicateAssignment() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.DUPLICATE_ASSIGNMENT), List.of(DuplicateAssignmentDiagnostic.class));
    }

    @Test
    public void testDuplicateDecisionLabel() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.DUPLICATE_DECISION_LABEL), List.of(DuplicateDecisionLabelDiagnostic.class));
    }

    @Test
    public void testDuplicateTypestate() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.DUPLICATE_TYPESTATE1,
                Tests.PerDiagnostic.DUPLICATE_TYPESTATE2),
                List.of(DuplicateTypestateDiagnostic.class));
    }


    @Test
    public void testDuplicateField() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.DUPLICATE_FIELD), List.of(
                DuplicateFieldDiagnostic.class,
                DuplicateFieldDiagnostic.class,
                DuplicateFieldDiagnostic.class));
    }

    @Test
    public void testDuplicateImport() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.DUPLICATE_IMPORT), List.of(DuplicateImportDiagnostic.class));
    }

    @Test
    public void testDuplicateMethod() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.DUPLICATE_METHOD), List.of(
                DuplicateMethodDiagnostic.class,
                DuplicateMethodDiagnostic.class));
    }

    @Test
    public void testDuplicatePredicate() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.DUPLICATE_PREDICATE), List.of(DuplicatePredicateDiagnostic.class));
    }

    @Test
    public void testDuplicateState() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.DUPLICATE_STATE), List.of(DuplicateStateDiagnostic.class));
    }

    @Test
    public void testEnumerateAllDecisions() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.ENUMERATE_ALL_DECISIONS), List.of(EnumerateAllDecisionsDiagnostic.class));
    }

    @Test
    public void testExpectedDecisionState() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.EXPECTED_DECISION_STATE), List.of(ExpectedDecisionStateDiagnostic.class));
    }

    @Test
    public void testExpectedMethod() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.EXPECTED_METHOD), List.of(ExpectedMethodDiagnostic.class,
                ExpectedMethodDiagnostic.class,
                ExpectedMethodDiagnostic.class));
    }

    @Test
    public void testExtFieldNotFoundInClass() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.EXT_FIELD_NOT_FOUND_IN_CLASS), List.of(ExtFieldNotFoundInClassDiagnostic.class,
                ExtFieldNotFoundInClassDiagnostic.class,
                ExtFieldNotFoundInClassDiagnostic.class));
    }

    @Test
    public void testBadExtFieldType() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.BAD_EXT_FIELD_TYPE), List.of(BadExtFieldTypeDiagnostic.class,
                BadExtFieldTypeDiagnostic.class,
                BadExtFieldTypeDiagnostic.class,
                BadExtFieldTypeDiagnostic.class));
    }

    @Test
    public void testExtFieldsAreImmutable() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.EXT_FIELDS_ARE_IMMUTABLE), List.of(ExtFieldsAreImmutableDiagnostic.class));
    }

    @Test
    public void testImportCannotBeResolved() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.IMPORT_CANNOT_BE_RESOLVED), List.of(ImportCannotBeResolvedDiagnostic.class));
    }

    @Test
    public void testInvalidRatioSum() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.INVALID_RATIO_SUM), List.of(InvalidRatioSumDiagnostic.class));
    }

    @Test
    public void testInvalidRatioValue() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.INVALID_RATIO_VALUE), List.of(
                InvalidRatioValueDiagnostic.class,
                InvalidRatioValueDiagnostic.class));
    }

    @Test
    public void testNonProductiveStates() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.NON_PRODUCTIVE_STATES), List.of(NonProductiveStatesDiagnostic.class));
    }

    @Test
    public void testNonReachableStates() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.NON_REACHABLE_STATES), List.of(NonReachableStatesDiagnostic.class));
    }

    @Test
    public void testPackageCannotBeResolved() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.PACKAGE_CANNOT_BE_RESOLVED), List.of(PackageCannotBeResolvedDiagnostic.class));
    }

    @Test
    public void testUndefinedAssignment() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.UNDEFINED_ASSIGNMENT), List.of(UndefinedAssignmentDiagnostic.class));
    }

    @Test
    public void testUndefinedField() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.UNDEFINED_FIELD), List.of(
                UndefinedFieldDiagnostic.class,
                UndefinedFieldDiagnostic.class));
    }

    @Test
    public void testUndefinedPredicate() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.UNDEFINED_PREDICATE), List.of(UndefinedPredicateDiagnostic.class));
    }

    @Test
    public void testUndefinedState() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.UNDEFINED_STATE), List.of(UndefinedStateDiagnostic.class));
    }

    @Test
    public void testUnexpectedDecisionLabel() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.UNEXPECTED_DECISION_LABEL), List.of(UnexpectedDecisionLabelDiagnostic.class));
    }

    @Test
    public void testUnexpectedDecisionState() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.UNEXPECTED_DECISION_STATE), List.of(
                UnexpectedDecisionStateDiagnostic.class,
                UnexpectedDecisionStateDiagnostic.class));
    }

    @Test
    public void testUnexpectedEmptyProtocol() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.UNEXPECTED_EMPTY_PROTOCOL), List.of(UnexpectedEmptyProtocolDiagnostic.class));
    }

    @Test
    public void testUnexpectedEmptyState() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.UNEXPECTED_EMPTY_STATE), List.of(UnexpectedEmptyStateDiagnostic.class));
    }

    @Test
    public void testUnknownType() throws IOException {
        hasDiagnostic(List.of(Tests.PerDiagnostic.UNKNOWN_TYPE), List.of(UnknownTypeDiagnostic.class));
    }
}
