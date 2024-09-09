package com.kitty.system.game.ranking.service;

import com.kitty.system.game.ranking.message.RespPropertyPetRankingList;
import java.util.Optional;
import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.system.game.ranking.pojo.field.PetPropertyRankingField;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.message.RespClassifyPetRankingList;
import com.kitty.system.game.ranking.pojo.field.PetClassifyRankingField;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.ranking.pojo.PetRankingSource;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.List;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.ranking.PetChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class PetRankingListService
{
    @Autowired
    private RoleService roleService;
    
    public PetRankingListService() {
    }
    
    @EventHandler({ EventType.PET_SCORE_CHANGE })
    public void handlePetScoreChangeEvent(PetChangeEvent petChangeEvent) {
        this.update(petChangeEvent.getPet(), petChangeEvent.getEquips());
    }
    
    @EventHandler({ EventType.PET_CHANGE })
    public void handlePetChangeEvent(PetChangeEvent petChangeEvent) {
        this.update(petChangeEvent.getPet(), petChangeEvent.getEquips());
    }
    
    public void update(Pet pet, List<PetEquip> equips) {
        boolean isHave = false;
        Role role = this.roleService.getPlayer((int)pet.getRoleId());
        for (int i = RankingList.petRankingListSource.size() - 1; i >= 0; --i) {
            PetRankingSource petRankingSource = (PetRankingSource)RankingList.petRankingListSource.get(i);
            if (petRankingSource != null && petRankingSource.getPetId() == (int)pet.getPetId()) {
                isHave = true;
                if (pet.isDelete()) {
                    RankingList.petRankingListSource.remove(petRankingSource);
                    break;
                }
                this.petChange(petRankingSource, pet, equips);
                break;
            }
        }
        if (!isHave) {
            int speed = CountUtils.countPetSpeed(pet, false);
            int attack = CountUtils.countPetPhyPower(pet, equips);
            RankingList.petRankingListSource.add(new PetRankingSource(role, pet, pet.getPetName(), speed, attack));
        }
    }
    
    private boolean petChange(PetRankingSource petRankingSource, Pet pet, List<PetEquip> equips) {
        boolean flag = false;
        if (petRankingSource.getRoleId() != (int)pet.getRoleId()) {
            Role role = this.roleService.getOnlinePlayer((int)pet.getRoleId());
            petRankingSource.setRoleId((int)role.getRoleId());
            petRankingSource.setRoleName(role.getName());
        }
        if (petRankingSource.getLife() != (int)pet.getMaxLife()) {
            petRankingSource.setLife((int)pet.getMaxLife());
        }
        if (petRankingSource.getMana() != (int)pet.getMaxMana()) {
            petRankingSource.setMana((int)pet.getMaxMana());
        }
        int speed = CountUtils.countPetSpeed(pet, false);
        if (petRankingSource.getSpeed() != speed) {
            petRankingSource.setSpeed(speed);
        }
        int attack = CountUtils.countPetPhyPower(pet, equips);
        if (petRankingSource.getAttack() != attack) {
            petRankingSource.setAttack(attack);
        }
        if (petRankingSource.getScore() != (int)pet.getScore()) {
            petRankingSource.setScore((int)pet.getScore());
            flag = true;
        }
        return flag;
    }
    
    public void getClassifyPetRankingList(Role role, int petSerial, byte serverId) {
        int count = 100;
        List<PetClassifyRankingField> returnList = new ArrayList<>();
        if (RankingList.petRankingListSource.size() <= 0) {
            MessagePusher.pushMessage(role, new RespClassifyPetRankingList(petSerial, serverId, returnList));
            return;
        }
        List<PetRankingSource> pageList = null;
        if (petSerial > 0) {
            synchronized (RankingList.petRankingListSource) {
                pageList = (List)RankingList.petRankingListSource.stream().filter(obj -> obj.getPetSerial() == petSerial).sorted(Comparator.comparing(PetRankingSource::getScore, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
            }
        }
        else {
            synchronized (RankingList.petRankingListSource) {
                pageList = (List)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getScore, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
            }
        }
        if (pageList == null || pageList.size() <= 0) {
            MessagePusher.pushMessage(role, new RespClassifyPetRankingList(petSerial, serverId, returnList));
            return;
        }
        int[] index = { 0 };
        synchronized (returnList) {
            returnList = (List)pageList.stream().map(petRankingField -> {
                PetClassifyRankingField petClassifyRankingField = new PetClassifyRankingField(petRankingField, ++index[0], serverId);
                return petClassifyRankingField;
            }).collect(Collectors.toList());
        }
        MessagePusher.pushMessage(role, new RespClassifyPetRankingList(petSerial, serverId, returnList));
        return;
    }
    
    public void getMyPropertyRanking(Role role, int serial, byte serverId) {
        Optional<PetRankingSource> optional = null;
        List<PetRankingSource> petPropertyRankingList = null;
        PetRankingSource petRankingSource = null;
        int propertyValue = 0;
        switch (serial) {
            case 0: {
                synchronized (RankingList.petRankingListSource) {
                    optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId() && obj.getSpeed() >= 0).max(Comparator.comparing(PetRankingSource::getSpeed));
                }
                if (optional.isPresent()) {
                    petRankingSource = (PetRankingSource)optional.get();
                    synchronized (RankingList.petRankingListSource) {
                        petPropertyRankingList = (List)RankingList.petRankingListSource.stream().filter(obj -> obj.getSpeed() >= 0).sorted(Comparator.comparing(PetRankingSource::getSpeed, Comparator.reverseOrder())).collect(Collectors.toList());
                    }
                    propertyValue = petRankingSource.getSpeed();
                    break;
                }
                break;
            }
            case 1: {
                synchronized (RankingList.petRankingListSource) {
                    optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId()).max(Comparator.comparing(PetRankingSource::getAttack));
                }
                if (optional.isPresent()) {
                    petRankingSource = (PetRankingSource)optional.get();
                    petPropertyRankingList = (List)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getAttack, Comparator.reverseOrder())).collect(Collectors.toList());
                    propertyValue = petRankingSource.getAttack();
                    break;
                }
                break;
            }
            case 2: {
                synchronized (RankingList.petRankingListSource) {
                    optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId()).max(Comparator.comparing(PetRankingSource::getLife));
                }
                if (optional.isPresent()) {
                    petRankingSource = (PetRankingSource)optional.get();
                    petPropertyRankingList = (List)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getLife, Comparator.reverseOrder())).collect(Collectors.toList());
                    propertyValue = petRankingSource.getLife();
                    break;
                }
                break;
            }
            case 3: {
                synchronized (RankingList.petRankingListSource) {
                    optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId()).max(Comparator.comparing(PetRankingSource::getMana));
                }
                if (optional.isPresent()) {
                    petRankingSource = (PetRankingSource)optional.get();
                    petPropertyRankingList = (List)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getMana, Comparator.reverseOrder())).collect(Collectors.toList());
                    propertyValue = petRankingSource.getMana();
                    break;
                }
                break;
            }
            case 4: {
                synchronized (RankingList.petRankingListSource) {
                    optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId() && obj.getSpeed() < 0).min(Comparator.comparing(PetRankingSource::getSpeed));
                }
                if (optional.isPresent()) {
                    petRankingSource = (PetRankingSource)optional.get();
                    synchronized (RankingList.petRankingListSource) {
                        petPropertyRankingList = (List)RankingList.petRankingListSource.stream().filter(obj -> obj.getSpeed() < 0).sorted(Comparator.comparing(PetRankingSource::getSpeed)).collect(Collectors.toList());
                    }
                    propertyValue = petRankingSource.getSpeed();
                    break;
                }
                break;
            }
        }
        if (petRankingSource != null) {
            int index = petPropertyRankingList.indexOf(petRankingSource);
            if (index != -1) {
                PetPropertyRankingField petPropertyRankingField = new PetPropertyRankingField(petRankingSource, ++index, serverId, propertyValue);
                MessagePusher.pushMessage(role, new RespMyRanking((byte)1, serial, serverId, petPropertyRankingField));
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespMyRanking((byte)1, serial, serverId, null));
    }
    
    public void getMyClassifyRanking(Role role, int serial, byte serverId) {
        PetRankingSource petRankingSource = null;
        if (serial > 0) {
            Optional<PetRankingSource> optional;
            synchronized (RankingList.petRankingListSource) {
                optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId() && obj.getPetSerial() == serial).max(Comparator.comparing(PetRankingSource::getScore));
            }
            if (optional.isPresent()) {
                petRankingSource = (PetRankingSource)optional.get();
            }
        }
        else {
            Optional<PetRankingSource> optional;
            synchronized (RankingList.petRankingListSource) {
                optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId()).max(Comparator.comparing(PetRankingSource::getScore));
            }
            if (optional.isPresent()) {
                petRankingSource = (PetRankingSource)optional.get();
            }
        }
        if (petRankingSource != null) {
            int index = RankingList.petRankingListSource.indexOf(petRankingSource);
            if (index != -1) {
                PetClassifyRankingField petClassifyRankingField = new PetClassifyRankingField(petRankingSource, ++index, serverId);
                MessagePusher.pushMessage(role, new RespMyRanking((byte)0, serial, serverId, petClassifyRankingField));
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespMyRanking((byte)0, serial, serverId, null));
    }
    
    public void getPetPropertyRankingList(Role role, byte type, byte serverId) {
        List<PetPropertyRankingField> petPropertyRankingFields = new ArrayList<>();
        if (RankingList.petRankingListSource.size() <= 0) {
            MessagePusher.pushMessage(role, new RespPropertyPetRankingList(type, serverId, petPropertyRankingFields));
            return;
        }
        int count = 100;
        List<PetRankingSource> pageList = null;
        switch (type) {
            case 0: {
                synchronized (RankingList.petRankingListSource) {
                    pageList = (List)RankingList.petRankingListSource.stream().filter(obj -> obj.getSpeed() >= 0).sorted(Comparator.comparing(PetRankingSource::getSpeed, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
                }
                break;
            }
            case 1: {
                pageList = (List)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getAttack, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
                break;
            }
            case 2: {
                pageList = (List)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getLife, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
                break;
            }
            case 3: {
                pageList = (List)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getMana, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
                break;
            }
            case 4: {
                synchronized (RankingList.petRankingListSource) {
                    pageList = (List)RankingList.petRankingListSource.stream().filter(obj -> obj.getSpeed() < 0).sorted(Comparator.comparing(PetRankingSource::getSpeed)).limit((long)count).collect(Collectors.toList());
                }
            }
        }
        if (pageList == null || pageList.size() <= 0) {
            MessagePusher.pushMessage(role, new RespPropertyPetRankingList(type, serverId, petPropertyRankingFields));
            return;
        }
        int[] index = { 0 };
        synchronized (pageList) {
            petPropertyRankingFields = (List)pageList.stream().map(petRankingField -> {
                int propertyValue = 0;
                switch (type) {
                    case 0: {
                        propertyValue = petRankingField.getSpeed();
                        break;
                    }
                    case 1: {
                        propertyValue = petRankingField.getAttack();
                        break;
                    }
                    case 2: {
                        propertyValue = petRankingField.getLife();
                        break;
                    }
                    case 3: {
                        propertyValue = petRankingField.getMana();
                        break;
                    }
                    case 4: {
                        propertyValue = petRankingField.getSpeed();
                        break;
                    }
                }
                PetPropertyRankingField petPropertyRankingField = new PetPropertyRankingField(petRankingField, ++index[0], serverId, propertyValue);
                return petPropertyRankingField;
            }).collect(Collectors.toList());
        }
        MessagePusher.pushMessage(role, new RespPropertyPetRankingList(type, serverId, petPropertyRankingFields));
        return;
    }
}
