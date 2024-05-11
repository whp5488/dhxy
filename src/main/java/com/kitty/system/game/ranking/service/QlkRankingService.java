
package com.kitty.system.game.ranking.service;

import com.kitty.system.game.ranking.pojo.RankingField;
import com.kitty.system.game.ranking.message.RespMyRanking;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import java.util.List;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.message.RespQlkRankingList;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.pojo.field.QlkRankingField;
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
    
    public void getQlkRinkingList(final Role role, final short qlkType, final byte serverId) {
        List<QlkRankingField> qlkRankingFields = new ArrayList<QlkRankingField>();
        if (RankingList.qlkRankingSources == null || RankingList.qlkRankingSources.size() <= 0) {
            MessagePusher.pushMessage(role, new RespQlkRankingList(qlkType, qlkRankingFields));
            return;
        }
        final PRO_DICT_TYPE dict = PRO_DICT_TYPE.getProDictTypeByDist(qlkType);
        List<QlkRankingSource> limitQlkRankingSources = new ArrayList<QlkRankingSource>();
        final int count = 100;
        switch (dict) {
            case Q_KJ: {
                limitQlkRankingSources = RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkj, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
                break;
            }
            case Q_KM: {
                limitQlkRankingSources = RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkm, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
                break;
            }
            case Q_KS: {
                limitQlkRankingSources = RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlks, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
                break;
            }
            case Q_KH: {
                limitQlkRankingSources = RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkh, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
                break;
            }
            case Q_KT: {
                limitQlkRankingSources = RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkt, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
                break;
            }
            default: {
                MessagePusher.pushMessage(role, new RespQlkRankingList(qlkType, qlkRankingFields));
                return;
            }
        }
        final int[] index = { 0 };
        synchronized (limitQlkRankingSources) {
            qlkRankingFields = limitQlkRankingSources.stream().map(obj -> {
                final QlkRankingField qlkRankingField = new QlkRankingField(obj, ++index[0], serverId);
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
    }
    
    public void getMyQlkRanking(final Role role, final int serial, final byte serverId) {
        final List<QlkRankingSource> qlks;
        synchronized (RankingList.qlkRankingSources) {
            qlks = RankingList.qlkRankingSources.stream().filter(obj -> obj.getRoleId() == role.getRoleId()).collect(Collectors.toList());
        }
        if (qlks == null || qlks.size() <= 0) {
            MessagePusher.pushMessage(role, new RespMyRanking((byte)5, serial, serverId, null));
            return;
        }
        final QlkRankingSource qlkRankingSource = qlks.get(0);
        final PRO_DICT_TYPE dict = PRO_DICT_TYPE.getProDictTypeByDist(serial);
        if (dict == null) {
            MessagePusher.pushMessage(role, new RespMyRanking((byte)5, serial, serverId, null));
            return;
        }
        final int count = 100;
        int index = -1;
        switch (dict) {
            case Q_KJ: {
                index = RankingList.qlkRankingSources.indexOf(qlkRankingSource);
                break;
            }
            case Q_KM: {
                index = RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkm, Comparator.reverseOrder())).limit(count).collect(Collectors.toList()).indexOf(qlkRankingSource);
                break;
            }
            case Q_KS: {
                index = RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlks, Comparator.reverseOrder())).limit(count).collect(Collectors.toList()).indexOf(qlkRankingSource);
                break;
            }
            case Q_KH: {
                index = RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkh, Comparator.reverseOrder())).limit(count).collect(Collectors.toList()).indexOf(qlkRankingSource);
                break;
            }
            case Q_KT: {
                index = RankingList.qlkRankingSources.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkt, Comparator.reverseOrder())).limit(count).collect(Collectors.toList()).indexOf(qlkRankingSource);
                break;
            }
            default: {
                MessagePusher.pushMessage(role, new RespMyRanking((byte)5, serial, serverId, null));
                return;
            }
        }
        final QlkRankingField qlkRankingField = new QlkRankingField(qlkRankingSource, ++index, serverId);
        MessagePusher.pushMessage(role, new RespMyRanking((byte)5, serial, serverId, qlkRankingField));
    }
}
