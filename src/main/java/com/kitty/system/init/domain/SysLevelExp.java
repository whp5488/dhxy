package com.kitty.system.init.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_level_exp")
public class SysLevelExp {
  @Comment("类型 1=英雄，2=宠物，3=伙伴，4=坐骑")
  @Column("type")
  private byte type;
  
  @Comment("等级")
  @Column("level")
  private int level;
  
  @Comment("未转生需要的经验值")
  @Column("need_exp_rei0")
  private long needExpRei0;
  
  @Comment("一转需要的经验值")
  @Column("need_exp_rei1")
  private long needExpRei1;
  
  @Comment("二转需要的经验值")
  @Column("need_exp_rei2")
  private long needExpRei2;
  
  @Comment("三转需要的经验值")
  @Column("need_exp_rei3")
  private long needExpRei3;
  
  @Comment("四转需要的经验值")
  @Column("need_exp_rei4")
  private long needExpRei4;
}
