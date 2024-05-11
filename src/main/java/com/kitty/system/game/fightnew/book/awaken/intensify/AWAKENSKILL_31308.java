
package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Map;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.HashMap;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import java.util.List;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31308 extends AwakenSkillProcessor
{
    @Override
    public void magicAttackEnd(final Fight fight, final FightObject effector, final Object... args) {
        if (effector.isPet()) {
            if (args == null && args.length < 3) {
                return;
            }
            final RoleSkill skill = (RoleSkill)args[0];
            final ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
            final List<MagicAttackParam> targets = (List<MagicAttackParam>)args[2];
            if (skill == null || actionDetailParam == null || targets == null) {
                return;
            }
            final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 50.0, 20.0);
            if (map == null) {
                return;
            }
            final double percent = map.getDouble("percent");
            if (percent != 0.0 && SkillAi.canAffectOnPro(percent) && effector.getSpeed() > 0) {
                final double addCpt = 0.15;
                final int result = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(effector.getSpeed()), String.valueOf(addCpt)));
                final int time = 3;
                effector.setStatus(1010, time);
                ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), time, result);
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(effector.getId(), true);
                final Map<Integer, Integer> statusMap = new HashMap<Integer, Integer>();
                statusMap.put(1010, time);
                targetParam.setStatusMap(statusMap);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 31308);
                skillRecord.setT(effector.getId());
            }
        }
    }
}
