
package com.kitty.system.game.role.service;

import com.kitty.mina.cache.Deploy;
import com.kitty.common.utils.CodeUtils;
import com.kitty.cache.JedisUtil;
import com.kitty.system.game.fightnew.book.definition.SkillConst;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.pet.domain.vo.SuitParam;
import com.kitty.system.game.pet.message.RespAssistPetList;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.market.message.RespMarketGoodCard;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.player.bean.PkRecord;
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
import com.kitty.system.game.fightnew.utils.MessageAssistUtil;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import com.kitty.system.game.fightnew.messages.ReqFightHandle;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.game.enter.message.ReqExtendStoreSize;
import com.kitty.system.game.general.message.ReqAssignAttrib;
import com.kitty.system.game.equip.domain.PetEquip;
import org.nutz.json.Json;
import com.kitty.system.game.skill.service.SkillService;
import com.kitty.system.game.tianfu.service.RoleDayInCeService;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.vo.AdviceAddPro;
import com.kitty.system.game.general.message.ReqSetRecommendAttrib;
import com.kitty.record.RecordThread;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.listener.event.RoleLevelUpEvent;
import com.kitty.system.game.role.message.RespLevelUp;
import com.kitty.system.game.scene.SceneManager;
import com.kitty.listener.event.ranking.RoleChangeEvent;
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
import java.util.Collection;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.game.team.domain.Team;
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
import java.util.Date;
import java.text.SimpleDateFormat;
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
import com.kitty.system.game.dirtywords.WordDisposePool;
import com.kitty.system.common.message.RespMsg;
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
import com.kitty.listener.BaseGameEvent;
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
import com.kitty.mina.message.Message;
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
import com.kitty.system.game.partner.service.PartnerService;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.partner.domain.Partner;
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
    
    public RoleService() {
        this.logger = LoggerFactory.getLogger(RoleService.class);
    }
    
    @EventHandler({ EventType.LOGOUT })
    public void handleLogoutEvent(final LogoutEvent logoutEvent) {
        final Role role = logoutEvent.getRole();
        HeartbeatFilter.cleanHeartBeatRecord(role.getId());
        role.setUnlock(false);
        role.setPreOnlineSec(this.getOnlineSec(role));
        this.clearBufferData(role.getId());
        role.save();
        this.roleService.removeOnline(role);
        this.roleService.removeOffline(role);
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        HeartbeatFilter.cleanHeartBeatRecord(role.getId());
        this.checkReset(role);
        final HorseService horseService = SpringUtils.getHorseService();
        horseService.refresh(role, horseService.getHorse(role));
        this.equipService.refreshPetEquip(role);
        this.petService.loadPet(role);
        SpringUtils.getBean(EchoService.class).cancelCountdown(role.getId());
    }

    public Integer fixRoleSerial(Integer serial) {
        // 转换问题
        // 红拂女, 沧浪君, 混天魔, 九尾狐, 莫解语, 南冠客
        Integer[] serialIds = new Integer[]{11046, 15041, 13041, 13046, 15046, 14041};
        for (Integer serialId : serialIds) {
            if (serial.equals(serialId)) {
                serial = serial - 10;
            }
        }
        return serial;
    }
    
    public void roleLevelChangeEvent(final Role role) {
        role.setGg(role.getGg() + 1);
        role.setLx(role.getLx() + 1);
        role.setLl(role.getLl() + 1);
        role.setMj(role.getMj() + 1);
        role.setFpoint(role.getFpoint() + 4);
        this.calculateBase(role);
        this.refreshRole(role);
        this.refreshPetClose(role);
        final Map<Integer, Partner> partnerMap = DataCache.PARTNER_MAP.get(role.getId());
        if (partnerMap != null && partnerMap.size() > 0) {
            final PartnerService partnerService = SpringUtils.getBean(PartnerService.class);
            final int partnerLv = 55;
            for (final Map.Entry<Integer, Partner> entry : partnerMap.entrySet()) {
                final Partner partner = entry.getValue();
                if (partner.getPartnerLv() < partnerLv) {
                    if (role.getLevel() >= partnerLv) {
                        partner.setPartnerLv((short)partnerLv);
                    }
                    else {
                        partner.setPartnerLv(role.getLevel().shortValue());
                    }
                    partnerService.partnerLevelChangeEvent(role, partner);
                    partner.save();
                }
            }
        }
    }
    
    public void clearBufferData(final long id) {
        DataCache.ROLE_DAY_IN_CE.remove(id);
        DataCache.ROLE_TIAN_FU.remove(id);
        DataCache.CUTTING_FERRULE_MAP.remove(id);
        DataCache.CARD_EXERCISE_MAP.remove(id);
        BagCache.BAG_GOODS_MAP.remove(id);
        BagCache.BAG_EQUIP_MAP.remove(id);
        BagCache.STORE_GOODS_MAP.remove(id);
        BagCache.STORE_EQUIP_MAP.remove(id);
        DataCache.EQUIP_MAP.remove(id);
        DataCache.EFFECT_MAP.remove(id);
        DataCache.HORSE_MAP.remove(id);
        DataCache.GIFT_RECORD.remove(id);
        DataCache.PARTNER_MAP.remove(id);
        PetCache.PET_MAP.remove(id);
        PetCache.PET_EQUIP_MAP.remove(id);
        DataCache.SETTING_MAP.remove(id);
        DataCache.DOMESTICATE_MAP.remove(id);
        DataCache.HOUSE_MAP.remove(id);
        DataCache.ROLE_EMAIL.remove(id);
        DataCache.WEEK_TREASURE.remove(id);
    }
    
    public void refreshPetClose(final Role role) {
        final Map<Integer, Pet> petMap = this.petService.selectPet(role);
        for (final Integer key : petMap.keySet()) {
            final Pet pet = petMap.get(key);
            this.petService.refreshPetLevelClose(pet);
            this.petService.refreshPetInfo(role, pet);
        }
    }
    
    public boolean checkCondition(final Role role, final List<Item> items, final Map<Integer, Integer> map) {
        if (map != null) {
            for (final Integer key : map.keySet()) {
                final boolean flag = this.checkCurrency(role, key, map.get(key));
                if (!flag) {
                    return false;
                }
            }
        }
        if (items != null) {
            final CheckMapInt checkMapInt = ItemCheck.checkAllItemTypes(items, role);
            if (!checkMapInt.isFlag()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean costCondition(final Role role, final Condition condition, final String reason, final boolean bnotify, final boolean bnotcut) {
        if (condition.getList() != null) {
            for (final Item item : condition.getList()) {
                final Integer goods = this.bagService.consumeBagGoods(role, item.getItem(), item.getNum(), true, true, "costCondition");
                if (goods != null) {
                    return false;
                }
            }
        }
        if (condition.getMap() != null) {
            final Map<Integer, Integer> map = condition.getMap();
            for (final Integer type : map.keySet()) {
                final boolean flag = this.deductCurrency(role, type, map.get(type), "costCondition");
                if (!flag) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void addOnline(final Role role) {
        if (role == null) {
            return;
        }
        DataCache.ONLINE_ROLES.put(role.getId(), role);
        this.redisService.del(role.getId() + "_countdown_" + SpringUtils.getBean(ServerService.class).getMarking());
    }
    
    public void removeOnline(final Role role) {
        if (role == null) {
            return;
        }
        DataCache.ONLINE_ROLES.remove(role.getId());
    }
    
    public void addOffline(final Role role, final boolean flag) {
        if (role == null) {
            return;
        }
        role.setOfLineTime(System.currentTimeMillis());
        DataCache.OFFLINE_ROLES.put(role.getId(), role);
        final String key = "temporary" + role.getId();
        final Boolean exists = this.redisService.exists(key);
        if (exists) {
            this.redisService.expire(key, 54000L);
        }
        this.sendOffline(role, flag);
    }
    
    public void removeOffline(final Role role) {
        if (role == null) {
            return;
        }
        DataCache.OFFLINE_ROLES.remove(role.getId());
        final String key = "temporary" + role.getId();
        final Boolean exists = this.redisService.exists(key);
        if (exists) {
            this.redisService.persist(key);
        }
    }
    
    public boolean isOnline(final int id) {
        return this.isOnline(SpringUtils.getPlayerService().getUidBy(id));
    }
    
    public boolean isOnline(final long id) {
        return DataCache.ONLINE_ROLES.containsKey(id);
    }
    
    public boolean canLogin(final Account account, final IoSession session) {
        final ServerService serverService = SpringUtils.getBean(ServerService.class);
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
    
    public Role createRole(final IoSession session, final String sid, final ReqCreateNewChar reqCreateNewChar) {
        final SysHero hero = PartDataPool.heroMap.get(reqCreateNewChar.getSerial());
        final SysRbAttr data = PartDataPool.rbAttrMap.get(1);
        if (hero == null) {
            return null;
        }
        String name = reqCreateNewChar.getName().trim();
        name = name.replace("#", "");
        if (!this.canCreateRole(session, name, LOGIC.LOGICTYPE_HERO.getValue())) {
            return null;
        }
        name = CheckCharUtils.replaceName(name);
        final Role role = new Role();
        role.setId(IdGenerator.getNextId());
        role.setSid(sid);
        role.setRoleId(this.getRoleId());
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
        role.setSkills(this.createSkillBox(role, reqCreateNewChar.getRace(), reqCreateNewChar.getSex(), data));
        role.setPos(new Pos());
        role.getExtendBox().setQuotaLimit(DataGlobalFunc.setQuota());
        role.getExtendBox().setSetMap(DataGlobalFunc.setting());
        role.getExtendBox().setNewDistrict(DataGlobalFunc.newDistrict());
        role.getRest().setParty(DataGlobalFunc.faction());
        this.calculateBase(role);
        role.setMarking(reqCreateNewChar.getMarking());
        final NutMap map = new NutMap();
        map.setv("a", 0);
        map.setv("b", 0);
        role.setColor(map.toString());
        final long time = System.currentTimeMillis();
        role.setEnterTime(time);
        role.save();
        this.createRoleRests(role);
        SpringUtils.getPlayerService().addPlayerProfile(new PlayerProfile(role));
        this.addOffline(role, false);
        EventDispatcher.getInstance().fireEvent(new CreationRoleEvent(EventType.CREATION_ROLE, role));
        return role;
    }
    
    public void createRoleRests(final Role role) {
        final CardExercise cardExercise = new CardExercise(this.worker.nextId(), role.getId());
        cardExercise.save();
        DataCache.CARD_EXERCISE_MAP.put(role.getId(), cardExercise);
        SpringUtils.getHorseService().creationHorse(role);
        final Setting setting = new Setting(this.worker.nextId(), role.getId());
        setting.save();
        DataCache.SETTING_MAP.put(role.getId(), setting);
        SpringUtils.getBean(HouseService.class).addHouse(role.getId(), 5100);
    }
    
    public void calculateBase(final Role role) {
        final int life = CountUtils.countLife(role);
        role.setCurrLife(life);
        role.setMaxLife(life);
        final int mana = CountUtils.countMana(role);
        role.setCurrMana(mana);
        role.setMaxMana(mana);
    }
    
    public Map<Integer, RoleSkill> createSkillBox(final Role role, final Integer race, final Integer sex, final SysRbAttr data) {
        final Map<Integer, RoleSkill> skills = new HashMap<Integer, RoleSkill>();
        final List<Integer> list = DataGlobalFunc.GetPlayerSkillId(race, sex);
        for (final Integer integer : list) {
            final Map<Integer, SysSkill> skillMap = DataGlobalFunc.getSkillListByAttr(integer);
            for (final Integer key : skillMap.keySet()) {
                final SysSkill sysSkill = skillMap.get(key);
                final int max_proficient = data.getMaxSkillExp();
                List<FieldByte> effects = null;
                if (role.getSkills().size() > 0) {
                    for (final RoleSkill value : role.getSkills().values()) {
                        final SysSkill skill = SkillDataPool.skillMap.get(value.getSkillId());
                        if (skill.getPos().equals(sysSkill.getPos()) && value.getEffects().size() > 0) {
                            effects = new ArrayList<FieldByte>();
                            for (final FieldByte fieldByte : value.getEffects()) {
                                final int type = fieldByte.getK() % 10;
                                effects.add(new FieldByte(sysSkill.getIcon() * 10 + type, fieldByte.getV()));
                            }
                        }
                    }
                }
                final RoleSkill skill2 = new RoleSkill(sysSkill.getSerial(), integer, data.getInitSkillExp(), max_proficient);
                if (effects != null) {
                    skill2.setEffects(effects);
                }
                skills.put(sysSkill.getSerial(), skill2);
            }
        }
        return skills;
    }
    
    public boolean canCreateRole(final IoSession session, String name, final int logic) {
        String type = "角色";
        if (LOGIC.LOGICTYPE_PET.getValue() == logic) {
            type = "宠物";
        }
        else if (LOGIC.HORSE.getValue() == logic) {
            type = "坐骑";
        }
        if (Strings.isEmpty((CharSequence)name)) {
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
        final List<String> set = WordDisposePool.checkDirtyWords(name);
        if (set.size() >= 1) {
            MessagePusher.pushMessage(session, new RespMsg(String.format("%s名存在非法字符。", type)));
            return false;
        }
        if (CheckCharUtils.checkQQNumber(name)) {
            MessagePusher.pushMessage(session, new RespMsg(String.format("%s名存在非法字符。", type)));
            return false;
        }
        final Matcher emojiTitle = CheckCharUtils.CHECK_EMOJI.matcher(name);
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
    
    public void enterMap(final IoSession session, final Role role) throws Exception {
        final List<PlayerProfile> playerProfiles = SpringUtils.getPlayerService().getPlayersBy(role.getSid());
        for (final PlayerProfile playerProfile : playerProfiles) {
            final Role onlineRole = this.getOnlinePlayer(playerProfile.getId());
            if (onlineRole != null) {
                final IoSession ioSession = SessionUtils.getSessionBy(onlineRole.getId());
                if (ioSession == null || ioSession.getId() == session.getId()) {
                    continue;
                }
                final RespOtherLogin respOtherLogin = new RespOtherLogin();
                respOtherLogin.setResult(1);
                MessagePusher.pushMessage(ioSession, respOtherLogin);
                Thread.sleep(1000L);
                ioSession.write((Object)new RespKickClient("你的账号已在其它设备登录。")).addListener((IoFutureListener)new IoFutureListener<IoFuture>() {
                    public void operationComplete(final IoFuture future) {
                        ioSession.closeNow();
                    }
                });
            }
        }
        SessionManager.INSTANCE.registerSession(role.getRoleId(), session);
        this.addOnline(role);
        this.removeOffline(role);
        session.setAttribute((Object)SessionProperties.CLIENT_STATUS, (Object)"1");
        session.setAttribute((Object)SessionProperties.PLAYER_ID, (Object)role.getRoleId());
        session.setAttribute((Object)SessionProperties.PLAYER_UID, (Object)role.getId());
        session.setAttribute((Object)SessionProperties.PLAYER_NAME, (Object)role.getName());
        role.setDistributeKey(SessionManager.INSTANCE.getNextDistributeKey());
        role.setState(1);
        final RespRoleAlreadyLogin respRoleAlreadyLogin = new RespRoleAlreadyLogin();
        respRoleAlreadyLogin.setRoleName(role.getName());
        MessagePusher.pushMessage(role, respRoleAlreadyLogin);
        final RespReplyServerTime respReplyServerTime = new RespReplyServerTime();
        respReplyServerTime.setServerTime(String.valueOf(TimeUtil.timeInMillis()));
        MessagePusher.pushMessage(role, respReplyServerTime);
        if (role.getLockTime() > System.currentTimeMillis()) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final Date date = new Date(role.getLockTime());
            final String tips = simpleDateFormat.format(date);
            final RespOtherLogin respOtherLogin2 = new RespOtherLogin();
            respOtherLogin2.setContent("您违规游戏行为，角色已被封停。\n解封时间：" + tips);
            respOtherLogin2.setResult(2);
            MessagePusher.pushMessage(role, respOtherLogin2);
            return;
        }
        this.sendPacket(role);
        MessagePusher.pushMessage(role, SafeUtil.getLockStatus(role));
        SpringUtils.getBean(PkService.class).loginCheckFromPrison(role);
        EventDispatcher.getInstance().fireEvent(new EnterMapEvent(EventType.ENTER_MAP, role));
        EventDispatcher.getInstance().fireEvent(new LoginEvent(EventType.LOGIN, role));
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            final int teamCount = this.teamService.getTeamCount(team);
            if (teamCount == 0) {
                SessionCache.ROLE_TEAM.remove(role.getRoleId());
            }
            else {
                this.teamService.updateTeamList(role);
                this.teamService.liveLoadTeamList(role);
            }
        }
        SpringUtils.getBean(EnterService.class).loadHmlNotify(role);
        if (role.getMobile() != null && role.getMobile().length() > 1) {
            MessagePusher.pushMessage(role.getId(), new RespCorrelation(role.getMobile()));
        }
        this.cardService.reqFiveExercise(role, false);
        this.stateRefresh(role);
        this.titleService.refreshTitle(role);
        this.refreshRole(role);
        this.sendExtraAttribute(role);
        this.refreshRoleCurrency(role);
        final Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
        for (final Long key : bagGoodsMap.keySet()) {
            final BagGoods goods = bagGoodsMap.get(key);
            this.bagService.refreshRoleGoods(role, goods, false);
        }
        final Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
        for (final Long key2 : bagEquipMap.keySet()) {
            final BagEquip bagEquip = bagEquipMap.get(key2);
            this.bagService.refreshRoleEquip(role, bagEquip, false);
        }
        final Map<Long, CardChange> card = this.cardService.getCard(role);
        for (final Long key3 : card.keySet()) {
            this.cardService.refresh(role, card.get(key3));
        }
        final Map<Long, StoreGoods> storeGoodsMap = this.storeService.getStoreGoods(role);
        for (final StoreGoods good : storeGoodsMap.values()) {
            this.storeService.refreshStoreGoods(role, good);
        }
        final Iterator<StoreEquip> itStoreEquips = this.storeService.getStoreEquip(role).values().iterator();
        while (itStoreEquips.hasNext()) {
            final StoreEquip equip = itStoreEquips.next();
            if (equip.getType() > 1) {
                this.storeService.refreshStoreEquip(role, equip);
            }
            else {
                this.logger.error("加载仓库装备异常数据 = {}", (Object)equip);
                itStoreEquips.remove();
            }
        }
        for (final RoleTianFu roleTianFu : DataCache.ROLE_TIAN_FU.get(role.getId())) {
            if (roleTianFu == null) {
                continue;
            }
            if (roleTianFu.getStorePos() <= 0) {
                continue;
            }
            this.storeService.refreshStoreTianFu(role, roleTianFu);
        }
        role.setEnterTime(System.currentTimeMillis());
        role.setOfLineTime(0L);
        role.setOnlineLoginTime(System.currentTimeMillis());
        role.setPrevHeartTime(0L);
        role.save();
        this.refreshInstruct(role);
        SessionManager.INSTANCE.removeFromAnonymous(session);
        final RespGeneralNotify respGeneralNotify = new RespGeneralNotify();
        respGeneralNotify.setNotify(39);
        respGeneralNotify.setValue("1");
        MessagePusher.pushMessage(role, respGeneralNotify);
    }
    
    public void refreshParty(final Role role) {
        this.titleService.refreshTitle(role);
        final RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(role.getRoleId());
        final List<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("faction", (byte)3, role.getFaction()));
        list.add(new FiedValue("factionLevel", (byte)3, this.getFactionLevel(role)));
        list.add(new FiedValue("factionName", (byte)4, this.getFactionName(role)));
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
    }
    
    public void refreshRole(final Role role) {
        this.refreshRoleStore(role);
        final RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(role.getRoleId());
        final List<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("life", (byte)3, CountUtils.countLife(role)));
        list.add(new FiedValue("mana", (byte)3, CountUtils.countMana(role)));
        list.add(new FiedValue("curr_life", (byte)3, role.getCurrLife()));
        list.add(new FiedValue("curr_mana", (byte)3, role.getCurrMana()));
        list.add(new FiedValue("buyPetGrid", (byte)3, role.getExtendBox().getBuyPetGrid()));
        list.add(new FiedValue("buyBagGrid", (byte)3, role.getExtendBox().getPosition()));
        list.add(new FiedValue("buyStoreGrid", (byte)3, role.getExtendBox().getStorePosition()));
        list.add(new FiedValue("buyPetEquipGrid", (byte)3, role.getExtendBox().getPetEquipPosition()));
        list.add(new FiedValue("type", (byte)2, role.getSerial().shortValue()));
        list.add(new FiedValue("level", (byte)2, role.getLevel().shortValue()));
        list.add(new FiedValue("relive", (byte)1, role.getRelive().byteValue()));
        list.add(new FiedValue("exp", (byte)4, String.valueOf(role.getExp())));
        list.add(new FiedValue("nextExp", (byte)4, String.valueOf(FormulaHero.formulaHeroLvUpExp(role.getLevel(), role.getLevel() + 1, role.getRelive()))));
        final List<FiedValue> deyList = this.deyList(role.getColor());
        if (deyList.size() > 0) {
            list.addAll(deyList);
        }
        list.add(new FiedValue("gg", (byte)2, role.getGg().shortValue()));
        list.add(new FiedValue("lx", (byte)2, role.getLx().shortValue()));
        list.add(new FiedValue("ll", (byte)2, role.getLl().shortValue()));
        list.add(new FiedValue("mj", (byte)2, role.getMj().shortValue()));
        list.add(new FiedValue("fpoint", (byte)2, role.getFpoint().shortValue()));
        list.add(new FiedValue("faction", (byte)3, role.getFaction()));
        list.add(new FiedValue("factionLevel", (byte)3, this.getFactionLevel(role)));
        list.add(new FiedValue("factionName", (byte)4, this.getFactionName(role)));
        list.add(new FiedValue("autoFight", (byte)1, role.getExtendBox().getAutoFight()));
        list.add(new FiedValue("title", (byte)3, role.getCurrTitle()));
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
    }
    
    public String getFactionName(final Role role) {
        String name = "";
        if (role.getFaction() > 0) {
            final Party party = SpringUtils.getPartyService().getParty(role.getFaction());
            if (party != null) {
                name = party.getName();
            }
        }
        return name;
    }
    
    public byte getFactionLevel(final Role role) {
        byte level = 0;
        if (role.getFaction() > 0) {
            final Party party = SpringUtils.getPartyService().getParty(role.getFaction());
            if (party != null) {
                level = party.getLevel();
            }
        }
        return level;
    }
    
    public void refreshRoleInfo(final Role role, final boolean flag) {
        final RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(role.getRoleId());
        final List<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("life", (byte)3, CountUtils.countLife(role)));
        list.add(new FiedValue("mana", (byte)3, CountUtils.countMana(role)));
        list.add(new FiedValue("curr_life", (byte)3, role.getCurrLife()));
        list.add(new FiedValue("curr_mana", (byte)3, role.getCurrMana()));
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
        if (flag) {
            this.refreshRoleStore(role);
        }
    }
    
    public void refreshRoleStore(final Role role) {
        MessagePusher.pushMessage(role, new RespRefreshStore(role.getSaveLife(), role.getSaveMana()));
    }
    
    public List<FiedValue> deyList(final String color) {
        final List<FiedValue> list = new ArrayList<FiedValue>();
        final Gson gson = new Gson();
        final NutMap nutMap = gson.fromJson(color, NutMap.class);
        if (nutMap != null) {
            final int a = nutMap.getInt("a");
            if (a > 0) {
                list.add(new FiedValue("a", (byte)3, a));
            }
            final int b = nutMap.getInt("b");
            if (b > 0) {
                list.add(new FiedValue("b", (byte)3, b));
            }
        }
        return list;
    }
    
    public void sendExtraAttribute(final Role role) {
        List<FiedValue> list = new ArrayList<FiedValue>();
        list = this.correction(role, list);
        list = this.faction(role, list);
        MessagePusher.pushMessage(role, new RespUpdateImprovement(role.getRoleId(), list));
    }
    
    public void stateRefresh(final Role role) {
        final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        final List<FiedValue> list = new ArrayList<FiedValue>();
        if (role.getMasquerade() > 0 && role.getCardExpireTime() > currTime) {
            final NutMap nutMap = new NutMap();
            nutMap.setv("serial", (Object)role.getMasquerade());
            nutMap.setv("expire", (Object)role.getCardExpireTime());
            list.add(new FiedValue("card", (byte)4, JsonUtils.map2String((Map<?, ?>)nutMap)));
        }
        final PkInfoBox box = role.getPkInfoBox();
        if (box.getIncense() > 0 && box.getIncense() > currTime) {
            list.add(new FiedValue("7", (byte)3, box.getIncense()));
        }
        if (box.isRedName()) {
            if (box.getRedTime() > 0 && box.getRedTime() > currTime) {
                list.add(new FiedValue("8", (byte)3, box.getRedTime()));
            }
            if (box.getPrisonTime() > 0 && box.getPrisonTime() > currTime) {
                list.add(new FiedValue("9", (byte)3, box.getPrisonTime()));
            }
            if (box.getConceal() > 0 && box.getConceal() > currTime) {
                list.add(new FiedValue("101", (byte)3, box.getConceal()));
            }
        }
        final int moveAdditionTime = role.getExtendBox().getState().getInt("shenxing", 0);
        if (moveAdditionTime > 0 && moveAdditionTime > currTime) {
            list.add(new FiedValue("109", (byte)3, moveAdditionTime));
        }
        MessagePusher.pushMessage(role, new RespStateRefresh(list));
    }
    
    public List<FiedValue> correction(final Role role, final List<FiedValue> list) {
        for (final Integer rei : role.getRest().getCorrection().keySet()) {
            list.add(new FiedValue("rei_" + rei, (byte)3, role.getRest().getCorrection().get(rei)));
        }
        return list;
    }
    
    public List<FiedValue> faction(final Role role, final List<FiedValue> list) {
        if (role.getRest().getParty().size() > 0) {
            final Map<Integer, Integer> party = role.getRest().getParty();
            for (final Integer pro : party.keySet()) {
                final Integer value = party.get(pro);
                if (value != null) {
                    list.add(new FiedValue(String.valueOf(pro), (byte)3, value));
                }
            }
            return list;
        }
        return list;
    }
    
    public void refreshRoleCurrency(final Role role) {
        MessagePusher.pushMessage(role, new RespCurrencyInfo(role));
    }
    
    public void refreshInstruct(final Role role) {
        final List<Instruct> list = new ArrayList<Instruct>();
        final Setting setting = SpringUtils.getBean(SettingService.class).getSetting(role);
        final Map<Short, Map<Byte, String>> map = setting.getSpecial();
        for (final Short serial : map.keySet()) {
            final Map<Byte, String> instructMap = map.get(serial);
            for (final Byte index : instructMap.keySet()) {
                list.add(new Instruct(serial, index, instructMap.get(index)));
            }
        }
        if (list.size() > 0) {
            MessagePusher.pushMessage(role, new RespInstruct(list));
        }
    }
    
    private void sendPacket(final Role role) {
        final String key = "temporary" + role.getId();
        final Overflow overflow = this.redisService.get(key, Overflow.class);
        if (overflow != null) {
            this.bagService.lodeTemporary(role, overflow.getList());
        }
    }
    
    public boolean addExp(final Role role, long exp, final String reason, final boolean is) {
        if (exp == 0L) {
            return true;
        }
        final ServerService serverService = SpringUtils.getBean(ServerService.class);
        if (serverService.getExpTimesLevel() != 0 && serverService.getExpTimes() != 0 && role.getRelive() * 1000 + role.getLevel() > serverService.getExpTimesLevel()) {
            exp *= serverService.getExpTimes();
        }
        if (exp < 0L) {
            final long tips_exp = exp = Math.abs(exp);
            long n;
            if (role.getExp() - exp <= 0L) {
                n = 0L;
            }
            else {
                n = role.getExp() - exp;
            }
            exp = n;
            role.setExp(exp);
            final RespUpdate respUpdate = new RespUpdate();
            final List<FiedValue> list = new ArrayList<FiedValue>();
            list.add(new FiedValue("exp", (byte)4, String.valueOf(role.getExp())));
            respUpdate.setId(role.getRoleId());
            respUpdate.setList(list);
            MessagePusher.pushMessage(role, respUpdate);
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
        int level = role.getLevel();
        final SysRbAttr sysRbattr = PartDataPool.rbAttrMap.get(role.getRelive() + 1);
        if (sysRbattr == null) {
            return false;
        }
        final Integer lvLimit = sysRbattr.getLvLimit();
        if (role.getLevel() >= lvLimit && role.getExp() >= DataGlobalFunc.storageExp(role.getRelive() * 1000 + role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue())) {
            final String msg = String.format("角色已到达%d转的上限%d级", role.getRelive(), lvLimit);
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, true));
            return false;
        }
        final String exp_tips = "#<G>" + exp + "#点经验";
        long storageExp = DataGlobalFunc.storageExp(role.getRelive() * 1000 + role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue());
        long n2;
        if (role.getExp() + exp > storageExp) {
            n2 = storageExp;
        }
        else {
            n2 = role.getExp() + exp;
        }
        long totalExp = n2;
        long needExp = FormulaHero.formulaHeroLvUpExp(role.getLevel(), role.getLevel() + 1, role.getRelive());
        if (totalExp >= needExp && role.getLevel() < lvLimit) {
            while (totalExp >= needExp) {
                totalExp -= needExp;
                ++level;
                role.setLevel(level);
                role.setExp(totalExp);
                this.roleLevelChangeEvent(role);
                if (level >= lvLimit) {
                    storageExp = DataGlobalFunc.storageExp(role.getRelive() * 1000 + role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue());
                    long n3;
                    if (totalExp > storageExp) {
                        n3 = storageExp;
                    }
                    else {
                        n3 = totalExp;
                    }
                    totalExp = n3;
                    role.setExp(totalExp);
                    break;
                }
                needExp = FormulaHero.formulaHeroLvUpExp(role.getLevel(), role.getLevel() + 1, role.getRelive());
            }
            SceneManager.INSTANCE.sendMessages(role, new RespLevelUp(role.getRoleId(), role.getLevel()));
            EventDispatcher.getInstance().fireEvent(new RoleLevelUpEvent(EventType.ROLE_LEVEL_UP, role, role.getRelive() * 1000 + (level - 1), role.getRelive() * 1000 + level));
            role.save();
        }
        else {
            role.setExp(totalExp);
            final RespUpdate respUpdate2 = new RespUpdate();
            final List<FiedValue> list2 = new ArrayList<FiedValue>();
            list2.add(new FiedValue("exp", (byte)4, String.valueOf(role.getExp())));
            respUpdate2.setList(list2);
            respUpdate2.setId(role.getRoleId());
            MessagePusher.pushMessage(role, respUpdate2);
            role.save();
        }
        if (is) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "你获得了#<IR4>#" + exp_tips, true));
        }
        EventDispatcher.getInstance().fireEvent(new RoleChangeEvent(EventType.ROLE_EXP_CHANGE, role));
        return true;
    }
    
    public boolean addCurrency(final Role role, final Integer type, Long num, String cause, final boolean isTips, final boolean joint) {
        if (num == 0L) {
            return false;
        }
        if (num < 0L) {
            this.logger.error("增加货币负数，角色 = {}, 方法 = {}，货币类型 = {}，数量 = {}", new Object[] { role.getName(), cause, type, num });
            return false;
        }
        final Long add_to = num;
        boolean flag = false;
        String msg = "";
        if (type.equals(Const.RESTYPE_GOLD)) {
            num += role.getGold();
            role.setGold(num);
            msg = "#<IR" + Const.RESTYPE_GOLD + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_SILVER)) {
            num += role.getSilver();
            role.setSilver(num);
            msg = "#<IR" + Const.RESTYPE_SILVER + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_COIN)) {
            num += role.getCoin();
            role.setCoin(num);
            msg = "#<IR" + Const.RESTYPE_COIN + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_GUO)) {
            num += role.getExtendBox().getGuoScore();
            role.getExtendBox().setGuoScore(num);
            msg = "#<IR" + Const.RESTYPE_GUO + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_BRAC)) {
            num += role.getExtendBox().getBracScore();
            role.getExtendBox().setBracScore(num);
            msg = "#<IR" + Const.RESTYPE_BRAC + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_NAO)) {
            num += role.getExtendBox().getNaoScore();
            role.getExtendBox().setNaoScore(num);
            msg = "#<IR" + Const.RESTYPE_NAO + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_DI)) {
            num += role.getExtendBox().getDiScore();
            role.getExtendBox().setDiScore(num);
            msg = "#<IR" + Const.RESTYPE_DI + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_ARENA)) {
            num += role.getExtendBox().getArenaScore();
            role.getExtendBox().setArenaScore(num);
            msg = "#<IR" + Const.RESTYPE_ARENA + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_LONG)) {
            num += role.getExtendBox().getLongScore();
            role.getExtendBox().setLongScore(num);
            msg = "#<IR" + Const.RESTYPE_LONG + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_NICE)) {
            num += role.getExtendBox().getNiceScore();
            role.getExtendBox().setNiceScore(num);
            msg = "#<IR" + Const.RESTYPE_NICE + ">#";
            flag = true;
        }
        else if (type.equals(Const.RESTYPE_TRIBUTE)) {
            num += role.getExtendBox().getHelpScore();
            role.getExtendBox().setHelpScore(num);
            msg = "#<IR" + Const.RESTYPE_TRIBUTE + ">#";
            flag = true;
        }
        else if (type.equals(Const.RES_TYPE_FEATS)) {
            num += role.getExtendBox().getFeatsScore();
            role.getExtendBox().setFeatsScore(num);
            msg = "#<IR" + Const.RES_TYPE_FEATS + ">#";
            flag = true;
        }
        else if (type.equals(Const.RES_TYPE_CHARGE_SCORE)) {
            num += role.getExtendBox().getChargeScore();
            role.getExtendBox().setChargeScore(num);
            msg = "#<IR" + Const.RES_TYPE_CHARGE_SCORE + ">#";
            flag = true;
        }
        else if (type.equals(Const.RES_TYPE_BOUND_GOLD)) {
            num += role.getExtendBox().getBoundGold();
            role.getExtendBox().setBoundGold(num);
            msg = "#<IR" + Const.RES_TYPE_BOUND_GOLD + ">#";
            flag = true;
        }
        else if (type.equals(Const.RES_TYPE_ACHIEVEMENT)) {
            num += role.getExtendBox().getAchievementScore();
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
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)3, (byte)1, String.format("货币类型：%d，增加数量%d ,增加后数量：%d，操作原因：%s", type, add_to, num, cause_), role.getMarking());
        }
        return flag;
    }
    
    public void commendAttrib(final Role role, final ReqSetRecommendAttrib notify) {
        String msg = "自定义";
        if (role.getRoleId().equals(notify.getId())) {
            final Map<String, Integer> map = role.getExtendBox().getAutoAttrib();
            map.put("gg", notify.getGg());
            map.put("lx", notify.getLx());
            map.put("ll", notify.getLl());
            map.put("mj", notify.getMj());
            map.put("type", notify.getType());
            if (notify.getType() <= 2) {
                final SysHero hero = PartDataPool.heroMap.get(role.getSerial());
                if (hero != null) {
                    final AdviceAddPro adviceAddPro = hero.getAdviceAddPro().get(notify.getType() - 1);
                    msg = adviceAddPro.getTitle();
                }
            }
        }
        else {
            final Pet pet = this.petService.selectPet(role).get(notify.getId());
            if (pet != null) {
                final Map<String, Integer> map2 = pet.getAutoAttrib();
                map2.put("gg", notify.getGg());
                map2.put("lx", notify.getLx());
                map2.put("ll", notify.getLl());
                map2.put("mj", notify.getMj());
                map2.put("type", notify.getType());
                pet.setAutoAttrib(map2);
                if (notify.getType() <= 2) {
                    final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
                    if (sysPet != null) {
                        final AdviceAddPro adviceAddPro2 = sysPet.getAdviceAddPro().get(notify.getType() - 1);
                        msg = adviceAddPro2.getTitle();
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
    
    public void changeRace(final Role role, Integer serial) {
        final Map<Integer, SysSkill> data_skill = SkillDataPool.skillMap;
        final Map<Integer, Integer> skillProficient = this.changeSkillProficient(role.getSkills(), data_skill);
        if (skillProficient.size() < 6) {
            MessagePusher.pushMessage(role, new RespHint(13, (short)0));
            return;
        }
        serial = fixRoleSerial(serial);
        final SysHero hero = PartDataPool.heroMap.get(serial);
        if (hero != null) {
            final RoleDayInCeService ceService = SpringUtils.getBean(RoleDayInCeService.class);
            if (ceService.checkDemount(role, hero.getRace()) > 0) {
                MessagePusher.pushMessage(role.getId(), new RespMsg("天策符背包格子不足，无法转换种族"));
                return;
            }
            if (!this.deductCurrency(role, Const.RESTYPE_GOLD, 300L, "转换种族")) {
                return;
            }
            this.resetRole(role, skillProficient, hero, data_skill, ceService, com.kitty.listener.event.RoleChangeEvent.RACE);
        }
    }
    
    public void resetRole(final Role role, final Map<Integer, Integer> skillProficient, final SysHero hero, final Map<Integer, SysSkill> data_skill, final RoleDayInCeService ceService, final int type) {
        final SysRbAttr sysRbAttr = PartDataPool.rbAttrMap.get(role.getRelive() + 1);
        final int count = FormulaHero.restorePorCount(role.getRelive(), role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue());
        role.setGg(role.getLevel());
        role.setLx(role.getLevel());
        role.setLl(role.getLevel());
        role.setMj(role.getLevel());
        role.setFpoint(count);
        role.setRace(hero.getRace());
        role.setSex(hero.getGender());
        role.setIcon(hero.getShape());
        role.getTask().setIcon(hero.getShape());
        role.setSerial(hero.getSerial());
        this.calculateBase(role);
        this.refreshRole(role);
        MessagePusher.pushMessage(role, new RespHint(13, (short)1));
        SpringUtils.getHorseService().convertMount(role);
        this.broadcastService.sendUpdateAppear(role);
        role.setSkills(this.createSkillBox(role, role.getRace(), role.getSex(), sysRbAttr));
        for (final Integer key : role.getSkills().keySet()) {
            final RoleSkill skill = role.getSkills().get(key);
            if (skill.getSkillId().equals(30041)) {
                continue;
            }
            final SysSkill sysSkill = data_skill.get(skill.getSkillId());
            final Integer proficient = skillProficient.get(sysSkill.getPos());
            skill.setProficient(proficient);
            skill.setMaxProficient(sysRbAttr.getMaxSkillExp());
        }
        SpringUtils.getBean(SkillService.class).loadRoleSkill(role);
        this.equipService.checkEquipValid(role);
        role.getExtendBox().setAutoFightSet(new HashMap<String, Object>());
        ceService.chageRace(role);
        role.save();
        EventDispatcher.getInstance().fireEvent(new com.kitty.listener.event.RoleChangeEvent(EventType.ROLE_CHANGE, role, null, null, type));
    }
    
    public void changeRelive(final Role role, Integer serial) {
        serial = fixRoleSerial(serial);
        final SysHero hero = PartDataPool.heroMap.get(serial);
        if (hero != null) {
            if (role.getRelive() < 4) {
                for (final Integer key : role.getSkills().keySet()) {
                    final RoleSkill skill = role.getSkills().get(key);
                    if (skill.getSkillId().equals(30041)) {
                        continue;
                    }
                    if (skill.getProficient() < skill.getMaxProficient()) {
                        MessagePusher.pushMessage(role, new RespMsg("法术#<Y>熟练#未满"));
                        return;
                    }
                }
                final RoleDayInCeService ceService = SpringUtils.getBean(RoleDayInCeService.class);
                if (ceService.checkDemount(role, hero.getRace()) > 0) {
                    MessagePusher.pushMessage(role.getId(), new RespMsg("天策符背包格子不足，无法转生"));
                    return;
                }
                if (!this.deductCurrency(role, Const.RESTYPE_GOLD, 10L, "角色转生")) {
                    return;
                }
                final SysRbAttr sysRbAttr = PartDataPool.rbAttrMap.get(role.getRelive() + 2);
                final int count = FormulaHero.restorePorCount(role.getRelive() + 1, sysRbAttr.getInitLv(), LOGIC.LOGICTYPE_HERO.getValue());
                role.setGg(sysRbAttr.getInitLv());
                role.setLx(sysRbAttr.getInitLv());
                role.setLl(sysRbAttr.getInitLv());
                role.setMj(sysRbAttr.getInitLv());
                role.setFpoint(count);
                role.setRace(hero.getRace());
                role.setSex(hero.getGender());
                role.setIcon(hero.getShape());
                role.getTask().setIcon(hero.getShape());
                role.setSerial(hero.getSerial());
                role.setRelive(role.getRelive() + 1);
                role.setLevel(sysRbAttr.getInitLv());
                MessagePusher.pushMessage(role, new RespHint(8, (short)1));
                SpringUtils.getHorseService().convertMount(role);
                SpringUtils.getBroadcastService().sendUpdateAppear(role);
                role.setSkills(this.createSkillBox(role, role.getRace(), role.getSex(), sysRbAttr));
                for (final Integer key2 : role.getSkills().keySet()) {
                    final RoleSkill skill2 = role.getSkills().get(key2);
                    if (skill2.getSkillId().equals(30041)) {
                        continue;
                    }
                    skill2.setProficient(sysRbAttr.getInitSkillExp());
                    skill2.setMaxProficient(sysRbAttr.getMaxSkillExp());
                }
                SpringUtils.getBean(SkillService.class).loadRoleSkill(role);
                this.equipService.checkEquipValid(role);
                final Integer reborn = DataGlobalFunc.getRoleReborn(role.getRelive(), role.getRace(), role.getSex());
                role.getRest().getCorrection().put(role.getRelive(), reborn);
                this.calculateBase(role);
                this.refreshRole(role);
                this.sendExtraAttribute(role);
                role.getExtendBox().setAutoFightSet(new HashMap<>());
                ceService.chageRace(role);
                role.save();
                EventDispatcher.getInstance().fireEvent(new com.kitty.listener.event.RoleChangeEvent(EventType.ROLE_CHANGE, role, null, null, com.kitty.listener.event.RoleChangeEvent.REI));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("转生已上限"));
            }
        }
    }
    
    public Map<Integer, Integer> changeSkillProficient(final Map<Integer, RoleSkill> map, final Map<Integer, SysSkill> data_skill) {
        final Map<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        for (final Integer key : map.keySet()) {
            final RoleSkill roleSkill = map.get(key);
            final SysSkill sysSkill = data_skill.get(roleSkill.getSkillId());
            if (sysSkill.getPos() > 0) {
                hashMap.put(sysSkill.getPos(), roleSkill.getProficient());
            }
        }
        return hashMap;
    }
    
    public void reliveCorrection(final Role role, final String string) {
        if (role.getRelive() >= 0) {
            if (this.bagService.consumeBagGoods(role, 90008, 1, false, true, "转生修正") != null) {
                MessagePusher.pushMessage(role, new RespMsg("您背包中没有回梦丹"));
                return;
            }
            final NutMap map = (NutMap)Json.fromJson(NutMap.class, (CharSequence)string);
            final Map<Integer, Integer> correction = new HashMap<Integer, Integer>();
            for (final String key : map.keySet()) {
                final int rei = Integer.valueOf(key);
                if (rei <= role.getRelive()) {
                    correction.put(rei, map.getInt(key));
                }
            }
            role.getRest().setCorrection(correction);
            this.calculateBase(role);
            this.refreshRole(role);
            MessagePusher.pushMessage(role, new RespUpdateImprovement(role.getRoleId(), this.correction(role, new ArrayList<FiedValue>())));
            MessagePusher.pushMessage(role, new RespHint(10, (short)1));
            MessagePusher.pushMessage(role, new RespMsg("修改转生修正成功"));
            role.save();
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("请先进行转生"));
        }
    }
    
    public void recover(final Role role, final byte type) {
        String msg = null;
        if (type == 0) {
            final Pet pet = this.petService.selectPet(role).get(role.getExtendBox().getCombatPet());
            if (pet != null) {
                if (pet.getCurrLife() >= pet.getMaxLife() && pet.getCurrMana() >= pet.getMaxMana()) {
                    msg = "生命值和法力值已满，不需要补充。";
                }
                else {
                    pet.setCurrLife(pet.getMaxLife());
                    pet.setCurrMana(pet.getMaxMana());
                    final RespUpdate respUpdate = new RespUpdate();
                    final List<FiedValue> list = new ArrayList<FiedValue>();
                    respUpdate.setId(pet.getPetId());
                    final List<PetEquip> equips = this.petService.getPetEffective(role.getId(), pet);
                    list.add(new FiedValue("life", (byte)3, CountUtils.countPetLife(pet, equips)));
                    list.add(new FiedValue("mana", (byte)3, CountUtils.countPetMana(pet, equips)));
                    list.add(new FiedValue("curr_life", (byte)3, pet.getCurrLife()));
                    list.add(new FiedValue("curr_mana", (byte)3, pet.getCurrMana()));
                    respUpdate.setList(list);
                    MessagePusher.pushMessage(role, respUpdate);
                }
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("当前没有出战宠物"));
            }
        }
        else if (role.getCurrLife() >= role.getMaxLife() && role.getCurrMana() >= role.getMaxMana()) {
            msg = "生命值和法力值已满，不需要补充。";
        }
        else {
            role.setCurrLife(role.getMaxLife());
            role.setCurrMana(role.getMaxMana());
            final RespUpdate respUpdate2 = new RespUpdate();
            final List<FiedValue> list2 = new ArrayList<FiedValue>();
            respUpdate2.setId(role.getRoleId());
            list2.add(new FiedValue("life", (byte)3, CountUtils.countLife(role)));
            list2.add(new FiedValue("mana", (byte)3, CountUtils.countMana(role)));
            list2.add(new FiedValue("curr_life", (byte)3, role.getCurrLife()));
            list2.add(new FiedValue("curr_mana", (byte)3, role.getCurrMana()));
            respUpdate2.setList(list2);
            MessagePusher.pushMessage(role, respUpdate2);
        }
        if (msg != null) {
            MessagePusher.pushMessage(role, new RespMsg(msg));
        }
    }
    
    public void assignAttrib(final Role role, final ReqAssignAttrib assignAttrib) {
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
        final int count = assignAttrib.getGg() + assignAttrib.getLx() + assignAttrib.getLl() + assignAttrib.getMj();
        if (role.getRoleId().equals(assignAttrib.getId())) {
            if (role.getFpoint() < count) {
                return;
            }
            final Integer restorePorCount = FormulaHero.restorePorCount(role.getRelive(), role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue());
            if (count > role.getFpoint() || role.getGg() - role.getLevel() + role.getMj() - role.getLevel() + role.getLl() - role.getLevel() + role.getLx() - role.getLevel() + count > restorePorCount) {
                msg = "加点失败";
                this.logger.error("角色:[{}],Id:[{}],加点超出:[{}],未分配点:[{}]", role.getName(), role.getRoleId(), count, role.getFpoint());
            }
            else {
                if (assignAttrib.getGg() > 0) {
                    role.setGg(role.getGg() + assignAttrib.getGg());
                }
                if (assignAttrib.getLx() > 0) {
                    role.setLx(role.getLx() + assignAttrib.getLx());
                }
                if (assignAttrib.getLl() > 0) {
                    role.setLl(role.getLl() + assignAttrib.getLl());
                }
                if (assignAttrib.getMj() > 0) {
                    role.setMj(role.getMj() + assignAttrib.getMj());
                }
                role.setFpoint(role.getFpoint() - count);
                final RespUpdate respUpdate = new RespUpdate();
                respUpdate.setId(role.getRoleId());
                final List<FiedValue> list = new ArrayList<FiedValue>();
                this.calculateBase(role);
                list.add(new FiedValue("gg", (byte)2, role.getGg().shortValue()));
                list.add(new FiedValue("lx", (byte)2, role.getLx().shortValue()));
                list.add(new FiedValue("ll", (byte)2, role.getLl().shortValue()));
                list.add(new FiedValue("mj", (byte)2, role.getMj().shortValue()));
                list.add(new FiedValue("fpoint", (byte)2, role.getFpoint().shortValue()));
                list.add(new FiedValue("life", (byte)3, CountUtils.countLife(role)));
                list.add(new FiedValue("mana", (byte)3, CountUtils.countMana(role)));
                list.add(new FiedValue("curr_life", (byte)3, role.getCurrLife()));
                list.add(new FiedValue("curr_mana", (byte)3, role.getCurrMana()));
                respUpdate.setList(list);
                role.save();
                MessagePusher.pushMessage(role, respUpdate);
            }
        }
        else {
            final Pet pet = this.petService.selectPet(role).get(assignAttrib.getId());
            if (pet != null) {
                if (pet.getFpoint() < count) {
                    return;
                }
                final Integer restorePorCount2 = FormulaHero.restorePorCount(pet.getRebirth(), pet.getPetLv(), LOGIC.LOGICTYPE_PET.getValue());
                if (count > pet.getFpoint() || pet.getGg() - pet.getPetLv() + pet.getMj() - pet.getPetLv() + pet.getLl() - pet.getPetLv() + pet.getLx() - pet.getPetLv() + count > restorePorCount2) {
                    msg = "加点失败";
                    this.logger.error("角色:[{}],角色id:[{}],宠物Id:[{}],加点超出:[{}],未分配点:[{}]", new Object[] { role.getName(), role.getRoleId(), pet.getId(), count, pet.getFpoint() });
                }
                else {
                    if (assignAttrib.getGg() > 0) {
                        pet.setGg(pet.getGg() + assignAttrib.getGg());
                    }
                    if (assignAttrib.getLx() > 0) {
                        pet.setLx(pet.getLx() + assignAttrib.getLx());
                    }
                    if (assignAttrib.getLl() > 0) {
                        pet.setLl(pet.getLl() + assignAttrib.getLl());
                    }
                    if (assignAttrib.getMj() > 0) {
                        pet.setMj(pet.getMj() + assignAttrib.getMj());
                    }
                    int fpoin = pet.getFpoint() - count;
                    if (fpoin < 0) {
                        this.logger.error("宠物分配点数扣除后小于0宠物唯一编号 = {}", pet.getId());
                        fpoin = 0;
                    }
                    pet.setFpoint(fpoin);
                    this.petService.calculateBase(role.getId(), pet);
                    this.petService.getArenaScore(role.getId(), pet);
                    this.petService.sendPetPanels(role, pet);
                    pet.save();
                }
            }
        }
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(msg));
    }
    
    public void extendStoreSize(final Role role, final ReqExtendStoreSize extendStoreSize) {
        boolean flag = false;
        if (LOGIC.LOGICTYPE_HERO.getValue() == extendStoreSize.type) {
            if (extendStoreSize.isStore == null || extendStoreSize.isStore == 0) {
                final int num = role.getExtendBox().getPosition() + 1;
                final Item item = ConvertDataPool.bagExtend.get(num);
                if (item != null && this.deductCurrency(role, item.getItem(), item.getNum(), "扩展背包格子")) {
                    role.getExtendBox().setPosition(num);
                    flag = true;
                }
                else {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("扩展背包格子已上限"));
                }
            }
            else {
                final int numStore = role.getExtendBox().getStorePosition() + 1;
                final Item itemStore = ConvertDataPool.storeExtend.get(numStore);
                if (itemStore != null && this.deductCurrency(role, itemStore.getItem(), itemStore.getNum(), "扩展仓库格子")) {
                    role.getExtendBox().setStorePosition(numStore);
                    flag = true;
                }
                else {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("扩展仓库格子已上限"));
                }
            }
        }
        else if (LOGIC.LOGICTYPE_PET.getValue() == extendStoreSize.type) {
            if (extendStoreSize.isStore == null || extendStoreSize.isStore == 0) {
                final int buyPetGrid = role.getExtendBox().getBuyPetGrid();
                if (buyPetGrid + 1 > Const.BUY_PET_GRID) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("扩展宠物携带已上限"));
                    return;
                }
                final Long money = Const.ADD_PET_LIMIT_COST[buyPetGrid];
                if (role.getSilver() >= money) {
                    this.deductCurrency(role, Const.RESTYPE_SILVER, money, "扩展宠物格子");
                    role.getExtendBox().setBuyPetGrid(buyPetGrid + 1);
                    flag = true;
                }
                else {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("银两不足"));
                }
            }
            else {
                final int num = role.getExtendBox().getPetEquipPosition() + 1;
                final Item item = ConvertDataPool.petBagEquipExtend.get(num);
                if (item != null && this.deductCurrency(role, item.getItem(), item.getNum(), "扩展背包格子")) {
                    role.getExtendBox().setPetEquipPosition(num);
                    flag = true;
                }
                else {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("扩展背包格子已上限"));
                }
            }
        }
        if (flag) {
            final RespUpdate respUpdate = new RespUpdate();
            respUpdate.setId(role.getRoleId());
            final List<FiedValue> list = new ArrayList<FiedValue>();
            list.add(new FiedValue("buyPetGrid", (byte)3, role.getExtendBox().getBuyPetGrid()));
            list.add(new FiedValue("buyBagGrid", (byte)3, role.getExtendBox().getPosition()));
            list.add(new FiedValue("buyStoreGrid", (byte)3, role.getExtendBox().getStorePosition()));
            list.add(new FiedValue("buyPetEquipGrid", (byte)3, role.getExtendBox().getPetEquipPosition()));
            respUpdate.setList(list);
            MessagePusher.pushMessage(role, respUpdate);
            role.save();
        }
    }
    
    public void updateDrill(final Role role) {
        role.getExtendBox().setDrill((byte)1);
        SpringUtils.getPlayerService().updateRole(role);
        role.save();
    }
    
    public void openAutoFightNew(final Role role, final Fight fight) {
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        try {
            final FightObject fightObject = fight.queryFightObject(role.getRoleId());
            if (fightObject == null) {
                return;
            }
            if (role.getExtendBox().getAutoFight() == 1) {
                fightObject.setAutoFight(1);
                final ScheduledFuture<?> timer = fight.getTimeOutTask();
                SkillAutoPicker autoPicker = fightNewService.getAutoObject(fightObject, fight);
                ReqFightHandle reqFightHandle = new ReqFightHandle();
                reqFightHandle.setRound(fight.getRound());
                reqFightHandle.setAutoFight(1);
                reqFightHandle.setAttackerId(fightObject.getId());
                reqFightHandle.setTargetId(autoPicker.getTargetId());
                reqFightHandle.setSkillId(autoPicker.getSkillId());
                reqFightHandle.setType(autoPicker.getType());
                final List<RespFightCommandInfo> fightCommandInfos = new ArrayList<RespFightCommandInfo>();
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
                final RespFightCommandInfo fightCommandInfoRole = new RespFightCommandInfo();
                fightCommandInfoRole.setId(fightObject.getId());
                fightCommandInfoRole.setSkillId(autoPicker.getSkillId());
                fightCommandInfoRole.setType(autoPicker.getType());
                fightCommandInfoRole.setAction(1);
                if (role.getRace() == RACE.RACE_LONG.getValue()) {
                    final Map<String, Object> autoSet = role.getExtendBox().getAutoFightSet();
                    final Object born = autoSet.get("born");
                    if (born != null) {
                        fightCommandInfoRole.setBorn(Integer.valueOf(String.valueOf(born)));
                    }
                    else {
                        fightCommandInfoRole.setBorn(1);
                    }
                }
                fightCommandInfos.add(fightCommandInfoRole);
                if (fightObject.getFightPetId() > 0) {
                    final FightObject petObject = fight.queryFightObject(fightObject.getFightPetId());
                    if (petObject != null && !petObject.isFallDown()) {
                        petObject.setAutoFight(1);
                        autoPicker = fightNewService.getAutoObject(petObject, fight);
                        if (timer != null && (!timer.isDone() || !timer.isCancelled()) && !petObject.isDoAction()) {
                            reqFightHandle = new ReqFightHandle();
                            reqFightHandle.setRound(fight.getRound());
                            reqFightHandle.setAutoFight(1);
                            reqFightHandle.setAttackerId(petObject.getId());
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
                        final RespFightCommandInfo fightCommandInfoPet = new RespFightCommandInfo();
                        fightCommandInfoPet.setId(petObject.getId());
                        fightCommandInfoPet.setSkillId(autoPicker.getSkillId());
                        fightCommandInfoPet.setType(autoPicker.getType());
                        fightCommandInfoPet.setAction(1);
                        fightCommandInfos.add(fightCommandInfoPet);
                    }
                }
                if (fightCommandInfos.size() <= 0) {
                    MessageAssistUtil.sendIsSuccess(role, fightObject.getId(), (byte)5, null);
                }
                else {
                    MessageAssistUtil.sendIsSuccess(role, fightObject.getId(), (byte)5, fightCommandInfos);
                }
            }
            else {
                fightObject.setAutoFight(0);
                if (fightObject.getFightPetId() > 0) {
                    final FightObject petObject2 = fight.queryFightObject(fightObject.getFightPetId());
                    if (petObject2 != null) {
                        petObject2.setAutoFight(0);
                    }
                }
                if (fightObject.isDoAction()) {
                    MessageAssistUtil.sendIsSuccess(role, fightObject.getId(), (byte)7, null);
                }
                else {
                    MessageAssistUtil.sendIsSuccess(role, fightObject.getId(), (byte)6, null);
                    fight.removeHostTask(fightObject.getId());
                    fight.removePetTask(fightObject.getFightPetId());
                }
            }
        }
        catch (final Exception e) {
            this.logger.error("战斗中设置自动战斗错误=让目标防御={}", Lang.getStackTrace((Throwable)e));
            final ReqFightHandle reqFightHandle2 = new ReqFightHandle();
            reqFightHandle2.setRound(fight.getRound());
            reqFightHandle2.setAutoFight(1);
            reqFightHandle2.setAttackerId(role.getRoleId());
            reqFightHandle2.setTargetId(role.getRoleId());
            reqFightHandle2.setSkillId(0);
            reqFightHandle2.setType(1);
            fightNewService.doActionNew(reqFightHandle2);
        }
    }
    
    public void dye(final Role role, final ReqDey reqDey) {
        final boolean clicker = this.mouserClicker(role, "dye", 250L);
        if (!clicker) {
            return;
        }
        int a = 0;
        int b = 0;
        int c = 0;
        long silver = 0L;
        final SysHero sysHero = PartDataPool.heroMap.get(11001);
        if (sysHero != null) {
            final Gson gson = new Gson();
            final NutMap map = (NutMap)gson.fromJson(role.getColor(), NutMap.class);
            final int curve_a = map.getInt("a");
            if (curve_a > 0 && reqDey.getColorOne() <= 0) {
                silver += 250000L;
                map.setv("a", 0);
            }
            final int curve_b = map.getInt("b");
            if (curve_b > 0 && reqDey.getColorTwo() <= 0) {
                silver += 250000L;
                map.setv("b", 0);
            }
            final SysRoleChangeColor color = PartDataPool.roleChangeColor.get(sysHero.getShape());
            if (reqDey.getColorOne() > 0) {
                final List<ChangeColor> colors = JSON.parseArray(color.getColorOne(), (Class)ChangeColor.class);
                final Optional<ChangeColor> optional = colors.stream().filter(data -> {
                    final boolean b2 = data.getSerial() == reqDey.getColorOne();
                    return b2;
                }).findFirst();
                if (optional.isPresent()) {
                    final ChangeColor changeColor = optional.get();
                    map.setv("a", (Object)changeColor.getSerial());
                    a += changeColor.getGoodsNum()[0];
                    b += changeColor.getGoodsNum()[1];
                    c += changeColor.getGoodsNum()[2];
                }
            }
            if (reqDey.getColorTwo() > 0) {
                final List<ChangeColor> colors = JSON.parseArray(color.getColorTwo(), (Class)ChangeColor.class);
                final Optional<ChangeColor> optional = colors.stream().filter(data -> {
                    final boolean b3 = data.getSerial() == reqDey.getColorTwo();
                    return b3;
                }).findFirst();
                if (optional.isPresent()) {
                    final ChangeColor changeColor = optional.get();
                    map.setv("b", (Object)changeColor.getSerial());
                    a += changeColor.getGoodsNum()[0];
                    b += changeColor.getGoodsNum()[1];
                    c += changeColor.getGoodsNum()[2];
                }
            }
            if (silver < 0L) {
                this.logger.error("染色货币数量负数{}", (Object)silver);
                return;
            }
            if (silver > 0L && role.getSilver() <= silver) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "客官，您的银两不足", false));
                return;
            }
            final Integer a_nu = this.bagService.getBagGoods(role, 75055, a);
            final Integer b_nu = this.bagService.getBagGoods(role, 75056, b);
            final Integer c_nu = this.bagService.getBagGoods(role, 75057, c);
            if (a_nu == null && b_nu == null && c_nu == null) {
                this.bagService.consumeBagGoods(role, 75055, a, true, true, null);
                this.bagService.consumeBagGoods(role, 75056, b, true, true, null);
                this.bagService.consumeBagGoods(role, 75057, c, true, true, null);
                this.deductCurrency(role, Const.RESTYPE_SILVER, silver, null);
                role.setColor(map.toString());
                role.save();
                final RespUpdate respUpdate = new RespUpdate();
                respUpdate.setId(role.getRoleId());
                final List<FiedValue> list = this.deyList(role.getColor());
                respUpdate.setList(list);
                MessagePusher.pushMessage(role, respUpdate);
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "染色成功", false));
                EventDispatcher.getInstance().fireEvent(new RoleAspectChangeEvent(EventType.ROLE_ASPECT_CHANGE, role));
            }
            else {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "客官，您的染料不足，巧妇难为无米之炊啊", false));
            }
        }
    }
    
    public void practiceUpGrade(final Role role, final int grade) {
        final PartyPractice practiceInfo = role.getRest().getPartyPractice();
        final Map<Integer, SysPartyPractice> map = ExpDataPool.partyPractice;
        final Integer partyPracticeLv = practiceInfo.getLevel();
        final int sumGrade = grade + partyPracticeLv;
        final SysPartyPractice practice = map.get(sumGrade);
        final int need = role.getRelive() * 1000 + role.getLevel();
        if (need < practice.getNeed()) {
            final int level = practice.getNeed() % 1000;
            final int relive = practice.getNeed() / 1000;
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
        int needTribute = 0;
        int needCoin = 0;
        for (int i = partyPracticeLv + 1; i <= sumGrade; ++i) {
            final SysPartyPractice partyPractice = map.get(i);
            if (partyPractice != null) {
                needTribute += partyPractice.getTribute();
                needCoin += partyPractice.getCoin();
            }
        }
        if (!this.checkCurrency(role, Const.RESTYPE_COIN, needCoin)) {
            return;
        }
        if (!this.checkCurrency(role, Const.RESTYPE_TRIBUTE, needTribute)) {
            return;
        }
        this.deductCurrency(role, Const.RESTYPE_COIN, needCoin, "帮派修炼提升：扣除贡币");
        this.deductCurrency(role, Const.RESTYPE_TRIBUTE, needTribute, "帮派修炼提升：扣除帮贡");
        practiceInfo.setPoints(practiceInfo.getPoints() + (sumGrade - partyPracticeLv));
        practiceInfo.setLevel(sumGrade);
        role.save();
        this.refreshPartyPracticeInfo(role);
    }
    
    public void assignPartyPractice(final Role role, final String nutMap) {
        final Map<Integer, Integer> practiceMap = role.getRest().getParty();
        final PartyPractice partyPractice = role.getRest().getPartyPractice();
        final NutMap map = Json.fromJson(NutMap.class, nutMap);
        final List<Integer> list = FACTION_PRO_DICT.toList();
        int count = 0;
        for (final String key : map.keySet()) {
            final Integer pro = Integer.valueOf(key);
            final boolean contains = list.contains(pro);
            if (!contains) {
                MessagePusher.pushMessage(role, new RespMsg("修炼属性中没有该修炼类型"));
                return;
            }
            final int num = map.getInt(key);
            if (num < 0) {
                return;
            }
            if (FACTION_PRO_DICT.K_KZ_CAP.getPro() == pro) {
                final int limit = partyPractice.getLevel() / 5;
                if (num > limit) {
                    MessagePusher.pushMessage(role, new RespMsg("每提升修炼等级5级可分配1点上限，当前已达上限"));
                    return;
                }
            }
            count += num - practiceMap.get(pro);
        }
        if (count > partyPractice.getPoints()) {
            MessagePusher.pushMessage(role, new RespMsg("剩余修炼点不足"));
            return;
        }
        for (final String key : map.keySet()) {
            final int num2 = map.getInt(key);
            if (num2 > 0) {
                practiceMap.put(Integer.valueOf(key), num2);
            }
        }
        final int points = partyPractice.getPoints() - count;
        final PartyPractice partyPractice2 = partyPractice;
        int points2;
        if (points < 0) {
            points2 = 0;
        }
        else {
            points2 = points;
        }
        partyPractice2.setPoints(points2);
        final List<FiedValue> faction = this.faction(role, new ArrayList<FiedValue>());
        MessagePusher.pushMessage(role, new RespUpdateImprovement(role.getRoleId(), faction));
        this.refreshPartyPracticeInfo(role);
        role.save();
    }
    
    public void resetAssignPartyPractice(final Role role) {
        if (!SafeUtil.isUnlock(role)) {
            return;
        }
        final boolean currency = this.deductCurrency(role, Const.RESTYPE_SILVER, 200000L, "重置帮派修炼加点");
        if (!currency) {
            return;
        }
        final PartyPractice practice = role.getRest().getPartyPractice();
        practice.setPoints(practice.getLevel());
        role.getRest().setParty(DataGlobalFunc.faction());
        final List<FiedValue> list = this.faction(role, new ArrayList<FiedValue>());
        MessagePusher.pushMessage(role, new RespUpdateImprovement(role.getRoleId(), list));
        MessagePusher.pushMessage(role, new RespMsg("重置成功"));
        this.refreshPartyPracticeInfo(role);
        role.save();
    }
    
    public void restAttribute(final Role role, final byte type) {
        if (!SafeUtil.isUnlock(role)) {
            return;
        }
        String msg = null;
        final int serial = Const.rest_attribute[type];
        if (type == 0) {
            final Integer count = FormulaHero.restorePorCount(role.getRelive(), role.getLevel(), LOGIC.LOGICTYPE_HERO.getValue());
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
                Integer count = 5;
                if (role.getGg() - 5 < role.getLevel()) {
                    count = role.getGg() - role.getLevel();
                }
                role.setGg(role.getGg() - count);
                role.setFpoint(count + role.getFpoint());
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
                Integer count = 5;
                if (role.getLx() - 5 < role.getLevel()) {
                    count = role.getLx() - role.getLevel();
                }
                role.setLx(role.getLx() - count);
                role.setFpoint(count + role.getFpoint());
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
                Integer count = 5;
                if (role.getLl() - 5 < role.getLevel()) {
                    count = role.getLl() - role.getLevel();
                }
                role.setLl(role.getLl() - count);
                role.setFpoint(count + role.getFpoint());
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
                Integer count = 5;
                if (role.getMj() - 5 < role.getLevel()) {
                    count = role.getMj() - role.getLevel();
                }
                role.setMj(role.getMj() - count);
                role.setFpoint(count + role.getFpoint());
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
    }
    
    public void refreshPartyPracticeInfo(final Role role) {
        final PartyPractice practiceInfo = role.getRest().getPartyPractice();
        MessagePusher.pushMessage(role, new RespPartyPracticeInfo((short)practiceInfo.getLevel(), (short)practiceInfo.getPoints()));
    }
    
    public void setElseFight(final Role role) {
        if (role.getExtendBox().getSetMap().size() <= 0) {
            role.getExtendBox().setSetMap(DataGlobalFunc.setting());
            role.save();
        }
        final RespElseFight elseFight = new RespElseFight();
        elseFight.setAutoFight(role.getExtendBox().getSetMap().get("else_auto_fight"));
        elseFight.setShowDbSpeed(role.getExtendBox().getSetMap().get("else_db_speed_show"));
        MessagePusher.pushMessage(role, elseFight);
    }
    
    public int getRoleId() {
        return SpringUtils.getIdentityService().getNextId(IdentityType.ROLE);
    }
    
    public Role getOnlinePlayer(final String name) {
        final long uid = SpringUtils.getPlayerService().getUidBy(name);
        return this.getOnlinePlayer(uid);
    }
    
    public boolean checkCurrency(final Role role, final Integer type, final long price) {
        if (type.equals(Const.RESTYPE_GOLD) && role.getGold() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RESTYPE_SILVER) && role.getSilver() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RESTYPE_COIN) && role.getCoin() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RESTYPE_GUO) && role.getExtendBox().getGuoScore() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RESTYPE_BRAC) && role.getExtendBox().getBracScore() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RESTYPE_NAO) && role.getExtendBox().getNaoScore() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RESTYPE_DI) && role.getExtendBox().getDiScore() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RESTYPE_ARENA) && role.getExtendBox().getArenaScore() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RESTYPE_LONG) && role.getExtendBox().getLongScore() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RESTYPE_NICE) && role.getExtendBox().getNiceScore() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RESTYPE_TRIBUTE) && role.getExtendBox().getHelpScore() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RES_TYPE_FEATS) && role.getExtendBox().getFeatsScore() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RES_TYPE_CHARGE_SCORE) && role.getExtendBox().getFeatsScore() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RES_TYPE_BOUND_GOLD) && role.getExtendBox().getBoundGold() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        if (type.equals(Const.RES_TYPE_ACHIEVEMENT) && role.getExtendBox().getAchievementScore() < price) {
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(price));
            return false;
        }
        return true;
    }
    
    public boolean deductCurrency(final Role role, final Integer type, final long num, final String string) {
        if (num <= 0L && string != null) {
            this.logger.error("扣除货币负数，角色 = {}, 方法 = {}，货币类型 = {}，数量 = {}", new Object[] { role.getName(), string, type, num });
            return false;
        }
        long residue = 0L;
        if (type.equals(Const.RESTYPE_GOLD) && role.getGold() >= num) {
            residue = role.getGold() - num;
            role.setGold(residue);
        }
        else if (type.equals(Const.RESTYPE_SILVER) && role.getSilver() >= num) {
            residue = role.getSilver() - num;
            role.setSilver(residue);
        }
        else if (type.equals(Const.RESTYPE_COIN) && role.getCoin() >= num) {
            residue = role.getCoin() - num;
            role.setCoin(residue);
        }
        else if (type.equals(Const.RESTYPE_GUO) && role.getExtendBox().getGuoScore() >= num) {
            residue = role.getExtendBox().getGuoScore() - num;
            role.getExtendBox().setGuoScore(residue);
        }
        else if (type.equals(Const.RESTYPE_BRAC) && role.getExtendBox().getBracScore() >= num) {
            residue = role.getExtendBox().getBracScore() - num;
            role.getExtendBox().setBracScore(residue);
        }
        else if (type.equals(Const.RESTYPE_NAO) && role.getExtendBox().getNaoScore() >= num) {
            residue = role.getExtendBox().getNaoScore() - num;
            role.getExtendBox().setNaoScore(residue);
        }
        else if (type.equals(Const.RESTYPE_DI) && role.getExtendBox().getDiScore() >= num) {
            residue = role.getExtendBox().getDiScore() - num;
            role.getExtendBox().setDiScore(residue);
        }
        else if (type.equals(Const.RESTYPE_ARENA) && role.getExtendBox().getArenaScore() >= num) {
            residue = role.getExtendBox().getArenaScore() - num;
            role.getExtendBox().setArenaScore(residue);
        }
        else if (type.equals(Const.RESTYPE_LONG) && role.getExtendBox().getLongScore() >= num) {
            residue = role.getExtendBox().getLongScore() - num;
            role.getExtendBox().setLongScore(residue);
        }
        else if (type.equals(Const.RESTYPE_NICE) && role.getExtendBox().getNiceScore() >= num) {
            residue = role.getExtendBox().getNiceScore() - num;
            role.getExtendBox().setNiceScore(residue);
        }
        else if (type.equals(Const.RESTYPE_TRIBUTE) && role.getExtendBox().getHelpScore() >= num) {
            residue = role.getExtendBox().getHelpScore() - num;
            role.getExtendBox().setHelpScore(residue);
        }
        else if (type.equals(Const.RES_TYPE_FEATS) && role.getExtendBox().getFeatsScore() >= num) {
            residue = role.getExtendBox().getFeatsScore() - num;
            role.getExtendBox().setFeatsScore(residue);
        }
        else if (type.equals(Const.RES_TYPE_CHARGE_SCORE) && role.getExtendBox().getChargeScore() >= num) {
            residue = role.getExtendBox().getChargeScore() - num;
            role.getExtendBox().setChargeScore(residue);
        }
        else if (type.equals(Const.RES_TYPE_BOUND_GOLD) && role.getExtendBox().getBoundGold() >= num) {
            residue = role.getExtendBox().getBoundGold() - num;
            role.getExtendBox().setBoundGold(residue);
        }
        else {
            if (type.equals(Const.RES_TYPE_ACHIEVEMENT) && role.getExtendBox().getAchievementScore() >= num) {
                residue = role.getExtendBox().getAchievementScore() - num;
                role.getExtendBox().setAchievementScore(residue);
                return true;
            }
            MessagePusher.notify2Misc(role, false, type, MoneyUtil.colours(num));
            return false;
        }
        role.save();
        this.refreshRoleCurrency(role);
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1000, (byte)2, String.format("货币类型：%d，消耗数量：%d，减少后数量%d，原因：%s", type, num, residue, string), role.getMarking());
        return true;
    }
    
    public Role getOnlinePlayer(final long id) {
        if (DataCache.ONLINE_ROLES.containsKey(id)) {
            return DataCache.ONLINE_ROLES.get(id);
        }
        if (DataCache.OFFLINE_ROLES.containsKey(id)) {
            return null;
        }
        return SpringUtils.getPlayerService().getPlayerBy(id);
    }
    
    public Role getPlayer(final long id) {
        if (DataCache.OFFLINE_ROLES.containsKey(id)) {
            return DataCache.OFFLINE_ROLES.get(id);
        }
        if (DataCache.ONLINE_ROLES.containsKey(id)) {
            return DataCache.ONLINE_ROLES.get(id);
        }
        return SpringUtils.getPlayerService().getPlayerBy(id);
    }
    
    public Role getOnlinePlayer(final int roleId) {
        final long id = SpringUtils.getPlayerService().getUidBy(roleId);
        if (DataCache.OFFLINE_ROLES.containsKey(id)) {
            return null;
        }
        if (DataCache.ONLINE_ROLES.containsKey(id)) {
            return DataCache.ONLINE_ROLES.get(id);
        }
        return SpringUtils.getPlayerService().getPlayerBy(id);
    }
    
    public Role getOfOnlinePlayer(final int roleId) {
        final long id = SpringUtils.getPlayerService().getUidBy(roleId);
        if (DataCache.OFFLINE_ROLES.containsKey(id)) {
            return DataCache.OFFLINE_ROLES.get(id);
        }
        final Role role = SpringUtils.getPlayerService().getPlayerBy(id);
        return role;
    }
    
    public Role getPlayer(final int roleId) {
        final long id = SpringUtils.getPlayerService().getUidBy(roleId);
        if (this.isOnline(id)) {
            return this.getOnlinePlayer(id);
        }
        return this.getOfOnlinePlayer(roleId);
    }
    
    public void recordPosition(final Role role, final ReqMultiMoveTo reqMultiMoveTo) {
        role.setPos(reqMultiMoveTo.getX(), reqMultiMoveTo.getY(), 0, reqMultiMoveTo.getMapId());
        if (this.teamService.isTeamLeader(role)) {
            final Team team = this.teamService.getTeam(role.getRoleId());
            if (team != null) {
                for (int i = 0; i < team.getList().size(); ++i) {
                    final Member member = team.getList().get(i);
                    if (member != null && !member.getId().equals(role.getId()) && member.isInTeam()) {
                        final Role memberRole = this.getPlayer(member.getId());
                        if (memberRole != null && role.getPos().getMapId().equals(memberRole.getPos().getMapId())) {
                            memberRole.setPos(reqMultiMoveTo.getX(), reqMultiMoveTo.getY(), 0, reqMultiMoveTo.getMapId());
                            SceneManager.INSTANCE.movePosition(memberRole);
                        }
                    }
                }
            }
        }
    }
    
    public void punishFightDead(final Role role, final long exp, final String reason) {
        if (role.getLastDead() > 0) {
            return;
        }
        this.addExp(role, exp, reason, true);
    }
    
    public void sendOffline(final Role role, final boolean flag) {
        final long millis = System.currentTimeMillis();
        final String marking = SpringUtils.getBean(ServerService.class).getMarking();
        final String key = role.getId() + "_countdown_" + marking + millis / 1000L;
        if (this.redisService.exists(key)) {
            return;
        }
        this.redisService.set(key, "", 0L);
        ThreadLocalUtil.addLocalTask(role, () -> {
            int time = 300000;
            if (flag) {
                final Team team = SpringUtils.getBean(TeamService.class).getTeam(role.getRoleId());
                if (team != null) {
                    time = role.getOfflineNotice().getTeamOfflineTime();
                }
                else {
                    time = role.getOfflineNotice().getAloneOfflineTime();
                }
            }
            role.setOfLineTime(millis);
            role.setState(2);
            role.save();
            SpringUtils.getBean(RabbitService.class).sendMessage(key, time);
        });
    }
    
    public long getOnlineSec(final Role role) {
        final long now = System.currentTimeMillis();
        if (now > role.getOnlineLoginTime()) {
            final int currentOnlineSec = (int)((now - role.getOnlineLoginTime()) / 1000L);
            return currentOnlineSec + role.getPreOnlineSec();
        }
        return 0L;
    }
    
    public void resetOnlineSec(final Role role) {
        role.setPreOnlineSec(0L);
        role.setOnlineLoginTime(System.currentTimeMillis());
    }
    
    public void checkReset(final Role role) {
        this.checkDailyFiveReset(role);
        this.checkWeeklyReset(role);
    }
    
    public void checkDailyFiveReset(final Role role) {
        if (!this.isCanDailyFiveReset(role)) {
            return;
        }
        this.doDailyFiveReset(role, false);
    }
    
    public void resetQuotaLimit(final Role role) {
        final int monday = TimeUtil.dayInMillis(TimeUtil.getWeekday(2));
        final int time = role.getExtendBox().getResetQuotaTime();
        if (monday != time) {
            role.getExtendBox().setQuotaLimit(DataGlobalFunc.setQuota());
            role.getExtendBox().setResetQuotaTime(monday);
        }
    }
    
    public void doDailyFiveReset(final Role role, final boolean flag) {
        if (flag) {
            this.checkWeeklyReset(role);
        }
        role.setDailyFiveResetTime(System.currentTimeMillis());
        this.resetOnlineSec(role);
        SpringUtils.getBean(SafeService.class).checkSafePasswordReset(role);
        this.resetQuotaLimit(role);
        role.getExtendBox().setDayQuotaLimit(NutMap.NEW());
        SpringUtils.getBean(ActivityService.class).dailyResetActivity(role);
        SpringUtils.getBean(TaskService.class).dailyResetTask(role);
        role.save();
        SpringUtils.getBean(WelfareService.class).dailyResetFiveGuess(role);
    }
    
    private boolean isCanDailyFiveReset(final Role role) {
        final long dailyFiveResetTime = role.getDailyFiveResetTime();
        final Calendar roleResetCalader = Calendar.getInstance();
        roleResetCalader.setTimeInMillis(dailyFiveResetTime);
        final Calendar systemResetTime = this.getSystemFiveResetTime();
        return roleResetCalader.before(systemResetTime);
    }
    
    private Calendar getSystemFiveResetTime() {
        final Calendar calendar = Calendar.getInstance();
        if (calendar.get(11) < 5) {
            calendar.add(6, -1);
        }
        calendar.set(11, 5);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar;
    }
    
    public void checkWeeklyReset(final Role role) {
        if (!this.isCanWeeklyReset(role)) {
            this.logger.info("不能进行每周重置，角色ID: {}, 上一次重置时间戳：{}", (Object)role.getRoleId(), (Object)role.getWeeklyResetTime());
            return;
        }
        this.doWeeklyReset(role);
    }
    
    public boolean isCanWeeklyReset(final Role role) {
        final long weeklyResetTime = role.getWeeklyResetTime();
        final Calendar roleResetCalader = Calendar.getInstance();
        roleResetCalader.setTimeInMillis(weeklyResetTime);
        final Calendar systemResetTime = this.getSystemWeeklyResetTime();
        return roleResetCalader.before(systemResetTime);
    }
    
    public Calendar getSystemWeeklyResetTime() {
        final Calendar calendar = Calendar.getInstance();
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
    
    public void doWeeklyReset(final Role role) {
        this.logger.warn("玩家{}进行每周重置，上一重置时间为{}", (Object)role.getRoleId(), (Object)role.getWeeklyResetTime());
        role.getTaskProgress().setLoop(1);
        role.setWeeklyResetTime(System.currentTimeMillis());
        role.getPkInfoBox().setKillRoles(new HashMap<Long, PkRecord>());
        role.getPkInfoBox().setBeKillRoles(new HashMap<Long, PkRecord>());
        role.save();
        this.petService.doWeeklyReset(role);
    }
    
    public Role getPlayerBy(final int roleId) {
        final long id = SpringUtils.getPlayerService().getUidBy(roleId);
        return this.getPlayerBy(id);
    }
    
    public Role getPlayerBy(final long id) {
        return SpringUtils.getPlayerService().getPlayerBy(id);
    }
    
    public void modifyName(final Role role, String newName) {
        if (!SafeUtil.isUnlock(role)) {
            return;
        }
        final Integer num = this.bagService.getBagGoodsNum(role, 90011);
        final IoSession session = SessionUtils.getSessionBy(role.getId());
        if (!this.canCreateRole(session, newName, LOGIC.LOGICTYPE_HERO.getValue())) {
            return;
        }
        newName = CheckCharUtils.replaceName(newName);
        if (num >= 1) {
            final Integer integer = this.bagService.consumeBagGoods(role, 90011, 1, false, true, "角色改名");
            if (integer != null) {
                return;
            }
        }
        else {
            final boolean flag = this.deductCurrency(role, Const.RESTYPE_GOLD, 600L, "角色改名");
            if (!flag) {
                return;
            }
        }
        final String oldName = role.getName();
        role.setName(newName);
        role.save();
        SpringUtils.getPlayerService().updateRoleName(oldName, role);
        MessagePusher.pushMessage(role, new RespHint(3, (short)1));
        this.broadcastService.sendUpdateAppear(role);
        EventDispatcher.getInstance().fireEvent(new com.kitty.listener.event.RoleChangeEvent(EventType.ROLE_CHANGE, role, oldName, newName, com.kitty.listener.event.RoleChangeEvent.NAME));
    }
    
    public void checkReserve(final Role role) {
        final String type = "即将";
        if (role.getSaveLife() <= 250000L) {
            final boolean flag = true;
            final int i18nId = 100;
            final Object[] args = { "气血", null };
            final int n = 1;
            String s;
            if (role.getSaveLife() > 0L) {
                s = type;
            }
            else {
                s = "已";
            }
            args[n] = s;
            MessagePusher.notify2Misc(role, flag, i18nId, args);
        }
        if (role.getSaveMana() <= 250000L) {
            final boolean flag2 = true;
            final int i18nId2 = 100;
            final Object[] args2 = { "法力", null };
            final int n2 = 1;
            String s2;
            if (role.getSaveMana() > 0L) {
                s2 = type;
            }
            else {
                s2 = "已";
            }
            args2[n2] = s2;
            MessagePusher.notify2Misc(role, flag2, i18nId2, args2);
        }
    }
    
    public void chatEquipDetail(final Role ownerRole, final Role role, final long equipId) {
        final List<WearEquip> wearEquips = this.equipService.selectWearEquip(ownerRole);
        WearEquip wearEquip = null;
        for (int i = wearEquips.size() - 1; i >= 0; --i) {
            if (wearEquips.get(i).getId().equals(equipId)) {
                wearEquip = wearEquips.get(i);
                break;
            }
        }
        if (wearEquip != null) {
            final RespMarketGoodCard card = new RespMarketGoodCard(String.valueOf(wearEquip.getId()), ownerRole.getRoleId(), wearEquip.getType(), wearEquip.getField(), wearEquip.getArtifice());
            MessagePusher.pushMessage(role, card);
            return;
        }
        final Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(ownerRole);
        final BagEquip bagEquip = bagEquipMap.get(equipId);
        if (bagEquip != null) {
            final RespMarketGoodCard card2 = new RespMarketGoodCard(String.valueOf(bagEquip.getId()), ownerRole.getRoleId(), bagEquip.getType(), bagEquip.getField(), bagEquip.getArtifice());
            MessagePusher.pushMessage(role, card2);
            return;
        }
        MessagePusher.pushMessage(role, new RespMsg("此链接已失效！"));
    }
    
    public void goBackBorn(final Role role, final Pos pos) {
        if (role == null) {
            return;
        }
        final ReqTelePort reqTelePort = new ReqTelePort();
        reqTelePort.setMapId(pos.getMapId());
        reqTelePort.setX(pos.getX());
        reqTelePort.setY(pos.getY());
        SpringUtils.getMapService().changeMap(role, reqTelePort, null);
    }
    
    public void getAssistPetList(final Role role, final Integer serial) {
        SuitParam suit = null;
        final RespAssistPetList respAssistPetList = new RespAssistPetList();
        respAssistPetList.setSerial(serial);
        boolean flag = true;
        if (serial == 0) {
            final List<SuitParam> suits = role.getExtendBox().getSuits();
            if (suits.size() <= 0) {
                flag = false;
                suits.add(new SuitParam());
            }
            suit = suits.get(0);
        }
        else {
            final List<SuitParam> suits = role.getExtendBox().getSuits();
            if (suits.size() <= 0) {
                flag = false;
                suits.add(new SuitParam());
            }
            suit = suits.get(serial);
        }
        final Map<Integer, Pet> petMap = this.petService.selectPet(role);
        List<Integer> petList = null;
        if (suit != null && suit.getPetIds() != null) {
            petList = suit.getPetIds().stream().collect(Collectors.toList());
        }
        else {
            petList = new ArrayList<Integer>();
        }
        final List<Integer> otherList = new ArrayList<Integer>();
        if (petMap.size() > 0) {
            for (final Map.Entry<Integer, Pet> entry : petMap.entrySet()) {
                final Pet pet = entry.getValue();
                if (pet.getSkills().size() > 0) {
                    for (final PetSkill petSkill : pet.getSkills()) {
                        if (SkillConst.assistSkills.contains(petSkill.getSerial())) {
                            if (petList.contains(pet.getPetId())) {
                                petList.remove(pet.getPetId());
                                break;
                            }
                            otherList.add(pet.getPetId());
                            break;
                        }
                    }
                }
            }
        }
        boolean isUpdate = false;
        if (petList.size() > 0) {
            for (final Integer petId : petList) {
                suit.getPetIds().remove(petId);
                isUpdate = true;
            }
        }
        if (otherList.size() > 0) {
            if (suit.getPetIds() == null) {
                suit.setPetIds(new ArrayList<Integer>());
            }
            for (final Integer petId : otherList) {
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
    
    public void addSuit(final Role role, final Integer serial, final String name, final List<Integer> petIds) {
        RespAssistPetList respAssistPetList = new RespAssistPetList();
        if (serial == null) {
            final List<SuitParam> suits = role.getExtendBox().getSuits();
            final SuitParam suit = new SuitParam();
            suit.setSerial(suits.size() + 1);
            suit.setName(name);
            suit.setPetIds(petIds);
            suits.add(suit);
            respAssistPetList.setSerial(suit.getSerial());
            respAssistPetList.setName(suit.getName());
            respAssistPetList.setPetIds(suit.getPetIds());
        }
        else {
            final List<SuitParam> suits = role.getExtendBox().getSuits();
            SuitParam suit = null;
            if (serial == 0) {
                suit = suits.get(0);
            }
            else {
                final Optional<SuitParam> any = suits.stream().filter(obj -> obj.getSerial().equals(serial)).findAny();
                if (any.isPresent()) {
                    suit = any.get();
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
    
    public boolean mouserClicker(final Role role, final String type, final long mouse) {
        final long millis = System.currentTimeMillis();
        final long time = role.getTempCache(type, 0L);
        if (time > 0L && millis - time < mouse) {
            return false;
        }
        if (millis - time <= 10L) {
            final Integer warn = role.getTempCache("warn", 0) + 1;
            role.pushTempCache("warn", warn);
            MessagePusher.pushMessage(role, new RespMsg("警告，你已经违规操作第#<R>" + warn + "#次，三次后封号处理"));
        }
        role.pushTempCache(type, millis);
        return true;
    }
    
    public boolean mouserCartoon(final Role role, final String type, final long mouse) {
        final long millis = System.currentTimeMillis();
        final long time = role.getTempCache(type, 0L);
        if (time > 0L && millis - time < mouse) {
            return false;
        }
        role.pushTempCache(type, millis);
        return true;
    }
    
    public void kickOut(final long id, final String msg) {
        final IoSession session = SessionUtils.getSessionBy(id);
        if (session != null) {
            MessagePusher.pushMessage(session, new RespMsg(msg));
            session.closeNow();
        }
    }
    
    public boolean isRelationMobile(final Role role, final String mobile) {
        if (role.getMobile() != null && role.getMobile().length() > 1) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("你已经绑定了"));
            return false;
        }
        final String relation = JedisUtil.get(role.getSid() + "_" + mobile);
        if (relation != null) {
            role.setMobile(relation);
            MessagePusher.pushMessage(role.getId(), new RespCorrelation(relation));
            MessagePusher.pushMessage(role.getId(), new RespMsg("你已经绑定了"));
            return false;
        }
        return true;
    }
    
    public void verificationCode(final Role role, final String mobile) {
        if (CodeUtils.verificationPhone(mobile) || !CodeUtils.checkPhone(mobile)) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("请正确输入手机号码"));
            return;
        }
        if (!this.isRelationMobile(role, mobile)) {
            return;
        }
        final boolean contain = JedisUtil.contain("*_" + mobile);
        if (contain) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("该手机已经被绑定！请改用其号码"));
            return;
        }
        final String key = role.getSid() + "_sms";
        final String code = JedisUtil.get(key);
        if (code != null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("验证码已经发送请注意查收"));
            return;
        }
        final boolean clicker = this.mouserClicker(role, "sms", 60000L);
        if (!clicker) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("验证码已经发送，请稍后再试"));
            return;
        }
        final String verification = String.valueOf((int)((Math.random() * 9.0 + 1.0) * 100000.0));
        JedisUtil.set(key, 3600, String.format("%s_%s", mobile, verification));
        final String sendSms = this.sendService.sendSms(role.getSid(), mobile, verification, 60);
        MessagePusher.pushMessage(role.getId(), new RespMsg(sendSms));
    }
    
    public void relationMobile(final Role role, final String verificationCode) {
        if (role.getMobile() != null && role.getMobile().length() > 1) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("你已经绑定了"));
            return;
        }
        final String key = role.getSid() + "_sms";
        final String value = JedisUtil.get(key);
        if (value == null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("验证码已过期，请重新获取"));
            return;
        }
        final String[] split = value.split("_");
        if (split.length < 2) {
            return;
        }
        if (!split[1].equals(verificationCode)) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("验证码错误"));
            return;
        }
        final String relation = JedisUtil.get(role.getSid() + "_" + split[0]);
        if (relation != null) {
            role.setMobile(relation);
            MessagePusher.pushMessage(role.getId(), new RespCorrelation(relation));
            MessagePusher.pushMessage(role.getId(), new RespMsg("你已经绑定了"));
            return;
        }
        final boolean contain = JedisUtil.contain("*_" + split[0]);
        if (contain) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("该手机已经被绑定！请改用其号码"));
            return;
        }
        role.setMobile(split[0]);
        MessagePusher.pushMessage(role.getId(), new RespCorrelation(role.getMobile()));
        JedisUtil.set(role.getSid() + "_" + split[0], role.getMobile());
        MessagePusher.pushMessage(role.getId(), new RespMsg("绑定成功"));
        this.bagService.sendReward(role, Deploy.mobile_binding, "手机绑定", 0);
        SpringUtils.getAccountService().updateAccountMobile(role.getSid(), role.getMobile());
        JedisUtil.del(key);
    }
}
