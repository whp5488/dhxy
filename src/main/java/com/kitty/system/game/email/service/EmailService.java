package com.kitty.system.game.email.service;

import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.BagCache;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.record.RecordThread;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.email.Sender;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.email.domain.Email;
import com.kitty.system.game.email.domain.EmailAccessory;
import com.kitty.system.game.email.domain.EmailSystem;
import com.kitty.system.game.email.domain.EmailSystemAccessory;
import com.kitty.system.game.email.message.RespEmailDetail;
import com.kitty.system.game.email.message.RespEmailList;
import com.kitty.system.game.email.message.vo.EmailParam;
import com.kitty.system.game.email.pool.EmailDataPool;
import com.kitty.system.game.equip.make.EquipManage;
import com.kitty.system.game.fightnew.utils.FightMessageUtil;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.model.PartyMember;
import com.kitty.system.game.party.pool.PartyDataPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.role.service.TitleService;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import com.kitty.system.game.vo.TitleInfo;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataTitle;
import com.kitty.system.init.pool.PartDataPool;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  @Autowired
  private Dao dao;
  
  @Autowired
  private BagService bagService;
  
  @Autowired
  private RoleService roleService;
  
  @Autowired
  private PetService petService;
  
  @Autowired
  private IdWorker idWorker;
  
  public void getEmailList(Role role) {
    List<EmailParam> emailParams = new ArrayList<>();
    List<Email> emails = (List<Email>)DataCache.ROLE_EMAIL.get(role.getId());
    if (EmailDataPool.emailSystemList != null && EmailDataPool.emailSystemList.size() > 0)
      for (EmailSystem emailSystem : EmailDataPool.emailSystemList) {
        boolean flag = true;
        for (Email email : emails) {
          if (emailSystem.getId().equals(email.getEmailId())) {
            flag = false;
            break;
          } 
        } 
        if (flag) {
          List<EmailSystemAccessory> accessory = emailSystem.getAccessory();
          byte acce = 0;
          if (accessory != null && accessory.size() > 0)
            acce = 1; 
          emailParams.add(new EmailParam(emailSystem, acce));
        } 
      }  
    if (emails != null && emails.size() > 0) {
      List<EmailParam> emailParams1 = (List<EmailParam>)emails.stream().filter(email -> !email.isDelete()).map(email -> new EmailParam(email)).collect(Collectors.toList());
      emailParams.addAll(emailParams1);
    } 
    RespEmailList respEmailList = new RespEmailList(emailParams);
    FightMessageUtil.sendFightPackage(role.getId().longValue(), (Message)respEmailList);
  }
  
  public void getEmailDetail(Role role, Long emailId, byte isSystem) {
    RespEmailDetail respEmailDetail = new RespEmailDetail();
    List<Email> emails = (List<Email>)DataCache.ROLE_EMAIL.get(role.getId());
    if (0 == isSystem) {
      Optional<Email> opt = emails.stream().filter(obj -> emailId.equals(obj.getId())).findFirst();
      if (!opt.isPresent())
        return; 
      Email email = opt.get();
      if (email == null || email.isDelete())
        return; 
      respEmailDetail.setEmailTitle(email.getEmailTitle());
      respEmailDetail.setContent(email.getContent());
      respEmailDetail.setSenderName(Sender.getName((int)email.getSender()));
      if (email.getAccessorys() != null && email.getAccessorys().size() > 0) {
        List<EmailSystemAccessory> emailParams = (List<EmailSystemAccessory>)email.getAccessorys().stream().map(accessory -> new EmailSystemAccessory(accessory.getType(), accessory.getSerial(), String.valueOf(accessory.getNum()))).collect(Collectors.toList());
        respEmailDetail.setAccessorys(emailParams);
      } else if (email.getRead() == 0) {
        email.setRead((byte)1);
        email.save();
      } 
    } else if (1 == isSystem) {
      EmailSystem emailSystem = null;
      for (EmailSystem obj : EmailDataPool.emailSystemList) {
        if (emailId.equals(obj.getId()))
          emailSystem = obj; 
      } 
      if (emailSystem == null)
        return; 
      respEmailDetail.setEmailTitle(emailSystem.getEmailTitle());
      respEmailDetail.setContent(emailSystem.getContent());
      respEmailDetail.setSenderName(Sender.getName(emailSystem.getSender()));
      List<EmailSystemAccessory> accessorys = emailSystem.getAccessory();
      if (accessorys != null && accessorys.size() > 0) {
        respEmailDetail.setAccessorys(accessorys);
      } else {
        Email email = new Email(emailSystem, this.idWorker.nextId(), role.getId().longValue());
        email.setRead((byte)1);
        email.save();
        emails.add(email);
      } 
    } 
    respEmailDetail.setEmailId(emailId.toString());
    FightMessageUtil.sendFightPackage(role.getId().longValue(), (Message)respEmailDetail);
  }
  
  public void pickUpGoods(Role role, Long emailId) {
    long currTime = (new Date()).getTime();
    List<Email> emails = (List<Email>)DataCache.ROLE_EMAIL.get(role.getId());
    if (emailId.longValue() == 1L) {
      if (EmailDataPool.emailSystemList != null && EmailDataPool.emailSystemList.size() > 0)
        for (EmailSystem emailSystem : EmailDataPool.emailSystemList) {
          boolean flag = true;
          for (Email email : emails) {
            if (emailSystem.getId().equals(email.getEmailId())) {
              flag = false;
              break;
            } 
          } 
          if (flag && 
            emailSystem.getAccessory() != null && emailSystem.getAccessory().size() > 0)
            saveEmail(role, emailSystem); 
        }  
      if (emails.size() > 0)
        for (Email email : emails) {
          if (!email.isDelete() && email.getExpirationTime() > currTime)
            if (email.getRead() == 0 && email.getAccessory() == 1) {
              boolean flag = getAccessory(role, email);
              if (flag) {
                email.setRead((byte)1);
                email.save();
              } 
            }  
        }  
      getEmailList(role);
    } else if (emailId.longValue() > 1L) {
      Optional<EmailSystem> opt = EmailDataPool.emailSystemList.stream().filter(obj -> emailId.equals(obj.getId())).findFirst();
      if (!opt.isPresent()) {
        Optional<Email> optRole = emails.stream().filter(obj -> emailId.equals(obj.getId())).findFirst();
        if (optRole.isPresent()) {
          Email email = optRole.get();
          if (email == null || email.isDelete()) {
            MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("邮件无效"));
            return;
          } 
          boolean flag = getAccessory(role, email);
          if (flag) {
            email.setRead((byte)1);
            email.save();
            getEmailList(role);
          } 
        } 
      } else {
        EmailSystem emailSystem = opt.get();
        if (emailSystem != null && !emailSystem.isDelete() && 
          emailSystem.getAccessory() != null && emailSystem.getAccessory().size() > 0) {
          saveEmail(role, emailSystem);
          getEmailList(role);
        } 
      } 
    } 
  }
  
  public Email saveEmail(Role role, EmailSystem emailSystem) {
    Email email = new Email(emailSystem, this.idWorker.nextId(), role.getId().longValue());
    List<EmailAccessory> emailAccessorys = new ArrayList<>();
    boolean checkFlag = true;
    for (EmailSystemAccessory emailSystemAccessory : emailSystem.getAccessory()) {
      EmailAccessory emailAccessory = new EmailAccessory(emailSystemAccessory, this.idWorker.nextId(), email.getId().longValue());
      emailAccessorys.add(emailAccessory);
      if (!checkAccessory(role, emailAccessory)) {
        checkFlag = false;
        break;
      } 
    } 
    if (checkFlag) {
      boolean read = true;
      for (EmailAccessory emailAccessory1 : emailAccessorys) {
        if (emailAccessory1.getReceive() == 0) {
          if (saveAccessory(role, emailAccessory1.getType(), emailAccessory1.getSerial(), emailAccessory1.getNum())) {
            emailAccessory1.setReceive((byte)1);
          } else {
            read = false;
          } 
          emailAccessory1.save();
        } 
      } 
      if (read)
        email.setRead((byte)1); 
      email.setAccessorys(emailAccessorys);
      email.save();
      ((List<Email>)DataCache.ROLE_EMAIL.get(role.getId())).add(email);
    } else {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("领取失败，空间不足"));
    } 
    return email;
  }
  
  private boolean getAccessory(Role role, Email email) {
    boolean flag = true;
    if (email.getAccessorys() != null && email.getAccessorys().size() > 0) {
      for (EmailAccessory emailAccessory : email.getAccessorys()) {
        if (!checkAccessory(role, emailAccessory)) {
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("背包或天策符空间不足"));
          return false;
        } 
      } 
      for (EmailAccessory emailAccessory : email.getAccessorys()) {
        if (emailAccessory.getReceive() == 0) {
          boolean b = saveAccessory(role, emailAccessory.getType(), emailAccessory.getSerial(), emailAccessory.getNum());
          if (b) {
            emailAccessory.setReceive((byte)1);
            emailAccessory.save();
            continue;
          } 
          flag = false;
        } 
      } 
    } 
    return flag;
  }
  
  private boolean checkAccessory(Role role, EmailAccessory emailAccessory) {
    int posBag;
    byte type = emailAccessory.getType();
    int serial = emailAccessory.getSerial();
    long num = emailAccessory.getNum();
    boolean flag = true;
    if (type < 0 || serial < 0 || (num <= 0L && type != 6) || (num < 0L && type == 6)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("系统异常"));
      return false;
    } 
    switch (type) {
      case 1:
        flag = this.bagService.checkPos(role, serial, (int)num);
        break;
      case 0:
        posBag = this.bagService.getPos(role, true);
        if (posBag <= -1)
          flag = false; 
        break;
      case 4:
        flag = ((RoleTianFuService)SpringUtils.getBean(RoleTianFuService.class)).checkTianFu(role, serial, (int)num);
        break;
    } 
    return flag;
  }
  
  private boolean saveAccessory(Role role, byte type, int serial, long num) {
    int isSuccesse;
    SysDataTitle sysDataTitle;
    boolean flag = false;
    if (type < 0 || serial < 0 || (num <= 0L && type != 6) || (num < 0L && type == 6)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("系统异常"));
      return flag;
    } 
    switch (type) {
      case 5:
        flag = saveCurrency(role, serial, num);
        break;
      case 1:
        flag = saveItem(role, serial, (int)num);
        break;
      case 0:
        flag = saveEquip(role, serial, (int)num);
        break;
      case 4:
        flag = ((RoleTianFuService)SpringUtils.getBean(RoleTianFuService.class)).putTianFu(role, serial, (int)num);
        break;
      case 3:
        isSuccesse = this.bagService.addGoods(role, serial, (int)num, 0, true, false, "领取邮件变身卡");
        if (isSuccesse != -1)
          flag = true; 
        break;
      case 6:
        sysDataTitle = (SysDataTitle)PartDataPool.titleMap.get(Integer.valueOf(serial));
        if (sysDataTitle != null) {
          ((TitleService)SpringUtils.getBean(TitleService.class)).addTitle(role, new TitleInfo(serial, (int)num));
          flag = true;
        } 
        break;
    } 
    return flag;
  }
  
  private boolean saveCurrency(Role role, int serial, long num) {
    if (Const.RES_TYPE_EXP.intValue() == serial) {
      this.roleService.addExp(role, num, "", true);
      return true;
    } 
    if (Const.RES_TYPE_EXP_PET.intValue() == serial) {
      Integer combatPet = role.getExtendBox().getCombatPet();
      if (combatPet.intValue() > 0) {
        Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
        this.petService.addExp(role, pet, num, true);
      } 
      return true;
    } 
    return this.roleService.addCurrency(role, Integer.valueOf(serial), Long.valueOf(num), "邮件", true, false);
  }
  
  private boolean saveItem(Role role, int serial, int num) {
    boolean flag = this.bagService.checkPos(role, serial, num);
    if (!flag) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("背包物品栏空间不足"));
      return false;
    } 
    boolean returnFlag = false;
    int isSucceed = this.bagService.addGoods(role, serial, num, 0, true, false, "领取邮件物品");
    if (isSucceed != -1) {
      RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1, (byte)1, String.format("邮件领取道具（%d）,数量：%d", new Object[] { Integer.valueOf(serial), Integer.valueOf(num) }), role.getMarking());
      returnFlag = true;
    } 
    return returnFlag;
  }
  
  private boolean saveEquip(Role role, int serial, int num) {
    int posBag = this.bagService.getPos(role, false);
    if (posBag <= -1) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("背包物品栏空间不足"));
      return false;
    } 
    BagEquip equipBag = EquipManage.createEquip(Integer.valueOf(serial));
    if (equipBag == null) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("未找到对应的装备"));
      return false;
    } 
    ((Map<Long, BagEquip>)BagCache.BAG_EQUIP_MAP.get(role.getId())).put(equipBag.getId(), equipBag);
    SpringUtils.getEquipService().addEquip(role, equipBag, posBag, 0, "邮件内装备放入背包");
    return true;
  }
  
  public void deleteEmail(Role role, Long emailId) {
    if (emailId.longValue() == 1L) {
      List<Email> emails = DataCache.ROLE_EMAIL.get(role.getId()).stream().filter(email -> !email.isDelete()).collect(Collectors.toList());
      if (emails == null || emails.size() <= 0)
        return; 
      if (EmailDataPool.emailSystemList != null && EmailDataPool.emailSystemList.size() > 0)
        for (EmailSystem emailSystem : EmailDataPool.emailSystemList) {
          boolean flag = true;
          for (Email email : emails) {
            if (emailSystem.getId().equals(email.getEmailId())) {
              flag = false;
              break;
            } 
          } 
          if (flag)
            if (emailSystem.getAccessory() == null || emailSystem.getAccessory().size() <= 0) {
              Email emailSave = new Email(emailSystem, this.idWorker.nextId(), role.getId().longValue());
              emailSave.setDelete(true);
              emailSave.save();
              ((List<Email>)DataCache.ROLE_EMAIL.get(role.getId())).add(emailSave);
            }  
        }  
      for (Email email : emails) {
        email.setDelete(true);
        email.save();
      } 
    } else if (emailId.longValue() > 1L) {
      Optional<Email> opt = ((List<Email>)DataCache.ROLE_EMAIL.get(role.getId())).stream().filter(obj -> emailId.equals(obj.getId())).findFirst();
      if (opt.isPresent()) {
        Email email = opt.get();
        if (email == null || email.isDelete()) {
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("邮件无效"));
          return;
        } 
        email.setDelete(true);
        email.save();
      } 
    } 
    getEmailList(role);
  }
  
  public void loadRoleEmail(Role role) {
    List<Email> emails = (List<Email>)DataCache.ROLE_EMAIL.get(role.getId());
    long currTime = (new Date()).getTime();
    if (emails == null) {
      emails = this.dao.query(Email.class, (Condition)Cnd.where("recipient", "=", role.getId())
          .and("expiration_time", ">", Long.valueOf(currTime)).desc("create_time"));
      if (emails == null) {
        emails = new ArrayList<>();
      } else {
        for (Email email : emails) {
          if (email.getAccessory() == 1) {
            List<EmailAccessory> emailsAccessorys = this.dao.query(EmailAccessory.class, (Condition)Cnd.where("email_id", "=", email.getId()));
            if (emailsAccessorys != null && emailsAccessorys.size() > 0)
              email.setAccessorys(emailsAccessorys); 
          } 
        } 
      } 
      DataCache.ROLE_EMAIL.put(role.getId(), emails);
    } 
  }
  
  public void addEmailSystem(EmailSystem emailSystem) {
    if (emailSystem.getSender() == 2) {
      Party party = (Party)PartyDataPool.partyMap.get(Integer.valueOf((int)emailSystem.getRecipient()));
      if (party != null) {
        Map<Integer, PartyMember> members = party.getMembers();
        if (members != null && members.size() > 0) {
          Set<Integer> roleIdSet = members.keySet();
          Iterator<Integer> iterator = roleIdSet.iterator();
          while (iterator.hasNext()) {
            Role player = ((RoleService)SpringUtils.getBean(RoleService.class)).getPlayer(((Integer)iterator.next()).intValue());
            if (player != null) {
              saveEmail(emailSystem, player.getId(), (byte)0);
              MessagePusher.pushMessage(player.getId().longValue(), (Message)new RespHint(29, (short)1));
            } 
          } 
        } 
      } 
      return;
    } 
    if (emailSystem.getRecipient() > 2000000L) {
      saveEmail(emailSystem, Long.valueOf(emailSystem.getRecipient()), (byte)0);
      MessagePusher.pushMessage(emailSystem.getRecipient(), (Message)new RespHint(29, (short)1));
      return;
    } 
    emailSystem.setId(Long.valueOf(this.idWorker.nextId()));
    emailSystem.setCreateTime(System.currentTimeMillis());
    emailSystem.save();
    EmailDataPool.emailSystemList.add(emailSystem);
    if (emailSystem.getRecipient() <= 0L) {
      ConcurrentHashMap<Long, Role> onlineRoles = DataCache.ONLINE_ROLES;
      for (Map.Entry<Long, Role> entry : onlineRoles.entrySet())
        MessagePusher.pushMessage(((Long)entry.getKey()).longValue(), (Message)new RespHint(29, (short)1)); 
    } else {
      MessagePusher.pushMessage(emailSystem.getRecipient(), (Message)new RespHint(29, (short)1));
    } 
  }
  
  private void saveEmail(EmailSystem emailSystem, Long roleId, byte isRead) {
    Email email = new Email(emailSystem, this.idWorker.nextId(), roleId.longValue());
    email.setRead(isRead);
    if (emailSystem.getAccessory() != null && emailSystem.getAccessory().size() > 0) {
      List<EmailAccessory> emailAccessorys = new ArrayList<>();
      for (EmailSystemAccessory emailSystemAccessory : emailSystem.getAccessory()) {
        EmailAccessory emailAccessory = new EmailAccessory(emailSystemAccessory, this.idWorker.nextId(), email.getId().longValue());
        emailAccessory.save();
        emailAccessorys.add(emailAccessory);
      } 
      if (emailAccessorys.size() > 0)
        email.setAccessorys(emailAccessorys); 
    } 
    email.save();
    List<Email> emails = (List<Email>)DataCache.ROLE_EMAIL.get(roleId);
    if (emails == null) {
      emails = new ArrayList<>();
      DataCache.ROLE_EMAIL.put(Long.valueOf(emailSystem.getRecipient()), emails);
    } 
    emails.add(email);
  }
  
  public void deleteEmailSystem(Long emailId) {
    if (emailId == null)
      return; 
    EmailSystem emailSystem = null;
    for (EmailSystem obj : EmailDataPool.emailSystemList) {
      if (emailId.equals(obj.getId()))
        emailSystem = obj; 
    } 
    if (emailSystem != null) {
      emailSystem.setDelete(false);
      emailSystem.save();
      EmailDataPool.emailSystemList.remove(emailSystem);
    } 
  }
}
