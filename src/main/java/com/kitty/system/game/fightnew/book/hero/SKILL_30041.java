
package com.kitty.system.game.fightnew.book.hero;

import com.kitty.system.game.role.domain.Role;
import java.util.Map;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.util.HashMap;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30041 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(30041);
        if (skill != null && !effector.isFrozen() && !effector.isSleep() && !effector.isConfusion() && !effector.isForgotten()) {
            final int round = effector.getSkillCoolMap().getInt(String.valueOf(30041));
            if (round <= 0) {
                final SysSkill sysSkill = SkillDataPool.skillMap.get(30041);
                final double[] power = sysSkill.getPower();
                final int currLife = effector.getCurrLife();
                final int maxLife = effector.getMaxLife();
                final int life_gain = (int)(currLife * power[0]);
                final int life_max_gain = (int)(maxLife * power[0]);
                effector.setMaxLife(maxLife + life_max_gain);
                effector.setCurrLife(currLife + life_gain);
                effector.getStatusSubMap().setv("1023_max_life", (Object)maxLife);
                final int currMana = effector.getCurrMana();
                final int maxMana = effector.getMaxMana();
                final int mana_gain = (int)(currMana * power[1]);
                final int mana_max_gain = (int)(maxMana * power[1]);
                effector.setMaxMana(maxMana + mana_max_gain);
                effector.setCurrMana(currMana + mana_gain);
                effector.getStatusSubMap().setv("1023_max_mana", (Object)maxMana);
                effector.getSkillCoolMap().setv(String.valueOf(30041), (Object)4);
                final int atk = effector.getPhyPower() + (int)(effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ATTACK.getDist()) * effector.getPhyPower());
                final double addAtk = 0.2 * atk;
                final double percent = addAtk / effector.getPhyPower();
                ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1023, PRO_DICT_TYPE.RISE_ATTACK.getDist(), 2, percent);
                effector.setStatus(1023, 2);
                final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 30041, effector.getId());
                final FightObjectParam targetParam = new FightObjectParam();
                targetParam.setChangeLife(life_gain);
                targetParam.setMaxLife(effector.getMaxLife());
                targetParam.setMaxMana(effector.getMaxMana());
                targetParam.setCurrentLife(effector.getCurrLife());
                targetParam.setCurrentMana(effector.getMaxMana());
                final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
                map.put(1023, 2);
                targetParam.setMissed(2);
                final int size = actionDetailParam.getTargetParamList().size();
                targetParam.setOrder(size);
                targetParam.setObjId(effector.getId());
                actionDetailParam.getTargetParamList().add(targetParam);
                fight.getActionParam().getActionDetailParams().add(actionDetailParam);
            }
            else {
                final Role role = effector.getRole();
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("变身技能冷却中。"));
            }
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
