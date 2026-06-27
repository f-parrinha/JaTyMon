package basic;

import jatymon.annotations.Typestate;

@Typestate("JavaIterator.protocol")
public class JavaIterator {
    public boolean hasNext() {
        return true;
    }
    public String next() {
        return null;
    }
}