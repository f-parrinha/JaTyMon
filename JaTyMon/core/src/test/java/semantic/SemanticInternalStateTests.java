package semantic;

import common.Tests;
import common.compilation.CompilationTester;
import jatymon.diagnostics.semantic.internalstate.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class SemanticInternalStateTests extends CompilationTester {

    @Test
    public void testBadInternalState() throws IOException {
        hasDiagnostic(List.of(Tests.InternalState.BAD_INTERNAL_STATE), List.of(
                DuplicateFieldDiagnostic.class,
                DuplicateFieldDiagnostic.class,
                DuplicateAssignmentDiagnostic.class,
                DuplicatePredicateDiagnostic.class,
                UndefinedAssignmentDiagnostic.class,
                UndefinedAssignmentDiagnostic.class,
                UndefinedPredicateDiagnostic.class,
                UndefinedFieldDiagnostic.class,
                ExtFieldsAreImmutableDiagnostic.class));
    }

    @Test
    public void testABPDual() throws IOException {
        success(Tests.InternalState.ABP_DUAL, Tests.InternalState.ABP);
    }

    @Test
    public void testABPSender() throws IOException {
        success(Tests.InternalState.ABP_SENDER, Tests.InternalState.ABP_DUAL, Tests.InternalState.ABP);
    }

    @Test
    public void testABPReceiver() throws IOException {
        success(Tests.InternalState.ABP_RECEIVER, Tests.InternalState.ABP_DUAL, Tests.InternalState.ABP);
    }

    @Test
    public void testBVPLeader() throws IOException {
        success(Tests.InternalState.BVP_LEADER, Tests.InternalState.BVP);
    }

    @Test
    public void testBVPPeer() throws IOException {
        success(Tests.InternalState.BVP_PEER, Tests.InternalState.BVP);
    }

}
