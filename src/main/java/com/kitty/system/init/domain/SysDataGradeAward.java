package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.Param;
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
@Table("data_grade_award")
public class SysDataGradeAward implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("等级")
  @Column("grade")
  private int grade;
  
  @Comment("奖励")
  @Column("award")
  @Default("[]")
  private List<Param> award;

}
