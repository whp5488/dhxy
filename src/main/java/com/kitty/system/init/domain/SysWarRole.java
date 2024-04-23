package com.kitty.system.init.domain;

import com.kitty.system.game.vo.init.Monster;
import com.kitty.system.game.vo.init.Summon;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_war_role")
public class SysWarRole implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("说明")
  @Column("des")
  private String des;
  
  @Comment("怪物数据")
  @Column("monster")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<Monster> monster;
  
  @Comment("召唤小怪")
  @Column("summon")
  @ColDefine(customType = "longtext")
  private Summon summon;
  
  @Comment("特殊类型")
  @Column("special_type")
  private byte specialType;
}
