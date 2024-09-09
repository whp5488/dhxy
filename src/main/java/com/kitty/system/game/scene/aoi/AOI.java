package com.kitty.system.game.scene.aoi;

import java.util.Set;
import com.kitty.mina.message.Message;
import com.kitty.system.game.role.domain.Role;

public abstract class AOI
{
    private int id;
    
    public AOI() {
    }
    
    public abstract void enter(Role role);
    
    public abstract void leave(long uid);
    
    public abstract void move(Role role);
    
    public abstract void goTo(Role role);
    
    public abstract void sendMessages(Role role, Message... messages);
    
    public abstract void giveAppear(Role role, Set<Long> enters);
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}
