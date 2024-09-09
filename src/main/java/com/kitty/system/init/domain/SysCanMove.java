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
    
    public SysCanMove() {
    }
    
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
        SysCanMove other = (SysCanMove)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$canMove = this.getCanMove();
        Object other$canMove = other.getCanMove();
        return ((this$canMove != null) ? this$canMove.equals(other$canMove) : (other$canMove == null)) && this.getTab() == other.getTab();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysCanMove;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $canMove = this.getCanMove();
        result = result * 59 + (($canMove == null) ? 43 : $canMove.hashCode());
        result = result * 59 + this.getTab();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysCanMove(serial=" + this.getSerial() + ", canMove=" + this.getCanMove() + ", tab=" + (int)this.getTab() + ")";
    }
}
