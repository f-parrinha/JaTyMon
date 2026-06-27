package jatymon.diagnostics.semantic.internalstate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class DuplicateFieldDiagnostic extends SemanticErrorDiagnostic {

    /*
     * Note: The same error must be shown for both "val" and "ext" values because they can be used together in
     *  the same expression. For example:
     *      ext x;
     *      val x =0;
     *      assign myAssign: x := x + 1
     *  It is impossible to know which x it is: is it the "ext" or the "val"?
     */

    public static final String MESSAGE = "Duplicate field '%s'";
    public DuplicateFieldDiagnostic(String name, String fileName, TokenPosition tokenPosition) {
        super(String.format(MESSAGE, name), fileName, tokenPosition);
    }
}
