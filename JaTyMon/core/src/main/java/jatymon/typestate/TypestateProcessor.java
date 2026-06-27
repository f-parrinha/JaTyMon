package jatymon.typestate;

import jatymon.diagnostics.DiagnosticsCollector;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.processing.TypestateClass;
import jatymon.typestate.ast.TypestateAst;
import jatymon.typestate.ast.TypestateAstFactory;
import jatymon.resolving.Resolver;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.resolving.UnusedImportDiagnostic;
import jatymon.diagnostics.semantic.typestate.DuplicateTypestateDiagnostic;
import jatymon.diagnostics.semantic.typestate.UnexpectedEmptyProtocolDiagnostic;
import jatymon.typestate.file.TypestateFile;
import jatymon.typestate.graph.Graph;
import jatymon.typestate.graph.GraphFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;

/**
 * Class {@code TypestateProcessor} processes files with the {@code @Typestate} annotation. It finds the correct file according
 *  to the path in the annotation, builds an AST, semantically validates the protocol, and then computes a graph representing
 *  the typestate.
 */
public class TypestateProcessor {
    private final Map<String, Path> processedTypestates;

    public TypestateProcessor() {
        this.processedTypestates = new HashMap<>();
    }

    /**
     * Completely processes a typestate in a {@code @Typestate} annotated class
     * @param annotated annotated TypeElement (class)
     * @param resolver resolver instance
     * @return {@code TypestateData} instance with processed information
     */
    public TypestateData process(final TypestateFile file,
                                 final TypestateClass annotated,
                                 final Resolver resolver,
                                 final DiagnosticsCollector diagnosticsCollector) throws IOException {
        final Path filePath = file.filePath();
        final String defaultPkgName = resolver.getProcessingEnv().getElementUtils().getPackageOf(annotated.typeElement()).getQualifiedName().toString();

        // Process AST
        final TypestateAst ast = processAst(file.stream(), file.filePath(), diagnosticsCollector);
        if (ast == null) {
            return null;
        }

        // Process semantics
        final TypestateAstValidator.Context ctx = processSemantics(ast, annotated, defaultPkgName, resolver, diagnosticsCollector);
        if (ctx == null) {
            return null;
        }

        // Process unused imports
        final String typestateName = ctx.getTypestateName();
        diagnosticsCollector.collectAll(processUnusedImports(ctx, resolver, typestateName));

        // Check for duplicate typestates
        final String typestateQfName = ctx.getTypestateQfName();
        if (processedTypestates.containsKey(typestateQfName) && !processedTypestates.get(typestateQfName).equals(filePath)) {
            diagnosticsCollector.collect(new DuplicateTypestateDiagnostic(typestateQfName, filePath.toString()));
            return null;
        }

        // Create graph and validate protocol correctness
        final Graph graph = GraphFactory.fromAst(typestateName, ctx.getFirstState(), ctx.getStates());
        diagnosticsCollector.collectAll(graph.validate());

        // Store and return result
        processedTypestates.put(typestateQfName, filePath);
        return new TypestateData(graph, ctx);
    }

    /**
     * Processes the AST of a typestate in a {@code fileName}. Processing actions collect diagnostics.
     * @param in input stream of the typestate file
     * @param filePath file path
     * @param diagnosticsCollector diagnostics collector instance
     * @return typestate AST
     * @throws IOException file does not exist
     */
    public TypestateAst processAst(final InputStream in,
                                    final Path filePath,
                                    final DiagnosticsCollector diagnosticsCollector) throws IOException {
        final TypestateAst ast = TypestateAstFactory.build(in, filePath);
        final int oldErrorLogsCount = diagnosticsCollector.errorsSize();
        diagnosticsCollector.collectAll(ast.diagnostics());
        return diagnosticsCollector.errorsSize() == oldErrorLogsCount ? ast : null;
    }

    /**
     * Processes semantic validation using the typestate AST. Processing actions collect diagnostics.
     * @param ast typestate AST
     * @param typestateClass annotated type element (class)
     * @param defaultPkgName default package name
     * @param resolver resolver instance
     * @param diagnosticsCollector diagnostics collector instance
     * @return ast semantic validation context instance storing validated information
     */
    public TypestateAstValidator.Context processSemantics(final TypestateAst ast,
                                                           final TypestateClass typestateClass,
                                                           final String defaultPkgName,
                                                           final Resolver resolver,
                                                           final DiagnosticsCollector diagnosticsCollector) {
        final String classQfName = typestateClass.getQualifiedName();
        final String className = typestateClass.getSimpleName();
        final String typestateName = ast.root().getDeclaration().getName();
        final TypestateAstValidator.Context ctx = new TypestateAstValidator.Context.Builder()
                .withTypestateName(typestateName)
                .withDefaultPkgQfName(defaultPkgName)
                .withClassQfName(classQfName)
                .withClassName(className)
                .withTypestateClass(typestateClass)
                .build();

        final List<AbstractDiagnostic> semanticDiagnostics = TypestateAstValidator.validate(ast, resolver, ctx);
        diagnosticsCollector.collectAll(semanticDiagnostics);
        return diagnosticsCollector.containsDiagnostic(UnexpectedEmptyProtocolDiagnostic.class) ? null : ctx;
    }

    /**
     * Processes unused imports in a typestate. Processing actions collect diagnostics.
     * @param ctx semantic validation context instance storing validated information
     * @param resolver resolver instance
     * @param typestateName typestate name
     * @return list with unused import diagnostics
     */
    public List<AbstractDiagnostic> processUnusedImports(final TypestateAstValidator.Context ctx,
                                      final Resolver resolver,
                                      final String typestateName) {
        final Set<String> usedImports = resolver.getUsedImports(ctx.getTypestateQfName());
        return ctx.getImports().stream()
                .filter(i -> !usedImports.contains(i))
                .<AbstractDiagnostic>map(i -> new UnusedImportDiagnostic(i, typestateName))
                .toList();
    }
}

