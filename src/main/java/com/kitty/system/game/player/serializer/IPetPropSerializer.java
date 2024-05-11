
package com.kitty.system.game.player.serializer;

import com.kitty.system.game.pet.domain.Pet;

public interface IPetPropSerializer
{
    void serialize(final Pet p0);
    
    void deserialize(final Pet p0);
}
