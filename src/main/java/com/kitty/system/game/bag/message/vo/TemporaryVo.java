package com.kitty.system.game.bag.message.vo;

import java.io.Serializable;

public class TemporaryVo implements Serializable
{
    private String id;
    private Integer serial;
    private Integer num;
    
    public TemporaryVo() {
    }
    
    public String getId() {
        return this.id;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getNum() {
        return this.num;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setNum(final Integer num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TemporaryVo)) {
            return false;
        }
        TemporaryVo other = (TemporaryVo)o;
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
        Object this$num = this.getNum();
        Object other$num = other.getNum();
        return (this$num != null) ? this$num.equals(other$num) : (other$num == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TemporaryVo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "TemporaryVo(id=" + this.getId() + ", serial=" + this.getSerial() + ", num=" + this.getNum() + ")";
    }
}
