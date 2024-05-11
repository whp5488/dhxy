
package com.kitty.system.game.ranking.util;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.ranking.pojo.WeaponsRankingSource;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import java.util.Optional;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.common.utils.BigDecimalUtil;
import org.apache.commons.collections.map.HashedMap;
import java.util.Map;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.pet.domain.Pet;
import java.util.List;
import com.kitty.system.game.role.domain.Role;

public class RankingUtils
{
    public static Map<String, Integer> getTotalRepair(final Role role, final List<Pet> pets, final List<WearEquip> wearEquips) {
        final Map<String, Integer> map = (Map<String, Integer>)new HashedMap();
        final int[] skillScores = { 0 };
        role.getSkills().values().stream().forEach(roleSkill -> {
            final int n;
            skillScores[0] += BigDecimalUtil.decimalScaleIntegerDown(0, roleSkill.getProficient() / 100);
            return;
        });
        final int skillScore = BigDecimalUtil.decimalScaleIntegerDown(0, BigDecimalUtil.multiply(String.valueOf(skillScores[0]), String.valueOf(2.5)));
        map.put("skillScore", skillScore);
        final int[] petScores = { 0 };
        if (pets != null) {
            pets.stream().forEach(pet -> {
                pet.doAfterInit();
                final int n2;
                petScores[0] += BigDecimalUtil.decimalScaleIntegerDown(0, pet.getScore());
                return;
            });
        }
        final int petScore = petScores[0];
        map.put("petScore", petScore);
        final int[] wearEquipScores = { 0 };
        if (wearEquips != null) {
            wearEquips.stream().forEach(wearEquip -> {
                wearEquip.doAfterInit();
                final Optional<FiedEquip> optional = wearEquip.getField().stream().filter(fiedEquip -> fiedEquip.getValue().equals(6679)).findAny();
                if (optional.isPresent()) {
                    final Integer value = optional.get().getValue();
                    if (value != null) {
                        final int n3;
                        wearEquipScores[0] += value;
                    }
                }
                return;
            });
        }
        final int wearEquipScore = wearEquipScores[0];
        map.put("wearEquipScore", wearEquipScore);
        return map;
    }
    
    public static int[] getRoleQlkWuxing(final Role role, final List<WearEquip> wearEquips, final QlkRankingSource qlkRankingSource) {
        final NutMap tianFuMap = TianFuUtils.countFuPlayerBookGain(role);
        int qlkj = 0;
        int qlkm = 0;
        int qlks = 0;
        int qlkh = 0;
        int qlkt = 0;
        if (qlkRankingSource != null) {
            qlkj = qlkRankingSource.getQlkj();
            qlkm = qlkRankingSource.getQlkm();
            qlks = qlkRankingSource.getQlks();
            qlkh = qlkRankingSource.getQlkh();
            qlkt = qlkRankingSource.getQlkt();
        }
        if (wearEquips != null && wearEquips.size() > 0) {
            for (int i = wearEquips.size() - 1; i >= 0; --i) {
                final WearEquip wearEquip = wearEquips.get(i);
                qlkj += (int)CountUtils.countEquipPro(wearEquip, PRO_DICT_TYPE.Q_KJ.getDist());
                qlkm += (int)CountUtils.countEquipPro(wearEquip, PRO_DICT_TYPE.Q_KM.getDist());
                qlks += (int)CountUtils.countEquipPro(wearEquip, PRO_DICT_TYPE.Q_KS.getDist());
                qlkh += (int)CountUtils.countEquipPro(wearEquip, PRO_DICT_TYPE.Q_KH.getDist());
                qlkt += (int)CountUtils.countEquipPro(wearEquip, PRO_DICT_TYPE.Q_KT.getDist());
            }
        }
        if (tianFuMap != null && tianFuMap.size() > 0) {
            qlkj += (int)(TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KJ.getDist()), qlkj) * 1000.0);
            qlkm += (int)(TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KM.getDist()), qlkm) * 1000.0);
            qlks += (int)(TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KS.getDist()), qlks) * 1000.0);
            qlkh += (int)(TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KH.getDist()), qlkh) * 1000.0);
            qlkt += (int)(TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KT.getDist()), qlkt) * 1000.0);
        }
        return new int[] { qlkj, qlkm, qlks, qlkh, qlkt };
    }
    
    public static WeaponsRankingSource createWeaponsRankingSource(final Role role, final WearEquip wearEquip, int score) {
        final Optional<FiedEquip> optional = wearEquip.getField().stream().filter(fiedEquip -> fiedEquip.getType().equals(6679)).findAny();
        if (optional.isPresent()) {
            final Integer value = optional.get().getValue();
            if (value != null) {
                score = value;
                final WeaponsRankingSource weaponsRankingSource = new WeaponsRankingSource(wearEquip, role, score);
                return weaponsRankingSource;
            }
        }
        final WeaponsRankingSource weaponsRankingSource2 = new WeaponsRankingSource(wearEquip, role, 0);
        return weaponsRankingSource2;
    }
}
