package com.kitty.system.game.npc.service;

import com.kitty.common.utils.CheckCharUtils;
import com.kitty.common.utils.DateUtils;
import com.kitty.common.utils.StringUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.activity.model.user.wars.GangWarsData;
import com.kitty.system.game.dirtywords.WordDisposePool;
import com.kitty.system.game.enter.message.RespGeneralNotify;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.npc.NpcDataPool;
import com.kitty.system.game.npc.domain.Mirror;
import com.kitty.system.game.npc.domain.NpcButtonParam;
import com.kitty.system.game.npc.domain.NpcContentParam;
import com.kitty.system.game.npc.interfaces.NpcButton;
import com.kitty.system.game.npc.message.ReqWorship;
import com.kitty.system.game.npc.message.RespNpcContent;
import com.kitty.system.game.npc.message.RespNpcLeave;
import com.kitty.system.game.npc.message.RespWorship;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.shop.service.ShopService;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.UnifiedValue;
import com.kitty.system.init.domain.SysNpcTypeInfo;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NpcService {
  private static final double DISTANCE = 50.0D;
  
  @Autowired
  public ShopService shopService;
  
  @Autowired
  public RoleService roleService;
  
  @Autowired
  public MapService mapService;
  
  public void requestNpcContent(Role role, int npcId) {
    Npc npc = this.mapService.getNpc(npcId);
    if (npc == null)
      return; 
    if (!canClickNpc(role, npc))
      return; 
    Consumer<NpcContentParam> npcContentHandler = (Consumer<NpcContentParam>)NpcDataPool.npcContentHandlers.get(Integer.valueOf(npc.getType()));
    if (npcContentHandler != null) {
      npcContentHandler.accept(new NpcContentParam(role, npc));
      return;
    } 
    String content = getNpcContent(role, npc);
    sendNpcContent(role, npc, content, (String)null);
  }
  
  public String getNpcContent(Role role, Npc npc) {
    List<UnifiedValue> spectialButtons = getSpectialButtons(role, npc);
    if (spectialButtons == null)
      return loadBut(role, npc); 
    return "";
  }
  
  public String loadBut(Role role, Npc npc) {
    StringBuffer but = new StringBuffer();
    Map<Integer, SysNpcTypeInfo> map = NpcDataPool.data_npc_type_info;
    List<Integer> types = npc.getNpcTypes();
    for (Integer type : types) {
      SysNpcTypeInfo typeInfo = map.get(type);
      if (typeInfo != null && 
        role.getRelive().intValue() >= typeInfo.getZs().intValue() && role.getLevel().intValue() >= typeInfo.getLv().intValue())
        but.append(typeInfo.getName()).append("|"); 
    } 
    if (but.length() <= 0)
      return but.toString(); 
    return but.deleteCharAt(but.length() - 1).toString();
  }
  
  private List<UnifiedValue> getSpectialButtons(Role role, Npc npc) {
    if (NpcDataPool.spetialButtons.containsKey(Integer.valueOf(npc.getId()))) {
      List<Function<Role, List<UnifiedValue>>> functions = (List<Function<Role, List<UnifiedValue>>>)NpcDataPool.spetialButtons.get(Integer.valueOf(npc.getId()));
      for (Function<Role, List<UnifiedValue>> function : functions) {
        List<UnifiedValue> list = function.apply(role);
        if (list.size() > 0)
          return list; 
      } 
    } 
    return null;
  }
  
  public void clickNpcButton(Role role, int npcId, String msg, String para) {
    RespNpcLeave respNpcLeave = new RespNpcLeave();
    respNpcLeave.setNpcId(npcId);
    ((TeamService)SpringUtils.getBean(TeamService.class)).pushMessage(role, (Message)respNpcLeave);
    if (msg == null)
      return; 
    Npc npc = null;
    if (!NpcDataPool.itemButtons.contains(msg)) {
      npc = this.mapService.getNpc(npcId);
      if (npc == null)
        return; 
      if (!canClickNpc(role, npc))
        return; 
    } 
    handleButtonChoose(role, npc, msg, para);
  }
  
  private void handleButtonChoose(Role role, Npc npc, String msg, String para) {
    Consumer<NpcButtonParam> npcButtonHandler = (Consumer<NpcButtonParam>)NpcDataPool.npcButtonHandlers.get(msg);
    if (npcButtonHandler != null) {
      npcButtonHandler.accept(new NpcButtonParam(role, npc, msg, "1".equals(para)));
      return;
    } 
  }
  
  public void sendNpcContent(Role role, Npc npc, int i18nId) {
    String content = I18nIdDataPool.getI18nContent(i18nId);
    sendNpcContent(role, npc, content, (String)null);
  }
  
  public void sendNpcContent(Role role, Npc npc, int i18nId, Object... args) {
    String content = I18nIdDataPool.getI18nContent(i18nId, args);
    sendNpcContent(role, npc, content, (String)null);
  }
  
  public void sendNpcContent(Role role, Npc npc, String content, String talk) {
    if (npc != null)
      sendRespNpcContent(role, getRespNpcContent(role, npc, content, talk)); 
  }
  
  public void sendRespNpcContent(Role role, RespNpcContent respNpcContent) {
    if (respNpcContent.getContent() != null) {
      MessagePusher.pushMessage(role, (Message)respNpcContent);
    } else {
      ((TeamService)SpringUtils.getBean(TeamService.class)).pushMessage(role, (Message)respNpcContent);
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
    } else {
      respNpcContent.setTalk(npc.getTalk());
    } 
    conditionUp(npc, respNpcContent);
    return respNpcContent;
  }
  
  public void conditionUp(Npc npc, RespNpcContent respNpcContent) {
    if (npc.getClassify() > 0) {
      String day;
      GangWarsHandler bean;
      GangWarsData objData;
      switch (npc.getClassify()) {
        case 10017:
          day = DateUtils.dayForWeek(new Date());
          if (!ActivityType.XM_DUGEON.getPeriod().contains(day))
            respNpcContent.setContent(StringUtils.deleteStringFront(respNpcContent.getContent(), "|", 1)); 
          break;
        case 3001:
          bean = (GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class);
          objData = bean.getObjData();
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
  
  public void npcButton(Role role, String msg, Npc npc) {
    switch (msg) {
      default:
        return;
      case "buy_npc_pet":
        this.shopService.sendNpcPet(role, Integer.valueOf(npc.getId()));
      case "buy_npc_drug":
        this.shopService.sendNpcDrug(role, Integer.valueOf(npc.getId()));
      case "buy_npc_equip":
        this.shopService.sendNpcEquip(role, Integer.valueOf(npc.getId()));
      case "buy_npc_grocery":
        this.shopService.sendNpcGrocery(role, Integer.valueOf(npc.getId()));
      case "buy_npc_guo":
        this.shopService.sendNpcGuo(role, Integer.valueOf(npc.getId()));
      case "buy_npc_brac":
        this.shopService.sendNpcBrac(role, Integer.valueOf(npc.getId()));
      case "buy_npc_nao":
        this.shopService.sendNpcNao(role, Integer.valueOf(npc.getId()));
      case "buy_npc_di":
        this.shopService.sendNpcDi(role, Integer.valueOf(npc.getId()));
      case "buy_npc_arena":
        this.shopService.sendNpcArena(role, Integer.valueOf(npc.getId()));
      case "buy_npc_long":
        this.shopService.sendNpcLong(role, Integer.valueOf(npc.getId()));
      case "buy_npc_nice":
        this.shopService.sendNpcNice(role, Integer.valueOf(npc.getId()));
      case "buy_npc_feats":
        break;
    } 
    this.shopService.sendNpcFeats(role, Integer.valueOf(npc.getId()));
  }
  
  public boolean canClickNpc(Role role, Npc npc) {
    if (Arrays.<String>asList(NpcButton.buttons).contains(npc.getShipper()))
      return true; 
    TeamService teamService = SpringUtils.getTeamService();
    if (!teamService.isInTeam(role)) {
      if (npc.getMap() > 0) {
        if (!role.getPos().getMapId().equals(Integer.valueOf(npc.getMap()))) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("当前位置距离NPC过远..."));
          return false;
        } 
        return DataGlobalFunc.visual(role.getPos().getX().intValue(), role.getPos().getY().intValue(), npc.getX(), npc.getY(), 50.0D);
      } 
      return true;
    } 
    Team team = teamService.getTeam(role.getRoleId().intValue());
    if (!team.isLeader(role.getRoleId().intValue())) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("既然是集体行动，还是老老实实听从队长的指挥啦。"));
      return false;
    } 
    if (npc.getMap() > 0) {
      if (!role.getPos().getMapId().equals(Integer.valueOf(npc.getMap())))
        return false; 
      return DataGlobalFunc.visual(role.getPos().getX().intValue(), role.getPos().getY().intValue(), npc.getX(), npc.getY(), 50.0D);
    } 
    return true;
  }
  
  public void openDlg(Role role, String dlgName) {
    RespGeneralNotify respGeneralNotify = new RespGeneralNotify();
    respGeneralNotify.setNotify(97);
    respGeneralNotify.setValue(dlgName);
    MessagePusher.pushMessage(role, (Message)respGeneralNotify);
  }
  
  public void closeDlg(Role role, String dlgName) {
    RespGeneralNotify respGeneralNotify = new RespGeneralNotify();
    respGeneralNotify.setNotify(98);
    respGeneralNotify.setValue(dlgName);
    MessagePusher.pushMessage(role, (Message)respGeneralNotify);
  }
  
  public void worship(IoSession session, ReqWorship reqWorship) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    Role player = this.roleService.getPlayer(reqWorship.getNpcId());
    if (player == null)
      return; 
    Mirror mirror = (Mirror)DataCache.MIRROR_MAP.get(player.getId());
    if (mirror == null)
      return; 
    MessagePusher.pushMessage(session, (Message)new RespWorship(reqWorship.getNpcId(), player.getName(), mirror.getMotto()));
  }
  
  public void motto(IoSession session, String motto) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    Mirror mirror = (Mirror)DataCache.MIRROR_MAP.get(role.getId());
    if (mirror == null)
      return; 
    if (motto.length() > 6) {
      MessagePusher.pushMessage(session, (Message)new RespMsg("内容过长"));
      MessagePusher.pushMessage(session, (Message)new RespHint(25, (short)0));
      return;
    } 
    motto = CheckCharUtils.replaceName(motto);
    List<String> set = WordDisposePool.checkDirtyWords(motto);
    if (set.size() >= 1 || CheckCharUtils.checkQQNumber(motto)) {
      MessagePusher.pushMessage(session, (Message)new RespMsg("内容存在非法字符。"));
      MessagePusher.pushMessage(session, (Message)new RespHint(25, (short)0));
      return;
    } 
    Matcher matcher = CheckCharUtils.CHECK_EMOJI.matcher(motto);
    if (matcher.find()) {
      MessagePusher.pushMessage(session, (Message)new RespMsg("内容存在非法字符。"));
      MessagePusher.pushMessage(session, (Message)new RespHint(25, (short)0));
      return;
    } 
    MessagePusher.pushMessage(session, (Message)new RespHint(25, (short)1));
    mirror.setMotto(motto);
    mirror.save();
  }
}
