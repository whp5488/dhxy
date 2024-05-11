
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
        final SysGiftItemOpen other = (SysGiftItemOpen)o;
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
        final Object this$giftSerial = this.getGiftSerial();
        final Object other$giftSerial = other.getGiftSerial();
        Label_0102: {
            if (this$giftSerial == null) {
                if (other$giftSerial == null) {
                    break Label_0102;
                }
            }
            else if (this$giftSerial.equals(other$giftSerial)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0139: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0139;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$num = this.getNum();
        final Object other$num = other.getNum();
        if (this$num == null) {
            if (other$num == null) {
                return Double.compare(this.getRatio(), other.getRatio()) == 0;
            }
        }
        else if (this$num.equals(other$num)) {
            return Double.compare(this.getRatio(), other.getRatio()) == 0;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysGiftItemOpen;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $giftSerial = this.getGiftSerial();
        result = result * 59 + (($giftSerial == null) ? 43 : $giftSerial.hashCode());
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        final long $ratio = Double.doubleToLongBits(this.getRatio());
        result = result * 59 + (int)($ratio >>> 32 ^ $ratio);
        return result;
    }
    
    @Override
    public String toString() {
        return "SysGiftItemOpen(id=" + this.getId() + ", giftSerial=" + this.getGiftSerial() + ", serial=" + this.getSerial() + ", num=" + this.getNum() + ", ratio=" + this.getRatio() + ")";
    }
}
