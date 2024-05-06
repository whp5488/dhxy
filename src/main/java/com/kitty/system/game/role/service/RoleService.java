package com.kitty.system.game.role.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.kitty.cache.JedisUtil;
import com.kitty.cache.RedisService;
import com.kitty.common.identity.IdentityType;
import com.kitty.common.model.Pos;
import com.kitty.common.start.ServerService;
import com.kitty.common.thread.IdGenerator;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.common.utils.CodeUtils;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.TimeUtil;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cxf.send.service.GameSendService;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.CreationRoleEvent;
import com.kitty.listener.event.EnterMapEvent;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.event.LogoutEvent;
import com.kitty.listener.event.RoleAspectChangeEvent;
//import com.kitty.listener.event.RoleChangeEvent;
import com.kitty.listener.event.RoleLevelUpEvent;
import com.kitty.listener.event.ranking.RoleChangeEvent;
import com.kitty.mina.cache.BagCache;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.Deploy;
import com.kitty.mina.cache.PetCache;
import com.kitty.mina.cache.SessionCache;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.filter.HeartbeatFilter;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.rabbit.RabbitService;
import com.kitty.record.RecordThread;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.common.message.RespStateRefresh;
import com.kitty.system.common.objects.ItemCheck;
import com.kitty.system.enums.war.itempro.def.FACTION_PRO_DICT;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.account.domain.Account;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.bag.domain.Overflow;
import com.kitty.system.game.bag.message.RespCurrencyInfo;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.card.domain.CardChange;
import com.kitty.system.game.card.domain.CardExercise;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.dirtywords.WordDisposePool;
import com.kitty.system.game.echo.service.EchoService;
import com.kitty.system.game.enter.ReqMultiMoveTo;
import com.kitty.system.game.enter.message.ReqDey;
import com.kitty.system.game.enter.message.ReqExtendStoreSize;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.enter.message.RespGeneralNotify;
import com.kitty.system.game.enter.message.RespKickClient;
import com.kitty.system.game.enter.message.RespReplyServerTime;
import com.kitty.system.game.enter.message.RespRoleAlreadyLogin;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.game.enter.service.EnterService;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import com.kitty.system.game.fightnew.book.vo.SkillAutoPicker;
import com.kitty.system.game.fightnew.messages.ReqFightHandle;
import com.kitty.system.game.fightnew.messages.old.RespElseFight;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.utils.MessageAssistUtil;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.system.game.general.message.ReqAssignAttrib;
import com.kitty.system.game.general.message.ReqSetRecommendAttrib;
import com.kitty.system.game.horse.service.HorseService;
import com.kitty.system.game.house.service.HouseService;
import com.kitty.system.game.market.message.RespMarketGoodCard;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.partner.service.PartnerService;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.pet.domain.vo.SuitParam;
import com.kitty.system.game.pet.message.RespAssistPetList;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.pk.service.PkService;
import com.kitty.system.game.player.PlayerProfile;
import com.kitty.system.game.player.bean.PkInfoBox;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.domain.Setting;
import com.kitty.system.game.role.domain.vo.PartyPractice;
import com.kitty.system.game.role.message.ReqCreateNewChar;
import com.kitty.system.game.role.message.RespCorrelation;
import com.kitty.system.game.role.message.RespInstruct;
import com.kitty.system.game.role.message.RespLevelUp;
import com.kitty.system.game.role.message.RespPartyPracticeInfo;
import com.kitty.system.game.role.message.RespRefreshStore;
import com.kitty.system.game.role.message.RespUpdateImprovement;
import com.kitty.system.game.role.message.vo.Instruct;
import com.kitty.system.game.safe.service.SafeService;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.scene.SceneManager;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.skill.service.SkillService;
import com.kitty.system.game.store.domain.StoreEquip;
import com.kitty.system.game.store.domain.StoreGoods;
import com.kitty.system.game.store.service.StoreService;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.game.tianfu.service.RoleDayInCeService;
import com.kitty.system.game.vo.CheckMapInt;
import com.kitty.system.game.vo.Condition;
import com.kitty.system.game.vo.FieldByte;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.welfare.service.WelfareService;
import com.kitty.system.init.domain.SysHero;
import com.kitty.system.init.domain.SysPartyPractice;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.init.domain.SysRoleChangeColor;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.vo.AdviceAddPro;
import com.kitty.system.init.domain.vo.ChangeColor;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.init.pool.ExpDataPool;
import com.kitty.system.init.pool.PartDataPool;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
  private final Logger logger = LoggerFactory.getLogger(RoleService.class);
  
  @Autowired
  IdWorker worker;
  
  @Autowired
  BagService bagService;
  
  @Autowired
  CardService cardService;
  
  @Autowired
  RoleService roleService;
  
  @Autowired
  PetService petService;
  
  @Autowired
  EquipService equipService;
  
  @Autowired
  NpcService npcService;
  
  @Autowired
  TeamService teamService;
  
  @Autowired
  BroadcastService broadcastService;
  
  @Autowired
  RedisService redisService;
  
  @Autowired
  StoreService storeService;
  
  @Autowired
  GameSendService sendService;
  
  @Autowired
  TitleService titleService;
  
  @EventHandler({EventType.LOGOUT})
  public void handleLogoutEvent(LogoutEvent logoutEvent) {
    Role role = logoutEvent.getRole();
    HeartbeatFilter.cleanHeartBeatRecord(role.getId().longValue());
    role.setUnlock(false);
    role.setPreOnlineSec(Long.valueOf(getOnlineSec(role)));
    clearBufferData(role.getId().longValue());
    role.save();
    this.roleService.removeOnline(role);
    this.roleService.removeOffline(role);
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    HeartbeatFilter.cleanHeartBeatRecord(role.getId().longValue());
    checkReset(role);
    HorseService horseService = SpringUtils.getHorseService();
    horseService.refresh(role, horseService.getHorse(role));
    this.equipService.refreshPetEquip(role);
    this.petService.loadPet(role);
    ((EchoService)SpringUtils.getBean(EchoService.class)).cancelCountdown(role.getId().longValue());
  }
  
  public void roleLevelChangeEvent(Role role) {
    role.setGg(Integer.valueOf(role.getGg().intValue() + 1));
    role.setLx(Integer.valueOf(role.getLx().intValue() + 1));
    role.setLl(Integer.valueOf(role.getLl().intValue() + 1));
    role.setMj(Integer.valueOf(role.getMj().intValue() + 1));
    role.setFpoint(Integer.valueOf(role.getFpoint().intValue() + 4));
    calculateBase(role);
    refreshRole(role);
    refreshPetClose(role);
    Map<Integer, Partner> partnerMap = (Map<Integer, Partner>)DataCache.PARTNER_MAP.get(role.getId());
    if (partnerMap != null && partnerMap.size() > 0) {
      PartnerService partnerService = (PartnerService)SpringUtils.getBean(PartnerService.class);
      int partnerLv = 55;
      for (Map.Entry<Integer, Partner> entry : partnerMap.entrySet()) {
        Partner partner = entry.getValue();
        if (partner.getPartnerLv() < partnerLv) {
          if (role.getLevel().intValue() >= partnerLv) {
            partner.setPartnerLv((short)partnerLv);
          } else {
            partner.setPartnerLv(role.getLevel().shortValue());
          } 
          partnerService.partnerLevelChangeEvent(role, partner);
          partner.save();
        } 
      } 
    } 
  }
  
  public void clearBufferData(long id) {
    DataCache.ROLE_DAY_IN_CE.remove(Long.valueOf(id));
    DataCache.ROLE_TIAN_FU.remove(Long.valueOf(id));
    DataCache.CUTTING_FERRULE_MAP.remove(Long.valueOf(id));
    DataCache.CARD_EXERCISE_MAP.remove(Long.valueOf(id));
    BagCache.BAG_GOODS_MAP.remove(Long.valueOf(id));
    BagCache.BAG_EQUIP_MAP.remove(Long.valueOf(id));
    BagCache.STORE_GOODS_MAP.remove(Long.valueOf(id));
    BagCache.STORE_EQUIP_MAP.remove(Long.valueOf(id));
    DataCache.EQUIP_MAP.remove(Long.valueOf(id));
    DataCache.EFFECT_MAP.remove(Long.valueOf(id));
    DataCache.HORSE_MAP.remove(Long.valueOf(id));
    DataCache.GIFT_RECORD.remove(Long.valueOf(id));
    DataCache.PARTNER_MAP.remove(Long.valueOf(id));
    PetCache.PET_MAP.remove(Long.valueOf(id));
    PetCache.PET_EQUIP_MAP.remove(Long.valueOf(id));
    DataCache.SETTING_MAP.remove(Long.valueOf(id));
    DataCache.DOMESTICATE_MAP.remove(Long.valueOf(id));
    DataCache.HOUSE_MAP.remove(Long.valueOf(id));
    DataCache.ROLE_EMAIL.remove(Long.valueOf(id));
    DataCache.WEEK_TREASURE.remove(Long.valueOf(id));
  }
  
  public void refreshPetClose(Role role) {
    Map<Integer, Pet> petMap = this.petService.selectPet(role);
    for (Integer key : petMap.keySet()) {
      Pet pet = petMap.get(key);
      this.petService.refreshPetLevelClose(pet);
      this.petService.refreshPetInfo(role, pet);
    } 
  }
  
  public boolean checkCondition(Role role, List<Item> items, Map<Integer, Integer> map) {
    if (map != null)
      for (Integer key : map.keySet()) {
        boolean flag = checkCurrency(role, key, map.get(key));
        if (!flag)
          return false; 
      }  
    if (items != null) {
      CheckMapInt checkMapInt = ItemCheck.checkAllItemTypes(items, role);
      if (!checkMapInt.isFlag())
        return false; 
    } 
    return true;
  }
  
  public boolean costCondition(Role role, Condition condition, String reason, boolean bnotify, boolean bnotcut) {
    if (condition.getList() != null)
      for (Item item : condition.getList()) {
        Integer goods = this.bagService.consumeBagGoods(role, item.getItem(), item.getNum(), true, true, "costCondition");
        if (goods != null)
          return false; 
      }  
    if (condition.getMap() != null) {
      Map<Integer, Integer> map = condition.getMap();
      for (Integer type : map.keySet()) {
        boolean flag = deductCurrency(role, type, map.get(type), "costCondition");
        if (!flag)
          return false; 
      } 
    } 
    return true;
  }
  
  public void addOnline(Role role) {
    if (role == null)
      return; 
    DataCache.ONLINE_ROLES.put(role.getId(), role);
    this.redisService.del(role.getId() + "_countdown_" + SpringUtils.getBean(ServerService.class).getMarking());
  }
  
  public void removeOnline(Role role) {
    if (role == null)
      return; 
    DataCache.ONLINE_ROLES.remove(role.getId());
  }
  
  public void addOffline(Role role, boolean flag) {
    if (role == null)
      return; 
    role.setOfLineTime(System.currentTimeMillis());
    DataCache.OFFLINE_ROLES.put(role.getId(), role);
    String key = "temporary" + role.getId();
    Boolean exists = this.redisService.exists(key);
    if (exists)
      this.redisService.expire(key, 54000L); 
    sendOffline(role, flag);
  }
  
  public void removeOffline(Role role) {
    if (role == null)
      return; 
    DataCache.OFFLINE_ROLES.remove(role.getId());
    String key = "temporary" + role.getId();
    Boolean exists = this.redisService.exists(key);
    if (exists)
      this.redisService.persist(key); 
  }
  
  public boolean isOnline(int id) {
    return isOnline(SpringUtils.getPlayerService().getUidBy(id));
  }
  
  public boolean isOnline(long id) {
    return DataCache.ONLINE_ROLES.containsKey(id);
  }
  
  public boolean canLogin(Account account, final IoSession session) {
    ServerService serverService = SpringUtils.getBean(ServerService.class);
    if (account == null) {
      MessagePusher.pushMessage(session, new RespNotifyMiscEx("账号不存在！！"));
      serverService.getScheduledExecutorService().schedule(() -> {
        session.closeNow();
      },  3L, TimeUnit.SECONDS);
      return false;
    } 
    if (account.getIsStop() <= 0) {
      MessagePusher.pushMessage(session, new RespNotifyMiscEx("账号已经被封闭，请联系管理员！！"));
      serverService.getScheduledExecutorService().schedule(() -> {
        session.closeNow();
      },  3L, TimeUnit.SECONDS);
      return false;
    } 
    return true;
  }
  
  public Role createRole(IoSession session, String sid, ReqCreateNewChar reqCreateNewChar) {
    SysHero hero = PartDataPool.heroMap.get(reqCreateNewChar.getSerial());
    SysRbAttr data = PartDataPool.rbAttrMap.get(1);
    if (hero != null) {
      String name = reqCreateNewChar.getName().trim();
      name = name.replace("#", "");
      if (!canCreateRole(session, name, LOGIC.LOGICTYPE_HERO.getValue()))
        return null; 
      name = CheckCharUtils.replaceName(name);
      Role role = new Role();
      role.setId(IdGenerator.getNextId());
      role.setSid(sid);
      role.setRoleId(getRoleId());
      role.setName(name);
      role.setSex(reqCreateNewChar.getSex());
      role.setRace(reqCreateNewChar.getRace());
      role.setLevel(0);
      role.setRelive(0);
      role.setExp(0L);
      role.setSaveLife(0L);
      role.setSaveMana(0L);
      role.setGold(0L);
      role.setIcon(hero.getShape());
      role.getTask().setIcon(hero.getShape());
      role.setSerial(hero.getSerial());
      role.setSkills(createSkillBox(role, reqCreateNewChar.getRace(), reqCreateNewChar.getSex(), data));
      role.setPos(new Pos());
      role.getExtendBox().setQuotaLimit(DataGlobalFunc.setQuota());
      role.getExtendBox().setSetMap(DataGlobalFunc.setting());
      role.getExtendBox().setNewDistrict(DataGlobalFunc.newDistrict());
      role.getRest().setParty(DataGlobalFunc.faction());
      calculateBase(role);
      role.setMarking(reqCreateNewChar.getMarking());
      NutMap map = new NutMap();
      map.setv("a", 0);
      map.setv("b", 0);
      role.setColor(map.toString());
      long time = System.currentTimeMillis();
      role.setEnterTime(time);
      role.save();
      createRoleRests(role);
      SpringUtils.getPlayerService().addPlayerProfile(new PlayerProfile(role));
      addOffline(role, false);
      EventDispatcher.getInstance().fireEvent(new CreationRoleEvent(EventType.CREATION_ROLE, role));
      return role;
    } 
    return null;
  }
  
  public void createRoleRests(Role role) {
    CardExercise cardExercise = new CardExercise(this.worker.nextId(), role.getId());
    cardExercise.save();
    DataCache.CARD_EXERCISE_MAP.put(role.getId(), cardExercise);
    SpringUtils.getHorseService().creationHorse(role);
    Setting setting = new Setting(this.worker.nextId(), role.getId());
    setting.save();
    DataCache.SETTING_MAP.put(role.getId(), setting);
    ((HouseService)SpringUtils.getBean(HouseService.class)).addHouse(role.getId(), 5100);
  }
  
  public void calculateBase(Role role) {
    int life = CountUtils.countLife(role);
    role.setCurrLife(life);
    role.setMaxLife(life);
    int mana = CountUtils.countMana(role);
    role.setCurrMana(mana);
    role.setMaxMana(mana);
  }
  
  public Map<Integer, RoleSkill> createSkillBox(Role role, Integer race, Integer sex, SysRbAttr data) {
    Map<Integer, RoleSkill> skills = new HashMap<>();
    List<Integer> list = DataGlobalFunc.GetPlayerSkillId(race, sex);
    for (Integer integer : list) {
      Map<Integer, SysSkill> skillMap = DataGlobalFunc.getSkillListByAttr(integer);
      for (Integer key : skillMap.keySet()) {
        SysSkill sysSkill = skillMap.get(key);
        int max_proficient = data.getMaxSkillExp();
        List<FieldByte> effects = null;
        if (role.getSkills().size() > 0)
          for (RoleSkill value : role.getSkills().values()) {
            SysSkill skill = SkillDataPool.skillMap.get(value.getSkillId());
            if (skill.getPos().equals(sysSkill.getPos()) && value.getEffects().size() > 0) {
              effects = new ArrayList<>();
              for (FieldByte fieldByte : value.getEffects()) {
                int type = fieldByte.getK() % 10;
                effects.add(new FieldByte(sysSkill.getIcon() * 10 + type, fieldByte.getV()));
              } 
            } 
          }  
        RoleSkill skill2 = new RoleSkill(sysSkill.getSerial(), integer, data.getInitSkillExp(), max_proficient);
        if (effects != null)
          skill2.setEffects(effects); 
        skills.put(sysSkill.getSerial(), skill2);
      } 
    } 
    return skills;
  }
  
  public boolean canCreateRole(IoSession session, String name, int logic) {
    String type = "角色";
    if (LOGIC.LOGICTYPE_PET.getValue() == logic) {
      type = "宠物";
    } else if (LOGIC.HORSE.getValue() == logic) {
      type = "坐骑";
    } 
    if (Strings.isEmpty(name)) {
      MessagePusher.pushMessage(session, new RespMsg(String.format("请输入正确的%s名。", type)));
      return false;
    } 
    if (name.length() > 6) {
      MessagePusher.pushMessage(session, new RespMsg(String.format("%s名称过长。", type)));
      return false;
    } 
    name = CheckCharUtils.replaceName(name);
    if (name.length() <= 1) {
      MessagePusher.pushMessage(session, new RespMsg(String.format("请输入正确的%s名。", type)));
      return false;
    } 
    List<String> set = WordDisposePool.checkDirtyWords(name);
    if (set.size() >= 1) {
      MessagePusher.pushMessage(session, new RespMsg(String.format("%s名存在非法字符。", type)));
      return false;
    } 
    if (CheckCharUtils.checkQQNumber(name)) {
      MessagePusher.pushMessage(session, new RespMsg(String.format("%s名存在非法字符。", type)));
      return false;
    } 
    Matcher emojiTitle = CheckCharUtils.CHECK_EMOJI.matcher(name);
    if (emojiTitle.find()) {
      MessagePusher.pushMessage(session, new RespMsg(String.format("%s名存在非法字符。", type)));
      return false;
    } 
    if (LOGIC.LOGICTYPE_HERO.getValue() == logic && !SpringUtils.getPlayerService().occupyName(name, 0L)) {
      MessagePusher.pushMessage(session, new RespMsg("这个角色名字已经存在。"));
      return false;
    } 
    return true;
  }
  
  public void enterMap(IoSession session, Role role) throws Exception {
    List<PlayerProfile> playerProfiles = SpringUtils.getPlayerService().getPlayersBy(role.getSid());
    for (PlayerProfile playerProfile : playerProfiles) {
      Role onlineRole = getOnlinePlayer(playerProfile.getId());
      if (onlineRole != null) {
        final IoSession ioSession = SessionUtils.getSessionBy(onlineRole.getId());
        if (ioSession != null && ioSession.getId() != session.getId()) {
          RespOtherLogin respOtherLogin = new RespOtherLogin();
          respOtherLogin.setResult(1);
          MessagePusher.pushMessage(ioSession, respOtherLogin);
          Thread.sleep(1000L);
          ioSession.write(new RespKickClient("你的账号已在其它设备登录。")).addListener(future -> ioSession.closeNow());
        } 
      } 
    } 
    SessionManager.INSTANCE.registerSession(role.getRoleId(), session);
    addOnline(role);
    removeOffline(role);
    session.setAttribute(SessionProperties.CLIENT_STATUS, "1");
    session.setAttribute(SessionProperties.PLAYER_ID, role.getRoleId());
    session.setAttribute(SessionProperties.PLAYER_UID, role.getId());
    session.setAttribute(SessionProperties.PLAYER_NAME, role.getName());
    role.setDistributeKey(SessionManager.INSTANCE.getNextDistributeKey());
    role.setState(1);
    RespRoleAlreadyLogin respRoleAlreadyLogin = new RespRoleAlreadyLogin();
    respRoleAlreadyLogin.setRoleName(role.getName());
    MessagePusher.pushMessage(role, respRoleAlreadyLogin);
    RespReplyServerTime respReplyServerTime = new RespReplyServerTime();
    respReplyServerTime.setServerTime(String.valueOf(TimeUtil.timeInMillis()));
    MessagePusher.pushMessage(role, respReplyServerTime);
    if (role.getLockTime() > System.currentTimeMillis()) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = new Date(role.getLockTime());
      String tips = simpleDateFormat.format(date);
      RespOtherLogin respOtherLogin2 = new RespOtherLogin();
      respOtherLogin2.setContent("您违规游戏行为，角色已被封停。\n解封时间：" + tips);
      respOtherLogin2.setResult(2);
      MessagePusher.pushMessage(role, respOtherLogin2);
      return;
    } 
    sendPacket(role);
    MessagePusher.pushMessage(role, SafeUtil.getLockStatus(role));
    ((PkService)SpringUtils.getBean(PkService.class)).loginCheckFromPrison(role);
    EventDispatcher.getInstance().fireEvent(new EnterMapEvent(EventType.ENTER_MAP, role));
    EventDispatcher.getInstance().fireEvent(new LoginEvent(EventType.LOGIN, role));
    Team team = this.teamService.getTeam(role.getRoleId());
    if (team != null) {
      int teamCount = this.teamService.getTeamCount(team);
      if (teamCount == 0) {
        SessionCache.ROLE_TEAM.remove(role.getRoleId());
      } else {
        this.teamService.updateTeamList(role);
        this.teamService.liveLoadTeamList(role);
      } 
    } 
    SpringUtils.getBean(EnterService.class).loadHmlNotify(role);
    if (role.getMobile() != null && role.getMobile().length() > 1)
      MessagePusher.pushMessage(role.getId(), new RespCorrelation(role.getMobile()));
    this.cardService.reqFiveExercise(role, false);
    stateRefresh(role);
    this.titleService.refreshTitle(role);
    refreshRole(role);
    sendExtraAttribute(role);
    refreshRoleCurrency(role);
    Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
    for (Long key : bagGoodsMap.keySet()) {
      BagGoods goods = bagGoodsMap.get(key);
      this.bagService.refreshRoleGoods(role, goods, false);
    } 
    Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
    for (Long key2 : bagEquipMap.keySet()) {
      BagEquip bagEquip = bagEquipMap.get(key2);
      this.bagService.refreshRoleEquip(role, bagEquip, false);
    } 
    Map<Long, CardChange> card = this.cardService.getCard(role);
    for (Long key3 : card.keySet())
      this.cardService.refresh(role, card.get(key3)); 
    Map<Long, StoreGoods> storeGoodsMap = this.storeService.getStoreGoods(role);
    for (StoreGoods good : storeGoodsMap.values())
      this.storeService.refreshStoreGoods(role, good); 
    Iterator<StoreEquip> itStoreEquips = this.storeService.getStoreEquip(role).values().iterator();
    while (itStoreEquips.hasNext()) {
      StoreEquip equip = itStoreEquips.next();
      if (equip.getType() > 1) {
        this.storeService.refreshStoreEquip(role, equip);
        continue;
      } 
      this.logger.error("加载仓库装备异常数据 = {}", equip);
      itStoreEquips.remove();
    } 
    for (RoleTianFu roleTianFu : DataCache.ROLE_TIAN_FU.get(role.getId())) {
      if (roleTianFu == null)
        continue; 
      if (roleTianFu.getStorePos() > 0)
        this.storeService.refreshStoreTianFu(role, roleTianFu); 
    } 
    role.setEnterTime(System.currentTimeMillis());
    role.setOfLineTime(0L);
    role.setOnlineLoginTime(System.currentTimeMillis());
    role.setPrevHeartTime(0L);
    role.save();
    refreshInstruct(role);
    SessionManager.INSTANCE.removeFromAnonymous(session);
    RespGeneralNotify respGeneralNotify = new RespGeneralNotify();
    respGeneralNotify.setNotify(39);
    respGeneralNotify.setValue("1");
    MessagePusher.pushMessage(role, respGeneralNotify);
  }
  
  public void refreshParty(Role role) {
    this.titleService.refreshTitle(role);
    RespUpdate respUpdate = new RespUpdate();
    respUpdate.setId(role.getRoleId());
    List<FiedValue> list = new ArrayList<>();
    list.add(new FiedValue("faction", (byte)3, role.getFaction()));
    list.add(new FiedValue("factionLevel", (byte)3, getFactionLevel(role)));
    list.add(new FiedValue("factionName", (byte)4, getFactionName(role)));
    respUpdate.setList(list);
    MessagePusher.pushMessage(role, respUpdate);
  }
  
  public void refreshRole(Role role) {
    refreshRoleStore(role);
    RespUpdate respUpdate = new RespUpdate();
    respUpdate.setId(role.getRoleId());
    List<FiedValue> list = new ArrayList<>();
    list.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countLife(role))));
    list.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countMana(role))));
    list.add(new FiedValue("curr_life", (byte)3, role.getCurrLife()));
    list.add(new FiedValue("curr_mana", (byte)3, role.getCurrMana()));
    list.add(new FiedValue("buyPetGrid", (byte)3, Integer.valueOf(role.getExtendBox().getBuyPetGrid())));
    list.add(new FiedValue("buyBagGrid", (byte)3, role.getExtendBox().getPosition()));
    list.add(new FiedValue("buyStoreGrid", (byte)3, role.getExtendBox().getStorePosition()));
    list.add(new FiedValue("buyPetEquipGrid", (byte)3, role.getExtendBox().getPetEquipPosition()));
    list.add(new FiedValue("type", (byte)2, Short.valueOf(role.getSerial().shortValue())));
    list.add(new FiedValue("level", (byte)2, Short.valueOf(role.getLevel().shortValue())));
    list.add(new FiedValue("relive", (byte)1, Byte.valueOf(role.getRelive().byteValue())));
    list.add(new FiedValue("exp", (byte)4, String.valueOf(role.getExp())));
    list.add(new FiedValue("nextExp", (byte)4, String.valueOf(FormulaHero.formulaHeroLvUpExp(role.getLevel(), Integer.valueOf(role.getLevel().intValue() + 1), role.getRelive()))));
    List<FiedValue> deyList = deyList(role.getColor());
    if (deyList.size() > 0)
      list.addAll(deyList); 
    list.add(new FiedValue("gg", (byte)2, Short.valueOf(role.getGg().shortValue())));
    list.add(new FiedValue("lx", (byte)2, Short.valueOf(role.getLx().shortValue())));
    list.add(new FiedValue("ll", (byte)2, Short.valueOf(role.getLl().shortValue())));
    list.add(new FiedValue("mj", (byte)2, Short.valueOf(role.getMj().shortValue())));
    list.add(new FiedValue("fpoint", (byte)2, Short.valueOf(role.getFpoint().shortValue())));
    list.add(new FiedValue("faction", (byte)3, role.getFaction()));
    list.add(new FiedValue("factionLevel", (byte)3, Byte.valueOf(getFactionLevel(role))));
    list.add(new FiedValue("factionName", (byte)4, getFactionName(role)));
    list.add(new FiedValue("autoFight", (byte)1, Byte.valueOf(role.getExtendBox().getAutoFight())));
    list.add(new FiedValue("title", (byte)3, Integer.valueOf(role.getCurrTitle())));
    respUpdate.setList(list);
    MessagePusher.pushMessage(role, (Message)respUpdate);
  }
  
  public String getFactionName(Role role) {
    String name = "";
    if (role.getFaction().intValue() > 0) {
      Party party = SpringUtils.getPartyService().getParty(role.getFaction());
      if (party != null)
        name = party.getName(); 
    } 
    return name;
  }
  
  public byte getFactionLevel(Role role) {
    byte level = 0;
    if (role.getFaction().intValue() > 0) {
      Party party = SpringUtils.getPartyService().getParty(role.getFaction());
      if (party != null)
        level = party.getLevel(); 
    } 
    return level;
  }
  
  public void refreshRoleInfo(Role role, boolean flag) {
    RespUpdate respUpdate = new RespUpdate();
    respUpdate.setId(role.getRoleId().intValue());
    List<FiedValue> list = new ArrayList<>();
    list.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countLife(role))));
    list.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countMana(role))));
    list.add(new FiedValue("curr_life", (byte)3, role.getCurrLife()));
    list.add(new FiedValue("curr_mana", (byte)3, role.getCurrMana()));
    respUpdate.setList(list);
    MessagePusher.pushMessage(role, (Message)respUpdate);
    if (flag)
      refreshRoleStore(role); 
  }
  
  public void refreshRoleStore(Role role) {
    MessagePusher.pushMessage(role, (Message)new RespRefreshStore(role.getSaveLife(), role.getSaveMana()));
  }
  
  public List<FiedValue> deyList(String color) {
    List<FiedValue> list = new ArrayList<>();
    Gson gson = new Gson();
    NutMap nutMap = (NutMap)gson.fromJson(color, NutMap.class);
    if (nutMap != null) {
      int a = nutMap.getInt("a");
      if (a > 0)
        list.add(new FiedValue("a", (byte)3, Integer.valueOf(a))); 
      int b = nutMap.getInt("b");
      if (b > 0)
        list.add(new FiedValue("b", (byte)3, Integer.valueOf(b))); 
    } 
    return list;
  }
  
  public void sendExtraAttribute(Role role) {
    List<FiedValue> list = new ArrayList<>();
    list = correction(role, list);
    list = faction(role, list);
    MessagePusher.pushMessage(role, (Message)new RespUpdateImprovement(role.getRoleId().intValue(), list));
  }
  
  public void stateRefresh(Role role) {
    int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    List<FiedValue> list = new ArrayList<>();
    if (role.getMasquerade() > 0 && role.getCardExpireTime() > currTime) {
      NutMap nutMap = new NutMap();
      nutMap.setv("serial", Integer.valueOf(role.getMasquerade()));
      nutMap.setv("expire", Integer.valueOf(role.getCardExpireTime()));
      list.add(new FiedValue("card", (byte)4, JsonUtils.map2String((Map)nutMap)));
    } 
    PkInfoBox box = role.getPkInfoBox();
    if (box.getIncense() > 0 && box.getIncense() > currTime)
      list.add(new FiedValue("7", (byte)3, Integer.valueOf(box.getIncense()))); 
    if (box.isRedName()) {
      if (box.getRedTime() > 0 && box.getRedTime() > currTime)
        list.add(new FiedValue("8", (byte)3, Integer.valueOf(box.getRedTime()))); 
      if (box.getPrisonTime() > 0 && box.getPrisonTime() > currTime)
        list.add(new FiedValue("9", (byte)3, Integer.valueOf(box.getPrisonTime()))); 
      if (box.getConceal() > 0 && box.getConceal() > currTime)
        list.add(new FiedValue("101", (byte)3, Integer.valueOf(box.getConceal()))); 
    } 
    int moveAdditionTime = role.getExtendBox().getState().getInt("shenxing", 0);
    if (moveAdditionTime > 0 && moveAdditionTime > currTime)
      list.add(new FiedValue("109", (byte)3, Integer.valueOf(moveAdditionTime))); 
    MessagePusher.pushMessage(role, (Message)new RespStateRefresh(list));
  }
  
  public List<FiedValue> correction(Role role, List<FiedValue> list) {
    for (Integer rei : role.getRest().getCorrection().keySet())
      list.add(new FiedValue("rei_" + rei, (byte)3, role.getRest().getCorrection().get(rei))); 
    return list;
  }
  
  public List<FiedValue> faction(Role role, List<FiedValue> list) {
    if (role.getRest().getParty().size() > 0) {
      Map<Integer, Integer> party = role.getRest().getParty();
      for (Integer pro : party.keySet()) {
        Integer value = party.get(pro);
        if (value != null)
          list.add(new FiedValue(String.valueOf(pro), (byte)3, value)); 
      } 
      return list;
    } 
    return list;
  }
  
  public void refreshRoleCurrency(Role role) {
    MessagePusher.pushMessage(role, (Message)new RespCurrencyInfo(role));
  }
  
  public void refreshInstruct(Role role) {
    List<Instruct> list = new ArrayList<>();
    Setting setting = ((SettingService)SpringUtils.getBean(SettingService.class)).getSetting(role);
    Map<Short, Map<Byte, String>> map = setting.getSpecial();
    for (Short serial : map.keySet()) {
      Map<Byte, String> instructMap = map.get(serial);
      for (Byte index : instructMap.keySet())
        list.add(new Instruct(serial.shortValue(), index.byteValue(), instructMap.get(index))); 
    } 
    if (list.size() > 0)
      MessagePusher.pushMessage(role, (Message)new RespInstruct(list)); 
  }
  
  private void sendPacket(Role role) {
    String key = "temporary" + role.getId();
    Overflow overflow = (Overflow)this.redisService.get(key, Overflow.class);
    if (overflow != null)
      this.bagService.lodeTemporary(role, overflow.getList()); 
  }
  
  public boolean addExp(Role role, long exp, String reason, boolean is) {
    if (exp == 0L)
      return true; 
    ServerService serverService = (ServerService)SpringUtils.getBean(ServerService.class);
    if (serverService.getExpTimesLevel() != 0 && serverService.getExpTimes() != 0 && role.getRelive().intValue() * 1000 + role.getLevel().intValue() > serverService.getExpTimesLevel())
      exp *= serverService.getExpTimes(); 
    if (exp < 0L) {
      long n, tips_exp = Math.abs(exp);
      if (role.getExp() - exp <= 0L) {
        n = 0L;
      } else {
        n = role.getExp() - exp;
      } 
      exp = n;
      role.setExp(exp);
      RespUpdate respUpdate = new RespUpdate();
      List<FiedValue> list = new ArrayList<>();
      list.add(new FiedValue("exp", (byte)4, String.valueOf(role.getExp())));
      respUpdate.setId(role.getRoleId().intValue());
      respUpdate.setList(list);
      MessagePusher.pushMessage(role, (Message)respUpdate);
      if (exp > 0L)
        if (reason != null) {
          MessagePusher.pushMessage(role, (Message)new RespMsg(reason + "#<IR4>##<R>" + tips_exp + "#点经验"));
        } else {
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "你被扣除了#<IR4>##<R>" + tips_exp + "#点经验", true));
        }  
      role.save();
      EventDispatcher.getInstance().fireEvent(new RoleChangeEvent(EventType.ROLE_EXP_CHANGE, role));
      return true;
    } 
    int level = role.getLevel().intValue();
    SysRbAttr sysRbattr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf(role.getRelive().intValue() + 1));
    if (sysRbattr != null) {
      long n2;
      Integer lvLimit = sysRbattr.getLvLimit();
      if (role.getLevel().intValue() >= lvLimit.intValue() && role.getExp() >= DataGlobalFunc.storageExp(role.getRelive().intValue() * 1000 + role.getLevel().intValue(), LOGIC.LOGICTYPE_HERO.getValue())) {
        String msg = String.format("角色已到达%d转的上限%d级", new Object[] { role.getRelive(), lvLimit });
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), msg, true));
        return false;
      } 
      String exp_tips = "#<G>" + exp + "#点经验";
      long storageExp = DataGlobalFunc.storageExp(role.getRelive().intValue() * 1000 + role.getLevel().intValue(), LOGIC.LOGICTYPE_HERO.getValue());
      if (role.getExp() + exp > storageExp) {
        n2 = storageExp;
      } else {
        n2 = role.getExp() + exp;
      } 
      long totalExp = n2;
      long needExp = FormulaHero.formulaHeroLvUpExp(role.getLevel(), Integer.valueOf(role.getLevel().intValue() + 1), role.getRelive());
      if (totalExp >= needExp && role.getLevel().intValue() < lvLimit.intValue()) {
        while (totalExp >= needExp) {
          totalExp -= needExp;
          level++;
          role.setLevel(Integer.valueOf(level));
          role.setExp(totalExp);
          roleLevelChangeEvent(role);
          if (level >= lvLimit.intValue()) {
            long n3;
            storageExp = DataGlobalFunc.storageExp(role.getRelive().intValue() * 1000 + role.getLevel().intValue(), LOGIC.LOGICTYPE_HERO.getValue());
            if (totalExp > storageExp) {
              n3 = storageExp;
            } else {
              n3 = totalExp;
            } 
            totalExp = n3;
            role.setExp(totalExp);
            break;
          } 
          needExp = FormulaHero.formulaHeroLvUpExp(role.getLevel(), Integer.valueOf(role.getLevel().intValue() + 1), role.getRelive());
        } 
        SceneManager.INSTANCE.sendMessages(role, new Message[] { (Message)new RespLevelUp(role.getRoleId().intValue(), role.getLevel().intValue()) });
        EventDispatcher.getInstance().fireEvent((BaseGameEvent)new RoleLevelUpEvent(EventType.ROLE_LEVEL_UP, role, role.getRelive().intValue() * 1000 + level - 1, role.getRelive().intValue() * 1000 + level));
        role.save();
      } else {
        role.setExp(totalExp);
        RespUpdate respUpdate2 = new RespUpdate();
        List<FiedValue> list2 = new ArrayList<>();
        list2.add(new FiedValue("exp", (byte)4, String.valueOf(role.getExp())));
        respUpdate2.setList(list2);
        respUpdate2.setId(role.getRoleId().intValue());
        MessagePusher.pushMessage(role, (Message)respUpdate2);
        role.save();
      } 
      if (is)
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "你获得了#<IR4>#" + exp_tips, true)); 
      EventDispatcher.getInstance().fireEvent((BaseGameEvent)new RoleChangeEvent(EventType.ROLE_EXP_CHANGE, role));
      return true;
    } 
    return false;
  }
  
  public boolean addCurrency(Role role, Integer type, Long num, String cause, boolean isTips, boolean joint) {
    if (num.longValue() == 0L)
      return false; 
    if (num.longValue() < 0L) {
      this.logger.error("增加货币负数，角色 = {}, 方法 = {}，货币类型 = {}，数量 = {}", new Object[] { role.getName(), cause, type, num });
      return false;
    } 
    Long add_to = num;
    boolean flag = false;
    String msg = "";
    if (type.equals(Const.RESTYPE_GOLD)) {
      num = Long.valueOf(role.getGold().longValue() + num.longValue());
      role.setGold(num);
      msg = "#<IR" + Const.RESTYPE_GOLD + ">#";
      flag = true;
    } else if (type.equals(Const.RESTYPE_SILVER)) {
      num = Long.valueOf(role.getSilver().longValue() + num.longValue());
      role.setSilver(num);
      msg = "#<IR" + Const.RESTYPE_SILVER + ">#";
      flag = true;
    } else if (type.equals(Const.RESTYPE_COIN)) {
      num = Long.valueOf(role.getCoin().longValue() + num.longValue());
      role.setCoin(num);
      msg = "#<IR" + Const.RESTYPE_COIN + ">#";
      flag = true;
    } else if (type.equals(Const.RESTYPE_GUO)) {
      num = Long.valueOf(role.getExtendBox().getGuoScore().longValue() + num.longValue());
      role.getExtendBox().setGuoScore(num);
      msg = "#<IR" + Const.RESTYPE_GUO + ">#";
      flag = true;
    } else if (type.equals(Const.RESTYPE_BRAC)) {
      num = Long.valueOf(role.getExtendBox().getBracScore().longValue() + num.longValue());
      role.getExtendBox().setBracScore(num);
      msg = "#<IR" + Const.RESTYPE_BRAC + ">#";
      flag = true;
    } else if (type.equals(Const.RESTYPE_NAO)) {
      num = Long.valueOf(role.getExtendBox().getNaoScore().longValue() + num.longValue());
      role.getExtendBox().setNaoScore(num);
      msg = "#<IR" + Const.RESTYPE_NAO + ">#";
      flag = true;
    } else if (type.equals(Const.RESTYPE_DI)) {
      num = Long.valueOf(role.getExtendBox().getDiScore().longValue() + num.longValue());
      role.getExtendBox().setDiScore(num);
      msg = "#<IR" + Const.RESTYPE_DI + ">#";
      flag = true;
    } else if (type.equals(Const.RESTYPE_ARENA)) {
      num = Long.valueOf(role.getExtendBox().getArenaScore().longValue() + num.longValue());
      role.getExtendBox().setArenaScore(num);
      msg = "#<IR" + Const.RESTYPE_ARENA + ">#";
      flag = true;
    } else if (type.equals(Const.RESTYPE_LONG)) {
      num = Long.valueOf(role.getExtendBox().getLongScore().longValue() + num.longValue());
      role.getExtendBox().setLongScore(num);
      msg = "#<IR" + Const.RESTYPE_LONG + ">#";
      flag = true;
    } else if (type.equals(Const.RESTYPE_NICE)) {
      num = Long.valueOf(role.getExtendBox().getNiceScore().longValue() + num.longValue());
      role.getExtendBox().setNiceScore(num);
      msg = "#<IR" + Const.RESTYPE_NICE + ">#";
      flag = true;
    } else if (type.equals(Const.RESTYPE_TRIBUTE)) {
      num = Long.valueOf(role.getExtendBox().getHelpScore().longValue() + num.longValue());
      role.getExtendBox().setHelpScore(num);
      msg = "#<IR" + Const.RESTYPE_TRIBUTE + ">#";
      flag = true;
    } else if (type.equals(Const.RES_TYPE_FEATS)) {
      num = Long.valueOf(role.getExtendBox().getFeatsScore().longValue() + num.longValue());
      role.getExtendBox().setFeatsScore(num);
      msg = "#<IR" + Const.RES_TYPE_FEATS + ">#";
      flag = true;
    } else if (type.equals(Const.RES_TYPE_CHARGE_SCORE)) {
      num = Long.valueOf(role.getExtendBox().getChargeScore().longValue() + num.longValue());
      role.getExtendBox().setChargeScore(num);
      msg = "#<IR" + Const.RES_TYPE_CHARGE_SCORE + ">#";
      flag = true;
    } else if (type.equals(Const.RES_TYPE_BOUND_GOLD)) {
      num = Long.valueOf(role.getExtendBox().getBoundGold().longValue() + num.longValue());
      role.getExtendBox().setBoundGold(num);
      msg = "#<IR" + Const.RES_TYPE_BOUND_GOLD + ">#";
      flag = true;
    } else if (type.equals(Const.RES_TYPE_ACHIEVEMENT)) {
      num = Long.valueOf(role.getExtendBox().getAchievementScore().longValue() + num.longValue());
      role.getExtendBox().setAchievementScore(num);
      msg = "#<IR" + Const.RES_TYPE_ACHIEVEMENT + ">#";
      flag = true;
    } 
    if (flag) {
      String cause_ = cause;
      if (isTips) {
        if (!joint)
          cause = ""; 
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), cause + "你获得了" + msg + MoneyUtil.colours(add_to), true));
      } 
      if (!type.equals(Const.RES_TYPE_ACHIEVEMENT))
        refreshRoleCurrency(role); 
      if (cause_ == null)
        cause_ = ""; 
      role.save();
      RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)3, (byte)1, String.format("货币类型：%d，增加数量%d ,增加后数量：%d，操作原因：%s", new Object[] { type, add_to, num, cause_ }), role.getMarking());
    } 
    return flag;
  }
  
  public void commendAttrib(Role role, ReqSetRecommendAttrib notify) {
    String msg = "自定义";
    if (role.getRoleId().equals(Integer.valueOf(notify.getId()))) {
      Map<String, Integer> map = role.getExtendBox().getAutoAttrib();
      map.put("gg", Integer.valueOf(notify.getGg()));
      map.put("lx", Integer.valueOf(notify.getLx()));
      map.put("ll", Integer.valueOf(notify.getLl()));
      map.put("mj", Integer.valueOf(notify.getMj()));
      map.put("type", Integer.valueOf(notify.getType()));
      if (notify.getType() <= 2) {
        SysHero hero = (SysHero)PartDataPool.heroMap.get(role.getSerial());
        if (hero != null) {
          AdviceAddPro adviceAddPro = hero.getAdviceAddPro().get(notify.getType() - 1);
          msg = adviceAddPro.getTitle();
        } 
      } 
    } else {
      Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(notify.getId()));
      if (pet != null) {
        Map<String, Integer> map2 = pet.getAutoAttrib();
        map2.put("gg", Integer.valueOf(notify.getGg()));
        map2.put("lx", Integer.valueOf(notify.getLx()));
        map2.put("ll", Integer.valueOf(notify.getLl()));
        map2.put("mj", Integer.valueOf(notify.getMj()));
        map2.put("type", Integer.valueOf(notify.getType()));
        pet.setAutoAttrib(map2);
        if (notify.getType() <= 2) {
          SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
          if (sysPet != null) {
            AdviceAddPro adviceAddPro2 = sysPet.getAdviceAddPro().get(notify.getType() - 1);
            msg = adviceAddPro2.getTitle();
          } 
        } 
        pet.save();
      } 
    } 
    if (notify.getStatus() != 0)
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "加点方案#<r:255,g:128,b:0>" + msg + "#已启用", false)); 
    role.save();
  }
  
  public void changeRace(Role role, Integer serial) {
    Map<Integer, SysSkill> data_skill = SkillDataPool.skillMap;
    Map<Integer, Integer> skillProficient = changeSkillProficient(role.getSkills(), data_skill);
    if (skillProficient.size() < 6) {
      MessagePusher.pushMessage(role, (Message)new RespHint(13, (short)0));
      return;
    } 
    SysHero hero = (SysHero)PartDataPool.heroMap.get(serial);
    if (hero != null) {
      RoleDayInCeService ceService = (RoleDayInCeService)SpringUtils.getBean(RoleDayInCeService.class);
      if (ceService.checkDemount(role, hero.getRace()) > 0) {
        MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("天策符背包格子不足，无法转换种族"));
        return;
      } 
      if (!deductCurrency(role, Const.RESTYPE_GOLD, 300L, "转换种族"))
        return; 
      resetRole(role, skillProficient, hero, data_skill, ceService, com.kitty.listener.event.RoleChangeEvent.RACE);
    } 
  }
  
  public void resetRole(Role role, Map<Integer, Integer> skillProficient, SysHero hero, Map<Integer, SysSkill> data_skill, RoleDayInCeService ceService, int type) {
    SysRbAttr sysRbAttr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf(role.getRelive().intValue() + 1));
    int count = FormulaHero.restorePorCount(role.getRelive().intValue(), role.getLevel().intValue(), LOGIC.LOGICTYPE_HERO.getValue());
    role.setGg(role.getLevel());
    role.setLx(role.getLevel());
    role.setLl(role.getLevel());
    role.setMj(role.getLevel());
    role.setFpoint(Integer.valueOf(count));
    role.setRace(Integer.valueOf(hero.getRace()));
    role.setSex(Integer.valueOf(hero.getGender()));
    role.setIcon(hero.getShape().intValue());
    role.getTask().setIcon(hero.getShape().intValue());
    role.setSerial(hero.getSerial());
    calculateBase(role);
    refreshRole(role);
    MessagePusher.pushMessage(role, (Message)new RespHint(13, (short)1));
    SpringUtils.getHorseService().convertMount(role);
    this.broadcastService.sendUpdateAppear(role);
    role.setSkills(createSkillBox(role, role.getRace(), role.getSex(), sysRbAttr));
    for (Integer key : role.getSkills().keySet()) {
      RoleSkill skill = (RoleSkill)role.getSkills().get(key);
      if (skill.getSkillId().equals(Integer.valueOf(30041)))
        continue; 
      SysSkill sysSkill = data_skill.get(skill.getSkillId());
      Integer proficient = skillProficient.get(sysSkill.getPos());
      skill.setProficient(proficient);
      skill.setMaxProficient(sysRbAttr.getMaxSkillExp());
    } 
    ((SkillService)SpringUtils.getBean(SkillService.class)).loadRoleSkill(role);
    this.equipService.checkEquipValid(role);
    role.getExtendBox().setAutoFightSet(new HashMap<>());
    ceService.chageRace(role);
    role.save();
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new com.kitty.listener.event.RoleChangeEvent(EventType.ROLE_CHANGE, role, null, null, type));
  }
  
  public void changeRelive(Role role, Integer serial) {
    SysHero hero = (SysHero)PartDataPool.heroMap.get(serial);
    if (hero != null)
      if (role.getRelive().intValue() < 4) {
        for (Integer key : role.getSkills().keySet()) {
          RoleSkill skill = (RoleSkill)role.getSkills().get(key);
          if (skill.getSkillId().equals(Integer.valueOf(30041)))
            continue; 
          if (skill.getProficient().intValue() < skill.getMaxProficient().intValue()) {
            MessagePusher.pushMessage(role, (Message)new RespMsg("法术#<Y>熟练#未满"));
            return;
          } 
        } 
        RoleDayInCeService ceService = (RoleDayInCeService)SpringUtils.getBean(RoleDayInCeService.class);
        if (ceService.checkDemount(role, hero.getRace()) > 0) {
          MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("天策符背包格子不足，无法转生"));
          return;
        } 
        if (!deductCurrency(role, Const.RESTYPE_GOLD, 10L, "角色转生"))
          return; 
        SysRbAttr sysRbAttr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf(role.getRelive().intValue() + 2));
        int count = FormulaHero.restorePorCount(role.getRelive().intValue() + 1, sysRbAttr.getInitLv().intValue(), LOGIC.LOGICTYPE_HERO.getValue());
        role.setGg(sysRbAttr.getInitLv());
        role.setLx(sysRbAttr.getInitLv());
        role.setLl(sysRbAttr.getInitLv());
        role.setMj(sysRbAttr.getInitLv());
        role.setFpoint(Integer.valueOf(count));
        role.setRace(Integer.valueOf(hero.getRace()));
        role.setSex(Integer.valueOf(hero.getGender()));
        role.setIcon(hero.getShape().intValue());
        role.getTask().setIcon(hero.getShape().intValue());
        role.setSerial(hero.getSerial());
        role.setRelive(Integer.valueOf(role.getRelive().intValue() + 1));
        role.setLevel(sysRbAttr.getInitLv());
        MessagePusher.pushMessage(role, (Message)new RespHint(8, (short)1));
        SpringUtils.getHorseService().convertMount(role);
        SpringUtils.getBroadcastService().sendUpdateAppear(role);
        role.setSkills(createSkillBox(role, role.getRace(), role.getSex(), sysRbAttr));
        for (Integer key2 : role.getSkills().keySet()) {
          RoleSkill skill2 = (RoleSkill)role.getSkills().get(key2);
          if (skill2.getSkillId().equals(Integer.valueOf(30041)))
            continue; 
          skill2.setProficient(sysRbAttr.getInitSkillExp());
          skill2.setMaxProficient(sysRbAttr.getMaxSkillExp());
        } 
        ((SkillService)SpringUtils.getBean(SkillService.class)).loadRoleSkill(role);
        this.equipService.checkEquipValid(role);
        Integer reborn = DataGlobalFunc.getRoleReborn(role.getRelive().intValue(), role.getRace().intValue(), role.getSex().intValue());
        role.getRest().getCorrection().put(role.getRelive(), reborn);
        calculateBase(role);
        refreshRole(role);
        sendExtraAttribute(role);
        role.getExtendBox().setAutoFightSet(new HashMap<>());
        ceService.chageRace(role);
        role.save();
        EventDispatcher.getInstance().fireEvent((BaseGameEvent)new com.kitty.listener.event.RoleChangeEvent(EventType.ROLE_CHANGE, role, null, null, com.kitty.listener.event.RoleChangeEvent.REI));
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("转生已上限"));
      }  
  }
  
  public Map<Integer, Integer> changeSkillProficient(Map<Integer, RoleSkill> map, Map<Integer, SysSkill> data_skill) {
    Map<Integer, Integer> hashMap = new HashMap<>();
    for (Integer key : map.keySet()) {
      RoleSkill roleSkill = map.get(key);
      SysSkill sysSkill = data_skill.get(roleSkill.getSkillId());
      if (sysSkill.getPos().intValue() > 0)
        hashMap.put(sysSkill.getPos(), roleSkill.getProficient()); 
    } 
    return hashMap;
  }
  
  public void reliveCorrection(Role role, String string) {
    if (role.getRelive().intValue() >= 0) {
      if (this.bagService.consumeBagGoods(role, 90008, 1, false, true, "转生修正") != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("您背包中没有回梦丹"));
        return;
      } 
      NutMap map = (NutMap)Json.fromJson(NutMap.class, string);
      Map<Integer, Integer> correction = new HashMap<>();
      for (String key : map.keySet()) {
        int rei = Integer.valueOf(key).intValue();
        if (rei <= role.getRelive().intValue())
          correction.put(Integer.valueOf(rei), Integer.valueOf(map.getInt(key))); 
      } 
      role.getRest().setCorrection(correction);
      calculateBase(role);
      refreshRole(role);
      MessagePusher.pushMessage(role, (Message)new RespUpdateImprovement(role.getRoleId().intValue(), correction(role, new ArrayList<>())));
      MessagePusher.pushMessage(role, (Message)new RespHint(10, (short)1));
      MessagePusher.pushMessage(role, (Message)new RespMsg("修改转生修正成功"));
      role.save();
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("请先进行转生"));
    } 
  }
  
  public void recover(Role role, byte type) {
    String msg = null;
    if (type == 0) {
      Pet pet = (Pet)this.petService.selectPet(role).get(role.getExtendBox().getCombatPet());
      if (pet != null) {
        if (pet.getCurrLife() >= pet.getMaxLife().intValue() && pet.getCurrMana().intValue() >= pet.getMaxMana().intValue()) {
          msg = "生命值和法力值已满，不需要补充。";
        } else {
          pet.setCurrLife(pet.getMaxLife().intValue());
          pet.setCurrMana(pet.getMaxMana());
          RespUpdate respUpdate = new RespUpdate();
          List<FiedValue> list = new ArrayList<>();
          respUpdate.setId(pet.getPetId().intValue());
          List<PetEquip> equips = this.petService.getPetEffective(role.getId().longValue(), pet);
          list.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countPetLife(pet, equips))));
          list.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countPetMana(pet, equips))));
          list.add(new FiedValue("curr_life", (byte)3, Integer.valueOf(pet.getCurrLife())));
          list.add(new FiedValue("curr_mana", (byte)3, pet.getCurrMana()));
          respUpdate.setList(list);
          MessagePusher.pushMessage(role, (Message)respUpdate);
        } 
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("当前没有出战宠物"));
      } 
    } else if (role.getCurrLife().intValue() >= role.getMaxLife().intValue() && role.getCurrMana().intValue() >= role.getMaxMana().intValue()) {
      msg = "生命值和法力值已满，不需要补充。";
    } else {
      role.setCurrLife(role.getMaxLife());
      role.setCurrMana(role.getMaxMana());
      RespUpdate respUpdate2 = new RespUpdate();
      List<FiedValue> list2 = new ArrayList<>();
      respUpdate2.setId(role.getRoleId().intValue());
      list2.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countLife(role))));
      list2.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countMana(role))));
      list2.add(new FiedValue("curr_life", (byte)3, role.getCurrLife()));
      list2.add(new FiedValue("curr_mana", (byte)3, role.getCurrMana()));
      respUpdate2.setList(list2);
      MessagePusher.pushMessage(role, (Message)respUpdate2);
    } 
    if (msg != null)
      MessagePusher.pushMessage(role, (Message)new RespMsg(msg)); 
  }
  
  public void assignAttrib(Role role, ReqAssignAttrib assignAttrib) {
    if (!SafeUtil.isUnlock(role))
      return; 
    String msg = "加点成功";
    if (assignAttrib.getGg() < 0)
      assignAttrib.setGg(0); 
    if (assignAttrib.getLl() < 0)
      assignAttrib.setLl(0); 
    if (assignAttrib.getLx() < 0)
      assignAttrib.setLx(0); 
    if (assignAttrib.getMj() < 0)
      assignAttrib.setMj(0); 
    int count = assignAttrib.getGg() + assignAttrib.getLx() + assignAttrib.getLl() + assignAttrib.getMj();
    if (role.getRoleId().equals(Integer.valueOf(assignAttrib.getId()))) {
      if (role.getFpoint().intValue() < count)
        return; 
      Integer restorePorCount = Integer.valueOf(FormulaHero.restorePorCount(role.getRelive().intValue(), role.getLevel().intValue(), LOGIC.LOGICTYPE_HERO.getValue()));
      if (count > role.getFpoint().intValue() || role.getGg().intValue() - role.getLevel().intValue() + role.getMj().intValue() - role.getLevel().intValue() + role.getLl().intValue() - role.getLevel().intValue() + role.getLx().intValue() - role.getLevel().intValue() + count > restorePorCount.intValue()) {
        msg = "加点失败";
        this.logger.error("角色:[{}],Id:[{}],加点超出:[{}],未分配点:[{}]", new Object[] { role.getName(), role.getRoleId(), Integer.valueOf(count), role.getFpoint() });
      } else {
        if (assignAttrib.getGg() > 0)
          role.setGg(Integer.valueOf(role.getGg().intValue() + assignAttrib.getGg())); 
        if (assignAttrib.getLx() > 0)
          role.setLx(Integer.valueOf(role.getLx().intValue() + assignAttrib.getLx())); 
        if (assignAttrib.getLl() > 0)
          role.setLl(Integer.valueOf(role.getLl().intValue() + assignAttrib.getLl())); 
        if (assignAttrib.getMj() > 0)
          role.setMj(Integer.valueOf(role.getMj().intValue() + assignAttrib.getMj())); 
        role.setFpoint(Integer.valueOf(role.getFpoint().intValue() - count));
        RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(role.getRoleId().intValue());
        List<FiedValue> list = new ArrayList<>();
        calculateBase(role);
        list.add(new FiedValue("gg", (byte)2, Short.valueOf(role.getGg().shortValue())));
        list.add(new FiedValue("lx", (byte)2, Short.valueOf(role.getLx().shortValue())));
        list.add(new FiedValue("ll", (byte)2, Short.valueOf(role.getLl().shortValue())));
        list.add(new FiedValue("mj", (byte)2, Short.valueOf(role.getMj().shortValue())));
        list.add(new FiedValue("fpoint", (byte)2, Short.valueOf(role.getFpoint().shortValue())));
        list.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countLife(role))));
        list.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countMana(role))));
        list.add(new FiedValue("curr_life", (byte)3, role.getCurrLife()));
        list.add(new FiedValue("curr_mana", (byte)3, role.getCurrMana()));
        respUpdate.setList(list);
        role.save();
        MessagePusher.pushMessage(role, (Message)respUpdate);
      } 
    } else {
      Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(assignAttrib.getId()));
      if (pet != null) {
        if (pet.getFpoint().intValue() < count)
          return; 
        Integer restorePorCount2 = Integer.valueOf(FormulaHero.restorePorCount(pet.getRebirth().intValue(), pet.getPetLv().intValue(), LOGIC.LOGICTYPE_PET.getValue()));
        if (count > pet.getFpoint().intValue() || pet.getGg().intValue() - pet.getPetLv().intValue() + pet.getMj().intValue() - pet.getPetLv().intValue() + pet.getLl().intValue() - pet.getPetLv().intValue() + pet.getLx().intValue() - pet.getPetLv().intValue() + count > restorePorCount2.intValue()) {
          msg = "加点失败";
          this.logger.error("角色:[{}],角色id:[{}],宠物Id:[{}],加点超出:[{}],未分配点:[{}]", new Object[] { role.getName(), role.getRoleId(), pet.getId(), Integer.valueOf(count), pet.getFpoint() });
        } else {
          if (assignAttrib.getGg() > 0)
            pet.setGg(Integer.valueOf(pet.getGg().intValue() + assignAttrib.getGg())); 
          if (assignAttrib.getLx() > 0)
            pet.setLx(Integer.valueOf(pet.getLx().intValue() + assignAttrib.getLx())); 
          if (assignAttrib.getLl() > 0)
            pet.setLl(Integer.valueOf(pet.getLl().intValue() + assignAttrib.getLl())); 
          if (assignAttrib.getMj() > 0)
            pet.setMj(Integer.valueOf(pet.getMj().intValue() + assignAttrib.getMj())); 
          int fpoin = pet.getFpoint().intValue() - count;
          if (fpoin < 0) {
            this.logger.error("宠物分配点数扣除后小于0宠物唯一编号 = {}", pet.getId());
            fpoin = 0;
          } 
          pet.setFpoint(Integer.valueOf(fpoin));
          this.petService.calculateBase(role.getId().longValue(), pet);
          this.petService.getArenaScore(role.getId().longValue(), pet);
          this.petService.sendPetPanels(role, pet);
          pet.save();
        } 
      } 
    } 
    MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(msg));
  }
  
  public void extendStoreSize(Role role, ReqExtendStoreSize extendStoreSize) {
    boolean flag = false;
    if (LOGIC.LOGICTYPE_HERO.getValue() == extendStoreSize.type) {
      if (extendStoreSize.isStore == null || extendStoreSize.isStore.intValue() == 0) {
        int num = role.getExtendBox().getPosition().intValue() + 1;
        Item item = (Item)ConvertDataPool.bagExtend.get(Integer.valueOf(num));
        if (item != null && deductCurrency(role, item.getItem(), item.getNum().intValue(), "扩展背包格子")) {
          role.getExtendBox().setPosition(Integer.valueOf(num));
          flag = true;
        } else {
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("扩展背包格子已上限"));
        } 
      } else {
        int numStore = role.getExtendBox().getStorePosition().intValue() + 1;
        Item itemStore = (Item)ConvertDataPool.storeExtend.get(Integer.valueOf(numStore));
        if (itemStore != null && deductCurrency(role, itemStore.getItem(), itemStore.getNum().intValue(), "扩展仓库格子")) {
          role.getExtendBox().setStorePosition(Integer.valueOf(numStore));
          flag = true;
        } else {
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("扩展仓库格子已上限"));
        } 
      } 
    } else if (LOGIC.LOGICTYPE_PET.getValue() == extendStoreSize.type) {
      if (extendStoreSize.isStore == null || extendStoreSize.isStore.intValue() == 0) {
        int buyPetGrid = role.getExtendBox().getBuyPetGrid();
        if (buyPetGrid + 1 > Const.BUY_PET_GRID.intValue()) {
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("扩展宠物携带已上限"));
          return;
        } 
        Long money = Const.ADD_PET_LIMIT_COST[buyPetGrid];
        if (role.getSilver().longValue() >= money.longValue()) {
          deductCurrency(role, Const.RESTYPE_SILVER, money.longValue(), "扩展宠物格子");
          role.getExtendBox().setBuyPetGrid(buyPetGrid + 1);
          flag = true;
        } else {
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("银两不足"));
        } 
      } else {
        int num = role.getExtendBox().getPetEquipPosition().intValue() + 1;
        Item item = (Item)ConvertDataPool.petBagEquipExtend.get(Integer.valueOf(num));
        if (item != null && deductCurrency(role, item.getItem(), item.getNum().intValue(), "扩展背包格子")) {
          role.getExtendBox().setPetEquipPosition(Integer.valueOf(num));
          flag = true;
        } else {
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("扩展背包格子已上限"));
        } 
      } 
    } 
    if (flag) {
      RespUpdate respUpdate = new RespUpdate();
      respUpdate.setId(role.getRoleId().intValue());
      List<FiedValue> list = new ArrayList<>();
      list.add(new FiedValue("buyPetGrid", (byte)3, Integer.valueOf(role.getExtendBox().getBuyPetGrid())));
      list.add(new FiedValue("buyBagGrid", (byte)3, role.getExtendBox().getPosition()));
      list.add(new FiedValue("buyStoreGrid", (byte)3, role.getExtendBox().getStorePosition()));
      list.add(new FiedValue("buyPetEquipGrid", (byte)3, role.getExtendBox().getPetEquipPosition()));
      respUpdate.setList(list);
      MessagePusher.pushMessage(role, (Message)respUpdate);
      role.save();
    } 
  }
  
  public void updateDrill(Role role) {
    role.getExtendBox().setDrill((byte)1);
    SpringUtils.getPlayerService().updateRole(role);
    role.save();
  }
  
  public void openAutoFightNew(Role role, Fight fight) {
    FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
    try {
      FightObject fightObject = fight.queryFightObject(role.getRoleId());
      if (fightObject == null)
        return; 
      if (role.getExtendBox().getAutoFight() == 1) {
        fightObject.setAutoFight(1);
        ScheduledFuture<?> timer = fight.getTimeOutTask();
        SkillAutoPicker autoPicker = fightNewService.getAutoObject(fightObject, fight);
        ReqFightHandle reqFightHandle = new ReqFightHandle();
        reqFightHandle.setRound(fight.getRound());
        reqFightHandle.setAutoFight(1);
        reqFightHandle.setAttackerId(fightObject.getId().intValue());
        reqFightHandle.setTargetId(autoPicker.getTargetId());
        reqFightHandle.setSkillId(autoPicker.getSkillId());
        reqFightHandle.setType(autoPicker.getType());
        List<RespFightCommandInfo> fightCommandInfos = new ArrayList<>();
        if (timer != null && (!timer.isDone() || !timer.isCancelled()) && !fightObject.isDoAction()) {
          if (autoPicker.isDef()) {
            reqFightHandle.setType(1);
          } else if (reqFightHandle.getType() == 0) {
            reqFightHandle.setType(0);
          } else {
            reqFightHandle.setType(7);
          } 
          fightNewService.doActionNew(reqFightHandle);
        } 
        RespFightCommandInfo fightCommandInfoRole = new RespFightCommandInfo();
        fightCommandInfoRole.setId(fightObject.getId().intValue());
        fightCommandInfoRole.setSkillId(autoPicker.getSkillId());
        fightCommandInfoRole.setType(autoPicker.getType());
        fightCommandInfoRole.setAction(1);
        if (role.getRace().intValue() == RACE.RACE_LONG.getValue()) {
          Map<String, Object> autoSet = role.getExtendBox().getAutoFightSet();
          Object born = autoSet.get("born");
          if (born != null) {
            fightCommandInfoRole.setBorn(Integer.valueOf(String.valueOf(born)).intValue());
          } else {
            fightCommandInfoRole.setBorn(1);
          } 
        } 
        fightCommandInfos.add(fightCommandInfoRole);
        if (fightObject.getFightPetId() > 0) {
          FightObject petObject = fight.queryFightObject(Integer.valueOf(fightObject.getFightPetId()));
          if (petObject != null && !petObject.isFallDown()) {
            petObject.setAutoFight(1);
            autoPicker = fightNewService.getAutoObject(petObject, fight);
            if (timer != null && (!timer.isDone() || !timer.isCancelled()) && !petObject.isDoAction()) {
              reqFightHandle = new ReqFightHandle();
              reqFightHandle.setRound(fight.getRound());
              reqFightHandle.setAutoFight(1);
              reqFightHandle.setAttackerId(petObject.getId().intValue());
              reqFightHandle.setTargetId(autoPicker.getTargetId());
              reqFightHandle.setType(autoPicker.getType());
              reqFightHandle.setSkillId(autoPicker.getSkillId());
              if (autoPicker.isDef()) {
                reqFightHandle.setType(1);
              } else if (reqFightHandle.getType() == 7) {
                reqFightHandle.setType(7);
              } else {
                reqFightHandle.setType(0);
              } 
              fightNewService.doActionNew(reqFightHandle);
            } 
            RespFightCommandInfo fightCommandInfoPet = new RespFightCommandInfo();
            fightCommandInfoPet.setId(petObject.getId().intValue());
            fightCommandInfoPet.setSkillId(autoPicker.getSkillId());
            fightCommandInfoPet.setType(autoPicker.getType());
            fightCommandInfoPet.setAction(1);
            fightCommandInfos.add(fightCommandInfoPet);
          } 
        } 
        if (fightCommandInfos.size() <= 0) {
          MessageAssistUtil.sendIsSuccess(role, fightObject.getId().intValue(), (byte)5, null);
        } else {
          MessageAssistUtil.sendIsSuccess(role, fightObject.getId().intValue(), (byte)5, fightCommandInfos);
        } 
      } else {
        fightObject.setAutoFight(0);
        if (fightObject.getFightPetId() > 0) {
          FightObject petObject2 = fight.queryFightObject(Integer.valueOf(fightObject.getFightPetId()));
          if (petObject2 != null)
            petObject2.setAutoFight(0); 
        } 
        if (fightObject.isDoAction()) {
          MessageAssistUtil.sendIsSuccess(role, fightObject.getId().intValue(), (byte)7, null);
        } else {
          MessageAssistUtil.sendIsSuccess(role, fightObject.getId().intValue(), (byte)6, null);
          fight.removeHostTask(fightObject.getId().intValue());
          fight.removePetTask(fightObject.getFightPetId());
        } 
      } 
    } catch (Exception e) {
      this.logger.error("战斗中设置自动战斗错误=让目标防御={}", Lang.getStackTrace(e));
      ReqFightHandle reqFightHandle2 = new ReqFightHandle();
      reqFightHandle2.setRound(fight.getRound());
      reqFightHandle2.setAutoFight(1);
      reqFightHandle2.setAttackerId(role.getRoleId().intValue());
      reqFightHandle2.setTargetId(role.getRoleId().intValue());
      reqFightHandle2.setSkillId(0);
      reqFightHandle2.setType(1);
      fightNewService.doActionNew(reqFightHandle2);
    } 
  }
  
  public void dye(Role role, ReqDey reqDey) {
    boolean clicker = mouserClicker(role, "dye", 250L);
    if (!clicker)
      return; 
    int a = 0;
    int b = 0;
    int c = 0;
    long silver = 0L;
    SysHero sysHero = (SysHero)PartDataPool.heroMap.get(Integer.valueOf(11001));
    if (sysHero != null) {
      Gson gson = new Gson();
      NutMap map = (NutMap)gson.fromJson(role.getColor(), NutMap.class);
      int curve_a = map.getInt("a");
      if (curve_a > 0 && reqDey.getColorOne() <= 0) {
        silver += 250000L;
        map.setv("a", Integer.valueOf(0));
      } 
      int curve_b = map.getInt("b");
      if (curve_b > 0 && reqDey.getColorTwo() <= 0) {
        silver += 250000L;
        map.setv("b", Integer.valueOf(0));
      } 
      SysRoleChangeColor color = (SysRoleChangeColor)PartDataPool.roleChangeColor.get(sysHero.getShape());
      if (reqDey.getColorOne() > 0) {
        List<ChangeColor> colors = JSON.parseArray(color.getColorOne(), ChangeColor.class);
        Optional<ChangeColor> optional = colors.stream().filter(data -> {
              boolean b2;
              if (data.getSerial() == reqDey.getColorOne()) {
                b2 = true;
              } else {
                b2 = false;
              } 
              return b2;
            }).findFirst();
        if (optional.isPresent()) {
          ChangeColor changeColor = optional.get();
          map.setv("a", Integer.valueOf(changeColor.getSerial()));
          a += changeColor.getGoodsNum()[0];
          b += changeColor.getGoodsNum()[1];
          c += changeColor.getGoodsNum()[2];
        } 
      } 
      if (reqDey.getColorTwo() > 0) {
        List<ChangeColor> colors = JSON.parseArray(color.getColorTwo(), ChangeColor.class);
        Optional<ChangeColor> optional = colors.stream().filter(data -> {
              boolean b3;
              if (data.getSerial() == reqDey.getColorTwo()) {
                b3 = true;
              } else {
                b3 = false;
              } 
              return b3;
            }).findFirst();
        if (optional.isPresent()) {
          ChangeColor changeColor = optional.get();
          map.setv("b", Integer.valueOf(changeColor.getSerial()));
          a += changeColor.getGoodsNum()[0];
          b += changeColor.getGoodsNum()[1];
          c += changeColor.getGoodsNum()[2];
        } 
      } 
      if (silver < 0L) {
        this.logger.error("染色货币数量负数{}", Long.valueOf(silver));
        return;
      } 
      if (silver > 0L && role.getSilver().longValue() <= silver) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "客官，您的银两不足", false));
        return;
      } 
      Integer a_nu = this.bagService.getBagGoods(role, 75055, a);
      Integer b_nu = this.bagService.getBagGoods(role, 75056, b);
      Integer c_nu = this.bagService.getBagGoods(role, 75057, c);
      if (a_nu == null && b_nu == null && c_nu == null) {
        this.bagService.consumeBagGoods(role, 75055, a, true, true, null);
        this.bagService.consumeBagGoods(role, 75056, b, true, true, null);
        this.bagService.consumeBagGoods(role, 75057, c, true, true, null);
        deductCurrency(role, Const.RESTYPE_SILVER, silver, null);
        role.setColor(map.toString());
        role.save();
        RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(role.getRoleId().intValue());
        List<FiedValue> list = deyList(role.getColor());
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, (Message)respUpdate);
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "染色成功", false));
        EventDispatcher.getInstance().fireEvent((BaseGameEvent)new RoleAspectChangeEvent(EventType.ROLE_ASPECT_CHANGE, role));
      } else {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "客官，您的染料不足，巧妇难为无米之炊啊", false));
      } 
    } 
  }
  
  public void practiceUpGrade(Role role, int grade) {
    PartyPractice practiceInfo = role.getRest().getPartyPractice();
    Map<Integer, SysPartyPractice> map = ExpDataPool.partyPractice;
    Integer partyPracticeLv = Integer.valueOf(practiceInfo.getLevel());
    int sumGrade = grade + partyPracticeLv.intValue();
    SysPartyPractice practice = map.get(Integer.valueOf(sumGrade));
    int need = role.getRelive().intValue() * 1000 + role.getLevel().intValue();
    if (need < practice.getNeed()) {
      int level = practice.getNeed() % 1000;
      int relive = practice.getNeed() / 1000;
      String msg = "你的#<R>角色等级#不足";
      if (relive > 0) {
        msg = msg + "#<R>" + relive + "转" + level + "级#";
      } else {
        msg = msg + "#<R>" + level + "级#";
      } 
      MessagePusher.pushMessage(role, (Message)new RespMsg(msg));
      return;
    } 
    int needTribute = 0;
    int needCoin = 0;
    for (int i = partyPracticeLv.intValue() + 1; i <= sumGrade; i++) {
      SysPartyPractice partyPractice = map.get(Integer.valueOf(i));
      if (partyPractice != null) {
        needTribute += partyPractice.getTribute();
        needCoin += partyPractice.getCoin();
      } 
    } 
    if (!checkCurrency(role, Const.RESTYPE_COIN, needCoin))
      return; 
    if (!checkCurrency(role, Const.RESTYPE_TRIBUTE, needTribute))
      return; 
    deductCurrency(role, Const.RESTYPE_COIN, needCoin, "帮派修炼提升：扣除贡币");
    deductCurrency(role, Const.RESTYPE_TRIBUTE, needTribute, "帮派修炼提升：扣除帮贡");
    practiceInfo.setPoints(practiceInfo.getPoints() + sumGrade - partyPracticeLv.intValue());
    practiceInfo.setLevel(sumGrade);
    role.save();
    refreshPartyPracticeInfo(role);
  }
  
  public void assignPartyPractice(Role role, String nutMap) {
    int points2;
    Map<Integer, Integer> practiceMap = role.getRest().getParty();
    PartyPractice partyPractice = role.getRest().getPartyPractice();
    NutMap map = (NutMap)Json.fromJson(NutMap.class, nutMap);
    List<Integer> list = FACTION_PRO_DICT.toList();
    int count = 0;
    for (String key : map.keySet()) {
      Integer pro = Integer.valueOf(key);
      boolean contains = list.contains(pro);
      if (!contains) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("修炼属性中没有该修炼类型"));
        return;
      } 
      int num = map.getInt(key);
      if (num < 0)
        return; 
      if (FACTION_PRO_DICT.K_KZ_CAP.getPro() == pro.intValue()) {
        int limit = partyPractice.getLevel() / 5;
        if (num > limit) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("每提升修炼等级5级可分配1点上限，当前已达上限"));
          return;
        } 
      } 
      count += num - ((Integer)practiceMap.get(pro)).intValue();
    } 
    if (count > partyPractice.getPoints()) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("剩余修炼点不足"));
      return;
    } 
    for (String key : map.keySet()) {
      int num2 = map.getInt(key);
      if (num2 > 0)
        practiceMap.put(Integer.valueOf(key), Integer.valueOf(num2)); 
    } 
    int points = partyPractice.getPoints() - count;
    PartyPractice partyPractice2 = partyPractice;
    if (points < 0) {
      points2 = 0;
    } else {
      points2 = points;
    } 
    partyPractice2.setPoints(points2);
    List<FiedValue> faction = faction(role, new ArrayList<>());
    MessagePusher.pushMessage(role, (Message)new RespUpdateImprovement(role.getRoleId().intValue(), faction));
    refreshPartyPracticeInfo(role);
    role.save();
  }
  
  public void resetAssignPartyPractice(Role role) {
    if (!SafeUtil.isUnlock(role))
      return; 
    boolean currency = deductCurrency(role, Const.RESTYPE_SILVER, 200000L, "重置帮派修炼加点");
    if (!currency)
      return; 
    PartyPractice practice = role.getRest().getPartyPractice();
    practice.setPoints(practice.getLevel());
    role.getRest().setParty(DataGlobalFunc.faction());
    List<FiedValue> list = faction(role, new ArrayList<>());
    MessagePusher.pushMessage(role, (Message)new RespUpdateImprovement(role.getRoleId().intValue(), list));
    MessagePusher.pushMessage(role, (Message)new RespMsg("重置成功"));
    refreshPartyPracticeInfo(role);
    role.save();
  }
  
  public void restAttribute(Role role, byte type) {
    if (!SafeUtil.isUnlock(role))
      return; 
    String msg = null;
    int serial = Const.rest_attribute[type];
    if (type == 0) {
      Integer count = Integer.valueOf(FormulaHero.restorePorCount(role.getRelive().intValue(), role.getLevel().intValue(), LOGIC.LOGICTYPE_HERO.getValue()));
      if (role.getFpoint().equals(count)) {
        msg = "你没有可以重置的属性点~";
      } else if (this.bagService.consumeBagGoods(role, serial, 1, false, true, "超级星梦石") == null) {
        role.setGg(role.getLevel());
        role.setLx(role.getLevel());
        role.setLl(role.getLevel());
        role.setMj(role.getLevel());
        role.setFpoint(count);
      } else {
        msg = "你没有超级星梦石";
      } 
    } else if (type == 1) {
      if (role.getLevel().equals(role.getGg())) {
        msg = "你没有可以重置的属性点~";
      } else if (this.bagService.consumeBagGoods(role, serial, 1, false, true, "根骨星梦石") == null) {
        Integer count = Integer.valueOf(5);
        if (role.getGg().intValue() - 5 < role.getLevel().intValue())
          count = Integer.valueOf(role.getGg().intValue() - role.getLevel().intValue()); 
        role.setGg(Integer.valueOf(role.getGg().intValue() - count.intValue()));
        role.setFpoint(Integer.valueOf(count.intValue() + role.getFpoint().intValue()));
      } else {
        msg = "你没有根骨星梦石";
      } 
    } else if (type == 2) {
      if (role.getLevel().equals(role.getLx())) {
        msg = "你没有可以重置的属性点~";
      } else if (this.bagService.consumeBagGoods(role, serial, 1, false, true, "灵力星梦石") == null) {
        Integer count = Integer.valueOf(5);
        if (role.getLx().intValue() - 5 < role.getLevel().intValue())
          count = Integer.valueOf(role.getLx().intValue() - role.getLevel().intValue()); 
        role.setLx(Integer.valueOf(role.getLx().intValue() - count.intValue()));
        role.setFpoint(Integer.valueOf(count.intValue() + role.getFpoint().intValue()));
      } else {
        msg = "你没有灵力星梦石";
      } 
    } else if (type == 3) {
      if (role.getLevel().equals(role.getLl())) {
        msg = "你没有可以重置的属性点~";
      } else if (this.bagService.consumeBagGoods(role, serial, 1, false, true, "力量星梦石") == null) {
        Integer count = Integer.valueOf(5);
        if (role.getLl().intValue() - 5 < role.getLevel().intValue())
          count = Integer.valueOf(role.getLl().intValue() - role.getLevel().intValue()); 
        role.setLl(Integer.valueOf(role.getLl().intValue() - count.intValue()));
        role.setFpoint(Integer.valueOf(count.intValue() + role.getFpoint().intValue()));
      } else {
        msg = "你没有力量星梦石";
      } 
    } else if (type == 4) {
      if (role.getLevel().equals(role.getMj())) {
        msg = "你没有可以重置的属性点~";
      } else if (this.bagService.consumeBagGoods(role, serial, 1, false, true, "敏捷星梦石") == null) {
        Integer count = Integer.valueOf(5);
        if (role.getMj().intValue() - 5 < role.getLevel().intValue())
          count = Integer.valueOf(role.getMj().intValue() - role.getLevel().intValue()); 
        role.setMj(Integer.valueOf(role.getMj().intValue() - count.intValue()));
        role.setFpoint(Integer.valueOf(count.intValue() + role.getFpoint().intValue()));
      } else {
        msg = "你没有敏捷星梦石";
      } 
    } 
    if (msg != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg(msg));
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("重置成功"));
      calculateBase(role);
      refreshRole(role);
      role.save();
    } 
  }
  
  public void refreshPartyPracticeInfo(Role role) {
    PartyPractice practiceInfo = role.getRest().getPartyPractice();
    MessagePusher.pushMessage(role, (Message)new RespPartyPracticeInfo((short)practiceInfo.getLevel(), (short)practiceInfo.getPoints()));
  }
  
  public void setElseFight(Role role) {
    if (role.getExtendBox().getSetMap().size() <= 0) {
      role.getExtendBox().setSetMap(DataGlobalFunc.setting());
      role.save();
    } 
    RespElseFight elseFight = new RespElseFight();
    elseFight.setAutoFight(((Integer)role.getExtendBox().getSetMap().get("else_auto_fight")).intValue());
    elseFight.setShowDbSpeed(((Integer)role.getExtendBox().getSetMap().get("else_db_speed_show")).intValue());
    MessagePusher.pushMessage(role, (Message)elseFight);
  }
  
  public int getRoleId() {
    return SpringUtils.getIdentityService().getNextId(IdentityType.ROLE);
  }
  
  public Role getOnlinePlayer(String name) {
    long uid = SpringUtils.getPlayerService().getUidBy(name);
    return getOnlinePlayer(uid);
  }
  
  public boolean checkCurrency(Role role, Integer type, long price) {
    if (type.equals(Const.RESTYPE_GOLD) && role.getGold().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RESTYPE_SILVER) && role.getSilver().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RESTYPE_COIN) && role.getCoin().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RESTYPE_GUO) && role.getExtendBox().getGuoScore().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RESTYPE_BRAC) && role.getExtendBox().getBracScore().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RESTYPE_NAO) && role.getExtendBox().getNaoScore().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RESTYPE_DI) && role.getExtendBox().getDiScore().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RESTYPE_ARENA) && role.getExtendBox().getArenaScore().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RESTYPE_LONG) && role.getExtendBox().getLongScore().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RESTYPE_NICE) && role.getExtendBox().getNiceScore().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RESTYPE_TRIBUTE) && role.getExtendBox().getHelpScore().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RES_TYPE_FEATS) && role.getExtendBox().getFeatsScore().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RES_TYPE_CHARGE_SCORE) && role.getExtendBox().getFeatsScore().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RES_TYPE_BOUND_GOLD) && role.getExtendBox().getBoundGold().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    if (type.equals(Const.RES_TYPE_ACHIEVEMENT) && role.getExtendBox().getAchievementScore().longValue() < price) {
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(price)) });
      return false;
    } 
    return true;
  }
  
  public boolean deductCurrency(Role role, Integer type, long num, String string) {
    if (num <= 0L && string != null) {
      this.logger.error("扣除货币负数，角色 = {}, 方法 = {}，货币类型 = {}，数量 = {}", new Object[] { role.getName(), string, type, Long.valueOf(num) });
      return false;
    } 
    long residue = 0L;
    if (type.equals(Const.RESTYPE_GOLD) && role.getGold().longValue() >= num) {
      residue = role.getGold().longValue() - num;
      role.setGold(Long.valueOf(residue));
    } else if (type.equals(Const.RESTYPE_SILVER) && role.getSilver().longValue() >= num) {
      residue = role.getSilver().longValue() - num;
      role.setSilver(Long.valueOf(residue));
    } else if (type.equals(Const.RESTYPE_COIN) && role.getCoin().longValue() >= num) {
      residue = role.getCoin().longValue() - num;
      role.setCoin(Long.valueOf(residue));
    } else if (type.equals(Const.RESTYPE_GUO) && role.getExtendBox().getGuoScore().longValue() >= num) {
      residue = role.getExtendBox().getGuoScore().longValue() - num;
      role.getExtendBox().setGuoScore(Long.valueOf(residue));
    } else if (type.equals(Const.RESTYPE_BRAC) && role.getExtendBox().getBracScore().longValue() >= num) {
      residue = role.getExtendBox().getBracScore().longValue() - num;
      role.getExtendBox().setBracScore(Long.valueOf(residue));
    } else if (type.equals(Const.RESTYPE_NAO) && role.getExtendBox().getNaoScore().longValue() >= num) {
      residue = role.getExtendBox().getNaoScore().longValue() - num;
      role.getExtendBox().setNaoScore(Long.valueOf(residue));
    } else if (type.equals(Const.RESTYPE_DI) && role.getExtendBox().getDiScore().longValue() >= num) {
      residue = role.getExtendBox().getDiScore().longValue() - num;
      role.getExtendBox().setDiScore(Long.valueOf(residue));
    } else if (type.equals(Const.RESTYPE_ARENA) && role.getExtendBox().getArenaScore().longValue() >= num) {
      residue = role.getExtendBox().getArenaScore().longValue() - num;
      role.getExtendBox().setArenaScore(Long.valueOf(residue));
    } else if (type.equals(Const.RESTYPE_LONG) && role.getExtendBox().getLongScore().longValue() >= num) {
      residue = role.getExtendBox().getLongScore().longValue() - num;
      role.getExtendBox().setLongScore(Long.valueOf(residue));
    } else if (type.equals(Const.RESTYPE_NICE) && role.getExtendBox().getNiceScore().longValue() >= num) {
      residue = role.getExtendBox().getNiceScore().longValue() - num;
      role.getExtendBox().setNiceScore(Long.valueOf(residue));
    } else if (type.equals(Const.RESTYPE_TRIBUTE) && role.getExtendBox().getHelpScore().longValue() >= num) {
      residue = role.getExtendBox().getHelpScore().longValue() - num;
      role.getExtendBox().setHelpScore(Long.valueOf(residue));
    } else if (type.equals(Const.RES_TYPE_FEATS) && role.getExtendBox().getFeatsScore().longValue() >= num) {
      residue = role.getExtendBox().getFeatsScore().longValue() - num;
      role.getExtendBox().setFeatsScore(Long.valueOf(residue));
    } else if (type.equals(Const.RES_TYPE_CHARGE_SCORE) && role.getExtendBox().getChargeScore().longValue() >= num) {
      residue = role.getExtendBox().getChargeScore().longValue() - num;
      role.getExtendBox().setChargeScore(Long.valueOf(residue));
    } else if (type.equals(Const.RES_TYPE_BOUND_GOLD) && role.getExtendBox().getBoundGold().longValue() >= num) {
      residue = role.getExtendBox().getBoundGold().longValue() - num;
      role.getExtendBox().setBoundGold(Long.valueOf(residue));
    } else {
      if (type.equals(Const.RES_TYPE_ACHIEVEMENT) && role.getExtendBox().getAchievementScore().longValue() >= num) {
        residue = role.getExtendBox().getAchievementScore().longValue() - num;
        role.getExtendBox().setAchievementScore(Long.valueOf(residue));
        return true;
      } 
      MessagePusher.notify2Misc(role, false, type.intValue(), new Object[] { MoneyUtil.colours(Long.valueOf(num)) });
      return false;
    } 
    role.save();
    refreshRoleCurrency(role);
    RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1000, (byte)2, String.format("货币类型：%d，消耗数量：%d，减少后数量%d，原因：%s", new Object[] { type, Long.valueOf(num), Long.valueOf(residue), string }), role.getMarking());
    return true;
  }
  
  public Role getOnlinePlayer(long id) {
    if (DataCache.ONLINE_ROLES.containsKey(Long.valueOf(id)))
      return (Role)DataCache.ONLINE_ROLES.get(Long.valueOf(id)); 
    if (DataCache.OFFLINE_ROLES.containsKey(Long.valueOf(id)))
      return null; 
    return SpringUtils.getPlayerService().getPlayerBy(id);
  }
  
  public Role getPlayer(long id) {
    if (DataCache.OFFLINE_ROLES.containsKey(Long.valueOf(id)))
      return (Role)DataCache.OFFLINE_ROLES.get(Long.valueOf(id)); 
    if (DataCache.ONLINE_ROLES.containsKey(Long.valueOf(id)))
      return (Role)DataCache.ONLINE_ROLES.get(Long.valueOf(id)); 
    return SpringUtils.getPlayerService().getPlayerBy(id);
  }
  
  public Role getOnlinePlayer(int roleId) {
    long id = SpringUtils.getPlayerService().getUidBy(roleId);
    if (DataCache.OFFLINE_ROLES.containsKey(Long.valueOf(id)))
      return null; 
    if (DataCache.ONLINE_ROLES.containsKey(Long.valueOf(id)))
      return (Role)DataCache.ONLINE_ROLES.get(Long.valueOf(id)); 
    return SpringUtils.getPlayerService().getPlayerBy(id);
  }
  
  public Role getOfOnlinePlayer(int roleId) {
    long id = SpringUtils.getPlayerService().getUidBy(roleId);
    if (DataCache.OFFLINE_ROLES.containsKey(Long.valueOf(id)))
      return (Role)DataCache.OFFLINE_ROLES.get(Long.valueOf(id)); 
    Role role = SpringUtils.getPlayerService().getPlayerBy(id);
    return role;
  }
  
  public Role getPlayer(int roleId) {
    long id = SpringUtils.getPlayerService().getUidBy(roleId);
    if (isOnline(id))
      return getOnlinePlayer(id); 
    return getOfOnlinePlayer(roleId);
  }
  
  public void recordPosition(Role role, ReqMultiMoveTo reqMultiMoveTo) {
    role.setPos(reqMultiMoveTo.getX(), reqMultiMoveTo.getY(), 0, reqMultiMoveTo.getMapId());
    if (this.teamService.isTeamLeader(role)) {
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      if (team != null)
        for (int i = 0; i < team.getList().size(); i++) {
          Member member = team.getList().get(i);
          if (member != null && 
            !member.getId().equals(role.getId()) && 
            member.isInTeam()) {
            Role memberRole = getPlayer(member.getId().longValue());
            if (memberRole != null && 
              role.getPos().getMapId().equals(memberRole.getPos().getMapId())) {
              memberRole.setPos(reqMultiMoveTo.getX(), reqMultiMoveTo.getY(), 0, reqMultiMoveTo.getMapId());
              SceneManager.INSTANCE.movePosition(memberRole);
            } 
          } 
        }  
    } 
  }
  
  public void punishFightDead(Role role, long exp, String reason) {
    if (role.getLastDead() > 0)
      return; 
    addExp(role, exp, reason, true);
  }
  
  public void sendOffline(Role role, boolean flag) {
    long millis = System.currentTimeMillis();
    String marking = SpringUtils.getBean(ServerService.class).getMarking();
    String key = role.getId() + "_countdown_" + marking + (millis / 1000L);
    if (this.redisService.exists(key))
      return; 
    this.redisService.set(key, "", 0L);
    ThreadLocalUtil.addLocalTask(role, () -> {
          int time = 300000;
          if (flag) {
            Team team = SpringUtils.getBean(TeamService.class).getTeam(role.getRoleId());
            if (team != null) {
              time = role.getOfflineNotice().getTeamOfflineTime();
            } else {
              time = role.getOfflineNotice().getAloneOfflineTime();
            } 
          } 
          role.setOfLineTime(millis);
          role.setState(2);
          role.save();
          SpringUtils.getBean(RabbitService.class).sendMessage(key, time);
        });
  }
  
  public long getOnlineSec(Role role) {
    long now = System.currentTimeMillis();
    if (now > role.getOnlineLoginTime()) {
      int currentOnlineSec = (int)((now - role.getOnlineLoginTime()) / 1000L);
      return currentOnlineSec + role.getPreOnlineSec();
    } 
    return 0L;
  }
  
  public void resetOnlineSec(Role role) {
    role.setPreOnlineSec(0L);
    role.setOnlineLoginTime(System.currentTimeMillis());
  }
  
  public void checkReset(Role role) {
    checkDailyFiveReset(role);
    checkWeeklyReset(role);
  }
  
  public void checkDailyFiveReset(Role role) {
    if (!isCanDailyFiveReset(role))
      return; 
    doDailyFiveReset(role, false);
  }
  
  public void resetQuotaLimit(Role role) {
    int monday = TimeUtil.dayInMillis(TimeUtil.getWeekday(2));
    int time = role.getExtendBox().getResetQuotaTime();
    if (monday != time) {
      role.getExtendBox().setQuotaLimit(DataGlobalFunc.setQuota());
      role.getExtendBox().setResetQuotaTime(monday);
    } 
  }
  
  public void doDailyFiveReset(Role role, boolean flag) {
    if (flag)
      checkWeeklyReset(role); 
    role.setDailyFiveResetTime(System.currentTimeMillis());
    resetOnlineSec(role);
    SpringUtils.getBean(SafeService.class).checkSafePasswordReset(role);
    resetQuotaLimit(role);
    role.getExtendBox().setDayQuotaLimit(NutMap.NEW());
    SpringUtils.getBean(ActivityService.class).dailyResetActivity(role);
    SpringUtils.getBean(TaskService.class).dailyResetTask(role);
    role.save();
    SpringUtils.getBean(WelfareService.class).dailyResetFiveGuess(role);
  }
  
  private boolean isCanDailyFiveReset(Role role) {
    long dailyFiveResetTime = role.getDailyFiveResetTime();
    Calendar roleResetCalader = Calendar.getInstance();
    roleResetCalader.setTimeInMillis(dailyFiveResetTime);
    Calendar systemResetTime = getSystemFiveResetTime();
    return roleResetCalader.before(systemResetTime);
  }
  
  private Calendar getSystemFiveResetTime() {
    Calendar calendar = Calendar.getInstance();
    if (calendar.get(11) < 5)
      calendar.add(6, -1);
    calendar.set(11, 5);
    calendar.set(12, 0);
    calendar.set(13, 0);
    calendar.set(14, 0);
    return calendar;
  }
  
  public void checkWeeklyReset(Role role) {
    if (!isCanWeeklyReset(role)) {
      this.logger.info("不能进行每周重置，角色ID: {}, 上一次重置时间戳：{}", role.getRoleId(), Long.valueOf(role.getWeeklyResetTime()));
      return;
    } 
    doWeeklyReset(role);
  }
  
  public boolean isCanWeeklyReset(Role role) {
    long weeklyResetTime = role.getWeeklyResetTime();
    Calendar roleResetCalader = Calendar.getInstance();
    roleResetCalader.setTimeInMillis(weeklyResetTime);
    Calendar systemResetTime = getSystemWeeklyResetTime();
    return roleResetCalader.before(systemResetTime);
  }
  
  public Calendar getSystemWeeklyResetTime() {
    Calendar calendar = Calendar.getInstance();
    if (calendar.get(7) < 2 || (calendar.get(7) == 2 && calendar.get(11) < 5))
      calendar.add(6, -7); 
    calendar.set(7, 2);
    calendar.set(11, 5);
    calendar.set(12, 0);
    calendar.set(13, 0);
    calendar.set(14, 0);
    return calendar;
  }
  
  public void doWeeklyReset(Role role) {
    this.logger.warn("玩家{}进行每周重置，上一重置时间为{}", role.getRoleId(), Long.valueOf(role.getWeeklyResetTime()));
    role.getTaskProgress().setLoop(1);
    role.setWeeklyResetTime(System.currentTimeMillis());
    role.getPkInfoBox().setKillRoles(new HashMap<>());
    role.getPkInfoBox().setBeKillRoles(new HashMap<>());
    role.save();
    this.petService.doWeeklyReset(role);
  }
  
  public Role getPlayerBy(int roleId) {
    long id = SpringUtils.getPlayerService().getUidBy(roleId);
    return getPlayerBy(id);
  }
  
  public Role getPlayerBy(long id) {
    return SpringUtils.getPlayerService().getPlayerBy(id);
  }
  
  public void modifyName(Role role, String newName) {
    if (!SafeUtil.isUnlock(role))
      return; 
    Integer num = this.bagService.getBagGoodsNum(role, Integer.valueOf(90011));
    IoSession session = SessionUtils.getSessionBy(role.getId().longValue());
    if (!canCreateRole(session, newName, LOGIC.LOGICTYPE_HERO.getValue()))
      return; 
    newName = CheckCharUtils.replaceName(newName);
    if (num.intValue() >= 1) {
      Integer integer = this.bagService.consumeBagGoods(role, 90011, 1, false, true, "角色改名");
      if (integer != null)
        return; 
    } else {
      boolean flag = deductCurrency(role, Const.RESTYPE_GOLD, 600L, "角色改名");
      if (!flag)
        return; 
    } 
    String oldName = role.getName();
    role.setName(newName);
    role.save();
    SpringUtils.getPlayerService().updateRoleName(oldName, role);
    MessagePusher.pushMessage(role, (Message)new RespHint(3, (short)1));
    this.broadcastService.sendUpdateAppear(role);
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new com.kitty.listener.event.RoleChangeEvent(EventType.ROLE_CHANGE, role, oldName, newName, com.kitty.listener.event.RoleChangeEvent.NAME));
  }
  
  public void checkReserve(Role role) {
    String type = "即将";
    if (role.getSaveLife().longValue() <= 250000L) {
      String s;
      boolean flag = true;
      int i18nId = 100;
      Object[] args = { "气血", null };
      int n = 1;
      if (role.getSaveLife().longValue() > 0L) {
        s = type;
      } else {
        s = "已";
      } 
      args[n] = s;
      MessagePusher.notify2Misc(role, flag, i18nId, args);
    } 
    if (role.getSaveMana().longValue() <= 250000L) {
      String s2;
      boolean flag2 = true;
      int i18nId2 = 100;
      Object[] args2 = { "法力", null };
      int n2 = 1;
      if (role.getSaveMana().longValue() > 0L) {
        s2 = type;
      } else {
        s2 = "已";
      } 
      args2[n2] = s2;
      MessagePusher.notify2Misc(role, flag2, i18nId2, args2);
    } 
  }
  
  public void chatEquipDetail(Role ownerRole, Role role, long equipId) {
    List<WearEquip> wearEquips = this.equipService.selectWearEquip(ownerRole);
    WearEquip wearEquip = null;
    int i = wearEquips.size() - 1;
    while (i >= 0) {
      if (((WearEquip)wearEquips.get(i)).getId().equals(Long.valueOf(equipId))) {
        wearEquip = wearEquips.get(i);
        break;
      } 
      i--;
    } 
    if (wearEquip != null) {
      RespMarketGoodCard card = new RespMarketGoodCard(String.valueOf(wearEquip.getId()), ownerRole.getRoleId().intValue(), wearEquip.getType(), wearEquip.getField(), wearEquip.getArtifice());
      MessagePusher.pushMessage(role, (Message)card);
      return;
    } 
    Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(ownerRole);
    BagEquip bagEquip = bagEquipMap.get(Long.valueOf(equipId));
    if (bagEquip != null) {
      RespMarketGoodCard card2 = new RespMarketGoodCard(String.valueOf(bagEquip.getId()), ownerRole.getRoleId().intValue(), bagEquip.getType(), bagEquip.getField(), bagEquip.getArtifice());
      MessagePusher.pushMessage(role, (Message)card2);
      return;
    } 
    MessagePusher.pushMessage(role, (Message)new RespMsg("此链接已失效！"));
  }
  
  public void goBackBorn(Role role, Pos pos) {
    if (role == null)
      return; 
    ReqTelePort reqTelePort = new ReqTelePort();
    reqTelePort.setMapId(pos.getMapId().intValue());
    reqTelePort.setX(pos.getX().intValue());
    reqTelePort.setY(pos.getY().intValue());
    SpringUtils.getMapService().changeMap(role, reqTelePort, null);
  }
  
  public void getAssistPetList(Role role, Integer serial) {
    SuitParam suit = null;
    RespAssistPetList respAssistPetList = new RespAssistPetList();
    respAssistPetList.setSerial(serial);
    boolean flag = true;
    if (serial.intValue() == 0) {
      List<SuitParam> suits = role.getExtendBox().getSuits();
      if (suits.size() <= 0) {
        flag = false;
        suits.add(new SuitParam());
      } 
      suit = suits.get(0);
    } else {
      List<SuitParam> suits = role.getExtendBox().getSuits();
      if (suits.size() <= 0) {
        flag = false;
        suits.add(new SuitParam());
      } 
      suit = suits.get(serial.intValue());
    } 
    Map<Integer, Pet> petMap = this.petService.selectPet(role);
    List<Integer> petList = null;
    if (suit != null && suit.getPetIds() != null) {
      petList = (List<Integer>)suit.getPetIds().stream().collect(Collectors.toList());
    } else {
      petList = new ArrayList<>();
    } 
    List<Integer> otherList = new ArrayList<>();
    if (petMap.size() > 0)
      for (Map.Entry<Integer, Pet> entry : petMap.entrySet()) {
        Pet pet = entry.getValue();
        if (pet.getSkills().size() > 0)
          for (PetSkill petSkill : pet.getSkills()) {
            if (SkillConst.assistSkills.contains(Integer.valueOf(petSkill.getSerial()))) {
              if (petList.contains(pet.getPetId())) {
                petList.remove(pet.getPetId());
                continue;
              } 
              otherList.add(pet.getPetId());
            } 
          }  
      }  
    boolean isUpdate = false;
    if (petList.size() > 0)
      for (Integer petId : petList) {
        suit.getPetIds().remove(petId);
        isUpdate = true;
      }  
    if (otherList.size() > 0) {
      if (suit.getPetIds() == null)
        suit.setPetIds(new ArrayList()); 
      for (Integer petId : otherList) {
        suit.getPetIds().add(petId);
        isUpdate = true;
      } 
    } 
    if (isUpdate)
      role.save(); 
    if (suit == null || suit.getPetIds() == null || suit.getPetIds().size() <= 0)
      flag = false; 
    if (!flag)
      MessagePusher.pushMessage(role, (Message)respAssistPetList); 
    respAssistPetList.setPetIds(suit.getPetIds());
    MessagePusher.pushMessage(role, (Message)respAssistPetList);
  }
  
  public void addSuit(Role role, Integer serial, String name, List<Integer> petIds) {
    RespAssistPetList respAssistPetList = new RespAssistPetList();
    if (serial == null) {
      List<SuitParam> suits = role.getExtendBox().getSuits();
      SuitParam suit = new SuitParam();
      suit.setSerial(Integer.valueOf(suits.size() + 1));
      suit.setName(name);
      suit.setPetIds(petIds);
      suits.add(suit);
      respAssistPetList.setSerial(suit.getSerial());
      respAssistPetList.setName(suit.getName());
      respAssistPetList.setPetIds(suit.getPetIds());
    } else {
      List<SuitParam> suits = role.getExtendBox().getSuits();
      SuitParam suit = null;
      if (serial.intValue() == 0) {
        suit = suits.get(0);
      } else {
        Optional<SuitParam> any = suits.stream().filter(obj -> obj.getSerial().equals(serial)).findAny();
        if (any.isPresent())
          suit = any.get(); 
      } 
      if (suit != null) {
        suit.setPetIds(petIds);
        suit.setName(name);
        respAssistPetList.setSerial(suit.getSerial());
        respAssistPetList.setName(suit.getName());
        respAssistPetList.setPetIds(suit.getPetIds());
      } else {
        respAssistPetList = null;
      } 
    } 
    MessagePusher.pushMessage(role, (Message)respAssistPetList);
  }
  
  public boolean mouserClicker(Role role, String type, long mouse) {
    long millis = System.currentTimeMillis();
    long time = ((Long)role.getTempCache(type, Long.valueOf(0L))).longValue();
    if (time > 0L && millis - time < mouse)
      return false; 
    if (millis - time <= 10L) {
      Integer warn = Integer.valueOf(((Integer)role.getTempCache("warn", Integer.valueOf(0))).intValue() + 1);
      role.pushTempCache("warn", warn);
      MessagePusher.pushMessage(role, (Message)new RespMsg("警告，你已经违规操作第#<R>" + warn + "#次，三次后封号处理"));
    } 
    role.pushTempCache(type, Long.valueOf(millis));
    return true;
  }
  
  public boolean mouserCartoon(Role role, String type, long mouse) {
    long millis = System.currentTimeMillis();
    long time = ((Long)role.getTempCache(type, Long.valueOf(0L))).longValue();
    if (time > 0L && millis - time < mouse)
      return false; 
    role.pushTempCache(type, Long.valueOf(millis));
    return true;
  }
  
  public void kickOut(long id, String msg) {
    IoSession session = SessionUtils.getSessionBy(id);
    if (session != null) {
      MessagePusher.pushMessage(session, (Message)new RespMsg(msg));
      session.closeNow();
    } 
  }
  
  public boolean isRelationMobile(Role role, String mobile) {
    if (role.getMobile() != null && role.getMobile().length() > 1) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你已经绑定了"));
      return false;
    } 
    String relation = JedisUtil.get(role.getSid() + "_" + mobile);
    if (relation != null) {
      role.setMobile(relation);
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespCorrelation(relation));
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你已经绑定了"));
      return false;
    } 
    return true;
  }
  
  public void verificationCode(Role role, String mobile) {
    if (CodeUtils.verificationPhone(mobile) || !CodeUtils.checkPhone(mobile)) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("请正确输入手机号码"));
      return;
    } 
    if (!isRelationMobile(role, mobile))
      return; 
    boolean contain = JedisUtil.contain("*_" + mobile);
    if (contain) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("该手机已经被绑定！请改用其号码"));
      return;
    } 
    String key = role.getSid() + "_sms";
    String code = JedisUtil.get(key);
    if (code != null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("验证码已经发送请注意查收"));
      return;
    } 
    boolean clicker = mouserClicker(role, "sms", 60000L);
    if (!clicker) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("验证码已经发送，请稍后再试"));
      return;
    } 
    String verification = String.valueOf((int)((Math.random() * 9.0D + 1.0D) * 100000.0D));
    JedisUtil.set(key, 3600, String.format("%s_%s", new Object[] { mobile, verification }));
    String sendSms = this.sendService.sendSms(role.getSid(), mobile, verification, 60);
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg(sendSms));
  }
  
  public void relationMobile(Role role, String verificationCode) {
    if (role.getMobile() != null && role.getMobile().length() > 1) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你已经绑定了"));
      return;
    } 
    String key = role.getSid() + "_sms";
    String value = JedisUtil.get(key);
    if (value == null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("验证码已过期，请重新获取"));
      return;
    } 
    String[] split = value.split("_");
    if (split.length < 2)
      return; 
    if (!split[1].equals(verificationCode)) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("验证码错误"));
      return;
    } 
    String relation = JedisUtil.get(role.getSid() + "_" + split[0]);
    if (relation != null) {
      role.setMobile(relation);
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespCorrelation(relation));
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你已经绑定了"));
      return;
    } 
    boolean contain = JedisUtil.contain("*_" + split[0]);
    if (contain) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("该手机已经被绑定！请改用其号码"));
      return;
    } 
    role.setMobile(split[0]);
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespCorrelation(role.getMobile()));
    JedisUtil.set(role.getSid() + "_" + split[0], role.getMobile());
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("绑定成功"));
    this.bagService.sendReward(role, Deploy.mobile_binding, "手机绑定", 0);
    SpringUtils.getAccountService().updateAccountMobile(role.getSid(), role.getMobile());
    JedisUtil.del(key);
  }
}
