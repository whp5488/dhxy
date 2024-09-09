package com.kitty.system.game.ranking.service;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.message.RespComprehensiveStrengthRankingList;
import com.kitty.system.game.ranking.pojo.field.ComprehensiveStrengthRankingField;
import java.util.stream.Collectors;
import java.util.Comparator;
import com.kitty.system.game.pet.domain.Pet;
import java.util.ArrayList;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import java.util.Map;
import java.util.Optional;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.ranking.pojo.ComprehensiveStrengthSource;
import com.kitty.system.game.ranking.util.RankingUtils;
import java.util.List;
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
    
    public ComprehensiveStrengthRankingListService() {
    }
    
    @EventHandler({ EventType.ROLE_SKILL_CHANGE })
    public void handleRoleSkillEvent(RoleChangeEvent roleChangeEvent) {
        Role role = roleChangeEvent.getRole();
        int[] skillScores = { 0 };
        role.getSkills().values().stream().forEach(roleSkill -> {
            skillScores[0] += BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Integer.valueOf((int)roleSkill.getProficient() / 100));
            return;
        });
        int skillScore = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), BigDecimalUtil.multiply(String.valueOf(skillScores[0]), String.valueOf(2.5)));
        Optional<ComprehensiveStrengthSource> optional = null;
        synchronized (RankingList.comprehensiveStrengthSources) {
            optional = RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId()).findAny();
        }
        if (optional != null && optional.isPresent()) {
            ComprehensiveStrengthSource comprehensiveStrengthSource = (ComprehensiveStrengthSource)optional.get();
            if (comprehensiveStrengthSource.getSkillScore() != skillScore) {
                comprehensiveStrengthSource.setSkillScore(skillScore);
            }
        }
        else {
            Map<String, Integer> totalRepairMap = RankingUtils.getTotalRepair(role, (List)RankingList.petMaps.get(role.getRoleId()), (List)RankingList.wearEquipMaps.get(role.getRoleId()));
            ComprehensiveStrengthSource css = new ComprehensiveStrengthSource(role, totalRepairMap);
            synchronized (RankingList.comprehensiveStrengthSources) {
                RankingList.comprehensiveStrengthSources.add(css);
            }
        }
    }
    
    @EventHandler({ EventType.PET_SCORE_CHANGE })
    public void handlePetChangeEvent(PetChangeEvent petChangeEvent) {
        Pet pet = petChangeEvent.getPet();
        Role role = this.roleService.getPlayer((int)pet.getRoleId());
        List<Pet> pets = (List)RankingList.petMaps.get(role.getRoleId());
        if (pets == null) {
            pets = new ArrayList<>();
        }
        boolean isDelete = false;
        if (pet.isDelete()) {
            pets.remove(pet);
            RankingList.pets.remove(pet);
            isDelete = true;
        }
        if (!isDelete && (pets == null || !pets.contains(pet))) {
            if (pets == null) {
                pets = new ArrayList<>();
            }
            pets.add(pet);
            RankingList.pets.add(pet);
        }
        int[] petScores = { 0 };
        synchronized (pets) {
            pets.stream().forEach(obj -> {
                if (!obj.equals(pet)) {
                    petScores[0] += (int)obj.getScore();
                }
                else {
                    petScores[0] += (int)pet.getScore();
                }
                return;
            });
        }
        int petScore = petScores[0];
        Optional<ComprehensiveStrengthSource> optional = null;
        synchronized (RankingList.comprehensiveStrengthSources) {
            optional = RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId()).findFirst();
        }
        if (optional != null && optional.isPresent()) {
            ComprehensiveStrengthSource comprehensiveStrengthSource = (ComprehensiveStrengthSource)optional.get();
            if (comprehensiveStrengthSource.getPetScore() != petScore) {
                comprehensiveStrengthSource.setPetScore(petScore);
            }
        }
        else {
            Map<String, Integer> totalRepairMap = RankingUtils.getTotalRepair(role, (List)RankingList.petMaps.get(role.getRoleId()), (List)RankingList.wearEquipMaps.get(role.getRoleId()));
            ComprehensiveStrengthSource css = new ComprehensiveStrengthSource(role, totalRepairMap);
            synchronized (RankingList.comprehensiveStrengthSources) {
                RankingList.comprehensiveStrengthSources.add(css);
            }
        }
    }
    
    public void getComprehensiveStrengthRankingList(Role role, byte race, byte serverId) {
        int count = 100;
        List<ComprehensiveStrengthRankingField> returnList = new ArrayList<>();
        int[] indexs = { 0 };
        if (race == 0) {
            synchronized (RankingList.comprehensiveStrengthSources) {
                List<ComprehensiveStrengthSource> collect = (List)RankingList.comprehensiveStrengthSources.stream().sorted(Comparator.comparing(ComprehensiveStrengthSource::getTotalRepair, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
                returnList = (List)collect.stream().map(comprehensiveStrengthSource -> {
                    ComprehensiveStrengthRankingField comprehensiveStrengthRankingField2 = new ComprehensiveStrengthRankingField(comprehensiveStrengthSource, ++indexs[0], serverId);
                    return comprehensiveStrengthRankingField2;
                }).collect(Collectors.toList());
            }
        }
        else {
            synchronized (RankingList.comprehensiveStrengthSources) {
                List<ComprehensiveStrengthSource> collect = (List)RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRace() == race).sorted(Comparator.comparing(ComprehensiveStrengthSource::getTotalRepair, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
                returnList = (List)collect.stream().map(comprehensiveStrengthSource -> {
                    ComprehensiveStrengthRankingField comprehensiveStrengthRankingField = new ComprehensiveStrengthRankingField(comprehensiveStrengthSource, ++indexs[0], serverId);
                    return comprehensiveStrengthRankingField;
                }).collect(Collectors.toList());
            }
        }
        MessagePusher.pushMessage(role, new RespComprehensiveStrengthRankingList(race, serverId, returnList));
    }
    
    public void getMyComprehensiveStrengthRanking(Role role, int serial, byte serverId) {
        Optional<ComprehensiveStrengthSource> optional = null;
        if (serial == 0) {
            synchronized (RankingList.comprehensiveStrengthSources) {
                optional = RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId()).findFirst();
            }
        }
        else {
            synchronized (RankingList.comprehensiveStrengthSources) {
                optional = RankingList.comprehensiveStrengthSources.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId() && obj.getRace() == serial).findFirst();
            }
        }
        if (optional != null && optional.isPresent()) {
            ComprehensiveStrengthSource comprehensiveStrengthSource = (ComprehensiveStrengthSource)optional.get();
            if (comprehensiveStrengthSource != null) {
                int index = RankingList.comprehensiveStrengthSources.indexOf(comprehensiveStrengthSource);
                if (index != -1) {
                    ComprehensiveStrengthRankingField comprehensiveStrengthRankingField = new ComprehensiveStrengthRankingField(comprehensiveStrengthSource, ++index, serverId);
                    MessagePusher.pushMessage(role, new RespMyRanking((byte)2, serial, serverId, comprehensiveStrengthRankingField));
                    return;
                }
            }
        }
        MessagePusher.pushMessage(role, new RespMyRanking((byte)2, serial, serverId, null));
    }
}
