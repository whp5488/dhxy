package com.kitty.system.game.onlinemall.domain;

public class OnlineMallInfo
{
    private int serial;
    private int type;
    private String recommend;
    private int sort;
    private int price;
    private int costType;
    private int give;
    private int discount;
    private int quotaLimit;
    private int mustVip;
    private int lvLimit;
    private int zsLimit;
    
    public OnlineMallInfo() {
        this.quotaLimit = -1;
        this.mustVip = 0;
        this.lvLimit = 0;
        this.zsLimit = 0;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getType() {
        return this.type;
    }
    
    public String getRecommend() {
        return this.recommend;
    }
    
    public int getSort() {
        return this.sort;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public int getCostType() {
        return this.costType;
    }
    
    public int getGive() {
        return this.give;
    }
    
    public int getDiscount() {
        return this.discount;
    }
    
    public int getQuotaLimit() {
        return this.quotaLimit;
    }
    
    public int getMustVip() {
        return this.mustVip;
    }
    
    public int getLvLimit() {
        return this.lvLimit;
    }
    
    public int getZsLimit() {
        return this.zsLimit;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setRecommend(final String recommend) {
        this.recommend = recommend;
    }
    
    public void setSort(final int sort) {
        this.sort = sort;
    }
    
    public void setPrice(final int price) {
        this.price = price;
    }
    
    public void setCostType(final int costType) {
        this.costType = costType;
    }
    
    public void setGive(final int give) {
        this.give = give;
    }
    
    public void setDiscount(final int discount) {
        this.discount = discount;
    }
    
    public void setQuotaLimit(final int quotaLimit) {
        this.quotaLimit = quotaLimit;
    }
    
    public void setMustVip(final int mustVip) {
        this.mustVip = mustVip;
    }
    
    public void setLvLimit(final int lvLimit) {
        this.lvLimit = lvLimit;
    }
    
    public void setZsLimit(final int zsLimit) {
        this.zsLimit = zsLimit;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OnlineMallInfo)) {
            return false;
        }
        OnlineMallInfo other = (OnlineMallInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        Object this$recommend = this.getRecommend();
        Object other$recommend = other.getRecommend();
        return ((this$recommend != null) ? this$recommend.equals(other$recommend) : (other$recommend == null)) && this.getSort() == other.getSort() && this.getPrice() == other.getPrice() && this.getCostType() == other.getCostType() && this.getGive() == other.getGive() && this.getDiscount() == other.getDiscount() && this.getQuotaLimit() == other.getQuotaLimit() && this.getMustVip() == other.getMustVip() && this.getLvLimit() == other.getLvLimit() && this.getZsLimit() == other.getZsLimit();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof OnlineMallInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getType();
        Object $recommend = this.getRecommend();
        result = result * 59 + (($recommend == null) ? 43 : $recommend.hashCode());
        result = result * 59 + this.getSort();
        result = result * 59 + this.getPrice();
        result = result * 59 + this.getCostType();
        result = result * 59 + this.getGive();
        result = result * 59 + this.getDiscount();
        result = result * 59 + this.getQuotaLimit();
        result = result * 59 + this.getMustVip();
        result = result * 59 + this.getLvLimit();
        result = result * 59 + this.getZsLimit();
        return result;
    }
    
    @Override
    public String toString() {
        return "OnlineMallInfo(serial=" + this.getSerial() + ", type=" + this.getType() + ", recommend=" + this.getRecommend() + ", sort=" + this.getSort() + ", price=" + this.getPrice() + ", costType=" + this.getCostType() + ", give=" + this.getGive() + ", discount=" + this.getDiscount() + ", quotaLimit=" + this.getQuotaLimit() + ", mustVip=" + this.getMustVip() + ", lvLimit=" + this.getLvLimit() + ", zsLimit=" + this.getZsLimit() + ")";
    }
}
