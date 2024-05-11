
package com.kitty.system.game.fightnew.book.drug.effect;

import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.book.vo.Effect;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;

public class DRUG_10027 extends DrugProcessor
{
    @Override
    public NutMap effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final Effect e = new Effect();
        int reduceLife;
        if (target.isMonster() && target.getSerial() == 1660031) {
            reduceLife = 10000;
        }
        else {
            reduceLife = 1;
        }
        if (target.getCurrLife() < reduceLife) {
            reduceLife = target.getCurrLife();
        }
        ActionParamUtils.changeLife(fight, effector, target, 5, 10027, 2, -reduceLife, false);
        return null;
    }
}
