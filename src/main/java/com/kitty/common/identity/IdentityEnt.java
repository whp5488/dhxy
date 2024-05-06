package com.kitty.common.identity;

import com.kitty.common.db.BaseEntity;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_identity")
@TableIndexes({@Index(fields = {"type"}, unique = true)})
public class IdentityEnt extends BaseEntity<Integer> {
  @Id(auto = false)
  private Integer type;
  
  @Column
  private int currId;
  
  public static IdentityEnt valueOf(int type, int seed) {
    IdentityEnt ent = new IdentityEnt();
    ent.type = type;
    ent.currId = seed;
    return ent;
  }
  
  public int getType() {
    return this.type.intValue();
  }
  
  public void setType(int type) {
    this.type = Integer.valueOf(type);
  }
  
  public int getCurrId() {
    return this.currId;
  }
  
  public void setCurrId(int currId) {
    this.currId = currId;
  }
  
  public synchronized int getNextId() {
    return this.currId++;
  }
  
  public Integer getId() {
    return this.type;
  }
  
  public void setType(Integer type) {
    this.type = type;
  }
}
