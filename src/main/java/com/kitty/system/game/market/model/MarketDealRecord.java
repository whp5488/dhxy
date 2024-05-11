
package com.kitty.system.game.market.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("t_market_deal_record")
public class MarketDealRecord extends BaseEntity<Long>
{
    @Id(auto = false)
    private Long id;
    @Column("market_id")
    private String marketId;
    @Column("buy_role_id")
    private int buyRoleId;
    @Column("sale_role_id")
    private int saleRoleId;
    @Column("serial")
    private int serial;
    @Column("price")
    private long price;
    @Column("market_price")
    private long marketPrice;
    @Column("amount")
    private short amount;
    @Column("time")
    private int time;
    @Column("status")
    private byte status;
    @Column("info")
    private String info;
    
    public MarketDealRecord(final Market m, final int amount, final long id, final int buyRoleId) {
        this.setMarketId(String.valueOf(m.getId()));
        this.setBuyRoleId(buyRoleId);
        this.setSaleRoleId(m.getRoleId());
        this.setId(id);
        this.setAmount((short)amount);
        this.setSerial(m.getSerial());
        this.setPrice(m.getPrice());
        this.setStatus((byte)2);
        this.setTime((int)(System.currentTimeMillis() / 1000L));
        if (m.getPara() != null) {
            this.setInfo(m.getPara());
        }
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public String getMarketId() {
        return this.marketId;
    }
    
    public int getBuyRoleId() {
        return this.buyRoleId;
    }
    
    public int getSaleRoleId() {
        return this.saleRoleId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public long getPrice() {
        return this.price;
    }
    
    public long getMarketPrice() {
        return this.marketPrice;
    }
    
    public short getAmount() {
        return this.amount;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public String getInfo() {
        return this.info;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setMarketId(final String marketId) {
        this.marketId = marketId;
    }
    
    public void setBuyRoleId(final int buyRoleId) {
        this.buyRoleId = buyRoleId;
    }
    
    public void setSaleRoleId(final int saleRoleId) {
        this.saleRoleId = saleRoleId;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPrice(final long price) {
        this.price = price;
    }
    
    public void setMarketPrice(final long marketPrice) {
        this.marketPrice = marketPrice;
    }
    
    public void setAmount(final short amount) {
        this.amount = amount;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setStatus(final byte status) {
        this.status = status;
    }
    
    public void setInfo(final String info) {
        this.info = info;
    }
    
    public MarketDealRecord() {
    }
}
