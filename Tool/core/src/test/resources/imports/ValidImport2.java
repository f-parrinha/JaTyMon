package imports;

import jatymon.annotations.Typestate;
import imports.folder.subfolder.SubEmptyClass;

@Typestate("ValidImport2")
public class ValidImport2 {
    public SubEmptyClass m() {
        return null;
    }
}