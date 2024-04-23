package com.kitty.system.game.confirm.service.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.game.confirm.handler.ConfirmHandler;
import com.kitty.system.game.confirm.message.ReqConfirmResult;
import com.kitty.system.game.confirm.model.LeaveTeamConfirm;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.service.TeamService;
import org.springframework.stereotype.Component;

@Component
public class LeaveTeamHandler extends ConfirmHandler {
  private static final String select = "1";
  
  public void handleConfirmResult(Role role, ReqConfirmResult reqConfirmResult) {
    LeaveTeamConfirm leaveTeamConfirm = (LeaveTeamConfirm)role.getConfirm();
    int type = leaveTeamConfirm.getType();
    if ("1".equals(reqConfirmResult.getSelect()) && 
      type == 1)
      at(role); 
  }
  
  public void at(Role role) {
    if (!((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).isLeave(role))
      return; 
    role.pushTempCache("at_leave", Boolean.valueOf(true));
    TeamService teamService = SpringUtils.getTeamService();
    teamService.leave(role, true);
    role.pushTempCache("at_leave", Boolean.valueOf(false));
  }
}
