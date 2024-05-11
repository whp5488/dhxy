
package com.kitty.system.game.confirm.service.handler;

import com.kitty.system.game.team.service.TeamService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.game.confirm.model.LeaveTempTeamConfirm;
import com.kitty.system.game.confirm.message.ReqConfirmResult;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;
import com.kitty.system.game.confirm.handler.ConfirmHandler;

@Component
public class LeaveTempTeamHandler extends ConfirmHandler
{
    private static final String select = "1";
    
    @Override
    public void handleConfirmResult(final Role role, final ReqConfirmResult reqConfirmResult) {
        final LeaveTempTeamConfirm confirm = (LeaveTempTeamConfirm)role.getConfirm();
        final int type = confirm.getType();
        if ("1".equals(reqConfirmResult.getSelect()) && type == 1) {
            this.at(role);
        }
    }
    
    public void at(final Role role) {
        if (!SpringUtils.getBean(AtWillHandler.class).isLeave(role)) {
            return;
        }
        role.pushTempCache("at_leave_temp", true);
        final TeamService teamService = SpringUtils.getTeamService();
        teamService.leaveTemp(role, true);
        role.pushTempCache("at_leave_temp", false);
    }
}
