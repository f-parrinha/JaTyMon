package jatymon.processing;

import jatymon.annotations.Ext;
import jatymon.annotations.Key;
import jatymon.annotations.Typestate;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationsCollector {
    public static final Set<Class<? extends Annotation>> SUPPORTED_ANNOTATIONS = Set.of(Typestate.class, Ext.class, Key.class);

    // Modifier settings on annotated field elements
    public static final Set<Modifier> REQUIRED_MODIFIERS = Set.of(Modifier.PROTECTED, Modifier.PUBLIC);
    public static final Set<Modifier> OPTIONAL_MODIFIERS = Set.of(Modifier.FINAL);
    public static final Set<Modifier> ALLOWED_MODIFIERS = Stream.concat(REQUIRED_MODIFIERS.stream(), OPTIONAL_MODIFIERS.stream())
            .collect(Collectors.toUnmodifiableSet());

    private static final String JAVA_LANG_OBJECT = "java.lang.Object";

    /*
     * Values are keyed by class qualified name because they are unique (there can be two classes with the same name in
     *  different packages, and there can be two variables with the same name in two different classes)
     */
    private final Map<String, TypestateClass> typestateClasses;

    public AnnotationsCollector() {
        this.typestateClasses = new HashMap<>();
    }

    /**
     * Collects Elements that are annotated with JaTyMon's custom annotations
     * @param roundEnv processing {@code RoundEnvironment} instance
     */
    public void collectFromRound(final RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWithAny(SUPPORTED_ANNOTATIONS).forEach(this::collectAnnotated);
    }

    /**
     * Returns a map with {@code TypestateClass} instances, mapped by their qualified name
     * @return map of {@code TypestateClass} instances
     */
    public Map<String, TypestateClass> getTypestateAnnotated() {
        return Collections.unmodifiableMap(typestateClasses);
    }

    /**
     * Checks if the given element is a Field and correctly annotated (according to our typestate tool)
     * @param element Java code element
     * @return true if it is a field annotated element, false if not
     */
    public boolean isFieldAnnotated(final Element element) {
        final Set<Modifier> modifiers = element.getModifiers();
        return element.getKind() == ElementKind.FIELD
                && (element.getAnnotation(Ext.class) != null || element.getAnnotation(Key.class) != null)
                && ALLOWED_MODIFIERS.containsAll(modifiers)
                && !Collections.disjoint(REQUIRED_MODIFIERS, modifiers);
    }


    /* -------------------- AUX METHODS -------------------- */


    private void collectAnnotated(final Element el) {
        if (el.getAnnotation(Typestate.class) != null && el instanceof TypeElement typeEl) {
            final CollectionResult result = collectAnnotatedFields(typeEl);
            typestateClasses.put(typeEl.getQualifiedName().toString(), new TypestateClass(typeEl, result.key(), result.extFields()));
        }
    }


    private CollectionResult collectAnnotatedFields(final TypeElement typeElement) {
        final Map<String, Element> extFields = new HashMap<>();
        Element key = null;
        TypeElement current = typeElement;
        while (current != null && !current.getQualifiedName().toString().equals(JAVA_LANG_OBJECT)) {
            for (final Element enclosed : current.getEnclosedElements()) {
                if (!isFieldAnnotated(enclosed)) continue;
                if (enclosed.getAnnotation(Key.class) != null) {
                    key = enclosed;
                } else {
                    extFields.put(enclosed.getSimpleName().toString(), enclosed);
                }
            }
            if (current.getSuperclass() instanceof DeclaredType dt && dt.asElement() instanceof TypeElement superElement) {
                current = superElement;
            } else {
                break;
            }
        }
        return new CollectionResult(key, extFields);
    }

    /**
     * Record {@code CollectionResult} contains the relevant data regarding collected annotated elements in a typestate annotated class
     * @param key typestate key
     * @param extFields typestate ext fields
     */
    public record CollectionResult(Element key, Map<String, Element> extFields) {}
}
