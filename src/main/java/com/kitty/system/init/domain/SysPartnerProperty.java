package com.kitty.system.init.domain;

import java.util.HashMap;
import org.nutz.dao.entity.annotation.Default;
import java.util.Map;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Table("data_partner_property")
public class SysPartnerProperty
{
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
    private Map<Integer, Integer> extSkillInfo;
    @Comment("")
    @Column("skill_info")
    @Default("{}")
    private Map<Integer, Integer> skillInfo;
    @Comment("")
    @Column("fight_ai")
    private String fightAi;
    
    public SysPartnerProperty() {
        this.extSkillInfo = new HashMap();
        this.skillInfo = new HashMap();
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getPartnerSerial() {
        return this.partnerSerial;
    }
    
    public int getRace() {
        return this.race;
    }
    
    public int getSex() {
        return this.sex;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public int getBasecon() {
        return this.basecon;
    }
    
    public int getWakan() {
        return this.wakan;
    }
    
    public int getAtk() {
        return this.atk;
    }
    
    public int getAgility() {
        return this.agility;
    }
    
    public int getPower() {
        return this.power;
    }
    
    public int getSpeed() {
        return this.speed;
    }
    
    public int getHpMax() {
        return this.hpMax;
    }
    
    public int getFire() {
        return this.fire;
    }
    
    public int getGold() {
        return this.gold;
    }
    
    public int getSoil() {
        return this.soil;
    }
    
    public int getWater() {
        return this.water;
    }
    
    public int getWood() {
        return this.wood;
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
    
    public double getPhyDef() {
        return this.phyDef;
    }
    
    public double getPhyDefNef() {
        return this.phyDefNef;
    }
    
    public double getFatalRate() {
        return this.fatalRate;
    }
    
    public int getHitCombo() {
        return this.hitCombo;
    }
    
    public double getHitComboRate() {
        return this.hitComboRate;
    }
    
    public double getHitRate() {
        return this.hitRate;
    }
    
    public double getVoidRate() {
        return this.voidRate;
    }
    
    public double getPhyDefNefRate() {
        return this.phyDefNefRate;
    }
    
    public double getRainRageOdds() {
        return this.rainRageOdds;
    }
    
    public double getFireRageOdds() {
        return this.fireRageOdds;
    }
    
    public double getWindRageOdds() {
        return this.windRageOdds;
    }
    
    public double getThunderRageOdds() {
        return this.thunderRageOdds;
    }
    
    public double getGfireRageOdds() {
        return this.gfireRageOdds;
    }
    
    public double getShanshiRageOdds() {
        return this.shanshiRageOdds;
    }
    
    public double getRainEhan() {
        return this.rainEhan;
    }
    
    public double getFireEhan() {
        return this.fireEhan;
    }
    
    public double getWindEhan() {
        return this.windEhan;
    }
    
    public double getThunderEhan() {
        return this.thunderEhan;
    }
    
    public int getRecoverEhan() {
        return this.recoverEhan;
    }
    
    public double getSanshiEhan() {
        return this.sanshiEhan;
    }
    
    public double getBreakEhan() {
        return this.breakEhan;
    }
    
    public double getAdAtkEhan() {
        return this.adAtkEhan;
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
    
    public double getDisorderEhan() {
        return this.disorderEhan;
    }
    
    public double getSealEhan() {
        return this.sealEhan;
    }
    
    public double getPoisonEhan() {
        return this.poisonEhan;
    }
    
    public double getSleepEhan() {
        return this.sleepEhan;
    }
    
    public double getForgetEhan() {
        return this.forgetEhan;
    }
    
    public double getCharmEhan() {
        return this.charmEhan;
    }
    
    public double getCureEhan() {
        return this.cureEhan;
    }
    
    public double getShockEhan() {
        return this.shockEhan;
    }
    
    public double getGfireEhan() {
        return this.gfireEhan;
    }
    
    public double getRainDef() {
        return this.rainDef;
    }
    
    public double getFireDef() {
        return this.fireDef;
    }
    
    public double getWindDef() {
        return this.windDef;
    }
    
    public double getThunderDef() {
        return this.thunderDef;
    }
    
    public double getDisorderDef() {
        return this.disorderDef;
    }
    
    public double getSealDef() {
        return this.sealDef;
    }
    
    public double getSleepDef() {
        return this.sleepDef;
    }
    
    public double getForgetDef() {
        return this.forgetDef;
    }
    
    public double getPoisonDef() {
        return this.poisonDef;
    }
    
    public double getShockDef() {
        return this.shockDef;
    }
    
    public double getGfireDef() {
        return this.gfireDef;
    }
    
    public double getSanshiDef() {
        return this.sanshiDef;
    }
    
    public double getRainDefNeg() {
        return this.rainDefNeg;
    }
    
    public double getFireDefNeg() {
        return this.fireDefNeg;
    }
    
    public double getWindDefNeg() {
        return this.windDefNeg;
    }
    
    public double getThunderDefNeg() {
        return this.thunderDefNeg;
    }
    
    public double getDisorderDefNeg() {
        return this.disorderDefNeg;
    }
    
    public double getShockDefNeg() {
        return this.shockDefNeg;
    }
    
    public double getGfireDefNeg() {
        return this.gfireDefNeg;
    }
    
    public double getSanshiDefNeg() {
        return this.sanshiDefNeg;
    }
    
    public int getSkillId() {
        return this.skillId;
    }
    
    public Map<Integer, Integer> getExtSkillInfo() {
        return this.extSkillInfo;
    }
    
    public Map<Integer, Integer> getSkillInfo() {
        return this.skillInfo;
    }
    
    public String getFightAi() {
        return this.fightAi;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setPartnerSerial(final int partnerSerial) {
        this.partnerSerial = partnerSerial;
    }
    
    public void setRace(final int race) {
        this.race = race;
    }
    
    public void setSex(final int sex) {
        this.sex = sex;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setBasecon(final int basecon) {
        this.basecon = basecon;
    }
    
    public void setWakan(final int wakan) {
        this.wakan = wakan;
    }
    
    public void setAtk(final int atk) {
        this.atk = atk;
    }
    
    public void setAgility(final int agility) {
        this.agility = agility;
    }
    
    public void setPower(final int power) {
        this.power = power;
    }
    
    public void setSpeed(final int speed) {
        this.speed = speed;
    }
    
    public void setHpMax(final int hpMax) {
        this.hpMax = hpMax;
    }
    
    public void setFire(final int fire) {
        this.fire = fire;
    }
    
    public void setGold(final int gold) {
        this.gold = gold;
    }
    
    public void setSoil(final int soil) {
        this.soil = soil;
    }
    
    public void setWater(final int water) {
        this.water = water;
    }
    
    public void setWood(final int wood) {
        this.wood = wood;
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
    
    public void setPhyDef(final double phyDef) {
        this.phyDef = phyDef;
    }
    
    public void setPhyDefNef(final double phyDefNef) {
        this.phyDefNef = phyDefNef;
    }
    
    public void setFatalRate(final double fatalRate) {
        this.fatalRate = fatalRate;
    }
    
    public void setHitCombo(final int hitCombo) {
        this.hitCombo = hitCombo;
    }
    
    public void setHitComboRate(final double hitComboRate) {
        this.hitComboRate = hitComboRate;
    }
    
    public void setHitRate(final double hitRate) {
        this.hitRate = hitRate;
    }
    
    public void setVoidRate(final double voidRate) {
        this.voidRate = voidRate;
    }
    
    public void setPhyDefNefRate(final double phyDefNefRate) {
        this.phyDefNefRate = phyDefNefRate;
    }
    
    public void setRainRageOdds(final double rainRageOdds) {
        this.rainRageOdds = rainRageOdds;
    }
    
    public void setFireRageOdds(final double fireRageOdds) {
        this.fireRageOdds = fireRageOdds;
    }
    
    public void setWindRageOdds(final double windRageOdds) {
        this.windRageOdds = windRageOdds;
    }
    
    public void setThunderRageOdds(final double thunderRageOdds) {
        this.thunderRageOdds = thunderRageOdds;
    }
    
    public void setGfireRageOdds(final double gfireRageOdds) {
        this.gfireRageOdds = gfireRageOdds;
    }
    
    public void setShanshiRageOdds(final double shanshiRageOdds) {
        this.shanshiRageOdds = shanshiRageOdds;
    }
    
    public void setRainEhan(final double rainEhan) {
        this.rainEhan = rainEhan;
    }
    
    public void setFireEhan(final double fireEhan) {
        this.fireEhan = fireEhan;
    }
    
    public void setWindEhan(final double windEhan) {
        this.windEhan = windEhan;
    }
    
    public void setThunderEhan(final double thunderEhan) {
        this.thunderEhan = thunderEhan;
    }
    
    public void setRecoverEhan(final int recoverEhan) {
        this.recoverEhan = recoverEhan;
    }
    
    public void setSanshiEhan(final double sanshiEhan) {
        this.sanshiEhan = sanshiEhan;
    }
    
    public void setBreakEhan(final double breakEhan) {
        this.breakEhan = breakEhan;
    }
    
    public void setAdAtkEhan(final double adAtkEhan) {
        this.adAtkEhan = adAtkEhan;
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
    
    public void setDisorderEhan(final double disorderEhan) {
        this.disorderEhan = disorderEhan;
    }
    
    public void setSealEhan(final double sealEhan) {
        this.sealEhan = sealEhan;
    }
    
    public void setPoisonEhan(final double poisonEhan) {
        this.poisonEhan = poisonEhan;
    }
    
    public void setSleepEhan(final double sleepEhan) {
        this.sleepEhan = sleepEhan;
    }
    
    public void setForgetEhan(final double forgetEhan) {
        this.forgetEhan = forgetEhan;
    }
    
    public void setCharmEhan(final double charmEhan) {
        this.charmEhan = charmEhan;
    }
    
    public void setCureEhan(final double cureEhan) {
        this.cureEhan = cureEhan;
    }
    
    public void setShockEhan(final double shockEhan) {
        this.shockEhan = shockEhan;
    }
    
    public void setGfireEhan(final double gfireEhan) {
        this.gfireEhan = gfireEhan;
    }
    
    public void setRainDef(final double rainDef) {
        this.rainDef = rainDef;
    }
    
    public void setFireDef(final double fireDef) {
        this.fireDef = fireDef;
    }
    
    public void setWindDef(final double windDef) {
        this.windDef = windDef;
    }
    
    public void setThunderDef(final double thunderDef) {
        this.thunderDef = thunderDef;
    }
    
    public void setDisorderDef(final double disorderDef) {
        this.disorderDef = disorderDef;
    }
    
    public void setSealDef(final double sealDef) {
        this.sealDef = sealDef;
    }
    
    public void setSleepDef(final double sleepDef) {
        this.sleepDef = sleepDef;
    }
    
    public void setForgetDef(final double forgetDef) {
        this.forgetDef = forgetDef;
    }
    
    public void setPoisonDef(final double poisonDef) {
        this.poisonDef = poisonDef;
    }
    
    public void setShockDef(final double shockDef) {
        this.shockDef = shockDef;
    }
    
    public void setGfireDef(final double gfireDef) {
        this.gfireDef = gfireDef;
    }
    
    public void setSanshiDef(final double sanshiDef) {
        this.sanshiDef = sanshiDef;
    }
    
    public void setRainDefNeg(final double rainDefNeg) {
        this.rainDefNeg = rainDefNeg;
    }
    
    public void setFireDefNeg(final double fireDefNeg) {
        this.fireDefNeg = fireDefNeg;
    }
    
    public void setWindDefNeg(final double windDefNeg) {
        this.windDefNeg = windDefNeg;
    }
    
    public void setThunderDefNeg(final double thunderDefNeg) {
        this.thunderDefNeg = thunderDefNeg;
    }
    
    public void setDisorderDefNeg(final double disorderDefNeg) {
        this.disorderDefNeg = disorderDefNeg;
    }
    
    public void setShockDefNeg(final double shockDefNeg) {
        this.shockDefNeg = shockDefNeg;
    }
    
    public void setGfireDefNeg(final double gfireDefNeg) {
        this.gfireDefNeg = gfireDefNeg;
    }
    
    public void setSanshiDefNeg(final double sanshiDefNeg) {
        this.sanshiDefNeg = sanshiDefNeg;
    }
    
    public void setSkillId(final int skillId) {
        this.skillId = skillId;
    }
    
    public void setExtSkillInfo(final Map<Integer, Integer> extSkillInfo) {
        this.extSkillInfo = extSkillInfo;
    }
    
    public void setSkillInfo(final Map<Integer, Integer> skillInfo) {
        this.skillInfo = skillInfo;
    }
    
    public void setFightAi(final String fightAi) {
        this.fightAi = fightAi;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPartnerProperty)) {
            return false;
        }
        SysPartnerProperty other = (SysPartnerProperty)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getPartnerSerial() != other.getPartnerSerial()) {
            return false;
        }
        if (this.getRace() != other.getRace()) {
            return false;
        }
        if (this.getSex() != other.getSex()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getBasecon() != other.getBasecon()) {
            return false;
        }
        if (this.getWakan() != other.getWakan()) {
            return false;
        }
        if (this.getAtk() != other.getAtk()) {
            return false;
        }
        if (this.getAgility() != other.getAgility()) {
            return false;
        }
        if (this.getPower() != other.getPower()) {
            return false;
        }
        if (this.getSpeed() != other.getSpeed()) {
            return false;
        }
        if (this.getHpMax() != other.getHpMax()) {
            return false;
        }
        if (this.getFire() != other.getFire()) {
            return false;
        }
        if (this.getGold() != other.getGold()) {
            return false;
        }
        if (this.getSoil() != other.getSoil()) {
            return false;
        }
        if (this.getWater() != other.getWater()) {
            return false;
        }
        if (this.getWood() != other.getWood()) {
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
        if (Double.compare(this.getPhyDef(), other.getPhyDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getPhyDefNef(), other.getPhyDefNef()) != 0) {
            return false;
        }
        if (Double.compare(this.getFatalRate(), other.getFatalRate()) != 0) {
            return false;
        }
        if (this.getHitCombo() != other.getHitCombo()) {
            return false;
        }
        if (Double.compare(this.getHitComboRate(), other.getHitComboRate()) != 0) {
            return false;
        }
        if (Double.compare(this.getHitRate(), other.getHitRate()) != 0) {
            return false;
        }
        if (Double.compare(this.getVoidRate(), other.getVoidRate()) != 0) {
            return false;
        }
        if (Double.compare(this.getPhyDefNefRate(), other.getPhyDefNefRate()) != 0) {
            return false;
        }
        if (Double.compare(this.getRainRageOdds(), other.getRainRageOdds()) != 0) {
            return false;
        }
        if (Double.compare(this.getFireRageOdds(), other.getFireRageOdds()) != 0) {
            return false;
        }
        if (Double.compare(this.getWindRageOdds(), other.getWindRageOdds()) != 0) {
            return false;
        }
        if (Double.compare(this.getThunderRageOdds(), other.getThunderRageOdds()) != 0) {
            return false;
        }
        if (Double.compare(this.getGfireRageOdds(), other.getGfireRageOdds()) != 0) {
            return false;
        }
        if (Double.compare(this.getShanshiRageOdds(), other.getShanshiRageOdds()) != 0) {
            return false;
        }
        if (Double.compare(this.getRainEhan(), other.getRainEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getFireEhan(), other.getFireEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getWindEhan(), other.getWindEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getThunderEhan(), other.getThunderEhan()) != 0) {
            return false;
        }
        if (this.getRecoverEhan() != other.getRecoverEhan()) {
            return false;
        }
        if (Double.compare(this.getSanshiEhan(), other.getSanshiEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getBreakEhan(), other.getBreakEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getAdAtkEhan(), other.getAdAtkEhan()) != 0) {
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
        if (Double.compare(this.getDisorderEhan(), other.getDisorderEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getSealEhan(), other.getSealEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getPoisonEhan(), other.getPoisonEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getSleepEhan(), other.getSleepEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getForgetEhan(), other.getForgetEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getCharmEhan(), other.getCharmEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getCureEhan(), other.getCureEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getShockEhan(), other.getShockEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getGfireEhan(), other.getGfireEhan()) != 0) {
            return false;
        }
        if (Double.compare(this.getRainDef(), other.getRainDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getFireDef(), other.getFireDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getWindDef(), other.getWindDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getThunderDef(), other.getThunderDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getDisorderDef(), other.getDisorderDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getSealDef(), other.getSealDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getSleepDef(), other.getSleepDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getForgetDef(), other.getForgetDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getPoisonDef(), other.getPoisonDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getShockDef(), other.getShockDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getGfireDef(), other.getGfireDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getSanshiDef(), other.getSanshiDef()) != 0) {
            return false;
        }
        if (Double.compare(this.getRainDefNeg(), other.getRainDefNeg()) != 0) {
            return false;
        }
        if (Double.compare(this.getFireDefNeg(), other.getFireDefNeg()) != 0) {
            return false;
        }
        if (Double.compare(this.getWindDefNeg(), other.getWindDefNeg()) != 0) {
            return false;
        }
        if (Double.compare(this.getThunderDefNeg(), other.getThunderDefNeg()) != 0) {
            return false;
        }
        if (Double.compare(this.getDisorderDefNeg(), other.getDisorderDefNeg()) != 0) {
            return false;
        }
        if (Double.compare(this.getShockDefNeg(), other.getShockDefNeg()) != 0) {
            return false;
        }
        if (Double.compare(this.getGfireDefNeg(), other.getGfireDefNeg()) != 0) {
            return false;
        }
        if (Double.compare(this.getSanshiDefNeg(), other.getSanshiDefNeg()) != 0) {
            return false;
        }
        if (this.getSkillId() != other.getSkillId()) {
            return false;
        }
        Object this$extSkillInfo = this.getExtSkillInfo();
        Object other$extSkillInfo = other.getExtSkillInfo();
        if (!((this$extSkillInfo != null) ? this$extSkillInfo.equals(other$extSkillInfo) : (other$extSkillInfo == null))) {
            return false;
        }
        Object this$skillInfo = this.getSkillInfo();
        Object other$skillInfo = other.getSkillInfo();
        if (!((this$skillInfo != null) ? this$skillInfo.equals(other$skillInfo) : (other$skillInfo == null))) {
            return false;
        }
        Object this$fightAi = this.getFightAi();
        Object other$fightAi = other.getFightAi();
        return (this$fightAi != null) ? this$fightAi.equals(other$fightAi) : (other$fightAi == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPartnerProperty;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getPartnerSerial();
        result = result * 59 + this.getRace();
        result = result * 59 + this.getSex();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getBasecon();
        result = result * 59 + this.getWakan();
        result = result * 59 + this.getAtk();
        result = result * 59 + this.getAgility();
        result = result * 59 + this.getPower();
        result = result * 59 + this.getSpeed();
        result = result * 59 + this.getHpMax();
        result = result * 59 + this.getFire();
        result = result * 59 + this.getGold();
        result = result * 59 + this.getSoil();
        result = result * 59 + this.getWater();
        result = result * 59 + this.getWood();
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
        long $phyDef = Double.doubleToLongBits(this.getPhyDef());
        result = result * 59 + (int)($phyDef >>> 32 ^ $phyDef);
        long $phyDefNef = Double.doubleToLongBits(this.getPhyDefNef());
        result = result * 59 + (int)($phyDefNef >>> 32 ^ $phyDefNef);
        long $fatalRate = Double.doubleToLongBits(this.getFatalRate());
        result = result * 59 + (int)($fatalRate >>> 32 ^ $fatalRate);
        result = result * 59 + this.getHitCombo();
        long $hitComboRate = Double.doubleToLongBits(this.getHitComboRate());
        result = result * 59 + (int)($hitComboRate >>> 32 ^ $hitComboRate);
        long $hitRate = Double.doubleToLongBits(this.getHitRate());
        result = result * 59 + (int)($hitRate >>> 32 ^ $hitRate);
        long $voidRate = Double.doubleToLongBits(this.getVoidRate());
        result = result * 59 + (int)($voidRate >>> 32 ^ $voidRate);
        long $phyDefNefRate = Double.doubleToLongBits(this.getPhyDefNefRate());
        result = result * 59 + (int)($phyDefNefRate >>> 32 ^ $phyDefNefRate);
        long $rainRageOdds = Double.doubleToLongBits(this.getRainRageOdds());
        result = result * 59 + (int)($rainRageOdds >>> 32 ^ $rainRageOdds);
        long $fireRageOdds = Double.doubleToLongBits(this.getFireRageOdds());
        result = result * 59 + (int)($fireRageOdds >>> 32 ^ $fireRageOdds);
        long $windRageOdds = Double.doubleToLongBits(this.getWindRageOdds());
        result = result * 59 + (int)($windRageOdds >>> 32 ^ $windRageOdds);
        long $thunderRageOdds = Double.doubleToLongBits(this.getThunderRageOdds());
        result = result * 59 + (int)($thunderRageOdds >>> 32 ^ $thunderRageOdds);
        long $gfireRageOdds = Double.doubleToLongBits(this.getGfireRageOdds());
        result = result * 59 + (int)($gfireRageOdds >>> 32 ^ $gfireRageOdds);
        long $shanshiRageOdds = Double.doubleToLongBits(this.getShanshiRageOdds());
        result = result * 59 + (int)($shanshiRageOdds >>> 32 ^ $shanshiRageOdds);
        long $rainEhan = Double.doubleToLongBits(this.getRainEhan());
        result = result * 59 + (int)($rainEhan >>> 32 ^ $rainEhan);
        long $fireEhan = Double.doubleToLongBits(this.getFireEhan());
        result = result * 59 + (int)($fireEhan >>> 32 ^ $fireEhan);
        long $windEhan = Double.doubleToLongBits(this.getWindEhan());
        result = result * 59 + (int)($windEhan >>> 32 ^ $windEhan);
        long $thunderEhan = Double.doubleToLongBits(this.getThunderEhan());
        result = result * 59 + (int)($thunderEhan >>> 32 ^ $thunderEhan);
        result = result * 59 + this.getRecoverEhan();
        long $sanshiEhan = Double.doubleToLongBits(this.getSanshiEhan());
        result = result * 59 + (int)($sanshiEhan >>> 32 ^ $sanshiEhan);
        long $breakEhan = Double.doubleToLongBits(this.getBreakEhan());
        result = result * 59 + (int)($breakEhan >>> 32 ^ $breakEhan);
        long $adAtkEhan = Double.doubleToLongBits(this.getAdAtkEhan());
        result = result * 59 + (int)($adAtkEhan >>> 32 ^ $adAtkEhan);
        long $adDefEhan = Double.doubleToLongBits(this.getAdDefEhan());
        result = result * 59 + (int)($adDefEhan >>> 32 ^ $adDefEhan);
        long $adSpdEhan = Double.doubleToLongBits(this.getAdSpdEhan());
        result = result * 59 + (int)($adSpdEhan >>> 32 ^ $adSpdEhan);
        long $sweepEhan = Double.doubleToLongBits(this.getSweepEhan());
        result = result * 59 + (int)($sweepEhan >>> 32 ^ $sweepEhan);
        long $thudEhan = Double.doubleToLongBits(this.getThudEhan());
        result = result * 59 + (int)($thudEhan >>> 32 ^ $thudEhan);
        long $disorderEhan = Double.doubleToLongBits(this.getDisorderEhan());
        result = result * 59 + (int)($disorderEhan >>> 32 ^ $disorderEhan);
        long $sealEhan = Double.doubleToLongBits(this.getSealEhan());
        result = result * 59 + (int)($sealEhan >>> 32 ^ $sealEhan);
        long $poisonEhan = Double.doubleToLongBits(this.getPoisonEhan());
        result = result * 59 + (int)($poisonEhan >>> 32 ^ $poisonEhan);
        long $sleepEhan = Double.doubleToLongBits(this.getSleepEhan());
        result = result * 59 + (int)($sleepEhan >>> 32 ^ $sleepEhan);
        long $forgetEhan = Double.doubleToLongBits(this.getForgetEhan());
        result = result * 59 + (int)($forgetEhan >>> 32 ^ $forgetEhan);
        long $charmEhan = Double.doubleToLongBits(this.getCharmEhan());
        result = result * 59 + (int)($charmEhan >>> 32 ^ $charmEhan);
        long $cureEhan = Double.doubleToLongBits(this.getCureEhan());
        result = result * 59 + (int)($cureEhan >>> 32 ^ $cureEhan);
        long $shockEhan = Double.doubleToLongBits(this.getShockEhan());
        result = result * 59 + (int)($shockEhan >>> 32 ^ $shockEhan);
        long $gfireEhan = Double.doubleToLongBits(this.getGfireEhan());
        result = result * 59 + (int)($gfireEhan >>> 32 ^ $gfireEhan);
        long $rainDef = Double.doubleToLongBits(this.getRainDef());
        result = result * 59 + (int)($rainDef >>> 32 ^ $rainDef);
        long $fireDef = Double.doubleToLongBits(this.getFireDef());
        result = result * 59 + (int)($fireDef >>> 32 ^ $fireDef);
        long $windDef = Double.doubleToLongBits(this.getWindDef());
        result = result * 59 + (int)($windDef >>> 32 ^ $windDef);
        long $thunderDef = Double.doubleToLongBits(this.getThunderDef());
        result = result * 59 + (int)($thunderDef >>> 32 ^ $thunderDef);
        long $disorderDef = Double.doubleToLongBits(this.getDisorderDef());
        result = result * 59 + (int)($disorderDef >>> 32 ^ $disorderDef);
        long $sealDef = Double.doubleToLongBits(this.getSealDef());
        result = result * 59 + (int)($sealDef >>> 32 ^ $sealDef);
        long $sleepDef = Double.doubleToLongBits(this.getSleepDef());
        result = result * 59 + (int)($sleepDef >>> 32 ^ $sleepDef);
        long $forgetDef = Double.doubleToLongBits(this.getForgetDef());
        result = result * 59 + (int)($forgetDef >>> 32 ^ $forgetDef);
        long $poisonDef = Double.doubleToLongBits(this.getPoisonDef());
        result = result * 59 + (int)($poisonDef >>> 32 ^ $poisonDef);
        long $shockDef = Double.doubleToLongBits(this.getShockDef());
        result = result * 59 + (int)($shockDef >>> 32 ^ $shockDef);
        long $gfireDef = Double.doubleToLongBits(this.getGfireDef());
        result = result * 59 + (int)($gfireDef >>> 32 ^ $gfireDef);
        long $sanshiDef = Double.doubleToLongBits(this.getSanshiDef());
        result = result * 59 + (int)($sanshiDef >>> 32 ^ $sanshiDef);
        long $rainDefNeg = Double.doubleToLongBits(this.getRainDefNeg());
        result = result * 59 + (int)($rainDefNeg >>> 32 ^ $rainDefNeg);
        long $fireDefNeg = Double.doubleToLongBits(this.getFireDefNeg());
        result = result * 59 + (int)($fireDefNeg >>> 32 ^ $fireDefNeg);
        long $windDefNeg = Double.doubleToLongBits(this.getWindDefNeg());
        result = result * 59 + (int)($windDefNeg >>> 32 ^ $windDefNeg);
        long $thunderDefNeg = Double.doubleToLongBits(this.getThunderDefNeg());
        result = result * 59 + (int)($thunderDefNeg >>> 32 ^ $thunderDefNeg);
        long $disorderDefNeg = Double.doubleToLongBits(this.getDisorderDefNeg());
        result = result * 59 + (int)($disorderDefNeg >>> 32 ^ $disorderDefNeg);
        long $shockDefNeg = Double.doubleToLongBits(this.getShockDefNeg());
        result = result * 59 + (int)($shockDefNeg >>> 32 ^ $shockDefNeg);
        long $gfireDefNeg = Double.doubleToLongBits(this.getGfireDefNeg());
        result = result * 59 + (int)($gfireDefNeg >>> 32 ^ $gfireDefNeg);
        long $sanshiDefNeg = Double.doubleToLongBits(this.getSanshiDefNeg());
        result = result * 59 + (int)($sanshiDefNeg >>> 32 ^ $sanshiDefNeg);
        result = result * 59 + this.getSkillId();
        Object $extSkillInfo = this.getExtSkillInfo();
        result = result * 59 + (($extSkillInfo == null) ? 43 : $extSkillInfo.hashCode());
        Object $skillInfo = this.getSkillInfo();
        result = result * 59 + (($skillInfo == null) ? 43 : $skillInfo.hashCode());
        Object $fightAi = this.getFightAi();
        result = result * 59 + (($fightAi == null) ? 43 : $fightAi.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPartnerProperty(id=" + this.getId() + ", partnerSerial=" + this.getPartnerSerial() + ", race=" + this.getRace() + ", sex=" + this.getSex() + ", level=" + (int)this.getLevel() + ", basecon=" + this.getBasecon() + ", wakan=" + this.getWakan() + ", atk=" + this.getAtk() + ", agility=" + this.getAgility() + ", power=" + this.getPower() + ", speed=" + this.getSpeed() + ", hpMax=" + this.getHpMax() + ", fire=" + this.getFire() + ", gold=" + this.getGold() + ", soil=" + this.getSoil() + ", water=" + this.getWater() + ", wood=" + this.getWood() + ", conFire=" + this.getConFire() + ", conGold=" + this.getConGold() + ", conSoil=" + this.getConSoil() + ", conWater=" + this.getConWater() + ", conWood=" + this.getConWood() + ", phyDef=" + this.getPhyDef() + ", phyDefNef=" + this.getPhyDefNef() + ", fatalRate=" + this.getFatalRate() + ", hitCombo=" + this.getHitCombo() + ", hitComboRate=" + this.getHitComboRate() + ", hitRate=" + this.getHitRate() + ", voidRate=" + this.getVoidRate() + ", phyDefNefRate=" + this.getPhyDefNefRate() + ", rainRageOdds=" + this.getRainRageOdds() + ", fireRageOdds=" + this.getFireRageOdds() + ", windRageOdds=" + this.getWindRageOdds() + ", thunderRageOdds=" + this.getThunderRageOdds() + ", gfireRageOdds=" + this.getGfireRageOdds() + ", shanshiRageOdds=" + this.getShanshiRageOdds() + ", rainEhan=" + this.getRainEhan() + ", fireEhan=" + this.getFireEhan() + ", windEhan=" + this.getWindEhan() + ", thunderEhan=" + this.getThunderEhan() + ", recoverEhan=" + this.getRecoverEhan() + ", sanshiEhan=" + this.getSanshiEhan() + ", breakEhan=" + this.getBreakEhan() + ", adAtkEhan=" + this.getAdAtkEhan() + ", adDefEhan=" + this.getAdDefEhan() + ", adSpdEhan=" + this.getAdSpdEhan() + ", sweepEhan=" + this.getSweepEhan() + ", thudEhan=" + this.getThudEhan() + ", disorderEhan=" + this.getDisorderEhan() + ", sealEhan=" + this.getSealEhan() + ", poisonEhan=" + this.getPoisonEhan() + ", sleepEhan=" + this.getSleepEhan() + ", forgetEhan=" + this.getForgetEhan() + ", charmEhan=" + this.getCharmEhan() + ", cureEhan=" + this.getCureEhan() + ", shockEhan=" + this.getShockEhan() + ", gfireEhan=" + this.getGfireEhan() + ", rainDef=" + this.getRainDef() + ", fireDef=" + this.getFireDef() + ", windDef=" + this.getWindDef() + ", thunderDef=" + this.getThunderDef() + ", disorderDef=" + this.getDisorderDef() + ", sealDef=" + this.getSealDef() + ", sleepDef=" + this.getSleepDef() + ", forgetDef=" + this.getForgetDef() + ", poisonDef=" + this.getPoisonDef() + ", shockDef=" + this.getShockDef() + ", gfireDef=" + this.getGfireDef() + ", sanshiDef=" + this.getSanshiDef() + ", rainDefNeg=" + this.getRainDefNeg() + ", fireDefNeg=" + this.getFireDefNeg() + ", windDefNeg=" + this.getWindDefNeg() + ", thunderDefNeg=" + this.getThunderDefNeg() + ", disorderDefNeg=" + this.getDisorderDefNeg() + ", shockDefNeg=" + this.getShockDefNeg() + ", gfireDefNeg=" + this.getGfireDefNeg() + ", sanshiDefNeg=" + this.getSanshiDefNeg() + ", skillId=" + this.getSkillId() + ", extSkillInfo=" + this.getExtSkillInfo() + ", skillInfo=" + this.getSkillInfo() + ", fightAi=" + this.getFightAi() + ")";
    }
}
