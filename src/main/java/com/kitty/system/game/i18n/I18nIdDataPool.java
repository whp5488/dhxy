package com.kitty.system.game.i18n;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.i18n.service.I18nIdService;
import org.springframework.stereotype.Component;
import com.kitty.system.game.config.Reloadable;

@Component
public class I18nIdDataPool implements Reloadable
{
    @Autowired
    private I18nIdService i18nIdService;
    public static Map<Integer, P_I18nId> ids;
    
    public I18nIdDataPool() {
    }
    
    @Override
    public void reload() {
        I18nIdDataPool.ids = this.i18nIdService.reload();
    }
    
    public static String getI18nContent(int i18nId) {
        P_I18nId idResource = (P_I18nId)I18nIdDataPool.ids.get(Integer.valueOf(i18nId));
        if (idResource != null) {
            return idResource.getContent();
        }
        return null;
    }
    
    public static String getI18nContent(int i18nId, Object... args) {
        P_I18nId idResource = (P_I18nId)I18nIdDataPool.ids.get(Integer.valueOf(i18nId));
        if (idResource != null) {
            return String.format(idResource.getContent(), args);
        }
        return null;
    }
}
