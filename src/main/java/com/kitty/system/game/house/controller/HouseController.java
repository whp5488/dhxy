
package com.kitty.system.game.house.controller;

import com.kitty.system.game.house.message.ReqHouseFriedVisit;
import com.kitty.system.game.house.message.ReqHouseGoHome;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.house.message.ReqBuyFurniture;
import com.kitty.system.game.role.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.house.service.HouseService;
import org.springframework.stereotype.Controller;

@Controller
public class HouseController
{
    @Autowired
    protected HouseService houseService;
    
    @RequestMapping
    public void reqBuyFurniture(final Role role, final ReqBuyFurniture buyFurniture) {
        this.houseService.reqBuyFurniture(role, buyFurniture.getSerial());
    }
    
    @RequestMapping
    public void reqHouseGoHome(final Role role, final ReqHouseGoHome reqHouseGoHome) {
        this.houseService.reqHouseGoHome(role, reqHouseGoHome.getType());
    }
    
    @RequestMapping
    public void reqHouseFriedVisit(final Role role, final ReqHouseFriedVisit reqHouseFriedVisit) {
        this.houseService.reqHouseFriedVisit(role, reqHouseFriedVisit.getId());
    }
}
