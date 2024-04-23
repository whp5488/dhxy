package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.HashMap;
import java.util.Map;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31515 extends AwakenSkillProcessor {
  public void magicAttackEnd(Fight fight, FightObject effector, Object... args) {
    if (args == null && args.length < 3)
      return; 
    RoleSkill skill = (RoleSkill)args[0];
    if (830341 != skill.getSkillId().intValue() && 830342 != skill.getSkillId().intValue() && 830343 != skill.getSkillId().intValue() && 830344 != skill
      .getSkillId().intValue() && 830345 != skill.getSkillId().intValue())
      return; 
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 60.0D, 20.0D);
    Map<String, Integer> dictMap = getDict(skill.getSkillId().intValue());
    if (dictMap.size() > 1 && 
      map != null) {
      double percent = map.getDouble("percent");
      if (percent > 0.0D) {
        int time = 2;
        Integer status = dictMap.get("s");
        FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
        host.setStatus(status.intValue(), time);
        ImbueUtil.addOrUpdStatus(host.getImbueMap(), status.intValue(), ((Integer)dictMap.get("d")).intValue(), time, percent);
        ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
        FightObjectParam targetHost = actionDetailParam.gainTargetParam(host.getId().intValue(), true);
        targetHost.setMissed(2);
        targetHost.getStatusMap().put(status, Integer.valueOf(time));
      } 
    } 
  }
  
  private Map<String, Integer> getDict(int skill) {
    Map<String, Integer> map = new HashMap<>();
    switch (skill) {
      case 830341:
        map.put("s", Integer.valueOf(2171));
        map.put("d", Integer.valueOf(PRO_DICT_TYPE.Q_KJ.getDist()));
        break;
      case 830342:
        map.put("s", Integer.valueOf(2172));
        map.put("d", Integer.valueOf(PRO_DICT_TYPE.Q_KM.getDist()));
        break;
      case 830343:
        map.put("s", Integer.valueOf(2173));
        map.put("d", Integer.valueOf(PRO_DICT_TYPE.Q_KS.getDist()));
        break;
      case 830344:
        map.put("s", Integer.valueOf(2174));
        map.put("d", Integer.valueOf(PRO_DICT_TYPE.Q_KH.getDist()));
        break;
      case 830345:
        map.put("s", Integer.valueOf(2175));
        map.put("d", Integer.valueOf(PRO_DICT_TYPE.Q_KT.getDist()));
        break;
    } 
    return map;
  }
}
