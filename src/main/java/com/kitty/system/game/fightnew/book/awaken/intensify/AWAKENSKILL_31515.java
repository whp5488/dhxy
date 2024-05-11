
package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.HashMap;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import java.util.Map;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31515 extends AwakenSkillProcessor
{
    @Override
    public void magicAttackEnd(final Fight fight, final FightObject effector, final Object... args) {
        if (args == null && args.length < 3) {
            return;
        }
        final RoleSkill skill = (RoleSkill)args[0];
        if (830341 != skill.getSkillId() && 830342 != skill.getSkillId() && 830343 != skill.getSkillId() && 830344 != skill.getSkillId() && 830345 != skill.getSkillId()) {
            return;
        }
        final NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 60.0, 20.0);
        final Map<String, Integer> dictMap = this.getDict(skill.getSkillId());
        if (dictMap.size() > 1 && map != null) {
            final double percent = map.getDouble("percent");
            if (percent > 0.0) {
                final int time = 2;
                final Integer status = dictMap.get("s");
                final FightObject host = fight.queryFightObject(effector.getHostId());
                host.setStatus(status, time);
                ImbueUtil.addOrUpdStatus(host.getImbueMap(), status, dictMap.get("d"), time, percent);
                final ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
                final FightObjectParam targetHost = actionDetailParam.gainTargetParam(host.getId(), true);
                targetHost.setMissed(2);
                targetHost.getStatusMap().put(status, time);
            }
        }
    }
    
    private Map<String, Integer> getDict(final int skill) {
        final Map<String, Integer> map = new HashMap<String, Integer>();
        switch (skill) {
            case 830341: {
                map.put("s", 2171);
                map.put("d", PRO_DICT_TYPE.Q_KJ.getDist());
                break;
            }
            case 830342: {
                map.put("s", 2172);
                map.put("d", PRO_DICT_TYPE.Q_KM.getDist());
                break;
            }
            case 830343: {
                map.put("s", 2173);
                map.put("d", PRO_DICT_TYPE.Q_KS.getDist());
                break;
            }
            case 830344: {
                map.put("s", 2174);
                map.put("d", PRO_DICT_TYPE.Q_KH.getDist());
                break;
            }
            case 830345: {
                map.put("s", 2175);
                map.put("d", PRO_DICT_TYPE.Q_KT.getDist());
                break;
            }
        }
        return map;
    }
}
