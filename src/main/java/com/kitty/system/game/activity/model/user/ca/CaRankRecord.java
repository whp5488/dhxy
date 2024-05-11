
package com.kitty.system.game.activity.model.user.ca;

import com.kitty.common.db.Db4EventService;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("rank_ca")
public class CaRankRecord extends BaseEntity<Long>
{
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
    @Column("score")
    private int score;
    @Column("grade")
    private int grade;
    @Column("equip_score")
    private int equipScore;
    @Column("full_rank")
    private int fullRank;
    
    public CaRankRecord() {
        this.score = 1000;
        this.grade = 0;
        this.equipScore = 0;
        this.fullRank = 0;
    }
    
    public CaRankRecord(final Long id, final long rId, final int roleId, final int serial, final String name) {
        this.score = 1000;
        this.grade = 0;
        this.equipScore = 0;
        this.fullRank = 0;
        this.id = id;
        this.rId = rId;
        this.roleId = roleId;
        this.serial = serial;
        this.name = name;
    }
    
    public CaRankRecord(final Long id, final long rId, final short score) {
        this.score = 1000;
        this.grade = 0;
        this.equipScore = 0;
        this.fullRank = 0;
        this.id = id;
        this.rId = rId;
        this.score = score;
    }
    
    public CaRankRecord(final long rId, final short score) {
        this.score = 1000;
        this.grade = 0;
        this.equipScore = 0;
        this.fullRank = 0;
        this.rId = rId;
        this.score = score;
    }
    
    public void save() {
        Db4EventService.getInstance().add2Queue(this);
    }
    
    @Override
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
    
    public int getScore() {
        return this.score;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public int getEquipScore() {
        return this.equipScore;
    }
    
    public int getFullRank() {
        return this.fullRank;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRId(final long rId) {
        this.rId = rId;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    public void setEquipScore(final int equipScore) {
        this.equipScore = equipScore;
    }
    
    public void setFullRank(final int fullRank) {
        this.fullRank = fullRank;
    }
    
    @Override
    public String toString() {
        return "CaRankRecord(id=" + this.getId() + ", rId=" + this.getRId() + ", roleId=" + this.getRoleId() + ", name=" + this.getName() + ", serial=" + this.getSerial() + ", score=" + this.getScore() + ", grade=" + this.getGrade() + ", equipScore=" + this.getEquipScore() + ", fullRank=" + this.getFullRank() + ")";
    }
}
