package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_shop_npc_pet")
public class SysShopNpcPet implements Serializable {
  @Comment("编号")
  @Column("serial")
  private int serial;
  
  @Comment("数量")
  @Column("num")
  private int num;
  
  @Comment("价格")
  @Column("price")
  private int price;
  
  @Comment("等级限制")
  @Column("lv_limit")
  private int lvLimit;
  
  @Comment("转生限制")
  @Column("zs_limit")
  private int zsLimit;
  
  @Comment("消费类型")
  @Column("type")
  private int type;
  
  @Comment("折扣")
  @Column("discount")
  private int discount;

}
