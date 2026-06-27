package jatymon;

import jatymon.annotations.Ext;
import jatymon.annotations.Key;
import jatymon.annotations.Typestate;
import jatymon.diagnostics.ErrorDiagnostic;
import jatymon.diagnostics.processor.TypestateAnnotationApplyToClassOnlyDiagnostic;
import jatymon.exceptions.AnnotatedMustBeClassException;
import jatymon.exceptions.FileDoesNotExistException;
import jatymon.logging.JaTyMonMessager;
import jatymon.monitor.MonitorData;
import jatymon.processing.AnnotationsCollector;
import jatymon.processing.ProcessorOptions;
import jatymon.processing.TypestateClass;
import jatymon.resolving.Resolver;
import jatymon.diagnostics.DiagnosticsCollector;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.MonitorWriter;
import jatymon.typestate.TypestateData;
import jatymon.typestate.TypestateProcessor;
import jatymon.diagnostics.processor.NoTypestatesToProcessDiagnostic;
import jatymon.typestate.file.TypestateFile;
import jatymon.typestate.file.TypestateFiler;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.*;

/**
 * Class {@code JaTyMonProcessor} handles the main execution for JaTyMon.
 * @author Francisco Parrinha
 */
@SupportedAnnotationTypes({Typestate.QF_NAME, Ext.QF_NAME, Key.QF_NAME})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedOptions({ProcessorOptions.OUTPUT, ProcessorOptions.GENERATE, ProcessorOptions.SILENT, ProcessorOptions.CONFIDENCE_LEVEL})
public class JaTyMonProcessor extends AbstractProcessor {
    public static String PROCESSOR_NAME = "JaTyMon";

    private final DeployMode deployMode;
    private final TypestateProcessor typestateProcessor;
    private final AnnotationsCollector annotationsCollector;
    private final Map<String, TypestateData> processedTypestates;
    private final Map<String, MonitorData> processedMonitors;

    private Resolver resolver;
    private ProcessorOptions processorOptions;
    private DiagnosticsCollector diagnosticsCollector;

    /**
     * This constructor is not called during normal execution. However, it can be used for testing purposes since it allows
     *  for a custom environment to be provided (and later analyzed).
     * @param deployMode production or testing modes
     */
    public JaTyMonProcessor(final DeployMode deployMode) {
        this.deployMode = deployMode;
        this.typestateProcessor = new TypestateProcessor();
        this.processedTypestates = new HashMap<>();
        this.processedMonitors = new HashMap<>();
        this.annotationsCollector = new AnnotationsCollector();
    }

    /**
     * This is the default constructor, and the one called during normal execution
     */
    public JaTyMonProcessor() {
        this(DeployMode.PRODUCTION);
    }

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.resolver = new Resolver(processingEnv);
        this.processorOptions = new ProcessorOptions(processingEnv);
        this.diagnosticsCollector = new DiagnosticsCollector(processorOptions.getSilent(), PROCESSOR_NAME, new JaTyMonMessager());
    }


    /*
     * Note: the return value by this method DOES NOT mean success/failure. It means weather the annotated
     *  elements are claimed by the processor. In other words, returning false allows other processor to re-process
     *  the elements, true tells the elements have been claimed and now other processors will not execute on these
     *  elements again.
     */
    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        annotationsCollector.collectFromRound(roundEnv);

        // Await for all rounds to finish and log results
        if (!roundEnv.processingOver()) {
            return true;
        }

        // Process typestates
        final Collection<TypestateClass> typestateClasses = annotationsCollector.getTypestateAnnotated().values();
        for (final var annotated : typestateClasses) {
            try {
                final TypestateFile typestateFile = TypestateFiler.getTypestateFile(annotated.typeElement(), processingEnv, deployMode);
                final TypestateData typestate = typestateProcessor.process(typestateFile, annotated, resolver, diagnosticsCollector);
                storeProcessedAnnotated(annotated, typestate);
            } catch (AnnotatedMustBeClassException e) {
                diagnosticsCollector.collect(new TypestateAnnotationApplyToClassOnlyDiagnostic());
            } catch (FileDoesNotExistException | IOException e) {
                diagnosticsCollector.collect(new ErrorDiagnostic(e.getMessage()));
            }
        }

        // Generate monitors
        if (typestateClasses.isEmpty()) {
            diagnosticsCollector.collect(new NoTypestatesToProcessDiagnostic());
        } else if (isSuccess() && processorOptions.getGenerate()) {
            for (final TypestateData current : processedTypestates.values()) {
                final MonitorData monitorData = MonitorFactory.build(resolver, current, processorOptions.getConfidenceLevel());
                MonitorWriter.write(monitorData, processorOptions.getOutput());
                processedMonitors.put(monitorData.name(), monitorData);
            }
        }

        diagnosticsCollector.logCollected();
        return true;
    }

    /**
     * Returns whether the current status of the process is successful
     * @return false
     */
    public boolean isSuccess() {
        return !diagnosticsCollector.containsErrors();
    }

    /**
     * Returns the processor diagnostics collector instance with all found diagnostics
     * @return diagnostics collector instance
     */
    public DiagnosticsCollector getDiagnosticsCollector() {
        return diagnosticsCollector;
    }

    /**
     * Returns an unmodifiable map of processed annotated elements (annotated qualified name -> typestate data)
     * @return unmodifiable map
     */
    public Map<String, TypestateData> getProcessedTypestates() {
        return processedTypestates;
    }

    /**
     * Returns an unmodifiable map of processed monitors (monitor qualified name -> monitor class)
     * @return unmodifiable map
     */
    public Map<String, MonitorData> getProcessedMonitors() {
        return processedMonitors;
    }


    private void storeProcessedAnnotated(final TypestateClass annotated, final TypestateData typestate) {
        if (typestate == null) return;
        processedTypestates.put(annotated.getQualifiedName(), typestate);
    }

    /**
     * Represents the type of deployment execution: testing or production
     */
    public enum DeployMode {
        PRODUCTION,
        TEST
    }
}
