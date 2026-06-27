package perdiagnostic.unknowntype;

import jatymon.annotations.Typestate;
import perdiagnostic.common.TestClass;

@Typestate("UnknownType")
public class UnknownType {
    public TestClass m() {
        return null;
    }
}