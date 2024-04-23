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
@Table("data_task_condition")
public class SysDataTaskCondition implements Serializable {
  @Comment("提交编号")
  @Column("submit_condition")
  private int submitCondition;
  
  @Comment("计数")
  @Column("need_counter")
  @Default("[]")
  private List<Param> needCounter;
  
  @Comment("前置任务编号")
  @Column("pre_task")
  private int preTask;
  
  @Comment("等级需求")
  @Column("need_grade")
  private int needGrade;

}
