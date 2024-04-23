package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_wx_other_war_data")
public class SysWxOtherWarData implements Serializable {
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("名称")
  @Column("name")
  private String name;
  
  @Comment("属性值")
  @Column("value")
  private Double value;

}
