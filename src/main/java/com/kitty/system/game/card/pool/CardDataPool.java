
package com.kitty.system.game.card.pool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.Comparator;
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
    
    public static void load() {
        final InitService initService = SpringUtils.getBean(InitService.class);
        CardDataPool.cardMap = initService.initCard();
        CardDataPool.cardFiveExerciseMap = initService.initCardFiveExercise();
        if (CardDataPool.cardFiveExerciseMap.size() > 0) {
            final Set<Integer> set = CardDataPool.cardFiveExerciseMap.keySet();
            final int max = CardDataPool.cardFiveExerciseMaxGrade = set.stream().max(Integer::compare).get();
        }
    }
    
    static {
        CardDataPool.cardMap = new HashMap<Integer, SysDataCard>();
        CardDataPool.cardFiveExerciseMap = new HashMap<Integer, SysDataCardFiveExercise>();
        CardDataPool.cardFiveExerciseMaxGrade = 100;
        CardDataPool.cardFiveExerciseItem = Arrays.asList(75032, 75033, 75034, 75035, 75036, 75037, 75038, 75039, 75040, 75041);
    }
}
