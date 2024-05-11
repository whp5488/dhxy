
package com.kitty.cxf.service.impl;

import com.kitty.system.game.email.domain.EmailSystem;
import com.kitty.system.admin.domain.BriefEmail;
import java.util.HashMap;
import com.kitty.system.admin.domain.BriePetfAcsry;
import com.kitty.system.admin.domain.BriefAcsry;
import com.kitty.system.admin.domain.BriefSin;
import com.kitty.system.admin.domain.BriefMagical;
import com.kitty.system.admin.domain.BriefHorse;
import com.kitty.system.admin.domain.BriefPet;
import com.kitty.system.game.welfare.domain.GiftBag;
import com.google.gson.Gson;
import com.kitty.system.admin.domain.BrieGift;
import com.kitty.cxf.pool.CxfDataPool;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.game.dirtywords.WordDisposePool;
import java.util.Iterator;
import java.util.List;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.player.PlayerProfile;
import com.kitty.cxf.model.Inquire;
import java.util.ArrayList;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.task.message.RespTasksDel;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.record.RecordThread;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.core.SchedulerManager;
import java.util.Map;
import com.kitty.cxf.common.MapStringUtil;
import com.kitty.system.game.email.service.EmailService;
import com.kitty.system.admin.service.GmService;
import com.kitty.system.game.market.service.MarketService;
import com.kitty.cxf.send.service.GameSendService;
import com.kitty.common.start.ServerService;
import com.kitty.system.init.service.InitService;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.admin.service.AdminService;
import com.kitty.common.start.GameServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import javax.jws.WebService;
import org.springframework.stereotype.Component;
import com.kitty.cxf.service.ServiceMaster;

@Component
@WebService(serviceName = "ServiceMaster", targetNamespace = "http://my.com", endpointInterface = "com.kitty.cxf.service.ServiceMaster")
public class ServiceMasterImpl implements ServiceMaster
{
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
    
    @Override
    public String notice(final String notice) {
        try {
            if (notice != null) {
                final Map<String, Object> toMap = MapStringUtil.getStringToMap(notice);
                final String msg = String.valueOf(toMap.get("msg"));
                final Integer num = Integer.valueOf(String.valueOf(toMap.get("num")));
                final Integer second = Integer.valueOf(String.valueOf(toMap.get("second")));
                this.adminService.asyncThreadNotice(msg, num, second);
                return this.result("0", "操作成功");
            }
            return this.result("500", "未知操作");
        }
        catch (final Exception e) {
            return this.result("500", "操作异常");
        }
    }
    
    @Override
    public String close(final String close) {
        try {
            if (close != null) {
                this.serverService.setState(0);
                final Map<String, Object> toMap = MapStringUtil.getStringToMap(close);
                final Integer time = Integer.valueOf(String.valueOf(toMap.get("time")));
                SchedulerManager.getInstance().schedule(() -> {
                    this.gameSendService.sendClose("操作关服");
                    Runtime.getRuntime().exit(0);
                    return;
                }, time * 1000L);
                return this.result("0", "开始进行关服");
            }
            return this.result("500", "未知操作");
        }
        catch (final Exception e) {
            this.gameSendService.sendClose("操作关服-异常");
            Runtime.getRuntime().exit(0);
            return this.result("500", "操作异常");
        }
    }
    
    @Override
    public String speak(final String speak) {
        try {
            if (speak == null) {
                return this.result("500", "未知操作");
            }
            final Map<String, Object> toMap = MapStringUtil.getStringToMap(speak);
            final Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
            final Integer type = Integer.valueOf(String.valueOf(toMap.get("type")));
            final long day = Long.valueOf(String.valueOf(toMap.get("day")));
            final Role target = this.roleService.getPlayer(roleId);
            if (type == 0) {
                this.adminService.shutChannel(target, day);
                MessagePusher.pushMessage(target, new RespNotifyMiscEx("你已被禁言!"));
                return this.result("0", "操作成功");
            }
            this.adminService.shutChannel(target, 0L);
            MessagePusher.pushMessage(target, new RespNotifyMiscEx("你可以畅所欲言了!"));
            return this.result("0", "操作成功");
        }
        catch (final Exception e) {
            return this.result("500", "操作异常");
        }
    }
    
    @Override
    public String seal(final String seal) {
        try {
            if (seal == null) {
                return this.result("500", "未知操作");
            }
            final Map<String, Object> toMap = MapStringUtil.getStringToMap(seal);
            final Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
            final Integer type = Integer.valueOf(String.valueOf(toMap.get("type")));
            final long day = Long.valueOf(String.valueOf(toMap.get("day")));
            final Role target = this.roleService.getPlayer(roleId);
            if (type == 0) {
                this.adminService.blockPlayer(target, day, null);
                return this.result("0", "操作成功", target.getSid());
            }
            this.adminService.blockPlayer(target, 0L, null);
            return this.result("0", "操作成功", target.getSid());
        }
        catch (final Exception e) {
            return this.result("500", "操作异常");
        }
    }
    
    @Override
    public String coin(final String coin) {
        try {
            if (coin == null) {
                return this.result("500", "未知操作");
            }
            final Map<String, Object> toMap = MapStringUtil.getStringToMap(coin);
            final Integer type = Integer.valueOf(String.valueOf(toMap.get("type")));
            final Integer serial = Integer.valueOf(toMap.get("serial").toString());
            final Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
            final Long num = Long.valueOf(String.valueOf(toMap.get("num")));
            final Role player = this.roleService.getPlayer(roleId);
            if (player == null) {
                this.result("500", "未找到玩家信息");
            }
            boolean b = false;
            if (type <= 0) {
                b = this.roleService.deductCurrency(player, serial, num, "系统");
                RecordThread.getInstance().add2Queue(player.getId(), player.getRoleId(), (short)1000, (byte)2, String.format("系统扣除，货币类型：%d，扣除数量：%d", type, num), player.getMarking());
            }
            else {
                b = this.roleService.addCurrency(player, serial, num, "系统", true, false);
            }
            if (b) {
                player.save();
                return this.result("0", "操作成功");
            }
            return this.result("500", "未找到货币类型");
        }
        catch (final Exception e) {
            return this.result("500", "操作异常");
        }
    }
    
    @Override
    public String recharge(final String recharge) {
        try {
            if (recharge == null) {
                return this.result("500", "未知操作");
            }
            final Map<String, Object> toMap = MapStringUtil.getStringToMap(recharge);
            final Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
            final Integer serial = Integer.valueOf(String.valueOf(toMap.get("serial")));
            final Integer num = Integer.valueOf(String.valueOf(toMap.get("num")));
            final Role player = SpringUtils.getRoleService().getPlayer(roleId);
            if (player == null) {
                return this.result("500", "未找到该角色");
            }
            final short status = this.adminService.recharge(player, serial);
            if (status == 0) {
                return this.result("500", "充值失败，未找到该充值项");
            }
            for (int i = 0; i < num - 1; ++i) {
                this.adminService.recharge(player, serial);
            }
            return this.result("0", "充值成功");
        }
        catch (final Exception e) {
            return this.result("500", "操作异常");
        }
    }
    
    @Override
    public String rechargeable(final String rechargeable) {
        try {
            if (rechargeable == null) {
                return this.result("500", "未知操作");
            }
            final Map<String, Object> toMap = MapStringUtil.getStringToMap(rechargeable);
            final Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
            final Integer num = Integer.valueOf(String.valueOf(toMap.get("num")));
            final Role player = SpringUtils.getRoleService().getPlayer(roleId);
            if (player == null) {
                return this.result("500", "未找到该角色");
            }
            final short status = this.adminService.customRecharge(player, num);
            if (status == 0) {
                return this.result("500", "充值失败");
            }
            return this.result("0", "充值成功");
        }
        catch (final Exception e) {
            return this.result("500", "操作异常");
        }
    }
    
    @Override
    public String prop(final String prop) {
        try {
            if (prop == null) {
                return this.result("500", "未知操作");
            }
            final Map<String, Object> toMap = MapStringUtil.getStringToMap(prop);
            final Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
            final Integer serial = Integer.valueOf(String.valueOf(toMap.get("serial")));
            final Integer num = Integer.valueOf(String.valueOf(toMap.get("num")));
            final Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(serial);
            if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue())) {
                final SysPet sysPet = PetDataPool.petMap.get(serial);
                if (sysPet == null) {
                    return this.result("500", "发送失败，未找到该道具");
                }
            }
            else {
                final ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(serial);
                if (itemEasyInfo == null) {
                    return this.result("500", "发送失败，未找到该道具");
                }
            }
            final Role player = SpringUtils.getRoleService().getPlayer(roleId);
            if (player == null) {
                return this.result("500", "未找到该角色");
            }
            final boolean checkPos = this.bagService.checkPos(player, serial, num);
            if (!checkPos) {
                return this.result("500", "玩家物品栏空间不足");
            }
            this.bagService.addGoods(player, serial, num, 0, true, false, "后台发送");
            return this.result("0", "发送成功");
        }
        catch (final Exception e) {
            return this.result("500", "操作异常");
        }
    }
    
    @Override
    public String gamers(final String gamers) {
        try {
            if (gamers != null) {
                final Map<String, Object> toMap = MapStringUtil.getStringToMap(gamers);
                final Integer type = Integer.valueOf(String.valueOf(toMap.get("type")));
                if (type == 1) {
                    final long uid = SpringUtils.getPlayerService().getUidBy(String.valueOf(toMap.get("name")));
                    final Role role = this.roleService.getPlayer(uid);
                    if (role != null) {
                        return this.packaging(role);
                    }
                }
                else {
                    final String name = String.valueOf(toMap.get("name"));
                    if (!NumberUtil.isNumber(name)) {
                        return this.result("500", "请输入数字编号");
                    }
                    final Role role2 = this.roleService.getPlayer(Integer.valueOf(name));
                    if (role2 != null) {
                        return this.packaging(role2);
                    }
                }
            }
            return this.result("500", "角色不在线或不存在");
        }
        catch (final Exception e) {
            return this.result("500", "操作异常");
        }
    }
    
    @Override
    public String exp(final String exp) {
        try {
            if (exp == null) {
                return this.result("500", "未知操作");
            }
            final Map<String, Object> toMap = MapStringUtil.getStringToMap(exp);
            final Integer roleId = Integer.valueOf(String.valueOf(toMap.get("name")));
            final Long num = Long.valueOf(String.valueOf(toMap.get("num")));
            final Role player = SpringUtils.getRoleService().getPlayer(roleId);
            if (player != null) {
                this.roleService.addExp(player, num, "系统", true);
                return this.result("0", "发送成功");
            }
            return this.result("500", "未找到该角色");
        }
        catch (final Exception e) {
            return this.result("500", "操作异常");
        }
    }
    
    @Override
    public String task(final String task) {
        try {
            if (task == null) {
                return this.result("500", "角色不在线或不存在");
            }
            final Map<String, Object> toMap = MapStringUtil.getStringToMap(task);
            final Integer taskId = Integer.valueOf(String.valueOf(toMap.get("taskId")));
            final TaskSet taskSet = this.taskService.getTaskSet(taskId);
            if (taskSet == null) {
                return this.result("500", "没有该任务");
            }
            final Integer name = Integer.valueOf(String.valueOf(toMap.get("name")));
            final Role role = this.roleService.getPlayer(name);
            role.getTask().updateToFinished(taskSet.getSerial());
            role.save();
            final RespTasksDel respTasksDel = new RespTasksDel();
            respTasksDel.setSerial(taskSet.getSerial());
            respTasksDel.setType((byte)2);
            MessagePusher.pushMessage(role, respTasksDel);
            return this.result("0", "成功放弃任务");
        }
        catch (final Exception e) {
            return this.result("500", "操作异常");
        }
    }
    
    @Override
    public String inquire(final String inquire) {
        try {
            if (inquire != null) {
                final List<Inquire> objects = new ArrayList<Inquire>();
                final Map<String, Object> toMap = MapStringUtil.getStringToMap(inquire);
                final String sid = String.valueOf(toMap.get("sid"));
                final List<PlayerProfile> profiles = SpringUtils.getPlayerService().getPlayersBy(sid);
                if (profiles != null && profiles.size() > 0) {
                    for (final PlayerProfile profile : profiles) {
                        final Role r = this.roleService.getPlayer(profile.getId());
                        if (r != null) {
                            final long totalGold = r.getTotalGold();
                            objects.add(new Inquire(r.getId(), r.getRoleId(), r.getName(), r.getSerial(), r.getRelive().byteValue(), r.getLevel().shortValue(), r.getGold().toString(), r.getSilver().toString(), String.valueOf(totalGold)));
                        }
                    }
                }
                final String data = JsonUtils.object2String(objects);
                return data;
            }
            return this.result("500", "[]");
        }
        catch (final Exception e) {
            return this.result("500", "[]");
        }
    }
    
    @Override
    public String market(final String market) {
        try {
            if (market != null) {
                final Map<String, Object> toMap = MapStringUtil.getStringToMap(market);
                final int type = Integer.valueOf(String.valueOf(toMap.get("type")));
                final int classify = Integer.valueOf(String.valueOf(toMap.get("classify")));
                final long id = Long.valueOf(String.valueOf(toMap.get("id")));
                switch (type) {
                    case 0: {
                        final byte status = Byte.valueOf(String.valueOf(toMap.get("status")));
                        final String msg = this.marketService.updateMarketStatus(classify, id, status);
                        if (msg != null) {
                            return this.result("500", msg);
                        }
                        break;
                    }
                    case 1: {
                        final String msg2 = this.marketService.withdraw(classify, id);
                        if (msg2 != null) {
                            return this.result("500", msg2);
                        }
                        break;
                    }
                    case 2: {
                        final String msg2 = this.marketService.deleteMarket(classify, id);
                        if (msg2 != null) {
                            return this.result("500", msg2);
                        }
                        break;
                    }
                    case 3: {
                        final Role player = SpringUtils.getRoleService().getPlayer(id);
                        if (player != null) {
                            player.getExtendBox().setBanTrade(classify);
                            player.save();
                            break;
                        }
                        return this.result("500", "未找到玩家信息");
                    }
                    default: {
                        return this.result("500", "未匹配到操作");
                    }
                }
            }
            return this.result("0", "操作成功");
        }
        catch (final Exception e) {
            return this.result("500", "未知错误");
        }
    }
    
    @Override
    public String load(final String load) {
        try {
            if (load != null) {
                final Map<String, Object> toMap = MapStringUtil.getStringToMap(load);
                final int type = Integer.valueOf(String.valueOf(toMap.get("type")));
                switch (type) {
                    case 0: {
                        SpringUtils.getBean(ServerService.class).versions(this.initService.initVersion());
                        break;
                    }
                    case 1: {
                        WordDisposePool.load();
                        break;
                    }
                    case 2: {
                        WelfareDataPool.loadGiftBag(this.initService);
                        break;
                    }
                    case 3: {
                        final int id = Integer.valueOf(String.valueOf(toMap.get("id")));
                        CxfDataPool.load(id);
                        break;
                    }
                    default: {
                        return this.result("500", "未匹配到操作");
                    }
                }
            }
            return this.result("0", "更新成功");
        }
        catch (final Exception e) {
            return this.result("500", "未知错误");
        }
    }
    
    @Override
    public String gift(final String gift) {
        try {
            if (gift != null) {
                final BrieGift brieGift = (BrieGift)new Gson().fromJson(gift, (Class)BrieGift.class);
                if (brieGift.getTime() < System.currentTimeMillis()) {
                    return this.result("500", "过期时间不能小于当前时间");
                }
                final GiftBag giftBag = new GiftBag();
                giftBag.setName(brieGift.getName());
                giftBag.setTime(brieGift.getTime());
                giftBag.setRewards(brieGift.getList());
                WelfareDataPool.giftBagMap.put(giftBag.getName(), giftBag);
                this.dao.insert((Object)giftBag);
            }
            return this.result("0", "更新成功");
        }
        catch (final Exception e) {
            return this.result("500", "未知错误");
        }
    }
    
    @Override
    public String adjustRoleGrade(final String adjustRoleGrade) {
        try {
            if (adjustRoleGrade != null) {
                final Map<String, Object> toMap = MapStringUtil.getStringToMap(adjustRoleGrade);
                final int name = Integer.valueOf(String.valueOf(toMap.get("name")));
                final int grade = Integer.valueOf(String.valueOf(toMap.get("grade")));
                if (!this.gmService.adjustRoleGrade(name, grade)) {
                    return this.result("500", "操作失败");
                }
            }
            return this.result("0", "操作成功");
        }
        catch (final Exception e) {
            return this.result("500", "未知错误");
        }
    }
    
    @Override
    public String pet(final String pet) {
        try {
            if (pet != null) {
                final String json = String.valueOf(pet);
                final BriefPet briefPet = (BriefPet)new Gson().fromJson(json, (Class)BriefPet.class);
                if (!this.gmService.customPet(briefPet)) {
                    return this.result("500", "操作失败");
                }
            }
            return this.result("0", "操作成功");
        }
        catch (final Exception e) {
            return this.result("500", "未知错误");
        }
    }
    
    @Override
    public String horse(final String horse) {
        try {
            if (horse != null) {
                final String json = String.valueOf(horse);
                final BriefHorse briefHorse = (BriefHorse)new Gson().fromJson(json, (Class)BriefHorse.class);
                if (!this.gmService.customHorse(briefHorse)) {
                    return this.result("500", "操作失败");
                }
            }
            return this.result("0", "操作成功");
        }
        catch (final Exception e) {
            return this.result("500", "未知错误");
        }
    }
    
    @Override
    public String rune(final String rune) {
        try {
            if (rune != null) {
                final Map<String, Object> toMap = MapStringUtil.getStringToMap(rune);
                final int name = Integer.valueOf(String.valueOf(toMap.get("name")));
                final int serial = Integer.valueOf(String.valueOf(toMap.get("serial")));
                final byte grade = Byte.valueOf(String.valueOf(toMap.get("grade")));
                if (!this.gmService.customRune(name, serial, grade)) {
                    return this.result("500", "操作失败");
                }
            }
            return this.result("0", "操作成功");
        }
        catch (final Exception e) {
            return this.result("500", "未知错误");
        }
    }
    
    @Override
    public String magical(final String magical) {
        try {
            if (magical != null) {
                final String json = String.valueOf(magical);
                final BriefMagical briefMagical = (BriefMagical)new Gson().fromJson(json, (Class)BriefMagical.class);
                final Role role = this.roleService.getPlayer(briefMagical.getId());
                if (role == null) {
                    return this.result("500", "没有该角色");
                }
                final int pos = this.bagService.getPos(role, false);
                if (pos <= 0) {
                    this.result("500", "你的物品栏已满");
                }
                if (!this.gmService.customMagical(role, pos, briefMagical)) {
                    return this.result("500", "操作失败");
                }
            }
            return this.result("0", "操作成功");
        }
        catch (final Exception e) {
            return this.result("500", "未知错误");
        }
    }
    
    @Override
    public String sin(final String sin) {
        try {
            if (sin != null) {
                final String json = String.valueOf(sin);
                final BriefSin briefSin = (BriefSin)new Gson().fromJson(json, (Class)BriefSin.class);
                final Role role = this.roleService.getPlayer(briefSin.getId());
                if (role == null) {
                    return this.result("500", "没有该角色");
                }
                final int pos = this.bagService.getPos(role, false);
                if (pos <= 0) {
                    this.result("500", "你的物品栏已满");
                }
                if (!this.gmService.customSin(role, pos, briefSin)) {
                    return this.result("500", "操作失败");
                }
            }
            return this.result("0", "操作成功");
        }
        catch (final Exception e) {
            e.printStackTrace();
            return this.result("500", "未知错误");
        }
    }
    
    @Override
    public String acsry(final String acsry) {
        try {
            if (acsry != null) {
                final String json = String.valueOf(acsry);
                final BriefAcsry briefAcsry = (BriefAcsry)new Gson().fromJson(json, (Class)BriefAcsry.class);
                final Role role = this.roleService.getPlayer(briefAcsry.getId());
                if (role == null) {
                    return this.result("500", "没有该角色");
                }
                final int pos = this.bagService.getPos(role, false);
                if (pos <= 0) {
                    this.result("500", "你的物品栏已满");
                }
                if (!this.gmService.customAcsry(role, pos, briefAcsry)) {
                    return this.result("500", "操作失败");
                }
            }
            return this.result("0", "操作成功");
        }
        catch (final Exception e) {
            e.printStackTrace();
            return this.result("500", "未知错误");
        }
    }
    
    @Override
    public String acsryPet(final String acsryPet) {
        try {
            if (acsryPet != null) {
                final String json = String.valueOf(acsryPet);
                final BriePetfAcsry briePetfAcsry = (BriePetfAcsry)new Gson().fromJson(json, (Class)BriePetfAcsry.class);
                final Role role = this.roleService.getPlayer(briePetfAcsry.getId());
                if (role == null) {
                    return this.result("500", "没有该角色");
                }
                final int pos = this.bagService.getPos(role, true);
                if (pos <= 0) {
                    this.result("500", "物品栏空间不足");
                }
                if (!this.gmService.customAcsryPet(role, pos, briePetfAcsry)) {
                    return this.result("500", "操作失败");
                }
            }
            return this.result("0", "操作成功");
        }
        catch (final Exception e) {
            return this.result("500", "未知错误");
        }
    }
    
    private String packaging(final Role role) {
        final Map<String, Object> map = new HashMap<String, Object>(10);
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
        final long totalGold = role.getTotalGold();
        map.put("total", totalGold);
        map.put("lock", role.getSafePwd());
        return MapStringUtil.getMapToString(map);
    }
    
    private String result(final String code, final String msg) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("msg", msg);
        return MapStringUtil.getMapToString(map);
    }
    
    private String result(final String code, final String msg, final String data) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        return MapStringUtil.getMapToString(map);
    }
    
    @Override
    public String email(final String email) {
        try {
            if (email != null) {
                final String json = String.valueOf(email);
                final BriefEmail briefEmail = (BriefEmail)new Gson().fromJson(json, (Class)BriefEmail.class);
                long recipient = 0L;
                if (briefEmail.getRec() > 0) {
                    final Role player = this.roleService.getPlayer(briefEmail.getRec());
                    if (player == null) {
                        return this.result("500", "用户不存在");
                    }
                    recipient = player.getId();
                }
                final EmailSystem emailSystem = new EmailSystem();
                emailSystem.setEmailTitle(briefEmail.getTitle());
                emailSystem.setSender(briefEmail.getSen());
                emailSystem.setRecipient(recipient);
                emailSystem.setContent(briefEmail.getCon());
                emailSystem.setExpirationTime(briefEmail.getTime());
                emailSystem.setAccessory(briefEmail.getList());
                this.emailService.addEmailSystem(emailSystem);
                return this.result("0", "操作成功");
            }
            return this.result("500", "未知错误");
        }
        catch (final Exception e) {
            return this.result("500", "未知错误");
        }
    }
    
    @Override
    public String delEmail(final String emailId) {
        final Map<String, Object> toMap = MapStringUtil.getStringToMap(emailId);
        final Long eid = Long.parseLong(String.valueOf(toMap.get("eid")));
        this.emailService.deleteEmailSystem(eid);
        return this.result("0", "操作成功");
    }
}
