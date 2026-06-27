package generics;

import jatymon.annotations.Typestate;
import java.util.Map;

@Typestate("InvalidGeneric6")
public class InvalidGeneric6 {
    public void m(Map<String, String> arg) {
        // Nothing to do here...
    }
}