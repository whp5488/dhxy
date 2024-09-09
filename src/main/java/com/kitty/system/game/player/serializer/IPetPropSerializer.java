package com.kitty.system.game.player.serializer;

import com.kitty.system.game.pet.domain.Pet;

public interface IPetPropSerializer
{
    public abstract void serialize(Pet pet);
    
    public abstract void deserialize(Pet pet);
}
