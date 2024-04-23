package com.kitty.system.game.i18n.service;

import com.kitty.common.mysql.TargetDataSource;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.i18n.P_I18nId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class I18nIdService {
  @TargetDataSource(name = "se")
  public Map<Integer, P_I18nId> reload() {
    Dao dao = (Dao)SpringUtils.getBean(Dao.class);
    List<P_I18nId> list = dao.query(P_I18nId.class, (Condition)Cnd.NEW());
    return (Map<Integer, P_I18nId>)list.stream().collect(Collectors.toMap(P_I18nId::getSerial, p_I18nId -> p_I18nId));
  }
}
