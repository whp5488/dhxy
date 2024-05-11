
package com.kitty.system.game.role.service;

import com.kitty.system.game.enter.RespTitleList;
import com.kitty.system.game.enter.vo.Title;
import com.kitty.system.game.role.message.RespChangeTitle;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.mina.message.Message;
import com.kitty.system.common.message.RespMsg;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysDataTitle;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.vo.TitleInfo;
import java.util.List;
import com.kitty.listener.event.HeartBeatEvent;
import com.kitty.system.game.broadcast.BroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kitty.system.game.echo.handler.EchoHandler;

@Service
public class TitleService extends EchoHandler
{
    @Autowired
    RoleService roleService;
    @Autowired
    BroadcastService broadcastService;
    
    @Override
    public void handleEcho(final HeartBeatEvent heartBeatEvent) {
        final Role role = heartBeatEvent.getRole();
        if (role == null) {
            return;
        }
        final ConcurrentHashMap<Integer, TitleInfo> titleMap = role.getExtendBox().getTitleMap();
        if (titleMap != null && titleMap.size() > 0) {
            try {
                final List<TitleInfo> list = titleMap.values().stream().filter(e -> e.getTime() > 0).collect(Collectors.toList());
                if (list.size() > 0) {
                    final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
                    for (final TitleInfo titleInfo : list) {
                        if (currTime >= titleInfo.getTime()) {
                            final SysDataTitle title = PartDataPool.titleMap.get(titleInfo.getSerial());
                            if (title != null) {
                                MessagePusher.notify2Misc(role, false, 501, title.getName());
                            }
                            this.delTitle(role, titleInfo.getSerial());
                        }
                    }
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void changeTitle(final Role role, final int serial, final byte type) {
        final TitleInfo titleInfo = role.getExtendBox().getTitleMap().get(serial);
        if (titleInfo != null) {
            if (type == 1) {
                role.setCurrTitle(serial);
                MessagePusher.pushMessage(role, new RespMsg("装备成功"));
            }
            else {
                role.setCurrTitle(-1);
                MessagePusher.pushMessage(role, new RespMsg("卸下成功"));
            }
            final RespUpdate respUpdate = new RespUpdate();
            final List<FiedValue> list = new ArrayList<FiedValue>();
            respUpdate.setId(role.getRoleId());
            list.add(new FiedValue("title", (byte)3, role.getCurrTitle()));
            respUpdate.setList(list);
            MessagePusher.pushMessage(role, respUpdate);
            this.broadcastService.sendUpdateAppear(role);
            role.save();
        }
    }
    
    public void addTitle(final Role role, final TitleInfo titleInfo) {
        boolean flag = false;
        if (Const.PARTY_TITLE_LIST.contains(titleInfo.getSerial())) {
            for (final Integer serial : Const.PARTY_TITLE_LIST) {
                role.getExtendBox().getTitleMap().remove(serial);
            }
            if (Const.PARTY_TITLE_LIST.contains(role.getCurrTitle())) {
                role.setCurrTitle(titleInfo.getSerial());
                flag = true;
            }
        }
        role.getExtendBox().getTitleMap().put(titleInfo.getSerial(), titleInfo);
        this.refreshTitle(role);
        if (flag) {
            this.broadcastService.sendUpdateAppear(role);
        }
        role.save();
    }
    
    public void delTitle(final Role role, final int serial) {
        final TitleInfo titleInfo = role.getExtendBox().getTitleMap().get(serial);
        if (titleInfo != null) {
            if (role.getCurrTitle() == serial) {
                role.setCurrTitle(-1);
            }
            role.getExtendBox().getTitleMap().remove(serial);
            if (this.roleService.isOnline(role.getId())) {
                this.broadcastService.sendUpdateAppear(role);
                MessagePusher.pushMessage(role, new RespChangeTitle(serial));
            }
            role.save();
        }
    }
    
    public int getTitle(final Role role) {
        final TitleInfo titleInfo = role.getExtendBox().getTitleMap().get(role.getCurrTitle());
        if (titleInfo != null) {
            return titleInfo.getSerial();
        }
        return -1;
    }
    
    public void refreshTitle(final Role role) {
        final List<Title> titles = new ArrayList<Title>();
        final ConcurrentHashMap<Integer, TitleInfo> titleMap = role.getExtendBox().getTitleMap();
        for (final Integer key : titleMap.keySet()) {
            titles.add(new Title(titleMap.get(key)));
        }
        MessagePusher.pushMessage(role, new RespTitleList(titles));
    }
}
