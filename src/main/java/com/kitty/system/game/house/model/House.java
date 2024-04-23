package com.kitty.system.game.house.model;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4CommonService;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("house")
public class House extends BaseEntity<Long> {
  @Comment("主键")
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  @Comment("角色ID")
  @Column("r_id")
  private long rId;
  
  @Comment("房屋名称")
  @Column("name")
  private String name;
  
  @Comment("主题编号")
  @Column("motif")
  private int motif;
  
  @Comment("使用状态：0.否 1.是")
  @Column("state")
  private byte state;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRId(long rId) {
    this.rId = rId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setMotif(int motif) {
    this.motif = motif;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public long getRId() {
    return this.rId;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getMotif() {
    return this.motif;
  }
  
  public byte getState() {
    return this.state;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public House() {}
  
  public House(Long id, long rId, String name, int motif, byte state) {
    this.id = id;
    this.rId = rId;
    this.name = name;
    this.motif = motif;
    this.state = state;
  }
  
  public void save() {
    Db4CommonService.getInstance().add2Queue(this);
  }
}
