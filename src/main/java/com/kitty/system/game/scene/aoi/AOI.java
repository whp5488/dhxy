
package com.kitty.system.game.scene.aoi;

import java.util.Set;
import com.kitty.mina.message.Message;
import com.kitty.system.game.role.domain.Role;

public abstract class AOI
{
    private int id;
    
    public abstract void enter(final Role p0);
    
    public abstract void leave(final long p0);
    
    public abstract void move(final Role p0);
    
    public abstract void goTo(final Role p0);
    
    public abstract void sendMessages(final Role p0, final Message... p1);
    
    public abstract void giveAppear(final Role p0, final Set<Long> p1);
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
}
