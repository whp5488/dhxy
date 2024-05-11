
package com.kitty.system.game.i18n.service;

import com.kitty.common.mysql.TargetDataSource;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.common.utils.spring.SpringUtils;
import org.nutz.dao.Dao;
import com.kitty.system.game.i18n.P_I18nId;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class I18nIdService
{
    @TargetDataSource(name = "se")
    public Map<Integer, P_I18nId> reload() {
        final Dao dao = SpringUtils.getBean(Dao.class);
        final List<P_I18nId> list = dao.query(P_I18nId.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(P_I18nId::getSerial, P_I18nId -> P_I18nId));
    }
}
