package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_shop_npc_grocery")
public class SysShopNpcGrocery implements Serializable {
  @Comment("编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("数量")
  @Column("num")
  private Integer num;
  
  @Comment("价格")
  @Column("price")
  private Integer price;
  
  @Comment("等级限制")
  @Column("lv_limit")
  private Integer lvLimit;
  
  @Comment("转生限制")
  @Column("zs_limit")
  private Integer zsLimit;
  
  @Comment("消费类型")
  @Column("type")
  private Integer type;
  
  @Comment("折扣")
  @Column("discount")
  private Integer discount;
  
  @Comment("限购数量")
  @Column("quota_limit")
  private Integer quotaLimit;
  
  @Comment("是否限制交易：0.否 1.是")
  @Column("bd")
  private Integer bd;
}
