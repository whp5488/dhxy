package com.kitty.system.game.activity.model.user.ca;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4EventService;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("rank_ca")
public class CaRankRecord extends BaseEntity<Long> {
  @Comment("主键")
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  @Column("r_id")
  private long rId;
  
  @Column("role_id")
  private int roleId;
  
  @Column("name")
  private String name;
  
  @Column("serial")
  private int serial;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRId(long rId) {
    this.rId = rId;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public void setGrade(int grade) {
    this.grade = grade;
  }
  
  public void setEquipScore(int equipScore) {
    this.equipScore = equipScore;
  }
  
  public void setFullRank(int fullRank) {
    this.fullRank = fullRank;
  }
  
  public String toString() {
    return "CaRankRecord(id=" + getId() + ", rId=" + getRId() + ", roleId=" + getRoleId() + ", name=" + getName() + ", serial=" + getSerial() + ", score=" + getScore() + ", grade=" + getGrade() + ", equipScore=" + getEquipScore() + ", fullRank=" + getFullRank() + ")";
  }
  
  public Long getId() {
    return this.id;
  }
  
  public long getRId() {
    return this.rId;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  @Column("score")
  private int score = 1000;
  
  public int getScore() {
    return this.score;
  }
  
  @Column("grade")
  private int grade = 0;
  
  public int getGrade() {
    return this.grade;
  }
  
  @Column("equip_score")
  private int equipScore = 0;
  
  public int getEquipScore() {
    return this.equipScore;
  }
  
  @Column("full_rank")
  private int fullRank = 0;
  
  public int getFullRank() {
    return this.fullRank;
  }
  
  public CaRankRecord(Long id, long rId, int roleId, int serial, String name) {
    this.id = id;
    this.rId = rId;
    this.roleId = roleId;
    this.serial = serial;
    this.name = name;
  }
  
  public CaRankRecord(Long id, long rId, short score) {
    this.id = id;
    this.rId = rId;
    this.score = score;
  }
  
  public CaRankRecord(long rId, short score) {
    this.rId = rId;
    this.score = score;
  }
  
  public void save() {
    Db4EventService.getInstance().add2Queue(this);
  }
  
  public CaRankRecord() {}
}
