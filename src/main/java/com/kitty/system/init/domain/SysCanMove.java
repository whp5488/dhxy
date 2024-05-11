
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_can_move")
public class SysCanMove implements Serializable
{
    @Column("serial")
    private int serial;
    @Column("can_move")
    @ColDefine(customType = "longtext")
    private String canMove;
    @Column("tab")
    private byte tab;
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getCanMove() {
        return this.canMove;
    }
    
    public byte getTab() {
        return this.tab;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setCanMove(final String canMove) {
        this.canMove = canMove;
    }
    
    public void setTab(final byte tab) {
        this.tab = tab;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysCanMove)) {
            return false;
        }
        final SysCanMove other = (SysCanMove)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$canMove = this.getCanMove();
        final Object other$canMove = other.getCanMove();
        if (this$canMove == null) {
            if (other$canMove == null) {
                return this.getTab() == other.getTab();
            }
        }
        else if (this$canMove.equals(other$canMove)) {
            return this.getTab() == other.getTab();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysCanMove;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $canMove = this.getCanMove();
        result = result * 59 + (($canMove == null) ? 43 : $canMove.hashCode());
        result = result * 59 + this.getTab();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysCanMove(serial=" + this.getSerial() + ", canMove=" + this.getCanMove() + ", tab=" + this.getTab() + ")";
    }
}
