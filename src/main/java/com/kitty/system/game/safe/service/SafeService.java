package com.kitty.system.game.safe.service;

import com.kitty.common.utils.TimeUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.safe.domain.ErrorRecord;
import com.kitty.system.game.safe.message.ReqModifyPassword;
import com.kitty.system.game.safe.message.ReqSetSafePassword;
import com.kitty.system.game.safe.message.ReqUnlock;
import com.kitty.system.game.safe.util.SafeUtil;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.mina.core.session.IoSession;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SafeService {
  private final Logger logger = LoggerFactory.getLogger(SafeService.class);
  
  @Autowired
  RoleService roleService;
  
  private ConcurrentMap<Long, ErrorRecord> errorRecords = new ConcurrentHashMap<>();
  
  public void setSafePassword(IoSession session, ReqSetSafePassword reqSetSafePassword) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    if (!Strings.isEmpty(role.getSafePwd()))
      return; 
    role.setSafePwd(reqSetSafePassword.getPassword());
    role.save();
    MessagePusher.pushMessage(session, (Message)new RespMsg("安全锁密码设置成功。"));
    role.setUnlock(true);
    MessagePusher.pushMessage(session, (Message)SafeUtil.getLockStatus(role));
  }
  
  public void openSafeDlg(IoSession session) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    if (Strings.isEmpty(role.getSafePwd())) {
      MessagePusher.pushMessage(session, (Message)new RespMsg("你当前没有设置安全锁密码，请先设置安全锁密码"));
      return;
    } 
    role.setUnlock(false);
    MessagePusher.pushMessage(session, (Message)SafeUtil.getLockStatus(role));
    MessagePusher.pushMessage(session, (Message)new RespMsg("安全锁上锁成功"));
  }
  
  public void unlock(IoSession session, ReqUnlock reqUnlock) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    long now = System.currentTimeMillis();
    long roleUid = role.getId().longValue();
    int total = getErrorNum(role);
    if (total >= 5) {
      MessagePusher.pushMessage(session, (Message)new RespMsg("错误次数太多"));
      return;
    } 
    if (reqUnlock.getPassword().equals(role.getSafePwd())) {
      role.setUnlock(true);
      MessagePusher.pushMessage(session, (Message)new RespMsg("安全锁密码验证成功。"));
      MessagePusher.pushMessage(session, (Message)SafeUtil.getLockStatus(role));
      ((NpcService)SpringUtils.getBean(NpcService.class)).closeDlg(role, "safeLock");
    } else {
      this.errorRecords.putIfAbsent(Long.valueOf(roleUid), new ErrorRecord());
      ((ErrorRecord)this.errorRecords.get(Long.valueOf(roleUid))).addErrorCount();
      ((ErrorRecord)this.errorRecords.get(Long.valueOf(roleUid))).setLastErrorTime(now);
      MessagePusher.pushMessage(session, (Message)new RespMsg(String.format("密码不正确，今天您还有剩余%d次机会。(每天五点重置次数)", new Object[] { Integer.valueOf(5 - total) })));
    } 
  }
  
  public void modifyPassword(IoSession session, ReqModifyPassword reqModifyPassword) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    if (role.getSafePwd().equals(reqModifyPassword.getOldPassword())) {
      role.setSafePwd(reqModifyPassword.getNewPassword());
      role.save();
      MessagePusher.pushMessage(session, (Message)new RespMsg("修改安全锁密码成功。"));
    } else {
      MessagePusher.pushMessage(session, (Message)new RespMsg("原始安全锁密码错误。"));
    } 
  }
  
  public void reqReset(Role role, byte type) {
    if (Strings.isEmpty(role.getSafePwd())) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你当前没有设置安全锁密码，请先设置安全锁密码。"));
      return;
    } 
    if (type == 1) {
      role.setSafeResetStartTime((int)(System.currentTimeMillis() / 1000L));
      role.save();
      MessagePusher.pushMessage(role, (Message)new RespMsg("时间解锁审查开始"));
    } 
    MessagePusher.pushMessage(role, (Message)SafeUtil.getLockStatus(role));
  }
  
  public void cancelReset(Role role) {
    if (role.getSafeResetStartTime() <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("当前没有申请安全锁强制解除。"));
      return;
    } 
    role.setSafeResetStartTime(0);
    role.save();
    MessagePusher.pushMessage(role, (Message)SafeUtil.getLockStatus(role));
  }
  
  public void checkSafePasswordReset(Role role) {
    if (role == null)
      return; 
    this.errorRecords.remove(role.getId());
    int endTime = SafeUtil.getSafePasswordResetEndTime(role.getSafeResetStartTime());
    if (endTime <= 0)
      return; 
    long now = System.currentTimeMillis();
    if (now / 1000L >= endTime) {
      this.logger.warn("{}({})进行安全锁重置，重置时间为:{}", new Object[] { role.getId(), role.getName(), TimeUtil.formatDate(new Date(endTime * 1000L), "yyyy-MM-dd HH:mm:ss") });
      role.setSafeResetStartTime(0);
      role.setSafePwd(null);
      role.save();
      MessagePusher.pushMessage(role, (Message)SafeUtil.getLockStatus(role));
    } 
  }
  
  public int getErrorNum(Role role) {
    int total = 0;
    long roleUid = role.getId().longValue();
    long now = System.currentTimeMillis();
    if (this.errorRecords.containsKey(Long.valueOf(roleUid))) {
      long lastErrorTime = ((ErrorRecord)this.errorRecords.get(Long.valueOf(roleUid))).getLastErrorTime();
      if (now - lastErrorTime > 86400000L) {
        this.errorRecords.remove(Long.valueOf(roleUid));
      } else {
        total = ((ErrorRecord)this.errorRecords.get(Long.valueOf(roleUid))).getErrorSum();
      } 
    } 
    return total;
  }
}
