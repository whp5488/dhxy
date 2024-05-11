
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
        final SysDataTaskMoAward other = (SysDataTaskMoAward)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$renMale = this.getRenMale();
        final Object other$renMale = other.getRenMale();
        Label_0078: {
            if (this$renMale == null) {
                if (other$renMale == null) {
                    break Label_0078;
                }
            }
            else if (this$renMale.equals(other$renMale)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$renFemale = this.getRenFemale();
        final Object other$renFemale = other.getRenFemale();
        Label_0115: {
            if (this$renFemale == null) {
                if (other$renFemale == null) {
                    break Label_0115;
                }
            }
            else if (this$renFemale.equals(other$renFemale)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$xianMale = this.getXianMale();
        final Object other$xianMale = other.getXianMale();
        Label_0152: {
            if (this$xianMale == null) {
                if (other$xianMale == null) {
                    break Label_0152;
                }
            }
            else if (this$xianMale.equals(other$xianMale)) {
                break Label_0152;
            }
            return false;
        }
        final Object this$xianFemale = this.getXianFemale();
        final Object other$xianFemale = other.getXianFemale();
        Label_0189: {
            if (this$xianFemale == null) {
                if (other$xianFemale == null) {
                    break Label_0189;
                }
            }
            else if (this$xianFemale.equals(other$xianFemale)) {
                break Label_0189;
            }
            return false;
        }
        final Object this$moMale = this.getMoMale();
        final Object other$moMale = other.getMoMale();
        Label_0226: {
            if (this$moMale == null) {
                if (other$moMale == null) {
                    break Label_0226;
                }
            }
            else if (this$moMale.equals(other$moMale)) {
                break Label_0226;
            }
            return false;
        }
        final Object this$moFemale = this.getMoFemale();
        final Object other$moFemale = other.getMoFemale();
        Label_0263: {
            if (this$moFemale == null) {
                if (other$moFemale == null) {
                    break Label_0263;
                }
            }
            else if (this$moFemale.equals(other$moFemale)) {
                break Label_0263;
            }
            return false;
        }
        final Object this$guiMale = this.getGuiMale();
        final Object other$guiMale = other.getGuiMale();
        Label_0300: {
            if (this$guiMale == null) {
                if (other$guiMale == null) {
                    break Label_0300;
                }
            }
            else if (this$guiMale.equals(other$guiMale)) {
                break Label_0300;
            }
            return false;
        }
        final Object this$guiFemale = this.getGuiFemale();
        final Object other$guiFemale = other.getGuiFemale();
        Label_0337: {
            if (this$guiFemale == null) {
                if (other$guiFemale == null) {
                    break Label_0337;
                }
            }
            else if (this$guiFemale.equals(other$guiFemale)) {
                break Label_0337;
            }
            return false;
        }
        final Object this$longMale = this.getLongMale();
        final Object other$longMale = other.getLongMale();
        Label_0374: {
            if (this$longMale == null) {
                if (other$longMale == null) {
                    break Label_0374;
                }
            }
            else if (this$longMale.equals(other$longMale)) {
                break Label_0374;
            }
            return false;
        }
        final Object this$longFemale = this.getLongFemale();
        final Object other$longFemale = other.getLongFemale();
        if (this$longFemale == null) {
            if (other$longFemale == null) {
                return this.getDungeon() == other.getDungeon();
            }
        }
        else if (this$longFemale.equals(other$longFemale)) {
            return this.getDungeon() == other.getDungeon();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskMoAward;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $renMale = this.getRenMale();
        result = result * 59 + (($renMale == null) ? 43 : $renMale.hashCode());
        final Object $renFemale = this.getRenFemale();
        result = result * 59 + (($renFemale == null) ? 43 : $renFemale.hashCode());
        final Object $xianMale = this.getXianMale();
        result = result * 59 + (($xianMale == null) ? 43 : $xianMale.hashCode());
        final Object $xianFemale = this.getXianFemale();
        result = result * 59 + (($xianFemale == null) ? 43 : $xianFemale.hashCode());
        final Object $moMale = this.getMoMale();
        result = result * 59 + (($moMale == null) ? 43 : $moMale.hashCode());
        final Object $moFemale = this.getMoFemale();
        result = result * 59 + (($moFemale == null) ? 43 : $moFemale.hashCode());
        final Object $guiMale = this.getGuiMale();
        result = result * 59 + (($guiMale == null) ? 43 : $guiMale.hashCode());
        final Object $guiFemale = this.getGuiFemale();
        result = result * 59 + (($guiFemale == null) ? 43 : $guiFemale.hashCode());
        final Object $longMale = this.getLongMale();
        result = result * 59 + (($longMale == null) ? 43 : $longMale.hashCode());
        final Object $longFemale = this.getLongFemale();
        result = result * 59 + (($longFemale == null) ? 43 : $longFemale.hashCode());
        result = result * 59 + this.getDungeon();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskMoAward(serial=" + this.getSerial() + ", renMale=" + this.getRenMale() + ", renFemale=" + this.getRenFemale() + ", xianMale=" + this.getXianMale() + ", xianFemale=" + this.getXianFemale() + ", moMale=" + this.getMoMale() + ", moFemale=" + this.getMoFemale() + ", guiMale=" + this.getGuiMale() + ", guiFemale=" + this.getGuiFemale() + ", longMale=" + this.getLongMale() + ", longFemale=" + this.getLongFemale() + ", dungeon=" + this.getDungeon() + ")";
    }
}
