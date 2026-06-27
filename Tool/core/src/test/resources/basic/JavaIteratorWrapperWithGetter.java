package basic;

import jatymon.annotations.Typestate;

@Typestate("JavaIteratorWrapperWithGetter.protocol")
public class JavaIteratorWrapperWithGetter {
    public void init(JavaIterator iterator) {
        // Nothing to do here...
    }
    public boolean hasNext() {
        return true;
    }
    public String next() {
        return null;
    }
    public JavaIterator getIterator() {
        return null;
    }
}