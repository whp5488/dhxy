package com.kitty.system.init.domain;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_task_formula")
public class SysDataTaskFormula {
  @Column("serial")
  private int serial;
  
  @Column("content")
  @Default("[]")
  private List<FiedValue> content;
}
