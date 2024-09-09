package com.kitty.system.game.npc.service;

import java.util.regex.Matcher;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.dirtywords.WordDisposePool;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.system.game.npc.message.RespWorship;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.npc.domain.Mirror;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.npc.message.ReqWorship;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.enter.message.RespGeneralNotify;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.data.DataGlobalFunc;
import java.util.Arrays;
import com.kitty.system.game.npc.interfaces.NpcButton;
import com.kitty.system.game.activity.model.user.wars.GangWarsData;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.common.utils.StringUtils;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.common.utils.DateUtils;
import java.util.Date;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.npc.message.RespNpcContent;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.npc.domain.NpcButtonParam;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.npc.message.RespNpcLeave;
import java.util.function.Function;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.init.domain.SysNpcTypeInfo;
import com.kitty.system.game.vo.UnifiedValue;
import java.util.List;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.npc.domain.NpcContentParam;
import com.kitty.system.game.npc.NpcDataPool;
import java.util.function.Consumer;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.shop.service.ShopService;
import org.springframework.stereotype.Service;

@Service
public class NpcService
{
    private static final double DISTANCE = 50.0;
    @Autowired
    public ShopService shopService;
    @Autowired
    public RoleService roleService;
    @Autowired
    public MapService mapService;
    
    public NpcService() {
    }
    
    public void requestNpcContent(Role role, int npcId) {
        Npc npc = this.mapService.getNpc(npcId);
        if (npc == null) {
            return;
        }
        if (!this.canClickNpc(role, npc)) {
            return;
        }
        Consumer<NpcContentParam> npcContentHandler = (Consumer)NpcDataPool.npcContentHandlers.get(Integer.valueOf(npc.getType()));
        if (npcContentHandler != null) {
            npcContentHandler.accept(new NpcContentParam(role, npc));
            return;
        }
        String content = this.getNpcContent(role, npc);
        this.sendNpcContent(role, npc, content, null);
        return;
    }
    
    public String getNpcContent(Role role, Npc npc) {
        List<UnifiedValue> spectialButtons = this.getSpectialButtons(role, npc);
        if (spectialButtons == null) {
            return this.loadBut(role, npc);
        }
        return "";
    }
    
    public String loadBut(Role role, Npc npc) {
        StringBuffer but = new StringBuffer();
        Map<Integer, SysNpcTypeInfo> map = NpcDataPool.data_npc_type_info;
        List<Integer> types = npc.getNpcTypes();
        for (Integer type : types) {
            SysNpcTypeInfo typeInfo = (SysNpcTypeInfo)map.get(type);
            if (typeInfo != null && (int)role.getRelive() >= (int)typeInfo.getZs() && (int)role.getLevel() >= (int)typeInfo.getLv()) {
                but.append(typeInfo.getName()).append("|");
            }
            else {
                continue;
            }
        }
        if (but.length() <= 0) {
            return but.toString();
        }
        return but.deleteCharAt(but.length() - 1).toString();
    }
    
    private List<UnifiedValue> getSpectialButtons(Role role, Npc npc) {
        if (NpcDataPool.spetialButtons.containsKey(Integer.valueOf(npc.getId()))) {
            List<Function<Role, List<UnifiedValue>>> functions = (List)NpcDataPool.spetialButtons.get(Integer.valueOf(npc.getId()));
            for (Function<Role, List<UnifiedValue>> function : functions) {
                List<UnifiedValue> list = (List)function.apply(role);
                if (list.size() > 0) {
                    return list;
                }
                continue;
            }
        }
        return null;
    }
    
    public void clickNpcButton(Role role, int npcId, String msg, String para) {
        RespNpcLeave respNpcLeave = new RespNpcLeave();
        respNpcLeave.setNpcId(npcId);
        ((TeamService)SpringUtils.getBean(TeamService.class)).pushMessage(role, respNpcLeave);
        if (msg == null) {
            return;
        }
        Npc npc = null;
        if (!NpcDataPool.itemButtons.contains(msg)) {
            npc = this.mapService.getNpc(npcId);
            if (npc == null) {
                return;
            }
            if (!this.canClickNpc(role, npc)) {
                return;
            }
        }
        this.handleButtonChoose(role, npc, msg, para);
        return;
    }
    
    private void handleButtonChoose(Role role, Npc npc, String msg, String para) {
        Consumer<NpcButtonParam> npcButtonHandler = (Consumer)NpcDataPool.npcButtonHandlers.get(msg);
        if (npcButtonHandler != null) {
            npcButtonHandler.accept(new NpcButtonParam(role, npc, msg, "1".equals(para)));
            return;
        }
        return;
    }
    
    public void sendNpcContent(Role role, Npc npc, int i18nId) {
        String content = I18nIdDataPool.getI18nContent(i18nId);
        this.sendNpcContent(role, npc, content, null);
    }
    
    public void sendNpcContent(Role role, Npc npc, int i18nId, Object... args) {
        String content = I18nIdDataPool.getI18nContent(i18nId, args);
        this.sendNpcContent(role, npc, content, null);
    }
    
    public void sendNpcContent(Role role, Npc npc, String content, String talk) {
        if (npc != null) {
            this.sendRespNpcContent(role, this.getRespNpcContent(role, npc, content, talk));
        }
    }
    
    public void sendRespNpcContent(Role role, RespNpcContent respNpcContent) {
        if (respNpcContent.getContent() != null) {
            MessagePusher.pushMessage(role, respNpcContent);
        }
        else {
            ((TeamService)SpringUtils.getBean(TeamService.class)).pushMessage(role, respNpcContent);
        }
    }
    
    public RespNpcContent getRespNpcContent(Role role, Npc npc, String content, String talk) {
        RespNpcContent respNpcContent = new RespNpcContent();
        respNpcContent.setId(npc.getId());
        respNpcContent.setNpcName(npc.getName());
        respNpcContent.setContent(content);
        respNpcContent.setIcon(npc.getIcon());
        if (talk != null && talk.length() > 1) {
            respNpcContent.setTalk(talk);
        }
        else {
            respNpcContent.setTalk(npc.getTalk());
        }
        this.conditionUp(npc, respNpcContent);
        return respNpcContent;
    }
    
    public void conditionUp(Npc npc, RespNpcContent respNpcContent) {
        if (npc.getClassify() > 0) {
            switch (npc.getClassify()) {
                case 10017: {
                    String day = DateUtils.dayForWeek(new Date());
                    if (!ActivityType.XM_DUGEON.getPeriod().contains(day)) {
                        respNpcContent.setContent(StringUtils.deleteStringFront(respNpcContent.getContent(), "|", 1));
                        break;
                    }
                    break;
                }
                case 3001: {
                    GangWarsHandler bean = (GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class);
                    GangWarsData objData = bean.getObjData();
                    if (objData != null && objData.getStage() == 1) {
                        respNpcContent.setTalk(StringUtils.deleteStringFront(respNpcContent.getTalk(), "|", 1));
                        break;
                    }
                    respNpcContent.setTalk(StringUtils.deleteStringAfter(respNpcContent.getTalk(), "|"));
                    respNpcContent.setContent(StringUtils.deleteStringFront(respNpcContent.getContent(), "|", 1));
                    break;
                }
            }
        }
    }
    
    public void npcButton(Role role, String msg, Npc npc) {
        int n = -1;
        switch (msg.hashCode()) {
            case -978226680: {
                if (msg.equals("buy_npc_pet")) {
                    n = 1;
                    break;
                }
                break;
            }
            case -260600873: {
                if (msg.equals("buy_npc_drug")) {
                    n = 2;
                    break;
                }
                break;
            }
            case 512201433: {
                if (msg.equals("buy_npc_equip")) {
                    n = 3;
                    break;
                }
                break;
            }
            case 102247798: {
                if (msg.equals("buy_npc_grocery")) {
                    n = 4;
                    break;
                }
                break;
            }
            case -978234838: {
                if (msg.equals("buy_npc_guo")) {
                    n = 5;
                    break;
                }
                break;
            }
            case -260661079: {
                if (msg.equals("buy_npc_brac")) {
                    n = 6;
                    break;
                }
                break;
            }
            case -978228731: {
                if (msg.equals("buy_npc_nao")) {
                    n = 7;
                    break;
                }
                break;
            }
            case -1001387396: {
                if (msg.equals("buy_npc_di")) {
                    n = 8;
                    break;
                }
                break;
            }
            case 508521904: {
                if (msg.equals("buy_npc_arena")) {
                    n = 9;
                    break;
                }
                break;
            }
            case -260365645: {
                if (msg.equals("buy_npc_long")) {
                    n = 10;
                    break;
                }
                break;
            }
            case -260312172: {
                if (msg.equals("buy_npc_nice")) {
                    n = 11;
                    break;
                }
                break;
            }
            case 512748586: {
                if (msg.equals("buy_npc_feats")) {
                    n = 12;
                    break;
                }
                break;
            }
        }
        switch (n) {
            case 1: {
                this.shopService.sendNpcPet(role, Integer.valueOf(npc.getId()));
                break;
            }
            case 2: {
                this.shopService.sendNpcDrug(role, Integer.valueOf(npc.getId()));
                break;
            }
            case 3: {
                this.shopService.sendNpcEquip(role, Integer.valueOf(npc.getId()));
                break;
            }
            case 4: {
                this.shopService.sendNpcGrocery(role, Integer.valueOf(npc.getId()));
                break;
            }
            case 5: {
                this.shopService.sendNpcGuo(role, Integer.valueOf(npc.getId()));
                break;
            }
            case 6: {
                this.shopService.sendNpcBrac(role, Integer.valueOf(npc.getId()));
                break;
            }
            case 7: {
                this.shopService.sendNpcNao(role, Integer.valueOf(npc.getId()));
                break;
            }
            case 8: {
                this.shopService.sendNpcDi(role, Integer.valueOf(npc.getId()));
                break;
            }
            case 9: {
                this.shopService.sendNpcArena(role, Integer.valueOf(npc.getId()));
                break;
            }
            case 10: {
                this.shopService.sendNpcLong(role, Integer.valueOf(npc.getId()));
                break;
            }
            case 11: {
                this.shopService.sendNpcNice(role, Integer.valueOf(npc.getId()));
                break;
            }
            case 12: {
                this.shopService.sendNpcFeats(role, Integer.valueOf(npc.getId()));
                break;
            }
            default: {
                break;
            }
        }
    }
    
    public boolean canClickNpc(Role role, Npc npc) {
        if (Arrays.asList(NpcButton.buttons).contains(npc.getShipper())) {
            return true;
        }
        TeamService teamService = SpringUtils.getTeamService();
        if (!teamService.isInTeam(role)) {
            if (npc.getMap() > 0) {
                if (!role.getPos().getMapId().equals(Integer.valueOf(npc.getMap()))) {
                    MessagePusher.pushMessage(role, new RespMsg("当前位置距离NPC过远..."));
                    return false;
                }
                return DataGlobalFunc.visual((int)role.getPos().getX(), (int)role.getPos().getY(), npc.getX(), npc.getY(), 50.0);
            }
            else {
                return true;
            }
        }
        else {
            Team team = teamService.getTeam((int)role.getRoleId());
            if (!team.isLeader((int)role.getRoleId())) {
                MessagePusher.pushMessage(role, new RespMsg("既然是集体行动，还是老老实实听从队长的指挥啦。"));
                return false;
            }
            return npc.getMap() <= 0 || (role.getPos().getMapId().equals(Integer.valueOf(npc.getMap())) && DataGlobalFunc.visual((int)role.getPos().getX(), (int)role.getPos().getY(), npc.getX(), npc.getY(), 50.0));
        }
    }
    
    public void openDlg(Role role, String dlgName) {
        RespGeneralNotify respGeneralNotify = new RespGeneralNotify();
        respGeneralNotify.setNotify(97);
        respGeneralNotify.setValue(dlgName);
        MessagePusher.pushMessage(role, respGeneralNotify);
    }
    
    public void closeDlg(Role role, String dlgName) {
        RespGeneralNotify respGeneralNotify = new RespGeneralNotify();
        respGeneralNotify.setNotify(98);
        respGeneralNotify.setValue(dlgName);
        MessagePusher.pushMessage(role, respGeneralNotify);
    }
    
    public void worship(IoSession session, ReqWorship reqWorship) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        Role player = this.roleService.getPlayer(reqWorship.getNpcId());
        if (player == null) {
            return;
        }
        Mirror mirror = (Mirror)DataCache.MIRROR_MAP.get(player.getId());
        if (mirror == null) {
            return;
        }
        MessagePusher.pushMessage(session, new RespWorship(reqWorship.getNpcId(), player.getName(), mirror.getMotto()));
        return;
    }
    
    public void motto(IoSession session, String motto) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        Mirror mirror = (Mirror)DataCache.MIRROR_MAP.get(role.getId());
        if (mirror == null) {
            return;
        }
        if (motto.length() > 6) {
            MessagePusher.pushMessage(session, new RespMsg("内容过长"));
            MessagePusher.pushMessage(session, new RespHint(25, (short)0));
            return;
        }
        motto = CheckCharUtils.replaceName(motto);
        List<String> set = WordDisposePool.checkDirtyWords(motto);
        if (set.size() >= 1 || CheckCharUtils.checkQQNumber(motto)) {
            MessagePusher.pushMessage(session, new RespMsg("内容存在非法字符。"));
            MessagePusher.pushMessage(session, new RespHint(25, (short)0));
            return;
        }
        Matcher matcher = CheckCharUtils.CHECK_EMOJI.matcher(motto);
        if (matcher.find()) {
            MessagePusher.pushMessage(session, new RespMsg("内容存在非法字符。"));
            MessagePusher.pushMessage(session, new RespHint(25, (short)0));
            return;
        }
        MessagePusher.pushMessage(session, new RespHint(25, (short)1));
        mirror.setMotto(motto);
        mirror.save();
        return;
    }
}
