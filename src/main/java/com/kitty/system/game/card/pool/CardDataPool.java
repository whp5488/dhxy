package com.kitty.system.game.card.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.init.domain.SysDataCardFiveExercise;
import com.kitty.system.init.service.InitService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CardDataPool {
  public static Map<Integer, SysDataCard> cardMap = new HashMap<>();
  
  public static Map<Integer, SysDataCardFiveExercise> cardFiveExerciseMap = new HashMap<>();
  
  public static int cardFiveExerciseMaxGrade = 100;
  
  public static List<Integer> cardFiveExerciseItem = Arrays.asList(new Integer[] { Integer.valueOf(75032), Integer.valueOf(75033), Integer.valueOf(75034), Integer.valueOf(75035), Integer.valueOf(75036), Integer.valueOf(75037), Integer.valueOf(75038), Integer.valueOf(75039), Integer.valueOf(75040), Integer.valueOf(75041) });
  
  public static void load() {
    InitService initService = (InitService)SpringUtils.getBean(InitService.class);
    cardMap = initService.initCard();
    cardFiveExerciseMap = initService.initCardFiveExercise();
    if (cardFiveExerciseMap.size() > 0) {
      Set<Integer> set = cardFiveExerciseMap.keySet();
      int max = ((Integer)set.stream().max(Integer::compare).get()).intValue();
      cardFiveExerciseMaxGrade = max;
    } 
  }
}
