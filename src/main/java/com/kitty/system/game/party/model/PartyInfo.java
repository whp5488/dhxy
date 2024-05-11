
package com.kitty.system.game.party.model;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;

public class PartyInfo
{
    private int serial;
    private String name;
    private byte level;
    private short members;
    private int active;
    private String aim;
    private short sum;
    private short win;
    private short lose;
    private short standings;
    private List<FiedValue> master;
    private List<FiedValue> flag;
    
    public PartyInfo() {
        this.sum = 0;
        this.win = 0;
        this.lose = 0;
        this.standings = 0;
        this.master = new ArrayList<FiedValue>();
        this.flag = new ArrayList<FiedValue>();
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getLevel() {
        return this.level;
    }
    
    public short getMembers() {
        return this.members;
    }
    
    public int getActive() {
        return this.active;
    }
    
    public String getAim() {
        return this.aim;
    }
    
    public short getSum() {
        return this.sum;
    }
    
    public short getWin() {
        return this.win;
    }
    
    public short getLose() {
        return this.lose;
    }
    
    public short getStandings() {
        return this.standings;
    }
    
    public List<FiedValue> getMaster() {
        return this.master;
    }
    
    public List<FiedValue> getFlag() {
        return this.flag;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLevel(final byte level) {
        this.level = level;
    }
    
    public void setMembers(final short members) {
        this.members = members;
    }
    
    public void setActive(final int active) {
        this.active = active;
    }
    
    public void setAim(final String aim) {
        this.aim = aim;
    }
    
    public void setSum(final short sum) {
        this.sum = sum;
    }
    
    public void setWin(final short win) {
        this.win = win;
    }
    
    public void setLose(final short lose) {
        this.lose = lose;
    }
    
    public void setStandings(final short standings) {
        this.standings = standings;
    }
    
    public void setMaster(final List<FiedValue> master) {
        this.master = master;
    }
    
    public void setFlag(final List<FiedValue> flag) {
        this.flag = flag;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PartyInfo)) {
            return false;
        }
        final PartyInfo other = (PartyInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0078: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0078;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0078;
            }
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getMembers() != other.getMembers()) {
            return false;
        }
        if (this.getActive() != other.getActive()) {
            return false;
        }
        final Object this$aim = this.getAim();
        final Object other$aim = other.getAim();
        Label_0154: {
            if (this$aim == null) {
                if (other$aim == null) {
                    break Label_0154;
                }
            }
            else if (this$aim.equals(other$aim)) {
                break Label_0154;
            }
            return false;
        }
        if (this.getSum() != other.getSum()) {
            return false;
        }
        if (this.getWin() != other.getWin()) {
            return false;
        }
        if (this.getLose() != other.getLose()) {
            return false;
        }
        if (this.getStandings() != other.getStandings()) {
            return false;
        }
        final Object this$master = this.getMaster();
        final Object other$master = other.getMaster();
        Label_0243: {
            if (this$master == null) {
                if (other$master == null) {
                    break Label_0243;
                }
            }
            else if (this$master.equals(other$master)) {
                break Label_0243;
            }
            return false;
        }
        final Object this$flag = this.getFlag();
        final Object other$flag = other.getFlag();
        if (this$flag == null) {
            if (other$flag == null) {
                return true;
            }
        }
        else if (this$flag.equals(other$flag)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PartyInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getMembers();
        result = result * 59 + this.getActive();
        final Object $aim = this.getAim();
        result = result * 59 + (($aim == null) ? 43 : $aim.hashCode());
        result = result * 59 + this.getSum();
        result = result * 59 + this.getWin();
        result = result * 59 + this.getLose();
        result = result * 59 + this.getStandings();
        final Object $master = this.getMaster();
        result = result * 59 + (($master == null) ? 43 : $master.hashCode());
        final Object $flag = this.getFlag();
        result = result * 59 + (($flag == null) ? 43 : $flag.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PartyInfo(serial=" + this.getSerial() + ", name=" + this.getName() + ", level=" + this.getLevel() + ", members=" + this.getMembers() + ", active=" + this.getActive() + ", aim=" + this.getAim() + ", sum=" + this.getSum() + ", win=" + this.getWin() + ", lose=" + this.getLose() + ", standings=" + this.getStandings() + ", master=" + this.getMaster() + ", flag=" + this.getFlag() + ")";
    }
}
