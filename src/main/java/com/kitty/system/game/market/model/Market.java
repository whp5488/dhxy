package com.kitty.system.game.market.model;

import java.util.ArrayList;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("t_market")
public class Market extends BaseEntity<Long>
{
    @Id(auto = false)
    private Long id;
    @Column("serial")
    private int serial;
    @Column("role_id")
    private Integer roleId;
    @Column("stall_menu_serial")
    private int stallMenuSerial;
    @Column("category")
    private int category;
    @Column("price")
    private long price;
    @Column("market_price")
    private long marketPrice;
    @Column("is_publicity")
    private int isPublicity;
    @Column("expire_time")
    private int expireTime;
    @Column("publicity_time")
    private int publicityTime;
    @Column("start_time")
    private int startTime;
    @Column("audit_time")
    private int auditTime;
    @Column("amount")
    private short amount;
    @Column("sale_amount")
    private short saleAmount;
    @Column("para")
    @ColDefine(customType = "longtext")
    private String para;
    @Column("focus")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<Integer> focus;
    @Column("status")
    private byte status;
    
    public Market() {
        this.isPublicity = 0;
        this.expireTime = 0;
        this.publicityTime = 0;
        this.startTime = 0;
        this.auditTime = 0;
        this.amount = 1;
        this.saleAmount = 0;
        this.focus = new ArrayList<>();
        this.status = 2;
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public int getStallMenuSerial() {
        return this.stallMenuSerial;
    }
    
    public int getCategory() {
        return this.category;
    }
    
    public long getPrice() {
        return this.price;
    }
    
    public long getMarketPrice() {
        return this.marketPrice;
    }
    
    public int getIsPublicity() {
        return this.isPublicity;
    }
    
    public int getExpireTime() {
        return this.expireTime;
    }
    
    public int getPublicityTime() {
        return this.publicityTime;
    }
    
    public int getStartTime() {
        return this.startTime;
    }
    
    public int getAuditTime() {
        return this.auditTime;
    }
    
    public short getAmount() {
        return this.amount;
    }
    
    public short getSaleAmount() {
        return this.saleAmount;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public List<Integer> getFocus() {
        return this.focus;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public void setStallMenuSerial(final int stallMenuSerial) {
        this.stallMenuSerial = stallMenuSerial;
    }
    
    public void setCategory(final int category) {
        this.category = category;
    }
    
    public void setPrice(final long price) {
        this.price = price;
    }
    
    public void setMarketPrice(final long marketPrice) {
        this.marketPrice = marketPrice;
    }
    
    public void setIsPublicity(final int isPublicity) {
        this.isPublicity = isPublicity;
    }
    
    public void setExpireTime(final int expireTime) {
        this.expireTime = expireTime;
    }
    
    public void setPublicityTime(final int publicityTime) {
        this.publicityTime = publicityTime;
    }
    
    public void setStartTime(final int startTime) {
        this.startTime = startTime;
    }
    
    public void setAuditTime(final int auditTime) {
        this.auditTime = auditTime;
    }
    
    public void setAmount(final short amount) {
        this.amount = amount;
    }
    
    public void setSaleAmount(final short saleAmount) {
        this.saleAmount = saleAmount;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    public void setFocus(final List<Integer> focus) {
        this.focus = focus;
    }
    
    public void setStatus(final byte status) {
        this.status = status;
    }
}
