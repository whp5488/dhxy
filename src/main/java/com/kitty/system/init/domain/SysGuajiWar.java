package com.kitty.system.init.domain;

import com.kitty.system.game.vo.init.Item;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_guaji_war")
public class SysGuajiWar implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("比率")
  @Column("ratio")
  private Integer ratio;
  
  @Comment("经验")
  @Column("exp")
  private Integer exp;
  
  @Comment("奖励道具")
  @Column("item")
  @Default("[]")
  private List<Item> item;
  
  @Comment("经验限制")
  @Column("limit_exp")
  private Integer limitExp;
  
  @Comment("战斗外键")
  @Column("war_data_id")
  private Integer warDataId;
}
