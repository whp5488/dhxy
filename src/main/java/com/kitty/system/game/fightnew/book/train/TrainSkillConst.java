package com.kitty.system.game.fightnew.book.train;

import java.util.Arrays;
import java.util.List;

public interface TrainSkillConst
{
    public static final int TRAINSKILL_80401 = 80401;
    public static final int TRAINSKILL_80402 = 80402;
    public static final int TRAINSKILL_80403 = 80403;
    public static final int TRAINSKILL_80404 = 80404;
    public static final int TRAINSKILL_80405 = 80405;
    public static final int TRAINSKILL_80406 = 80406;
    public static final int TRAINSKILL_80501 = 80501;
    public static final int TRAINSKILL_80502 = 80502;
    public static final int TRAINSKILL_80503 = 80503;
    public static final int TRAINSKILL_80504 = 80504;
    public static final int TRAINSKILL_80505 = 80505;
    public static final int TRAINSKILL_80506 = 80506;
    public static final int TRAINSKILL_80507 = 80507;
    public static final int TRAINSKILL_80508 = 80508;
    public static final int TRAINSKILL_80509 = 80509;
    public static final int TRAINSKILL_80510 = 80510;
    public static final int TRAINSKILL_80511 = 80511;
    public static final int TRAINSKILL_80512 = 80512;
    
    public static List<Integer> getRoundStartSkills() {
        return Arrays.asList(Integer.valueOf(80401), Integer.valueOf(80406), Integer.valueOf(80501), Integer.valueOf(80505), Integer.valueOf(80507), Integer.valueOf(80511));
    }
    
    public static List<Integer> getBeforeMagicAttaSkills() {
        return Arrays.asList(Integer.valueOf(80403), Integer.valueOf(80506));
    }
    
    public static List<Integer> getChangeHurtEffectorSkills() {
        return Arrays.asList(Integer.valueOf(80402), Integer.valueOf(80502));
    }
    
    public static List<Integer> getChangeHurtTargetSkills() {
        return Arrays.asList(Integer.valueOf(80503));
    }
    
    public static List<Integer> getchageHurtMPSkills() {
        return Arrays.asList(Integer.valueOf(80504));
    }
    
    public static List<Integer> getAddStatusSkills() {
        return Arrays.asList(Integer.valueOf(80404), Integer.valueOf(80406));
    }
    
    public static List<Integer> getReduceLifeEndEffectorSkills() {
        return Arrays.asList(Integer.valueOf(80501));
    }
    
    public static List<Integer> getReduceLifeEndTargetSkills() {
        return Arrays.asList(Integer.valueOf(80405), Integer.valueOf(80406), Integer.valueOf(80511));
    }
    
    public static List<Integer> getMagicAttackEndSkills() {
        return Arrays.asList(Integer.valueOf(80507), Integer.valueOf(80508), Integer.valueOf(80509));
    }
    
    public static List<Integer> getMagicAttackEndOneSkills() {
        return Arrays.asList(Integer.valueOf(80512));
    }
    
    public static List<Integer> getNormalAttackAfterSkills() {
        return Arrays.asList(Integer.valueOf(80510));
    }
}
