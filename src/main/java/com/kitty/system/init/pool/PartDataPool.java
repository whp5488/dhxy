package com.kitty.system.init.pool;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.system.init.domain.vo.Move;
import com.kitty.cache.RedisService;
import com.kitty.system.init.domain.SysCanMove;
import com.kitty.system.init.service.InitService;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.Tgroups;
import com.kitty.system.init.domain.SysCommonAward;
import com.kitty.system.init.domain.SysDataTitle;
import com.kitty.system.init.domain.SysRoleChangeColor;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.init.domain.SysPetExercise;
import com.kitty.system.init.domain.SysReiEn;
import com.kitty.system.init.domain.SysHero;
import java.util.Map;
import java.util.List;

public class PartDataPool
{
    public static List<String> MARKING;
    public static Map<Integer, SysHero> heroMap;
    public static Map<Integer, SysReiEn> reiEnMap;
    public static Map<Integer, SysPetExercise> petExerciseMap;
    public static Map<Integer, SysRbAttr> rbAttrMap;
    public static Map<Integer, SysRoleChangeColor> roleChangeColor;
    public static List<Integer> can_move_map;
    public static Map<Integer, SysDataTitle> titleMap;
    public static Map<String, SysCommonAward> commonAwardMap;
    public static Map<String, Tgroups> tgroupsMap;
    
    public PartDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        PartDataPool.heroMap = initService.initHero();
        PartDataPool.reiEnMap = initService.initReiEn();
        PartDataPool.petExerciseMap = initService.initPetExercise();
        PartDataPool.rbAttrMap = initService.initRbattr();
        PartDataPool.roleChangeColor = initService.initRoleChangeColor();
        PartDataPool.titleMap = initService.initTitle();
        initCanMove(initService.initCanMove());
        String[] split;
        for (String s : split = ((ServerService)SpringUtils.getBean(ServerService.class)).getMarking().split(",")) {
            PartDataPool.MARKING.add(s);
        }
        PartDataPool.tgroupsMap = initService.initGroups();
        PartDataPool.commonAwardMap = initService.initCommonAward();
    }
    
    public static void initCanMove(List<SysCanMove> list) {
        RedisService redisService = (RedisService)SpringUtils.getBean(RedisService.class);
        for (SysCanMove sysCanMove : list) {
            Move move = new Move();
            String uncompress = ZipUtil.uncompress(sysCanMove.getCanMove());
            List<String> collection = (List)JsonUtils.string2Collection(uncompress, List.class, String.class);
            move.getList().addAll(collection);
            redisService.set("move_" + sysCanMove.getSerial(), move, 0L);
            if (sysCanMove.getTab() <= 0) {
                PartDataPool.can_move_map.add(Integer.valueOf(sysCanMove.getSerial()));
            }
            else {
                continue;
            }
        }
    }
    
    static {
        PartDataPool.MARKING = new ArrayList<>();
        PartDataPool.heroMap = new HashMap();
        PartDataPool.reiEnMap = new HashMap();
        PartDataPool.petExerciseMap = new HashMap();
        PartDataPool.rbAttrMap = new HashMap();
        PartDataPool.roleChangeColor = new HashMap();
        PartDataPool.can_move_map = new ArrayList<>();
        PartDataPool.titleMap = new HashMap();
        PartDataPool.commonAwardMap = new HashMap();
        PartDataPool.tgroupsMap = new HashMap();
    }
}
