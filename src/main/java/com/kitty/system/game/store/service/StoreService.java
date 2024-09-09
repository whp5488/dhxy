package com.kitty.system.game.store.service;

import com.kitty.system.game.role.domain.Goods;
import java.util.ConcurrentModificationException;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.equip.domain.EquipEasy;
import com.kitty.system.game.bag.message.RespFlyInfo;
import com.kitty.system.game.player.bean.PlayerExtendBox;
import java.util.Collections;
import com.kitty.system.game.vo.utils.Const;
import org.nutz.dao.Cnd;
import java.util.ArrayList;
import org.nutz.lang.util.NutMap;
import com.alibaba.fastjson.JSONObject;
import com.kitty.record.RecordThread;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.store.domain.StoreGoods;
import com.kitty.system.game.bag.domain.BagGoods;
import java.util.Iterator;
import com.kitty.system.game.tianfu.message.RespTianFuDebris;
import com.kitty.system.game.tianfu.message.RespTianFuDelete;
import com.kitty.system.game.tianfu.message.RespTianFuList;
import java.util.Arrays;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import com.kitty.system.game.bag.message.RespDeleteItem;
import java.util.stream.Collectors;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.mina.cache.DataCache;
import java.util.List;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.mina.cache.BagCache;
import java.util.Map;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.store.domain.StoreEquip;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class StoreService
{
    private final Logger logger;
    @Autowired
    private Dao dao;
    @Autowired
    private IdWorker worker;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BagService bagService;
    
    public StoreService() {
        this.logger = LoggerFactory.getLogger(StoreService.class);
    }
    
    public void excuteStorePara(Role role, Byte type, Byte storeType, String posOrId) {
        boolean clicker = this.roleService.mouserClicker(role, "store", 250L);
        if (!clicker) {
            return;
        }
        switch ((byte)storeType) {
            case 0: {
                if ((byte)type == 1) {
                    int pos = Integer.parseInt(posOrId);
                    int posStor = this.getPos(role);
                    if (posStor <= -1) {
                        MessagePusher.pushMessage(role, new RespNotifyMiscEx("仓库物品栏空间不足"));
                        return;
                    }
                    Map<Long, BagEquip> bagEquip = this.bagService.getBagEquip(role);
                    BagEquip equip = (BagEquip)bagEquip.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
                    if (equip != null) {
                        StoreEquip equipStore = new StoreEquip();
                        BeanUtils.copyBeanProp(equipStore, equip);
                        equip.setDelete(true);
                        ((Map)BagCache.BAG_EQUIP_MAP.get(role.getId())).remove(equip.getId());
                        equip.save();
                        equipStore.setPos(posStor);
                        ((Map)BagCache.STORE_EQUIP_MAP.get(role.getId())).put(equipStore.getId(), equipStore);
                        equipStore.save();
                        this.bagService.deleteInform(role, (byte)0, equip.getPos());
                        this.refreshStoreEquip(role, equipStore);
                    }
                    break;
                }
                else if ((byte)type == 0) {
                    int pos = Integer.parseInt(posOrId);
                    int posBag = this.bagService.getPos(role, false);
                    if (posBag <= -1) {
                        MessagePusher.pushMessage(role, new RespNotifyMiscEx("背包物品栏空间不足"));
                        return;
                    }
                    Map<Long, StoreEquip> storeEquipMap = this.getStoreEquip(role);
                    StoreEquip storeEquip = (StoreEquip)storeEquipMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
                    if (storeEquip != null) {
                        BagEquip equipBag = new BagEquip();
                        BeanUtils.copyBeanProp(equipBag, storeEquip);
                        storeEquip.setDelete(true);
                        ((Map)BagCache.STORE_EQUIP_MAP.get(role.getId())).remove(storeEquip.getId());
                        storeEquip.save();
                        equipBag.setPos(posBag);
                        ((Map)BagCache.BAG_EQUIP_MAP.get(role.getId())).put(equipBag.getId(), equipBag);
                        equipBag.save();
                        this.bagService.refreshRoleEquip(role, equipBag, false);
                        this.bagService.deleteInform(role, (byte)1, storeEquip.getPos());
                    }
                    break;
                }
                else {
                    break;
                }
            }
            case 1: {
                if ((byte)type == 1) {
                    this.excuteGoods(role, Integer.parseInt(posOrId));
                    break;
                }
                if ((byte)type == 0) {
                    this.excuteGoodsStore(role, Integer.parseInt(posOrId));
                    break;
                }
                break;
            }
            case 4: {
                if ((byte)type == 1) {
                    long tianFuId = Long.parseLong(posOrId);
                    int posStor2 = this.getPos(role);
                    List<RoleTianFu> list2 = DataCache.ROLE_TIAN_FU.get(role.getId());
                    RoleTianFu roleTianFu2 = (RoleTianFu)list2.stream().filter(obj -> obj.getId().equals(Long.valueOf(tianFuId)) && obj.getIsWear() == 0 && obj.getStorePos() == 0).findFirst().orElse(null);
                    if (roleTianFu2 != null) {
                        if (roleTianFu2.getTianFuSerial() == 9997) {
                            this.debrisToStore(role, roleTianFu2, (int)roleTianFu2.getCount(), posStor2);
                        }
                        else {
                            if (posStor2 <= -1) {
                                MessagePusher.pushMessage(role, new RespNotifyMiscEx("仓库物品栏空间不足"));
                                return;
                            }
                            roleTianFu2.setStorePos(posStor2);
                            roleTianFu2.save();
                            this.refreshStoreTianFu(role, roleTianFu2);
                            MessagePusher.pushMessage(role, new RespTianFuDelete(Arrays.asList(roleTianFu2.getId().toString())));
                        }
                    }
                    break;
                }
                else if ((byte)type == 0) {
                    long tianFuId = Long.parseLong(posOrId);
                    List<RoleTianFu> list = DataCache.ROLE_TIAN_FU.get(role.getId());
                    RoleTianFu roleTianFu = (RoleTianFu)list.stream().filter(obj -> (long)obj.getId() == tianFuId && obj.getStorePos() > 0).findFirst().orElse(null);
                    if (roleTianFu != null) {
                        if (roleTianFu.getTianFuSerial() == 9997) {
                            int count = roleTianFu.getCount();
                            if (count > 9999) {
                                MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符数量已达上限"));
                                return;
                            }
                            this.debrisGet(role, roleTianFu, count);
                        }
                        else {
                            List<RoleTianFu> roleTianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
                            if (roleTianFus.size() >= 75) {
                                MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符数量已达上限"));
                                return;
                            }
                            MessagePusher.pushMessage(role, new RespDeleteItem((byte)1, roleTianFu.getStorePos()));
                            roleTianFu.setStorePos(0);
                            roleTianFu.save();
                            MessagePusher.pushMessage(role, new RespTianFuList((byte)1, Arrays.asList(new RoleTianFuClient(roleTianFu))));
                        }
                    }
                    break;
                }
                else {
                    break;
                }
            }
            default: {
                this.logger.error("仓库存储无该类型【{}】操作", storeType);
                break;
            }
        }
        return;
    }
    
    private void debrisToStore(Role role, RoleTianFu roleTianFu, int count, int posStor) {
        List<RoleTianFu> debrisList = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getTianFuSerial() == 9997 && obj.getStorePos() != 0).collect(Collectors.toList());
        int countOld = count;
        for (RoleTianFu debris : debrisList) {
            if (debris.getCount() < 9999) {
                int tempNum = 9999 - debris.getCount();
                if (tempNum >= count) {
                    tempNum = count;
                }
                debris.setCount((short)(debris.getCount() + tempNum));
                debris.save();
                this.refreshStoreTianFu(role, debris);
                count -= tempNum;
                if (count == 0) {
                    break;
                }
                continue;
            }
            else {
                continue;
            }
        }
        if (count > 0) {
            if (posStor <= -1) {
                roleTianFu.setCount((short)(roleTianFu.getCount() - (countOld - count)));
                roleTianFu.save();
                MessagePusher.pushMessage(role, new RespTianFuDebris(roleTianFu));
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("仓库物品栏空间不足"));
                return;
            }
            MessagePusher.pushMessage(role, new RespTianFuDelete(Arrays.asList(roleTianFu.getId().toString())));
            roleTianFu.setStorePos(posStor);
            roleTianFu.setCount((short)count);
            roleTianFu.save();
            this.refreshStoreTianFu(role, roleTianFu);
        }
        else {
            roleTianFu.setCount((short)(roleTianFu.getCount() - countOld));
            if (roleTianFu.getCount() <= 0) {
                roleTianFu.setDelete(true);
                MessagePusher.pushMessage(role, new RespTianFuDelete(Arrays.asList(roleTianFu.getId().toString())));
                (DataCache.ROLE_TIAN_FU.get(role.getId())).remove(roleTianFu);
            }
            roleTianFu.save();
        }
    }
    
    private void debrisGet(Role role, RoleTianFu storeTianFu, int count) {
        List<RoleTianFu> list = DataCache.ROLE_TIAN_FU.get(role.getId());
        RoleTianFu debris = (RoleTianFu)list.stream().filter(obj -> obj.getTianFuSerial() == 9997 && obj.getStorePos() == 0).findFirst().orElse(null);
        if (debris != null) {
            if (debris.getCount() + count > 9999) {
                if (debris.getCount() >= 9999) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("背包中天策符碎片数量已达上限"));
                    return;
                }
                count = 9999 - debris.getCount();
            }
            debris.setCount((short)(debris.getCount() + count));
            debris.save();
            MessagePusher.pushMessage(role, new RespTianFuDebris(debris));
            this.debrisToBag(role, storeTianFu, count);
        }
        else {
            List<RoleTianFu> roleTianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
            if (roleTianFus.size() >= 75) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符数量已达上限"));
                return;
            }
            if (storeTianFu.getCount() == count) {
                MessagePusher.pushMessage(role, new RespDeleteItem((byte)1, storeTianFu.getStorePos()));
                storeTianFu.setStorePos(0);
                storeTianFu.save();
                MessagePusher.pushMessage(role, new RespTianFuDebris(storeTianFu));
                return;
            }
            RoleTianFu roleTianFu = new RoleTianFu();
            BeanUtils.copyBeanProp(roleTianFu, storeTianFu);
            roleTianFu.setId(Long.valueOf(this.worker.nextId()));
            roleTianFu.setCount((short)count);
            roleTianFu.setStorePos(0);
            roleTianFu.save();
            MessagePusher.pushMessage(role, new RespTianFuDebris(roleTianFu));
            this.debrisToBag(role, roleTianFu, count);
        }
    }
    
    private void debrisToBag(Role role, RoleTianFu storeTianFu, int count) {
        if (storeTianFu.getCount() <= count) {
            count -= storeTianFu.getCount();
            storeTianFu.setCount((short)0);
            storeTianFu.setDelete(true);
            (DataCache.ROLE_TIAN_FU.get(role.getId())).remove(storeTianFu);
            MessagePusher.pushMessage(role, new RespDeleteItem((byte)1, storeTianFu.getStorePos()));
        }
        else {
            storeTianFu.setCount((short)(storeTianFu.getCount() - count));
            count = 0;
            this.refreshStoreTianFu(role, storeTianFu);
        }
        storeTianFu.save();
        if (count > 0) {
            List<RoleTianFu> storeDebrisList = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getTianFuSerial() == 9997 && obj.getStorePos() != 0 && !obj.getId().equals(storeTianFu.getId())).collect(Collectors.toList());
            for (RoleTianFu debrisTianFu : storeDebrisList) {
                int tempNum;
                if (count - debrisTianFu.getCount() < 0) {
                    tempNum = count;
                }
                else {
                    tempNum = debrisTianFu.getCount();
                }
                debrisTianFu.setCount((short)(debrisTianFu.getCount() - tempNum));
                if (debrisTianFu.getCount() <= 0) {
                    debrisTianFu.setDelete(true);
                    (DataCache.ROLE_TIAN_FU.get(role.getId())).remove(debrisTianFu);
                    MessagePusher.pushMessage(role, new RespDeleteItem((byte)1, debrisTianFu.getStorePos()));
                }
                else {
                    this.refreshStoreTianFu(role, debrisTianFu);
                }
                debrisTianFu.save();
                count -= tempNum;
                if (count <= 0) {
                    break;
                }
                continue;
            }
        }
    }
    
    private void excuteGoods(Role role, int pos) {
        Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
        BagGoods goods = (BagGoods)bagGoodsMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
        if (goods != null) {
            int posStor = this.getPos(role);
            if (posStor <= -1) {
                Map<Long, StoreGoods> storeGoodsMap = this.getStoreGoods(role);
                StoreGoods storeGoods = (StoreGoods)storeGoodsMap.values().stream().filter(obj -> obj.getSerial() == goods.getSerial()).findFirst().orElse(null);
                Map<Object, Object> map = DataGlobalFunc.GetItemDataByItemTypeId(Integer.valueOf(goods.getSerial()));
                JSONObject json = JsonUtils.objectToJson(map.get(Integer.valueOf(goods.getSerial())));
                int canMerge = (int)json.getInteger("canMerge");
                if (storeGoods == null || storeGoods.getNum() + goods.getNum() > canMerge) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("仓库物品栏空间不足"));
                    return;
                }
            }
            int count = goods.getNum();
            StoreGoods goodsStore = new StoreGoods();
            BeanUtils.copyBeanProp(goodsStore, goods);
            this.bagService.removeItem(role, pos, 1, "存入仓库，数量：" + count);
            this.saveStoreGoods(role, goodsStore, goodsStore.getNum());
            this.bagService.refreshRoleGoods(role, goods, false);
            RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)1, (byte)1, String.format("道具（%d）存入仓库，仓库数量增加：%d", Integer.valueOf(goods.getSerial()), Integer.valueOf(count)), role.getMarking());
            RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)1, (byte)2, String.format("道具（%d）存入仓库，背包数量减少：%d", Integer.valueOf(goods.getSerial()), Integer.valueOf(count)), role.getMarking());
        }
    }
    
    private void excuteGoodsStore(Role role, int pos) {
        Map<Long, StoreGoods> storeGoodsMap = this.getStoreGoods(role);
        StoreGoods goods = (StoreGoods)storeGoodsMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
        if (goods != null) {
            int count = goods.getNum();
            boolean flag = this.bagService.checkPos(role, goods.getSerial(), count);
            if (!flag) {
                return;
            }
            NutMap rests = goods.getRests();
            if (rests.size() > 0) {
                for (Map.Entry<String, Object> entry : rests.entrySet()) {
                    this.bagService.addGoods(role, goods.getSerial(), (int)entry.getValue(), Integer.parseInt((String)entry.getKey()), false, false, "仓库取出");
                }
            }
            goods.setRests(NutMap.NEW());
            goods.setDelete(true);
            ((Map)BagCache.STORE_GOODS_MAP.get(role.getId())).remove(goods.getId());
            goods.save();
            this.refreshStoreGoods(role, goods);
            RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)1, (byte)2, String.format("道具（%d）从仓库取出，仓库数量减少：%d", Integer.valueOf(goods.getSerial()), Integer.valueOf(count)), role.getMarking());
            RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)1, (byte)1, String.format("道具（%d）从仓库取出，背包数量增加：%d", Integer.valueOf(goods.getSerial()), Integer.valueOf(count)), role.getMarking());
        }
    }
    
    private void saveStoreGoods(Role role, StoreGoods storeGood, int num) {
        List<StoreGoods> storeGoods = (List)this.getStoreGoods(role).values().stream().collect(Collectors.toList());
        Map<Object, Object> map = DataGlobalFunc.GetItemDataByItemTypeId(Integer.valueOf(storeGood.getSerial()));
        if (map == null) {
            return;
        }
        JSONObject json = JsonUtils.objectToJson(map.get(Integer.valueOf(storeGood.getSerial())));
        int canMerge = (int)json.getInteger("canMerge");
        List<StoreGoods> collect = (List)storeGoods.stream().filter(item -> item.getSerial() == storeGood.getSerial()).collect(Collectors.toList());
        if (collect.size() > 0) {
            for (StoreGoods goods : collect) {
                int residue = canMerge - goods.getNum();
                if (residue <= 0) {
                    continue;
                }
                if (residue >= num) {
                    this.saveGoods(storeGood.getRests(), Integer.valueOf(num), goods.getRests());
                    goods.save();
                    this.refreshStoreGoods(role, goods);
                    num = 0;
                    break;
                }
                num = Math.abs(residue - num);
                this.saveGoods(storeGood.getRests(), Integer.valueOf(residue), goods.getRests());
                goods.save();
                this.refreshStoreGoods(role, goods);
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
            storeGood.setPos(this.getPos(role));
            storeGood.setId(this.worker.nextId());
            storeGood.save();
            ((Map)BagCache.STORE_GOODS_MAP.get(role.getId())).put(storeGood.getId(), storeGood);
            this.refreshStoreGoods(role, storeGood);
        }
        return;
    }
    
    public boolean saveGoods(NutMap restsSrc, Integer num, NutMap rests) {
        List<String> removeKeys = new ArrayList<>();
        for (String key : restsSrc.keySet()) {
            if ((int)num <= 0) {
                return true;
            }
            boolean flag = true;
            for (String key2 : rests.keySet()) {
                if (key.equals(key2)) {
                    num = Integer.valueOf(this.excuteGoodNum(restsSrc, rests, key, (int)num, true));
                    flag = false;
                    break;
                }
                continue;
            }
            if (flag) {
                num = Integer.valueOf(this.excuteGoodNum(restsSrc, rests, key, (int)num, false));
            }
            if (restsSrc.getInt(key) <= 0) {
                removeKeys.add(key);
            }
            else {
                continue;
            }
        }
        if (removeKeys.size() > 0) {
            for (String key : removeKeys) {
                restsSrc.remove(key);
            }
        }
        return true;
    }
    
    private int excuteGoodNum(NutMap restsSrc, NutMap rests, String key, int num, boolean flag) {
        int value;
        if (num < restsSrc.getInt(key)) {
            value = num;
        }
        else {
            value = restsSrc.getInt(key);
        }
        if (flag) {
            rests.setv(key, Integer.valueOf(rests.getInt(key) + value));
        }
        else {
            rests.setv(key, Integer.valueOf(value));
        }
        restsSrc.setv(key, Integer.valueOf(restsSrc.getInt(key) - value));
        num -= value;
        return num;
    }
    
    public int getPos(Role role) {
        return (int)this.getMinAndMaxPos(role);
    }
    
    private Integer getMinAndMaxPos(Role role) {
        List<Integer> list = new ArrayList<>();
        Map<Long, StoreEquip> equips = this.getStoreEquip(role);
        for (StoreEquip equip : equips.values()) {
            list.add(Integer.valueOf(equip.getPos()));
        }
        Map<Long, StoreGoods> goods = this.getStoreGoods(role);
        for (StoreGoods good : goods.values()) {
            list.add(Integer.valueOf(good.getPos()));
        }
        List<RoleTianFu> roleTianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getStorePos() > 0).collect(Collectors.toList());
        for (RoleTianFu roleTianFu : roleTianFus) {
            list.add(Integer.valueOf(roleTianFu.getStorePos()));
        }
        return this.getVacancySize(role, list);
    }
    
    public Map<Long, StoreEquip> getStoreEquip(Role role) {
        Map<Long, StoreEquip> storeGoodsMap = (Map)BagCache.STORE_EQUIP_MAP.get(role.getId());
        if (storeGoodsMap == null) {
            List<StoreEquip> list = this.dao.query(StoreEquip.class, Cnd.where("r_id", "=", role.getId()));
            list.forEach(equip -> equip.doAfterInit());
            Map<Long, StoreEquip> map = (Map)list.stream().collect(Collectors.toMap(StoreEquip::getId, StoreEquip -> StoreEquip));
            BagCache.STORE_EQUIP_MAP.put(role.getId(), map);
            return map;
        }
        return storeGoodsMap;
    }
    
    public Map<Long, StoreGoods> getStoreGoods(Role role) {
        Map<Long, StoreGoods> storeGoodsMap = (Map)BagCache.STORE_GOODS_MAP.get(role.getId());
        if (storeGoodsMap == null) {
            List<StoreGoods> list = this.dao.query(StoreGoods.class, Cnd.where("r_id", "=", role.getId()));
            Map<Long, StoreGoods> map = (Map)list.stream().collect(Collectors.toMap(StoreGoods::getId, StoreGoods -> StoreGoods));
            BagCache.STORE_GOODS_MAP.put(role.getId(), map);
            return map;
        }
        return storeGoodsMap;
    }
    
    public Integer getVacancySize(Role role, List<Integer> list) {
        PlayerExtendBox extendBox = role.getExtendBox();
        List<Integer> objects = new ArrayList<>();
        Integer pos = Integer.valueOf(0);
        pos = Integer.valueOf((int)extendBox.getStorePosition() * (int)Const.BAG_POS + (int)Const.STORE_POS_MIN);
        if (list.size() < (int)pos) {
            for (int i = 1; i <= (int)pos; ++i) {
                objects.add(Integer.valueOf(i));
            }
            List<Integer> vacancy = new ArrayList<>();
            for (Integer v : objects) {
                if (!list.contains(v)) {
                    vacancy.add(v);
                }
                else {
                    continue;
                }
            }
            return (vacancy.size() <= 0) ? Integer.valueOf(-1) : ((Integer)Collections.min(vacancy));
        }
        return Integer.valueOf(-1);
    }
    
    public void refreshStoreEquip(Role role, StoreEquip equip) {
        RespFlyInfo respFlyInfo = new RespFlyInfo();
        respFlyInfo.setEquip(new EquipEasy(equip, (int)role.getRoleId()));
        respFlyInfo.setPos((byte)1);
        MessagePusher.pushMessage(role, respFlyInfo);
    }
    
    public void refreshStoreTianFu(Role role, RoleTianFu roleTianFu) {
        RoleTianFuClient roleTianFuClient = new RoleTianFuClient(roleTianFu);
        RespFlyInfo respFlyInfo = new RespFlyInfo();
        respFlyInfo.setRoleTianFuClient(roleTianFuClient);
        respFlyInfo.setPos((byte)1);
        respFlyInfo.setType((byte)2);
        MessagePusher.pushMessage(role, respFlyInfo);
    }
    
    public void refreshStoreGoods(Role role, StoreGoods goods) {
        RespFlyInfo respFlyInfo = new RespFlyInfo();
        respFlyInfo.setType((byte)1);
        List<FiedValue> list = new ArrayList<>();
        NutMap rests = goods.getRests();
        try {
            for (String key : rests.keySet()) {
                list.add(new FiedValue(key, (byte)3, Integer.valueOf(rests.getInt(key))));
            }
            NutMap special = goods.getSpecial();
            for (String key2 : special.keySet()) {
                list.add(new FiedValue(key2, (byte)3, Integer.valueOf(special.getInt(key2))));
            }
        }
        catch (ConcurrentModificationException ex) {}
        respFlyInfo.setGoods(new Goods(String.valueOf(goods.getId()), Integer.valueOf(goods.getNum()), Integer.valueOf(goods.getSerial()), Integer.valueOf(goods.getPos()), list));
        respFlyInfo.setPos((byte)1);
        MessagePusher.pushMessage(role, respFlyInfo);
    }
}
