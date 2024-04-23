package com.kitty.system.game.market.model;

import com.kitty.common.db.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_market")
public class Market extends BaseEntity<Long> {
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
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }
  
  public void setStallMenuSerial(int stallMenuSerial) {
    this.stallMenuSerial = stallMenuSerial;
  }
  
  public void setCategory(int category) {
    this.category = category;
  }
  
  public void setPrice(long price) {
    this.price = price;
  }
  
  public void setMarketPrice(long marketPrice) {
    this.marketPrice = marketPrice;
  }
  
  public void setIsPublicity(int isPublicity) {
    this.isPublicity = isPublicity;
  }
  
  public void setExpireTime(int expireTime) {
    this.expireTime = expireTime;
  }
  
  public void setPublicityTime(int publicityTime) {
    this.publicityTime = publicityTime;
  }
  
  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }
  
  public void setAuditTime(int auditTime) {
    this.auditTime = auditTime;
  }
  
  public void setAmount(short amount) {
    this.amount = amount;
  }
  
  public void setSaleAmount(short saleAmount) {
    this.saleAmount = saleAmount;
  }
  
  public void setPara(String para) {
    this.para = para;
  }
  
  public void setFocus(List<Integer> focus) {
    this.focus = focus;
  }
  
  public void setStatus(byte status) {
    this.status = status;
  }
  
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
  
  @Column("is_publicity")
  private int isPublicity = 0;
  
  public int getIsPublicity() {
    return this.isPublicity;
  }
  
  @Column("expire_time")
  private int expireTime = 0;
  
  public int getExpireTime() {
    return this.expireTime;
  }
  
  @Column("publicity_time")
  private int publicityTime = 0;
  
  public int getPublicityTime() {
    return this.publicityTime;
  }
  
  @Column("start_time")
  private int startTime = 0;
  
  public int getStartTime() {
    return this.startTime;
  }
  
  @Column("audit_time")
  private int auditTime = 0;
  
  public int getAuditTime() {
    return this.auditTime;
  }
  
  @Column("amount")
  private short amount = 1;
  
  public short getAmount() {
    return this.amount;
  }
  
  @Column("sale_amount")
  private short saleAmount = 0;
  
  @Column("para")
  @ColDefine(customType = "longtext")
  private String para;
  
  public short getSaleAmount() {
    return this.saleAmount;
  }
  
  public String getPara() {
    return this.para;
  }
  
  @Column("focus")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<Integer> focus = new ArrayList<>();
  
  public List<Integer> getFocus() {
    return this.focus;
  }
  
  @Column("status")
  private byte status = 2;
  
  public byte getStatus() {
    return this.status;
  }
}
