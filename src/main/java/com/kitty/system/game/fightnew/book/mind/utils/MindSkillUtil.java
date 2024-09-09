package com.kitty.system.game.fightnew.book.mind.utils;

import org.slf4j.LoggerFactory;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.itempro.mind.MIND;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.book.mind.pojo.MindParam;
import java.util.Map;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.mind.service.MindService;
import org.nutz.lang.util.NutMap;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import java.util.Iterator;
import com.kitty.system.init.domain.vo.MindDefineRange;
import com.kitty.system.game.vo.FieldProp;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.init.domain.vo.MindDefine;
import com.kitty.system.game.mind.domain.Mind;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.Logger;

public class MindSkillUtil
{
    static Logger logger;
    
    public MindSkillUtil() {
    }
    
    public static double countMindProp(Role role, List<Mind> list, int dist, double base) {
        double cap = 0.0;
        for (Mind mind : list) {
            MindDefine mindDefine = (MindDefine)ConvertDataPool.mindDefineList.stream().filter(e -> e.getSerial() == mind.getSerial()).findFirst().orElse(null);
            if (mindDefine == null) {
                continue;
            }
            for (FieldProp fieldProp : mind.getRefine()) {
                if (fieldProp.getType().equals(Integer.valueOf(dist))) {
                    for (MindDefineRange mindDefineRange : mindDefine.getList()) {
                        if (mindDefineRange.getS() == dist) {
                            cap += countMindProp(mindDefineRange.getT(), mindDefineRange.getRange(), fieldProp, base);
                        }
                        else {
                            continue;
                        }
                    }
                    if (mindDefine.getMap().size() > 0) {
                        List<MindDefineRange> defineRanges = (List)mindDefine.getMap().get(role.getSerial());
                        if (defineRanges != null) {
                            for (MindDefineRange mindDefineRange2 : defineRanges) {
                                if (mindDefineRange2.getS() == dist) {
                                    cap += countMindProp(mindDefineRange2.getT(), mindDefineRange2.getRange(), fieldProp, base);
                                }
                                else {
                                    continue;
                                }
                            }
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
        }
        return cap;
    }
    
    public static double countMindProp(int type, List<Double> range, FieldProp fieldProp, double base) {
        double cap = 0.0;
        if (PRO_TYPE.Pro_Value_NUM_TYPE.getValue() == type) {
            if (range.size() < 1) {
                MindSkillUtil.logger.error("心法计算基础属性错误-整数");
                return cap;
            }
            cap = (double)(int)fieldProp.getValue() / 100.0 * (double)range.get(0);
        }
        else if (PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue() == type) {
            if (range.size() < 2 && range.size() > 0) {
                cap = (double)(int)fieldProp.getValue() / 100.0 * (double)range.get(0) / 100.0;
            }
            else {
                double min = (double)range.get(0) * ((double)(int)fieldProp.getValue() / 100.0);
                double max = (double)range.get(1) * ((double)(int)fieldProp.getValue() / 100.0);
                cap = BigDecimalUtil.decimalScaleDouble(Integer.valueOf(1), Double.valueOf(Math.max(min, max - base * 0.44))) / 100.0;
            }
        }
        else {
            MindSkillUtil.logger.error("心法计算属性错误-类型：{}", fieldProp.getType());
        }
        return cap;
    }
    
    public static NutMap addition(Role role) {
        NutMap nutMap = NutMap.NEW();
        try {
            if (role != null) {
                List<Mind> mindList = ((MindService)SpringUtils.getBean(MindService.class)).getMind((long)role.getId());
                if (mindList != null && mindList.size() > 0) {
                    for (Mind mind : mindList) {
                        getMindPro(role, mind, nutMap);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return nutMap;
    }
    
    private static void getMindPro(Role role, Mind mind, NutMap nutMap) {
        if (mind != null) {
            Map<Integer, MindParam> mindParamMap = (Map)MindParam.mindParamMap.get(Integer.valueOf(mind.getClassify()));
            if (mindParamMap != null && mindParamMap.size() > 0) {
                List<FieldProp> refineList = mind.getRefine();
                if (refineList != null && refineList.size() > 0) {
                    double value = 0.0;
                    for (FieldProp fieldProp : refineList) {
                        MindParam mindParam = (MindParam)mindParamMap.get(fieldProp.getType());
                        if (mindParam != null) {
                            value = getValue(role, mindParam, (int)fieldProp.getValue());
                            if (value != 0.0) {
                                nutMap.setv(String.valueOf(fieldProp.getType()), Double.valueOf(nutMap.getDouble(String.valueOf(fieldProp.getType()), 0.0) + value));
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    private static double getValue(Role role, MindParam mindParam, int value) {
        double returnValue = 0.0;
        if (mindParam.getBasic() == 0) {
            returnValue = (double)(mindParam.getPercent() * value) / 10000.0;
        }
        else if (mindParam.getBasic() != 0) {
            double basicValue = (double)(mindParam.getBasic() * value) / 100.0;
            double percentValue = (double)(mindParam.getPercent() * (int)role.getGg() * value) / 10000.0;
            returnValue = basicValue + percentValue;
        }
        return returnValue;
    }
    
    public static double getMindValue(FightObject target, int dist) {
        if (!target.isPlayer()) {
            return 0.0;
        }
        Role role = target.getRole();
        double value = 0.0;
        try {
            if (role != null && dist > 0) {
                List<Mind> mindList = ((MindService)SpringUtils.getBean(MindService.class)).getMind((long)role.getId());
                if (mindList != null && mindList.size() > 0) {
                    for (Mind m : mindList) {
                        if (m != null) {
                            List<FieldProp> fieldProps = m.getRefine();
                            if (fieldProps != null) {
                                for (FieldProp fieldProp : fieldProps) {
                                    if ((int)fieldProp.getType() == dist) {
                                        if (MIND.XS_ZS_X.getDist() == dist) {
                                            value += getZsHpValue(target, role, (int)fieldProp.getValue());
                                        }
                                        else if (MIND.XS_ZS_F.getDist() == dist) {
                                            value += getZsMpValue(target, (int)fieldProp.getValue());
                                        }
                                        else if (MIND.SS_HX_XG.getDist() == dist) {
                                            value += getSshxxgValue(m.getClassify(), (int)fieldProp.getValue());
                                        }
                                        else if (MIND.XS_WL_XF.getDist() == dist) {
                                            value += getXfWlValue(target, m.getClassify(), (int)fieldProp.getValue());
                                        }
                                        else {
                                            continue;
                                        }
                                    }
                                    else {
                                        continue;
                                    }
                                }
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
    
    private static double getZsHpValue(FightObject target, Role role, int xld) {
        String divide = BigDecimalUtil.divide(String.valueOf(target.getCurrLife()), String.valueOf(target.getMaxLife()), 3);
        double percent = Double.parseDouble(divide);
        if (percent > 1.0) {
            percent = 1.0;
        }
        double value = (double)(30 * (int)role.getGg()) * percent * (double)xld / 100.0;
        return value;
    }
    
    private static double getZsMpValue(FightObject target, int xld) {
        String divide = BigDecimalUtil.divide(String.valueOf(target.getCurrMana()), String.valueOf(target.getMaxMana()), 3);
        double percent = Double.parseDouble(divide);
        if (percent > 1.0) {
            percent = 1.0;
        }
        double value = 3000.0 * percent * (double)xld / 100.0;
        return value;
    }
    
    private static double getSshxxgValue(int classify, int xld) {
        double value = 0.0;
        int power = 0;
        if (1 == classify || 2 == classify) {
            power = 3000;
        }
        else if (3 == classify || 4 == classify) {
            power = 30000;
        }
        if (power != 0) {
            value = (double)(power * xld) / 100.0;
        }
        return value;
    }
    
    private static double getXfWlValue(FightObject target, int classify, int xld) {
        double phyRes = target.getSelfAllTempPro(PRO_DICT_TYPE.WL_XS.getDist());
        String valueStr = "0.0";
        if (phyRes != 0.0) {
            valueStr = BigDecimalUtil.multiply(String.valueOf(phyRes), String.valueOf(0.44));
        }
        double floor = (double)(2 * xld) / 10000.0;
        double upper = 0.0;
        if (2 == classify || 3 == classify) {
            upper = (double)(13 * xld) / 10000.0;
        }
        else if (4 == classify) {
            upper = (double)(26 * xld) / 10000.0;
        }
        String subtract = BigDecimalUtil.subtract(String.valueOf(upper), valueStr);
        double upperZ = Double.parseDouble(subtract);
        double max = Math.max(floor, upperZ);
        return max;
    }
    
    static {
        MindSkillUtil.logger = LoggerFactory.getLogger(MindSkillUtil.class);
    }
}
