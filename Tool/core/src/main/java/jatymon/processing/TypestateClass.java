package jatymon.processing;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Map;
import java.util.Objects;

/**
 * Record {@code TypestateAnnotated} holds relevant data of a class annotated with the @Typestate annotation, including its
 * "sub annotations" @Ext and @Key
 */
public record TypestateClass(TypeElement typeElement, Element key, Map<String, Element> extFields) {


    public String getQualifiedName() {
        return typeElement.getQualifiedName().toString();
    }

    public String getSimpleName() {
        return typeElement.getSimpleName().toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TypestateClass(final TypeElement element, final Element key1, final Map<String, Element> fields)
                && typeElement.equals(element)
                && key.equals(key1)
                && extFields.equals(fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeElement, key, extFields);
    }

    @Override
    public String toString() {
        return "TypestateClass{typeElement: %s, key: %s, extFields: %s}".formatted(typeElement, key, extFields);
    }
}
