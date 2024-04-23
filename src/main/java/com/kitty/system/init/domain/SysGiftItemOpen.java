package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_gift_item_open")
public class SysGiftItemOpen implements Serializable {
  @Column("id")
  @Id
  private Integer id;
  
  @Comment("礼包编号")
  @Column("gift_serial")
  private Integer giftSerial;
  
  @Comment("道具编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("数量")
  @Column("num")
  private Integer num;
  
  @Comment("概率")
  @Column("ratio")
  private double ratio;
}
