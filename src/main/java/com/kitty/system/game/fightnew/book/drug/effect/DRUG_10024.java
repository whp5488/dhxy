package com.kitty.system.game.fightnew.book.drug.effect;

import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightnew.book.drug.utils.DrugUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;

public class DRUG_10024 extends DrugProcessor
{
    public DRUG_10024() {
    }
    
    @Override
    public NutMap effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        List<FightObject> targetSide = fight.getTargetSide((int)effector.getId());
        for (FightObject targetObject : targetSide) {
            if (targetObject.getStatusMap().getInt(String.valueOf(1011)) > 0) {
                DrugUtil.clearCloaking(fight, effector, targetObject, 10024);
            }
            else {
                continue;
            }
        }
        return null;
    }
    
    @Override
    public boolean checkExcute(Fight fight, FightObject effector, FightObject target, Object... args) {
        List<FightObject> targetSide = fight.getTargetSide((int)effector.getId());
        for (FightObject targetObject : targetSide) {
            if (targetObject.getStatusMap().getInt(String.valueOf(1011)) > 0) {
                return true;
            }
            continue;
        }
        return false;
    }
}
