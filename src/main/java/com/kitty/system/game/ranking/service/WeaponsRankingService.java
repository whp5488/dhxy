package com.kitty.system.game.ranking.service;

import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.ChangeEquipEvent;
import com.kitty.listener.event.ChangeRuneWearEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.system.game.ranking.message.RespWeaponsRankingList;
import com.kitty.system.game.ranking.pojo.ComprehensiveStrengthSource;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import com.kitty.system.game.ranking.pojo.RankingField;
import com.kitty.system.game.ranking.pojo.WeaponsRankingSource;
import com.kitty.system.game.ranking.pojo.field.WeaponsRankingField;
import com.kitty.system.game.ranking.util.RankingUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeaponsRankingService {
  @Autowired
  RoleService roleService;
  
  @EventHandler({EventType.CHANGE_RUNE_WEAR})
  public void handleChangeRuneEvent(ChangeRuneWearEvent runeWearEvent) {
    Role role = runeWearEvent.getRole();
    if (role == null)
      return; 
    List<WearEquip> wearEquips = (List<WearEquip>)RankingList.wearEquipMaps.get(role.getId());
    if (wearEquips == null)
      wearEquips = new ArrayList<>(); 
    wearEquipQlkChage(role, wearEquips);
  }
  
  @EventHandler({EventType.CHANGE_EQUIP_WEAR})
  public void handleChangeEquipEvent(ChangeEquipEvent changeEquipEvent) {
    Role role = changeEquipEvent.getRole();
    WearEquip wearEquip = changeEquipEvent.getWearEquip();
    if (role == null || wearEquip == null)
      return; 
    List<WearEquip> wearEquips = (List<WearEquip>)RankingList.wearEquipMaps.get(role.getId());
    if (wearEquips == null || !wearEquips.contains(wearEquip)) {
      if (wearEquips == null)
        wearEquips = new ArrayList<>(); 
      synchronized (wearEquips) {
        wearEquips.add(wearEquip);
      } 
      synchronized (RankingList.wearEquips) {
        RankingList.wearEquips.add(wearEquip);
      } 
    } else {
      wearEquips.set(wearEquips.indexOf(wearEquip), wearEquip);
    } 
    if (wearEquip.isDelete()) {
      wearEquips.remove(wearEquip);
      synchronized (RankingList.wearEquips) {
        RankingList.wearEquips.remove(wearEquip);
      } 
      WeaponsRankingSource weaponsRankingSource = new WeaponsRankingSource(wearEquip.getId());
      RankingList.weaponsRankingSources.remove(weaponsRankingSource);
      QlkRankingSource qlkRankingSource = new QlkRankingSource(role.getRoleId().intValue());
      RankingList.qlkRankingSources.remove(qlkRankingSource);
      ComprehensiveStrengthSource comprehensiveStrengthSource = new ComprehensiveStrengthSource(role.getRoleId().intValue());
      RankingList.comprehensiveStrengthSources.remove(comprehensiveStrengthSource);
    } 
    wearEquipScoreChage(role, wearEquips, wearEquip);
    wearEquipQlkChage(role, wearEquips);
    wearEquipScoreChage(role, wearEquip);
  }
  
  private void wearEquipScoreChage(Role role, List<WearEquip> wearEquips, WearEquip wearEquip) {

    FiedEquip wearEquipScore = wearEquip.getField().stream().filter(obj -> obj.getType().equals(Integer.valueOf(6679))).findFirst().orElse(null);
    if (wearEquipScore == null)
      return; 
    int[] wearEquipScores = { 0 };
    wearEquips.stream().forEach(obj -> {
          if (!obj.equals(wearEquip)) {
            //Optional<FiedEquip> optional = obj.getField().stream().filter(()).findAny();
            Optional<FiedEquip> optional = obj.getField().stream().filter((fiedEquip) -> {
              return fiedEquip.getType().equals(6679);
            }).findAny();
            if (optional.isPresent()) {
              Integer value = ((FiedEquip)optional.get()).getValue();
              if (value != null)
                wearEquipScores[0] = wearEquipScores[0] + value.intValue(); 
            } 
          } else if (wearEquipScore.getValue() != null) {
            wearEquipScores[0] = wearEquipScores[0] + wearEquipScore.getValue().intValue();
          } 
        });
    int score = wearEquipScores[0];
    Optional<ComprehensiveStrengthSource> optional;
    synchronized (RankingList.comprehensiveStrengthSources) {
      optional = RankingList.comprehensiveStrengthSources.stream().filter(obj -> (obj.getRoleId() == role.getRoleId().intValue())).findAny();
    } 
    if (optional != null && optional.isPresent()) {
      ComprehensiveStrengthSource comprehensiveStrengthSource = optional.get();
      if (comprehensiveStrengthSource.getWearEquipScore() != score)
        comprehensiveStrengthSource.setWearEquipScore(score); 
    } else {
      Map<String, Integer> totalRepairMap = RankingUtils.getTotalRepair(role, (List)RankingList.petMaps.get(role.getRoleId()), (List)RankingList.wearEquipMaps.get(role.getRoleId()));
      ComprehensiveStrengthSource css = new ComprehensiveStrengthSource(role, totalRepairMap);
      RankingList.comprehensiveStrengthSources.add(css);
    } 
  }
  
  private void wearEquipQlkChage(Role role, List<WearEquip> wearEquips) {
    QlkRankingSource qlkRankingSource = null;
    synchronized (RankingList.qlkRankingSources) {
      qlkRankingSource = RankingList.qlkRankingSources.stream().filter(obj -> role.getRoleId().equals(Integer.valueOf(obj.getRoleId()))).findFirst().orElse(null);
    } 
    if (qlkRankingSource != null) {
      if (wearEquips.size() > 0) {
        int[] roleQlkWuxing = RankingUtils.getRoleQlkWuxing(role, wearEquips, null);
        if (roleQlkWuxing[0] != qlkRankingSource.getQlkj())
          qlkRankingSource.setQlkj(roleQlkWuxing[0]); 
        if (roleQlkWuxing[1] != qlkRankingSource.getQlkm())
          qlkRankingSource.setQlkm(roleQlkWuxing[1]); 
        if (roleQlkWuxing[2] != qlkRankingSource.getQlks())
          qlkRankingSource.setQlks(roleQlkWuxing[2]); 
        if (roleQlkWuxing[3] != qlkRankingSource.getQlkh())
          qlkRankingSource.setQlkh(roleQlkWuxing[3]); 
        if (roleQlkWuxing[4] != qlkRankingSource.getQlkt())
          qlkRankingSource.setQlkt(roleQlkWuxing[4]); 
      } 
    } else {
      int[] roleQlkWuxing = RankingUtils.getRoleQlkWuxing(role, wearEquips, null);
      qlkRankingSource = new QlkRankingSource(role, roleQlkWuxing);
      RankingList.qlkRankingSources.add(qlkRankingSource);
    } 
  }
  
  private void wearEquipScoreChage(Role role, WearEquip wearEquip) {
    WeaponsRankingSource weaponsRankingSource = null;
    synchronized (RankingList.weaponsRankingSources) {
      weaponsRankingSource = RankingList.weaponsRankingSources.stream().filter(obj -> obj.getWearEquipId().equals(wearEquip.getId())).findFirst().orElse(null);
    } 
    if (weaponsRankingSource != null) {
      FiedEquip fiedEquip = wearEquip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6679))).findFirst().orElse(null);
      if (fiedEquip != null)
        if (!fiedEquip.getValue().equals(Integer.valueOf(weaponsRankingSource.getScore())))
          weaponsRankingSource.setScore(fiedEquip.getValue().intValue());  
    } else {
      weaponsRankingSource = RankingUtils.createWeaponsRankingSource(role, wearEquip, 0);
      RankingList.weaponsRankingSources.add(weaponsRankingSource);
    } 
  }
  
  public void getWeaponsRankingList(Role role, byte race, byte equipType) {
    List<WeaponsRankingSource> limitWeaponsRankingSources, weaponsRankingSources = RankingList.weaponsRankingSources;
    if (weaponsRankingSources == null || weaponsRankingSources.size() <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespWeaponsRankingList(race, equipType, new ArrayList()));
      return;
    } 
    int count = 100;
    List<WeaponsRankingSource> collect = (List<WeaponsRankingSource>)weaponsRankingSources.stream().sorted(Comparator.comparing(WeaponsRankingSource::getScore).reversed()).collect(Collectors.toList());
    if (race == 0) {
      limitWeaponsRankingSources = (List<WeaponsRankingSource>)collect.stream().filter(obj -> (obj.getEquipType() == equipType)).limit(count).collect(Collectors.toList());
    } else {
      limitWeaponsRankingSources = (List<WeaponsRankingSource>)collect.stream().filter(obj -> (obj.getEquipType() == equipType && obj.getRace() == race)).limit(count).collect(Collectors.toList());
    } 
    int[] index = { 0 };
    List<WeaponsRankingField> list = (List<WeaponsRankingField>)limitWeaponsRankingSources.stream().map(weapons -> new WeaponsRankingField(weapons, index[0] = index[0] + 1, (byte)0)).collect(Collectors.toList());
    MessagePusher.pushMessage(role, (Message)new RespWeaponsRankingList(race, equipType, list));
  }
  
  public void getMyWeaponsRanking(Role role, byte type, int serial, byte serverId) {
    List<WeaponsRankingSource> equips;
    synchronized (RankingList.weaponsRankingSources) {
      equips = (List<WeaponsRankingSource>)RankingList.weaponsRankingSources.stream().filter(obj -> (obj.getRoleId() == role.getRoleId().intValue())).collect(Collectors.toList());
    } 
    if (equips == null || equips.size() <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespMyRanking(type, serial, serverId, null));
      return;
    } 
    Optional<WeaponsRankingSource> optional = equips.stream().max(Comparator.comparing(WeaponsRankingSource::getScore));
    if (optional.isPresent()) {
      WeaponsRankingSource weaponsRankingSource = optional.get();
      int index = 1;
      index = RankingList.weaponsRankingSources.indexOf(weaponsRankingSource);
      if (index == -1) {
        MessagePusher.pushMessage(role, (Message)new RespMyRanking(type, serial, serverId, null));
        return;
      } 
      WeaponsRankingField weaponsRankingField = new WeaponsRankingField(weaponsRankingSource, ++index, serverId);
      MessagePusher.pushMessage(role, (Message)new RespMyRanking(type, serial, serverId, (RankingField)weaponsRankingField));
    } 
  }
}
