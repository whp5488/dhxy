package com.kitty.system.game.equip.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4PetEquipService;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import java.util.List;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("pet_equip")
public class PetEquip extends BaseEntity<Long> {
  @Id(auto = false)
  private Long id;
  
  @Column("r_id")
  private long rId;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRId(long rId) {
    this.rId = rId;
  }
  
  public void setPId(long pId) {
    this.pId = pId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setBd(int bd) {
    this.bd = bd;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setPosition(int position) {
    this.position = position;
  }
  
  public void setProductOrder(byte productOrder) {
    this.productOrder = productOrder;
  }
  
  public void setField(List<FiedEquip> field) {
    this.field = field;
  }
  
  public void setPositionNum(short positionNum) {
    this.positionNum = positionNum;
  }
  
  public void setLock(byte lock) {
    this.lock = lock;
  }
  
  public String toString() {
    return "PetEquip(id=" + getId() + ", rId=" + getRId() + ", pId=" + getPId() + ", name=" + getName() + ", bd=" + getBd() + ", serial=" + getSerial() + ", position=" + getPosition() + ", productOrder=" + getProductOrder() + ", field=" + getField() + ", positionNum=" + getPositionNum() + ", lock=" + getLock() + ")";
  }
  
  public Long getId() {
    return this.id;
  }
  
  public long getRId() {
    return this.rId;
  }
  
  @Column("p_id")
  private long pId = 0L;
  
  @Column("name")
  private String name;
  
  @Column("bd")
  private int bd;
  
  @Column("serial")
  private int serial;
  
  @Column("position")
  private int position;
  
  @Column("product_order")
  private byte productOrder;
  
  public long getPId() {
    return this.pId;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getBd() {
    return this.bd;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public byte getProductOrder() {
    return this.productOrder;
  }
  
  @Column("field")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<FiedEquip> field = new ArrayList<>();
  
  public List<FiedEquip> getField() {
    return this.field;
  }
  
  @Column("position_num")
  private short positionNum = 2;
  
  public short getPositionNum() {
    return this.positionNum;
  }
  
  @Column("lock_state")
  private byte lock = 0;
  
  public byte getLock() {
    return this.lock;
  }
  
  public PetEquip(String name, int bd, int serial, int position, byte productOrder, List<FiedEquip> field) {
    this.name = name;
    this.bd = bd;
    this.serial = serial;
    this.position = position;
    this.productOrder = productOrder;
    this.field = field;
  }
  
  public PetEquip(Long id, long rId, long pId, String name, int bd, int serial, int position, byte productOrder, List<FiedEquip> field, short positionNum, byte lock) {
    this.id = id;
    this.rId = rId;
    this.pId = pId;
    this.name = name;
    this.bd = bd;
    this.serial = serial;
    this.position = position;
    this.productOrder = productOrder;
    this.field = field;
    this.positionNum = positionNum;
    this.lock = lock;
  }
  
  public void save() {
    Db4PetEquipService.getInstance().add2Queue(this);
  }
  
  public PetEquip() {}
}
