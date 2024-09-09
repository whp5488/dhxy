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
    public FU_321305() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        RoleSkill roleSkill = (RoleSkill)args[0];
        if (!effector.isPlayer()) {
            return;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321305);
        if (tianFuObject == null) {
            return;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 321305);
        if (SKILLATTR.SKILLATTR_CONFUSE.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_YIWANG.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_SLEEP.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_ICE.getValue() == (int)roleSkill.getType()) {
            int hitCount = (int)args[1];
            if (hitCount <= 0) {
                return;
            }
            double percenta = 0.2 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            int addLife2 = (int)((double)effector.getMaxLife() * percenta * (double)hitCount);
            TianFuUtils.whfmMethod(fight, effector, addLife2, actionDetailParam);
            skillRecord.setT((long)(int)effector.getId());
        }
        else {
            ArrayList<Integer> fallDownList = (ArrayList)effector.getTempCacheValue("fallDownList");
            if (fallDownList == null || fallDownList.size() <= 0) {
                return;
            }
            FightObject host;
            if (effector.isPet()) {
                host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
            }
            else {
                host = effector;
            }
            double percentb = 1.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            int addLife = (int)((double)host.getMaxLife() * percentb * (double)fallDownList.size());
            TianFuUtils.whfmMethod(fight, host, addLife, actionDetailParam);
            skillRecord.setT((long)(int)effector.getId());
        }
        return;
    }
}
