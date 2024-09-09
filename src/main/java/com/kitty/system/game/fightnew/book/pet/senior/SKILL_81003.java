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
    public SKILL_81003() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        ImbueParam kf = ImbueUtil.gainStatusValue(effector.getImbueMap(), 2031, PRO_DICT_TYPE.K_F.getDist());
        double kfValue = (kf.getValue() + 0.02 > 0.4) ? 0.4 : (kf.getValue() + 0.02);
        kf.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
        kf.setValue(kfValue);
        ImbueParam kl = ImbueUtil.gainStatusValue(effector.getImbueMap(), 2031, PRO_DICT_TYPE.K_L.getDist());
        double klValue = (kl.getValue() + 0.02 > 0.4) ? 0.4 : (kl.getValue() + 0.02);
        kl.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
        kl.setValue(klValue);
        ImbueParam ks = ImbueUtil.gainStatusValue(effector.getImbueMap(), 2031, PRO_DICT_TYPE.K_S.getDist());
        double ksValue = (ks.getValue() + 0.02 > 0.4) ? 0.4 : (ks.getValue() + 0.02);
        ks.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
        ks.setValue(ksValue);
        ImbueParam kh = ImbueUtil.gainStatusValue(effector.getImbueMap(), 2031, PRO_DICT_TYPE.K_H.getDist());
        double khValue = (kh.getValue() + 0.02 > 0.4) ? 0.4 : (kh.getValue() + 0.02);
        kh.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
        kh.setValue(khValue);
        ImbueParam kgh = ImbueUtil.gainStatusValue(effector.getImbueMap(), 2031, PRO_DICT_TYPE.K_GH.getDist());
        double kghValue = (kgh.getValue() + 0.02 > 0.4) ? 0.4 : (kgh.getValue() + 0.02);
        kgh.setSr(WarMaxRound.WAR_MAX_ROUND.getValue());
        kgh.setValue(kghValue);
        effector.getStatusMap().setv(String.valueOf(2031), Integer.valueOf(WarMaxRound.WAR_MAX_ROUND.getValue()));
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 0;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
