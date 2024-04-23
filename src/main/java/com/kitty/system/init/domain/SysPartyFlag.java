package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_party_flag")
public class SysPartyFlag implements Serializable {
  @Comment("索引")
  @Column("index")
  private short index;
  
  @Comment("价格")
  @Column("price")
  private int price;
  
  @Comment("消费类型（1.元宝，2.银币，3.贡币）")
  @Column("type")
  private int type;
  
  @Comment("分类：0.旗帜 1.边饰 2.纹章 3.旗帜颜色 4.纹章颜色")
  @Column("classify")
  private short classify;
}
