package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_map_teleporter")
public class SysMapTeleporter implements Serializable {
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("类型")
  @Column("type")
  private Integer type;
  
  @Comment("地图名称")
  @Column("from_map_name")
  private String name;
  
  @Comment("地图编号")
  @Column("from_map_serial")
  private Integer fromMapSerial;
  
  @Comment("地图传送点X")
  @Column("from_x")
  private Integer fromX;
  
  @Comment("地图传送点Y")
  @Column("from_y")
  private Integer fromY;
  
  @Comment("传送地图编号")
  @Column("to_map_serial")
  private Integer toMapSerial;
  
  @Comment("传送地图名称")
  @Column("to_map_name")
  private String toMapName;
  
  @Comment("传送地图X")
  @Column("to_map_x")
  private Integer toMapX;
  
  @Comment("传送地图Y")
  @Column("to_map_y")
  private Integer toMapY;
  
  @Comment("传送到地图朝向")
  @Column("dir")
  private Integer dir;
}
