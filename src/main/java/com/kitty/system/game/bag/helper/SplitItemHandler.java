package com.kitty.system.game.bag.helper;

import java.util.Map;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.enums.war.itempro.def.ITEM_LARGE_SON_TYPE;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("SplitItem")
public class SplitItemHandler extends BagHandler
{
    public SplitItemHandler() {
    }
    
    @Override
    public String acceptOperate(Role role, Integer pos, Integer num, String param) {
        Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
        BagGoods goods = (BagGoods)bagGoodsMap.values().stream().filter(item -> pos.equals(Integer.valueOf(item.getPos()))).findFirst().orElse(null);
        if (goods == null) {
            return null;
        }
        Integer itemType = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(goods.getSerial()));
        if (itemType.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue())) && DataGlobalFunc.getItemSubTypeByItemTypeId(Integer.valueOf(goods.getSerial())).equals(Integer.valueOf(ITEM_LARGE_SON_TYPE.ITEM_DEF_TYPE_SKILL_BOOK.getValue()))) {
            Integer type = DataGlobalFunc.getPetSkillBookTypeByItemTypeId(Integer.valueOf(goods.getSerial()));
            if (type.equals(Integer.valueOf(PETSKILLSUB.SUBTYPE_SENIOR.getValue()))) {
                boolean flag = this.bagService.checkPos(role, 75017, 10);
                if (flag) {
                    Integer nu = this.bagService.consumeBagGoodsByPos(role, 1, (int)pos, true, "分解高级技能书");
                    if (nu != null) {
                        return null;
                    }
                    this.bagService.addGoods(role, 75017, 10, 0, true, true, "分解高级技能书");
                    return "你获得了#<II75017>##<LB1>[高级技能书残卷]#X10";
                }
            }
            else if (type.equals(Integer.valueOf(PETSKILLSUB.SUBTYPE_SUPREME.getValue()))) {
                Integer integer = this.bagService.consumeBagGoodsByPos(role, 1, (int)pos, true, "终极技能书");
                if (integer != null) {
                    return null;
                }
                this.bagService.addGoods(role, 75018, 20, 0, true, true, "终极技能书");
                return "你获得了#<II75018>##<LB1>[终极技能书残卷]#X20";
            }
        }
        int i = 0;
        while (i < (int)num) {
            int bagPos = this.bagService.getPos(role, false);
            if (bagPos < 0) {
                return "物品栏空间不足";
            }
            ++i;
        }
        return null;
    }
}
