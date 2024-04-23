package com.kitty.system.game.ranking.util;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import com.kitty.system.game.ranking.pojo.WeaponsRankingSource;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.FiedEquip;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections.map.HashedMap;
import org.nutz.lang.util.NutMap;

public class RankingUtils {
  public static Map<String, Integer> getTotalRepair(Role role, List<Pet> pets, List<WearEquip> wearEquips) {
    HashedMap hashedMap = new HashedMap();
    int[] skillScores = { 0 };
    role.getSkills().values().stream().forEach(roleSkill -> skillScores[0] = skillScores[0] + BigDecimalUtil.decimalScaleIntegerDown(0, roleSkill.getProficient() / 100));
    int skillScore = BigDecimalUtil.decimalScaleIntegerDown(0, BigDecimalUtil.multiply(String.valueOf(skillScores[0]), String.valueOf(2.5D)));
    hashedMap.put("skillScore", skillScore);
    int[] petScores = { 0 };
    if (pets != null)
      pets.stream().forEach(pet -> {
            pet.doAfterInit();
            petScores[0] = petScores[0] + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), pet.getScore());
          }); 
    int petScore = petScores[0];
    hashedMap.put("petScore", petScore);
    int[] wearEquipScores = { 0 };
    if (wearEquips != null)
      wearEquips.stream().forEach(wearEquip -> {
            wearEquip.doAfterInit();
            // Optional<FiedEquip> optional = wearEquip.getField().stream().filter(()).findAny();
            Optional<FiedEquip> optional = wearEquip.getField().stream().filter((fiedEquip) -> {
              return fiedEquip.getValue().equals(6679);
            }).findAny();
            if (optional.isPresent()) {
              Integer value = optional.get().getValue();
              if (value != null)
                wearEquipScores[0] = wearEquipScores[0] + value;
            } 
          }); 
    int wearEquipScore = wearEquipScores[0];
    hashedMap.put("wearEquipScore", Integer.valueOf(wearEquipScore));
    return (Map<String, Integer>)hashedMap;
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
    if (wearEquips != null && wearEquips.size() > 0)
      for (int i = wearEquips.size() - 1; i >= 0; i--) {
        WearEquip wearEquip = wearEquips.get(i);
        qlkj = (int)(qlkj + CountUtils.countEquipPro(wearEquip, Integer.valueOf(PRO_DICT_TYPE.Q_KJ.getDist())));
        qlkm = (int)(qlkm + CountUtils.countEquipPro(wearEquip, Integer.valueOf(PRO_DICT_TYPE.Q_KM.getDist())));
        qlks = (int)(qlks + CountUtils.countEquipPro(wearEquip, Integer.valueOf(PRO_DICT_TYPE.Q_KS.getDist())));
        qlkh = (int)(qlkh + CountUtils.countEquipPro(wearEquip, Integer.valueOf(PRO_DICT_TYPE.Q_KH.getDist())));
        qlkt = (int)(qlkt + CountUtils.countEquipPro(wearEquip, Integer.valueOf(PRO_DICT_TYPE.Q_KT.getDist())));
      }  
    if (tianFuMap != null && tianFuMap.size() > 0) {
      qlkj = (int)(qlkj + TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KJ.getDist()), qlkj) * 1000.0D);
      qlkm = (int)(qlkm + TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KM.getDist()), qlkm) * 1000.0D);
      qlks = (int)(qlks + TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KS.getDist()), qlks) * 1000.0D);
      qlkh = (int)(qlkh + TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KH.getDist()), qlkh) * 1000.0D);
      qlkt = (int)(qlkt + TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(PRO_DICT_TYPE.Q_KT.getDist()), qlkt) * 1000.0D);
    } 
    return new int[] { qlkj, qlkm, qlks, qlkh, qlkt };
  }
  
  public static WeaponsRankingSource createWeaponsRankingSource(Role role, WearEquip wearEquip, int score) {
    Optional<FiedEquip> optional = wearEquip.getField().stream().filter(fiedEquip -> fiedEquip.getType().equals(Integer.valueOf(6679))).findAny();
    if (optional.isPresent()) {
      Integer value = ((FiedEquip)optional.get()).getValue();
      if (value != null) {
        score = value.intValue();
        WeaponsRankingSource weaponsRankingSource1 = new WeaponsRankingSource(wearEquip, role, score);
        return weaponsRankingSource1;
      } 
    } 
    WeaponsRankingSource weaponsRankingSource = new WeaponsRankingSource(wearEquip, role, 0);
    return weaponsRankingSource;
  }
}
