package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53716)
public class RespInputInstruct extends Message
{
    private int attaId;
    private int targetId;
    private int action;
    private int skillId;
    private int sandClock;
    
    public void setAttaId(final int attaId) {
        this.attaId = attaId;
    }
    
    public void setTargetId(final int targetId) {
        this.targetId = targetId;
    }
    
    public void setAction(final int action) {
        this.action = action;
    }
    
    public void setSkillId(final int skillId) {
        this.skillId = skillId;
    }
    
    public void setSandClock(final int sandClock) {
        this.sandClock = sandClock;
    }
    
    public int getAttaId() {
        return this.attaId;
    }
    
    public int getTargetId() {
        return this.targetId;
    }
    
    public int getAction() {
        return this.action;
    }
    
    public int getSkillId() {
        return this.skillId;
    }
    
    public int getSandClock() {
        return this.sandClock;
    }
    
    public RespInputInstruct(final int attaId, final int targetId, final int action, final int skillId, final int sandClock) {
        this.attaId = attaId;
        this.targetId = targetId;
        this.action = action;
        this.skillId = skillId;
        this.sandClock = sandClock;
    }
    
    public RespInputInstruct() {
    }
}
