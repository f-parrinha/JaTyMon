package jatymon.processing;

import jatymon.monitor.MonitorData;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class {@code ProcessorArgs} represents data objects containing flags for the different possible processor arguments
 * @author Francisco Parrinha
 */
public final class ProcessorOptions {
    public static final String OUTPUT = "output";
    public static final String GENERATE = "generate";
    public static final String SILENT = "silent";
    public static final String CONFIDENCE_LEVEL = "confidenceLevel";

    private final Map<String, Object> args;

    public ProcessorOptions() {
        args = new HashMap<>();
        args.put(OUTPUT, null);
        args.put(GENERATE, false);
        args.put(SILENT, true);
        args.put(CONFIDENCE_LEVEL, MonitorData.DEFAULT_CONFIDENCE_LEVEL);
    }

    public ProcessorOptions(final ProcessingEnvironment processingEnv) {
        args = new HashMap<>();
        args.put(OUTPUT, processingEnv.getOptions().get(OUTPUT));
        args.put(GENERATE, extractGenerateMode(processingEnv));
        args.put(SILENT, extractSilentMode(processingEnv));
        args.put(CONFIDENCE_LEVEL, extractConfidenceLevel(processingEnv));
    }

    public String getOutput() {
        return args.get(OUTPUT).toString();
    }

    public boolean getGenerate() {
        return (boolean) args.get(GENERATE);
    }
    public boolean getSilent() {
        return (boolean) args.get(SILENT);
    }
    public double getConfidenceLevel() {
        return (double) args.get(CONFIDENCE_LEVEL);
    }

    public ProcessorOptions withConfidenceLevel(final double confidenceLevel) {
        args.put(CONFIDENCE_LEVEL, confidenceLevel);
        return this;
    }

    public ProcessorOptions withOutput(final String val) {
        args.put(OUTPUT, val);
        return this;
    }

    public ProcessorOptions withSilent(final boolean val) {
        args.put(SILENT, val);
        return this;
    }

    public ProcessorOptions withGenerate(final boolean val) {
        args.put(GENERATE, val);
        return this;
    }

    public Map<String, Object> asMap() {
        return Collections.unmodifiableMap(args);
    }

    private boolean extractGenerateMode(final ProcessingEnvironment processingEnv) {
        final String generateMode = processingEnv.getOptions().get(ProcessorOptions.GENERATE);
        if (generateMode == null || generateMode.isEmpty()) {
            return true;
        } else {
            return Boolean.parseBoolean(generateMode);
        }
    }

    private boolean extractSilentMode(final ProcessingEnvironment processingEnv) {
        final String silentMode = processingEnv.getOptions().get(ProcessorOptions.SILENT);
        if (silentMode == null || silentMode.isEmpty()) {
            return false;
        } else {
            return Boolean.parseBoolean(silentMode);
        }
    }

    private double extractConfidenceLevel(final ProcessingEnvironment processingEnv) {
        final String confidenceLevel = processingEnv.getOptions().get(ProcessorOptions.CONFIDENCE_LEVEL);
        return confidenceLevel == null || confidenceLevel.trim().isEmpty()
                ? MonitorData.DEFAULT_CONFIDENCE_LEVEL
                : Double.parseDouble(confidenceLevel);
    }
}
