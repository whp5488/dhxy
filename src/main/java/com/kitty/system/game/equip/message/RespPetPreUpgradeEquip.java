
package com.kitty.system.game.equip.message;

import java.beans.ConstructorProperties;
import com.kitty.system.game.equip.helper.vo.PetEquipNeo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32777)
public class RespPetPreUpgradeEquip extends Message
{
    private List<PetEquipNeo> list;
    private byte type;
    private byte ask;
    private byte grade;
    
    public List<PetEquipNeo> getList() {
        return this.list;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public byte getAsk() {
        return this.ask;
    }
    
    public byte getGrade() {
        return this.grade;
    }
    
    public void setList(final List<PetEquipNeo> list) {
        this.list = list;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setAsk(final byte ask) {
        this.ask = ask;
    }
    
    public void setGrade(final byte grade) {
        this.grade = grade;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPetPreUpgradeEquip)) {
            return false;
        }
        final RespPetPreUpgradeEquip other = (RespPetPreUpgradeEquip)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return this.getType() == other.getType() && this.getAsk() == other.getAsk() && this.getGrade() == other.getGrade();
            }
        }
        else if (this$list.equals(other$list)) {
            return this.getType() == other.getType() && this.getAsk() == other.getAsk() && this.getGrade() == other.getGrade();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPetPreUpgradeEquip;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getType();
        result = result * 59 + this.getAsk();
        result = result * 59 + this.getGrade();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPetPreUpgradeEquip(list=" + this.getList() + ", type=" + this.getType() + ", ask=" + this.getAsk() + ", grade=" + this.getGrade() + ")";
    }
    
    public RespPetPreUpgradeEquip() {
    }
    
    @ConstructorProperties({ "list", "type", "ask", "grade" })
    public RespPetPreUpgradeEquip(final List<PetEquipNeo> list, final byte type, final byte ask, final byte grade) {
        this.list = list;
        this.type = type;
        this.ask = ask;
        this.grade = grade;
    }
}
