package com.kitty.system.init.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_task_npc_name_type")
public class SysDataTaskNpcNameType {
  @Column("key")
  private String key;
  
  @Column("value")
  private List<String> value;
}
