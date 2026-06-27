package generics;

import jatymon.annotations.Typestate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Typestate("ValidGeneric6")
public class ValidGeneric6 {
    public void m1(List<String> arg) {
        // Nothing to do here...
    }

    public void m2(List<?> arg) {
        // Nothing to do here...
    }

    public void m3(List arg) {
        // Nothing to do here...
    }
}