package com.kitty.system.game.service;

import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.MoveEvent;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.init.Monster;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysGuajiMap;
import com.kitty.system.init.domain.SysGuajiWar;
import com.kitty.system.init.domain.SysWarRole;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MonsterService {
  Logger logger = LoggerFactory.getLogger(MonsterService.class);
  
  private ConcurrentMap<Integer, Integer> monsterTimes = new ConcurrentHashMap<>();
  
  public int updateMonsterTime(int roleId) {
    int pre = ((Integer)this.monsterTimes.getOrDefault(Integer.valueOf(roleId), Integer.valueOf(0))).intValue();
    this.monsterTimes.put(Integer.valueOf(roleId), Integer.valueOf(pre + 1));
    return pre + 1;
  }
  
  public void flushMonsterTime(int roleId) {
    this.monsterTimes.remove(Integer.valueOf(roleId));
  }
  
  public Object[] getMapMonster(Role role, int mapId) {
    Object[] objs = new Object[2];
    List<SysDataMonster> list = new ArrayList<>();
    Integer warRoleSerial = null;
    SysGuajiMap sysGuajiMap = (SysGuajiMap)MapDataPool.guaJiMap.get(Integer.valueOf(mapId));
    if (sysGuajiMap != null) {
      Integer warDataId = sysGuajiMap.getWarDataId();
      List<SysGuajiWar> wars = (List<SysGuajiWar>)FightDataPool.data_guaji_war_list.get(warDataId);
      if (wars != null && wars.size() > 0) {
        Random random = new Random();
        int index = random.nextInt(wars.size());
        SysWarRole warRole = (SysWarRole)FightDataPool.data_war_role.get(((SysGuajiWar)wars.get(index)).getSerial());
        warRoleSerial = warRole.getSerial();
        if (warRole != null && 
          warRole.getMonster() != null && warRole.getMonster().size() > 0) {
          List<SysDataMonster> monsters = getMonstersByWarRandom(FightDataPool.data_monster, warRole, role);
          list.addAll(monsters);
        } 
      } 
    } 
    objs[0] = warRoleSerial;
    objs[1] = list;
    return objs;
  }
  
  public List<SysDataMonster> getMonstersByWarRandom(Map<Integer, SysDataMonster> data_monster, SysWarRole warRole, Role role) {
    List<SysDataMonster> list = new ArrayList<>();
    Team team = ((FightNewService)SpringUtils.getBean(FightNewService.class)).getRoleFightTeam(role);
    Integer monsterLevel = Integer.valueOf(role.getRelive().intValue() * 1000 + role.getLevel().intValue());
    if (team != null) {
      List<Member> listTeam = team.getList();
      RoleService roleService = SpringUtils.getRoleService();
      int lvCount = 0;
      for (int j = 0; j < listTeam.size(); j++) {
        Member member = listTeam.get(j);
        Role teamRole = roleService.getPlayerBy(member.getRoleId().intValue());
        lvCount += teamRole.getRelive().intValue() * 1000 + teamRole.getLevel().intValue();
      } 
      monsterLevel = Integer.valueOf(lvCount / listTeam.size());
    } 
    Random random = new Random();
    byte[] monsters = { 6, 7, 8, 9, 10 };
    byte count = monsters[random.nextInt(monsters.length)];
    List<Integer> poss = Const.poss;
    for (int i = 0; i < count; i++) {
      int monsterIndex = random.nextInt(warRole.getMonster().size());
      SysDataMonster monster = data_monster.get(((Monster)warRole.getMonster().get(monsterIndex)).getSerial());
      SysDataMonster dataMonster = new SysDataMonster();
      BeanUtils.copyBeanProp(dataMonster, monster);
      int pos = ((Integer)poss.get(i)).intValue();
      dataMonster.setPos(pos);
      DataGlobalFunc.createMonster(monsterLevel.intValue(), dataMonster);
      list.add(dataMonster);
    } 
    return list;
  }
  
  public List<SysDataMonster> getMonsters(Map<Integer, SysDataMonster> data_monster, SysWarRole warRole) {
    List<SysDataMonster> list = new ArrayList<>();
    for (int i = 0; i < warRole.getMonster().size(); i++) {
      SysDataMonster monster = data_monster.get(((Monster)warRole.getMonster().get(i)).getSerial());
      monster.setPos(((Monster)warRole.getMonster().get(i)).getPos().intValue());
      SysDataMonster dataMonster = new SysDataMonster();
      BeanUtils.copyBeanProp(dataMonster, monster);
      list.add(dataMonster);
    } 
    if (warRole != null && warRole.getSpecialType() > 0)
      specialMonsterFight(list, data_monster, warRole); 
    return list;
  }
  
  @EventHandler({EventType.MOVE})
  public void handleMoveEvent(MoveEvent moveEvent) {
    Role role = moveEvent.getRole();
    if (role == null)
      return; 
    boolean flag = SkillAi.canAffectOnPro(0.1D);
    long theTime = System.currentTimeMillis() / 1000L;
    Integer incenseExpireTime = (Integer)role.getExpireTimeMap().get("incenseExpireTime");
    if (flag && theTime <= incenseExpireTime.intValue()) {
      if (role.getLevel().intValue() < 33)
        return; 
      MonsterService monsterService = (MonsterService)SpringUtils.getBean(MonsterService.class);
      Object[] objs = monsterService.getMapMonster(role, role.getPos().getMapId().intValue());
      List<SysDataMonster> mapMonster = (List<SysDataMonster>)objs[1];
      if (mapMonster == null || mapMonster.size() <= 0)
        return; 
      Team team = ((FightNewService)SpringUtils.getBean(FightNewService.class)).getRoleFightTeam(role);
      ((FightNewService)SpringUtils.getBean(FightNewService.class)).enterFieldFight(team, role);
      monsterService.flushMonsterTime(role.getRoleId().intValue());
    } 
  }
  
  private void specialMonsterFight(List<SysDataMonster> list, Map<Integer, SysDataMonster> data_monster, SysWarRole warRole) {
    switch (warRole.getSpecialType()) {
      case 1:
        star18Excute(list, data_monster, warRole);
        break;
    } 
  }
  
  private void star18Excute(List<SysDataMonster> list, Map<Integer, SysDataMonster> data_monster, SysWarRole warRole) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 122
    //   4: aload_1
    //   5: invokeinterface size : ()I
    //   10: ifle -> 122
    //   13: aconst_null
    //   14: astore #4
    //   16: aload_1
    //   17: invokeinterface iterator : ()Ljava/util/Iterator;
    //   22: astore #5
    //   24: aload #5
    //   26: invokeinterface hasNext : ()Z
    //   31: ifeq -> 67
    //   34: aload #5
    //   36: invokeinterface next : ()Ljava/lang/Object;
    //   41: checkcast com/kitty/system/init/domain/SysDataMonster
    //   44: astore #6
    //   46: aload #6
    //   48: invokevirtual getPos : ()I
    //   51: sipush #10003
    //   54: if_icmpne -> 64
    //   57: aload #6
    //   59: astore #4
    //   61: goto -> 67
    //   64: goto -> 24
    //   67: aload #4
    //   69: ifnull -> 122
    //   72: aload #4
    //   74: invokevirtual getSkills : ()Ljava/util/ArrayList;
    //   77: astore #5
    //   79: aload #4
    //   81: new java/util/ArrayList
    //   84: dup
    //   85: invokespecial <init> : ()V
    //   88: invokevirtual setSkills : (Ljava/util/ArrayList;)V
    //   91: aload #4
    //   93: invokevirtual getSkills : ()Ljava/util/ArrayList;
    //   96: aload #5
    //   98: invokevirtual addAll : (Ljava/util/Collection;)Z
    //   101: pop
    //   102: aload #4
    //   104: sipush #30042
    //   107: ldc '骑马'
    //   109: invokestatic addSkill : (Lcom/kitty/system/init/domain/SysDataMonster;ILjava/lang/String;)V
    //   112: aload #4
    //   114: sipush #30043
    //   117: ldc '摄魂大法'
    //   119: invokestatic addSkill : (Lcom/kitty/system/init/domain/SysDataMonster;ILjava/lang/String;)V
    //   122: new com/kitty/system/game/service/MonsterService$1
    //   125: dup
    //   126: aload_0
    //   127: invokespecial <init> : (Lcom/kitty/system/game/service/MonsterService;)V
    //   130: astore #4
    //   132: aload #4
    //   134: invokeinterface size : ()I
    //   139: iconst_1
    //   140: isub
    //   141: istore #5
    //   143: invokestatic values : ()[Lcom/kitty/system/enums/war/wardef/STAR_PERCENT;
    //   146: astore #9
    //   148: aload #9
    //   150: arraylength
    //   151: istore #10
    //   153: iconst_0
    //   154: istore #11
    //   156: iload #11
    //   158: iload #10
    //   160: if_icmpge -> 284
    //   163: aload #9
    //   165: iload #11
    //   167: aaload
    //   168: astore #12
    //   170: iload #5
    //   172: ifge -> 178
    //   175: goto -> 287
    //   178: aload #12
    //   180: invokevirtual getPercent : ()D
    //   183: invokestatic canAffectOnPro : (D)Z
    //   186: istore #6
    //   188: iload #6
    //   190: ifeq -> 278
    //   193: aload_2
    //   194: aload #12
    //   196: invokevirtual getSerial : ()I
    //   199: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   202: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   207: checkcast com/kitty/system/init/domain/SysDataMonster
    //   210: astore #7
    //   212: aload #7
    //   214: ifnull -> 278
    //   217: new com/kitty/system/init/domain/SysDataMonster
    //   220: dup
    //   221: invokespecial <init> : ()V
    //   224: astore #8
    //   226: aload #8
    //   228: aload #7
    //   230: invokestatic copyBeanProp : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   233: aload #8
    //   235: aload #4
    //   237: iload #5
    //   239: invokeinterface get : (I)Ljava/lang/Object;
    //   244: checkcast java/lang/Integer
    //   247: invokevirtual intValue : ()I
    //   250: invokevirtual setPos : (I)V
    //   253: aload_1
    //   254: aload #8
    //   256: invokeinterface add : (Ljava/lang/Object;)Z
    //   261: pop
    //   262: aload #4
    //   264: iload #5
    //   266: invokeinterface remove : (I)Ljava/lang/Object;
    //   271: pop
    //   272: iinc #5, -1
    //   275: iconst_0
    //   276: istore #6
    //   278: iinc #11, 1
    //   281: goto -> 156
    //   284: goto -> 143
    //   287: invokestatic getStar18SelectPet : ()Lcom/kitty/system/init/domain/SysDataMonster;
    //   290: astore #9
    //   292: aload #9
    //   294: ifnull -> 329
    //   297: aload #9
    //   299: sipush #10103
    //   302: invokevirtual setPos : (I)V
    //   305: aload #9
    //   307: aload #9
    //   309: invokevirtual getDes : ()Ljava/lang/String;
    //   312: invokevirtual setName : (Ljava/lang/String;)V
    //   315: aload #9
    //   317: invokestatic randomMonsterPetSkill : (Lcom/kitty/system/init/domain/SysDataMonster;)V
    //   320: aload_1
    //   321: aload #9
    //   323: invokeinterface add : (Ljava/lang/Object;)Z
    //   328: pop
    //   329: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #218	-> 0
    //   #219	-> 13
    //   #220	-> 16
    //   #221	-> 46
    //   #222	-> 57
    //   #223	-> 61
    //   #225	-> 64
    //   #226	-> 67
    //   #228	-> 72
    //   #229	-> 79
    //   #230	-> 91
    //   #232	-> 102
    //   #233	-> 112
    //   #237	-> 122
    //   #244	-> 132
    //   #250	-> 143
    //   #252	-> 170
    //   #253	-> 175
    //   #256	-> 178
    //   #257	-> 188
    //   #258	-> 193
    //   #259	-> 212
    //   #261	-> 217
    //   #262	-> 226
    //   #263	-> 233
    //   #264	-> 253
    //   #267	-> 262
    //   #268	-> 272
    //   #270	-> 275
    //   #250	-> 278
    //   #278	-> 287
    //   #279	-> 292
    //   #281	-> 297
    //   #283	-> 305
    //   #284	-> 315
    //   #285	-> 320
    //   #287	-> 329
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   46	18	6	sysDataMonster	Lcom/kitty/system/init/domain/SysDataMonster;
    //   79	43	5	skills	Ljava/util/ArrayList;
    //   16	106	4	dataMonster	Lcom/kitty/system/init/domain/SysDataMonster;
    //   212	66	7	sysDataMonster	Lcom/kitty/system/init/domain/SysDataMonster;
    //   226	52	8	dataMonster	Lcom/kitty/system/init/domain/SysDataMonster;
    //   170	108	12	starPercent	Lcom/kitty/system/enums/war/wardef/STAR_PERCENT;
    //   188	96	6	flag	Z
    //   0	330	0	this	Lcom/kitty/system/game/service/MonsterService;
    //   0	330	1	list	Ljava/util/List;
    //   0	330	2	data_monster	Ljava/util/Map;
    //   0	330	3	warRole	Lcom/kitty/system/init/domain/SysWarRole;
    //   132	198	4	poss	Ljava/util/List;
    //   143	187	5	i	I
    //   292	38	9	dataMonsterPet	Lcom/kitty/system/init/domain/SysDataMonster;
    // Local variable type table:
    //   start	length	slot	name	signature
    //   79	43	5	skills	Ljava/util/ArrayList<Lcom/kitty/system/init/domain/vo/SkillParameter;>;
    //   0	330	1	list	Ljava/util/List<Lcom/kitty/system/init/domain/SysDataMonster;>;
    //   0	330	2	data_monster	Ljava/util/Map<Ljava/lang/Integer;Lcom/kitty/system/init/domain/SysDataMonster;>;
    //   132	198	4	poss	Ljava/util/List<Ljava/lang/Integer;>;
  }
}
