package com.kitty.system.game.safe.util;

import java.util.Calendar;
import com.kitty.system.game.safe.message.RespSafeLockDlg;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import org.nutz.lang.Strings;
import com.kitty.system.game.role.domain.Role;

public class SafeUtil
{
    public SafeUtil() {
    }
    
    public static boolean isUnlock(Role role) {
        if (role != null && !Strings.isEmpty(role.getSafePwd()) && !role.isUnlock()) {
            MessagePusher.pushMessage((long)role.getId(), new RespNotifyMiscEx(role.getId(), "您正在处于#<LB1>安全锁##<R>上锁#状态，该操作无法执行。", true));
            ((NpcService)SpringUtils.getBean(NpcService.class)).openDlg(role, "safeLock");
            return false;
        }
        return true;
    }
    
    public static RespSafeLockDlg getLockStatus(Role role) {
        boolean isSet = !Strings.isEmpty(role.getSafePwd());
        RespSafeLockDlg respSafeLockDlg = new RespSafeLockDlg();
        respSafeLockDlg.setUnlock(role.isUnlock());
        respSafeLockDlg.setSet(isSet);
        respSafeLockDlg.setReset(role.getSafeResetStartTime());
        return respSafeLockDlg;
    }
    
    public static int getSafePasswordResetEndTime(int resetStartTime) {
        if (resetStartTime <= 0) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)resetStartTime * 1000L);
        if (calendar.get(11) < 5) {
            calendar.add(6, 6);
        }
        else {
            calendar.add(6, 7);
        }
        calendar.set(11, 5);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return (int)(calendar.getTimeInMillis() / 1000L);
    }
}
