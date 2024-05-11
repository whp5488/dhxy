
package com.kitty.system.game.equip.helper;

import java.util.Arrays;
import java.util.LinkedHashMap;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.Iterator;
import java.util.Map;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.message.RespPetPreUpgradeEquip;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.game.equip.helper.vo.PetEquipNeo;
import java.util.ArrayList;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("PetEquipCompose")
public class PetEquipComposeHandler extends UpgradeHandler
{
    public static final List<Integer> integers;
    public static final String NOT_STUFF = "材料不足";
    
    @Override
    public String acceptUpgrade(final Role role, final Integer position, final Integer type, final String param, final String cause) {
        final int residue = this.bagService.residuePetEquipBagPos(role);
        if (residue < this.expend(type)) {
            return "物品栏空间不足";
        }
        if (!NumberUtil.isNumber(param)) {
            return null;
        }
        final int grade = Integer.valueOf(param);
        if (grade < 1 || grade > 3) {
            return null;
        }
        if (!this.checkGrade(role.getRelive() * 1000 + role.getLevel(), grade)) {
            return null;
        }
        final int expend = this.expend(type);
        final Map<Integer, Integer> consume = this.consume(grade, position, expend);
        for (final Integer key : consume.keySet()) {
            final Integer amount = consume.get(key);
            if (!key.equals(Const.RESTYPE_SILVER)) {
                final Integer num = this.bagService.getBagGoods(role, key, amount);
                if (num != null) {
                    return "材料不足";
                }
                continue;
            }
            else {
                if (role.getSilver() < amount) {
                    return "材料不足";
                }
                continue;
            }
        }
        final boolean deduct = this.deduct(role, consume);
        if (!deduct) {
            return "道具或者货币扣除失败";
        }
        final List<PetEquipNeo> objects = new ArrayList<PetEquipNeo>(expend);
        byte b;
        if (position <= 0) {
            final ACC_GRADE priceless = ACC_GRADE.PRICELESS;
            b = ACC_GRADE.PLAY_WITH.getValue();
        }
        else {
            b = ACC_GRADE.PRICELESS.getValue();
        }
        final byte productOrder = b;
        for (int i = 0; i < expend; ++i) {
            final PetEquip petBaseBieEq = MakeAttribute.createPetBaseBieEq(grade, productOrder, PetEquipComposeHandler.integers, 0);
            petBaseBieEq.setId(this.worker.nextId());
            petBaseBieEq.setRId(role.getId());
            this.bagService.addPetEquip(role, petBaseBieEq, true, "宠物配饰打造");
            objects.add(new PetEquipNeo(petBaseBieEq.getId(), petBaseBieEq.getSerial(), petBaseBieEq.getProductOrder(), petBaseBieEq.getField()));
        }
        MessagePusher.pushMessage(role.getId(), new RespPetPreUpgradeEquip(objects, type.byteValue(), position.byteValue(), (byte)grade));
        return null;
    }
    
    public boolean checkGrade(final int grade, final int type) {
        return (type != 2 || grade >= 2120) && (type != 3 || grade >= 3145);
    }
    
    public Map<Integer, Integer> consume(final int grade, final int position, final int num) {
        final Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
        if (grade == 1) {
            map.put(Const.RESTYPE_SILVER, 50000 * num);
        }
        else if (grade == 2) {
            map.put(75030, 1 * num);
            map.put(Const.RESTYPE_SILVER, 100000 * num);
        }
        else if (grade == 3) {
            map.put(75030, 2 * num);
            map.put(75047, 1 * num);
            map.put(Const.RESTYPE_SILVER, 150000 * num);
        }
        if (map.size() > 0) {
            final int phoenix = (position <= 0) ? 75401 : 75402;
            map.put(phoenix, 1 * num);
            map.put(75400, 1 * num);
        }
        return map;
    }
    
    static {
        integers = Arrays.asList(92, 7, 1);
    }
}
