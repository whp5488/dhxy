
package com.kitty.system.game.bag.service;

import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.init.domain.SysDataTaskRewardDynamic;
import java.util.HashMap;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.activity.handler.time.ThreeScopeHandler;
import com.kitty.system.game.activity.handler.time.SealDemonHandler;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.init.domain.SysDataExchange;
import com.kitty.system.game.bag.message.ReqExchange;
import com.kitty.system.game.bag.helper.BagHandler;
import org.nutz.lang.Strings;
import com.kitty.system.game.bag.helper.BagHelper;
import com.kitty.system.game.bag.message.ReqOperateItem;
import com.kitty.system.game.bag.message.RespUseIncense;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.game.bag.message.ReqDrugUse;
import java.util.Comparator;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.equip.make.MakeAttribute;
import java.util.Arrays;
import com.kitty.system.init.domain.vo.Prop;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import com.kitty.system.init.domain.SysGiftItemOpen;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.init.domain.SysGiftItem;
import com.kitty.system.game.role.domain.Setting;
import com.kitty.system.game.card.domain.CardExercise;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.role.service.SettingService;
import com.kitty.system.common.message.RespCherish;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysEffect;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.bag.message.RespLodeTemporary;
import com.kitty.system.game.bag.message.vo.TemporaryVo;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.bag.domain.vo.Temporary;
import com.kitty.common.utils.uuid.IdUtils;
import java.io.Serializable;
import com.kitty.system.game.bag.domain.Overflow;
import com.kitty.system.game.bag.message.RespDeleteItem;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.DelEquipEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.role.domain.Goods;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.equip.domain.EquipEasy;
import com.kitty.system.game.bag.message.RespFlyInfo;
import com.kitty.system.game.equip.domain.PetEquipEasy;
import com.kitty.system.game.bag.message.RespFlyPetEqInfo;
import com.kitty.common.model.Pos;
import java.util.Random;
import java.util.ConcurrentModificationException;
import com.kitty.logs.LoggerUtils;
import org.nutz.lang.util.NutMap;
import java.util.Date;
import com.kitty.common.thread.IdGenerator;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.init.domain.SysDataCard;
import java.text.MessageFormat;
import com.kitty.record.RecordThread;
import com.kitty.system.game.bag.message.RespIconCartoon;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import com.kitty.mina.cache.SessionUtils;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.alibaba.fastjson.JSONObject;
import com.kitty.common.utils.JsonUtils;
import java.util.Iterator;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.bag.domain.BagEquip;
import java.util.stream.Collector;
import com.kitty.mina.cache.PetCache;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.BagCache;
import com.kitty.system.game.bag.domain.BagGoods;
import java.util.Map;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import com.kitty.system.queue.BroadcastThread;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.cache.RedisService;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class BagService
{
    Logger logger;
    @Autowired
    private Dao dao;
    @Autowired
    private IdWorker worker;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CardService cardService;
    @Autowired
    private PetService petService;
    @Autowired
    private BroadcastThread broadcastThread;
    
    public BagService() {
        this.logger = LoggerFactory.getLogger(BagService.class);
    }
    
    public Map<Long, BagGoods> getBagGoods(final Role role) {
        final Map<Long, BagGoods> bagGoodsMap = BagCache.BAG_GOODS_MAP.get(role.getId());
        if (bagGoodsMap == null) {
            final List<BagGoods> list = this.dao.query(BagGoods.class, Cnd.where("r_id", "=", (Object)role.getId()));
            final Map<Long, BagGoods> map = list.stream().collect(Collectors.toMap(BagGoods::getId, BagGoods -> BagGoods));
            BagCache.BAG_GOODS_MAP.put(role.getId(), map);
            return map;
        }
        return bagGoodsMap;
    }
    
    public Map<Long, PetEquip> getPetBagEquip(final long id) {
        final Map<Long, PetEquip> bagGoodsMap = PetCache.PET_EQUIP_MAP.get(id);
        if (bagGoodsMap == null) {
            final List<PetEquip> list = this.dao.query(PetEquip.class, Cnd.where("r_id", "=", (Object)id));
            final Map<Long, PetEquip> map = list.stream().collect(Collectors.toMap(PetEquip::getId, PetEquip -> PetEquip));
            PetCache.PET_EQUIP_MAP.put(id, map);
            return map;
        }
        return bagGoodsMap;
    }
    
    public List<PetEquip> getPetWearEquip(final long id, final long pId) {
        final Map<Long, PetEquip> petBagEquip = this.getPetBagEquip(id);
        if (petBagEquip == null || petBagEquip.size() <= 0) {
            return null;
        }
        return petBagEquip.values().stream().filter(e -> e.getPId() == pId).collect(Collectors.toList());
    }
    
    public Map<Long, BagEquip> getBagEquip(final Role role) {
        final Map<Long, BagEquip> bagGoodsMap = BagCache.BAG_EQUIP_MAP.get(role.getId());
        if (bagGoodsMap == null) {
            final List<BagEquip> list = this.dao.query(BagEquip.class, Cnd.where("r_id", "=", (Object)role.getId()));
            list.forEach(equip -> equip.doAfterInit());
            final Map<Long, BagEquip> map = list.stream().collect(Collectors.toMap(BagEquip::getId, BagEquip -> BagEquip));
            BagCache.BAG_EQUIP_MAP.put(role.getId(), map);
            return map;
        }
        return bagGoodsMap;
    }
    
    public boolean checkPos(final Role role, final int serial, final int num) {
        boolean flag = false;
        final ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(serial);
        if (easyInfo != null && easyInfo.getCategory() == ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue()) {
            if (easyInfo.getIsOpen() == 1) {
                final int size = easyInfo.getExtra().size() + easyInfo.getCycle();
                if (size <= 0) {
                    return true;
                }
                for (int i = 0; i < size; ++i) {
                    final int pos = this.getPos(role, false);
                    if (pos <= -1) {
                        flag = false;
                        break;
                    }
                    flag = true;
                }
            }
            else {
                flag = this.checkCanMerge(role, DataGlobalFunc.GetItemDataByItemTypeId(serial), serial, num);
            }
        }
        else {
            if (easyInfo != null && easyInfo.getIsOpen() == 1 && Const.currency.contains(easyInfo.getType())) {
                return true;
            }
            flag = this.checkCanMerge(role, DataGlobalFunc.GetItemDataByItemTypeId(serial), serial, num);
        }
        if (!flag) {
            MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
        }
        return flag;
    }
    
    public boolean checkPos(final Role role, final List<Param> list) {
        for (final Param param : list) {
            if (!ItemClassify.getValue().contains(param.getK())) {
                final boolean pos = this.checkPos(role, param.getK(), param.getV());
                if (!pos) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    public boolean checkCanMerge(final Role role, final Map map, final int serial, int num) {
        if (map == null) {
            return this.checkOther(role, serial, num);
        }
        boolean flag = false;
        final Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        final Object obj = map.get(serial);
        final JSONObject json = JsonUtils.objectToJson(obj);
        final Integer canMerge = json.getInteger("canMerge");
        if (canMerge == null) {
            return false;
        }
        final List<BagGoods> collect = bagGoodsMap.values().stream().filter(item -> item.getSerial() == serial).collect(Collectors.toList());
        if (collect.size() > 0) {
            for (final BagGoods goods : collect) {
                final int residue = canMerge - DataGlobalFunc.getBagGoodsNum(goods);
                if (residue <= 0) {
                    continue;
                }
                if (residue >= num) {
                    flag = true;
                    break;
                }
                num = Math.abs(residue - num);
            }
        }
        if (!flag) {
            final int pos = this.getPos(role, false);
            if (pos > -1) {
                flag = true;
            }
        }
        return flag;
    }
    
    public boolean checkOther(final Role role, final int serial, final int num) {
        if (serial == 9997) {
            final RoleTianFu roleTianFu = DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getTianFuSerial() == serial && obj.getStorePos() == 0).findFirst().orElse(null);
            if (roleTianFu != null) {
                if (roleTianFu.getCount() + num > 9999) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符栏空间不足"));
                    return false;
                }
            }
            else if (num > 9999) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符栏空间不足"));
                return false;
            }
        }
        return true;
    }
    
    public int getPos(final IoSession session, final boolean is) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return -1;
        }
        return this.getPos(role, is);
    }
    
    public int getPos(final Role role, final boolean is) {
        return this.getMinAndMaxPos(role, is);
    }
    
    private Integer getMinAndMaxPos(final Role role, final boolean is) {
        final List<Integer> list = new ArrayList<Integer>();
        if (is) {
            final Map<Long, PetEquip> petEquipMap = PetCache.PET_EQUIP_MAP.get(role.getId());
            if (petEquipMap != null) {
                final int pos = role.getExtendBox().getPetEquipPosition() * Const.BAG_POS + Const.PET_EQUIP_BAG_POS_MIN;
                if (petEquipMap.values().stream().filter(e -> e.getPId() <= 0L).count() >= pos) {
                    return -1;
                }
            }
            return 1;
        }
        final Map<Long, BagEquip> bagEquipMap = this.getBagEquip(role);
        for (final Long key : bagEquipMap.keySet()) {
            final BagEquip equip = bagEquipMap.get(key);
            list.add(equip.getPos());
        }
        final Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        for (final Long key2 : bagGoodsMap.keySet()) {
            final BagGoods bagGoods = bagGoodsMap.get(key2);
            list.add(bagGoods.getPos());
        }
        return this.getVacancySize(role, list, is);
    }
    
    public int residuePetEquipBagPos(final Role role) {
        int totalPos = 0;
        int used = 0;
        final Map<Long, PetEquip> petEquipMap = PetCache.PET_EQUIP_MAP.get(role.getId());
        if (petEquipMap != null) {
            used = petEquipMap.size();
        }
        totalPos = role.getExtendBox().getPetEquipPosition() * Const.BAG_POS + Const.PET_EQUIP_BAG_POS_MIN;
        return totalPos - used;
    }
    
    public int residueBagPos(final Role role, final boolean isStore) {
        int totalPos = 0;
        int used = 0;
        if (!isStore) {
            final Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
            final Map<Long, BagEquip> bagEquipMap = this.getBagEquip(role);
            totalPos = role.getExtendBox().getPosition() * Const.BAG_POS + role.getRelive() * Const.BAG_POS + Const.BAG_POS_MIN;
            used = bagGoodsMap.size() + bagEquipMap.size();
        }
        return totalPos - used;
    }
    
    public Integer getVacancySize(final Role role, final List<Integer> list, final boolean is) {
        final List<Integer> objects = new ArrayList<Integer>();
        Integer pos = 0;
        if (is) {
            pos = role.getExtendBox().getPetEquipPosition() * Const.BAG_POS + Const.PET_EQUIP_BAG_POS_MIN;
        }
        else {
            pos = role.getExtendBox().getPosition() * Const.BAG_POS + role.getRelive() * Const.BAG_POS + Const.BAG_POS_MIN;
        }
        if (list.size() < pos) {
            for (int i = 1; i <= pos; ++i) {
                objects.add(i);
            }
            final List<Integer> vacancy = new ArrayList<Integer>();
            for (final Integer v : objects) {
                if (!list.contains(v)) {
                    vacancy.add(v);
                }
            }
            return (vacancy.size() <= 0) ? Integer.valueOf(-1) : Collections.min((Collection<? extends Integer>)vacancy);
        }
        return -1;
    }
    
    public void addPetEquip(final Role role, final PetEquip equip, final boolean flag, final String cause) {
        equip.save();
        final Map<Long, PetEquip> bagEquipMap = this.getPetBagEquip(role.getId());
        bagEquipMap.put(equip.getId(), equip);
        this.refreshPetEquip(role, equip);
        if (flag) {
            final RespIconCartoon respIconCartoon = new RespIconCartoon();
            respIconCartoon.setSerial(equip.getSerial());
            respIconCartoon.setOnlyId(String.valueOf(equip.getId()));
            MessagePusher.pushMessage(role, respIconCartoon);
        }
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)1, cause + "，装备入包 - " + JsonUtils.object2String(equip), role.getMarking());
    }
    
    public void addEquip(final Role role, final BagEquip equip, final boolean flag, final String cause) {
        equip.save();
        final Map<Long, BagEquip> bagEquipMap = this.getBagEquip(role);
        bagEquipMap.put(equip.getId(), equip);
        this.refreshRoleEquip(role, equip, false);
        if (flag) {
            final RespIconCartoon respIconCartoon = new RespIconCartoon();
            respIconCartoon.setSerial(equip.getType());
            respIconCartoon.setOnlyId(String.valueOf(equip.getId()));
            MessagePusher.pushMessage(role, respIconCartoon);
        }
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)1, cause + "，装备入包 - " + JsonUtils.object2String(equip), role.getMarking());
    }
    
    public int addGoods(final Role role, final int serial, int num, final int bd, final boolean isCartoon, final boolean isRecord, String cause) {
        if (num <= 0) {
            this.logger.error("添加道具到背包错误，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", new Object[] { role.getRoleId(), role.getName(), serial, num, cause });
            return -1;
        }
        if (num >= 5999) {
            this.logger.error("添加道具到背包可能异常，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", new Object[] { role.getRoleId(), role.getName(), serial, num, cause });
        }
        final int record = num;
        final Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        final ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(serial);
        if (easyInfo != null && easyInfo.getIsOpen() == 1) {
            if (Const.currency.contains(easyInfo.getType())) {
                this.roleService.addCurrency(role, easyInfo.getType() % 1000, easyInfo.getValue() * (long)num, "添加道具到背包", true, false);
                role.save();
                return -1;
            }
            if (easyInfo.getCategory() == ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue()) {
                for (int i = 0; i < num; ++i) {
                    final boolean gift = this.openGift(role, serial, false);
                    if (!gift) {
                        break;
                    }
                }
                return -1;
            }
            if (this.otherItem(role, serial, num, cause)) {
                return -1;
            }
            this.logger.error("角色{} 直接开启物品未处理：编号为 = {}, 数量 = {}", new Object[] { role.getName(), serial, num });
        }
        else if (this.otherItem(role, serial, num, cause)) {
            return -1;
        }
        final Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(serial);
        if (type >= ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue() && type <= ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue()) {
            return -1;
        }
        final Map map = DataGlobalFunc.GetItemDataByItemTypeId(serial);
        if (map != null) {
            final Object obj = map.get(serial);
            final JSONObject json = JsonUtils.objectToJson(obj);
            final int canMerge = json.getInteger("canMerge");
            final Integer original = this.getBagGoodsNum(role, serial);
            final List<BagGoods> collect = bagGoodsMap.values().stream().filter(e -> e.getSerial() == serial).collect(Collectors.toList());
            if (collect.size() > 0) {
                for (final BagGoods goods : collect) {
                    final int residue = canMerge - DataGlobalFunc.getBagGoodsNum(goods);
                    if (residue <= 0) {
                        continue;
                    }
                    final int count = 0;
                    if (residue >= num) {
                        this.saveBagGoods(role, serial, num, goods, count, bd, isCartoon);
                        num = 0;
                        break;
                    }
                    num = Math.abs(residue - num);
                    this.saveBagGoods(role, serial, residue, goods, num, bd, isCartoon);
                }
            }
            while (num > 0) {
                int add = 0;
                final int mod = num - canMerge;
                if (mod > 0) {
                    add = canMerge;
                }
                else {
                    add = num;
                }
                num = mod;
                this.addBagGoods(role, bagGoodsMap, serial, add, bd, isCartoon);
            }
            if (cause == null) {
                cause = "";
            }
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)1, String.format("道具：%d，原有数量：%d，新增数量：%d，原因：%s", serial, original, record, cause), role.getMarking());
            return num;
        }
        if (this.otherItem(role, serial, num, cause)) {
            return -1;
        }
        final String format = MessageFormat.format("角色{} 未找到物品：编号为 = {}, 数量 = {}", role.getName(), serial, num);
        this.logger.error(format);
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)1, format, role.getMarking());
        return -1;
    }
    
    public boolean otherItem(final Role role, final int serial, final int num, final String cause) {
        final SysDataCard card = CardDataPool.cardMap.get(serial);
        if (card != null) {
            this.cardService.addCard(role, card.getSerial(), num, 0, true, true);
            return true;
        }
        if (serial == 9997) {
            final RoleTianFuService bean = SpringUtils.getBean(RoleTianFuService.class);
            final List<RoleTianFu> list = DataCache.ROLE_TIAN_FU.get(role.getId());
            RoleTianFu debris = list.stream().filter(obj -> obj.getTianFuSerial() == serial && obj.getStorePos() == 0).findFirst().orElse(null);
            if (debris != null) {
                if (debris.getCount() + num > 9999) {
                    RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)1, String.format("天策格子不足放入临时背包-未处理（1），道具：%d，新增数量：%d，原因：%s", serial, num, cause), role.getMarking());
                    return true;
                }
                debris.setCount((short)(debris.getCount() + num));
            }
            else {
                if (num > 9999) {
                    RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)1, String.format("天策格子不足放入临时背包-未处理（2），道具：%d，新增数量：%d，原因：%s", serial, num, cause), role.getMarking());
                    return true;
                }
                debris = new RoleTianFu();
                debris.setId(IdGenerator.getNextId());
                debris.setRoleId(role.getId());
                debris.setTianFuSerial(9997);
                debris.setCreateTime(new Date());
                debris.setCount((short)num);
                DataCache.ROLE_TIAN_FU.get(role.getId()).add(debris);
            }
            debris.save();
            bean.refreshTianFuDebris(role);
            this.cartoon(role, 9997);
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)1, String.format("道具：%d，新增数量：%d，原因：%s", serial, num, cause), role.getMarking());
            return true;
        }
        return false;
    }
    
    public Integer consumeBagGoods(final Role role, final int serial, final int num, final boolean isRecord, final boolean isSend, String cause) {
        if (num <= 0 && cause != null) {
            this.logger.error("消耗道具错误，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", new Object[] { role.getName(), role.getRoleId(), serial, num, cause });
            return 10000000;
        }
        final Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        final Integer sum = bagGoodsMap.values().stream().filter(e -> e.getSerial() == serial).collect(Collectors.summingInt(e -> DataGlobalFunc.getBagGoodsNum(e)));
        if (sum >= num) {
            final List<BagGoods> collect = bagGoodsMap.values().stream().filter(e -> e.getSerial() == serial).collect(Collectors.toList());
            int count = 0;
            try {
                final Integer original = this.getBagGoodsNum(role, serial);
                for (BagGoods goods : collect) {
                    final int goodsNum = DataGlobalFunc.getBagGoodsNum(goods);
                    final Integer surplus = goodsNum - (num - count);
                    if (surplus <= 0) {
                        goods.setRests(new NutMap());
                        count += goodsNum;
                        goods.setDelete(true);
                        bagGoodsMap.remove(goods.getId());
                        goods.save();
                    }
                    else {
                        count += goodsNum - surplus;
                        goods = this.consume(bagGoodsMap, goods, (surplus > 0) ? (goodsNum - surplus) : goodsNum);
                    }
                    if (isSend) {
                        this.refreshRoleGoods(role, goods, false);
                    }
                    if (count >= num) {
                        break;
                    }
                }
                if (cause == null) {
                    cause = "";
                }
                RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)2, String.format("道具：%d，原有数量：%d，减少数量：%d，原因：%s", serial, original, num, cause), role.getMarking());
            }
            catch (final ConcurrentModificationException e) {
                LoggerUtils.error("消耗道具下标异常", e.getMessage());
            }
            if (isRecord) {}
            return null;
        }
        return num - sum;
    }
    
    public Integer consumeBagGoodsByPos(final Role role, final int num, final int pos, final boolean isRecord, final String cause) {
        if (num <= 0) {
            this.logger.error("根据位置消耗道具错误，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", new Object[] { role.getName(), role.getRoleId(), pos, num, cause });
            return 10000000;
        }
        final Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        BagGoods goods = bagGoodsMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
        if (goods == null) {
            return num;
        }
        final int count = DataGlobalFunc.getBagGoodsNum(goods);
        if (count < num) {
            return num - count;
        }
        final Integer surplus = count - num;
        if (surplus <= 0) {
            goods.setRests(new NutMap());
            goods.setDelete(true);
            goods.save();
            bagGoodsMap.remove(goods.getId());
        }
        else {
            goods = this.consume(bagGoodsMap, goods, (surplus > 0) ? num : count);
        }
        this.refreshRoleGoods(role, goods, false);
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)2, String.format("根据位置消耗道具：%d，减少数量：%d，原因：%s", goods.getSerial(), num, cause), role.getMarking());
        if (isRecord) {}
        return null;
    }
    
    public Integer getBagGoods(final Role role, final int serial, final int num) {
        final Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        final Integer sum = bagGoodsMap.values().stream().filter(goods -> goods.getSerial() == serial).collect(Collectors.summingInt(goods -> DataGlobalFunc.getBagGoodsNum(goods)));
        if (sum >= num) {
            return null;
        }
        return num - sum;
    }
    
    public Integer getBagGoodsNum(final Role role, final Integer serial) {
        final Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        final Integer sum = bagGoodsMap.values().stream().filter(goods -> goods.getSerial() == serial).collect(Collectors.summingInt(goods -> DataGlobalFunc.getBagGoodsNum(goods)));
        return (sum == null) ? 0 : sum;
    }
    
    public boolean saveBagGoods(final Role role, final int serial, final int num, final BagGoods goods, final int surplus, final int bd, final boolean isCartoon) {
        final String key = String.valueOf(bd);
        final int count = goods.getRests().getInt(key);
        if (count > 0) {
            goods.getRests().setv(key, (Object)(count + num));
        }
        else {
            goods.getRests().setv(key, (Object)num);
        }
        this.refreshRoleGoods(role, goods, false);
        if (surplus >= 0 && isCartoon) {
            this.cartoon(role, serial);
        }
        goods.save();
        return true;
    }
    
    public boolean addBagGoods(final Role role, final Map<Long, BagGoods> bagGoodsMap, final int serial, final int num, final int bd, final boolean isCartoon) {
        final int pos = this.getPos(role, false);
        if (pos <= 0) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("物品栏空间不足,已经物品放置临时包裹"));
            this.saveTemporary(role, serial, 1, num, bd);
            return false;
        }
        final NutMap nutMap = new NutMap();
        nutMap.setv(String.valueOf(bd), (Object)num);
        final BagGoods goods = new BagGoods(this.worker.nextId(), role.getId(), serial, pos, nutMap);
        if (Const.treasureMap.contains(serial)) {
            final NutMap specialMap = this.treasureMapHand();
            goods.setSpecial(specialMap);
        }
        if (isCartoon) {
            this.cartoon(role, serial);
        }
        this.refreshRoleGoods(role, goods, false);
        goods.save();
        bagGoodsMap.put(goods.getId(), goods);
        return true;
    }
    
    public NutMap treasureMapHand() {
        final Random random = new Random();
        final int index = random.nextInt(Const.treasureScene.size());
        final Integer serial = Const.treasureScene.get(index);
        final Pos pos = DataGlobalFunc.getTreasureMap(serial);
        final NutMap map = new NutMap();
        map.setv("map", (Object)pos.getMapId());
        map.setv("x", (Object)pos.getX());
        map.setv("y", (Object)pos.getY());
        return map;
    }
    
    public void cartoon(final Role role, final Integer serial) {
        if (this.roleService.mouserCartoon(role, serial.toString(), 200L)) {
            final RespIconCartoon respIconCartoon = new RespIconCartoon();
            respIconCartoon.setSerial(serial);
            MessagePusher.pushMessage(role, respIconCartoon);
        }
    }
    
    public void refreshPetEquip(final Role role, final PetEquip equip) {
        final RespFlyPetEqInfo respFlyPetEqInfo = new RespFlyPetEqInfo();
        respFlyPetEqInfo.setEquip(new PetEquipEasy(equip));
        MessagePusher.pushMessage(role, respFlyPetEqInfo);
    }
    
    public void refreshRoleEquip(final Role role, final BagEquip equip, final boolean isStore) {
        final RespFlyInfo respFlyInfo = new RespFlyInfo();
        respFlyInfo.setEquip(new EquipEasy(equip, role.getRoleId()));
        if (isStore) {
            respFlyInfo.setPos((byte)1);
        }
        MessagePusher.pushMessage(role, respFlyInfo);
    }
    
    public void refreshRoleGoods(final Role role, final BagGoods goods, final boolean isStore) {
        final RespFlyInfo respFlyInfo = new RespFlyInfo();
        respFlyInfo.setType((byte)1);
        final List<FiedValue> list = new ArrayList<FiedValue>();
        final NutMap rests = goods.getRests();
        try {
            for (final String key : rests.keySet()) {
                list.add(new FiedValue(key, (byte)3, rests.getInt(key)));
            }
            final NutMap special = goods.getSpecial();
            for (final String key2 : special.keySet()) {
                list.add(new FiedValue(key2, (byte)3, special.getInt(key2)));
            }
        }
        catch (final ConcurrentModificationException ex) {}
        respFlyInfo.setGoods(new Goods(String.valueOf(goods.getId()), DataGlobalFunc.getBagGoodsNum(goods), goods.getSerial(), goods.getPos(), list));
        if (isStore) {
            respFlyInfo.setPos((byte)1);
        }
        MessagePusher.pushMessage(role, respFlyInfo);
    }
    
    public void removeItem(final Role role, final int pos, final int type, final String des) {
        if (type == 0) {
            final Map<Long, BagEquip> bagEquipMap = this.getBagEquip(role);
            final BagEquip bagEquip = bagEquipMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
            if (bagEquip != null) {
                this.deleteRoleEquip(role, bagEquip, des);
            }
        }
        else {
            this.deleteRoleItem(role, pos, des);
        }
    }
    
    public void deleteRoleItem(final Role role, final int pos, final String des) {
        final Map<Long, BagGoods> bagGoods = this.getBagGoods(role);
        final BagGoods goods = bagGoods.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
        if (goods != null) {
            goods.setRests(new NutMap());
            goods.setDelete(true);
            goods.save();
            bagGoods.remove(goods.getId());
            this.deleteInform(role, (byte)0, pos);
            if (des != null) {
                RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)2, des + "，道具编号：" + goods.getSerial(), role.getMarking());
            }
        }
    }
    
    public boolean deleteRoleEquip(final Role role, final BagEquip equip, final String cause) {
        final Map<Long, BagEquip> bagEquipMap = this.getBagEquip(role);
        if (bagEquipMap.get(equip.getId()) != null) {
            bagEquipMap.remove(equip.getId());
            equip.setDelete(true);
            equip.save();
            this.deleteInform(role, (byte)0, equip.getPos());
            EventDispatcher.getInstance().fireEvent(new DelEquipEvent(EventType.DEL_EQUIP, role, equip.getId()));
            if (cause != null) {
                RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)2, cause + "，删除装备，" + JsonUtils.object2String(equip), role.getMarking());
            }
            return true;
        }
        return false;
    }
    
    public void deleteInform(final Role role, final byte type, final int pos) {
        MessagePusher.pushMessage(role, new RespDeleteItem(type, pos));
    }
    
    public void saveTemporary(final Role role, final Integer serial, final Integer type, final Integer num, final Integer bd) {
        final String key = "temporary" + role.getId();
        Overflow overflow = this.redisService.get(key, Overflow.class);
        if (overflow == null) {
            overflow = new Overflow();
        }
        final List<Temporary> list = overflow.getList();
        if (list.size() < 8) {
            list.add(new Temporary(IdUtils.simpleUUID(), serial, type, num, bd));
            overflow.setList(list);
            this.lodeTemporary(role, list);
            this.redisService.set(key, overflow, 0L);
        }
        else {
            final String format = MessageFormat.format("临时背包流失 = 编号：{0}，物品类型：{1}，流失数量：{2}", serial, (type == 0) ? "装备" : "道具", num);
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1000, (byte)2, format, role.getMarking());
        }
    }
    
    public void getTemporary(final Role role, final String id) {
        final String key = "temporary" + role.getId();
        final Overflow overflow = this.redisService.get(key, Overflow.class);
        if (overflow == null) {
            return;
        }
        List<Temporary> list = overflow.getList();
        if ("0".equals(id)) {
            for (int i = list.size() - 1; i >= 0; --i) {
                final Temporary temporary = list.get(i);
                final Overflow fetch = this.fetch(role, list, temporary.getId());
                list = fetch.getList();
                if (!fetch.isFlag()) {
                    break;
                }
            }
        }
        else {
            final Overflow fetch2 = this.fetch(role, list, id);
            list = fetch2.getList();
        }
        if (list.size() > 0) {
            overflow.setList(list);
            this.lodeTemporary(role, list);
            this.redisService.set(key, overflow, 0L);
        }
        else {
            this.redisService.del(key);
            this.lodeTemporary(role, new ArrayList<Temporary>());
        }
    }
    
    public Overflow fetch(final Role role, final List<Temporary> list, final String id) {
        boolean flag = true;
        final Overflow overflow = new Overflow();
        final List<String> dels = new ArrayList<String>();
        final List<Temporary> collect = list.stream().filter(e -> e.getId().equals(id)).collect(Collectors.toList());
        for (final Temporary temporary : collect) {
            if (temporary.getType() == 0) {
                if (this.getPos(role, false) <= -1) {
                    flag = false;
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("你的物品栏已满"));
                    break;
                }
                dels.add(temporary.getId());
                SpringUtils.getBean(EquipService.class).upgradeEquip(role, 0, 11, String.valueOf(temporary.getSerial()), 0, false, "临时背包");
            }
            else {
                if (!this.checkPos(role, temporary.getSerial(), temporary.getNum())) {
                    flag = false;
                    break;
                }
                dels.add(temporary.getId());
                final int num = this.addGoods(role, temporary.getSerial(), temporary.getNum(), temporary.getRests(), true, true, "临时背包取出");
                if (num > 0) {
                    flag = false;
                    list.add(new Temporary(IdUtils.fastUUID(), temporary.getSerial(), temporary.getType(), num, temporary.getRests()));
                    break;
                }
                continue;
            }
        }
        for (final String del : dels) {
            list.removeIf(e -> e.getId().equals(del));
        }
        overflow.setFlag(flag);
        overflow.setList(list);
        return overflow;
    }
    
    public void lodeTemporary(final Role role, final List<Temporary> list) {
        final List<TemporaryVo> objects = new ArrayList<TemporaryVo>();
        for (final Temporary temporary : list) {
            final TemporaryVo temporaryVo = new TemporaryVo();
            temporaryVo.setId(temporary.getId());
            temporaryVo.setSerial(temporary.getSerial());
            temporaryVo.setNum(temporary.getNum());
            objects.add(temporaryVo);
        }
        MessagePusher.pushMessage(role, new RespLodeTemporary(objects));
    }
    
    public void useItem(final Role role, final int pos, short num) {
        final boolean clicker = this.roleService.mouserClicker(role, "use", 250L);
        if (!clicker) {
            return;
        }
        try {
            final Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
            final BagGoods goods = bagGoodsMap.values().stream().filter(item -> item.getPos() == pos).findFirst().orElse(null);
            if (goods == null) {
                return;
            }
            if (DataGlobalFunc.getBagGoodsNum(goods) < num) {
                MessagePusher.pushMessage(role, new RespMsg("物品数量不足"));
                return;
            }
            final Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(goods.getSerial());
            if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue())) {
                for (int i = 0; i < num; ++i) {
                    final boolean b = this.openGift(role, goods.getSerial(), true);
                    if (!b) {
                        break;
                    }
                }
            }
            else if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue())) {
                final SysPet sysPet = PetDataPool.petMap.get(goods.getSerial());
                if (sysPet != null) {
                    final PetService bean = SpringUtils.getBean(PetService.class);
                    final boolean flag = bean.canGetPet(role, sysPet, false);
                    if (flag) {
                        int bd = 0;
                        if (goods.getRests().getInt("2", 0) > 0) {
                            bd = 2;
                        }
                        final Pet pet = bean.addPet(role, sysPet, bd, "宠物碎片合成");
                        bean.loadPet(role, pet);
                        this.deleteRoleItem(role, pos, "宠物碎片合成");
                    }
                }
            }
            else if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_STUFF.getValue())) {
                final ItemEasyInfo info = DataGlobalFunc.getItemEasyInfo(goods.getSerial());
                if (CardDataPool.cardFiveExerciseItem.contains(goods.getSerial())) {
                    if (DataGlobalFunc.needGradeJudge(1, 80, role.getRelive(), role.getLevel(), "") != null) {
                        MessagePusher.pushMessage(role, new RespMsg("#<LG1>五行修炼#1转80级开启，请先提升等级"));
                        return;
                    }
                    final CardExercise cardExercise = this.cardService.getCardExercise(role);
                    final Param wxInfo = this.cardService.getWxInfo(this.cardService.getCardExercise(role), info.getType());
                    if (wxInfo.getK() >= CardDataPool.cardFiveExerciseMaxGrade) {
                        MessagePusher.pushMessage(role, new RespMsg("你已经将该五行修炼至登峰造极"));
                        return;
                    }
                    final Integer integer = this.consumeBagGoods(role, goods.getSerial(), 1, true, true, "使用物品-五行修炼");
                    if (integer != null) {
                        return;
                    }
                    this.cardService.addExp(role, info.getValue(), info.getType(), cardExercise, wxInfo, "使用道具");
                }
                else {
                    final SysEffect sysEffect = ItemDataPool.effectMap.get(info.getApplyObjectSerial());
                    if (sysEffect != null) {
                        final Integer integer2 = this.consumeBagGoods(role, goods.getSerial(), 1, true, true, "使用物品-特效" + info.getName());
                        if (integer2 != null) {
                            return;
                        }
                        SpringUtils.getEquipService().addEffect(role, sysEffect.getSerial(), 0);
                        MessagePusher.pushMessage(role, new RespCherish(info.getSerial(), (short)1));
                    }
                }
            }
            else if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue())) {
                final ItemEasyInfo info = DataGlobalFunc.getItemEasyInfo(goods.getSerial());
                if (info != null) {
                    if (info.getUseCntLimit() > 0) {
                        final int anInt = role.getExtendBox().getDayQuotaLimit().getInt(String.valueOf(info.getSerial()), 0);
                        if (anInt + num > info.getUseCntLimit()) {
                            final int may = info.getUseCntLimit() - anInt;
                            if (may <= 0) {
                                MessagePusher.pushMessage(role, new RespMsg("今日使用" + info.getName() + "已上限，请明日再使用吧！"));
                                return;
                            }
                            num = (short)may;
                        }
                        role.getExtendBox().getDayQuotaLimit().setv(String.valueOf(info.getSerial()), (Object)(anInt + num));
                    }
                    if (info.getType() > 0 && Const.currency.contains(info.getType())) {
                        this.roleService.addCurrency(role, info.getType() % 1000, info.getValue() * (long)num, "使用物品-货币", true, false);
                        this.consumeBagGoods(role, goods.getSerial(), num, true, true, "使用物品-货币");
                    }
                    else if (info.getType() == ItemClassify.STATUS.getId()) {
                        this.useStatus(role, info, num);
                    }
                    else if (info.getType() == ItemClassify.PLUG.getId()) {
                        final int current = new Long(System.currentTimeMillis() / 1000L).intValue();
                        final Setting setting = SpringUtils.getBean(SettingService.class).getSetting(role);
                        int endTime = setting.getPlugEndTime();
                        if (endTime <= 0 || endTime <= current) {
                            endTime = current;
                        }
                        setting.setPlugEndTime(endTime + info.getValue() * num);
                        setting.save();
                        this.consumeBagGoods(role, goods.getSerial(), num, true, true, "使用物品-小助手");
                        MessagePusher.pushMessage(role.getId(), new RespMsg(String.format("小助手到期时间：#<R>%s#", TimeUtil.convertTime(setting.getPlugEndTime()))));
                    }
                    else if (goods.getSerial() == 90013) {
                        this.roleService.addCurrency(role, Const.RESTYPE_TRIBUTE, info.getValue() * (long)num, "使用物品-帮派书简", true, false);
                        this.consumeBagGoods(role, goods.getSerial(), num, true, true, "使用物品-帮派书简");
                    }
                    else if (info.getType() == ItemClassify.HERO_EXP.getId()) {
                        this.roleService.addExp(role, info.getValue() * (long)num, "使用道具", true);
                        this.consumeBagGoods(role, goods.getSerial(), num, true, true, "使用物品-人物经验");
                    }
                    else if (goods.getSerial() == 90009) {
                        if (role.getMasquerade() > 0) {
                            this.consumeBagGoods(role, goods.getSerial(), 1, true, true, "使用物品-还原丹");
                            SpringUtils.getBean(CardService.class).cardExpire(role);
                        }
                    }
                    else {
                        this.logger.error("使用道具未处理：{}", (Object)goods.getSerial());
                    }
                }
                else {
                    this.logger.error("未找到消耗的道具信息：{}", (Object)goods.getSerial());
                }
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("使用物品未处理"));
            }
        }
        catch (final Exception e) {
            this.logger.error("使用物品异常---------------------------------------------------");
            e.printStackTrace();
        }
    }
    
    public boolean openGift(final Role role, final Integer typeId, final boolean isConsume) {
        final SysGiftItem giftItem = ItemDataPool.data_gift_item.get(typeId);
        final List<Item> objects = new ArrayList<Item>();
        objects.addAll(giftItem.getExtra());
        final boolean flag = this.checkPos(role, giftItem.getSerial(), 1);
        if (!flag) {
            return false;
        }
        if (isConsume) {
            final Integer bagGoods = this.consumeBagGoods(role, giftItem.getSerial(), 1, true, true, "开启礼包");
            if (bagGoods != null) {
                return false;
            }
        }
        if (flag && giftItem.getCycle().equals(1)) {
            final List<SysGiftItemOpen> collect = ItemDataPool.data_gift_item_open.stream().filter(item -> item.getGiftSerial().equals(giftItem.getSerial())).collect(Collectors.toList());
            if (collect.size() <= 0) {
                MessagePusher.pushMessage(role, new RespMsg("使用物品失败"));
                return false;
            }
            final List<Lottery> list = new ArrayList<Lottery>();
            collect.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", itemOpen.getRatio())));
            final List<Double> orignalRates = new ArrayList<Double>(list.size());
            for (final Lottery lottery : list) {
                orignalRates.add(lottery.getBili());
            }
            for (int j = 0; j < giftItem.getCycle(); ++j) {
                final int index = LotteryUtils.lottery(orignalRates);
                final Integer serial = list.get(index).getSerial();
                final SysGiftItemOpen giftItemOpen = collect.stream().filter(item -> item.getId().equals(serial)).findFirst().orElse(null);
                if (giftItemOpen == null) {
                    MessagePusher.pushMessage(role, new RespMsg("使用物品错误"));
                    return false;
                }
                objects.add(new Item(giftItemOpen.getSerial(), giftItemOpen.getNum()));
            }
        }
        else if (giftItem.getCycle().equals(8866)) {
            if (giftItem.getParam() == null) {
                return false;
            }
            final Prop prop = JsonUtils.string2Object(giftItem.getParam(), Prop.class);
            final List<Param> list2 = new ArrayList<Param>();
            if (prop.getOnset() == prop.getOver()) {
                list2.add(new Param(prop.getSerial(), prop.getOver()));
            }
            else {
                final List<Integer> integers = Arrays.asList(70, 28, 2);
                final Double num = MakeAttribute.createRndRange(new double[] { prop.getOnset(), prop.getOver() }, integers);
                list2.add(new Param(prop.getSerial(), num.intValue()));
            }
            this.sendReward(role, list2, "开启礼包：" + giftItem.getSerial(), 0);
        }
        if (giftItem.getCoin() > 0L) {
            this.roleService.addCurrency(role, Const.RESTYPE_COIN, giftItem.getCoin(), "开启礼包", true, false);
        }
        if (giftItem.getSilver() > 0L) {
            this.roleService.addCurrency(role, Const.RESTYPE_SILVER, giftItem.getSilver(), "开启礼包", true, false);
        }
        if (giftItem.getGold() > 0L) {
            this.roleService.addCurrency(role, Const.RESTYPE_GOLD, giftItem.getGold(), "开启礼包", true, false);
        }
        if (giftItem.getExp() > 0) {
            this.openGiftExp(role, giftItem.getExp());
        }
        final Iterator<Item> iterator2 = objects.iterator();
        while (iterator2.hasNext()) {
            final Item item = iterator2.next();
            if (item.getItem() > 0 && item.getNum() > 0) {
                this.addGoods(role, item.getItem(), item.getNum(), giftItem.getBind(), true, true, "开启礼包");
            }
        }
        return true;
    }
    
    public void openGiftExp(final Role role, final int serial) {
        final long petExp = 0L;
        long heroExp = 0L;
        Pet pet = null;
        if (serial == 3003) {
            heroExp = BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + (role.getLevel() + role.getRelive() * 15.0) / 140.0 * 2.5) / 30.0 * 3.0).longValue();
            final Integer combatPet = role.getExtendBox().getCombatPet();
            if (combatPet > 0) {
                pet = this.petService.selectPet(role).get(combatPet);
                if (pet != null) {
                    heroExp = BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + (pet.getPetLv() + pet.getRebirth() * 15.0) / 140.0 * 2.5) / 30.0 * 3.0 * 1.2).longValue();
                }
            }
        }
        if (heroExp > 0L) {
            this.roleService.addExp(role, heroExp, "", true);
        }
        if (pet != null && petExp > 0L) {
            this.petService.addExp(role, pet, petExp, true);
        }
    }
    
    public void openGiftTips(final Role role, final Integer serial, final Integer num, final String tips) {
        final Integer itemType = DataGlobalFunc.GetItemTypeByItemTypeId(serial);
        if (itemType == ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue() && DataGlobalFunc.getItemSubTypeByItemTypeId(serial) == 2) {
            final int bookType = DataGlobalFunc.getPetSkillBookTypeByItemTypeId(serial);
            if (bookType == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {}
        }
    }
    
    public BagGoods consume(final Map<Long, BagGoods> bagGoodsMap, final BagGoods goods, int consume) {
        final NutMap rests = goods.getRests();
        final String key = String.valueOf(2);
        final int num = rests.getInt(key, 0);
        if (num < consume) {
            rests.remove((Object)key);
            consume -= num;
            final List<Integer> list = new ArrayList<Integer>();
            for (final String s : rests.keySet()) {
                list.add(Integer.valueOf(s));
            }
            final List<Integer> collect = list.stream().sorted(Comparator.comparing(Integer::intValue)).collect(Collectors.toList());
            for (final Integer key_ : collect) {
                final int anInt = rests.getInt(String.valueOf(key_), 0);
                if (anInt > 0) {
                    if (consume > anInt) {
                        rests.remove((Object)String.valueOf(key_));
                        consume -= anInt;
                    }
                    else {
                        final int residue = anInt - consume;
                        if (residue <= 0) {
                            rests.remove((Object)String.valueOf(key_));
                            break;
                        }
                        rests.setv(String.valueOf(key_), (Object)residue);
                        break;
                    }
                }
            }
        }
        else {
            final int residue2 = num - consume;
            if (residue2 <= 0) {
                rests.remove((Object)String.valueOf(key));
            }
            else {
                rests.setv(String.valueOf(key), (Object)residue2);
            }
        }
        goods.setRests(rests);
        if (DataGlobalFunc.getBagGoodsNum(goods) <= 0) {
            goods.setRests(new NutMap());
            goods.setDelete(true);
            bagGoodsMap.remove(goods.getId());
        }
        goods.save();
        return goods;
    }
    
    public void useDrug(final Role role, final ReqDrugUse drugUse) {
        final Integer goodsNum = this.getBagGoodsNum(role, drugUse.getSerial());
        if (goodsNum <= 0) {
            return;
        }
        final SysDrug drug = ItemDataPool.data_drug.get(drugUse.getSerial());
        if (drug != null) {
            if (drug.getShowClass() == 4 || drug.getUseType() == 2) {
                return;
            }
            if (drug.getDrugAddHpStored() > 0) {
                this.consumeBagGoods(role, drug.getSerial(), 1, true, true, "使用药品-气血储备");
                role.setSaveLife(role.getSaveLife() + drug.getDrugAddHpStored());
                MessagePusher.notify2Player(role, 104, drug.getName(), "气血", drug.getDrugAddHpStored());
                this.roleService.refreshRoleStore(role);
                role.save();
                return;
            }
            if (drug.getDrugAddMpStored() > 0) {
                this.consumeBagGoods(role, drug.getSerial(), 1, true, true, "使用药品-法力储备");
                role.setSaveMana(role.getSaveLife() + drug.getDrugAddMpStored());
                MessagePusher.notify2Player(role, 104, drug.getName(), "法力", drug.getDrugAddMpStored());
                this.roleService.refreshRoleStore(role);
                role.save();
                return;
            }
            if (role.getRoleId().equals(drugUse.getId())) {
                if (drug.getShowClass() == 1) {
                    if (role.getCurrLife() >= role.getMaxLife()) {
                        MessagePusher.notify2Misc(role, false, 101, "你");
                    }
                    else {
                        final int need = role.getMaxLife() - role.getCurrLife();
                        final double addHp = drug.getDrugAddHpValue() + drug.getDrugAddHpPercent() / 100.0 * role.getMaxLife();
                        final int num = BigDecimalUtil.decimalScaleRoundUpInt(0, addHp / need);
                        if (goodsNum > num) {
                            role.setCurrLife(role.getMaxLife());
                            this.consumeBagGoods(role, drug.getSerial(), num, true, true, "使用药品-恢复气血1");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                        else {
                            role.setCurrLife((int)(addHp * goodsNum));
                            this.consumeBagGoods(role, drug.getSerial(), goodsNum, true, true, "使用药品-恢复气血2");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                    }
                }
                else if (drug.getShowClass() == 2) {
                    if (role.getCurrLife() >= role.getMaxLife()) {
                        MessagePusher.notify2Misc(role, false, 102, "你");
                    }
                    else {
                        final int need = role.getMaxLife() - role.getCurrLife();
                        final double addMp = drug.getDrugAddMpValue() + drug.getDrugAddMpPercent() / 100.0 * role.getMaxMana();
                        final int num = BigDecimalUtil.decimalScaleRoundUpInt(0, addMp / need);
                        if (goodsNum > num) {
                            role.setCurrMana(role.getMaxMana());
                            this.consumeBagGoods(role, drug.getSerial(), num, true, true, "使用药品-恢复法力1");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                        else {
                            role.setCurrMana((int)(addMp * goodsNum));
                            this.consumeBagGoods(role, drug.getSerial(), goodsNum, true, true, "使用药品-恢复法力2");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                    }
                }
                else if (drug.getShowClass() == 3) {
                    if (role.getCurrLife() >= role.getMaxLife() && role.getCurrMana() >= role.getCurrMana()) {
                        MessagePusher.notify2Misc(role, false, 103, "你");
                    }
                    else {
                        final int needLife = role.getMaxLife() - role.getCurrLife();
                        final int needMana = role.getMaxLife() - role.getCurrLife();
                        final double addHp2 = drug.getDrugAddHpValue() + drug.getDrugAddHpPercent() / 100.0 * role.getMaxLife();
                        final double addMp2 = drug.getDrugAddMpValue() + drug.getDrugAddMpPercent() / 100.0 * role.getMaxMana();
                        int num2 = 1;
                        if (needLife > 0) {
                            num2 = BigDecimalUtil.decimalScaleRoundUpInt(0, addHp2 / needLife);
                        }
                        if (needMana > 0) {
                            final int upInt = BigDecimalUtil.decimalScaleRoundUpInt(0, addMp2 / needLife);
                            if (num2 < upInt) {
                                num2 = upInt;
                            }
                        }
                        if (goodsNum > num2) {
                            role.setCurrLife(role.getMaxLife());
                            role.setCurrMana(role.getMaxMana());
                            this.consumeBagGoods(role, drug.getSerial(), num2, true, true, "使用药品-双加药1");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                        else {
                            role.setCurrLife((int)(addHp2 * goodsNum));
                            role.setCurrMana((int)(addMp2 * goodsNum));
                            this.consumeBagGoods(role, drug.getSerial(), goodsNum, true, true, "使用药品-双加药2");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                    }
                }
            }
            else {
                final Pet pet = this.petService.selectPet(role).get(drugUse.getId());
                if (pet != null) {
                    final PetService petService = SpringUtils.getPetService();
                    if (drug.getShowClass() == 1) {
                        if (role.getCurrLife() >= role.getMaxLife()) {
                            MessagePusher.notify2Misc(role, false, 101, "#<LB1>" + pet.getPetName() + "#");
                        }
                        else {
                            final int need2 = pet.getMaxLife() - pet.getCurrLife();
                            final double addHp3 = drug.getDrugAddHpValue() + drug.getDrugAddHpPercent() / 100.0 * pet.getMaxLife();
                            final int num3 = BigDecimalUtil.decimalScaleRoundUpInt(0, addHp3 / need2);
                            if (goodsNum > num3) {
                                pet.setCurrLife(pet.getMaxLife());
                                this.consumeBagGoods(role, drug.getSerial(), num3, true, true, "宠物使用药品-血药1");
                                petService.refreshPetInfo(role, pet);
                            }
                            else {
                                pet.setCurrLife((int)(addHp3 * goodsNum));
                                this.consumeBagGoods(role, drug.getSerial(), goodsNum, true, true, "宠物使用药品-血药2");
                                petService.refreshPetInfo(role, pet);
                            }
                        }
                    }
                    else if (drug.getShowClass() == 2) {
                        if (role.getCurrLife() >= role.getMaxLife()) {
                            MessagePusher.notify2Misc(role, false, 102, "#<LB1>" + pet.getPetName() + "#");
                        }
                        else {
                            final int need2 = role.getMaxLife() - role.getCurrLife();
                            final double addMp3 = drug.getDrugAddMpValue() + drug.getDrugAddMpPercent() / 100.0 * role.getMaxMana();
                            final int num3 = BigDecimalUtil.decimalScaleRoundUpInt(0, addMp3 / need2);
                            if (goodsNum > num3) {
                                pet.setCurrMana(pet.getMaxMana());
                                this.consumeBagGoods(role, drug.getSerial(), num3, true, true, "宠物使用药品-蓝药1");
                                petService.refreshPetInfo(role, pet);
                            }
                            else {
                                pet.setCurrMana((int)(addMp3 * goodsNum));
                                this.consumeBagGoods(role, drug.getSerial(), goodsNum, true, true, "宠物使用药品-蓝药2");
                                petService.refreshPetInfo(role, pet);
                            }
                        }
                    }
                    else if (drug.getShowClass() == 3) {
                        if (role.getCurrLife() >= role.getMaxLife() && role.getCurrMana() >= role.getCurrMana()) {
                            MessagePusher.notify2Misc(role, false, 103, "#<LB1>" + pet.getPetName() + "#");
                        }
                        else {
                            final int needLife2 = pet.getMaxLife() - pet.getCurrLife();
                            final int needMana2 = pet.getMaxLife() - pet.getCurrLife();
                            final double addHp4 = drug.getDrugAddHpValue() + drug.getDrugAddHpPercent() / 100.0 * pet.getMaxLife();
                            final double addMp4 = drug.getDrugAddMpValue() + drug.getDrugAddMpPercent() / 100.0 * pet.getMaxMana();
                            int num4 = 1;
                            if (needLife2 > 0) {
                                num4 = BigDecimalUtil.decimalScaleRoundUpInt(0, addHp4 / needLife2);
                            }
                            if (needMana2 > 0) {
                                final int upInt2 = BigDecimalUtil.decimalScaleRoundUpInt(0, addMp4 / needLife2);
                                if (num4 < upInt2) {
                                    num4 = upInt2;
                                }
                            }
                            if (goodsNum > num4) {
                                pet.setCurrLife(pet.getMaxLife());
                                pet.setCurrMana(pet.getMaxMana());
                                this.consumeBagGoods(role, drug.getSerial(), num4, true, true, "宠物使用药品-双加药1");
                                petService.refreshPetInfo(role, pet);
                            }
                            else {
                                pet.setCurrLife((int)(addHp4 * goodsNum));
                                pet.setCurrMana((int)(addMp4 * goodsNum));
                                this.consumeBagGoods(role, drug.getSerial(), goodsNum, true, true, "宠物使用药品-双加药2");
                                petService.refreshPetInfo(role, pet);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void useStatus(final Role role, final ItemEasyInfo info, final short type) {
        if (type <= 0) {
            role.getExtendBox().getState().remove((Object)info.getDict());
        }
        else {
            final Integer integer = this.consumeBagGoods(role, info.getSerial(), 1, true, true, "使用-" + info.getName());
            if (integer != null) {
                return;
            }
            final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
            role.getExtendBox().getState().setv(info.getDict(), (Object)(currTime + info.getValue()));
        }
        this.roleService.stateRefresh(role);
        role.save();
        SpringUtils.getBroadcastService().sendUpdateAppear(role);
    }
    
    public void useIncense(final Role role, final int serial, int residueTimes) {
        if (serial == 90005) {
            if (residueTimes > 0) {
                final Integer goods = this.consumeBagGoods(role, serial, 1, true, true, "使用-引妖香");
                if (goods != null) {
                    return;
                }
            }
            else {
                residueTimes = 0;
            }
            role.getExpireTimeMap().put("incenseExpireTime", residueTimes);
        }
        else if (serial == 90006) {
            final Integer goods = this.consumeBagGoods(role, 90006, 1, true, true, "使用-百里香");
            if (goods != null) {
                return;
            }
            residueTimes = 0;
            role.getExpireTimeMap().put("incenseExpireTime", residueTimes);
        }
        else if (serial == 90007) {
            if (residueTimes > 0) {
                final Integer goods = this.consumeBagGoods(role, serial, 1, true, true, "使用-夺命香");
                if (goods != null) {
                    return;
                }
            }
            role.getPkInfoBox().setIncense(residueTimes);
        }
        this.roleService.stateRefresh(role);
        role.save();
        MessagePusher.pushMessage(role, new RespUseIncense(serial, residueTimes));
    }
    
    public void itemOperate(final Role role, final ReqOperateItem operateItem) {
        final int type = operateItem.getType();
        final boolean clicker = this.roleService.mouserClicker(role, "up_it_" + type, 250L);
        if (!clicker) {
            return;
        }
        final BagHandler handler = BagHelper.INSTANCE.getHandler(type);
        if (handler != null) {
            final String message = handler.acceptOperate(role, operateItem.getPos(), operateItem.getNum(), operateItem.getParam());
            if (!Strings.isEmpty((CharSequence)message) && message != null) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), message, false));
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
        }
    }
    
    public boolean send(final Role role, final int serial, final int type, final int num, final int pos, final String cause, final int bd) {
        if (type == 0) {
            SpringUtils.getBean(EquipService.class).upgradeEquip(role, 0, 11, String.valueOf(serial), 0, false, cause);
            if (pos >= 0) {
                this.deleteRoleItem(role, pos, null);
            }
            return true;
        }
        if (type == 1 || type == 2) {
            if (serial == ItemClassify.RESTYPE_SILVER.getId()) {
                this.roleService.addCurrency(role, Const.RESTYPE_SILVER, (long)num, cause, true, false);
            }
            else if (serial == ItemClassify.RESTYPE_COIN.getId()) {
                this.roleService.addCurrency(role, Const.RESTYPE_COIN, (long)num, cause, true, false);
            }
            else if (serial == ItemClassify.RES_TYPE_ACHIEVEMENT.getId()) {
                this.roleService.addCurrency(role, Const.RES_TYPE_ACHIEVEMENT, (long)num, cause, true, false);
            }
            else {
                this.addGoods(role, serial, num, bd, true, true, cause);
            }
            if (pos >= 0) {
                this.deleteRoleItem(role, pos, cause);
            }
            return true;
        }
        return false;
    }
    
    public void exchange(final Role role, final ReqExchange reqExchange) {
        final boolean clicker = this.roleService.mouserClicker(role, "exchange", 250L);
        if (!clicker) {
            return;
        }
        final SysDataExchange exchange = WelfareDataPool.exchangeMap.get(reqExchange.getSerial());
        if (exchange == null) {
            return;
        }
        boolean flag = false;
        try {
            flag = this.checkPos(role, exchange.getSerial(), exchange.getNum() * reqExchange.getNum());
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("兑换物品检查背包空位出错 物品编号 = {}，物品数量 = {}", (Object)reqExchange.getSerial(), (Object)reqExchange.getNum());
            return;
        }
        if (!flag) {
            return;
        }
        final int needNum = reqExchange.getNum() * exchange.getNeedNum();
        final Integer integer = this.consumeBagGoods(role, exchange.getNeedSerial(), needNum, false, true, "兑换物品");
        if (integer != null) {
            final ItemEasyInfo info = DataGlobalFunc.getItemEasyInfo(exchange.getNeedSerial());
            if (info != null) {
                MessagePusher.pushMessage(role, new RespMsg(info.getName() + "缺少：#<R>" + integer + "#"));
            }
            return;
        }
        if (exchange.getType() == 1) {
            this.addGoods(role, exchange.getSerial(), exchange.getNum() * reqExchange.getNum(), 0, true, true, "兑换物品");
        }
    }
    
    public boolean shelvesConsume(final Role role, final int pos, int consume) {
        final int time = Math.toIntExact(System.currentTimeMillis() / 1000L);
        final Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        final BagGoods goods = bagGoodsMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
        if (goods == null) {
            return false;
        }
        final NutMap rests = goods.getRests();
        final int num = DataGlobalFunc.unrestricted(goods.getRests());
        if (consume > num) {
            return false;
        }
        final List<Integer> list = new ArrayList<Integer>();
        for (final String s : rests.keySet()) {
            final int key = Integer.valueOf(s);
            if (key != 2 && key < time) {
                list.add(key);
            }
        }
        list.add(0);
        for (final Integer key2 : list) {
            final int anInt = rests.getInt(String.valueOf(key2), 0);
            if (anInt > 0) {
                if (consume > anInt) {
                    rests.remove((Object)String.valueOf(key2));
                    consume -= anInt;
                }
                else {
                    final int residue = anInt - consume;
                    if (residue <= 0) {
                        rests.remove((Object)String.valueOf(key2));
                        break;
                    }
                    rests.setv(String.valueOf(key2), (Object)residue);
                    break;
                }
            }
        }
        goods.setRests(rests);
        if (DataGlobalFunc.getBagGoodsNum(goods) <= 0) {
            goods.setRests(new NutMap());
            goods.setDelete(true);
            bagGoodsMap.remove(goods.getId());
        }
        goods.save();
        this.refreshRoleGoods(role, goods, false);
        return true;
    }
    
    public void send(final Role role, final int serial, final byte type, final int num, final int pos, final byte isNotice, final String tips, final int bd) {
        if (type == 0) {
            this.send(role, serial, type, num, pos, "挖宝", bd);
        }
        else if (type == 1 || type == 2) {
            final boolean flag = this.send(role, serial, type, num, pos, "挖宝", bd);
            if (flag && isNotice == 1) {
                final ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(serial);
                if (easyInfo != null && tips != null) {
                    final String msg = String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", serial, 0, 0, serial, serial, easyInfo.getName());
                    this.broadcastThread.add2Queue(String.format(tips, role.getName(), msg, num));
                }
            }
        }
        else if (type == 3) {
            this.cardService.addCard(role, serial, num, 0, true, true);
            this.deleteRoleItem(role, pos, "挖宝");
        }
        else if (type == 100) {
            SpringUtils.getBean(SealDemonHandler.class).flushConditionByLevel(role, num);
            this.deleteRoleItem(role, pos, "挖宝封妖");
        }
        else if (type == 101) {
            SpringUtils.getBean(ThreeScopeHandler.class).flushConditionByLevel(role, num);
            this.deleteRoleItem(role, pos, "挖宝妖王");
        }
    }
    
    public List<BagGoods> getBagItem(final Role role, final int serial) {
        final Map<Long, BagGoods> goodsMap = this.getBagGoods(role);
        return goodsMap.values().stream().filter(e -> e.getSerial() == serial).collect(Collectors.toList());
    }
    
    public void sendReward(final Role role, final List<Param> rewards, final String cause, final int grade) {
        for (final Param reward : rewards) {
            if (reward.getK().equals(ItemClassify.RESTS.getId()) && reward.getV().equals(0) && reward.getT().equals(0)) {
                final List<Item> list = this.taskRewardDynamic(reward.getD());
                for (final Item item : list) {
                    this.rewardItem(role, item.getItem(), item.getNum(), 0, cause);
                }
            }
            else if (reward.getK().equals(ItemClassify.HERO_EXP.getId()) && reward.getT().equals(0)) {
                boolean flag = true;
                if (reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro(reward.getR() / 1000.0);
                }
                if (!flag || reward.getT() != 0) {
                    continue;
                }
                this.roleService.addExp(role, reward.getV(), "", true);
            }
            else if (reward.getK().equals(ItemClassify.PET_EXP.getId()) && reward.getT().equals(0)) {
                boolean flag = true;
                if (reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro(reward.getR() / 1000.0);
                }
                if (!flag || reward.getT() != 0) {
                    continue;
                }
                final Integer combatPet = role.getExtendBox().getCombatPet();
                if (combatPet <= 0) {
                    continue;
                }
                final Pet pet = this.petService.selectPet(role).get(combatPet);
                this.petService.addExp(role, pet, reward.getV(), true);
            }
            else if (reward.getK().equals(ItemClassify.RESTYPE_COIN.getId()) && reward.getT().equals(0)) {
                boolean flag = true;
                if (reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro(reward.getR() / 1000.0);
                }
                if (!flag || reward.getT() != 0) {
                    continue;
                }
                this.roleService.addCurrency(role, Const.RESTYPE_COIN, (long)reward.getV(), "奖励派发1 - " + cause, true, false);
            }
            else if (reward.getK().equals(ItemClassify.RESTYPE_SILVER.getId()) && reward.getT().equals(0)) {
                boolean flag = true;
                if (reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro(reward.getR() / 1000.0);
                }
                if (!flag || reward.getT() != 0) {
                    continue;
                }
                this.roleService.addCurrency(role, Const.RESTYPE_SILVER, (long)reward.getV(), "奖励派发2 - " + cause, true, false);
            }
            else if (reward.getK().equals(ItemClassify.RES_TYPE_BRAC.getId()) && reward.getT().equals(0)) {
                boolean flag = true;
                if (reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro(reward.getR() / 1000.0);
                }
                if (!flag || reward.getT() != 0) {
                    continue;
                }
                this.roleService.addCurrency(role, Const.RESTYPE_BRAC, (long)reward.getV(), "降魔积分 - " + cause, true, false);
            }
            else if (reward.getK().equals(ItemClassify.RES_TYPE_BOUND_GOLD.getId()) && reward.getT().equals(0)) {
                boolean flag = true;
                if (reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro(reward.getR() / 1000.0);
                }
                if (!flag || reward.getT() != 0) {
                    continue;
                }
                this.roleService.addCurrency(role, Const.RES_TYPE_BOUND_GOLD, (long)reward.getV(), "奖励派发 3 " + cause, true, false);
            }
            else if (reward.getK().equals(ItemClassify.RES_TYPE_GOLD.getId()) && reward.getT().equals(0)) {
                boolean flag = true;
                if (reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro(reward.getR() / 1000.0);
                }
                if (!flag || reward.getT() != 0) {
                    continue;
                }
                this.roleService.addCurrency(role, Const.RESTYPE_GOLD, (long)reward.getV(), "奖励派发 4 - " + cause, true, false);
            }
            else if (reward.getK().equals(ItemClassify.RES_TYPE_CHARGE_SCORE.getId()) && reward.getT().equals(0)) {
                boolean flag = true;
                if (reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro(reward.getR() / 1000.0);
                }
                if (!flag || reward.getT() != 0) {
                    continue;
                }
                this.roleService.addCurrency(role, Const.RES_TYPE_CHARGE_SCORE, (long)reward.getV(), "奖励派发充值积分 - " + cause, true, false);
            }
            else if (reward.getK().equals(ItemClassify.RESTYPE_TRIBUTE.getId()) && reward.getT().equals(0)) {
                boolean flag = true;
                if (reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro(reward.getR() / 1000.0);
                }
                if (!flag || reward.getT() != 0) {
                    continue;
                }
                this.roleService.addCurrency(role, Const.RESTYPE_TRIBUTE, (long)reward.getV(), "奖励派发5 - " + cause, true, false);
            }
            else if (reward.getK().equals(ItemClassify.RES_TYPE_ACHIEVEMENT.getId()) && reward.getT().equals(0)) {
                boolean flag = true;
                if (reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro(reward.getR() / 1000.0);
                }
                if (!flag || reward.getT() != 0) {
                    continue;
                }
                this.roleService.addCurrency(role, Const.RES_TYPE_ACHIEVEMENT, (long)reward.getV(), "奖励派发5 - " + cause, true, false);
            }
            else if (reward.getT().byteValue() == 2 && DataGlobalFunc.GetItemTypeByItemTypeId(reward.getK()).equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue())) {
                final SysPet sysPet = PetDataPool.petMap.get(reward.getK());
                if (sysPet == null) {
                    continue;
                }
                final boolean flag2 = this.petService.canGetPet(role, sysPet, false);
                if (flag2) {
                    final Pet pet = this.petService.addPet(role, sysPet, 0, cause);
                    this.petService.loadPet(role, pet);
                    this.petService.petInfo(role, pet);
                    if (role.getExtendBox().getCombatPet() > 0) {
                        continue;
                    }
                    role.getExtendBox().setCombatPet(pet.getPetId());
                    role.save();
                }
                else {
                    this.rewardItem(role, reward.getK(), reward.getV(), reward.getBd(), cause);
                }
            }
            else {
                if (reward.getT() <= 0) {
                    continue;
                }
                if (reward.getR() <= 0) {
                    this.rewardItem(role, reward.getK(), reward.getV(), reward.getBd(), cause);
                }
                else {
                    if (reward.getR() <= 0) {
                        continue;
                    }
                    final boolean flag = SkillAi.canAffectOnPro(reward.getR() / 1000.0);
                    if (!flag) {
                        continue;
                    }
                    this.rewardItem(role, reward.getK(), reward.getV(), reward.getBd(), cause);
                }
            }
        }
    }
    
    public List<Item> taskRewardDynamic(final String dist) {
        final List<Item> list = new ArrayList<Item>();
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        final List<SysDataTaskRewardDynamic> collect = TaskDataPool.taskRewardDynamicList.stream().filter(e -> e.getDict().equals(dist)).collect(Collectors.toList());
        for (final SysDataTaskRewardDynamic dynamic : collect) {
            final double rate = TaskDataPool.taskRewardDynamicRateList.get(dynamic.getIdentify());
            final boolean flag = SkillAi.canAffectOnPro(rate);
            if (flag) {
                Integer num = map.get(dynamic.getSerial());
                if (num == null) {
                    num = 0;
                }
                final int numCalculate = TaskDataPool.taskRewardDynamicNumList.get(dynamic.getIdentify());
                num += numCalculate;
                if (num <= 0) {
                    continue;
                }
                map.put(dynamic.getSerial(), num);
                list.add(new Item(dynamic.getSerial(), num, dynamic.getNotice()));
            }
        }
        return list;
    }
    
    public void rewardItem(final Role role, final int serial, final int num, final int bd, final String cause) {
        try {
            final Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(serial);
            if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue()) || type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue()) || type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()) || type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue())) {
                this.creationEquip(role, serial, "任务奖励");
            }
            else if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue())) {
                final SysPet sysPet = PetDataPool.petMap.get(serial);
                if (sysPet != null) {
                    this.addGoods(role, serial, num, bd, true, false, "任务奖励物品1：" + cause);
                }
            }
            else {
                final ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(serial);
                if (itemEasyInfo != null) {
                    if (itemEasyInfo.getType().equals(1)) {
                        if (itemEasyInfo.getValue() > 1000) {
                            this.creationEquip(role, itemEasyInfo.getValue(), cause);
                        }
                        else if (itemEasyInfo.getValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
                            final List<SysSinBieEq> bieEqs = EquipDataPool.sinBieEq.values().stream().collect(Collectors.toList());
                            final List<SysSinBieEq> collect = bieEqs.stream().filter(eq -> eq.getPos() == itemEasyInfo.getCostType() && (eq.getOwnerRole().contains(0) || eq.getOwnerRole().contains(role.getSerial()) || eq.getOwnerRole().contains(role.getRace())) && (eq.getSex() == 0 || role.getSex() == eq.getSex()) && (eq.getRace() == 0 || role.getRace() == eq.getRace()) && DataGlobalFunc.getPj(eq.getSerial()) == itemEasyInfo.getItemPJ()).collect(Collectors.toList());
                            final int index = RandomUtil.round(0, collect.size() - 1);
                            this.creationEquip(role, collect.get(index).getSerial(), cause);
                        }
                    }
                    else {
                        this.addGoods(role, serial, num, bd, true, false, "任务奖励物品2：" + cause);
                    }
                }
                else if (serial == 9997) {
                    this.addGoods(role, serial, num, bd, true, false, "任务奖励天策府：" + cause);
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            LoggerUtils.error("任务奖励物品 = {}", e);
        }
    }
    
    public void creationEquip(final Role role, final int serial, final String cause) {
        final EquipAlike equipData = DataGlobalFunc.getEquipData(serial);
        if (equipData != null) {
            SpringUtils.getBean(EquipService.class).upgradeEquip(role, 0, 11, String.valueOf(equipData.getSerial()), 0, false, cause);
        }
    }
}
