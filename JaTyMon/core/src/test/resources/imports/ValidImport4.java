package imports;

import jatymon.annotations.Typestate;
import imports.folder.subfolder.SubEmptyClass;
import imports.folder.EmptyClass;

@Typestate("ValidImport4")
public class ValidImport4 {
    public SubEmptyClass m(final EmptyClass emptyClass) {
        return null;
    }
}