package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_exchange")
public class SysDataExchange implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("物品编号")
  @Column("serial")
  private int serial;
  
  @Comment("物品数量")
  @Column("num")
  private int num;
  
  @Comment("碎片编号")
  @Column("need_serial")
  private int needSerial;
  
  @Comment("碎片数量")
  @Column("need_num")
  private int needNum;
  
  @Comment("类型：0.装备 1.道具 2.宠物 3.变身卡 4.天策符")
  @Column("type")
  private int type;

}
