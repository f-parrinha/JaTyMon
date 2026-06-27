package jatymon.exceptions;

import javax.lang.model.element.Element;

public class NotTypeElementException extends RuntimeException {
    private static final String MESSAGE = "The given element '%s' is not a TypeElement";
    public NotTypeElementException(Element element) {
        super(MESSAGE.formatted(element));
    }
}
