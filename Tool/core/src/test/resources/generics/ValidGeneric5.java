package generics;

import jatymon.annotations.Typestate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Typestate("ValidGeneric5")
public class ValidGeneric5 {
    public Map<String, Map<List<Integer>, Set<Optional<String>>>> m(
            Map<String, List<Set<Integer>>> arg0,
            Function<Map<String, Integer>, List<Set<Optional<String>>>> arg1,
            List<Map<String, Set<Optional<Integer>>>> arg2) {
        return null;
    }
}