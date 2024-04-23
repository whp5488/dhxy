package com.kitty.system.game.market.model;

import com.kitty.common.db.BaseEntity;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_market_deal_record")
public class MarketDealRecord extends BaseEntity<Long> {
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
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setMarketId(String marketId) {
    this.marketId = marketId;
  }
  
  public void setBuyRoleId(int buyRoleId) {
    this.buyRoleId = buyRoleId;
  }
  
  public void setSaleRoleId(int saleRoleId) {
    this.saleRoleId = saleRoleId;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setPrice(long price) {
    this.price = price;
  }
  
  public void setMarketPrice(long marketPrice) {
    this.marketPrice = marketPrice;
  }
  
  public void setAmount(short amount) {
    this.amount = amount;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setStatus(byte status) {
    this.status = status;
  }
  
  public void setInfo(String info) {
    this.info = info;
  }
  
  public MarketDealRecord() {}
  
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
  
  public MarketDealRecord(Market m, int amount, long id, int buyRoleId) {
    setMarketId(String.valueOf(m.getId()));
    setBuyRoleId(buyRoleId);
    setSaleRoleId(m.getRoleId().intValue());
    setId(Long.valueOf(id));
    setAmount((short)amount);
    setSerial(m.getSerial());
    setPrice(m.getPrice());
    setStatus((byte)2);
    setTime((int)(System.currentTimeMillis() / 1000L));
    if (m.getPara() != null)
      setInfo(m.getPara()); 
  }
}
