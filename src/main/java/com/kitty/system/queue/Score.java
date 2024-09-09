package com.kitty.system.queue;

import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.itempro.def.EQUIP_PRO_DICT_SCORE;
import com.kitty.system.game.vo.FiedEquip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Score
{
    public Score() {
    }
    
    public static void main(String[] args) {
        Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
        List<FiedEquip> field = new ArrayList<>();
        field.add(new FiedEquip(90004, 17));
        field.add(new FiedEquip(90004, 14));
        field.add(new FiedEquip(90606, 75));
        field.add(new FiedEquip(90106, 52));
        double score = 1100.0;
        for (FiedEquip equip : field) {
            int dist = DataGlobalFunc.GetProDictType(equip.getType());
            Double odds = map.get(dist);
            score += (double)(int)equip.getValue() * odds;
        }
        System.out.println("score = " + score);
    }
}
