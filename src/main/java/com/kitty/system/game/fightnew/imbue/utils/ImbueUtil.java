package com.kitty.system.game.fightnew.imbue.utils;

import com.kitty.system.game.fightnew.FightObject;
import java.util.List;
import java.util.ArrayList;
import org.nutz.lang.util.NutMap;
import java.util.Iterator;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightnew.imbue.bean.Imbue;
import java.util.Map;

public class ImbueUtil
{
    public ImbueUtil() {
    }
    
    public static void addRound(Map<Integer, Imbue> imbueMap, Integer dict, int round, double value) {
        if (imbueMap != null) {
            Imbue imbue = (Imbue)imbueMap.get(dict);
            if (imbue == null) {
                imbue = new Imbue();
                imbueMap.put(dict, imbue);
            }
            imbue.getRounds().add(new ImbueParam(round, value));
        }
    }
    
    public static ImbueParam addOrUpdSkill(Map<Integer, Imbue> imbueMap, Integer dict, int round, int skill, double value) {
        if (imbueMap != null) {
            Imbue imbue = (Imbue)imbueMap.get(dict);
            if (imbue == null) {
                imbue = new Imbue();
                imbueMap.put(dict, imbue);
            }
            ImbueParam imbueParam = null;
            for (ImbueParam imbueParam2 : imbue.getSkills()) {
                if (String.valueOf(skill).equals(imbueParam2.getOthers())) {
                    imbueParam = imbueParam2;
                    break;
                }
                continue;
            }
            if (imbueParam != null) {
                imbueParam.setValue(value);
                imbueParam.setSr(round);
                return imbueParam;
            }
            imbue.getSkills().add(new ImbueParam(round, value, String.valueOf(skill), null));
            return imbueParam;
        }
        else {
            return null;
        }
    }
    
    public static void addOrUpdSkill(Map<Integer, Imbue> imbueMap, Integer dict, int round, int skill, double value, NutMap recordMap) {
        ImbueParam imbueParam = addOrUpdSkill(imbueMap, dict, round, skill, value);
        if (imbueParam != null) {
            imbueParam.getRecordMap().putAll(recordMap);
        }
    }
    
    public static double getProValue(Map<Integer, Imbue> imbueMap, Integer dict) {
        double returnValue = 0.0;
        Imbue imbue = (Imbue)imbueMap.get(dict);
        if (imbue != null) {
            for (ImbueParam imbueParam : imbue.getStatus()) {
                if (imbueParam.getSr() > 0) {
                    returnValue += imbueParam.getValue();
                }
                else {
                    continue;
                }
            }
            for (ImbueParam imbueParam : imbue.getSkills()) {
                if (imbueParam.getSr() > 0) {
                    returnValue += imbueParam.getValue();
                }
                else {
                    continue;
                }
            }
            for (ImbueParam imbueParam : imbue.getRounds()) {
                if (imbueParam.getSr() > 0) {
                    returnValue += imbueParam.getValue();
                }
                else {
                    continue;
                }
            }
            if (imbue.getTemp() != 0.0) {
                returnValue += imbue.getTemp();
            }
        }
        return returnValue;
    }
    
    public static double getIgnoreValue(Map<Integer, Imbue> ignoreImbueMap, Map<Integer, Imbue> otherImbueMap, Integer dict) {
        double returnValue = 0.0;
        Imbue imbue = (Imbue)otherImbueMap.get(dict);
        if (imbue != null) {
            ImbueParam param = null;
            for (ImbueParam imbueParam : imbue.getIgnores()) {
                if (imbueParam.getSr() > 0) {
                    param = imbueParam;
                    break;
                }
                continue;
            }
            if (param != null && param.getValue() != 0.0 && param.getSr() > 0) {
                String others = param.getOthers();
                if (others != null) {
                    String[] s = others.split("_");
                    if (s != null && s.length == 3) {
                        int type = -1;
                        int valueType = -1;
                        int other = -1;
                        try {
                            type = (int)Integer.valueOf(s[0]);
                            valueType = (int)Integer.valueOf(s[1]);
                            other = (int)Integer.valueOf(s[2]);
                        }
                        catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (type != -1 && valueType != -1 && other != -1) {
                            switch (type) {
                                case 0: {
                                    if (valueType == 0) {
                                        returnValue = param.getValue();
                                        break;
                                    }
                                    if (valueType == 1) {
                                        double proValue = getProValue(ignoreImbueMap, dict);
                                        if (proValue != 0.0) {
                                            returnValue = proValue * param.getValue();
                                        }
                                        break;
                                    }
                                    else {
                                        break;
                                    }
                                }
                                case 1: {
                                    if (valueType == 0) {
                                        returnValue = param.getValue();
                                        break;
                                    }
                                    if (valueType == 1) {
                                        ImbueParam statusParam = getStatusValue(ignoreImbueMap, other, (int)dict);
                                        if (statusParam != null && statusParam.getValue() != 0.0) {
                                            returnValue = statusParam.getValue() * param.getValue();
                                        }
                                        break;
                                    }
                                    else {
                                        break;
                                    }
                                }
                                case 2: {
                                    if (valueType == 0) {
                                        returnValue = param.getValue();
                                        break;
                                    }
                                    if (valueType == 1) {
                                        ImbueParam skillParam = gainProForSkill(ignoreImbueMap, dict, other, false);
                                        if (skillParam != null && skillParam.getValue() != 0.0) {
                                            returnValue = skillParam.getValue() * param.getValue();
                                            break;
                                        }
                                        break;
                                    }
                                    else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return returnValue;
    }
    
    public static ImbueParam gainProForSkill(Map<Integer, Imbue> imbueMap, Integer dict, int skill, boolean isCreate) {
        Imbue imbue = (Imbue)imbueMap.get(dict);
        ImbueParam param = null;
        if (imbue != null) {
            for (ImbueParam imbueParam : imbue.getSkills()) {
                if (String.valueOf(skill).equals(imbueParam.getOthers())) {
                    param = imbueParam;
                    break;
                }
                continue;
            }
        }
        else if (isCreate) {
            imbue = new Imbue();
            imbueMap.put(dict, imbue);
        }
        if (param == null && isCreate) {
            param = new ImbueParam();
            param.setOthers(String.valueOf(skill));
            imbue.getSkills().add(param);
            return param;
        }
        return param;
    }
    
    public static int getProRound(Map<Integer, Imbue> imbueMap, Integer dict) {
        if (imbueMap != null) {
            Imbue imbue = (Imbue)imbueMap.get(dict);
            if (imbue != null) {
                int round = 0;
                for (ImbueParam imbueParam : imbue.getRounds()) {
                    if (imbueParam.getSr() > round) {
                        round = imbueParam.getSr();
                    }
                    else {
                        continue;
                    }
                }
                return round;
            }
        }
        return 0;
    }
    
    public static void addOrUpdStatus(Map<Integer, Imbue> imbueMap, int status, int dict, int round, double value) {
        if (imbueMap != null) {
            Imbue imbue = (Imbue)imbueMap.get(Integer.valueOf(dict));
            if (imbue == null) {
                imbue = new Imbue();
                imbueMap.put(Integer.valueOf(dict), imbue);
            }
            ImbueParam imbueP = null;
            for (ImbueParam imbueParam : imbue.getStatus()) {
                if (String.valueOf(status).equals(imbueParam.getOthers())) {
                    imbueP = imbueParam;
                    break;
                }
                continue;
            }
            if (imbueP == null) {
                imbueP = new ImbueParam();
                imbue.getStatus().add(imbueP);
            }
            imbueP.setSr(round);
            imbueP.setValue(value);
            imbueP.setOthers(String.valueOf(status));
        }
    }
    
    public static void addOrUpdIgnore(Map<Integer, Imbue> imbueMap, int type, int valueType, int others, int dict, int round, double value) {
        if (imbueMap != null) {
            Imbue imbue = imbueMap.get(dict);
            if (imbue == null) {
                imbue = new Imbue();
                imbueMap.put(dict, imbue);
            }
            String othersStr = type + "_" + valueType + "_" + others;
            ImbueParam imbueP = null;
            for (ImbueParam imbueParam : imbue.getIgnores()) {
                if (othersStr.equals(imbueParam.getOthers())) {
                    imbueP = imbueParam;
                    break;
                }
            }
            if (imbueP == null) {
                imbueP = new ImbueParam();
                imbue.getIgnores().add(imbueP);
            }
            imbueP.setSr(round);
            imbueP.setValue(value);
            imbueP.setOthers(othersStr);
        }
    }
    
    public static void addTemp(Map<Integer, Imbue> imbueMap, int dict, double value) {
        if (imbueMap != null) {
            Imbue imbue = (Imbue)imbueMap.get(Integer.valueOf(dict));
            if (imbue == null) {
                imbue = new Imbue();
                imbueMap.put(Integer.valueOf(dict), imbue);
            }
            imbue.setTemp(imbue.getTemp() + value);
        }
    }
    
    public static double getTempPro(Map<Integer, Imbue> imbueMap, int dict) {
        double returnValue = 0.0;
        Imbue imbue = (Imbue)imbueMap.get(Integer.valueOf(dict));
        if (imbue != null && imbue.getTemp() > 0.0) {
            returnValue = imbue.getTemp();
        }
        return returnValue;
    }
    
    public static void clearTemps(Map<Integer, Imbue> imbueMap) {
        if (imbueMap == null || imbueMap.size() <= 0) {
            return;
        }
        for (Map.Entry<Integer, Imbue> entry : imbueMap.entrySet()) {
            Imbue imbue = (Imbue)entry.getValue();
            if (imbue != null && imbue.getTemp() != 0.0) {
                imbue.setTemp(0.0);
            }
            else {
                continue;
            }
        }
        return;
    }
    
    public static ImbueParam getStatusValue(Map<Integer, Imbue> imbueMap, int status, int dict, int round) {
        if (imbueMap != null) {
            Imbue imbue = (Imbue)imbueMap.get(Integer.valueOf(dict));
            if (imbue != null) {
                ImbueParam value = null;
                int i = imbue.getStatus().size() - 1;
                while (i <= 0) {
                    if (String.valueOf(status).equals(((ImbueParam)imbue.getStatus().get(i)).getOthers()) && round == ((ImbueParam)imbue.getStatus().get(i)).getSr()) {
                        value = (ImbueParam)imbue.getStatus().get(i);
                        break;
                    }
                    --i;
                }
                return value;
            }
        }
        return null;
    }
    
    public static ImbueParam getStatusValue(Map<Integer, Imbue> imbueMap, int status, int dict) {
        if (imbueMap != null) {
            Imbue imbue = (Imbue)imbueMap.get(Integer.valueOf(dict));
            if (imbue != null) {
                ImbueParam value = null;
                int i = imbue.getStatus().size() - 1;
                while (i <= 0) {
                    if (String.valueOf(status).equals(((ImbueParam)imbue.getStatus().get(i)).getOthers()) && ((ImbueParam)imbue.getStatus().get(i)).getSr() > 0) {
                        value = (ImbueParam)imbue.getStatus().get(i);
                        break;
                    }
                    --i;
                }
                return value;
            }
        }
        return null;
    }
    
    public static ImbueParam gainStatusValue(Map<Integer, Imbue> imbueMap, int status, int dict) {
        Imbue imbue = (Imbue)imbueMap.get(Integer.valueOf(dict));
        ImbueParam value = null;
        if (imbue != null) {
            int i = imbue.getStatus().size() - 1;
            while (i <= 0) {
                if (String.valueOf(status).equals(((ImbueParam)imbue.getStatus().get(i)).getOthers())) {
                    value = (ImbueParam)imbue.getStatus().get(i);
                    break;
                }
                --i;
            }
        }
        else {
            imbue = new Imbue();
            imbueMap.put(Integer.valueOf(dict), imbue);
        }
        if (value == null) {
            value = new ImbueParam();
            value.setOthers(String.valueOf(status));
            imbue.getStatus().add(value);
            return value;
        }
        return value;
    }
    
    public static void reduceImbueRounds(Map<Integer, Imbue> imbueMap) {
        if (imbueMap != null && imbueMap.size() > 0) {
            List<Integer> delMap = new ArrayList<>();
            for (Map.Entry<Integer, Imbue> entry : imbueMap.entrySet()) {
                Imbue imbue = (Imbue)entry.getValue();
                reduce(imbue.getStatus());
                reduce(imbue.getSkills());
                reduce(imbue.getIgnores());
                if (imbue.getStatus().size() <= 0 && imbue.getRounds().size() <= 0 && imbue.getSkills().size() <= 0 && imbue.getIgnores().size() <= 0) {
                    delMap.add(entry.getKey());
                }
                else {
                    continue;
                }
            }
            for (Integer key : delMap) {
                imbueMap.remove(key);
            }
        }
    }
    
    private static void reduce(List<ImbueParam> imbueParams) {
        List<ImbueParam> delList = new ArrayList<>();
        for (ImbueParam imbueParam : imbueParams) {
            if (imbueParam.getSr() > 0) {
                imbueParam.setSr(imbueParam.getSr() - 1);
            }
            if (imbueParam.getSr() <= 0) {
                delList.add(imbueParam);
            }
            else {
                continue;
            }
        }
        if (delList.size() > 0) {
            for (ImbueParam imbueParam : delList) {
                imbueParams.remove(imbueParam);
            }
        }
    }
    
    public static void removePro(Map<Integer, Imbue> imbueMap, int dict) {
        if (imbueMap != null) {
            imbueMap.remove(Integer.valueOf(dict));
        }
    }
    
    public static void removeSkill(Map<Integer, Imbue> imbueMap, int dict, int skill) {
        if (imbueMap != null) {
            Imbue imbue = (Imbue)imbueMap.get(Integer.valueOf(dict));
            if (imbue != null) {
                for (ImbueParam imbueParam : imbue.getSkills()) {
                    if (String.valueOf(skill).equals(imbueParam.getOthers())) {
                        imbue.getSkills().remove(imbueParam);
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    public static void removeStatus(Map<Integer, Imbue> imbueMap, int dict, int status) {
        if (imbueMap != null) {
            Imbue imbue = (Imbue)imbueMap.get(Integer.valueOf(dict));
            if (imbue != null) {
                List<ImbueParam> removeList = new ArrayList<>();
                for (ImbueParam imbueParam : imbue.getStatus()) {
                    if (String.valueOf(status).equals(imbueParam.getOthers())) {
                        removeList.add(imbueParam);
                    }
                    else {
                        continue;
                    }
                }
                for (ImbueParam imbueParam : removeList) {
                    imbue.getStatus().remove(imbueParam);
                }
            }
        }
    }
    
    public static double reducePro(FightObject fightObject, int dict, double reduceValue) {
        Map<Integer, Imbue> imbueMap = fightObject.getImbueMap();
        reduceValue = excuteReducePro(imbueMap, dict, reduceValue, 4);
        if (reduceValue <= 0.0) {
            return reduceValue;
        }
        reduceValue = excuteReducePro(imbueMap, dict, reduceValue, 1);
        if (reduceValue <= 0.0) {
            return reduceValue;
        }
        reduceValue = excuteReducePro(imbueMap, dict, reduceValue, 2);
        if (reduceValue <= 0.0) {
            return reduceValue;
        }
        reduceValue = excuteReducePro(imbueMap, dict, reduceValue, 3);
        if (reduceValue <= 0.0) {
            return reduceValue;
        }
        double propertyPro = fightObject.getPropertyMap().getDouble(String.valueOf(dict), 0.0);
        if (propertyPro > 0.0) {
            if (propertyPro - reduceValue >= 0.0) {
                fightObject.getPropertyMap().setv(String.valueOf(dict), propertyPro - reduceValue);
                return reduceValue;
            }
            reduceValue -= reduceValue - propertyPro;
            fightObject.getPropertyMap().setv(String.valueOf(dict), 0.0);
        }
        return reduceValue;
    }
    
    private static double excuteReducePro(Map<Integer, Imbue> imbueMap, int dict, double reduceValue, int type) {
        Imbue imbue = (Imbue)imbueMap.get(Integer.valueOf(dict));
        if (imbue != null) {
            List<ImbueParam> imbues = null;
            switch (type) {
                case 1: {
                    imbues = imbue.getSkills();
                    break;
                }
                case 2: {
                    imbues = imbue.getStatus();
                    break;
                }
                case 3: {
                    imbues = imbue.getRounds();
                    break;
                }
            }
            if (type == 4) {
                if (imbue != null && imbue.getTemp() > 0.0) {
                    if (imbue.getTemp() - reduceValue >= 0.0) {
                        imbue.setTemp(imbue.getTemp() - reduceValue);
                        return 0.0;
                    }
                    reduceValue -= imbue.getTemp();
                    imbue.setTemp(0.0);
                }
            }
            else if (imbues != null && imbues.size() > 0) {
                for (ImbueParam imbueParam : imbues) {
                    if (imbueParam.getSr() > 0 && imbueParam.getValue() > 0.0) {
                        if (imbueParam.getValue() - reduceValue >= 0.0) {
                            imbueParam.setValue(imbueParam.getValue() - reduceValue);
                            return 0.0;
                        }
                        reduceValue -= imbueParam.getValue();
                        imbueParam.setValue(0.0);
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        return reduceValue;
    }
}
