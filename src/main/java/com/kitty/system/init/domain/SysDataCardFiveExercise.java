package com.kitty.system.init.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_card_five_exercise")
public class SysDataCardFiveExercise {
  
  @Comment("修炼等级")
  @Column("grade")
  private int grade = 0;

  @Comment("金经验")
  @Column("gold")
  private int gold = 0;

  @Comment("木经验")
  @Column("wood")
  private int wood = 0;

  @Comment("水经验")
  @Column("water")
  private int water = 0;

  @Comment("火经验")
  @Column("fire")
  private int fire = 0;

  @Comment("土经验")
  @Column("soil")
  private int soil = 0;

  @Comment("五行升级需要的贡币")
  @Column("wx")
  private Integer wx = Integer.valueOf(0);

}
