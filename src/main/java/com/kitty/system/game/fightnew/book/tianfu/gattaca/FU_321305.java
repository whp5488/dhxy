
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import java.util.ArrayList;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321305 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public void attackOver(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        final RoleSkill roleSkill = (RoleSkill)args[0];
        if (!effector.isPlayer()) {
            return;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321305);
        if (tianFuObject == null) {
            return;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 321305);
        if (SKILLATTR.SKILLATTR_CONFUSE.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_YIWANG.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_SLEEP.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_ICE.getValue() == roleSkill.getType()) {
            final int hitCount = (int)args[1];
            if (hitCount <= 0) {
                return;
            }
            final double percenta = 0.2 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final int addLife = (int)(effector.getMaxLife() * percenta * hitCount);
            TianFuUtils.whfmMethod(fight, effector, addLife, actionDetailParam);
            skillRecord.setT(effector.getId());
        }
        else {
            final ArrayList<Integer> fallDownList = (ArrayList<Integer>)effector.getTempCacheValue("fallDownList");
            if (fallDownList == null || fallDownList.size() <= 0) {
                return;
            }
            FightObject host;
            if (effector.isPet()) {
                host = fight.queryFightObject(effector.getHostId());
            }
            else {
                host = effector;
            }
            final double percentb = 1.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final int addLife2 = (int)(host.getMaxLife() * percentb * fallDownList.size());
            TianFuUtils.whfmMethod(fight, host, addLife2, actionDetailParam);
            skillRecord.setT(effector.getId());
        }
    }
}
