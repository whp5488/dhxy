package com.kitty.system.init.domain;

import java.util.HashMap;
import org.nutz.dao.entity.annotation.ColDefine;
import com.kitty.system.init.domain.vo.MonsterPower;
import com.kitty.system.init.domain.vo.MonsterParameter;
import java.util.List;
import java.util.Map;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Comment;
import com.kitty.system.init.domain.vo.SkillParameter;
import java.util.ArrayList;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("t_data_monster")
public class SysDataMonster implements Serializable
{
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
    private MonsterPower powerHp;
    @Comment("攻击系数")
    @Column("power_ap")
    private MonsterPower powerAp;
    @Comment("速度系数")
    @Column("power_sp")
    private MonsterPower powerSp;
    @Comment("属性系数")
    @Column("power_attr")
    @ColDefine(customType = "longtext")
    private Map<Integer, MonsterPower> powerAttr;
    @Column("vanish")
    private int vanish;
    private int monsterType;
    
    public SysDataMonster() {
        this.powerHp = new MonsterPower();
        this.powerAp = new MonsterPower();
        this.powerSp = new MonsterPower();
        this.powerAttr = new HashMap();
        this.vanish = 0;
        this.monsterType = -1;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getClose() {
        return this.close;
    }
    
    public ArrayList<SkillParameter> getSkills() {
        return this.skills;
    }
    
    public String getJudgedYe() {
        return this.judgedYe;
    }
    
    public ArrayList<Map<String, Integer>> getPSkills() {
        return this.pSkills;
    }
    
    public Integer getShape() {
        return this.shape;
    }
    
    public Integer getFightTalk() {
        return this.fightTalk;
    }
    
    public Integer getIsBoss() {
        return this.isBoss;
    }
    
    public Integer getHp() {
        return this.hp;
    }
    
    public Integer getMp() {
        return this.mp;
    }
    
    public Integer getAp() {
        return this.ap;
    }
    
    public Integer getSp() {
        return this.sp;
    }
    
    public Integer getWxJin() {
        return this.wxJin;
    }
    
    public Integer getWxMu() {
        return this.wxMu;
    }
    
    public Integer getWxShui() {
        return this.wxShui;
    }
    
    public Integer getWxHuo() {
        return this.wxHuo;
    }
    
    public Integer getWxTu() {
        return this.wxTu;
    }
    
    public Integer getLevelMode() {
        return this.levelMode;
    }
    
    public Integer getGrade() {
        return this.grade;
    }
    
    public String getDes() {
        return this.des;
    }
    
    public double getFsGjGl() {
        return this.fsGjGl;
    }
    
    public Integer getJnSld() {
        return this.jnSld;
    }
    
    public double getLwGjGl() {
        return this.lwGjGl;
    }
    
    public Integer getEscapeRound() {
        return this.escapeRound;
    }
    
    public double getEscapeRatio() {
        return this.escapeRatio;
    }
    
    public double getCEscapeRatio() {
        return this.cEscapeRatio;
    }
    
    public double getPdeFend() {
        return this.pdeFend;
    }
    
    public double getPPofangPro() {
        return this.pPofangPro;
    }
    
    public double getPPofang() {
        return this.pPofang;
    }
    
    public double getPFanZhen() {
        return this.pFanZhen;
    }
    
    public double getPFanZhenPro() {
        return this.pFanZhenPro;
    }
    
    public double getFPdeFend() {
        return this.fPdeFend;
    }
    
    public double getPAcc() {
        return this.pAcc;
    }
    
    public double getPSbl() {
        return this.pSbl;
    }
    
    public double getPKuangBao() {
        return this.pKuangBao;
    }
    
    public double getFengOdd() {
        return this.fengOdd;
    }
    
    public double getLeiOdd() {
        return this.leiOdd;
    }
    
    public double getShuiOdd() {
        return this.shuiOdd;
    }
    
    public double getHuoOdd() {
        return this.huoOdd;
    }
    
    public double getGuiHuoOdd() {
        return this.guiHuoOdd;
    }
    
    public double getSanShiOdd() {
        return this.sanShiOdd;
    }
    
    public double getPLjPro() {
        return this.pLjPro;
    }
    
    public Integer getPLjTimes() {
        return this.pLjTimes;
    }
    
    public double getPWlFjPro() {
        return this.pWlFjPro;
    }
    
    public Integer getPWlFjTimes() {
        return this.pWlFjTimes;
    }
    
    public double getKHunLuan() {
        return this.kHunLuan;
    }
    
    public double getKFengYin() {
        return this.kFengYin;
    }
    
    public double getKHunShui() {
        return this.kHunShui;
    }
    
    public double getKZhongDu() {
        return this.kZhongDu;
    }
    
    public double getKYiWang() {
        return this.kYiWang;
    }
    
    public double getKGuiHuo() {
        return this.kGuiHuo;
    }
    
    public double getKSanShi() {
        return this.kSanShi;
    }
    
    public double getKShuaiRuo() {
        return this.kShuaiRuo;
    }
    
    public double getKZhenShe() {
        return this.kZhenShe;
    }
    
    public double getKFeng() {
        return this.kFeng;
    }
    
    public double getKHuo() {
        return this.kHuo;
    }
    
    public double getKShui() {
        return this.kShui;
    }
    
    public double getKLei() {
        return this.kLei;
    }
    
    public double getFkFeng() {
        return this.fkFeng;
    }
    
    public double getFkHuo() {
        return this.fkHuo;
    }
    
    public double getFkShui() {
        return this.fkShui;
    }
    
    public double getFkLei() {
        return this.fkLei;
    }
    
    public double getFkZhongDu() {
        return this.fkZhongDu;
    }
    
    public double getFkHunLuan() {
        return this.fkHunLuan;
    }
    
    public double getFkFengYin() {
        return this.fkFengYin;
    }
    
    public double getFkHunShui() {
        return this.fkHunShui;
    }
    
    public double getFkYiWang() {
        return this.fkYiWang;
    }
    
    public double getFkGuiHuo() {
        return this.fkGuiHuo;
    }
    
    public double getFkZhenShe() {
        return this.fkZhenShe;
    }
    
    public double getFkSanShi() {
        return this.fkSanShi;
    }
    
    public int getPos() {
        return this.pos;
    }
    
    public List<MonsterParameter> getSelectParameterList() {
        return this.selectParameterList;
    }
    
    public List<MonsterParameter> getItemUseParameterList() {
        return this.itemUseParameterList;
    }
    
    public byte getAttr() {
        return this.attr;
    }
    
    public int getType() {
        return this.type;
    }
    
    public double getAdAtkEhan() {
        return this.adAtkEhan;
    }
    
    public double getBreakEhan() {
        return this.breakEhan;
    }
    
    public double getCharmEhan() {
        return this.charmEhan;
    }
    
    public double getCureEhan() {
        return this.cureEhan;
    }
    
    public double getDisorderEhan() {
        return this.disorderEhan;
    }
    
    public double getGfireEhan() {
        return this.gfireEhan;
    }
    
    public double getRainEhan() {
        return this.rainEhan;
    }
    
    public double getRecoverEhan() {
        return this.recoverEhan;
    }
    
    public double getShockEhan() {
        return this.shockEhan;
    }
    
    public double getWindEhan() {
        return this.windEhan;
    }
    
    public double getFireEhan() {
        return this.fireEhan;
    }
    
    public double getThunderEhan() {
        return this.thunderEhan;
    }
    
    public double getPoisonEhan() {
        return this.poisonEhan;
    }
    
    public double getSealEhan() {
        return this.sealEhan;
    }
    
    public double getSleepEhan() {
        return this.sleepEhan;
    }
    
    public double getForgetEhan() {
        return this.forgetEhan;
    }
    
    public double getAdDefEhan() {
        return this.adDefEhan;
    }
    
    public double getAdSpdEhan() {
        return this.adSpdEhan;
    }
    
    public double getSweepEhan() {
        return this.sweepEhan;
    }
    
    public double getThudEhan() {
        return this.thudEhan;
    }
    
    public double getSanshiEhan() {
        return this.sanshiEhan;
    }
    
    public double getConFire() {
        return this.conFire;
    }
    
    public double getConGold() {
        return this.conGold;
    }
    
    public double getConSoil() {
        return this.conSoil;
    }
    
    public double getConWater() {
        return this.conWater;
    }
    
    public double getConWood() {
        return this.conWood;
    }
    
    public MonsterPower getPowerHp() {
        return this.powerHp;
    }
    
    public MonsterPower getPowerAp() {
        return this.powerAp;
    }
    
    public MonsterPower getPowerSp() {
        return this.powerSp;
    }
    
    public Map<Integer, MonsterPower> getPowerAttr() {
        return this.powerAttr;
    }
    
    public int getVanish() {
        return this.vanish;
    }
    
    public int getMonsterType() {
        return this.monsterType;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setClose(final Integer close) {
        this.close = close;
    }
    
    public void setSkills(final ArrayList<SkillParameter> skills) {
        this.skills = skills;
    }
    
    public void setJudgedYe(final String judgedYe) {
        this.judgedYe = judgedYe;
    }
    
    public void setPSkills(final ArrayList<Map<String, Integer>> pSkills) {
        this.pSkills = pSkills;
    }
    
    public void setShape(final Integer shape) {
        this.shape = shape;
    }
    
    public void setFightTalk(final Integer fightTalk) {
        this.fightTalk = fightTalk;
    }
    
    public void setIsBoss(final Integer isBoss) {
        this.isBoss = isBoss;
    }
    
    public void setHp(final Integer hp) {
        this.hp = hp;
    }
    
    public void setMp(final Integer mp) {
        this.mp = mp;
    }
    
    public void setAp(final Integer ap) {
        this.ap = ap;
    }
    
    public void setSp(final Integer sp) {
        this.sp = sp;
    }
    
    public void setWxJin(final Integer wxJin) {
        this.wxJin = wxJin;
    }
    
    public void setWxMu(final Integer wxMu) {
        this.wxMu = wxMu;
    }
    
    public void setWxShui(final Integer wxShui) {
        this.wxShui = wxShui;
    }
    
    public void setWxHuo(final Integer wxHuo) {
        this.wxHuo = wxHuo;
    }
    
    public void setWxTu(final Integer wxTu) {
        this.wxTu = wxTu;
    }
    
    public void setLevelMode(final Integer levelMode) {
        this.levelMode = levelMode;
    }
    
    public void setGrade(final Integer grade) {
        this.grade = grade;
    }
    
    public void setDes(final String des) {
        this.des = des;
    }
    
    public void setFsGjGl(final double fsGjGl) {
        this.fsGjGl = fsGjGl;
    }
    
    public void setJnSld(final Integer jnSld) {
        this.jnSld = jnSld;
    }
    
    public void setLwGjGl(final double lwGjGl) {
        this.lwGjGl = lwGjGl;
    }
    
    public void setEscapeRound(final Integer escapeRound) {
        this.escapeRound = escapeRound;
    }
    
    public void setEscapeRatio(final double escapeRatio) {
        this.escapeRatio = escapeRatio;
    }
    
    public void setCEscapeRatio(final double cEscapeRatio) {
        this.cEscapeRatio = cEscapeRatio;
    }
    
    public void setPdeFend(final double pdeFend) {
        this.pdeFend = pdeFend;
    }
    
    public void setPPofangPro(final double pPofangPro) {
        this.pPofangPro = pPofangPro;
    }
    
    public void setPPofang(final double pPofang) {
        this.pPofang = pPofang;
    }
    
    public void setPFanZhen(final double pFanZhen) {
        this.pFanZhen = pFanZhen;
    }
    
    public void setPFanZhenPro(final double pFanZhenPro) {
        this.pFanZhenPro = pFanZhenPro;
    }
    
    public void setFPdeFend(final double fPdeFend) {
        this.fPdeFend = fPdeFend;
    }
    
    public void setPAcc(final double pAcc) {
        this.pAcc = pAcc;
    }
    
    public void setPSbl(final double pSbl) {
        this.pSbl = pSbl;
    }
    
    public void setPKuangBao(final double pKuangBao) {
        this.pKuangBao = pKuangBao;
    }
    
    public void setFengOdd(final double fengOdd) {
        this.fengOdd = fengOdd;
    }
    
    public void setLeiOdd(final double leiOdd) {
        this.leiOdd = leiOdd;
    }
    
    public void setShuiOdd(final double shuiOdd) {
        this.shuiOdd = shuiOdd;
    }
    
    public void setHuoOdd(final double huoOdd) {
        this.huoOdd = huoOdd;
    }
    
    public void setGuiHuoOdd(final double guiHuoOdd) {
        this.guiHuoOdd = guiHuoOdd;
    }
    
    public void setSanShiOdd(final double sanShiOdd) {
        this.sanShiOdd = sanShiOdd;
    }
    
    public void setPLjPro(final double pLjPro) {
        this.pLjPro = pLjPro;
    }
    
    public void setPLjTimes(final Integer pLjTimes) {
        this.pLjTimes = pLjTimes;
    }
    
    public void setPWlFjPro(final double pWlFjPro) {
        this.pWlFjPro = pWlFjPro;
    }
    
    public void setPWlFjTimes(final Integer pWlFjTimes) {
        this.pWlFjTimes = pWlFjTimes;
    }
    
    public void setKHunLuan(final double kHunLuan) {
        this.kHunLuan = kHunLuan;
    }
    
    public void setKFengYin(final double kFengYin) {
        this.kFengYin = kFengYin;
    }
    
    public void setKHunShui(final double kHunShui) {
        this.kHunShui = kHunShui;
    }
    
    public void setKZhongDu(final double kZhongDu) {
        this.kZhongDu = kZhongDu;
    }
    
    public void setKYiWang(final double kYiWang) {
        this.kYiWang = kYiWang;
    }
    
    public void setKGuiHuo(final double kGuiHuo) {
        this.kGuiHuo = kGuiHuo;
    }
    
    public void setKSanShi(final double kSanShi) {
        this.kSanShi = kSanShi;
    }
    
    public void setKShuaiRuo(final double kShuaiRuo) {
        this.kShuaiRuo = kShuaiRuo;
    }
    
    public void setKZhenShe(final double kZhenShe) {
        this.kZhenShe = kZhenShe;
    }
    
    public void setKFeng(final double kFeng) {
        this.kFeng = kFeng;
    }
    
    public void setKHuo(final double kHuo) {
        this.kHuo = kHuo;
    }
    
    public void setKShui(final double kShui) {
        this.kShui = kShui;
    }
    
    public void setKLei(final double kLei) {
        this.kLei = kLei;
    }
    
    public void setFkFeng(final double fkFeng) {
        this.fkFeng = fkFeng;
    }
    
    public void setFkHuo(final double fkHuo) {
        this.fkHuo = fkHuo;
    }
    
    public void setFkShui(final double fkShui) {
        this.fkShui = fkShui;
    }
    
    public void setFkLei(final double fkLei) {
        this.fkLei = fkLei;
    }
    
    public void setFkZhongDu(final double fkZhongDu) {
        this.fkZhongDu = fkZhongDu;
    }
    
    public void setFkHunLuan(final double fkHunLuan) {
        this.fkHunLuan = fkHunLuan;
    }
    
    public void setFkFengYin(final double fkFengYin) {
        this.fkFengYin = fkFengYin;
    }
    
    public void setFkHunShui(final double fkHunShui) {
        this.fkHunShui = fkHunShui;
    }
    
    public void setFkYiWang(final double fkYiWang) {
        this.fkYiWang = fkYiWang;
    }
    
    public void setFkGuiHuo(final double fkGuiHuo) {
        this.fkGuiHuo = fkGuiHuo;
    }
    
    public void setFkZhenShe(final double fkZhenShe) {
        this.fkZhenShe = fkZhenShe;
    }
    
    public void setFkSanShi(final double fkSanShi) {
        this.fkSanShi = fkSanShi;
    }
    
    public void setPos(final int pos) {
        this.pos = pos;
    }
    
    public void setSelectParameterList(final List<MonsterParameter> selectParameterList) {
        this.selectParameterList = selectParameterList;
    }
    
    public void setItemUseParameterList(final List<MonsterParameter> itemUseParameterList) {
        this.itemUseParameterList = itemUseParameterList;
    }
    
    public void setAttr(final byte attr) {
        this.attr = attr;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setAdAtkEhan(final double adAtkEhan) {
        this.adAtkEhan = adAtkEhan;
    }
    
    public void setBreakEhan(final double breakEhan) {
        this.breakEhan = breakEhan;
    }
    
    public void setCharmEhan(final double charmEhan) {
        this.charmEhan = charmEhan;
    }
    
    public void setCureEhan(final double cureEhan) {
        this.cureEhan = cureEhan;
    }
    
    public void setDisorderEhan(final double disorderEhan) {
        this.disorderEhan = disorderEhan;
    }
    
    public void setGfireEhan(final double gfireEhan) {
        this.gfireEhan = gfireEhan;
    }
    
    public void setRainEhan(final double rainEhan) {
        this.rainEhan = rainEhan;
    }
    
    public void setRecoverEhan(final double recoverEhan) {
        this.recoverEhan = recoverEhan;
    }
    
    public void setShockEhan(final double shockEhan) {
        this.shockEhan = shockEhan;
    }
    
    public void setWindEhan(final double windEhan) {
        this.windEhan = windEhan;
    }
    
    public void setFireEhan(final double fireEhan) {
        this.fireEhan = fireEhan;
    }
    
    public void setThunderEhan(final double thunderEhan) {
        this.thunderEhan = thunderEhan;
    }
    
    public void setPoisonEhan(final double poisonEhan) {
        this.poisonEhan = poisonEhan;
    }
    
    public void setSealEhan(final double sealEhan) {
        this.sealEhan = sealEhan;
    }
    
    public void setSleepEhan(final double sleepEhan) {
        this.sleepEhan = sleepEhan;
    }
    
    public void setForgetEhan(final double forgetEhan) {
        this.forgetEhan = forgetEhan;
    }
    
    public void setAdDefEhan(final double adDefEhan) {
        this.adDefEhan = adDefEhan;
    }
    
    public void setAdSpdEhan(final double adSpdEhan) {
        this.adSpdEhan = adSpdEhan;
    }
    
    public void setSweepEhan(final double sweepEhan) {
        this.sweepEhan = sweepEhan;
    }
    
    public void setThudEhan(final double thudEhan) {
        this.thudEhan = thudEhan;
    }
    
    public void setSanshiEhan(final double sanshiEhan) {
        this.sanshiEhan = sanshiEhan;
    }
    
    public void setConFire(final double conFire) {
        this.conFire = conFire;
    }
    
    public void setConGold(final double conGold) {
        this.conGold = conGold;
    }
    
    public void setConSoil(final double conSoil) {
        this.conSoil = conSoil;
    }
    
    public void setConWater(final double conWater) {
        this.conWater = conWater;
    }
    
    public void setConWood(final double conWood) {
        this.conWood = conWood;
    }
    
    public void setPowerHp(final MonsterPower powerHp) {
        this.powerHp = powerHp;
    }
    
    public void setPowerAp(final MonsterPower powerAp) {
        this.powerAp = powerAp;
    }
    
    public void setPowerSp(final MonsterPower powerSp) {
        this.powerSp = powerSp;
    }
    
    public void setPowerAttr(final Map<Integer, MonsterPower> powerAttr) {
        this.powerAttr = powerAttr;
    }
    
    public void setVanish(final int vanish) {
        this.vanish = vanish;
    }
    
    public void setMonsterType(final int monsterType) {
        this.monsterType = monsterType;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataMonster)) {
            return false;
        }
        SysDataMonster other = (SysDataMonster)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$close = this.getClose();
        Object other$close = other.getClose();
        if (!((this$close != null) ? this$close.equals(other$close) : (other$close == null))) {
            return false;
        }
        Object this$skills = this.getSkills();
        Object other$skills = other.getSkills();
        if (!((this$skills != null) ? this$skills.equals(other$skills) : (other$skills == null))) {
            return false;
        }
        Object this$judgedYe = this.getJudgedYe();
        Object other$judgedYe = other.getJudgedYe();
        if (!((this$judgedYe != null) ? this$judgedYe.equals(other$judgedYe) : (other$judgedYe == null))) {
            return false;
        }
        Object this$pSkills = this.getPSkills();
        Object other$pSkills = other.getPSkills();
        if (!((this$pSkills != null) ? this$pSkills.equals(other$pSkills) : (other$pSkills == null))) {
            return false;
        }
        Object this$shape = this.getShape();
        Object other$shape = other.getShape();
        if (!((this$shape != null) ? this$shape.equals(other$shape) : (other$shape == null))) {
            return false;
        }
        Object this$fightTalk = this.getFightTalk();
        Object other$fightTalk = other.getFightTalk();
        if (!((this$fightTalk != null) ? this$fightTalk.equals(other$fightTalk) : (other$fightTalk == null))) {
            return false;
        }
        Object this$isBoss = this.getIsBoss();
        Object other$isBoss = other.getIsBoss();
        if (!((this$isBoss != null) ? this$isBoss.equals(other$isBoss) : (other$isBoss == null))) {
            return false;
        }
        Object this$hp = this.getHp();
        Object other$hp = other.getHp();
        if (!((this$hp != null) ? this$hp.equals(other$hp) : (other$hp == null))) {
            return false;
        }
        Object this$mp = this.getMp();
        Object other$mp = other.getMp();
        if (!((this$mp != null) ? this$mp.equals(other$mp) : (other$mp == null))) {
            return false;
        }
        Object this$ap = this.getAp();
        Object other$ap = other.getAp();
        if (!((this$ap != null) ? this$ap.equals(other$ap) : (other$ap == null))) {
            return false;
        }
        Object this$sp = this.getSp();
        Object other$sp = other.getSp();
        if (!((this$sp != null) ? this$sp.equals(other$sp) : (other$sp == null))) {
            return false;
        }
        Object this$wxJin = this.getWxJin();
        Object other$wxJin = other.getWxJin();
        if (!((this$wxJin != null) ? this$wxJin.equals(other$wxJin) : (other$wxJin == null))) {
            return false;
        }
        Object this$wxMu = this.getWxMu();
        Object other$wxMu = other.getWxMu();
        if (!((this$wxMu != null) ? this$wxMu.equals(other$wxMu) : (other$wxMu == null))) {
            return false;
        }
        Object this$wxShui = this.getWxShui();
        Object other$wxShui = other.getWxShui();
        if (!((this$wxShui != null) ? this$wxShui.equals(other$wxShui) : (other$wxShui == null))) {
            return false;
        }
        Object this$wxHuo = this.getWxHuo();
        Object other$wxHuo = other.getWxHuo();
        if (!((this$wxHuo != null) ? this$wxHuo.equals(other$wxHuo) : (other$wxHuo == null))) {
            return false;
        }
        Object this$wxTu = this.getWxTu();
        Object other$wxTu = other.getWxTu();
        if (!((this$wxTu != null) ? this$wxTu.equals(other$wxTu) : (other$wxTu == null))) {
            return false;
        }
        Object this$levelMode = this.getLevelMode();
        Object other$levelMode = other.getLevelMode();
        if (!((this$levelMode != null) ? this$levelMode.equals(other$levelMode) : (other$levelMode == null))) {
            return false;
        }
        Object this$grade = this.getGrade();
        Object other$grade = other.getGrade();
        if (!((this$grade != null) ? this$grade.equals(other$grade) : (other$grade == null))) {
            return false;
        }
        Object this$des = this.getDes();
        Object other$des = other.getDes();
        if (!((this$des != null) ? this$des.equals(other$des) : (other$des == null))) {
            return false;
        }
        if (Double.compare(this.getFsGjGl(), other.getFsGjGl()) != 0) {
            return false;
        }
        Object this$jnSld = this.getJnSld();
        Object other$jnSld = other.getJnSld();
        if (!((this$jnSld != null) ? this$jnSld.equals(other$jnSld) : (other$jnSld == null))) {
            return false;
        }
        if (Double.compare(this.getLwGjGl(), other.getLwGjGl()) != 0) {
            return false;
        }
        Object this$escapeRound = this.getEscapeRound();
        Object other$escapeRound = other.getEscapeRound();
        if (!((this$escapeRound != null) ? this$escapeRound.equals(other$escapeRound) : (other$escapeRound == null))) {
            return false;
        }
        if (Double.compare(this.getEscapeRatio(), other.getEscapeRatio()) != 0) {
            return false;
        }
        if (Double.compare(this.getCEscapeRatio(), other.getCEscapeRatio()) != 0) {
            return false;
        }
        if (Double.compare(this.getPdeFend(), other.getPdeFend()) != 0) {
            return false;
        }
        if (Double.compare(this.getPPofangPro(), other.getPPofangPro()) != 0) {
            return false;
        }
        if (Double.compare(this.getPPofang(), other.getPPofang()) != 0) {
            return false;
        }
        if (Double.compare(this.getPFanZhen(), other.getPFanZhen()) != 0) {
            return false;
        }
        if (Double.compare(this.getPFanZhenPro(), other.getPFanZhenPro()) != 0) {
            return false;
        }
        if (Double.compare(this.getFPdeFend(), other.getFPdeFend()) != 0) {
            return false;
        }
        if (Double.compare(this.getPAcc(), other.getPAcc()) != 0) {
            return false;
        }
        if (Double.compare(this.getPSbl(), other.getPSbl()) != 0) {
            return false;
        }
        if (Double.compare(this.getPKuangBao(), other.getPKuangBao()) != 0) {
            return false;
        }
        if (Double.compare(this.getFengOdd(), other.getFengOdd()) != 0) {
            return false;
        }
        if (Double.compare(this.getLeiOdd(), other.getLeiOdd()) != 0) {
            return false;
        }
        if (Double.compare(this.getShuiOdd(), other.getShuiOdd()) != 0) {
            return false;
        }
        if (Double.compare(this.getHuoOdd(), other.getHuoOdd()) != 0) {
            return false;
        }
        if (Double.compare(this.getGuiHuoOdd(), other.getGuiHuoOdd()) != 0) {
            return false;
        }
        if (Double.compare(this.getSanShiOdd(), other.getSanShiOdd()) != 0) {
            return false;
        }
        if (Double.compare(this.getPLjPro(), other.getPLjPro()) != 0) {
            return false;
        }
        Object this$pLjTimes = this.getPLjTimes();
        Object other$pLjTimes = other.getPLjTimes();
        if (!((this$pLjTimes != null) ? this$pLjTimes.equals(other$pLjTimes) : (other$pLjTimes == null))) {
            return false;
        }
        if (Double.compare(this.getPWlFjPro(), other.getPWlFjPro()) != 0) {
            return false;
        }
        Object this$pWlFjTimes = this.getPWlFjTimes();
        Object other$pWlFjTimes = other.getPWlFjTimes();
        if (!((this$pWlFjTimes != null) ? this$pWlFjTimes.equals(other$pWlFjTimes) : (other$pWlFjTimes == null))) {
            return false;
        }
        if (Double.compare(this.getKHunLuan(), other.getKHunLuan()) != 0) {
            return false;
        }
        if (Double.compare(this.getKFengYin(), other.getKFengYin()) != 0) {
            return false;
        }
        if (Double.compare(this.getKHunShui(), other.getKHunShui()) != 0) {
            return false;
        }
        if (Double.compare(this.getKZhongDu(), other.getKZhongDu()) != 0) {
            return false;
        }
        if (Double.compare(this.getKYiWang(), other.getKYiWang()) != 0) {
            return false;
        }
        if (Double.compare(this.getKGuiHuo(), other.getKGuiHuo()) != 0) {
            return false;
        }
        if (Double.compare(this.getKSanShi(), other.getKSanShi()) != 0) {
            return false;
        }
        if (Double.compare(this.getKShuaiRuo(), other.getKShuaiRuo()) != 0) {
            return false;
        }
        if (Double.compare(this.getKZhenShe(), other.getKZhenShe()) != 0) {
            return false;
        }
        if (Double.compare(this.getKFeng(), other.getKFeng()) != 0) {
            return false;
        }
        if (Double.compare(this.getKHuo(), other.getKHuo()) != 0) {
            return false;
        }
        if (Double.compare(this.getKShui(), other.getKShui()) != 0) {
            return false;
        }
        if (Double.compare(this.getKLei(), other.getKLei()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkFeng(), other.getFkFeng()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkHuo(), other.getFkHuo()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkShui(), other.getFkShui()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkLei(), other.getFkLei()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkZhongDu(), other.getFkZhongDu()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkHunLuan(), other.getFkHunLuan()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkFengYin(), other.getFkFengYin()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkHunShui(), other.getFkHunShui()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkYiWang(), other.getFkYiWang()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkGuiHuo(), other.getFkGuiHuo()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkZhenShe(), other.getFkZhenShe()) != 0) {
            return false;
        }
        if (Double.compare(this.getFkSanShi(), other.getFkSanShi()) != 0) {
            return false;
        }
        if (this.getPos() != other.getPos()) {
            return false;
        }
        Object this$selectParameterList = this.getSelectParameterList();
        Object other$selectParameterList = other.getSelectParameterList();
        if (!((this$selectParameterList != null) ? this$selectParameterList.equals(other$selectParameterList) : (other$selectParameterList == null))) {
            return false;
        }
        Object this$itemUseParameterList = this.getItemUseParameterList();
        Object other$itemUseParameterList = other.getItemUseParameterList();
        if (!((this$itemUseParameterList != null) ? this$itemUseParameterList.equals(other$itemUseParameterList) : (other$itemUseParameterList == null))) {
            return false;
        }
        if (this.getAttr() != other.getAttr()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (Double.compare(this.getAdAtkEhan(), other.getAdAtkEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getBreakEhan(), other.getBreakEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getCharmEhan(), other.getCharmEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getCureEhan(), other.getCureEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getDisorderEhan(), other.getDisorderEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getGfireEhan(), other.getGfireEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getRainEhan(), other.getRainEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getRecoverEhan(), other.getRecoverEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getShockEhan(), other.getShockEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getWindEhan(), other.getWindEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getFireEhan(), other.getFireEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getThunderEhan(), other.getThunderEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getPoisonEhan(), other.getPoisonEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getSealEhan(), other.getSealEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getSleepEhan(), other.getSleepEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getForgetEhan(), other.getForgetEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getAdDefEhan(), other.getAdDefEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getAdSpdEhan(), other.getAdSpdEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getSweepEhan(), other.getSweepEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getThudEhan(), other.getThudEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getSanshiEhan(), other.getSanshiEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getConFire(), other.getConFire()) != 0) {
            return false;
        }
        if (Double.compare(this.getConGold(), other.getConGold()) != 0) {
            return false;
        }
        if (Double.compare(this.getConSoil(), other.getConSoil()) != 0) {
            return false;
        }
        if (Double.compare(this.getConWater(), other.getConWater()) != 0) {
            return false;
        }
        if (Double.compare(this.getConWood(), other.getConWood()) != 0) {
            return false;
        }
        Object this$powerHp = this.getPowerHp();
        Object other$powerHp = other.getPowerHp();
        if (!((this$powerHp != null) ? this$powerHp.equals(other$powerHp) : (other$powerHp == null))) {
            return false;
        }
        Object this$powerAp = this.getPowerAp();
        Object other$powerAp = other.getPowerAp();
        if (!((this$powerAp != null) ? this$powerAp.equals(other$powerAp) : (other$powerAp == null))) {
            return false;
        }
        Object this$powerSp = this.getPowerSp();
        Object other$powerSp = other.getPowerSp();
        if (!((this$powerSp != null) ? this$powerSp.equals(other$powerSp) : (other$powerSp == null))) {
            return false;
        }
        Object this$powerAttr = this.getPowerAttr();
        Object other$powerAttr = other.getPowerAttr();
        return ((this$powerAttr != null) ? this$powerAttr.equals(other$powerAttr) : (other$powerAttr == null)) && this.getVanish() == other.getVanish() && this.getMonsterType() == other.getMonsterType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataMonster;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $close = this.getClose();
        result = result * 59 + (($close == null) ? 43 : $close.hashCode());
        Object $skills = this.getSkills();
        result = result * 59 + (($skills == null) ? 43 : $skills.hashCode());
        Object $judgedYe = this.getJudgedYe();
        result = result * 59 + (($judgedYe == null) ? 43 : $judgedYe.hashCode());
        Object $pSkills = this.getPSkills();
        result = result * 59 + (($pSkills == null) ? 43 : $pSkills.hashCode());
        Object $shape = this.getShape();
        result = result * 59 + (($shape == null) ? 43 : $shape.hashCode());
        Object $fightTalk = this.getFightTalk();
        result = result * 59 + (($fightTalk == null) ? 43 : $fightTalk.hashCode());
        Object $isBoss = this.getIsBoss();
        result = result * 59 + (($isBoss == null) ? 43 : $isBoss.hashCode());
        Object $hp = this.getHp();
        result = result * 59 + (($hp == null) ? 43 : $hp.hashCode());
        Object $mp = this.getMp();
        result = result * 59 + (($mp == null) ? 43 : $mp.hashCode());
        Object $ap = this.getAp();
        result = result * 59 + (($ap == null) ? 43 : $ap.hashCode());
        Object $sp = this.getSp();
        result = result * 59 + (($sp == null) ? 43 : $sp.hashCode());
        Object $wxJin = this.getWxJin();
        result = result * 59 + (($wxJin == null) ? 43 : $wxJin.hashCode());
        Object $wxMu = this.getWxMu();
        result = result * 59 + (($wxMu == null) ? 43 : $wxMu.hashCode());
        Object $wxShui = this.getWxShui();
        result = result * 59 + (($wxShui == null) ? 43 : $wxShui.hashCode());
        Object $wxHuo = this.getWxHuo();
        result = result * 59 + (($wxHuo == null) ? 43 : $wxHuo.hashCode());
        Object $wxTu = this.getWxTu();
        result = result * 59 + (($wxTu == null) ? 43 : $wxTu.hashCode());
        Object $levelMode = this.getLevelMode();
        result = result * 59 + (($levelMode == null) ? 43 : $levelMode.hashCode());
        Object $grade = this.getGrade();
        result = result * 59 + (($grade == null) ? 43 : $grade.hashCode());
        Object $des = this.getDes();
        result = result * 59 + (($des == null) ? 43 : $des.hashCode());
        long $fsGjGl = Double.doubleToLongBits(this.getFsGjGl());
        result = result * 59 + (int)($fsGjGl >>> 32 ^ $fsGjGl);
        Object $jnSld = this.getJnSld();
        result = result * 59 + (($jnSld == null) ? 43 : $jnSld.hashCode());
        long $lwGjGl = Double.doubleToLongBits(this.getLwGjGl());
        result = result * 59 + (int)($lwGjGl >>> 32 ^ $lwGjGl);
        Object $escapeRound = this.getEscapeRound();
        result = result * 59 + (($escapeRound == null) ? 43 : $escapeRound.hashCode());
        long $escapeRatio = Double.doubleToLongBits(this.getEscapeRatio());
        result = result * 59 + (int)($escapeRatio >>> 32 ^ $escapeRatio);
        long $cEscapeRatio = Double.doubleToLongBits(this.getCEscapeRatio());
        result = result * 59 + (int)($cEscapeRatio >>> 32 ^ $cEscapeRatio);
        long $pdeFend = Double.doubleToLongBits(this.getPdeFend());
        result = result * 59 + (int)($pdeFend >>> 32 ^ $pdeFend);
        long $pPofangPro = Double.doubleToLongBits(this.getPPofangPro());
        result = result * 59 + (int)($pPofangPro >>> 32 ^ $pPofangPro);
        long $pPofang = Double.doubleToLongBits(this.getPPofang());
        result = result * 59 + (int)($pPofang >>> 32 ^ $pPofang);
        long $pFanZhen = Double.doubleToLongBits(this.getPFanZhen());
        result = result * 59 + (int)($pFanZhen >>> 32 ^ $pFanZhen);
        long $pFanZhenPro = Double.doubleToLongBits(this.getPFanZhenPro());
        result = result * 59 + (int)($pFanZhenPro >>> 32 ^ $pFanZhenPro);
        long $fPdeFend = Double.doubleToLongBits(this.getFPdeFend());
        result = result * 59 + (int)($fPdeFend >>> 32 ^ $fPdeFend);
        long $pAcc = Double.doubleToLongBits(this.getPAcc());
        result = result * 59 + (int)($pAcc >>> 32 ^ $pAcc);
        long $pSbl = Double.doubleToLongBits(this.getPSbl());
        result = result * 59 + (int)($pSbl >>> 32 ^ $pSbl);
        long $pKuangBao = Double.doubleToLongBits(this.getPKuangBao());
        result = result * 59 + (int)($pKuangBao >>> 32 ^ $pKuangBao);
        long $fengOdd = Double.doubleToLongBits(this.getFengOdd());
        result = result * 59 + (int)($fengOdd >>> 32 ^ $fengOdd);
        long $leiOdd = Double.doubleToLongBits(this.getLeiOdd());
        result = result * 59 + (int)($leiOdd >>> 32 ^ $leiOdd);
        long $shuiOdd = Double.doubleToLongBits(this.getShuiOdd());
        result = result * 59 + (int)($shuiOdd >>> 32 ^ $shuiOdd);
        long $huoOdd = Double.doubleToLongBits(this.getHuoOdd());
        result = result * 59 + (int)($huoOdd >>> 32 ^ $huoOdd);
        long $guiHuoOdd = Double.doubleToLongBits(this.getGuiHuoOdd());
        result = result * 59 + (int)($guiHuoOdd >>> 32 ^ $guiHuoOdd);
        long $sanShiOdd = Double.doubleToLongBits(this.getSanShiOdd());
        result = result * 59 + (int)($sanShiOdd >>> 32 ^ $sanShiOdd);
        long $pLjPro = Double.doubleToLongBits(this.getPLjPro());
        result = result * 59 + (int)($pLjPro >>> 32 ^ $pLjPro);
        Object $pLjTimes = this.getPLjTimes();
        result = result * 59 + (($pLjTimes == null) ? 43 : $pLjTimes.hashCode());
        long $pWlFjPro = Double.doubleToLongBits(this.getPWlFjPro());
        result = result * 59 + (int)($pWlFjPro >>> 32 ^ $pWlFjPro);
        Object $pWlFjTimes = this.getPWlFjTimes();
        result = result * 59 + (($pWlFjTimes == null) ? 43 : $pWlFjTimes.hashCode());
        long $kHunLuan = Double.doubleToLongBits(this.getKHunLuan());
        result = result * 59 + (int)($kHunLuan >>> 32 ^ $kHunLuan);
        long $kFengYin = Double.doubleToLongBits(this.getKFengYin());
        result = result * 59 + (int)($kFengYin >>> 32 ^ $kFengYin);
        long $kHunShui = Double.doubleToLongBits(this.getKHunShui());
        result = result * 59 + (int)($kHunShui >>> 32 ^ $kHunShui);
        long $kZhongDu = Double.doubleToLongBits(this.getKZhongDu());
        result = result * 59 + (int)($kZhongDu >>> 32 ^ $kZhongDu);
        long $kYiWang = Double.doubleToLongBits(this.getKYiWang());
        result = result * 59 + (int)($kYiWang >>> 32 ^ $kYiWang);
        long $kGuiHuo = Double.doubleToLongBits(this.getKGuiHuo());
        result = result * 59 + (int)($kGuiHuo >>> 32 ^ $kGuiHuo);
        long $kSanShi = Double.doubleToLongBits(this.getKSanShi());
        result = result * 59 + (int)($kSanShi >>> 32 ^ $kSanShi);
        long $kShuaiRuo = Double.doubleToLongBits(this.getKShuaiRuo());
        result = result * 59 + (int)($kShuaiRuo >>> 32 ^ $kShuaiRuo);
        long $kZhenShe = Double.doubleToLongBits(this.getKZhenShe());
        result = result * 59 + (int)($kZhenShe >>> 32 ^ $kZhenShe);
        long $kFeng = Double.doubleToLongBits(this.getKFeng());
        result = result * 59 + (int)($kFeng >>> 32 ^ $kFeng);
        long $kHuo = Double.doubleToLongBits(this.getKHuo());
        result = result * 59 + (int)($kHuo >>> 32 ^ $kHuo);
        long $kShui = Double.doubleToLongBits(this.getKShui());
        result = result * 59 + (int)($kShui >>> 32 ^ $kShui);
        long $kLei = Double.doubleToLongBits(this.getKLei());
        result = result * 59 + (int)($kLei >>> 32 ^ $kLei);
        long $fkFeng = Double.doubleToLongBits(this.getFkFeng());
        result = result * 59 + (int)($fkFeng >>> 32 ^ $fkFeng);
        long $fkHuo = Double.doubleToLongBits(this.getFkHuo());
        result = result * 59 + (int)($fkHuo >>> 32 ^ $fkHuo);
        long $fkShui = Double.doubleToLongBits(this.getFkShui());
        result = result * 59 + (int)($fkShui >>> 32 ^ $fkShui);
        long $fkLei = Double.doubleToLongBits(this.getFkLei());
        result = result * 59 + (int)($fkLei >>> 32 ^ $fkLei);
        long $fkZhongDu = Double.doubleToLongBits(this.getFkZhongDu());
        result = result * 59 + (int)($fkZhongDu >>> 32 ^ $fkZhongDu);
        long $fkHunLuan = Double.doubleToLongBits(this.getFkHunLuan());
        result = result * 59 + (int)($fkHunLuan >>> 32 ^ $fkHunLuan);
        long $fkFengYin = Double.doubleToLongBits(this.getFkFengYin());
        result = result * 59 + (int)($fkFengYin >>> 32 ^ $fkFengYin);
        long $fkHunShui = Double.doubleToLongBits(this.getFkHunShui());
        result = result * 59 + (int)($fkHunShui >>> 32 ^ $fkHunShui);
        long $fkYiWang = Double.doubleToLongBits(this.getFkYiWang());
        result = result * 59 + (int)($fkYiWang >>> 32 ^ $fkYiWang);
        long $fkGuiHuo = Double.doubleToLongBits(this.getFkGuiHuo());
        result = result * 59 + (int)($fkGuiHuo >>> 32 ^ $fkGuiHuo);
        long $fkZhenShe = Double.doubleToLongBits(this.getFkZhenShe());
        result = result * 59 + (int)($fkZhenShe >>> 32 ^ $fkZhenShe);
        long $fkSanShi = Double.doubleToLongBits(this.getFkSanShi());
        result = result * 59 + (int)($fkSanShi >>> 32 ^ $fkSanShi);
        result = result * 59 + this.getPos();
        Object $selectParameterList = this.getSelectParameterList();
        result = result * 59 + (($selectParameterList == null) ? 43 : $selectParameterList.hashCode());
        Object $itemUseParameterList = this.getItemUseParameterList();
        result = result * 59 + (($itemUseParameterList == null) ? 43 : $itemUseParameterList.hashCode());
        result = result * 59 + this.getAttr();
        result = result * 59 + this.getType();
        long $adAtkEhan = Double.doubleToLongBits(this.getAdAtkEhan());
        result = result * 59 + (int)($adAtkEhan >>> 32 ^ $adAtkEhan);
        long $breakEhan = Double.doubleToLongBits(this.getBreakEhan());
        result = result * 59 + (int)($breakEhan >>> 32 ^ $breakEhan);
        long $charmEhan = Double.doubleToLongBits(this.getCharmEhan());
        result = result * 59 + (int)($charmEhan >>> 32 ^ $charmEhan);
        long $cureEhan = Double.doubleToLongBits(this.getCureEhan());
        result = result * 59 + (int)($cureEhan >>> 32 ^ $cureEhan);
        long $disorderEhan = Double.doubleToLongBits(this.getDisorderEhan());
        result = result * 59 + (int)($disorderEhan >>> 32 ^ $disorderEhan);
        long $gfireEhan = Double.doubleToLongBits(this.getGfireEhan());
        result = result * 59 + (int)($gfireEhan >>> 32 ^ $gfireEhan);
        long $rainEhan = Double.doubleToLongBits(this.getRainEhan());
        result = result * 59 + (int)($rainEhan >>> 32 ^ $rainEhan);
        long $recoverEhan = Double.doubleToLongBits(this.getRecoverEhan());
        result = result * 59 + (int)($recoverEhan >>> 32 ^ $recoverEhan);
        long $shockEhan = Double.doubleToLongBits(this.getShockEhan());
        result = result * 59 + (int)($shockEhan >>> 32 ^ $shockEhan);
        long $windEhan = Double.doubleToLongBits(this.getWindEhan());
        result = result * 59 + (int)($windEhan >>> 32 ^ $windEhan);
        long $fireEhan = Double.doubleToLongBits(this.getFireEhan());
        result = result * 59 + (int)($fireEhan >>> 32 ^ $fireEhan);
        long $thunderEhan = Double.doubleToLongBits(this.getThunderEhan());
        result = result * 59 + (int)($thunderEhan >>> 32 ^ $thunderEhan);
        long $poisonEhan = Double.doubleToLongBits(this.getPoisonEhan());
        result = result * 59 + (int)($poisonEhan >>> 32 ^ $poisonEhan);
        long $sealEhan = Double.doubleToLongBits(this.getSealEhan());
        result = result * 59 + (int)($sealEhan >>> 32 ^ $sealEhan);
        long $sleepEhan = Double.doubleToLongBits(this.getSleepEhan());
        result = result * 59 + (int)($sleepEhan >>> 32 ^ $sleepEhan);
        long $forgetEhan = Double.doubleToLongBits(this.getForgetEhan());
        result = result * 59 + (int)($forgetEhan >>> 32 ^ $forgetEhan);
        long $adDefEhan = Double.doubleToLongBits(this.getAdDefEhan());
        result = result * 59 + (int)($adDefEhan >>> 32 ^ $adDefEhan);
        long $adSpdEhan = Double.doubleToLongBits(this.getAdSpdEhan());
        result = result * 59 + (int)($adSpdEhan >>> 32 ^ $adSpdEhan);
        long $sweepEhan = Double.doubleToLongBits(this.getSweepEhan());
        result = result * 59 + (int)($sweepEhan >>> 32 ^ $sweepEhan);
        long $thudEhan = Double.doubleToLongBits(this.getThudEhan());
        result = result * 59 + (int)($thudEhan >>> 32 ^ $thudEhan);
        long $sanshiEhan = Double.doubleToLongBits(this.getSanshiEhan());
        result = result * 59 + (int)($sanshiEhan >>> 32 ^ $sanshiEhan);
        long $conFire = Double.doubleToLongBits(this.getConFire());
        result = result * 59 + (int)($conFire >>> 32 ^ $conFire);
        long $conGold = Double.doubleToLongBits(this.getConGold());
        result = result * 59 + (int)($conGold >>> 32 ^ $conGold);
        long $conSoil = Double.doubleToLongBits(this.getConSoil());
        result = result * 59 + (int)($conSoil >>> 32 ^ $conSoil);
        long $conWater = Double.doubleToLongBits(this.getConWater());
        result = result * 59 + (int)($conWater >>> 32 ^ $conWater);
        long $conWood = Double.doubleToLongBits(this.getConWood());
        result = result * 59 + (int)($conWood >>> 32 ^ $conWood);
        Object $powerHp = this.getPowerHp();
        result = result * 59 + (($powerHp == null) ? 43 : $powerHp.hashCode());
        Object $powerAp = this.getPowerAp();
        result = result * 59 + (($powerAp == null) ? 43 : $powerAp.hashCode());
        Object $powerSp = this.getPowerSp();
        result = result * 59 + (($powerSp == null) ? 43 : $powerSp.hashCode());
        Object $powerAttr = this.getPowerAttr();
        result = result * 59 + (($powerAttr == null) ? 43 : $powerAttr.hashCode());
        result = result * 59 + this.getVanish();
        result = result * 59 + this.getMonsterType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataMonster(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", close=" + this.getClose() + ", skills=" + this.getSkills() + ", judgedYe=" + this.getJudgedYe() + ", pSkills=" + this.getPSkills() + ", shape=" + this.getShape() + ", fightTalk=" + this.getFightTalk() + ", isBoss=" + this.getIsBoss() + ", hp=" + this.getHp() + ", mp=" + this.getMp() + ", ap=" + this.getAp() + ", sp=" + this.getSp() + ", wxJin=" + this.getWxJin() + ", wxMu=" + this.getWxMu() + ", wxShui=" + this.getWxShui() + ", wxHuo=" + this.getWxHuo() + ", wxTu=" + this.getWxTu() + ", levelMode=" + this.getLevelMode() + ", grade=" + this.getGrade() + ", des=" + this.getDes() + ", fsGjGl=" + this.getFsGjGl() + ", jnSld=" + this.getJnSld() + ", lwGjGl=" + this.getLwGjGl() + ", escapeRound=" + this.getEscapeRound() + ", escapeRatio=" + this.getEscapeRatio() + ", cEscapeRatio=" + this.getCEscapeRatio() + ", pdeFend=" + this.getPdeFend() + ", pPofangPro=" + this.getPPofangPro() + ", pPofang=" + this.getPPofang() + ", pFanZhen=" + this.getPFanZhen() + ", pFanZhenPro=" + this.getPFanZhenPro() + ", fPdeFend=" + this.getFPdeFend() + ", pAcc=" + this.getPAcc() + ", pSbl=" + this.getPSbl() + ", pKuangBao=" + this.getPKuangBao() + ", fengOdd=" + this.getFengOdd() + ", leiOdd=" + this.getLeiOdd() + ", shuiOdd=" + this.getShuiOdd() + ", huoOdd=" + this.getHuoOdd() + ", guiHuoOdd=" + this.getGuiHuoOdd() + ", sanShiOdd=" + this.getSanShiOdd() + ", pLjPro=" + this.getPLjPro() + ", pLjTimes=" + this.getPLjTimes() + ", pWlFjPro=" + this.getPWlFjPro() + ", pWlFjTimes=" + this.getPWlFjTimes() + ", kHunLuan=" + this.getKHunLuan() + ", kFengYin=" + this.getKFengYin() + ", kHunShui=" + this.getKHunShui() + ", kZhongDu=" + this.getKZhongDu() + ", kYiWang=" + this.getKYiWang() + ", kGuiHuo=" + this.getKGuiHuo() + ", kSanShi=" + this.getKSanShi() + ", kShuaiRuo=" + this.getKShuaiRuo() + ", kZhenShe=" + this.getKZhenShe() + ", kFeng=" + this.getKFeng() + ", kHuo=" + this.getKHuo() + ", kShui=" + this.getKShui() + ", kLei=" + this.getKLei() + ", fkFeng=" + this.getFkFeng() + ", fkHuo=" + this.getFkHuo() + ", fkShui=" + this.getFkShui() + ", fkLei=" + this.getFkLei() + ", fkZhongDu=" + this.getFkZhongDu() + ", fkHunLuan=" + this.getFkHunLuan() + ", fkFengYin=" + this.getFkFengYin() + ", fkHunShui=" + this.getFkHunShui() + ", fkYiWang=" + this.getFkYiWang() + ", fkGuiHuo=" + this.getFkGuiHuo() + ", fkZhenShe=" + this.getFkZhenShe() + ", fkSanShi=" + this.getFkSanShi() + ", pos=" + this.getPos() + ", selectParameterList=" + this.getSelectParameterList() + ", itemUseParameterList=" + this.getItemUseParameterList() + ", attr=" + (int)this.getAttr() + ", type=" + this.getType() + ", adAtkEhan=" + this.getAdAtkEhan() + ", breakEhan=" + this.getBreakEhan() + ", charmEhan=" + this.getCharmEhan() + ", cureEhan=" + this.getCureEhan() + ", disorderEhan=" + this.getDisorderEhan() + ", gfireEhan=" + this.getGfireEhan() + ", rainEhan=" + this.getRainEhan() + ", recoverEhan=" + this.getRecoverEhan() + ", shockEhan=" + this.getShockEhan() + ", windEhan=" + this.getWindEhan() + ", fireEhan=" + this.getFireEhan() + ", thunderEhan=" + this.getThunderEhan() + ", poisonEhan=" + this.getPoisonEhan() + ", sealEhan=" + this.getSealEhan() + ", sleepEhan=" + this.getSleepEhan() + ", forgetEhan=" + this.getForgetEhan() + ", adDefEhan=" + this.getAdDefEhan() + ", adSpdEhan=" + this.getAdSpdEhan() + ", sweepEhan=" + this.getSweepEhan() + ", thudEhan=" + this.getThudEhan() + ", sanshiEhan=" + this.getSanshiEhan() + ", conFire=" + this.getConFire() + ", conGold=" + this.getConGold() + ", conSoil=" + this.getConSoil() + ", conWater=" + this.getConWater() + ", conWood=" + this.getConWood() + ", powerHp=" + this.getPowerHp() + ", powerAp=" + this.getPowerAp() + ", powerSp=" + this.getPowerSp() + ", powerAttr=" + this.getPowerAttr() + ", vanish=" + this.getVanish() + ", monsterType=" + this.getMonsterType() + ")";
    }
}
