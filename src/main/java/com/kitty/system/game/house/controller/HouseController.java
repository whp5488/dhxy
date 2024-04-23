package com.kitty.system.game.house.controller;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.house.message.ReqBuyFurniture;
import com.kitty.system.game.house.message.ReqHouseFriedVisit;
import com.kitty.system.game.house.message.ReqHouseGoHome;
import com.kitty.system.game.house.service.HouseService;
import com.kitty.system.game.role.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HouseController {
  @Autowired
  protected HouseService houseService;
  
  @RequestMapping
  public void reqBuyFurniture(Role role, ReqBuyFurniture buyFurniture) {
    this.houseService.reqBuyFurniture(role, buyFurniture.getSerial());
  }
  
  @RequestMapping
  public void reqHouseGoHome(Role role, ReqHouseGoHome reqHouseGoHome) {
    this.houseService.reqHouseGoHome(role, reqHouseGoHome.getType());
  }
  
  @RequestMapping
  public void reqHouseFriedVisit(Role role, ReqHouseFriedVisit reqHouseFriedVisit) {
    this.houseService.reqHouseFriedVisit(role, reqHouseFriedVisit.getId());
  }
}
