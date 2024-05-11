
package com.kitty.system.game.equip.message;

import java.beans.ConstructorProperties;
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
        final RespPreUpgradeEquip other = (RespPreUpgradeEquip)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0078: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0078;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return this.getAsk() == other.getAsk();
            }
        }
        else if (this$list.equals(other$list)) {
            return this.getAsk() == other.getAsk();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPreUpgradeEquip;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getAsk();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPreUpgradeEquip(type=" + this.getType() + ", id=" + this.getId() + ", list=" + this.getList() + ", ask=" + this.getAsk() + ")";
    }
    
    public RespPreUpgradeEquip() {
    }
    
    @ConstructorProperties({ "type", "id", "list", "ask" })
    public RespPreUpgradeEquip(final byte type, final String id, final List<EquipNeo> list, final byte ask) {
        this.type = type;
        this.id = id;
        this.list = list;
        this.ask = ask;
    }
}
