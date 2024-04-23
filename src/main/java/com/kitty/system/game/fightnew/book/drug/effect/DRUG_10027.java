package com.kitty.system.game.fightnew.book.drug.effect;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.DrugProcessor;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import org.nutz.lang.util.NutMap;

public class DRUG_10027 extends DrugProcessor {
  public NutMap effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    int reduceLife;
    Effect e = new Effect();
    if (target.isMonster() && target.getSerial() == 1660031) {
      reduceLife = 10000;
    } else {
      reduceLife = 1;
    } 
    if (target.getCurrLife() < reduceLife)
      reduceLife = target.getCurrLife(); 
    ActionParamUtils.changeLife(fight, effector, target, 5, 10027, 2, -reduceLife, false);
    return null;
  }
}
