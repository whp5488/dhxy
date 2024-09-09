package com.kitty.system.game.fightnew.book.awaken.intensify;

import java.util.Iterator;
import java.util.List;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31422 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31422() {
    }
    
    @Override
    public void beforePhysicalAttack(Fight fight, FightObject effector, Object... args) {
        NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 30.0, 20.0);
        if (map == null) {
            return;
        }
        double percent = map.getDouble("percent");
        if (percent > 0.0 && SkillAi.canAffectOnPro(percent)) {
            List<FightObject> mySide = fight.getMySide((int)effector.getId());
            int allPhyPower = 0;
            for (FightObject obj : mySide) {
                if (obj.isPet() && !obj.getId().equals(effector.getId())) {
                    allPhyPower += obj.getPhyPower();
                }
                else {
                    continue;
                }
            }
            if (allPhyPower > 0) {
                double v = (double)allPhyPower * 0.5;
                double addPercent = v / (double)effector.getPhyPower();
                ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.RISE_ATTACK.getDist()), 1, addPercent);
            }
        }
        return;
    }
}
