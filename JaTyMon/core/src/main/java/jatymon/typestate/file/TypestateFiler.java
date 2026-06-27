package jatymon.typestate.file;

import jatymon.JaTyMonProcessor;
import jatymon.annotations.Typestate;
import jatymon.common.FileUtils;
import jatymon.exceptions.AnnotatedMustBeClassException;
import jatymon.exceptions.FileDoesNotExistException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TypestateFiler {
    private TypestateFiler() {
    }

    /**
     * Returns a typestate resource (file) from a Typestate annotated element
     * @param annotated     annotated Element
     * @param processingEnv processor processing environment
     */
    public static TypestateFile getTypestateFile(final Element annotated,
                                                 final ProcessingEnvironment processingEnv,
                                                 final JaTyMonProcessor.DeployMode deployMode) throws FileDoesNotExistException, AnnotatedMustBeClassException {
        final String filePath = getAnnotationValue(annotated, processingEnv);

        final String[] parts = filePath.split("/");
        final String file = parts[parts.length - 1];
        final String path = Arrays.stream(parts, 0, parts.length - 1).collect(Collectors.joining("."));

        try {
            final InputStream stream;
            if (deployMode == JaTyMonProcessor.DeployMode.PRODUCTION) {
                final FileObject fileObject = processingEnv.getFiler().getResource(StandardLocation.SOURCE_PATH, path, file);
                stream = fileObject.openInputStream();
            } else if (deployMode == JaTyMonProcessor.DeployMode.TEST) {
                stream = Files.newInputStream(FileUtils.getPath(filePath));
            } else {
                throw new FileDoesNotExistException(filePath);
            }
            return new TypestateFile(stream, Paths.get(filePath));
        } catch (final IOException e) {
            throw new FileDoesNotExistException(filePath);
        }
    }

    /**
     * Returns the path as string to the protocol file stored in the @Typestate annotation, linked to the provided class
     * PRE: the provided class must have a @Typestate annotation
     *
     * @param annotated     class with a typestate annotation
     * @param processingEnv processor processing environment
     */
    public static String getAnnotationValue(final Element annotated, final ProcessingEnvironment processingEnv) throws AnnotatedMustBeClassException {
        if (annotated.getKind() != ElementKind.CLASS || !(annotated instanceof TypeElement)) {
            throw new AnnotatedMustBeClassException(annotated);
        }

        final Typestate typestate = annotated.getAnnotation(Typestate.class);
        if (typestate == null) {
            return "";
        }

        final PackageElement pkg = processingEnv.getElementUtils().getPackageOf(annotated);
        final String packageName = pkg.getQualifiedName().toString().replace(".", "/");
        return String.format("%s%s%s", packageName, packageName.isEmpty() ? "" : "/", normalizeTypestateValue(typestate));
    }

    /**
     * Extracts the value from a Typestate annotation, returning a normalized value (e.g. ensure it ends with the correct
     * file extension, support for './' to specify current directory, etc...)
     *
     * @param typestate typestate annotation
     */
    public static String normalizeTypestateValue(final Typestate typestate) {
        String result = typestate.value();
        int offset = result.startsWith("/") ? 1 : result.startsWith("./") ? 2 : 3;
        if (offset != 3) {
            result = result.substring(offset);
        }
        if (!result.endsWith(FileUtils.PROTOCOL_FILE_EXTENSION)) {
            result = result.concat(FileUtils.PROTOCOL_FILE_EXTENSION);
        }

        return result;
    }
}
