
package com.kitty.system.game.safe.service;

import com.kitty.common.utils.TimeUtil;
import java.util.Date;
import com.kitty.system.game.safe.message.ReqModifyPassword;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.safe.message.ReqUnlock;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import org.nutz.lang.Strings;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.safe.message.ReqSetSafePassword;
import org.apache.mina.core.session.IoSession;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.safe.domain.ErrorRecord;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SafeService
{
    private final Logger logger;
    @Autowired
    RoleService roleService;
    private ConcurrentMap<Long, ErrorRecord> errorRecords;
    
    public SafeService() {
        this.logger = LoggerFactory.getLogger(SafeService.class);
        this.errorRecords = new ConcurrentHashMap<Long, ErrorRecord>();
    }
    
    public void setSafePassword(final IoSession session, final ReqSetSafePassword reqSetSafePassword) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (!Strings.isEmpty((CharSequence)role.getSafePwd())) {
            return;
        }
        role.setSafePwd(reqSetSafePassword.getPassword());
        role.save();
        MessagePusher.pushMessage(session, new RespMsg("安全锁密码设置成功。"));
        role.setUnlock(true);
        MessagePusher.pushMessage(session, SafeUtil.getLockStatus(role));
    }
    
    public void openSafeDlg(final IoSession session) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (Strings.isEmpty((CharSequence)role.getSafePwd())) {
            MessagePusher.pushMessage(session, new RespMsg("你当前没有设置安全锁密码，请先设置安全锁密码"));
            return;
        }
        role.setUnlock(false);
        MessagePusher.pushMessage(session, SafeUtil.getLockStatus(role));
        MessagePusher.pushMessage(session, new RespMsg("安全锁上锁成功"));
    }
    
    public void unlock(final IoSession session, final ReqUnlock reqUnlock) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final long now = System.currentTimeMillis();
        final long roleUid = role.getId();
        final int total = this.getErrorNum(role);
        if (total >= 5) {
            MessagePusher.pushMessage(session, new RespMsg("错误次数太多"));
            return;
        }
        if (reqUnlock.getPassword().equals(role.getSafePwd())) {
            role.setUnlock(true);
            MessagePusher.pushMessage(session, new RespMsg("安全锁密码验证成功。"));
            MessagePusher.pushMessage(session, SafeUtil.getLockStatus(role));
            SpringUtils.getBean(NpcService.class).closeDlg(role, "safeLock");
        }
        else {
            this.errorRecords.putIfAbsent(roleUid, new ErrorRecord());
            this.errorRecords.get(roleUid).addErrorCount();
            this.errorRecords.get(roleUid).setLastErrorTime(now);
            MessagePusher.pushMessage(session, new RespMsg(String.format("密码不正确，今天您还有剩余%d次机会。(每天五点重置次数)", 5 - total)));
        }
    }
    
    public void modifyPassword(final IoSession session, final ReqModifyPassword reqModifyPassword) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (role.getSafePwd().equals(reqModifyPassword.getOldPassword())) {
            role.setSafePwd(reqModifyPassword.getNewPassword());
            role.save();
            MessagePusher.pushMessage(session, new RespMsg("修改安全锁密码成功。"));
        }
        else {
            MessagePusher.pushMessage(session, new RespMsg("原始安全锁密码错误。"));
        }
    }
    
    public void reqReset(final Role role, final byte type) {
        if (Strings.isEmpty((CharSequence)role.getSafePwd())) {
            MessagePusher.pushMessage(role, new RespMsg("你当前没有设置安全锁密码，请先设置安全锁密码。"));
            return;
        }
        if (type == 1) {
            role.setSafeResetStartTime((int)(System.currentTimeMillis() / 1000L));
            role.save();
            MessagePusher.pushMessage(role, new RespMsg("时间解锁审查开始"));
        }
        MessagePusher.pushMessage(role, SafeUtil.getLockStatus(role));
    }
    
    public void cancelReset(final Role role) {
        if (role.getSafeResetStartTime() <= 0) {
            MessagePusher.pushMessage(role, new RespMsg("当前没有申请安全锁强制解除。"));
            return;
        }
        role.setSafeResetStartTime(0);
        role.save();
        MessagePusher.pushMessage(role, SafeUtil.getLockStatus(role));
    }
    
    public void checkSafePasswordReset(final Role role) {
        if (role == null) {
            return;
        }
        this.errorRecords.remove(role.getId());
        final int endTime = SafeUtil.getSafePasswordResetEndTime(role.getSafeResetStartTime());
        if (endTime <= 0) {
            return;
        }
        final long now = System.currentTimeMillis();
        if (now / 1000L >= endTime) {
            this.logger.warn("{}({})进行安全锁重置，重置时间为:{}", new Object[] { role.getId(), role.getName(), TimeUtil.formatDate(new Date(endTime * 1000L), "yyyy-MM-dd HH:mm:ss") });
            role.setSafeResetStartTime(0);
            role.setSafePwd(null);
            role.save();
            MessagePusher.pushMessage(role, SafeUtil.getLockStatus(role));
        }
    }
    
    public int getErrorNum(final Role role) {
        int total = 0;
        final long roleUid = role.getId();
        final long now = System.currentTimeMillis();
        if (this.errorRecords.containsKey(roleUid)) {
            final long lastErrorTime = this.errorRecords.get(roleUid).getLastErrorTime();
            if (now - lastErrorTime > 86400000L) {
                this.errorRecords.remove(roleUid);
            }
            else {
                total = this.errorRecords.get(roleUid).getErrorSum();
            }
        }
        return total;
    }
}
