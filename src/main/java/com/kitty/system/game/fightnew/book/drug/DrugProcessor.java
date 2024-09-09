package com.kitty.system.game.fightnew.book.drug;

import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10028;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10027;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10025;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10024;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10023;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10022;
import com.kitty.system.game.fightnew.book.drug.effect.DRUG_10021;
import java.util.HashMap;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import java.util.Map;

public abstract class DrugProcessor
{
    static Map<Integer, DrugProcessor> drugs;
    
    public DrugProcessor() {
    }
    
    public static DrugProcessor queryProcessor(Integer serial) {
        return (DrugProcessor)DrugProcessor.drugs.get(serial);
    }
    
    public abstract NutMap effect(Fight fight, FightObject effector, FightObject target, Object... args);
    
    public boolean checkExcute(Fight fight, FightObject effector, FightObject target, Object... args) {
        return true;
    }
    
    static {
        (DrugProcessor.drugs = new HashMap()).put(Integer.valueOf(10021), new DRUG_10021());
        DrugProcessor.drugs.put(Integer.valueOf(10022), new DRUG_10022());
        DrugProcessor.drugs.put(Integer.valueOf(10023), new DRUG_10023());
        DrugProcessor.drugs.put(Integer.valueOf(10024), new DRUG_10024());
        DrugProcessor.drugs.put(Integer.valueOf(10025), new DRUG_10025());
        DrugProcessor.drugs.put(Integer.valueOf(10027), new DRUG_10027());
        DrugProcessor.drugs.put(Integer.valueOf(10028), new DRUG_10028());
    }
}
