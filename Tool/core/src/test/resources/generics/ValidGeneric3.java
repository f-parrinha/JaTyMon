package generics;

import jatymon.annotations.Typestate;

import java.util.List;
import java.util.Map;

@Typestate("ValidGeneric3")
public class ValidGeneric3 {
    public void m(Map<String, List<Integer>> map) {
        // Nothing to do here...
    }
}

