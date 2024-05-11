
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
    @Override
    public NutMap additionSuit(final Role role, final Pet pet, final Object... args) {
        final byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
        if (suitLevParam > 0) {
            final double percent = this.getPercent(suitLevParam);
            final NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()), (Object)percent);
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap additionAwaken(final Role role, final Pet pet, final Object... args) {
        final short awaken = pet.getAwaken();
        if (awaken > 0) {
            final double percent = this.getPercent(awaken);
            final NutMap map = NutMap.NEW();
            map.put(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()), (Object)percent);
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap enteringSide(final Fight fight, final FightObject effector, final Object... args) {
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 100.0, 20.0);
        if (map != null) {
            final double percent = map.getDouble("percent");
            if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
                final Pet pet = effector.getPet();
                if (pet != null) {
                    final int dict = this.getDict(pet);
                    final NutMap addMap = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 60.0, 20.0);
                    final double add = addMap.getDouble("percent");
                    if (add > 0.0) {
                        ImbueUtil.addRound(effector.getImbueMap(), dict, 3, add * 100.0);
                        ActionParamUtils.actionForTargetNoth(fight, effector, effector, 7, 31509, 2);
                        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31509);
                        skillRecord.setT(effector.getId());
                    }
                }
            }
        }
        return null;
    }
    
    private int getDict(final Pet pet) {
        int dict = 0;
        if (dict < pet.getMj()) {
            dict = PRO_DICT_TYPE.RISE_SPEED.getDist();
        }
        if (dict < pet.getLl()) {
            dict = PRO_DICT_TYPE.RISE_ATTACK.getDist();
        }
        if (dict < pet.getGg()) {
            dict = PRO_DICT_TYPE.RISE_DEF.getDist();
        }
        if (dict < pet.getLx()) {
            dict = PRO_DICT_TYPE.RISE_DEF.getDist();
        }
        return dict;
    }
    
    private double getPercent(final short lev) {
        return 60 * lev / 20.0;
    }
}
