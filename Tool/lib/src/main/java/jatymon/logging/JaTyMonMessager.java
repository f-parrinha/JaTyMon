package jatymon.logging;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Custom messager/printer with custom color for the JaTyC tool
 * @author Francisco Parrinha
 */
public class JaTyMonMessager implements Messager {
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence msg) {
        System.out.printf("[%s] %s%n", getPaintedKind(kind), msg);
    }

    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element e) {
        System.out.printf("[%s] %s (Element: %s)%n",  getPaintedKind(kind), msg, e);
    }

    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence msg, final Element e, final AnnotationMirror a) {
        System.out.printf("[%s] %s (Element: %s, AnnotationMirror: %s)%n",  getPaintedKind(kind), msg, e, a);

    }

    @Override
    public void printMessage(final Diagnostic.Kind kind,
                             final CharSequence msg,
                             final Element e,
                             final AnnotationMirror a,
                             final AnnotationValue v) {
        System.out.printf("[%s] %s (Element: %s, AnnotationMirror: %s, AnnotationValue: %s)%n", getPaintedKind(kind), msg, e, a, v);
    }

    private String getPaintedKind(final Diagnostic.Kind kind) {
        switch (kind) {
            case ERROR -> {
                return String.format("\u001B[1m\u001B[31m%s\u001B[0m", kind);
            }
            case WARNING -> {
                return String.format("\u001B[1m\u001B[33m%s\u001B[0m", kind);
            }
            case NOTE ->
            {
                return String.format("\u001B[1m\u001B[32m%s\u001B[0m", kind);
            }
            default -> {
                return String.format("\u001B[1m%s\u001B[0m", kind);
            }
        }
    }
}
