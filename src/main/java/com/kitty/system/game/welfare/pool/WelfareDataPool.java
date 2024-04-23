package com.kitty.system.game.welfare.pool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.equip.EquipDistKind;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.game.welfare.domain.GiftBag;
import com.kitty.system.init.domain.SysCharge;
import com.kitty.system.init.domain.SysDataChargeAward;
import com.kitty.system.init.domain.SysDataExchange;
import com.kitty.system.init.domain.SysDataGradeAward;
import com.kitty.system.init.domain.SysDataLottery;
import com.kitty.system.init.domain.SysDataLotteryRare;
import com.kitty.system.init.domain.SysDataLotterySuper;
import com.kitty.system.init.domain.SysDataLuckDraw;
import com.kitty.system.init.domain.SysDataMagicBox;
import com.kitty.system.init.domain.SysDataNewDistrictSign;
import com.kitty.system.init.domain.SysDataPrizeDraw;
import com.kitty.system.init.domain.SysUpgrade;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class WelfareDataPool {
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    newDistrictMap = initService.initNewDistrict();
    gradeAwardMap = initService.initGradeAward();
    chargeMap = initService.initCharge();
    exchangeMap = initService.initExchange();
    List<SysDataChargeAward> chargeAwardList = initService.initChargeAward();
    chargeAwardMap = (Map<Integer, SysDataChargeAward>)chargeAwardList.stream().filter(e -> {
          boolean b;
          if (e.getType() == 0) {
            b = true;
          } else {
            b = false;
          } 
          return b;
        }).collect(Collectors.toMap(SysDataChargeAward::getSerial, sysDataChargeAward -> sysDataChargeAward));
    dayChargeAwardMap = (Map<Integer, SysDataChargeAward>)chargeAwardList.stream().filter(e -> {
          boolean b2;
          if (e.getType() == 1) {
            b2 = true;
          } else {
            b2 = false;
          } 
          return b2;
        }).collect(Collectors.toMap(SysDataChargeAward::getSerial, sysDataChargeAward -> sysDataChargeAward));
    prizeDrawMap = initService.initPrizeDraw();
    loadGiftBag(initService);
    initUpgrade(initService);
    initLottery(initService);
    initLuckDraw(initService);
    initLotterySuper(initService);
    initLotteryRare(initService);
    initMagicBox(initService);
    initMagicBoxConsume();
  }
  
  public static void init(Map<String, String> map) {
    String first = map.get("first_recharge");
    if (first != null)
      try {
        List<Param> list = (List<Param>)JsonUtils.string2Collection(first, List.class, Param.class);
        firstRechargeGift.addAll(list);
      } catch (Exception e) {
        Logger.getLogger("首冲奖励初始化异常");
      }  
    String creationRole = map.get("creation_role");
    if (creationRole != null)
      try {
        creationRoleMap.putAll((Map<? extends Integer, ? extends List<Param>>)JSON.parseObject(creationRole, new TypeReference<HashMap<Integer, List<Param>>>() {
              
              },  new com.alibaba.fastjson.parser.Feature[0]));
      } catch (Exception e2) {
        Logger.getLogger("创建角色奖励初始化异常");
      }  
  }
  
  public static void loadGiftBag(InitService initService) {
    giftBagMap = initService.initGiftBag();
  }
  
  public static void initUpgrade(InitService initService) {
    data_upgrade = initService.initUpgrade();
  }
  
  public static void initLuckDraw(InitService initService) {
    luckDraw = initService.initLuckDraw();
  }
  
  public static void initLottery(InitService initService) {
    lottery = initService.initLottery();
  }
  
  public static void initLotterySuper(InitService initService) {
    lotterySuper = initService.initLotterySuper();
  }
  
  public static void initLotteryRare(InitService initService) {
    lotteryRare = initService.initLotteryRare();
  }
  
  public static void initMagicBox(InitService initService) {
    List<SysDataMagicBox> list = initService.initMagicBox();
    for (SysDataMagicBox magicBox : list) {
      List<SysDataMagicBox> sysDataMagicBoxes = magicBoxMap.get(Integer.valueOf(magicBox.getAffiliation()));
      if (sysDataMagicBoxes == null)
        sysDataMagicBoxes = new ArrayList<>(); 
      sysDataMagicBoxes.add(magicBox);
      magicBoxMap.put(Integer.valueOf(magicBox.getAffiliation()), sysDataMagicBoxes);
    } 
  }
  
  public static void initMagicBoxConsume() {
    HashMap<Byte, Item> mapQl = new HashMap<>();
    mapQl.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90102), Integer.valueOf(2)));
    mapQl.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90103), Integer.valueOf(1)));
    magicBoxConsumeMap.put(Integer.valueOf(21067), mapQl);
    HashMap<Byte, Item> mapNs = new HashMap<>();
    mapNs.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90096), Integer.valueOf(2)));
    mapNs.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90097), Integer.valueOf(1)));
    magicBoxConsumeMap.put(Integer.valueOf(21064), mapNs);
    HashMap<Byte, Item> mapXw = new HashMap<>();
    mapXw.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90051), Integer.valueOf(2)));
    mapXw.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90057), Integer.valueOf(1)));
    magicBoxConsumeMap.put(Integer.valueOf(21069), mapXw);
    HashMap<Byte, Item> mapHxy = new HashMap<>();
    mapHxy.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90049), Integer.valueOf(2)));
    mapHxy.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90055), Integer.valueOf(1)));
    magicBoxConsumeMap.put(Integer.valueOf(21053), mapHxy);
    HashMap<Byte, Item> mapFs = new HashMap<>();
    mapFs.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90050), Integer.valueOf(2)));
    mapFs.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90056), Integer.valueOf(1)));
    magicBoxConsumeMap.put(Integer.valueOf(21059), mapFs);
    HashMap<Byte, Item> mapNsg = new HashMap<>();
    mapNsg.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90046), Integer.valueOf(2)));
    mapNsg.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90052), Integer.valueOf(1)));
    magicBoxConsumeMap.put(Integer.valueOf(21013), mapNsg);
    HashMap<Byte, Item> mapSnw = new HashMap<>();
    mapSnw.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90047), Integer.valueOf(2)));
    mapSnw.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90053), Integer.valueOf(1)));
    magicBoxConsumeMap.put(Integer.valueOf(21027), mapSnw);
    HashMap<Byte, Item> mapFh = new HashMap<>();
    mapFh.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90048), Integer.valueOf(2)));
    mapFh.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90054), Integer.valueOf(1)));
    magicBoxConsumeMap.put(Integer.valueOf(21037), mapFh);
  }
  
  public static List<SysDataLotterySuper> createRandoms(int n) {
    Map<Integer, String> map = new HashMap<>();
    List<SysDataLotterySuper> news = new ArrayList<>();
    if (lotterySuper.size() <= n)
      return lotterySuper; 
    while (map.size() < n) {
      int random = (int)(Math.random() * lotterySuper.size());
      if (!map.containsKey(Integer.valueOf(random))) {
        map.put(Integer.valueOf(random), "");
        news.add(lotterySuper.get(random));
      } 
    } 
    return news;
  }
  
  public static List<SysDataLotteryRare> createRareRandoms(int n) {
    Map<Integer, String> map = new HashMap<>();
    List<SysDataLotteryRare> news = new ArrayList<>();
    if (lotteryRare.size() <= n)
      return lotteryRare; 
    while (map.size() < n) {
      int random = (int)(Math.random() * lotteryRare.size());
      if (!map.containsKey(Integer.valueOf(random))) {
        map.put(Integer.valueOf(random), "-");
        news.add(lotteryRare.get(random));
      } 
    } 
    return news;
  }
  
  public static int getLotterySuperEquip(int serial, int race) {
    FiedEquip info = EquipDistKind.getInfo(Integer.valueOf(serial));
    if (info != null) {
      int random = DataGlobalFunc.randomEquip(info.getType().intValue(), info.getValue().intValue(), race);
      if (random > 0)
        serial = random; 
    } 
    return serial;
  }
  
  public static short FIVE_GUESS_COUNT = 80;
  
  public static int FIVE_GUESS_INNER_PROB = 20;
  
  public static int FIVE_GUESS_OUTER_PROB = 80;
  
  public static String[] FIVE_GUESS_INNER_NAME = "金、木、水、火、土".split("、");
  
  public static String[] FIVE_GUESS_OUTER_NAME = "永、朱雀、年、白泽、画中仙、青龙、白虎、玄武、浪淘沙、颜如玉、范式之魂、垂云叟".split("、");
  
  public static int prizeDrawTop = 200;
  
  public static long ONCE_PRIZE_DRAW = 5000L;
  
  public static long FIVE_PRIZE_DRAW = 22500L;
  
  public static long ONCE_LUCK_DRAW = 1L;
  
  public static long TEN_LUCK_DRAW = 10L;
  
  public static Map<Byte, SysDataNewDistrictSign> newDistrictMap = new HashMap<>();
  
  public static Map<Integer, SysDataGradeAward> gradeAwardMap = new HashMap<>();
  
  public static Map<Integer, SysDataChargeAward> chargeAwardMap = new HashMap<>();
  
  public static Map<Integer, SysDataChargeAward> dayChargeAwardMap = new HashMap<>();
  
  public static Map<Integer, SysCharge> chargeMap = new HashMap<>();
  
  public static Map<Integer, SysDataExchange> exchangeMap = new HashMap<>();
  
  public static List<SysDataPrizeDraw> prizeDrawMap = new ArrayList<>();
  
  public static Map<Integer, List<SysDataMagicBox>> magicBoxMap = new HashMap<>();
  
  public static Map<Integer, Map<Byte, Item>> magicBoxConsumeMap = new HashMap<>();
  
  public static Map<String, GiftBag> giftBagMap = new HashMap<>();
  
  public static Map<Integer, SysUpgrade> data_upgrade = new HashMap<>();
  
  public static List<SysDataLuckDraw> luckDraw = new ArrayList<>();
  
  public static List<SysDataLottery> lottery = new ArrayList<>();
  
  public static List<SysDataLotterySuper> lotterySuper = new ArrayList<>();
  
  public static List<SysDataLotteryRare> lotteryRare = new ArrayList<>();
  
  public static List<Param> firstRechargeGift = new ArrayList<>();
  
  public static Map<Integer, List<Param>> creationRoleMap = new HashMap<>();
}
