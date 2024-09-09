package com.kitty.system.init.product;

import java.util.Iterator;
import java.util.Map;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.service.CommonService;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.system.game.config.Reloadable;
import java.util.concurrent.ConcurrentMap;

public enum ProductDataPool
{
    INSTANCE;
    
    private ConcurrentMap<Class<?>, Reloadable> datas;
    
    private ProductDataPool() {
        this.datas = new ConcurrentHashMap();
    }
    
    public void load() {
        ((CommonService)SpringUtils.getBean(CommonService.class)).loadProduct();
        Map<String, Reloadable> storages = SpringUtils.getBeansOfType(Reloadable.class);
        for (Map.Entry<String, Reloadable> entry : storages.entrySet()) {
            Reloadable reloadable = (Reloadable)entry.getValue();
            reloadable.reload();
            this.datas.put(reloadable.getClass(), reloadable);
        }
    }
}
