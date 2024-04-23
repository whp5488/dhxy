package com.kitty.system.game.mirror.model;

import com.kitty.common.db.BaseEntity;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_fightmirror")
public class FightMirror extends BaseEntity {
  @Id(auto = false)
  private Long id;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }
  
  public void setRoleMirror(RoleMirror roleMirror) {
    this.roleMirror = roleMirror;
  }
  
  public void setPetMirror(PetMirror petMirror) {
    this.petMirror = petMirror;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public long getCreateTime() {
    return this.createTime;
  }
  
  public RoleMirror getRoleMirror() {
    return this.roleMirror;
  }
  
  public PetMirror getPetMirror() {
    return this.petMirror;
  }
  
  @Column
  private long createTime = System.currentTimeMillis();
  
  @Column
  @ColDefine(type = ColType.TEXT)
  private RoleMirror roleMirror;
  
  @Column
  @ColDefine(type = ColType.TEXT)
  private PetMirror petMirror;
}
