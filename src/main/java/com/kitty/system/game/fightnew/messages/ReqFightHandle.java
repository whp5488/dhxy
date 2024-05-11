
package com.kitty.system.game.fightnew.messages;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 3470)
public class ReqFightHandle extends Message
{
    private int round;
    private int autoFight;
    private int attackerId;
    private int targetId;
    private int type;
    private int skillId;
    
    public void setRound(final int round) {
        this.round = round;
    }
    
    public void setAutoFight(final int autoFight) {
        this.autoFight = autoFight;
    }
    
    public void setAttackerId(final int attackerId) {
        this.attackerId = attackerId;
    }
    
    public void setTargetId(final int targetId) {
        this.targetId = targetId;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setSkillId(final int skillId) {
        this.skillId = skillId;
    }
    
    public int getRound() {
        return this.round;
    }
    
    public int getAutoFight() {
        return this.autoFight;
    }
    
    public int getAttackerId() {
        return this.attackerId;
    }
    
    public int getTargetId() {
        return this.targetId;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getSkillId() {
        return this.skillId;
    }
    
    @ConstructorProperties({ "round", "autoFight", "attackerId", "targetId", "type", "skillId" })
    public ReqFightHandle(final int round, final int autoFight, final int attackerId, final int targetId, final int type, final int skillId) {
        this.round = round;
        this.autoFight = autoFight;
        this.attackerId = attackerId;
        this.targetId = targetId;
        this.type = type;
        this.skillId = skillId;
    }
    
    public ReqFightHandle() {
    }
}
