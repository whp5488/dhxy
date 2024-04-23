package com.kitty.system.game.ranking.service;

import com.kitty.cache.JedisUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import com.kitty.system.game.activity.model.user.ca.vo.CaCache;
import com.kitty.system.game.activity.model.user.lu.WaterRankRecord;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.message.ReqCaNomRanking;
import com.kitty.system.game.ranking.message.RespCaNomRanking;
import com.kitty.system.game.ranking.message.RespMaterNomRanking;
import com.kitty.system.game.ranking.message.RespMaterTeamRanking;
import com.kitty.system.game.ranking.pojo.CaRank;
import com.kitty.system.game.ranking.pojo.MaterRank;
import com.kitty.system.game.ranking.pojo.MaterTeamRank;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.init.domain.Tgroups;
import com.kitty.system.init.pool.PartDataPool;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DuelRankingService {
  public static final int LIMIT = 100;
  
  @Autowired
  RoleService roleService;
  
  @Autowired
  EquipService equipService;
  
  public void getMaterNomRanking(IoSession session) {
    List<WaterRankRecord> list = (List<WaterRankRecord>)RankingList.WATER_RANK_RECORDS.values().stream().sorted(Comparator.comparing(WaterRankRecord::getScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getEquipScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getGrade, Comparator.reverseOrder())).collect(Collectors.toList());
    if (list.size() > 100)
      list = list.subList(0, 100); 
    RespMaterNomRanking respMaterNomRanking = new RespMaterNomRanking();
    for (WaterRankRecord record : list) {
      MaterRank rank = setMaterRank(this.roleService.getPlayer(record.getRId()), record.getScore());
      if (rank != null)
        respMaterNomRanking.getList().add(rank); 
    } 
    MessagePusher.pushMessage(session, (Message)respMaterNomRanking);
  }
  
  public void getMaterTeamRanking(IoSession session) {
    RespMaterTeamRanking materTeamRanking = new RespMaterTeamRanking();
    List<WaterRankRecord> list = (List<WaterRankRecord>)RankingList.WATER_RANK_RECORDS.values().stream().sorted(Comparator.comparing(WaterRankRecord::getScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getEquipScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getGrade, Comparator.reverseOrder())).collect(Collectors.toList());
    if (list.size() > 0) {
      Map<Long, MaterTeamRank> map = new HashMap<>(100);
      for (WaterRankRecord record : list) {
        Role role = this.roleService.getPlayer(record.getRId());
        if (role == null)
          continue; 
        MaterTeamRank materTeamRank = map.get(Long.valueOf(record.getLeader()));
        if (materTeamRank == null) {
          materTeamRank = new MaterTeamRank();
          MaterRank materRank = setMaterRank(this.roleService.getPlayer(record.getLeader()), record.getScore());
          if (materRank != null) {
            materTeamRank.getList().add(materRank);
            materTeamRank.setRestsScore(record.getEquipScore() + record.getGrade());
          } 
        } 
        if (!role.getId().equals(Long.valueOf(record.getLeader())))
          continue; 
        MaterRank rank = setMaterRank(this.roleService.getPlayer(record.getLeader()), record.getScore());
        if (rank != null)
          materTeamRank.getList().add(rank); 
        double average = materTeamRank.getList().stream().mapToInt(MaterRank::getScore).average().getAsDouble();
        materTeamRank.setScore((int)average);
        materTeamRank.setRestsScore(materTeamRank.getRestsScore() + record.getEquipScore() + record.getGrade());
      } 
      List<MaterTeamRank> collect = (List<MaterTeamRank>)map.values().stream().sorted(Comparator.comparing(MaterTeamRank::getScore, Comparator.reverseOrder()).thenComparing(MaterTeamRank::getRestsScore, Comparator.reverseOrder())).collect(Collectors.toList());
      if (collect.size() > 100)
        collect = collect.subList(0, 100); 
      materTeamRanking.getList().addAll(collect);
    } 
    MessagePusher.pushMessage(session, (Message)materTeamRanking);
  }
  
  public MaterRank setMaterRank(Role role, int score) {
    if (role != null) {
      List<WearEquip> wearEquips = this.equipService.getEquipValid(role, false);
      int gem = 0;
      if (wearEquips.size() > 0)
        gem = DataGlobalFunc.getWeaponGemNum(wearEquips); 
      int weapon = role.getWeaponIcon();
      List<Integer> lightEffect = this.equipService.getWearEffect(role);
      List<FiedValue> list = this.roleService.deyList(role.getColor());
      return new MaterRank(role.getRoleId().intValue(), role.getName(), role.getSerial().intValue(), role.getRelive().byteValue(), role.getLevel().shortValue(), score, weapon, gem, lightEffect, new ArrayList(), list);
    } 
    return null;
  }
  
  public void getCaNomRanking(IoSession session, ReqCaNomRanking reqCaNomRanking) {
    RespCaNomRanking caNomRanking = new RespCaNomRanking();
    caNomRanking.setType(reqCaNomRanking.getType());
    caNomRanking.setClassify(reqCaNomRanking.getClassify());
    int minGrade = 0;
    int maxGrade = 0;
    if (reqCaNomRanking.getType() == 3) {
      maxGrade = 2000;
    } else if (reqCaNomRanking.getType() == 2) {
      minGrade = 2000;
      maxGrade = 3000;
    } else if (reqCaNomRanking.getType() == 1) {
      minGrade = 3120;
      maxGrade = 4000;
    } else {
      minGrade = 3000;
      maxGrade = 3120;
    } 
    int finalMinGrade = minGrade;
    int finalMaxGrade = maxGrade;
    if (reqCaNomRanking.getClassify() == 0) {
      List<CaRankRecord> collect = (List<CaRankRecord>)RankingList.CA_RANK_RECORDS.values().stream().filter(e -> (e.getGrade() >= finalMinGrade && e.getGrade() < finalMaxGrade)).sorted(Comparator.comparing(CaRankRecord::getScore, Comparator.reverseOrder()).thenComparing(CaRankRecord::getGrade, Comparator.reverseOrder()).thenComparing(CaRankRecord::getEquipScore, Comparator.reverseOrder())).collect(Collectors.toList());
      if (collect.size() > 100)
        collect = collect.subList(0, 100); 
      for (CaRankRecord record : collect)
        caNomRanking.getList().add(new CaRank(record)); 
    } else if (reqCaNomRanking.getClassify() == 1) {
      List<CaRank> list = new ArrayList<>();
      Set<String> keys = JedisUtil.keys("jzca:*");
      if (keys.size() > 0)
        return; 
      for (String key : keys) {
        String value = JedisUtil.get(key);
        if (value != null)
          try {
            CaCache caCache = (CaCache)JsonUtils.string2Object(value, CaCache.class);
            if (caCache.getGrade() >= finalMinGrade && caCache.getGrade() < finalMaxGrade) {
              Tgroups tgroups = (Tgroups)PartDataPool.tgroupsMap.get(caCache.getMarking());
              if (tgroups == null)
                continue; 
              list.add(new CaRank(caCache, tgroups.getGroupsName()));
            } 
          } catch (Exception e) {
            e.printStackTrace();
          }  
      } 
      list = (List<CaRank>)list.stream().sorted(Comparator.comparing(CaRank::getScore, Comparator.reverseOrder()).thenComparing(CaRank::getRelive, Comparator.reverseOrder()).thenComparing(CaRank::getLevel, Comparator.reverseOrder())).collect(Collectors.toList());
      if (list.size() > 100)
        list = list.subList(0, 100); 
      caNomRanking.setList(list);
    } 
    MessagePusher.pushMessage(session, (Message)caNomRanking);
  }
}
