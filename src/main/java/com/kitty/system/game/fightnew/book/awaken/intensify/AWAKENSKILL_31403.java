
package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31403 extends AwakenSkillProcessor
{
    @Override
    public NutMap skillIntensify(final Fight fight, final PetFightObject petFightObject, final Object... args) {
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble(petFightObject, 30.0, 20.0);
        if (map == null) {
            return null;
        }
        final double percent = map.getDouble("percent");
        if (percent != 0.0) {
            final FightObject target = (FightObject)args[0];
            if (target != null) {
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1015, PRO_DICT_TYPE.K_F.getDist(), 3, -percent);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1015, PRO_DICT_TYPE.K_L.getDist(), 3, -percent);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1015, PRO_DICT_TYPE.K_S.getDist(), 3, -percent);
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1015, PRO_DICT_TYPE.K_H.getDist(), 3, -percent);
            }
        }
        return null;
    }
}
