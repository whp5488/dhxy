
package com.kitty.system.game.ranking.service;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.ranking.pojo.RankingField;
import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.message.RespComprehensiveStrengthRankingList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.system.game.ranking.pojo.field.ComprehensiveStrengthRankingField;
import java.util.ArrayList;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import java.util.Map;
import java.util.Optional;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.ranking.util.RankingUtils;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.pet.domain.Pet;
import java.util.List;
import com.kitty.system.game.ranking.pojo.ComprehensiveStrengthSource;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.listener.event.ranking.RoleChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class ComprehensiveStrengthRankingListService
{
    @Autowired
    private RoleService roleService;
    
    @EventHandler({ EventType.ROLE_SKILL_CHANGE })
    public void handleRoleSkillEvent(final RoleChangeEvent roleChangeEvent) {
        final Role role = roleChangeEvent.getRole();
        final int[] skillScores = { 0 };
        role.getSkills().values().stream().forEach(roleSkill -> {
            final int n;
            skillScores[0] += BigDecimalUtil.decimalScaleIntegerDown(0, roleSkill.getProficient() / 100);
            return;
        });
        final int skillScore = BigDecimalUtil.decimalScaleIntegerDown(0, BigDecimalUtil.multiply(String.valueOf(skillScores[0]), String.valueOf(2.5)));
        Optional<ComprehensiveStrengthSource> optional = null;
        synchronized (RankingList.comprehensiveStrengthSources) {
            optional = RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRoleId() == role.getRoleId()).findAny();
        }
        if (optional != null && optional.isPresent()) {
            final ComprehensiveStrengthSource comprehensiveStrengthSource = optional.get();
            if (comprehensiveStrengthSource.getSkillScore() != skillScore) {
                comprehensiveStrengthSource.setSkillScore(skillScore);
            }
        }
        else {
            final Map<String, Integer> totalRepairMap = RankingUtils.getTotalRepair(role, RankingList.petMaps.get(role.getRoleId()), RankingList.wearEquipMaps.get(role.getRoleId()));
            final ComprehensiveStrengthSource css = new ComprehensiveStrengthSource(role, totalRepairMap);
            synchronized (RankingList.comprehensiveStrengthSources) {
                RankingList.comprehensiveStrengthSources.add(css);
            }
        }
    }
    
    @EventHandler({ EventType.PET_SCORE_CHANGE })
    public void handlePetChangeEvent(final PetChangeEvent petChangeEvent) {
        final Pet pet = petChangeEvent.getPet();
        final Role role = this.roleService.getPlayer(pet.getRoleId());
        List<Pet> pets = RankingList.petMaps.get(role.getRoleId());
        if (pets == null) {
            pets = new ArrayList<Pet>();
        }
        boolean isDelete = false;
        if (pet.isDelete()) {
            pets.remove(pet);
            RankingList.pets.remove(pet);
            isDelete = true;
        }
        if (!isDelete && (pets == null || !pets.contains(pet))) {
            if (pets == null) {
                pets = new ArrayList<Pet>();
            }
            pets.add(pet);
            RankingList.pets.add(pet);
        }
        final int[] petScores = { 0 };
        synchronized (pets) {
            pets.forEach(obj -> {
                if (!obj.equals(pet)) {
                    petScores[0] += obj.getScore();
                }
                else {
                    petScores[0] += pet.getScore();
                }
            });
        }
        final int petScore = petScores[0];
        Optional<ComprehensiveStrengthSource> optional = null;
        synchronized (RankingList.comprehensiveStrengthSources) {
            optional = RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRoleId() == role.getRoleId()).findFirst();
        }
        if (optional != null && optional.isPresent()) {
            final ComprehensiveStrengthSource comprehensiveStrengthSource = optional.get();
            if (comprehensiveStrengthSource.getPetScore() != petScore) {
                comprehensiveStrengthSource.setPetScore(petScore);
            }
        }
        else {
            final Map<String, Integer> totalRepairMap = RankingUtils.getTotalRepair(role, RankingList.petMaps.get(role.getRoleId()), RankingList.wearEquipMaps.get(role.getRoleId()));
            final ComprehensiveStrengthSource css = new ComprehensiveStrengthSource(role, totalRepairMap);
            synchronized (RankingList.comprehensiveStrengthSources) {
                RankingList.comprehensiveStrengthSources.add(css);
            }
        }
    }
    
    public void getComprehensiveStrengthRankingList(final Role role, final byte race, final byte serverId) {
        final int count = 100;
        List<ComprehensiveStrengthRankingField> returnList = new ArrayList<ComprehensiveStrengthRankingField>();
        final int[] indexs = { 0 };
        if (race == 0) {
            synchronized (RankingList.comprehensiveStrengthSources) {
                final List<ComprehensiveStrengthSource> collect = RankingList.comprehensiveStrengthSources.stream().sorted(Comparator.comparing(ComprehensiveStrengthSource::getTotalRepair, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
                returnList = collect.stream().map(comprehensiveStrengthSource -> {
                    final ComprehensiveStrengthRankingField comprehensiveStrengthRankingField = new ComprehensiveStrengthRankingField(comprehensiveStrengthSource, ++indexs[0], serverId);
                    return comprehensiveStrengthRankingField;
                }).collect(Collectors.toList());
            }
        }
        else {
            synchronized (RankingList.comprehensiveStrengthSources) {
                final List<ComprehensiveStrengthSource> collect = RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRace() == race).sorted(Comparator.comparing(ComprehensiveStrengthSource::getTotalRepair, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
                returnList = collect.stream().map(comprehensiveStrengthSource -> {
                    final ComprehensiveStrengthRankingField comprehensiveStrengthRankingField2 = new ComprehensiveStrengthRankingField(comprehensiveStrengthSource, ++indexs[0], serverId);
                    return comprehensiveStrengthRankingField2;
                }).collect(Collectors.toList());
            }
        }
        MessagePusher.pushMessage(role, new RespComprehensiveStrengthRankingList(race, serverId, returnList));
    }
    
    public void getMyComprehensiveStrengthRanking(final Role role, final int serial, final byte serverId) {
        Optional<ComprehensiveStrengthSource> optional = null;
        if (serial == 0) {
            synchronized (RankingList.comprehensiveStrengthSources) {
                optional = RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRoleId() == role.getRoleId()).findFirst();
            }
        }
        else {
            synchronized (RankingList.comprehensiveStrengthSources) {
                optional = RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRoleId() == role.getRoleId() && obj.getRace() == serial).findFirst();
            }
        }
        if (optional != null && optional.isPresent()) {
            final ComprehensiveStrengthSource comprehensiveStrengthSource = optional.get();
            if (comprehensiveStrengthSource != null) {
                int index = RankingList.comprehensiveStrengthSources.indexOf(comprehensiveStrengthSource);
                if (index != -1) {
                    final ComprehensiveStrengthRankingField comprehensiveStrengthRankingField = new ComprehensiveStrengthRankingField(comprehensiveStrengthSource, ++index, serverId);
                    MessagePusher.pushMessage(role, new RespMyRanking((byte)2, serial, serverId, comprehensiveStrengthRankingField));
                    return;
                }
            }
        }
        MessagePusher.pushMessage(role, new RespMyRanking((byte)2, serial, serverId, null));
    }
}
