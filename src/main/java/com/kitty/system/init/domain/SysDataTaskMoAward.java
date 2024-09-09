package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_task_mo_award")
public class SysDataTaskMoAward implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("道具编号")
    @Column("serial")
    private int serial;
    @Comment("男人")
    @Column("ren_male")
    private Integer renMale;
    @Comment("女人")
    @Column("ren_female")
    private Integer renFemale;
    @Comment("男仙")
    @Column("xian_male")
    private Integer xianMale;
    @Comment("女仙")
    @Column("xian_female")
    private Integer xianFemale;
    @Comment("男魔")
    @Column("mo_male")
    private Integer moMale;
    @Comment("女魔")
    @Column("mo_female")
    private Integer moFemale;
    @Comment("男鬼")
    @Column("gui_male")
    private Integer guiMale;
    @Comment("女鬼")
    @Column("gui_female")
    private Integer guiFemale;
    @Comment("男龙")
    @Column("long_male")
    private Integer longMale;
    @Comment("女龙")
    @Column("long_female")
    private Integer longFemale;
    @Comment("副本")
    @Column("dungeon")
    private int dungeon;
    
    public SysDataTaskMoAward() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public Integer getRenMale() {
        return this.renMale;
    }
    
    public Integer getRenFemale() {
        return this.renFemale;
    }
    
    public Integer getXianMale() {
        return this.xianMale;
    }
    
    public Integer getXianFemale() {
        return this.xianFemale;
    }
    
    public Integer getMoMale() {
        return this.moMale;
    }
    
    public Integer getMoFemale() {
        return this.moFemale;
    }
    
    public Integer getGuiMale() {
        return this.guiMale;
    }
    
    public Integer getGuiFemale() {
        return this.guiFemale;
    }
    
    public Integer getLongMale() {
        return this.longMale;
    }
    
    public Integer getLongFemale() {
        return this.longFemale;
    }
    
    public int getDungeon() {
        return this.dungeon;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRenMale(final Integer renMale) {
        this.renMale = renMale;
    }
    
    public void setRenFemale(final Integer renFemale) {
        this.renFemale = renFemale;
    }
    
    public void setXianMale(final Integer xianMale) {
        this.xianMale = xianMale;
    }
    
    public void setXianFemale(final Integer xianFemale) {
        this.xianFemale = xianFemale;
    }
    
    public void setMoMale(final Integer moMale) {
        this.moMale = moMale;
    }
    
    public void setMoFemale(final Integer moFemale) {
        this.moFemale = moFemale;
    }
    
    public void setGuiMale(final Integer guiMale) {
        this.guiMale = guiMale;
    }
    
    public void setGuiFemale(final Integer guiFemale) {
        this.guiFemale = guiFemale;
    }
    
    public void setLongMale(final Integer longMale) {
        this.longMale = longMale;
    }
    
    public void setLongFemale(final Integer longFemale) {
        this.longFemale = longFemale;
    }
    
    public void setDungeon(final int dungeon) {
        this.dungeon = dungeon;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskMoAward)) {
            return false;
        }
        SysDataTaskMoAward other = (SysDataTaskMoAward)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$renMale = this.getRenMale();
        Object other$renMale = other.getRenMale();
        if (!((this$renMale != null) ? this$renMale.equals(other$renMale) : (other$renMale == null))) {
            return false;
        }
        Object this$renFemale = this.getRenFemale();
        Object other$renFemale = other.getRenFemale();
        if (!((this$renFemale != null) ? this$renFemale.equals(other$renFemale) : (other$renFemale == null))) {
            return false;
        }
        Object this$xianMale = this.getXianMale();
        Object other$xianMale = other.getXianMale();
        if (!((this$xianMale != null) ? this$xianMale.equals(other$xianMale) : (other$xianMale == null))) {
            return false;
        }
        Object this$xianFemale = this.getXianFemale();
        Object other$xianFemale = other.getXianFemale();
        if (!((this$xianFemale != null) ? this$xianFemale.equals(other$xianFemale) : (other$xianFemale == null))) {
            return false;
        }
        Object this$moMale = this.getMoMale();
        Object other$moMale = other.getMoMale();
        if (!((this$moMale != null) ? this$moMale.equals(other$moMale) : (other$moMale == null))) {
            return false;
        }
        Object this$moFemale = this.getMoFemale();
        Object other$moFemale = other.getMoFemale();
        if (!((this$moFemale != null) ? this$moFemale.equals(other$moFemale) : (other$moFemale == null))) {
            return false;
        }
        Object this$guiMale = this.getGuiMale();
        Object other$guiMale = other.getGuiMale();
        if (!((this$guiMale != null) ? this$guiMale.equals(other$guiMale) : (other$guiMale == null))) {
            return false;
        }
        Object this$guiFemale = this.getGuiFemale();
        Object other$guiFemale = other.getGuiFemale();
        if (!((this$guiFemale != null) ? this$guiFemale.equals(other$guiFemale) : (other$guiFemale == null))) {
            return false;
        }
        Object this$longMale = this.getLongMale();
        Object other$longMale = other.getLongMale();
        if (!((this$longMale != null) ? this$longMale.equals(other$longMale) : (other$longMale == null))) {
            return false;
        }
        Object this$longFemale = this.getLongFemale();
        Object other$longFemale = other.getLongFemale();
        return ((this$longFemale != null) ? this$longFemale.equals(other$longFemale) : (other$longFemale == null)) && this.getDungeon() == other.getDungeon();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskMoAward;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $renMale = this.getRenMale();
        result = result * 59 + (($renMale == null) ? 43 : $renMale.hashCode());
        Object $renFemale = this.getRenFemale();
        result = result * 59 + (($renFemale == null) ? 43 : $renFemale.hashCode());
        Object $xianMale = this.getXianMale();
        result = result * 59 + (($xianMale == null) ? 43 : $xianMale.hashCode());
        Object $xianFemale = this.getXianFemale();
        result = result * 59 + (($xianFemale == null) ? 43 : $xianFemale.hashCode());
        Object $moMale = this.getMoMale();
        result = result * 59 + (($moMale == null) ? 43 : $moMale.hashCode());
        Object $moFemale = this.getMoFemale();
        result = result * 59 + (($moFemale == null) ? 43 : $moFemale.hashCode());
        Object $guiMale = this.getGuiMale();
        result = result * 59 + (($guiMale == null) ? 43 : $guiMale.hashCode());
        Object $guiFemale = this.getGuiFemale();
        result = result * 59 + (($guiFemale == null) ? 43 : $guiFemale.hashCode());
        Object $longMale = this.getLongMale();
        result = result * 59 + (($longMale == null) ? 43 : $longMale.hashCode());
        Object $longFemale = this.getLongFemale();
        result = result * 59 + (($longFemale == null) ? 43 : $longFemale.hashCode());
        result = result * 59 + this.getDungeon();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskMoAward(serial=" + this.getSerial() + ", renMale=" + this.getRenMale() + ", renFemale=" + this.getRenFemale() + ", xianMale=" + this.getXianMale() + ", xianFemale=" + this.getXianFemale() + ", moMale=" + this.getMoMale() + ", moFemale=" + this.getMoFemale() + ", guiMale=" + this.getGuiMale() + ", guiFemale=" + this.getGuiFemale() + ", longMale=" + this.getLongMale() + ", longFemale=" + this.getLongFemale() + ", dungeon=" + this.getDungeon() + ")";
    }
}
