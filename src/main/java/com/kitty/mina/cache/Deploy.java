package com.kitty.mina.cache;

import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.init.domain.vo.Param;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Deploy {
  public static int treasure = -1;
  
  public static int treasurePrice = 6888;
  
  public static boolean curfew = true;
  
  public static double curfewStartTime = 0.0D;
  
  public static double curfewEndTime = 8.0D;
  
  public static int speak = 30;
  
  public static int market = 98;
  
  public static boolean market_limit = false;
  
  public static int party = 328;
  
  public static int party_limit = 10;
  
  public static int five_guess = 648;
  
  public static int mobile = 1;
  
  public static int market_need_relive = 0;
  
  public static int market_need_level = 90;
  
  public static int five_guess_need_relive = 1;
  
  public static int five_guess_need_level = 110;
  
  public static List<Param> mobile_binding = new ArrayList<>();
  
  public static List<Integer> firstClaimPet = Arrays.asList(new Integer[] { Integer.valueOf(21057), Integer.valueOf(21058) });
  
  public static void load(Map<String, String> map) {
    String speak = map.get("speak");
    if (speak != null)
      Deploy.speak = Integer.valueOf(speak).intValue(); 
    String market = map.get("market");
    if (market != null)
      Deploy.market = Integer.valueOf(market).intValue(); 
    String party = map.get("party");
    if (party != null)
      Deploy.party = Integer.valueOf(party).intValue(); 
    String party_limit = map.get("party_limit");
    if (party_limit != null)
      Deploy.party_limit = Integer.valueOf(party_limit).intValue(); 
    String fiveGuess = map.get("five_guess");
    if (fiveGuess != null)
      five_guess = Integer.valueOf(fiveGuess).intValue(); 
    String mobile = map.get("mobile");
    if (mobile != null)
      Deploy.mobile = Integer.valueOf(mobile).intValue(); 
    String mobile_binding = map.get("mobile_binding");
    if (mobile_binding != null)
      try {
        Deploy.mobile_binding = (List<Param>)JsonUtils.string2Collection(mobile_binding, List.class, Param.class);
      } catch (Exception e) {
        Logger.getLogger("绑定有礼初始化异常");
      }  
    String curfew = map.get("curfew");
    if (curfew != null)
      try {
        Deploy.curfew = (Integer.valueOf(curfew).intValue() == 1);
      } catch (Exception e) {
        Logger.getLogger("发言限制-宵禁设置初始化异常");
      }  
    String curfewStartTime = map.get("curfewStartTime");
    if (curfewStartTime != null)
      try {
        Deploy.curfewStartTime = Double.valueOf(curfewStartTime).doubleValue();
      } catch (Exception e) {
        Logger.getLogger("发言限制-宵禁开始时间初始化异常");
      }  
    String curfewEndTime = map.get("curfewEndTime");
    if (curfewEndTime != null)
      try {
        Deploy.curfewEndTime = Double.valueOf(curfewEndTime).doubleValue();
      } catch (Exception e) {
        Logger.getLogger("发言限制-宵禁结束时间初始化异常");
      }  
    String firstClaimPet = map.get("firstClaimPet");
    if (mobile_binding != null)
      try {
        String[] split = firstClaimPet.split(",");
        if (split.length > 1) {
          List<Integer> list = new ArrayList<>(2);
          for (String key : split) {
            if (NumberUtil.isNumber(key.trim()))
              list.add(Integer.valueOf(key.trim())); 
          } 
          Deploy.firstClaimPet = list;
        } 
      } catch (Exception e) {
        Logger.getLogger("新手领取宠物初始化异常");
      }  
  }
}
