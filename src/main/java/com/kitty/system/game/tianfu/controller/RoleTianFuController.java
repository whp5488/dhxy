package com.kitty.system.game.tianfu.controller;

import com.kitty.system.game.tianfu.message.ReqTianFuWear;
import com.kitty.system.game.tianfu.message.ReqDayInCeUpgrade;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import com.kitty.system.game.tianfu.message.ReqTianFuResolve;
import com.kitty.system.game.tianfu.message.ReqTianFuSynthesis;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.tianfu.message.ReqTianFu;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.tianfu.service.RoleDayInCeService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import org.springframework.stereotype.Controller;

@Controller
public class RoleTianFuController
{
    @Autowired
    private RoleTianFuService roleTianFuService;
    @Autowired
    private RoleDayInCeService roleDayInCeService;
    
    public RoleTianFuController() {
    }
    
    @RequestMapping
    public void roleTianFu(IoSession session, ReqTianFu reqTianFu) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        switch (reqTianFu.getType()) {
            case 1: {
                this.roleTianFuService.roleTianFuList(role);
                break;
            }
            case 2: {
                this.roleTianFuService.refreshTianFuDebris(role);
                break;
            }
        }
        return;
    }
    
    @RequestMapping
    public void roleTianFuSynthesis(IoSession session, ReqTianFuSynthesis reqTianFuSynthesis) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        Short num = Short.valueOf(reqTianFuSynthesis.getNum());
        if (num == null || (short)num <= 0) {
            return;
        }
        this.roleTianFuService.roleTianFuSynthesis(role, num);
        return;
    }
    
    @RequestMapping
    public void roleTianFuResolve(IoSession session, ReqTianFuResolve reqTianFuResolve) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        List<String> ids = reqTianFuResolve.getIds();
        List<Long> idList = new ArrayList<>();
        if (ids == null || ids.size() <= 0) {
            return;
        }
        for (String idStr : ids) {
            Long id;
            try {
                id = Long.valueOf(Long.parseLong(idStr));
            }
            catch (NumberFormatException e) {
                continue;
            }
            idList.add(id);
        }
        if (idList.size() != ids.size()) {
            return;
        }
        Byte type = Byte.valueOf(reqTianFuResolve.getType());
        if (0 == (byte)type) {
            this.roleTianFuService.roleTianFuResolve(role, idList);
        }
        else if (1 == (byte)type) {
            this.roleTianFuService.roleTianFuIdentify(role, idList);
        }
        return;
    }
    
    @RequestMapping
    public void dayInCe(IoSession session, ReqDayInCeUpgrade reqDayInCeUpgrade) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        Integer addLevel = reqDayInCeUpgrade.getAddLevel();
        if (addLevel == null || (int)addLevel == 0) {
            this.roleDayInCeService.getDayInCeLevel(role);
        }
        else if ((int)addLevel > 0) {
            this.roleDayInCeService.addDayInCeLevel(role, addLevel);
        }
        else if ((int)addLevel == -1) {
            this.roleDayInCeService.getAchievement(role);
        }
        return;
    }
    
    @RequestMapping
    public void tianFuWear(IoSession session, ReqTianFuWear reqTianFuWear) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        byte type = reqTianFuWear.getType();
        if (1 != type && 0 != type) {
            return;
        }
        String tianFuIdStr = reqTianFuWear.getId();
        if (tianFuIdStr == null || "".equals(tianFuIdStr)) {
            return;
        }
        long tianFuId = 0L;
        try {
            tianFuId = Long.parseLong(tianFuIdStr);
        }
        catch (NumberFormatException e) {
            System.err.println("佩戴/卸下天策符id整数转换错误");
        }
        short grid = reqTianFuWear.getGrid();
        if (grid < 1) {
            return;
        }
        this.roleDayInCeService.tianFuWear(role, type, tianFuId, (int)grid);
        return;
    }
}
