package com.kitty.system.game.train.helper;

import com.kitty.common.utils.spring.SpringUtils;
import java.util.HashMap;
import java.util.Map;

public enum UpgradeHelper
{
    INSTANCE;
    
    private static final Map<Integer, UpgradeHandler> handlers;
    
    public void init(Integer upgradeType, UpgradeHandler upgradeHandler) {
        UpgradeHelper.handlers.put(upgradeType, upgradeHandler);
    }
    
    public UpgradeHandler getHandler(Integer upgradeType) {
        return UpgradeHelper.handlers.get(upgradeType);
    }
    
    static {
        handlers = new HashMap<>();
        UpgradeHelper.INSTANCE.init(22, SpringUtils.getBean("TrainSealBuy", TrainSealBuyHandler.class));
        UpgradeHelper.INSTANCE.init(23, SpringUtils.getBean("TrainSealPreRefine", TrainSealPreRefineHandler.class));
        UpgradeHelper.INSTANCE.init(24, SpringUtils.getBean("TrainSealRefine", TrainSealRefineHandler.class));
        UpgradeHelper.INSTANCE.init(25, SpringUtils.getBean("TrainSealRefineReplace", TrainSealRefineReplaceHandler.class));
    }
}
