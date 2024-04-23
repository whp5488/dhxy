package com.kitty.system.game.role.service;

import com.kitty.listener.event.HeartBeatEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.echo.handler.EchoHandler;
import com.kitty.system.game.enter.RespTitleList;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.game.enter.vo.Title;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.message.RespChangeTitle;
import com.kitty.system.game.vo.TitleInfo;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataTitle;
import com.kitty.system.init.pool.PartDataPool;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TitleService extends EchoHandler {
  @Autowired
  RoleService roleService;
  
  @Autowired
  BroadcastService broadcastService;
  
  public void handleEcho(HeartBeatEvent heartBeatEvent) {
    Role role = heartBeatEvent.getRole();
    if (role == null)
      return; 
    ConcurrentHashMap<Integer, TitleInfo> titleMap = role.getExtendBox().getTitleMap();
    if (titleMap != null && titleMap.size() > 0)
      try {
        List<TitleInfo> list = (List<TitleInfo>)titleMap.values().stream().filter(e -> (e.getTime() > 0)).collect(Collectors.toList());
        if (list.size() > 0) {
          int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
          for (TitleInfo titleInfo : list) {
            if (currTime >= titleInfo.getTime()) {
              SysDataTitle title = (SysDataTitle)PartDataPool.titleMap.get(Integer.valueOf(titleInfo.getSerial()));
              if (title != null)
                MessagePusher.notify2Misc(role, false, 501, new Object[] { title.getName() }); 
              delTitle(role, titleInfo.getSerial());
            } 
          } 
        } 
      } catch (Exception e) {
        e.printStackTrace();
      }  
  }
  
  public void changeTitle(Role role, int serial, byte type) {
    TitleInfo titleInfo = (TitleInfo)role.getExtendBox().getTitleMap().get(Integer.valueOf(serial));
    if (titleInfo != null) {
      if (type == 1) {
        role.setCurrTitle(serial);
        MessagePusher.pushMessage(role, (Message)new RespMsg("装备成功"));
      } else {
        role.setCurrTitle(-1);
        MessagePusher.pushMessage(role, (Message)new RespMsg("卸下成功"));
      } 
      RespUpdate respUpdate = new RespUpdate();
      List<FiedValue> list = new ArrayList<>();
      respUpdate.setId(role.getRoleId().intValue());
      list.add(new FiedValue("title", (byte)3, Integer.valueOf(role.getCurrTitle())));
      respUpdate.setList(list);
      MessagePusher.pushMessage(role, (Message)respUpdate);
      this.broadcastService.sendUpdateAppear(role);
      role.save();
    } 
  }
  
  public void addTitle(Role role, TitleInfo titleInfo) {
    boolean flag = false;
    if (Const.PARTY_TITLE_LIST.contains(Integer.valueOf(titleInfo.getSerial()))) {
      for (Integer serial : Const.PARTY_TITLE_LIST)
        role.getExtendBox().getTitleMap().remove(serial); 
      if (Const.PARTY_TITLE_LIST.contains(Integer.valueOf(role.getCurrTitle()))) {
        role.setCurrTitle(titleInfo.getSerial());
        flag = true;
      } 
    } 
    role.getExtendBox().getTitleMap().put(Integer.valueOf(titleInfo.getSerial()), titleInfo);
    refreshTitle(role);
    if (flag)
      this.broadcastService.sendUpdateAppear(role); 
    role.save();
  }
  
  public void delTitle(Role role, int serial) {
    TitleInfo titleInfo = (TitleInfo)role.getExtendBox().getTitleMap().get(Integer.valueOf(serial));
    if (titleInfo != null) {
      if (role.getCurrTitle() == serial)
        role.setCurrTitle(-1); 
      role.getExtendBox().getTitleMap().remove(Integer.valueOf(serial));
      if (this.roleService.isOnline(role.getId().longValue())) {
        this.broadcastService.sendUpdateAppear(role);
        MessagePusher.pushMessage(role, (Message)new RespChangeTitle(serial));
      } 
      role.save();
    } 
  }
  
  public int getTitle(Role role) {
    TitleInfo titleInfo = (TitleInfo)role.getExtendBox().getTitleMap().get(Integer.valueOf(role.getCurrTitle()));
    if (titleInfo != null)
      return titleInfo.getSerial(); 
    return -1;
  }
  
  public void refreshTitle(Role role) {
    List<Title> titles = new ArrayList<>();
    ConcurrentHashMap<Integer, TitleInfo> titleMap = role.getExtendBox().getTitleMap();
    for (Integer key : titleMap.keySet())
      titles.add(new Title(titleMap.get(key))); 
    MessagePusher.pushMessage(role, (Message)new RespTitleList(titles));
  }
}
