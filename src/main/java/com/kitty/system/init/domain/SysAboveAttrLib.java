package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_above_attr_lib")
public class SysAboveAttrLib implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Column("dict")
  private String dict;
  
  @Column("base_attr")
  @ColDefine(customType = "text")
  private String baseAttr;
  
  @Column("rnd_range")
  private String rndRange;

}
