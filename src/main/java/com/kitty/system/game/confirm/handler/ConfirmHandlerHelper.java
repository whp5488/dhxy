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
        this.handlers = new HashMap();
    }
    
    private void registerHandler(ConfirmType confirmType, ConfirmHandler confirmHandler) {
        this.handlers.put(confirmType, confirmHandler);
    }
    
    public ConfirmHandler getTargetHandler(ConfirmType confirmType) {
        return (ConfirmHandler)this.handlers.get(confirmType);
    }
    
    public void initialize() {
        this.registerHandler(ConfirmType.RETURN_TEAM, (ConfirmHandler)SpringUtils.getBean(ReturnTeamHandler.class));
        this.registerHandler(ConfirmType.REQUEST_TEAM_LEADER, (ConfirmHandler)SpringUtils.getBean(RequestTeamLeaderHandler.class));
        this.registerHandler(ConfirmType.LEAVE_TEAM, (ConfirmHandler)SpringUtils.getBean(LeaveTeamHandler.class));
        this.registerHandler(ConfirmType.LEAVE_TEMP_TEAM, (ConfirmHandler)SpringUtils.getBean(LeaveTempTeamHandler.class));
        this.registerHandler(ConfirmType.COUNTRY, (ConfirmHandler)SpringUtils.getBean(QuestionHandler.class));
    }
    
    static {
        ConfirmHandlerHelper.INSTANCE.initialize();
    }
}
