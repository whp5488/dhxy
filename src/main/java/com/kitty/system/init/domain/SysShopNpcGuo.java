package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_shop_npc_guo")
public class SysShopNpcGuo implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Column("serial")
  private Integer serial;
  
  @Column("num")
  private int num;
  
  @Column("price")
  private int price;
  
  @Column("sort_no")
  private int sortNo;
  
  @Column("lv_limit")
  private int lvLimit;
  
  @Column("zs_limit")
  private int zsLimit;
  
  @Column("discount")
  private int discount;
  
  @Column("quota_limit")
  private int quotaLimit;
  
  @Column("bind")
  private int bind;

}
