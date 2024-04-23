package com.kitty.system.game.player.serializer.ipml;

import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.PetBase;
import com.kitty.system.game.pet.domain.vo.PetCorrection;
import com.kitty.system.game.player.serializer.IPetPropSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PetSerializer implements IPetPropSerializer {
  Logger logger = LoggerFactory.getLogger(getClass());
  
  public void serialize(Pet pet) {
    String base = JsonUtils.object2String(pet.getPetBase());
    String correctionInfo = JsonUtils.object2String(pet.getCorrectionInfo());
    pet.setBase(base);
    pet.setCorrectionInfoJson(correctionInfo);
  }
  
  public void deserialize(Pet pet) {
    String base = pet.getBase();
    String correctionInfo = pet.getCorrectionInfoJson();
    if (base != null && !base.isEmpty())
      pet.setPetBase((PetBase)JsonUtils.string2Object(base, PetBase.class)); 
    if (correctionInfo != null && !correctionInfo.isEmpty())
      pet.setCorrectionInfo((PetCorrection)JsonUtils.string2Object(correctionInfo, PetCorrection.class)); 
  }
}
