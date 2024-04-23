package com.kitty.system.init.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_task_npc_name")
public class SysDataTaskNpcName {
  @Column("key")
  private String key;
  
  @Column("name")
  private String name;
}
