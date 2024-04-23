package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_task_reward_dynamic")
public class SysDataTaskRewardDynamic implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("标识")
  @Column("identify")
  private int identify;
  
  @Comment("字典")
  @Column("dict")
  private String dict;
  
  @Comment("物品编号")
  @Column("serial")
  private int serial;
  
  @Comment("比例计算")
  @Column("rate_calculate")
  private String rateCalculate;
  
  @Comment("数量计算")
  @Column("num_calculate")
  private String numCalculate;
  
  @Comment("公告编号")
  @Column("notice")
  private int notice;

}
