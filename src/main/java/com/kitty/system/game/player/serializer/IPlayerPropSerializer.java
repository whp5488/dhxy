
package com.kitty.system.game.player.serializer;

import com.kitty.system.game.role.domain.Role;

public interface IPlayerPropSerializer
{
    void serialize(final Role p0);
    
    void deserialize(final Role p0);
}
