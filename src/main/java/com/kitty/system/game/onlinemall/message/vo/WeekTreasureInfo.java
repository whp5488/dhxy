
package com.kitty.system.game.onlinemall.message.vo;

import com.kitty.system.game.activity.domain.WeekTreasure;
import java.io.Serializable;

public class WeekTreasureInfo implements Serializable
{
    private String id;
    private String rId;
    private int serial;
    private short share;
    
    public WeekTreasureInfo() {
        this.serial = 0;
        this.share = 0;
    }
    
    public WeekTreasureInfo(final WeekTreasure obj) {
        this.serial = 0;
        this.share = 0;
        this.id = String.valueOf(obj.getId());
        this.rId = String.valueOf(obj.getRId());
        this.serial = obj.getSerial();
        this.share = obj.getShare();
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getRId() {
        return this.rId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public short getShare() {
        return this.share;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setRId(final String rId) {
        this.rId = rId;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setShare(final short share) {
        this.share = share;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WeekTreasureInfo)) {
            return false;
        }
        final WeekTreasureInfo other = (WeekTreasureInfo)o;
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
        final Object this$rId = this.getRId();
        final Object other$rId = other.getRId();
        if (this$rId == null) {
            if (other$rId == null) {
                return this.getSerial() == other.getSerial() && this.getShare() == other.getShare();
            }
        }
        else if (this$rId.equals(other$rId)) {
            return this.getSerial() == other.getSerial() && this.getShare() == other.getShare();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof WeekTreasureInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $rId = this.getRId();
        result = result * 59 + (($rId == null) ? 43 : $rId.hashCode());
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getShare();
        return result;
    }
    
    @Override
    public String toString() {
        return "WeekTreasureInfo(id=" + this.getId() + ", rId=" + this.getRId() + ", serial=" + this.getSerial() + ", share=" + this.getShare() + ")";
    }
}
