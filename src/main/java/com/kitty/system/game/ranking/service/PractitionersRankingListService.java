package com.kitty.system.game.ranking.service;

import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.ranking.RoleChangeEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.system.game.ranking.message.RespPractitionersRankingList;
import com.kitty.system.game.ranking.pojo.PractitionersSource;
import com.kitty.system.game.ranking.pojo.RankingField;
import com.kitty.system.game.ranking.pojo.field.PractitionersField;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PractitionersRankingListService {
  @Autowired
  RoleService roleService;
  
  @EventHandler({EventType.ROLE_EXP_CHANGE})
  public void handlePractitionersChangeEvent(RoleChangeEvent roleChangeEvent) {
    Role role = roleChangeEvent.getRole();
    Optional<PractitionersSource> optional = null;
    synchronized (RankingList.practitionersSources) {
      optional = RankingList.practitionersSources.stream().filter(obj -> (obj.getRoleId() == role.getRoleId().intValue())).findAny();
    } 
    if (optional != null && optional.isPresent()) {
      PractitionersSource practitionersSource = optional.get();
      if (practitionersSource.getRelive() != role.getRelive().intValue())
        practitionersSource.setRelive(role.getRelive().intValue()); 
      if (practitionersSource.getLevel() != role.getLevel().intValue()) {
        practitionersSource.setLevel(role.getLevel().intValue());
        long expForRebirthLevel = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_HERO.getValue(), role.getRelive().intValue(), role.getLevel().intValue() + 1);
        practitionersSource.setNextLevelNeedExp(expForRebirthLevel);
      } 
      if (practitionersSource.getExp() != role.getExp())
        practitionersSource.setExp(role.getExp()); 
    } else {
      long expForRebirthLevel = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_HERO.getValue(), role.getRelive().intValue(), role.getLevel().intValue() + 1);
      PractitionersSource practitionersSource = new PractitionersSource(role, expForRebirthLevel);
      RankingList.practitionersSources.add(practitionersSource);
    } 
  }
  
  public void getPractitionersRankingList(Role role) {
    int count = 100;
    List<PractitionersSource> returnSources = (List<PractitionersSource>)RankingList.practitionersSources.stream().sorted(Comparator.comparing(PractitionersSource::getReliveAndLevel, Comparator.reverseOrder()).thenComparing(PractitionersSource::getExp, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
    int[] index = { 0 };
    List<PractitionersField> returnFeilds = null;
    synchronized (returnSources) {
      returnFeilds = (List<PractitionersField>)returnSources.stream().limit(count).map(practitionersSource -> new PractitionersField(practitionersSource, index[0] = index[0] + 1, (byte)0)).collect(Collectors.toList());
    } 
    MessagePusher.pushMessage(role, (Message)new RespPractitionersRankingList(returnFeilds));
  }
  
  public void getMyPractitionersRanking(Role role) {
    PractitionersSource practitionersSource = new PractitionersSource(role);
    int index = RankingList.practitionersSources.indexOf(practitionersSource);
    if (index == -1)
      MessagePusher.pushMessage(role, (Message)new RespMyRanking((byte)3, 0, (byte)0, null)); 
    PractitionersField practitionersField = new PractitionersField(practitionersSource, ++index, (byte)0);
    MessagePusher.pushMessage(role, (Message)new RespMyRanking((byte)3, 0, (byte)0, (RankingField)practitionersField));
  }
}
