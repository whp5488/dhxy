
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
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        PartyDataPool.partyFlagList = initService.initPartyFlag();
    }
    
    public static int getScene(final byte level) {
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
        PartyDataPool.partyMap = new ConcurrentHashMap<Integer, Party>();
        PartyDataPool.partyName2Id = new ConcurrentHashMap<String, Integer>();
        PartyDataPool.partyPermissions = new HashMap<String, Byte>();
        PartyDataPool.partyCareer = new HashMap<Byte, Byte>();
        PartyDataPool.jobId2Names = new HashMap<Byte, String>();
        PartyDataPool.jobId2TitleSerial = new HashMap<Byte, Integer>();
        PartyDataPool.partyFlagList = new ArrayList<SysPartyFlag>();
        PartyDataPool.offlineRoleParty = new ConcurrentHashMap<Integer, Integer>();
        PartyDataPool.partyUpgrades = new HashMap<Byte, Integer>();
        PartyDataPool.partyUpgradesSilver = new HashMap<Byte, Integer>();
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3002), role -> role.getFaction());
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3021), role -> role.getFaction());
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3022), role -> role.getFaction());
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3001), role -> role.getVisible());
        PartyDataPool.partyPermissions.put("帮主", (byte)1);
        PartyDataPool.partyPermissions.put("副帮主", (byte)2);
        PartyDataPool.partyPermissions.put("左护法", (byte)3);
        PartyDataPool.partyPermissions.put("右护法", (byte)4);
        PartyDataPool.partyPermissions.put("长老", (byte)5);
        PartyDataPool.partyPermissions.put("堂主", (byte)6);
        PartyDataPool.partyPermissions.put("团长", (byte)7);
        PartyDataPool.partyPermissions.put("帮众", (byte)8);
        PartyDataPool.partyCareer.put((byte)1, (byte)70);
        PartyDataPool.partyCareer.put((byte)2, (byte)60);
        PartyDataPool.partyCareer.put((byte)3, (byte)50);
        PartyDataPool.partyCareer.put((byte)4, (byte)40);
        PartyDataPool.partyCareer.put((byte)5, (byte)30);
        PartyDataPool.partyCareer.put((byte)6, (byte)20);
        PartyDataPool.partyCareer.put((byte)7, (byte)15);
        PartyDataPool.partyCareer.put((byte)8, (byte)10);
        PartyDataPool.jobId2Names.put((byte)70, "帮主");
        PartyDataPool.jobId2Names.put((byte)60, "副帮主");
        PartyDataPool.jobId2Names.put((byte)50, "左护法");
        PartyDataPool.jobId2Names.put((byte)40, "右护法");
        PartyDataPool.jobId2Names.put((byte)30, "长老");
        PartyDataPool.jobId2Names.put((byte)20, "堂主");
        PartyDataPool.jobId2Names.put((byte)15, "团长");
        PartyDataPool.jobId2Names.put((byte)10, "帮众");
        PartyDataPool.jobId2TitleSerial.put((byte)70, 300001);
        PartyDataPool.jobId2TitleSerial.put((byte)60, 300002);
        PartyDataPool.jobId2TitleSerial.put((byte)50, 300003);
        PartyDataPool.jobId2TitleSerial.put((byte)40, 300004);
        PartyDataPool.jobId2TitleSerial.put((byte)30, 300005);
        PartyDataPool.jobId2TitleSerial.put((byte)20, 300006);
        PartyDataPool.jobId2TitleSerial.put((byte)15, 300010);
        PartyDataPool.jobId2TitleSerial.put((byte)10, 300007);
        PartyDataPool.partyUpgrades.put((byte)2, 975000);
        PartyDataPool.partyUpgrades.put((byte)3, 4050000);
        PartyDataPool.partyUpgrades.put((byte)4, 9250000);
        PartyDataPool.partyUpgrades.put((byte)5, 15000000);
        PartyDataPool.partyUpgrades.put((byte)6, 25000000);
        PartyDataPool.partyUpgrades.put((byte)7, 35000000);
        PartyDataPool.partyUpgrades.put((byte)8, 45000000);
        PartyDataPool.partyUpgradesSilver.put((byte)2, 2000000);
        PartyDataPool.partyUpgradesSilver.put((byte)3, 3000000);
        PartyDataPool.partyUpgradesSilver.put((byte)4, 5000000);
        PartyDataPool.partyUpgradesSilver.put((byte)5, 10000000);
        PartyDataPool.partyUpgradesSilver.put((byte)6, 30000000);
        PartyDataPool.partyUpgradesSilver.put((byte)7, 60000000);
        PartyDataPool.partyUpgradesSilver.put((byte)8, 100000000);
    }
}
