
package com.kitty.system.game.fightnew.messages;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.messages.vo.SkillCool;
import com.kitty.system.game.fightnew.model.AutoFightSkillParam;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 3586)
public class RespRoundStartParam extends Message
{
    private int objId;
    private int round;
    private String startTime;
    private boolean isAutoFight;
    private List<AutoFightSkillParam> autoFightSkillParams;
    private List<SkillCool> skillCools;
    
    public void addSkillCools(final List<SkillCool> cools) {
        if (this.skillCools == null) {
            this.skillCools = new ArrayList<SkillCool>();
        }
        this.skillCools.addAll(cools);
    }
    
    public void addAutoFightSkillParam(final AutoFightSkillParam autoFightSkillParam) {
        if (this.autoFightSkillParams == null) {
            this.autoFightSkillParams = new ArrayList<AutoFightSkillParam>();
        }
        this.autoFightSkillParams.add(autoFightSkillParam);
    }
    
    public void setObjId(final int objId) {
        this.objId = objId;
    }
    
    public void setRound(final int round) {
        this.round = round;
    }
    
    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }
    
    public void setAutoFight(final boolean isAutoFight) {
        this.isAutoFight = isAutoFight;
    }
    
    public void setAutoFightSkillParams(final List<AutoFightSkillParam> autoFightSkillParams) {
        this.autoFightSkillParams = autoFightSkillParams;
    }
    
    public void setSkillCools(final List<SkillCool> skillCools) {
        this.skillCools = skillCools;
    }
    
    public int getObjId() {
        return this.objId;
    }
    
    public int getRound() {
        return this.round;
    }
    
    public String getStartTime() {
        return this.startTime;
    }
    
    public boolean isAutoFight() {
        return this.isAutoFight;
    }
    
    public List<AutoFightSkillParam> getAutoFightSkillParams() {
        return this.autoFightSkillParams;
    }
    
    public List<SkillCool> getSkillCools() {
        return this.skillCools;
    }
    
    @ConstructorProperties({ "objId", "round", "startTime", "isAutoFight", "autoFightSkillParams", "skillCools" })
    public RespRoundStartParam(final int objId, final int round, final String startTime, final boolean isAutoFight, final List<AutoFightSkillParam> autoFightSkillParams, final List<SkillCool> skillCools) {
        this.isAutoFight = false;
        this.objId = objId;
        this.round = round;
        this.startTime = startTime;
        this.isAutoFight = isAutoFight;
        this.autoFightSkillParams = autoFightSkillParams;
        this.skillCools = skillCools;
    }
    
    public RespRoundStartParam() {
        this.isAutoFight = false;
    }
}
