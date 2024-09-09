package com.kitty.system.game.ranking.service;

import com.kitty.system.game.ranking.message.RespMyRanking;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.ranking.message.RespPractitionersRankingList;
import com.kitty.system.game.ranking.pojo.field.PractitionersField;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import java.util.Optional;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.ranking.pojo.PractitionersSource;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.wardef.LOGIC;
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
    
    public PractitionersRankingListService() {
    }
    
    @EventHandler({ EventType.ROLE_EXP_CHANGE })
    public void handlePractitionersChangeEvent(RoleChangeEvent roleChangeEvent) {
        Role role = roleChangeEvent.getRole();
        Optional<PractitionersSource> optional = null;
        synchronized (RankingList.practitionersSources) {
            optional = RankingList.practitionersSources.stream().filter(obj -> obj.getRoleId() == (int)role.getRoleId()).findAny();
        }
        if (optional != null && optional.isPresent()) {
            PractitionersSource practitionersSource2 = (PractitionersSource)optional.get();
            if (practitionersSource2.getRelive() != (int)role.getRelive()) {
                practitionersSource2.setRelive((int)role.getRelive());
            }
            if (practitionersSource2.getLevel() != (int)role.getLevel()) {
                practitionersSource2.setLevel((int)role.getLevel());
                long expForRebirthLevel2 = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_HERO.getValue(), (int)role.getRelive(), (int)role.getLevel() + 1);
                practitionersSource2.setNextLevelNeedExp(expForRebirthLevel2);
            }
            if (practitionersSource2.getExp() != role.getExp()) {
                practitionersSource2.setExp(role.getExp());
            }
        }
        else {
            long expForRebirthLevel = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_HERO.getValue(), (int)role.getRelive(), (int)role.getLevel() + 1);
            PractitionersSource practitionersSource = new PractitionersSource(role, expForRebirthLevel);
            RankingList.practitionersSources.add(practitionersSource);
        }
    }
    
    public void getPractitionersRankingList(Role role) {
        int count = 100;
        List<PractitionersSource> returnSources = (List)RankingList.practitionersSources.stream().sorted(Comparator.comparing(PractitionersSource::getReliveAndLevel, Comparator.reverseOrder()).thenComparing(PractitionersSource::getExp, Comparator.reverseOrder())).limit((long)count).collect(Collectors.toList());
        int[] index = { 0 };
        List<PractitionersField> returnFeilds = null;
        synchronized (returnSources) {
            returnFeilds = (List)returnSources.stream().limit((long)count).map(practitionersSource -> {
                PractitionersField practitionersField = new PractitionersField(practitionersSource, ++index[0], (byte)0);
                return practitionersField;
            }).collect(Collectors.toList());
        }
        MessagePusher.pushMessage(role, new RespPractitionersRankingList(returnFeilds));
    }
    
    public void getMyPractitionersRanking(Role role) {
        PractitionersSource practitionersSource = new PractitionersSource(role);
        int index = RankingList.practitionersSources.indexOf(practitionersSource);
        if (index == -1) {
            MessagePusher.pushMessage(role, new RespMyRanking((byte)3, 0, (byte)0, null));
        }
        PractitionersField practitionersField = new PractitionersField(practitionersSource, ++index, (byte)0);
        MessagePusher.pushMessage(role, new RespMyRanking((byte)3, 0, (byte)0, practitionersField));
    }
}
