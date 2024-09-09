package com.kitty.system.game.welfare.pool;

import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.equip.EquipDistKind;
import java.util.Iterator;
import java.util.ArrayList;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import java.util.HashMap;
import com.alibaba.fastjson.TypeReference;
import java.util.logging.Logger;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.init.service.InitService;
import java.util.stream.Collectors;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.init.domain.SysDataLotteryRare;
import com.kitty.system.init.domain.SysDataLotterySuper;
import com.kitty.system.init.domain.SysDataLottery;
import com.kitty.system.init.domain.SysDataLuckDraw;
import com.kitty.system.init.domain.SysUpgrade;
import com.kitty.system.game.welfare.domain.GiftBag;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.init.domain.SysDataMagicBox;
import com.kitty.system.init.domain.SysDataPrizeDraw;
import java.util.List;
import com.kitty.system.init.domain.SysDataExchange;
import com.kitty.system.init.domain.SysCharge;
import com.kitty.system.init.domain.SysDataChargeAward;
import com.kitty.system.init.domain.SysDataGradeAward;
import com.kitty.system.init.domain.SysDataNewDistrictSign;
import java.util.Map;

public class WelfareDataPool
{
    public static short FIVE_GUESS_COUNT;
    public static int FIVE_GUESS_INNER_PROB;
    public static int FIVE_GUESS_OUTER_PROB;
    public static String[] FIVE_GUESS_INNER_NAME;
    public static String[] FIVE_GUESS_OUTER_NAME;
    public static int prizeDrawTop;
    public static long ONCE_PRIZE_DRAW;
    public static long FIVE_PRIZE_DRAW;
    public static long ONCE_LUCK_DRAW;
    public static long TEN_LUCK_DRAW;
    public static Map<Byte, SysDataNewDistrictSign> newDistrictMap;
    public static Map<Integer, SysDataGradeAward> gradeAwardMap;
    public static Map<Integer, SysDataChargeAward> chargeAwardMap;
    public static Map<Integer, SysDataChargeAward> dayChargeAwardMap;
    public static Map<Integer, SysCharge> chargeMap;
    public static Map<Integer, SysDataExchange> exchangeMap;
    public static List<SysDataPrizeDraw> prizeDrawMap;
    public static Map<Integer, List<SysDataMagicBox>> magicBoxMap;
    public static Map<Integer, Map<Byte, Item>> magicBoxConsumeMap;
    public static Map<String, GiftBag> giftBagMap;
    public static Map<Integer, SysUpgrade> data_upgrade;
    public static List<SysDataLuckDraw> luckDraw;
    public static List<SysDataLottery> lottery;
    public static List<SysDataLotterySuper> lotterySuper;
    public static List<SysDataLotteryRare> lotteryRare;
    public static List<Param> firstRechargeGift;
    public static Map<Integer, List<Param>> creationRoleMap;
    
    public WelfareDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        WelfareDataPool.newDistrictMap = initService.initNewDistrict();
        WelfareDataPool.gradeAwardMap = initService.initGradeAward();
        WelfareDataPool.chargeMap = initService.initCharge();
        WelfareDataPool.exchangeMap = initService.initExchange();
        List<SysDataChargeAward> chargeAwardList = initService.initChargeAward();
        WelfareDataPool.chargeAwardMap = (Map)chargeAwardList.stream().filter(e -> e.getType() == 0).collect(Collectors.toMap(SysDataChargeAward::getSerial, SysDataChargeAward -> SysDataChargeAward));
        WelfareDataPool.dayChargeAwardMap = (Map)chargeAwardList.stream().filter(e -> e.getType() == 1).collect(Collectors.toMap(SysDataChargeAward::getSerial, SysDataChargeAward -> SysDataChargeAward));
        WelfareDataPool.prizeDrawMap = initService.initPrizeDraw();
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
        String first = (String)map.get("first_recharge");
        if (first != null) {
            try {
                List<Param> list = (List)JsonUtils.string2Collection(first, List.class, Param.class);
                WelfareDataPool.firstRechargeGift.addAll(list);
            }
            catch (Exception e) {
                Logger.getLogger("首冲奖励初始化异常");
            }
        }
        String creationRole = (String)map.get("creation_role");
        if (creationRole != null) {
            try {
                WelfareDataPool.creationRoleMap.putAll((Map)JSON.parseObject(creationRole, new TypeReference<HashMap<Integer, List<Param>>>() {}, new Feature[0]));
            }
            catch (Exception e2) {
                Logger.getLogger("创建角色奖励初始化异常");
            }
        }
    }
    
    public static void loadGiftBag(InitService initService) {
        WelfareDataPool.giftBagMap = initService.initGiftBag();
    }
    
    public static void initUpgrade(InitService initService) {
        WelfareDataPool.data_upgrade = initService.initUpgrade();
    }
    
    public static void initLuckDraw(InitService initService) {
        WelfareDataPool.luckDraw = initService.initLuckDraw();
    }
    
    public static void initLottery(InitService initService) {
        WelfareDataPool.lottery = initService.initLottery();
    }
    
    public static void initLotterySuper(InitService initService) {
        WelfareDataPool.lotterySuper = initService.initLotterySuper();
    }
    
    public static void initLotteryRare(InitService initService) {
        WelfareDataPool.lotteryRare = initService.initLotteryRare();
    }
    
    public static void initMagicBox(InitService initService) {
        List<SysDataMagicBox> list = initService.initMagicBox();
        for (SysDataMagicBox magicBox : list) {
            List<SysDataMagicBox> sysDataMagicBoxes = (List)WelfareDataPool.magicBoxMap.get(Integer.valueOf(magicBox.getAffiliation()));
            if (sysDataMagicBoxes == null) {
                sysDataMagicBoxes = new ArrayList<>();
            }
            sysDataMagicBoxes.add(magicBox);
            WelfareDataPool.magicBoxMap.put(Integer.valueOf(magicBox.getAffiliation()), sysDataMagicBoxes);
        }
    }
    
    public static void initMagicBoxConsume() {
        HashMap<Byte, Item> mapXw = new HashMap();
        mapXw.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90051), Integer.valueOf(2)));
        mapXw.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90057), Integer.valueOf(1)));
        WelfareDataPool.magicBoxConsumeMap.put(Integer.valueOf(21069), mapXw);
        HashMap<Byte, Item> mapHxy = new HashMap();
        mapHxy.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90049), Integer.valueOf(2)));
        mapHxy.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90055), Integer.valueOf(1)));
        WelfareDataPool.magicBoxConsumeMap.put(Integer.valueOf(21053), mapHxy);
        HashMap<Byte, Item> mapFs = new HashMap();
        mapFs.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90050), Integer.valueOf(2)));
        mapFs.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90056), Integer.valueOf(1)));
        WelfareDataPool.magicBoxConsumeMap.put(Integer.valueOf(21059), mapFs);
        HashMap<Byte, Item> mapNsg = new HashMap();
        mapNsg.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90046), Integer.valueOf(2)));
        mapNsg.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90052), Integer.valueOf(1)));
        WelfareDataPool.magicBoxConsumeMap.put(Integer.valueOf(21013), mapNsg);
        HashMap<Byte, Item> mapSnw = new HashMap();
        mapSnw.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90047), Integer.valueOf(2)));
        mapSnw.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90053), Integer.valueOf(1)));
        WelfareDataPool.magicBoxConsumeMap.put(Integer.valueOf(21027), mapSnw);
        HashMap<Byte, Item> mapFh = new HashMap();
        mapFh.put(Byte.valueOf((byte)0), new Item(Integer.valueOf(90048), Integer.valueOf(2)));
        mapFh.put(Byte.valueOf((byte)1), new Item(Integer.valueOf(90054), Integer.valueOf(1)));
        WelfareDataPool.magicBoxConsumeMap.put(Integer.valueOf(21037), mapFh);
    }
    
    public static List<SysDataLotterySuper> createRandoms(int n) {
        Map<Integer, String> map = new HashMap();
        List<SysDataLotterySuper> news = new ArrayList<>();
        if (WelfareDataPool.lotterySuper.size() <= n) {
            return WelfareDataPool.lotterySuper;
        }
        while (map.size() < n) {
            int random = (int)(Math.random() * (double)WelfareDataPool.lotterySuper.size());
            if (!map.containsKey(Integer.valueOf(random))) {
                map.put(Integer.valueOf(random), "");
                news.add(WelfareDataPool.lotterySuper.get(random));
            }
            else {
                continue;
            }
        }
        return news;
    }
    
    public static List<SysDataLotteryRare> createRareRandoms(int n) {
        Map<Integer, String> map = new HashMap();
        List<SysDataLotteryRare> news = new ArrayList<>();
        if (WelfareDataPool.lotteryRare.size() <= n) {
            return WelfareDataPool.lotteryRare;
        }
        while (map.size() < n) {
            int random = (int)(Math.random() * (double)WelfareDataPool.lotteryRare.size());
            if (!map.containsKey(Integer.valueOf(random))) {
                map.put(Integer.valueOf(random), "-");
                news.add(WelfareDataPool.lotteryRare.get(random));
            }
            else {
                continue;
            }
        }
        return news;
    }
    
    public static int getLotterySuperEquip(int serial, int race) {
        FiedEquip info = EquipDistKind.getInfo(Integer.valueOf(serial));
        if (info != null) {
            int random = DataGlobalFunc.randomEquip((int)info.getType(), (int)info.getValue(), race);
            if (random > 0) {
                serial = random;
            }
        }
        return serial;
    }
    
    static {
        WelfareDataPool.FIVE_GUESS_COUNT = 80;
        WelfareDataPool.FIVE_GUESS_INNER_PROB = 20;
        WelfareDataPool.FIVE_GUESS_OUTER_PROB = 80;
        WelfareDataPool.FIVE_GUESS_INNER_NAME = "金、木、水、火、土".split("、");
        WelfareDataPool.FIVE_GUESS_OUTER_NAME = "永、朱雀、年、白泽、画中仙、青龙、白虎、玄武、浪淘沙、颜如玉、范式之魂、垂云叟".split("、");
        WelfareDataPool.prizeDrawTop = 200;
        WelfareDataPool.ONCE_PRIZE_DRAW = 5000L;
        WelfareDataPool.FIVE_PRIZE_DRAW = 22500L;
        WelfareDataPool.ONCE_LUCK_DRAW = 1L;
        WelfareDataPool.TEN_LUCK_DRAW = 10L;
        WelfareDataPool.newDistrictMap = new HashMap();
        WelfareDataPool.gradeAwardMap = new HashMap();
        WelfareDataPool.chargeAwardMap = new HashMap();
        WelfareDataPool.dayChargeAwardMap = new HashMap();
        WelfareDataPool.chargeMap = new HashMap();
        WelfareDataPool.exchangeMap = new HashMap();
        WelfareDataPool.prizeDrawMap = new ArrayList<>();
        WelfareDataPool.magicBoxMap = new HashMap();
        WelfareDataPool.magicBoxConsumeMap = new HashMap();
        WelfareDataPool.giftBagMap = new HashMap();
        WelfareDataPool.data_upgrade = new HashMap();
        WelfareDataPool.luckDraw = new ArrayList<>();
        WelfareDataPool.lottery = new ArrayList<>();
        WelfareDataPool.lotterySuper = new ArrayList<>();
        WelfareDataPool.lotteryRare = new ArrayList<>();
        WelfareDataPool.firstRechargeGift = new ArrayList<>();
        WelfareDataPool.creationRoleMap = new HashMap();
    }
}
