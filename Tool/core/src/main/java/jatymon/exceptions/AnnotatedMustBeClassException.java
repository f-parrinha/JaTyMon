package jatymon.exceptions;

import javax.lang.model.element.Element;

public class AnnotatedMustBeClassException extends Exception {
    public static final String MESSAGE = "Annotated element '%s' is not a class";
    public AnnotatedMustBeClassException(final Element element) {
        super(MESSAGE.formatted(element.getSimpleName()));
    }
}
