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
    
    public SysUpgrade() {
    }
    
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
        SysUpgrade other = (SysUpgrade)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$zs = this.getZs();
        Object other$zs = other.getZs();
        if (!((this$zs != null) ? this$zs.equals(other$zs) : (other$zs == null))) {
            return false;
        }
        Object this$lv = this.getLv();
        Object other$lv = other.getLv();
        if (!((this$lv != null) ? this$lv.equals(other$lv) : (other$lv == null))) {
            return false;
        }
        Object this$reward = this.getReward();
        Object other$reward = other.getReward();
        return (this$reward != null) ? this$reward.equals(other$reward) : (other$reward == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysUpgrade;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $zs = this.getZs();
        result = result * 59 + (($zs == null) ? 43 : $zs.hashCode());
        Object $lv = this.getLv();
        result = result * 59 + (($lv == null) ? 43 : $lv.hashCode());
        Object $reward = this.getReward();
        result = result * 59 + (($reward == null) ? 43 : $reward.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysUpgrade(id=" + this.getId() + ", zs=" + this.getZs() + ", lv=" + this.getLv() + ", reward=" + this.getReward() + ")";
    }
}
