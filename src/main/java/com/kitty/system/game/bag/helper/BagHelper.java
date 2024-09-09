package com.kitty.system.game.bag.helper;

import com.kitty.common.utils.spring.SpringUtils;
import java.util.HashMap;
import java.util.Map;

public enum BagHelper
{
    INSTANCE;
    
    private static final Map<Integer, BagHandler> handlers;
    
    public void init(Integer type, BagHandler bagHandler) {
        BagHelper.handlers.put(type, bagHandler);
    }
    
    public BagHandler getHandler(Integer type) {
        return (BagHandler)BagHelper.handlers.get(type);
    }
    
    static {
        handlers = new HashMap();
        BagHelper.INSTANCE.init(Integer.valueOf(1), (BagHandler)SpringUtils.getBean("GemFit", GemFitHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(2), (BagHandler)SpringUtils.getBean("GemDetach", GemDetachHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(3), (BagHandler)SpringUtils.getBean("GemCurdle", GemCurdleHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(4), (BagHandler)SpringUtils.getBean("SkillBook", SkillBookHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(5), (BagHandler)SpringUtils.getBean("FiveElements", FiveElementsHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(6), (BagHandler)SpringUtils.getBean("SketchBook", SketchBookHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(7), (BagHandler)SpringUtils.getBean("Potholing", PotholingHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(8), (BagHandler)SpringUtils.getBean("CardCurdle", CardCurdleHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(9), (BagHandler)SpringUtils.getBean("CardDetach", CardDetachHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(100), (BagHandler)SpringUtils.getBean("SplitItem", SplitItemHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(10001), (BagHandler)SpringUtils.getBean("Nwcprophecy", NwcprophecyHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(10), (BagHandler)SpringUtils.getBean("TreasureStoneChange", TreasureStoneChangeHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(11), (BagHandler)SpringUtils.getBean("TreasureStoneCompose", TreasureStoneComposeHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(13), (BagHandler)SpringUtils.getBean("RarePotholing", RarePotholingHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(14), (BagHandler)SpringUtils.getBean("ZhenLuComposeInfo", ZhenLuComposeInfoHandler.class));
        BagHelper.INSTANCE.init(Integer.valueOf(15), (BagHandler)SpringUtils.getBean("ZhenLuCase", ZhenLuCaseHandler.class));
    }
}
