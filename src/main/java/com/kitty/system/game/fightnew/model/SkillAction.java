
package com.kitty.system.game.fightnew.model;

import java.beans.ConstructorProperties;
import com.kitty.system.init.domain.SysDataAcsrySuit;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.Iterator;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import java.util.Collection;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysDataAcsrySuitColor;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.enums.war.wardef.LOGIC_SKILL;
import com.kitty.system.game.fightnew.utils.FightOtherUtil;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.enums.war.wardef.LOGIC;
import java.util.Arrays;
import com.kitty.system.game.fightnew.utils.FightRateUtil;
import java.util.Map;
import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.ArrayList;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import java.util.List;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.slf4j.Logger;

public class SkillAction
{
    protected Logger logger;
    private Fight fight;
    private FightObject effector;
    private RoleSkill skill;
    private List<Integer> skillTargets;
    private List<Integer> sideTargets;
    private SkillProcessor processor;
    private List<MagicAttackParam> magicTargets;
    private List<ActionDetailParam> actionDetailParams;
    
    public SkillAction(final Fight fight, final FightObject effector, final RoleSkill roleSkill, final SkillProcessor processor) {
        this.logger = LoggerFunction.FIGHT.getLogger();
        this.actionDetailParams = new ArrayList<ActionDetailParam>();
        this.effector = effector;
        this.fight = fight;
        this.skill = roleSkill;
        this.processor = processor;
    }
    
    public List<Integer> pickUpTargets(final Integer targetNum) {
        if (this.skill != null && 39006 == this.skill.getSkillId()) {
            final List<FightObject> targetSide = this.fight.getTargetSide(this.effector.getId());
            if (targetSide.size() > 0) {
                return this.skillTargets = targetSide.stream().filter(obj -> !obj.isFallDown()).map(FightObject::getId).collect(Collectors.toList());
            }
        }
        if (this.skill.getType() == SKILLATTR.SKILLATTR_XIXUE.getValue()) {
            this.sideTargets = SkillTargetPicker.getAssistTarget(this.fight, this.effector, targetNum);
        }
        if (this.effector.getStatusValue(1023) > 0 && this.skill.getType() == SKILLATTR.SKILLATTR_HENGSAO.getValue()) {
            this.skillTargets = SkillTargetPicker.getRowAttackTarget(this.fight, this.effector, targetNum);
        }
        else {
            this.skillTargets = SpringUtils.getBean(FightNewService.class).getTargetListNew(this.effector, targetNum, this.fight);
        }
        return this.skillTargets;
    }
    
    public void doMagicEffect() {
        final FightMessageNewService fightMessageService = SpringUtils.getBean(FightMessageNewService.class);
        boolean hasEnemy = false;
        for (final Integer targetId : this.skillTargets) {
            final FightObject target = this.fight.queryFightObject(targetId);
            if (!this.fight.isSameSide(this.effector, target)) {
                hasEnemy = true;
                break;
            }
        }
        int hitTimes = 1;
        PassiveSkillUtils.beforeMagic(this.fight, this.effector, this.skill, this.skillTargets);
        if (this.effector.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC.getDist()), 0) > 0) {
            hitTimes += this.effector.getBeAttackedMap().getInt(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC.getDist()));
        }
        final List<MagicAttackParam> targets = new ArrayList<MagicAttackParam>();
        MagicAttackParam magicAttackParam = null;
        for (int i = 0; i < hitTimes; ++i) {
            ActionDetailParam actionDetailParam = null;
            for (int j = 0; j < this.skillTargets.size(); ++j) {
                final Integer targetId2 = this.skillTargets.get(j);
                final FightObject target2 = this.fight.queryFightObject(targetId2);
                if (i > 0) {
                    if (targets.size() > 0) {
                        for (final MagicAttackParam magicAttackParam2 : targets) {
                            if (magicAttackParam2.getTarget().getId().equals(target2.getId())) {
                                magicAttackParam = magicAttackParam2;
                                break;
                            }
                        }
                    }
                    if (magicAttackParam == null) {
                        magicAttackParam = new MagicAttackParam();
                        targets.add(magicAttackParam);
                    }
                }
                else {
                    magicAttackParam = new MagicAttackParam();
                    targets.add(magicAttackParam);
                }
                magicAttackParam.setTarget(target2);
                final List<Integer> positiveStatus = new ArrayList<Integer>();
                for (final Map.Entry<String, Object> entry : target2.getStatusMap().entrySet()) {
                    final String key = entry.getKey();
                    final Object value = entry.getValue();
                    if (!key.matches("^[0-9]+$")) {
                        continue;
                    }
                    if (value == null) {
                        continue;
                    }
                    if ((int)value <= 0) {
                        continue;
                    }
                    if (1022 != Integer.parseInt(key) && 1009 != Integer.parseInt(key) && 1008 != Integer.parseInt(key) && 1010 != Integer.parseInt(key)) {
                        continue;
                    }
                    positiveStatus.add(Integer.parseInt(key));
                }
                magicAttackParam.setPositiveStatus(positiveStatus);
                if (target2 != null) {
                    PassiveSkillUtils.beforeMagicOne(this.fight, this.effector, target2, this.skill);
                    final boolean stunt = FightRateUtil.calcMagicStuntRate(this.effector, this.skill);
                    final List<Integer> woodAssistSkills = Arrays.asList(SKILLATTR.SKILLATTR_XIXUE.getValue(), SKILLATTR.SKILLATTR_ZHIYU.getValue());
                    if (!target2.isFallDown() || woodAssistSkills.contains(this.skill.getType()) || LOGIC.LOGICTYPE_PET.getValue() == this.effector.getLogic()) {
                        int sideId = 0;
                        if (this.sideTargets != null && this.sideTargets.size() > j) {
                            sideId = this.sideTargets.get(j);
                        }
                        final BattleUnit battleUnit = new BattleUnit(this.fight, this.effector, target2, this.skill);
                        PassiveSkillUtils.beforeSkillEffect(this.fight, this.effector, target2, this.skill);
                        PassiveSkillUtils.beforeCountHurt(this.fight, this.effector, target2);
                        Effect effect;
                        if (84003 != this.skill.getSkillId() && 83024 != this.skill.getSkillId()) {
                            effect = this.processor.effect(this.fight, this.effector, target2, stunt);
                        }
                        else {
                            effect = this.processor.effect(this.fight, this.effector, target2, stunt, this.actionDetailParams);
                        }
                        if (effect != null && effect.getHurt() > 0) {
                            if (target2 == null) {
                                continue;
                            }
                            if (target2.isFallDown()) {
                                continue;
                            }
                        }
                        if (effect == null) {
                            if (this.effector.isFallDown() && !this.fight.getDeadPetIds().contains(this.effector.getId())) {
                                final FightObject targetObject = this.fight.queryFightObject(this.effector.getTargetId());
                                this.fight.onActorDie(this.effector);
                                break;
                            }
                            break;
                        }
                        else {
                            if (this.effector.isASide() != target2.isASide()) {
                                PassiveSkillUtils.excutePetPKPetPassiveSkill(this.fight, this.effector, target2, effect);
                            }
                            if (effect.getHurt() > 0 && stunt) {
                                effect.setHurt(Math.max(1, (int)(effect.getHurt() * 1.5)));
                            }
                            if (i > 0) {
                                final double percent = this.effector.getBeAttackedMap().getDouble(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC_HURT_PERCENT.getDist()), 0.0);
                                if (percent > 0.0) {
                                    effect.setHurt((int)(effect.getHurt() * percent));
                                }
                            }
                            battleUnit.setStunt(stunt);
                            battleUnit.setSideId(sideId);
                            battleUnit.setDoubleHit(hitTimes - 1);
                            battleUnit.setMagicDoubleHitI(i + 1);
                            battleUnit.setEffect(effect);
                            battleUnit.triggerMagic();
                            final ActionDetailParam actionDetailParamReturn = battleUnit.getActionDetailParam();
                            if (actionDetailParam == null) {
                                final RespFightShouting fightShouting = fightMessageService.fightShouting(this.effector, "法攻");
                                final FightObjectParam attackerParam = actionDetailParamReturn.gainAttackerParam();
                                ActionParamUtils.updateObjectInfo(this.effector, 7, this.skill.getSkillId(), 2, fightShouting, attackerParam);
                                if (this.effector.isPlayer() && FightOtherUtil.isPlayerSkill(this.skill.getSkillId()) == LOGIC_SKILL.LOGIC_TYPE_SKILL.getValue()) {
                                    final SysDataAcsrySuit acsrySuit = AcsrySkillUtil.getAcsrySuit(this.effector.getRole());
                                    if (acsrySuit != null) {
                                        final Map<Integer, SysDataAcsrySuitColor> acsrySuitColorMap = EquipDataPool.acsrySuitColorMap.get(acsrySuit.getDist());
                                        if (acsrySuitColorMap != null) {
                                            final SysDataAcsrySuitColor acsrySuitColor = acsrySuitColorMap.get(this.skill.getSkillId());
                                            if (acsrySuitColor != null) {
                                                actionDetailParamReturn.setColorId(acsrySuitColor.getColorId());
                                            }
                                        }
                                    }
                                }
                                actionDetailParam = actionDetailParamReturn;
                            }
                            else if (actionDetailParamReturn.getTargetParamList().size() > 0) {
                                actionDetailParam.getTargetParamList().addAll(actionDetailParamReturn.getTargetParamList());
                            }
                            if (magicAttackParam.getHurt() == 0) {
                                magicAttackParam.setHurt(effect.getHurt());
                            }
                            if (battleUnit.isDodge()) {
                                magicAttackParam.setDodge(false);
                            }
                            else {
                                magicAttackParam.setDodge(true);
                            }
                            magicAttackParam.setClearStatusList(battleUnit.getClearStatusList());
                            magicAttackParam.setTargetReduceMp(battleUnit.getTargetReduceMp());
                            magicAttackParam.setSideTarget(battleUnit.getSideTarget());
                            ImbueUtil.clearTemps(this.effector.getImbueMap());
                            if (j == this.skillTargets.size() - 1) {}
                            if ("add_to_target".equals(effect.getOtherType())) {
                                this.processor.effect(this.fight, this.effector, target2, stunt, null, 1);
                            }
                        }
                    }
                }
            }
            if (actionDetailParam != null) {
                this.actionDetailParams.add(actionDetailParam);
            }
        }
        this.magicTargets = targets;
    }
    
    public void dragonMorph() {
        this.effector.setMorph(0);
        this.processor.effect(this.fight, this.effector, null, new Object[0]);
    }
    
    public ActionDetailParam fetchActionDetail() {
        ActionDetailParam actionDetailParam = null;
        if (this.actionDetailParams.size() <= 0) {
            actionDetailParam = new ActionDetailParam();
            this.actionDetailParams.add(actionDetailParam);
        }
        else {
            actionDetailParam = this.actionDetailParams.get(this.actionDetailParams.size() - 1);
        }
        return actionDetailParam;
    }
    
    public Logger getLogger() {
        return this.logger;
    }
    
    public Fight getFight() {
        return this.fight;
    }
    
    public FightObject getEffector() {
        return this.effector;
    }
    
    public RoleSkill getSkill() {
        return this.skill;
    }
    
    public List<Integer> getSkillTargets() {
        return this.skillTargets;
    }
    
    public List<Integer> getSideTargets() {
        return this.sideTargets;
    }
    
    public SkillProcessor getProcessor() {
        return this.processor;
    }
    
    public List<MagicAttackParam> getMagicTargets() {
        return this.magicTargets;
    }
    
    public List<ActionDetailParam> getActionDetailParams() {
        return this.actionDetailParams;
    }
    
    public void setLogger(final Logger logger) {
        this.logger = logger;
    }
    
    public void setFight(final Fight fight) {
        this.fight = fight;
    }
    
    public void setEffector(final FightObject effector) {
        this.effector = effector;
    }
    
    public void setSkill(final RoleSkill skill) {
        this.skill = skill;
    }
    
    public void setSkillTargets(final List<Integer> skillTargets) {
        this.skillTargets = skillTargets;
    }
    
    public void setSideTargets(final List<Integer> sideTargets) {
        this.sideTargets = sideTargets;
    }
    
    public void setProcessor(final SkillProcessor processor) {
        this.processor = processor;
    }
    
    public void setMagicTargets(final List<MagicAttackParam> magicTargets) {
        this.magicTargets = magicTargets;
    }
    
    public void setActionDetailParams(final List<ActionDetailParam> actionDetailParams) {
        this.actionDetailParams = actionDetailParams;
    }
    
    @ConstructorProperties({ "logger", "fight", "effector", "skill", "skillTargets", "sideTargets", "processor", "magicTargets", "actionDetailParams" })
    public SkillAction(final Logger logger, final Fight fight, final FightObject effector, final RoleSkill skill, final List<Integer> skillTargets, final List<Integer> sideTargets, final SkillProcessor processor, final List<MagicAttackParam> magicTargets, final List<ActionDetailParam> actionDetailParams) {
        this.logger = LoggerFunction.FIGHT.getLogger();
        this.actionDetailParams = new ArrayList<ActionDetailParam>();
        this.logger = logger;
        this.fight = fight;
        this.effector = effector;
        this.skill = skill;
        this.skillTargets = skillTargets;
        this.sideTargets = sideTargets;
        this.processor = processor;
        this.magicTargets = magicTargets;
        this.actionDetailParams = actionDetailParams;
    }
    
    public SkillAction() {
        this.logger = LoggerFunction.FIGHT.getLogger();
        this.actionDetailParams = new ArrayList<ActionDetailParam>();
    }
}
