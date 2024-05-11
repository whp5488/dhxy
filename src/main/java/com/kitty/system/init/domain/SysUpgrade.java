
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_upgrade")
public class SysUpgrade implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("编号")
    @Column("id")
    private Integer id;
    @Comment("转生")
    @Column("zs")
    private Integer zs;
    @Comment("等级")
    @Column("lv")
    private Integer lv;
    @Comment("奖励")
    @Column("reward")
    private String reward;
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getZs() {
        return this.zs;
    }
    
    public Integer getLv() {
        return this.lv;
    }
    
    public String getReward() {
        return this.reward;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setZs(final Integer zs) {
        this.zs = zs;
    }
    
    public void setLv(final Integer lv) {
        this.lv = lv;
    }
    
    public void setReward(final String reward) {
        this.reward = reward;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysUpgrade)) {
            return false;
        }
        final SysUpgrade other = (SysUpgrade)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$zs = this.getZs();
        final Object other$zs = other.getZs();
        Label_0102: {
            if (this$zs == null) {
                if (other$zs == null) {
                    break Label_0102;
                }
            }
            else if (this$zs.equals(other$zs)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$lv = this.getLv();
        final Object other$lv = other.getLv();
        Label_0139: {
            if (this$lv == null) {
                if (other$lv == null) {
                    break Label_0139;
                }
            }
            else if (this$lv.equals(other$lv)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$reward = this.getReward();
        final Object other$reward = other.getReward();
        if (this$reward == null) {
            if (other$reward == null) {
                return true;
            }
        }
        else if (this$reward.equals(other$reward)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysUpgrade;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $zs = this.getZs();
        result = result * 59 + (($zs == null) ? 43 : $zs.hashCode());
        final Object $lv = this.getLv();
        result = result * 59 + (($lv == null) ? 43 : $lv.hashCode());
        final Object $reward = this.getReward();
        result = result * 59 + (($reward == null) ? 43 : $reward.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysUpgrade(id=" + this.getId() + ", zs=" + this.getZs() + ", lv=" + this.getLv() + ", reward=" + this.getReward() + ")";
    }
}
