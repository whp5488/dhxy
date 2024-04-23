package com.kitty.system.init.pool;

import com.kitty.cache.RedisService;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysCanMove;
import com.kitty.system.init.domain.SysCommonAward;
import com.kitty.system.init.domain.SysDataTitle;
import com.kitty.system.init.domain.SysHero;
import com.kitty.system.init.domain.SysPetExercise;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.init.domain.SysReiEn;
import com.kitty.system.init.domain.SysRoleChangeColor;
import com.kitty.system.init.domain.Tgroups;
import com.kitty.system.init.domain.vo.Move;
import com.kitty.system.init.service.InitService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartDataPool {
  public static List<String> MARKING = new ArrayList<>();
  
  public static Map<Integer, SysHero> heroMap = new HashMap<>();
  
  public static Map<Integer, SysReiEn> reiEnMap = new HashMap<>();
  
  public static Map<Integer, SysPetExercise> petExerciseMap = new HashMap<>();
  
  public static Map<Integer, SysRbAttr> rbAttrMap = new HashMap<>();
  
  public static Map<Integer, SysRoleChangeColor> roleChangeColor = new HashMap<>();
  
  public static List<Integer> can_move_map = new ArrayList<>();
  
  public static Map<Integer, SysDataTitle> titleMap = new HashMap<>();
  
  public static Map<String, SysCommonAward> commonAwardMap = new HashMap<>();
  
  public static Map<String, Tgroups> tgroupsMap = new HashMap<>();
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    heroMap = initService.initHero();
    reiEnMap = initService.initReiEn();
    petExerciseMap = initService.initPetExercise();
    rbAttrMap = initService.initRbattr();
    roleChangeColor = initService.initRoleChangeColor();
    titleMap = initService.initTitle();
    initCanMove(initService.initCanMove());
    String[] split = ((ServerService)SpringUtils.getBean(ServerService.class)).getMarking().split(",");
    for (String s : split)
      MARKING.add(s); 
    tgroupsMap = initService.initGroups();
    commonAwardMap = initService.initCommonAward();
  }
  
  public static void initCanMove(List<SysCanMove> list) {
    RedisService redisService = (RedisService)SpringUtils.getBean(RedisService.class);
    for (SysCanMove sysCanMove : list) {
      Move move = new Move();
      String uncompress = ZipUtil.uncompress(sysCanMove.getCanMove());
      List<String> collection = (List<String>)JsonUtils.string2Collection(uncompress, List.class, String.class);
      move.getList().addAll(collection);
      redisService.set("move_" + sysCanMove.getSerial(), (Serializable)move, 0L);
      if (sysCanMove.getTab() <= 0)
        can_move_map.add(Integer.valueOf(sysCanMove.getSerial())); 
    } 
  }
}
