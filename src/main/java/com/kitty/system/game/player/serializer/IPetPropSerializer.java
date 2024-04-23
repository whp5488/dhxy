package com.kitty.system.game.player.serializer;

import com.kitty.system.game.pet.domain.Pet;

public interface IPetPropSerializer {
  void serialize(Pet paramPet);
  
  void deserialize(Pet paramPet);
}
