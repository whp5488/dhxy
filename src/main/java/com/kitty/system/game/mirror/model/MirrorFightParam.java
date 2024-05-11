
package com.kitty.system.game.mirror.model;

import java.util.List;

public class MirrorFightParam
{
    private byte fightType;
    private int npcId;
    FightMirror fightMirror;
    List<FightMirror> fightMirrorList;
    
    public MirrorFightParam(final byte fightType, final int npcId, final FightMirror fightMirror) {
        this.fightType = fightType;
        this.npcId = npcId;
        this.fightMirror = fightMirror;
    }
    
    public MirrorFightParam(final byte fightType, final int npcId, final List<FightMirror> fightMirrorList) {
        this.fightType = fightType;
        this.npcId = npcId;
        this.fightMirrorList = fightMirrorList;
    }
    
    public byte getFightType() {
        return this.fightType;
    }
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public FightMirror getFightMirror() {
        return this.fightMirror;
    }
    
    public List<FightMirror> getFightMirrorList() {
        return this.fightMirrorList;
    }
    
    public void setFightType(final byte fightType) {
        this.fightType = fightType;
    }
    
    public void setNpcId(final int npcId) {
        this.npcId = npcId;
    }
    
    public void setFightMirror(final FightMirror fightMirror) {
        this.fightMirror = fightMirror;
    }
    
    public void setFightMirrorList(final List<FightMirror> fightMirrorList) {
        this.fightMirrorList = fightMirrorList;
    }
}
