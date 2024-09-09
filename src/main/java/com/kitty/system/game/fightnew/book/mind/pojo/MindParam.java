package com.kitty.system.game.fightnew.book.mind.pojo;

import com.kitty.system.enums.war.itempro.mind.MIND;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class MindParam implements Serializable
{
    public static Map<Integer, Map<Integer, MindParam>> mindParamMap;
    private int basic;
    private int percent;
    
    public int getBasic() {
        return this.basic;
    }
    
    public int getPercent() {
        return this.percent;
    }
    
    public void setBasic(final int basic) {
        this.basic = basic;
    }
    
    public void setPercent(final int percent) {
        this.percent = percent;
    }
    
    public MindParam() {
    }
    
    public MindParam(final int basic, final int percent) {
        this.basic = basic;
        this.percent = percent;
    }
    
    static {
        MindParam.mindParamMap = new HashMap();
        Map<Integer, MindParam> csMap = new HashMap();
        csMap.put(Integer.valueOf(MIND.XS_WL.getDist()), new MindParam(3000, 60));
        csMap.put(Integer.valueOf(MIND.XS_F.getDist()), new MindParam(2500, 50));
        csMap.put(Integer.valueOf(MIND.XS_L.getDist()), new MindParam(2500, 50));
        csMap.put(Integer.valueOf(MIND.XS_S.getDist()), new MindParam(2500, 50));
        csMap.put(Integer.valueOf(MIND.XS_H.getDist()), new MindParam(2500, 50));
        csMap.put(Integer.valueOf(MIND.XS_GH.getDist()), new MindParam(2500, 50));
        csMap.put(Integer.valueOf(MIND.XS_SS.getDist()), new MindParam(1000, 20));
        csMap.put(Integer.valueOf(MIND.XS_DU.getDist()), new MindParam(875, 20));
        csMap.put(Integer.valueOf(MIND.SS_HX_XG.getDist()), new MindParam(3000, 0));
        MindParam.mindParamMap.put(Integer.valueOf(1), csMap);
        Map<Integer, MindParam> slMap = new HashMap();
        slMap.put(Integer.valueOf(MIND.XS_WL.getDist()), new MindParam(3000, 60));
        slMap.put(Integer.valueOf(MIND.XS_F.getDist()), new MindParam(2500, 50));
        slMap.put(Integer.valueOf(MIND.XS_L.getDist()), new MindParam(2500, 50));
        slMap.put(Integer.valueOf(MIND.XS_S.getDist()), new MindParam(2500, 50));
        slMap.put(Integer.valueOf(MIND.XS_H.getDist()), new MindParam(2500, 50));
        slMap.put(Integer.valueOf(MIND.XS_GH.getDist()), new MindParam(2500, 50));
        slMap.put(Integer.valueOf(MIND.XS_SS.getDist()), new MindParam(1000, 20));
        slMap.put(Integer.valueOf(MIND.XS_DU.getDist()), new MindParam(875, 20));
        slMap.put(Integer.valueOf(MIND.SS_HX_XG.getDist()), new MindParam(3000, 0));
        MindParam.mindParamMap.put(Integer.valueOf(2), slMap);
        Map<Integer, MindParam> dfMap = new HashMap();
        dfMap.put(Integer.valueOf(MIND.XS_WL.getDist()), new MindParam(1560, 2400));
        dfMap.put(Integer.valueOf(MIND.XS_F.getDist()), new MindParam(1300, 2000));
        dfMap.put(Integer.valueOf(MIND.XS_L.getDist()), new MindParam(1300, 2000));
        dfMap.put(Integer.valueOf(MIND.XS_S.getDist()), new MindParam(1300, 2000));
        dfMap.put(Integer.valueOf(MIND.XS_H.getDist()), new MindParam(1300, 2000));
        dfMap.put(Integer.valueOf(MIND.XS_GH.getDist()), new MindParam(1300, 2000));
        dfMap.put(Integer.valueOf(MIND.XS_SS.getDist()), new MindParam(520, 800));
        dfMap.put(Integer.valueOf(MIND.XS_DU.getDist()), new MindParam(455, 700));
        dfMap.put(Integer.valueOf(MIND.SS_HX_XG.getDist()), new MindParam(3000, 0));
        MindParam.mindParamMap.put(Integer.valueOf(2), dfMap);
        Map<Integer, MindParam> zjMap = new HashMap();
        zjMap.put(Integer.valueOf(MIND.KB_WL.getDist()), new MindParam(0, 18));
        zjMap.put(Integer.valueOf(MIND.KB_F.getDist()), new MindParam(0, 12));
        zjMap.put(Integer.valueOf(MIND.KB_L.getDist()), new MindParam(0, 12));
        zjMap.put(Integer.valueOf(MIND.KB_S.getDist()), new MindParam(0, 12));
        zjMap.put(Integer.valueOf(MIND.KB_H.getDist()), new MindParam(0, 12));
        zjMap.put(Integer.valueOf(MIND.KB_SS.getDist()), new MindParam(0, 12));
        MindParam.mindParamMap.put(Integer.valueOf(2), zjMap);
    }
}
