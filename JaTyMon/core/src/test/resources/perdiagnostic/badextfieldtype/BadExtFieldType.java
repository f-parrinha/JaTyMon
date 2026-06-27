package perdiagnostic.badextfieldtype;

import jatymon.annotations.Ext;
import jatymon.annotations.Typestate;

@Typestate("BadExtFieldType")
public class BadExtFieldType {

    @Ext
    protected char x;

    @Ext
    protected String y;

    @Ext
    public double w;

    @Ext
    public float z;

    public void m() {
        // Nothing to do here
    }
}