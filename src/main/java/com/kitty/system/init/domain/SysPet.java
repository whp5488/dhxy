package com.kitty.system.init.domain;

import com.kitty.system.game.vo.init.Item;
import com.kitty.system.init.domain.vo.AdviceAddPro;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_pet")
public class SysPet implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("宠物名称")
  @Column("name")
  private String name;
  
  @Comment("天生技能")
  @Column("skills")
  @Default("[]")
  private List<Integer> skills;
  
  @Comment("等级类型 1.普通 2.高级 3.特殊 4.神兽 5.珍惜神兽")
  @Column("level_type")
  private Integer levelType;
  
  @Comment("外观")
  @Column("shape")
  private Integer shape;
  
  @Comment("高成长")
  @Column("grow_up_h")
  private double growUpH;
  
  @Comment("成长率")
  @Column("growth_rate")
  @Default("[]")
  private int[] growthRate;
  
  @Comment("权重数据")
  @Column("weight")
  @Default("[]")
  private int[] Weight;
  
  @Comment("初始生命值-min")
  @Column("hp_min")
  private Integer hpMin;
  
  @Comment("初始生命值-max")
  @Column("hp_max")
  private Integer hpMax;
  
  @Comment("初始法力值-min")
  @Column("mp_min")
  private Integer mpMin;
  
  @Comment("初始法力值-max")
  @Column("mp_max")
  private Integer mpMax;
  
  @Comment("初始攻击力-min")
  @Column("ap_min")
  private Integer apMin;
  
  @Comment("初始攻击力-max")
  @Column("ap_max")
  private Integer apMax;
  
  @Comment("初始速度-min")
  @Column("sp_min")
  private Integer spMin;
  
  @Comment("初始速度-max")
  @Column("sp_max")
  private Integer spMax;
  
  @Comment("五行-金")
  @Column("wx_jin")
  private String wxJin;
  
  @Comment("五行-木")
  @Column("wx_mu")
  private String wxMu;
  
  @Comment("五行-水")
  @Column("wx_shui")
  private String wxShui;
  
  @Comment("五行-火")
  @Column("wx_huo")
  private String wxHuo;
  
  @Comment("五行-土")
  @Column("wx_tu")
  private String wxTu;
  
  @Comment("使用的魔法编号")
  @Column("skill_no")
  private Integer skillNo;
  
  @Comment("宠物类型:1.物理宠物,2.法术宠物")
  @Column("pet_type")
  private Integer petType;
  
  @Comment("推荐加点")
  @Column("advice_add_pro")
  @Default("[]")
  private List<AdviceAddPro> adviceAddPro;
  
  @Comment("携带转生")
  @Column("open_zs")
  private Integer openZs;
  
  @Comment("携带等级")
  @Column("open_lv")
  private Integer openLv;
  
  @Comment("合成需要的道具")
  @Column("need_item")
  @Default("[]")
  private List<Item> needItem;
  
  @Comment("是否可合成：0.否，1.是")
  @Column("can_compose")
  private Integer canCompose;
  
  @Comment("捕捉几率")
  @Column("catch_ratio")
  private String catchRatio;
  
  @Comment("捕捉的地图bia")
  @Column("catch_map")
  private Integer catchMap;
  
  @Comment("命中率")
  @Column("pacc")
  private String pacc;
  
  @Comment("不变的分数")
  @Column("static_init_var_score")
  private Integer staticInitVarScore;
  
  @Comment("宠物可获取的颜色值")
  @Column("color")
  @Default("[]")
  private List<Byte> color;
  
  @Comment("觉醒技能")
  @Column("jx_skill")
  private Integer jxSkill;
  
  @Comment("饰品染色")
  @Column("acc_color")
  private String accColor;
  
  @Comment("可使用的幻肤道具")
  @Column("brand_new")
  @Default("[]")
  private List<Integer> brandNew;

  private int canMerge = 1;

}
