package com.kitty.system.admin.service;

import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.record.RecordThread;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.init.domain.SysCharge;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.gate.message.RespOtherLogin;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.kitty.system.game.role.domain.Role;
import org.springframework.scheduling.annotation.Async;
import com.kitty.logs.LoggerFunction;
import com.kitty.cxf.send.service.GameSendService;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class AdminService
{
    Logger logger;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private ServerService serverService;
    @Autowired
    private GameSendService gameSendService;
    
    public AdminService() {
        this.logger = LoggerFunction.GM.getLogger();
    }
    
    @Async("asyncServiceExecutor")
    public void asyncThreadNotice(String msg, int num, int millis) {
        try {
            for (int i = 0; i < num; ++i) {
                this.chatService.sendAdnotice(msg);
                if (millis > 0) {
                    Thread.sleep(millis * 1000);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void shutChannel(Role target, long time) {
        if (time <= 0L) {
            target.setWordTime(Long.valueOf(0L));
        }
        else {
            target.setWordTime(Long.valueOf(time));
        }
        target.save();
    }
    
    public void blockPlayer(Role target, long time, String cause) {
        if (time <= 0L) {
            target.setLockTime(0L);
        }
        else {
            target.setLockTime(time);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(target.getLockTime());
            String tips = simpleDateFormat.format(date);
            RespOtherLogin respOtherLogin = new RespOtherLogin();
            respOtherLogin.setContent("当前角色被封闭至#<R>" + tips + "#。");
            respOtherLogin.setResult(2);
            MessagePusher.pushMessage(target, respOtherLogin);
            if (cause != null) {
                this.gameSendService.sendSeal(target.getSid(), target.getName(), cause, tips);
            }
        }
        target.save();
    }
    
    public short recharge(Role target, int serial) {
        SysCharge charge = WelfareDataPool.chargeMap.get(serial);
        if (charge == null) {
            return 0;
        }
        long gold = charge.getAmount() * this.serverService.getRatio();
        int tc = target.getTc() + charge.getAmount();
        target.setTc(tc);
        long total_gold = target.getTotalGold();
        target.setTotalGold(total_gold + gold);
        this.roleService.addCurrency(target, Const.RESTYPE_GOLD, gold + charge.getGive(), "充值成功", true, true);
        if (charge.getScore() > 0L) {
            this.roleService.addCurrency(target, Const.RES_TYPE_CHARGE_SCORE, charge.getScore(), "角色充值", true, true);
        }
        NutConcurrentMap dayRecharge = target.getTaskProgress().getEveryDayRecharge();
        String when = TimeUtil.when();
        long dayGold = dayRecharge.getLong(when + "_gold", 0L);
        dayRecharge.setv(when + "_gold", dayGold + gold);
        int dayCash = dayRecharge.getInt(when + "_cash", 0);
        dayRecharge.setv(when + "_cash", dayCash + charge.getAmount());
        target.save();
        RecordThread.getInstance().add2Queue(target.getId(), target.getRoleId(), (short)999, (byte)1, String.format("充值，金额：%d", Integer.valueOf(charge.getAmount())), target.getMarking());
        return 1;
    }
    
    public short customRecharge(Role target, int amount) {
        long gold = amount * this.serverService.getRatio();
        int tc = target.getTc() + amount;
        target.setTc(tc);
        long total_gold = target.getTotalGold();
        target.setTotalGold(total_gold + gold);
        this.roleService.addCurrency(target, Const.RESTYPE_GOLD, gold, "角色自定义充值", true, true);
        int score = amount / 10;
        this.roleService.addCurrency(target, Const.RES_TYPE_CHARGE_SCORE, (long) score, "角色自定义充值", true, true);
        NutConcurrentMap dayRecharge = target.getTaskProgress().getEveryDayRecharge();
        String when = TimeUtil.when();
        long dayGold = dayRecharge.getLong(when + "_gold", 0L);
        dayRecharge.setv(when + "_gold", dayGold + gold);
        int dayCash = dayRecharge.getInt(when + "_cash", 0);
        dayRecharge.setv(when + "_cash", dayCash + amount);
        target.save();
        RecordThread.getInstance().add2Queue(target.getId(), target.getRoleId(), (short)999, (byte)1, String.format("自定义充值充值，金额：%d", Integer.valueOf(amount)), target.getMarking());
        return 1;
    }
}
