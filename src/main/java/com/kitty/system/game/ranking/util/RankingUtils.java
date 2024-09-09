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
    public RankingUtils() {
    }
    
    public static Map<String, Integer> getTotalRepair(Role role, List<Pet> pets, List<WearEquip> wearEquips) {
        Map<String, Integer> map = new HashedMap();
        int[] skillScores = { 0 };
        role.getSkills().values().stream().forEach(roleSkill -> {
            skillScores[0] += BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Integer.valueOf((int)roleSkill.getProficient() / 100));
            return;
        });
        int skillScore = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), BigDecimalUtil.multiply(String.valueOf(skillScores[0]), String.valueOf(2.5)));
        map.put("skillScore", Integer.valueOf(skillScore));
        int[] petScores = { 0 };
        if (pets != null) {
            pets.stream().forEach(pet -> {
                pet.doAfterInit();
                petScores[0] += BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), pet.getScore());
                return;
            });
        }
        int petScore = petScores[0];
        map.put("petScore", Integer.valueOf(petScore));
        int[] wearEquipScores = { 0 };
        if (wearEquips != null) {
            wearEquips.stream().forEach(wearEquip -> {
                wearEquip.doAfterInit();
                Optional<FiedEquip> optional = wearEquip.getField().stream().filter(fiedEquip -> fiedEquip.getValue().equals(Integer.valueOf(6679))).findAny();
                if (optional.isPresent()) {
                    Integer value = ((FiedEquip)optional.get()).getValue();
                    if (value != null) {
                        wearEquipScores[0] += (int)value;
                    }
                }
                return;
            });
        }
        int wearEquipScore = wearEquipScores[0];
        map.put("wearEquipScore", Integer.valueOf(wearEquipScore));
        return map;
    }
    
    public static int[] getRoleQlkWuxing(Role role, List<WearEquip> wearEquips, QlkRankingSource qlkRankingSource) {
        NutMap tianFuMap = TianFuUtils.countFuPlayerBookGain(role);
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
                WearEquip wearEquip = (WearEquip)wearEquips.get(i);
                qlkj = (int)((double)qlkj + CountUtils.countEquipPro(wearEquip, Integer.valueOf(PRO_DICT_TYPE.Q_KJ.getDist())));
                qlkm = (int)((double)qlkm + CountUtils.countEquipPro(wearEquip, Integer.valueOf(PRO_DICT_TYPE.Q_KM.getDist())));
                qlks = (int)((double)qlks + CountUtils.countEquipPro(wearEquip, Integer.valueOf(PRO_DICT_TYPE.Q_KS.getDist())));
                qlkh = (int)((double)qlkh + CountUtils.countEquipPro(wearEquip, Integer.valueOf(PRO_DICT_TYPE.Q_KH.getDist())));
                qlkt = (int)((double)qlkt + CountUtils.countEquipPro(wearEquip, Integer.valueOf(PRO_DICT_TYPE.Q_KT.getDist())));
            }
        }
        if (tianFuMap != null && tianFuMap.size() > 0) {
            qlkj = (int)((double)qlkj + TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KJ.getDist()), (double)qlkj) * 1000.0);
            qlkm = (int)((double)qlkm + TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KM.getDist()), (double)qlkm) * 1000.0);
            qlks = (int)((double)qlks + TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KS.getDist()), (double)qlks) * 1000.0);
            qlkh = (int)((double)qlkh + TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KH.getDist()), (double)qlkh) * 1000.0);
            qlkt = (int)((double)qlkt + TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KT.getDist()), (double)qlkt) * 1000.0);
        }
        return new int[] { qlkj, qlkm, qlks, qlkh, qlkt };
    }
    
    public static WeaponsRankingSource createWeaponsRankingSource(Role role, WearEquip wearEquip, int score) {
        Optional<FiedEquip> optional = wearEquip.getField().stream().filter(fiedEquip -> fiedEquip.getType().equals(Integer.valueOf(6679))).findAny();
        if (optional.isPresent()) {
            Integer value = ((FiedEquip)optional.get()).getValue();
            if (value != null) {
                score = (int)value;
                WeaponsRankingSource weaponsRankingSource = new WeaponsRankingSource(wearEquip, role, score);
                return weaponsRankingSource;
            }
        }
        WeaponsRankingSource weaponsRankingSource2 = new WeaponsRankingSource(wearEquip, role, 0);
        return weaponsRankingSource2;
    }
}
