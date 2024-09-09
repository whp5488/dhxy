package com.kitty.system.game.fightnew.substitution.service;

import java.util.HashMap;
import com.kitty.system.game.fightnew.substitution.handler.CsSubstitutionHandler;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.fightnew.substitution.SubstitutionHandler;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SubstitutionService
{
    public static Map<Integer, SubstitutionHandler> substitution;
    
    public SubstitutionService() {
    }
    
    public void init() {
        SubstitutionService.substitution.put(Integer.valueOf(FIGHT_TYPE.CS_SUB.getValue()), new CsSubstitutionHandler());
    }
    
    public static SubstitutionHandler querySubstitution(Integer type) {
        return (SubstitutionHandler)SubstitutionService.substitution.get(type);
    }
    
    static {
        SubstitutionService.substitution = new HashMap();
    }
}
