
package com.kitty.system.queue;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import com.kitty.system.enums.war.itempro.def.EQUIP_PRO_DICT_SCORE;

public class Score
{
    public static void main(final String[] args) {
        final Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
        final List<FiedEquip> field = new ArrayList<FiedEquip>();
        field.add(new FiedEquip(90004, 17));
        field.add(new FiedEquip(90004, 14));
        field.add(new FiedEquip(90606, 75));
        field.add(new FiedEquip(90106, 52));
        double score = 1100.0;
        for (final FiedEquip equip : field) {
            final int dist = DataGlobalFunc.GetProDictType(equip.getType());
            final Double odds = map.get(dist);
            score += equip.getValue() * odds;
        }
        System.out.println("score = " + score);
    }
}
