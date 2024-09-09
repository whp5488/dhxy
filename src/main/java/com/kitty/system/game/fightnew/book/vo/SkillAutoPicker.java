package com.kitty.system.game.fightnew.book.vo;

public class SkillAutoPicker
{
    private int skillId;
    private int targetId;
    private boolean def;
    private int type;
    
    public SkillAutoPicker() {
    }
    
    public int getSkillId() {
        return this.skillId;
    }
    
    public SkillAutoPicker setSkillId(int skillId) {
        this.skillId = skillId;
        return this;
    }
    
    public int getTargetId() {
        return this.targetId;
    }
    
    public SkillAutoPicker setTargetId(int targetId) {
        this.targetId = targetId;
        return this;
    }
    
    public boolean isDef() {
        return this.def;
    }
    
    public SkillAutoPicker setDef(boolean def) {
        this.def = def;
        return this;
    }
    
    public int getType() {
        return this.type;
    }
    
    public SkillAutoPicker setType(int type) {
        this.type = type;
        return this;
    }
}
