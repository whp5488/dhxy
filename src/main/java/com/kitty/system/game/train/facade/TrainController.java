package com.kitty.system.game.train.facade;

import com.kitty.system.game.train.message.ReqTrainPerfect;
import com.kitty.system.game.train.message.ReqUpTrainGrade;
import com.kitty.system.game.train.message.ReqChangeSeal;
import com.kitty.system.game.train.message.ReqUpgradeSeal;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.train.message.ReqMagicCircle;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.train.TrainService;
import org.springframework.stereotype.Controller;

@Controller
public class TrainController
{
    @Autowired
    TrainService trainService;
    
    public TrainController() {
    }
    
    @RequestMapping
    public void switchMagicCircle(IoSession session, ReqMagicCircle reqMagicCircle) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.trainService.switchMagicCircle(role, reqMagicCircle);
    }
    
    @RequestMapping
    public void upgradeSeal(IoSession session, ReqUpgradeSeal upgradeSeal) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.trainService.upgrade(role, Long.valueOf(upgradeSeal.getId()), upgradeSeal.getType(), upgradeSeal.getParam(), upgradeSeal.getOperationType(), true, "客户端-信物打造处理");
    }
    
    @RequestMapping
    public void changeSeal(IoSession session, ReqChangeSeal reqChangeSeal) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.trainService.changeSeal(role, reqChangeSeal);
    }
    
    @RequestMapping
    public void upTrainGrade(IoSession session, ReqUpTrainGrade reqUpTrainGrade) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.trainService.upTrainGrade(role, reqUpTrainGrade);
    }
    
    @RequestMapping
    public void trainPerfect(IoSession session, ReqTrainPerfect reqTrainPerfect) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.trainService.trainPerfect(role);
    }
}
