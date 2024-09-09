package com.kitty.system.game.fightnew.book.train.five;

import com.kitty.system.game.fightnew.book.train.utils.TrainSkillUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.train.TrainSkillProcessor;

public class TRAINSKILL_80505 extends TrainSkillProcessor
{
    public TRAINSKILL_80505() {
    }
    
    @Override
    public void roundStart(Fight fight, FightObject effector, Object... objs) {
        if (effector.isPlayer()) {
            FightObject petObject = fight.queryFightObject(Integer.valueOf(effector.getFightPetId()));
            if (petObject != null && !petObject.isFallDown() && petObject.getComeRound() == fight.getRound()) {
                double percent = TrainSkillUtil.getPercent((long)effector.getRole().getId(), 80505, 0);
                if (percent > 0.0 && petObject.getSpeed() != 0) {
                    int speedAbs = Math.abs(petObject.getSpeed());
                    int changeSpeed = (int)((double)speedAbs * percent);
                    if (changeSpeed > 0) {
                        petObject.setSpeed(petObject.getSpeed() + changeSpeed);
                    }
                }
            }
        }
    }
}
