package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.common.utils.RandomUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import java.util.Iterator;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31507 extends AwakenSkillProcessor {
  public void enterFight(Fight fight, FightObject fightObject, Object... args) {
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)fightObject, 40.0D, 20.0D);
    if (map == null)
      return; 
    double percent = map.getDouble("percent");
    if (percent > 0.0D) {
      FightObject host = fight.queryFightObject(Integer.valueOf(fightObject.getHostId()));
      if (host != null) {
        int dict = getDict(host);
        ImbueUtil.addOrUpdSkill(host.getImbueMap(), Integer.valueOf(dict), WarMaxRound.WAR_MAX_ROUND.getValue(), 31507, percent);
      } 
    } 
  }
  
  private int getDict(FightObject fightObject) {
    double flag = 0.0D;
    int returnDict = 0;
    List<Integer> qWuXinDist = PRO_DICT_TYPE.getQWuXinDist();
    for (Iterator<Integer> iterator = qWuXinDist.iterator(); iterator.hasNext(); ) {
      int dict = ((Integer)iterator.next()).intValue();
      if (flag < fightObject.getSelfAllTempPro(dict)) {
        flag = fightObject.getSelfAllTempPro(dict);
        returnDict = dict;
      } 
    } 
    if (returnDict == 0) {
      int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(qWuXinDist.size() - 1));
      returnDict = ((Integer)qWuXinDist.get(round)).intValue();
    } 
    return returnDict;
  }
  
  public void exitSide(Fight fight, FightObject fightObject, Object... args) {
    if (fightObject.isFallDown()) {
      FightObject host = fight.queryFightObject(Integer.valueOf(fightObject.getHostId()));
      if (host != null) {
        int dict = getDict(host);
        ImbueUtil.removeSkill(host.getImbueMap(), dict, 31507);
      } 
    } 
  }
}
