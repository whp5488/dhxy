
package com.kitty.system.game.bag.message;

import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import com.kitty.system.game.equip.domain.EquipEasy;
import com.kitty.system.game.role.domain.Goods;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65525)
public class RespFlyInfo extends Message
{
    private byte pos;
    private byte type;
    private Goods goods;
    private EquipEasy equip;
    private RoleTianFuClient roleTianFuClient;
    
    public RespFlyInfo() {
        this.pos = 0;
        this.type = 0;
    }
    
    public byte getPos() {
        return this.pos;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public Goods getGoods() {
        return this.goods;
    }
    
    public EquipEasy getEquip() {
        return this.equip;
    }
    
    public RoleTianFuClient getRoleTianFuClient() {
        return this.roleTianFuClient;
    }
    
    public void setPos(final byte pos) {
        this.pos = pos;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setGoods(final Goods goods) {
        this.goods = goods;
    }
    
    public void setEquip(final EquipEasy equip) {
        this.equip = equip;
    }
    
    public void setRoleTianFuClient(final RoleTianFuClient roleTianFuClient) {
        this.roleTianFuClient = roleTianFuClient;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespFlyInfo)) {
            return false;
        }
        final RespFlyInfo other = (RespFlyInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPos() != other.getPos()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        final Object this$goods = this.getGoods();
        final Object other$goods = other.getGoods();
        Label_0091: {
            if (this$goods == null) {
                if (other$goods == null) {
                    break Label_0091;
                }
            }
            else if (this$goods.equals(other$goods)) {
                break Label_0091;
            }
            return false;
        }
        final Object this$equip = this.getEquip();
        final Object other$equip = other.getEquip();
        Label_0128: {
            if (this$equip == null) {
                if (other$equip == null) {
                    break Label_0128;
                }
            }
            else if (this$equip.equals(other$equip)) {
                break Label_0128;
            }
            return false;
        }
        final Object this$roleTianFuClient = this.getRoleTianFuClient();
        final Object other$roleTianFuClient = other.getRoleTianFuClient();
        if (this$roleTianFuClient == null) {
            if (other$roleTianFuClient == null) {
                return true;
            }
        }
        else if (this$roleTianFuClient.equals(other$roleTianFuClient)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFlyInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPos();
        result = result * 59 + this.getType();
        final Object $goods = this.getGoods();
        result = result * 59 + (($goods == null) ? 43 : $goods.hashCode());
        final Object $equip = this.getEquip();
        result = result * 59 + (($equip == null) ? 43 : $equip.hashCode());
        final Object $roleTianFuClient = this.getRoleTianFuClient();
        result = result * 59 + (($roleTianFuClient == null) ? 43 : $roleTianFuClient.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFlyInfo(pos=" + this.getPos() + ", type=" + this.getType() + ", goods=" + this.getGoods() + ", equip=" + this.getEquip() + ", roleTianFuClient=" + this.getRoleTianFuClient() + ")";
    }
}
