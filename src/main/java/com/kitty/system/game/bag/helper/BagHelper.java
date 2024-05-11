
package com.kitty.system.game.bag.helper;

import com.kitty.common.utils.spring.SpringUtils;
import java.util.HashMap;
import java.util.Map;

public enum BagHelper
{
    INSTANCE;
    
    private static final Map<Integer, BagHandler> handlers;
    
    public void init(final Integer type, final BagHandler bagHandler) {
        BagHelper.handlers.put(type, bagHandler);
    }
    
    public BagHandler getHandler(final Integer type) {
        return BagHelper.handlers.get(type);
    }
    
    static {
        handlers = new HashMap<Integer, BagHandler>();
        BagHelper.INSTANCE.init(1, SpringUtils.getBean("GemFit", GemFitHandler.class));
        BagHelper.INSTANCE.init(2, SpringUtils.getBean("GemDetach", GemDetachHandler.class));
        BagHelper.INSTANCE.init(3, SpringUtils.getBean("GemCurdle", GemCurdleHandler.class));
        BagHelper.INSTANCE.init(4, SpringUtils.getBean("SkillBook", SkillBookHandler.class));
        BagHelper.INSTANCE.init(5, SpringUtils.getBean("FiveElements", FiveElementsHandler.class));
        BagHelper.INSTANCE.init(6, SpringUtils.getBean("SketchBook", SketchBookHandler.class));
        BagHelper.INSTANCE.init(7, SpringUtils.getBean("Potholing", PotholingHandler.class));
        BagHelper.INSTANCE.init(8, SpringUtils.getBean("CardCurdle", CardCurdleHandler.class));
        BagHelper.INSTANCE.init(9, SpringUtils.getBean("CardDetach", CardDetachHandler.class));
        BagHelper.INSTANCE.init(100, SpringUtils.getBean("SplitItem", SplitItemHandler.class));
        BagHelper.INSTANCE.init(10001, SpringUtils.getBean("Nwcprophecy", NwcprophecyHandler.class));
        BagHelper.INSTANCE.init(10, SpringUtils.getBean("TreasureStoneChange", TreasureStoneChangeHandler.class));
        BagHelper.INSTANCE.init(11, SpringUtils.getBean("TreasureStoneCompose", TreasureStoneComposeHandler.class));
        BagHelper.INSTANCE.init(13, SpringUtils.getBean("RarePotholing", RarePotholingHandler.class));
        BagHelper.INSTANCE.init(14, SpringUtils.getBean("ZhenLuComposeInfo", ZhenLuComposeInfoHandler.class));
        BagHelper.INSTANCE.init(15, SpringUtils.getBean("ZhenLuCase", ZhenLuCaseHandler.class));
    }
}
