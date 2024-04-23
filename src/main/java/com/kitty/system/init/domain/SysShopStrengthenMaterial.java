package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_shop_strengthen_material")
public class SysShopStrengthenMaterial implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("名称")
  @Column("name")
  private String name;
  
  @Comment("消费类型")
  @Column("type")
  private Integer type;
  
  @Comment("价格")
  @Column("price")
  private double price;
  
  @Comment("排序")
  @Column("sort_no")
  private Integer sortNo;
  
  @Comment("等级限制")
  @Column("lv_limit")
  private Integer lvLimit;
  
  @Comment("转生限制")
  @Column("zs_limit")
  private Integer zsLimit;
  
  @Comment("是否vip才可购买 0 否 1 是")
  @Column("must_vip")
  private Integer mustVip;
  
  @Comment("折扣")
  @Column("discount")
  private Integer discount;

}
