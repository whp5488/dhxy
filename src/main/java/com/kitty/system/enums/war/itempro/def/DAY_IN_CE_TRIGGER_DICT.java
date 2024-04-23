package com.kitty.system.enums.war.itempro.def;

public enum DAY_IN_CE_TRIGGER_DICT {
  ENTERING_SIDE("进场时", "enteringSide"),
  ROUND_START("回合开始时", "roundStart"),
  SUBTRACT_MP("扣除法量前", "subtractMp"),
  CALCULATE_HURT("计算伤害时", "calculateHurt"),
  STATUS_REDUCE("状态减减时", "statusReduce"),
  USE_DRUG("使用药品时", "useDrug"),
  MAGIC_MISS("法术未命中", "magicMiss"),
  BEFORE_DO_ACTION("出手动作前", "beforeDoAction"),
  BEFORE_DO_MAGIC("释放法术前", "beforeDoMagic"),
  BEFORE_COUNT_HURT("计算伤害前执行", "beforeCountHurt"),
  BEFORE_SKILL_EFFECT("执行技能effect前", "beforeSkillEffect"),
  TARGET_HITS("命中目标时", "targetHits"),
  BE_HIT_BY("被命中时", "beHitBy"),
  TARGET_HITS_MORE("命中目标时", "targetHitsMore"),
  ADD_EFFECT_ALL("增加效果(物攻，法术)", "addEffectAll"),
  ADD_EFFECT("增加效果", "addEffect"),
  ADD_MAGIC_EFFECT("增加法术效果", "addMagicEffect"),
  EXIT_OR_CLEAR_POSITIVE("离场或清除正面状态", "exitOrClearPositive"),
  PHYSICAL_ATTACK("物理攻击时", "physicalAttack"),
  BEFORE_PHYSICAL_ATTACK("物理攻击前", "beforePhysicalAttack"),
  WEAR_INSTANTLY_TAKE_EFFECT("佩戴即刻生效", "wearInstantlyTakeEffect");
  
  DAY_IN_CE_TRIGGER_DICT(String name, String value) {
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
