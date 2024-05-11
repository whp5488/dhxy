
package com.kitty.system.init.pool;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;
import java.util.Collection;
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
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        PartDataPool.heroMap = initService.initHero();
        PartDataPool.reiEnMap = initService.initReiEn();
        PartDataPool.petExerciseMap = initService.initPetExercise();
        PartDataPool.rbAttrMap = initService.initRbattr();
        PartDataPool.roleChangeColor = initService.initRoleChangeColor();
        PartDataPool.titleMap = initService.initTitle();
        initCanMove(initService.initCanMove());
        final String[] split2;
        final String[] split = split2 = SpringUtils.getBean(ServerService.class).getMarking().split(",");
        for (final String s : split2) {
            PartDataPool.MARKING.add(s);
        }
        PartDataPool.tgroupsMap = initService.initGroups();
        PartDataPool.commonAwardMap = initService.initCommonAward();
    }
    
    public static void initCanMove(final List<SysCanMove> list) {
        final RedisService redisService = SpringUtils.getBean(RedisService.class);
        for (final SysCanMove sysCanMove : list) {
            final Move move = new Move();
            final String uncompress = ZipUtil.uncompress(sysCanMove.getCanMove());
            final List<String> collection = JsonUtils.string2Collection(uncompress, List.class, String.class);
            move.getList().addAll(collection);
            redisService.set("move_" + sysCanMove.getSerial(), move, 0L);
            if (sysCanMove.getTab() <= 0) {
                PartDataPool.can_move_map.add(sysCanMove.getSerial());
            }
        }
    }
    
    static {
        PartDataPool.MARKING = new ArrayList<String>();
        PartDataPool.heroMap = new HashMap<Integer, SysHero>();
        PartDataPool.reiEnMap = new HashMap<Integer, SysReiEn>();
        PartDataPool.petExerciseMap = new HashMap<Integer, SysPetExercise>();
        PartDataPool.rbAttrMap = new HashMap<Integer, SysRbAttr>();
        PartDataPool.roleChangeColor = new HashMap<Integer, SysRoleChangeColor>();
        PartDataPool.can_move_map = new ArrayList<Integer>();
        PartDataPool.titleMap = new HashMap<Integer, SysDataTitle>();
        PartDataPool.commonAwardMap = new HashMap<String, SysCommonAward>();
        PartDataPool.tgroupsMap = new HashMap<String, Tgroups>();
    }
}
