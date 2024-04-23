package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("t_task_item")
public class SysTaskItem implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("道具id")
  @Column("item_id")
  private Integer itemId;
  
  @Comment("道具名称")
  @Column("name")
  private String name;
  
  @Comment("价格")
  @Column("price")
  private Integer price;
  
  @Comment("重新定价")
  @Column("reprice")
  private Integer reprice;
  
  @Comment("道具形状")
  @Column("itemShape")
  private Integer itemshape;
  
  @Comment("背包重叠数量")
  @Column("canMerge")
  private Integer canMerge;
  
  @Comment("提示")
  @Column("tips")
  private String tips;
  
  @Comment("描述")
  @Column("des")
  private String des;
  
  @Comment("道具品阶")
  @Column("itemPJ")
  private Integer itempj;
  
  @Comment("效果id")
  @Column("xiaoguoID")
  private String xiaoguoid;
  
  @Comment("次数")
  @Column("cishu")
  private Integer cishu;
  
  @Comment("同步")
  @Column("tongbu")
  private Integer tongbu;
  
  @Comment("类型")
  @Column("leix")
  private Integer leix;
  
  @Comment("位置")
  @Column("weizhi")
  private String weizhi;
}
