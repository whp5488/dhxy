package com.kitty.system.game.fightnew.book.utils;

import java.util.Iterator;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.FightObject;
import org.nutz.lang.util.NutMap;
import java.util.List;

public class ExcuteSkillReturn
{
    public ExcuteSkillReturn() {
    }
    
    public static void excuteTrainSkillReturn(List<NutMap> nutMapList, FightObject target, FightObjectParam targetParam) {
        if (nutMapList != null && nutMapList.size() > 0) {
            for (NutMap nutMap : nutMapList) {
                String type = nutMap.getString("type");
                int n = -1;
                switch (type.hashCode()) {
                    case -1585986401: {
                        if (type.equals("clearStatus")) {
                            n = 0;
                            break;
                        }
                        break;
                    }
                }
                switch (n) {
                    case 0: {
                        excuteClearStatus(nutMap, target, targetParam);
                        continue;
                    }
                }
            }
        }
    }
    
    private static void excuteClearStatus(NutMap nutMap, FightObject target, FightObjectParam targetParam) {
        List<Integer> clearStatusList = null;
        try {
            clearStatusList = nutMap.getList("clearStatus", Integer.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (clearStatusList != null && clearStatusList.size() > 0) {
            for (Integer status : clearStatusList) {
                target.removeStatus((int)status);
            }
            targetParam.addClearStatus(clearStatusList);
        }
    }
}
