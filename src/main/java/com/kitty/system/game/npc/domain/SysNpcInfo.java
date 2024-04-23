package com.kitty.system.game.npc.domain;

import com.kitty.common.model.Pos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_npc_info")
public class SysNpcInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static final int TYPE_NORMAL = 1;
  
  public static final int TYPE_UNUSUAL = 2;
  
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("npc名字")
  @Column("name")
  private String name;
  
  @Comment("外貌")
  @Column("shape")
  private Integer shape;
  
  @Comment("位置信息")
  @Column("pos_info")
  private String posInfo;
  
  @Comment("对话内容")
  @Column("talk")
  private String talk;

  @Comment("npc按钮")
  @Column("npc_types")
  @Default("[]")
  private List<Integer> npcTypes = new ArrayList<>();
  
  @Comment("类型")
  @Column("type")
  private int type;
  
  @Comment("货摊")
  @Column("shipper")
  private String shipper;
  
  @Comment("分类：0.场景 其他：对应活动或者副本编号")
  @Column("classify")
  private int classify;
  
  @Comment("周期性显示在地图")
  @Column("period")
  @Default("[]")
  private List<Integer> period;
  
  private Pos pos;

}
