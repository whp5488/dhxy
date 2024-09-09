package com.kitty.system.init.pool;

import java.util.ArrayList;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.enums.war.itempro.def.PRACTICE_PET_PRO_DICT;
import java.util.HashMap;
import com.kitty.system.enums.war.itempro.def.ZB_PRO_BASE_DICT;
import com.kitty.system.enums.war.itempro.mind.MIND;
import java.util.Arrays;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.init.domain.vo.MindDefineRange;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.init.domain.vo.MindDefine;
import java.util.List;
import java.util.Map;

public class ConvertDataPool
{
    public static Map<Integer, Map<Integer, Integer>> defPetCorrectionMaxMap;
    public static Map<Integer, Double> defPetCorrectionMap;
    public static List<Integer> PRO_BASE_DICT;
    public static List<MindDefine> mindDefineList;
    public static Map<Integer, Integer[]> petSkillReplace;
    public static Map<Integer, Item> bagExtend;
    public static Map<Integer, Item> storeExtend;
    public static Map<Integer, Item> petBagEquipExtend;
    
    public ConvertDataPool() {
    }
    
    public static void load() {
        initProBaseDict();
        initDefPetCorrectionMaxMap();
        initDefPetCorrection();
        initPetSkillReplace();
        initBagExtend();
        initStoreExtend();
        initPetBagEquipExtend();
        initMindDefine();
        System.out.println("属性定义初始化完成");
    }
    
    public static void initMindDefine() {
        MindDefine csx = new MindDefine(1, 1000, Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.HP_CAP.getDist(), PRO_TYPE.Pro_Value_NUM_TYPE.getValue(), Arrays.asList(Double.valueOf(15000.0))), new MindDefineRange(PRO_DICT_TYPE.MP_CAP.getDist(), PRO_TYPE.Pro_Value_NUM_TYPE.getValue(), Arrays.asList(Double.valueOf(1500.0))), new MindDefineRange(MIND.XS_ZS_F.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0]))));
        csx.getMap().put(Integer.valueOf(14001), Arrays.asList(new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0]))));
        csx.getMap().put(Integer.valueOf(14011), Arrays.asList(new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0]))));
        csx.getMap().put(Integer.valueOf(14031), Arrays.asList(new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0]))));
        ConvertDataPool.mindDefineList.add(csx);
        ConvertDataPool.mindDefineList.add(new MindDefine(1, 1001, Arrays.asList(new MindDefineRange(MIND.XS_ZS_X.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_WL.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_H.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])))));
        ConvertDataPool.mindDefineList.add(new MindDefine(1, 1002, Arrays.asList(new MindDefineRange(MIND.XS_F.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_L.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_SS.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])))));
        ConvertDataPool.mindDefineList.add(new MindDefine(1, 1003, Arrays.asList(new MindDefineRange(MIND.XS_S.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_GH.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_DU.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])))));
        MindDefine slx = new MindDefine(2, 2000, Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.HP_CAP.getDist(), PRO_TYPE.Pro_Value_NUM_TYPE.getValue(), Arrays.asList(Double.valueOf(25000.0))), new MindDefineRange(PRO_DICT_TYPE.MP_CAP.getDist(), PRO_TYPE.Pro_Value_NUM_TYPE.getValue(), Arrays.asList(Double.valueOf(1500.0))), new MindDefineRange(MIND.XS_ZS_F.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0]))));
        slx.getMap().put(Integer.valueOf(14001), Arrays.asList(new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0]))));
        slx.getMap().put(Integer.valueOf(14011), Arrays.asList(new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0]))));
        slx.getMap().put(Integer.valueOf(14031), Arrays.asList(new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0]))));
        ConvertDataPool.mindDefineList.add(slx);
        ConvertDataPool.mindDefineList.add(new MindDefine(2, 2001, Arrays.asList(new MindDefineRange(MIND.K_L.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(MIND.K_DU.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(MIND.XS_WL_XF.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(2, 2002, Arrays.asList(new MindDefineRange(MIND.K_S.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(MIND.K_GH.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(PRO_DICT_TYPE.K_FY.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(6.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(2, 2003, Arrays.asList(new MindDefineRange(MIND.K_F.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(MIND.K_SS.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(PRO_DICT_TYPE.K_HL.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(6.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(2, 2004, Arrays.asList(new MindDefineRange(MIND.K_H.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(PRO_DICT_TYPE.K_HS.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(6.0))), new MindDefineRange(PRO_DICT_TYPE.K_YW.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(Double.valueOf(6.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(2, 2005, Arrays.asList(new MindDefineRange(MIND.XS_ZS_X.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_WL.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_F.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])))));
        ConvertDataPool.mindDefineList.add(new MindDefine(2, 2006, Arrays.asList(new MindDefineRange(MIND.XS_H.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_S.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_SS.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])))));
        ConvertDataPool.mindDefineList.add(new MindDefine(2, 2007, Arrays.asList(new MindDefineRange(MIND.XS_L.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_GH.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_DU.getDist(), PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue(), Arrays.asList(new Double[0])))));
        MindDefine dfx = new MindDefine(3, 3000, Arrays.asList(new MindDefineRange[0]));
        dfx.getMap().put(Integer.valueOf(11001), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HL.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        dfx.getMap().put(Integer.valueOf(11011), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HL.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        dfx.getMap().put(Integer.valueOf(11031), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HL.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        dfx.getMap().put(Integer.valueOf(11006), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_D.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        dfx.getMap().put(Integer.valueOf(11016), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_D.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        dfx.getMap().put(Integer.valueOf(11036), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_D.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        dfx.getMap().put(Integer.valueOf(12001), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_F.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(12011), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_F.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(12031), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_F.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(12006), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_H.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(12016), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_H.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(12036), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_H.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(13001), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_SPEED.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(13011), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_SPEED.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(13031), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_SPEED.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(13006), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_DEF.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(13016), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_DEF.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(13036), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_DEF.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(14001), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_SS.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_NUM_TYPE.getValue(), Arrays.asList(Double.valueOf(30000.0)))));
        dfx.getMap().put(Integer.valueOf(14011), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_SS.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_NUM_TYPE.getValue(), Arrays.asList(Double.valueOf(30000.0)))));
        dfx.getMap().put(Integer.valueOf(14031), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_SS.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_NUM_TYPE.getValue(), Arrays.asList(Double.valueOf(30000.0)))));
        dfx.getMap().put(Integer.valueOf(14003), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_MH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        dfx.getMap().put(Integer.valueOf(14013), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_MH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        dfx.getMap().put(Integer.valueOf(14033), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_MH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        dfx.getMap().put(Integer.valueOf(15001), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(15011), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(15031), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(15006), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(15016), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        dfx.getMap().put(Integer.valueOf(15036), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        ConvertDataPool.mindDefineList.add(dfx);
        ConvertDataPool.mindDefineList.add(new MindDefine(3, 3001, Arrays.asList(new MindDefineRange(MIND.K_S.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(MIND.K_DU.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(MIND.XS_WL_XF.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(3, 3002, Arrays.asList(new MindDefineRange(MIND.K_L.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(MIND.K_GH.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(PRO_DICT_TYPE.K_FY_LMT.getDist(), Arrays.asList(Double.valueOf(6.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(3, 3003, Arrays.asList(new MindDefineRange(MIND.K_F.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(MIND.K_SS.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(PRO_DICT_TYPE.K_HL_LMT.getDist(), Arrays.asList(Double.valueOf(6.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(3, 3004, Arrays.asList(new MindDefineRange(MIND.K_H.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(13.0))), new MindDefineRange(PRO_DICT_TYPE.K_HS_LMT.getDist(), Arrays.asList(Double.valueOf(6.0))), new MindDefineRange(PRO_DICT_TYPE.K_YW_LMT.getDist(), Arrays.asList(Double.valueOf(6.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(3, 3005, Arrays.asList(new MindDefineRange(MIND.XS_ZS_X.getDist(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_WL.getDist(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_S.getDist(), Arrays.asList(new Double[0])))));
        ConvertDataPool.mindDefineList.add(new MindDefine(3, 3006, Arrays.asList(new MindDefineRange(MIND.XS_H.getDist(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_L.getDist(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_SS.getDist(), Arrays.asList(new Double[0])))));
        ConvertDataPool.mindDefineList.add(new MindDefine(3, 3007, Arrays.asList(new MindDefineRange(MIND.XS_F.getDist(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_GH.getDist(), Arrays.asList(new Double[0])), new MindDefineRange(MIND.XS_DU.getDist(), Arrays.asList(new Double[0])))));
        MindDefine zjx = new MindDefine(4, 4000, Arrays.asList(new MindDefineRange[0]));
        zjx.getMap().put(Integer.valueOf(11001), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HL.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        zjx.getMap().put(Integer.valueOf(11011), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HL.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        zjx.getMap().put(Integer.valueOf(11031), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HL.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        zjx.getMap().put(Integer.valueOf(11006), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_D.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        zjx.getMap().put(Integer.valueOf(11016), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_D.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        zjx.getMap().put(Integer.valueOf(11036), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_D.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_FY.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_HS.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        zjx.getMap().put(Integer.valueOf(12001), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_F.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(12011), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_F.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(12031), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_F.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(12006), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_H.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(12016), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_H.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(12036), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_H.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_L.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_S.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(13001), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_SPEED.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(13011), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_SPEED.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(13031), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_SPEED.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(13006), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_DEF.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(13016), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_DEF.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(13036), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_DEF.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_ZS.getDist(), Arrays.asList(Double.valueOf(1.5))), new MindDefineRange(PRO_DICT_TYPE.RISE_ATTACK.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(14001), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_SS.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_NUM_TYPE.getValue(), Arrays.asList(Double.valueOf(30000.0)))));
        zjx.getMap().put(Integer.valueOf(14011), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_SS.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_NUM_TYPE.getValue(), Arrays.asList(Double.valueOf(30000.0)))));
        zjx.getMap().put(Integer.valueOf(14031), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.OMIT_K_SS.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0))), new MindDefineRange(MIND.SS_HX_XG.getDist(), PRO_TYPE.Pro_Value_NUM_TYPE.getValue(), Arrays.asList(Double.valueOf(30000.0)))));
        zjx.getMap().put(Integer.valueOf(14003), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_MH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        zjx.getMap().put(Integer.valueOf(14013), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_MH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        zjx.getMap().put(Integer.valueOf(14033), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.RISE_MH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_GH.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.OMIT_K_YW.getDist(), Arrays.asList(Double.valueOf(3.0)))));
        zjx.getMap().put(Integer.valueOf(15001), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(15011), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(15031), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(15006), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(15016), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        zjx.getMap().put(Integer.valueOf(15036), Arrays.asList(new MindDefineRange(PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.HIT_RATIO.getDist(), Arrays.asList(Double.valueOf(7.0))), new MindDefineRange(PRO_DICT_TYPE.RISE_ZY.getDist(), Arrays.asList(Double.valueOf(7.0)))));
        ConvertDataPool.mindDefineList.add(zjx);
        ConvertDataPool.mindDefineList.add(new MindDefine(4, 4001, Arrays.asList(new MindDefineRange(MIND.K_L.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(26.0))), new MindDefineRange(PRO_DICT_TYPE.K_TM.getDist(), Arrays.asList(Double.valueOf(10.0))), new MindDefineRange(PRO_DICT_TYPE.K_XL.getDist(), Arrays.asList(Double.valueOf(10.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(4, 4002, Arrays.asList(new MindDefineRange(MIND.K_S.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(26.0))), new MindDefineRange(PRO_DICT_TYPE.K_QM.getDist(), Arrays.asList(Double.valueOf(5.0))), new MindDefineRange(PRO_DICT_TYPE.K_FG.getDist(), Arrays.asList(Double.valueOf(10.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(4, 4003, Arrays.asList(new MindDefineRange(MIND.K_F.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(26.0))), new MindDefineRange(PRO_DICT_TYPE.K_FG.getDist(), Arrays.asList(Double.valueOf(10.0))), new MindDefineRange(PRO_DICT_TYPE.K_FG.getDist(), Arrays.asList(Double.valueOf(12.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(4, 4004, Arrays.asList(new MindDefineRange(MIND.K_H.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(26.0))), new MindDefineRange(PRO_DICT_TYPE.K_XL.getDist(), Arrays.asList(Double.valueOf(10.0))), new MindDefineRange(MIND.KB_SS.getDist(), Arrays.asList(Double.valueOf(12.0), Double.valueOf(12.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(4, 4005, Arrays.asList(new MindDefineRange(MIND.K_GH.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(26.0))), new MindDefineRange(PRO_DICT_TYPE.K_TM.getDist(), Arrays.asList(Double.valueOf(5.0))), new MindDefineRange(MIND.KB_L.getDist(), Arrays.asList(Double.valueOf(12.0), Double.valueOf(12.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(4, 4006, Arrays.asList(new MindDefineRange(MIND.K_SS.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(26.0))), new MindDefineRange(PRO_DICT_TYPE.K_QM.getDist(), Arrays.asList(Double.valueOf(5.0))), new MindDefineRange(MIND.KB_S.getDist(), Arrays.asList(Double.valueOf(12.0), Double.valueOf(12.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(4, 4007, Arrays.asList(new MindDefineRange(MIND.XS_WL_XF.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(26.0))), new MindDefineRange(PRO_DICT_TYPE.K_FG.getDist(), Arrays.asList(Double.valueOf(10.0))), new MindDefineRange(MIND.KB_F.getDist(), Arrays.asList(Double.valueOf(12.0), Double.valueOf(12.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(4, 4008, Arrays.asList(new MindDefineRange(MIND.K_DU.getDist(), Arrays.asList(Double.valueOf(2.0), Double.valueOf(26.0))), new MindDefineRange(PRO_DICT_TYPE.K_XL.getDist(), Arrays.asList(Double.valueOf(10.0))), new MindDefineRange(MIND.KB_H.getDist(), Arrays.asList(Double.valueOf(12.0), Double.valueOf(12.0))))));
        ConvertDataPool.mindDefineList.add(new MindDefine(4, 4009, Arrays.asList(new MindDefineRange(MIND.KB_WL.getDist(), Arrays.asList(Double.valueOf(18.0), Double.valueOf(18.0))), new MindDefineRange(PRO_DICT_TYPE.K_TM.getDist(), Arrays.asList(Double.valueOf(5.0))), new MindDefineRange(PRO_DICT_TYPE.K_QM.getDist(), Arrays.asList(Double.valueOf(5.0))))));
    }
    
    public static void initProBaseDict() {
        List<Integer> list = ZB_PRO_BASE_DICT.toList();
        ConvertDataPool.PRO_BASE_DICT.addAll(list);
    }
    
    public static void initDefPetCorrectionMaxMap() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_FY.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HL.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HS.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_YW.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_F.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_L.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_S.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_H.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_GH.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_SS.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_DU.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.WL_XS.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()), Integer.valueOf(3));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro()), Integer.valueOf(10));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro()), Integer.valueOf(10));
        ConvertDataPool.defPetCorrectionMaxMap.put(Integer.valueOf(1), map);
        map = new HashMap();
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_FY.getPro()), Integer.valueOf(20));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HL.getPro()), Integer.valueOf(20));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HS.getPro()), Integer.valueOf(20));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_YW.getPro()), Integer.valueOf(20));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_F.getPro()), Integer.valueOf(12));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_L.getPro()), Integer.valueOf(12));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_S.getPro()), Integer.valueOf(12));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_H.getPro()), Integer.valueOf(12));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_GH.getPro()), Integer.valueOf(12));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_SS.getPro()), Integer.valueOf(12));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_DU.getPro()), Integer.valueOf(12));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.WL_XS.getPro()), Integer.valueOf(15));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro()), Integer.valueOf(15));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro()), Integer.valueOf(15));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()), Integer.valueOf(3));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro()), Integer.valueOf(15));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro()), Integer.valueOf(15));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro()), Integer.valueOf(15));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro()), Integer.valueOf(15));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro()), Integer.valueOf(13));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro()), Integer.valueOf(13));
        ConvertDataPool.defPetCorrectionMaxMap.put(Integer.valueOf(2), map);
        map = new HashMap();
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_FY.getPro()), Integer.valueOf(24));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HL.getPro()), Integer.valueOf(24));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HS.getPro()), Integer.valueOf(24));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_YW.getPro()), Integer.valueOf(24));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_F.getPro()), Integer.valueOf(14));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_L.getPro()), Integer.valueOf(14));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_S.getPro()), Integer.valueOf(14));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_H.getPro()), Integer.valueOf(14));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_GH.getPro()), Integer.valueOf(14));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_SS.getPro()), Integer.valueOf(14));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_DU.getPro()), Integer.valueOf(14));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.WL_XS.getPro()), Integer.valueOf(20));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro()), Integer.valueOf(20));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro()), Integer.valueOf(20));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()), Integer.valueOf(3));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro()), Integer.valueOf(20));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro()), Integer.valueOf(20));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro()), Integer.valueOf(20));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro()), Integer.valueOf(20));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro()), Integer.valueOf(16));
        ConvertDataPool.defPetCorrectionMaxMap.put(Integer.valueOf(3), map);
        map = new HashMap();
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_FY.getPro()), Integer.valueOf(26));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HL.getPro()), Integer.valueOf(26));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HS.getPro()), Integer.valueOf(26));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_YW.getPro()), Integer.valueOf(26));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_F.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_L.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_S.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_H.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_GH.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_SS.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_DU.getPro()), Integer.valueOf(16));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.WL_XS.getPro()), Integer.valueOf(25));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro()), Integer.valueOf(25));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro()), Integer.valueOf(25));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()), Integer.valueOf(3));
        map.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro()), Integer.valueOf(25));
        map.put(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro(), Integer.valueOf(25));
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro(), Integer.valueOf(25));
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro(), Integer.valueOf(25));
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro(), Integer.valueOf(16));
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro(), Integer.valueOf(19));
        ConvertDataPool.defPetCorrectionMaxMap.put(4, map);
        map = new HashMap<>();
        map.put(PRACTICE_PET_PRO_DICT.K_FY.getPro(), 28);
        map.put(PRACTICE_PET_PRO_DICT.K_HL.getPro(), 28);
        map.put(PRACTICE_PET_PRO_DICT.K_HS.getPro(), 28);
        map.put(PRACTICE_PET_PRO_DICT.K_YW.getPro(), 28);
        map.put(PRACTICE_PET_PRO_DICT.K_F.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_L.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_S.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_H.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_GH.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_SS.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.K_DU.getPro(), 19);
        map.put(PRACTICE_PET_PRO_DICT.WL_XS.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro(), 3);
        map.put(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro(), 30);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro(), 21);
        map.put(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro(), 21);
        ConvertDataPool.defPetCorrectionMaxMap.put(5, map);
    }
    
    public static void initDefPetCorrection() {
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_FY.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HL.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_HS.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_YW.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_F.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_L.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_S.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_H.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_GH.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_SS.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.K_DU.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.WL_XS.getPro()), Double.valueOf(0.03));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro()), Double.valueOf(0.015));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.MISS_RATIO.getPro()), Double.valueOf(0.015));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()), Double.valueOf(1.0));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro()), Double.valueOf(0.015));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro()), Double.valueOf(0.015));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_EXTENT.getPro()), Double.valueOf(0.03));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.CONQUER_RATIO.getPro()), Double.valueOf(0.03));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_RATIO.getPro()), Double.valueOf(0.04));
        ConvertDataPool.defPetCorrectionMap.put(Integer.valueOf(PRACTICE_PET_PRO_DICT.SHOCK_EXTENT.getPro()), Double.valueOf(0.04));
    }
    
    public static void initPetSkillReplace() {
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(0), new Integer[] { Integer.valueOf(88), Integer.valueOf(12) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(1), new Integer[] { Integer.valueOf(82), Integer.valueOf(18) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(2), new Integer[] { Integer.valueOf(90), Integer.valueOf(10) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(3), new Integer[] { Integer.valueOf(92), Integer.valueOf(8) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(4), new Integer[] { Integer.valueOf(80), Integer.valueOf(20) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(5), new Integer[] { Integer.valueOf(91), Integer.valueOf(9) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(6), new Integer[] { Integer.valueOf(89), Integer.valueOf(11) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(7), new Integer[] { Integer.valueOf(86), Integer.valueOf(14) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(8), new Integer[] { Integer.valueOf(80), Integer.valueOf(20) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(9), new Integer[] { Integer.valueOf(87), Integer.valueOf(13) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(10), new Integer[] { Integer.valueOf(88), Integer.valueOf(12) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(11), new Integer[] { Integer.valueOf(86), Integer.valueOf(14) });
        ConvertDataPool.petSkillReplace.put(Integer.valueOf(12), new Integer[] { Integer.valueOf(95), Integer.valueOf(5) });
    }
    
    public static void initBagExtend() {
        ConvertDataPool.bagExtend.put(Integer.valueOf(1), new Item(Const.RESTYPE_SILVER, Integer.valueOf(500000)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(2), new Item(Const.RESTYPE_SILVER, Integer.valueOf(1000000)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(3), new Item(Const.RESTYPE_SILVER, Integer.valueOf(500000)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(4), new Item(Const.RESTYPE_SILVER, Integer.valueOf(1000000)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(5), new Item(Const.RESTYPE_SILVER, Integer.valueOf(2000000)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(6), new Item(Const.RESTYPE_SILVER, Integer.valueOf(3000000)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(7), new Item(Const.RESTYPE_SILVER, Integer.valueOf(3500000)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(8), new Item(Const.RESTYPE_SILVER, Integer.valueOf(4000000)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(9), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(10), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(11), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(12), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(13), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(14), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
        ConvertDataPool.bagExtend.put(Integer.valueOf(15), new Item(Const.RESTYPE_GOLD, Integer.valueOf(250)));
    }
    
    public static void initStoreExtend() {
        ConvertDataPool.storeExtend.put(Integer.valueOf(1), new Item(Const.RESTYPE_SILVER, Integer.valueOf(500000)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(2), new Item(Const.RESTYPE_SILVER, Integer.valueOf(1000000)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(3), new Item(Const.RESTYPE_SILVER, Integer.valueOf(500000)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(4), new Item(Const.RESTYPE_SILVER, Integer.valueOf(1000000)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(5), new Item(Const.RESTYPE_SILVER, Integer.valueOf(2000000)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(6), new Item(Const.RESTYPE_SILVER, Integer.valueOf(3000000)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(7), new Item(Const.RESTYPE_SILVER, Integer.valueOf(3500000)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(8), new Item(Const.RESTYPE_SILVER, Integer.valueOf(4000000)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(9), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(10), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(11), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(12), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(13), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(14), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(15), new Item(Const.RESTYPE_GOLD, Integer.valueOf(250)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(16), new Item(Const.RESTYPE_GOLD, Integer.valueOf(500)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(17), new Item(Const.RESTYPE_GOLD, Integer.valueOf(500)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(18), new Item(Const.RESTYPE_GOLD, Integer.valueOf(500)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(19), new Item(Const.RESTYPE_GOLD, Integer.valueOf(500)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(20), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1000)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(21), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1000)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(22), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1000)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(23), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1500)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(24), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1500)));
        ConvertDataPool.storeExtend.put(Integer.valueOf(25), new Item(Const.RESTYPE_GOLD, Integer.valueOf(1500)));
    }
    
    public static void initPetBagEquipExtend() {
        ConvertDataPool.petBagEquipExtend.put(Integer.valueOf(1), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
        ConvertDataPool.petBagEquipExtend.put(Integer.valueOf(2), new Item(Const.RESTYPE_GOLD, Integer.valueOf(100)));
        ConvertDataPool.petBagEquipExtend.put(Integer.valueOf(3), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
        ConvertDataPool.petBagEquipExtend.put(Integer.valueOf(4), new Item(Const.RESTYPE_GOLD, Integer.valueOf(150)));
        ConvertDataPool.petBagEquipExtend.put(Integer.valueOf(5), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
        ConvertDataPool.petBagEquipExtend.put(Integer.valueOf(6), new Item(Const.RESTYPE_GOLD, Integer.valueOf(200)));
        ConvertDataPool.petBagEquipExtend.put(Integer.valueOf(7), new Item(Const.RESTYPE_GOLD, Integer.valueOf(250)));
    }
    
    static {
        ConvertDataPool.defPetCorrectionMaxMap = new HashMap();
        ConvertDataPool.defPetCorrectionMap = new HashMap();
        ConvertDataPool.PRO_BASE_DICT = new ArrayList<>();
        ConvertDataPool.mindDefineList = new ArrayList<>();
        ConvertDataPool.petSkillReplace = new HashMap();
        ConvertDataPool.bagExtend = new HashMap();
        ConvertDataPool.storeExtend = new HashMap();
        ConvertDataPool.petBagEquipExtend = new HashMap();
    }
}
