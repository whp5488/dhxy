
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81003 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final ImbueParam kf = ImbueUtil.gainStatusValue(effector.getImbueMap(), 2031, PRO_DICT_TYPE.K_F.getDist());
        final double kfValue = (kf.getValue() + 0.02 > 0.4) ? 0.4 : (kf.getValue() + 0.02);
        kf.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
        kf.setValue(kfValue);
        final ImbueParam kl = ImbueUtil.gainStatusValue(effector.getImbueMap(), 2031, PRO_DICT_TYPE.K_L.getDist());
        final double klValue = (kl.getValue() + 0.02 > 0.4) ? 0.4 : (kl.getValue() + 0.02);
        kl.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
        kl.setValue(klValue);
        final ImbueParam ks = ImbueUtil.gainStatusValue(effector.getImbueMap(), 2031, PRO_DICT_TYPE.K_S.getDist());
        final double ksValue = (ks.getValue() + 0.02 > 0.4) ? 0.4 : (ks.getValue() + 0.02);
        ks.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
        ks.setValue(ksValue);
        final ImbueParam kh = ImbueUtil.gainStatusValue(effector.getImbueMap(), 2031, PRO_DICT_TYPE.K_H.getDist());
        final double khValue = (kh.getValue() + 0.02 > 0.4) ? 0.4 : (kh.getValue() + 0.02);
        kh.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
        kh.setValue(khValue);
        final ImbueParam kgh = ImbueUtil.gainStatusValue(effector.getImbueMap(), 2031, PRO_DICT_TYPE.K_GH.getDist());
        final double kghValue = (kgh.getValue() + 0.02 > 0.4) ? 0.4 : (kgh.getValue() + 0.02);
        kgh.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
        kgh.setValue(kghValue);
        effector.getStatusMap().setv(String.valueOf(2031), (Object)WarMaxRound.WAR_MAX_ROUND.getValue());
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 0;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
