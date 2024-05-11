
package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
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
    @Override
    public NutMap additionSuit(final Role role, final Pet pet, final Object... args) {
        final byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
        if (suitLevParam > 0) {
            final double percenta = this.getPercent(suitLevParam);
            final NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.TM_KB_RATIO.getDist()), (Object)percenta);
            map.put(String.valueOf(PRO_DICT_TYPE.QM_KB_RATIO.getDist()), (Object)percenta);
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap additionAwaken(final Role role, final Pet pet, final Object... args) {
        final short awaken = pet.getAwaken();
        if (awaken > 0) {
            final double percenta = this.getPercent(awaken);
            final NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.TM_KB_RATIO.getDist()), (Object)percenta);
            map.put(String.valueOf(PRO_DICT_TYPE.QM_KB_RATIO.getDist()), (Object)percenta);
            return map;
        }
        return null;
    }
    
    private double getPercent(final short lev) {
        return 25 * lev / 20.0 / 100.0;
    }
    
    @Override
    public void magicAttackEndOther(final Fight fight, final FightObject effector, final Object... args) {
        if (args != null && args.length > 1) {
            final RoleSkill skill = (RoleSkill)args[0];
            if (skill != null && (PETSKILLATTR.PETSKILLATTR_FG.getValue() == skill.getType() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == skill.getType())) {
                final List<MagicAttackParam> magicTargets = (List<MagicAttackParam>)args[1];
                if (magicTargets != null && magicTargets.size() > 0) {
                    final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 31514);
                    final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 30.0, 20.0);
                    if (map == null || map.getDouble("percent") <= 0.0) {
                        return;
                    }
                    double percentb = map.getDouble("percent");
                    if (percentb > 1.0) {
                        percentb = 1.0;
                    }
                    final List<FightObjectParam> targetParams = new ArrayList<FightObjectParam>();
                    final int round = 2;
                    int i = 0;
                    for (final MagicAttackParam magicAttackParam : magicTargets) {
                        if (magicAttackParam.getTarget() != null) {
                            final FightObject target = fight.queryFightObject(magicAttackParam.getTarget().getId());
                            if (target == null || target.isFallDown() || target.isFrozen()) {
                                continue;
                            }
                            if (i == 0) {
                                actionDetailParam.setTargetId(target.getId());
                            }
                            ++i;
                            final int maxMana = target.getMaxMana();
                            target.setStatus(2169, round);
                            target.getStatusSubMap().setv("2169_max_mana", (Object)maxMana);
                            final int newMaxMana = (int)(maxMana * (1.0 - percentb));
                            final int reduceMana = newMaxMana - target.getMaxMana();
                            target.setMaxMana(newMaxMana);
                            final FightObjectParam targetParam = new FightObjectParam();
                            targetParam.setChangeMana(reduceMana);
                            if (newMaxMana < target.getCurrMana()) {
                                target.setCurrMana(newMaxMana);
                            }
                            targetParam.setMaxMana(target.getMaxMana());
                            targetParam.setMissed(2);
                            targetParam.getStatusMap().put(2169, round);
                            final int size = targetParams.size();
                            targetParam.setOrder(size);
                            targetParam.setObjId(target.getId());
                            targetParams.add(targetParam);
                        }
                    }
                    if (targetParams.size() > 0) {
                        actionDetailParam.setTargetParamList(targetParams);
                        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
                        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31514);
                        skillRecord.setT(actionDetailParam.getTargetId());
                    }
                }
            }
        }
    }
    
    @Override
    public void statusOver(final Fight fight, final FightObject fightObject, final Object... args) {
        if (args != null && args.length > 0) {
            final int status = (int)args[0];
            if (2169 == status) {
                final int oldMaxMana = fightObject.getMaxMana();
                fightObject.setMaxMana(fightObject.getStatusSubMap().getInt("2169_max_mana"));
                final ActionDetailParam actionDetailParam = new ActionDetailParam(fightObject, 43, 0, fightObject.getId());
                final FightObjectParam targetParam = new FightObjectParam();
                targetParam.setMaxMana(fightObject.getMaxMana());
                targetParam.setChangeMana(fightObject.getMaxMana() - oldMaxMana);
                targetParam.setMissed(2);
                targetParam.setOrder(1);
                targetParam.setObjId(fightObject.getId());
                actionDetailParam.getTargetParamList().add(targetParam);
                fight.getActionParam().getActionDetailParams().add(actionDetailParam);
            }
        }
    }
}
