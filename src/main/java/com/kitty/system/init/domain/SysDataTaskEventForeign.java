package com.kitty.system.init.domain;

import com.kitty.system.enums.task.TaskEvent;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_task_event_foreign")
public class SysDataTaskEventForeign implements Serializable {
  @Comment("事件编号")
  @Column("serial")
  private int serial;
  
  @Comment("类型")
  @Column("type")
  private TaskEvent type;
  
  @Comment("角色限制（角色编号）")
  @Column("facade_serial")
  private int facadeSerial;
  
  @Comment("key")
  @Column("k")
  private int k;
  
  @Comment("value")
  @Column("v")
  private int v;
  
  @Comment("其他")
  @Column("other")
  private String other;
}
