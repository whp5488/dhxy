
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
import java.util.Collection;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.init.service.InitService;
import java.util.function.Function;
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
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        WelfareDataPool.newDistrictMap = initService.initNewDistrict();
        WelfareDataPool.gradeAwardMap = initService.initGradeAward();
        WelfareDataPool.chargeMap = initService.initCharge();
        WelfareDataPool.exchangeMap = initService.initExchange();
        final List<SysDataChargeAward> chargeAwardList = initService.initChargeAward();
        WelfareDataPool.chargeAwardMap = chargeAwardList.stream().filter(e -> {
            final boolean b = e.getType() == 0;
            return b;
        }).collect(Collectors.toMap(SysDataChargeAward::getSerial, SysDataChargeAward -> SysDataChargeAward));
        WelfareDataPool.dayChargeAwardMap = chargeAwardList.stream().filter(e -> {
            final boolean b2 = e.getType() == 1;
            return b2;
        }).collect(Collectors.toMap(SysDataChargeAward::getSerial, SysDataChargeAward -> SysDataChargeAward));
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
    
    public static void init(final Map<String, String> map) {
        final String first = map.get("first_recharge");
        if (first != null) {
            try {
                final List<Param> list = JsonUtils.string2Collection(first, List.class, Param.class);
                WelfareDataPool.firstRechargeGift.addAll(list);
            }
            catch (final Exception e) {
                Logger.getLogger("首冲奖励初始化异常");
            }
        }
        final String creationRole = map.get("creation_role");
        if (creationRole != null) {
            try {
                WelfareDataPool.creationRoleMap.putAll((Map<? extends Integer, ? extends List<Param>>)JSON.parseObject(creationRole, (TypeReference)new TypeReference<HashMap<Integer, List<Param>>>() {}, new Feature[0]));
            }
            catch (final Exception e2) {
                Logger.getLogger("创建角色奖励初始化异常");
            }
        }
    }
    
    public static void loadGiftBag(final InitService initService) {
        WelfareDataPool.giftBagMap = initService.initGiftBag();
    }
    
    public static void initUpgrade(final InitService initService) {
        WelfareDataPool.data_upgrade = initService.initUpgrade();
    }
    
    public static void initLuckDraw(final InitService initService) {
        WelfareDataPool.luckDraw = initService.initLuckDraw();
    }
    
    public static void initLottery(final InitService initService) {
        WelfareDataPool.lottery = initService.initLottery();
    }
    
    public static void initLotterySuper(final InitService initService) {
        WelfareDataPool.lotterySuper = initService.initLotterySuper();
    }
    
    public static void initLotteryRare(final InitService initService) {
        WelfareDataPool.lotteryRare = initService.initLotteryRare();
    }
    
    public static void initMagicBox(final InitService initService) {
        final List<SysDataMagicBox> list = initService.initMagicBox();
        for (final SysDataMagicBox magicBox : list) {
            List<SysDataMagicBox> sysDataMagicBoxes = WelfareDataPool.magicBoxMap.get(magicBox.getAffiliation());
            if (sysDataMagicBoxes == null) {
                sysDataMagicBoxes = new ArrayList<SysDataMagicBox>();
            }
            sysDataMagicBoxes.add(magicBox);
            WelfareDataPool.magicBoxMap.put(magicBox.getAffiliation(), sysDataMagicBoxes);
        }
    }
    
    public static void initMagicBoxConsume() {
        final HashMap<Byte, Item> mapQl = new HashMap<Byte, Item>();
        mapQl.put((byte)0, new Item(90102, 2));
        mapQl.put((byte)1, new Item(90103, 1));
        WelfareDataPool.magicBoxConsumeMap.put(21067, mapQl);
        final HashMap<Byte, Item> mapNs = new HashMap<Byte, Item>();
        mapNs.put((byte)0, new Item(90096, 2));
        mapNs.put((byte)1, new Item(90097, 1));
        WelfareDataPool.magicBoxConsumeMap.put(21064, mapNs);
        final HashMap<Byte, Item> mapXw = new HashMap<Byte, Item>();
        mapXw.put((byte)0, new Item(90051, 2));
        mapXw.put((byte)1, new Item(90057, 1));
        WelfareDataPool.magicBoxConsumeMap.put(21069, mapXw);
        final HashMap<Byte, Item> mapHxy = new HashMap<Byte, Item>();
        mapHxy.put((byte)0, new Item(90049, 2));
        mapHxy.put((byte)1, new Item(90055, 1));
        WelfareDataPool.magicBoxConsumeMap.put(21053, mapHxy);
        final HashMap<Byte, Item> mapFs = new HashMap<Byte, Item>();
        mapFs.put((byte)0, new Item(90050, 2));
        mapFs.put((byte)1, new Item(90056, 1));
        WelfareDataPool.magicBoxConsumeMap.put(21059, mapFs);
        final HashMap<Byte, Item> mapNsg = new HashMap<Byte, Item>();
        mapNsg.put((byte)0, new Item(90046, 2));
        mapNsg.put((byte)1, new Item(90052, 1));
        WelfareDataPool.magicBoxConsumeMap.put(21013, mapNsg);
        final HashMap<Byte, Item> mapSnw = new HashMap<Byte, Item>();
        mapSnw.put((byte)0, new Item(90047, 2));
        mapSnw.put((byte)1, new Item(90053, 1));
        WelfareDataPool.magicBoxConsumeMap.put(21027, mapSnw);
        final HashMap<Byte, Item> mapFh = new HashMap<Byte, Item>();
        mapFh.put((byte)0, new Item(90048, 2));
        mapFh.put((byte)1, new Item(90054, 1));
        WelfareDataPool.magicBoxConsumeMap.put(21037, mapFh);
    }
    
    public static List<SysDataLotterySuper> createRandoms(final int n) {
        final Map<Integer, String> map = new HashMap<Integer, String>();
        final List<SysDataLotterySuper> news = new ArrayList<SysDataLotterySuper>();
        if (WelfareDataPool.lotterySuper.size() <= n) {
            return WelfareDataPool.lotterySuper;
        }
        while (map.size() < n) {
            final int random = (int)(Math.random() * WelfareDataPool.lotterySuper.size());
            if (!map.containsKey(random)) {
                map.put(random, "");
                news.add(WelfareDataPool.lotterySuper.get(random));
            }
        }
        return news;
    }
    
    public static List<SysDataLotteryRare> createRareRandoms(final int n) {
        final Map<Integer, String> map = new HashMap<Integer, String>();
        final List<SysDataLotteryRare> news = new ArrayList<SysDataLotteryRare>();
        if (WelfareDataPool.lotteryRare.size() <= n) {
            return WelfareDataPool.lotteryRare;
        }
        while (map.size() < n) {
            final int random = (int)(Math.random() * WelfareDataPool.lotteryRare.size());
            if (!map.containsKey(random)) {
                map.put(random, "-");
                news.add(WelfareDataPool.lotteryRare.get(random));
            }
        }
        return news;
    }
    
    public static int getLotterySuperEquip(int serial, final int race) {
        final FiedEquip info = EquipDistKind.getInfo(serial);
        if (info != null) {
            final int random = DataGlobalFunc.randomEquip(info.getType(), info.getValue(), race);
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
        WelfareDataPool.newDistrictMap = new HashMap<Byte, SysDataNewDistrictSign>();
        WelfareDataPool.gradeAwardMap = new HashMap<Integer, SysDataGradeAward>();
        WelfareDataPool.chargeAwardMap = new HashMap<Integer, SysDataChargeAward>();
        WelfareDataPool.dayChargeAwardMap = new HashMap<Integer, SysDataChargeAward>();
        WelfareDataPool.chargeMap = new HashMap<Integer, SysCharge>();
        WelfareDataPool.exchangeMap = new HashMap<Integer, SysDataExchange>();
        WelfareDataPool.prizeDrawMap = new ArrayList<SysDataPrizeDraw>();
        WelfareDataPool.magicBoxMap = new HashMap<Integer, List<SysDataMagicBox>>();
        WelfareDataPool.magicBoxConsumeMap = new HashMap<Integer, Map<Byte, Item>>();
        WelfareDataPool.giftBagMap = new HashMap<String, GiftBag>();
        WelfareDataPool.data_upgrade = new HashMap<Integer, SysUpgrade>();
        WelfareDataPool.luckDraw = new ArrayList<SysDataLuckDraw>();
        WelfareDataPool.lottery = new ArrayList<SysDataLottery>();
        WelfareDataPool.lotterySuper = new ArrayList<SysDataLotterySuper>();
        WelfareDataPool.lotteryRare = new ArrayList<SysDataLotteryRare>();
        WelfareDataPool.firstRechargeGift = new ArrayList<Param>();
        WelfareDataPool.creationRoleMap = new HashMap<Integer, List<Param>>();
    }
}
