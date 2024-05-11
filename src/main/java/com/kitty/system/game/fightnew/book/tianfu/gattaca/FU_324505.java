
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Optional;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324505 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (!effector.isPlayer() && effector.getRace() != RACE.RACE_REN.getValue() && effector.getRace() != RACE.RACE_GUI.getValue()) {
            return null;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324505);
        if (tianFuObject == null) {
            return null;
        }
        final Optional<RoleSkill> skillOptional = effector.getSkillIds().stream().filter(obj -> obj.getSkillId().equals(effector.getSkillId())).findFirst();
        if (skillOptional.isPresent()) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324505);
            final RoleSkill roleSkill = skillOptional.get();
            final double percenta = 0.8 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            if (SKILLATTR.SKILLATTR_CONFUSE.getValue() == roleSkill.getType()) {
                this.addProperty(fight, effector, 1002, PRO_DICT_TYPE.RISE_HL.getDist(), percenta);
                skillRecord.setT(effector.getId());
            }
            else if (SKILLATTR.SKILLATTR_ICE.getValue() == roleSkill.getType()) {
                this.addProperty(fight, effector, 1003, PRO_DICT_TYPE.RISE_FY.getDist(), percenta);
                skillRecord.setT(effector.getId());
            }
            else if (SKILLATTR.SKILLATTR_SLEEP.getValue() == roleSkill.getType()) {
                this.addProperty(fight, effector, 1004, PRO_DICT_TYPE.RISE_HS.getDist(), percenta);
                skillRecord.setT(effector.getId());
            }
            else if (SKILLATTR.SKILLATTR_YIWANG.getValue() == roleSkill.getType()) {
                this.addProperty(fight, effector, 1006, PRO_DICT_TYPE.RISE_YW.getDist(), percenta);
                skillRecord.setT(effector.getId());
            }
        }
        return null;
    }
    
    public boolean getTriggerPercent(final Fight fight, final Integer effectorId, final int status) {
        double percent = 0.1;
        final List<FightObject> mySide = fight.getMySide(effectorId).stream().filter(obj -> obj.getStatusValue(status) > 0).collect(Collectors.toList());
        if (mySide != null && mySide.size() > 0) {
            percent += percent * mySide.size();
        }
        return SkillAi.canAffectOnPro(percent);
    }
    
    private void addProperty(final Fight fight, final FightObject effector, final int status, final int dist, final double percenta) {
        if (this.getTriggerPercent(fight, effector.getId(), status)) {
            ImbueUtil.addRound(effector.getImbueMap(), dist, 1, percenta);
        }
    }
}
