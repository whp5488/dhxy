package com.kitty.system.game.player.serializer;

import com.kitty.system.game.horse.domain.Horse;

public interface IHorsePropSerializer
{
    public abstract void serialize(Horse obj);
    
    public abstract void deserialize(Horse obj);
}
