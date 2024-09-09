package com.kitty.system.game.equip.message;

import com.kitty.system.game.equip.helper.vo.EquipNeo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32775)
public class RespPreUpgradeEquip extends Message
{
    private byte type;
    private String id;
    private List<EquipNeo> list;
    private byte ask;
    
    public byte getType() {
        return this.type;
    }
    
    public String getId() {
        return this.id;
    }
    
    public List<EquipNeo> getList() {
        return this.list;
    }
    
    public byte getAsk() {
        return this.ask;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setList(final List<EquipNeo> list) {
        this.list = list;
    }
    
    public void setAsk(final byte ask) {
        this.ask = ask;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPreUpgradeEquip)) {
            return false;
        }
        RespPreUpgradeEquip other = (RespPreUpgradeEquip)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return ((this$list != null) ? this$list.equals(other$list) : (other$list == null)) && this.getAsk() == other.getAsk();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPreUpgradeEquip;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getAsk();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPreUpgradeEquip(type=" + (int)this.getType() + ", id=" + this.getId() + ", list=" + this.getList() + ", ask=" + (int)this.getAsk() + ")";
    }
    
    public RespPreUpgradeEquip() {
    }
    
    public RespPreUpgradeEquip(final byte type, final String id, final List<EquipNeo> list, final byte ask) {
        this.type = type;
        this.id = id;
        this.list = list;
        this.ask = ask;
    }
}
