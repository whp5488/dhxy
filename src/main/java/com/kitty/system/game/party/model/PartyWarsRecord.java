package com.kitty.system.game.party.model;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4EventService;
import java.beans.ConstructorProperties;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("party_wars_record")
public class PartyWarsRecord extends BaseEntity<Long> {
  @Id(auto = false)
  private Long id;
  
  @Column("serial")
  @Comment("帮派编号")
  private int serial;
  
  @Column("time")
  @Comment("日期")
  private int time;
  
  @Column("combat_results")
  @Comment("战斗结果：0.失败 1.胜利 2.轮空 3.未比赛")
  private byte combatResults;
  
  @Column("enemy_serial")
  @Comment("敌方帮派编号 ")
  private int enemySerial;
  
  @Column("enemy_name")
  @Comment("敌方帮派名称 ")
  private String enemyName;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setCombatResults(byte combatResults) {
    this.combatResults = combatResults;
  }
  
  public void setEnemySerial(int enemySerial) {
    this.enemySerial = enemySerial;
  }
  
  public void setEnemyName(String enemyName) {
    this.enemyName = enemyName;
  }
  
  public void setEnemyNum(int enemyNum) {
    this.enemyNum = enemyNum;
  }
  
  public void setSideNum(int sideNum) {
    this.sideNum = sideNum;
  }
  
  @ConstructorProperties({"id", "serial", "time", "combatResults", "enemySerial", "enemyName", "enemyNum", "sideNum"})
  public PartyWarsRecord(Long id, int serial, int time, byte combatResults, int enemySerial, String enemyName, int enemyNum, int sideNum) {
    this.id = id;
    this.serial = serial;
    this.time = time;
    this.combatResults = combatResults;
    this.enemySerial = enemySerial;
    this.enemyName = enemyName;
    this.enemyNum = enemyNum;
    this.sideNum = sideNum;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public byte getCombatResults() {
    return this.combatResults;
  }
  
  public int getEnemySerial() {
    return this.enemySerial;
  }
  
  public String getEnemyName() {
    return this.enemyName;
  }
  
  @Column("enemy_num")
  @Comment("敌方帮派参战人数")
  private int enemyNum = 0;
  
  public int getEnemyNum() {
    return this.enemyNum;
  }
  
  @Column("side_num")
  @Comment("己方帮派参战人数")
  private int sideNum = 0;
  
  public int getSideNum() {
    return this.sideNum;
  }
  
  public void save() {
    Db4EventService.getInstance().add2Queue(this);
  }
  
  public PartyWarsRecord() {}
}
