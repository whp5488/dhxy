
package com.kitty.system.game.partner.enter;

import java.beans.ConstructorProperties;

public class PartnerFiedValue
{
    private int uid;
    private int typeId;
    private byte relive;
    private short level;
    private String exp;
    private byte isRecruit;
    private byte isFight;
    private byte partnerOrder;
    private int dsId;
    
    public int getUid() {
        return this.uid;
    }
    
    public int getTypeId() {
        return this.typeId;
    }
    
    public byte getRelive() {
        return this.relive;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public String getExp() {
        return this.exp;
    }
    
    public byte getIsRecruit() {
        return this.isRecruit;
    }
    
    public byte getIsFight() {
        return this.isFight;
    }
    
    public byte getPartnerOrder() {
        return this.partnerOrder;
    }
    
    public int getDsId() {
        return this.dsId;
    }
    
    public void setUid(final int uid) {
        this.uid = uid;
    }
    
    public void setTypeId(final int typeId) {
        this.typeId = typeId;
    }
    
    public void setRelive(final byte relive) {
        this.relive = relive;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setExp(final String exp) {
        this.exp = exp;
    }
    
    public void setIsRecruit(final byte isRecruit) {
        this.isRecruit = isRecruit;
    }
    
    public void setIsFight(final byte isFight) {
        this.isFight = isFight;
    }
    
    public void setPartnerOrder(final byte partnerOrder) {
        this.partnerOrder = partnerOrder;
    }
    
    public void setDsId(final int dsId) {
        this.dsId = dsId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PartnerFiedValue)) {
            return false;
        }
        final PartnerFiedValue other = (PartnerFiedValue)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getUid() != other.getUid()) {
            return false;
        }
        if (this.getTypeId() != other.getTypeId()) {
            return false;
        }
        if (this.getRelive() != other.getRelive()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        final Object this$exp = this.getExp();
        final Object other$exp = other.getExp();
        if (this$exp == null) {
            if (other$exp == null) {
                return this.getIsRecruit() == other.getIsRecruit() && this.getIsFight() == other.getIsFight() && this.getPartnerOrder() == other.getPartnerOrder() && this.getDsId() == other.getDsId();
            }
        }
        else if (this$exp.equals(other$exp)) {
            return this.getIsRecruit() == other.getIsRecruit() && this.getIsFight() == other.getIsFight() && this.getPartnerOrder() == other.getPartnerOrder() && this.getDsId() == other.getDsId();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PartnerFiedValue;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getUid();
        result = result * 59 + this.getTypeId();
        result = result * 59 + this.getRelive();
        result = result * 59 + this.getLevel();
        final Object $exp = this.getExp();
        result = result * 59 + (($exp == null) ? 43 : $exp.hashCode());
        result = result * 59 + this.getIsRecruit();
        result = result * 59 + this.getIsFight();
        result = result * 59 + this.getPartnerOrder();
        result = result * 59 + this.getDsId();
        return result;
    }
    
    @Override
    public String toString() {
        return "PartnerFiedValue(uid=" + this.getUid() + ", typeId=" + this.getTypeId() + ", relive=" + this.getRelive() + ", level=" + this.getLevel() + ", exp=" + this.getExp() + ", isRecruit=" + this.getIsRecruit() + ", isFight=" + this.getIsFight() + ", partnerOrder=" + this.getPartnerOrder() + ", dsId=" + this.getDsId() + ")";
    }
    
    public PartnerFiedValue() {
    }
    
    @ConstructorProperties({ "uid", "typeId", "relive", "level", "exp", "isRecruit", "isFight", "partnerOrder", "dsId" })
    public PartnerFiedValue(final int uid, final int typeId, final byte relive, final short level, final String exp, final byte isRecruit, final byte isFight, final byte partnerOrder, final int dsId) {
        this.uid = uid;
        this.typeId = typeId;
        this.relive = relive;
        this.level = level;
        this.exp = exp;
        this.isRecruit = isRecruit;
        this.isFight = isFight;
        this.partnerOrder = partnerOrder;
        this.dsId = dsId;
    }
}
