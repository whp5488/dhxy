
package com.kitty.system.game.equip.helper;

import java.util.Iterator;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import java.util.ArrayList;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.vo.FiedEquip;
import java.util.List;
import java.util.Map;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.message.RespPetEquipDisplace;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("PetEquipDisplace")
public class PetEquipDisplace extends UpgradeHandler
{
    @Override
    public String acceptUpgrade(final Role role, final Integer pos, final Integer type, final String param, final String cause) {
        final Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip(role.getId());
        if (petEquipMap == null || petEquipMap.size() <= 0) {
            return null;
        }
        final String[] split = param.split("-");
        if (split.length < 1) {
            return null;
        }
        if (!NumberUtil.isNumber(split[0]) || !NumberUtil.isNumber(split[1])) {
            return null;
        }
        final long main = Long.valueOf(split[0]);
        final long second = Long.valueOf(split[1]);
        final PetEquip equipMain = petEquipMap.get(main);
        final PetEquip equipSecond = petEquipMap.get(second);
        if (equipMain == null || equipSecond == null) {
            return null;
        }
        final List<FiedEquip> fieldEquipMain = equipMain.getField();
        final List<FiedEquip> fieldEquipSecond = equipSecond.getField();
        final List<FiedEquip> equipMainDisplace = this.displace(fieldEquipMain, this.advAttrLib(fieldEquipSecond));
        final List<FiedEquip> equipSecondDisplace = this.displace(fieldEquipMain, this.advAttrLib(fieldEquipMain));
        equipMain.setField(equipMainDisplace);
        equipSecond.setField(equipSecondDisplace);
        equipMain.setField(MakeAttribute.calEquipScore(equipMain));
        equipSecond.setField(MakeAttribute.calEquipScore(equipSecond));
        equipMain.save();
        equipSecond.save();
        MessagePusher.pushMessage(role.getId(), new RespPetEquipDisplace(split[0], split[1]));
        this.equipService.refreshPetEquip(role, equipMain);
        this.equipService.refreshPetEquip(role, equipSecond);
        return null;
    }
    
    public List<FiedEquip> displace(final List<FiedEquip> field, final FiedEquip fiedEquip) {
        final Map<Integer, SysPetAcsryAdvAttrLib> map = EquipDataPool.petAcsryAdvAttrLibMap;
        final List<FiedEquip> fieldEquipList = new ArrayList<FiedEquip>();
        for (final FiedEquip obj : field) {
            if (map.get(obj.getType()) == null && !obj.getType().equals(6672)) {
                fieldEquipList.add(obj);
            }
        }
        if (fiedEquip != null) {
            fieldEquipList.add(fiedEquip);
        }
        return fieldEquipList;
    }
    
    public FiedEquip advAttrLib(final List<FiedEquip> field) {
        FiedEquip fiedEquip = null;
        for (final FiedEquip obj : field) {
            if (EquipDataPool.petAcsryAdvAttrLibMap.get(obj.getType()) != null) {
                fiedEquip = obj;
                break;
            }
        }
        return fiedEquip;
    }
}
