
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.game.vo.init.Item;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_guaji_war")
public class SysGuajiWar implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("编号")
    @Column("serial")
    private Integer serial;
    @Comment("比率")
    @Column("ratio")
    private Integer ratio;
    @Comment("经验")
    @Column("exp")
    private Integer exp;
    @Comment("奖励道具")
    @Column("item")
    @Default("[]")
    private List<Item> item;
    @Comment("经验限制")
    @Column("limit_exp")
    private Integer limitExp;
    @Comment("战斗外键")
    @Column("war_data_id")
    private Integer warDataId;
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getRatio() {
        return this.ratio;
    }
    
    public Integer getExp() {
        return this.exp;
    }
    
    public List<Item> getItem() {
        return this.item;
    }
    
    public Integer getLimitExp() {
        return this.limitExp;
    }
    
    public Integer getWarDataId() {
        return this.warDataId;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setRatio(final Integer ratio) {
        this.ratio = ratio;
    }
    
    public void setExp(final Integer exp) {
        this.exp = exp;
    }
    
    public void setItem(final List<Item> item) {
        this.item = item;
    }
    
    public void setLimitExp(final Integer limitExp) {
        this.limitExp = limitExp;
    }
    
    public void setWarDataId(final Integer warDataId) {
        this.warDataId = warDataId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysGuajiWar)) {
            return false;
        }
        final SysGuajiWar other = (SysGuajiWar)o;
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
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0102: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0102;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$ratio = this.getRatio();
        final Object other$ratio = other.getRatio();
        Label_0139: {
            if (this$ratio == null) {
                if (other$ratio == null) {
                    break Label_0139;
                }
            }
            else if (this$ratio.equals(other$ratio)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$exp = this.getExp();
        final Object other$exp = other.getExp();
        Label_0176: {
            if (this$exp == null) {
                if (other$exp == null) {
                    break Label_0176;
                }
            }
            else if (this$exp.equals(other$exp)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$item = this.getItem();
        final Object other$item = other.getItem();
        Label_0213: {
            if (this$item == null) {
                if (other$item == null) {
                    break Label_0213;
                }
            }
            else if (this$item.equals(other$item)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$limitExp = this.getLimitExp();
        final Object other$limitExp = other.getLimitExp();
        Label_0250: {
            if (this$limitExp == null) {
                if (other$limitExp == null) {
                    break Label_0250;
                }
            }
            else if (this$limitExp.equals(other$limitExp)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$warDataId = this.getWarDataId();
        final Object other$warDataId = other.getWarDataId();
        if (this$warDataId == null) {
            if (other$warDataId == null) {
                return true;
            }
        }
        else if (this$warDataId.equals(other$warDataId)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysGuajiWar;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $ratio = this.getRatio();
        result = result * 59 + (($ratio == null) ? 43 : $ratio.hashCode());
        final Object $exp = this.getExp();
        result = result * 59 + (($exp == null) ? 43 : $exp.hashCode());
        final Object $item = this.getItem();
        result = result * 59 + (($item == null) ? 43 : $item.hashCode());
        final Object $limitExp = this.getLimitExp();
        result = result * 59 + (($limitExp == null) ? 43 : $limitExp.hashCode());
        final Object $warDataId = this.getWarDataId();
        result = result * 59 + (($warDataId == null) ? 43 : $warDataId.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysGuajiWar(id=" + this.getId() + ", serial=" + this.getSerial() + ", ratio=" + this.getRatio() + ", exp=" + this.getExp() + ", item=" + this.getItem() + ", limitExp=" + this.getLimitExp() + ", warDataId=" + this.getWarDataId() + ")";
    }
}
