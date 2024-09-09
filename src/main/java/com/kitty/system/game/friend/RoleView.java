package com.kitty.system.game.friend;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.player.PlayerProfile;
import com.kitty.system.game.role.domain.Role;

public class RoleView
{
    private Role role;
    private PlayerProfile playerProfile;
    
    public RoleView(long id) {
        this.role = SpringUtils.getRoleService().getPlayer(id);
        if (this.role == null) {
            this.playerProfile = SpringUtils.getPlayerService().queryPlayerProfile(id);
        }
    }
    
    public RoleView(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("RoleView传入参数role不能为null");
        }
        this.role = role;
        return;
    }
    
    public boolean isOnline() {
        return this.role != null;
    }
    
    public long getId() {
        return (long)((this.role != null) ? this.role.getId() : this.playerProfile.getId());
    }
    
    public int getRoleId() {
        return (int)((this.role != null) ? this.role.getRoleId() : this.playerProfile.getRoleId());
    }
    
    public String getName() {
        return (this.role != null) ? this.role.getName() : this.playerProfile.getName();
    }
    
    public int getIcon() {
        return (this.role != null) ? this.role.getIcon() : this.playerProfile.getIcon();
    }
    
    public int getLevel() {
        return (this.role != null) ? ((int)this.role.getLevel()) : this.playerProfile.getLevel();
    }
    
    public int getFaction() {
        return (this.role != null) ? ((int)this.role.getFaction()) : this.playerProfile.getFaction();
    }
    
    public int getRace() {
        return (this.role != null) ? ((int)this.role.getRace()) : this.playerProfile.getRace();
    }
    
    public int getGender() {
        return (this.role != null) ? ((int)this.role.getSex()) : this.playerProfile.getSex();
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }
}
