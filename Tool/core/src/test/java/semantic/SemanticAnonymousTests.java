package semantic;

import common.Tests;
import common.compilation.CompilationTester;
import jatymon.diagnostics.semantic.graph.NonProductiveStatesDiagnostic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class SemanticAnonymousTests extends CompilationTester {

    @Test
    public void testAnonymousSimple() throws IOException {
        success(Tests.Anonymous.ANONYMOUS_SIMPLE);
    }

    @Test
    public void testAnonymousComplex() throws IOException  {
        success(Tests.Anonymous.ANONYMOUS_COMPLEX);
    }

    @Test
    public void testAnonymousEnd() throws IOException  {
        success(Tests.Anonymous.ANONYMOUS_END);
    }

    @Test
    public void testAnonymousEnd2() throws IOException  {
        success(Tests.Anonymous.ANONYMOUS_END2);
    }

    @Test
    public void testAnonymousEnd3() throws IOException  {
        success(Tests.Anonymous.ANONYMOUS_END3);
    }

    @Test
    public void testAnonymousNotEnd() throws IOException  {
        success(Tests.Anonymous.ANONYMOUS_NOT_END);
    }
    @Test
    public void testAnonymousNotEnd2() throws IOException  {
        hasDiagnostic(List.of(Tests.Anonymous.ANONYMOUS_NOT_END2), List.of(NonProductiveStatesDiagnostic.class));
    }

    @Test
    public void testAnonymousIdentity() throws IOException  {
        success(Tests.Anonymous.ANONYMOUS_IDENTITY);
    }

    @Test
    public void testAnonymousIdentity2() throws IOException  {
        success(Tests.Anonymous.ANONYMOUS_IDENTITY2);
    }

    @Test
    public void testAnonymousWithDecisionState() throws IOException  {
        hasDiagnostic(List.of(Tests.Anonymous.ANONYMOUS_WITH_DECISION_STATE), List.of(NonProductiveStatesDiagnostic.class));
    }
}
