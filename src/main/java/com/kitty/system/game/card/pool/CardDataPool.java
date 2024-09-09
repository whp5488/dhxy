package com.kitty.system.game.card.pool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.service.InitService;
import java.util.List;
import com.kitty.system.init.domain.SysDataCardFiveExercise;
import com.kitty.system.init.domain.SysDataCard;
import java.util.Map;

public class CardDataPool
{
    public static Map<Integer, SysDataCard> cardMap;
    public static Map<Integer, SysDataCardFiveExercise> cardFiveExerciseMap;
    public static int cardFiveExerciseMaxGrade;
    public static List<Integer> cardFiveExerciseItem;
    
    public CardDataPool() {
    }
    
    public static void load() {
        InitService initService = (InitService)SpringUtils.getBean(InitService.class);
        CardDataPool.cardMap = initService.initCard();
        CardDataPool.cardFiveExerciseMap = initService.initCardFiveExercise();
        if (CardDataPool.cardFiveExerciseMap.size() > 0) {
            Set<Integer> set = CardDataPool.cardFiveExerciseMap.keySet();
            int max = CardDataPool.cardFiveExerciseMaxGrade = (int)set.stream().max(Integer::compare).get();
        }
    }
    
    static {
        CardDataPool.cardMap = new HashMap();
        CardDataPool.cardFiveExerciseMap = new HashMap();
        CardDataPool.cardFiveExerciseMaxGrade = 100;
        CardDataPool.cardFiveExerciseItem = Arrays.asList(Integer.valueOf(75032), Integer.valueOf(75033), Integer.valueOf(75034), Integer.valueOf(75035), Integer.valueOf(75036), Integer.valueOf(75037), Integer.valueOf(75038), Integer.valueOf(75039), Integer.valueOf(75040), Integer.valueOf(75041));
    }
}
