package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_gift_item_open")
public class SysGiftItemOpen implements Serializable
{
    @Column("id")
    @Id
    private Integer id;
    @Comment("礼包编号")
    @Column("gift_serial")
    private Integer giftSerial;
    @Comment("道具编号")
    @Column("serial")
    private Integer serial;
    @Comment("数量")
    @Column("num")
    private Integer num;
    @Comment("概率")
    @Column("ratio")
    private double ratio;
    
    public SysGiftItemOpen() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getGiftSerial() {
        return this.giftSerial;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getNum() {
        return this.num;
    }
    
    public double getRatio() {
        return this.ratio;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setGiftSerial(final Integer giftSerial) {
        this.giftSerial = giftSerial;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setNum(final Integer num) {
        this.num = num;
    }
    
    public void setRatio(final double ratio) {
        this.ratio = ratio;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysGiftItemOpen)) {
            return false;
        }
        SysGiftItemOpen other = (SysGiftItemOpen)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$giftSerial = this.getGiftSerial();
        Object other$giftSerial = other.getGiftSerial();
        if (!((this$giftSerial != null) ? this$giftSerial.equals(other$giftSerial) : (other$giftSerial == null))) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$num = this.getNum();
        Object other$num = other.getNum();
        return ((this$num != null) ? this$num.equals(other$num) : (other$num == null)) && Double.compare(this.getRatio(), other.getRatio()) == 0;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysGiftItemOpen;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $giftSerial = this.getGiftSerial();
        result = result * 59 + (($giftSerial == null) ? 43 : $giftSerial.hashCode());
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        long $ratio = Double.doubleToLongBits(this.getRatio());
        result = result * 59 + (int)($ratio >>> 32 ^ $ratio);
        return result;
    }
    
    @Override
    public String toString() {
        return "SysGiftItemOpen(id=" + this.getId() + ", giftSerial=" + this.getGiftSerial() + ", serial=" + this.getSerial() + ", num=" + this.getNum() + ", ratio=" + this.getRatio() + ")";
    }
}
