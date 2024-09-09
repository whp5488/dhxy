package com.kitty.system.game.player.serializer;

import com.kitty.system.game.role.domain.Role;

public interface IPlayerPropSerializer
{
    public abstract void serialize(Role player);
    
    public abstract void deserialize(Role player);
}
