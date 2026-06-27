package generics;

import jatymon.annotations.Typestate;

import java.util.List;
import java.util.Map;

@Typestate("ValidGeneric4")
public class ValidGeneric4 {
    public Map<String, Integer> m(List<String> list) {
        return null;
    }
}

