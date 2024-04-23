package com.kitty.system.game.player.serializer;

import com.kitty.system.game.role.domain.Role;

public interface IPlayerPropSerializer {
  void serialize(Role paramRole);
  
  void deserialize(Role paramRole);
}
