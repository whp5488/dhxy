
package com.kitty.system.game.vo.init;

import java.beans.ConstructorProperties;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import java.io.Serializable;

public class Summon implements Serializable
{
    private Integer serial;
    private Integer num;
    private Integer rate;
    @Default("[]")
    private List<Integer> monster;
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getNum() {
        return this.num;
    }
    
    public Integer getRate() {
        return this.rate;
    }
    
    public List<Integer> getMonster() {
        return this.monster;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setNum(final Integer num) {
        this.num = num;
    }
    
    public void setRate(final Integer rate) {
        this.rate = rate;
    }
    
    public void setMonster(final List<Integer> monster) {
        this.monster = monster;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Summon)) {
            return false;
        }
        final Summon other = (Summon)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0065: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0065;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$num = this.getNum();
        final Object other$num = other.getNum();
        Label_0102: {
            if (this$num == null) {
                if (other$num == null) {
                    break Label_0102;
                }
            }
            else if (this$num.equals(other$num)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$rate = this.getRate();
        final Object other$rate = other.getRate();
        Label_0139: {
            if (this$rate == null) {
                if (other$rate == null) {
                    break Label_0139;
                }
            }
            else if (this$rate.equals(other$rate)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$monster = this.getMonster();
        final Object other$monster = other.getMonster();
        if (this$monster == null) {
            if (other$monster == null) {
                return true;
            }
        }
        else if (this$monster.equals(other$monster)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Summon;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        final Object $rate = this.getRate();
        result = result * 59 + (($rate == null) ? 43 : $rate.hashCode());
        final Object $monster = this.getMonster();
        result = result * 59 + (($monster == null) ? 43 : $monster.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Summon(serial=" + this.getSerial() + ", num=" + this.getNum() + ", rate=" + this.getRate() + ", monster=" + this.getMonster() + ")";
    }
    
    public Summon() {
    }
    
    @ConstructorProperties({ "serial", "num", "rate", "monster" })
    public Summon(final Integer serial, final Integer num, final Integer rate, final List<Integer> monster) {
        this.serial = serial;
        this.num = num;
        this.rate = rate;
        this.monster = monster;
    }
}
