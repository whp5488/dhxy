package com.kitty.system.game.tianfu.controller;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.tianfu.message.ReqDayInCeUpgrade;
import com.kitty.system.game.tianfu.message.ReqTianFu;
import com.kitty.system.game.tianfu.message.ReqTianFuResolve;
import com.kitty.system.game.tianfu.message.ReqTianFuSynthesis;
import com.kitty.system.game.tianfu.message.ReqTianFuWear;
import com.kitty.system.game.tianfu.service.RoleDayInCeService;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import java.util.ArrayList;
import java.util.List;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RoleTianFuController {
  @Autowired
  private RoleTianFuService roleTianFuService;
  
  @Autowired
  private RoleDayInCeService roleDayInCeService;
  
  @RequestMapping
  public void roleTianFu(IoSession session, ReqTianFu reqTianFu) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    switch (reqTianFu.getType()) {
      case 1:
        this.roleTianFuService.roleTianFuList(role);
        break;
      case 2:
        this.roleTianFuService.refreshTianFuDebris(role);
        break;
    } 
  }
  
  @RequestMapping
  public void roleTianFuSynthesis(IoSession session, ReqTianFuSynthesis reqTianFuSynthesis) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    Short num = Short.valueOf(reqTianFuSynthesis.getNum());
    if (num == null || num.shortValue() <= 0)
      return; 
    this.roleTianFuService.roleTianFuSynthesis(role, num);
  }
  
  @RequestMapping
  public void roleTianFuResolve(IoSession session, ReqTianFuResolve reqTianFuResolve) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    List<String> ids = reqTianFuResolve.getIds();
    List<Long> idList = new ArrayList<>();
    if (ids == null || ids.size() <= 0)
      return; 
    for (String idStr : ids) {
      Long id;
      try {
        id = Long.valueOf(Long.parseLong(idStr));
      } catch (NumberFormatException e) {
        continue;
      } 
      idList.add(id);
    } 
    if (idList.size() != ids.size())
      return; 
    Byte type = Byte.valueOf(reqTianFuResolve.getType());
    if (0 == type.byteValue()) {
      this.roleTianFuService.roleTianFuResolve(role, idList);
    } else if (1 == type.byteValue()) {
      this.roleTianFuService.roleTianFuIdentify(role, idList);
    } 
  }
  
  @RequestMapping
  public void dayInCe(IoSession session, ReqDayInCeUpgrade reqDayInCeUpgrade) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    Integer addLevel = reqDayInCeUpgrade.getAddLevel();
    if (addLevel == null || addLevel.intValue() == 0) {
      this.roleDayInCeService.getDayInCeLevel(role);
    } else if (addLevel.intValue() > 0) {
      this.roleDayInCeService.addDayInCeLevel(role, addLevel);
    } else if (addLevel.intValue() == -1) {
      this.roleDayInCeService.getAchievement(role);
    } 
  }
  
  @RequestMapping
  public void tianFuWear(IoSession session, ReqTianFuWear reqTianFuWear) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    byte type = reqTianFuWear.getType();
    if (1 != type && 0 != type)
      return; 
    String tianFuIdStr = reqTianFuWear.getId();
    if (tianFuIdStr == null || "".equals(tianFuIdStr))
      return; 
    long tianFuId = 0L;
    try {
      tianFuId = Long.parseLong(tianFuIdStr);
    } catch (NumberFormatException e) {
      System.err.println("佩戴/卸下天策符id整数转换错误");
    } 
    short grid = reqTianFuWear.getGrid();
    if (grid < 1)
      return; 
    this.roleDayInCeService.tianFuWear(role, type, tianFuId, grid);
  }
}
