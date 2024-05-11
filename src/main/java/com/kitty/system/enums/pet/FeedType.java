
package com.kitty.system.enums.pet;

public enum FeedType
{
    REFINE("refine"), 
    REFINE_HIGH("refine_high"), 
    PSYCH("psych"), 
    SUPER_PSYCH("super_psych"), 
    BONE("bone"), 
    EXERCISE("exercise"), 
    INTIMATE("intimate"), 
    INSET_STONE("inset"), 
    GRID("grid"), 
    EXP("exp"), 
    RESTS_SKILL("rests_skill"), 
    DOGZ("dogz"), 
    SOUL("soul"), 
    YUAN_QI_DAN("yuan_qi_dan"), 
    AWAKEN("awaken"), 
    HF("hf"), 
    CUT_HF("cut_hf");
    
    private String type;
    
    private FeedType(final String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}
