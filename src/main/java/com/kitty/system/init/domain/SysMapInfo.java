package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_map_info")
public class SysMapInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("")
  @Column("id")
  private Integer id;
  
  @Comment("地图名称")
  @Column("name")
  private String name;
  
  @Comment("地图编号")
  @Column("map_serial")
  private Integer mapSerial;
  
  @Comment("等级限制")
  @Column("lv_limit")
  private String lvLimit;
  
  @Comment("出生点")
  @Column("born_point")
  private String bornPoint;
  
  @Comment("战斗地图（0否 1是 ）")
  @Column("war_map")
  private Integer warMap;
  
  @Comment("地图类型（0否 1是 ）")
  @Column("map_type")
  private Integer mapType;
  
  @Comment("跨服地图（0否 1是 ）")
  @Column("cankuafu")
  private Integer canKuaFu;
  
  @Comment("是否能直接离开（0否 1是 ）")
  @Column("caneasyleave")
  private int caneasyleave;
  
  @Comment("地图宽")
  @Column("bgw")
  private int bgw;
  
  @Comment("地图高")
  @Column("bgh")
  private int bgh;
  
  @Comment("是否是pk地图（0否 1是 ）")
  @Column("is_pk_map")
  private int isPkMap;
  
  @Comment("通缉地图（0否 1是 ）")
  @Column("is_wanted")
  private int isWanted;
  
  @Comment("副本（0否 1是 ）")
  @Column("fuben")
  private Integer fuBen;
  
  @Comment("等级范围")
  @Column("level_scope")
  private String levelScope;
  
  @Comment("地图跳转提示")
  @Column("how_to_out")
  private String howToOut;

}
