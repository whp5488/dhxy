
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
    @EventHandler({ EventType.CHANGE_EQUIP_WEAR })
    public void handleChangeEquipEvent(final ChangeEquipEvent changeEquipEvent) {
        final Role role = changeEquipEvent.getRole();
        final WearEquip wearEquip = changeEquipEvent.getWearEquip();
        if (role == null || wearEquip == null) {
            return;
        }
        this.sync(role);
    }
    
    @EventHandler({ EventType.ROLE_CHANGE })
    public void handleRoleChangeEvent(final RoleChangeEvent roleChangeEvent) {
        this.sync(roleChangeEvent.getRole());
    }
    
    @EventHandler({ EventType.ROLE_ASPECT_CHANGE })
    public void handleRoleAspectChangeEvent(final RoleAspectChangeEvent roleAspectChangeEvent) {
        this.sync(roleAspectChangeEvent.getRole());
    }
    
    public void sync(final Role role) {
        final PetRankingSource petRankingSource = RankingList.petRankingListSource.stream().filter(e -> role.getRoleId().equals(e.getRoleId())).findFirst().orElse(null);
        if (petRankingSource != null) {
            petRankingSource.rests(role);
        }
        final PractitionersSource practitionersSource = RankingList.practitionersSources.stream().filter(e -> role.getRoleId().equals(e.getRoleId())).findFirst().orElse(null);
        if (practitionersSource != null) {
            practitionersSource.rests(role);
        }
        final ComprehensiveStrengthSource strengthSource = RankingList.comprehensiveStrengthSources.stream().filter(e -> role.getRoleId().equals(e.getRoleId())).findFirst().orElse(null);
        if (strengthSource != null) {
            strengthSource.rests(role);
        }
        final WeaponsRankingSource weaponsRankingSource = RankingList.weaponsRankingSources.stream().filter(e -> role.getRoleId().equals(e.getRoleId())).findFirst().orElse(null);
        if (weaponsRankingSource != null) {
            weaponsRankingSource.rests(role);
        }
        final QlkRankingSource qlkRankingSource = RankingList.qlkRankingSources.stream().filter(e -> role.getRoleId().equals(e.getRoleId())).findFirst().orElse(null);
        if (qlkRankingSource != null) {
            qlkRankingSource.rests(role);
        }
    }
}
