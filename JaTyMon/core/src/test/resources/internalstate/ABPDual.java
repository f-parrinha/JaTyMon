package internalstate;

import jatymon.annotations.Ext;
import jatymon.annotations.Typestate;

@Typestate("ABPDual")
public class ABPDual extends ABP {
    @Ext
    protected int n;
}