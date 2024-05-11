
package com.kitty.system.game.fightnew.imbue.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightnew.imbue.bean.Imbue;
import java.util.Map;

public class ImbueUtil
{
    public static void addRound(final Map<Integer, Imbue> imbueMap, final Integer dict, final int round, final double value) {
        if (imbueMap != null) {
            Imbue imbue = imbueMap.get(dict);
            if (imbue == null) {
                imbue = new Imbue();
                imbueMap.put(dict, imbue);
            }
            imbue.getRounds().add(new ImbueParam(round, value));
        }
    }
    
    public static void addOrUpdSkill(final Map<Integer, Imbue> imbueMap, final Integer dict, final int round, final int skill, final double value) {
        if (imbueMap != null) {
            Imbue imbue = imbueMap.get(dict);
            if (imbue == null) {
                imbue = new Imbue();
                imbueMap.put(dict, imbue);
            }
            ImbueParam imbueParam = null;
            for (final ImbueParam imbueParam2 : imbue.getSkills()) {
                if (String.valueOf(skill).equals(imbueParam2.getOthers())) {
                    imbueParam = imbueParam2;
                    break;
                }
            }
            if (imbueParam != null) {
                imbueParam.setValue(value);
                imbueParam.setSr(round);
            }
            else {
                imbue.getSkills().add(new ImbueParam(round, value, String.valueOf(skill)));
            }
        }
    }
    
    public static double getProValue(final Map<Integer, Imbue> imbueMap, final Integer dict) {
        double returnValue = 0.0;
        final Imbue imbue = imbueMap.get(dict);
        if (imbue != null) {
            for (final ImbueParam imbueParam : imbue.getStatus()) {
                if (imbueParam.getSr() > 0) {
                    returnValue += imbueParam.getValue();
                }
            }
            for (final ImbueParam imbueParam : imbue.getSkills()) {
                if (imbueParam.getSr() > 0) {
                    returnValue += imbueParam.getValue();
                }
            }
            for (final ImbueParam imbueParam : imbue.getRounds()) {
                if (imbueParam.getSr() > 0) {
                    returnValue += imbueParam.getValue();
                }
            }
            if (imbue.getTemp() != 0.0) {
                returnValue += imbue.getTemp();
            }
        }
        return returnValue;
    }
    
    public static ImbueParam gainProForSkill(final Map<Integer, Imbue> imbueMap, final Integer dict, final int skill, final boolean isCreate) {
        Imbue imbue = imbueMap.get(dict);
        ImbueParam param = null;
        if (imbue != null) {
            for (final ImbueParam imbueParam : imbue.getSkills()) {
                if (String.valueOf(skill).equals(imbueParam.getOthers())) {
                    param = imbueParam;
                    break;
                }
            }
        }
        else {
            imbue = new Imbue();
            imbueMap.put(dict, imbue);
        }
        if (param == null && isCreate) {
            param = new ImbueParam();
            param.setOthers(String.valueOf(skill));
            imbue.getSkills().add(param);
        }
        return param;
    }
    
    public static int getProRound(final Map<Integer, Imbue> imbueMap, final Integer dict) {
        if (imbueMap != null) {
            final Imbue imbue = imbueMap.get(dict);
            if (imbue != null) {
                int round = 0;
                for (final ImbueParam imbueParam : imbue.getRounds()) {
                    if (imbueParam.getSr() > round) {
                        round = imbueParam.getSr();
                    }
                }
                return round;
            }
        }
        return 0;
    }
    
    public static void addOrUpdStatus(final Map<Integer, Imbue> imbueMap, final int status, final int dict, final int round, final double value) {
        if (imbueMap != null) {
            Imbue imbue = imbueMap.get(dict);
            if (imbue == null) {
                imbue = new Imbue();
                imbueMap.put(dict, imbue);
            }
            ImbueParam imbueP = null;
            for (final ImbueParam imbueParam : imbue.getStatus()) {
                if (String.valueOf(status).equals(imbueParam.getOthers())) {
                    imbueP = imbueParam;
                    break;
                }
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
    
    public static void addTemp(final Map<Integer, Imbue> imbueMap, final int dict, final double value) {
        if (imbueMap != null) {
            Imbue imbue = imbueMap.get(dict);
            if (imbue == null) {
                imbue = new Imbue();
                imbueMap.put(dict, imbue);
            }
            imbue.setTemp(imbue.getTemp() + value);
        }
    }
    
    public static void clearTemps(final Map<Integer, Imbue> imbueMap) {
        if (imbueMap == null || imbueMap.size() <= 0) {
            return;
        }
        for (final Map.Entry<Integer, Imbue> entry : imbueMap.entrySet()) {
            final Imbue imbue = entry.getValue();
            if (imbue != null && imbue.getTemp() != 0.0) {
                imbue.setTemp(0.0);
            }
        }
    }
    
    public static ImbueParam getStatusValue(final Map<Integer, Imbue> imbueMap, final int status, final int dict, final int round) {
        if (imbueMap != null) {
            final Imbue imbue = imbueMap.get(dict);
            if (imbue != null) {
                ImbueParam value = null;
                for (int i = imbue.getStatus().size() - 1; i <= 0; --i) {
                    if (String.valueOf(status).equals(imbue.getStatus().get(i).getOthers()) && round == imbue.getStatus().get(i).getSr()) {
                        value = imbue.getStatus().get(i);
                        break;
                    }
                }
                return value;
            }
        }
        return null;
    }
    
    public static ImbueParam getStatusValue(final Map<Integer, Imbue> imbueMap, final int status, final int dict) {
        if (imbueMap != null) {
            final Imbue imbue = imbueMap.get(dict);
            if (imbue != null) {
                ImbueParam value = null;
                for (int i = imbue.getStatus().size() - 1; i <= 0; --i) {
                    if (String.valueOf(status).equals(imbue.getStatus().get(i).getOthers()) && imbue.getStatus().get(i).getSr() > 0) {
                        value = imbue.getStatus().get(i);
                        break;
                    }
                }
                return value;
            }
        }
        return null;
    }
    
    public static ImbueParam gainStatusValue(final Map<Integer, Imbue> imbueMap, final int status, final int dict) {
        Imbue imbue = imbueMap.get(dict);
        ImbueParam value = null;
        if (imbue != null) {
            for (int i = imbue.getStatus().size() - 1; i <= 0; --i) {
                if (String.valueOf(status).equals(imbue.getStatus().get(i).getOthers())) {
                    value = imbue.getStatus().get(i);
                    break;
                }
            }
        }
        else {
            imbue = new Imbue();
            imbueMap.put(dict, imbue);
        }
        if (value == null) {
            value = new ImbueParam();
            value.setOthers(String.valueOf(status));
            imbue.getStatus().add(value);
        }
        return value;
    }
    
    public static void reduceImbueRounds(final Map<Integer, Imbue> imbueMap) {
        if (imbueMap != null && imbueMap.size() > 0) {
            final List<Integer> delMap = new ArrayList<Integer>();
            for (final Map.Entry<Integer, Imbue> entry : imbueMap.entrySet()) {
                final Imbue imbue = entry.getValue();
                reduce(imbue.getStatus());
                reduce(imbue.getSkills());
                if (imbue.getStatus().size() <= 0 && imbue.getRounds().size() <= 0 && imbue.getSkills().size() <= 0) {
                    delMap.add(entry.getKey());
                }
            }
            for (final Integer key : delMap) {
                imbueMap.remove(key);
            }
        }
    }
    
    private static void reduce(final List<ImbueParam> imbueParams) {
        final List<ImbueParam> delList = new ArrayList<ImbueParam>();
        for (final ImbueParam imbueParam : imbueParams) {
            if (imbueParam.getSr() > 0) {
                imbueParam.setSr(imbueParam.getSr() - 1);
            }
            if (imbueParam.getSr() <= 0) {
                delList.add(imbueParam);
            }
        }
        if (delList.size() > 0) {
            for (final ImbueParam imbueParam : delList) {
                imbueParams.remove(imbueParam);
            }
        }
    }
    
    public static void removePro(final Map<Integer, Imbue> imbueMap, final int dict) {
        if (imbueMap != null) {
            imbueMap.remove(dict);
        }
    }
    
    public static void removeSkill(final Map<Integer, Imbue> imbueMap, final int dict, final int skill) {
        if (imbueMap != null) {
            final Imbue imbue = imbueMap.get(dict);
            if (imbue != null) {
                for (final ImbueParam imbueParam : imbue.getSkills()) {
                    if (String.valueOf(skill).equals(imbueParam.getOthers())) {
                        imbue.getSkills().remove(imbueParam);
                        break;
                    }
                }
            }
        }
    }
    
    public static void removeStatus(final Map<Integer, Imbue> imbueMap, final int dict, final int status) {
        if (imbueMap != null) {
            final Imbue imbue = imbueMap.get(dict);
            if (imbue != null) {
                final List<ImbueParam> removeList = new ArrayList<ImbueParam>();
                for (final ImbueParam imbueParam : imbue.getStatus()) {
                    if (String.valueOf(status).equals(imbueParam.getOthers())) {
                        removeList.add(imbueParam);
                    }
                }
                for (final ImbueParam imbueParam : removeList) {
                    imbue.getStatus().remove(imbueParam);
                }
            }
        }
    }
}
