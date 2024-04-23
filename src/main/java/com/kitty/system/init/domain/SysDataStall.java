package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("t_data_stall")
public class SysDataStall implements Serializable {
  @Column("serial")
  private Integer serial;
  
  @Comment("商品菜单编号")
  @Column("stall_menu_serial")
  private Integer stallMenuSerial;
  
  @Comment("货币类型：1.仙玉，2.银币，3.贡币")
  @Column("type")
  private Integer type;
  
  @Comment("商品名称")
  @Column("name")
  private String name;
  
  @Comment("分类")
  @Column("category")
  private int category;
  
  @Comment("分类名称")
  @Column("category_name")
  private String categoryName;
  
  @Comment("基础价格")
  @Column("basics_price")
  private Long basicsPrice;
  
  @Comment("涨值")
  @Column("rise")
  private double rise;
  
  @Comment("跌值")
  @Column("fall")
  private double fall;
  
  @Comment("手续费比例")
  @Column("revenue_ratio")
  private double revenueRatio;
  
  @Comment("物品类型：0.装备 1.道具 2.宠物 3.变身卡 4.天策符")
  @Column("is_item")
  private byte isItem;
  
  @Comment("可售时间：秒(s)")
  @Column("expire_time")
  private Integer expireTime;
  
  @Comment("公示期：秒(s)")
  @Column("publicity_period")
  private Integer publicityPeriod;
  
  @Comment("审核时间：秒(s)")
  @Column("audit_time")
  private Integer auditTime;
  
  @Comment("是否校验张跌值：0.否 1.是")
  @Column("is_check")
  private byte isCheck;

}
