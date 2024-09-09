package com.kitty.system.game.shop.service;

import com.kitty.system.game.card.domain.CardChange;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.init.domain.SysShopNpcGuoRecycle;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.init.domain.SysShopNpcFeats;
import com.kitty.system.init.domain.SysShopNpcNice;
import com.kitty.system.init.domain.SysShopNpcLong;
import com.kitty.system.init.domain.SysShopNpcArena;
import com.kitty.system.init.domain.SysShopNpcDi;
import com.kitty.system.init.domain.SysShopNpcNao;
import com.kitty.system.init.domain.SysShopNpcBrac;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysShopNpcGuo;
import com.kitty.system.enums.common.Mall;
import com.kitty.system.init.domain.SysShopNpcGrocery;
import com.kitty.system.init.domain.SysShopNpcEquip;
import com.kitty.system.init.domain.SysShopNpcDrug;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.shop.domain.ItemInfo;
import com.kitty.system.init.domain.SysShopNpcPet;
import com.kitty.system.game.onlinemall.pool.MallDataPool;
import com.kitty.system.game.shop.message.RespGoodsList;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.bag.service.BagService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class ShopService
{
    @Autowired
    public RoleService roleService;
    @Autowired
    public BagService bagService;
    
    public ShopService() {
    }
    
    public void sendNpcPet(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcPet> petMap = MallDataPool.shop_npc_pet;
        for (Integer key : petMap.keySet()) {
            SysShopNpcPet pet = (SysShopNpcPet)petMap.get(key);
            if (pet != null) {
                ItemInfo itemInfo = new ItemInfo(pet.getSerial(), pet.getNum(), pet.getType(), pet.getPrice(), pet.getDiscount(), -1);
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcDrug(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcDrug> drugMap = MallDataPool.shop_npc_drug;
        for (Integer key : drugMap.keySet()) {
            SysShopNpcDrug drug = (SysShopNpcDrug)drugMap.get(key);
            if (drug != null) {
                ItemInfo itemInfo = new ItemInfo((int)drug.getSerial(), (int)drug.getNum(), (int)drug.getType(), (int)drug.getPrice(), (int)drug.getDiscount(), -1);
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcEquip(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcEquip> equipMap = MallDataPool.shop_npc_equip;
        for (Integer key : equipMap.keySet()) {
            SysShopNpcEquip equip = (SysShopNpcEquip)equipMap.get(key);
            if (equip != null) {
                ItemInfo itemInfo = new ItemInfo((int)equip.getSerial(), (int)equip.getNum(), (int)equip.getType(), (int)equip.getPrice(), (int)equip.getDiscount(), -1);
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcGrocery(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcGrocery> groceryMap = MallDataPool.shop_npc_grocery;
        Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (Integer key : groceryMap.keySet()) {
            SysShopNpcGrocery grocery = (SysShopNpcGrocery)groceryMap.get(key);
            if (grocery != null) {
                ItemInfo itemInfo = this.sendQuota((int)grocery.getSerial(), (int)grocery.getNum(), (int)grocery.getType(), (int)grocery.getPrice(), (int)grocery.getDiscount(), (int)grocery.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_GROCERY.getValue());
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcGuo(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcGuo> guoMap = MallDataPool.shop_npc_guo;
        Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (Integer key : guoMap.keySet()) {
            SysShopNpcGuo shop = (SysShopNpcGuo)guoMap.get(key);
            if (shop != null) {
                ItemInfo itemInfo = this.sendQuota((int)shop.getSerial(), shop.getNum(), (int)Const.RESTYPE_GUO, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_GUO.getValue());
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcBrac(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcBrac> map = MallDataPool.shop_npc_brac;
        Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (Integer key : map.keySet()) {
            SysShopNpcBrac shop = (SysShopNpcBrac)map.get(key);
            if (shop != null) {
                ItemInfo itemInfo = this.sendQuota((int)shop.getSerial(), shop.getNum(), (int)Const.RESTYPE_BRAC, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_BRAC.getValue());
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcNao(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcNao> map = MallDataPool.shop_npc_nao;
        Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (Integer key : map.keySet()) {
            SysShopNpcNao shop = (SysShopNpcNao)map.get(key);
            if (shop != null) {
                ItemInfo itemInfo = this.sendQuota((int)shop.getSerial(), shop.getNum(), (int)Const.RESTYPE_NAO, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_NAO.getValue());
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcDi(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcDi> map = MallDataPool.shop_npc_di;
        Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (Integer key : map.keySet()) {
            SysShopNpcDi shop = (SysShopNpcDi)map.get(key);
            if (shop != null) {
                ItemInfo itemInfo = this.sendQuota((int)shop.getSerial(), shop.getNum(), (int)Const.RESTYPE_DI, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_DI.getValue());
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcArena(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcArena> map = MallDataPool.shop_npc_arena;
        Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (Integer key : map.keySet()) {
            SysShopNpcArena shop = (SysShopNpcArena)map.get(key);
            if (shop != null) {
                ItemInfo itemInfo = this.sendQuota((int)shop.getSerial(), shop.getNum(), (int)Const.RESTYPE_ARENA, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_ARENA.getValue());
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcLong(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcLong> map = MallDataPool.shop_npc_long;
        Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (Integer key : map.keySet()) {
            SysShopNpcLong shop = (SysShopNpcLong)map.get(key);
            if (shop != null) {
                ItemInfo itemInfo = this.sendQuota((int)shop.getSerial(), shop.getNum(), (int)Const.RESTYPE_LONG, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_LONG.getValue());
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcNice(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcNice> map = MallDataPool.shop_npc_nice;
        Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (Integer key : map.keySet()) {
            SysShopNpcNice shop = (SysShopNpcNice)map.get(key);
            if (shop != null) {
                ItemInfo itemInfo = this.sendQuota((int)shop.getSerial(), shop.getNum(), (int)Const.RESTYPE_NICE, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_NICE.getValue());
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcFeats(Role role, Integer serial) {
        List<ItemInfo> list = new ArrayList<>();
        RespGoodsList respGoodsList = new RespGoodsList();
        Map<Integer, SysShopNpcFeats> map = MallDataPool.shop_npc_feats;
        Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (Integer key : map.keySet()) {
            SysShopNpcFeats shop = (SysShopNpcFeats)map.get(key);
            if (shop != null) {
                ItemInfo itemInfo = this.sendQuota((int)shop.getSerial(), shop.getNum(), (int)Const.RES_TYPE_FEATS, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_FEATS.getValue());
                list.add(itemInfo);
            }
            else {
                continue;
            }
        }
        respGoodsList.setId((int)serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public ItemInfo sendQuota(int serial, int num, int type, int price, int discount, int quotaLimit, Map<Integer, Map<Integer, Integer>> quotaMap, int mall) {
        ItemInfo itemInfo = new ItemInfo(serial, num, type, price, discount, -1);
        if (quotaLimit > -1) {
            Map<Integer, Integer> map = (Map)quotaMap.get(Integer.valueOf(mall));
            Integer purchased = (Integer)map.get(Integer.valueOf(serial));
            if (purchased != null) {
                itemInfo.setQuotaLimit(quotaLimit - (int)purchased);
            }
            else {
                itemInfo.setQuotaLimit(quotaLimit);
            }
        }
        return itemInfo;
    }
    
    public void buyNpcPet(Role role, int serial, int num) {
        SysShopNpcPet pet = (SysShopNpcPet)MallDataPool.shop_npc_pet.get(Integer.valueOf(serial));
        if (pet != null) {
            String msg = DataGlobalFunc.needGradeJudge(pet.getZsLimit(), pet.getLvLimit(), (int)role.getRelive(), (int)role.getLevel(), "你的#<R>角色等级#不足");
            if (msg != null) {
                MessagePusher.pushMessage(role, new RespMsg(msg));
                return;
            }
            this.buy(role, pet.getSerial(), num, pet.getDiscount(), pet.getPrice(), pet.getType(), pet.getNum(), 0);
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
    }
    
    public void buyNpcDrug(Role role, int serial, int num) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        SysShopNpcDrug drug = (SysShopNpcDrug)MallDataPool.shop_npc_drug.get(Integer.valueOf(serial));
        if (drug != null) {
            this.buy(role, (int)drug.getSerial(), num, (int)drug.getDiscount(), (int)drug.getPrice(), (int)drug.getType(), (int)drug.getNum(), 0);
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
        return;
    }
    
    public void buyNpcGrocery(Role role, int serial, int num, int shipper) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        SysShopNpcGrocery shop = (SysShopNpcGrocery)MallDataPool.shop_npc_grocery.get(Integer.valueOf(serial));
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_GROCERY.getValue(), (int)shop.getSerial(), (int)shop.getQuotaLimit(), num);
            if (num > 0) {
                boolean buy = this.buy(role, (int)shop.getSerial(), num, (int)shop.getDiscount(), (int)shop.getPrice(), (int)shop.getType(), (int)shop.getNum(), (int)shop.getBd());
                if (buy && (int)shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), shop.getQuotaLimit(), Integer.valueOf(num), shop.getType(), Integer.valueOf(Mall.NPC_SHOP_GROCERY.getValue()), shop.getPrice(), shop.getDiscount());
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
        return;
    }
    
    public void buyNpcGuo(Role role, int serial, int num, int shipper) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        SysShopNpcGuo shop = (SysShopNpcGuo)MallDataPool.shop_npc_guo.get(Integer.valueOf(serial));
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_GUO.getValue(), (int)shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                boolean buy = this.buy(role, (int)shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), (int)Const.RESTYPE_GUO, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_GUO, Integer.valueOf(Mall.NPC_SHOP_GUO.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount()));
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
        return;
    }
    
    public void buyNpcBrac(Role role, int serial, int num, int shipper) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        SysShopNpcBrac shop = (SysShopNpcBrac)MallDataPool.shop_npc_brac.get(Integer.valueOf(serial));
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_BRAC.getValue(), (int)shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                boolean buy = this.buy(role, (int)shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), (int)Const.RESTYPE_BRAC, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_BRAC, Integer.valueOf(Mall.NPC_SHOP_BRAC.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount()));
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
        return;
    }
    
    public void buyNpcNao(Role role, int serial, int num, int shipper) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        SysShopNpcNao shop = (SysShopNpcNao)MallDataPool.shop_npc_nao.get(Integer.valueOf(serial));
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_NAO.getValue(), (int)shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                boolean buy = this.buy(role, (int)shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), (int)Const.RESTYPE_NAO, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_NAO, Integer.valueOf(Mall.NPC_SHOP_NAO.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount()));
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
        return;
    }
    
    public void buyNpcDi(Role role, int serial, int num, int shipper) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        SysShopNpcDi shop = (SysShopNpcDi)MallDataPool.shop_npc_di.get(Integer.valueOf(serial));
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_DI.getValue(), (int)shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                boolean buy = this.buy(role, (int)shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), (int)Const.RESTYPE_DI, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_DI, Integer.valueOf(Mall.NPC_SHOP_DI.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount()));
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
        return;
    }
    
    public void buyNpcArena(Role role, int serial, int num, int shipper) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        SysShopNpcArena shop = (SysShopNpcArena)MallDataPool.shop_npc_arena.get(Integer.valueOf(serial));
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_ARENA.getValue(), (int)shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                boolean buy = this.buy(role, (int)shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), (int)Const.RESTYPE_ARENA, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_ARENA, Integer.valueOf(Mall.NPC_SHOP_ARENA.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount()));
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
        return;
    }
    
    public void buyNpcLong(Role role, int serial, int num, int shipper) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        SysShopNpcLong shop = (SysShopNpcLong)MallDataPool.shop_npc_long.get(Integer.valueOf(serial));
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_LONG.getValue(), (int)shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                boolean buy = this.buy(role, (int)shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), (int)Const.RESTYPE_LONG, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_LONG, Integer.valueOf(Mall.NPC_SHOP_LONG.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount()));
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
        return;
    }
    
    public void buyNpcNice(Role role, int serial, int num, int shipper) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        SysShopNpcNice shop = (SysShopNpcNice)MallDataPool.shop_npc_nice.get(Integer.valueOf(serial));
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_NICE.getValue(), (int)shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                boolean buy = this.buy(role, (int)shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), (int)Const.RESTYPE_NICE, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RESTYPE_NICE, Integer.valueOf(Mall.NPC_SHOP_NICE.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount()));
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
        return;
    }
    
    public void buyNpcFeats(Role role, int serial, int num, int shipper) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        SysShopNpcFeats shop = (SysShopNpcFeats)MallDataPool.shop_npc_feats.get(Integer.valueOf(serial));
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_FEATS.getValue(), (int)shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                boolean buy = this.buy(role, (int)shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), (int)Const.RES_TYPE_FEATS, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, Integer.valueOf(shipper), shop.getSerial(), Integer.valueOf(shop.getQuotaLimit()), Integer.valueOf(num), Const.RES_TYPE_FEATS, Integer.valueOf(Mall.NPC_SHOP_FEATS.getValue()), Integer.valueOf(shop.getPrice()), Integer.valueOf(shop.getDiscount()));
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
        return;
    }
    
    public int checkQuotaLimit(Role role, int mall, int serial, int quotaLimit, int num) {
        if (quotaLimit > -1) {
            Map<Integer, Integer> map = (Map)role.getExtendBox().getQuotaLimit().get(Integer.valueOf(mall));
            Integer integer = (Integer)map.get(Integer.valueOf(serial));
            if (integer != null && (int)integer >= quotaLimit) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("该道具购买以上限"));
                num = 0;
            }
            else {
                Integer count = Integer.valueOf(num + ((integer == null) ? 0 : ((int)integer)));
                if ((int)count > quotaLimit) {
                    num = quotaLimit - (int)integer;
                }
            }
        }
        return num;
    }
    
    public void buyNpcEquip(Role role, int serial, int num) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        SysShopNpcEquip equip = (SysShopNpcEquip)MallDataPool.shop_npc_equip.get(Integer.valueOf(serial));
        if (equip != null) {
            if (this.bagService.getPos(role, false) > -1) {
                RoleService roleService = SpringUtils.getRoleService();
                double discount = ((int)equip.getDiscount() > 0) ? ((double)(int)equip.getDiscount() / 100.0) : 1.0;
                int sum_price = (int)((double)(int)equip.getPrice() * discount * (double)num);
                if (roleService.checkCurrency(role, equip.getType(), (long)sum_price) && roleService.deductCurrency(role, equip.getType(), (long)sum_price, "武器店")) {
                    ((EquipService)SpringUtils.getBean(EquipService.class)).upgradeEquip(role, 0, 11, String.valueOf(equip.getSerial()), 0, false, "武器店");
                }
            }
        }
        else {
            RespMsg respMsg = new RespMsg();
            respMsg.setMsg("商品已售馨");
            MessagePusher.pushMessage(role, respMsg);
        }
        return;
    }
    
    public boolean buy(Role role, int serial, int num, int discount, int price, int type, int once, int bd) {
        num = Math.max(1, num);
        if (this.bagService.checkPos(role, serial, num)) {
            RoleService roleService = SpringUtils.getRoleService();
            double dis = (discount > 0) ? ((double)discount / 100.0) : 1.0;
            int sum_price = (int)((double)price * dis * (double)num);
            if (roleService.checkCurrency(role, Integer.valueOf(type), (long)sum_price)) {
                roleService.deductCurrency(role, Integer.valueOf(type), (long)sum_price, "执行物品购买");
                ((BagService)SpringUtils.getBean(BagService.class)).addGoods(role, serial, once * num, bd, true, true, "商店购买");
                return true;
            }
            return false;
        }
        else {
            return false;
        }
    }
    
    public void buyLimit(Role role, Integer shipper, Integer serial, Integer quotaLimit, Integer num, Integer resType, Integer type, Integer price, Integer discount) {
        Map<Integer, Integer> map = (Map)role.getExtendBox().getQuotaLimit().get(type);
        num = Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), map.get(serial)) + (int)num);
        map.put(serial, num);
        role.save();
        List<ItemInfo> list = new ArrayList<>();
        ItemInfo itemInfo = new ItemInfo((int)serial, (int)quotaLimit, (int)resType, (int)price, (int)discount, (int)quotaLimit - (int)map.get(serial));
        list.add(itemInfo);
        RespGoodsList respGoodsList = new RespGoodsList();
        respGoodsList.setType(1);
        respGoodsList.setId((int)shipper);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void recycleNpcGuo(Role role, String param, int serial, int num) {
        num = Math.max(1, num);
        Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(serial));
        if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_CARD.getValue()))) {
            SysDataCard dataCard = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(serial));
            if (dataCard != null) {
                CardService cardService = (CardService)SpringUtils.getBean(CardService.class);
                Map<Long, CardChange> cardMap = cardService.getCard(role);
                CardChange cardChange = (CardChange)cardMap.values().stream().filter(e -> e.getSerial() == serial).findFirst().orElse(null);
                if (cardChange == null) {
                    return;
                }
                int recycleNpcGuoCardPrice = this.recycleNpcGuoCardPrice(dataCard);
                if (recycleNpcGuoCardPrice <= 0) {
                    return;
                }
                boolean detach = cardService.cardDetach(role, serial, num);
                if (detach) {
                    cardService.refresh(role, cardChange);
                    this.roleService.addCurrency(role, Const.RESTYPE_GUO, Long.valueOf((long)(recycleNpcGuoCardPrice * num)), "出售成功，获得", true, true);
                }
                return;
            }
        }
        else if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue())) || type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()))) {
            if (NumberUtil.isNumber(param)) {
                BagEquip bagEquip = (BagEquip)this.bagService.getBagEquip(role).get(Long.valueOf(param));
                if (bagEquip == null) {
                    return;
                }
                if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()))) {
                    if (DataGlobalFunc.getPj(bagEquip.getType()) == 1) {
                        boolean flag = this.bagService.deleteRoleEquip(role, bagEquip, "郭氏回收");
                        if (flag) {
                            this.roleService.addCurrency(role, Const.RESTYPE_GUO, Long.valueOf(90000L), "出售成功，获得", false, false);
                        }
                    }
                    else {
                        MessagePusher.pushMessage(role, new RespMsg("无法回收"));
                    }
                }
                else if (bagEquip.getProductOrder() == ACC_GRADE.PRICELESS.getValue()) {
                    boolean flag = this.bagService.deleteRoleEquip(role, bagEquip, "郭氏回收");
                    if (flag) {
                        this.roleService.addCurrency(role, Const.RESTYPE_GUO, Long.valueOf(120000L), "出售成功，获得", false, false);
                    }
                }
                else {
                    MessagePusher.pushMessage(role, new RespMsg("无法回收"));
                }
            }
            return;
        }
        else {
            Integer goods = this.bagService.getBagGoods(role, serial, num);
            if (goods != null) {
                MessagePusher.pushMessage(role, new RespMsg("背包中物品数量不足"));
                return;
            }
            SysShopNpcGuoRecycle recycle = (SysShopNpcGuoRecycle)MallDataPool.shop_npc_guo_recycle.get(Integer.valueOf(serial));
            if (recycle != null) {
                if (this.bagService.consumeBagGoods(role, serial, num, true, true, "郭氏回收") != null) {
                    return;
                }
                this.roleService.addCurrency(role, Const.RESTYPE_GUO, Long.valueOf((long)(recycle.getPrice() * num)), "出售成功，获得", true, true);
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespMsg("该物品无法回收"));
    }
    
    public int recycleNpcGuoCardPrice(SysDataCard dataCard) {
        if (dataCard.getMorph() > 0) {
            if (dataCard.getRarity() == 2) {
                return 150;
            }
            if (dataCard.getRarity() == 3) {
                return 600;
            }
            if (dataCard.getRarity() == 4) {
                return 3000;
            }
            if (dataCard.getRarity() == 5) {
                if (dataCard.getSerial() > 184060) {
                    return 20250;
                }
                return 12000;
            }
        }
        else {
            if (dataCard.getRarity() == 2) {
                return 100;
            }
            if (dataCard.getRarity() == 3) {
                return 400;
            }
            if (dataCard.getRarity() == 4) {
                return 2000;
            }
            if (dataCard.getRarity() == 5) {
                if (dataCard.getSerial() > 184060) {
                    return 13500;
                }
                return 8000;
            }
        }
        return 0;
    }
}
