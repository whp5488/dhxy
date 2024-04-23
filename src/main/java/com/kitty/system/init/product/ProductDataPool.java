package com.kitty.system.init.product;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.config.Reloadable;
import com.kitty.system.init.service.CommonService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum ProductDataPool {
  INSTANCE;
  
  private ConcurrentMap<Class<?>, Reloadable> datas;
  
  ProductDataPool() {
    this.datas = new ConcurrentHashMap<>();
  }
  
  public void load() {
    ((CommonService)SpringUtils.getBean(CommonService.class)).loadProduct();
    Map<String, Reloadable> storages = SpringUtils.getBeansOfType(Reloadable.class);
    for (Map.Entry<String, Reloadable> entry : storages.entrySet()) {
      Reloadable reloadable = entry.getValue();
      reloadable.reload();
      this.datas.put(reloadable.getClass(), reloadable);
    } 
  }
}
