package com.kitty.system.game.ranking.service;

import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import com.kitty.system.game.ranking.pojo.WeaponsRankingSource;
import com.kitty.system.game.ranking.pojo.ComprehensiveStrengthSource;
import com.kitty.system.game.ranking.pojo.PractitionersSource;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.pojo.PetRankingSource;
import com.kitty.listener.event.RoleAspectChangeEvent;
import com.kitty.listener.event.RoleChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.ChangeEquipEvent;
import org.springframework.stereotype.Component;

@Component
public class SyncAppearance
{
    public SyncAppearance() {
    }
    
    @EventHandler({ EventType.CHANGE_EQUIP_WEAR })
    public void handleChangeEquipEvent(ChangeEquipEvent changeEquipEvent) {
        Role role = changeEquipEvent.getRole();
        WearEquip wearEquip = changeEquipEvent.getWearEquip();
        if (role == null || wearEquip == null) {
            return;
        }
        this.sync(role);
        return;
    }
    
    @EventHandler({ EventType.ROLE_CHANGE })
    public void handleRoleChangeEvent(RoleChangeEvent roleChangeEvent) {
        this.sync(roleChangeEvent.getRole());
    }
    
    @EventHandler({ EventType.ROLE_ASPECT_CHANGE })
    public void handleRoleAspectChangeEvent(RoleAspectChangeEvent roleAspectChangeEvent) {
        this.sync(roleAspectChangeEvent.getRole());
    }
    
    public void sync(Role role) {
        PetRankingSource petRankingSource = (PetRankingSource)RankingList.petRankingListSource.stream().filter(e -> role.getRoleId().equals(Integer.valueOf(e.getRoleId()))).findFirst().orElse(null);
        if (petRankingSource != null) {
            petRankingSource.rests(role);
        }
        PractitionersSource practitionersSource = (PractitionersSource)RankingList.practitionersSources.stream().filter(e -> role.getRoleId().equals(Integer.valueOf(e.getRoleId()))).findFirst().orElse(null);
        if (practitionersSource != null) {
            practitionersSource.rests(role);
        }
        ComprehensiveStrengthSource strengthSource = (ComprehensiveStrengthSource)RankingList.comprehensiveStrengthSources.stream().filter(e -> role.getRoleId().equals(Integer.valueOf(e.getRoleId()))).findFirst().orElse(null);
        if (strengthSource != null) {
            strengthSource.rests(role);
        }
        WeaponsRankingSource weaponsRankingSource = (WeaponsRankingSource)RankingList.weaponsRankingSources.stream().filter(e -> role.getRoleId().equals(Integer.valueOf(e.getRoleId()))).findFirst().orElse(null);
        if (weaponsRankingSource != null) {
            weaponsRankingSource.rests(role);
        }
        QlkRankingSource qlkRankingSource = (QlkRankingSource)RankingList.qlkRankingSources.stream().filter(e -> role.getRoleId().equals(Integer.valueOf(e.getRoleId()))).findFirst().orElse(null);
        if (qlkRankingSource != null) {
            qlkRankingSource.rests(role);
        }
    }
}
