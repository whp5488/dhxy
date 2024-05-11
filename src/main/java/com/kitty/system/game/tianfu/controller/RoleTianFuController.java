
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
    
    @RequestMapping
    public void roleTianFu(final IoSession session, final ReqTianFu reqTianFu) {
        final Role role = SessionUtils.getRoleBySession(session);
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
    }
    
    @RequestMapping
    public void roleTianFuSynthesis(final IoSession session, final ReqTianFuSynthesis reqTianFuSynthesis) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final Short num = reqTianFuSynthesis.getNum();
        if (num == null || num <= 0) {
            return;
        }
        this.roleTianFuService.roleTianFuSynthesis(role, num);
    }
    
    @RequestMapping
    public void roleTianFuResolve(final IoSession session, final ReqTianFuResolve reqTianFuResolve) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final List<String> ids = reqTianFuResolve.getIds();
        final List<Long> idList = new ArrayList<Long>();
        if (ids == null || ids.size() <= 0) {
            return;
        }
        for (final String idStr : ids) {
            Long id;
            try {
                id = Long.parseLong(idStr);
            }
            catch (final NumberFormatException e) {
                continue;
            }
            idList.add(id);
        }
        if (idList.size() != ids.size()) {
            return;
        }
        final Byte type = reqTianFuResolve.getType();
        if (0 == type) {
            this.roleTianFuService.roleTianFuResolve(role, idList);
        }
        else if (1 == type) {
            this.roleTianFuService.roleTianFuIdentify(role, idList);
        }
    }
    
    @RequestMapping
    public void dayInCe(final IoSession session, final ReqDayInCeUpgrade reqDayInCeUpgrade) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final Integer addLevel = reqDayInCeUpgrade.getAddLevel();
        if (addLevel == null || addLevel == 0) {
            this.roleDayInCeService.getDayInCeLevel(role);
        }
        else if (addLevel > 0) {
            this.roleDayInCeService.addDayInCeLevel(role, addLevel);
        }
        else if (addLevel == -1) {
            this.roleDayInCeService.getAchievement(role);
        }
    }
    
    @RequestMapping
    public void tianFuWear(final IoSession session, final ReqTianFuWear reqTianFuWear) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final byte type = reqTianFuWear.getType();
        if (1 != type && 0 != type) {
            return;
        }
        final String tianFuIdStr = reqTianFuWear.getId();
        if (tianFuIdStr == null || "".equals(tianFuIdStr)) {
            return;
        }
        long tianFuId = 0L;
        try {
            tianFuId = Long.parseLong(tianFuIdStr);
        }
        catch (final NumberFormatException e) {
            System.err.println("佩戴/卸下天策符id整数转换错误");
        }
        final short grid = reqTianFuWear.getGrid();
        if (grid < 1) {
            return;
        }
        this.roleDayInCeService.tianFuWear(role, type, tianFuId, grid);
    }
}
