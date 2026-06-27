package jatymon.typestate.parser;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.diagnostics.syntax.SyntaxErrorDiagnostic;
import org.antlr.v4.runtime.*;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TypestateParserErrorListener extends BaseErrorListener {
    private final List<AbstractDiagnostic> diagnostics;


    public TypestateParserErrorListener() {
        this.diagnostics = new LinkedList<>();
    }

    @Override
    public void syntaxError(final Recognizer<?, ?> recognizer,
                            final Object offendingSymbol,
                            final int line,
                            final int column,
                            final String msg,
                            final RecognitionException e) {
        String fileName = recognizer.getInputStream().getSourceName();
        if (!fileName.isEmpty()) {
            fileName = Paths.get(fileName).getFileName().toString();
        }

        diagnostics.add(new SyntaxErrorDiagnostic(fileName, new TokenPosition(fileName, 0, line, column), msg));
    }

    public List<AbstractDiagnostic> getDiagnostics() {
        return Collections.unmodifiableList(diagnostics);
    }
}
