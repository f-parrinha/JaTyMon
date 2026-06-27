package semantic;

import common.Tests;
import common.compilation.CompilationTester;
import jatymon.diagnostics.semantic.resolving.ImportCannotBeResolvedDiagnostic;
import jatymon.diagnostics.semantic.resolving.UnknownTypeDiagnostic;
import jatymon.diagnostics.semantic.resolving.UnusedImportDiagnostic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class SemanticImportsTests extends CompilationTester {

    @Test
    public void testInvalidImportTest1() throws IOException {
        hasDiagnostic(List.of(Tests.Imports.INVALID_IMPORT_1), List.of(UnknownTypeDiagnostic.class, UnusedImportDiagnostic.class));
    }

    @Test
    public void testInvalidImportTest2() throws IOException {
        hasDiagnostic(List.of(Tests.Imports.INVALID_IMPORT_2), List.of(UnknownTypeDiagnostic.class));
    }

    @Test
    public void testInvalidImportTest3() throws IOException {
        hasDiagnostic(List.of(Tests.Imports.INVALID_IMPORT_3), List.of(ImportCannotBeResolvedDiagnostic.class));
    }

    @Test
    public void testInvalidImportTest4() throws IOException {
        hasDiagnostic(List.of(Tests.Imports.INVALID_IMPORT_4), List.of(UnknownTypeDiagnostic.class));
    }

    @Test
    public void testUnusedImport() throws IOException {
        hasDiagnostic(List.of(Tests.Imports.UNUSED_IMPORT), List.of(UnusedImportDiagnostic.class));
    }

    @Test
    public void testValidImportTest1() throws IOException {
        success(Tests.Imports.VALID_IMPORT_1);
    }

    @Test
    public void testValidImportTest2() throws IOException {
        success(Tests.Imports.VALID_IMPORT_2);

    }

    @Test
    public void testValidImportTest3() throws IOException {
        success(Tests.Imports.VALID_IMPORT_3);

    }

    @Test
    public void testValidImportTest4() throws IOException {
        success(Tests.Imports.VALID_IMPORT_4);
    }

    @Test
    public void testValidImportTest5() throws IOException {
        success(Tests.Imports.VALID_IMPORT_5);
    }
}
