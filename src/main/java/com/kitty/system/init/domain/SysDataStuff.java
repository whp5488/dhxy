package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_stuff")
public class SysDataStuff implements Serializable {
  @Comment("")
  @Column("id")
  private Integer id;
  
  @Comment("道具编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("道具名称")
  @Column("name")
  private String name;
  
  @Comment("价格")
  @Column("price")
  private Integer price;
  
  @Comment("回收价格")
  @Column("reprice")
  private Integer reprice;
  
  @Comment("道具外形")
  @Column("shape")
  private Integer shape;
  
  @Comment("货币类型")
  @Column("cost_type")
  private Integer costType;
  
  @Comment("背包重叠数量")
  @Column("can_merge")
  private Integer canMerge;
  
  @Comment("批量")
  @Column("batches")
  private Integer batches;
  
  @Comment("提示")
  @Column("tips")
  private String tips;
  
  @Comment("价值")
  @Column("value")
  private Integer value;
  
  @Comment("道具品阶")
  @Column("item_pj")
  private Integer itemPJ;
  
  @Comment("使用次数限制")
  @Column("use_cnt_limit")
  private Integer useCntLimit;
  
  @Comment("转生需求")
  @Column("need_zs")
  private Integer needZs;
  
  @Comment("等级需求")
  @Column("need_lv")
  private Integer needLv;
  
  @Comment("是否开启")
  @Column("is_open")
  private Integer isOpen;
  
  @Column("type")
  private Integer type;
  
  @Comment("适用对象编号")
  @Column("apply_object_serial")
  private Integer applyObjectSerial;
}
