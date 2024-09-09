package com.kitty.system.game.fightnew.book.train;

import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80512;
import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80511;
import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80510;
import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80509;
import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80508;
import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80507;
import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80506;
import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80505;
import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80504;
import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80503;
import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80502;
import com.kitty.system.game.fightnew.book.train.five.TRAINSKILL_80501;
import com.kitty.system.game.fightnew.book.train.four.TRAINSKILL_80406;
import com.kitty.system.game.fightnew.book.train.four.TRAINSKILL_80405;
import com.kitty.system.game.fightnew.book.train.four.TRAINSKILL_80404;
import com.kitty.system.game.fightnew.book.train.four.TRAINSKILL_80403;
import com.kitty.system.game.fightnew.book.train.four.TRAINSKILL_80402;
import com.kitty.system.game.fightnew.book.train.four.TRAINSKILL_80401;
import java.util.HashMap;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import java.util.Map;

public abstract class TrainSkillProcessor
{
    static Map<Integer, TrainSkillProcessor> trains;
    
    public TrainSkillProcessor() {
    }
    
    public static TrainSkillProcessor queryProcessor(Integer serial) {
        return (TrainSkillProcessor)TrainSkillProcessor.trains.get(serial);
    }
    
    public void roundStart(Fight fight, FightObject effector, Object... objs) {
    }
    
    public void beforeMagicAtta(Fight fight, FightObject effector, FightObject target, Object... objs) {
    }
    
    public int changeHurt(Fight fight, FightObject effector, FightObject target, Object... objs) {
        return 0;
    }
    
    public int changeHurtMp(Fight fight, FightObject effector, FightObject target, Object... objs) {
        return 0;
    }
    
    public NutMap addStatus(Fight fight, FightObject effector, FightObject target, Object... objs) {
        return null;
    }
    
    public NutMap reduceLifeEnd(Fight fight, FightObject effector, FightObject target, Object... objs) {
        return null;
    }
    
    public void magicAttackEnd(Fight fight, FightObject effector, FightObject target, Object... objs) {
    }
    
    public void magicAttackEndOne(Fight fight, FightObject effector, FightObject target, Object... objs) {
    }
    
    public void normalAttackAfter(Fight fight, FightObject effector, FightObject target, Object... objs) {
    }
    
    static {
        (TrainSkillProcessor.trains = new HashMap()).put(Integer.valueOf(80401), new TRAINSKILL_80401());
        TrainSkillProcessor.trains.put(Integer.valueOf(80402), new TRAINSKILL_80402());
        TrainSkillProcessor.trains.put(Integer.valueOf(80403), new TRAINSKILL_80403());
        TrainSkillProcessor.trains.put(Integer.valueOf(80404), new TRAINSKILL_80404());
        TrainSkillProcessor.trains.put(Integer.valueOf(80405), new TRAINSKILL_80405());
        TrainSkillProcessor.trains.put(Integer.valueOf(80406), new TRAINSKILL_80406());
        TrainSkillProcessor.trains.put(Integer.valueOf(80501), new TRAINSKILL_80501());
        TrainSkillProcessor.trains.put(Integer.valueOf(80502), new TRAINSKILL_80502());
        TrainSkillProcessor.trains.put(Integer.valueOf(80503), new TRAINSKILL_80503());
        TrainSkillProcessor.trains.put(Integer.valueOf(80504), new TRAINSKILL_80504());
        TrainSkillProcessor.trains.put(Integer.valueOf(80505), new TRAINSKILL_80505());
        TrainSkillProcessor.trains.put(Integer.valueOf(80506), new TRAINSKILL_80506());
        TrainSkillProcessor.trains.put(Integer.valueOf(80507), new TRAINSKILL_80507());
        TrainSkillProcessor.trains.put(Integer.valueOf(80508), new TRAINSKILL_80508());
        TrainSkillProcessor.trains.put(Integer.valueOf(80509), new TRAINSKILL_80509());
        TrainSkillProcessor.trains.put(Integer.valueOf(80510), new TRAINSKILL_80510());
        TrainSkillProcessor.trains.put(Integer.valueOf(80511), new TRAINSKILL_80511());
        TrainSkillProcessor.trains.put(Integer.valueOf(80512), new TRAINSKILL_80512());
    }
}
