package com.kitty.system.game.ranking.service;

import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.message.RespWeaponsRankingList;
import com.kitty.system.game.ranking.pojo.field.WeaponsRankingField;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import com.kitty.system.game.ranking.util.RankingUtils;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.ranking.pojo.ComprehensiveStrengthSource;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import com.kitty.system.game.ranking.pojo.WeaponsRankingSource;
import com.kitty.listener.event.ChangeEquipEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import com.kitty.system.game.ranking.RankingList;
import java.util.List;
import com.kitty.listener.event.ChangeRuneWearEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class WeaponsRankingService
{
    @Autowired
    RoleService roleService;
    
    public WeaponsRankingService() {
    }
    
    @EventHandler({ EventType.CHANGE_RUNE_WEAR })
    public void handleChangeRuneEvent(ChangeRuneWearEvent runeWearEvent) {
        Role role = runeWearEvent.getRole();
        if (role == null) {
            return;
        }
        List<WearEquip> wearEquips = (List)RankingList.wearEquipMaps.get(role.getId());
        if (wearEquips == null) {
            wearEquips = new ArrayList<>();
        }
        this.wearEquipQlkChage(role, wearEquips);
        return;
    }
    
    @EventHandler({ EventType.CHANGE_EQUIP_WEAR })
    public void handleChangeEquipEvent(ChangeEquipEvent changeEquipEvent) {
        Role role = changeEquipEvent.getRole();
        WearEquip wearEquip = changeEquipEvent.getWearEquip();
        if (role == null || wearEquip == null) {
            return;
        }
        List<WearEquip> wearEquips = (List)RankingList.wearEquipMaps.get(role.getId());
        if (wearEquips == null || !wearEquips.contains(wearEquip)) {
            if (wearEquips == null) {
                wearEquips = new ArrayList<>();
            }
            synchronized (wearEquips) {
                wearEquips.add(wearEquip);
            }
            synchronized (RankingList.wearEquips) {
                RankingList.wearEquips.add(wearEquip);
            }
        }
        else {
            wearEquips.set(wearEquips.indexOf(wearEquip), wearEquip);
        }
        if (wearEquip.isDelete()) {
            wearEquips.remove(wearEquip);
            synchronized (RankingList.wearEquips) {
                RankingList.wearEquips.remove(wearEquip);
            }
            WeaponsRankingSource weaponsRankingSource = new WeaponsRankingSource(wearEquip.getId());
            RankingList.weaponsRankingSources.remove(weaponsRankingSource);
            QlkRankingSource qlkRankingSource = new QlkRankingSource((int)role.getRoleId());
            RankingList.qlkRankingSources.remove(qlkRankingSource);
            ComprehensiveStrengthSource comprehensiveStrengthSource = new ComprehensiveStrengthSource((int)role.getRoleId());
            RankingList.comprehensiveStrengthSources.remove(comprehensiveStrengthSource);
        }
        this.wearEquipScoreChage(role, wearEquips, wearEquip);
        this.wearEquipQlkChage(role, wearEquips);
        this.wearEquipScoreChage(role, wearEquip);
        return;
    }
    
    private void wearEquipScoreChage(Role role, List<WearEquip> wearEquips, WearEquip wearEquip) {
        FiedEquip wearEquipScore = (FiedEquip)wearEquip.getField().stream().filter(obj -> obj.getType().equals(Integer.valueOf(6679))).findFirst().orElse(null);
        if (wearEquipScore == null) {
            return;
        }
        int[] wearEquipScores = { 0 };
        wearEquips.stream().forEach(obj -> {
            if (!obj.equals(wearEquip)) {
                Optional<FiedEquip> optional2 = obj.getField().stream().filter(fiedEquip -> fiedEquip.getType().equals(Integer.valueOf(6679))).findAny();
                if (optional2.isPresent()) {
                    Integer value = ((FiedEquip)optional2.get()).getValue();
                    if (value != null) {
                        wearEquipScores[0] += (int)value;
                    }
                }
            }
            else if (wearEquipScore.getValue() != null) {
                wearEquipScores[0] += (int)wearEquipScore.getValue();
            }
            return;
        });
        int score = wearEquipScores[0];
        Optional<ComprehensiveStrengthSource> optional;
        synchronized (RankingList.comprehensiveStrengthSources) {
            optional = RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId()).findAny();
        }
        if (optional != null && optional.isPresent()) {
            ComprehensiveStrengthSource comprehensiveStrengthSource = (ComprehensiveStrengthSource)optional.get();
            if (comprehensiveStrengthSource.getWearEquipScore() != score) {
                comprehensiveStrengthSource.setWearEquipScore(score);
            }
        }
        else {
            Map<String, Integer> totalRepairMap = RankingUtils.getTotalRepair(role, (List)RankingList.petMaps.get(role.getRoleId()), (List)RankingList.wearEquipMaps.get(role.getRoleId()));
            ComprehensiveStrengthSource css = new ComprehensiveStrengthSource(role, totalRepairMap);
            RankingList.comprehensiveStrengthSources.add(css);
        }
        return;
    }
    
    private void wearEquipQlkChage(Role role, List<WearEquip> wearEquips) {
        QlkRankingSource qlkRankingSource = null;
        synchronized (RankingList.qlkRankingSources) {
            qlkRankingSource = (QlkRankingSource)RankingList.qlkRankingSources.stream().filter(obj -> role.getRoleId().equals(Integer.valueOf(obj.getRoleId()))).findFirst().orElse(null);
        }
        if (qlkRankingSource != null) {
            if (wearEquips.size() > 0) {
                int[] roleQlkWuxing = RankingUtils.getRoleQlkWuxing(role, wearEquips, null);
                if (roleQlkWuxing[0] != qlkRankingSource.getQlkj()) {
                    qlkRankingSource.setQlkj(roleQlkWuxing[0]);
                }
                if (roleQlkWuxing[1] != qlkRankingSource.getQlkm()) {
                    qlkRankingSource.setQlkm(roleQlkWuxing[1]);
                }
                if (roleQlkWuxing[2] != qlkRankingSource.getQlks()) {
                    qlkRankingSource.setQlks(roleQlkWuxing[2]);
                }
                if (roleQlkWuxing[3] != qlkRankingSource.getQlkh()) {
                    qlkRankingSource.setQlkh(roleQlkWuxing[3]);
                }
                if (roleQlkWuxing[4] != qlkRankingSource.getQlkt()) {
                    qlkRankingSource.setQlkt(roleQlkWuxing[4]);
                }
            }
        }
        else {
            int[] roleQlkWuxing = RankingUtils.getRoleQlkWuxing(role, wearEquips, null);
            qlkRankingSource = new QlkRankingSource(role, roleQlkWuxing);
            RankingList.qlkRankingSources.add(qlkRankingSource);
        }
    }
    
    private void wearEquipScoreChage(Role role, WearEquip wearEquip) {
        WeaponsRankingSource weaponsRankingSource = null;
        synchronized (RankingList.weaponsRankingSources) {
            weaponsRankingSource = (WeaponsRankingSource)RankingList.weaponsRankingSources.stream().filter(obj -> obj.getWearEquipId().equals(wearEquip.getId())).findFirst().orElse(null);
        }
        if (weaponsRankingSource != null) {
            FiedEquip fiedEquip = (FiedEquip)wearEquip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6679))).findFirst().orElse(null);
            if (fiedEquip != null && !fiedEquip.getValue().equals(Integer.valueOf(weaponsRankingSource.getScore()))) {
                weaponsRankingSource.setScore((int)fiedEquip.getValue());
            }
        }
        else {
            weaponsRankingSource = RankingUtils.createWeaponsRankingSource(role, wearEquip, 0);
            RankingList.weaponsRankingSources.add(weaponsRankingSource);
        }
    }
    
    public void getWeaponsRankingList(Role role, byte race, byte equipType) {
        List<WeaponsRankingSource> weaponsRankingSources = RankingList.weaponsRankingSources;
        if (weaponsRankingSources == null || weaponsRankingSources.size() <= 0) {
            MessagePusher.pushMessage(role, new RespWeaponsRankingList(race, equipType, new ArrayList<>()));
            return;
        }
        int count = 100;
        List<WeaponsRankingSource> collect = (List)weaponsRankingSources.stream().sorted(Comparator.comparing(WeaponsRankingSource::getScore).reversed()).collect(Collectors.toList());
        List<WeaponsRankingSource> limitWeaponsRankingSources;
        if (race == 0) {
            limitWeaponsRankingSources = (List)collect.stream().filter(obj -> obj.getEquipType() == equipType).limit((long)count).collect(Collectors.toList());
        }
        else {
            limitWeaponsRankingSources = (List)collect.stream().filter(obj -> obj.getEquipType() == equipType && obj.getRace() == race).limit((long)count).collect(Collectors.toList());
        }
        int[] index = { 0 };
        List<WeaponsRankingField> list = (List)limitWeaponsRankingSources.stream().map(weapons -> {
            WeaponsRankingField weaponsRankingField = new WeaponsRankingField(weapons, ++index[0], (byte)0);
            return weaponsRankingField;
        }).collect(Collectors.toList());
        MessagePusher.pushMessage(role, new RespWeaponsRankingList(race, equipType, list));
        return;
    }
    
    public void getMyWeaponsRanking(Role role, byte type, int serial, byte serverId) {
        List<WeaponsRankingSource> equips;
        synchronized (RankingList.weaponsRankingSources) {
            equips = (List)RankingList.weaponsRankingSources.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId()).collect(Collectors.toList());
        }
        if (equips == null || equips.size() <= 0) {
            MessagePusher.pushMessage(role, new RespMyRanking(type, serial, serverId, null));
            return;
        }
        Optional<WeaponsRankingSource> optional = equips.stream().max(Comparator.comparing(WeaponsRankingSource::getScore));
        if (optional.isPresent()) {
            WeaponsRankingSource weaponsRankingSource = (WeaponsRankingSource)optional.get();
            int index = 1;
            index = RankingList.weaponsRankingSources.indexOf(weaponsRankingSource);
            if (index == -1) {
                MessagePusher.pushMessage(role, new RespMyRanking(type, serial, serverId, null));
                return;
            }
            WeaponsRankingField weaponsRankingField = new WeaponsRankingField(weaponsRankingSource, ++index, serverId);
            MessagePusher.pushMessage(role, new RespMyRanking(type, serial, serverId, weaponsRankingField));
        }
        return;
    }
}
