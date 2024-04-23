package com.kitty.system.game.tianfu.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_day_in_ce_practice")
public class SysDayInCePractice implements Serializable {
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

}
