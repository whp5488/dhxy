
package com.kitty.system.game.ranking.service;

import com.kitty.system.game.ranking.message.RespPropertyPetRankingList;
import java.util.Optional;
import com.kitty.system.game.ranking.pojo.RankingField;
import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.system.game.ranking.pojo.field.PetPropertyRankingField;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.message.RespClassifyPetRankingList;
import com.kitty.system.game.ranking.pojo.field.PetClassifyRankingField;
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
    
    @EventHandler({ EventType.PET_SCORE_CHANGE })
    public void handlePetScoreChangeEvent(final PetChangeEvent petChangeEvent) {
        this.update(petChangeEvent.getPet(), petChangeEvent.getEquips());
    }
    
    @EventHandler({ EventType.PET_CHANGE })
    public void handlePetChangeEvent(final PetChangeEvent petChangeEvent) {
        this.update(petChangeEvent.getPet(), petChangeEvent.getEquips());
    }
    
    public void update(final Pet pet, final List<PetEquip> equips) {
        boolean isHave = false;
        final Role role = this.roleService.getPlayer(pet.getRoleId());
        for (int i = RankingList.petRankingListSource.size() - 1; i >= 0; --i) {
            final PetRankingSource petRankingSource = RankingList.petRankingListSource.get(i);
            if (petRankingSource != null) {
                if (petRankingSource.getPetId() == pet.getPetId()) {
                    isHave = true;
                    if (pet.isDelete()) {
                        RankingList.petRankingListSource.remove(petRankingSource);
                        break;
                    }
                    this.petChange(petRankingSource, pet, equips);
                    break;
                }
            }
        }
        if (!isHave) {
            final int speed = CountUtils.countPetSpeed(pet, false);
            final int attack = CountUtils.countPetPhyPower(pet, equips);
            RankingList.petRankingListSource.add(new PetRankingSource(role, pet, pet.getPetName(), speed, attack));
        }
    }
    
    private boolean petChange(final PetRankingSource petRankingSource, final Pet pet, final List<PetEquip> equips) {
        boolean flag = false;
        if (petRankingSource.getRoleId() != pet.getRoleId()) {
            final Role role = this.roleService.getOnlinePlayer(pet.getRoleId());
            petRankingSource.setRoleId(role.getRoleId());
            petRankingSource.setRoleName(role.getName());
        }
        if (petRankingSource.getLife() != pet.getMaxLife()) {
            petRankingSource.setLife(pet.getMaxLife());
        }
        if (petRankingSource.getMana() != pet.getMaxMana()) {
            petRankingSource.setMana(pet.getMaxMana());
        }
        final int speed = CountUtils.countPetSpeed(pet, false);
        if (petRankingSource.getSpeed() != speed) {
            petRankingSource.setSpeed(speed);
        }
        final int attack = CountUtils.countPetPhyPower(pet, equips);
        if (petRankingSource.getAttack() != attack) {
            petRankingSource.setAttack(attack);
        }
        if (petRankingSource.getScore() != pet.getScore()) {
            petRankingSource.setScore(pet.getScore());
            flag = true;
        }
        return flag;
    }
    
    public void getClassifyPetRankingList(final Role role, final int petSerial, final byte serverId) {
        final int count = 100;
        List<PetClassifyRankingField> returnList = new ArrayList<PetClassifyRankingField>();
        if (RankingList.petRankingListSource.size() <= 0) {
            MessagePusher.pushMessage(role, new RespClassifyPetRankingList(petSerial, serverId, returnList));
            return;
        }
        List<PetRankingSource> pageList = null;
        if (petSerial > 0) {
            synchronized (RankingList.petRankingListSource) {
                pageList = RankingList.petRankingListSource.stream().filter(obj -> obj.getPetSerial() == petSerial).sorted(Comparator.comparing(PetRankingSource::getScore, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
            }
        }
        else {
            synchronized (RankingList.petRankingListSource) {
                pageList = RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getScore, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
            }
        }
        if (pageList == null || pageList.size() <= 0) {
            MessagePusher.pushMessage(role, new RespClassifyPetRankingList(petSerial, serverId, returnList));
            return;
        }
        final int[] index = { 0 };
        synchronized (returnList) {
            returnList = pageList.stream().map(petRankingField -> {
                final PetClassifyRankingField petClassifyRankingField = new PetClassifyRankingField(petRankingField, ++index[0], serverId);
                return petClassifyRankingField;
            }).collect(Collectors.toList());
        }
        MessagePusher.pushMessage(role, new RespClassifyPetRankingList(petSerial, serverId, returnList));
    }
    
    public void getMyPropertyRanking(final Role role, final int serial, final byte serverId) {
        Optional<PetRankingSource> optional = null;
        List<PetRankingSource> petPropertyRankingList = null;
        PetRankingSource petRankingSource = null;
        int propertyValue = 0;
        switch (serial) {
            case 0: {
                synchronized (RankingList.petRankingListSource) {
                    optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == role.getRoleId() && obj.getSpeed() >= 0).max(Comparator.comparing(PetRankingSource::getSpeed));
                }
                if (optional.isPresent()) {
                    petRankingSource = optional.get();
                    synchronized (RankingList.petRankingListSource) {
                        petPropertyRankingList = RankingList.petRankingListSource.stream().filter(obj -> obj.getSpeed() >= 0).sorted(Comparator.comparing(PetRankingSource::getSpeed, Comparator.reverseOrder())).collect(Collectors.toList());
                    }
                    propertyValue = petRankingSource.getSpeed();
                    break;
                }
                break;
            }
            case 1: {
                synchronized (RankingList.petRankingListSource) {
                    optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == role.getRoleId()).max(Comparator.comparing(PetRankingSource::getAttack));
                }
                if (optional.isPresent()) {
                    petRankingSource = optional.get();
                    petPropertyRankingList = RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getAttack, Comparator.reverseOrder())).collect(Collectors.toList());
                    propertyValue = petRankingSource.getAttack();
                    break;
                }
                break;
            }
            case 2: {
                synchronized (RankingList.petRankingListSource) {
                    optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == role.getRoleId()).max(Comparator.comparing(PetRankingSource::getLife));
                }
                if (optional.isPresent()) {
                    petRankingSource = optional.get();
                    petPropertyRankingList = RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getLife, Comparator.reverseOrder())).collect(Collectors.toList());
                    propertyValue = petRankingSource.getLife();
                    break;
                }
                break;
            }
            case 3: {
                synchronized (RankingList.petRankingListSource) {
                    optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == role.getRoleId()).max(Comparator.comparing(PetRankingSource::getMana));
                }
                if (optional.isPresent()) {
                    petRankingSource = optional.get();
                    petPropertyRankingList = RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getMana, Comparator.reverseOrder())).collect(Collectors.toList());
                    propertyValue = petRankingSource.getMana();
                    break;
                }
                break;
            }
            case 4: {
                synchronized (RankingList.petRankingListSource) {
                    optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == role.getRoleId() && obj.getSpeed() < 0).min(Comparator.comparing(PetRankingSource::getSpeed));
                }
                if (optional.isPresent()) {
                    petRankingSource = optional.get();
                    synchronized (RankingList.petRankingListSource) {
                        petPropertyRankingList = RankingList.petRankingListSource.stream().filter(obj -> obj.getSpeed() < 0).sorted(Comparator.comparing(PetRankingSource::getSpeed)).collect(Collectors.toList());
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
                final PetPropertyRankingField petPropertyRankingField = new PetPropertyRankingField(petRankingSource, ++index, serverId, propertyValue);
                MessagePusher.pushMessage(role, new RespMyRanking((byte)1, serial, serverId, petPropertyRankingField));
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespMyRanking((byte)1, serial, serverId, null));
    }
    
    public void getMyClassifyRanking(final Role role, final int serial, final byte serverId) {
        PetRankingSource petRankingSource = null;
        if (serial > 0) {
            final Optional<PetRankingSource> optional;
            synchronized (RankingList.petRankingListSource) {
                optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == role.getRoleId() && obj.getPetSerial() == serial).max(Comparator.comparing(PetRankingSource::getScore));
            }
            if (optional.isPresent()) {
                petRankingSource = optional.get();
            }
        }
        else {
            final Optional<PetRankingSource> optional;
            synchronized (RankingList.petRankingListSource) {
                optional = RankingList.petRankingListSource.stream().filter(obj -> obj.getRoleId() == role.getRoleId()).max(Comparator.comparing(PetRankingSource::getScore));
            }
            if (optional.isPresent()) {
                petRankingSource = optional.get();
            }
        }
        if (petRankingSource != null) {
            int index = RankingList.petRankingListSource.indexOf(petRankingSource);
            if (index != -1) {
                final PetClassifyRankingField petClassifyRankingField = new PetClassifyRankingField(petRankingSource, ++index, serverId);
                MessagePusher.pushMessage(role, new RespMyRanking((byte)0, serial, serverId, petClassifyRankingField));
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespMyRanking((byte)0, serial, serverId, null));
    }
    
    public void getPetPropertyRankingList(final Role role, final byte type, final byte serverId) {
        List<PetPropertyRankingField> petPropertyRankingFields = new ArrayList<PetPropertyRankingField>();
        if (RankingList.petRankingListSource.size() <= 0) {
            MessagePusher.pushMessage(role, new RespPropertyPetRankingList(type, serverId, petPropertyRankingFields));
            return;
        }
        final int count = 100;
        List<PetRankingSource> pageList = null;
        switch (type) {
            case 0: {
                synchronized (RankingList.petRankingListSource) {
                    pageList = RankingList.petRankingListSource.stream().filter(obj -> obj.getSpeed() >= 0).sorted(Comparator.comparing(PetRankingSource::getSpeed, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
                }
                break;
            }
            case 1: {
                pageList = RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getAttack, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
                break;
            }
            case 2: {
                pageList = RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getLife, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
                break;
            }
            case 3: {
                pageList = RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getMana, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
                break;
            }
            case 4: {
                synchronized (RankingList.petRankingListSource) {
                    pageList = RankingList.petRankingListSource.stream().filter(obj -> obj.getSpeed() < 0).sorted(Comparator.comparing(PetRankingSource::getSpeed)).limit(count).collect(Collectors.toList());
                }
                break;
            }
        }
        if (pageList == null || pageList.size() <= 0) {
            MessagePusher.pushMessage(role, new RespPropertyPetRankingList(type, serverId, petPropertyRankingFields));
            return;
        }
        final int[] index = { 0 };
        synchronized (pageList) {
            petPropertyRankingFields = pageList.stream().map(petRankingField -> {
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
                final PetPropertyRankingField petPropertyRankingField = new PetPropertyRankingField(petRankingField, ++index[0], serverId, propertyValue);
                return petPropertyRankingField;
            }).collect(Collectors.toList());
        }
        MessagePusher.pushMessage(role, new RespPropertyPetRankingList(type, serverId, petPropertyRankingFields));
    }
}
