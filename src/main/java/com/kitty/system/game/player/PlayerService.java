
package com.kitty.system.game.player;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.kitty.mina.session.SessionManager;
import com.kitty.listener.event.LogoutEvent;
import com.kitty.listener.event.RoleExpireEvent;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.LogoutSelfEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.scene.SceneManager;
import com.kitty.system.game.email.service.EmailService;
import com.kitty.system.game.house.service.HouseService;
import com.kitty.system.game.general.service.GeneralService;
import com.kitty.system.game.role.service.SettingService;
import com.kitty.system.game.welfare.service.WelfareService;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import com.kitty.system.game.tianfu.service.RoleDayInCeService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.partner.service.PartnerService;
import java.util.Iterator;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import java.io.Serializable;
import com.kitty.system.game.player.bean.PlayerRoles;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.role.message.RespExistedRoleList;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.RoleInfo;
import org.apache.mina.core.session.IoSession;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
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
        this.playerProfiles = new ConcurrentHashMap<Long, PlayerProfile>();
        this.name2roleUid = new ConcurrentHashMap<String, Long>();
        this.roleId2roleUid = new ConcurrentHashMap<Integer, Long>();
    }
    
    public PlayerProfile queryPlayerProfile(final long playerId) {
        return this.playerProfiles.get(playerId);
    }
    
    public Role saveToCache(final Role role) {
        this.saveSync(role);
        return role;
    }
    
    public void saveSync(final Role role) {
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
        final String sql = "select id, s_id, role_id, `name`, icon, serial, `level`, `exp`, relive, race, enter_time, of_line_time, sex, faction, extend_box_json, marking from role";
        final List<PlayerProfile> profileList = this.jdbcTemplate.query(sql, (RowMapper)new PlayerRowMapper());
        this.logger.info("玩家数量=={}", (Object)profileList.size());
        profileList.forEach(p -> this.addPlayerProfile(p));
    }
    
    public List<RoleInfo> listRoleInfos(final String sid, final String marking, final IoSession session) {
        final List<RoleInfo> list = new ArrayList<RoleInfo>();
        final List<PlayerProfile> profiles = this.getPlayersBy(sid);
        for (int i = 0; i < profiles.size(); ++i) {
            final PlayerProfile playerProfile = profiles.get(i);
            if (marking == null) {
                final RoleInfo info = new RoleInfo(playerProfile);
                list.add(info);
            }
            else if (marking.equals(playerProfile.getMarking())) {
                final RoleInfo info = new RoleInfo(playerProfile);
                list.add(info);
            }
        }
        if (session != null) {
            final RespExistedRoleList respExistedRoleList = new RespExistedRoleList();
            respExistedRoleList.setOpenServerTime(new Long(System.currentTimeMillis() / 1000L).intValue());
            respExistedRoleList.setList(list);
            respExistedRoleList.setAccount_online(1);
            MessagePusher.pushMessage(session, respExistedRoleList);
        }
        return list;
    }
    
    public void updateRoleName(final String oldName, final Role role) {
        final PlayerProfile playerProfile = this.playerProfiles.get(role.getId());
        if (playerProfile == null) {
            return;
        }
        this.name2roleUid.remove(oldName);
        this.name2roleUid.put(role.getName(), role.getId());
        playerProfile.setName(role.getName());
    }
    
    public void updateRole(final Role role) {
        final PlayerProfile playerProfile = this.playerProfiles.get(role.getId());
        if (playerProfile == null) {
            return;
        }
        final PlayerProfile profile = new PlayerProfile(role);
        this.playerProfiles.replace(role.getId(), profile);
    }
    
    public boolean occupyName(final String roleName, final long id) {
        return this.name2roleUid.putIfAbsent(roleName, id) == null;
    }
    
    public void addPlayerProfile(final PlayerProfile baseInfo) {
        this.playerProfiles.put(baseInfo.getId(), baseInfo);
        this.name2roleUid.put(baseInfo.getName(), baseInfo.getId());
        this.roleId2roleUid.put(baseInfo.getRoleId(), baseInfo.getId());
        this.cacheRoles(baseInfo.getSid(), baseInfo.getId());
    }
    
    public void cacheRoles(final String sid, final Long id) {
        PlayerRoles playerRoles = this.redisService.get(sid + "_sid", PlayerRoles.class);
        if (playerRoles == null) {
            playerRoles = new PlayerRoles();
            final List<Role> roles = this.dao.query(Role.class, Cnd.where("s_id", "=", sid));
            for (final Role role : roles) {
                playerRoles.getList().add(role.getId());
            }
        }
        playerRoles.getList().add(id);
        this.redisService.set(sid + "_sid", playerRoles, 0L);
    }
    
    public List<PlayerProfile> getPlayersBy(final String sid) {
        final List<PlayerProfile> list = new ArrayList<PlayerProfile>();
        final PlayerProfile playerProfile = this.redisService.get(sid, PlayerProfile.class);
        final List<Role> roles = this.dao.query(Role.class, Cnd.where("s_id", "=", sid));
        for (final Role role : roles) {
            list.add(new PlayerProfile(role));
        }
        if (playerProfile != null) {
            final PlayerProfile profile = list.stream().filter(e -> e.getId().equals(playerProfile.getId())).findFirst().orElse(null);
            if (profile == null) {
                list.add(playerProfile);
            }
        }
        return list;
    }
    
    public Role getPlayerBy(final long id) {
        if (id <= 0L) {
            return null;
        }
        final Role role = (Role)this.dao.fetch(Role.class, Cnd.where("id", "=", id));
        if (role != null) {
            SpringUtils.getBean(PartnerService.class).loadPartner(role);
            role.doAfterInit();
            this.loadRoleRests(role);
            if (!DataCache.ONLINE_ROLES.containsKey(id)) {
                role.setState(2);
                SpringUtils.getRoleService().addOffline(role, false);
            }
        }
        return role;
    }
    
    public void loadRoleRests(final Role role) {
        this.equipService.selectEffect(role);
        this.equipService.selectWearEquip(role);
        this.cardService.getCard(role);
        this.cardService.getCardExercise(role);
        this.bagService.getBagGoods(role);
        this.bagService.getPetBagEquip(role.getId());
        this.bagService.getBagEquip(role);
        this.storeService.getStoreGoods(role);
        this.storeService.getStoreEquip(role);
        SpringUtils.getBean(RoleDayInCeService.class).getDataForRoleId(role.getId());
        SpringUtils.getBean(RoleTianFuService.class).getDataByRoleId(role.getId());
        SpringUtils.getHorseService().getHorse(role);
        SpringUtils.getBean(WelfareService.class).getGiftBagRecord(role);
        SpringUtils.getPetService().selectPet(role);
        SpringUtils.getBean(PartnerService.class).loadPartner(role);
        SpringUtils.getFriendService().loadPal(role);
        SpringUtils.getBean(SettingService.class).getSetting(role);
        SpringUtils.getBean(GeneralService.class).loadDomesticate(role);
        SpringUtils.getBean(HouseService.class).getHouse(role.getRoleId());
        SpringUtils.getBean(EmailService.class).loadRoleEmail(role);
    }
    
    public void onPlayerLogout(final Role role) {
        SceneManager.INSTANCE.leaveMap(role.getPos().getMapId(), role);
        EventDispatcher.getInstance().fireEvent(new LogoutSelfEvent(EventType.LOGOUT_SELF, role));
        EventDispatcher.getInstance().fireEvent(new RoleExpireEvent(EventType.ROLE_EXPIRE, role));
        EventDispatcher.getInstance().fireEvent(new LogoutEvent(EventType.LOGOUT, role));
        SessionManager.INSTANCE.unRegisterSession(role.getRoleId());
    }
    
    public long getUidBy(final int roleId) {
        return this.roleId2roleUid.getOrDefault(roleId, 0L);
    }
    
    public long getUidBy(final String name) {
        return this.name2roleUid.getOrDefault(name, 0L);
    }
    
    private class PlayerRowMapper implements RowMapper<PlayerProfile>
    {
        public PlayerProfile mapRow(final ResultSet re, final int arg1) throws SQLException {
            return new PlayerProfile(re.getString("s_id"), re.getInt("role_id"), re.getLong("id"), re.getString("name"), re.getInt("icon"), re.getInt("serial"), re.getInt("level"), re.getLong("exp"), re.getInt("relive"), re.getInt("race"), re.getLong("enter_time"), re.getLong("of_line_time"), re.getInt("sex"), re.getInt("faction"), re.getString("extend_box_json"), re.getString("marking"));
        }
    }
}
