package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.Field;
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
@Table("data_rei_en")
public class SysReiEn implements Serializable {
  @Comment("key")
  @Column("key")
  private int key;
  
  @Comment("转生")
  @Column("rei")
  private int rei;
  
  @Comment("种族")
  @Column("race")
  private int race;
  
  @Comment("性别")
  @Column("sex")
  private int sex;
  
  @Comment("数据编号")
  @Column("serial")
  private int serial;
  
  @Comment("修正属性")
  @Column("rei_en")
  @Default("[]")
  private List<Field> reiEn;

}
