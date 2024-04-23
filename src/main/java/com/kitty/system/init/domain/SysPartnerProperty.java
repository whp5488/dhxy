package com.kitty.system.init.domain;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Getter
@Setter
@Table("data_partner_property")
public class SysPartnerProperty {
  @Comment("主键")
  @Column("id")
  private int id;
  
  @Comment("伙伴编号")
  @Column("partner_serial")
  private int partnerSerial;
  
  @Comment("种族")
  @Column("race")
  private int race;
  
  @Comment("性别")
  @Column("sex")
  private int sex;
  
  @Comment("等级")
  @Column("level")
  private short level;
  
  @Comment("根骨")
  @Column("basecon")
  private int basecon;
  
  @Comment("灵性")
  @Column("wakan")
  private int wakan;
  
  @Comment("攻击")
  @Column("atk")
  private int atk;
  
  @Comment("敏捷")
  @Column("agility")
  private int agility;
  
  @Comment("力量")
  @Column("power")
  private int power;
  
  @Comment("速度")
  @Column("speed")
  private int speed;
  
  @Comment("气血")
  @Column("hp_max")
  private int hpMax;
  
  @Comment("五行-火")
  @Column("fire")
  private int fire;
  
  @Comment("五行-金")
  @Column("gold")
  private int gold;
  
  @Comment("五行-土")
  @Column("soil")
  private int soil;
  
  @Comment("五行-水")
  @Column("water")
  private int water;
  
  @Comment("五行-木")
  @Column("wood")
  private int wood;
  
  @Comment("强力克火")
  @Column("con_fire")
  private double conFire;
  
  @Comment("强力克金")
  @Column("con_gold")
  private double conGold;
  
  @Comment("强力克土")
  @Column("con_soil")
  private double conSoil;
  
  @Comment("强力克水")
  @Column("con_water")
  private double conWater;
  
  @Comment("强力克木")
  @Column("con_wood")
  private double conWood;
  
  @Comment("物理吸收")
  @Column("phy_def")
  private double phyDef;
  
  @Comment("物理破防程度")
  @Column("phy_def_nef")
  private double phyDefNef;
  
  @Comment("狂暴率")
  @Column("fatal_rate")
  private double fatalRate;
  
  @Comment("连击次数")
  @Column("hit_combo")
  private int hitCombo;
  
  @Comment("连击率")
  @Column("hit_combo_rate")
  private double hitComboRate;
  
  @Comment("命中率")
  @Column("hit_rate")
  private double hitRate;
  
  @Comment("闪躲率")
  @Column("void_rate")
  private double voidRate;
  
  @Comment("物理破防概率")
  @Column("phy_def_nef_rate")
  private double phyDefNefRate;
  
  @Comment("水系狂暴率")
  @Column("rain_rage_odds")
  private double rainRageOdds;
  
  @Comment("火系狂暴率")
  @Column("fire_rage_odds")
  private double fireRageOdds;
  
  @Comment("风系狂暴率")
  @Column("wind_rage_odds")
  private double windRageOdds;
  
  @Comment("雷系狂暴率")
  @Column("thunder_rage_odds")
  private double thunderRageOdds;
  
  @Comment("鬼火狂暴率")
  @Column("gfire_rage_odds")
  private double gfireRageOdds;
  
  @Comment("三尸狂暴率")
  @Column("shanshi_rage_odds")
  private double shanshiRageOdds;
  
  @Comment("加强水")
  @Column("rain_ehan")
  private double rainEhan;
  
  @Comment("加强火")
  @Column("fire_ehan")
  private double fireEhan;
  
  @Comment("加强风")
  @Column("wind_ehan")
  private double windEhan;
  
  @Comment("加强雷")
  @Column("thunder_ehan")
  private double thunderEhan;
  
  @Comment("强三尸回血")
  @Column("recover_ehan")
  private int recoverEhan;
  
  @Comment("强三尸虫")
  @Column("sanshi_ehan")
  private double sanshiEhan;
  
  @Comment("加强破甲")
  @Column("break_ehan")
  private double breakEhan;
  
  @Comment("加强加攻")
  @Column("ad_atk_ehan")
  private double adAtkEhan;
  
  @Comment("加强加防")
  @Column("ad_def_ehan")
  private double adDefEhan;
  
  @Comment("加强加速")
  @Column("ad_spd_ehan")
  private double adSpdEhan;
  
  @Comment("加强横扫")
  @Column("sweep_ehan")
  private double sweepEhan;
  
  @Comment("加强震击")
  @Column("thud_ehan")
  private double thudEhan;
  
  @Comment("加强混乱")
  @Column("disorder_ehan")
  private double disorderEhan;
  
  @Comment("加强封印")
  @Column("seal_ehan")
  private double sealEhan;
  
  @Comment("加强毒")
  @Column("poison_ehan")
  private double poisonEhan;
  
  @Comment("加强昏睡")
  @Column("sleep_ehan")
  private double sleepEhan;
  
  @Comment("加强遗忘")
  @Column("forget_ehan")
  private double forgetEhan;
  
  @Comment("加强魅惑")
  @Column("charm_ehan")
  private double charmEhan;
  
  @Comment("加强治愈")
  @Column("cure_ehan")
  private double cureEhan;
  
  @Comment("加强震摄")
  @Column("shock_ehan")
  private double shockEhan;
  
  @Comment("加强鬼火")
  @Column("gfire_ehan")
  private double gfireEhan;
  
  @Comment("抗水")
  @Column("rain_def")
  private double rainDef;
  
  @Comment("抗火")
  @Column("fire_def")
  private double fireDef;
  
  @Comment("抗风")
  @Column("wind_def")
  private double windDef;
  
  @Comment("抗雷")
  @Column("thunder_def")
  private double thunderDef;
  
  @Comment("抗混乱")
  @Column("disorder_def")
  private double disorderDef;
  
  @Comment("抗封印")
  @Column("seal_def")
  private double sealDef;
  
  @Comment("抗昏睡")
  @Column("sleep_def")
  private double sleepDef;
  
  @Comment("抗遗忘")
  @Column("forget_def")
  private double forgetDef;
  
  @Comment("抗毒")
  @Column("poison_def")
  private double poisonDef;
  
  @Comment("抗震慑")
  @Column("shock_def")
  private double shockDef;
  
  @Comment("抗鬼火")
  @Column("gfire_def")
  private double gfireDef;
  
  @Comment("抗三尸")
  @Column("sanshi_def")
  private double sanshiDef;
  
  @Comment("忽视抗水")
  @Column("rain_def_neg")
  private double rainDefNeg;
  
  @Comment("忽视抗火")
  @Column("fire_def_neg")
  private double fireDefNeg;
  
  @Comment("忽视抗风")
  @Column("wind_def_neg")
  private double windDefNeg;
  
  @Comment("忽视抗雷")
  @Column("thunder_def_neg")
  private double thunderDefNeg;
  
  @Comment("忽视抗混")
  @Column("disorder_def_neg")
  private double disorderDefNeg;
  
  @Comment("忽视抗震慑")
  @Column("shock_def_neg")
  private double shockDefNeg;
  
  @Comment("忽视抗鬼火")
  @Column("gfire_def_neg")
  private double gfireDefNeg;
  
  @Comment("忽视抗三尸")
  @Column("sanshi_def_neg")
  private double sanshiDefNeg;
  
  @Comment("")
  @Column("skillId")
  private int skillId;

  @Comment("")
  @Column("ext_skill_info")
  @Default("{}")
  private Map<Integer, Integer> extSkillInfo = new HashMap<>();

  @Comment("")
  @Column("skill_info")
  @Default("{}")
  private Map<Integer, Integer> skillInfo = new HashMap<>();
  
  @Comment("")
  @Column("fight_ai")
  private String fightAi;

}
