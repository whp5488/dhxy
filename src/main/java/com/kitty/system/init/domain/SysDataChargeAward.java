package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.Param;
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
@Table("data_charge_award")
public class SysDataChargeAward implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("奖励编号")
  @Column("serial")
  private int serial;
  
  @Comment("累计金额")
  @Column("amount")
  private long amount;
  
  @Comment("奖励")
  @Column("award")
  @Default("[]")
  private List<Param> award;
  
  @Comment("类型：0.充值奖励 1.每日充值奖励")
  @Column("type")
  private byte type;

}
