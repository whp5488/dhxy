package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.MonsterParameter;
import com.kitty.system.init.domain.vo.MonsterPower;
import com.kitty.system.init.domain.vo.SkillParameter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("t_data_monster")
public class SysDataMonster implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Column("id")
  private Integer id;
  
  @Column("serial")
  private Integer serial;
  
  @Column("name")
  private String name;
  
  @Column("close")
  private Integer close;
  
  @Comment("技能，格式：[{\"serial\":30001,\"fightType\":0,\"priority\":0}]")
  @Column("skills")
  @Default("[]")
  private ArrayList<SkillParameter> skills;
  
  @Column("judged_ye")
  private String judgedYe;
  
  @Comment("领悟技能，格式：[{\"serial\":30001,\"fightType\":0}]")
  @Column("p_skills")
  @Default("[]")
  private ArrayList<Map<String, Integer>> pSkills;
  
  @Column("shape")
  private Integer shape;
  
  @Column("fight_talk")
  private Integer fightTalk;
  
  @Column("is_boss")
  private Integer isBoss;
  
  @Column("hp")
  private Integer hp;
  
  @Column("mp")
  private Integer mp;
  
  @Column("ap")
  private Integer ap;
  
  @Column("sp")
  private Integer sp;
  
  @Column("wx_jin")
  private Integer wxJin;
  
  @Column("wx_mu")
  private Integer wxMu;
  
  @Column("wx_shui")
  private Integer wxShui;
  
  @Column("wx_huo")
  private Integer wxHuo;
  
  @Column("wx_tu")
  private Integer wxTu;
  
  @Column("level_mode")
  private Integer levelMode;
  
  @Column("monster_level")
  private Integer grade;
  
  @Column("des")
  private String des;
  
  @Column("fs_gj_gl")
  private double fsGjGl;
  
  @Column("jn_sld")
  private Integer jnSld;
  
  @Column("lw_gj_gl")
  private double lwGjGl;
  
  @Column("escape_round")
  private Integer escapeRound;
  
  @Column("escape_ratio")
  private double escapeRatio;
  
  @Column("c_escape_ratio")
  private double cEscapeRatio;
  
  @Column("pde_fend")
  private double pdeFend;
  
  @Column("p_pofang_pro")
  private double pPofangPro;
  
  @Column("p_pofang")
  private double pPofang;
  
  @Column("p_fanzhen")
  private double pFanZhen;
  
  @Column("p_fanzhen_pro")
  private double pFanZhenPro;
  
  @Column("f_pde_fend")
  private double fPdeFend;
  
  @Column("p_acc")
  private double pAcc;
  
  @Column("p_sbl")
  private double pSbl;
  
  @Column("p_kuang_bao")
  private double pKuangBao;
  
  @Column("feng_odd")
  private double fengOdd;
  
  @Column("lei_odd")
  private double leiOdd;
  
  @Column("shui_odd")
  private double shuiOdd;
  
  @Column("huo_odd")
  private double huoOdd;
  
  @Column("gui_huo_odd")
  private double guiHuoOdd;
  
  @Column("san_shi_odd")
  private double sanShiOdd;
  
  @Column("p_lj_pro")
  private double pLjPro;
  
  @Column("p_lj_times")
  private Integer pLjTimes;
  
  @Column("p_wl_fj_pro")
  private double pWlFjPro;
  
  @Column("p_wl_fj_times")
  private Integer pWlFjTimes;
  
  @Column("k_hun_luan")
  private double kHunLuan;
  
  @Column("k_feng_yin")
  private double kFengYin;
  
  @Column("k_hun_shui")
  private double kHunShui;
  
  @Column("k_zhong_du")
  private double kZhongDu;
  
  @Column("k_yi_wang")
  private double kYiWang;
  
  @Column("k_gui_huo")
  private double kGuiHuo;
  
  @Column("k_san_shi")
  private double kSanShi;
  
  @Column("k_shuai_ruo")
  private double kShuaiRuo;
  
  @Column("k_zhen_she")
  private double kZhenShe;
  
  @Column("k_feng")
  private double kFeng;
  
  @Column("k_huo")
  private double kHuo;
  
  @Column("k_shui")
  private double kShui;
  
  @Column("k_lei")
  private double kLei;
  
  @Column("fk_feng")
  private double fkFeng;
  
  @Column("fk_huo")
  private double fkHuo;
  
  @Column("fk_shui")
  private double fkShui;
  
  @Column("fk_lei")
  private double fkLei;
  
  @Column("fk_zhong_du")
  private double fkZhongDu;
  
  @Column("fk_hun_luan")
  private double fkHunLuan;
  
  @Column("fk_feng_yin")
  private double fkFengYin;
  
  @Column("fk_hun_shui")
  private double fkHunShui;
  
  @Column("fk_yi_wang")
  private double fkYiWang;
  
  @Column("fk_gui_huo")
  private double fkGuiHuo;
  
  @Column("fk_zhen_she")
  private double fkZhenShe;
  
  @Column("fk_san_shi")
  private double fkSanShi;
  
  private int pos;
  
  @Column("select_parameter_json")
  private List<MonsterParameter> selectParameterList;
  
  @Column("item_use_parameter_json")
  private List<MonsterParameter> itemUseParameterList;
  
  @Column("attr")
  private byte attr;
  
  @Comment("类型")
  @Column("type")
  private int type;
  
  @Comment("加强加攻")
  @Column("ad_atk_ehan")
  private double adAtkEhan;
  
  @Comment("加强破甲")
  @Column("break_ehan")
  private double breakEhan;
  
  @Comment("加强魅惑")
  @Column("charm_ehan")
  private double charmEhan;
  
  @Comment("加强治愈")
  @Column("cure_ehan")
  private double cureEhan;
  
  @Comment("加强混乱")
  @Column("disorder_ehan")
  private double disorderEhan;
  
  @Comment("加强鬼火")
  @Column("gfire_ehan")
  private double gfireEhan;
  
  @Comment("加强水")
  @Column("rain_ehan")
  private double rainEhan;
  
  @Comment("强三尸回血")
  @Column("recover_ehan")
  private double recoverEhan;
  
  @Comment("加强震摄")
  @Column("shock_ehan")
  private double shockEhan;
  
  @Comment("加强风")
  @Column("wind_ehan")
  private double windEhan;
  
  @Comment("加强火")
  @Column("fire_ehan")
  private double fireEhan;
  
  @Comment("加强雷")
  @Column("thunder_ehan")
  private double thunderEhan;
  
  @Comment("加强毒")
  @Column("poison_ehan")
  private double poisonEhan;
  
  @Comment("加强封印")
  @Column("seal_ehan")
  private double sealEhan;
  
  @Comment("加强昏睡")
  @Column("sleep_ehan")
  private double sleepEhan;
  
  @Comment("加强遗忘")
  @Column("forget_ehan")
  private double forgetEhan;
  
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
  
  @Comment("强三尸虫")
  @Column("sanshi_ehan")
  private double sanshiEhan;
  
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

  @Comment("气血系数")
  @Column("power_hp")
  private MonsterPower powerHp = new MonsterPower();

  @Comment("攻击系数")
  @Column("power_ap")
  private MonsterPower powerAp = new MonsterPower();

  @Comment("速度系数")
  @Column("power_sp")
  private MonsterPower powerSp = new MonsterPower();

  @Comment("属性系数")
  @Column("power_attr")
  @ColDefine(customType = "longtext")
  private Map<Integer, MonsterPower> powerAttr = new HashMap<>();

  @Column("vanish")
  private int vanish = 0;
  
  private int monsterType = -1;
}
