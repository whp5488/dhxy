package com.kitty.common.utils.spring;

import java.util.Map;
import org.springframework.beans.BeansException;
import javax.annotation.PostConstruct;
import com.kitty.system.init.service.InitService;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.kitty.system.queue.BroadcastThread;
import com.kitty.system.game.market.service.MarketService;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.system.game.friend.service.FriendService;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.mind.service.MindService;
import com.kitty.system.game.horse.service.HorseService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.account.service.AccountService;
import com.kitty.cross.core.callback.CallBackService;
import com.kitty.system.game.player.PlayerService;
import com.kitty.common.identity.IdentityService;
import javax.annotation.Resource;
import org.nutz.dao.Dao;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContextAware;

@Component
public class SpringUtils implements ApplicationContextAware
{
    private static ApplicationContext applicationContext;
    private static SpringUtils self;
    @Resource
    private Dao dao;
    @Resource
    private IdentityService identityService;
    @Resource
    private PlayerService playerService;
    @Resource
    private CallBackService callBackService;
    @Resource
    private AccountService accountService;
    @Resource
    private RoleService roleService;
    @Resource
    private EquipService equipService;
    @Resource
    private PetService petService;
    @Resource
    private HorseService horseService;
    @Resource
    private MindService mindService;
    @Resource
    private ChatService chatService;
    @Resource
    private TeamService teamService;
    @Resource
    private PartyService partyService;
    @Resource
    private FriendService friendService;
    @Resource
    private ActivityService activityService;
    @Resource
    private FightNewService fightNewService;
    @Resource
    private FightMessageNewService fightMessageNewService;
    @Resource
    private MapService mapService;
    @Resource
    private BroadcastService broadcastService;
    @Resource
    private MarketService marketService;
    @Resource
    private BroadcastThread broadcastThread;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private InitService initService;
    
    public SpringUtils() {
    }
    
    @PostConstruct
    private void init() {
        SpringUtils.self = this;
    }
    
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }
    
    public static ApplicationContext getApplicationContext() {
        return SpringUtils.applicationContext;
    }
    
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }
    
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
    
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
    
    public static <T> Map<String, T> getBeansOfType(Class<T> var1) {
        return getApplicationContext().getBeansOfType(var1);
    }
    
    public static final Dao getDao() {
        return SpringUtils.self.dao;
    }
    
    public static final IdentityService getIdentityService() {
        return SpringUtils.self.identityService;
    }
    
    public static final PlayerService getPlayerService() {
        return SpringUtils.self.playerService;
    }
    
    public static final CallBackService getCallBackService() {
        return SpringUtils.self.callBackService;
    }
    
    public static final AccountService getAccountService() {
        return SpringUtils.self.accountService;
    }
    
    public static final RoleService getRoleService() {
        return SpringUtils.self.roleService;
    }
    
    public static final EquipService getEquipService() {
        return SpringUtils.self.equipService;
    }
    
    public static final PetService getPetService() {
        return SpringUtils.self.petService;
    }
    
    public static final HorseService getHorseService() {
        return SpringUtils.self.horseService;
    }
    
    public static final MindService getMindService() {
        return SpringUtils.self.mindService;
    }
    
    public static final ChatService getChatService() {
        return SpringUtils.self.chatService;
    }
    
    public static final TeamService getTeamService() {
        return SpringUtils.self.teamService;
    }
    
    public static final PartyService getPartyService() {
        return SpringUtils.self.partyService;
    }
    
    public static final FriendService getFriendService() {
        return SpringUtils.self.friendService;
    }
    
    public static final ActivityService getActivityService() {
        return SpringUtils.self.activityService;
    }
    
    public static final FightNewService getFightNewService() {
        return SpringUtils.self.fightNewService;
    }
    
    public static final FightMessageNewService getFightMessageNewService() {
        return SpringUtils.self.fightMessageNewService;
    }
    
    public static final MapService getMapService() {
        return SpringUtils.self.mapService;
    }
    
    public static final BroadcastService getBroadcastService() {
        return SpringUtils.self.broadcastService;
    }
    
    public static final MarketService getMarketService() {
        return SpringUtils.self.marketService;
    }
    
    public static final BroadcastThread getBroadcastThread() {
        return SpringUtils.self.broadcastThread;
    }
    
    public static final MongoTemplate getMongoTemplate() {
        return SpringUtils.self.mongoTemplate;
    }
    
    public static final InitService getInitService() {
        return SpringUtils.self.initService;
    }
}
