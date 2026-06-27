package internalstate;

import jatymon.annotations.Ext;
import jatymon.annotations.Typestate;

@Typestate("BVPLeader")
public class BVPLeader extends BVP {
    @Ext
    protected int k;
    @Ext
    protected int n;
}