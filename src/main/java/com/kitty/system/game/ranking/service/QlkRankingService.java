package com.kitty.system.game.ranking.service;

import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.system.game.ranking.pojo.field.QlkRankingField;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.message.RespQlkRankingList;
import java.util.stream.Collectors;
import java.util.Comparator;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import java.util.List;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.ranking.RankingList;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class QlkRankingService
{
    @Autowired
    RoleService roleService;
    
    public QlkRankingService() {
    }
    
    public void getQlkRinkingList(Role role, short qlkType, byte serverId) {
        List<QlkRankingField> qlkRankingFields = new ArrayList<>();
        if (RankingList.qlkRankingSources == null || RankingList.qlkRankingSources.size() <= 0) {
            MessagePusher.pushMessage(role, new RespQlkRankingList(qlkType, qlkRankingFields));
            return;
        }
        PRO_DICT_TYPE dict = PRO_DICT_TYPE.getProDictTypeByDist((int)qlkType);
        List<QlkRankingSource> limitQlkRankingSources = new ArrayList<>();
        int count = 100;
        switch (dict) {
            case Q_KJ: {
                limitQlkRankingSources = (List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkj, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
                break;
            }
            case Q_KM: {
                limitQlkRankingSources = (List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkm, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
                break;
            }
            case Q_KS: {
                limitQlkRankingSources = (List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlks, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
                break;
            }
            case Q_KH: {
                limitQlkRankingSources = (List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkh, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
                break;
            }
            case Q_KT: {
                limitQlkRankingSources = (List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkt, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
                break;
            }
            default: {
                MessagePusher.pushMessage(role, new RespQlkRankingList(qlkType, qlkRankingFields));
                return;
            }
        }
        int[] index = { 0 };
        synchronized (limitQlkRankingSources) {
            qlkRankingFields = (List)limitQlkRankingSources.stream().map(obj -> {
                QlkRankingField qlkRankingField = new QlkRankingField(obj, ++index[0], serverId);
                qlkRankingField.setQlkValues(obj.getQlkj());
                switch (dict) {
                    case Q_KM: {
                        qlkRankingField.setQlkValues(obj.getQlkm());
                        break;
                    }
                    case Q_KS: {
                        qlkRankingField.setQlkValues(obj.getQlks());
                        break;
                    }
                    case Q_KH: {
                        qlkRankingField.setQlkValues(obj.getQlkh());
                        break;
                    }
                    case Q_KT: {
                        qlkRankingField.setQlkValues(obj.getQlkt());
                        break;
                    }
                }
                return qlkRankingField;
            }).collect(Collectors.toList());
        }
        MessagePusher.pushMessage(role, new RespQlkRankingList(qlkType, qlkRankingFields));
        return;
    }
    
    public void getMyQlkRanking(Role role, int serial, byte serverId) {
        List<QlkRankingSource> qlks;
        synchronized (RankingList.qlkRankingSources) {
            qlks = (List)RankingList.qlkRankingSources.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId()).collect(Collectors.toList());
        }
        if (qlks == null || qlks.size() <= 0) {
            MessagePusher.pushMessage(role, new RespMyRanking((byte)5, serial, serverId, null));
            return;
        }
        QlkRankingSource qlkRankingSource = (QlkRankingSource)qlks.get(0);
        PRO_DICT_TYPE dict = PRO_DICT_TYPE.getProDictTypeByDist(serial);
        if (dict == null) {
            MessagePusher.pushMessage(role, new RespMyRanking((byte)5, serial, serverId, null));
            return;
        }
        int count = 100;
        int index = -1;
        switch (dict) {
            case Q_KJ: {
                index = RankingList.qlkRankingSources.indexOf(qlkRankingSource);
                break;
            }
            case Q_KM: {
                index = ((List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkm, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList())).indexOf(qlkRankingSource);
                break;
            }
            case Q_KS: {
                index = ((List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlks, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList())).indexOf(qlkRankingSource);
                break;
            }
            case Q_KH: {
                index = ((List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkh, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList())).indexOf(qlkRankingSource);
                break;
            }
            case Q_KT: {
                index = ((List)RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkt, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList())).indexOf(qlkRankingSource);
                break;
            }
            default: {
                MessagePusher.pushMessage(role, new RespMyRanking((byte)5, serial, serverId, null));
                return;
            }
        }
        QlkRankingField qlkRankingField = new QlkRankingField(qlkRankingSource, ++index, serverId);
        MessagePusher.pushMessage(role, new RespMyRanking((byte)5, serial, serverId, qlkRankingField));
        return;
    }
}
