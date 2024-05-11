
package com.kitty.system.game.tianfu.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_day_in_ce_practice")
public class SysDayInCePractice implements Serializable
{
    @Comment("主键")
    @Column("id")
    private int id;
    @Column("level")
    private short level;
    @Column("achievement_score")
    private int achievementScore;
    @Column("coin")
    private int coin;
    @Column("need")
    private short need;
    
    public int getId() {
        return this.id;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public int getAchievementScore() {
        return this.achievementScore;
    }
    
    public int getCoin() {
        return this.coin;
    }
    
    public short getNeed() {
        return this.need;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setAchievementScore(final int achievementScore) {
        this.achievementScore = achievementScore;
    }
    
    public void setCoin(final int coin) {
        this.coin = coin;
    }
    
    public void setNeed(final short need) {
        this.need = need;
    }
}
