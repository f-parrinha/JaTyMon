package imports;

import jatymon.annotations.Typestate;
import imports.folder.EmptyClass;

@Typestate("UnusedImport")
public class UnusedImport {
    public EmptyClass m() {
        // Nothing to do here...
        return null;
    }
}
