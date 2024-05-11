
package com.kitty.mina.cache;

import com.kitty.system.game.store.domain.StoreEquip;
import com.kitty.system.game.store.domain.StoreGoods;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.domain.BagGoods;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface BagCache
{
    public static final ConcurrentHashMap<Long, Map<Long, BagGoods>> BAG_GOODS_MAP = new ConcurrentHashMap<Long, Map<Long, BagGoods>>();
    public static final ConcurrentHashMap<Long, Map<Long, BagEquip>> BAG_EQUIP_MAP = new ConcurrentHashMap<Long, Map<Long, BagEquip>>();
    public static final ConcurrentHashMap<Long, Map<Long, StoreGoods>> STORE_GOODS_MAP = new ConcurrentHashMap<Long, Map<Long, StoreGoods>>();
    public static final ConcurrentHashMap<Long, Map<Long, StoreEquip>> STORE_EQUIP_MAP = new ConcurrentHashMap<Long, Map<Long, StoreEquip>>();
}
