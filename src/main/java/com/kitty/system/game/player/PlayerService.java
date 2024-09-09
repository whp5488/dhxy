package com.kitty.system.game.player;

import java.sql.SQLException;
import java.sql.ResultSet;
import org.springframework.jdbc.core.RowMapper;
import com.kitty.mina.session.SessionManager;
import com.kitty.listener.event.LogoutEvent;
import com.kitty.listener.event.RoleExpireEvent;
import com.kitty.listener.event.LogoutSelfEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.scene.SceneManager;
import com.kitty.system.game.email.service.EmailService;
import com.kitty.system.game.house.service.HouseService;
import com.kitty.system.game.general.service.GeneralService;
import com.kitty.system.game.role.service.SettingService;
import com.kitty.system.game.welfare.service.WelfareService;
import com.kitty.system.game.train.TrainService;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import com.kitty.system.game.tianfu.service.RoleDayInCeService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.partner.service.PartnerService;
import java.util.Iterator;
import org.nutz.dao.Cnd;
import com.kitty.system.game.player.bean.PlayerRoles;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.role.message.RespExistedRoleList;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.RoleInfo;
import org.apache.mina.core.session.IoSession;
import java.util.List;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.kitty.system.game.store.service.StoreService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.card.service.CardService;
import com.kitty.cache.RedisService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PlayerService
{
    Logger logger;
    @Autowired
    private Dao dao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CardService cardService;
    @Autowired
    private EquipService equipService;
    @Autowired
    private BagService bagService;
    @Autowired
    private StoreService storeService;
    private ConcurrentMap<Long, PlayerProfile> playerProfiles;
    private ConcurrentHashMap<String, Long> name2roleUid;
    private ConcurrentHashMap<Integer, Long> roleId2roleUid;
    
    public PlayerService() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.playerProfiles = new ConcurrentHashMap();
        this.name2roleUid = new ConcurrentHashMap();
        this.roleId2roleUid = new ConcurrentHashMap();
    }
    
    public PlayerProfile queryPlayerProfile(long playerId) {
        return (PlayerProfile)this.playerProfiles.get(Long.valueOf(playerId));
    }
    
    public Role saveToCache(Role role) {
        this.saveSync(role);
        return role;
    }
    
    public void saveSync(Role role) {
        if (DataCache.OFFLINE_ROLES.containsKey(role.getId())) {
            DataCache.OFFLINE_ROLES.put(role.getId(), role);
        }
        if (DataCache.ONLINE_ROLES.containsKey(role.getId())) {
            DataCache.ONLINE_ROLES.put(role.getId(), role);
        }
    }
    
    public void loadAllPlayerProfiles() {
        this.playerProfiles.clear();
        this.name2roleUid.clear();
        String sql = "select id, s_id, role_id, `name`, icon, serial, `level`, `exp`, relive, race, enter_time, of_line_time, sex, faction, extend_box_json, marking from role";
        List<PlayerProfile> profileList = this.jdbcTemplate.query(sql, new PlayerService.PlayerRowMapper());
        this.logger.info("玩家数量=={}", Integer.valueOf(profileList.size()));
        profileList.forEach(p -> this.addPlayerProfile(p));
    }
    
    public List<RoleInfo> listRoleInfos(String sid, String marking, IoSession session) {
        List<RoleInfo> list = new ArrayList<>();
        List<PlayerProfile> profiles = this.getPlayersBy(sid);
        for (int i = 0; i < profiles.size(); ++i) {
            PlayerProfile playerProfile = (PlayerProfile)profiles.get(i);
            if (marking == null) {
                RoleInfo info = new RoleInfo(playerProfile);
                list.add(info);
            }
            else if (marking.equals(playerProfile.getMarking())) {
                RoleInfo info = new RoleInfo(playerProfile);
                list.add(info);
            }
        }
        if (session != null) {
            RespExistedRoleList respExistedRoleList = new RespExistedRoleList();
            respExistedRoleList.setOpenServerTime(new Long(System.currentTimeMillis() / 1000L).intValue());
            respExistedRoleList.setList(list);
            respExistedRoleList.setAccount_online(1);
            MessagePusher.pushMessage(session, respExistedRoleList);
            return list;
        }
        return list;
    }
    
    public void updateRoleName(String oldName, Role role) {
        PlayerProfile playerProfile = (PlayerProfile)this.playerProfiles.get(role.getId());
        if (playerProfile == null) {
            return;
        }
        this.name2roleUid.remove(oldName);
        this.name2roleUid.put(role.getName(), role.getId());
        playerProfile.setName(role.getName());
        return;
    }
    
    public void updateRole(Role role) {
        PlayerProfile playerProfile = (PlayerProfile)this.playerProfiles.get(role.getId());
        if (playerProfile == null) {
            return;
        }
        PlayerProfile profile = new PlayerProfile(role);
        this.playerProfiles.replace(role.getId(), profile);
        return;
    }
    
    public boolean occupyName(String roleName, long id) {
        return this.name2roleUid.putIfAbsent(roleName, Long.valueOf(id)) == null;
    }
    
    public void addPlayerProfile(PlayerProfile baseInfo) {
        this.playerProfiles.put(baseInfo.getId(), baseInfo);
        this.name2roleUid.put(baseInfo.getName(), baseInfo.getId());
        this.roleId2roleUid.put(baseInfo.getRoleId(), baseInfo.getId());
        this.cacheRoles(baseInfo.getSid(), baseInfo.getId());
    }
    
    public void cacheRoles(String sid, Long id) {
        PlayerRoles playerRoles = (PlayerRoles)this.redisService.get(sid + "_sid", PlayerRoles.class);
        if (playerRoles == null) {
            playerRoles = new PlayerRoles();
            List<Role> roles = this.dao.query(Role.class, Cnd.where("s_id", "=", sid));
            for (Role role : roles) {
                playerRoles.getList().add(role.getId());
            }
        }
        playerRoles.getList().add(id);
        this.redisService.set(sid + "_sid", playerRoles, 0L);
    }
    
    public List<PlayerProfile> getPlayersBy(String sid) {
        List<PlayerProfile> list = new ArrayList<>();
        PlayerProfile playerProfile = (PlayerProfile)this.redisService.get(sid, PlayerProfile.class);
        List<Role> roles = this.dao.query(Role.class, Cnd.where("s_id", "=", sid));
        for (Role role : roles) {
            list.add(new PlayerProfile(role));
        }
        if (playerProfile != null) {
            PlayerProfile profile = (PlayerProfile)list.stream().filter(e -> e.getId().equals(playerProfile.getId())).findFirst().orElse(null);
            if (profile == null) {
                list.add(playerProfile);
            }
        }
        return list;
    }
    
    public Role getPlayerBy(long id) {
        if (id <= 0L) {
            return null;
        }
        Role role = (Role)this.dao.fetch(Role.class, Cnd.where("id", "=", Long.valueOf(id)));
        if (role != null) {
            ((PartnerService)SpringUtils.getBean(PartnerService.class)).loadPartner(role);
            role.doAfterInit();
            this.loadRoleRests(role);
            if (!DataCache.ONLINE_ROLES.containsKey(Long.valueOf(id))) {
                role.setState(2);
                SpringUtils.getRoleService().addOffline(role, false);
            }
        }
        return role;
    }
    
    public void loadRoleRests(Role role) {
        this.equipService.selectEffect(role);
        this.equipService.selectWearEquip(role);
        this.cardService.getCard(role);
        this.cardService.getCardExercise(role);
        this.bagService.getBagGoods(role);
        this.bagService.getPetBagEquip((long)role.getId());
        this.bagService.getBagEquip(role);
        this.storeService.getStoreGoods(role);
        this.storeService.getStoreEquip(role);
        ((RoleDayInCeService)SpringUtils.getBean(RoleDayInCeService.class)).getDataForRoleId(role.getId());
        ((RoleTianFuService)SpringUtils.getBean(RoleTianFuService.class)).getDataByRoleId(role.getId());
        TrainService trainService = (TrainService)SpringUtils.getBean(TrainService.class);
        trainService.getTrain(role.getId());
        trainService.getTrainSeal(role.getId());
        SpringUtils.getMindService().getMind((long)role.getId());
        SpringUtils.getHorseService().getHorse(role);
        ((WelfareService)SpringUtils.getBean(WelfareService.class)).getGiftBagRecord(role);
        SpringUtils.getPetService().selectPet(role);
        ((PartnerService)SpringUtils.getBean(PartnerService.class)).loadPartner(role);
        SpringUtils.getFriendService().loadPal(role);
        ((SettingService)SpringUtils.getBean(SettingService.class)).getSetting(role);
        ((GeneralService)SpringUtils.getBean(GeneralService.class)).loadDomesticate(role);
        ((HouseService)SpringUtils.getBean(HouseService.class)).getHouse((long)(int)role.getRoleId());
        ((EmailService)SpringUtils.getBean(EmailService.class)).loadRoleEmail(role);
    }
    
    public void onPlayerLogout(Role role) {
        SceneManager.INSTANCE.leaveMap((int)role.getPos().getMapId(), role);
        EventDispatcher.getInstance().fireEvent(new LogoutSelfEvent(EventType.LOGOUT_SELF, role));
        EventDispatcher.getInstance().fireEvent(new RoleExpireEvent(EventType.ROLE_EXPIRE, role));
        EventDispatcher.getInstance().fireEvent(new LogoutEvent(EventType.LOGOUT, role));
        SessionManager.INSTANCE.unRegisterSession((int)role.getRoleId());
    }
    
    public long getUidBy(int roleId) {
        return (long)this.roleId2roleUid.getOrDefault(Integer.valueOf(roleId), Long.valueOf(0L));
    }
    
    public long getUidBy(String name) {
        return (long)this.name2roleUid.getOrDefault(name, Long.valueOf(0L));
    }
    
    private class PlayerRowMapper implements RowMapper<PlayerProfile>
    {
        public PlayerProfile mapRow(ResultSet re, int arg1) throws SQLException {
            return new PlayerProfile(re.getString("s_id"), Integer.valueOf(re.getInt("role_id")), Long.valueOf(re.getLong("id")), re.getString("name"), Integer.valueOf(re.getInt("icon")), Integer.valueOf(re.getInt("serial")), Integer.valueOf(re.getInt("level")), re.getLong("exp"), Integer.valueOf(re.getInt("relive")), Integer.valueOf(re.getInt("race")), re.getLong("enter_time"), re.getLong("of_line_time"), Integer.valueOf(re.getInt("sex")), re.getInt("faction"), re.getString("extend_box_json"), re.getString("marking"));
        }
    }
}
