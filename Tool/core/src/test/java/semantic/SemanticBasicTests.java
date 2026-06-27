package semantic;

import common.Tests;
import common.compilation.CompilationTester;
import jatymon.diagnostics.semantic.decisions.EnumerateAllDecisionsDiagnostic;
import jatymon.diagnostics.semantic.decisions.ExpectedDecisionStateDiagnostic;
import jatymon.diagnostics.semantic.decisions.UnexpectedDecisionLabelDiagnostic;
import jatymon.diagnostics.semantic.decisions.UnexpectedDecisionStateDiagnostic;
import jatymon.diagnostics.semantic.graph.NonProductiveStatesDiagnostic;
import jatymon.diagnostics.semantic.resolving.UnknownTypeDiagnostic;
import jatymon.diagnostics.semantic.typestate.DuplicateMethodDiagnostic;
import jatymon.diagnostics.semantic.typestate.UnexpectedEmptyStateDiagnostic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;


/**
 * Tests ASTValidator for all protocol files in the basic folder (from original JaTyC)
 */
public class SemanticBasicTests extends CompilationTester {

    @Test
    public void testCircular() throws IOException {
        success(Tests.Basic.CIRCULAR);
    }

    @Test
    public void testCircularWithGetter() throws IOException {
        success(Tests.Basic.CIRCULAR_WITH_GETTER);
    }

    @Test
    public void testFaultyFile() throws IOException {
        hasDiagnostic(List.of(Tests.Basic.FAULTY_FILE,
                Tests.Basic.FILE_STATUS), List.of(
                        EnumerateAllDecisionsDiagnostic.class,
                        UnexpectedDecisionLabelDiagnostic.class,
                        EnumerateAllDecisionsDiagnostic.class,
                        ExpectedDecisionStateDiagnostic.class,
                        UnexpectedEmptyStateDiagnostic.class,
                        UnexpectedDecisionStateDiagnostic.class,
                        UnknownTypeDiagnostic.class,
                        DuplicateMethodDiagnostic.class,
                        NonProductiveStatesDiagnostic.class)
        );
    }

    @Test
    public void testFile() throws IOException {
        success(Tests.Basic.FILE, Tests.Basic.FILE_STATUS);
    }

    @Test
    public void testFileInCollection() throws IOException {
        success(Tests.Basic.FILE_IN_COLLECTION, Tests.Basic.FILE_STATUS, Tests.Basic.FILE_STATE);
    }

    @Test
    public void testJavaIterator() throws IOException {
        success(Tests.Basic.JAVA_ITERATOR);
    }

    @Test
    public void testJavaIteratorWrapper() throws IOException {
        success(Tests.Basic.JAVA_ITERATOR_WRAPPER);
    }

    @Test
    public void testJavaIteratorWrapperWithGetter() throws IOException {
        success(Tests.Basic.JAVA_ITERATOR_WRAPPER_WITH_GETTER);
    }

    @Test
    public void testLinearity() throws IOException {
        success(Tests.Basic.LINEARITY);
    }

    @Test
    public void testReachEnd1() throws IOException {
        hasDiagnostic(List.of(Tests.Basic.REACH_END1), List.of(NonProductiveStatesDiagnostic.class));
    }

    @Test
    public void testReachEnd2() throws IOException {
        success(Tests.Basic.REACH_END2);
    }

    @Test
    public void testReachEnd3() throws IOException {
        hasDiagnostic(List.of(Tests.Basic.REACH_END3), List.of(NonProductiveStatesDiagnostic.class));
    }

    @Test
    public void testReachEnd4() throws IOException {
        success(Tests.Basic.REACH_END4);
    }
}
