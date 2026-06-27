package basic;

import jatymon.annotations.Typestate;

@Typestate("JavaIteratorWrapper.protocol")
public class JavaIteratorWrapper {
    public void init(JavaIterator iterator) {
        // Nothing to do here...
    }
    public boolean hasNext() {
        return true;
    }
    public String next() {
        return null;
    }
}