package com.kitty.system.game.suit.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4CommonService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("suit")
public class Suit extends BaseEntity<Long> {
  @Comment("主键")
  @Id(auto = false)
  @Column("id")
  private long id;
  
  @Comment("玩家唯一ID")
  @Column("r_id")
  private long rId;
  
  @Comment("套装方案名称")
  @Column("name")
  private String name;
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setRId(long rId) {
    this.rId = rId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setEquip(List<Long> equip) {
    this.equip = equip;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public long getRId() {
    return this.rId;
  }
  
  public String getName() {
    return this.name;
  }
  
  @Comment("装备集合")
  @Column("equip")
  @Default("[]")
  private List<Long> equip = Collections.synchronizedList(new ArrayList<>());
  
  @Comment("状态：0.未启用 1.启用")
  @Column("state")
  private byte state;
  
  @Comment("开启时间")
  @Column("time")
  private int time;
  
  public List<Long> getEquip() {
    return this.equip;
  }
  
  public byte getState() {
    return this.state;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public Suit(long id, long rId, String name, byte state) {
    this.id = id;
    this.rId = rId;
    this.time = (int)(System.currentTimeMillis() / 1000L);
    this.name = name;
    this.state = state;
  }
  
  public Long getId() {
    return Long.valueOf(this.id);
  }
  
  public void save() {
    Db4CommonService.getInstance().add2Queue(this);
  }
  
  public Suit() {}
}
