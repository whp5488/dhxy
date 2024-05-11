
package com.kitty.system.game.confirm.handler;

import com.kitty.system.game.confirm.service.handler.QuestionHandler;
import com.kitty.system.game.confirm.service.handler.LeaveTempTeamHandler;
import com.kitty.system.game.confirm.service.handler.LeaveTeamHandler;
import com.kitty.system.game.confirm.service.handler.RequestTeamLeaderHandler;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.confirm.service.handler.ReturnTeamHandler;
import java.util.HashMap;
import com.kitty.system.enums.confirm.ConfirmType;
import java.util.Map;

public enum ConfirmHandlerHelper
{
    INSTANCE;
    
    private Map<ConfirmType, ConfirmHandler> handlers;
    
    private ConfirmHandlerHelper() {
        this.handlers = new HashMap<ConfirmType, ConfirmHandler>();
    }
    
    private void registerHandler(final ConfirmType confirmType, final ConfirmHandler confirmHandler) {
        this.handlers.put(confirmType, confirmHandler);
    }
    
    public ConfirmHandler getTargetHandler(final ConfirmType confirmType) {
        return this.handlers.get(confirmType);
    }
    
    public void initialize() {
        this.registerHandler(ConfirmType.RETURN_TEAM, SpringUtils.getBean(ReturnTeamHandler.class));
        this.registerHandler(ConfirmType.REQUEST_TEAM_LEADER, SpringUtils.getBean(RequestTeamLeaderHandler.class));
        this.registerHandler(ConfirmType.LEAVE_TEAM, SpringUtils.getBean(LeaveTeamHandler.class));
        this.registerHandler(ConfirmType.LEAVE_TEMP_TEAM, SpringUtils.getBean(LeaveTempTeamHandler.class));
        this.registerHandler(ConfirmType.COUNTRY, SpringUtils.getBean(QuestionHandler.class));
    }
    
    static {
        ConfirmHandlerHelper.INSTANCE.initialize();
    }
}
