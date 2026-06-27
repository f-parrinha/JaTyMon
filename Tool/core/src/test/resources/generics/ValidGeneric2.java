package generics;

import jatymon.annotations.Typestate;

import java.util.Map;

@Typestate("ValidGeneric2")
public class ValidGeneric2 {
    public Map<String, Integer> m() {
        return null;
    }
}

