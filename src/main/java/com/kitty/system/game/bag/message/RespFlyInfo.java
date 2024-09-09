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
        RespFlyInfo other = (RespFlyInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPos() != other.getPos()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        Object this$goods = this.getGoods();
        Object other$goods = other.getGoods();
        if (!((this$goods != null) ? this$goods.equals(other$goods) : (other$goods == null))) {
            return false;
        }
        Object this$equip = this.getEquip();
        Object other$equip = other.getEquip();
        if (!((this$equip != null) ? this$equip.equals(other$equip) : (other$equip == null))) {
            return false;
        }
        Object this$roleTianFuClient = this.getRoleTianFuClient();
        Object other$roleTianFuClient = other.getRoleTianFuClient();
        return (this$roleTianFuClient != null) ? this$roleTianFuClient.equals(other$roleTianFuClient) : (other$roleTianFuClient == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFlyInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPos();
        result = result * 59 + this.getType();
        Object $goods = this.getGoods();
        result = result * 59 + (($goods == null) ? 43 : $goods.hashCode());
        Object $equip = this.getEquip();
        result = result * 59 + (($equip == null) ? 43 : $equip.hashCode());
        Object $roleTianFuClient = this.getRoleTianFuClient();
        result = result * 59 + (($roleTianFuClient == null) ? 43 : $roleTianFuClient.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFlyInfo(pos=" + (int)this.getPos() + ", type=" + (int)this.getType() + ", goods=" + this.getGoods() + ", equip=" + this.getEquip() + ", roleTianFuClient=" + this.getRoleTianFuClient() + ")";
    }
}
