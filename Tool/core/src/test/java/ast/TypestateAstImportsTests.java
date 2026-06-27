package ast;

import common.Tests;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TypestateAstImportsTests extends TypestateAstTester {
    @Test
    public void testInvalidImportTest1() throws IOException {
        success(Tests.Imports.INVALID_IMPORT_1);
    }

    @Test
    public void testInvalidImportTest2() throws IOException {
        success(Tests.Imports.INVALID_IMPORT_2);
    }

    @Test
    public void testInvalidImportTest3() throws IOException {
        success(Tests.Imports.INVALID_IMPORT_3);
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
