
package com.kitty.mina.message;

import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.i18n.P_I18nId;
import org.apache.mina.core.session.IoSession;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.role.domain.Role;

public class MessagePusher
{
    public static void pushMessage(final Role role, final Message message) {
        if (role == null) {
            return;
        }
        pushMessage(role.getId(), message);
    }
    
    public static void pushMessages(final Role role, final Message... messages) {
        if (role == null) {
            return;
        }
        for (final Message message : messages) {
            pushMessage(role.getId(), message);
        }
    }
    
    public static void pushMessage(final long playerId, final Message message) {
        final IoSession session = SessionUtils.getSessionBy(playerId);
        pushMessage(session, message);
    }
    
    public static void pushMessage(final IoSession session, final Message message) {
        if (session == null || message == null || session.isClosing()) {
            return;
        }
        session.write((Object)message);
    }
    
    public static void notify2Player(final IoSession session, final int i18nId, final Object... args) {
        final P_I18nId idResource = I18nIdDataPool.ids.get(i18nId);
        if (idResource != null) {
            final String content = String.format(idResource.getContent(), args);
            pushMessage(session, new RespMsg(content));
        }
    }
    
    public static void notify2Player(final Role role, final int i18nId, final Object... args) {
        if (role == null) {
            return;
        }
        final IoSession session = SessionUtils.getSessionBy(role.getId());
        notify2Player(session, i18nId, args);
    }
    
    public static void notify2Lb(final Role role, final int i18nId, final Object... args) {
        if (role == null) {
            return;
        }
        final P_I18nId idResource = I18nIdDataPool.ids.get(i18nId);
        if (idResource != null) {
            final String content = String.format(idResource.getContent(), args);
            SpringUtils.getBroadcastThread().add2Queue(content);
        }
    }
    
    public static void notify2Misc(final Role role, final boolean flag, final int i18nId, final Object... args) {
        if (role == null) {
            return;
        }
        final P_I18nId idResource = I18nIdDataPool.ids.get(i18nId);
        if (idResource != null) {
            final String content = String.format(idResource.getContent(), args);
            pushMessage(role, new RespNotifyMiscEx(role.getId(), content, flag));
        }
    }
}
