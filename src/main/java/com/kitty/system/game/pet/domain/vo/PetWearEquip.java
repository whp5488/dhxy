package com.kitty.system.game.pet.domain.vo;

import com.kitty.system.game.equip.domain.WearEquip;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class PetWearEquip implements Serializable {
  List<WearEquip> list;
}
