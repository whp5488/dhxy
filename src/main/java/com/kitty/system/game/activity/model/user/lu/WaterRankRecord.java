package com.kitty.system.game.activity.model.user.lu;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4EventService;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("rank_water")
public class WaterRankRecord extends BaseEntity<Long> {
  @Comment("主键")
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  @Column("r_id")
  private long rId;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRId(long rId) {
    this.rId = rId;
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
  
  public void setLeader(long leader) {
    this.leader = leader;
  }
  
  public String toString() {
    return "WaterRankRecord(id=" + getId() + ", rId=" + getRId() + ", score=" + getScore() + ", grade=" + getGrade() + ", equipScore=" + getEquipScore() + ", leader=" + getLeader() + ")";
  }
  
  public Long getId() {
    return this.id;
  }
  
  public long getRId() {
    return this.rId;
  }
  
  @Column("score")
  private int score = 0;
  
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
  
  @Column("leader")
  private long leader = 0L;
  
  public long getLeader() {
    return this.leader;
  }
  
  public WaterRankRecord(Long id, long rId) {
    this.id = id;
    this.rId = rId;
  }
  
  public WaterRankRecord(Long id, long rId, int score, int grade, int equipScore, long leader) {
    this.id = id;
    this.rId = rId;
    this.score = score;
    this.grade = grade;
    this.equipScore = equipScore;
    this.leader = leader;
  }
  
  public WaterRankRecord(long rId, int score, int grade, int equipScore, long leader) {
    this.rId = rId;
    this.score = score;
    this.grade = grade;
    this.equipScore = equipScore;
    this.leader = leader;
  }
  
  public void save() {
    Db4EventService.getInstance().add2Queue(this);
  }
  
  public WaterRankRecord() {}
}
