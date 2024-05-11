
package com.kitty.system.game.shop.service;

import com.kitty.system.init.domain.SysShopNpcGuoRecycle;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.card.domain.CardChange;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.data.DataGlobalFunc;
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
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.init.domain.SysShopNpcPet;
import com.kitty.system.game.onlinemall.pool.MallDataPool;
import com.kitty.system.game.shop.message.RespGoodsList;
import com.kitty.system.game.shop.domain.ItemInfo;
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
    
    public void sendNpcPet(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcPet> petMap = MallDataPool.shop_npc_pet;
        for (final Integer key : petMap.keySet()) {
            final SysShopNpcPet pet = petMap.get(key);
            if (pet != null) {
                final ItemInfo itemInfo = new ItemInfo(pet.getSerial(), pet.getNum(), pet.getType(), pet.getPrice(), pet.getDiscount(), -1);
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcDrug(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcDrug> drugMap = MallDataPool.shop_npc_drug;
        for (final Integer key : drugMap.keySet()) {
            final SysShopNpcDrug drug = drugMap.get(key);
            if (drug != null) {
                final ItemInfo itemInfo = new ItemInfo(drug.getSerial(), drug.getNum(), drug.getType(), drug.getPrice(), drug.getDiscount(), -1);
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcEquip(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcEquip> equipMap = MallDataPool.shop_npc_equip;
        for (final Integer key : equipMap.keySet()) {
            final SysShopNpcEquip equip = equipMap.get(key);
            if (equip != null) {
                final ItemInfo itemInfo = new ItemInfo(equip.getSerial(), equip.getNum(), equip.getType(), equip.getPrice(), equip.getDiscount(), -1);
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcGrocery(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcGrocery> groceryMap = MallDataPool.shop_npc_grocery;
        final Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (final Integer key : groceryMap.keySet()) {
            final SysShopNpcGrocery grocery = groceryMap.get(key);
            if (grocery != null) {
                final ItemInfo itemInfo = this.sendQuota(grocery.getSerial(), grocery.getNum(), grocery.getType(), grocery.getPrice(), grocery.getDiscount(), grocery.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_GROCERY.getValue());
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcGuo(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcGuo> guoMap = MallDataPool.shop_npc_guo;
        final Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (final Integer key : guoMap.keySet()) {
            final SysShopNpcGuo shop = guoMap.get(key);
            if (shop != null) {
                final ItemInfo itemInfo = this.sendQuota(shop.getSerial(), shop.getNum(), Const.RESTYPE_GUO, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_GUO.getValue());
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcBrac(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcBrac> map = MallDataPool.shop_npc_brac;
        final Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (final Integer key : map.keySet()) {
            final SysShopNpcBrac shop = map.get(key);
            if (shop != null) {
                final ItemInfo itemInfo = this.sendQuota(shop.getSerial(), shop.getNum(), Const.RESTYPE_BRAC, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_BRAC.getValue());
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcNao(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcNao> map = MallDataPool.shop_npc_nao;
        final Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (final Integer key : map.keySet()) {
            final SysShopNpcNao shop = map.get(key);
            if (shop != null) {
                final ItemInfo itemInfo = this.sendQuota(shop.getSerial(), shop.getNum(), Const.RESTYPE_NAO, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_NAO.getValue());
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcDi(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcDi> map = MallDataPool.shop_npc_di;
        final Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (final Integer key : map.keySet()) {
            final SysShopNpcDi shop = map.get(key);
            if (shop != null) {
                final ItemInfo itemInfo = this.sendQuota(shop.getSerial(), shop.getNum(), Const.RESTYPE_DI, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_DI.getValue());
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcArena(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcArena> map = MallDataPool.shop_npc_arena;
        final Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (final Integer key : map.keySet()) {
            final SysShopNpcArena shop = map.get(key);
            if (shop != null) {
                final ItemInfo itemInfo = this.sendQuota(shop.getSerial(), shop.getNum(), Const.RESTYPE_ARENA, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_ARENA.getValue());
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcLong(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcLong> map = MallDataPool.shop_npc_long;
        final Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (final Integer key : map.keySet()) {
            final SysShopNpcLong shop = map.get(key);
            if (shop != null) {
                final ItemInfo itemInfo = this.sendQuota(shop.getSerial(), shop.getNum(), Const.RESTYPE_LONG, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_LONG.getValue());
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcNice(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcNice> map = MallDataPool.shop_npc_nice;
        final Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (final Integer key : map.keySet()) {
            final SysShopNpcNice shop = map.get(key);
            if (shop != null) {
                final ItemInfo itemInfo = this.sendQuota(shop.getSerial(), shop.getNum(), Const.RESTYPE_NICE, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_NICE.getValue());
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void sendNpcFeats(final Role role, final Integer serial) {
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final RespGoodsList respGoodsList = new RespGoodsList();
        final Map<Integer, SysShopNpcFeats> map = MallDataPool.shop_npc_feats;
        final Map<Integer, Map<Integer, Integer>> quotaMap = role.getExtendBox().getQuotaLimit();
        for (final Integer key : map.keySet()) {
            final SysShopNpcFeats shop = map.get(key);
            if (shop != null) {
                final ItemInfo itemInfo = this.sendQuota(shop.getSerial(), shop.getNum(), Const.RES_TYPE_FEATS, shop.getPrice(), shop.getDiscount(), shop.getQuotaLimit(), quotaMap, Mall.NPC_SHOP_FEATS.getValue());
                list.add(itemInfo);
            }
        }
        respGoodsList.setId(serial);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public ItemInfo sendQuota(final int serial, final int num, final int type, final int price, final int discount, final int quotaLimit, final Map<Integer, Map<Integer, Integer>> quotaMap, final int mall) {
        final ItemInfo itemInfo = new ItemInfo(serial, num, type, price, discount, -1);
        if (quotaLimit > -1) {
            final Map<Integer, Integer> map = quotaMap.get(mall);
            final Integer purchased = map.get(serial);
            if (purchased != null) {
                itemInfo.setQuotaLimit(quotaLimit - purchased);
            }
            else {
                itemInfo.setQuotaLimit(quotaLimit);
            }
        }
        return itemInfo;
    }
    
    public void buyNpcPet(final Role role, final int serial, final int num) {
        final SysShopNpcPet pet = MallDataPool.shop_npc_pet.get(serial);
        if (pet != null) {
            final String msg = DataGlobalFunc.needGradeJudge(pet.getZsLimit(), pet.getLvLimit(), role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
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
    
    public void buyNpcDrug(final Role role, final int serial, final int num) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final SysShopNpcDrug drug = MallDataPool.shop_npc_drug.get(serial);
        if (drug != null) {
            this.buy(role, drug.getSerial(), num, drug.getDiscount(), drug.getPrice(), drug.getType(), drug.getNum(), 0);
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
    }
    
    public void buyNpcGrocery(final Role role, final int serial, int num, final int shipper) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final SysShopNpcGrocery shop = MallDataPool.shop_npc_grocery.get(serial);
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_GROCERY.getValue(), shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                final boolean buy = this.buy(role, shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), shop.getType(), shop.getNum(), shop.getBd());
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, shipper, shop.getSerial(), shop.getQuotaLimit(), num, shop.getType(), Mall.NPC_SHOP_GROCERY.getValue(), shop.getPrice(), shop.getDiscount());
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
    }
    
    public void buyNpcGuo(final Role role, final int serial, int num, final int shipper) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final SysShopNpcGuo shop = MallDataPool.shop_npc_guo.get(serial);
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_GUO.getValue(), shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                final boolean buy = this.buy(role, shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_GUO, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, shipper, shop.getSerial(), shop.getQuotaLimit(), num, Const.RESTYPE_GUO, Mall.NPC_SHOP_GUO.getValue(), shop.getPrice(), shop.getDiscount());
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
    }
    
    public void buyNpcBrac(final Role role, final int serial, int num, final int shipper) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final SysShopNpcBrac shop = MallDataPool.shop_npc_brac.get(serial);
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_BRAC.getValue(), shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                final boolean buy = this.buy(role, shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_BRAC, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, shipper, shop.getSerial(), shop.getQuotaLimit(), num, Const.RESTYPE_BRAC, Mall.NPC_SHOP_BRAC.getValue(), shop.getPrice(), shop.getDiscount());
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
    }
    
    public void buyNpcNao(final Role role, final int serial, int num, final int shipper) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final SysShopNpcNao shop = MallDataPool.shop_npc_nao.get(serial);
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_NAO.getValue(), shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                final boolean buy = this.buy(role, shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_NAO, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, shipper, shop.getSerial(), shop.getQuotaLimit(), num, Const.RESTYPE_NAO, Mall.NPC_SHOP_NAO.getValue(), shop.getPrice(), shop.getDiscount());
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
    }
    
    public void buyNpcDi(final Role role, final int serial, int num, final int shipper) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final SysShopNpcDi shop = MallDataPool.shop_npc_di.get(serial);
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_DI.getValue(), shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                final boolean buy = this.buy(role, shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_DI, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, shipper, shop.getSerial(), shop.getQuotaLimit(), num, Const.RESTYPE_DI, Mall.NPC_SHOP_DI.getValue(), shop.getPrice(), shop.getDiscount());
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
    }
    
    public void buyNpcArena(final Role role, final int serial, int num, final int shipper) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final SysShopNpcArena shop = MallDataPool.shop_npc_arena.get(serial);
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_ARENA.getValue(), shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                final boolean buy = this.buy(role, shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_ARENA, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, shipper, shop.getSerial(), shop.getQuotaLimit(), num, Const.RESTYPE_ARENA, Mall.NPC_SHOP_ARENA.getValue(), shop.getPrice(), shop.getDiscount());
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
    }
    
    public void buyNpcLong(final Role role, final int serial, int num, final int shipper) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final SysShopNpcLong shop = MallDataPool.shop_npc_long.get(serial);
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_LONG.getValue(), shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                final boolean buy = this.buy(role, shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_LONG, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, shipper, shop.getSerial(), shop.getQuotaLimit(), num, Const.RESTYPE_LONG, Mall.NPC_SHOP_LONG.getValue(), shop.getPrice(), shop.getDiscount());
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
    }
    
    public void buyNpcNice(final Role role, final int serial, int num, final int shipper) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final SysShopNpcNice shop = MallDataPool.shop_npc_nice.get(serial);
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_NICE.getValue(), shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                final boolean buy = this.buy(role, shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), Const.RESTYPE_NICE, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, shipper, shop.getSerial(), shop.getQuotaLimit(), num, Const.RESTYPE_NICE, Mall.NPC_SHOP_NICE.getValue(), shop.getPrice(), shop.getDiscount());
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
    }
    
    public void buyNpcFeats(final Role role, final int serial, int num, final int shipper) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final SysShopNpcFeats shop = MallDataPool.shop_npc_feats.get(serial);
        if (shop != null) {
            num = this.checkQuotaLimit(role, Mall.NPC_SHOP_FEATS.getValue(), shop.getSerial(), shop.getQuotaLimit(), num);
            if (num > 0) {
                final boolean buy = this.buy(role, shop.getSerial(), num, shop.getDiscount(), shop.getPrice(), Const.RES_TYPE_FEATS, shop.getNum(), 0);
                if (buy && shop.getQuotaLimit() > -1) {
                    this.buyLimit(role, shipper, shop.getSerial(), shop.getQuotaLimit(), num, Const.RES_TYPE_FEATS, Mall.NPC_SHOP_FEATS.getValue(), shop.getPrice(), shop.getDiscount());
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("商品已售馨"));
        }
    }
    
    public int checkQuotaLimit(final Role role, final int mall, final int serial, final int quotaLimit, int num) {
        if (quotaLimit > -1) {
            final Map<Integer, Integer> map = role.getExtendBox().getQuotaLimit().get(mall);
            final Integer integer = map.get(serial);
            if (integer != null && integer >= quotaLimit) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("该道具购买以上限"));
                num = 0;
            }
            else {
                final Integer count = num + ((integer == null) ? 0 : integer);
                if (count > quotaLimit) {
                    num = quotaLimit - integer;
                }
            }
        }
        return num;
    }
    
    public void buyNpcEquip(final Role role, final int serial, final int num) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final SysShopNpcEquip equip = MallDataPool.shop_npc_equip.get(serial);
        if (equip != null) {
            if (this.bagService.getPos(role, false) > -1) {
                final RoleService roleService = SpringUtils.getRoleService();
                final double discount = (equip.getDiscount() > 0) ? (equip.getDiscount() / 100.0) : 1.0;
                final int sum_price = (int)(equip.getPrice() * discount * num);
                if (roleService.checkCurrency(role, equip.getType(), sum_price) && roleService.deductCurrency(role, equip.getType(), sum_price, "武器店")) {
                    SpringUtils.getBean(EquipService.class).upgradeEquip(role, 0, 11, String.valueOf(equip.getSerial()), 0, false, "武器店");
                }
            }
        }
        else {
            final RespMsg respMsg = new RespMsg();
            respMsg.setMsg("商品已售馨");
            MessagePusher.pushMessage(role, respMsg);
        }
    }
    
    public boolean buy(final Role role, final int serial, int num, final int discount, final int price, final int type, final int once, final int bd) {
        num = Math.max(1, num);
        if (!this.bagService.checkPos(role, serial, num)) {
            return false;
        }
        final RoleService roleService = SpringUtils.getRoleService();
        final double dis = (discount > 0) ? (discount / 100.0) : 1.0;
        final int sum_price = (int)(price * dis * num);
        if (roleService.checkCurrency(role, type, sum_price)) {
            roleService.deductCurrency(role, type, sum_price, "执行物品购买");
            SpringUtils.getBean(BagService.class).addGoods(role, serial, once * num, bd, true, true, "商店购买");
            return true;
        }
        return false;
    }
    
    public void buyLimit(final Role role, final Integer shipper, final Integer serial, final Integer quotaLimit, Integer num, final Integer resType, final Integer type, final Integer price, final Integer discount) {
        final Map<Integer, Integer> map = role.getExtendBox().getQuotaLimit().get(type);
        num += BigDecimalUtil.decimalScaleIntegerDown(0, map.get(serial));
        map.put(serial, num);
        role.save();
        final List<ItemInfo> list = new ArrayList<ItemInfo>();
        final ItemInfo itemInfo = new ItemInfo(serial, quotaLimit, resType, price, discount, quotaLimit - map.get(serial));
        list.add(itemInfo);
        final RespGoodsList respGoodsList = new RespGoodsList();
        respGoodsList.setType(1);
        respGoodsList.setId(shipper);
        respGoodsList.setList(list);
        MessagePusher.pushMessage(role, respGoodsList);
    }
    
    public void recycleNpcGuo(final Role role, final String param, final int serial, int num) {
        num = Math.max(1, num);
        final Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(serial);
        if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_CARD.getValue())) {
            final SysDataCard dataCard = CardDataPool.cardMap.get(serial);
            if (dataCard != null) {
                final CardService cardService = SpringUtils.getBean(CardService.class);
                final Map<Long, CardChange> cardMap = cardService.getCard(role);
                final CardChange cardChange = cardMap.values().stream().filter(e -> e.getSerial() == serial).findFirst().orElse(null);
                if (cardChange == null) {
                    return;
                }
                final int recycleNpcGuoCardPrice = this.recycleNpcGuoCardPrice(dataCard);
                if (recycleNpcGuoCardPrice <= 0) {
                    return;
                }
                final boolean detach = cardService.cardDetach(role, serial, num);
                if (detach) {
                    cardService.refresh(role, cardChange);
                    this.roleService.addCurrency(role, Const.RESTYPE_GUO, (long)(recycleNpcGuoCardPrice * num), "出售成功，获得", true, true);
                }
                return;
            }
        }
        else {
            if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue()) || type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue())) {
                if (NumberUtil.isNumber(param)) {
                    final BagEquip bagEquip = this.bagService.getBagEquip(role).get(Long.valueOf(param));
                    if (bagEquip == null) {
                        return;
                    }
                    if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue())) {
                        if (DataGlobalFunc.getPj(bagEquip.getType()) == 1) {
                            final boolean flag = this.bagService.deleteRoleEquip(role, bagEquip, "郭氏回收");
                            if (flag) {
                                this.roleService.addCurrency(role, Const.RESTYPE_GUO, 90000L, "出售成功，获得", false, false);
                            }
                        }
                        else {
                            MessagePusher.pushMessage(role, new RespMsg("无法回收"));
                        }
                    }
                    else if (bagEquip.getProductOrder() == ACC_GRADE.PRICELESS.getValue()) {
                        final boolean flag = this.bagService.deleteRoleEquip(role, bagEquip, "郭氏回收");
                        if (flag) {
                            this.roleService.addCurrency(role, Const.RESTYPE_GUO, 120000L, "出售成功，获得", false, false);
                        }
                    }
                    else {
                        MessagePusher.pushMessage(role, new RespMsg("无法回收"));
                    }
                }
                return;
            }
            final Integer goods = this.bagService.getBagGoods(role, serial, num);
            if (goods != null) {
                MessagePusher.pushMessage(role, new RespMsg("背包中物品数量不足"));
                return;
            }
            final SysShopNpcGuoRecycle recycle = MallDataPool.shop_npc_guo_recycle.get(serial);
            if (recycle != null) {
                if (this.bagService.consumeBagGoods(role, serial, num, true, true, "郭氏回收") != null) {
                    return;
                }
                this.roleService.addCurrency(role, Const.RESTYPE_GUO, (long)(recycle.getPrice() * num), "出售成功，获得", true, true);
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespMsg("该物品无法回收"));
    }
    
    public int recycleNpcGuoCardPrice(final SysDataCard dataCard) {
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
