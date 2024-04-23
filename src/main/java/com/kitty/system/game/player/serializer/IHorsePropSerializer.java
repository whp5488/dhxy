package com.kitty.system.game.player.serializer;

import com.kitty.system.game.horse.domain.Horse;

public interface IHorsePropSerializer {
  void serialize(Horse paramHorse);
  
  void deserialize(Horse paramHorse);
}
