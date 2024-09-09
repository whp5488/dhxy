package com.kitty.system.game.bag.service;

import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysDataTaskRewardDynamic;
import com.kitty.system.game.task.pool.TaskDataPool;
import java.util.HashMap;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.activity.handler.time.SealDemonHandler;
import com.kitty.system.game.activity.handler.time.ThreeScopeHandler;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.init.domain.SysDataExchange;
import com.kitty.system.game.bag.message.ReqExchange;
import com.kitty.system.game.bag.helper.BagHandler;
import org.nutz.lang.Strings;
import com.kitty.system.game.bag.helper.BagHelper;
import com.kitty.system.game.bag.message.ReqOperateItem;
import com.kitty.system.game.bag.message.RespUseIncense;
import com.kitty.system.admin.service.AdminService;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.game.bag.message.ReqDrugUse;
import java.util.Comparator;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.init.domain.SysGiftItemOpen;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import com.kitty.system.game.equip.make.MakeAttribute;
import java.util.Arrays;
import com.kitty.system.init.domain.vo.Prop;
import com.kitty.system.init.domain.SysGiftItem;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.card.domain.CardExercise;
import com.kitty.system.game.role.domain.Setting;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.common.message.RespCherish;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysEffect;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.role.service.SettingService;
import com.kitty.system.game.bag.message.RespLodeTemporary;
import com.kitty.system.game.bag.message.vo.TemporaryVo;
import com.kitty.system.game.equip.service.EquipService;
import java.io.Serializable;
import com.kitty.system.game.bag.domain.vo.Temporary;
import com.kitty.common.utils.uuid.IdUtils;
import com.kitty.system.game.bag.domain.Overflow;
import com.kitty.system.game.bag.message.RespDeleteItem;
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
import com.kitty.system.game.train.TrainService;
import java.util.Date;
import com.kitty.common.thread.IdGenerator;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import com.kitty.system.game.train.pool.TrainDataPool;
import com.kitty.system.init.domain.SysDataTrainSeal;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.init.domain.SysDataCard;
import java.text.MessageFormat;
import com.kitty.record.RecordThread;
import com.kitty.system.game.bag.message.RespIconCartoon;
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
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.mina.cache.PetCache;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.List;
import java.util.stream.Collectors;
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
    
    public Map<Long, BagGoods> getBagGoods(Role role) {
        Map<Long, BagGoods> bagGoodsMap = BagCache.BAG_GOODS_MAP.get(role.getId());
        if (bagGoodsMap == null) {
            List<BagGoods> list = this.dao.query(BagGoods.class, Cnd.where("r_id", "=", role.getId()));
            Map<Long, BagGoods> map = list.stream().collect(Collectors.toMap(BagGoods::getId, BagGoods -> BagGoods));
            BagCache.BAG_GOODS_MAP.put(role.getId(), map);
            return map;
        }
        return bagGoodsMap;
    }
    
    public Map<Long, PetEquip> getPetBagEquip(long id) {
        Map<Long, PetEquip> bagGoodsMap = PetCache.PET_EQUIP_MAP.get(id);
        if (bagGoodsMap == null) {
            List<PetEquip> list = this.dao.query(PetEquip.class, Cnd.where("r_id", "=", id));
            Map<Long, PetEquip> map = list.stream().collect(Collectors.toMap(PetEquip::getId, PetEquip -> PetEquip));
            PetCache.PET_EQUIP_MAP.put(id, map);
            return map;
        }
        return bagGoodsMap;
    }
    
    public List<PetEquip> getPetWearEquip(long id, long pId) {
        Map<Long, PetEquip> petBagEquip = this.getPetBagEquip(id);
        if (petBagEquip == null || petBagEquip.size() <= 0) {
            return null;
        }
        return petBagEquip.values().stream().filter(e -> e.getPId() == pId).collect(Collectors.toList());
    }
    
    public Map<Long, BagEquip> getBagEquip(Role role) {
        Map<Long, BagEquip> bagGoodsMap = BagCache.BAG_EQUIP_MAP.get(role.getId());
        if (bagGoodsMap == null) {
            List<BagEquip> list = this.dao.query(BagEquip.class, Cnd.where("r_id", "=", role.getId()));
            list.forEach(equip -> equip.doAfterInit());
            Map<Long, BagEquip> map = list.stream().collect(Collectors.toMap(BagEquip::getId, BagEquip -> BagEquip));
            BagCache.BAG_EQUIP_MAP.put(role.getId(), map);
            return map;
        }
        return bagGoodsMap;
    }
    
    public boolean checkPos(Role role, int serial, int num) {
        boolean flag = false;
        ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(serial);
        if (easyInfo != null && easyInfo.getCategory() == ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue()) {
            if ((int)easyInfo.getIsOpen() == 1) {
                int size = easyInfo.getExtra().size() + easyInfo.getCycle();
                if (size <= 0) {
                    return true;
                }
                int i = 0;
                while (i < size) {
                    int pos = this.getPos(role, false);
                    if (pos <= -1) {
                        flag = false;
                        break;
                    }
                    flag = true;
                    ++i;
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
    
    public boolean checkPos(Role role, List<Param> list) {
        for (Param param : list) {
            if (!ItemClassify.getValue().contains(param.getK())) {
                boolean pos = this.checkPos(role, param.getK(), param.getV());
                if (!pos) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean checkCanMerge(Role role, Map map, int serial, int num) {
        if (map == null) {
            return this.checkOther(role, serial, num);
        }
        boolean flag = false;
        Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        Object obj = map.get(serial);
        JSONObject json = JsonUtils.objectToJson(obj);
        Integer canMerge = json.getInteger("canMerge");
        if (canMerge == null) {
            return false;
        }
        List<BagGoods> collect = bagGoodsMap.values().stream().filter(item -> item.getSerial() == serial).collect(Collectors.toList());
        if (collect.size() > 0) {
            for (BagGoods goods : collect) {
                int residue = canMerge - DataGlobalFunc.getBagGoodsNum(goods);
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
            int pos = this.getPos(role, false);
            if (pos > -1) {
                flag = true;
            }
        }
        return flag;
    }
    
    public boolean checkOther(Role role, int serial, int num) {
        if (serial == 9997) {
            RoleTianFu roleTianFu = (DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getTianFuSerial() == serial && obj.getStorePos() == 0).findFirst().orElse(null);
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
    
    public int getPos(IoSession session, boolean is) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return -1;
        }
        return this.getPos(role, is);
    }
    
    public int getPos(Role role, boolean is) {
        return this.getMinAndMaxPos(role, is);
    }
    
    private Integer getMinAndMaxPos(Role role, boolean is) {
        List<Integer> list = new ArrayList<>();
        if (is) {
            Map<Long, PetEquip> petEquipMap = PetCache.PET_EQUIP_MAP.get(role.getId());
            if (petEquipMap != null) {
                int pos = role.getExtendBox().getPetEquipPosition() * (int)Const.BAG_POS + Const.PET_EQUIP_BAG_POS_MIN;
                if (petEquipMap.values().stream().filter(e -> e.getPId() <= 0L).count() >= (long)pos) {
                    return -1;
                }
            }
            return 1;
        }
        else {
            Map<Long, BagEquip> bagEquipMap = this.getBagEquip(role);
            for (Long key : bagEquipMap.keySet()) {
                BagEquip equip = bagEquipMap.get(key);
                list.add(equip.getPos());
            }
            Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
            for (Long key2 : bagGoodsMap.keySet()) {
                BagGoods bagGoods = bagGoodsMap.get(key2);
                list.add(bagGoods.getPos());
            }
            return this.getVacancySize(role, list, is);
        }
    }
    
    public int residuePetEquipBagPos(Role role) {
        int totalPos = 0;
        int used = 0;
        Map<Long, PetEquip> petEquipMap = PetCache.PET_EQUIP_MAP.get(role.getId());
        if (petEquipMap != null) {
            used = petEquipMap.size();
        }
        totalPos = (int)role.getExtendBox().getPetEquipPosition() * Const.BAG_POS + Const.PET_EQUIP_BAG_POS_MIN;
        return totalPos - used;
    }
    
    public int residueBagPos(Role role, boolean isStore) {
        int totalPos = 0;
        int used = 0;
        if (!isStore) {
            Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
            Map<Long, BagEquip> bagEquipMap = this.getBagEquip(role);
            totalPos = (int)role.getExtendBox().getPosition() * Const.BAG_POS + role.getRelive() * (int)Const.BAG_POS + Const.BAG_POS_MIN;
            used = bagGoodsMap.size() + bagEquipMap.size();
        }
        return totalPos - used;
    }
    
    public Integer getVacancySize(Role role, List<Integer> list, boolean is) {
        List<Integer> objects = new ArrayList<>();
        Integer pos;
        if (is) {
            pos = role.getExtendBox().getPetEquipPosition() * (int) Const.BAG_POS + Const.PET_EQUIP_BAG_POS_MIN;
        }
        else {
            pos = role.getExtendBox().getPosition() * (int) Const.BAG_POS + role.getRelive() * (int) Const.BAG_POS + Const.BAG_POS_MIN;
        }
        if (list.size() < pos) {
            for (int i = 1; i <= pos; ++i) {
                objects.add(i);
            }
            List<Integer> vacancy = new ArrayList<>();
            for (Integer v : objects) {
                if (!list.contains(v)) {
                    vacancy.add(v);
                }
            }
            return (vacancy.size() <= 0) ? Integer.valueOf(-1) : Collections.min(vacancy);
        }
        return -1;
    }
    
    public void addPetEquip(Role role, PetEquip equip, boolean flag, String cause) {
        equip.save();
        Map<Long, PetEquip> bagEquipMap = this.getPetBagEquip(role.getId());
        bagEquipMap.put(equip.getId(), equip);
        this.refreshPetEquip(role, equip);
        if (flag) {
            RespIconCartoon respIconCartoon = new RespIconCartoon();
            respIconCartoon.setSerial(equip.getSerial());
            respIconCartoon.setOnlyId(String.valueOf(equip.getId()));
            MessagePusher.pushMessage(role, respIconCartoon);
        }
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)1, cause + "，装备入包 - " + JsonUtils.object2String(equip), role.getMarking());
    }
    
    public void addEquip(Role role, BagEquip equip, boolean flag, String cause) {
        equip.save();
        Map<Long, BagEquip> bagEquipMap = this.getBagEquip(role);
        bagEquipMap.put(equip.getId(), equip);
        this.refreshRoleEquip(role, equip, false);
        if (flag) {
            RespIconCartoon respIconCartoon = new RespIconCartoon();
            respIconCartoon.setSerial(equip.getType());
            respIconCartoon.setOnlyId(String.valueOf(equip.getId()));
            MessagePusher.pushMessage(role, respIconCartoon);
        }
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)1, cause + "，装备入包 - " + JsonUtils.object2String(equip), role.getMarking());
    }
    
    public int addGoods(Role role, int serial, int num, int bd, boolean isCartoon, boolean isRecord, String cause) {
        if (num <= 0) {
            this.logger.error("添加道具到背包错误，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", role.getRoleId(), role.getName(), serial, num, cause);
            return -1;
        }
        if (num >= 5999) {
            this.logger.error("添加道具到背包可能异常，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", role.getRoleId(), role.getName(), serial, num, cause);
        }
        int record = num;
        Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(serial);
        if (easyInfo != null && easyInfo.getIsOpen() == 1) {
            if (Const.currency.contains(easyInfo.getType())) {
                this.roleService.addCurrency(role, easyInfo.getType() % 1000, (long) easyInfo.getValue() * (long) num, "添加道具到背包", true, false);
                role.save();
                return -1;
            }
            if (easyInfo.getCategory() == ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue()) {
                int i = 0;
                while (i < num) {
                    boolean gift = this.openGift(role, serial, false);
                    if (!gift) {
                        break;
                    }
                    ++i;
                }
                return -1;
            }
            if (this.otherItem(role, serial, num, cause)) {
                return -1;
            }
            this.logger.error("角色{} 直接开启物品未处理：编号为 = {}, 数量 = {}", role.getName(), serial, num);
        }
        else if (this.otherItem(role, serial, num, cause)) {
            return -1;
        }
        Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(serial);
        if ((int)type >= ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue() && type <= ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue()) {
            return -1;
        }
        Map<Object, Object> map = DataGlobalFunc.GetItemDataByItemTypeId(serial);
        if (map == null) {
            if (this.otherItem(role, serial, num, cause)) {
                return -1;
            }
            String format = MessageFormat.format("角色{} 未找到物品：编号为 = {}, 数量 = {}", role.getName(), serial, num);
            this.logger.error(format);
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)1, format, role.getMarking());
            return -1;
        }
        else {
            Object obj = map.get(serial);
            JSONObject json = JsonUtils.objectToJson(obj);
            int canMerge = json.getInteger("canMerge");
            Integer original = this.getBagGoodsNum(role, serial);
            List<BagGoods> collect = bagGoodsMap.values().stream().filter(e -> e.getSerial() == serial).collect(Collectors.toList());
            if (collect.size() > 0) {
                for (BagGoods goods : collect) {
                    int residue = canMerge - DataGlobalFunc.getBagGoodsNum(goods);
                    if (residue <= 0) {
                        continue;
                    }
                    int count = 0;
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
                int mod = num - canMerge;
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
    }
    
    public boolean otherItem(Role role, int serial, int num, String cause) {
        SysDataCard card = CardDataPool.cardMap.get(serial);
        if (card != null) {
            this.cardService.addCard(role, card.getSerial(), num, 0, true, true);
            return true;
        }
        SysDataTrainSeal trainSeal = TrainDataPool.trainSealMap.get(serial);
        if (trainSeal != null) {
            SpringUtils.getBean(TrainService.class).upgrade(role, 0L, 22, String.valueOf(trainSeal.getSerial()), 0, false, "信物道具");
            return true;
        }
        if (serial == 9997) {
            RoleTianFuService bean = SpringUtils.getBean(RoleTianFuService.class);
            List<RoleTianFu> list = DataCache.ROLE_TIAN_FU.get(role.getId());
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
                (DataCache.ROLE_TIAN_FU.get(role.getId())).add(debris);
            }
            debris.save();
            bean.refreshTianFuDebris(role);
            this.cartoon(role, 9997);
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)1, String.format("道具：%d，新增数量：%d，原因：%s", Integer.valueOf(serial), Integer.valueOf(num), cause), role.getMarking());
            return true;
        }
        else {
            return false;
        }
    }
    
    public Integer consumeBagGoods(Role role, int serial, int num, boolean isRecord, boolean isSend, String cause) {
        if (num <= 0 && cause != null) {
            this.logger.error("消耗道具错误，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", role.getName(), role.getRoleId(), serial, num, cause);
            return 10000000;
        }
        Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        Integer sum = bagGoodsMap.values().stream().filter(e -> e.getSerial() == serial).collect(Collectors.summingInt(e -> DataGlobalFunc.getBagGoodsNum(e)));
        if ((int)sum >= num) {
            List<BagGoods> collect = bagGoodsMap.values().stream().filter(e -> e.getSerial() == serial).collect(Collectors.toList());
            int count = 0;
            try {
                Integer original = this.getBagGoodsNum(role, serial);
                for (BagGoods goods : collect) {
                    int goodsNum = DataGlobalFunc.getBagGoodsNum(goods);
                    Integer surplus = goodsNum - (num - count);
                    if (surplus <= 0) {
                        goods.setRests(new NutMap());
                        count += goodsNum;
                        goods.setDelete(true);
                        bagGoodsMap.remove(goods.getId());
                        goods.save();
                    }
                    else {
                        count += goodsNum - surplus;
                        goods = this.consume(bagGoodsMap, goods, ((int)surplus > 0) ? (goodsNum - (int)surplus) : goodsNum);
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
                RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)2, String.format("道具：%d，原有数量：%d，减少数量：%d，原因：%s", Integer.valueOf(serial), original, Integer.valueOf(num), cause), role.getMarking());
            }
            catch (ConcurrentModificationException e) {
                LoggerUtils.error("消耗道具下标异常", e.getMessage());
            }
            return null;
        }
        return Integer.valueOf(num - sum);
    }
    
    public Integer consumeBagGoodsByPos(Role role, int num, int pos, boolean isRecord, String cause) {
        if (num <= 0) {
            this.logger.error("根据位置消耗道具错误，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", role.getName(), role.getRoleId(), pos, num, cause);
            return Integer.valueOf(10000000);
        }
        Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        BagGoods goods = bagGoodsMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
        if (goods != null) {
            int count = DataGlobalFunc.getBagGoodsNum(goods);
            if (count < num) {
                return num - count;
            }
            Integer surplus = count - num;
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
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)2, String.format("根据位置消耗道具：%d，减少数量：%d，原因：%s", Integer.valueOf(goods.getSerial()), Integer.valueOf(num), cause), role.getMarking());
            return null;
        }
        else {
            return num;
        }
    }
    
    public Integer getBagGoods(Role role, int serial, int num) {
        Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        Integer sum = bagGoodsMap.values().stream().filter(goods -> goods.getSerial() == serial).collect(Collectors.summingInt(goods -> DataGlobalFunc.getBagGoodsNum(goods)));
        if (sum >= num) {
            return null;
        }
        return num - sum;
    }
    
    public Integer getBagGoodsNum(Role role, Integer serial) {
        Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        Integer sum = bagGoodsMap.values().stream().filter(goods -> goods.getSerial() == serial).collect(Collectors.summingInt(goods -> DataGlobalFunc.getBagGoodsNum(goods)));
        return (sum == null) ? 0 : sum;
    }
    
    public boolean saveBagGoods(Role role, int serial, int num, BagGoods goods, int surplus, int bd, boolean isCartoon) {
        String key = String.valueOf(bd);
        int count = goods.getRests().getInt(key);
        if (count > 0) {
            goods.getRests().setv(key, count + num);
        }
        else {
            goods.getRests().setv(key, num);
        }
        this.refreshRoleGoods(role, goods, false);
        if (surplus >= 0 && isCartoon) {
            this.cartoon(role, serial);
        }
        goods.save();
        return true;
    }
    
    public boolean addBagGoods(Role role, Map<Long, BagGoods> bagGoodsMap, int serial, int num, int bd, boolean isCartoon) {
        int pos = this.getPos(role, false);
        if (pos <= 0) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("物品栏空间不足,已经物品放置临时包裹"));
            this.saveTemporary(role, serial, 1, num, bd);
            return false;
        }
        NutMap nutMap = new NutMap();
        nutMap.setv(String.valueOf(bd), num);
        BagGoods goods = new BagGoods(this.worker.nextId(), role.getId(), serial, pos, nutMap);
        if (Const.treasureMap.contains(serial)) {
            NutMap specialMap = this.treasureMapHand();
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
        Random random = new Random();
        int index = random.nextInt(Const.treasureScene.size());
        Integer serial = Const.treasureScene.get(index);
        Pos pos = DataGlobalFunc.getTreasureMap(serial);
        NutMap map = new NutMap();
        map.setv("map", pos.getMapId());
        map.setv("x", pos.getX());
        map.setv("y", pos.getY());
        return map;
    }
    
    public void cartoon(Role role, Integer serial) {
        if (this.roleService.mouserCartoon(role, serial.toString(), 200L)) {
            RespIconCartoon respIconCartoon = new RespIconCartoon();
            respIconCartoon.setSerial((int)serial);
            MessagePusher.pushMessage(role, respIconCartoon);
        }
    }
    
    public void refreshPetEquip(Role role, PetEquip equip) {
        RespFlyPetEqInfo respFlyPetEqInfo = new RespFlyPetEqInfo();
        respFlyPetEqInfo.setEquip(new PetEquipEasy(equip));
        MessagePusher.pushMessage(role, respFlyPetEqInfo);
    }
    
    public void refreshRoleEquip(Role role, BagEquip equip, boolean isStore) {
        RespFlyInfo respFlyInfo = new RespFlyInfo();
        respFlyInfo.setEquip(new EquipEasy(equip, role.getRoleId()));
        if (isStore) {
            respFlyInfo.setPos((byte)1);
        }
        MessagePusher.pushMessage(role, respFlyInfo);
    }
    
    public void refreshRoleGoods(Role role, BagGoods goods, boolean isStore) {
        RespFlyInfo respFlyInfo = new RespFlyInfo();
        respFlyInfo.setType((byte)1);
        List<FiedValue> list = new ArrayList<>();
        NutMap rests = goods.getRests();
        try {
            for (String key : rests.keySet()) {
                list.add(new FiedValue(key, (byte)3, rests.getInt(key)));
            }
            NutMap special = goods.getSpecial();
            for (String key2 : special.keySet()) {
                list.add(new FiedValue(key2, (byte)3, special.getInt(key2)));
            }
        }
        catch (ConcurrentModificationException ex) {}
        respFlyInfo.setGoods(new Goods(String.valueOf(goods.getId()), DataGlobalFunc.getBagGoodsNum(goods), Integer.valueOf(goods.getSerial()), Integer.valueOf(goods.getPos()), list));
        if (isStore) {
            respFlyInfo.setPos((byte)1);
        }
        MessagePusher.pushMessage(role, respFlyInfo);
    }
    
    public void removeItem(Role role, int pos, int type, String des) {
        if (type == 0) {
            Map<Long, BagEquip> bagEquipMap = this.getBagEquip(role);
            BagEquip bagEquip = bagEquipMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
            if (bagEquip != null) {
                this.deleteRoleEquip(role, bagEquip, des);
            }
        }
        else {
            this.deleteRoleItem(role, pos, des);
        }
    }
    
    public void deleteRoleItem(Role role, int pos, String des) {
        Map<Long, BagGoods> bagGoods = this.getBagGoods(role);
        BagGoods goods = bagGoods.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
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
    
    public boolean deleteRoleEquip(Role role, BagEquip equip, String cause) {
        Map<Long, BagEquip> bagEquipMap = this.getBagEquip(role);
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
        else {
            return false;
        }
    }
    
    public void deleteInform(Role role, byte type, int pos) {
        MessagePusher.pushMessage(role, new RespDeleteItem(type, pos));
    }
    
    public void saveTemporary(Role role, Integer serial, Integer type, Integer num, Integer bd) {
        String key = "temporary" + role.getId();
        Overflow overflow = this.redisService.get(key, Overflow.class);
        if (overflow == null) {
            overflow = new Overflow();
        }
        List<Temporary> list = overflow.getList();
        if (list.size() < 8) {
            list.add(new Temporary(IdUtils.simpleUUID(), serial, type, num, bd));
            overflow.setList(list);
            this.lodeTemporary(role, list);
            this.redisService.set(key, overflow, 0L);
        }
        else {
            String format = MessageFormat.format("临时背包流失 = 编号：{0}，物品类型：{1}，流失数量：{2}", serial, ((int)type == 0) ? "装备" : "道具", num);
            RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)1000, (byte)2, format, role.getMarking());
        }
    }
    
    public void getTemporary(Role role, String id) {
        String key = "temporary" + role.getId();
        Overflow overflow = this.redisService.get(key, Overflow.class);
        if (overflow == null) {
            return;
        }
        List<Temporary> list = overflow.getList();
        if ("0".equals(id)) {
            int i = list.size() - 1;
            while (i >= 0) {
                Temporary temporary = list.get(i);
                Overflow fetch2 = this.fetch(role, list, temporary.getId());
                list = fetch2.getList();
                if (!fetch2.isFlag()) {
                    break;
                }
                --i;
            }
        }
        else {
            Overflow fetch = this.fetch(role, list, id);
            list = fetch.getList();
        }
        if (list.size() > 0) {
            overflow.setList(list);
            this.lodeTemporary(role, list);
            this.redisService.set(key, overflow, 0L);
        }
        else {
            this.redisService.del(key);
            this.lodeTemporary(role, new ArrayList<>());
        }
    }
    
    public Overflow fetch(Role role, List<Temporary> list, String id) {
        boolean flag = true;
        Overflow overflow = new Overflow();
        List<String> dels = new ArrayList<>();
        List<Temporary> collect = list.stream().filter(e -> e.getId().equals(id)).collect(Collectors.toList());
        for (Temporary temporary : collect) {
            if ((int)temporary.getType() == 0) {
                if (this.getPos(role, false) > -1) {
                    dels.add(temporary.getId());
                    SpringUtils.getBean(EquipService.class).upgradeEquip(role, 0, 11, String.valueOf(temporary.getSerial()), 0, false, "临时背包");
                }
                else {
                    flag = false;
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("你的物品栏已满"));
                    break;
                }
            }
            else if (this.checkPos(role, temporary.getSerial(), temporary.getNum())) {
                dels.add(temporary.getId());
                int num = this.addGoods(role, temporary.getSerial(), temporary.getNum(), temporary.getRests(), true, true, "临时背包取出");
                if (num > 0) {
                    flag = false;
                    list.add(new Temporary(IdUtils.fastUUID(), temporary.getSerial(), temporary.getType(), num, temporary.getRests()));
                    break;
                }
            }
            else {
                flag = false;
                break;
            }
        }
        for (String del : dels) {
            list.removeIf(e -> e.getId().equals(del));
        }
        overflow.setFlag(flag);
        overflow.setList(list);
        return overflow;
    }
    
    public void lodeTemporary(Role role, List<Temporary> list) {
        List<TemporaryVo> objects = new ArrayList<>();
        for (Temporary temporary : list) {
            TemporaryVo temporaryVo = new TemporaryVo();
            temporaryVo.setId(temporary.getId());
            temporaryVo.setSerial(temporary.getSerial());
            temporaryVo.setNum(temporary.getNum());
            objects.add(temporaryVo);
        }
        MessagePusher.pushMessage(role, new RespLodeTemporary(objects));
    }
    
    public void useItem(Role role, int pos, short num) {
        boolean clicker = this.roleService.mouserClicker(role, "use", 250L);
        if (!clicker) {
            return;
        }
        try {
            Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
            BagGoods goods = bagGoodsMap.values().stream().filter(item -> item.getPos() == pos).findFirst().orElse(null);
            if (goods == null) {
                return;
            }
            if (DataGlobalFunc.getBagGoodsNum(goods) < num) {
                MessagePusher.pushMessage(role, new RespMsg("物品数量不足"));
                return;
            }
            Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(goods.getSerial());
            if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue())) {
                int i = 0;
                while (i < num) {
                    boolean b = this.openGift(role, goods.getSerial(), true);
                    if (!b) {
                        break;
                    }
                    ++i;
                }
            }
            else if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue())) {
                SysPet sysPet = PetDataPool.petMap.get(goods.getSerial());
                if (sysPet != null) {
                    PetService bean = SpringUtils.getBean(PetService.class);
                    boolean flag = bean.canGetPet(role, sysPet, false);
                    if (flag) {
                        int bd = 0;
                        if (goods.getRests().getInt("2", 0) > 0) {
                            bd = 2;
                        }
                        Pet pet = bean.addPet(role, sysPet, bd, "宠物碎片合成");
                        bean.loadPet(role, pet);
                        this.deleteRoleItem(role, pos, "宠物碎片合成");
                    }
                }
            }
            else if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_STUFF.getValue())) {
                ItemEasyInfo info = DataGlobalFunc.getItemEasyInfo(goods.getSerial());
                if (CardDataPool.cardFiveExerciseItem.contains(goods.getSerial())) {
                    if (DataGlobalFunc.needGradeJudge(1, 80, (int)role.getRelive(), (int)role.getLevel(), "") != null) {
                        MessagePusher.pushMessage(role, new RespMsg("#<LG1>五行修炼#1转80级开启，请先提升等级"));
                        return;
                    }
                    CardExercise cardExercise = this.cardService.getCardExercise(role);
                    Param wxInfo = this.cardService.getWxInfo(this.cardService.getCardExercise(role), (int)info.getType());
                    if ((int)wxInfo.getK() >= CardDataPool.cardFiveExerciseMaxGrade) {
                        MessagePusher.pushMessage(role, new RespMsg("你已经将该五行修炼至登峰造极"));
                        return;
                    }
                    Integer integer2 = this.consumeBagGoods(role, goods.getSerial(), 1, true, true, "使用物品-五行修炼");
                    if (integer2 != null) {
                        return;
                    }
                    this.cardService.addExp(role, (int)info.getValue(), (int)info.getType(), cardExercise, wxInfo, "使用道具");
                }
                else {
                    SysEffect sysEffect = (SysEffect)ItemDataPool.effectMap.get(info.getApplyObjectSerial());
                    if (sysEffect != null) {
                        Integer integer = this.consumeBagGoods(role, goods.getSerial(), 1, true, true, "使用物品-特效" + info.getName());
                        if (integer != null) {
                            return;
                        }
                        SpringUtils.getEquipService().addEffect(role, sysEffect.getSerial(), 0);
                        MessagePusher.pushMessage(role, new RespCherish((int)info.getSerial(), (short)1));
                    }
                }
            }
            else if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue())) {
                ItemEasyInfo info = DataGlobalFunc.getItemEasyInfo(goods.getSerial());
                if (info != null) {
                    if (info.getUseCntLimit() > 0) {
                        int anInt = role.getExtendBox().getDayQuotaLimit().getInt(String.valueOf(info.getSerial()), 0);
                        if (anInt + num > info.getUseCntLimit()) {
                            int may = info.getUseCntLimit() - anInt;
                            if (may <= 0) {
                                MessagePusher.pushMessage(role, new RespMsg("今日使用" + info.getName() + "已上限，请明日再使用吧！"));
                                return;
                            }
                            num = (short)may;
                        }
                        role.getExtendBox().getDayQuotaLimit().setv(String.valueOf(info.getSerial()), anInt + num);
                    }
                    if (info.getType() > 0 && Const.currency.contains(info.getType())) {
                        this.roleService.addCurrency(role, info.getType() % 1000, (long) info.getValue() * (long) num, "使用物品-货币", true, false);
                        this.consumeBagGoods(role, goods.getSerial(), num, true, true, "使用物品-货币");
                    }
                    else if (info.getType() == ItemClassify.STATUS.getId()) {
                        this.useStatus(role, info, num);
                    }
                    else if (info.getType() == ItemClassify.CHARGE.getId()) {
                        this.useCharge(role, info, num);
                    }
                    else if (info.getType() == ItemClassify.PLUG.getId()) {
                        int current = new Long(System.currentTimeMillis() / 1000L).intValue();
                        Setting setting = SpringUtils.getBean(SettingService.class).getSetting(role);
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
                        this.roleService.addCurrency(role, Const.RESTYPE_TRIBUTE, (long) info.getValue() * (long) num, "使用物品-帮派书简", true, false);
                        this.consumeBagGoods(role, goods.getSerial(), num, true, true, "使用物品-帮派书简");
                    }
                    else if ((int)info.getType() == ItemClassify.HERO_EXP.getId()) {
                        this.roleService.addExp(role, (long)info.getValue() * (long)num, "使用道具", true);
                        this.consumeBagGoods(role, goods.getSerial(), num, true, true, "使用物品-人物经验");
                    }
                    else if (goods.getSerial() == 90009) {
                        if (role.getMasquerade() > 0) {
                            this.consumeBagGoods(role, goods.getSerial(), 1, true, true, "使用物品-还原丹");
                            ((CardService)SpringUtils.getBean(CardService.class)).cardExpire(role);
                        }
                    }
                    else {
                        this.logger.error("使用道具未处理：{}", goods.getSerial());
                    }
                }
                else {
                    this.logger.error("未找到消耗的道具信息：{}", goods.getSerial());
                }
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("使用物品未处理"));
            }
        }
        catch (Exception e) {
            this.logger.error("使用物品异常---------------------------------------------------");
            e.printStackTrace();
        }
        return;
    }
    
    public boolean openGift(Role role, Integer typeId, boolean isConsume) {
        SysGiftItem giftItem = (SysGiftItem)ItemDataPool.data_gift_item.get(typeId);
        List<Item> objects = new ArrayList<>();
        objects.addAll(giftItem.getExtra());
        boolean flag = this.checkPos(role, (int)giftItem.getSerial(), 1);
        if (!flag) {
            return false;
        }
        if (isConsume) {
            Integer bagGoods = this.consumeBagGoods(role, (int)giftItem.getSerial(), 1, true, true, "开启礼包");
            if (bagGoods != null) {
                return false;
            }
        }
        if (flag && giftItem.getCycle().equals(Integer.valueOf(1))) {
            List<SysGiftItemOpen> collect = (List)ItemDataPool.data_gift_item_open.stream().filter(item -> item.getGiftSerial().equals(giftItem.getSerial())).collect(Collectors.toList());
            if (collect.size() <= 0) {
                MessagePusher.pushMessage(role, new RespMsg("使用物品失败"));
                return false;
            }
            List<Lottery> list2 = new ArrayList<>();
            collect.forEach(itemOpen -> list2.add(new Lottery(itemOpen.getId(), "", itemOpen.getRatio())));
            List<Double> orignalRates = new ArrayList(list2.size());
            for (Lottery lottery : list2) {
                orignalRates.add(lottery.getBili());
            }
            int j = 0;
            while (j < (int)giftItem.getCycle()) {
                int index = LotteryUtils.lottery(orignalRates);
                Integer serial = ((Lottery)list2.get(index)).getSerial();
                SysGiftItemOpen giftItemOpen = (SysGiftItemOpen)collect.stream().filter(item -> item.getId().equals(serial)).findFirst().orElse(null);
                if (giftItemOpen != null) {
                    objects.add(new Item(giftItemOpen.getSerial(), giftItemOpen.getNum()));
                    ++j;
                }
                else {
                    MessagePusher.pushMessage(role, new RespMsg("使用物品错误"));
                    return false;
                }
            }
        }
        else if (giftItem.getCycle().equals(Integer.valueOf(8866))) {
            if (giftItem.getParam() == null) {
                return false;
            }
            Prop prop = (Prop)JsonUtils.string2Object(giftItem.getParam(), Prop.class);
            List<Param> list = new ArrayList<>();
            if (prop.getOnset() == prop.getOver()) {
                list.add(new Param(Integer.valueOf(prop.getSerial()), Integer.valueOf(prop.getOver())));
            }
            else {
                List<Integer> integers = Arrays.asList(Integer.valueOf(70), Integer.valueOf(28), Integer.valueOf(2));
                Double num = Double.valueOf(MakeAttribute.createRndRange(new double[] { (double)prop.getOnset(), (double)prop.getOver() }, integers));
                list.add(new Param(Integer.valueOf(prop.getSerial()), Integer.valueOf(num.intValue())));
            }
            this.sendReward(role, list, "开启礼包：" + giftItem.getSerial(), 0);
        }
        if ((long)giftItem.getCoin() > 0L) {
            this.roleService.addCurrency(role, Const.RESTYPE_COIN, giftItem.getCoin(), "开启礼包", true, false);
        }
        if ((long)giftItem.getSilver() > 0L) {
            this.roleService.addCurrency(role, Const.RESTYPE_SILVER, giftItem.getSilver(), "开启礼包", true, false);
        }
        if ((long)giftItem.getGold() > 0L) {
            this.roleService.addCurrency(role, Const.RESTYPE_GOLD, giftItem.getGold(), "开启礼包", true, false);
        }
        if (giftItem.getExp() > 0) {
            this.openGiftExp(role, giftItem.getExp());
        }
        Iterator<Item> iterator2 = objects.iterator();
        while (iterator2.hasNext()) {
            Item item = (Item)iterator2.next();
            if ((int)item.getItem() > 0 && (int)item.getNum() > 0) {
                this.addGoods(role, (int)item.getItem(), (int)item.getNum(), (int)giftItem.getBind(), true, true, "开启礼包");
            }
            else {
                continue;
            }
        }
        return true;
    }
    
    public void openGiftExp(Role role, int serial) {
        long petExp = 0L;
        long heroExp = 0L;
        Pet pet = null;
        if (serial == 3003) {
            heroExp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.6E7 * (1.0 + ((double)(int)role.getLevel() + (double)(int)role.getRelive() * 15.0) / 140.0 * 2.5) / 30.0 * 3.0)).longValue();
            Integer combatPet = role.getExtendBox().getCombatPet();
            if ((int)combatPet > 0) {
                pet = (Pet)this.petService.selectPet(role).get(combatPet);
                if (pet != null) {
                    heroExp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.6E7 * (1.0 + ((double)(int)pet.getPetLv() + (double)(int)pet.getRebirth() * 15.0) / 140.0 * 2.5) / 30.0 * 3.0 * 1.2)).longValue();
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
    
    public void openGiftTips(Role role, Integer serial, Integer num, String tips) {
        Integer itemType = DataGlobalFunc.GetItemTypeByItemTypeId(serial);
        if ((int)itemType == ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue() && (int)DataGlobalFunc.getItemSubTypeByItemTypeId(serial) == 2) {
            int bookType = (int)DataGlobalFunc.getPetSkillBookTypeByItemTypeId(serial);
            PETSKILLSUB.SUBTYPE_SUPREME.getValue();
        }
    }
    
    public BagGoods consume(Map<Long, BagGoods> bagGoodsMap, BagGoods goods, int consume) {
        NutMap rests = goods.getRests();
        String key = String.valueOf(2);
        int num = rests.getInt(key, 0);
        if (num < consume) {
            rests.remove(key);
            consume -= num;
            List<Integer> list = new ArrayList<>();
            for (String s : rests.keySet()) {
                list.add(Integer.valueOf(s));
            }
            List<Integer> collect = (List)list.stream().sorted(Comparator.comparing(Integer::intValue, Comparator.reverseOrder())).collect(Collectors.toList());
            for (Integer key_ : collect) {
                int anInt = rests.getInt(String.valueOf(key_), 0);
                if (anInt > 0) {
                    if (consume > anInt) {
                        rests.remove(String.valueOf(key_));
                        consume -= anInt;
                    }
                    else {
                        int residue2 = anInt - consume;
                        if (residue2 <= 0) {
                            rests.remove(String.valueOf(key_));
                            break;
                        }
                        rests.setv(String.valueOf(key_), Integer.valueOf(residue2));
                        break;
                    }
                }
                else {
                    continue;
                }
            }
        }
        else {
            int residue = num - consume;
            if (residue <= 0) {
                rests.remove(key);
            }
            else {
                rests.setv(key, Integer.valueOf(residue));
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
    
    public void useDrug(Role role, ReqDrugUse drugUse) {
        Integer goodsNum = this.getBagGoodsNum(role, Integer.valueOf(drugUse.getSerial()));
        if ((int)goodsNum <= 0) {
            return;
        }
        SysDrug drug = (SysDrug)ItemDataPool.data_drug.get(Integer.valueOf(drugUse.getSerial()));
        if (drug != null) {
            if ((int)drug.getShowClass() == 4 || (int)drug.getUseType() == 2) {
                return;
            }
            if ((int)drug.getDrugAddHpStored() > 0) {
                this.consumeBagGoods(role, (int)drug.getSerial(), 1, true, true, "使用药品-气血储备");
                role.setSaveLife(Long.valueOf((long)role.getSaveLife() + (long)(int)drug.getDrugAddHpStored()));
                MessagePusher.notify2Player(role, 104, drug.getName(), "气血", drug.getDrugAddHpStored());
                this.roleService.refreshRoleStore(role);
                role.save();
                return;
            }
            if ((int)drug.getDrugAddMpStored() > 0) {
                this.consumeBagGoods(role, (int)drug.getSerial(), 1, true, true, "使用药品-法力储备");
                role.setSaveMana(Long.valueOf((long)role.getSaveLife() + (long)(int)drug.getDrugAddMpStored()));
                MessagePusher.notify2Player(role, 104, drug.getName(), "法力", drug.getDrugAddMpStored());
                this.roleService.refreshRoleStore(role);
                role.save();
                return;
            }
            if (role.getRoleId().equals(Integer.valueOf(drugUse.getId()))) {
                if ((int)drug.getShowClass() == 1) {
                    if ((int)role.getCurrLife() >= (int)role.getMaxLife()) {
                        MessagePusher.notify2Misc(role, false, 101, "你");
                    }
                    else {
                        int need2 = (int)role.getMaxLife() - (int)role.getCurrLife();
                        double addHp4 = (double)(int)drug.getDrugAddHpValue() + (double)(int)drug.getDrugAddHpPercent() / 100.0 * (double)(int)role.getMaxLife();
                        int num4 = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addHp4 / (double)need2));
                        if ((int)goodsNum > num4) {
                            role.setCurrLife(role.getMaxLife());
                            this.consumeBagGoods(role, (int)drug.getSerial(), num4, true, true, "使用药品-恢复气血1");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                        else {
                            role.setCurrLife(Integer.valueOf((int)(addHp4 * (double)(int)goodsNum)));
                            this.consumeBagGoods(role, (int)drug.getSerial(), (int)goodsNum, true, true, "使用药品-恢复气血2");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                    }
                }
                else if ((int)drug.getShowClass() == 2) {
                    if ((int)role.getCurrLife() >= (int)role.getMaxLife()) {
                        MessagePusher.notify2Misc(role, false, 102, "你");
                    }
                    else {
                        int need2 = (int)role.getMaxLife() - (int)role.getCurrLife();
                        double addMp4 = (double)(int)drug.getDrugAddMpValue() + (double)(int)drug.getDrugAddMpPercent() / 100.0 * (double)(int)role.getMaxMana();
                        int num4 = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addMp4 / (double)need2));
                        if ((int)goodsNum > num4) {
                            role.setCurrMana(role.getMaxMana());
                            this.consumeBagGoods(role, (int)drug.getSerial(), num4, true, true, "使用药品-恢复法力1");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                        else {
                            role.setCurrMana(Integer.valueOf((int)(addMp4 * (double)(int)goodsNum)));
                            this.consumeBagGoods(role, (int)drug.getSerial(), (int)goodsNum, true, true, "使用药品-恢复法力2");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                    }
                }
                else if ((int)drug.getShowClass() == 3) {
                    if ((int)role.getCurrLife() >= (int)role.getMaxLife() && (int)role.getCurrMana() >= (int)role.getCurrMana()) {
                        MessagePusher.notify2Misc(role, false, 103, "你");
                    }
                    else {
                        int needLife2 = (int)role.getMaxLife() - (int)role.getCurrLife();
                        int needMana2 = (int)role.getMaxLife() - (int)role.getCurrLife();
                        double addHp3 = (double)(int)drug.getDrugAddHpValue() + (double)(int)drug.getDrugAddHpPercent() / 100.0 * (double)(int)role.getMaxLife();
                        double addMp3 = (double)(int)drug.getDrugAddMpValue() + (double)(int)drug.getDrugAddMpPercent() / 100.0 * (double)(int)role.getMaxMana();
                        int num3 = 1;
                        if (needLife2 > 0) {
                            num3 = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addHp3 / (double)needLife2));
                        }
                        if (needMana2 > 0) {
                            int upInt2 = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addMp3 / (double)needLife2));
                            if (num3 < upInt2) {
                                num3 = upInt2;
                            }
                        }
                        if ((int)goodsNum > num3) {
                            role.setCurrLife(role.getMaxLife());
                            role.setCurrMana(role.getMaxMana());
                            this.consumeBagGoods(role, (int)drug.getSerial(), num3, true, true, "使用药品-双加药1");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                        else {
                            role.setCurrLife(Integer.valueOf((int)(addHp3 * (double)(int)goodsNum)));
                            role.setCurrMana(Integer.valueOf((int)(addMp3 * (double)(int)goodsNum)));
                            this.consumeBagGoods(role, (int)drug.getSerial(), (int)goodsNum, true, true, "使用药品-双加药2");
                            this.roleService.refreshRoleInfo(role, false);
                        }
                    }
                }
            }
            else {
                Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(drugUse.getId()));
                if (pet != null) {
                    PetService petService = SpringUtils.getPetService();
                    if ((int)drug.getShowClass() == 1) {
                        if ((int)role.getCurrLife() >= (int)role.getMaxLife()) {
                            MessagePusher.notify2Misc(role, false, 101, "#<LB1>" + pet.getPetName() + "#");
                        }
                        else {
                            int need = (int)pet.getMaxLife() - pet.getCurrLife();
                            double addHp2 = (double)(int)drug.getDrugAddHpValue() + (double)(int)drug.getDrugAddHpPercent() / 100.0 * (double)(int)pet.getMaxLife();
                            int num2 = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addHp2 / (double)need));
                            if ((int)goodsNum > num2) {
                                pet.setCurrLife((int)pet.getMaxLife());
                                this.consumeBagGoods(role, (int)drug.getSerial(), num2, true, true, "宠物使用药品-血药1");
                                petService.refreshPetInfo(role, pet);
                            }
                            else {
                                pet.setCurrLife((int)(addHp2 * (double)(int)goodsNum));
                                this.consumeBagGoods(role, (int)drug.getSerial(), (int)goodsNum, true, true, "宠物使用药品-血药2");
                                petService.refreshPetInfo(role, pet);
                            }
                        }
                    }
                    else if ((int)drug.getShowClass() == 2) {
                        if ((int)role.getCurrLife() >= (int)role.getMaxLife()) {
                            MessagePusher.notify2Misc(role, false, 102, "#<LB1>" + pet.getPetName() + "#");
                        }
                        else {
                            int need = (int)role.getMaxLife() - (int)role.getCurrLife();
                            double addMp2 = (double)(int)drug.getDrugAddMpValue() + (double)(int)drug.getDrugAddMpPercent() / 100.0 * (double)(int)role.getMaxMana();
                            int num2 = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addMp2 / (double)need));
                            if ((int)goodsNum > num2) {
                                pet.setCurrMana(pet.getMaxMana());
                                this.consumeBagGoods(role, (int)drug.getSerial(), num2, true, true, "宠物使用药品-蓝药1");
                                petService.refreshPetInfo(role, pet);
                            }
                            else {
                                pet.setCurrMana(Integer.valueOf((int)(addMp2 * (double)(int)goodsNum)));
                                this.consumeBagGoods(role, (int)drug.getSerial(), (int)goodsNum, true, true, "宠物使用药品-蓝药2");
                                petService.refreshPetInfo(role, pet);
                            }
                        }
                    }
                    else if ((int)drug.getShowClass() == 3) {
                        if ((int)role.getCurrLife() >= (int)role.getMaxLife() && (int)role.getCurrMana() >= (int)role.getCurrMana()) {
                            MessagePusher.notify2Misc(role, false, 103, "#<LB1>" + pet.getPetName() + "#");
                        }
                        else {
                            int needLife = (int)pet.getMaxLife() - pet.getCurrLife();
                            int needMana = (int)pet.getMaxLife() - pet.getCurrLife();
                            double addHp = (double)(int)drug.getDrugAddHpValue() + (double)(int)drug.getDrugAddHpPercent() / 100.0 * (double)(int)pet.getMaxLife();
                            double addMp = (double)(int)drug.getDrugAddMpValue() + (double)(int)drug.getDrugAddMpPercent() / 100.0 * (double)(int)pet.getMaxMana();
                            int num = 1;
                            if (needLife > 0) {
                                num = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addHp / (double)needLife));
                            }
                            if (needMana > 0) {
                                int upInt = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addMp / (double)needLife));
                                if (num < upInt) {
                                    num = upInt;
                                }
                            }
                            if ((int)goodsNum > num) {
                                pet.setCurrLife((int)pet.getMaxLife());
                                pet.setCurrMana(pet.getMaxMana());
                                this.consumeBagGoods(role, (int)drug.getSerial(), num, true, true, "宠物使用药品-双加药1");
                                petService.refreshPetInfo(role, pet);
                            }
                            else {
                                pet.setCurrLife((int)(addHp * (double)(int)goodsNum));
                                pet.setCurrMana(Integer.valueOf((int)(addMp * (double)(int)goodsNum)));
                                this.consumeBagGoods(role, (int)drug.getSerial(), (int)goodsNum, true, true, "宠物使用药品-双加药2");
                                petService.refreshPetInfo(role, pet);
                            }
                        }
                    }
                }
            }
        }
        return;
    }
    
    public void useStatus(Role role, ItemEasyInfo info, short type) {
        if (type <= 0) {
            role.getExtendBox().getState().remove(info.getDict());
        }
        else {
            Integer integer = this.consumeBagGoods(role, (int)info.getSerial(), 1, true, true, "使用-" + info.getName());
            if (integer != null) {
                return;
            }
            int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
            role.getExtendBox().getState().setv(info.getDict(), Integer.valueOf(currTime + (int)info.getValue()));
        }
        this.roleService.stateRefresh(role);
        role.save();
        SpringUtils.getBroadcastService().sendUpdateAppear(role);
    }
    
    public void useCharge(Role role, ItemEasyInfo info, short num) {
        Integer integer = this.consumeBagGoods(role, (int)info.getSerial(), (int)num, true, true, "使用-" + info.getName());
        if (integer != null) {
            return;
        }
        AdminService adminService = (AdminService)SpringUtils.getBean(AdminService.class);
        for (int i = 0; i < num; ++i) {
            adminService.recharge(role, (int)info.getValue());
        }
        return;
    }
    
    public void useIncense(Role role, int serial, int residueTimes) {
        if (serial == 90005) {
            if (residueTimes > 0) {
                Integer goods = this.consumeBagGoods(role, serial, 1, true, true, "使用-引妖香");
                if (goods != null) {
                    return;
                }
            }
            else {
                residueTimes = 0;
            }
            role.getExpireTimeMap().put("incenseExpireTime", Integer.valueOf(residueTimes));
        }
        else if (serial == 90006) {
            Integer goods = this.consumeBagGoods(role, 90006, 1, true, true, "使用-百里香");
            if (goods != null) {
                return;
            }
            residueTimes = 0;
            role.getExpireTimeMap().put("incenseExpireTime", Integer.valueOf(residueTimes));
        }
        else if (serial == 90007) {
            if (residueTimes > 0) {
                Integer goods = this.consumeBagGoods(role, serial, 1, true, true, "使用-夺命香");
                if (goods != null) {
                    return;
                }
            }
            role.getPkInfoBox().setIncense(residueTimes);
        }
        this.roleService.stateRefresh(role);
        role.save();
        MessagePusher.pushMessage(role, new RespUseIncense(Integer.valueOf(serial), Integer.valueOf(residueTimes)));
    }
    
    public void itemOperate(Role role, ReqOperateItem operateItem) {
        int type = operateItem.getType();
        boolean clicker = this.roleService.mouserClicker(role, "up_it_" + type, 250L);
        if (!clicker) {
            return;
        }
        BagHandler handler = BagHelper.INSTANCE.getHandler(Integer.valueOf(type));
        if (handler != null) {
            String message = handler.acceptOperate(role, Integer.valueOf(operateItem.getPos()), Integer.valueOf(operateItem.getNum()), operateItem.getParam());
            if (!Strings.isEmpty(message) && message != null) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), message, false));
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
        }
        return;
    }
    
    public boolean send(Role role, int serial, int type, int num, int pos, String cause, int bd) {
        if (type == 0) {
            ((EquipService)SpringUtils.getBean(EquipService.class)).upgradeEquip(role, 0, 11, String.valueOf(serial), 0, false, cause);
            if (pos >= 0) {
                this.deleteRoleItem(role, pos, null);
            }
            return true;
        }
        else if (type == 1 || type == 2) {
            if (serial == ItemClassify.RESTYPE_SILVER.getId()) {
                this.roleService.addCurrency(role, Const.RESTYPE_SILVER, Long.valueOf((long)num), cause, true, false);
            }
            else if (serial == ItemClassify.RESTYPE_COIN.getId()) {
                this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf((long)num), cause, true, false);
            }
            else if (serial == ItemClassify.RES_TYPE_ACHIEVEMENT.getId()) {
                this.roleService.addCurrency(role, Const.RES_TYPE_ACHIEVEMENT, Long.valueOf((long)num), cause, true, false);
            }
            else {
                this.addGoods(role, serial, num, bd, true, true, cause);
            }
            if (pos >= 0) {
                this.deleteRoleItem(role, pos, cause);
            }
            return true;
        }
        else {
            return false;
        }
    }
    
    public void exchange(Role role, ReqExchange reqExchange) {
        boolean clicker = this.roleService.mouserClicker(role, "exchange", 250L);
        if (!clicker) {
            return;
        }
        SysDataExchange exchange = (SysDataExchange)WelfareDataPool.exchangeMap.get(Integer.valueOf(reqExchange.getSerial()));
        if (exchange == null) {
            return;
        }
        boolean flag = false;
        try {
            flag = this.checkPos(role, exchange.getSerial(), exchange.getNum() * reqExchange.getNum());
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("兑换物品检查背包空位出错 物品编号 = {}，物品数量 = {}", Integer.valueOf(reqExchange.getSerial()), Short.valueOf(reqExchange.getNum()));
            return;
        }
        if (!flag) {
            return;
        }
        int needNum = reqExchange.getNum() * exchange.getNeedNum();
        Integer integer = this.consumeBagGoods(role, exchange.getNeedSerial(), needNum, false, true, "兑换物品");
        if (integer != null) {
            ItemEasyInfo info = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(exchange.getNeedSerial()));
            if (info != null) {
                MessagePusher.pushMessage(role, new RespMsg(info.getName() + "缺少：#<R>" + integer + "#"));
            }
            return;
        }
        else {
            if (exchange.getType() == 1) {
                this.addGoods(role, exchange.getSerial(), exchange.getNum() * reqExchange.getNum(), 0, true, true, "兑换物品");
            }
            return;
        }
    }
    
    public boolean shelvesConsume(Role role, int pos, int consume) {
        int time = Math.toIntExact(System.currentTimeMillis() / 1000L);
        Map<Long, BagGoods> bagGoodsMap = this.getBagGoods(role);
        BagGoods goods = (BagGoods)bagGoodsMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
        if (goods == null) {
            return false;
        }
        NutMap rests = goods.getRests();
        int num = DataGlobalFunc.unrestricted(goods.getRests());
        if (consume > num) {
            return false;
        }
        List<Integer> list = new ArrayList<>();
        for (String s : rests.keySet()) {
            int key = (int)Integer.valueOf(s);
            if (key != 2 && key < time) {
                list.add(Integer.valueOf(key));
            }
            else {
                continue;
            }
        }
        list.add(Integer.valueOf(0));
        for (Integer key2 : list) {
            int anInt = rests.getInt(String.valueOf(key2), 0);
            if (anInt > 0) {
                if (consume > anInt) {
                    rests.remove(String.valueOf(key2));
                    consume -= anInt;
                }
                else {
                    int residue = anInt - consume;
                    if (residue <= 0) {
                        rests.remove(String.valueOf(key2));
                        break;
                    }
                    rests.setv(String.valueOf(key2), Integer.valueOf(residue));
                    break;
                }
            }
            else {
                continue;
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
    
    public void send(Role role, int serial, byte type, int num, int pos, byte isNotice, String tips, int bd) {
        if (type == 0) {
            this.send(role, serial, (int)type, num, pos, "挖宝", bd);
        }
        else if (type == 1 || type == 2) {
            boolean flag = this.send(role, serial, (int)type, num, pos, "挖宝", bd);
            if (flag && isNotice == 1) {
                ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(serial));
                if (easyInfo != null && tips != null) {
                    String msg = String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", Integer.valueOf(serial), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(serial), Integer.valueOf(serial), easyInfo.getName());
                    this.broadcastThread.add2Queue(String.format(tips, role.getName(), msg, Integer.valueOf(num)));
                }
            }
        }
        else if (type == 3) {
            this.cardService.addCard(role, Integer.valueOf(serial), Integer.valueOf(num), Integer.valueOf(0), true, true);
            this.deleteRoleItem(role, pos, "挖宝");
        }
        else if (type == 100) {
            ((SealDemonHandler)SpringUtils.getBean(SealDemonHandler.class)).flushConditionByLevel(role, num);
            this.deleteRoleItem(role, pos, "挖宝封妖");
        }
        else if (type == 101) {
            ((ThreeScopeHandler)SpringUtils.getBean(ThreeScopeHandler.class)).flushConditionByLevel(role, num);
            this.deleteRoleItem(role, pos, "挖宝妖王");
        }
    }
    
    public List<BagGoods> getBagItem(Role role, int serial) {
        Map<Long, BagGoods> goodsMap = this.getBagGoods(role);
        return (List)goodsMap.values().stream().filter(e -> e.getSerial() == serial).collect(Collectors.toList());
    }
    
    public void sendReward(Role role, List<Param> rewards, String cause, int grade) {
        for (Param reward : rewards) {
            if (reward.getK().equals(Integer.valueOf(ItemClassify.RESTS.getId())) && reward.getV().equals(Integer.valueOf(0)) && reward.getT().equals(Integer.valueOf(0))) {
                List<Item> list = this.taskRewardDynamic(reward.getD());
                for (Item item : list) {
                    this.rewardItem(role, (int)item.getItem(), (int)item.getNum(), 0, cause);
                }
            }
            else if (reward.getK().equals(Integer.valueOf(ItemClassify.HERO_EXP.getId())) && reward.getT().equals(Integer.valueOf(0))) {
                boolean flag = true;
                if ((int)reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro((double)(int)reward.getR() / 1000.0);
                }
                if (flag && (int)reward.getT() == 0) {
                    this.roleService.addExp(role, (long)reward.getV(), "", true);
                }
                else {
                    continue;
                }
            }
            else if (reward.getK().equals(Integer.valueOf(ItemClassify.PET_EXP.getId())) && reward.getT().equals(Integer.valueOf(0))) {
                boolean flag = true;
                if ((int)reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro((double)(int)reward.getR() / 1000.0);
                }
                if (flag && (int)reward.getT() == 0) {
                    Integer combatPet = role.getExtendBox().getCombatPet();
                    if ((int)combatPet > 0) {
                        Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
                        this.petService.addExp(role, pet, (long)reward.getV(), true);
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            else if (reward.getK().equals(Integer.valueOf(ItemClassify.RESTYPE_COIN.getId())) && reward.getT().equals(Integer.valueOf(0))) {
                boolean flag = true;
                if ((int)reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro((double)(int)reward.getR() / 1000.0);
                }
                if (flag && (int)reward.getT() == 0) {
                    this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf((long)reward.getV()), "奖励派发1 - " + cause, true, false);
                }
                else {
                    continue;
                }
            }
            else if (reward.getK().equals(Integer.valueOf(ItemClassify.RESTYPE_SILVER.getId())) && reward.getT().equals(Integer.valueOf(0))) {
                boolean flag = true;
                if ((int)reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro((double)(int)reward.getR() / 1000.0);
                }
                if (flag && (int)reward.getT() == 0) {
                    this.roleService.addCurrency(role, Const.RESTYPE_SILVER, Long.valueOf((long)reward.getV()), "奖励派发2 - " + cause, true, false);
                }
                else {
                    continue;
                }
            }
            else if (reward.getK().equals(Integer.valueOf(ItemClassify.RES_TYPE_BRAC.getId())) && reward.getT().equals(Integer.valueOf(0))) {
                boolean flag = true;
                if ((int)reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro((double)(int)reward.getR() / 1000.0);
                }
                if (flag && (int)reward.getT() == 0) {
                    this.roleService.addCurrency(role, Const.RESTYPE_BRAC, Long.valueOf((long)reward.getV()), "降魔积分 - " + cause, true, false);
                }
                else {
                    continue;
                }
            }
            else if (reward.getK().equals(Integer.valueOf(ItemClassify.RES_TYPE_BOUND_GOLD.getId())) && reward.getT().equals(Integer.valueOf(0))) {
                boolean flag = true;
                if ((int)reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro((double)(int)reward.getR() / 1000.0);
                }
                if (flag && (int)reward.getT() == 0) {
                    this.roleService.addCurrency(role, Const.RES_TYPE_BOUND_GOLD, Long.valueOf((long)reward.getV()), "奖励派发 3 " + cause, true, false);
                }
                else {
                    continue;
                }
            }
            else if (reward.getK().equals(Integer.valueOf(ItemClassify.RES_TYPE_GOLD.getId())) && reward.getT().equals(Integer.valueOf(0))) {
                boolean flag = true;
                if ((int)reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro((double)(int)reward.getR() / 1000.0);
                }
                if (flag && (int)reward.getT() == 0) {
                    this.roleService.addCurrency(role, Const.RESTYPE_GOLD, Long.valueOf((long)reward.getV()), "奖励派发 4 - " + cause, true, false);
                }
                else {
                    continue;
                }
            }
            else if (reward.getK().equals(Integer.valueOf(ItemClassify.RES_TYPE_CHARGE_SCORE.getId())) && reward.getT().equals(Integer.valueOf(0))) {
                boolean flag = true;
                if ((int)reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro((double)(int)reward.getR() / 1000.0);
                }
                if (flag && (int)reward.getT() == 0) {
                    this.roleService.addCurrency(role, Const.RES_TYPE_CHARGE_SCORE, Long.valueOf((long)reward.getV()), "奖励派发充值积分 - " + cause, true, false);
                }
                else {
                    continue;
                }
            }
            else if (reward.getK().equals(Integer.valueOf(ItemClassify.RESTYPE_TRIBUTE.getId())) && reward.getT().equals(Integer.valueOf(0))) {
                boolean flag = true;
                if ((int)reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro((double)(int)reward.getR() / 1000.0);
                }
                if (flag && (int)reward.getT() == 0) {
                    this.roleService.addCurrency(role, Const.RESTYPE_TRIBUTE, Long.valueOf((long)reward.getV()), "奖励派发5 - " + cause, true, false);
                }
                else {
                    continue;
                }
            }
            else if (reward.getK().equals(Integer.valueOf(ItemClassify.RES_TYPE_ACHIEVEMENT.getId())) && reward.getT().equals(Integer.valueOf(0))) {
                boolean flag = true;
                if ((int)reward.getR() > 0) {
                    flag = SkillAi.canAffectOnPro((double)(int)reward.getR() / 1000.0);
                }
                if (flag && (int)reward.getT() == 0) {
                    this.roleService.addCurrency(role, Const.RES_TYPE_ACHIEVEMENT, Long.valueOf((long)reward.getV()), "奖励派发5 - " + cause, true, false);
                }
                else {
                    continue;
                }
            }
            else if (reward.getT().byteValue() == 2 && DataGlobalFunc.GetItemTypeByItemTypeId(reward.getK()).equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue()))) {
                SysPet sysPet = (SysPet)PetDataPool.petMap.get(reward.getK());
                if (sysPet != null) {
                    boolean flag2 = this.petService.canGetPet(role, sysPet, false);
                    if (flag2) {
                        Pet pet = this.petService.addPet(role, sysPet, reward.getBd(), cause);
                        this.petService.loadPet(role, pet);
                        this.petService.petInfo(role, pet);
                        if ((int)role.getExtendBox().getCombatPet() <= 0) {
                            role.getExtendBox().setCombatPet(pet.getPetId());
                            role.save();
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        this.rewardItem(role, (int)reward.getK(), (int)reward.getV(), reward.getBd(), cause);
                    }
                }
                else {
                    continue;
                }
            }
            else if ((int)reward.getT() > 0) {
                if ((int)reward.getR() <= 0) {
                    this.rewardItem(role, (int)reward.getK(), (int)reward.getV(), reward.getBd(), cause);
                }
                else if ((int)reward.getR() > 0) {
                    boolean flag = SkillAi.canAffectOnPro((double)(int)reward.getR() / 1000.0);
                    if (flag) {
                        this.rewardItem(role, (int)reward.getK(), (int)reward.getV(), reward.getBd(), cause);
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
    }
    
    public List<Item> taskRewardDynamic(String dist) {
        List<Item> list = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap();
        List<SysDataTaskRewardDynamic> collect = (List)TaskDataPool.taskRewardDynamicList.stream().filter(e -> e.getDict().equals(dist)).collect(Collectors.toList());
        for (SysDataTaskRewardDynamic dynamic : collect) {
            double rate = (double)TaskDataPool.taskRewardDynamicRateList.get(Integer.valueOf(dynamic.getIdentify()));
            boolean flag = SkillAi.canAffectOnPro(rate);
            if (flag) {
                Integer num = (Integer)map.get(Integer.valueOf(dynamic.getSerial()));
                if (num == null) {
                    num = Integer.valueOf(0);
                }
                int numCalculate = (int)TaskDataPool.taskRewardDynamicNumList.get(Integer.valueOf(dynamic.getIdentify()));
                num = Integer.valueOf((int)num + numCalculate);
                if ((int)num > 0) {
                    map.put(Integer.valueOf(dynamic.getSerial()), num);
                    list.add(new Item(Integer.valueOf(dynamic.getSerial()), num, Integer.valueOf(dynamic.getNotice())));
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
        return list;
    }
    
    public void rewardItem(Role role, int serial, int num, int bd, String cause) {
        try {
            Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(serial));
            if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue())) || type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue())) || type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue())) || type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()))) {
                this.creationEquip(role, serial, "任务奖励");
            }
            else if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue()))) {
                SysPet sysPet = (SysPet)PetDataPool.petMap.get(Integer.valueOf(serial));
                if (sysPet != null) {
                    this.addGoods(role, serial, num, bd, true, false, "任务奖励物品1：" + cause);
                }
            }
            else {
                ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(serial));
                if (itemEasyInfo != null) {
                    if (itemEasyInfo.getType().equals(Integer.valueOf(1))) {
                        if ((int)itemEasyInfo.getValue() > 1000) {
                            this.creationEquip(role, (int)itemEasyInfo.getValue(), cause);
                        }
                        else if ((int)itemEasyInfo.getValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
                            List<SysSinBieEq> bieEqs = (List)EquipDataPool.sinBieEq.values().stream().collect(Collectors.toList());
                            List<SysSinBieEq> collect = (List)bieEqs.stream().filter(eq -> eq.getPos() == (int)itemEasyInfo.getCostType() && (eq.getOwnerRole().contains(Integer.valueOf(0)) || eq.getOwnerRole().contains(role.getSerial()) || eq.getOwnerRole().contains(role.getRace())) && (eq.getSex() == 0 || (int)role.getSex() == eq.getSex()) && (eq.getRace() == 0 || (int)role.getRace() == eq.getRace()) && DataGlobalFunc.getPj(eq.getSerial()) == (int)itemEasyInfo.getItemPJ()).collect(Collectors.toList());
                            int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
                            this.creationEquip(role, ((SysSinBieEq)collect.get(index)).getSerial(), cause);
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
        catch (Exception e) {
            e.printStackTrace();
            LoggerUtils.error("任务奖励物品 = {}", e);
        }
    }
    
    public void creationEquip(Role role, int serial, String cause) {
        EquipAlike equipData = DataGlobalFunc.getEquipData(Integer.valueOf(serial));
        if (equipData != null) {
            ((EquipService)SpringUtils.getBean(EquipService.class)).upgradeEquip(role, 0, 11, String.valueOf(equipData.getSerial()), 0, false, cause);
        }
    }
}
