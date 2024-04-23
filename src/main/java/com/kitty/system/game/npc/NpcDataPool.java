package com.kitty.system.game.npc;

import com.alibaba.fastjson.JSONArray;
import com.kitty.common.model.Pos;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.activity.handler.time.BurningHotHandler;
import com.kitty.system.game.activity.handler.time.MonsterNianHandler;
import com.kitty.system.game.activity.handler.time.PartyBanditHandler;
import com.kitty.system.game.activity.handler.time.SealDemonHandler;
import com.kitty.system.game.activity.handler.time.StarOfficerHandler;
import com.kitty.system.game.activity.handler.time.ThreeScopeHandler;
import com.kitty.system.game.activity.handler.time.WorldBoosHandler;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.hg.HgFightHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsPkHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.npc.domain.NpcButtonParam;
import com.kitty.system.game.npc.domain.NpcContentParam;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.pk.service.PkService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.service.QuestionService;
import com.kitty.system.game.task.service.handler.CarnivalTaskHandler;
import com.kitty.system.game.task.service.handler.CatchesGhostsTaskHandler;
import com.kitty.system.game.task.service.handler.CrusadeRenegadeTaskHandler;
import com.kitty.system.game.task.service.handler.DragonTaskHandler;
import com.kitty.system.game.task.service.handler.FiveTaskHandler;
import com.kitty.system.game.task.service.handler.GodsCourtTaskHandler;
import com.kitty.system.game.task.service.handler.LookForTaskHandler;
import com.kitty.system.game.task.service.handler.LoopTaskHandler;
import com.kitty.system.game.task.service.handler.PartyTaskHandler;
import com.kitty.system.game.task.service.handler.SchoolTaskHandler;
import com.kitty.system.game.task.service.handler.TaskHelper;
import com.kitty.system.game.task.service.handler.TowerTaskHandler;
import com.kitty.system.game.task.service.handler.TreasureMapHandler;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.task.service.handler.dungeon.DungeonMwkFront;
import com.kitty.system.game.task.service.handler.dungeon.DungeonXmHandler;
import com.kitty.system.game.vo.UnifiedValue;
import com.kitty.system.game.welfare.service.WelfareService;
import com.kitty.system.init.domain.SysNpcTypeInfo;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class NpcDataPool {
  public static Map<String, Consumer<NpcButtonParam>> npcButtonHandlers = new HashMap<>();
  
  public static Map<String, Consumer<NpcButtonParam>> npcButtonPrefixHandlers = new HashMap<>();
  
  public static Map<Integer, Consumer<NpcContentParam>> npcContentHandlers = new HashMap<>();
  
  public static Map<Integer, List<Function<Role, List<UnifiedValue>>>> spetialButtons = new HashMap<>();
  
  public static List<String> itemButtons = new ArrayList<>();
  
  public static Map<Integer, SysNpcTypeInfo> data_npc_type_info = new HashMap<>();
  
  static {
    npcButtonHandlers.put("runs_prison", npcButtonParam -> ((PkService)SpringUtils.getBean(PkService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("view_prison", npcButtonParam -> ((PkService)SpringUtils.getBean(PkService.class)).viewPrisoner(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("five_guess", npcButtonParam -> ((WelfareService)SpringUtils.getBean(WelfareService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_pet", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_drug", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_equip", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_grocery", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_guo", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_brac", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_nao", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_di", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_arena", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_long", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_nice", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("buy_npc_feats", npcButtonParam -> ((NpcService)SpringUtils.getBean(NpcService.class)).npcButton(npcButtonParam.getRole(), npcButtonParam.getMsg(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("school_task", npcButtonParam -> ((SchoolTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.SCHOOL)).acceptSchoolTask(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("cbt_task", npcButtonParam -> ((TreasureMapHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.TREASURE_MAP)).acceptTreasureMapTask(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("party_task", npcButtonParam -> ((PartyTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.PARTY)).acceptPartyTask(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("carnival_task", npcButtonParam -> ((CarnivalTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.CARNIVAL)).acceptSchoolTask(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("duanwu_task", npcButtonParam -> ((DragonTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.DRAGON)).acceptSchoolTask(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("loop_task", npcButtonParam -> ((LoopTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.LOOP)).acceptLoopTask(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("tower_copy", npcButtonParam -> ((TowerTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.TOWER)).createTowerDugeon(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("look_for_copy", npcButtonParam -> ((LookForTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.LOOK_FOR)).createLookForDugeon(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("xm_copy", npcButtonParam -> ((DungeonXmHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.XM)).createXmDugeon(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_xm", npcButtonParam -> ((DungeonXmHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.XM)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_tower", npcButtonParam -> ((TowerTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.TOWER)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_look_for", npcButtonParam -> ((LookForTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.LOOK_FOR)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("five_task", npcButtonParam -> ((FiveTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.FIVE)).acceptFiveTask(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("zg_task", npcButtonParam -> ((CatchesGhostsTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.CATCHES_GHOSTS)).acceptCatchesGhostsTask(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_zg", npcButtonParam -> ((CatchesGhostsTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.CATCHES_GHOSTS)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("txy_task", npcButtonParam -> ((GodsCourtTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.GODS_COURT)).acceptGodsCourtTask(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_txy", npcButtonParam -> ((GodsCourtTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.GODS_COURT)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("pz_task", npcButtonParam -> ((CrusadeRenegadeTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.CRUSADE_RENEGADE)).acceptTask(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_pz", npcButtonParam -> ((CrusadeRenegadeTaskHandler)TaskHelper.INSTANCE.getTaskHandler(TaskType.CRUSADE_RENEGADE)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_p_bandit", npcButtonParam -> ((PartyBanditHandler)SpringUtils.getBean(PartyBanditHandler.class)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_star", npcButtonParam -> ((StarOfficerHandler)SpringUtils.getBean(StarOfficerHandler.class)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_boos", npcButtonParam -> ((WorldBoosHandler)SpringUtils.getBean(WorldBoosHandler.class)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_ns", npcButtonParam -> ((MonsterNianHandler)SpringUtils.getBean(MonsterNianHandler.class)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_har", npcButtonParam -> ((BurningHotHandler)SpringUtils.getBean(BurningHotHandler.class)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_seal", npcButtonParam -> ((SealDemonHandler)SpringUtils.getBean(SealDemonHandler.class)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("threats_scope", npcButtonParam -> ((ThreeScopeHandler)SpringUtils.getBean(ThreeScopeHandler.class)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("req_dls", npcButtonParam -> ((QuestionService)SpringUtils.getBean(QuestionService.class)).reqQuestionDls(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("get_dls", npcButtonParam -> ((QuestionService)SpringUtils.getBean(QuestionService.class)).getQuestionDls(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("req_xs", npcButtonParam -> ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).reqQuestionXs(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("get_xs", npcButtonParam -> ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).getQuestionXs(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("req_ss", npcButtonParam -> ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).reqQuestionSs(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("get_ss", npcButtonParam -> ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).getQuestionSs(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("req_ds", npcButtonParam -> ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).reqQuestionDs(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("enter_ds", npcButtonParam -> ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).enter(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("kj_rank", npcButtonParam -> ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).reqKjRank(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("m_f_c", npcButtonParam -> ((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).chooseEnterDungeon(npcButtonParam.getRole(), npcButtonParam.getNpc(), "m_f_c"));
    npcButtonHandlers.put("m_f_z", npcButtonParam -> ((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).chooseEnterDungeon(npcButtonParam.getRole(), npcButtonParam.getNpc(), "m_f_z"));
    npcButtonHandlers.put("threats_mo_1700001", npcButtonParam -> ((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc(), null));
    npcButtonHandlers.put("threats_mo_c_170002", npcButtonParam -> ((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc(), "threats_mo_c_170002"));
    npcButtonHandlers.put("threats_mo_z_170003", npcButtonParam -> ((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).startFight(npcButtonParam.getRole(), npcButtonParam.getNpc(), "threats_mo_z_170003"));
    npcContentHandlers.put(Integer.valueOf(24), npcContentParam -> ((PartyBanditHandler)SpringUtils.getBean(PartyBanditHandler.class)).sendNpcContent(npcContentParam.getRole(), npcContentParam.getNpc()));
    npcContentHandlers.put(Integer.valueOf(20), npcContentParam -> ((StarOfficerHandler)SpringUtils.getBean(StarOfficerHandler.class)).sendNpcContent(npcContentParam.getRole(), npcContentParam.getNpc()));
    npcContentHandlers.put(Integer.valueOf(21), npcContentParam -> ((SealDemonHandler)SpringUtils.getBean(SealDemonHandler.class)).sendNpcContent(npcContentParam.getRole(), npcContentParam.getNpc()));
    npcContentHandlers.put(Integer.valueOf(22), npcContentParam -> ((ThreeScopeHandler)SpringUtils.getBean(ThreeScopeHandler.class)).sendNpcContent(npcContentParam.getRole(), npcContentParam.getNpc()));
    npcContentHandlers.put(Integer.valueOf(23), npcContentParam -> ((WorldBoosHandler)SpringUtils.getBean(WorldBoosHandler.class)).sendNpcContent(npcContentParam.getRole(), npcContentParam.getNpc()));
    npcButtonHandlers.put("enter_at", npcButtonParam -> ((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).enterMap(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("runs_at", npcButtonParam -> ((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).leave(npcButtonParam.getRole()));
    npcButtonHandlers.put("enter_ca", npcButtonParam -> ((CaHandler)SpringUtils.getBean(CaHandler.class)).enterMap(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("runs_ca", npcButtonParam -> ((CaHandler)SpringUtils.getBean(CaHandler.class)).leave(npcButtonParam.getRole()));
    npcButtonHandlers.put("enter_lu", npcButtonParam -> ((WaterHandler)SpringUtils.getBean(WaterHandler.class)).enterMap(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("runs_lu", npcButtonParam -> ((WaterHandler)SpringUtils.getBean(WaterHandler.class)).leave(npcButtonParam.getRole()));
    npcButtonHandlers.put("enter_gang_wars", npcButtonParam -> ((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).enterMap(npcButtonParam.getRole(), npcButtonParam.getNpc()));
    npcButtonHandlers.put("to_fight_wars", npcButtonParam -> ((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).goToMap(npcButtonParam.getRole(), npcButtonParam.getNpc(), "to_fight_wars"));
    npcButtonHandlers.put("activate_cannon", npcButtonParam -> ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).operateCannon(npcButtonParam.getRole(), "activate_cannon"));
    npcButtonHandlers.put("break_off_cannon", npcButtonParam -> ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).operateCannon(npcButtonParam.getRole(), "break_off_cannon"));
    npcButtonHandlers.put("atk_cannon", npcButtonParam -> ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).operateGate(npcButtonParam.getRole(), "atk_cannon"));
    npcButtonHandlers.put("off_cannon", npcButtonParam -> ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).operateGate(npcButtonParam.getRole(), "off_cannon"));
    npcButtonHandlers.put("break_off_gate", npcButtonParam -> ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).operateGate(npcButtonParam.getRole(), "break_off_gate"));
    npcButtonHandlers.put("to_home_wars", npcButtonParam -> ((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).goToMap(npcButtonParam.getRole(), npcButtonParam.getNpc(), "to_home_wars"));
    npcButtonHandlers.put("runs_gang_wars", npcButtonParam -> ((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).leave(npcButtonParam.getRole()));
    npcButtonHandlers.put("hg_yz", npcButtonParam -> ((HgFightHandler)SpringUtils.getBean(HgFightHandler.class)).receive(npcButtonParam.getRole(), npcButtonParam.getNpc()));
  }
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    initNpcInfo(initService);
    initNpcTypeInfo(initService);
  }
  
  public static void initNpcInfo(InitService initService) {
    List<SysNpcInfo> list = initService.initNpcInfo();
    DataCache.ID_NPC.clear();
    DataCache.NAME_NPC_ID.clear();
    DataCache.MAP_NPC_ID_S.clear();
    DataCache.PLACE_NPC.clear();
    for (SysNpcInfo npc : list) {
      Pos pos = (Pos)JSONArray.parseObject(npc.getPosInfo(), Pos.class);
      npc.setPos(pos);
      if (npc.getType() == 2) {
        DataCache.PLACE_NPC.put(npc.getSerial(), npc);
        continue;
      } 
      DataCache.ID_NPC.put(npc.getSerial(), npc);
      DataCache.NAME_NPC_ID.put(npc.getName(), npc.getSerial());
      Set<Integer> set = DataCache.MAP_NPC_ID_S.computeIfAbsent(npc.getPos().getMapId(), k -> new HashSet());
      set.add(npc.getSerial());
    } 
  }
  
  public static void initNpcTypeInfo(InitService initService) {
    data_npc_type_info = initService.initNpcTypeInfo();
  }
}
