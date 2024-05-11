
package com.kitty.system.game.fightnew.book.awaken.other;

import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31505 extends AwakenSkillProcessor
{
    @Override
    public void sufferHarm(final Fight fight, final FightObject fightObject, final Object... args) {
        if (args == null || args.length < 2 || fightObject.getSkillCoolMap().getInt(String.valueOf(31505)) > 0) {
            return;
        }
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)fightObject, 50.0, 20.0);
        if (map == null) {
            return;
        }
        final double percent = map.getDouble("percent");
        if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
            final FightObject effector = (FightObject)args[1];
            final double reduce = -0.2;
            final int round = 3;
            ImbueUtil.addOrUpdSkill(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_F.getDist(), round, 31505, reduce);
            ImbueUtil.addOrUpdSkill(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_L.getDist(), round, 31505, reduce);
            ImbueUtil.addOrUpdSkill(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_S.getDist(), round, 31505, reduce);
            ImbueUtil.addOrUpdSkill(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_H.getDist(), round, 31505, reduce);
            ImbueUtil.addOrUpdSkill(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_GH.getDist(), round, 31505, reduce);
            ImbueUtil.addOrUpdSkill(effector.getImbueMap(), PRO_DICT_TYPE.OMIT_K_D.getDist(), round, 31505, reduce);
            ImbueUtil.addOrUpdSkill(effector.getImbueMap(), PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), round, 31505, reduce);
            effector.setStatus(2177, round);
            ActionParamUtils.addStatus(fight, fightObject, effector, 7, 31505, 1, 2177, round);
        }
    }
}
