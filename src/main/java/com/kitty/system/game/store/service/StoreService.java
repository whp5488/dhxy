
package com.kitty.system.game.store.service;

import com.kitty.system.game.role.domain.Goods;
import java.util.ConcurrentModificationException;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.equip.domain.EquipEasy;
import com.kitty.system.game.bag.message.RespFlyInfo;
import com.kitty.system.game.player.bean.PlayerExtendBox;
import java.util.Collection;
import java.util.Collections;
import com.kitty.system.game.vo.utils.Const;
import java.util.function.Function;
import org.nutz.dao.Condition;
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
import com.kitty.system.game.tianfu.message.RespTianFuList;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import com.kitty.system.game.bag.message.RespDeleteItem;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.tianfu.message.RespTianFuDelete;
import java.util.Arrays;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import java.util.List;
import com.kitty.mina.cache.BagCache;
import java.util.Map;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.store.domain.StoreEquip;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
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
    
    public void excuteStorePara(final Role role, final Byte type, final Byte storeType, final String posOrId) {
        final boolean clicker = this.roleService.mouserClicker(role, "store", 250L);
        if (!clicker) {
            return;
        }
        switch (storeType) {
            case 0: {
                if (type == 1) {
                    final int pos = Integer.parseInt(posOrId);
                    final int posStor = this.getPos(role);
                    if (posStor <= -1) {
                        MessagePusher.pushMessage(role, new RespNotifyMiscEx("仓库物品栏空间不足"));
                        return;
                    }
                    final Map<Long, BagEquip> bagEquip = this.bagService.getBagEquip(role);
                    final BagEquip equip = bagEquip.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
                    if (equip != null) {
                        final StoreEquip equipStore = new StoreEquip();
                        BeanUtils.copyBeanProp(equipStore, equip);
                        equip.setDelete(true);
                        BagCache.BAG_EQUIP_MAP.get(role.getId()).remove(equip.getId());
                        equip.save();
                        equipStore.setPos(posStor);
                        BagCache.STORE_EQUIP_MAP.get(role.getId()).put(equipStore.getId(), equipStore);
                        equipStore.save();
                        this.bagService.deleteInform(role, (byte)0, equip.getPos());
                        this.refreshStoreEquip(role, equipStore);
                    }
                } else {
                    if (type != 0) {
                        break;
                    }
                    final int pos = Integer.parseInt(posOrId);
                    final int posBag = this.bagService.getPos(role, false);
                    if (posBag <= -1) {
                        MessagePusher.pushMessage(role, new RespNotifyMiscEx("背包物品栏空间不足"));
                        return;
                    }
                    final Map<Long, StoreEquip> storeEquipMap = this.getStoreEquip(role);
                    final StoreEquip storeEquip = storeEquipMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
                    if (storeEquip != null) {
                        final BagEquip equipBag = new BagEquip();
                        BeanUtils.copyBeanProp(equipBag, storeEquip);
                        storeEquip.setDelete(true);
                        BagCache.STORE_EQUIP_MAP.get(role.getId()).remove(storeEquip.getId());
                        storeEquip.save();
                        equipBag.setPos(posBag);
                        BagCache.BAG_EQUIP_MAP.get(role.getId()).put(equipBag.getId(), equipBag);
                        equipBag.save();
                        this.bagService.refreshRoleEquip(role, equipBag, false);
                        this.bagService.deleteInform(role, (byte)1, storeEquip.getPos());
                    }
                }
                break;
            }
            case 1: {
                if (type == 1) {
                    this.excuteGoods(role, Integer.parseInt(posOrId));
                    break;
                }
                if (type == 0) {
                    this.excuteGoodsStore(role, Integer.parseInt(posOrId));
                    break;
                }
                break;
            }
            case 4: {
                if (type == 1) {
                    final long tianFuId = Long.parseLong(posOrId);
                    final int posStor2 = this.getPos(role);
                    final List<RoleTianFu> list = DataCache.ROLE_TIAN_FU.get(role.getId());
                    final RoleTianFu roleTianFu = list.stream().filter(obj -> obj.getId().equals(tianFuId) && obj.getIsWear() == 0 && obj.getStorePos() == 0).findFirst().orElse(null);
                    if (roleTianFu != null) {
                        if (roleTianFu.getTianFuSerial() == 9997) {
                            this.debrisToStore(role, roleTianFu, roleTianFu.getCount(), posStor2);
                        }
                        else {
                            if (posStor2 <= -1) {
                                MessagePusher.pushMessage(role, new RespNotifyMiscEx("仓库物品栏空间不足"));
                                return;
                            }
                            roleTianFu.setStorePos(posStor2);
                            roleTianFu.save();
                            this.refreshStoreTianFu(role, roleTianFu);
                            MessagePusher.pushMessage(role, new RespTianFuDelete(Arrays.asList(roleTianFu.getId().toString())));
                        }
                    }
                    break;
                }
                if (type == 0) {
                    final long tianFuId = Long.parseLong(posOrId);
                    final List<RoleTianFu> list2 = DataCache.ROLE_TIAN_FU.get(role.getId());
                    final RoleTianFu roleTianFu2 = list2.stream().filter(obj -> obj.getId() == tianFuId && obj.getStorePos() > 0).findFirst().orElse(null);
                    if (roleTianFu2 != null) {
                        if (roleTianFu2.getTianFuSerial() == 9997) {
                            final int count = roleTianFu2.getCount();
                            if (count > 9999) {
                                MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符数量已达上限"));
                                return;
                            }
                            this.debrisGet(role, roleTianFu2, count);
                        }
                        else {
                            final List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
                            if (roleTianFus.size() >= 75) {
                                MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符数量已达上限"));
                                return;
                            }
                            MessagePusher.pushMessage(role, new RespDeleteItem((byte)1, roleTianFu2.getStorePos()));
                            roleTianFu2.setStorePos(0);
                            roleTianFu2.save();
                            MessagePusher.pushMessage(role, new RespTianFuList((byte)1, Arrays.asList(new RoleTianFuClient(roleTianFu2))));
                        }
                    }
                    break;
                }
                break;
            }
            default: {
                this.logger.error("仓库存储无该类型【{}】操作", (Object)storeType);
                break;
            }
        }
    }
    
    private void debrisToStore(final Role role, final RoleTianFu roleTianFu, int count, final int posStor) {
        final List<RoleTianFu> debrisList = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getTianFuSerial() == 9997 && obj.getStorePos() != 0).collect(Collectors.toList());
        final int countOld = count;
        for (final RoleTianFu debris : debrisList) {
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
                DataCache.ROLE_TIAN_FU.get(role.getId()).remove(roleTianFu);
            }
            roleTianFu.save();
        }
    }
    
    private void debrisGet(final Role role, final RoleTianFu storeTianFu, int count) {
        final List<RoleTianFu> list = DataCache.ROLE_TIAN_FU.get(role.getId());
        final RoleTianFu debris = list.stream().filter(obj -> obj.getTianFuSerial() == 9997 && obj.getStorePos() == 0).findFirst().orElse(null);
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
            final List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
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
            final RoleTianFu roleTianFu = new RoleTianFu();
            BeanUtils.copyBeanProp(roleTianFu, storeTianFu);
            roleTianFu.setId(this.worker.nextId());
            roleTianFu.setCount((short)count);
            roleTianFu.setStorePos(0);
            roleTianFu.save();
            MessagePusher.pushMessage(role, new RespTianFuDebris(roleTianFu));
            this.debrisToBag(role, roleTianFu, count);
        }
    }
    
    private void debrisToBag(final Role role, final RoleTianFu storeTianFu, int count) {
        if (storeTianFu.getCount() <= count) {
            count -= storeTianFu.getCount();
            storeTianFu.setCount((short)0);
            storeTianFu.setDelete(true);
            DataCache.ROLE_TIAN_FU.get(role.getId()).remove(storeTianFu);
            MessagePusher.pushMessage(role, new RespDeleteItem((byte)1, storeTianFu.getStorePos()));
        }
        else {
            storeTianFu.setCount((short)(storeTianFu.getCount() - count));
            count = 0;
            this.refreshStoreTianFu(role, storeTianFu);
        }
        storeTianFu.save();
        if (count > 0) {
            final List<RoleTianFu> storeDebrisList = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getTianFuSerial() == 9997 && obj.getStorePos() != 0 && !obj.getId().equals(storeTianFu.getId())).collect(Collectors.toList());
            for (final RoleTianFu debrisTianFu : storeDebrisList) {
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
                    DataCache.ROLE_TIAN_FU.get(role.getId()).remove(debrisTianFu);
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
            }
        }
    }
    
    private void excuteGoods(final Role role, final int pos) {
        final Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
        final BagGoods goods = bagGoodsMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
        if (goods != null) {
            final int posStor = this.getPos(role);
            if (posStor <= -1) {
                final Map<Long, StoreGoods> storeGoodsMap = this.getStoreGoods(role);
                final StoreGoods storeGoods = storeGoodsMap.values().stream().filter(obj -> obj.getSerial() == goods.getSerial()).findFirst().orElse(null);
                final Map map = DataGlobalFunc.GetItemDataByItemTypeId(goods.getSerial());
                final JSONObject json = JsonUtils.objectToJson(map.get(goods.getSerial()));
                final int canMerge = json.getInteger("canMerge");
                if (storeGoods == null || storeGoods.getNum() + goods.getNum() > canMerge) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("仓库物品栏空间不足"));
                    return;
                }
            }
            final int count = goods.getNum();
            final StoreGoods goodsStore = new StoreGoods();
            BeanUtils.copyBeanProp(goodsStore, goods);
            this.bagService.removeItem(role, pos, 1, "存入仓库，数量：" + count);
            this.saveStoreGoods(role, goodsStore, goodsStore.getNum());
            this.bagService.refreshRoleGoods(role, goods, false);
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)1, String.format("道具（%d）存入仓库，仓库数量增加：%d", goods.getSerial(), count), role.getMarking());
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)2, String.format("道具（%d）存入仓库，背包数量减少：%d", goods.getSerial(), count), role.getMarking());
        }
    }
    
    private void excuteGoodsStore(final Role role, final int pos) {
        final Map<Long, StoreGoods> storeGoodsMap = this.getStoreGoods(role);
        final StoreGoods goods = storeGoodsMap.values().stream().filter(e -> e.getPos() == pos).findFirst().orElse(null);
        if (goods != null) {
            final int count = goods.getNum();
            final boolean flag = this.bagService.checkPos(role, goods.getSerial(), count);
            if (!flag) {
                return;
            }
            final NutMap rests = goods.getRests();
            if (rests.size() > 0) {
                for (final Map.Entry<String, Object> entry : rests.entrySet()) {
                    this.bagService.addGoods(role, goods.getSerial(), (Integer)entry.getValue(), Integer.parseInt(entry.getKey()), false, false, "仓库取出");
                }
            }
            goods.setRests(NutMap.NEW());
            goods.setDelete(true);
            BagCache.STORE_GOODS_MAP.get(role.getId()).remove(goods.getId());
            goods.save();
            this.refreshStoreGoods(role, goods);
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)2, String.format("道具（%d）从仓库取出，仓库数量减少：%d", goods.getSerial(), count), role.getMarking());
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)1, String.format("道具（%d）从仓库取出，背包数量增加：%d", goods.getSerial(), count), role.getMarking());
        }
    }
    
    private void saveStoreGoods(final Role role, final StoreGoods storeGood, int num) {
        final List<StoreGoods> storeGoods = this.getStoreGoods(role).values().stream().collect(Collectors.toList());
        final Map map = DataGlobalFunc.GetItemDataByItemTypeId(storeGood.getSerial());
        if (map == null) {
            return;
        }
        final JSONObject json = JsonUtils.objectToJson(map.get(storeGood.getSerial()));
        final int canMerge = json.getInteger("canMerge");
        final List<StoreGoods> collect = storeGoods.stream().filter(item -> item.getSerial() == storeGood.getSerial()).collect(Collectors.toList());
        if (collect.size() > 0) {
            for (final StoreGoods goods : collect) {
                final int residue = canMerge - goods.getNum();
                if (residue <= 0) {
                    continue;
                }
                if (residue >= num) {
                    this.saveGoods(storeGood.getRests(), num, goods.getRests());
                    goods.save();
                    this.refreshStoreGoods(role, goods);
                    num = 0;
                    break;
                }
                num = Math.abs(residue - num);
                this.saveGoods(storeGood.getRests(), residue, goods.getRests());
                goods.save();
                this.refreshStoreGoods(role, goods);
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
            storeGood.setPos(this.getPos(role));
            storeGood.setId(this.worker.nextId());
            storeGood.save();
            BagCache.STORE_GOODS_MAP.get(role.getId()).put(storeGood.getId(), storeGood);
            this.refreshStoreGoods(role, storeGood);
        }
    }
    
    public boolean saveGoods(final NutMap restsSrc, Integer num, final NutMap rests) {
        final List<String> removeKeys = new ArrayList<String>();
        for (final String key : restsSrc.keySet()) {
            if (num <= 0) {
                return true;
            }
            boolean flag = true;
            for (final String key2 : rests.keySet()) {
                if (key.equals(key2)) {
                    num = this.excuteGoodNum(restsSrc, rests, key, num, true);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                num = this.excuteGoodNum(restsSrc, rests, key, num, false);
            }
            if (restsSrc.getInt(key) > 0) {
                continue;
            }
            removeKeys.add(key);
        }
        if (removeKeys.size() > 0) {
            for (final String key : removeKeys) {
                restsSrc.remove((Object)key);
            }
        }
        return true;
    }
    
    private int excuteGoodNum(final NutMap restsSrc, final NutMap rests, final String key, int num, final boolean flag) {
        int value;
        if (num < restsSrc.getInt(key)) {
            value = num;
        }
        else {
            value = restsSrc.getInt(key);
        }
        if (flag) {
            rests.setv(key, (Object)(rests.getInt(key) + value));
        }
        else {
            rests.setv(key, (Object)value);
        }
        restsSrc.setv(key, (Object)(restsSrc.getInt(key) - value));
        num -= value;
        return num;
    }
    
    public int getPos(final Role role) {
        return this.getMinAndMaxPos(role);
    }
    
    private Integer getMinAndMaxPos(final Role role) {
        final List<Integer> list = new ArrayList<Integer>();
        final Map<Long, StoreEquip> equips = this.getStoreEquip(role);
        for (final StoreEquip equip : equips.values()) {
            list.add(equip.getPos());
        }
        final Map<Long, StoreGoods> goods = this.getStoreGoods(role);
        for (final StoreGoods good : goods.values()) {
            list.add(good.getPos());
        }
        final List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getStorePos() > 0).collect(Collectors.toList());
        for (final RoleTianFu roleTianFu : roleTianFus) {
            list.add(roleTianFu.getStorePos());
        }
        return this.getVacancySize(role, list);
    }
    
    public Map<Long, StoreEquip> getStoreEquip(final Role role) {
        final Map<Long, StoreEquip> storeGoodsMap = BagCache.STORE_EQUIP_MAP.get(role.getId());
        if (storeGoodsMap == null) {
            final List<StoreEquip> list = this.dao.query(StoreEquip.class, Cnd.where("r_id", "=", (Object)role.getId()));
            list.forEach(equip -> equip.doAfterInit());
            final Map<Long, StoreEquip> map = list.stream().collect(Collectors.toMap(StoreEquip::getId, StoreEquip -> StoreEquip));
            BagCache.STORE_EQUIP_MAP.put(role.getId(), map);
            return map;
        }
        return storeGoodsMap;
    }
    
    public Map<Long, StoreGoods> getStoreGoods(final Role role) {
        final Map<Long, StoreGoods> storeGoodsMap = BagCache.STORE_GOODS_MAP.get(role.getId());
        if (storeGoodsMap == null) {
            final List<StoreGoods> list = this.dao.query(StoreGoods.class, Cnd.where("r_id", "=", (Object)role.getId()));
            final Map<Long, StoreGoods> map = list.stream().collect(Collectors.toMap(StoreGoods::getId, StoreGoods -> StoreGoods));
            BagCache.STORE_GOODS_MAP.put(role.getId(), map);
            return map;
        }
        return storeGoodsMap;
    }
    
    public Integer getVacancySize(final Role role, final List<Integer> list) {
        final PlayerExtendBox extendBox = role.getExtendBox();
        final List<Integer> objects = new ArrayList<Integer>();
        Integer pos = 0;
        pos = extendBox.getStorePosition() * Const.BAG_POS + Const.STORE_POS_MIN;
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
    
    public void refreshStoreEquip(final Role role, final StoreEquip equip) {
        final RespFlyInfo respFlyInfo = new RespFlyInfo();
        respFlyInfo.setEquip(new EquipEasy(equip, role.getRoleId()));
        respFlyInfo.setPos((byte)1);
        MessagePusher.pushMessage(role, respFlyInfo);
    }
    
    public void refreshStoreTianFu(final Role role, final RoleTianFu roleTianFu) {
        final RoleTianFuClient roleTianFuClient = new RoleTianFuClient(roleTianFu);
        final RespFlyInfo respFlyInfo = new RespFlyInfo();
        respFlyInfo.setRoleTianFuClient(roleTianFuClient);
        respFlyInfo.setPos((byte)1);
        respFlyInfo.setType((byte)2);
        MessagePusher.pushMessage(role, respFlyInfo);
    }
    
    public void refreshStoreGoods(final Role role, final StoreGoods goods) {
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
        respFlyInfo.setGoods(new Goods(String.valueOf(goods.getId()), goods.getNum(), goods.getSerial(), goods.getPos(), list));
        respFlyInfo.setPos((byte)1);
        MessagePusher.pushMessage(role, respFlyInfo);
    }
}
