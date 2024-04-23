package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.spring.SpringUtils;
import java.util.HashMap;
import java.util.Map;

public enum UpgradeHelper {
  INSTANCE;
  
  private static final Map<Integer, UpgradeHandler> handlers;
  
  static {
    handlers = new HashMap<>();
    INSTANCE.init(Integer.valueOf(11), (UpgradeHandler)SpringUtils.getBean("EquipBuy", EquipBuyHandler.class));
    INSTANCE.init(Integer.valueOf(1), (UpgradeHandler)SpringUtils.getBean("EquipCompose", EquipComposeHandler.class));
    INSTANCE.init(Integer.valueOf(5), (UpgradeHandler)SpringUtils.getBean("AcsryAuth", AcsryAuthHandler.class));
    INSTANCE.init(Integer.valueOf(2), (UpgradeHandler)SpringUtils.getBean("EquipSeniorRemold", EquipSeniorRemoldHandler.class));
    INSTANCE.init(Integer.valueOf(3), (UpgradeHandler)SpringUtils.getBean("EquipSinRemold", EquipSinRemoldHandler.class));
    INSTANCE.init(Integer.valueOf(6), (UpgradeHandler)SpringUtils.getBean("EquipUpgrade", EquipUpgradeHandler.class));
    INSTANCE.init(Integer.valueOf(7), (UpgradeHandler)SpringUtils.getBean("EquipMagicalUpGrade", EquipMagicalUpGradeHandler.class));
    INSTANCE.init(Integer.valueOf(8), (UpgradeHandler)SpringUtils.getBean("EquipSinUpGrade", EquipSinUpGradeHandler.class));
    INSTANCE.init(Integer.valueOf(9), (UpgradeHandler)SpringUtils.getBean("EquipGem", EquipGemHandler.class));
    INSTANCE.init(Integer.valueOf(10), (UpgradeHandler)SpringUtils.getBean("EquipDisconnect", EquipDisconnectHandler.class));
    INSTANCE.init(Integer.valueOf(12), (UpgradeHandler)SpringUtils.getBean("EquipPreRemold", EquipPreRemoldHandler.class));
    INSTANCE.init(Integer.valueOf(14), (UpgradeHandler)SpringUtils.getBean("EquipPurify", EquipPurifyHandler.class));
    INSTANCE.init(Integer.valueOf(13), (UpgradeHandler)SpringUtils.getBean("EquipRemoldReplace", EquipRemoldReplaceHandler.class));
    INSTANCE.init(Integer.valueOf(4), (UpgradeHandler)SpringUtils.getBean("EquipRefine", EquipRefineHandler.class));
    INSTANCE.init(Integer.valueOf(15), (UpgradeHandler)SpringUtils.getBean("EquipPreRefine", EquipPreRefineHandler.class));
    INSTANCE.init(Integer.valueOf(16), (UpgradeHandler)SpringUtils.getBean("EquipRefineReplace", EquipRefineReplaceHandler.class));
    INSTANCE.init(Integer.valueOf(17), (UpgradeHandler)SpringUtils.getBean("EquipRepair", EquipRepairHandler.class));
    INSTANCE.init(Integer.valueOf(18), (UpgradeHandler)SpringUtils.getBean("AcsryRemold", AcsryRemoldHandler.class));
    INSTANCE.init(Integer.valueOf(19), (UpgradeHandler)SpringUtils.getBean("PetEquipCompose", PetEquipComposeHandler.class));
    INSTANCE.init(Integer.valueOf(20), (UpgradeHandler)SpringUtils.getBean("PetEquipDisconnect", PetEquipDisconnectHandler.class));
    INSTANCE.init(Integer.valueOf(21), (UpgradeHandler)SpringUtils.getBean("PetEquipDisplace", PetEquipDisplace.class));
  }
  
  public void init(Integer upgradeType, UpgradeHandler upgradeHandler) {
    handlers.put(upgradeType, upgradeHandler);
  }
  
  public UpgradeHandler getHandler(Integer upgradeType) {
    return handlers.get(upgradeType);
  }
}
