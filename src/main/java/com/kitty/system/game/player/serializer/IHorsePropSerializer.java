
package com.kitty.system.game.player.serializer;

import com.kitty.system.game.horse.domain.Horse;

public interface IHorsePropSerializer
{
    void serialize(final Horse p0);
    
    void deserialize(final Horse p0);
}
