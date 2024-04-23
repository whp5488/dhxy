package com.kitty.system.game.bag.helper;

import com.kitty.cache.RedisService;
import com.kitty.common.bean.Lottery;
import com.kitty.common.model.Pos;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.bag.message.RespPotholing;
import com.kitty.system.game.bag.message.vo.PotholingVo;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.init.domain.SysDataLotteryRare;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("RarePotholing")
public class RarePotholingHandler extends BagHandler {
  private static final int SERIAL = 90061;
  
  public String acceptOperate(Role role, Integer pos, Integer num, String param) {
    String msg = DataGlobalFunc.needGradeJudge(0, 60, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
    if (msg != null)
      return msg; 
    Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
    BagGoods goods = bagGoodsMap.values().stream().filter(item -> pos.equals(Integer.valueOf(item.getPos()))).findFirst().orElse(null);
    if (goods == null)
      return null; 
    if (goods.getSerial() != 90061)
      return null; 
    Pos rolePos = role.getPos();
    for (String key : goods.getSpecial().keySet()) {
      if (key.equals("map")) {
        int map = goods.getSpecial().getInt(key);
        if (!rolePos.getMapId().equals(Integer.valueOf(map))) {
          msg = "当前位置没有宝物哦，请移动到指定位置挖宝";
          break;
        } 
      } 
    } 
    if (msg == null) {
      RedisService redisService = (RedisService)SpringUtils.getBean(RedisService.class);
      RespPotholing respPotholing = (RespPotholing)redisService.get(role.getId() + "rare_potholing", RespPotholing.class);
      if (respPotholing != null) {
        role.pushTempCache("rare_potholing", redisService.get(role.getId() + "rare_potholing_data", String.class));
        MessagePusher.pushMessage(role, (Message)respPotholing);
        return null;
      } 
      List<SysDataLotteryRare> randoms = WelfareDataPool.createRareRandoms(15);
      List<Lottery> list = new ArrayList<>();
      randoms.forEach(itemOpen -> list.add(new Lottery(Integer.valueOf(itemOpen.getId()), "", Double.valueOf(itemOpen.getRate()))));
      List<Double> orignalRates = new ArrayList<>(list.size());
      for (Lottery lottery1 : list)
        orignalRates.add(lottery1.getBili()); 
      int index = LotteryUtils.lottery(orignalRates);
      Lottery lottery = list.get(index);
      int site = 0;
      List<PotholingVo> objects = new ArrayList<>();
      for (int i = 0; i < randoms.size(); i++) {
        SysDataLotteryRare obj = randoms.get(i);
        int serial = WelfareDataPool.getLotterySuperEquip(obj.getSerial(), role.getRace().intValue());
        if (lottery.getSerial().equals(Integer.valueOf(obj.getId()))) {
          role.pushTempCache("rare_potholing", serial + "-" + obj.getType() + "-" + obj.getNum() + "-" + obj.getIsNotice());
          site = i + 1;
        } 
        objects.add(new PotholingVo(Integer.valueOf(serial), Integer.valueOf(obj.getNum())));
      } 
      RespPotholing potholing = new RespPotholing(objects, site, String.valueOf(goods.getId()));
      MessagePusher.pushMessage(role, (Message)potholing);
      redisService.set(role.getId() + "rare_potholing", (Serializable)potholing, 180L);
      redisService.set(role.getId() + "rare_potholing_data", (Serializable)role.getTempCache("rare_potholing", null), 180L);
    } 
    return msg;
  }
}
