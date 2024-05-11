
package com.kitty.system.game.ranking.service;

import java.util.Set;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.Tgroups;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.activity.model.user.ca.vo.CaCache;
import com.kitty.cache.JedisUtil;
import com.kitty.system.game.ranking.pojo.CaRank;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import com.kitty.system.game.ranking.message.RespCaNomRanking;
import com.kitty.system.game.ranking.message.ReqCaNomRanking;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.equip.domain.WearEquip;
import java.util.ArrayList;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.role.domain.Role;
import java.util.Map;
import java.util.Collection;
import com.kitty.system.game.ranking.pojo.MaterTeamRank;
import java.util.HashMap;
import com.kitty.system.game.ranking.message.RespMaterTeamRanking;
import com.kitty.system.game.ranking.pojo.MaterRank;
import java.util.Iterator;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.message.RespMaterNomRanking;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.activity.model.user.lu.WaterRankRecord;
import java.util.List;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.equip.service.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class DuelRankingService
{
    public static final int LIMIT = 100;
    @Autowired
    RoleService roleService;
    @Autowired
    EquipService equipService;
    
    public void getMaterNomRanking(final IoSession session) {
        List<WaterRankRecord> list = RankingList.WATER_RANK_RECORDS.values().stream().sorted(Comparator.comparing(WaterRankRecord::getScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getEquipScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getGrade, Comparator.reverseOrder())).collect(Collectors.toList());
        if (list.size() > 100) {
            list = list.subList(0, 100);
        }
        final RespMaterNomRanking respMaterNomRanking = new RespMaterNomRanking();
        for (final WaterRankRecord record : list) {
            final MaterRank rank = this.setMaterRank(this.roleService.getPlayer(record.getRId()), record.getScore());
            if (rank != null) {
                respMaterNomRanking.getList().add(rank);
            }
        }
        MessagePusher.pushMessage(session, respMaterNomRanking);
    }
    
    public void getMaterTeamRanking(final IoSession session) {
        final RespMaterTeamRanking materTeamRanking = new RespMaterTeamRanking();
        final List<WaterRankRecord> list = RankingList.WATER_RANK_RECORDS.values().stream().sorted(Comparator.comparing(WaterRankRecord::getScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getEquipScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getGrade, Comparator.reverseOrder())).collect(Collectors.toList());
        if (list.size() > 0) {
            final Map<Long, MaterTeamRank> map = new HashMap<Long, MaterTeamRank>(100);
            for (final WaterRankRecord record : list) {
                final Role role = this.roleService.getPlayer(record.getRId());
                if (role == null) {
                    continue;
                }
                MaterTeamRank materTeamRank = map.get(record.getLeader());
                if (materTeamRank == null) {
                    materTeamRank = new MaterTeamRank();
                    final MaterRank rank = this.setMaterRank(this.roleService.getPlayer(record.getLeader()), record.getScore());
                    if (rank != null) {
                        materTeamRank.getList().add(rank);
                        materTeamRank.setRestsScore(record.getEquipScore() + record.getGrade());
                    }
                }
                if (!role.getId().equals(record.getLeader())) {
                    continue;
                }
                final MaterRank rank = this.setMaterRank(this.roleService.getPlayer(record.getLeader()), record.getScore());
                if (rank != null) {
                    materTeamRank.getList().add(rank);
                }
                final double average = materTeamRank.getList().stream().mapToInt(MaterRank::getScore).average().getAsDouble();
                materTeamRank.setScore((int)average);
                materTeamRank.setRestsScore(materTeamRank.getRestsScore() + record.getEquipScore() + record.getGrade());
            }
            List<MaterTeamRank> collect = map.values().stream().sorted(Comparator.comparing(MaterTeamRank::getScore, Comparator.reverseOrder()).thenComparing(MaterTeamRank::getRestsScore, Comparator.reverseOrder())).collect(Collectors.toList());
            if (collect.size() > 100) {
                collect = collect.subList(0, 100);
            }
            materTeamRanking.getList().addAll(collect);
        }
        MessagePusher.pushMessage(session, materTeamRanking);
    }
    
    public MaterRank setMaterRank(final Role role, final int score) {
        if (role != null) {
            final List<WearEquip> wearEquips = this.equipService.getEquipValid(role, false);
            int gem = 0;
            if (wearEquips.size() > 0) {
                gem = DataGlobalFunc.getWeaponGemNum(wearEquips);
            }
            final int weapon = role.getWeaponIcon();
            final List<Integer> lightEffect = this.equipService.getWearEffect(role);
            final List<FiedValue> list = this.roleService.deyList(role.getColor());
            return new MaterRank(role.getRoleId(), role.getName(), role.getSerial(), role.getRelive().byteValue(), role.getLevel().shortValue(), score, weapon, gem, lightEffect, new ArrayList<Integer>(), list);
        }
        return null;
    }
    
    public void getCaNomRanking(final IoSession session, final ReqCaNomRanking reqCaNomRanking) {
        final RespCaNomRanking caNomRanking = new RespCaNomRanking();
        caNomRanking.setType(reqCaNomRanking.getType());
        caNomRanking.setClassify(reqCaNomRanking.getClassify());
        int minGrade = 0;
        int maxGrade = 0;
        if (reqCaNomRanking.getType() == 3) {
            maxGrade = 2000;
        }
        else if (reqCaNomRanking.getType() == 2) {
            minGrade = 2000;
            maxGrade = 3000;
        }
        else if (reqCaNomRanking.getType() == 1) {
            minGrade = 3120;
            maxGrade = 4000;
        }
        else {
            minGrade = 3000;
            maxGrade = 3120;
        }
        final int finalMinGrade = minGrade;
        final int finalMaxGrade = maxGrade;
        if (reqCaNomRanking.getClassify() == 0) {
            List<CaRankRecord> collect = RankingList.CA_RANK_RECORDS.values().stream().filter(e -> e.getGrade() >= finalMinGrade && e.getGrade() < finalMaxGrade).sorted(Comparator.comparing(CaRankRecord::getScore, Comparator.reverseOrder()).thenComparing(CaRankRecord::getGrade, Comparator.reverseOrder()).thenComparing(CaRankRecord::getEquipScore, Comparator.reverseOrder())).collect(Collectors.toList());
            if (collect.size() > 100) {
                collect = collect.subList(0, 100);
            }
            for (final CaRankRecord record : collect) {
                caNomRanking.getList().add(new CaRank(record));
            }
        }
        else if (reqCaNomRanking.getClassify() == 1) {
            List<CaRank> list = new ArrayList<CaRank>();
            final Set<String> keys = JedisUtil.keys("jzca:*");
            if (keys.size() > 0) {
                return;
            }
            for (final String key : keys) {
                final String value = JedisUtil.get(key);
                if (value != null) {
                    try {
                        final CaCache caCache = JsonUtils.string2Object(value, CaCache.class);
                        if (caCache.getGrade() < finalMinGrade || caCache.getGrade() >= finalMaxGrade) {
                            continue;
                        }
                        final Tgroups tgroups = PartDataPool.tgroupsMap.get(caCache.getMarking());
                        if (tgroups == null) {
                            continue;
                        }
                        list.add(new CaRank(caCache, tgroups.getGroupsName()));
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            list = list.stream().sorted(Comparator.comparing(CaRank::getScore, Comparator.reverseOrder()).thenComparing(CaRank::getRelive, Comparator.reverseOrder()).thenComparing(CaRank::getLevel, Comparator.reverseOrder())).collect(Collectors.toList());
            if (list.size() > 100) {
                list = list.subList(0, 100);
            }
            caNomRanking.setList(list);
        }
        MessagePusher.pushMessage(session, caNomRanking);
    }
}
