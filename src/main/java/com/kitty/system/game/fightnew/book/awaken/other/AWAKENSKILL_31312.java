
package com.kitty.system.game.fightnew.book.awaken.other;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31312 extends AwakenSkillProcessor
{
    @Override
    public void phyAttackEnd(final Fight fight, final FightObject effector, final Object... args) {
        if (args == null || args.length < 2) {
            return;
        }
        final BattleUnit battleUnit = (BattleUnit)args[0];
        final ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
        if (battleUnit == null || actionDetailParam == null) {
            return;
        }
        final FightObject target = fight.queryFightObject(battleUnit.getTarget().getId());
        if (target == null || target.isFallDown()) {
            return;
        }
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 5.0, 20.0);
        if (map == null) {
            return;
        }
        final double percent = map.getDouble("percent");
        if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
            int addSd = 0;
            if (target.getSpeed() != 0) {
                addSd = (int)(target.getSpeed() * 0.25);
            }
            if (addSd > 0) {
                ImbueUtil.addRound(target.getImbueMap(), PRO_DICT_TYPE.RISE_SPEED.getDist(), 1, -addSd);
            }
            else if (addSd < 0) {
                ImbueUtil.addRound(target.getImbueMap(), PRO_DICT_TYPE.RISE_SPEED.getDist(), 1, addSd);
            }
        }
    }
}
