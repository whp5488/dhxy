package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31509 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31509() {
    }
    
    @Override
    public NutMap additionSuit(Role role, Pet pet, Object... args) {
        byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
        if (suitLevParam > 0) {
            double percent = this.getPercent((short)suitLevParam);
            NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()), Double.valueOf(percent));
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap additionAwaken(Role role, Pet pet, Object... args) {
        short awaken = pet.getAwaken();
        if (awaken > 0) {
            double percent = this.getPercent(awaken);
            NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()), Double.valueOf(percent));
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap enteringSide(Fight fight, FightObject effector, Object... args) {
        NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 100.0, 20.0);
        if (map != null) {
            double percent = map.getDouble("percent");
            if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                Pet pet = effector.getPet();
                if (pet != null) {
                    int dict = this.getDict(pet);
                    NutMap addMap = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 60.0, 20.0);
                    double add = addMap.getDouble("percent");
                    if (add > 0.0) {
                        ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(dict), 3, add * 100.0);
                        ActionParamUtils.actionForTargetNoth(fight, effector, effector, 7, 31509, 2);
                        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 31509);
                        skillRecord.setT((long)(int)effector.getId());
                    }
                }
            }
        }
        return null;
    }
    
    private int getDict(Pet pet) {
        int dict = 0;
        if (dict < (int)pet.getMj()) {
            dict = PRO_DICT_TYPE.RISE_SPEED.getDist();
        }
        if (dict < (int)pet.getLl()) {
            dict = PRO_DICT_TYPE.RISE_ATTACK.getDist();
        }
        if (dict < (int)pet.getGg()) {
            dict = PRO_DICT_TYPE.RISE_DEF.getDist();
        }
        if (dict < (int)pet.getLx()) {
            dict = PRO_DICT_TYPE.RISE_DEF.getDist();
        }
        return dict;
    }
    
    private double getPercent(short lev) {
        return (double)(60 * lev) / 20.0;
    }
}
