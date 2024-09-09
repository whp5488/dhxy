package com.kitty.system.game.horse.message;

import com.kitty.system.game.horse.message.vo.MountBase;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45079)
public class RespPreUpgradeHorse extends Message
{
    private String id;
    private List<MountBase> list;
    private byte ask;
    
    public String getId() {
        return this.id;
    }
    
    public List<MountBase> getList() {
        return this.list;
    }
    
    public byte getAsk() {
        return this.ask;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setList(final List<MountBase> list) {
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
        if (!(o instanceof RespPreUpgradeHorse)) {
            return false;
        }
        RespPreUpgradeHorse other = (RespPreUpgradeHorse)o;
        if (!other.canEqual(this)) {
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
        return other instanceof RespPreUpgradeHorse;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getAsk();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPreUpgradeHorse(id=" + this.getId() + ", list=" + this.getList() + ", ask=" + (int)this.getAsk() + ")";
    }
    
    public RespPreUpgradeHorse() {
    }
    
    public RespPreUpgradeHorse(final String id, final List<MountBase> list, final byte ask) {
        this.id = id;
        this.list = list;
        this.ask = ask;
    }
}
