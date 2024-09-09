package com.kitty.system.game.fightnew.book.drug.effect;

import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.book.drug.utils.DrugUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;

public class DRUG_10025 extends DrugProcessor
{
    public DRUG_10025() {
    }
    
    @Override
    public NutMap effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (target == null || target.isFallDown() || target.getStatusValue(1011) <= 0) {
            target = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
        }
        DrugUtil.clearCloaking(fight, effector, target, 10025);
        return null;
    }
    
    @Override
    public boolean checkExcute(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (target == null || target.isFallDown() || target.getStatusValue(1011) <= 0) {
            List<FightObject> targetSide = (List)fight.getTargetSide((int)effector.getId()).stream().filter(obj -> !obj.isFallDown() && obj.getStatusValue(1011) > 0).collect(Collectors.toList());
            if (targetSide.size() > 0) {
                effector.setTargetId((int)((FightObject)targetSide.get(0)).getId());
            }
            else {
                return false;
            }
        }
        return true;
    }
}
