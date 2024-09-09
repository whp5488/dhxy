package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import java.util.List;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31514 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31514() {
    }
    
    @Override
    public NutMap additionSuit(Role role, Pet pet, Object... args) {
        byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
        if (suitLevParam > 0) {
            double percenta = this.getPercent((short)suitLevParam);
            NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.TM_KB_RATIO.getDist()), Double.valueOf(percenta));
            map.put(String.valueOf(PRO_DICT_TYPE.QM_KB_RATIO.getDist()), Double.valueOf(percenta));
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap additionAwaken(Role role, Pet pet, Object... args) {
        short awaken = pet.getAwaken();
        if (awaken > 0) {
            double percenta = this.getPercent(awaken);
            NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.TM_KB_RATIO.getDist()), Double.valueOf(percenta));
            map.put(String.valueOf(PRO_DICT_TYPE.QM_KB_RATIO.getDist()), Double.valueOf(percenta));
            return map;
        }
        return null;
    }
    
    private double getPercent(short lev) {
        return (double)(25 * lev) / 20.0 / 100.0;
    }
    
    @Override
    public void magicAttackEndOther(Fight fight, FightObject effector, Object... args) {
        if (args != null && args.length > 1) {
            RoleSkill skill = (RoleSkill)args[0];
            if (skill != null && (PETSKILLATTR.PETSKILLATTR_FG.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == (int)skill.getType())) {
                List<MagicAttackParam> magicTargets = (List)args[1];
                if (magicTargets != null && magicTargets.size() > 0) {
                    ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 31514);
                    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 30.0, 20.0);
                    if (map == null || map.getDouble("percent") <= 0.0) {
                        return;
                    }
                    double percentb = map.getDouble("percent");
                    if (percentb > 1.0) {
                        percentb = 1.0;
                    }
                    List<FightObjectParam> targetParams = new ArrayList<>();
                    int round = 2;
                    int i = 0;
                    for (MagicAttackParam magicAttackParam : magicTargets) {
                        if (magicAttackParam.getTarget() != null) {
                            FightObject target = fight.queryFightObject(magicAttackParam.getTarget().getId());
                            if (target != null && !target.isFallDown() && !target.isFrozen()) {
                                if (i == 0) {
                                    actionDetailParam.setTargetId((int)target.getId());
                                }
                                ++i;
                                int maxMana = target.getMaxMana();
                                target.setStatus(2169, round);
                                target.getStatusSubMap().setv("2169_max_mana", Integer.valueOf(maxMana));
                                int newMaxMana = (int)((double)maxMana * (1.0 - percentb));
                                int reduceMana = newMaxMana - target.getMaxMana();
                                target.setMaxMana(newMaxMana);
                                FightObjectParam targetParam = new FightObjectParam();
                                targetParam.setChangeMana(reduceMana);
                                if (newMaxMana < target.getCurrMana()) {
                                    target.setCurrMana(newMaxMana);
                                }
                                targetParam.setMaxMana(target.getMaxMana());
                                targetParam.setMissed(2);
                                targetParam.getStatusMap().put(Integer.valueOf(2169), Integer.valueOf(round));
                                int size = targetParams.size();
                                targetParam.setOrder(size);
                                targetParam.setObjId((int)target.getId());
                                targetParams.add(targetParam);
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                    if (targetParams.size() > 0) {
                        actionDetailParam.setTargetParamList(targetParams);
                        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
                        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 31514);
                        skillRecord.setT((long)actionDetailParam.getTargetId());
                    }
                }
            }
        }
    }
    
    @Override
    public void statusOver(Fight fight, FightObject fightObject, Object... args) {
        if (args != null && args.length > 0) {
            int status = (int)args[0];
            if (2169 == status) {
                int oldMaxMana = fightObject.getMaxMana();
                fightObject.setMaxMana(fightObject.getStatusSubMap().getInt("2169_max_mana"));
                ActionDetailParam actionDetailParam = new ActionDetailParam(fightObject, 43, 0, (int)fightObject.getId());
                FightObjectParam targetParam = new FightObjectParam();
                targetParam.setMaxMana(fightObject.getMaxMana());
                targetParam.setChangeMana(fightObject.getMaxMana() - oldMaxMana);
                targetParam.setMissed(2);
                targetParam.setOrder(1);
                targetParam.setObjId((int)fightObject.getId());
                actionDetailParam.getTargetParamList().add(targetParam);
                fight.getActionParam().getActionDetailParams().add(actionDetailParam);
            }
        }
    }
}
