package com.kitty.system.game.role.service;

import com.kitty.mina.cache.Deploy;
import com.kitty.common.utils.CodeUtils;
import com.kitty.cache.JedisUtil;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.stream.Collectors;
import com.kitty.system.game.pet.domain.vo.SuitParam;
import com.kitty.system.game.pet.message.RespAssistPetList;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.market.message.RespMarketGoodCard;
import com.kitty.system.game.equip.domain.WearEquip;
import java.util.Calendar;
import com.kitty.system.game.welfare.service.WelfareService;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.system.game.safe.service.SafeService;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.rabbit.RabbitService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.enter.ReqMultiMoveTo;
import com.kitty.common.identity.IdentityType;
import com.kitty.system.game.fightnew.messages.old.RespElseFight;
import com.kitty.system.game.role.message.RespPartyPracticeInfo;
import com.kitty.system.enums.war.itempro.def.FACTION_PRO_DICT;
import com.kitty.system.game.role.domain.vo.PartyPractice;
import com.kitty.system.init.domain.SysPartyPractice;
import com.kitty.system.init.pool.ExpDataPool;
import java.util.Optional;
import com.kitty.listener.event.RoleAspectChangeEvent;
import com.alibaba.fastjson.JSON;
import com.kitty.system.init.domain.vo.ChangeColor;
import com.kitty.system.init.domain.SysRoleChangeColor;
import com.kitty.system.game.enter.message.ReqDey;
import com.kitty.system.game.fightnew.book.vo.SkillAutoPicker;
import java.util.concurrent.ScheduledFuture;
import com.kitty.system.game.fightnew.FightObject;
import org.nutz.lang.Lang;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import com.kitty.system.game.fightnew.messages.ReqFightHandle;
import com.kitty.system.game.fightnew.utils.MessageAssistUtil;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.game.enter.message.ReqExtendStoreSize;
import com.kitty.system.game.general.message.ReqAssignAttrib;
import com.kitty.system.game.equip.domain.PetEquip;
import org.nutz.json.Json;
import com.kitty.system.game.skill.service.SkillService;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.tianfu.service.RoleDayInCeService;
import com.kitty.system.init.domain.vo.AdviceAddPro;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.general.message.ReqSetRecommendAttrib;
import com.kitty.record.RecordThread;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.listener.event.ranking.RoleChangeEvent;
import com.kitty.listener.event.RoleLevelUpEvent;
import com.kitty.system.game.role.message.RespLevelUp;
import com.kitty.mina.message.Message;
import com.kitty.system.game.scene.SceneManager;
import com.kitty.system.game.bag.domain.Overflow;
import com.kitty.system.game.role.message.RespInstruct;
import com.kitty.system.game.role.message.vo.Instruct;
import com.kitty.system.game.bag.message.RespCurrencyInfo;
import com.kitty.system.game.player.bean.PkInfoBox;
import com.kitty.system.common.message.RespStateRefresh;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.role.message.RespUpdateImprovement;
import com.google.gson.Gson;
import com.kitty.system.game.role.message.RespRefreshStore;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.game.team.domain.Team;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.kitty.system.game.enter.message.RespGeneralNotify;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.game.store.domain.StoreEquip;
import com.kitty.system.game.store.domain.StoreGoods;
import com.kitty.system.game.card.domain.CardChange;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.role.message.RespCorrelation;
import com.kitty.system.game.enter.service.EnterService;
import com.kitty.mina.cache.SessionCache;
import com.kitty.listener.event.EnterMapEvent;
import com.kitty.system.game.pk.service.PkService;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.enter.message.RespReplyServerTime;
import com.kitty.system.game.enter.message.RespRoleAlreadyLogin;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.session.SessionManager;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import com.kitty.system.game.enter.message.RespKickClient;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.mina.cache.SessionUtils;
import java.util.regex.Matcher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.dirtywords.WordDisposePool;
import org.nutz.lang.Strings;
import com.kitty.system.game.vo.FieldByte;
import java.util.ArrayList;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import java.util.HashMap;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.house.service.HouseService;
import com.kitty.system.game.role.domain.Setting;
import com.kitty.system.game.card.domain.CardExercise;
import com.kitty.listener.event.CreationRoleEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.player.PlayerProfile;
import org.nutz.lang.util.NutMap;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.model.Pos;
import com.kitty.common.thread.IdGenerator;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysHero;
import com.kitty.system.game.role.message.ReqCreateNewChar;
import java.util.concurrent.TimeUnit;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.account.domain.Account;
import com.kitty.common.start.ServerService;
import java.io.Serializable;
import com.kitty.system.game.vo.Condition;
import com.kitty.system.game.vo.CheckMapInt;
import com.kitty.system.common.objects.ItemCheck;
import com.kitty.system.game.vo.init.Item;
import java.util.List;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.mina.cache.PetCache;
import com.kitty.mina.cache.BagCache;
import java.util.Iterator;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.partner.service.PartnerService;
import com.kitty.mina.cache.DataCache;
import java.util.Map;
import com.kitty.system.game.horse.service.HorseService;
import com.kitty.system.game.echo.service.EchoService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.filter.HeartbeatFilter;
import com.kitty.listener.event.LogoutEvent;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.mind.service.MindService;
import com.kitty.cxf.send.service.GameSendService;
import com.kitty.system.game.store.service.StoreService;
import com.kitty.cache.RedisService;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.bag.service.BagService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.common.thread.IdWorker;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class RoleService
{
    private final Logger logger;
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
    @Autowired
    MindService mindService;
    
    public RoleService() {
        this.logger = LoggerFactory.getLogger(RoleService.class);
    }
    
    @EventHandler({ EventType.LOGOUT })
    public void handleLogoutEvent(LogoutEvent logoutEvent) {
        Role role = logoutEvent.getRole();
        HeartbeatFilter.cleanHeartBeatRecord((long)role.getId());
        role.setUnlock(false);
        role.setPreOnlineSec(Long.valueOf(this.getOnlineSec(role)));
        this.clearBufferData((long)role.getId());
        role.save();
        this.roleService.removeOnline(role);
        this.roleService.removeOffline(role);
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        Role role = loginEvent.getRole();
        HeartbeatFilter.cleanHeartBeatRecord((long)role.getId());
        this.checkReset(role);
        HorseService horseService = SpringUtils.getHorseService();
        horseService.refresh(role, horseService.getHorse(role));
        this.equipService.refreshPetEquip(role);
        this.petService.loadPet(role);
        ((EchoService)SpringUtils.getBean(EchoService.class)).cancelCountdown((long)role.getId());
    }
    
    public void roleLevelChangeEvent(Role role) {
        role.setGg(Integer.valueOf((int)role.getGg() + 1));
        role.setLx(Integer.valueOf((int)role.getLx() + 1));
        role.setLl(Integer.valueOf((int)role.getLl() + 1));
        role.setMj(Integer.valueOf((int)role.getMj() + 1));
        role.setFpoint(Integer.valueOf((int)role.getFpoint() + 4));
        this.calculateBase(role);
        this.refreshRole(role);
        this.refreshPetClose(role);
        Map<Integer, Partner> partnerMap = (Map)DataCache.PARTNER_MAP.get(role.getId());
        if (partnerMap != null && partnerMap.size() > 0) {
            PartnerService partnerService = (PartnerService)SpringUtils.getBean(PartnerService.class);
            int partnerLv = 55;
            for (Map.Entry<Integer, Partner> entry : partnerMap.entrySet()) {
                Partner partner = (Partner)entry.getValue();
                if (partner.getPartnerLv() < partnerLv) {
                    if ((int)role.getLevel() >= partnerLv) {
                        partner.setPartnerLv((short)partnerLv);
                    }
                    else {
                        partner.setPartnerLv(role.getLevel().shortValue());
                    }
                    partnerService.partnerLevelChangeEvent(role, partner);
                    partner.save();
                }
                else {
                    continue;
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
            Pet pet = (Pet)petMap.get(key);
            this.petService.refreshPetLevelClose(pet);
            this.petService.refreshPetInfo(role, pet);
        }
    }
    
    public boolean checkCondition(Role role, List<Item> items, Map<Integer, Integer> map) {
        if (map != null) {
            for (Integer key : map.keySet()) {
                boolean flag = this.checkCurrency(role, key, (long)(int)map.get(key));
                if (!flag) {
                    return false;
                }
                continue;
            }
        }
        if (items != null) {
            CheckMapInt checkMapInt = ItemCheck.checkAllItemTypes(items, role);
            if (!checkMapInt.isFlag()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean costCondition(Role role, Condition condition, String reason, boolean bnotify, boolean bnotcut) {
        if (condition.getList() != null) {
            for (Item item : condition.getList()) {
                Integer goods = this.bagService.consumeBagGoods(role, (int)item.getItem(), (int)item.getNum(), true, true, "costCondition");
                if (goods != null) {
                    return false;
                }
                continue;
            }
        }
        if (condition.getMap() != null) {
            Map<Integer, Integer> map = condition.getMap();
            for (Integer type : map.keySet()) {
                boolean flag = this.deductCurrency(role, type, (long)(int)map.get(type), "costCondition");
                if (!flag) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    public void addOnline(Role role) {
        if (role == null) {
            return;
        }
        DataCache.ONLINE_ROLES.put(role.getId(), role);
        this.redisService.del(role.getId() + "_countdown_" + ((ServerService)SpringUtils.getBean(ServerService.class)).getMarking());
        return;
    }
    
    public void removeOnline(Role role) {
        if (role == null) {
            return;
        }
        DataCache.ONLINE_ROLES.remove(role.getId());
        return;
    }
    
    public void addOffline(Role role, boolean flag) {
        if (role == null) {
            return;
        }
        role.setOfLineTime(Long.valueOf(System.currentTimeMillis()));
        DataCache.OFFLINE_ROLES.put(role.getId(), role);
        String key = "temporary" + role.getId();
        Boolean exists = this.redisService.exists(key);
        if ((boolean)exists) {
            this.redisService.expire(key, 54000L);
        }
        this.sendOffline(role, flag);
        return;
    }
    
    public void removeOffline(Role role) {
        if (role == null) {
            return;
        }
        DataCache.OFFLINE_ROLES.remove(role.getId());
        String key = "temporary" + role.getId();
        Boolean exists = this.redisService.exists(key);
        if ((boolean)exists) {
            this.redisService.persist(key);
        }
        return;
    }
    
    public boolean isOnline(int id) {
        return this.isOnline(SpringUtils.getPlayerService().getUidBy(id));
    }
    
    public boolean isOnline(long id) {
        return DataCache.ONLINE_ROLES.containsKey(Long.valueOf(id));
    }
    
    public boolean canLogin(Account account, final IoSession session) {
        ServerService serverService = (ServerService)SpringUtils.getBean(ServerService.class);
        if (account == null) {
            MessagePusher.pushMessage(session, new RespNotifyMiscEx("账号不存在！！"));
            serverService.getScheduledExecutorService().schedule(new Runnable() {
                @Override
                public void run() {
                    session.closeNow();
                }
            }, 3L, TimeUnit.SECONDS);
            return false;
        }
        if (account.getIsStop() <= 0) {
            MessagePusher.pushMessage(session, new RespNotifyMiscEx("账号已经被封闭，请联系管理员！！"));
            serverService.getScheduledExecutorService().schedule(new Runnable() {
                @Override
                public void run() {
                    session.closeNow();
                }
            }, 3L, TimeUnit.SECONDS);
            return false;
        }
        return true;
    }
    
    public Role createRole(IoSession session, String sid, ReqCreateNewChar reqCreateNewChar) {
        SysHero hero = (SysHero)PartDataPool.heroMap.get(reqCreateNewChar.getSerial());
        SysRbAttr data = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf(1));
        if (hero != null) {
            String name = reqCreateNewChar.getName().trim();
            name = name.replace("#", "");
            if (!this.canCreateRole(session, name, LOGIC.LOGICTYPE_HERO.getValue())) {
                return null;
            }
            name = CheckCharUtils.replaceName(name);
            Role role = new Role();
            role.setId(Long.valueOf(IdGenerator.getNextId()));
            role.setSid(sid);
            role.setRoleId(Integer.valueOf(this.getRoleId()));
            role.setName(name);
            role.setSex(reqCreateNewChar.getSex());
            role.setRace(reqCreateNewChar.getRace());
            role.setLevel(Integer.valueOf(0));
            role.setRelive(Integer.valueOf(0));
            role.setExp(0L);
            role.setSaveLife(Long.valueOf(0L));
            role.setSaveMana(Long.valueOf(0L));
            role.setGold(Long.valueOf(0L));
            role.setIcon((int)hero.getShape());
            role.getTask().setIcon((int)hero.getShape());
            role.setSerial(hero.getSerial());
            role.setSkills(this.createSkillBox(role, reqCreateNewChar.getRace(), reqCreateNewChar.getSex(), data));
            role.setPos(new Pos());
            role.getExtendBox().setQuotaLimit(DataGlobalFunc.setQuota());
            role.getExtendBox().setSetMap(DataGlobalFunc.setting());
            role.getExtendBox().setNewDistrict(DataGlobalFunc.newDistrict());
            role.getRest().setParty(DataGlobalFunc.faction());
            this.calculateBase(role);
            role.setMarking(reqCreateNewChar.getMarking());
            NutMap map = new NutMap();
            map.setv("a", Integer.valueOf(0));
            map.setv("b", Integer.valueOf(0));
            role.setColor(map.toString());
            long time = System.currentTimeMillis();
            role.setEnterTime(Long.valueOf(time));
            role.save();
            this.createRoleRests(role);
            SpringUtils.getPlayerService().addPlayerProfile(new PlayerProfile(role));
            this.addOffline(role, false);
            EventDispatcher.getInstance().fireEvent(new CreationRoleEvent(EventType.CREATION_ROLE, role));
            return role;
        }
        else {
            return null;
        }
    }
    
    public void createRoleRests(Role role) {
        CardExercise cardExercise = new CardExercise(this.worker.nextId(), (long)role.getId());
        cardExercise.save();
        DataCache.CARD_EXERCISE_MAP.put(role.getId(), cardExercise);
        SpringUtils.getHorseService().creationHorse(role);
        Setting setting = new Setting(this.worker.nextId(), (long)role.getId());
        setting.save();
        DataCache.SETTING_MAP.put(role.getId(), setting);
        ((HouseService)SpringUtils.getBean(HouseService.class)).addHouse((long)role.getId(), 5100);
    }
    
    public void calculateBase(Role role) {
        int life = CountUtils.countLife(role);
        role.setCurrLife(Integer.valueOf(life));
        role.setMaxLife(Integer.valueOf(life));
        int mana = CountUtils.countMana(role);
        role.setCurrMana(Integer.valueOf(mana));
        role.setMaxMana(Integer.valueOf(mana));
    }
    
    public Map<Integer, RoleSkill> createSkillBox(Role role, Integer race, Integer sex, SysRbAttr data) {
        Map<Integer, RoleSkill> skills = new HashMap();
        List<Integer> list = DataGlobalFunc.GetPlayerSkillId(race, sex);
        for (Integer integer : list) {
            Map<Integer, SysSkill> skillMap = DataGlobalFunc.getSkillListByAttr(integer);
            for (Integer key : skillMap.keySet()) {
                SysSkill sysSkill = (SysSkill)skillMap.get(key);
                int max_proficient = (int)data.getMaxSkillExp();
                List<FieldByte> effects = null;
                if (role.getSkills().size() > 0) {
                    for (RoleSkill value : role.getSkills().values()) {
                        SysSkill skill = (SysSkill)SkillDataPool.skillMap.get(value.getSkillId());
                        if (skill.getPos().equals(sysSkill.getPos()) && value.getEffects().size() > 0) {
                            effects = new ArrayList<>();
                            for (FieldByte fieldByte : value.getEffects()) {
                                int type = (int)fieldByte.getK() % 10;
                                effects.add(new FieldByte(Integer.valueOf(sysSkill.getIcon() * 10 + type), fieldByte.getV()));
                            }
                        }
                        else {
                            continue;
                        }
                    }
                }
                RoleSkill skill2 = new RoleSkill(sysSkill.getSerial(), integer, data.getInitSkillExp(), Integer.valueOf(max_proficient));
                if (effects != null) {
                    skill2.setEffects(effects);
                }
                skills.put(sysSkill.getSerial(), skill2);
            }
        }
        return skills;
    }
    
    public boolean canCreateRole(IoSession session, String name, int logic) {
        String type = "角色";
        if (LOGIC.LOGICTYPE_PET.getValue() == logic) {
            type = "宠物";
        }
        else if (LOGIC.HORSE.getValue() == logic) {
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
            Role onlineRole = this.getOnlinePlayer((long)playerProfile.getId());
            if (onlineRole != null) {
                final IoSession ioSession = SessionUtils.getSessionBy((long)onlineRole.getId());
                if (ioSession != null && ioSession.getId() != session.getId()) {
                    RespOtherLogin respOtherLogin = new RespOtherLogin();
                    respOtherLogin.setResult(Integer.valueOf(1));
                    MessagePusher.pushMessage(ioSession, respOtherLogin);
                    Thread.sleep(1000L);
                    ioSession.write(new RespKickClient("你的账号已在其它设备登录。")).addListener(new IoFutureListener<IoFuture>() {
                        public void operationComplete(IoFuture future) {
                            ioSession.closeNow();
                        }
                    });
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
        SessionManager.INSTANCE.registerSession((int)role.getRoleId(), session);
        this.addOnline(role);
        this.removeOffline(role);
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
        if ((long)role.getLockTime() > System.currentTimeMillis()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date((long)role.getLockTime());
            String tips = simpleDateFormat.format(date);
            RespOtherLogin respOtherLogin2 = new RespOtherLogin();
            respOtherLogin2.setContent("您违规游戏行为，角色已被封停。\n解封时间：" + tips);
            respOtherLogin2.setResult(Integer.valueOf(2));
            MessagePusher.pushMessage(role, respOtherLogin2);
            return;
        }
        this.sendPacket(role);
        MessagePusher.pushMessage(role, SafeUtil.getLockStatus(role));
        ((PkService)SpringUtils.getBean(PkService.class)).loginCheckFromPrison(role);
        EventDispatcher.getInstance().fireEvent(new EnterMapEvent(EventType.ENTER_MAP, role));
        EventDispatcher.getInstance().fireEvent(new LoginEvent(EventType.LOGIN, role));
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            int teamCount = this.teamService.getTeamCount(team);
            if (teamCount == 0) {
                SessionCache.ROLE_TEAM.remove(role.getRoleId());
            }
            else {
                this.teamService.updateTeamList(role);
                this.teamService.liveLoadTeamList(role);
            }
        }
        ((EnterService)SpringUtils.getBean(EnterService.class)).loadHmlNotify(role);
        if (role.getMobile() != null && role.getMobile().length() > 1) {
            MessagePusher.pushMessage((long)role.getId(), new RespCorrelation(role.getMobile()));
        }
        this.cardService.reqFiveExercise(role, false);
        this.stateRefresh(role);
        this.titleService.refreshTitle(role);
        this.refreshRole(role);
        this.sendExtraAttribute(role);
        this.refreshRoleCurrency(role);
        Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
        for (Long key : bagGoodsMap.keySet()) {
            BagGoods goods = (BagGoods)bagGoodsMap.get(key);
            this.bagService.refreshRoleGoods(role, goods, false);
        }
        Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
        for (Long key2 : bagEquipMap.keySet()) {
            BagEquip bagEquip = (BagEquip)bagEquipMap.get(key2);
            this.bagService.refreshRoleEquip(role, bagEquip, false);
        }
        Map<Long, CardChange> card = this.cardService.getCard(role);
        for (Long key3 : card.keySet()) {
            this.cardService.refresh(role, (CardChange)card.get(key3));
        }
        Map<Long, StoreGoods> storeGoodsMap = this.storeService.getStoreGoods(role);
        for (StoreGoods good : storeGoodsMap.values()) {
            this.storeService.refreshStoreGoods(role, good);
        }
        Iterator<StoreEquip> itStoreEquips = this.storeService.getStoreEquip(role).values().iterator();
        while (itStoreEquips.hasNext()) {
            StoreEquip equip = (StoreEquip)itStoreEquips.next();
            if (equip.getType() > 1) {
                this.storeService.refreshStoreEquip(role, equip);
            }
            else {
                this.logger.error("加载仓库装备异常数据 = {}", equip);
                itStoreEquips.remove();
            }
        }
        for (RoleTianFu roleTianFu : DataCache.ROLE_TIAN_FU.get(role.getId())) {
            if (roleTianFu == null) {
                continue;
            }
            if (roleTianFu.getStorePos() > 0) {
                this.storeService.refreshStoreTianFu(role, roleTianFu);
            }
            else {
                continue;
            }
        }
        role.setEnterTime(Long.valueOf(System.currentTimeMillis()));
        role.setOfLineTime(Long.valueOf(0L));
        role.setOnlineLoginTime(System.currentTimeMillis());
        role.setPrevHeartTime(0L);
        role.save();
        this.refreshInstruct(role);
        SessionManager.INSTANCE.removeFromAnonymous(session);
        RespGeneralNotify respGeneralNotify = new RespGeneralNotify();
        respGeneralNotify.setNotify(39);
        respGeneralNotify.setValue("1");
        MessagePusher.pushMessage(role, respGeneralNotify);
        return;
    }
    
    public void refreshParty(Role role) {
        this.titleService.refreshTitle(role);
        RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId((int)role.getRoleId());
        List<FiedValue> list = new ArrayList<>();
        list.add(new FiedValue("faction", (byte)3, role.getFaction()));
        list.add(new FiedValue("factionLevel", (byte)3, Byte.valueOf(this.getFactionLevel(role))));
        list.add(new FiedValue("factionName", (byte)4, this.getFactionName(role)));
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
    }
    
    public void refreshRole(Role role) {
        this.refreshRoleStore(role);
        RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId((int)role.getRoleId());
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
        list.add(new FiedValue("nextExp", (byte)4, String.valueOf(FormulaHero.formulaHeroLvUpExp(role.getLevel(), Integer.valueOf((int)role.getLevel() + 1), role.getRelive()))));
        List<FiedValue> deyList = this.deyList(role.getColor());
        if (deyList.size() > 0) {
            list.addAll(deyList);
        }
        list.add(new FiedValue("gg", (byte)2, Short.valueOf(role.getGg().shortValue())));
        list.add(new FiedValue("lx", (byte)2, Short.valueOf(role.getLx().shortValue())));
        list.add(new FiedValue("ll", (byte)2, Short.valueOf(role.getLl().shortValue())));
        list.add(new FiedValue("mj", (byte)2, Short.valueOf(role.getMj().shortValue())));
        list.add(new FiedValue("fpoint", (byte)2, Short.valueOf(role.getFpoint().shortValue())));
        list.add(new FiedValue("faction", (byte)3, role.getFaction()));
        list.add(new FiedValue("factionLevel", (byte)3, Byte.valueOf(this.getFactionLevel(role))));
        list.add(new FiedValue("factionName", (byte)4, this.getFactionName(role)));
        list.add(new FiedValue("autoFight", (byte)1, Byte.valueOf(role.getExtendBox().getAutoFight())));
        list.add(new FiedValue("title", (byte)3, Integer.valueOf(role.getCurrTitle())));
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
    }
    
    public String getFactionName(Role role) {
        String name = "";
        if ((int)role.getFaction() > 0) {
            Party party = SpringUtils.getPartyService().getParty(role.getFaction());
            if (party != null) {
                name = party.getName();
            }
        }
        return name;
    }
    
    public byte getFactionLevel(Role role) {
        byte level = 0;
        if ((int)role.getFaction() > 0) {
            Party party = SpringUtils.getPartyService().getParty(role.getFaction());
            if (party != null) {
                level = party.getLevel();
            }
        }
        return level;
    }
    
    public void refreshRoleInfo(Role role, boolean flag) {
        RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId((int)role.getRoleId());
        List<FiedValue> list = new ArrayList<>();
        list.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countLife(role))));
        list.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countMana(role))));
        list.add(new FiedValue("curr_life", (byte)3, role.getCurrLife()));
        list.add(new FiedValue("curr_mana", (byte)3, role.getCurrMana()));
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
        if (flag) {
            this.refreshRoleStore(role);
        }
    }
    
    public void refreshRoleStore(Role role) {
        MessagePusher.pushMessage(role, new RespRefreshStore(role.getSaveLife(), role.getSaveMana()));
    }
    
    public List<FiedValue> deyList(String color) {
        List<FiedValue> list = new ArrayList<>();
        Gson gson = new Gson();
        NutMap nutMap = (NutMap)gson.fromJson(color, NutMap.class);
        if (nutMap != null) {
            int a = nutMap.getInt("a");
            if (a > 0) {
                list.add(new FiedValue("a", (byte)3, Integer.valueOf(a)));
            }
            int b = nutMap.getInt("b");
            if (b > 0) {
                list.add(new FiedValue("b", (byte)3, Integer.valueOf(b)));
            }
        }
        return list;
    }
    
    public void sendExtraAttribute(Role role) {
        List<FiedValue> list = new ArrayList<>();
        list = this.correction(role, list);
        list = this.faction(role, list);
        MessagePusher.pushMessage(role, new RespUpdateImprovement((int)role.getRoleId(), list));
    }
    
    public void stateRefresh(Role role) {
        int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        List<FiedValue> list = new ArrayList<>();
        if (role.getMasquerade() > 0 && role.getCardExpireTime() > currTime) {
            NutMap nutMap = new NutMap();
            nutMap.setv("serial", Integer.valueOf(role.getMasquerade()));
            nutMap.setv("expire", Integer.valueOf(role.getCardExpireTime()));
            list.add(new FiedValue("card", (byte)4, JsonUtils.map2String(nutMap)));
        }
        PkInfoBox box = role.getPkInfoBox();
        if (box.getIncense() > 0 && box.getIncense() > currTime) {
            list.add(new FiedValue("7", (byte)3, Integer.valueOf(box.getIncense())));
        }
        if (box.isRedName()) {
            if (box.getRedTime() > 0 && box.getRedTime() > currTime) {
                list.add(new FiedValue("8", (byte)3, Integer.valueOf(box.getRedTime())));
            }
            if (box.getPrisonTime() > 0 && box.getPrisonTime() > currTime) {
                list.add(new FiedValue("9", (byte)3, Integer.valueOf(box.getPrisonTime())));
            }
            if (box.getConceal() > 0 && box.getConceal() > currTime) {
                list.add(new FiedValue("101", (byte)3, Integer.valueOf(box.getConceal())));
            }
        }
        int moveAdditionTime = role.getExtendBox().getState().getInt("shenxing", 0);
        if (moveAdditionTime > 0 && moveAdditionTime > currTime) {
            list.add(new FiedValue("109", (byte)3, Integer.valueOf(moveAdditionTime)));
        }
        MessagePusher.pushMessage(role, new RespStateRefresh(list));
    }
    
    public List<FiedValue> correction(Role role, List<FiedValue> list) {
        for (Integer rei : role.getRest().getCorrection().keySet()) {
            list.add(new FiedValue("rei_" + rei, (byte)3, role.getRest().getCorrection().get(rei)));
        }
        return list;
    }
    
    public List<FiedValue> faction(Role role, List<FiedValue> list) {
        if (role.getRest().getParty().size() > 0) {
            Map<Integer, Integer> party = role.getRest().getParty();
            for (Integer pro : party.keySet()) {
                Integer value = (Integer)party.get(pro);
                if (value != null) {
                    list.add(new FiedValue(String.valueOf(pro), (byte)3, value));
                }
                else {
                    continue;
                }
            }
            return list;
        }
        return list;
    }
    
    public void refreshRoleCurrency(Role role) {
        MessagePusher.pushMessage(role, new RespCurrencyInfo(role));
    }
    
    public void refreshInstruct(Role role) {
        List<Instruct> list = new ArrayList<>();
        Setting setting = ((SettingService)SpringUtils.getBean(SettingService.class)).getSetting(role);
        Map<Short, Map<Byte, String>> map = setting.getSpecial();
        for (Short serial : map.keySet()) {
            Map<Byte, String> instructMap = (Map)map.get(serial);
            for (Byte index : instructMap.keySet()) {
                list.add(new Instruct((short)serial, (byte)index, (String)instructMap.get(index)));
            }
        }
        if (list.size() > 0) {
            MessagePusher.pushMessage(role, new RespInstruct(list));
        }
    }
    
    private void sendPacket(Role role) {
        String key = "temporary" + role.getId();
        Overflow overflow = (Overflow)this.redisService.get(key, Overflow.class);
        if (overflow != null) {
            this.bagService.lodeTemporary(role, overflow.getList());
        }
    }
    
    public boolean addExp(Role role, long exp, String reason, boolean is) {
        if (exp == 0L) {
            return true;
        }
        ServerService serverService = (ServerService)SpringUtils.getBean(ServerService.class);
        if (serverService.getExpTimesLevel() != 0 && serverService.getExpTimes() != 0 && (int)role.getRelive() * 1000 + (int)role.getLevel() > serverService.getExpTimesLevel()) {
            exp *= (long)serverService.getExpTimes();
        }
        if (exp < 0L) {
            long tips_exp;
            exp = (tips_exp = Math.abs(exp));
            exp = ((role.getExp() - exp <= 0L) ? 0L : (role.getExp() - exp));
            role.setExp(exp);
            RespUpdate respUpdate2 = new RespUpdate();
            List<FiedValue> list2 = new ArrayList<>();
            list2.add(new FiedValue("exp", (byte)4, String.valueOf(role.getExp())));
            respUpdate2.setId((int)role.getRoleId());
            respUpdate2.setList(list2);
            MessagePusher.pushMessage(role, respUpdate2);
            if (exp > 0L) {
                if (reason != null) {
                    MessagePusher.pushMessage(role, new RespMsg(reason + "#<IR4>##<R>" + tips_exp + "#点经验"));
                }
                else {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "你被扣除了#<IR4>##<R>" + tips_exp + "#点经验", true));
                }
            }
            role.save();
            EventDispatcher.getInstance().fireEvent(new RoleChangeEvent(EventType.ROLE_EXP_CHANGE, role));
            return true;
        }
        else {
            int level = (int)role.getLevel();
            SysRbAttr sysRbattr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf((int)role.getRelive() + 1));
            if (sysRbattr != null) {
                Integer lvLimit = sysRbattr.getLvLimit();
                if ((int)role.getLevel() >= (int)lvLimit && role.getExp() >= DataGlobalFunc.storageExp((int)role.getRelive() * 1000 + (int)role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue())) {
                    String msg = String.format("角色已到达%d转的上限%d级", role.getRelive(), lvLimit);
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, true));
                    return false;
                }
                String exp_tips = "#<G>" + exp + "#点经验";
                long storageExp = DataGlobalFunc.storageExp((int)role.getRelive() * 1000 + (int)role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue());
                long totalExp = (role.getExp() + exp > storageExp) ? storageExp : (role.getExp() + exp);
                long needExp = FormulaHero.formulaHeroLvUpExp(role.getLevel(), Integer.valueOf((int)role.getLevel() + 1), role.getRelive());
                if (totalExp >= needExp && (int)role.getLevel() < (int)lvLimit) {
                    while (totalExp >= needExp) {
                        totalExp -= needExp;
                        ++level;
                        role.setLevel(Integer.valueOf(level));
                        role.setExp(totalExp);
                        this.roleLevelChangeEvent(role);
                        if (level >= (int)lvLimit) {
                            storageExp = DataGlobalFunc.storageExp((int)role.getRelive() * 1000 + (int)role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue());
                            totalExp = ((totalExp > storageExp) ? storageExp : totalExp);
                            role.setExp(totalExp);
                            break;
                        }
                        needExp = FormulaHero.formulaHeroLvUpExp(role.getLevel(), Integer.valueOf((int)role.getLevel() + 1), role.getRelive());
                    }
                    SceneManager.INSTANCE.sendMessages(role, new RespLevelUp((int)role.getRoleId(), (int)role.getLevel()));
                    EventDispatcher.getInstance().fireEvent(new RoleLevelUpEvent(EventType.ROLE_LEVEL_UP, role, (int)role.getRelive() * 1000 + (level - 1), (int)role.getRelive() * 1000 + level));
                    role.save();
                }
                else {
                    role.setExp(totalExp);
                    RespUpdate respUpdate = new RespUpdate();
                    List<FiedValue> list = new ArrayList<>();
                    list.add(new FiedValue("exp", (byte)4, String.valueOf(role.getExp())));
                    respUpdate.setList(list);
                    respUpdate.setId((int)role.getRoleId());
                    MessagePusher.pushMessage(role, respUpdate);
                    role.save();
                }
                if (is) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "你获得了#<IR4>#" + exp_tips, true));
                }
                EventDispatcher.getInstance().fireEvent(new RoleChangeEvent(EventType.ROLE_EXP_CHANGE, role));
                return true;
            }
            else {
                return false;
            }
        }
    }
    
    public boolean addCurrency(Role role, Integer type, Long num, String cause, boolean isTips, boolean joint) {
        if ((long)num == 0L) {
            return false;
        }
        if ((long)num < 0L) {
            this.logger.error("增加货币负数，角色 = {}, 方法 = {}，货币类型 = {}，数量 = {}", new Object[] { role.getName(), cause, type, num });
            return false;
        }
        Long add_to = num;
        boolean flag = false;
        String msg = "";
        if (type.equals(Const.RESTYPE_GOLD)) {
            num = Long.valueOf((long)role.getGold() + (long)num);
            role.setGold(num);
            msg = "#<IR" + Const.RESTYPE_GOLD + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_SILVER)) {
            num = Long.valueOf((long)role.getSilver() + (long)num);
            role.setSilver(num);
            msg = "#<IR" + Const.RESTYPE_SILVER + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_COIN)) {
            num = Long.valueOf((long)role.getCoin() + (long)num);
            role.setCoin(num);
            msg = "#<IR" + Const.RESTYPE_COIN + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_GUO)) {
            num = Long.valueOf((long)role.getExtendBox().getGuoScore() + (long)num);
            role.getExtendBox().setGuoScore(num);
            msg = "#<IR" + Const.RESTYPE_GUO + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_BRAC)) {
            num = Long.valueOf((long)role.getExtendBox().getBracScore() + (long)num);
            role.getExtendBox().setBracScore(num);
            msg = "#<IR" + Const.RESTYPE_BRAC + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_NAO)) {
            num = Long.valueOf((long)role.getExtendBox().getNaoScore() + (long)num);
            role.getExtendBox().setNaoScore(num);
            msg = "#<IR" + Const.RESTYPE_NAO + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_DI)) {
            num = Long.valueOf((long)role.getExtendBox().getDiScore() + (long)num);
            role.getExtendBox().setDiScore(num);
            msg = "#<IR" + Const.RESTYPE_DI + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_ARENA)) {
            num = Long.valueOf((long)role.getExtendBox().getArenaScore() + (long)num);
            role.getExtendBox().setArenaScore(num);
            msg = "#<IR" + Const.RESTYPE_ARENA + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_LONG)) {
            num = Long.valueOf((long)role.getExtendBox().getLongScore() + (long)num);
            role.getExtendBox().setLongScore(num);
            msg = "#<IR" + Const.RESTYPE_LONG + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_NICE)) {
            num = Long.valueOf((long)role.getExtendBox().getNiceScore() + (long)num);
            role.getExtendBox().setNiceScore(num);
            msg = "#<IR" + Const.RESTYPE_NICE + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_TRIBUTE)) {
            num = Long.valueOf((long)role.getExtendBox().getHelpScore() + (long)num);
            role.getExtendBox().setHelpScore(num);
            msg = "#<IR" + Const.RESTYPE_TRIBUTE + ">#";
            flag = true;
        }
        else if (type.equals(Const.RES_TYPE_FEATS)) {
            num = Long.valueOf((long)role.getExtendBox().getFeatsScore() + (long)num);
            role.getExtendBox().setFeatsScore(num);
            msg = "#<IR" + Const.RES_TYPE_FEATS + ">#";
            flag = true;
        }
        else if (type.equals(Const.RES_TYPE_CHARGE_SCORE)) {
            num = Long.valueOf((long)role.getExtendBox().getChargeScore() + (long)num);
            role.getExtendBox().setChargeScore(num);
            msg = "#<IR" + Const.RES_TYPE_CHARGE_SCORE + ">#";
            flag = true;
        }
        else if (type.equals(Const.RES_TYPE_BOUND_GOLD)) {
            num = Long.valueOf((long)role.getExtendBox().getBoundGold() + (long)num);
            role.getExtendBox().setBoundGold(num);
            msg = "#<IR" + Const.RES_TYPE_BOUND_GOLD + ">#";
            flag = true;
        }
        else if (type.equals(Const.RES_TYPE_ACHIEVEMENT)) {
            num = Long.valueOf((long)role.getExtendBox().getAchievementScore() + (long)num);
            role.getExtendBox().setAchievementScore(num);
            msg = "#<IR" + Const.RES_TYPE_ACHIEVEMENT + ">#";
            flag = true;
        }
        if (flag) {
            String cause_ = cause;
            if (isTips) {
                if (!joint) {
                    cause = "";
                }
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), cause + "你获得了" + msg + MoneyUtil.colours(add_to), true));
            }
            if (!type.equals(Const.RES_TYPE_ACHIEVEMENT)) {
                this.refreshRoleCurrency(role);
            }
            if (cause_ == null) {
                cause_ = "";
            }
            role.save();
            RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)3, (byte)1, String.format("货币类型：%d，增加数量%d ,增加后数量：%d，操作原因：%s", type, add_to, num, cause_), role.getMarking());
        }
        return flag;
    }
    
    public void commendAttrib(Role role, ReqSetRecommendAttrib notify) {
        String msg = "自定义";
        if (role.getRoleId().equals(Integer.valueOf(notify.getId()))) {
            Map<String, Integer> map2 = role.getExtendBox().getAutoAttrib();
            map2.put("gg", Integer.valueOf(notify.getGg()));
            map2.put("lx", Integer.valueOf(notify.getLx()));
            map2.put("ll", Integer.valueOf(notify.getLl()));
            map2.put("mj", Integer.valueOf(notify.getMj()));
            map2.put("type", Integer.valueOf(notify.getType()));
            if (notify.getType() <= 2) {
                SysHero hero = (SysHero)PartDataPool.heroMap.get(role.getSerial());
                if (hero != null) {
                    AdviceAddPro adviceAddPro2 = (AdviceAddPro)hero.getAdviceAddPro().get(notify.getType() - 1);
                    msg = adviceAddPro2.getTitle();
                }
            }
        }
        else {
            Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(notify.getId()));
            if (pet != null) {
                Map<String, Integer> map = pet.getAutoAttrib();
                map.put("gg", Integer.valueOf(notify.getGg()));
                map.put("lx", Integer.valueOf(notify.getLx()));
                map.put("ll", Integer.valueOf(notify.getLl()));
                map.put("mj", Integer.valueOf(notify.getMj()));
                map.put("type", Integer.valueOf(notify.getType()));
                pet.setAutoAttrib(map);
                if (notify.getType() <= 2) {
                    SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
                    if (sysPet != null) {
                        AdviceAddPro adviceAddPro = (AdviceAddPro)sysPet.getAdviceAddPro().get(notify.getType() - 1);
                        msg = adviceAddPro.getTitle();
                    }
                }
                pet.save();
            }
        }
        if (notify.getStatus() != 0) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "加点方案#<r:255,g:128,b:0>" + msg + "#已启用", false));
        }
        role.save();
    }
    
    public void changeRace(Role role, Integer serial) {
        Map<Integer, SysSkill> data_skill = SkillDataPool.skillMap;
        Map<Integer, Integer> skillProficient = this.changeSkillProficient(role.getSkills(), data_skill);
        if (skillProficient.size() < 6) {
            MessagePusher.pushMessage(role, new RespHint(13, (short)0));
            return;
        }
        SysHero hero = (SysHero)PartDataPool.heroMap.get(serial);
        if (hero != null) {
            RoleDayInCeService ceService = (RoleDayInCeService)SpringUtils.getBean(RoleDayInCeService.class);
            if (ceService.checkDemount(role, hero.getRace()) > 0) {
                MessagePusher.pushMessage((long)role.getId(), new RespMsg("天策符背包格子不足，无法转换种族"));
                return;
            }
            if (!this.deductCurrency(role, Const.RESTYPE_GOLD, 300L, "转换种族")) {
                return;
            }
            this.resetRole(role, skillProficient, hero, data_skill, ceService, com.kitty.listener.event.RoleChangeEvent.RACE);
        }
        return;
    }
    
    public void resetRole(Role role, Map<Integer, Integer> skillProficient, SysHero hero, Map<Integer, SysSkill> data_skill, RoleDayInCeService ceService, int type) {
        SysRbAttr sysRbAttr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf((int)role.getRelive() + 1));
        int count = FormulaHero.restorePorCount((int)role.getRelive(), (int)role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue());
        role.setGg(role.getLevel());
        role.setLx(role.getLevel());
        role.setLl(role.getLevel());
        role.setMj(role.getLevel());
        role.setFpoint(Integer.valueOf(count));
        role.setRace(Integer.valueOf(hero.getRace()));
        role.setSex(Integer.valueOf(hero.getGender()));
        role.setIcon((int)hero.getShape());
        role.getTask().setIcon((int)hero.getShape());
        role.setSerial(hero.getSerial());
        this.mindService.correction(role);
        this.calculateBase(role);
        this.refreshRole(role);
        MessagePusher.pushMessage(role, new RespHint(13, (short)1));
        SpringUtils.getHorseService().convertMount(role);
        this.broadcastService.sendUpdateAppear(role);
        role.setSkills(this.createSkillBox(role, role.getRace(), role.getSex(), sysRbAttr));
        for (Integer key : role.getSkills().keySet()) {
            RoleSkill skill = (RoleSkill)role.getSkills().get(key);
            if (skill.getSkillId().equals(Integer.valueOf(30041))) {
                continue;
            }
            SysSkill sysSkill = (SysSkill)data_skill.get(skill.getSkillId());
            Integer proficient = (Integer)skillProficient.get(sysSkill.getPos());
            skill.setProficient(proficient);
            skill.setMaxProficient(sysRbAttr.getMaxSkillExp());
        }
        ((SkillService)SpringUtils.getBean(SkillService.class)).loadRoleSkill(role);
        this.equipService.checkEquipValid(role);
        role.getExtendBox().setAutoFightSet(new HashMap());
        ceService.chageRace(role);
        role.save();
        EventDispatcher.getInstance().fireEvent(new com.kitty.listener.event.RoleChangeEvent(EventType.ROLE_CHANGE, role, null, null, type));
    }
    
    public void changeRelive(Role role, Integer serial) {
        SysHero hero = (SysHero)PartDataPool.heroMap.get(serial);
        if (hero != null) {
            if ((int)role.getRelive() < 3) {
                for (Integer key : role.getSkills().keySet()) {
                    RoleSkill skill = (RoleSkill)role.getSkills().get(key);
                    if (skill.getSkillId().equals(Integer.valueOf(30041))) {
                        continue;
                    }
                    if ((int)skill.getProficient() < (int)skill.getMaxProficient()) {
                        MessagePusher.pushMessage(role, new RespMsg("法术#<Y>熟练#未满"));
                        return;
                    }
                    continue;
                }
                RoleDayInCeService ceService = (RoleDayInCeService)SpringUtils.getBean(RoleDayInCeService.class);
                if (ceService.checkDemount(role, hero.getRace()) > 0) {
                    MessagePusher.pushMessage((long)role.getId(), new RespMsg("天策符背包格子不足，无法转生"));
                    return;
                }
                if (!this.deductCurrency(role, Const.RESTYPE_GOLD, 10L, "角色转生")) {
                    return;
                }
                SysRbAttr sysRbAttr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf((int)role.getRelive() + 2));
                int count = FormulaHero.restorePorCount((int)role.getRelive() + 1, (int)sysRbAttr.getInitLv(), LOGIC.LOGICTYPE_HERO.getValue());
                role.setGg(sysRbAttr.getInitLv());
                role.setLx(sysRbAttr.getInitLv());
                role.setLl(sysRbAttr.getInitLv());
                role.setMj(sysRbAttr.getInitLv());
                role.setFpoint(Integer.valueOf(count));
                role.setRace(Integer.valueOf(hero.getRace()));
                role.setSex(Integer.valueOf(hero.getGender()));
                role.setIcon((int)hero.getShape());
                role.getTask().setIcon((int)hero.getShape());
                role.setSerial(hero.getSerial());
                role.setRelive(Integer.valueOf((int)role.getRelive() + 1));
                role.setLevel(sysRbAttr.getInitLv());
                MessagePusher.pushMessage(role, new RespHint(8, (short)1));
                SpringUtils.getHorseService().convertMount(role);
                SpringUtils.getBroadcastService().sendUpdateAppear(role);
                role.setSkills(this.createSkillBox(role, role.getRace(), role.getSex(), sysRbAttr));
                for (Integer key2 : role.getSkills().keySet()) {
                    RoleSkill skill2 = (RoleSkill)role.getSkills().get(key2);
                    if (skill2.getSkillId().equals(Integer.valueOf(30041))) {
                        continue;
                    }
                    skill2.setProficient(sysRbAttr.getInitSkillExp());
                    skill2.setMaxProficient(sysRbAttr.getMaxSkillExp());
                }
                ((SkillService)SpringUtils.getBean(SkillService.class)).loadRoleSkill(role);
                this.equipService.checkEquipValid(role);
                Integer reborn = DataGlobalFunc.getRoleReborn((int)role.getRelive(), (int)role.getRace(), (int)role.getSex());
                role.getRest().getCorrection().put(role.getRelive(), reborn);
                this.mindService.correction(role);
                this.calculateBase(role);
                this.refreshRole(role);
                this.sendExtraAttribute(role);
                role.getExtendBox().setAutoFightSet(new HashMap());
                ceService.chageRace(role);
                role.save();
                EventDispatcher.getInstance().fireEvent(new com.kitty.listener.event.RoleChangeEvent(EventType.ROLE_CHANGE, role, null, null, com.kitty.listener.event.RoleChangeEvent.REI));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("转生已上限"));
            }
        }
    }
    
    public Map<Integer, Integer> changeSkillProficient(Map<Integer, RoleSkill> map, Map<Integer, SysSkill> data_skill) {
        Map<Integer, Integer> hashMap = new HashMap();
        for (Integer key : map.keySet()) {
            RoleSkill roleSkill = (RoleSkill)map.get(key);
            SysSkill sysSkill = (SysSkill)data_skill.get(roleSkill.getSkillId());
            if ((int)sysSkill.getPos() > 0) {
                hashMap.put(sysSkill.getPos(), roleSkill.getProficient());
            }
            else {
                continue;
            }
        }
        return hashMap;
    }
    
    public void reliveCorrection(Role role, String string) {
        if ((int)role.getRelive() >= 0) {
            if (this.bagService.consumeBagGoods(role, 90008, 1, false, true, "转生修正") != null) {
                MessagePusher.pushMessage(role, new RespMsg("您背包中没有回梦丹"));
                return;
            }
            NutMap map = (NutMap)Json.fromJson(NutMap.class, string);
            Map<Integer, Integer> correction = new HashMap();
            for (String key : map.keySet()) {
                int rei = (int)Integer.valueOf(key);
                if (rei <= (int)role.getRelive()) {
                    correction.put(Integer.valueOf(rei), Integer.valueOf(map.getInt(key)));
                }
                else {
                    continue;
                }
            }
            role.getRest().setCorrection(correction);
            this.calculateBase(role);
            this.refreshRole(role);
            MessagePusher.pushMessage(role, new RespUpdateImprovement((int)role.getRoleId(), this.correction(role, new ArrayList<>())));
            MessagePusher.pushMessage(role, new RespHint(10, (short)1));
            MessagePusher.pushMessage(role, new RespMsg("修改转生修正成功"));
            role.save();
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("请先进行转生"));
        }
    }
    
    public void recover(Role role, byte type) {
        String msg = null;
        if (type == 0) {
            Pet pet = (Pet)this.petService.selectPet(role).get(role.getExtendBox().getCombatPet());
            if (pet != null) {
                if (pet.getCurrLife() >= (int)pet.getMaxLife() && (int)pet.getCurrMana() >= (int)pet.getMaxMana()) {
                    msg = "生命值和法力值已满，不需要补充。";
                }
                else {
                    pet.setCurrLife((int)pet.getMaxLife());
                    pet.setCurrMana(pet.getMaxMana());
                    RespUpdate respUpdate2 = new RespUpdate();
                    List<FiedValue> list2 = new ArrayList<>();
                    respUpdate2.setId((int)pet.getPetId());
                    List<PetEquip> equips = this.petService.getPetEffective((long)role.getId(), pet);
                    list2.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countPetLife(pet, equips))));
                    list2.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countPetMana(pet, equips))));
                    list2.add(new FiedValue("curr_life", (byte)3, Integer.valueOf(pet.getCurrLife())));
                    list2.add(new FiedValue("curr_mana", (byte)3, pet.getCurrMana()));
                    respUpdate2.setList(list2);
                    MessagePusher.pushMessage(role, respUpdate2);
                }
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("当前没有出战宠物"));
            }
        }
        else if ((int)role.getCurrLife() >= (int)role.getMaxLife() && (int)role.getCurrMana() >= (int)role.getMaxMana()) {
            msg = "生命值和法力值已满，不需要补充。";
        }
        else {
            role.setCurrLife(role.getMaxLife());
            role.setCurrMana(role.getMaxMana());
            RespUpdate respUpdate = new RespUpdate();
            List<FiedValue> list = new ArrayList<>();
            respUpdate.setId((int)role.getRoleId());
            list.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countLife(role))));
            list.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countMana(role))));
            list.add(new FiedValue("curr_life", (byte)3, role.getCurrLife()));
            list.add(new FiedValue("curr_mana", (byte)3, role.getCurrMana()));
            respUpdate.setList(list);
            MessagePusher.pushMessage(role, respUpdate);
        }
        if (msg != null) {
            MessagePusher.pushMessage(role, new RespMsg(msg));
        }
    }
    
    public void assignAttrib(Role role, ReqAssignAttrib assignAttrib) {
        if (!SafeUtil.isUnlock(role)) {
            return;
        }
        String msg = "加点成功";
        if (assignAttrib.getGg() < 0) {
            assignAttrib.setGg(0);
        }
        if (assignAttrib.getLl() < 0) {
            assignAttrib.setLl(0);
        }
        if (assignAttrib.getLx() < 0) {
            assignAttrib.setLx(0);
        }
        if (assignAttrib.getMj() < 0) {
            assignAttrib.setMj(0);
        }
        int count = assignAttrib.getGg() + assignAttrib.getLx() + assignAttrib.getLl() + assignAttrib.getMj();
        if (role.getRoleId().equals(Integer.valueOf(assignAttrib.getId()))) {
            if ((int)role.getFpoint() < count) {
                return;
            }
            Integer restorePorCount2 = Integer.valueOf(FormulaHero.restorePorCount((int)role.getRelive(), (int)role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue()));
            if (count > (int)role.getFpoint() || (int)role.getGg() - (int)role.getLevel() + (int)role.getMj() - (int)role.getLevel() + (int)role.getLl() - (int)role.getLevel() + (int)role.getLx() - (int)role.getLevel() + count > (int)restorePorCount2) {
                msg = "加点失败";
                this.logger.error("角色:[{}],Id:[{}],加点超出:[{}],未分配点:[{}]", new Object[] { role.getName(), role.getRoleId(), Integer.valueOf(count), role.getFpoint() });
            }
            else {
                if (assignAttrib.getGg() > 0) {
                    role.setGg(Integer.valueOf((int)role.getGg() + assignAttrib.getGg()));
                }
                if (assignAttrib.getLx() > 0) {
                    role.setLx(Integer.valueOf((int)role.getLx() + assignAttrib.getLx()));
                }
                if (assignAttrib.getLl() > 0) {
                    role.setLl(Integer.valueOf((int)role.getLl() + assignAttrib.getLl()));
                }
                if (assignAttrib.getMj() > 0) {
                    role.setMj(Integer.valueOf((int)role.getMj() + assignAttrib.getMj()));
                }
                role.setFpoint(Integer.valueOf((int)role.getFpoint() - count));
                RespUpdate respUpdate = new RespUpdate();
                respUpdate.setId((int)role.getRoleId());
                List<FiedValue> list = new ArrayList<>();
                this.calculateBase(role);
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
                MessagePusher.pushMessage(role, respUpdate);
            }
        }
        else {
            Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(assignAttrib.getId()));
            if (pet != null) {
                if ((int)pet.getFpoint() < count) {
                    return;
                }
                Integer restorePorCount = Integer.valueOf(FormulaHero.restorePorCount((int)pet.getRebirth(), (int)pet.getPetLv(), LOGIC.LOGICTYPE_PET.getValue()));
                if (count > (int)pet.getFpoint() || (int)pet.getGg() - (int)pet.getPetLv() + (int)pet.getMj() - (int)pet.getPetLv() + (int)pet.getLl() - (int)pet.getPetLv() + (int)pet.getLx() - (int)pet.getPetLv() + count > (int)restorePorCount) {
                    msg = "加点失败";
                    this.logger.error("角色:[{}],角色id:[{}],宠物Id:[{}],加点超出:[{}],未分配点:[{}]", new Object[] { role.getName(), role.getRoleId(), pet.getId(), Integer.valueOf(count), pet.getFpoint() });
                }
                else {
                    if (assignAttrib.getGg() > 0) {
                        pet.setGg(Integer.valueOf((int)pet.getGg() + assignAttrib.getGg()));
                    }
                    if (assignAttrib.getLx() > 0) {
                        pet.setLx(Integer.valueOf((int)pet.getLx() + assignAttrib.getLx()));
                    }
                    if (assignAttrib.getLl() > 0) {
                        pet.setLl(Integer.valueOf((int)pet.getLl() + assignAttrib.getLl()));
                    }
                    if (assignAttrib.getMj() > 0) {
                        pet.setMj(Integer.valueOf((int)pet.getMj() + assignAttrib.getMj()));
                    }
                    int fpoin = (int)pet.getFpoint() - count;
                    if (fpoin < 0) {
                        this.logger.error("宠物分配点数扣除后小于0宠物唯一编号 = {}", pet.getId());
                        fpoin = 0;
                    }
                    pet.setFpoint(Integer.valueOf(fpoin));
                    this.petService.calculateBase((long)role.getId(), pet);
                    this.petService.getArenaScore((long)role.getId(), pet);
                    this.petService.sendPetPanels(role, pet);
                    pet.save();
                }
            }
        }
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(msg));
        return;
    }
    
    public void extendStoreSize(Role role, ReqExtendStoreSize extendStoreSize) {
        boolean flag = false;
        if (LOGIC.LOGICTYPE_HERO.getValue() == extendStoreSize.type) {
            if (extendStoreSize.isStore == null || (int)extendStoreSize.isStore == 0) {
                int num = (int)role.getExtendBox().getPosition() + 1;
                Item item = (Item)ConvertDataPool.bagExtend.get(Integer.valueOf(num));
                if (item != null && this.deductCurrency(role, item.getItem(), (long)(int)item.getNum(), "扩展背包格子")) {
                    role.getExtendBox().setPosition(Integer.valueOf(num));
                    flag = true;
                }
                else {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("扩展背包格子已上限"));
                }
            }
            else {
                int numStore = (int)role.getExtendBox().getStorePosition() + 1;
                Item itemStore = (Item)ConvertDataPool.storeExtend.get(Integer.valueOf(numStore));
                if (itemStore != null && this.deductCurrency(role, itemStore.getItem(), (long)(int)itemStore.getNum(), "扩展仓库格子")) {
                    role.getExtendBox().setStorePosition(Integer.valueOf(numStore));
                    flag = true;
                }
                else {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("扩展仓库格子已上限"));
                }
            }
        }
        else if (LOGIC.LOGICTYPE_PET.getValue() == extendStoreSize.type) {
            if (extendStoreSize.isStore == null || (int)extendStoreSize.isStore == 0) {
                int buyPetGrid = role.getExtendBox().getBuyPetGrid();
                if (buyPetGrid + 1 > (int)Const.BUY_PET_GRID) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("扩展宠物携带已上限"));
                    return;
                }
                Long money = Const.ADD_PET_LIMIT_COST[buyPetGrid];
                if ((long)role.getSilver() >= (long)money) {
                    this.deductCurrency(role, Const.RESTYPE_SILVER, (long)money, "扩展宠物格子");
                    role.getExtendBox().setBuyPetGrid(buyPetGrid + 1);
                    flag = true;
                }
                else {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("银两不足"));
                }
            }
            else {
                int num = (int)role.getExtendBox().getPetEquipPosition() + 1;
                Item item = (Item)ConvertDataPool.petBagEquipExtend.get(Integer.valueOf(num));
                if (item != null && this.deductCurrency(role, item.getItem(), (long)(int)item.getNum(), "扩展背包格子")) {
                    role.getExtendBox().setPetEquipPosition(Integer.valueOf(num));
                    flag = true;
                }
                else {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("扩展背包格子已上限"));
                }
            }
        }
        if (flag) {
            RespUpdate respUpdate = new RespUpdate();
            respUpdate.setId((int)role.getRoleId());
            List<FiedValue> list = new ArrayList<>();
            list.add(new FiedValue("buyPetGrid", (byte)3, Integer.valueOf(role.getExtendBox().getBuyPetGrid())));
            list.add(new FiedValue("buyBagGrid", (byte)3, role.getExtendBox().getPosition()));
            list.add(new FiedValue("buyStoreGrid", (byte)3, role.getExtendBox().getStorePosition()));
            list.add(new FiedValue("buyPetEquipGrid", (byte)3, role.getExtendBox().getPetEquipPosition()));
            respUpdate.setList(list);
            MessagePusher.pushMessage(role, respUpdate);
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
            if (fightObject == null) {
                return;
            }
            if (role.getExtendBox().getAutoFight() == 1) {
                fightObject.setAutoFight(1);
                ScheduledFuture<?> timer = fight.getTimeOutTask();
                SkillAutoPicker autoPicker = fightNewService.getAutoObject(fightObject, fight);
                ReqFightHandle reqFightHandle = new ReqFightHandle();
                reqFightHandle.setRound(fight.getRound());
                reqFightHandle.setAutoFight(1);
                reqFightHandle.setAttackerId((int)fightObject.getId());
                reqFightHandle.setTargetId(autoPicker.getTargetId());
                reqFightHandle.setSkillId(autoPicker.getSkillId());
                reqFightHandle.setType(autoPicker.getType());
                List<RespFightCommandInfo> fightCommandInfos = new ArrayList<>();
                if (timer != null && (!timer.isDone() || !timer.isCancelled()) && !fightObject.isDoAction()) {
                    if (autoPicker.isDef()) {
                        reqFightHandle.setType(1);
                    }
                    else if (reqFightHandle.getType() == 0) {
                        reqFightHandle.setType(0);
                    }
                    else {
                        reqFightHandle.setType(7);
                    }
                    fightNewService.doActionNew(reqFightHandle);
                }
                RespFightCommandInfo fightCommandInfoRole = new RespFightCommandInfo();
                fightCommandInfoRole.setId((int)fightObject.getId());
                fightCommandInfoRole.setSkillId(autoPicker.getSkillId());
                fightCommandInfoRole.setType(autoPicker.getType());
                fightCommandInfoRole.setAction(1);
                if ((int)role.getRace() == RACE.RACE_LONG.getValue()) {
                    Map<String, Object> autoSet = role.getExtendBox().getAutoFightSet();
                    Object born = autoSet.get("born");
                    if (born != null) {
                        fightCommandInfoRole.setBorn((int)Integer.valueOf(String.valueOf(born)));
                    }
                    else {
                        fightCommandInfoRole.setBorn(1);
                    }
                }
                fightCommandInfos.add(fightCommandInfoRole);
                if (fightObject.getFightPetId() > 0) {
                    FightObject petObject2 = fight.queryFightObject(Integer.valueOf(fightObject.getFightPetId()));
                    if (petObject2 != null && !petObject2.isFallDown()) {
                        petObject2.setAutoFight(1);
                        autoPicker = fightNewService.getAutoObject(petObject2, fight);
                        if (timer != null && (!timer.isDone() || !timer.isCancelled()) && !petObject2.isDoAction()) {
                            reqFightHandle = new ReqFightHandle();
                            reqFightHandle.setRound(fight.getRound());
                            reqFightHandle.setAutoFight(1);
                            reqFightHandle.setAttackerId((int)petObject2.getId());
                            reqFightHandle.setTargetId(autoPicker.getTargetId());
                            reqFightHandle.setType(autoPicker.getType());
                            reqFightHandle.setSkillId(autoPicker.getSkillId());
                            if (autoPicker.isDef()) {
                                reqFightHandle.setType(1);
                            }
                            else if (reqFightHandle.getType() == 7) {
                                reqFightHandle.setType(7);
                            }
                            else {
                                reqFightHandle.setType(0);
                            }
                            fightNewService.doActionNew(reqFightHandle);
                        }
                        RespFightCommandInfo fightCommandInfoPet = new RespFightCommandInfo();
                        fightCommandInfoPet.setId((int)petObject2.getId());
                        fightCommandInfoPet.setSkillId(autoPicker.getSkillId());
                        fightCommandInfoPet.setType(autoPicker.getType());
                        fightCommandInfoPet.setAction(1);
                        fightCommandInfos.add(fightCommandInfoPet);
                    }
                }
                if (fightCommandInfos.size() <= 0) {
                    MessageAssistUtil.sendIsSuccess(role, (int)fightObject.getId(), (byte)5, null);
                }
                else {
                    MessageAssistUtil.sendIsSuccess(role, (int)fightObject.getId(), (byte)5, fightCommandInfos);
                }
            }
            else {
                fightObject.setAutoFight(0);
                if (fightObject.getFightPetId() > 0) {
                    FightObject petObject = fight.queryFightObject(Integer.valueOf(fightObject.getFightPetId()));
                    if (petObject != null) {
                        petObject.setAutoFight(0);
                    }
                }
                if (fightObject.isDoAction()) {
                    MessageAssistUtil.sendIsSuccess(role, (int)fightObject.getId(), (byte)7, null);
                }
                else {
                    MessageAssistUtil.sendIsSuccess(role, (int)fightObject.getId(), (byte)6, null);
                    fight.removeHostTask((int)fightObject.getId());
                    fight.removePetTask(fightObject.getFightPetId());
                }
            }
        }
        catch (Exception e) {
            this.logger.error("战斗中设置自动战斗错误=让目标防御={}", Lang.getStackTrace(e));
            ReqFightHandle reqFightHandle2 = new ReqFightHandle();
            reqFightHandle2.setRound(fight.getRound());
            reqFightHandle2.setAutoFight(1);
            reqFightHandle2.setAttackerId((int)role.getRoleId());
            reqFightHandle2.setTargetId((int)role.getRoleId());
            reqFightHandle2.setSkillId(0);
            reqFightHandle2.setType(1);
            fightNewService.doActionNew(reqFightHandle2);
        }
    }
    
    public void dye(Role role, ReqDey reqDey) {
        boolean clicker = this.mouserClicker(role, "dye", 250L);
        if (!clicker) {
            return;
        }
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
                Optional<ChangeColor> optional = colors.stream().filter(data -> data.getSerial() == reqDey.getColorOne()).findFirst();
                if (optional.isPresent()) {
                    ChangeColor changeColor = (ChangeColor)optional.get();
                    map.setv("a", Integer.valueOf(changeColor.getSerial()));
                    a += changeColor.getGoodsNum()[0];
                    b += changeColor.getGoodsNum()[1];
                    c += changeColor.getGoodsNum()[2];
                }
            }
            if (reqDey.getColorTwo() > 0) {
                List<ChangeColor> colors = JSON.parseArray(color.getColorTwo(), ChangeColor.class);
                Optional<ChangeColor> optional = colors.stream().filter(data -> data.getSerial() == reqDey.getColorTwo()).findFirst();
                if (optional.isPresent()) {
                    ChangeColor changeColor = (ChangeColor)optional.get();
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
            if (silver > 0L && (long)role.getSilver() <= silver) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "客官，您的银两不足", false));
                return;
            }
            Integer a_nu = this.bagService.getBagGoods(role, 75055, a);
            Integer b_nu = this.bagService.getBagGoods(role, 75056, b);
            Integer c_nu = this.bagService.getBagGoods(role, 75057, c);
            if (a_nu == null && b_nu == null && c_nu == null) {
                this.bagService.consumeBagGoods(role, 75055, a, true, true, null);
                this.bagService.consumeBagGoods(role, 75056, b, true, true, null);
                this.bagService.consumeBagGoods(role, 75057, c, true, true, null);
                this.deductCurrency(role, Const.RESTYPE_SILVER, silver, null);
                role.setColor(map.toString());
                role.save();
                RespUpdate respUpdate = new RespUpdate();
                respUpdate.setId((int)role.getRoleId());
                List<FiedValue> list = this.deyList(role.getColor());
                respUpdate.setList(list);
                MessagePusher.pushMessage(role, respUpdate);
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "染色成功", false));
                EventDispatcher.getInstance().fireEvent(new RoleAspectChangeEvent(EventType.ROLE_ASPECT_CHANGE, role));
            }
            else {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "客官，您的染料不足，巧妇难为无米之炊啊", false));
            }
        }
        return;
    }
    
    public void practiceUpGrade(Role role, int grade) {
        PartyPractice practiceInfo = role.getRest().getPartyPractice();
        Map<Integer, SysPartyPractice> map = ExpDataPool.partyPractice;
        Integer partyPracticeLv = Integer.valueOf(practiceInfo.getLevel());
        int sumGrade = grade + (int)partyPracticeLv;
        SysPartyPractice practice = (SysPartyPractice)map.get(Integer.valueOf(sumGrade));
        int need = (int)role.getRelive() * 1000 + (int)role.getLevel();
        if (need < practice.getNeed()) {
            int level = practice.getNeed() % 1000;
            int relive = practice.getNeed() / 1000;
            String msg = "你的#<R>角色等级#不足";
            if (relive > 0) {
                msg = msg + "#<R>" + relive + "转" + level + "级#";
            }
            else {
                msg = msg + "#<R>" + level + "级#";
            }
            MessagePusher.pushMessage(role, new RespMsg(msg));
            return;
        }
        else {
            int needTribute = 0;
            int needCoin = 0;
            for (int i = (int)partyPracticeLv + 1; i <= sumGrade; ++i) {
                SysPartyPractice partyPractice = (SysPartyPractice)map.get(Integer.valueOf(i));
                if (partyPractice != null) {
                    needTribute += partyPractice.getTribute();
                    needCoin += partyPractice.getCoin();
                }
            }
            if (!this.checkCurrency(role, Const.RESTYPE_COIN, (long)needCoin)) {
                return;
            }
            if (!this.checkCurrency(role, Const.RESTYPE_TRIBUTE, (long)needTribute)) {
                return;
            }
            this.deductCurrency(role, Const.RESTYPE_COIN, (long)needCoin, "帮派修炼提升：扣除贡币");
            this.deductCurrency(role, Const.RESTYPE_TRIBUTE, (long)needTribute, "帮派修炼提升：扣除帮贡");
            practiceInfo.setPoints(practiceInfo.getPoints() + (sumGrade - (int)partyPracticeLv));
            practiceInfo.setLevel(sumGrade);
            role.save();
            this.refreshPartyPracticeInfo(role);
            return;
        }
    }
    
    public void assignPartyPractice(Role role, String nutMap) {
        Map<Integer, Integer> practiceMap = role.getRest().getParty();
        PartyPractice partyPractice = role.getRest().getPartyPractice();
        NutMap map = (NutMap)Json.fromJson(NutMap.class, nutMap);
        List<Integer> list = FACTION_PRO_DICT.toList();
        int count = 0;
        for (String key : map.keySet()) {
            Integer pro = Integer.valueOf(key);
            boolean contains = list.contains(pro);
            if (!contains) {
                MessagePusher.pushMessage(role, new RespMsg("修炼属性中没有该修炼类型"));
                return;
            }
            int num = map.getInt(key);
            if (num < 0) {
                return;
            }
            if (FACTION_PRO_DICT.K_KZ_CAP.getPro() == (int)pro) {
                int limit = partyPractice.getLevel() / 5;
                if (num > limit) {
                    MessagePusher.pushMessage(role, new RespMsg("每提升修炼等级5级可分配1点上限，当前已达上限"));
                    return;
                }
            }
            count += num - (int)practiceMap.get(pro);
        }
        if (count > partyPractice.getPoints()) {
            MessagePusher.pushMessage(role, new RespMsg("剩余修炼点不足"));
            return;
        }
        for (String key : map.keySet()) {
            int num2 = map.getInt(key);
            if (num2 > 0) {
                practiceMap.put(Integer.valueOf(key), Integer.valueOf(num2));
            }
            else {
                continue;
            }
        }
        int points = partyPractice.getPoints() - count;
        partyPractice.setPoints((points < 0) ? 0 : points);
        List<FiedValue> faction = this.faction(role, new ArrayList<>());
        MessagePusher.pushMessage(role, new RespUpdateImprovement((int)role.getRoleId(), faction));
        this.refreshPartyPracticeInfo(role);
        role.save();
        return;
    }
    
    public void resetAssignPartyPractice(Role role) {
        if (!SafeUtil.isUnlock(role)) {
            return;
        }
        boolean currency = this.deductCurrency(role, Const.RESTYPE_SILVER, 200000L, "重置帮派修炼加点");
        if (!currency) {
            return;
        }
        PartyPractice practice = role.getRest().getPartyPractice();
        practice.setPoints(practice.getLevel());
        role.getRest().setParty(DataGlobalFunc.faction());
        List<FiedValue> list = this.faction(role, new ArrayList<>());
        MessagePusher.pushMessage(role, new RespUpdateImprovement((int)role.getRoleId(), list));
        MessagePusher.pushMessage(role, new RespMsg("重置成功"));
        this.refreshPartyPracticeInfo(role);
        role.save();
        return;
    }
    
    public void restAttribute(Role role, byte type) {
        if (!SafeUtil.isUnlock(role)) {
            return;
        }
        String msg = null;
        int serial = Const.rest_attribute[type];
        if (type == 0) {
            Integer count = Integer.valueOf(FormulaHero.restorePorCount((int)role.getRelive(), (int)role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue()));
            if (role.getFpoint().equals(count)) {
                msg = "你没有可以重置的属性点~";
            }
            else if (this.bagService.consumeBagGoods(role, serial, 1, false, true, "超级星梦石") == null) {
                role.setGg(role.getLevel());
                role.setLx(role.getLevel());
                role.setLl(role.getLevel());
                role.setMj(role.getLevel());
                role.setFpoint(count);
            }
            else {
                msg = "你没有超级星梦石";
            }
        }
        else if (type == 1) {
            if (role.getLevel().equals(role.getGg())) {
                msg = "你没有可以重置的属性点~";
            }
            else if (this.bagService.consumeBagGoods(role, serial, 1, false, true, "根骨星梦石") == null) {
                Integer count = Integer.valueOf(5);
                if ((int)role.getGg() - 5 < (int)role.getLevel()) {
                    count = Integer.valueOf((int)role.getGg() - (int)role.getLevel());
                }
                role.setGg(Integer.valueOf((int)role.getGg() - (int)count));
                role.setFpoint(Integer.valueOf((int)count + (int)role.getFpoint()));
            }
            else {
                msg = "你没有根骨星梦石";
            }
        }
        else if (type == 2) {
            if (role.getLevel().equals(role.getLx())) {
                msg = "你没有可以重置的属性点~";
            }
            else if (this.bagService.consumeBagGoods(role, serial, 1, false, true, "灵力星梦石") == null) {
                Integer count = Integer.valueOf(5);
                if ((int)role.getLx() - 5 < (int)role.getLevel()) {
                    count = Integer.valueOf((int)role.getLx() - (int)role.getLevel());
                }
                role.setLx(Integer.valueOf((int)role.getLx() - (int)count));
                role.setFpoint(Integer.valueOf((int)count + (int)role.getFpoint()));
            }
            else {
                msg = "你没有灵力星梦石";
            }
        }
        else if (type == 3) {
            if (role.getLevel().equals(role.getLl())) {
                msg = "你没有可以重置的属性点~";
            }
            else if (this.bagService.consumeBagGoods(role, serial, 1, false, true, "力量星梦石") == null) {
                Integer count = Integer.valueOf(5);
                if ((int)role.getLl() - 5 < (int)role.getLevel()) {
                    count = Integer.valueOf((int)role.getLl() - (int)role.getLevel());
                }
                role.setLl(Integer.valueOf((int)role.getLl() - (int)count));
                role.setFpoint(Integer.valueOf((int)count + (int)role.getFpoint()));
            }
            else {
                msg = "你没有力量星梦石";
            }
        }
        else if (type == 4) {
            if (role.getLevel().equals(role.getMj())) {
                msg = "你没有可以重置的属性点~";
            }
            else if (this.bagService.consumeBagGoods(role, serial, 1, false, true, "敏捷星梦石") == null) {
                Integer count = Integer.valueOf(5);
                if ((int)role.getMj() - 5 < (int)role.getLevel()) {
                    count = Integer.valueOf((int)role.getMj() - (int)role.getLevel());
                }
                role.setMj(Integer.valueOf((int)role.getMj() - (int)count));
                role.setFpoint(Integer.valueOf((int)count + (int)role.getFpoint()));
            }
            else {
                msg = "你没有敏捷星梦石";
            }
        }
        if (msg != null) {
            MessagePusher.pushMessage(role, new RespMsg(msg));
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("重置成功"));
            this.calculateBase(role);
            this.refreshRole(role);
            role.save();
        }
        return;
    }
    
    public void refreshPartyPracticeInfo(Role role) {
        PartyPractice practiceInfo = role.getRest().getPartyPractice();
        MessagePusher.pushMessage(role, new RespPartyPracticeInfo((short)practiceInfo.getLevel(), (short)practiceInfo.getPoints()));
    }
    
    public void setElseFight(Role role) {
        if (role.getExtendBox().getSetMap().size() <= 0) {
            role.getExtendBox().setSetMap(DataGlobalFunc.setting());
            role.save();
        }
        RespElseFight elseFight = new RespElseFight();
        elseFight.setAutoFight((int)role.getExtendBox().getSetMap().get("else_auto_fight"));
        elseFight.setShowDbSpeed((int)role.getExtendBox().getSetMap().get("else_db_speed_show"));
        MessagePusher.pushMessage(role, elseFight);
    }
    
    public int getRoleId() {
        return SpringUtils.getIdentityService().getNextId(IdentityType.ROLE);
    }
    
    public Role getOnlinePlayer(String name) {
        long uid = SpringUtils.getPlayerService().getUidBy(name);
        return this.getOnlinePlayer(uid);
    }
    
    public boolean checkCurrency(Role role, Integer type, long price) {
        if (type.equals(Const.RESTYPE_GOLD) && (long)role.getGold() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RESTYPE_SILVER) && (long)role.getSilver() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RESTYPE_COIN) && (long)role.getCoin() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RESTYPE_GUO) && (long)role.getExtendBox().getGuoScore() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RESTYPE_BRAC) && (long)role.getExtendBox().getBracScore() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RESTYPE_NAO) && (long)role.getExtendBox().getNaoScore() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RESTYPE_DI) && (long)role.getExtendBox().getDiScore() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RESTYPE_ARENA) && (long)role.getExtendBox().getArenaScore() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RESTYPE_LONG) && (long)role.getExtendBox().getLongScore() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RESTYPE_NICE) && (long)role.getExtendBox().getNiceScore() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RESTYPE_TRIBUTE) && (long)role.getExtendBox().getHelpScore() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RES_TYPE_FEATS) && (long)role.getExtendBox().getFeatsScore() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RES_TYPE_CHARGE_SCORE) && (long)role.getExtendBox().getChargeScore() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RES_TYPE_BOUND_GOLD) && (long)role.getExtendBox().getBoundGold() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
            return false;
        }
        if (type.equals(Const.RES_TYPE_ACHIEVEMENT) && (long)role.getExtendBox().getAchievementScore() < price) {
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(price)));
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
        if (type.equals(Const.RESTYPE_GOLD) && (long)role.getGold() >= num) {
            residue = (long)role.getGold() - num;
            role.setGold(Long.valueOf(residue));
        }
        else if (type.equals(Const.RESTYPE_SILVER) && (long)role.getSilver() >= num) {
            residue = (long)role.getSilver() - num;
            role.setSilver(Long.valueOf(residue));
        }
        else if (type.equals(Const.RESTYPE_COIN) && (long)role.getCoin() >= num) {
            residue = (long)role.getCoin() - num;
            role.setCoin(Long.valueOf(residue));
        }
        else if (type.equals(Const.RESTYPE_GUO) && (long)role.getExtendBox().getGuoScore() >= num) {
            residue = (long)role.getExtendBox().getGuoScore() - num;
            role.getExtendBox().setGuoScore(Long.valueOf(residue));
        }
        else if (type.equals(Const.RESTYPE_BRAC) && (long)role.getExtendBox().getBracScore() >= num) {
            residue = (long)role.getExtendBox().getBracScore() - num;
            role.getExtendBox().setBracScore(Long.valueOf(residue));
        }
        else if (type.equals(Const.RESTYPE_NAO) && (long)role.getExtendBox().getNaoScore() >= num) {
            residue = (long)role.getExtendBox().getNaoScore() - num;
            role.getExtendBox().setNaoScore(Long.valueOf(residue));
        }
        else if (type.equals(Const.RESTYPE_DI) && (long)role.getExtendBox().getDiScore() >= num) {
            residue = (long)role.getExtendBox().getDiScore() - num;
            role.getExtendBox().setDiScore(Long.valueOf(residue));
        }
        else if (type.equals(Const.RESTYPE_ARENA) && (long)role.getExtendBox().getArenaScore() >= num) {
            residue = (long)role.getExtendBox().getArenaScore() - num;
            role.getExtendBox().setArenaScore(Long.valueOf(residue));
        }
        else if (type.equals(Const.RESTYPE_LONG) && (long)role.getExtendBox().getLongScore() >= num) {
            residue = (long)role.getExtendBox().getLongScore() - num;
            role.getExtendBox().setLongScore(Long.valueOf(residue));
        }
        else if (type.equals(Const.RESTYPE_NICE) && (long)role.getExtendBox().getNiceScore() >= num) {
            residue = (long)role.getExtendBox().getNiceScore() - num;
            role.getExtendBox().setNiceScore(Long.valueOf(residue));
        }
        else if (type.equals(Const.RESTYPE_TRIBUTE) && (long)role.getExtendBox().getHelpScore() >= num) {
            residue = (long)role.getExtendBox().getHelpScore() - num;
            role.getExtendBox().setHelpScore(Long.valueOf(residue));
        }
        else if (type.equals(Const.RES_TYPE_FEATS) && (long)role.getExtendBox().getFeatsScore() >= num) {
            residue = (long)role.getExtendBox().getFeatsScore() - num;
            role.getExtendBox().setFeatsScore(Long.valueOf(residue));
        }
        else if (type.equals(Const.RES_TYPE_CHARGE_SCORE) && (long)role.getExtendBox().getChargeScore() >= num) {
            residue = (long)role.getExtendBox().getChargeScore() - num;
            role.getExtendBox().setChargeScore(Long.valueOf(residue));
        }
        else if (type.equals(Const.RES_TYPE_BOUND_GOLD) && (long)role.getExtendBox().getBoundGold() >= num) {
            residue = (long)role.getExtendBox().getBoundGold() - num;
            role.getExtendBox().setBoundGold(Long.valueOf(residue));
        }
        else {
            if (type.equals(Const.RES_TYPE_ACHIEVEMENT) && (long)role.getExtendBox().getAchievementScore() >= num) {
                residue = (long)role.getExtendBox().getAchievementScore() - num;
                role.getExtendBox().setAchievementScore(Long.valueOf(residue));
                return true;
            }
            MessagePusher.notify2Misc(role, false, (int)type, MoneyUtil.colours(Long.valueOf(num)));
            return false;
        }
        role.save();
        this.refreshRoleCurrency(role);
        RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)1000, (byte)2, String.format("货币类型：%d，消耗数量：%d，减少后数量%d，原因：%s", type, Long.valueOf(num), Long.valueOf(residue), string), role.getMarking());
        return true;
    }
    
    public Role getOnlinePlayer(long id) {
        if (DataCache.ONLINE_ROLES.containsKey(Long.valueOf(id))) {
            return (Role)DataCache.ONLINE_ROLES.get(Long.valueOf(id));
        }
        if (DataCache.OFFLINE_ROLES.containsKey(Long.valueOf(id))) {
            return null;
        }
        return SpringUtils.getPlayerService().getPlayerBy(id);
    }
    
    public Role getPlayer(long id) {
        if (DataCache.OFFLINE_ROLES.containsKey(Long.valueOf(id))) {
            return (Role)DataCache.OFFLINE_ROLES.get(Long.valueOf(id));
        }
        if (DataCache.ONLINE_ROLES.containsKey(Long.valueOf(id))) {
            return (Role)DataCache.ONLINE_ROLES.get(Long.valueOf(id));
        }
        return SpringUtils.getPlayerService().getPlayerBy(id);
    }
    
    public Role getOnlinePlayer(int roleId) {
        long id = SpringUtils.getPlayerService().getUidBy(roleId);
        if (DataCache.OFFLINE_ROLES.containsKey(Long.valueOf(id))) {
            return null;
        }
        if (DataCache.ONLINE_ROLES.containsKey(Long.valueOf(id))) {
            return (Role)DataCache.ONLINE_ROLES.get(Long.valueOf(id));
        }
        return SpringUtils.getPlayerService().getPlayerBy(id);
    }
    
    public Role getOfOnlinePlayer(int roleId) {
        long id = SpringUtils.getPlayerService().getUidBy(roleId);
        if (DataCache.OFFLINE_ROLES.containsKey(Long.valueOf(id))) {
            return (Role)DataCache.OFFLINE_ROLES.get(Long.valueOf(id));
        }
        Role role = SpringUtils.getPlayerService().getPlayerBy(id);
        return role;
    }
    
    public Role getPlayer(int roleId) {
        long id = SpringUtils.getPlayerService().getUidBy(roleId);
        if (this.isOnline(id)) {
            return this.getOnlinePlayer(id);
        }
        return this.getOfOnlinePlayer(roleId);
    }
    
    public void recordPosition(Role role, ReqMultiMoveTo reqMultiMoveTo) {
        role.setPos((int)reqMultiMoveTo.getX(), (int)reqMultiMoveTo.getY(), 0, reqMultiMoveTo.getMapId());
        if (this.teamService.isTeamLeader(role)) {
            Team team = this.teamService.getTeam((int)role.getRoleId());
            if (team != null) {
                for (int i = 0; i < team.getList().size(); ++i) {
                    Member member = (Member)team.getList().get(i);
                    if (member != null && !member.getId().equals(role.getId()) && member.isInTeam()) {
                        Role memberRole = this.getPlayer((long)member.getId());
                        if (memberRole != null && role.getPos().getMapId().equals(memberRole.getPos().getMapId())) {
                            memberRole.setPos((int)reqMultiMoveTo.getX(), (int)reqMultiMoveTo.getY(), 0, reqMultiMoveTo.getMapId());
                            SceneManager.INSTANCE.movePosition(memberRole);
                        }
                    }
                }
            }
        }
    }
    
    public void punishFightDead(Role role, long exp, String reason) {
        if (role.getLastDead() > 0) {
            return;
        }
        this.addExp(role, exp, reason, true);
        return;
    }
    
    public void sendOffline(Role role, boolean flag) {
        long millis = System.currentTimeMillis();
        String marking = ((ServerService)SpringUtils.getBean(ServerService.class)).getMarking();
        String key = role.getId() + "_countdown_" + marking + millis / 1000L;
        if ((boolean)this.redisService.exists(key)) {
            return;
        }
        this.redisService.set(key, "", 0L);
        ThreadLocalUtil.addLocalTask(role, () -> {
            int time = 300000;
            if (flag) {
                Team team = ((TeamService)SpringUtils.getBean(TeamService.class)).getTeam((int)role.getRoleId());
                if (team != null) {
                    time = role.getOfflineNotice().getTeamOfflineTime();
                }
                else {
                    time = role.getOfflineNotice().getAloneOfflineTime();
                }
            }
            role.setOfLineTime(Long.valueOf(millis));
            role.setState(2);
            role.save();
            ((RabbitService)SpringUtils.getBean(RabbitService.class)).sendMessage(key, Integer.valueOf(time));
            return;
        });
        return;
    }
    
    public long getOnlineSec(Role role) {
        long now = System.currentTimeMillis();
        if (now > role.getOnlineLoginTime()) {
            int currentOnlineSec = (int)((now - role.getOnlineLoginTime()) / 1000L);
            return (long)currentOnlineSec + (long)role.getPreOnlineSec();
        }
        return 0L;
    }
    
    public void resetOnlineSec(Role role) {
        role.setPreOnlineSec(Long.valueOf(0L));
        role.setOnlineLoginTime(System.currentTimeMillis());
    }
    
    public void checkReset(Role role) {
        this.checkDailyFiveReset(role);
        this.checkWeeklyReset(role);
    }
    
    public void checkDailyFiveReset(Role role) {
        if (!this.isCanDailyFiveReset(role)) {
            return;
        }
        this.doDailyFiveReset(role, false);
        return;
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
        if (flag) {
            this.checkWeeklyReset(role);
        }
        role.setDailyFiveResetTime(Long.valueOf(System.currentTimeMillis()));
        this.resetOnlineSec(role);
        ((SafeService)SpringUtils.getBean(SafeService.class)).checkSafePasswordReset(role);
        this.resetQuotaLimit(role);
        role.getExtendBox().setDayQuotaLimit(NutMap.NEW());
        ((ActivityService)SpringUtils.getBean(ActivityService.class)).dailyResetActivity(role);
        ((TaskService)SpringUtils.getBean(TaskService.class)).dailyResetTask(role);
        role.save();
        ((WelfareService)SpringUtils.getBean(WelfareService.class)).dailyResetFiveGuess(role);
    }
    
    private boolean isCanDailyFiveReset(Role role) {
        long dailyFiveResetTime = (long)role.getDailyFiveResetTime();
        Calendar roleResetCalader = Calendar.getInstance();
        roleResetCalader.setTimeInMillis(dailyFiveResetTime);
        Calendar systemResetTime = this.getSystemFiveResetTime();
        return roleResetCalader.before(systemResetTime);
    }
    
    private Calendar getSystemFiveResetTime() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(11) < 5) {
            calendar.add(6, -1);
        }
        calendar.set(11, 5);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar;
    }
    
    public void checkWeeklyReset(Role role) {
        if (!this.isCanWeeklyReset(role)) {
            this.logger.info("不能进行每周重置，角色ID: {}, 上一次重置时间戳：{}", role.getRoleId(), Long.valueOf(role.getWeeklyResetTime()));
            return;
        }
        this.doWeeklyReset(role);
        return;
    }
    
    public boolean isCanWeeklyReset(Role role) {
        long weeklyResetTime = role.getWeeklyResetTime();
        Calendar roleResetCalader = Calendar.getInstance();
        roleResetCalader.setTimeInMillis(weeklyResetTime);
        Calendar systemResetTime = this.getSystemWeeklyResetTime();
        return roleResetCalader.before(systemResetTime);
    }
    
    public Calendar getSystemWeeklyResetTime() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(7) < 2 || (calendar.get(7) == 2 && calendar.get(11) < 5)) {
            calendar.add(6, -7);
        }
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
        role.getPkInfoBox().setKillRoles(new HashMap());
        role.getPkInfoBox().setBeKillRoles(new HashMap());
        role.save();
        this.petService.doWeeklyReset(role);
    }
    
    public Role getPlayerBy(int roleId) {
        long id = SpringUtils.getPlayerService().getUidBy(roleId);
        return this.getPlayerBy(id);
    }
    
    public Role getPlayerBy(long id) {
        return SpringUtils.getPlayerService().getPlayerBy(id);
    }
    
    public void modifyName(Role role, String newName) {
        if (!SafeUtil.isUnlock(role)) {
            return;
        }
        Integer num = this.bagService.getBagGoodsNum(role, Integer.valueOf(90011));
        IoSession session = SessionUtils.getSessionBy((long)role.getId());
        if (!this.canCreateRole(session, newName, LOGIC.LOGICTYPE_HERO.getValue())) {
            return;
        }
        newName = CheckCharUtils.replaceName(newName);
        if ((int)num >= 1) {
            Integer integer = this.bagService.consumeBagGoods(role, 90011, 1, false, true, "角色改名");
            if (integer != null) {
                return;
            }
        }
        else {
            boolean flag = this.deductCurrency(role, Const.RESTYPE_GOLD, 600L, "角色改名");
            if (!flag) {
                return;
            }
        }
        String oldName = role.getName();
        role.setName(newName);
        role.save();
        SpringUtils.getPlayerService().updateRoleName(oldName, role);
        MessagePusher.pushMessage(role, new RespHint(3, (short)1));
        this.broadcastService.sendUpdateAppear(role);
        EventDispatcher.getInstance().fireEvent(new com.kitty.listener.event.RoleChangeEvent(EventType.ROLE_CHANGE, role, oldName, newName, com.kitty.listener.event.RoleChangeEvent.NAME));
        return;
    }
    
    public void checkReserve(Role role) {
        String type = "即将";
        if ((long)role.getSaveLife() <= 250000L) {
            MessagePusher.notify2Misc(role, true, 100, "气血", ((long)role.getSaveLife() > 0L) ? type : "已");
        }
        if ((long)role.getSaveMana() <= 250000L) {
            MessagePusher.notify2Misc(role, true, 100, "法力", ((long)role.getSaveMana() > 0L) ? type : "已");
        }
    }
    
    public void chatEquipDetail(Role ownerRole, Role role, long equipId) {
        List<WearEquip> wearEquips = this.equipService.selectWearEquip(ownerRole);
        WearEquip wearEquip = null;
        int i = wearEquips.size() - 1;
        while (i >= 0) {
            if (((WearEquip)wearEquips.get(i)).getId().equals(Long.valueOf(equipId))) {
                wearEquip = (WearEquip)wearEquips.get(i);
                break;
            }
            --i;
        }
        if (wearEquip != null) {
            RespMarketGoodCard card2 = new RespMarketGoodCard(String.valueOf(wearEquip.getId()), (int)ownerRole.getRoleId(), wearEquip.getType(), wearEquip.getField(), wearEquip.getArtifice());
            MessagePusher.pushMessage(role, card2);
            return;
        }
        Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(ownerRole);
        BagEquip bagEquip = (BagEquip)bagEquipMap.get(Long.valueOf(equipId));
        if (bagEquip != null) {
            RespMarketGoodCard card = new RespMarketGoodCard(String.valueOf(bagEquip.getId()), (int)ownerRole.getRoleId(), bagEquip.getType(), bagEquip.getField(), bagEquip.getArtifice());
            MessagePusher.pushMessage(role, card);
            return;
        }
        MessagePusher.pushMessage(role, new RespMsg("此链接已失效！"));
        return;
    }
    
    public void goBackBorn(Role role, Pos pos) {
        if (role == null) {
            return;
        }
        ReqTelePort reqTelePort = new ReqTelePort();
        reqTelePort.setMapId((int)pos.getMapId());
        reqTelePort.setX((int)pos.getX());
        reqTelePort.setY((int)pos.getY());
        SpringUtils.getMapService().changeMap(role, reqTelePort, null);
        return;
    }
    
    public void getAssistPetList(Role role, Integer serial) {
        SuitParam suit = null;
        RespAssistPetList respAssistPetList = new RespAssistPetList();
        respAssistPetList.setSerial(serial);
        boolean flag = true;
        if ((int)serial == 0) {
            List<SuitParam> suits = role.getExtendBox().getSuits();
            if (suits.size() <= 0) {
                flag = false;
                suits.add(new SuitParam());
            }
            suit = (SuitParam)suits.get(0);
        }
        else {
            List<SuitParam> suits = role.getExtendBox().getSuits();
            if (suits.size() <= 0) {
                flag = false;
                suits.add(new SuitParam());
            }
            suit = (SuitParam)suits.get((int)serial);
        }
        Map<Integer, Pet> petMap = this.petService.selectPet(role);
        List<Integer> petList = null;
        if (suit != null && suit.getPetIds() != null) {
            petList = (List)suit.getPetIds().stream().collect(Collectors.toList());
        }
        else {
            petList = new ArrayList<>();
        }
        List<Integer> otherList = new ArrayList<>();
        if (petMap.size() > 0) {
            for (Map.Entry<Integer, Pet> entry : petMap.entrySet()) {
                Pet pet = (Pet)entry.getValue();
                if (pet.getSkills().size() > 0) {
                    for (PetSkill petSkill : pet.getSkills()) {
                        if (SkillConst.assistSkills.contains(Integer.valueOf(petSkill.getSerial()))) {
                            if (petList.contains(pet.getPetId())) {
                                petList.remove(pet.getPetId());
                                break;
                            }
                            otherList.add(pet.getPetId());
                            break;
                        }
                        else {
                            continue;
                        }
                    }
                }
                else {
                    continue;
                }
            }
        }
        boolean isUpdate = false;
        if (petList.size() > 0) {
            for (Integer petId : petList) {
                suit.getPetIds().remove(petId);
                isUpdate = true;
            }
        }
        if (otherList.size() > 0) {
            if (suit.getPetIds() == null) {
                suit.setPetIds(new ArrayList<>());
            }
            for (Integer petId : otherList) {
                suit.getPetIds().add(petId);
                isUpdate = true;
            }
        }
        if (isUpdate) {
            role.save();
        }
        if (suit == null || suit.getPetIds() == null || suit.getPetIds().size() <= 0) {
            flag = false;
        }
        if (!flag) {
            MessagePusher.pushMessage(role, respAssistPetList);
        }
        respAssistPetList.setPetIds(suit.getPetIds());
        MessagePusher.pushMessage(role, respAssistPetList);
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
        }
        else {
            List<SuitParam> suits = role.getExtendBox().getSuits();
            SuitParam suit = null;
            if ((int)serial == 0) {
                suit = (SuitParam)suits.get(0);
            }
            else {
                Optional<SuitParam> any = suits.stream().filter(obj -> obj.getSerial().equals(serial)).findAny();
                if (any.isPresent()) {
                    suit = (SuitParam)any.get();
                }
            }
            if (suit != null) {
                suit.setPetIds(petIds);
                suit.setName(name);
                respAssistPetList.setSerial(suit.getSerial());
                respAssistPetList.setName(suit.getName());
                respAssistPetList.setPetIds(suit.getPetIds());
            }
            else {
                respAssistPetList = null;
            }
        }
        MessagePusher.pushMessage(role, respAssistPetList);
    }
    
    public boolean mouserClicker(Role role, String type, long mouse) {
        long millis = System.currentTimeMillis();
        long time = (long)role.getTempCache(type, Long.valueOf(0L));
        if (time > 0L && millis - time < mouse) {
            return false;
        }
        if (millis - time <= 10L) {
            Integer warn = Integer.valueOf((int)role.getTempCache("warn", Integer.valueOf(0)) + 1);
            role.pushTempCache("warn", warn);
            MessagePusher.pushMessage(role, new RespMsg("警告，你已经违规操作第#<R>" + warn + "#次，三次后封号处理"));
        }
        role.pushTempCache(type, Long.valueOf(millis));
        return true;
    }
    
    public boolean mouserCartoon(Role role, String type, long mouse) {
        long millis = System.currentTimeMillis();
        long time = (long)role.getTempCache(type, Long.valueOf(0L));
        if (time > 0L && millis - time < mouse) {
            return false;
        }
        role.pushTempCache(type, Long.valueOf(millis));
        return true;
    }
    
    public void kickOut(long id, String msg) {
        IoSession session = SessionUtils.getSessionBy(id);
        if (session != null) {
            MessagePusher.pushMessage(session, new RespMsg(msg));
            session.closeNow();
        }
    }
    
    public boolean isRelationMobile(Role role, String mobile) {
        if (role.getMobile() != null && role.getMobile().length() > 1) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("你已经绑定了"));
            return false;
        }
        String relation = JedisUtil.get(role.getSid() + "_" + mobile);
        if (relation != null) {
            role.setMobile(relation);
            MessagePusher.pushMessage((long)role.getId(), new RespCorrelation(relation));
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("你已经绑定了"));
            return false;
        }
        return true;
    }
    
    public void verificationCode(Role role, String mobile) {
        if (CodeUtils.verificationPhone(mobile) || !CodeUtils.checkPhone(mobile)) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("请正确输入手机号码"));
            return;
        }
        if (!this.isRelationMobile(role, mobile)) {
            return;
        }
        boolean contain = JedisUtil.contain("*_" + mobile);
        if (contain) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("该手机已经被绑定！请改用其号码"));
            return;
        }
        String key = role.getSid() + "_sms";
        String code = JedisUtil.get(key);
        if (code != null) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("验证码已经发送请注意查收"));
            return;
        }
        boolean clicker = this.mouserClicker(role, "sms", 60000L);
        if (!clicker) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("验证码已经发送，请稍后再试"));
            return;
        }
        String verification = String.valueOf((int)((Math.random() * 9.0 + 1.0) * 100000.0));
        JedisUtil.set(key, 3600, String.format("%s_%s", mobile, verification));
        String sendSms = this.sendService.sendSms(role.getSid(), mobile, verification, 60);
        MessagePusher.pushMessage((long)role.getId(), new RespMsg(sendSms));
        return;
    }
    
    public void relationMobile(Role role, String verificationCode) {
        if (role.getMobile() != null && role.getMobile().length() > 1) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("你已经绑定了"));
            return;
        }
        String key = role.getSid() + "_sms";
        String value = JedisUtil.get(key);
        if (value == null) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("验证码已过期，请重新获取"));
            return;
        }
        String[] split = value.split("_");
        if (split.length < 2) {
            return;
        }
        if (!split[1].equals(verificationCode)) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("验证码错误"));
            return;
        }
        String relation = JedisUtil.get(role.getSid() + "_" + split[0]);
        if (relation != null) {
            role.setMobile(relation);
            MessagePusher.pushMessage((long)role.getId(), new RespCorrelation(relation));
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("你已经绑定了"));
            return;
        }
        boolean contain = JedisUtil.contain("*_" + split[0]);
        if (contain) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("该手机已经被绑定！请改用其号码"));
            return;
        }
        role.setMobile(split[0]);
        MessagePusher.pushMessage((long)role.getId(), new RespCorrelation(role.getMobile()));
        JedisUtil.set(role.getSid() + "_" + split[0], role.getMobile());
        MessagePusher.pushMessage((long)role.getId(), new RespMsg("绑定成功"));
        this.bagService.sendReward(role, Deploy.mobile_binding, "手机绑定", 0);
        SpringUtils.getAccountService().updateAccountMobile(role.getSid(), role.getMobile());
        JedisUtil.del(key);
        return;
    }
}
