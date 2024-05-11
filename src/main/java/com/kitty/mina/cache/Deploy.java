
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
    
    public static void load(final Map<String, String> map) {
        final String speak = map.get("speak");
        if (speak != null) {
            Deploy.speak = Integer.valueOf(speak);
        }
        final String market = map.get("market");
        if (market != null) {
            Deploy.market = Integer.valueOf(market);
        }
        final String party = map.get("party");
        if (party != null) {
            Deploy.party = Integer.valueOf(party);
        }
        final String party_limit = map.get("party_limit");
        if (party_limit != null) {
            Deploy.party_limit = Integer.valueOf(party_limit);
        }
        final String fiveGuess = map.get("five_guess");
        if (fiveGuess != null) {
            Deploy.five_guess = Integer.valueOf(fiveGuess);
        }
        final String mobile = map.get("mobile");
        if (mobile != null) {
            Deploy.mobile = Integer.valueOf(mobile);
        }
        final String mobile_binding = map.get("mobile_binding");
        if (mobile_binding != null) {
            try {
                Deploy.mobile_binding = JsonUtils.string2Collection(mobile_binding, List.class, Param.class);
            }
            catch (final Exception e) {
                Logger.getLogger("绑定有礼初始化异常");
            }
        }
        final String curfew = map.get("curfew");
        if (curfew != null) {
            try {
                Deploy.curfew = (Integer.valueOf(curfew) == 1);
            }
            catch (final Exception e2) {
                Logger.getLogger("发言限制-宵禁设置初始化异常");
            }
        }
        final String curfewStartTime = map.get("curfewStartTime");
        if (curfewStartTime != null) {
            try {
                Deploy.curfewStartTime = Double.valueOf(curfewStartTime);
            }
            catch (final Exception e3) {
                Logger.getLogger("发言限制-宵禁开始时间初始化异常");
            }
        }
        final String curfewEndTime = map.get("curfewEndTime");
        if (curfewEndTime != null) {
            try {
                Deploy.curfewEndTime = Double.valueOf(curfewEndTime);
            }
            catch (final Exception e4) {
                Logger.getLogger("发言限制-宵禁结束时间初始化异常");
            }
        }
        final String firstClaimPet = map.get("firstClaimPet");
        if (mobile_binding != null) {
            try {
                final String[] split = firstClaimPet.split(",");
                if (split.length > 1) {
                    final List<Integer> list = new ArrayList<Integer>(2);
                    for (final String key : split) {
                        if (NumberUtil.isNumber(key.trim())) {
                            list.add(Integer.valueOf(key.trim()));
                        }
                    }
                    Deploy.firstClaimPet = list;
                }
            }
            catch (final Exception e5) {
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
        Deploy.market_need_level = 90;
        Deploy.five_guess_need_relive = 1;
        Deploy.five_guess_need_level = 110;
        Deploy.mobile_binding = new ArrayList<Param>();
        Deploy.firstClaimPet = Arrays.asList(21057, 21058);
    }
}
