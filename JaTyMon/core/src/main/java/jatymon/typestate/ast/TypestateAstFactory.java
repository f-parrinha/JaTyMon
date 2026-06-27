package jatymon.typestate.ast;

import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.declaration.TTypestateNode;
import jatymon.typestate.parser.TypestateParserErrorListener;
import jatymon.typestate.parser.generated.TypestateLexer;
import jatymon.typestate.parser.generated.TypestateParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class TypestateAstFactory {

    /**
     * The {@code build()} method constructs a raw AST from a file .protocol, given its file path. It returns the root
     *  of the tree. The AST is considered to be raw as it does not perform any semantic checks. For that purpose, use ASTValidator.
     * @param input a CharStream for ANTLR lexer/parser.
     * @return the AST's root node
     * @throws IOException there is no file in the given path
     */
    public static TypestateAst build(final CharStream input) throws IOException {
        final var lexer = new TypestateLexer(input);
        final var tokens = new CommonTokenStream(lexer);
        final var parser = new TypestateParser(tokens);
        final var errorListener = new TypestateParserErrorListener();
        final var visitor = new TypestateAstVisitor();

        // Setup error handlers
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        parser.setErrorHandler(new DefaultErrorStrategy());

        // Create AST and collect diagnostics
        final TTypestateNode root = visitor.visitStart(parser.start());
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        diagnostics.addAll(errorListener.getDiagnostics());
        diagnostics.addAll(visitor.getDiagnostics());
        return new TypestateAst(root, diagnostics);
    }

    public static TypestateAst build(final Path filePath) throws IOException {
        final CharStream input = CharStreams.fromFileName(filePath.toString());
        return build(input);
    }

    public static TypestateAst build(final InputStream inputStream, final Path filePath) throws IOException {
        final CharStream input = CharStreams.fromString(new String(inputStream.readAllBytes()), filePath.getFileName().toString());
        return build(input);
    }
}
