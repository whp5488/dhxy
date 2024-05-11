
package com.kitty.system.game.npc.service;

import java.util.regex.Matcher;
import com.kitty.system.game.dirtywords.WordDisposePool;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.npc.message.RespWorship;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.npc.domain.Mirror;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.npc.message.ReqWorship;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.enter.message.RespGeneralNotify;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
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
import com.kitty.mina.message.Message;
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
import com.kitty.system.game.npc.NpcDataPool;
import com.kitty.system.game.npc.domain.NpcContentParam;
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
    
    public void requestNpcContent(final Role role, final int npcId) {
        final Npc npc = this.mapService.getNpc(npcId);
        if (npc == null) {
            return;
        }
        if (!this.canClickNpc(role, npc)) {
            return;
        }
        final Consumer<NpcContentParam> npcContentHandler = NpcDataPool.npcContentHandlers.get(npc.getType());
        if (npcContentHandler != null) {
            npcContentHandler.accept(new NpcContentParam(role, npc));
            return;
        }
        final String content = this.getNpcContent(role, npc);
        this.sendNpcContent(role, npc, content, null);
    }
    
    public String getNpcContent(final Role role, final Npc npc) {
        final List<UnifiedValue> spectialButtons = this.getSpectialButtons(role, npc);
        if (spectialButtons == null) {
            return this.loadBut(role, npc);
        }
        return "";
    }
    
    public String loadBut(final Role role, final Npc npc) {
        final StringBuffer but = new StringBuffer();
        final Map<Integer, SysNpcTypeInfo> map = NpcDataPool.data_npc_type_info;
        final List<Integer> types = npc.getNpcTypes();
        for (final Integer type : types) {
            final SysNpcTypeInfo typeInfo = map.get(type);
            if (typeInfo != null && role.getRelive() >= typeInfo.getZs() && role.getLevel() >= typeInfo.getLv()) {
                but.append(typeInfo.getName()).append("|");
            }
        }
        if (but.length() <= 0) {
            return but.toString();
        }
        return but.deleteCharAt(but.length() - 1).toString();
    }
    
    private List<UnifiedValue> getSpectialButtons(final Role role, final Npc npc) {
        if (NpcDataPool.spetialButtons.containsKey(npc.getId())) {
            final List<Function<Role, List<UnifiedValue>>> functions = NpcDataPool.spetialButtons.get(npc.getId());
            for (final Function<Role, List<UnifiedValue>> function : functions) {
                final List<UnifiedValue> list = function.apply(role);
                if (list.size() > 0) {
                    return list;
                }
            }
        }
        return null;
    }
    
    public void clickNpcButton(final Role role, final int npcId, final String msg, final String para) {
        final RespNpcLeave respNpcLeave = new RespNpcLeave();
        respNpcLeave.setNpcId(npcId);
        SpringUtils.getBean(TeamService.class).pushMessage(role, respNpcLeave);
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
    }
    
    private void handleButtonChoose(final Role role, final Npc npc, final String msg, final String para) {
        final Consumer<NpcButtonParam> npcButtonHandler = NpcDataPool.npcButtonHandlers.get(msg);
        if (npcButtonHandler != null) {
            npcButtonHandler.accept(new NpcButtonParam(role, npc, msg, "1".equals(para)));
        }
    }
    
    public void sendNpcContent(final Role role, final Npc npc, final int i18nId) {
        final String content = I18nIdDataPool.getI18nContent(i18nId);
        this.sendNpcContent(role, npc, content, null);
    }
    
    public void sendNpcContent(final Role role, final Npc npc, final int i18nId, final Object... args) {
        final String content = I18nIdDataPool.getI18nContent(i18nId, args);
        this.sendNpcContent(role, npc, content, null);
    }
    
    public void sendNpcContent(final Role role, final Npc npc, final String content, final String talk) {
        if (npc != null) {
            this.sendRespNpcContent(role, this.getRespNpcContent(role, npc, content, talk));
        }
    }
    
    public void sendRespNpcContent(final Role role, final RespNpcContent respNpcContent) {
        if (respNpcContent.getContent() != null) {
            MessagePusher.pushMessage(role, respNpcContent);
        }
        else {
            SpringUtils.getBean(TeamService.class).pushMessage(role, respNpcContent);
        }
    }
    
    public RespNpcContent getRespNpcContent(final Role role, final Npc npc, final String content, final String talk) {
        final RespNpcContent respNpcContent = new RespNpcContent();
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
    
    public void conditionUp(final Npc npc, final RespNpcContent respNpcContent) {
        if (npc.getClassify() > 0) {
            switch (npc.getClassify()) {
                case 10017: {
                    final String day = DateUtils.dayForWeek(new Date());
                    if (!ActivityType.XM_DUGEON.getPeriod().contains(day)) {
                        respNpcContent.setContent(StringUtils.deleteStringFront(respNpcContent.getContent(), "|", 1));
                        break;
                    }
                    break;
                }
                case 3001: {
                    final GangWarsHandler bean = SpringUtils.getBean(GangWarsHandler.class);
                    final GangWarsData objData = bean.getObjData();
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
    
    public void npcButton(final Role role, final String msg, final Npc npc) {
        switch (msg) {
            case "buy_npc_pet": {
                this.shopService.sendNpcPet(role, npc.getId());
                break;
            }
            case "buy_npc_drug": {
                this.shopService.sendNpcDrug(role, npc.getId());
                break;
            }
            case "buy_npc_equip": {
                this.shopService.sendNpcEquip(role, npc.getId());
                break;
            }
            case "buy_npc_grocery": {
                this.shopService.sendNpcGrocery(role, npc.getId());
                break;
            }
            case "buy_npc_guo": {
                this.shopService.sendNpcGuo(role, npc.getId());
                break;
            }
            case "buy_npc_brac": {
                this.shopService.sendNpcBrac(role, npc.getId());
                break;
            }
            case "buy_npc_nao": {
                this.shopService.sendNpcNao(role, npc.getId());
                break;
            }
            case "buy_npc_di": {
                this.shopService.sendNpcDi(role, npc.getId());
                break;
            }
            case "buy_npc_arena": {
                this.shopService.sendNpcArena(role, npc.getId());
                break;
            }
            case "buy_npc_long": {
                this.shopService.sendNpcLong(role, npc.getId());
                break;
            }
            case "buy_npc_nice": {
                this.shopService.sendNpcNice(role, npc.getId());
                break;
            }
            case "buy_npc_feats": {
                this.shopService.sendNpcFeats(role, npc.getId());
                break;
            }
        }
    }
    
    public boolean canClickNpc(final Role role, final Npc npc) {
        if (Arrays.asList(NpcButton.buttons).contains(npc.getShipper())) {
            return true;
        }
        final TeamService teamService = SpringUtils.getTeamService();
        if (!teamService.isInTeam(role)) {
            if (npc.getMap() <= 0) {
                return true;
            }
            if (!role.getPos().getMapId().equals(npc.getMap())) {
                MessagePusher.pushMessage(role, new RespMsg("当前位置距离NPC过远..."));
                return false;
            }
            return DataGlobalFunc.visual(role.getPos().getX(), role.getPos().getY(), npc.getX(), npc.getY(), 50.0);
        }
        else {
            final Team team = teamService.getTeam(role.getRoleId());
            if (!team.isLeader(role.getRoleId())) {
                MessagePusher.pushMessage(role, new RespMsg("既然是集体行动，还是老老实实听从队长的指挥啦。"));
                return false;
            }
            return npc.getMap() <= 0 || (role.getPos().getMapId().equals(npc.getMap()) && DataGlobalFunc.visual(role.getPos().getX(), role.getPos().getY(), npc.getX(), npc.getY(), 50.0));
        }
    }
    
    public void openDlg(final Role role, final String dlgName) {
        final RespGeneralNotify respGeneralNotify = new RespGeneralNotify();
        respGeneralNotify.setNotify(97);
        respGeneralNotify.setValue(dlgName);
        MessagePusher.pushMessage(role, respGeneralNotify);
    }
    
    public void closeDlg(final Role role, final String dlgName) {
        final RespGeneralNotify respGeneralNotify = new RespGeneralNotify();
        respGeneralNotify.setNotify(98);
        respGeneralNotify.setValue(dlgName);
        MessagePusher.pushMessage(role, respGeneralNotify);
    }
    
    public void worship(final IoSession session, final ReqWorship reqWorship) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final Role player = this.roleService.getPlayer(reqWorship.getNpcId());
        if (player == null) {
            return;
        }
        final Mirror mirror = DataCache.MIRROR_MAP.get(player.getId());
        if (mirror == null) {
            return;
        }
        MessagePusher.pushMessage(session, new RespWorship(reqWorship.getNpcId(), player.getName(), mirror.getMotto()));
    }
    
    public void motto(final IoSession session, String motto) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final Mirror mirror = DataCache.MIRROR_MAP.get(role.getId());
        if (mirror == null) {
            return;
        }
        if (motto.length() > 6) {
            MessagePusher.pushMessage(session, new RespMsg("内容过长"));
            MessagePusher.pushMessage(session, new RespHint(25, (short)0));
            return;
        }
        motto = CheckCharUtils.replaceName(motto);
        final List<String> set = WordDisposePool.checkDirtyWords(motto);
        if (set.size() >= 1 || CheckCharUtils.checkQQNumber(motto)) {
            MessagePusher.pushMessage(session, new RespMsg("内容存在非法字符。"));
            MessagePusher.pushMessage(session, new RespHint(25, (short)0));
            return;
        }
        final Matcher matcher = CheckCharUtils.CHECK_EMOJI.matcher(motto);
        if (matcher.find()) {
            MessagePusher.pushMessage(session, new RespMsg("内容存在非法字符。"));
            MessagePusher.pushMessage(session, new RespHint(25, (short)0));
            return;
        }
        MessagePusher.pushMessage(session, new RespHint(25, (short)1));
        mirror.setMotto(motto);
        mirror.save();
    }
}
