package imports;

import jatymon.annotations.Typestate;
import imports.folder.EmptyClass;

@Typestate("ValidImport1")
public class ValidImport1 {
    public EmptyClass m() {
        return null;
    }
}