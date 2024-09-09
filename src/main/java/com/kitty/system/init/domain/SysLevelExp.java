package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Table("data_level_exp")
public class SysLevelExp
{
    @Comment("类型  1=英雄，2=宠物，3=伙伴，4=坐骑")
    @Column("type")
    private byte type;
    @Comment("等级")
    @Column("level")
    private int level;
    @Comment("未转生需要的经验值")
    @Column("need_exp_rei0")
    private long needExpRei0;
    @Comment("一转需要的经验值")
    @Column("need_exp_rei1")
    private long needExpRei1;
    @Comment("二转需要的经验值")
    @Column("need_exp_rei2")
    private long needExpRei2;
    @Comment("三转需要的经验值")
    @Column("need_exp_rei3")
    private long needExpRei3;
    
    public SysLevelExp() {
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public long getNeedExpRei0() {
        return this.needExpRei0;
    }
    
    public long getNeedExpRei1() {
        return this.needExpRei1;
    }
    
    public long getNeedExpRei2() {
        return this.needExpRei2;
    }
    
    public long getNeedExpRei3() {
        return this.needExpRei3;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setNeedExpRei0(final long needExpRei0) {
        this.needExpRei0 = needExpRei0;
    }
    
    public void setNeedExpRei1(final long needExpRei1) {
        this.needExpRei1 = needExpRei1;
    }
    
    public void setNeedExpRei2(final long needExpRei2) {
        this.needExpRei2 = needExpRei2;
    }
    
    public void setNeedExpRei3(final long needExpRei3) {
        this.needExpRei3 = needExpRei3;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysLevelExp)) {
            return false;
        }
        SysLevelExp other = (SysLevelExp)o;
        return other.canEqual(this) && this.getType() == other.getType() && this.getLevel() == other.getLevel() && this.getNeedExpRei0() == other.getNeedExpRei0() && this.getNeedExpRei1() == other.getNeedExpRei1() && this.getNeedExpRei2() == other.getNeedExpRei2() && this.getNeedExpRei3() == other.getNeedExpRei3();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysLevelExp;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getLevel();
        long $needExpRei0 = this.getNeedExpRei0();
        result = result * 59 + (int)($needExpRei0 >>> 32 ^ $needExpRei0);
        long $needExpRei2 = this.getNeedExpRei1();
        result = result * 59 + (int)($needExpRei2 >>> 32 ^ $needExpRei2);
        long $needExpRei3 = this.getNeedExpRei2();
        result = result * 59 + (int)($needExpRei3 >>> 32 ^ $needExpRei3);
        long $needExpRei4 = this.getNeedExpRei3();
        result = result * 59 + (int)($needExpRei4 >>> 32 ^ $needExpRei4);
        return result;
    }
    
    @Override
    public String toString() {
        return "SysLevelExp(type=" + (int)this.getType() + ", level=" + this.getLevel() + ", needExpRei0=" + this.getNeedExpRei0() + ", needExpRei1=" + this.getNeedExpRei1() + ", needExpRei2=" + this.getNeedExpRei2() + ", needExpRei3=" + this.getNeedExpRei3() + ")";
    }
}
