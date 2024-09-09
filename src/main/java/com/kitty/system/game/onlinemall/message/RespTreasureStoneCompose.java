package com.kitty.system.game.onlinemall.message;

import java.util.ArrayList;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8413)
public class RespTreasureStoneCompose extends Message
{
    private int serial;
    private String id;
    private List<FieldProp> list;
    
    public RespTreasureStoneCompose() {
        this.list = new ArrayList<>();
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getId() {
        return this.id;
    }
    
    public List<FieldProp> getList() {
        return this.list;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setList(final List<FieldProp> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespTreasureStoneCompose)) {
            return false;
        }
        RespTreasureStoneCompose other = (RespTreasureStoneCompose)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTreasureStoneCompose;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTreasureStoneCompose(serial=" + this.getSerial() + ", id=" + this.getId() + ", list=" + this.getList() + ")";
    }
}
