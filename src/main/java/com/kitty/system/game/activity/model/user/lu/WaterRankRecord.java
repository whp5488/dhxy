package com.kitty.system.game.activity.model.user.lu;

import com.kitty.common.db.Db4EventService;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("rank_water")
public class WaterRankRecord extends BaseEntity<Long>
{
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private Long id;
    @Column("r_id")
    private long rId;
    @Column("score")
    private int score;
    @Column("grade")
    private int grade;
    @Column("equip_score")
    private int equipScore;
    @Column("leader")
    private long leader;
    
    public WaterRankRecord() {
        this.score = 0;
        this.grade = 0;
        this.equipScore = 0;
        this.leader = 0L;
    }
    
    public WaterRankRecord(Long id, long rId) {
        this.score = 0;
        this.grade = 0;
        this.equipScore = 0;
        this.leader = 0L;
        this.id = id;
        this.rId = rId;
    }
    
    public WaterRankRecord(Long id, long rId, int score, int grade, int equipScore, long leader) {
        this.score = 0;
        this.grade = 0;
        this.equipScore = 0;
        this.leader = 0L;
        this.id = id;
        this.rId = rId;
        this.score = score;
        this.grade = grade;
        this.equipScore = equipScore;
        this.leader = leader;
    }
    
    public WaterRankRecord(long rId, int score, int grade, int equipScore, long leader) {
        this.score = 0;
        this.grade = 0;
        this.equipScore = 0;
        this.leader = 0L;
        this.rId = rId;
        this.score = score;
        this.grade = grade;
        this.equipScore = equipScore;
        this.leader = leader;
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
    
    public int getScore() {
        return this.score;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public int getEquipScore() {
        return this.equipScore;
    }
    
    public long getLeader() {
        return this.leader;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRId(final long rId) {
        this.rId = rId;
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
    
    public void setLeader(final long leader) {
        this.leader = leader;
    }
    
    @Override
    public String toString() {
        return "WaterRankRecord(id=" + this.getId() + ", rId=" + this.getRId() + ", score=" + this.getScore() + ", grade=" + this.getGrade() + ", equipScore=" + this.getEquipScore() + ", leader=" + this.getLeader() + ")";
    }
}
