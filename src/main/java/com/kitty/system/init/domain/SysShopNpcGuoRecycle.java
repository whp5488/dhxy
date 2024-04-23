package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_shop_npc_guo_recycle")
public class SysShopNpcGuoRecycle implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Column("serial")
  private Integer serial;
  
  @Column("price")
  private int price;
}
