package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_purify_attr")
public class SysPurifyAttr implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Column("dict")
  private String dict;
  
  @Column("lower_bound")
  private int lowerBound;
  
  @Column("upper_bound")
  private int upperBound;

}
