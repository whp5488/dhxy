package com.kitty.system.game.player.bean;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class PkInfoBox implements Serializable
{
    private boolean redName;
    private int incense;
    private int conceal;
    private int redTime;
    private int prisonTime;
    private Map<Long, PkRecord> killRoles;
    private Map<Long, PkRecord> beKillRoles;
    
    public PkInfoBox() {
        this.killRoles = new HashMap();
        this.beKillRoles = new HashMap();
    }
    
    public boolean isRedName() {
        return this.redName;
    }
    
    public int getIncense() {
        return this.incense;
    }
    
    public int getConceal() {
        return this.conceal;
    }
    
    public int getRedTime() {
        return this.redTime;
    }
    
    public int getPrisonTime() {
        return this.prisonTime;
    }
    
    public Map<Long, PkRecord> getKillRoles() {
        return this.killRoles;
    }
    
    public Map<Long, PkRecord> getBeKillRoles() {
        return this.beKillRoles;
    }
    
    public void setRedName(final boolean redName) {
        this.redName = redName;
    }
    
    public void setIncense(final int incense) {
        this.incense = incense;
    }
    
    public void setConceal(final int conceal) {
        this.conceal = conceal;
    }
    
    public void setRedTime(final int redTime) {
        this.redTime = redTime;
    }
    
    public void setPrisonTime(final int prisonTime) {
        this.prisonTime = prisonTime;
    }
    
    public void setKillRoles(final Map<Long, PkRecord> killRoles) {
        this.killRoles = killRoles;
    }
    
    public void setBeKillRoles(final Map<Long, PkRecord> beKillRoles) {
        this.beKillRoles = beKillRoles;
    }
}
