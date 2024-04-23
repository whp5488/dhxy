package com.kitty.system.game.bag.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.DbBagGoodsService;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.util.NutMap;

@Table("bag_goods")
public class BagGoods extends BaseEntity<Long> {
  @Comment("主键")
  @Id(auto = false)
  @Column("id")
  private long id;
  
  @Comment("玩家唯一ID")
  @Column("r_id")
  private long rId;
  
  @Comment("物品编号")
  @Column("serial")
  private int serial;
  
  @Comment("背包位置")
  @Column("pos")
  private int pos;
  
  @Comment("其他属性")
  @Column("rests")
  private NutMap rests = new NutMap();
  
  @Comment("特殊属性")
  @Column("special")
  private NutMap special = new NutMap();
  
  public Long getId() {
    return Long.valueOf(this.id);
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public long getrId() {
    return this.rId;
  }
  
  public void setrId(long rId) {
    this.rId = rId;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public int getPos() {
    return this.pos;
  }
  
  public void setPos(int pos) {
    this.pos = pos;
  }
  
  public NutMap getRests() {
    return this.rests;
  }
  
  public void setRests(NutMap rests) {
    this.rests = rests;
  }
  
  public NutMap getSpecial() {
    return this.special;
  }
  
  public void setSpecial(NutMap special) {
    this.special = special;
  }
  
  public int getNum() {
    int num = 0;
    NutMap rests = getRests();
    for (String key : rests.keySet())
      num += rests.getInt(key, 0); 
    return num;
  }
  
  public BagGoods(long id, long rId, int serial, int pos) {
    this.id = id;
    this.rId = rId;
    this.serial = serial;
    this.pos = pos;
  }
  
  public BagGoods(long id, long rId, int serial, int pos, NutMap rests) {
    this.id = id;
    this.rId = rId;
    this.serial = serial;
    this.pos = pos;
    this.rests = rests;
  }
  
  public void save() {
    DbBagGoodsService.getInstance().add2Queue(this);
  }
  
  public BagGoods() {}
}
