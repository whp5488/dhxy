package com.kitty.system.game.equip.helper;

import java.util.Iterator;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import java.util.ArrayList;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.vo.FiedEquip;
import java.util.List;
import java.util.Map;
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
    public PetEquipDisplace() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip((long)role.getId());
        if (petEquipMap == null || petEquipMap.size() <= 0) {
            return null;
        }
        String[] split = param.split("-");
        if (split.length < 1) {
            return null;
        }
        if (!NumberUtil.isNumber(split[0]) || !NumberUtil.isNumber(split[1])) {
            return null;
        }
        long main = (long)Long.valueOf(split[0]);
        long second = (long)Long.valueOf(split[1]);
        if (main == second) {
            return null;
        }
        PetEquip equipMain = (PetEquip)petEquipMap.get(Long.valueOf(main));
        PetEquip equipSecond = (PetEquip)petEquipMap.get(Long.valueOf(second));
        if (equipMain == null || equipSecond == null) {
            return null;
        }
        List<FiedEquip> fieldEquipMain = equipMain.getField();
        List<FiedEquip> fieldEquipSecond = equipSecond.getField();
        List<FiedEquip> equipMainDisplace = this.displace(fieldEquipMain, this.advAttrLib(fieldEquipSecond));
        List<FiedEquip> equipSecondDisplace = this.displace(fieldEquipSecond, this.advAttrLib(fieldEquipMain));
        equipMain.setField(equipMainDisplace);
        equipSecond.setField(equipSecondDisplace);
        equipMain.setField(MakeAttribute.calEquipScore(equipMain));
        equipSecond.setField(MakeAttribute.calEquipScore(equipSecond));
        equipMain.save();
        equipSecond.save();
        MessagePusher.pushMessage((long)role.getId(), new RespPetEquipDisplace(split[0], split[1]));
        this.equipService.refreshPetEquip(role, equipMain);
        this.equipService.refreshPetEquip(role, equipSecond);
        return null;
    }
    
    public List<FiedEquip> displace(List<FiedEquip> field, FiedEquip fiedEquip) {
        Map<Integer, SysPetAcsryAdvAttrLib> map = EquipDataPool.petAcsryAdvAttrLibMap;
        List<FiedEquip> fieldEquipList = new ArrayList<>();
        for (FiedEquip obj : field) {
            if (map.get(obj.getType()) == null && !obj.getType().equals(Integer.valueOf(6672))) {
                fieldEquipList.add(obj);
            }
            else {
                continue;
            }
        }
        if (fiedEquip != null) {
            fieldEquipList.add(fiedEquip);
            return fieldEquipList;
        }
        return fieldEquipList;
    }
    
    public FiedEquip advAttrLib(List<FiedEquip> field) {
        FiedEquip fiedEquip = null;
        for (FiedEquip obj : field) {
            if (EquipDataPool.petAcsryAdvAttrLibMap.get(obj.getType()) != null) {
                fiedEquip = obj;
                break;
            }
            continue;
        }
        return fiedEquip;
    }
}
