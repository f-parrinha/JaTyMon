package ast;

import common.Tests;
import jatymon.exceptions.FileDoesNotExistException;
import jatymon.diagnostics.syntax.SyntaxErrorDiagnostic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class TypestateAstBasicTests extends TypestateAstTester {

    @Test
    public void testCircular() throws IOException {
        success(Tests.Basic.CIRCULAR);
    }

    @Test
    public void testCircularWithGetter() throws IOException, FileDoesNotExistException {
        success(Tests.Basic.CIRCULAR_WITH_GETTER);
    }

    @Test
    public void testFaultyFile() throws IOException, FileDoesNotExistException {
        success(Tests.Basic.FAULTY_FILE);
    }

    @Test
    public void testFaultyFile2() throws FileDoesNotExistException, IOException {
        hasDiagnostic(Tests.Basic.FAULTY_FILE2, List.of(SyntaxErrorDiagnostic.class));
    }

    @Test
    public void testFile() throws IOException, FileDoesNotExistException {
        success(Tests.Basic.FILE);
    }

    @Test
    public void testFileInCollection() throws IOException, FileDoesNotExistException {
        success(Tests.Basic.FILE_IN_COLLECTION);
    }

    @Test
    public void testJavaIterator() throws IOException, FileDoesNotExistException {
        success(Tests.Basic.JAVA_ITERATOR);
    }

    @Test
    public void testJavaIteratorWrapper() throws IOException, FileDoesNotExistException {
        success(Tests.Basic.JAVA_ITERATOR_WRAPPER);
    }

    @Test
    public void testJavaIteratorWrapperWithGetter() throws IOException, FileDoesNotExistException {
        success(Tests.Basic.JAVA_ITERATOR_WRAPPER_WITH_GETTER);
    }

    @Test
    public void testLinearityProtocol() throws IOException, FileDoesNotExistException {
        success(Tests.Basic.LINEARITY);
    }
}
