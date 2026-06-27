package generics;

import jatymon.annotations.Typestate;
import java.util.List;

@Typestate("InvalidGeneric4")
public class InvalidGeneric4 {
    public void m(List<NonExistent> arg) {
        // Nothing to do here...
    }

    public class NonExistent { }
}