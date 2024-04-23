package com.kitty.system.game.fightnew.book.acsry.def;

public enum ACSRY_SKILL_TRIGGER_DICT {
  BEFORE_ROUND_START("回合开始时", "beforeRoundStart"),
  BEFORE_ROUND_START_TARGET("回合开始时", "beforeRoundStartTarget"),
  ROUND_END("回合结束时", "roundEnd"),
  MAGIC_SELECT_TARGET("法术选择目标时", "magicSelectTarget"),
  ATTACK_END("攻击完毕后", "attackEnd"),
  COUNT_RECOVER_BLOOD_END("计算完回血量后", "countRecoverBloodEnd"),
  COUNT_RECOVER_BLOOD_END_SS("计算完回血量后(三尸)", "countRecoverBloodEndSS"),
  COUNT_RECOVER_MP_END("计算完回法量后", "countRecoverMpEnd"),
  REDUCE_MP("扣除法力时", "reduceMp"),
  POISON_REDUCE_HP("毒法伤害时", "poisonReduceHp"),
  STATUS_OVER("状态结束时", "statusOver"),
  WEAR_TAKE_EFFECT("穿戴即生效", "wearTakeEffect"),
  BEFORE_EXECUTE_MAGIC("执行法术前", "beforeExecuteMagic"),
  EXECUTE_REDUCE_HP("执行扣血前", "executeReduceHp"),
  BEFORE_REDUCE_HURT_MP("执行伤害减法前", "beforeReduceHurtMp"),
  AFTER_INPUT_INSTRUCT("输入指令后", "afterInputInstruct"),
  BEFORE_PHYSICAL_ATTACK("物理攻击执行前", "beforePhysicalAttack"),
  INITIALIZE_FIGHT_OBJECT("初始化战斗对象时", "initializeFightObject");
  
  ACSRY_SKILL_TRIGGER_DICT(String name, String value) {
    this.name = name;
    this.value = value;
  }
  
  private String name;
  
  private String value;
  
  public String getName() {
    return this.name;
  }
  
  public String getValue() {
    return this.value;
  }
}
