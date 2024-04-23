package com.kitty.system.init.domain;

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
@Table("data_task_fight")
public class SysDataTaskFight implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("战斗编号")
  @Column("serial")
  private int serial;
  
  @Comment("任务编号")
  @Column("task_serial")
  private int taskSerial;
  
  @Comment("war_role中的编号")
  @Column("tar_npc_data")
  private int tarNpcData;
  
  @Comment("失败事件编号")
  @Column("lose_event")
  @Default("[]")
  private List<Integer> loseEvent;
  
  @Comment("胜利事件编号")
  @Column("win_event")
  @Default("[]")
  private List<Integer> winEvent;
  
  @Comment("侠义/好心值")
  @Column("chivalry")
  private Integer chivalry;
}
