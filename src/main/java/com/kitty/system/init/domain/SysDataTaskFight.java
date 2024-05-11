
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_task_fight")
public class SysDataTaskFight implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("战斗编号")
    @Column("serial")
    private int serial;
    @Comment("任务编号")
    @Column("task_serial")
    private int taskSerial;
    @Comment("war_role中的编号")
    @Column("tar_npc_data")
    private int tarNpcData;
    @Comment("失败事件编号")
    @Column("lose_event")
    @Default("[]")
    private List<Integer> loseEvent;
    @Comment("胜利事件编号")
    @Column("win_event")
    @Default("[]")
    private List<Integer> winEvent;
    @Comment("侠义/好心值")
    @Column("chivalry")
    private Integer chivalry;
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getTaskSerial() {
        return this.taskSerial;
    }
    
    public int getTarNpcData() {
        return this.tarNpcData;
    }
    
    public List<Integer> getLoseEvent() {
        return this.loseEvent;
    }
    
    public List<Integer> getWinEvent() {
        return this.winEvent;
    }
    
    public Integer getChivalry() {
        return this.chivalry;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setTaskSerial(final int taskSerial) {
        this.taskSerial = taskSerial;
    }
    
    public void setTarNpcData(final int tarNpcData) {
        this.tarNpcData = tarNpcData;
    }
    
    public void setLoseEvent(final List<Integer> loseEvent) {
        this.loseEvent = loseEvent;
    }
    
    public void setWinEvent(final List<Integer> winEvent) {
        this.winEvent = winEvent;
    }
    
    public void setChivalry(final Integer chivalry) {
        this.chivalry = chivalry;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskFight)) {
            return false;
        }
        final SysDataTaskFight other = (SysDataTaskFight)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getTaskSerial() != other.getTaskSerial()) {
            return false;
        }
        if (this.getTarNpcData() != other.getTarNpcData()) {
            return false;
        }
        final Object this$loseEvent = this.getLoseEvent();
        final Object other$loseEvent = other.getLoseEvent();
        Label_0104: {
            if (this$loseEvent == null) {
                if (other$loseEvent == null) {
                    break Label_0104;
                }
            }
            else if (this$loseEvent.equals(other$loseEvent)) {
                break Label_0104;
            }
            return false;
        }
        final Object this$winEvent = this.getWinEvent();
        final Object other$winEvent = other.getWinEvent();
        Label_0141: {
            if (this$winEvent == null) {
                if (other$winEvent == null) {
                    break Label_0141;
                }
            }
            else if (this$winEvent.equals(other$winEvent)) {
                break Label_0141;
            }
            return false;
        }
        final Object this$chivalry = this.getChivalry();
        final Object other$chivalry = other.getChivalry();
        if (this$chivalry == null) {
            if (other$chivalry == null) {
                return true;
            }
        }
        else if (this$chivalry.equals(other$chivalry)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskFight;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getTaskSerial();
        result = result * 59 + this.getTarNpcData();
        final Object $loseEvent = this.getLoseEvent();
        result = result * 59 + (($loseEvent == null) ? 43 : $loseEvent.hashCode());
        final Object $winEvent = this.getWinEvent();
        result = result * 59 + (($winEvent == null) ? 43 : $winEvent.hashCode());
        final Object $chivalry = this.getChivalry();
        result = result * 59 + (($chivalry == null) ? 43 : $chivalry.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskFight(serial=" + this.getSerial() + ", taskSerial=" + this.getTaskSerial() + ", tarNpcData=" + this.getTarNpcData() + ", loseEvent=" + this.getLoseEvent() + ", winEvent=" + this.getWinEvent() + ", chivalry=" + this.getChivalry() + ")";
    }
}
