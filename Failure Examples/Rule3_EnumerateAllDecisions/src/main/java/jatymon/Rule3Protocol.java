package jatymon;

import jatymon.annotations.Typestate;

@Typestate("Rule3Protocol")
public class Rule3Protocol {
    
     public MyEnum decideEnum() {
         return MyEnum.FOUR;
     }

     public boolean decideBoolean() {
         return true;
     }
}
