package internalstate;

import jatymon.annotations.Ext;
import jatymon.annotations.Typestate;

@Typestate("ABPSender")
public class ABPSender extends ABPDual {
    /* Nothing to do here... This example can also test if the processor detected methods from super class */
    /* NOTE: Let it extend ABPDual instead of ABP to test if it can get to methods of super classes */
}