
package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import java.util.ArrayList;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31416 extends AwakenSkillProcessor
{
    @Override
    public NutMap enteringSide(final Fight fight, final FightObject effector, final Object... args) {
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 15.0, 20.0);
        if (map != null) {
            double percent = map.getDouble("percent");
            if (percent > 0.0) {
                if (percent > 0.3) {
                    percent = 0.3;
                }
                final double selfHit = effector.getSelfAllTempPro(PRO_DICT_TYPE.HIT_RATIO.getDist());
                if (selfHit > 0.0) {
                    final double addHit = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(selfHit), String.valueOf(percent)));
                    final List<Integer> targets = new ArrayList<Integer>();
                    for (final FightObject obj : fight.getMySide(effector.getId())) {
                        if (obj.isPet()) {
                            ImbueUtil.addRound(obj.getImbueMap(), PRO_DICT_TYPE.HIT_RATIO.getDist(), 2, addHit);
                            targets.add(obj.getId());
                        }
                    }
                    if (targets.size() > 0) {
                        ActionParamUtils.actionForTargetsNoth(fight, effector, targets, 7, 31416, 2);
                        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31416);
                        skillRecord.setT(targets.get(0));
                    }
                }
            }
        }
        return null;
    }
}
