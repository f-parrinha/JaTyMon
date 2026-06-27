package common.compilation;

import com.google.testing.compile.Compilation;
import jatymon.JaTyMonProcessor;
import jatymon.logging.JaTyMonMessager;

import javax.tools.Diagnostic;

/**
 * Record {@code CompilationResult} contains all required information regarding the result of a test compilation
 * @author Francisco Parrinha
 */
public record CompilationResult(Compilation compilation, JaTyMonProcessor processor) {
    public CompilationResult(final Compilation compilation, final JaTyMonProcessor processor) {
        this.compilation = compilation;
        this.processor = processor;
        printCompilationLog();
    }

    public boolean isSuccess() {
        return compilation.status().equals(Compilation.Status.SUCCESS);
    }

    private void printCompilationLog() {
        if (isSuccess()) return;

        final JaTyMonMessager messager = new JaTyMonMessager();
        compilation.errors().forEach(d -> messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(CompilationTester.COMPILATION_ERR_MSG, d.getSource().getName(), d.getLineNumber(), d.getColumnNumber(), d.getMessage(null))));

    }
}
