package imports;

import jatymon.annotations.Typestate;
import imports.folder.EmptyClass;

@Typestate("InvalidImport2")
public class InvalidImport2 {
    public EmptyClass m() {
        return null;
    }
}