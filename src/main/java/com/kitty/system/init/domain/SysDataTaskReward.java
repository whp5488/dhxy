package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.Param;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_task_reward")
public class SysDataTaskReward implements Serializable {
  @Column("serial")
  private String serial;
  
  @Column("rewards")
  @Default("[]")
  @ColDefine(customType = "text")
  private List<Param> rewards;
  
  @Column("type")
  private int type;
}
