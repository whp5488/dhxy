
package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.spring.SpringUtils;
import java.util.HashMap;
import java.util.Map;

public enum UpgradeHelper
{
    INSTANCE;
    
    private static final Map<Integer, UpgradeHandler> handlers;
    
    public void init(final Integer upgradeType, final UpgradeHandler upgradeHandler) {
        UpgradeHelper.handlers.put(upgradeType, upgradeHandler);
    }
    
    public UpgradeHandler getHandler(final Integer upgradeType) {
        return UpgradeHelper.handlers.get(upgradeType);
    }
    
    static {
        handlers = new HashMap<Integer, UpgradeHandler>();
        UpgradeHelper.INSTANCE.init(11, SpringUtils.getBean("EquipBuy", EquipBuyHandler.class));
        UpgradeHelper.INSTANCE.init(1, SpringUtils.getBean("EquipCompose", EquipComposeHandler.class));
        UpgradeHelper.INSTANCE.init(5, SpringUtils.getBean("AcsryAuth", AcsryAuthHandler.class));
        UpgradeHelper.INSTANCE.init(2, SpringUtils.getBean("EquipSeniorRemold", EquipSeniorRemoldHandler.class));
        UpgradeHelper.INSTANCE.init(3, SpringUtils.getBean("EquipSinRemold", EquipSinRemoldHandler.class));
        UpgradeHelper.INSTANCE.init(6, SpringUtils.getBean("EquipUpgrade", EquipUpgradeHandler.class));
        UpgradeHelper.INSTANCE.init(7, SpringUtils.getBean("EquipMagicalUpGrade", EquipMagicalUpGradeHandler.class));
        UpgradeHelper.INSTANCE.init(8, SpringUtils.getBean("EquipSinUpGrade", EquipSinUpGradeHandler.class));
        UpgradeHelper.INSTANCE.init(9, SpringUtils.getBean("EquipGem", EquipGemHandler.class));
        UpgradeHelper.INSTANCE.init(10, SpringUtils.getBean("EquipDisconnect", EquipDisconnectHandler.class));
        UpgradeHelper.INSTANCE.init(12, SpringUtils.getBean("EquipPreRemold", EquipPreRemoldHandler.class));
        UpgradeHelper.INSTANCE.init(14, SpringUtils.getBean("EquipPurify", EquipPurifyHandler.class));
        UpgradeHelper.INSTANCE.init(13, SpringUtils.getBean("EquipRemoldReplace", EquipRemoldReplaceHandler.class));
        UpgradeHelper.INSTANCE.init(4, SpringUtils.getBean("EquipRefine", EquipRefineHandler.class));
        UpgradeHelper.INSTANCE.init(15, SpringUtils.getBean("EquipPreRefine", EquipPreRefineHandler.class));
        UpgradeHelper.INSTANCE.init(16, SpringUtils.getBean("EquipRefineReplace", EquipRefineReplaceHandler.class));
        UpgradeHelper.INSTANCE.init(17, SpringUtils.getBean("EquipRepair", EquipRepairHandler.class));
        UpgradeHelper.INSTANCE.init(18, SpringUtils.getBean("AcsryRemold", AcsryRemoldHandler.class));
        UpgradeHelper.INSTANCE.init(19, SpringUtils.getBean("PetEquipCompose", PetEquipComposeHandler.class));
        UpgradeHelper.INSTANCE.init(20, SpringUtils.getBean("PetEquipDisconnect", PetEquipDisconnectHandler.class));
        UpgradeHelper.INSTANCE.init(21, SpringUtils.getBean("PetEquipDisplace", PetEquipDisplace.class));
    }
}
