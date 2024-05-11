
package com.kitty.system.boss.model;

import com.kitty.system.init.domain.SysDataMonster;
import java.util.List;

public class BossFightParam
{
    private List<SysDataMonster> bossParamList;
    private int fightType;
    private int taskId;
    private int npcId;
    private short npcLevel;
    private List<String> shoutingList;
    private boolean loadGuard;
    private byte specialMonsterType;
    
    public BossFightParam(final List<SysDataMonster> bossParamList, final int fightType) {
        this.loadGuard = true;
        this.bossParamList = bossParamList;
        this.fightType = fightType;
    }
    
    public BossFightParam(final List<SysDataMonster> bossParamList, final int fightType, final int taskId, final int npcId) {
        this.loadGuard = true;
        this.bossParamList = bossParamList;
        this.fightType = fightType;
        this.taskId = taskId;
        this.npcId = npcId;
    }
    
    public List<SysDataMonster> getBossParamList() {
        return this.bossParamList;
    }
    
    public void setBossParamList(final List<SysDataMonster> bossParamList) {
        this.bossParamList = bossParamList;
    }
    
    public int getFightType() {
        return this.fightType;
    }
    
    public int getTaskId() {
        return this.taskId;
    }
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public short getNpcLevel() {
        return this.npcLevel;
    }
    
    public List<String> getShoutingList() {
        return this.shoutingList;
    }
    
    public boolean isLoadGuard() {
        return this.loadGuard;
    }
    
    public byte getSpecialMonsterType() {
        return this.specialMonsterType;
    }
    
    public void setFightType(final int fightType) {
        this.fightType = fightType;
    }
    
    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }
    
    public void setNpcId(final int npcId) {
        this.npcId = npcId;
    }
    
    public void setNpcLevel(final short npcLevel) {
        this.npcLevel = npcLevel;
    }
    
    public void setShoutingList(final List<String> shoutingList) {
        this.shoutingList = shoutingList;
    }
    
    public void setLoadGuard(final boolean loadGuard) {
        this.loadGuard = loadGuard;
    }
    
    public void setSpecialMonsterType(final byte specialMonsterType) {
        this.specialMonsterType = specialMonsterType;
    }
}
