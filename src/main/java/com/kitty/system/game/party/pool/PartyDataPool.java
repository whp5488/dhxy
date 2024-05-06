package com.kitty.system.game.party.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysPartyFlag;
import com.kitty.system.init.service.InitService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PartyDataPool {
  public static Map<Integer, Party> partyMap = new ConcurrentHashMap<>();
  
  public static Map<String, Integer> partyName2Id = new ConcurrentHashMap<>();
  
  public static Map<String, Byte> partyPermissions = new HashMap<>();
  
  public static Map<Byte, Byte> partyCareer = new HashMap<>();
  
  public static Map<Byte, String> jobId2Names = new HashMap<>();
  
  public static Map<Byte, Integer> jobId2TitleSerial = new HashMap<>();
  
  public static List<SysPartyFlag> partyFlagList = new ArrayList<>();
  
  public static Map<Integer, Integer> offlineRoleParty = new ConcurrentHashMap<>();
  
  public static Map<Byte, Integer> partyUpgrades = new HashMap<>();
  
  public static Map<Byte, Integer> partyUpgradesSilver = new HashMap<>();
  
  static {
    MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3002), role -> role.getFaction());
    MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3021), role -> role.getFaction());
    MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3022), role -> role.getFaction());
    MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3001), role -> Integer.valueOf(role.getVisible()));
    partyPermissions.put("帮主", Byte.valueOf((byte)1));
    partyPermissions.put("副帮主", Byte.valueOf((byte)2));
    partyPermissions.put("左护法", Byte.valueOf((byte)3));
    partyPermissions.put("右护法", Byte.valueOf((byte)4));
    partyPermissions.put("长老", Byte.valueOf((byte)5));
    partyPermissions.put("堂主", Byte.valueOf((byte)6));
    partyPermissions.put("团长", Byte.valueOf((byte)7));
    partyPermissions.put("帮众", Byte.valueOf((byte)8));
    partyCareer.put(Byte.valueOf((byte)1), Byte.valueOf((byte)70));
    partyCareer.put(Byte.valueOf((byte)2), Byte.valueOf((byte)60));
    partyCareer.put(Byte.valueOf((byte)3), Byte.valueOf((byte)50));
    partyCareer.put(Byte.valueOf((byte)4), Byte.valueOf((byte)40));
    partyCareer.put(Byte.valueOf((byte)5), Byte.valueOf((byte)30));
    partyCareer.put(Byte.valueOf((byte)6), Byte.valueOf((byte)20));
    partyCareer.put(Byte.valueOf((byte)7), Byte.valueOf((byte)15));
    partyCareer.put(Byte.valueOf((byte)8), Byte.valueOf((byte)10));
    jobId2Names.put(Byte.valueOf((byte)70), "帮主");
    jobId2Names.put(Byte.valueOf((byte)60), "副帮主");
    jobId2Names.put(Byte.valueOf((byte)50), "左护法");
    jobId2Names.put(Byte.valueOf((byte)40), "右护法");
    jobId2Names.put(Byte.valueOf((byte)30), "长老");
    jobId2Names.put(Byte.valueOf((byte)20), "堂主");
    jobId2Names.put(Byte.valueOf((byte)15), "团长");
    jobId2Names.put(Byte.valueOf((byte)10), "帮众");
    jobId2TitleSerial.put(Byte.valueOf((byte)70), Integer.valueOf(300001));
    jobId2TitleSerial.put(Byte.valueOf((byte)60), Integer.valueOf(300002));
    jobId2TitleSerial.put(Byte.valueOf((byte)50), Integer.valueOf(300003));
    jobId2TitleSerial.put(Byte.valueOf((byte)40), Integer.valueOf(300004));
    jobId2TitleSerial.put(Byte.valueOf((byte)30), Integer.valueOf(300005));
    jobId2TitleSerial.put(Byte.valueOf((byte)20), Integer.valueOf(300006));
    jobId2TitleSerial.put(Byte.valueOf((byte)15), Integer.valueOf(300010));
    jobId2TitleSerial.put(Byte.valueOf((byte)10), Integer.valueOf(300007));
    partyUpgrades.put(Byte.valueOf((byte)2), Integer.valueOf(975000));
    partyUpgrades.put(Byte.valueOf((byte)3), Integer.valueOf(4050000));
    partyUpgrades.put(Byte.valueOf((byte)4), Integer.valueOf(9250000));
    partyUpgrades.put(Byte.valueOf((byte)5), Integer.valueOf(15000000));
    partyUpgrades.put(Byte.valueOf((byte)6), Integer.valueOf(25000000));
    partyUpgrades.put(Byte.valueOf((byte)7), Integer.valueOf(35000000));
    partyUpgrades.put(Byte.valueOf((byte)8), Integer.valueOf(45_000_000));
    partyUpgradesSilver.put(Byte.valueOf((byte)2), Integer.valueOf(2000000));
    partyUpgradesSilver.put(Byte.valueOf((byte)3), Integer.valueOf(3000000));
    partyUpgradesSilver.put(Byte.valueOf((byte)4), Integer.valueOf(5000000));
    partyUpgradesSilver.put(Byte.valueOf((byte)5), Integer.valueOf(10000000));
    partyUpgradesSilver.put(Byte.valueOf((byte)6), Integer.valueOf(30000000));
    partyUpgradesSilver.put(Byte.valueOf((byte)7), Integer.valueOf(60000000));
    partyUpgradesSilver.put(Byte.valueOf((byte)8), Integer.valueOf(100000000));
  }
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    partyFlagList = initService.initPartyFlag();
  }
  
  public static int getScene(byte level) {
    int scene = 3002;
    if (level >= 5) {
      scene = 3022;
    } else if (level < 5 && level > 3) {
      scene = 3021;
    } 
    return scene;
  }
}
