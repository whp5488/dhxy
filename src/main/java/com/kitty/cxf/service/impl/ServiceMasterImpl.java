package com.kitty.cxf.service.impl;

import com.google.gson.Gson;
import com.kitty.common.core.SchedulerManager;
import com.kitty.common.start.GameServer;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cxf.common.MapStringUtil;
import com.kitty.cxf.model.Inquire;
import com.kitty.cxf.pool.CxfDataPool;
import com.kitty.cxf.send.service.GameSendService;
import com.kitty.cxf.service.ServiceMaster;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.record.RecordThread;
import com.kitty.system.admin.domain.BrieGift;
import com.kitty.system.admin.domain.BriePetfAcsry;
import com.kitty.system.admin.domain.BriefAcsry;
import com.kitty.system.admin.domain.BriefEmail;
import com.kitty.system.admin.domain.BriefHorse;
import com.kitty.system.admin.domain.BriefMagical;
import com.kitty.system.admin.domain.BriefPet;
import com.kitty.system.admin.domain.BriefSin;
import com.kitty.system.admin.service.AdminService;
import com.kitty.system.admin.service.GmService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.dirtywords.WordDisposePool;
import com.kitty.system.game.email.domain.EmailSystem;
import com.kitty.system.game.email.service.EmailService;
import com.kitty.system.game.market.service.MarketService;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.player.PlayerProfile;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.task.message.RespTasksDel;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.welfare.domain.GiftBag;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@WebService(serviceName = "ServiceMaster", targetNamespace = "http://my.com", endpointInterface = "com.kitty.cxf.service.ServiceMaster")
public class ServiceMasterImpl implements ServiceMaster {
  private final String SUCCESS = "0";
  
  private final String ERROR = "500";
  
  @Autowired
  Dao dao;
  
  @Autowired
  GameServer gameServer;
  
  @Autowired
  private AdminService adminService;
  
  @Autowired
  private RoleService roleService;
  
  @Autowired
  private BagService bagService;
  
  @Autowired
  private TaskService taskService;
  
  @Autowired
  private InitService initService;
  
  @Autowired
  private ServerService serverService;
  
  @Autowired
  private GameSendService gameSendService;
  
  @Autowired
  private MarketService marketService;
  
  @Autowired
  private GmService gmService;
  
  @Autowired
  private EmailService emailService;
  
  public String notice(String notice) {
    try {
      if (notice != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(notice);
        String msg = String.valueOf(toMap.get("msg"));
        Integer num = Integer.valueOf(String.valueOf(toMap.get("num")));
        Integer second = Integer.valueOf(String.valueOf(toMap.get("second")));
        this.adminService.asyncThreadNotice(msg, num, second);
        return result("0", "操作成功");
      } 
      return result("500", "未知操作");
    } catch (Exception e) {
      return result("500", "操作异常");
    } 
  }
  
  public String close(String close) {
    try {
      if (close != null) {
        this.serverService.setState(0);
        Map<String, Object> toMap = MapStringUtil.getStringToMap(close);
        int time = Integer.parseInt(String.valueOf(toMap.get("time")));
        SchedulerManager.getInstance().schedule(() -> {
              this.gameSendService.sendClose("操作关服");
              Runtime.getRuntime().exit(0);
            }, time * 1000L);
        return result("0", "开始进行关服");
      } 
      return result("500", "未知操作");
    } catch (Exception e) {
      this.gameSendService.sendClose("操作关服-异常");
      Runtime.getRuntime().exit(0);
      return result("500", "操作异常");
    } 
  }
  
  public String speak(String speak) {
    try {
      if (speak != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(speak);
        Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
        Integer type = Integer.valueOf(String.valueOf(toMap.get("type")));
        long day = Long.valueOf(String.valueOf(toMap.get("day")));
        Role target = this.roleService.getPlayer(roleId);
        if (type == 0) {
          this.adminService.shutChannel(target, day);
          MessagePusher.pushMessage(target, (Message)new RespNotifyMiscEx("你已被禁言!"));
          return result("0", "操作成功");
        } 
        this.adminService.shutChannel(target, 0L);
        MessagePusher.pushMessage(target, (Message)new RespNotifyMiscEx("你可以畅所欲言了!"));
        return result("0", "操作成功");
      } 
      return result("500", "未知操作");
    } catch (Exception e) {
      return result("500", "操作异常");
    } 
  }
  
  public String seal(String seal) {
    try {
      if (seal != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(seal);
        Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
        Integer type = Integer.valueOf(String.valueOf(toMap.get("type")));
        long day = Long.valueOf(String.valueOf(toMap.get("day")));
        Role target = this.roleService.getPlayer(roleId);
        if (type == 0) {
          this.adminService.blockPlayer(target, day, null);
          return result("0", "操作成功", target.getSid());
        } 
        this.adminService.blockPlayer(target, 0L, null);
        return result("0", "操作成功", target.getSid());
      } 
      return result("500", "未知操作");
    } catch (Exception e) {
      return result("500", "操作异常");
    } 
  }
  
  public String coin(String coin) {
    try {
      if (coin != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(coin);
        Integer type = Integer.valueOf(String.valueOf(toMap.get("type")));
        Integer serial = Integer.valueOf(toMap.get("serial").toString());
        Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
        Long num = Long.valueOf(String.valueOf(toMap.get("num")));
        Role player = this.roleService.getPlayer(roleId);
        if (player == null)
          result("500", "未找到玩家信息"); 
        boolean b = false;
        if (type <= 0) {
          b = this.roleService.deductCurrency(player, serial, num, "系统");
          RecordThread.getInstance().add2Queue(player.getId(), player.getRoleId(), (short)1000, (byte)2, String.format("系统扣除，货币类型：%d，扣除数量：%d", type, num), player.getMarking());
        } else {
          b = this.roleService.addCurrency(player, serial, num, "系统", true, false);
        } 
        if (b) {
          player.save();
        } else {
          return result("500", "未找到货币类型");
        } 
        return result("0", "操作成功");
      } 
      return result("500", "未知操作");
    } catch (Exception e) {
      return result("500", "操作异常");
    } 
  }
  
  public String recharge(String recharge) {
    try {
      if (recharge != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(recharge);
        Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
        Integer serial = Integer.valueOf(String.valueOf(toMap.get("serial")));
        Integer num = Integer.valueOf(String.valueOf(toMap.get("num")));
        Role player = SpringUtils.getRoleService().getPlayer(roleId);
        if (player != null) {
          short status = this.adminService.recharge(player, serial);
          if (status == 0)
            return result("500", "充值失败，未找到该充值项"); 
          for (int i = 0; i < num - 1; i++)
            this.adminService.recharge(player, serial);
          return result("0", "充值成功");
        } 
        return result("500", "未找到该角色");
      } 
      return result("500", "未知操作");
    } catch (Exception e) {
      return result("500", "操作异常");
    } 
  }
  
  public String rechargeable(String rechargeable) {
    try {
      if (rechargeable != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(rechargeable);
        Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
        Integer num = Integer.valueOf(String.valueOf(toMap.get("num")));
        Role player = SpringUtils.getRoleService().getPlayer(roleId);
        if (player != null) {
          short status = this.adminService.customRecharge(player, num);
          if (status == 0)
            return result("500", "充值失败"); 
          return result("0", "充值成功");
        } 
        return result("500", "未找到该角色");
      } 
      return result("500", "未知操作");
    } catch (Exception e) {
      return result("500", "操作异常");
    } 
  }
  
  public String prop(String prop) {
    try {
      if (prop != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(prop);
        Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
        Integer serial = Integer.valueOf(String.valueOf(toMap.get("serial")));
        Integer num = Integer.valueOf(String.valueOf(toMap.get("num")));
        Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(serial);
        if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue())) {
          SysPet sysPet = (SysPet)PetDataPool.petMap.get(serial);
          if (sysPet == null)
            return result("500", "发送失败，未找到该道具"); 
        } else {
          ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(serial);
          if (itemEasyInfo == null)
            return result("500", "发送失败，未找到该道具"); 
        } 
        Role player = SpringUtils.getRoleService().getPlayer(roleId);
        if (player != null) {
          boolean checkPos = this.bagService.checkPos(player, serial, num);
          if (!checkPos)
            return result("500", "玩家物品栏空间不足"); 
          this.bagService.addGoods(player, serial, num, 0, true, false, "后台发送");
          return result("0", "发送成功");
        } 
        return result("500", "未找到该角色");
      } 
      return result("500", "未知操作");
    } catch (Exception e) {
      return result("500", "操作异常");
    } 
  }
  
  public String gamers(String gamers) {
    try {
      if (gamers != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(gamers);
        Integer type = Integer.valueOf(String.valueOf(toMap.get("type")));
        if (type == 1) {
          long uid = SpringUtils.getPlayerService().getUidBy(String.valueOf(toMap.get("name")));
          Role role = this.roleService.getPlayer(uid);
          if (role != null)
            return packaging(role); 
        } else {
          String name = String.valueOf(toMap.get("name"));
          if (NumberUtil.isNumber(name)) {
            Role role = this.roleService.getPlayer(Integer.valueOf(name));
            if (role != null)
              return packaging(role); 
          } else {
            return result("500", "请输入数字编号");
          } 
        } 
      } 
      return result("500", "角色不在线或不存在");
    } catch (Exception e) {
      return result("500", "操作异常");
    } 
  }
  
  public String exp(String exp) {
    try {
      if (exp != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(exp);
        Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
        Long num = Long.valueOf(String.valueOf(toMap.get("num")));
        Role player = SpringUtils.getRoleService().getPlayer(roleId);
        if (player != null) {
          this.roleService.addExp(player, num, "系统", true);
          return result("0", "发送成功");
        } 
        return result("500", "未找到该角色");
      } 
      return result("500", "未知操作");
    } catch (Exception e) {
      return result("500", "操作异常");
    } 
  }
  
  public String task(String task) {
    try {
      if (task != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(task);
        Integer taskId = Integer.valueOf(String.valueOf(toMap.get("taskId")));
        TaskSet taskSet = this.taskService.getTaskSet(taskId);
        if (taskSet == null)
          return result("500", "没有该任务"); 
        Integer name = Integer.valueOf(String.valueOf(toMap.get("name")));
        Role role = this.roleService.getPlayer(name);
        role.getTask().updateToFinished(taskSet.getSerial());
        role.save();
        RespTasksDel respTasksDel = new RespTasksDel();
        respTasksDel.setSerial(taskSet.getSerial());
        respTasksDel.setType((byte)2);
        MessagePusher.pushMessage(role, (Message)respTasksDel);
        return result("0", "成功放弃任务");
      } 
      return result("500", "角色不在线或不存在");
    } catch (Exception e) {
      return result("500", "操作异常");
    } 
  }
  
  public String inquire(String inquire) {
    try {
      if (inquire != null) {
        List<Inquire> objects = new ArrayList<>();
        Map<String, Object> toMap = MapStringUtil.getStringToMap(inquire);
        String sid = String.valueOf(toMap.get("sid"));
        List<PlayerProfile> profiles = SpringUtils.getPlayerService().getPlayersBy(sid);
        if (profiles != null && profiles.size() > 0)
          for (PlayerProfile profile : profiles) {
            Role r = this.roleService.getPlayer(profile.getId());
            if (r != null) {
              long totalGold = r.getTotalGold();
              objects.add(new Inquire(r.getId(), r.getRoleId(), r.getName(), r.getSerial(), r.getRelive().byteValue(), r.getLevel().shortValue(), r.getGold().toString(), r.getSilver().toString(), String.valueOf(totalGold)));
            } 
          }  
        String data = JsonUtils.object2String(objects);
        return data;
      } 
      return result("500", "[]");
    } catch (Exception e) {
      return result("500", "[]");
    } 
  }
  
  public String market(String market) {
    try {
      if (market != null) {
        byte status;
        String msg;
        Role player;
        String str1;
        Map<String, Object> toMap = MapStringUtil.getStringToMap(market);
        int type = Integer.valueOf(String.valueOf(toMap.get("type")));
        int classify = Integer.valueOf(String.valueOf(toMap.get("classify")));
        long id = Long.valueOf(String.valueOf(toMap.get("id")));
        switch (type) {
          case 0:
            status = Byte.valueOf(String.valueOf(toMap.get("status")));
            str1 = this.marketService.updateMarketStatus(classify, id, status);
            if (str1 != null)
              return result("500", str1); 
            return result("0", "操作成功");
          case 1:
            msg = this.marketService.withdraw(classify, id);
            if (msg != null)
              return result("500", msg); 
            return result("0", "操作成功");
          case 2:
            msg = this.marketService.deleteMarket(classify, id);
            if (msg != null)
              return result("500", msg); 
            return result("0", "操作成功");
          case 3:
            player = SpringUtils.getRoleService().getPlayer(id);
            if (player != null) {
              player.getExtendBox().setBanTrade(classify);
              player.save();
            } else {
              return result("500", "未找到玩家信息");
            } 
            return result("0", "操作成功");
        } 
        return result("500", "未匹配到操作");
      } 
      return result("0", "操作成功");
    } catch (Exception e) {
      return result("500", "未知错误");
    } 
  }
  
  public String load(String load) {
    try {
      if (load != null) {
        int id;
        Map<String, Object> toMap = MapStringUtil.getStringToMap(load);
        int type = Integer.valueOf(String.valueOf(toMap.get("type")));
        switch (type) {
          case 0:
            ((ServerService)SpringUtils.getBean(ServerService.class)).versions(this.initService.initVersion());
            return result("0", "更新成功");
          case 1:
            WordDisposePool.load();
            return result("0", "更新成功");
          case 2:
            WelfareDataPool.loadGiftBag(this.initService);
            return result("0", "更新成功");
          case 3:
            id = Integer.valueOf(String.valueOf(toMap.get("id")));
            CxfDataPool.load(id);
            return result("0", "更新成功");
        } 
        return result("500", "未匹配到操作");
      } 
      return result("0", "更新成功");
    } catch (Exception e) {
      return result("500", "未知错误");
    } 
  }
  
  public String gift(String gift) {
    try {
      if (gift != null) {
        BrieGift brieGift = (BrieGift)(new Gson()).fromJson(gift, BrieGift.class);
        if (brieGift.getTime() < System.currentTimeMillis())
          return result("500", "过期时间不能小于当前时间"); 
        GiftBag giftBag = new GiftBag();
        giftBag.setName(brieGift.getName());
        giftBag.setTime(brieGift.getTime());
        giftBag.setRewards(brieGift.getList());
        WelfareDataPool.giftBagMap.put(giftBag.getName(), giftBag);
        this.dao.insert(giftBag);
      } 
      return result("0", "更新成功");
    } catch (Exception e) {
      return result("500", "未知错误");
    } 
  }
  
  public String adjustRoleGrade(String adjustRoleGrade) {
    try {
      if (adjustRoleGrade != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(adjustRoleGrade);
        int name = Integer.valueOf(String.valueOf(toMap.get("name")));
        int grade = Integer.valueOf(String.valueOf(toMap.get("grade")));
        if (!this.gmService.adjustRoleGrade(name, grade))
          return result("500", "操作失败"); 
      } 
      return result("0", "操作成功");
    } catch (Exception e) {
      return result("500", "未知错误");
    } 
  }
  
  public String pet(String pet) {
    try {
      if (pet != null) {
        String json = String.valueOf(pet);
        BriefPet briefPet = (BriefPet)(new Gson()).fromJson(json, BriefPet.class);
        if (!this.gmService.customPet(briefPet))
          return result("500", "操作失败"); 
      } 
      return result("0", "操作成功");
    } catch (Exception e) {
      return result("500", "未知错误");
    } 
  }
  
  public String horse(String horse) {
    try {
      if (horse != null) {
        String json = String.valueOf(horse);
        BriefHorse briefHorse = (BriefHorse)(new Gson()).fromJson(json, BriefHorse.class);
        if (!this.gmService.customHorse(briefHorse))
          return result("500", "操作失败"); 
      } 
      return result("0", "操作成功");
    } catch (Exception e) {
      return result("500", "未知错误");
    } 
  }
  
  public String rune(String rune) {
    try {
      if (rune != null) {
        Map<String, Object> toMap = MapStringUtil.getStringToMap(rune);
        int name = Integer.valueOf(String.valueOf(toMap.get("name")));
        int serial = Integer.valueOf(String.valueOf(toMap.get("serial")));
        byte grade = Byte.valueOf(String.valueOf(toMap.get("grade")));
        if (!this.gmService.customRune(name, serial, grade))
          return result("500", "操作失败"); 
      } 
      return result("0", "操作成功");
    } catch (Exception e) {
      return result("500", "未知错误");
    } 
  }
  
  public String magical(String magical) {
    try {
      if (magical != null) {
        String json = String.valueOf(magical);
        BriefMagical briefMagical = (BriefMagical)(new Gson()).fromJson(json, BriefMagical.class);
        Role role = this.roleService.getPlayer(briefMagical.getId());
        if (role == null)
          return result("500", "没有该角色"); 
        int pos = this.bagService.getPos(role, false);
        if (pos <= 0)
          result("500", "你的物品栏已满"); 
        if (!this.gmService.customMagical(role, pos, briefMagical))
          return result("500", "操作失败"); 
      } 
      return result("0", "操作成功");
    } catch (Exception e) {
      return result("500", "未知错误");
    } 
  }
  
  public String sin(String sin) {
    try {
      if (sin != null) {
        String json = String.valueOf(sin);
        BriefSin briefSin = (BriefSin)(new Gson()).fromJson(json, BriefSin.class);
        Role role = this.roleService.getPlayer(briefSin.getId());
        if (role == null)
          return result("500", "没有该角色"); 
        int pos = this.bagService.getPos(role, false);
        if (pos <= 0)
          result("500", "你的物品栏已满"); 
        if (!this.gmService.customSin(role, pos, briefSin))
          return result("500", "操作失败"); 
      } 
      return result("0", "操作成功");
    } catch (Exception e) {
      e.printStackTrace();
      return result("500", "未知错误");
    } 
  }
  
  public String acsry(String acsry) {
    try {
      if (acsry != null) {
        String json = String.valueOf(acsry);
        BriefAcsry briefAcsry = (BriefAcsry)(new Gson()).fromJson(json, BriefAcsry.class);
        Role role = this.roleService.getPlayer(briefAcsry.getId());
        if (role == null)
          return result("500", "没有该角色"); 
        int pos = this.bagService.getPos(role, false);
        if (pos <= 0)
          result("500", "你的物品栏已满"); 
        if (!this.gmService.customAcsry(role, pos, briefAcsry))
          return result("500", "操作失败"); 
      } 
      return result("0", "操作成功");
    } catch (Exception e) {
      e.printStackTrace();
      return result("500", "未知错误");
    } 
  }
  
  public String acsryPet(String acsryPet) {
    try {
      if (acsryPet != null) {
        String json = String.valueOf(acsryPet);
        BriePetfAcsry briePetfAcsry = (BriePetfAcsry)(new Gson()).fromJson(json, BriePetfAcsry.class);
        Role role = this.roleService.getPlayer(briePetfAcsry.getId());
        if (role == null)
          return result("500", "没有该角色"); 
        int pos = this.bagService.getPos(role, true);
        if (pos <= 0)
          result("500", "物品栏空间不足"); 
        if (!this.gmService.customAcsryPet(role, pos, briePetfAcsry))
          return result("500", "操作失败"); 
      } 
      return result("0", "操作成功");
    } catch (Exception e) {
      return result("500", "未知错误");
    } 
  }
  
  private String packaging(Role role) {
    Map<String, Object> map = new HashMap<>(10);
    map.put("code", "0");
    map.put("msg", "查询成功");
    map.put("id", role.getId());
    map.put("sid", role.getSid());
    map.put("rid", role.getRoleId());
    map.put("n", role.getName());
    map.put("no", role.getSerial());
    map.put("re", role.getRelive());
    map.put("gr", role.getLevel());
    map.put("xy", role.getGold());
    map.put("yl", role.getSilver());
    long totalGold = role.getTotalGold();
    map.put("total", totalGold);
    map.put("lock", role.getSafePwd());
    return MapStringUtil.getMapToString(map);
  }
  
  private String result(String code, String msg) {
    Map<String, Object> map = new HashMap<>();
    map.put("code", code);
    map.put("msg", msg);
    return MapStringUtil.getMapToString(map);
  }
  
  private String result(String code, String msg, String data) {
    Map<String, Object> map = new HashMap<>();
    map.put("code", code);
    map.put("msg", msg);
    map.put("data", data);
    return MapStringUtil.getMapToString(map);
  }
  
  public String email(String email) {
    try {
      if (email != null) {
        String json = String.valueOf(email);
        BriefEmail briefEmail = (BriefEmail)(new Gson()).fromJson(json, BriefEmail.class);
        long recipient = 0L;
        if (briefEmail.getRec() > 0) {
          Role player = this.roleService.getPlayer(briefEmail.getRec());
          if (player == null)
            return result("500", "用户不存在"); 
          recipient = player.getId();
        } 
        EmailSystem emailSystem = new EmailSystem();
        emailSystem.setEmailTitle(briefEmail.getTitle());
        emailSystem.setSender(briefEmail.getSen());
        emailSystem.setRecipient(recipient);
        emailSystem.setContent(briefEmail.getCon());
        emailSystem.setExpirationTime(briefEmail.getTime());
        emailSystem.setAccessory(briefEmail.getList());
        this.emailService.addEmailSystem(emailSystem);
        return result("0", "操作成功");
      } 
      return result("500", "未知错误");
    } catch (Exception e) {
      return result("500", "未知错误");
    } 
  }
  
  public String delEmail(String emailId) {
    Map<String, Object> toMap = MapStringUtil.getStringToMap(emailId);
    Long eid = Long.parseLong(String.valueOf(toMap.get("eid")));
    this.emailService.deleteEmailSystem(eid);
    return result("0", "操作成功");
  }
}
