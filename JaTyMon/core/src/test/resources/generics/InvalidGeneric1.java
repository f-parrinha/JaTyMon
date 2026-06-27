package generics;

import jatymon.annotations.Typestate;
import java.util.*;

@Typestate("InvalidGeneric1")
public class InvalidGeneric1 {
    public List<String> m() {
        return null;
    }
}