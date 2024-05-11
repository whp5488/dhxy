
package com.kitty.system.game.welfare.domain;

import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.ColDefine;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("t_gift_bag")
public class GiftBag
{
    @Column("name")
    private String name;
    @Column("reward")
    @ColDefine(type = ColType.TEXT)
    private List<Param> rewards;
    @Column("time")
    private long time;
    
    public String getName() {
        return this.name;
    }
    
    public List<Param> getRewards() {
        return this.rewards;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setRewards(final List<Param> rewards) {
        this.rewards = rewards;
    }
    
    public void setTime(final long time) {
        this.time = time;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GiftBag)) {
            return false;
        }
        final GiftBag other = (GiftBag)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$rewards = this.getRewards();
        final Object other$rewards = other.getRewards();
        if (this$rewards == null) {
            if (other$rewards == null) {
                return this.getTime() == other.getTime();
            }
        }
        else if (this$rewards.equals(other$rewards)) {
            return this.getTime() == other.getTime();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof GiftBag;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $rewards = this.getRewards();
        result = result * 59 + (($rewards == null) ? 43 : $rewards.hashCode());
        final long $time = this.getTime();
        result = result * 59 + (int)($time >>> 32 ^ $time);
        return result;
    }
    
    @Override
    public String toString() {
        return "GiftBag(name=" + this.getName() + ", rewards=" + this.getRewards() + ", time=" + this.getTime() + ")";
    }
}
