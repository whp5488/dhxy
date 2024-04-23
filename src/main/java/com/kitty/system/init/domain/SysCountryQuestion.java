package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_country_question")
public class SysCountryQuestion implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("问题编号")
  @Column("serial")
  private int serial;
  
  @Comment("答案")
  @Column("answer")
  private String answer;
}
