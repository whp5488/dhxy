
package com.kitty.system.game.fightnew.book.awaken.intensify;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31502 extends AwakenSkillProcessor
{
    @Override
    public void beforePhysicalAttack(final Fight fight, final FightObject effector, final Object... args) {
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 30.0, 20.0);
        if (map == null) {
            return;
        }
        final double percent = map.getDouble("percent");
        if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
            final int phyAttack = CountUtils.getPhyAttack(effector);
            final double phyA = phyAttack * 1.0 / effector.getPhyPower();
            ImbueUtil.addRound(effector.getImbueMap(), PRO_DICT_TYPE.RISE_ATTACK.getDist(), 1, phyA);
        }
    }
}
