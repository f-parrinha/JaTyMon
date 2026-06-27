package imports;

import jatymon.annotations.Typestate;
import imports.folder.EmptyClass;

@Typestate("InvalidImport1")
public class InvalidImport1 {
    public EmptyClass m() {
        return null;
    }
}