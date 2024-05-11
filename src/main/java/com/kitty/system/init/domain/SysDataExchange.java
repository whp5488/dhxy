
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_exchange")
public class SysDataExchange implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("物品编号")
    @Column("serial")
    private int serial;
    @Comment("物品数量")
    @Column("num")
    private int num;
    @Comment("碎片编号")
    @Column("need_serial")
    private int needSerial;
    @Comment("碎片数量")
    @Column("need_num")
    private int needNum;
    @Comment("类型：0.装备 1.道具 2.宠物 3.变身卡 4.天策符")
    @Column("type")
    private int type;
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public int getNeedSerial() {
        return this.needSerial;
    }
    
    public int getNeedNum() {
        return this.needNum;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public void setNeedSerial(final int needSerial) {
        this.needSerial = needSerial;
    }
    
    public void setNeedNum(final int needNum) {
        this.needNum = needNum;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataExchange)) {
            return false;
        }
        final SysDataExchange other = (SysDataExchange)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getNum() == other.getNum() && this.getNeedSerial() == other.getNeedSerial() && this.getNeedNum() == other.getNeedNum() && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataExchange;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNum();
        result = result * 59 + this.getNeedSerial();
        result = result * 59 + this.getNeedNum();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataExchange(serial=" + this.getSerial() + ", num=" + this.getNum() + ", needSerial=" + this.getNeedSerial() + ", needNum=" + this.getNeedNum() + ", type=" + this.getType() + ")";
    }
}
