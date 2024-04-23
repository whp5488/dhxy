package com.kitty.system.game.fightnew.book.drug;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10021;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10022;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10023;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10024;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10025;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10027;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10028;
import java.util.HashMap;
import java.util.Map;
import org.nutz.lang.util.NutMap;

public abstract class DrugProcessor {
  static Map<Integer, DrugProcessor> drugs = new HashMap<>();
  
  static {
    drugs.put(Integer.valueOf(10021), new DRUG_10021());
    drugs.put(Integer.valueOf(10022), new DRUG_10022());
    drugs.put(Integer.valueOf(10023), new DRUG_10023());
    drugs.put(Integer.valueOf(10024), new DRUG_10024());
    drugs.put(Integer.valueOf(10025), new DRUG_10025());
    drugs.put(Integer.valueOf(10027), new DRUG_10027());
    drugs.put(Integer.valueOf(10028), new DRUG_10028());
  }
  
  public static DrugProcessor queryProcessor(Integer serial) {
    return drugs.get(serial);
  }
  
  public boolean checkExcute(Fight fight, FightObject effector, FightObject target, Object... args) {
    return true;
  }
  
  public abstract NutMap effect(Fight paramFight, FightObject paramFightObject1, FightObject paramFightObject2, Object... paramVarArgs);
}
