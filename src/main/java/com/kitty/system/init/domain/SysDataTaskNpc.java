package com.kitty.system.init.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_task_npc")
public class SysDataTaskNpc {
  @Column("serial")
  private int serial;
  
  @Column("scene")
  private int scene;
  
  @Column("x")
  private int x;
  
  @Column("y")
  private int y;
  
  @Column("dir")
  private int dir;
  
  @Column("shape")
  private int shape;
  
  @Column("name")
  private String name;
  
  @Column("shape_type")
  private int shapeType;
  
  @Column("talk")
  private String talk;
  
  @Column("types")
  @Default("[]")
  private List<Integer> types;
  
  @Column("is_scene")
  private int isScene;

}
