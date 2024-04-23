package com.kitty.system.init.domain;

import java.io.Serializable;
import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_guaji_map")
public class SysGuajiMap implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("地图编号")
  @Column("map_serial")
  private Integer mapSerial;
  
  @Comment("地图名称")
  @Column("map_name")
  private String mapName;
  
  @Comment("建议等级")
  @Column("recommend_lv")
  @Default("[]")
  private Integer[] recommendLv;
  
  @Comment("战斗外键")
  @Column("war_data_id")
  private Integer warDataId;
  
  @Comment("转生需求")
  @Column("unlock_zs")
  private Integer unlockzs;
  
  @Comment("等级需求")
  @Column("unlock_lv")
  private Integer unlocklv;
  
  @Comment("")
  @Column("always_judge_lv_flag")
  private Integer always_judge_lv_flag;
}
