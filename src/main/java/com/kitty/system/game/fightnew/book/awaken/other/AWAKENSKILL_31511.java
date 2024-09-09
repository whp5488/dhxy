package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import java.util.Iterator;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.List;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31511 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31511() {
    }
    
    @Override
    public NutMap enteringSide(Fight fight, FightObject effector, Object... args) {
        NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 25.0, 20.0);
        if (map != null) {
            double percent = map.getDouble("percent");
            if (percent > 0.0) {
                ActionDetailParam actionDetailParam = null;
                boolean a = SkillAi.canAffectOnPro(percent);
                if (a) {
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 31511);
                    List<FightObject> targets = PassiveSkillUtils.getTargets(fight, effector, (byte)1);
                    if (targets != null && targets.size() > 0) {
                        int controlStatus = 0;
                        FightObject target = null;
                        for (FightObject fightObject : targets) {
                            controlStatus = AwakenSkillEffectUtils.getStatus(fightObject, StatusConst.getControlStatus());
                            if (controlStatus > 0) {
                                target = fightObject;
                                break;
                            }
                            continue;
                        }
                        if (target != null && controlStatus > 0) {
                            target.removeStatus(controlStatus);
                            List<Integer> clearList = new ArrayList<>();
                            clearList.add(Integer.valueOf(controlStatus));
                            actionDetailParam = ActionParamUtils.clearStatusReturn(effector, target, 7, 31511, 2, clearList);
                            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
                            skillRecord.setT((long)(int)target.getId());
                        }
                    }
                }
                boolean b = SkillAi.canAffectOnPro(percent);
                if (b) {
                    SkillRecord skillRecord2 = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 31516);
                    List<FightObject> targetSide = fight.getTargetSide((int)effector.getId());
                    if (targetSide != null && targetSide.size() > 0) {
                        List<FightObject> targets2 = PassiveSkillUtils.getTargets(fight, (FightObject)targetSide.get(0), (byte)1);
                        if (targets2 != null && targets2.size() > 0) {
                            List<FightObject> list = new ArrayList<>();
                            for (FightObject obj : targets2) {
                                if (AwakenSkillEffectUtils.getStatus(obj, StatusConst.getMiniPositiveStatus()) > 0) {
                                    list.add(obj);
                                }
                                else {
                                    continue;
                                }
                            }
                            if (list.size() > 0) {
                                int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
                                FightObject target2 = (FightObject)list.get(round);
                                if (target2 != null) {
                                    int status = AwakenSkillEffectUtils.getStatus(target2, StatusConst.getMiniPositiveStatus());
                                    if (status > 0) {
                                        target2.removeStatus(status);
                                        List<Integer> dicts = (List)PRO_DICT_TYPE.getGFSDict().get(Integer.valueOf(status));
                                        if (dicts != null && dicts.size() > 0) {
                                            for (Integer dist : dicts) {
                                                ImbueUtil.removeStatus(target2.getImbueMap(), (int)dist, status);
                                            }
                                        }
                                        List<Integer> clearList2 = new ArrayList<>();
                                        clearList2.add(Integer.valueOf(status));
                                        if (actionDetailParam != null) {
                                            FightObjectParam targetParam = ActionParamUtils.clearStatusForTarget((int)target2.getId(), 2, clearList2, 2);
                                            actionDetailParam.getTargetParamList().add(targetParam);
                                        }
                                        else {
                                            ActionParamUtils.clearStatus(fight, effector, target2, 7, 31511, 2, clearList2);
                                        }
                                    }
                                    skillRecord2.setT((long)(int)target2.getId());
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
