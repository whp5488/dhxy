package com.kitty.system.game.card.domain;

import com.kitty.common.db.DbCardExerciseService;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("card_five_exercise")
public class CardExercise extends BaseEntity<Long>
{
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private long id;
    @Comment("玩家唯一ID")
    @Column("r_id")
    private long rId;
    @Comment("五行等级")
    @Column("five_grade")
    private int fiveGrade;
    @Comment("金等级")
    @Column("gold_grade")
    private int goldGrade;
    @Comment("金经验")
    @Column("gold_exp")
    private int goldExp;
    @Comment("木等级")
    @Column("wood_grade")
    private int woodGrade;
    @Comment("木经验")
    @Column("wood_exp")
    private int woodExp;
    @Comment("水等级")
    @Column("water_grade")
    private int waterGrade;
    @Comment("水经验")
    @Column("water_exp")
    private int waterExp;
    @Comment("火等级")
    @Column("fire_grade")
    private int fireGrade;
    @Comment("火经验")
    @Column("fire_exp")
    private int fireExp;
    @Comment("土等级")
    @Column("soil_grade")
    private int soilGrade;
    @Comment("土经验")
    @Column("soil_exp")
    private int soilExp;
    @Comment("使用次数")
    @Column("use_count")
    private int useCount;
    
    @Override
    public Long getId() {
        return Long.valueOf(this.id);
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getrId() {
        return this.rId;
    }
    
    public void setrId(long rId) {
        this.rId = rId;
    }
    
    public int getFiveGrade() {
        return this.fiveGrade;
    }
    
    public void setFiveGrade(int fiveGrade) {
        this.fiveGrade = fiveGrade;
    }
    
    public int getGoldGrade() {
        return this.goldGrade;
    }
    
    public void setGoldGrade(int goldGrade) {
        this.goldGrade = goldGrade;
    }
    
    public int getGoldExp() {
        return this.goldExp;
    }
    
    public void setGoldExp(int goldExp) {
        this.goldExp = goldExp;
    }
    
    public int getWoodGrade() {
        return this.woodGrade;
    }
    
    public void setWoodGrade(int woodGrade) {
        this.woodGrade = woodGrade;
    }
    
    public int getWoodExp() {
        return this.woodExp;
    }
    
    public void setWoodExp(int woodExp) {
        this.woodExp = woodExp;
    }
    
    public int getWaterGrade() {
        return this.waterGrade;
    }
    
    public void setWaterGrade(int waterGrade) {
        this.waterGrade = waterGrade;
    }
    
    public int getWaterExp() {
        return this.waterExp;
    }
    
    public void setWaterExp(int waterExp) {
        this.waterExp = waterExp;
    }
    
    public int getFireGrade() {
        return this.fireGrade;
    }
    
    public void setFireGrade(int fireGrade) {
        this.fireGrade = fireGrade;
    }
    
    public int getFireExp() {
        return this.fireExp;
    }
    
    public void setFireExp(int fireExp) {
        this.fireExp = fireExp;
    }
    
    public int getSoilGrade() {
        return this.soilGrade;
    }
    
    public void setSoilGrade(int soilGrade) {
        this.soilGrade = soilGrade;
    }
    
    public int getSoilExp() {
        return this.soilExp;
    }
    
    public void setSoilExp(int soilExp) {
        this.soilExp = soilExp;
    }
    
    public int getUseCount() {
        return this.useCount;
    }
    
    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }
    
    public CardExercise() {
        this.fiveGrade = 0;
        this.goldGrade = 0;
        this.goldExp = 0;
        this.woodGrade = 0;
        this.woodExp = 0;
        this.waterGrade = 0;
        this.waterExp = 0;
        this.fireGrade = 0;
        this.fireExp = 0;
        this.soilGrade = 0;
        this.soilExp = 0;
        this.useCount = 100;
    }
    
    public CardExercise(long id, long rId) {
        this.fiveGrade = 0;
        this.goldGrade = 0;
        this.goldExp = 0;
        this.woodGrade = 0;
        this.woodExp = 0;
        this.waterGrade = 0;
        this.waterExp = 0;
        this.fireGrade = 0;
        this.fireExp = 0;
        this.soilGrade = 0;
        this.soilExp = 0;
        this.useCount = 100;
        this.id = id;
        this.rId = rId;
    }
    
    public CardExercise(long id, long rId, int fiveGrade, int goldGrade, int goldExp, int woodGrade, int woodExp, int waterGrade, int waterExp, int fireGrade, int fireExp, int soilGrade, int soilExp, int useCount) {
        this.fiveGrade = 0;
        this.goldGrade = 0;
        this.goldExp = 0;
        this.woodGrade = 0;
        this.woodExp = 0;
        this.waterGrade = 0;
        this.waterExp = 0;
        this.fireGrade = 0;
        this.fireExp = 0;
        this.soilGrade = 0;
        this.soilExp = 0;
        this.useCount = 100;
        this.id = id;
        this.rId = rId;
        this.fiveGrade = fiveGrade;
        this.goldGrade = goldGrade;
        this.goldExp = goldExp;
        this.woodGrade = woodGrade;
        this.woodExp = woodExp;
        this.waterGrade = waterGrade;
        this.waterExp = waterExp;
        this.fireGrade = fireGrade;
        this.fireExp = fireExp;
        this.soilGrade = soilGrade;
        this.soilExp = soilExp;
        this.useCount = useCount;
    }
    
    public void save() {
        DbCardExerciseService.getInstance().add2Queue(this);
    }
}
