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
    
    public SysGuajiWar() {
    }
    
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
        SysGuajiWar other = (SysGuajiWar)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$ratio = this.getRatio();
        Object other$ratio = other.getRatio();
        if (!((this$ratio != null) ? this$ratio.equals(other$ratio) : (other$ratio == null))) {
            return false;
        }
        Object this$exp = this.getExp();
        Object other$exp = other.getExp();
        if (!((this$exp != null) ? this$exp.equals(other$exp) : (other$exp == null))) {
            return false;
        }
        Object this$item = this.getItem();
        Object other$item = other.getItem();
        if (!((this$item != null) ? this$item.equals(other$item) : (other$item == null))) {
            return false;
        }
        Object this$limitExp = this.getLimitExp();
        Object other$limitExp = other.getLimitExp();
        if (!((this$limitExp != null) ? this$limitExp.equals(other$limitExp) : (other$limitExp == null))) {
            return false;
        }
        Object this$warDataId = this.getWarDataId();
        Object other$warDataId = other.getWarDataId();
        return (this$warDataId != null) ? this$warDataId.equals(other$warDataId) : (other$warDataId == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysGuajiWar;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $ratio = this.getRatio();
        result = result * 59 + (($ratio == null) ? 43 : $ratio.hashCode());
        Object $exp = this.getExp();
        result = result * 59 + (($exp == null) ? 43 : $exp.hashCode());
        Object $item = this.getItem();
        result = result * 59 + (($item == null) ? 43 : $item.hashCode());
        Object $limitExp = this.getLimitExp();
        result = result * 59 + (($limitExp == null) ? 43 : $limitExp.hashCode());
        Object $warDataId = this.getWarDataId();
        result = result * 59 + (($warDataId == null) ? 43 : $warDataId.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysGuajiWar(id=" + this.getId() + ", serial=" + this.getSerial() + ", ratio=" + this.getRatio() + ", exp=" + this.getExp() + ", item=" + this.getItem() + ", limitExp=" + this.getLimitExp() + ", warDataId=" + this.getWarDataId() + ")";
    }
}
