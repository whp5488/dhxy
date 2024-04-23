package com.kitty.system.init.domain;

import com.kitty.system.game.vo.Field;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_refine")
public class SysRefine implements Serializable {
  @Column("id")
  private int id;
  
  @Column("dict")
  private String dict;
  
  @Column("position")
  private int position;
  
  @Column("fields")
  @Default("[]")
  private List<Field> fields;
  
  @Column("pos")
  @Default("[]")
  private List<Integer> pos;

}
