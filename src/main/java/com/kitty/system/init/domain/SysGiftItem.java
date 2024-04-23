package com.kitty.system.init.domain;

import com.kitty.system.game.vo.init.Item;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_gift_item")
public class SysGiftItem implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("道具编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("道具名称")
  @Column("name")
  private String name;
  
  @Comment("价格")
  @Column("price")
  private Integer price;
  
  @Comment("回收价格 0直接回收")
  @Column("reprice")
  private Integer reprice;
  
  @Comment("元宝")
  @Column("gold")
  private Long gold;
  
  @Comment("银两")
  @Column("silver")
  private Long silver;
  
  @Comment("贡币")
  @Column("coin")
  private Long coin;
  
  @Comment("经验：通过计算")
  @Column("exp")
  private int exp;
  
  @Comment("道具外观")
  @Column("shape")
  private Integer shape;
  
  @Comment("背包重叠数量")
  @Column("can_merge")
  private Integer canMerge;
  
  @Comment("绑定状态")
  @Column("bind")
  private Integer bind;
  
  @Comment("转生需求")
  @Column("need_zs")
  private Integer needZs;
  
  @Comment("等级需求")
  @Column("need_lv")
  private Integer needLv;
  
  @Comment("使用日期限制范围")
  @Column("use_need_day")
  @Default("[]")
  private Integer[] useNeedDay;
  
  @Comment("额外奖励道具")
  @Column("extra")
  @Default("[]")
  @ColDefine(customType = "text")
  private List<Item> extra;
  
  @Comment("是否概率开启：0.否，1.是")
  @Column("cycle")
  private Integer cycle;
  
  @Comment("是否开启：0.否，1.是")
  @Column("is_open")
  private Integer isOpen;
  
  @Comment("参数")
  @Column("param")
  private String param;
}
