package com.kitty.system.game.ranking.service;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.system.game.ranking.message.RespQlkRankingList;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import com.kitty.system.game.ranking.pojo.RankingField;
import com.kitty.system.game.ranking.pojo.field.QlkRankingField;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QlkRankingService {
  @Autowired
  RoleService roleService;
  
  public void getQlkRinkingList(Role role, short qlkType, byte serverId) {
    List<QlkRankingField> qlkRankingFields = new ArrayList<>();
    if (RankingList.qlkRankingSources == null || RankingList.qlkRankingSources.size() <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespQlkRankingList(qlkType, qlkRankingFields));
      return;
    } 
    PRO_DICT_TYPE dict = PRO_DICT_TYPE.getProDictTypeByDist(qlkType);
    List<QlkRankingSource> limitQlkRankingSources = new ArrayList<>();
    int count = 100;
    switch (dict) {
      case Q_KJ:
        limitQlkRankingSources = (List<QlkRankingSource>)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkj, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
        break;
      case Q_KM:
        limitQlkRankingSources = (List<QlkRankingSource>)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkm, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
        break;
      case Q_KS:
        limitQlkRankingSources = (List<QlkRankingSource>)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlks, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
        break;
      case Q_KH:
        limitQlkRankingSources = (List<QlkRankingSource>)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkh, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
        break;
      case Q_KT:
        limitQlkRankingSources = (List<QlkRankingSource>)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkt, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
        break;
      default:
        MessagePusher.pushMessage(role, (Message)new RespQlkRankingList(qlkType, qlkRankingFields));
        return;
    } 
    int[] index = { 0 };
    synchronized (limitQlkRankingSources) {
      qlkRankingFields = (List<QlkRankingField>)limitQlkRankingSources.stream().map(obj -> {
            QlkRankingField qlkRankingField = new QlkRankingField(obj, index[0] = index[0] + 1, serverId);
            qlkRankingField.setQlkValues(obj.getQlkj());
            switch (dict) {
              case Q_KM:
                qlkRankingField.setQlkValues(obj.getQlkm());
                break;
              case Q_KS:
                qlkRankingField.setQlkValues(obj.getQlks());
                break;
              case Q_KH:
                qlkRankingField.setQlkValues(obj.getQlkh());
                break;
              case Q_KT:
                qlkRankingField.setQlkValues(obj.getQlkt());
                break;
            } 
            return qlkRankingField;
          }).collect(Collectors.toList());
    } 
    MessagePusher.pushMessage(role, (Message)new RespQlkRankingList(qlkType, qlkRankingFields));
  }
  
  public void getMyQlkRanking(Role role, int serial, byte serverId) {
    List<QlkRankingSource> qlks;
    synchronized (RankingList.qlkRankingSources) {
      qlks = (List<QlkRankingSource>)RankingList.qlkRankingSources.stream().filter(obj -> (obj.getRoleId() == role.getRoleId().intValue())).collect(Collectors.toList());
    } 
    if (qlks == null || qlks.size() <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespMyRanking((byte)5, serial, serverId, null));
      return;
    } 
    QlkRankingSource qlkRankingSource = qlks.get(0);
    PRO_DICT_TYPE dict = PRO_DICT_TYPE.getProDictTypeByDist(serial);
    if (dict == null) {
      MessagePusher.pushMessage(role, (Message)new RespMyRanking((byte)5, serial, serverId, null));
      return;
    } 
    int count = 100;
    int index = -1;
    switch (dict) {
      case Q_KJ:
        index = RankingList.qlkRankingSources.indexOf(qlkRankingSource);
        break;
      case Q_KM:
        index = ((List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkm, Comparator.reverseOrder())).limit(count).collect(Collectors.toList())).indexOf(qlkRankingSource);
        break;
      case Q_KS:
        index = ((List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlks, Comparator.reverseOrder())).limit(count).collect(Collectors.toList())).indexOf(qlkRankingSource);
        break;
      case Q_KH:
        index = ((List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkh, Comparator.reverseOrder())).limit(count).collect(Collectors.toList())).indexOf(qlkRankingSource);
        break;
      case Q_KT:
        index = ((List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkt, Comparator.reverseOrder())).limit(count).collect(Collectors.toList())).indexOf(qlkRankingSource);
        break;
      default:
        MessagePusher.pushMessage(role, (Message)new RespMyRanking((byte)5, serial, serverId, null));
        return;
    } 
    QlkRankingField qlkRankingField = new QlkRankingField(qlkRankingSource, ++index, serverId);
    MessagePusher.pushMessage(role, (Message)new RespMyRanking((byte)5, serial, serverId, (RankingField)qlkRankingField));
  }
}
