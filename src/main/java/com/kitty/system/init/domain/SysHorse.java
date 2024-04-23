package com.kitty.system.init.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("h_horse")
public class SysHorse implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("编号")
  @Column("serial")
  private int serial;
  
  @Comment("坐骑名称")
  @Column("name")
  private String name;
  
  @Comment("骑乘等级")
  @Column("need_grade")
  private int needGrade;
  
  @Comment("骑乘角色")
  @Column("need_serial")
  @Default("[]")
  private List<Integer> needSerial;
  
  @Comment("坐骑位置")
  @Column("seat")
  private byte seat;

}
