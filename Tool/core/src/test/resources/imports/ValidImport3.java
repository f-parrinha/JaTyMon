package imports;

import jatymon.annotations.Typestate;
import imports.folder.subfolder.SubEmptyClass;
import imports.folder.EmptyClass;

@Typestate("ValidImport3")
public class ValidImport3 {
    public SubEmptyClass m(EmptyClass emptyClass) {
        return null;
    }
}