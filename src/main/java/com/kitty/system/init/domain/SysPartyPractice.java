package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_party_practice")
public class SysPartyPractice implements Serializable
{
    @Column("level")
    private int level;
    @Column("tribute")
    private int tribute;
    @Column("coin")
    private int coin;
    @Column("need")
    private int need;
    
    public SysPartyPractice() {
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getTribute() {
        return this.tribute;
    }
    
    public int getCoin() {
        return this.coin;
    }
    
    public int getNeed() {
        return this.need;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setTribute(final int tribute) {
        this.tribute = tribute;
    }
    
    public void setCoin(final int coin) {
        this.coin = coin;
    }
    
    public void setNeed(final int need) {
        this.need = need;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPartyPractice)) {
            return false;
        }
        SysPartyPractice other = (SysPartyPractice)o;
        return other.canEqual(this) && this.getLevel() == other.getLevel() && this.getTribute() == other.getTribute() && this.getCoin() == other.getCoin() && this.getNeed() == other.getNeed();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPartyPractice;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getTribute();
        result = result * 59 + this.getCoin();
        result = result * 59 + this.getNeed();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPartyPractice(level=" + this.getLevel() + ", tribute=" + this.getTribute() + ", coin=" + this.getCoin() + ", need=" + this.getNeed() + ")";
    }
}
