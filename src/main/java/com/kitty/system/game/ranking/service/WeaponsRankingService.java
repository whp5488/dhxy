
package com.kitty.system.game.ranking.service;

import com.kitty.system.game.ranking.pojo.RankingField;
import com.kitty.system.game.ranking.message.RespMyRanking;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.message.RespWeaponsRankingList;
import com.kitty.system.game.ranking.pojo.field.WeaponsRankingField;
import java.util.Map;
import java.util.Optional;
import com.kitty.system.game.ranking.util.RankingUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.ranking.pojo.ComprehensiveStrengthSource;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import com.kitty.system.game.ranking.pojo.WeaponsRankingSource;
import com.kitty.listener.event.ChangeEquipEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.equip.domain.WearEquip;
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
    
    @EventHandler({ EventType.CHANGE_RUNE_WEAR })
    public void handleChangeRuneEvent(final ChangeRuneWearEvent runeWearEvent) {
        final Role role = runeWearEvent.getRole();
        if (role == null) {
            return;
        }
        List<WearEquip> wearEquips = RankingList.wearEquipMaps.get(role.getId());
        if (wearEquips == null) {
            wearEquips = new ArrayList<WearEquip>();
        }
        this.wearEquipQlkChage(role, wearEquips);
    }
    
    @EventHandler({ EventType.CHANGE_EQUIP_WEAR })
    public void handleChangeEquipEvent(final ChangeEquipEvent changeEquipEvent) {
        final Role role = changeEquipEvent.getRole();
        final WearEquip wearEquip = changeEquipEvent.getWearEquip();
        if (role == null || wearEquip == null) {
            return;
        }
        List<WearEquip> wearEquips = RankingList.wearEquipMaps.get(role.getId());
        if (wearEquips == null || !wearEquips.contains(wearEquip)) {
            if (wearEquips == null) {
                wearEquips = new ArrayList<WearEquip>();
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
            final WeaponsRankingSource weaponsRankingSource = new WeaponsRankingSource(wearEquip.getId());
            RankingList.weaponsRankingSources.remove(weaponsRankingSource);
            final QlkRankingSource qlkRankingSource = new QlkRankingSource(role.getRoleId());
            RankingList.qlkRankingSources.remove(qlkRankingSource);
            final ComprehensiveStrengthSource comprehensiveStrengthSource = new ComprehensiveStrengthSource(role.getRoleId());
            RankingList.comprehensiveStrengthSources.remove(comprehensiveStrengthSource);
        }
        this.wearEquipScoreChage(role, wearEquips, wearEquip);
        this.wearEquipQlkChage(role, wearEquips);
        this.wearEquipScoreChage(role, wearEquip);
    }
    
    private void wearEquipScoreChage(final Role role, final List<WearEquip> wearEquips, final WearEquip wearEquip) {
        final FiedEquip wearEquipScore = wearEquip.getField().stream().filter(obj -> obj.getType().equals(6679)).findFirst().orElse(null);
        if (wearEquipScore == null) {
            return;
        }
        final int[] wearEquipScores = { 0 };
        wearEquips.stream().forEach(obj -> {
            if (!obj.equals(wearEquip)) {
                final Optional<FiedEquip> optional2 = obj.getField().stream().filter(fiedEquip -> fiedEquip.getType().equals(6679)).findAny();
                if (optional2.isPresent()) {
                    final Integer value = optional2.get().getValue();
                    if (value != null) {
                        final int n;
                        wearEquipScores[0] += value;
                    }
                }
            }
            else if (wearEquipScore.getValue() != null) {
                final int n2;
                wearEquipScores[0] += wearEquipScore.getValue();
            }
            return;
        });
        final int score = wearEquipScores[0];
        final Optional<ComprehensiveStrengthSource> optional;
        synchronized (RankingList.comprehensiveStrengthSources) {
            optional = RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRoleId() == role.getRoleId()).findAny();
        }
        if (optional != null && optional.isPresent()) {
            final ComprehensiveStrengthSource comprehensiveStrengthSource = optional.get();
            if (comprehensiveStrengthSource.getWearEquipScore() != score) {
                comprehensiveStrengthSource.setWearEquipScore(score);
            }
        }
        else {
            final Map<String, Integer> totalRepairMap = RankingUtils.getTotalRepair(role, RankingList.petMaps.get(role.getRoleId()), RankingList.wearEquipMaps.get(role.getRoleId()));
            final ComprehensiveStrengthSource css = new ComprehensiveStrengthSource(role, totalRepairMap);
            RankingList.comprehensiveStrengthSources.add(css);
        }
    }
    
    private void wearEquipQlkChage(final Role role, final List<WearEquip> wearEquips) {
        QlkRankingSource qlkRankingSource = null;
        synchronized (RankingList.qlkRankingSources) {
            qlkRankingSource = RankingList.qlkRankingSources.stream().filter(obj -> role.getRoleId().equals(obj.getRoleId())).findFirst().orElse(null);
        }
        if (qlkRankingSource != null) {
            if (wearEquips.size() > 0) {
                final int[] roleQlkWuxing = RankingUtils.getRoleQlkWuxing(role, wearEquips, null);
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
            final int[] roleQlkWuxing = RankingUtils.getRoleQlkWuxing(role, wearEquips, null);
            qlkRankingSource = new QlkRankingSource(role, roleQlkWuxing);
            RankingList.qlkRankingSources.add(qlkRankingSource);
        }
    }
    
    private void wearEquipScoreChage(final Role role, final WearEquip wearEquip) {
        WeaponsRankingSource weaponsRankingSource = null;
        synchronized (RankingList.weaponsRankingSources) {
            weaponsRankingSource = RankingList.weaponsRankingSources.stream().filter(obj -> obj.getWearEquipId().equals(wearEquip.getId())).findFirst().orElse(null);
        }
        if (weaponsRankingSource != null) {
            final FiedEquip fiedEquip = wearEquip.getField().stream().filter(e -> e.getType().equals(6679)).findFirst().orElse(null);
            if (fiedEquip != null && !fiedEquip.getValue().equals(weaponsRankingSource.getScore())) {
                weaponsRankingSource.setScore(fiedEquip.getValue());
            }
        }
        else {
            weaponsRankingSource = RankingUtils.createWeaponsRankingSource(role, wearEquip, 0);
            RankingList.weaponsRankingSources.add(weaponsRankingSource);
        }
    }
    
    public void getWeaponsRankingList(final Role role, final byte race, final byte equipType) {
        final List<WeaponsRankingSource> weaponsRankingSources = RankingList.weaponsRankingSources;
        if (weaponsRankingSources == null || weaponsRankingSources.size() <= 0) {
            MessagePusher.pushMessage(role, new RespWeaponsRankingList(race, equipType, new ArrayList<WeaponsRankingField>()));
            return;
        }
        final int count = 100;
        final List<WeaponsRankingSource> collect = weaponsRankingSources.stream().sorted(Comparator.comparing(WeaponsRankingSource::getScore).reversed()).collect(Collectors.toList());
        List<WeaponsRankingSource> limitWeaponsRankingSources;
        if (race == 0) {
            limitWeaponsRankingSources = collect.stream().filter(obj -> obj.getEquipType() == equipType).limit(count).collect(Collectors.toList());
        }
        else {
            limitWeaponsRankingSources = collect.stream().filter(obj -> obj.getEquipType() == equipType && obj.getRace() == race).limit(count).collect(Collectors.toList());
        }
        final int[] index = { 0 };
        final List<WeaponsRankingField> list = limitWeaponsRankingSources.stream().map(weapons -> {
            final WeaponsRankingField weaponsRankingField = new WeaponsRankingField(weapons, ++index[0], (byte)0);
            return weaponsRankingField;
        }).collect(Collectors.toList());
        MessagePusher.pushMessage(role, new RespWeaponsRankingList(race, equipType, list));
    }
    
    public void getMyWeaponsRanking(final Role role, final byte type, final int serial, final byte serverId) {
        final List<WeaponsRankingSource> equips;
        synchronized (RankingList.weaponsRankingSources) {
            equips = RankingList.weaponsRankingSources.stream().filter(obj -> obj.getRoleId() == role.getRoleId()).collect(Collectors.toList());
        }
        if (equips == null || equips.size() <= 0) {
            MessagePusher.pushMessage(role, new RespMyRanking(type, serial, serverId, null));
            return;
        }
        final Optional<WeaponsRankingSource> optional = equips.stream().max(Comparator.comparing(WeaponsRankingSource::getScore));
        if (optional.isPresent()) {
            final WeaponsRankingSource weaponsRankingSource = optional.get();
            int index = 1;
            index = RankingList.weaponsRankingSources.indexOf(weaponsRankingSource);
            if (index == -1) {
                MessagePusher.pushMessage(role, new RespMyRanking(type, serial, serverId, null));
                return;
            }
            final WeaponsRankingField weaponsRankingField = new WeaponsRankingField(weaponsRankingSource, ++index, serverId);
            MessagePusher.pushMessage(role, new RespMyRanking(type, serial, serverId, weaponsRankingField));
        }
    }
}
