package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_task_reward_foreign")
public class SysDataTaskForeign implements Serializable {
  @Column("serial")
  private int serial;
  
  @Column("role_icon")
  private int roleIcon;
  
  @Column("task_reward_serial")
  private String taskRewardSerial;
}
