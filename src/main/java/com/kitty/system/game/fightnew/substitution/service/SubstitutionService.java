package com.kitty.system.game.fightnew.substitution.service;

import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.fightnew.substitution.SubstitutionHandler;
import com.kitty.system.game.fightnew.substitution.handler.CsSubstitutionHandler;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SubstitutionService {
  public static Map<Integer, SubstitutionHandler> substitution = new HashMap<>();
  
  public void init() {
    substitution.put(FIGHT_TYPE.CS_SUB.getValue(), new CsSubstitutionHandler());
  }
  
  public static SubstitutionHandler querySubstitution(Integer type) {
    return substitution.get(type);
  }
}
