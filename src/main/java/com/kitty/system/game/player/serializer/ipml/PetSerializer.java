
package com.kitty.system.game.player.serializer.ipml;

import com.kitty.system.game.pet.domain.vo.PetCorrection;
import com.kitty.system.game.pet.domain.PetBase;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.pet.domain.Pet;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.kitty.system.game.player.serializer.IPetPropSerializer;

public class PetSerializer implements IPetPropSerializer
{
    Logger logger;
    
    public PetSerializer() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    @Override
    public void serialize(final Pet pet) {
        final String base = JsonUtils.object2String(pet.getPetBase());
        final String correctionInfo = JsonUtils.object2String(pet.getCorrectionInfo());
        pet.setBase(base);
        pet.setCorrectionInfoJson(correctionInfo);
    }
    
    @Override
    public void deserialize(final Pet pet) {
        final String base = pet.getBase();
        final String correctionInfo = pet.getCorrectionInfoJson();
        if (base != null && !base.isEmpty()) {
            pet.setPetBase(JsonUtils.string2Object(base, PetBase.class));
        }
        if (correctionInfo != null && !correctionInfo.isEmpty()) {
            pet.setCorrectionInfo(JsonUtils.string2Object(correctionInfo, PetCorrection.class));
        }
    }
}
