
package com.kitty.system.game.fightnew.book.awaken;

import java.util.Arrays;
import java.util.List;

public interface AwakenSkillConst
{
    public static final int AWAKENSKILL_31303 = 31303;
    public static final int AWAKENSKILL_31305 = 31305;
    public static final int AWAKENSKILL_31307 = 31307;
    public static final int AWAKENSKILL_31310 = 31310;
    public static final int AWAKENSKILL_31313 = 31313;
    public static final int AWAKENSKILL_31315 = 31315;
    public static final int AWAKENSKILL_31316 = 31316;
    public static final int AWAKENSKILL_31317 = 31317;
    public static final int AWAKENSKILL_31318 = 31318;
    public static final int AWAKENSKILL_31319 = 31319;
    public static final int AWAKENSKILL_31320 = 31320;
    public static final int AWAKENSKILL_31323 = 31323;
    public static final int AWAKENSKILL_31324 = 31324;
    public static final int AWAKENSKILL_31325 = 31325;
    public static final int AWAKENSKILL_31327 = 31327;
    public static final int AWAKENSKILL_31418 = 31418;
    public static final int AWAKENSKILL_31421 = 31421;
    public static final int AWAKENSKILL_31424 = 31424;
    public static final int AWAKENSKILL_31301 = 31301;
    public static final int AWAKENSKILL_31302 = 31302;
    public static final int AWAKENSKILL_31306 = 31306;
    public static final int AWAKENSKILL_31321 = 31321;
    public static final int AWAKENSKILL_31322 = 31322;
    public static final int AWAKENSKILL_31405 = 31405;
    public static final int AWAKENSKILL_31517 = 31517;
    public static final int AWAKENSKILL_31304 = 31304;
    public static final int AWAKENSKILL_31308 = 31308;
    public static final int AWAKENSKILL_31309 = 31309;
    public static final int AWAKENSKILL_31314 = 31314;
    public static final int AWAKENSKILL_31403 = 31403;
    public static final int AWAKENSKILL_31407 = 31407;
    public static final int AWAKENSKILL_31410 = 31410;
    public static final int AWAKENSKILL_31411 = 31411;
    public static final int AWAKENSKILL_31413 = 31413;
    public static final int AWAKENSKILL_31415 = 31415;
    public static final int AWAKENSKILL_31417 = 31417;
    public static final int AWAKENSKILL_31419 = 31419;
    public static final int AWAKENSKILL_31422 = 31422;
    public static final int AWAKENSKILL_31426 = 31426;
    public static final int AWAKENSKILL_31427 = 31427;
    public static final int AWAKENSKILL_31501 = 31501;
    public static final int AWAKENSKILL_31502 = 31502;
    public static final int AWAKENSKILL_31504 = 31504;
    public static final int AWAKENSKILL_31506 = 31506;
    public static final int AWAKENSKILL_31508 = 51508;
    public static final int AWAKENSKILL_31510 = 31510;
    public static final int AWAKENSKILL_31513 = 31513;
    public static final int AWAKENSKILL_31514 = 31514;
    public static final int AWAKENSKILL_31515 = 31515;
    public static final int AWAKENSKILL_31311 = 31311;
    public static final int AWAKENSKILL_31312 = 31312;
    public static final int AWAKENSKILL_31401 = 31401;
    public static final int AWAKENSKILL_31402 = 31402;
    public static final int AWAKENSKILL_31404 = 31404;
    public static final int AWAKENSKILL_31406 = 31406;
    public static final int AWAKENSKILL_31408 = 31408;
    public static final int AWAKENSKILL_31412 = 31412;
    public static final int AWAKENSKILL_31414 = 31414;
    public static final int AWAKENSKILL_31416 = 31416;
    public static final int AWAKENSKILL_31503 = 31503;
    public static final int AWAKENSKILL_31505 = 31505;
    public static final int AWAKENSKILL_31507 = 31507;
    public static final int AWAKENSKILL_31509 = 31509;
    public static final int AWAKENSKILL_31511 = 31511;
    public static final int AWAKENSKILL_31512 = 51512;
    public static final int AWAKENSKILL_31516 = 31516;
    
    static List<Integer> getAdditionSkills() {
        return Arrays.asList(31303, 31305, 31307, 31310, 31313, 31315, 31316, 31317, 31318, 31319, 31320, 31323, 31324, 31325, 31327, 31418, 31421, 31424, 31509, 31514);
    }
    
    static List<Integer> getRoundStartSkills() {
        return Arrays.asList(31301, 31302, 31306, 31321, 31322, 31405, 31517);
    }
    
    static List<Integer> getMagicAttackEndSkills() {
        return Arrays.asList(31308, 31413, 31415, 31515);
    }
    
    static List<Integer> getMagicAttackEndOtherSkills() {
        return Arrays.asList(31406, 31514);
    }
    
    static List<Integer> getBeforePhysicalAttackSkills() {
        return Arrays.asList(31422, 31502);
    }
    
    static List<Integer> getPhyAttackEndSkills() {
        return Arrays.asList(31312, 31419, 31504, 31513);
    }
    
    static List<Integer> getBeforeDoActionSkills() {
        return Arrays.asList(31510);
    }
    
    static List<Integer> getReviveSkills() {
        return Arrays.asList(31401);
    }
    
    static List<Integer> getIsGuardSkills() {
        return Arrays.asList(31402);
    }
    
    static List<Integer> getCountRecoverBloodEndSkills() {
        return Arrays.asList(31404);
    }
    
    static List<Integer> getEnteringSideSkills() {
        return Arrays.asList(31408, 31416, 31509, 31511, 31516);
    }
    
    static List<Integer> getSufferHarmSkills() {
        return Arrays.asList(31412, 31505, 31503);
    }
    
    static List<Integer> getEnterFightSkills() {
        return Arrays.asList(31414, 31507);
    }
    
    static List<Integer> getExitSideSkills() {
        return Arrays.asList(31507);
    }
    
    static List<Integer> getSkillIntensifyTMSkills() {
        return Arrays.asList(31407, 31426);
    }
    
    default List<Integer> getStatusOverSkills() {
        return Arrays.asList(31514);
    }
    
    static List<Integer> getChangeHurtSkills() {
        return Arrays.asList(51512);
    }
    
    static List<Integer> getUseDrugSkills() {
        return Arrays.asList(31311);
    }
}
