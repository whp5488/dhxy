package com.kitty.system.game.fightnew.book.awaken.other;

import java.util.Iterator;
import java.util.List;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;

public class AWAKENSKILL_31507 extends AwakenSkillProcessor
{
    public AWAKENSKILL_31507() {
    }
    
    @Override
    public void enterFight(Fight fight, FightObject fightObject, Object... args) {
        NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)fightObject, 40.0, 20.0);
        if (map == null) {
            return;
        }
        double percent = map.getDouble("percent");
        if (percent > 0.0) {
            FightObject host = fight.queryFightObject(Integer.valueOf(fightObject.getHostId()));
            if (host != null) {
                int dict = this.getDict(host);
                ImbueUtil.addOrUpdSkill(host.getImbueMap(), Integer.valueOf(dict), WarMaxRound.WAR_MAX_ROUND.getValue(), 31507, percent);
            }
        }
        return;
    }
    
    private int getDict(FightObject fightObject) {
        double flag = 0.0;
        int returnDict = 0;
        List<Integer> qWuXinDist = PRO_DICT_TYPE.getQWuXinDist();
        for (int dict : qWuXinDist) {
            if (flag < fightObject.getSelfAllTempPro(dict)) {
                flag = fightObject.getSelfAllTempPro(dict);
                returnDict = dict;
            }
            else {
                continue;
            }
        }
        if (returnDict == 0) {
            int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(qWuXinDist.size() - 1));
            returnDict = (int)qWuXinDist.get(round);
        }
        return returnDict;
    }
    
    @Override
    public void exitSide(Fight fight, FightObject fightObject, Object... args) {
        if (fightObject.isFallDown()) {
            FightObject host = fight.queryFightObject(Integer.valueOf(fightObject.getHostId()));
            if (host != null) {
                int dict = this.getDict(host);
                ImbueUtil.removeSkill(host.getImbueMap(), dict, 31507);
            }
        }
    }
}
