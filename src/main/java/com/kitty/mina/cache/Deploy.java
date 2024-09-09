package com.kitty.mina.cache;

import java.util.Arrays;
import com.kitty.common.utils.NumberUtil;
import java.util.ArrayList;
import java.util.logging.Logger;
import com.kitty.common.utils.JsonUtils;
import java.util.Map;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;

public class Deploy
{
    public static int treasure;
    public static int treasurePrice;
    public static boolean curfew;
    public static double curfewStartTime;
    public static double curfewEndTime;
    public static int speak;
    public static int market;
    public static boolean market_limit;
    public static int party;
    public static int party_limit;
    public static int five_guess;
    public static int mobile;
    public static int market_need_relive;
    public static int market_need_level;
    public static int five_guess_need_relive;
    public static int five_guess_need_level;
    public static List<Param> mobile_binding;
    public static List<Integer> firstClaimPet;
    
    public Deploy() {
    }
    
    public static void load(Map<String, String> map) {
        String speak = (String)map.get("speak");
        if (speak != null) {
            Deploy.speak = (int)Integer.valueOf(speak);
        }
        String market = (String)map.get("market");
        if (market != null) {
            Deploy.market = (int)Integer.valueOf(market);
        }
        String party = (String)map.get("party");
        if (party != null) {
            Deploy.party = (int)Integer.valueOf(party);
        }
        String party_limit = (String)map.get("party_limit");
        if (party_limit != null) {
            Deploy.party_limit = (int)Integer.valueOf(party_limit);
        }
        String fiveGuess = (String)map.get("five_guess");
        if (fiveGuess != null) {
            Deploy.five_guess = (int)Integer.valueOf(fiveGuess);
        }
        String mobile = (String)map.get("mobile");
        if (mobile != null) {
            Deploy.mobile = (int)Integer.valueOf(mobile);
        }
        String mobile_binding = (String)map.get("mobile_binding");
        if (mobile_binding != null) {
            try {
                Deploy.mobile_binding = (List)JsonUtils.string2Collection(mobile_binding, List.class, Param.class);
            }
            catch (Exception e) {
                Logger.getLogger("绑定有礼初始化异常");
            }
        }
        String curfew = (String)map.get("curfew");
        if (curfew != null) {
            try {
                Deploy.curfew = ((int)Integer.valueOf(curfew) == 1);
            }
            catch (Exception e2) {
                Logger.getLogger("发言限制-宵禁设置初始化异常");
            }
        }
        String curfewStartTime = (String)map.get("curfewStartTime");
        if (curfewStartTime != null) {
            try {
                Deploy.curfewStartTime = (double)Double.valueOf(curfewStartTime);
            }
            catch (Exception e3) {
                Logger.getLogger("发言限制-宵禁开始时间初始化异常");
            }
        }
        String curfewEndTime = (String)map.get("curfewEndTime");
        if (curfewEndTime != null) {
            try {
                Deploy.curfewEndTime = (double)Double.valueOf(curfewEndTime);
            }
            catch (Exception e4) {
                Logger.getLogger("发言限制-宵禁结束时间初始化异常");
            }
        }
        String firstClaimPet = (String)map.get("firstClaimPet");
        if (mobile_binding != null) {
            try {
                String[] split = firstClaimPet.split(",");
                if (split.length > 1) {
                    List<Integer> list = new ArrayList(2);
                    for (String key : split) {
                        if (NumberUtil.isNumber(key.trim())) {
                            list.add(Integer.valueOf(key.trim()));
                        }
                    }
                    Deploy.firstClaimPet = list;
                }
            }
            catch (Exception e5) {
                Logger.getLogger("新手领取宠物初始化异常");
            }
        }
    }
    
    static {
        Deploy.treasure = -1;
        Deploy.treasurePrice = 6888;
        Deploy.curfew = true;
        Deploy.curfewStartTime = 0.0;
        Deploy.curfewEndTime = 8.0;
        Deploy.speak = 30;
        Deploy.market = 98;
        Deploy.market_limit = false;
        Deploy.party = 328;
        Deploy.party_limit = 10;
        Deploy.five_guess = 648;
        Deploy.mobile = 1;
        Deploy.market_need_relive = 0;
        Deploy.market_need_level = 0;
        Deploy.five_guess_need_relive = 1;
        Deploy.five_guess_need_level = 110;
        Deploy.mobile_binding = new ArrayList<>();
        Deploy.firstClaimPet = Arrays.asList(Integer.valueOf(21057), Integer.valueOf(21058));
    }
}
