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
@Table("data_task_event")
public class SysDataTaskEvent implements Serializable {
  @Comment("事件编号")
  @Column("serial")
  private int serial;
  
  @Comment("执行编号")
  @Column("event")
  private int event;
  
  @Comment("类型")
  @Column("type")
  private TaskEvent type;
  
  @Comment("事件外键")
  @Column("foreign")
  private int foreign;
}
