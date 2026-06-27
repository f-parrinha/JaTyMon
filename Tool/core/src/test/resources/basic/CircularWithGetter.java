package basic;

import jatymon.annotations.Typestate;

@Typestate("CircularWithGetter")
public class CircularWithGetter {
    public void finish() {
        // Nothing to do here...
    }

    public void setF(CircularObjWithGetter instance) {
        // Nothing to do here...
    }
}