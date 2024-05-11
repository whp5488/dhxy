
package com.kitty.system.game.fightnew.model;

import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import java.util.Collection;
import java.util.HashMap;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.logs.LoggerFunction;
import java.util.Optional;
import com.kitty.system.game.fightnew.book.hero.utils.HeroSkillUtil;
import java.util.Iterator;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.wardef.ATTRCAP;
import com.kitty.system.common.enter.FiedValue;
import org.nutz.lang.util.NutMap;
import com.kitty.system.enums.war.wardef.LOGIC_SKILL;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import org.apache.commons.lang3.ArrayUtils;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.FightOtherUtil;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import java.util.Map;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.service.CountUtils;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import java.util.List;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import org.slf4j.Logger;

public class BattleUnit
{
    private final Logger logger;
    private FightObject effector;
    private FightObject target;
    private RoleSkill skill;
    private int skillType;
    private Fight fight;
    private boolean isBackAttack;
    private boolean isFury;
    private double furyHPCount;
    private boolean isDodge;
    private Effect effect;
    private int sideId;
    private int doubleHit;
    public int magicDoubleHitI;
    private List<Integer> hitList;
    private List<Integer> clearStatusList;
    private int phyHurt;
    private int targetReduceMp;
    private FightObject sideTarget;
    private ActionDetailParam actionDetailParam;
    private ActionDetailParam actionDetailParamBac;
    
    public void triggerPhysic() {
        this.createActionDetailParam(this.effector, this.target, 0, 0, false);
        this.countDoubleHit();
        this.afterUsePhysic();
    }
    
    private void afterUsePhysic() {
        final FightMessageNewService fightMessageNewService = SpringUtils.getBean(FightMessageNewService.class);
        final RespFightShouting fightShouting = fightMessageNewService.fightShouting(this.effector, "物攻");
        final FightObjectParam attackerParam = this.actionDetailParam.gainAttackerParam();
        ActionParamUtils.updateObjectInfo(this.effector, 0, 0, 2, fightShouting, attackerParam);
        if (this.effector.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
            this.petDoPhyHit();
        }
        else {
            this.doPhyHit();
        }
    }
    
    public void doPhyHit() {
        if (this.effector.isASide() == this.target.isASide() && !this.effector.isConfusion()) {
            this.doDamageToTargetHp(this.target, 1, null, false);
        }
        else {
            this.doubleHit = Math.max(this.doubleHit, 1);
            final boolean isProtect = true;
            int tempHurt = 0;
            double rdk = 0.0;
            for (int i = 0; i < this.doubleHit; ++i) {
                List<Integer> list = new ArrayList<Integer>();
                final Effect effect = CountUtils.countHurt(this.fight, this.effector, this.target, rdk, 1.0);
                int hurt = effect.getHurt();
                rdk = effect.getHit();
                if (this.target.isFrozen() || this.target.RoleIsDamgeImmunity()) {
                    hurt = 0;
                }
                tempHurt = hurt;
                if (i <= 0 && !this.target.isSleep()) {
                    this.isDodge = SkillAi.checkMiss(this.effector, this.target);
                }
                if (!this.isDodge) {
                    this.dodgeReturnParam(this.target.getId(), 0);
                    break;
                }
                final double fury = this.effector.getSelfAllTempPro(PRO_DICT_TYPE.RAGE_RATIO.getDist());
                if (fury > 0.0) {
                    this.isFury = SkillAi.checkFury(this.effector);
                }
                final Map<String, Object> ohterMap = this.handleHurtAndOther(effect, hurt, list, i);
                list = (List<Integer>)ohterMap.get("list");
                int skillId = Integer.valueOf(String.valueOf(ohterMap.get("skillId")));
                if (this.skill != null && this.skill.getSkillId() == 80023) {
                    skillId = this.skill.getSkillId();
                }
                final int targetAction = Integer.valueOf(String.valueOf(ohterMap.get("targetAction")));
                hurt = Integer.valueOf(String.valueOf(ohterMap.get("hurt")));
                if (i > 0 && !this.target.isFrozen()) {
                    hurt = Math.max((int)(hurt * Math.pow(0.6, i)), 1);
                }
                this.isFury = false;
                if (this.target.getStatusMap().getInt(String.valueOf(2070)) > 0) {
                    hurt *= (int)(1.0 + this.target.getStatusMap().getDouble("2070value"));
                }
                this.phyHurt = hurt;
                hurt = this.isGuard(hurt, isProtect, i);
                if (hurt > 0) {
                    boolean isDoubleHit = false;
                    if (i > 0) {
                        isDoubleHit = true;
                    }
                    this.doDamageToTargetHp(this.target, hurt, list, isDoubleHit);
                }
                else if (this.target.isFrozen()) {
                    this.changeLifeParamTarget(this.fight, this.target, hurt, 2, false);
                }
                if (i == this.doubleHit - 1) {
                    PassiveSkillUtils.phySicalLastHit(this.fight, this.effector, this.target, this.skill, hurt, this.actionDetailParam);
                }
                if (this.target.isFallDown()) {
                    break;
                }
                if (this.effector.isFallDown()) {
                    break;
                }
            }
            if (this.isDodge) {
                this.doBackHurt(Boolean.TRUE, tempHurt);
            }
        }
    }
    
    public void triggerMagic() {
        this.createActionDetailParam(this.effector, this.target, 7, this.skill.getSkillId(), false);
        this.isDodge = SkillCompute.computeSkillHit(this.fight, this.effector, this.target, this.skill);
        if (this.isDodge && !this.effect.isTargetHit()) {
            this.isDodge = this.effect.isTargetHit();
        }
        final List<Integer> list = new ArrayList<Integer>();
        if (this.isFury) {
            list.add(100);
        }
        boolean penetrate = false;
        if (this.effect.getList() != null && this.effect.getList().size() > 0) {
            final Object args = this.elsePro(this.effect.getList(), "penetrate");
            penetrate = (args != null && Boolean.parseBoolean(args.toString()));
        }
        if (penetrate) {
            list.add(101);
        }
        if (this.effect == null || this.effect.getType() <= 0 || this.isDodge) {
            this.dodgeReturnParam(this.target.getId(), 0);
        }
        else {
            final int tianFuAddHurt = PassiveSkillUtils.addMagicEffect(this.fight, this.effector, this.target, this.skill, this.effect.getHurt(), this.effect, this.magicDoubleHitI);
            if (tianFuAddHurt > 0) {
                this.effect.setHurt(this.effect.getHurt() + tianFuAddHurt);
            }
            this.hitList = list;
            this.afterUseMagic(this.effect);
        }
    }
    
    private void afterUseMagic(final Effect effect) {
        final List<Integer> list = new ArrayList<Integer>();
        boolean isWuxin = false;
        int status = -1;
        final int unitSkill = FightOtherUtil.isPlayerSkill(this.skill.getSkillId());
        if (effect.getTime() > 0) {
            final NutMap targetStatus = this.target.getStatusMap();
            FightParamPool.addControlStatus(this.fight, this.target, effect.getType());
            targetStatus.setv(String.valueOf(effect.getType()), (Object)effect.getTime());
            this.target.getStatusSubMap().setv(effect.getType() + "target", (Object)this.effector.getId());
            if (this.effector.isPet() && StatusConst.getWuxingStatus().contains(effect.getType())) {
                isWuxin = true;
                status = effect.getType();
            }
            list.add(effect.getType());
            list.add(effect.getTime());
        }
        if (LOGIC.LOGICTYPE_PET.getValue() != this.effector.getLogic() && ArrayUtils.contains((Object[])SkillAi.obstacle, (Object)this.skillType)) {
            this.sendStatus(list);
            if (this.skillType == SKILLATTR.SKILLATTR_POISON.getValue()) {
                this.doMagicHit();
            }
            else {
                this.clearStatus();
            }
        }
        else if (unitSkill == LOGIC_SKILL.LOGIC_TYPE_SKILL.getValue() && (this.skillType == SKILLATTR.SKILLATTR_ZHIYU.getValue() || this.skillType == SKILLATTR.SKILLATTR_PAN.getValue() || this.skillType == SKILLATTR.SKILLATTR_ATTACK.getValue() || this.skillType == SKILLATTR.SKILLATTR_SPEED.getValue() || this.skillType == SKILLATTR.SKILLATTR_SHUAIRUO.getValue())) {
            this.sendStatus(list);
        }
        else if (unitSkill == LOGIC_SKILL.LOGIC_TYPE_PET_SKILL.getValue() && (this.skillType == SKILLATTR.SKILLATTR_ZHIYU.getValue() || this.skillType == SKILLATTR.SKILLATTR_PAN.getValue() || this.skillType == SKILLATTR.SKILLATTR_ATTACK.getValue() || this.skillType == SKILLATTR.SKILLATTR_SPEED.getValue() || this.skillType == SKILLATTR.SKILLATTR_SHUAIRUO.getValue())) {
            this.sendStatus(list);
            this.doMagicHit();
        }
        else if (this.skillType == SKILLATTR.SKILLATTR_ZHENJI.getValue() || this.skillType == SKILLATTR.SKILLATTR_POJIA.getValue()) {
            this.sendStatus(list);
            this.doMagicHit();
        }
        else if (LOGIC.LOGICTYPE_PET.getValue() == this.effector.getLogic() && list.size() > 0) {
            this.sendStatus(list);
            if (isWuxin) {
                this.clearWuxin(effect);
            }
        }
        else {
            this.doMagicHit();
        }
        this.doBackHurt(Boolean.FALSE, effect.getHurt());
    }
    
    private void doMagicHit() {
        if (this.effect != null) {
            final int type = 100000;
            if (this.effect.getList() != null && this.effect.getList().size() > 0) {
                this.doMagicHitSpecial(this.effect.getList());
            }
            if (this.effect.getHurt() > 0) {
                this.effect.setHurt(this.excuteGuard(this.effect.getHurt(), 100000));
                if (this.target.getStatusMap().getInt(String.valueOf(2070)) > 0) {
                    this.effect.setHurt((int)(this.effect.getHurt() * (1.0 + this.target.getStatusMap().getDouble("2070value"))));
                }
                final int guardHurt = this.excuteTianFuDrug(this.target, this.effect.getHurt(), (this.skillType == SKILLATTR.SKILLATTR_POISON.getValue()) ? 1005 : type);
                this.effect.setHurt(guardHurt);
                this.doDamageToTargetHp(this.target, this.effect.getHurt(), this.hitList, false);
                if (this.target.isFallDown()) {
                    this.effector.addFallDownList(this.target.getId());
                    this.fight.onActorDie(this.target);
                }
                if (this.effector.isFallDown()) {
                    this.fight.onActorDie(this.effector);
                }
            }
        }
    }
    
    public void doMagicHitSpecial(final List<FiedValue> list) {
        final Object mana = this.elsePro(list, "mana");
        if (mana != null) {
            final int mp = Integer.valueOf(String.valueOf(mana));
            this.doDamageToTargetMp(this.target, mp, 100000);
        }
        this.changeList(list, this.target);
    }
    
    public void recoverLife(int recover, final FightObject target) {
        final int need = target.getMaxLife() - target.getCurrLife();
        if (recover > need) {
            recover = need;
        }
        final int changeLifeSS = PassiveSkillUtils.countRecoverBloodEndSS(this.fight, this.effector, target, this.skill, recover);
        recover += changeLifeSS;
        final int changeLife = PassiveSkillUtils.countRecoverBloodEnd(this.fight, target, recover, this.skill);
        recover += changeLife;
        final FightObjectParam targetParam = this.changeLifeParamTarget(target, recover, 2);
        targetParam.addDamageType(119);
    }
    
    private void doBackHurt(final boolean phyAttack, final int hurt) {
        if (hurt <= 0) {
            return;
        }
        if (this.target.isFallDown() || this.effector.isFallDown()) {
            return;
        }
        if (this.skill != null && SKILLATTR.SKILLATTR_ZHEN.getValue() == this.skill.getType()) {
            return;
        }
        final double ftOdds = this.target.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.SHOCK_RATIO.getDist()));
        final boolean flag = SkillAi.canAffectOnPro(ftOdds);
        if (flag) {
            double ftPro = this.target.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist()));
            final double ftPro_k = this.effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.WL_XS.getDist())) + this.effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.SH_DK.getDist()));
            ftPro -= ftPro_k;
            if (ftPro < ATTRCAP.MIN_FTLV_NUM.getValue()) {
                ftPro = ATTRCAP.MIN_FTLV_NUM.getValue();
            }
            else if (ftPro > ATTRCAP.MAX_FTLV_NUM.getValue()) {
                ftPro = ATTRCAP.MAX_FTLV_NUM.getValue();
            }
            int backHurt = BigDecimalUtil.decimalScaleIntegerDown(0, Math.max(ftPro * hurt, 1.0));
            if (backHurt >= this.effector.getCurrLife()) {
                backHurt = this.effector.getCurrLife();
            }
            final FightObjectParam attackerParam = this.actionDetailParam.gainTargetParam(this.effector.getId(), true);
            PassiveSkillUtils.excuteChangeLifeAuto(this.fight, this.effector, this.actionDetailParam, attackerParam, -backHurt);
        }
    }
    
    public void doDamageToTargetHp(final FightObject target, int hurt, final List<Integer> list, final boolean isDoubleHit) {
        hurt += PassiveSkillUtils.changeHurt(this.fight, this.effector, target, hurt, this.isFury, this.skill, this.clearStatusList);
        if ((FIGHT_TYPE.NIAN.getValue() == this.fight.getType() && target.getSerial() == 1660031) || (FIGHT_TYPE.BURNING_HOT.getValue() == this.fight.getType() && target.getSerial() == 1660032)) {
            hurt = 1;
        }
        final int life = target.getCurrLife() - hurt;
        if (life < 0) {
            hurt = target.getCurrLife();
        }
        this.clearStatus();
        this.changeLifeParamTarget(this.fight, target, -hurt, 1, isDoubleHit);
        if (target.isFallDown() && LOGIC.LOGICTYPE_PET.getValue() == target.getLogic()) {
            final List<RoleSkill> skillIds = target.getSkillIds();
            final List<SysPetSkill> sysPetSkills = PetTriggerPassiveSkill.beforePetExit(skillIds, this.fight.isPvp());
            if (sysPetSkills.size() > 0) {
                for (final SysPetSkill petSkill : sysPetSkills) {
                    final SkillProcessor processor = SkillProcessor.queryProcessor(petSkill.getSerial());
                    final NutMap passive = processor.passive(this.fight, target, this.effector);
                    if (passive != null && passive.getBoolean("successful")) {
                        this.getTargetParam(target.getId()).setCurrentLife(target.getCurrLife());
                        if (passive.getInt("specialStatus", 0) > 0) {
                            this.getTargetParam(target.getId()).setSpecialStatus(passive.getInt("specialStatus", 0));
                        }
                        if (passive.getList("clearStatus", (Class)Integer.class) == null || passive.getList("clearStatus", (Class)Integer.class).size() <= 0) {
                            continue;
                        }
                        final List<Integer> clearStatus = passive.getList("clearStatus", (Class)Integer.class);
                        this.getTargetParam(target.getId()).setClearStatus(clearStatus);
                    }
                }
            }
        }
        final FightObjectParam targetParam = this.getTargetParam(target.getId());
        if (list != null && list.size() > 0) {
            targetParam.setDamageType(list);
        }
        this.leechGain(hurt);
        final RespFightShouting fightShouting = SpringUtils.getBean(FightMessageNewService.class).fightShouting(target, "挨打");
        targetParam.setFightShouting(fightShouting);
    }
    
    public void doDamageToTargetMp(final FightObject target, int mp, final int type) {
        if (mp > 0) {
            if (this.isFury) {
                mp *= (int)2.5;
            }
            int currMana = target.getCurrMana() - mp;
            if (currMana < 0) {
                mp = target.getCurrMana();
                currMana = 0;
            }
            if (mp > 0) {
                mp += PassiveSkillUtils.beforeReduceHurtMp(this.fight, this.effector, target, this.skill, mp);
                this.targetReduceMp = mp;
                this.changeManaParamTarget(this.fight, target, -mp, 2);
            }
        }
    }
    
    public void leechGain(final int damage) {
        if (this.skillType == SKILLATTR.SKILLATTR_XIXUE.getValue() && this.sideId > 0) {
            FightObject sideTarget = this.fight.queryFightObject(this.sideId);
            final String serial = String.valueOf(this.skill.getSkillId());
            if (sideTarget == null) {
                sideTarget = this.effector;
                this.effector.getStatusSubMap().setv(serial, (Object)1);
            }
            if (sideTarget.getStatusSubMap().getInt(serial) <= 0) {
                final double extra = this.effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_SS.getDist());
                int recover = SkillCompute.computeSkillLeech(this.skill.getSkillId(), damage, extra);
                recover = this.excuteRecover(recover, this.effector);
                this.recoverLife(recover, sideTarget);
                this.sideTarget = sideTarget;
            }
        }
    }
    
    public void clearStatus() {
        List<Integer> list = new ArrayList<Integer>();
        if (this.skillType == SKILLATTR.SKILLATTR_ICE.getValue()) {
            if (this.target.isConfusion()) {
                list.add(1002);
                this.target.setStatus(1002, 0);
            }
            if (this.target.isForgotten()) {
                list.add(1006);
                this.target.setStatus(1006, 0);
            }
        }
        else if (this.skillType == SKILLATTR.SKILLATTR_SLEEP.getValue()) {
            if (this.target.isConfusion()) {
                list.add(1002);
                this.target.setStatus(1002, 0);
            }
            if (this.target.isForgotten()) {
                list.add(1006);
                this.target.setStatus(1006, 0);
            }
        }
        else if (this.skillType == SKILLATTR.SKILLATTR_YIWANG.getValue()) {
            if (this.target.isConfusion()) {
                list.add(1002);
                this.target.setStatus(1002, 0);
            }
        }
        else if (this.skillType == SKILLATTR.SKILLATTR_CONFUSE.getValue() && this.target.isForgotten()) {
            list.add(1006);
            this.target.setStatus(1006, 0);
        }
        if (this.skillType != SKILLATTR.SKILLATTR_SLEEP.getValue() && this.target.isSleep()) {
            final String perpetrator = this.target.getStatusMap().getString("1004perpetrator");
            if (perpetrator != null && perpetrator.contains(String.valueOf(71343))) {
                this.target.getStatusMap().unset("1004perpetrator");
            }
            else {
                list.add(1004);
                this.target.setStatus(1004, 0);
            }
        }
        list = this.clearRendArmor(list);
        this.clearStatusList = list;
        if (list.size() > 0) {
            int skillId = 0;
            if (this.skill != null && this.skill.getSkillId() > 0) {
                skillId = this.skill.getSkillId();
            }
            this.clearStatusParamTarget(this.target, list, 2);
        }
    }
    
    private void clearWuxin(final Effect effect) {
        if (effect.getList() != null && effect.getList().size() > 0) {
            this.changeTarget(effect.getList(), this.target);
        }
    }
    
    public List<Integer> clearRendArmor(final List<Integer> list) {
        if ((this.effect == null || this.effect.getType() != 1021) && this.target.getStatusMap().getInt(String.valueOf(1021)) > 0) {
            this.target.removeStatus(1021);
            HeroSkillUtil.clearPj(this.target);
            list.add(1021);
        }
        return list;
    }
    
    private void sendStatus(final List<Integer> list) {
        if (list.size() > 0) {
            this.statusParamTarget(this.target, list.get(0), list.get(1), 2);
        }
    }
    
    public Object elsePro(final List<FiedValue> list, final String dist) {
        final Optional<FiedValue> optional = list.stream().filter(fied -> fied.getType().equals(dist)).findFirst();
        if (optional.isPresent()) {
            final FiedValue fiedValue = optional.get();
            return fiedValue.getValue();
        }
        return null;
    }
    
    public void setStunt(final boolean stunt) {
        this.isFury = stunt;
    }
    
    public void setDoubleHit(final int doubleHit) {
        this.doubleHit = doubleHit;
    }
    
    public void setEffect(final Effect effect) {
        this.effect = effect;
    }
    
    public void setSideId(final int sideId) {
        this.sideId = sideId;
    }
    
    public BattleUnit(final Fight fight, final FightObject effector, final FightObject target) {
        this(fight, effector, target, null);
    }
    
    public BattleUnit(final Fight fight, final FightObject effector, final FightObject target, final RoleSkill skill) {
        this.logger = LoggerFunction.FIGHT.getLogger();
        this.furyHPCount = 2.2;
        this.isDodge = true;
        this.doubleHit = 1;
        this.magicDoubleHitI = 1;
        this.actionDetailParam = new ActionDetailParam();
        this.actionDetailParamBac = null;
        this.effector = effector;
        this.target = target;
        this.fight = fight;
        this.skill = skill;
        if (skill != null && skill.getType() != null) {
            this.skillType = skill.getType();
        }
    }
    
    private int excuteRecover(final int recover, final FightObject effector) {
        final double percent = 0.0;
        int reduce = 0;
        if (percent > 0.0) {
            reduce = (int)(recover * percent);
        }
        final int add = 0;
        return recover - reduce + add;
    }
    
    private int excuteGuard(final int hurt, final int type) {
        int returnHurt = 0;
        int statusType = 0;
        if (this.target.getStatusMap().getInt(String.valueOf(2010)) > 0) {
            statusType = 2010;
        }
        else {
            if (this.target.getStatusMap().getInt(String.valueOf(8)) <= 0) {
                return hurt;
            }
            statusType = 8;
        }
        returnHurt = this.guard(hurt, type, statusType);
        if (returnHurt == 0) {
            return hurt;
        }
        return returnHurt;
    }
    
    private int guard(final int hurt, final int type, final int statusType) {
        int returnHurt = 0;
        if (this.target.getStatusValue(1011) >= 1) {
            this.target.getStatusMap().put("1011value", 0);
            this.target.setStatus(1011, 0);
            final List<Integer> statusList = new ArrayList<Integer>();
            statusList.add(1011);
            final FightObjectParam targetParam = this.getTargetParam(this.target.getId());
            targetParam.setClearStatus(statusList);
        }
        final FightObject guardObj = this.fight.queryFightObject(this.target.getStatusMap().getInt(statusType + "value"));
        if (guardObj != null && !this.target.getId().equals(guardObj.getId())) {
            int guardHurt = BigDecimalUtil.decimalScaleIntegerDown(0, hurt * this.target.getStatusMap().getDouble(statusType + "percent") * this.target.getStatusMap().getDouble(statusType + "reducePercent"));
            guardHurt = Math.max(1, guardHurt);
            int skillId = 0;
            if (this.skill != null && this.skill.getSkillId() > 0) {
                skillId = this.skill.getSkillId();
            }
            this.doDamageToTargetHp(guardObj, guardHurt, null, false);
            this.getTargetParam(guardObj.getId()).setGuardTarget(this.target.getId());
            guardObj.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.THE_ROUND_DRUG.getDist()), (Object)(this.fight.getRound() + "_true"));
            returnHurt = BigDecimalUtil.decimalScaleIntegerDown(0, hurt * (1.0 - this.target.getStatusMap().getDouble(statusType + "percent")));
            if (returnHurt < 1) {
                returnHurt = 1;
            }
        }
        else {
            returnHurt = BigDecimalUtil.decimalScaleIntegerDown(0, hurt);
        }
        return returnHurt;
    }
    
    public void petDoPhyHit() {
        if (this.effector.isASide() == this.target.isASide() && !this.effector.isConfusion()) {
            this.doDamageToTargetHp(this.target, 1, null, false);
        }
        else {
            this.doubleHit = Math.max(this.doubleHit, 1);
            if (this.doubleHit > 1) {
                this.doubleHit = PassiveSkillUtils.doubleHitExcute(this.fight, this.effector, this.target, this.skill, this, PET_PASSIVE_SKILL_TRIGGER_DICT.NORMAL_ATTACK_DOUBLE_HIT.getValue(), this.doubleHit);
            }
            final boolean isProtect = true;
            int tempHurt = 0;
            double rdk = 0.0;
            for (int i = 0; i < this.doubleHit; ++i) {
                List<Integer> list = new ArrayList<Integer>();
                if (i == 0) {
                    PassiveSkillUtils.getPassiveSkill(this.fight, this.effector, this.target, this.skill, this, PET_PASSIVE_SKILL_TRIGGER_DICT.STRENGTHEN_NORMAL_ATTACK_PHY.getValue(), 0);
                }
                Effect effect = null;
                if (this.effect != null) {
                    effect = this.effect;
                }
                else {
                    effect = CountUtils.countHurt(this.fight, this.effector, this.target, rdk, 1.0);
                }
                int hurt = effect.getHurt();
                rdk = effect.getHit();
                if (this.effector.isASide() != this.target.isASide()) {
                    PassiveSkillUtils.excutePetPKPetPassiveSkill(this.fight, this.effector, this.target, effect);
                }
                if (this.target.isFrozen() || this.target.RoleIsDamgeImmunity()) {
                    hurt = 0;
                }
                tempHurt = hurt;
                if (i <= 0 && !this.target.isSleep()) {
                    this.isDodge = SkillAi.checkMiss(this.effector, this.target);
                }
                if (!this.isDodge) {
                    this.dodgeReturnParam(this.target.getId(), 0);
                    PetPassiveSkillUtil.writeActionDetailParam(this, (this.skill != null && i == 0) ? ((int)this.skill.getSkillId()) : 0);
                    break;
                }
                final double fury = this.effector.getSelfAllTempPro(PRO_DICT_TYPE.RAGE_RATIO.getDist());
                if (fury > 0.0) {
                    this.isFury = SkillAi.checkFury(this.effector);
                }
                final Map<String, Object> ohterMap = this.handleHurtAndOther(effect, hurt, list, i);
                list = (List<Integer>)ohterMap.get("list");
                final int skillId = (Integer) ohterMap.get("skillId");
                final int targetAction =  (Integer) ohterMap.get("targetAction");
                hurt = (Integer) ohterMap.get("hurt");
                if (i > 0 && !this.target.isFrozen()) {
                    hurt = Math.max((int)(hurt * Math.pow(0.6, i)), 1);
                }
                this.isFury = false;
                final Effect effectPart = PassiveSkillUtils.getPassiveSkill(this.fight, this.effector, this.target, this.skill, this, PET_PASSIVE_SKILL_TRIGGER_DICT.STRENGTHEN_NORMAL_ATTACK.getValue(), hurt);
                if (effectPart != null && effectPart.getHurt() != 0) {
                    hurt = effectPart.getHurt();
                }
                if (this.target.getStatusMap().getInt(String.valueOf(2070)) > 0) {
                    hurt *= (int)(1.0 + this.target.getStatusMap().getDouble("2070value"));
                }
                if (effect.getTime() > 0) {
                    final NutMap targetStatus = this.target.getStatusMap();
                    targetStatus.setv(String.valueOf(effect.getType()), (Object)effect.getTime());
                    this.statusParamTarget(this.target, effect.getType(), effect.getTime(), 2);
                }
                this.phyHurt = hurt;
                hurt = this.isGuard(hurt, isProtect, i);
                if (hurt > 0) {
                    boolean isDoubleHit = false;
                    if (i > 0) {
                        isDoubleHit = true;
                    }
                    this.doDamageToTargetHp(this.target, hurt, list, isDoubleHit);
                }
                else if (this.target.isFrozen()) {
                    this.changeLifeParamTarget(this.fight, this.target, hurt, 2, false);
                }
                if (effectPart != null) {
                    if (effectPart.getType() > 0 && effectPart.getTime() > 0) {
                        this.statusParamTarget(this.target, effectPart.getType(), effectPart.getTime(), 2);
                    }
                    if (effectPart.getList() != null && effectPart.getList().size() > 0) {
                        this.changeList(effectPart.getList(), this.target);
                    }
                }
                int skillSerial = 0;
                if (this.skill != null && i == 0) {
                    skillSerial = this.skill.getSkillId();
                }
                PassiveSkillUtils.normalAttack(this.fight, this.effector, this.target, this, this.phyHurt, skillSerial);
                if (this.target.isFallDown()) {
                    break;
                }
                if (this.effector.isFallDown()) {
                    break;
                }
            }
            if (this.isDodge) {
                this.doBackHurt(Boolean.TRUE, tempHurt);
            }
        }
    }
    
    private Map<String, Object> handleHurtAndOther(final Effect effect, int hurt, final List<Integer> list, final int times) {
        final double tianFuFuryValue = this.effector.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.PHYSICAL_ATTACK_FURY.getDist()));
        if (tianFuFuryValue > 0.0) {
            this.isFury = true;
        }
        int skillId = 0;
        int targetAction = 1;
        final Map<String, Object> map = new HashMap<String, Object>();
        if (this.isFury) {
            if (tianFuFuryValue > 0.0) {
                hurt *= (int)tianFuFuryValue;
            }
            else {
                hurt *= (int)this.furyHPCount;
            }
        }
        if (hurt > this.target.getCurrLife()) {
            hurt = this.target.getCurrLife();
        }
        if (this.isBackAttack) {
            this.isBackAttack = false;
            this.doubleHit = 0;
        }
        if (this.target.isFrozen()) {
            targetAction = 2;
        }
        if (this.isFury) {
            list.add(100);
        }
        if (effect.getType() == 101) {
            list.add(101);
        }
        if (this.doubleHit > 1) {
            list.add(102);
        }
        if (this.skill != null && times == 0) {
            skillId = this.skill.getSkillId();
        }
        if (hurt > 0) {
            if (this.target.getStatusMap().getInt(String.valueOf(2010)) > 0) {
                list.add(106);
            }
            hurt = this.excuteGuard(hurt, 4098);
        }
        map.put("hurt", hurt);
        map.put("skillId", skillId);
        map.put("list", list);
        map.put("targetAction", targetAction);
        return map;
    }
    
    private int isGuard(int hurt, final boolean isProtect, final int index) {
        if (isProtect) {
            final List<FightObject> victim = new ArrayList<FightObject>();
            if (this.target.isASide()) {
                victim.addAll(this.fight.getListA());
            }
            else {
                victim.addAll(this.fight.getListB());
            }
            final NutMap map = AwakenSkillUtils.isGuard(this.fight, this.target);
            if (map != null) {
                final double awakenPercent = map.getDouble("percent");
                if (awakenPercent > 0.0) {
                    final int petFightObjectId = map.getInt("petFightObjectId");
                    final FightObject fightObject = this.fight.queryFightObject(petFightObjectId);
                    if (fightObject != null && !fightObject.isFallDown()) {
                        hurt = this.excuteDrug(fightObject, hurt, 1, awakenPercent);
                        if (hurt <= 0) {
                            return hurt;
                        }
                    }
                }
            }
            for (final FightObject object : victim) {
                if (!object.getId().equals(this.target.getId()) && object.getType() == 8 && object.getTargetId() == this.target.getId()) {
                    final int completed = object.getSkillCoolMap().getInt(String.valueOf(8));
                    if (completed > 0) {
                        continue;
                    }
                    object.getSkillCoolMap().put(String.valueOf(8), (Object)1);
                    final double percent = object.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_PERCENT.getDist()), 0.0);
                    hurt = this.excuteDrug(object, hurt, index, percent);
                }
            }
        }
        return hurt;
    }
    
    private int excuteDrug(final FightObject object, int hurt, final int index, final double percent) {
        if (object.getStatusValue(1011) >= 1) {
            object.getStatusMap().put("1011value", 0);
            object.getStatusMap().setv(String.valueOf(1011), 0);
            final List<Integer> clearStatusList = new ArrayList<Integer>();
            clearStatusList.add(1011);
            this.clearStatusParamTarget(object, clearStatusList, 2);
        }
        boolean isDoubleHit = false;
        if (index > 0) {
            isDoubleHit = true;
        }
        if (percent > 0.0 && percent < 1.0) {
            final String roundDrug = this.target.getBeAttackedMap().getString(String.valueOf(OTHER_SKILL_DICT_TYPE.THE_ROUND_DRUG.getDist()));
            if (roundDrug != null) {
                final String[] strs = roundDrug.split("_");
                if (strs.length == 2 && strs[0].equals(String.valueOf(this.fight.getRound())) && "true".equals(strs[1])) {
                    return hurt;
                }
            }
            double drugHurt = hurt * percent;
            hurt -= (int)drugHurt;
            final double hurtPercent = object.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_CHANGE_PERCENT.getDist()), 0.0);
            if (hurtPercent > 0.0) {
                drugHurt -= drugHurt * hurtPercent;
            }
            this.doDamageToTargetHp(object, BigDecimalUtil.decimalScaleIntegerDown(0, drugHurt), null, isDoubleHit);
            this.getTargetParam(object.getId()).setGuardTarget(this.target.getId());
            return hurt;
        }
        this.doDamageToTargetHp(object, BigDecimalUtil.decimalScaleIntegerDown(0, hurt), null, isDoubleHit);
        this.getTargetParam(object.getId()).setGuardTarget(this.target.getId());
        return 0;
    }
    
    private int excuteTianFuDrug(final FightObject target, int hurt, final int type) {
        final String roundDrug = target.getBeAttackedMap().getString(String.valueOf(OTHER_SKILL_DICT_TYPE.THE_ROUND_DRUG.getDist()));
        if (roundDrug != null) {
            final String[] strs = roundDrug.split("_");
            if (strs.length == 2 && strs[0].equals(String.valueOf(this.fight.getRound())) && "true".equals(strs[1])) {
                return hurt;
            }
        }
        final List<FightObject> victim = new ArrayList<FightObject>();
        if (target.isASide()) {
            victim.addAll(this.fight.getListA());
        }
        else {
            victim.addAll(this.fight.getListB());
        }
        for (final FightObject object : victim) {
            if (!object.getId().equals(target.getId()) && object.getType() == 8 && object.getTargetId() == target.getId()) {
                final int completed = object.getSkillCoolMap().getInt(String.valueOf(8));
                if (completed > 0) {
                    continue;
                }
                final double percent = object.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_PERCENT.getDist()), 0.0);
                if (percent > 0.0) {
                    if (object.getStatusValue(1011) >= 1) {
                        object.getStatusMap().put("1011value", 0);
                        object.getStatusMap().setv(String.valueOf(1011), 0);
                        final List<Integer> clearStatusList = new ArrayList<Integer>();
                        clearStatusList.add(1011);
                        this.clearStatusParamTarget(object, clearStatusList, 2);
                    }
                    object.getSkillCoolMap().put(String.valueOf(8), (Object)1);
                    double drugHurt = hurt * percent;
                    hurt -= (int)drugHurt;
                    final double hurtPercent = object.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_CHANGE_PERCENT.getDist()), 0.0);
                    if (hurtPercent > 0.0) {
                        drugHurt -= drugHurt * hurtPercent;
                    }
                    this.doDamageToTargetHp(object, BigDecimalUtil.decimalScaleIntegerDown(0, drugHurt), null, false);
                    break;
                }
                continue;
            }
        }
        return hurt;
    }
    
    public FightObjectParam getTargetParam(final int fightObjectId) {
        final FightObjectParam targetParam = this.getActionDetailParam().gainTargetParam(fightObjectId, true);
        return targetParam;
    }
    
    public void createActionDetailParam(final FightObject effector, final FightObject target, final int type, final int skillSerial, final boolean isNew) {
        if (isNew) {
            this.actionDetailParam = new ActionDetailParam();
        }
        this.actionDetailParam.setType(type);
        this.actionDetailParam.setSkillSerial(skillSerial);
        this.actionDetailParam.setAttackerId(effector.getId());
        this.actionDetailParam.setTargetId(target.getId());
        this.actionDetailParam.addHfType(effector, skillSerial);
    }
    
    public void dodgeReturnParam(final int targetId, final int missed) {
        final FightObjectParam targetParam = this.getTargetParam(targetId);
        targetParam.setMissed(missed);
        targetParam.setHit(false);
    }
    
    public void changeLifeParamTarget(final Fight fight, final FightObject target, final int changeLife, final int missed, final boolean isDoubleHit) {
        if (!isDoubleHit) {
            this.changeLifeParamTarget(target, changeLife, missed);
        }
        else {
            final FightObjectParam targetParam = this.getActionDetailParam().gainTargetParam(target.getId(), false);
            if (targetParam != null) {
                final FightObjectParam targetParamC = new FightObjectParam();
                targetParamC.setOrder(1);
                targetParamC.setObjId(target.getId());
                PassiveSkillUtils.excuteChangeLife(fight, target, targetParamC, changeLife);
                targetParamC.setMissed(missed);
                targetParamC.setDamageType(targetParam.getDamageType());
                this.getActionDetailParam().getTargetParamList().add(targetParamC);
            }
            else {
                this.changeLifeParamTarget(target, changeLife, missed);
            }
        }
    }
    
    public FightObjectParam changeLifeParamTarget(final FightObject target, final int changeLife, final int missed) {
        final FightObjectParam targetParam = this.getTargetParam(target.getId());
        PassiveSkillUtils.excuteChangeLife(this.fight, target, targetParam, changeLife);
        targetParam.setMissed(missed);
        return targetParam;
    }
    
    public void changeManaParamTarget(final Fight fight, final FightObject target, final int changeMana, final int missed) {
        final FightObjectParam targetParam = this.getTargetParam(target.getId());
        PassiveSkillUtils.excuteChangeMana(fight, target, targetParam, changeMana);
        targetParam.setMissed(missed);
    }
    
    public void clearStatusParamTarget(final FightObject target, final List<Integer> clearStatusList, final int missed) {
        final FightObjectParam targetParam = this.getTargetParam(target.getId());
        targetParam.setMissed(missed);
        final List<Integer> clearStatus = targetParam.getClearStatus();
        if (clearStatus == null) {
            targetParam.setClearStatus(clearStatusList);
        }
        else {
            clearStatus.addAll(clearStatusList);
        }
    }
    
    public void statusParamTarget(final FightObject target, final int status, final int statusRound, final int missed) {
        final FightObjectParam targetParam = this.getTargetParam(target.getId());
        targetParam.getStatusMap().put(status, statusRound);
        targetParam.setMissed(missed);
    }
    
    public void changeList(final List<FiedValue> list, final FightObject target) {
        this.changeTarget(list, target);
        this.changeAtta(list, this.effector);
    }
    
    private void changeTarget(final List<FiedValue> list, final FightObject target) {
        final Object targetChangeHp = this.elsePro(list, "targetChangeHp");
        final Object targetChangeMp = this.elsePro(list, "targetChangeMp");
        final Object targetMaxHp = this.elsePro(list, "targetMaxHp");
        final Object targetMaxMp = this.elsePro(list, "targetMaxMp");
        final Object targetClearStatus = this.elsePro(list, "targetClearStatus");
        final Object targetStatusMap = this.elsePro(list, "targetStatusMap");
        final FightObjectParam targetParam = this.getTargetParam(target.getId());
        if (targetChangeHp != null) {
            final int changeLife = targetParam.getChangeLife();
            if (changeLife != 0) {
                targetParam.setChangeLife(changeLife + Integer.parseInt(String.valueOf(targetChangeHp)));
            }
            else {
                targetParam.setChangeLife(Integer.parseInt(String.valueOf(targetChangeHp)));
            }
            target.setCurrLife(target.getCurrLife() + Integer.parseInt(String.valueOf(targetChangeHp)));
            targetParam.setCurrentLife(target.getCurrLife());
        }
        if (targetChangeMp != null) {
            final int changeMana = targetParam.getChangeMana();
            if (changeMana != 0) {
                targetParam.setChangeMana(changeMana + Integer.parseInt(String.valueOf(targetChangeMp)));
            }
            else {
                targetParam.setChangeMana(Integer.parseInt(String.valueOf(targetChangeMp)));
            }
            target.setCurrMana(target.getCurrMana() + Integer.parseInt(String.valueOf(targetChangeMp)));
            targetParam.setCurrentMana(target.getCurrMana());
        }
        if (targetMaxHp != null) {
            targetParam.setMaxLife(Integer.parseInt(String.valueOf(targetMaxHp)));
        }
        if (targetMaxMp != null) {
            targetParam.setMaxMana(Integer.parseInt(String.valueOf(targetMaxMp)));
        }
        if (targetClearStatus != null) {
            final List<Integer> statusList = (List<Integer>)targetClearStatus;
            if (statusList.size() > 0) {
                targetParam.setClearStatus(statusList);
            }
        }
        if (targetStatusMap != null) {
            final Map<Integer, Integer> statusMap = (Map<Integer, Integer>)targetStatusMap;
            if (statusMap.size() > 0) {
                for (final Map.Entry<Integer, Integer> entry : statusMap.entrySet()) {
                    targetParam.getStatusMap().put(entry.getKey(), entry.getValue());
                }
            }
        }
    }
    
    private void changeAtta(final List<FiedValue> list, final FightObject target) {
        final Object attaClearStatus = this.elsePro(list, "attaClearStatus");
        final Object attaStatusMap = this.elsePro(list, "attaStatusMap");
        final FightObjectParam attaParam = this.actionDetailParam.gainTargetParam(target.getId(), true);
        if (attaClearStatus != null) {
            final List<Integer> statusList = (List<Integer>)attaClearStatus;
            if (statusList.size() > 0) {
                attaParam.addClearStatus(statusList);
            }
        }
        if (attaStatusMap != null) {
            final Map<Integer, Integer> statusMap = (Map<Integer, Integer>)attaStatusMap;
            if (statusMap.size() > 0) {
                for (final Map.Entry<Integer, Integer> entry : statusMap.entrySet()) {
                    attaParam.getStatusMap().put(entry.getKey(), entry.getValue());
                }
            }
        }
    }
    
    public void countDoubleHit() {
        if (SkillAi.checkDoubleHit(this.effector)) {
            final int num = BigDecimalUtil.decimalScaleIntegerDown(0, this.effector.getPropertyMap().get((Object)String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist())));
            this.doubleHit += num;
        }
        final boolean flag = SkillAi.checkPetIsSkill(this.fight, this.effector.isASide(), 81057);
        if (flag) {
            final int num2 = PetPassiveSkillUtil.reduceDoubleHit(this.fight, this.effector);
            this.doubleHit = Math.max(1, this.doubleHit - num2);
        }
    }
    
    public FightObject getTarget() {
        return this.target;
    }
    
    public RoleSkill getSkill() {
        return this.skill;
    }
    
    public boolean isDodge() {
        return this.isDodge;
    }
    
    public Effect getEffect() {
        return this.effect;
    }
    
    public void setMagicDoubleHitI(final int magicDoubleHitI) {
        this.magicDoubleHitI = magicDoubleHitI;
    }
    
    public List<Integer> getClearStatusList() {
        return this.clearStatusList;
    }
    
    public int getPhyHurt() {
        return this.phyHurt;
    }
    
    public int getTargetReduceMp() {
        return this.targetReduceMp;
    }
    
    public FightObject getSideTarget() {
        return this.sideTarget;
    }
    
    public void setActionDetailParam(final ActionDetailParam actionDetailParam) {
        this.actionDetailParam = actionDetailParam;
    }
    
    public ActionDetailParam getActionDetailParam() {
        return this.actionDetailParam;
    }
    
    public void setActionDetailParamBac(final ActionDetailParam actionDetailParamBac) {
        this.actionDetailParamBac = actionDetailParamBac;
    }
    
    public ActionDetailParam getActionDetailParamBac() {
        return this.actionDetailParamBac;
    }
}
