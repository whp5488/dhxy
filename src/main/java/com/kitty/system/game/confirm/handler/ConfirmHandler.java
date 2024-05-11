
package com.kitty.system.game.confirm.handler;

import com.kitty.system.game.confirm.message.ReqConfirmResult;
import com.kitty.system.game.role.domain.Role;

public abstract class ConfirmHandler
{
    public abstract void handleConfirmResult(final Role p0, final ReqConfirmResult p1);
}
