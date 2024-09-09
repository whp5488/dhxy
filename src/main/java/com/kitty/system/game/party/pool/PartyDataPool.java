package com.kitty.system.game.party.pool;

import com.kitty.system.game.map.pool.MapDataPool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysPartyFlag;
import java.util.List;
import com.kitty.system.game.party.model.Party;
import java.util.Map;

public class PartyDataPool
{
    public static Map<Integer, Party> partyMap;
    public static Map<String, Integer> partyName2Id;
    public static Map<String, Byte> partyPermissions;
    public static Map<Byte, Byte> partyCareer;
    public static Map<Byte, String> jobId2Names;
    public static Map<Byte, Integer> jobId2TitleSerial;
    public static List<SysPartyFlag> partyFlagList;
    public static Map<Integer, Integer> offlineRoleParty;
    public static Map<Byte, Integer> partyUpgrades;
    public static Map<Byte, Integer> partyUpgradesSilver;
    
    public PartyDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        PartyDataPool.partyFlagList = initService.initPartyFlag();
    }
    
    public static int getScene(byte level) {
        int scene = 3002;
        if (level >= 5) {
            scene = 3022;
        }
        else if (level < 5 && level > 3) {
            scene = 3021;
        }
        return scene;
    }
    
    static {
        PartyDataPool.partyMap = new ConcurrentHashMap();
        PartyDataPool.partyName2Id = new ConcurrentHashMap();
        PartyDataPool.partyPermissions = new HashMap();
        PartyDataPool.partyCareer = new HashMap();
        PartyDataPool.jobId2Names = new HashMap();
        PartyDataPool.jobId2TitleSerial = new HashMap();
        PartyDataPool.partyFlagList = new ArrayList<>();
        PartyDataPool.offlineRoleParty = new ConcurrentHashMap();
        PartyDataPool.partyUpgrades = new HashMap();
        PartyDataPool.partyUpgradesSilver = new HashMap();
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3002), role -> role.getFaction());
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3021), role -> role.getFaction());
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3022), role -> role.getFaction());
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3001), role -> Integer.valueOf(role.getVisible()));
        PartyDataPool.partyPermissions.put("帮主", Byte.valueOf((byte)1));
        PartyDataPool.partyPermissions.put("副帮主", Byte.valueOf((byte)2));
        PartyDataPool.partyPermissions.put("左护法", Byte.valueOf((byte)3));
        PartyDataPool.partyPermissions.put("右护法", Byte.valueOf((byte)4));
        PartyDataPool.partyPermissions.put("长老", Byte.valueOf((byte)5));
        PartyDataPool.partyPermissions.put("堂主", Byte.valueOf((byte)6));
        PartyDataPool.partyPermissions.put("团长", Byte.valueOf((byte)7));
        PartyDataPool.partyPermissions.put("帮众", Byte.valueOf((byte)8));
        PartyDataPool.partyCareer.put(Byte.valueOf((byte)1), Byte.valueOf((byte)70));
        PartyDataPool.partyCareer.put(Byte.valueOf((byte)2), Byte.valueOf((byte)60));
        PartyDataPool.partyCareer.put(Byte.valueOf((byte)3), Byte.valueOf((byte)50));
        PartyDataPool.partyCareer.put(Byte.valueOf((byte)4), Byte.valueOf((byte)40));
        PartyDataPool.partyCareer.put(Byte.valueOf((byte)5), Byte.valueOf((byte)30));
        PartyDataPool.partyCareer.put(Byte.valueOf((byte)6), Byte.valueOf((byte)20));
        PartyDataPool.partyCareer.put(Byte.valueOf((byte)7), Byte.valueOf((byte)15));
        PartyDataPool.partyCareer.put(Byte.valueOf((byte)8), Byte.valueOf((byte)10));
        PartyDataPool.jobId2Names.put(Byte.valueOf((byte)70), "帮主");
        PartyDataPool.jobId2Names.put(Byte.valueOf((byte)60), "副帮主");
        PartyDataPool.jobId2Names.put(Byte.valueOf((byte)50), "左护法");
        PartyDataPool.jobId2Names.put(Byte.valueOf((byte)40), "右护法");
        PartyDataPool.jobId2Names.put(Byte.valueOf((byte)30), "长老");
        PartyDataPool.jobId2Names.put(Byte.valueOf((byte)20), "堂主");
        PartyDataPool.jobId2Names.put(Byte.valueOf((byte)15), "团长");
        PartyDataPool.jobId2Names.put(Byte.valueOf((byte)10), "帮众");
        PartyDataPool.jobId2TitleSerial.put(Byte.valueOf((byte)70), Integer.valueOf(300001));
        PartyDataPool.jobId2TitleSerial.put(Byte.valueOf((byte)60), Integer.valueOf(300002));
        PartyDataPool.jobId2TitleSerial.put(Byte.valueOf((byte)50), Integer.valueOf(300003));
        PartyDataPool.jobId2TitleSerial.put(Byte.valueOf((byte)40), Integer.valueOf(300004));
        PartyDataPool.jobId2TitleSerial.put(Byte.valueOf((byte)30), Integer.valueOf(300005));
        PartyDataPool.jobId2TitleSerial.put(Byte.valueOf((byte)20), Integer.valueOf(300006));
        PartyDataPool.jobId2TitleSerial.put(Byte.valueOf((byte)15), Integer.valueOf(300010));
        PartyDataPool.jobId2TitleSerial.put(Byte.valueOf((byte)10), Integer.valueOf(300007));
        PartyDataPool.partyUpgrades.put(Byte.valueOf((byte)2), Integer.valueOf(975000));
        PartyDataPool.partyUpgrades.put(Byte.valueOf((byte)3), Integer.valueOf(4050000));
        PartyDataPool.partyUpgrades.put(Byte.valueOf((byte)4), Integer.valueOf(9250000));
        PartyDataPool.partyUpgrades.put(Byte.valueOf((byte)5), Integer.valueOf(15000000));
        PartyDataPool.partyUpgrades.put(Byte.valueOf((byte)6), Integer.valueOf(25000000));
        PartyDataPool.partyUpgrades.put(Byte.valueOf((byte)7), Integer.valueOf(35000000));
        PartyDataPool.partyUpgrades.put(Byte.valueOf((byte)8), Integer.valueOf(45000000));
        PartyDataPool.partyUpgradesSilver.put(Byte.valueOf((byte)2), Integer.valueOf(2000000));
        PartyDataPool.partyUpgradesSilver.put(Byte.valueOf((byte)3), Integer.valueOf(3000000));
        PartyDataPool.partyUpgradesSilver.put(Byte.valueOf((byte)4), Integer.valueOf(5000000));
        PartyDataPool.partyUpgradesSilver.put(Byte.valueOf((byte)5), Integer.valueOf(10000000));
        PartyDataPool.partyUpgradesSilver.put(Byte.valueOf((byte)6), Integer.valueOf(30000000));
        PartyDataPool.partyUpgradesSilver.put(Byte.valueOf((byte)7), Integer.valueOf(60000000));
        PartyDataPool.partyUpgradesSilver.put(Byte.valueOf((byte)8), Integer.valueOf(100000000));
    }
}
