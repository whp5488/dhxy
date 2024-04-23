package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_can_move")
public class SysCanMove implements Serializable {
  @Column("serial")
  private int serial;
  
  @Column("can_move")
  @ColDefine(customType = "longtext")
  private String canMove;
  
  @Column("tab")
  private byte tab;

}
