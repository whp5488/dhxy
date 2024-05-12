
package com.kitty.system.game.email.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import com.kitty.system.game.party.model.PartyMember;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.party.pool.PartyDataPool;
import com.kitty.system.game.party.model.Party;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.BagCache;
import com.kitty.system.game.bag.domain.BagEquip;
import java.util.Map;
import com.kitty.system.game.equip.make.EquipManage;
import com.kitty.record.RecordThread;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.vo.TitleInfo;
import com.kitty.system.game.role.service.TitleService;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysDataTitle;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import com.kitty.system.game.email.domain.EmailAccessory;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.util.Date;
import java.util.Optional;
import com.kitty.system.enums.email.Sender;
import com.kitty.system.game.email.message.RespEmailDetail;
import com.kitty.system.game.email.domain.EmailSystemAccessory;
import java.util.Iterator;
import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.utils.FightMessageUtil;
import com.kitty.system.game.email.message.RespEmailList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.email.domain.EmailSystem;
import com.kitty.system.game.email.pool.EmailDataPool;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.email.domain.Email;
import java.util.List;
import com.kitty.system.game.email.message.vo.EmailParam;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.thread.IdWorker;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.bag.service.BagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class EmailService
{
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
    
    public void getEmailList(final Role role) {
        final List<EmailParam> emailParams = new ArrayList<EmailParam>();
        final List<Email> emails = DataCache.ROLE_EMAIL.get(role.getId());
        if (EmailDataPool.emailSystemList != null && EmailDataPool.emailSystemList.size() > 0) {
            for (final EmailSystem emailSystem : EmailDataPool.emailSystemList) {
                boolean flag = true;
                final Iterator<Email> iterator2 = emails.iterator();
                while (iterator2.hasNext()) {
                    Email email = iterator2.next();
                    if (emailSystem.getId().equals(email.getEmailId())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    final List<EmailSystemAccessory> accessory = emailSystem.getAccessory();
                    byte acce = 0;
                    if (accessory != null && accessory.size() > 0) {
                        acce = 1;
                    }
                    emailParams.add(new EmailParam(emailSystem, acce));
                }
            }
        }
        if (emails != null && emails.size() > 0) {
            final List<EmailParam> emailParams2 = emails.stream().filter(email -> !email.isDelete()).map(email -> new EmailParam(email)).collect(Collectors.toList());
            emailParams.addAll(emailParams2);
        }
        final RespEmailList respEmailList = new RespEmailList(emailParams);
        FightMessageUtil.sendFightPackage(role.getId(), respEmailList);
    }
    
    public void getEmailDetail(final Role role, final Long emailId, final byte isSystem) {
        final RespEmailDetail respEmailDetail = new RespEmailDetail();
        final List<Email> emails = DataCache.ROLE_EMAIL.get(role.getId());
        if (0 == isSystem) {
            final Optional<Email> opt = emails.stream().filter(obj -> emailId.equals(obj.getId())).findFirst();
            if (!opt.isPresent()) {
                return;
            }
            final Email email = opt.get();
            if (email == null || email.isDelete()) {
                return;
            }
            respEmailDetail.setEmailTitle(email.getEmailTitle());
            respEmailDetail.setContent(email.getContent());
            respEmailDetail.setSenderName(Sender.getName((int)email.getSender()));
            if (email.getAccessorys() != null && email.getAccessorys().size() > 0) {
                final List<EmailSystemAccessory> emailParams = email.getAccessorys().stream().map(accessory -> new EmailSystemAccessory(accessory.getType(), accessory.getSerial(), String.valueOf(accessory.getNum()))).collect(Collectors.toList());
                respEmailDetail.setAccessorys(emailParams);
            }
            else if (email.getRead() == 0) {
                email.setRead((byte)1);
                email.save();
            }
        }
        else if (1 == isSystem) {
            EmailSystem emailSystem = null;
            for (final EmailSystem obj : EmailDataPool.emailSystemList) {
                if (emailId.equals(obj.getId())) {
                    emailSystem = obj;
                }
            }
            if (emailSystem == null) {
                return;
            }
            respEmailDetail.setEmailTitle(emailSystem.getEmailTitle());
            respEmailDetail.setContent(emailSystem.getContent());
            respEmailDetail.setSenderName(Sender.getName(emailSystem.getSender()));
            final List<EmailSystemAccessory> accessorys = emailSystem.getAccessory();
            if (accessorys != null && accessorys.size() > 0) {
                respEmailDetail.setAccessorys(accessorys);
            }
            else {
                final Email email2 = new Email(emailSystem, this.idWorker.nextId(), role.getId());
                email2.setRead((byte)1);
                email2.save();
                emails.add(email2);
            }
        }
        respEmailDetail.setEmailId(emailId.toString());
        FightMessageUtil.sendFightPackage(role.getId(), respEmailDetail);
    }
    
    public void pickUpGoods(final Role role, final Long emailId) {
        final long currTime = new Date().getTime();
        final List<Email> emails = DataCache.ROLE_EMAIL.get(role.getId());
        if (emailId == 1L) {
            if (EmailDataPool.emailSystemList != null && EmailDataPool.emailSystemList.size() > 0) {
                for (final EmailSystem emailSystem : EmailDataPool.emailSystemList) {
                    boolean flag = true;
                    for (final Email email : emails) {
                        if (emailSystem.getId().equals(email.getEmailId())) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag && emailSystem.getAccessory() != null && emailSystem.getAccessory().size() > 0) {
                        this.saveEmail(role, emailSystem);
                    }
                }
            }
            if (emails.size() > 0) {
                for (final Email email2 : emails) {
                    if (!email2.isDelete() && email2.getExpirationTime() > currTime && email2.getRead() == 0 && email2.getAccessory() == 1) {
                        final boolean flag2 = this.getAccessory(role, email2);
                        if (!flag2) {
                            continue;
                        }
                        email2.setRead((byte)1);
                        email2.save();
                    }
                }
            }
            this.getEmailList(role);
        }
        else if (emailId > 1L) {
            final Optional<EmailSystem> opt = EmailDataPool.emailSystemList.stream().filter(obj -> emailId.equals(obj.getId())).findFirst();
            if (!opt.isPresent()) {
                final Optional<Email> optRole = emails.stream().filter(obj -> emailId.equals(obj.getId())).findFirst();
                if (optRole.isPresent()) {
                    final Email email3 = optRole.get();
                    if (email3 == null || email3.isDelete()) {
                        MessagePusher.pushMessage(role, new RespNotifyMiscEx("邮件无效"));
                        return;
                    }
                    final boolean flag3 = this.getAccessory(role, email3);
                    if (flag3) {
                        email3.setRead((byte)1);
                        email3.save();
                        this.getEmailList(role);
                    }
                }
            }
            else {
                final EmailSystem emailSystem2 = opt.get();
                if (emailSystem2 != null && !emailSystem2.isDelete() && emailSystem2.getAccessory() != null && emailSystem2.getAccessory().size() > 0) {
                    this.saveEmail(role, emailSystem2);
                    this.getEmailList(role);
                }
            }
        }
    }
    
    public Email saveEmail(final Role role, final EmailSystem emailSystem) {
        final Email email = new Email(emailSystem, this.idWorker.nextId(), role.getId());
        final List<EmailAccessory> emailAccessorys = new ArrayList<EmailAccessory>();
        boolean checkFlag = true;
        for (final EmailSystemAccessory emailSystemAccessory : emailSystem.getAccessory()) {
            final EmailAccessory emailAccessory = new EmailAccessory(emailSystemAccessory, this.idWorker.nextId(), email.getId());
            emailAccessorys.add(emailAccessory);
            if (!this.checkAccessory(role, emailAccessory)) {
                checkFlag = false;
                break;
            }
        }
        if (checkFlag) {
            boolean read = true;
            for (final EmailAccessory emailAccessory2 : emailAccessorys) {
                if (emailAccessory2.getReceive() == 0) {
                    if (this.saveAccessory(role, emailAccessory2.getType(), emailAccessory2.getSerial(), emailAccessory2.getNum())) {
                        emailAccessory2.setReceive((byte)1);
                    }
                    else {
                        read = false;
                    }
                    emailAccessory2.save();
                }
            }
            if (read) {
                email.setRead((byte)1);
            }
            email.setAccessorys(emailAccessorys);
            email.save();
            DataCache.ROLE_EMAIL.get(role.getId()).add(email);
        }
        else {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("领取失败，空间不足"));
        }
        return email;
    }
    
    private boolean getAccessory(final Role role, final Email email) {
        boolean flag = true;
        if (email.getAccessorys() != null && email.getAccessorys().size() > 0) {
            for (final EmailAccessory emailAccessory : email.getAccessorys()) {
                if (!this.checkAccessory(role, emailAccessory)) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("背包或天策符空间不足"));
                    return false;
                }
            }
            for (final EmailAccessory emailAccessory : email.getAccessorys()) {
                if (emailAccessory.getReceive() == 0) {
                    final boolean b = this.saveAccessory(role, emailAccessory.getType(), emailAccessory.getSerial(), emailAccessory.getNum());
                    if (b) {
                        emailAccessory.setReceive((byte)1);
                        emailAccessory.save();
                    }
                    else {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }
    
    private boolean checkAccessory(final Role role, final EmailAccessory emailAccessory) {
        final byte type = emailAccessory.getType();
        final int serial = emailAccessory.getSerial();
        final long num = emailAccessory.getNum();
        boolean flag = true;
        if (type < 0 || serial < 0 || (num <= 0L && type != 6) || (num < 0L && type == 6)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("系统异常"));
            return false;
        }
        switch (type) {
            case 1: {
                flag = this.bagService.checkPos(role, serial, (int)num);
                break;
            }
            case 0: {
                final int posBag = this.bagService.getPos(role, true);
                if (posBag <= -1) {
                    flag = false;
                    break;
                }
                break;
            }
            case 4: {
                flag = SpringUtils.getBean(RoleTianFuService.class).checkTianFu(role, serial, (int)num);
                break;
            }
        }
        return flag;
    }
    
    private boolean saveAccessory(final Role role, final byte type, final int serial, final long num) {
        boolean flag = false;
        if (type < 0 || serial < 0 || (num <= 0L && type != 6) || (num < 0L && type == 6)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("系统异常"));
            return flag;
        }
        switch (type) {
            case 5: {
                flag = this.saveCurrency(role, serial, num);
                break;
            }
            case 1: {
                flag = this.saveItem(role, serial, (int)num);
                break;
            }
            case 0: {
                flag = this.saveEquip(role, serial, (int)num);
                break;
            }
            case 4: {
                flag = SpringUtils.getBean(RoleTianFuService.class).putTianFu(role, serial, (int)num);
                break;
            }
            case 3: {
                final int isSuccesse = this.bagService.addGoods(role, serial, (int)num, 0, true, false, "领取邮件变身卡");
                if (isSuccesse != -1) {
                    flag = true;
                    break;
                }
                break;
            }
            case 6: {
                final SysDataTitle sysDataTitle = PartDataPool.titleMap.get(serial);
                if (sysDataTitle != null) {
                    SpringUtils.getBean(TitleService.class).addTitle(role, new TitleInfo(serial, (int)num));
                    flag = true;
                    break;
                }
                break;
            }
        }
        return flag;
    }
    
    private boolean saveCurrency(final Role role, final int serial, final long num) {
        if (Const.RES_TYPE_EXP == serial) {
            this.roleService.addExp(role, num, "", true);
            return true;
        }
        if (Const.RES_TYPE_EXP_PET == serial) {
            final Integer combatPet = role.getExtendBox().getCombatPet();
            if (combatPet > 0) {
                final Pet pet = this.petService.selectPet(role).get(combatPet);
                this.petService.addExp(role, pet, num, true);
            }
            return true;
        }
        return this.roleService.addCurrency(role, serial, num, "邮件", true, false);
    }
    
    private boolean saveItem(final Role role, final int serial, final int num) {
        final boolean flag = this.bagService.checkPos(role, serial, num);
        if (!flag) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("背包物品栏空间不足"));
            return false;
        }
        boolean returnFlag = false;
        final int isSucceed = this.bagService.addGoods(role, serial, num, 0, true, false, "领取邮件物品");
        if (isSucceed != -1) {
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1, (byte)1, String.format("邮件领取道具（%d）,数量：%d", serial, num), role.getMarking());
            returnFlag = true;
        }
        return returnFlag;
    }
    
    private boolean saveEquip(final Role role, final int serial, final int num) {
        final int posBag = this.bagService.getPos(role, false);
        if (posBag <= -1) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("背包物品栏空间不足"));
            return false;
        }
        final BagEquip equipBag = EquipManage.createEquip(serial);
        if (equipBag == null) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("未找到对应的装备"));
            return false;
        }
        BagCache.BAG_EQUIP_MAP.get(role.getId()).put(equipBag.getId(), equipBag);
        SpringUtils.getEquipService().addEquip(role, equipBag, posBag, 0, "邮件内装备放入背包");
        return true;
    }
    
    public void deleteEmail(final Role role, final Long emailId) {
        if (emailId == 1L) {
            final List<Email> emails = (List<Email>)DataCache.ROLE_EMAIL.get(role.getId()).stream().filter(email -> !email.isDelete()).collect(Collectors.toList());
            if (emails == null || emails.size() <= 0) {
                return;
            }
            if (EmailDataPool.emailSystemList != null && EmailDataPool.emailSystemList.size() > 0) {
                for (final EmailSystem emailSystem : EmailDataPool.emailSystemList) {
                    boolean flag = true;
                    final Iterator<Email> iterator2 = emails.iterator();
                    while (iterator2.hasNext()) {
                        Email email = iterator2.next();
                        if (emailSystem.getId().equals(email.getEmailId())) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag && (emailSystem.getAccessory() == null || emailSystem.getAccessory().size() <= 0)) {
                        final Email emailSave = new Email(emailSystem, this.idWorker.nextId(), role.getId());
                        emailSave.setDelete(true);
                        emailSave.save();
                        DataCache.ROLE_EMAIL.get(role.getId()).add(emailSave);
                    }
                }
            }
            for (final Email email2 : emails) {
                email2.setDelete(true);
                email2.save();
            }
        }
        else if (emailId > 1L) {
            final Optional<Email> opt = DataCache.ROLE_EMAIL.get(role.getId()).stream().filter(obj -> emailId.equals(obj.getId())).findFirst();
            if (opt.isPresent()) {
                final Email email3 = opt.get();
                if (email3 == null || email3.isDelete()) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("邮件无效"));
                    return;
                }
                email3.setDelete(true);
                email3.save();
            }
        }
        this.getEmailList(role);
    }
    
    public void loadRoleEmail(final Role role) {
        List<Email> emails = DataCache.ROLE_EMAIL.get(role.getId());
        final long currTime = new Date().getTime();
        if (emails == null) {
            emails = this.dao.query(Email.class, Cnd.where("recipient", "=", role.getId()).and("expiration_time", ">", (Object)currTime).desc("create_time"));
            if (emails == null) {
                emails = new ArrayList<Email>();
            }
            else {
                for (final Email email : emails) {
                    if (email.getAccessory() == 1) {
                        final List<EmailAccessory> emailsAccessorys = this.dao.query(EmailAccessory.class, Cnd.where("email_id", "=", email.getId()));
                        if (emailsAccessorys == null || emailsAccessorys.size() <= 0) {
                            continue;
                        }
                        email.setAccessorys(emailsAccessorys);
                    }
                }
            }
            DataCache.ROLE_EMAIL.put(role.getId(), emails);
        }
    }
    
    public void addEmailSystem(final EmailSystem emailSystem) {
        if (emailSystem.getSender() == 2) {
            final Party party = PartyDataPool.partyMap.get((int)emailSystem.getRecipient());
            if (party != null) {
                final Map<Integer, PartyMember> members = party.getMembers();
                if (members != null && members.size() > 0) {
                    final Set<Integer> roleIdSet = members.keySet();
                    final Iterator<Integer> iterator = roleIdSet.iterator();
                    while (iterator.hasNext()) {
                        final Role player = SpringUtils.getBean(RoleService.class).getPlayer(iterator.next());
                        if (player != null) {
                            this.saveEmail(emailSystem, player.getId(), (byte)0);
                            MessagePusher.pushMessage(player.getId(), new RespHint(29, (short)1));
                        }
                    }
                }
            }
            return;
        }
        if (emailSystem.getRecipient() > 2000000L) {
            this.saveEmail(emailSystem, emailSystem.getRecipient(), (byte)0);
            MessagePusher.pushMessage(emailSystem.getRecipient(), new RespHint(29, (short)1));
            return;
        }
        emailSystem.setId(this.idWorker.nextId());
        emailSystem.setCreateTime(System.currentTimeMillis());
        emailSystem.save();
        EmailDataPool.emailSystemList.add(emailSystem);
        if (emailSystem.getRecipient() <= 0L) {
            final ConcurrentHashMap<Long, Role> onlineRoles = DataCache.ONLINE_ROLES;
            for (final Map.Entry<Long, Role> entry : onlineRoles.entrySet()) {
                MessagePusher.pushMessage(entry.getKey(), new RespHint(29, (short)1));
            }
        }
        else {
            MessagePusher.pushMessage(emailSystem.getRecipient(), new RespHint(29, (short)1));
        }
    }
    
    private void saveEmail(final EmailSystem emailSystem, final Long roleId, final byte isRead) {
        final Email email = new Email(emailSystem, this.idWorker.nextId(), roleId);
        email.setRead(isRead);
        if (emailSystem.getAccessory() != null && emailSystem.getAccessory().size() > 0) {
            final List<EmailAccessory> emailAccessorys = new ArrayList<EmailAccessory>();
            for (final EmailSystemAccessory emailSystemAccessory : emailSystem.getAccessory()) {
                final EmailAccessory emailAccessory = new EmailAccessory(emailSystemAccessory, this.idWorker.nextId(), email.getId());
                emailAccessory.save();
                emailAccessorys.add(emailAccessory);
            }
            if (emailAccessorys.size() > 0) {
                email.setAccessorys(emailAccessorys);
            }
        }
        email.save();
        List<Email> emails = DataCache.ROLE_EMAIL.get(roleId);
        if (emails == null) {
            emails = new ArrayList<Email>();
            DataCache.ROLE_EMAIL.put(emailSystem.getRecipient(), emails);
        }
        emails.add(email);
    }
    
    public void deleteEmailSystem(final Long emailId) {
        if (emailId == null) {
            return;
        }
        EmailSystem emailSystem = null;
        for (final EmailSystem obj : EmailDataPool.emailSystemList) {
            if (emailId.equals(obj.getId())) {
                emailSystem = obj;
            }
        }
        if (emailSystem != null) {
            emailSystem.setDelete(false);
            emailSystem.save();
            EmailDataPool.emailSystemList.remove(emailSystem);
        }
    }
}
