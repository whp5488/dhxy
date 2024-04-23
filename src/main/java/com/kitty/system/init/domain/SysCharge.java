package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_charge")
public class SysCharge implements Serializable {
  @Comment("充值编号")
  @Column("serial")
  private int serial;
  
  @Comment("金额")
  @Column("amount")
  private int amount;
  
  @Comment("赠送仙玉")
  @Column("give")
  private long give;
  
  @Comment("赠送积分")
  @Column("score")
  private long score;
}
