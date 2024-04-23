package com.kitty.system.init.domain;

import com.kitty.system.enums.task.TaskType;
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
@Table("data_task_catches_ghosts")
public class SysDataTaskCatchesGhosts implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("任务编号")
  @Column("task_serial")
  private int serial;
  
  @Comment("下个任务编号")
  @Column("next_task_serial")
  private int nextTaskSerial;
  
  @Comment("提交NPC编号")
  @Column("submit_npc")
  private int submitNpc;
  
  @Comment("是否可以放弃： 1.是 0.否")
  @Column("give_up")
  private int giveUp;
  
  @Comment("是否战斗：0否，1.是")
  @Column("need_fight")
  private int needFight;
  
  @Comment("接取任务条件")
  @Column("get_condition")
  private int getCondition;
  
  @Comment("提交任务条件")
  @Column("submit_condition")
  private int submitCondition;
  
  @Comment("添加任务事件")
  @Column("add_task_event")
  @Default("[]")
  private List<Integer> addTaskEvent;
  
  @Comment("完成任务事件")
  @Column("finish_task_event")
  @Default("[]")
  private List<Integer> finishTaskEvent;
  
  @Comment("客户端请求的事件")
  @Column("ask_event")
  @Default("[]")
  private List<Integer> askEvent;
  
  @Comment("任务类型")
  @Column("task_type")
  private TaskType taskType;
  
  @Comment("类型：0.对话 1.战斗")
  @Column("type")
  private int type;
  
  @Comment("任务奖励")
  @Column("task_reward")
  private String taskReward;
  
  @Comment("任务超时(-1 则没有超时时间)ms")
  @Column("timeout_sec")
  private int timeoutSec;

}
