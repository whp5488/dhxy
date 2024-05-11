
package com.kitty.system.enums.war.itempro.def;

public enum HORSE_SKILL_TRIGGER_DICT
{
    BEFORE_MAGIC_HURT("法术伤害前使用", "beforeMagicHurt"), 
    TARGET_HITS("命中目标时", "targetHits"), 
    BY_TARGET_HITS("被命中时", "byTargetHits"), 
    REDUCE_HURT("扣减伤害前", "reduceHurt"), 
    REDUCE_HURT_TARGET("扣减伤害前（目标）", "reduceHurtTarget"), 
    STUDY_FINISH("学习完毕后", "studyFinish"), 
    STUDY_FINISH_LAST("学习完毕后(最后)", "studyFinishLast"), 
    STUDY_FINISH_ZYFY("学习完毕后(左右逢源)", "studyFinishZyfy");
    
    private String name;
    private String value;
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    private HORSE_SKILL_TRIGGER_DICT(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
}
