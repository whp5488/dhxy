package com.kitty.system.game.mind.helper;

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
        UpgradeHelper.INSTANCE.init(26, SpringUtils.getBean("MindLearn", MindLearnHandler.class));
        UpgradeHelper.INSTANCE.init(27, SpringUtils.getBean("MindPreRefine", MindPreRefineHandler.class));
        UpgradeHelper.INSTANCE.init(28, SpringUtils.getBean("MindRefine", MindRefineHandler.class));
        UpgradeHelper.INSTANCE.init(29, SpringUtils.getBean("MindRefineReplace", MindRefineReplaceHandler.class));
    }
}
