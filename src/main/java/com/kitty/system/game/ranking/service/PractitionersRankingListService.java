
package com.kitty.system.game.ranking.service;

import com.kitty.system.game.ranking.pojo.RankingField;
import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.message.RespPractitionersRankingList;
import com.kitty.system.game.ranking.pojo.field.PractitionersField;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import java.util.List;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import java.util.Optional;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.ranking.pojo.PractitionersSource;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.listener.event.ranking.RoleChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class PractitionersRankingListService
{
    @Autowired
    RoleService roleService;
    
    @EventHandler({ EventType.ROLE_EXP_CHANGE })
    public void handlePractitionersChangeEvent(final RoleChangeEvent roleChangeEvent) {
        final Role role = roleChangeEvent.getRole();
        Optional<PractitionersSource> optional = null;
        synchronized (RankingList.practitionersSources) {
            optional = RankingList.practitionersSources.stream().filter(obj -> obj.getRoleId() == role.getRoleId()).findAny();
        }
        if (optional != null && optional.isPresent()) {
            final PractitionersSource practitionersSource = optional.get();
            if (practitionersSource.getRelive() != role.getRelive()) {
                practitionersSource.setRelive(role.getRelive());
            }
            if (practitionersSource.getLevel() != role.getLevel()) {
                practitionersSource.setLevel(role.getLevel());
                final long expForRebirthLevel = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_HERO.getValue(), role.getRelive(), role.getLevel() + 1);
                practitionersSource.setNextLevelNeedExp(expForRebirthLevel);
            }
            if (practitionersSource.getExp() != role.getExp()) {
                practitionersSource.setExp(role.getExp());
            }
        }
        else {
            final long expForRebirthLevel2 = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_HERO.getValue(), role.getRelive(), role.getLevel() + 1);
            final PractitionersSource practitionersSource2 = new PractitionersSource(role, expForRebirthLevel2);
            RankingList.practitionersSources.add(practitionersSource2);
        }
    }
    
    public void getPractitionersRankingList(final Role role) {
        final int count = 100;
        final List<PractitionersSource> returnSources = RankingList.practitionersSources.stream().sorted(Comparator.comparing(PractitionersSource::getReliveAndLevel, Comparator.reverseOrder()).thenComparing(PractitionersSource::getExp, Comparator.reverseOrder())).limit(count).collect(Collectors.toList());
        final int[] index = { 0 };
        List<PractitionersField> returnFeilds = null;
        synchronized (returnSources) {
            returnFeilds = returnSources.stream().limit(count).map(practitionersSource -> {
                final PractitionersField practitionersField = new PractitionersField(practitionersSource, ++index[0], (byte)0);
                return practitionersField;
            }).collect(Collectors.toList());
        }
        MessagePusher.pushMessage(role, new RespPractitionersRankingList(returnFeilds));
    }
    
    public void getMyPractitionersRanking(final Role role) {
        final PractitionersSource practitionersSource = new PractitionersSource(role);
        int index = RankingList.practitionersSources.indexOf(practitionersSource);
        if (index == -1) {
            MessagePusher.pushMessage(role, new RespMyRanking((byte)3, 0, (byte)0, null));
        }
        final PractitionersField practitionersField = new PractitionersField(practitionersSource, ++index, (byte)0);
        MessagePusher.pushMessage(role, new RespMyRanking((byte)3, 0, (byte)0, practitionersField));
    }
}
