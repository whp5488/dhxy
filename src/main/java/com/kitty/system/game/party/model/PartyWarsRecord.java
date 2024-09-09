package com.kitty.system.game.party.model;

import com.kitty.common.db.Db4EventService;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("party_wars_record")
public class PartyWarsRecord extends BaseEntity<Long>
{
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
    @Column("enemy_num")
    @Comment("敌方帮派参战人数")
    private int enemyNum;
    @Column("side_num")
    @Comment("己方帮派参战人数")
    private int sideNum;
    
    public void save() {
        Db4EventService.getInstance().add2Queue(this);
    }
    
    @Override
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
    
    public int getEnemyNum() {
        return this.enemyNum;
    }
    
    public int getSideNum() {
        return this.sideNum;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setCombatResults(final byte combatResults) {
        this.combatResults = combatResults;
    }
    
    public void setEnemySerial(final int enemySerial) {
        this.enemySerial = enemySerial;
    }
    
    public void setEnemyName(final String enemyName) {
        this.enemyName = enemyName;
    }
    
    public void setEnemyNum(final int enemyNum) {
        this.enemyNum = enemyNum;
    }
    
    public void setSideNum(final int sideNum) {
        this.sideNum = sideNum;
    }
    
    public PartyWarsRecord(final Long id, final int serial, final int time, final byte combatResults, final int enemySerial, final String enemyName, final int enemyNum, final int sideNum) {
        this.enemyNum = 0;
        this.sideNum = 0;
        this.id = id;
        this.serial = serial;
        this.time = time;
        this.combatResults = combatResults;
        this.enemySerial = enemySerial;
        this.enemyName = enemyName;
        this.enemyNum = enemyNum;
        this.sideNum = sideNum;
    }
    
    public PartyWarsRecord() {
        this.enemyNum = 0;
        this.sideNum = 0;
    }
}
