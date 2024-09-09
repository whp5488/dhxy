package com.kitty.system.game.fightnew.book.hero;

import java.util.Map;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.HashMap;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_30041 extends SkillProcessor
{
    public SKILL_30041() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(30041));
        if (skill != null && !effector.isFrozen() && !effector.isSleep() && !effector.isConfusion() && !effector.isForgotten()) {
            int round = effector.getSkillCoolMap().getInt(String.valueOf(30041));
            if (round <= 0) {
                SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30041));
                double[] power = sysSkill.getPower();
                int currLife = effector.getCurrLife();
                int maxLife = effector.getMaxLife();
                int life_gain = (int)((double)currLife * power[0]);
                int life_max_gain = (int)((double)maxLife * power[0]);
                effector.setMaxLife(maxLife + life_max_gain);
                effector.setCurrLife(currLife + life_gain);
                effector.getStatusSubMap().setv("1023_max_life", Integer.valueOf(maxLife));
                int currMana = effector.getCurrMana();
                int maxMana = effector.getMaxMana();
                int mana_gain = (int)((double)currMana * power[1]);
                int mana_max_gain = (int)((double)maxMana * power[1]);
                effector.setMaxMana(maxMana + mana_max_gain);
                effector.setCurrMana(currMana + mana_gain);
                effector.getStatusSubMap().setv("1023_max_mana", Integer.valueOf(maxMana));
                effector.getSkillCoolMap().setv(String.valueOf(30041), Integer.valueOf(4));
                int atk = effector.getPhyPower() + (int)(effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ATTACK.getDist()) * (double)effector.getPhyPower());
                double addAtk = 0.2 * (double)atk;
                double percent = addAtk / (double)effector.getPhyPower();
                ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1023, PRO_DICT_TYPE.RISE_ATTACK.getDist(), 2, percent);
                effector.setStatus(1023, 2);
                ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 30041, (int)effector.getId());
                FightObjectParam targetParam = new FightObjectParam();
                targetParam.setChangeLife(life_gain);
                targetParam.setMaxLife(effector.getMaxLife());
                targetParam.setMaxMana(effector.getMaxMana());
                targetParam.setCurrentLife(effector.getCurrLife());
                targetParam.setCurrentMana(effector.getMaxMana());
                Map<Integer, Integer> map = new HashMap();
                map.put(Integer.valueOf(1023), Integer.valueOf(2));
                targetParam.setMissed(2);
                int size = actionDetailParam.getTargetParamList().size();
                targetParam.setOrder(size);
                targetParam.setObjId((int)effector.getId());
                actionDetailParam.getTargetParamList().add(targetParam);
                fight.getActionParam().getActionDetailParams().add(actionDetailParam);
            }
            else {
                Role role = effector.getRole();
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("变身技能冷却中。"));
            }
        }
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
