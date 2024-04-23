package com.kitty.system.game.ranking.service;

import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.message.RespClassifyPetRankingList;
import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.system.game.ranking.message.RespPropertyPetRankingList;
import com.kitty.system.game.ranking.pojo.PetRankingSource;
import com.kitty.system.game.ranking.pojo.RankingField;
import com.kitty.system.game.ranking.pojo.field.PetClassifyRankingField;
import com.kitty.system.game.ranking.pojo.field.PetPropertyRankingField;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetRankingListService {
  @Autowired
  private RoleService roleService;
  
  @EventHandler({EventType.PET_SCORE_CHANGE})
  public void handlePetScoreChangeEvent(PetChangeEvent petChangeEvent) {
    update(petChangeEvent.getPet(), petChangeEvent.getEquips());
  }
  
  @EventHandler({EventType.PET_CHANGE})
  public void handlePetChangeEvent(PetChangeEvent petChangeEvent) {
    update(petChangeEvent.getPet(), petChangeEvent.getEquips());
  }
  
  public void update(Pet pet, List<PetEquip> equips) {
    boolean isHave = false;
    Role role = this.roleService.getPlayer(pet.getRoleId().intValue());
    for (int i = RankingList.petRankingListSource.size() - 1; i >= 0; i--) {
      PetRankingSource petRankingSource = RankingList.petRankingListSource.get(i);
      if (petRankingSource != null)
        if (petRankingSource.getPetId() == pet.getPetId().intValue()) {
          isHave = true;
          if (pet.isDelete()) {
            RankingList.petRankingListSource.remove(petRankingSource);
            break;
          } 
          petChange(petRankingSource, pet, equips);
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
    if (petRankingSource.getRoleId() != pet.getRoleId().intValue()) {
      Role role = this.roleService.getOnlinePlayer(pet.getRoleId().intValue());
      petRankingSource.setRoleId(role.getRoleId().intValue());
      petRankingSource.setRoleName(role.getName());
    } 
    if (petRankingSource.getLife() != pet.getMaxLife().intValue())
      petRankingSource.setLife(pet.getMaxLife().intValue()); 
    if (petRankingSource.getMana() != pet.getMaxMana().intValue())
      petRankingSource.setMana(pet.getMaxMana().intValue()); 
    int speed = CountUtils.countPetSpeed(pet, false);
    if (petRankingSource.getSpeed() != speed)
      petRankingSource.setSpeed(speed); 
    int attack = CountUtils.countPetPhyPower(pet, equips);
    if (petRankingSource.getAttack() != attack)
      petRankingSource.setAttack(attack); 
    if (petRankingSource.getScore() != pet.getScore().intValue()) {
      petRankingSource.setScore(pet.getScore().intValue());
      flag = true;
    } 
    return flag;
  }
  
  public void getClassifyPetRankingList(Role role, int petSerial, byte serverId) {
    int count = 100;
    List<PetClassifyRankingField> returnList = new ArrayList<>();
    if (RankingList.petRankingListSource.size() <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespClassifyPetRankingList(petSerial, serverId, returnList));
      return;
    } 
    List<PetRankingSource> pageList = null;
    if (petSerial > 0) {
      synchronized (RankingList.petRankingListSource) {
        pageList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().filter(obj -> (obj.getPetSerial() == petSerial)).sorted(Comparator.comparing(PetRankingSource::getScore, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
      } 
    } else {
      synchronized (RankingList.petRankingListSource) {
        pageList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getScore, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
      } 
    } 
    if (pageList == null || pageList.size() <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespClassifyPetRankingList(petSerial, serverId, returnList));
      return;
    } 
    int[] index = { 0 };
    synchronized (returnList) {
      returnList = (List<PetClassifyRankingField>)pageList.stream().map(petRankingField -> new PetClassifyRankingField(petRankingField, index[0] = index[0] + 1, serverId)).collect(Collectors.toList());
    } 
    MessagePusher.pushMessage(role, (Message)new RespClassifyPetRankingList(petSerial, serverId, returnList));
  }
  
  public void getMyPropertyRanking(Role role, int serial, byte serverId) {
    Optional<PetRankingSource> optional = null;
    List<PetRankingSource> petPropertyRankingList = null;
    PetRankingSource petRankingSource = null;
    int propertyValue = 0;
    switch (serial) {
      case 0:
        synchronized (RankingList.petRankingListSource) {
          optional = RankingList.petRankingListSource.stream().filter(obj -> (obj.getRoleId() == role.getRoleId().intValue() && obj.getSpeed() >= 0)).max(Comparator.comparing(PetRankingSource::getSpeed));
        } 
        if (optional.isPresent()) {
          petRankingSource = optional.get();
          synchronized (RankingList.petRankingListSource) {
            petPropertyRankingList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().filter(obj -> (obj.getSpeed() >= 0)).sorted(Comparator.comparing(PetRankingSource::getSpeed, Comparator.reverseOrder())).collect(Collectors.toList());
          } 
          propertyValue = petRankingSource.getSpeed();
        } 
        break;
      case 1:
        synchronized (RankingList.petRankingListSource) {
          optional = RankingList.petRankingListSource.stream().filter(obj -> (obj.getRoleId() == role.getRoleId().intValue())).max(Comparator.comparing(PetRankingSource::getAttack));
        } 
        if (optional.isPresent()) {
          petRankingSource = optional.get();
          petPropertyRankingList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getAttack, Comparator.reverseOrder())).collect(Collectors.toList());
          propertyValue = petRankingSource.getAttack();
        } 
        break;
      case 2:
        synchronized (RankingList.petRankingListSource) {
          optional = RankingList.petRankingListSource.stream().filter(obj -> (obj.getRoleId() == role.getRoleId().intValue())).max(Comparator.comparing(PetRankingSource::getLife));
        } 
        if (optional.isPresent()) {
          petRankingSource = optional.get();
          petPropertyRankingList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getLife, Comparator.reverseOrder())).collect(Collectors.toList());
          propertyValue = petRankingSource.getLife();
        } 
        break;
      case 3:
        synchronized (RankingList.petRankingListSource) {
          optional = RankingList.petRankingListSource.stream().filter(obj -> (obj.getRoleId() == role.getRoleId().intValue())).max(Comparator.comparing(PetRankingSource::getMana));
        } 
        if (optional.isPresent()) {
          petRankingSource = optional.get();
          petPropertyRankingList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getMana, Comparator.reverseOrder())).collect(Collectors.toList());
          propertyValue = petRankingSource.getMana();
        } 
        break;
      case 4:
        synchronized (RankingList.petRankingListSource) {
          optional = RankingList.petRankingListSource.stream().filter(obj -> (obj.getRoleId() == role.getRoleId().intValue() && obj.getSpeed() < 0)).min(Comparator.comparing(PetRankingSource::getSpeed));
        } 
        if (optional.isPresent()) {
          petRankingSource = optional.get();
          synchronized (RankingList.petRankingListSource) {
            petPropertyRankingList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().filter(obj -> (obj.getSpeed() < 0)).sorted(Comparator.comparing(PetRankingSource::getSpeed)).collect(Collectors.toList());
          } 
          propertyValue = petRankingSource.getSpeed();
        } 
        break;
    } 
    if (petRankingSource != null) {
      int index = petPropertyRankingList.indexOf(petRankingSource);
      if (index != -1) {
        PetPropertyRankingField petPropertyRankingField = new PetPropertyRankingField(petRankingSource, ++index, serverId, propertyValue);
        MessagePusher.pushMessage(role, (Message)new RespMyRanking((byte)1, serial, serverId, (RankingField)petPropertyRankingField));
        return;
      } 
    } 
    MessagePusher.pushMessage(role, (Message)new RespMyRanking((byte)1, serial, serverId, null));
  }
  
  public void getMyClassifyRanking(Role role, int serial, byte serverId) {
    PetRankingSource petRankingSource = null;
    if (serial > 0) {
      Optional<PetRankingSource> optional;
      synchronized (RankingList.petRankingListSource) {
        optional = RankingList.petRankingListSource.stream().filter(obj -> (obj.getRoleId() == role.getRoleId().intValue() && obj.getPetSerial() == serial)).max(Comparator.comparing(PetRankingSource::getScore));
      } 
      if (optional.isPresent())
        petRankingSource = optional.get(); 
    } else {
      Optional<PetRankingSource> optional;
      synchronized (RankingList.petRankingListSource) {
        optional = RankingList.petRankingListSource.stream().filter(obj -> (obj.getRoleId() == role.getRoleId().intValue())).max(Comparator.comparing(PetRankingSource::getScore));
      } 
      if (optional.isPresent())
        petRankingSource = optional.get(); 
    } 
    if (petRankingSource != null) {
      int index = RankingList.petRankingListSource.indexOf(petRankingSource);
      if (index != -1) {
        PetClassifyRankingField petClassifyRankingField = new PetClassifyRankingField(petRankingSource, ++index, serverId);
        MessagePusher.pushMessage(role, (Message)new RespMyRanking((byte)0, serial, serverId, (RankingField)petClassifyRankingField));
        return;
      } 
    } 
    MessagePusher.pushMessage(role, (Message)new RespMyRanking((byte)0, serial, serverId, null));
  }
  
  public void getPetPropertyRankingList(Role role, byte type, byte serverId) {
    List<PetPropertyRankingField> petPropertyRankingFields = new ArrayList<>();
    if (RankingList.petRankingListSource.size() <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespPropertyPetRankingList(type, serverId, petPropertyRankingFields));
      return;
    } 
    int count = 100;
    List<PetRankingSource> pageList = null;
    switch (type) {
      case 0:
        synchronized (RankingList.petRankingListSource) {
          pageList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().filter(obj -> (obj.getSpeed() >= 0)).sorted(Comparator.comparing(PetRankingSource::getSpeed, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
        } 
        break;
      case 1:
        pageList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getAttack, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
        break;
      case 2:
        pageList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getLife, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
        break;
      case 3:
        pageList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().sorted(Comparator.comparing(PetRankingSource::getMana, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
        break;
      case 4:
        synchronized (RankingList.petRankingListSource) {
          pageList = (List<PetRankingSource>)RankingList.petRankingListSource.stream().filter(obj -> (obj.getSpeed() < 0)).sorted(Comparator.comparing(PetRankingSource::getSpeed)).limit(count).collect(Collectors.toList());
        } 
        break;
    } 
    if (pageList == null || pageList.size() <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespPropertyPetRankingList(type, serverId, petPropertyRankingFields));
      return;
    } 
    int[] index = { 0 };
    synchronized (pageList) {
      petPropertyRankingFields = (List<PetPropertyRankingField>)pageList.stream().map(petRankingField -> {
            int propertyValue = 0;
            switch (type) {
              case 0:
                propertyValue = petRankingField.getSpeed();
                break;
              case 1:
                propertyValue = petRankingField.getAttack();
                break;
              case 2:
                propertyValue = petRankingField.getLife();
                break;
              case 3:
                propertyValue = petRankingField.getMana();
                break;
              case 4:
                propertyValue = petRankingField.getSpeed();
                break;
            } 
            return new PetPropertyRankingField(petRankingField, index[0] = index[0] + 1, serverId, propertyValue);
          }).collect(Collectors.toList());
    } 
    MessagePusher.pushMessage(role, (Message)new RespPropertyPetRankingList(type, serverId, petPropertyRankingFields));
  }
}
