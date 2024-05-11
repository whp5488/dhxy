
package com.kitty.cxf.utlis;

import org.apache.mina.core.future.CloseFuture;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.Logger;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.logs.LoggerFunction;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

@Component
public class Promote
{
    public void closeSession(final IoSession session) {
        if (session == null) {
            return;
        }
        final Logger logger = LoggerFunction.MONOIOR.getLogger();
        final Role role = SessionUtils.getRoleBySession(session);
        try {
            final String name = (role != null) ? role.getName() : "";
            if (session.isConnected() && !session.isClosing()) {
                final CloseFuture future = session.closeNow();
                future.addListener((IoFutureListener)new IoFutureListener<IoFuture>() {
                    public void operationComplete(final IoFuture future) {
                        logger.error("session[{}] closed complete,player[{}]", (Object)session.getId(), (Object)name);
                    }
                });
            }
        }
        catch (final Exception e) {
            logger.error("session[{}] closed complete exception, player[{}]", (Object)session.getId(), (Object)role.getName());
            e.getMessage();
        }
    }
}
