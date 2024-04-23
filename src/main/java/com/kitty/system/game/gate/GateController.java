package com.kitty.system.game.gate;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.filter.ModuleCounterFilter;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.session.SessionProperties;
import com.kitty.system.game.account.domain.Account;
import com.kitty.system.game.account.service.AccountService;
import com.kitty.system.game.gate.message.LineData;
import com.kitty.system.game.gate.message.ReqKickOffClient;
import com.kitty.system.game.gate.message.ReqLoginAccount;
import com.kitty.system.game.gate.message.ReqLoginPreview;
import com.kitty.system.game.gate.message.ReqlineData;
import com.kitty.system.game.gate.message.RespAgentResult;
import com.kitty.system.game.gate.message.RespLoginPreview;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.system.game.gate.message.ResplineData;
import com.kitty.system.game.gate.model.QueueUpService;
import com.kitty.system.game.role.message.ReqRequestLineInfo;
import com.kitty.system.game.role.message.RespStartLogin;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.mina.core.session.IoSession;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GateController {
  Logger logger = LoggerFactory.getLogger(GateController.class);
  
  @Autowired
  QueueUpService queueUpService;
  
  @Autowired
  ServerService serverService;
  
  @RequestMapping
  public void loginToDist(IoSession session, ReqRequestLineInfo request) {
    String sid = request.getAccountId();
    try {
      if (sid.length() != "00SgzcIo".length()) {
        ModuleCounterFilter.banIp(session);
        session.closeNow();
        return;
      } 
      Account account = ((AccountService)SpringUtils.getBean(AccountService.class)).getAccount(sid);
      if (account == null || !account.getId().equals(sid)) {
        RespOtherLogin respOtherLogin = new RespOtherLogin();
        respOtherLogin.setContent("账号登录失败。（原因：9021）");
        respOtherLogin.setResult(Integer.valueOf(0));
        MessagePusher.pushMessage(session, (Message)respOtherLogin);
        ModuleCounterFilter.banIp(session);
        this.logger.error("没有账号=={}", request.getAccountId());
        session.closeNow();
        return;
      } 
      if (!SpringUtils.getRoleService().canLogin(account, session)) {
        this.logger.error("账号登录失败。（原因：9022）");
        session.closeNow();
        return;
      } 
      session.setAttribute(SessionProperties.ACCOUNT_SID, sid);
      RespStartLogin respStartLogin = new RespStartLogin();
      respStartLogin.setCookie("4526387");
      respStartLogin.setType("normal");
      MessagePusher.pushMessage(session, (Message)respStartLogin);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  @RequestMapping
  public void requestLineData(IoSession session, ReqlineData reqlineData) {
    ResplineData resplineData = new ResplineData();
    resplineData.setDatas(new ArrayList(Collections.singleton(new LineData())));
    LineData lineData = new LineData();
    lineData.setLine_name(((ServerService)SpringUtils.getBean(ServerService.class)).getName());
    lineData.setExpect_time(500);
    resplineData.getDatas().add(lineData);
    MessagePusher.pushMessage(session, (Message)resplineData);
  }
  
  @RequestMapping
  public void kickOffClient(IoSession session, ReqKickOffClient offClient) {
    this.logger.error("你已被强制下线");
  }
  
  @RequestMapping
  public void loginAccount(IoSession session, ReqLoginAccount reqLoginAccount) {
    if (this.serverService.getState() <= 0) {
      RespOtherLogin respOtherLogin = new RespOtherLogin();
      respOtherLogin.setContent("游戏维护中");
      respOtherLogin.setResult(Integer.valueOf(4));
      MessagePusher.pushMessage(session, (Message)respOtherLogin);
      return;
    } 
    if ("cmd_l_login_preview_player".equals(reqLoginAccount.getType())) {
      RespLoginPreview preview = new RespLoginPreview();
      preview.setCookie("4526387");
      preview.setSid(reqLoginAccount.getAccountId());
      MessagePusher.pushMessage(session, (Message)preview);
      session.closeNow();
      return;
    } 
    ServerService service = (ServerService)SpringUtils.getBean(ServerService.class);
    String versions = service.getVersions();
    String engine = service.getEngine();
    if (!versions.equals(reqLoginAccount.getGameV()) || !engine.equals(reqLoginAccount.getEngV())) {
      RespOtherLogin respOtherLogin = new RespOtherLogin();
      respOtherLogin.setContent("请更新游戏");
      respOtherLogin.setResult(Integer.valueOf(4));
      MessagePusher.pushMessage(session, (Message)respOtherLogin);
      return;
    } 
    String sid = (String)SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.ACCOUNT_SID, String.class);
    if (Strings.isEmpty(sid)) {
      RespOtherLogin respOtherLogin = new RespOtherLogin();
      respOtherLogin.setContent("账号登录失败。#r(原因：9023)");
      respOtherLogin.setResult(Integer.valueOf(0));
      MessagePusher.pushMessage(session, (Message)respOtherLogin);
      ModuleCounterFilter.banIp(session);
      session.closeNow();
      this.logger.error("没有账号异常==1111");
      return;
    } 
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    LoginAuth request = new LoginAuth();
    DataCache.LOGIN_AUTH_MAP.put(sid, request);
    RespAgentResult respAgentResult = new RespAgentResult();
    respAgentResult.setAuth_key(request.getAuthKey());
    respAgentResult.setMsg("允许a该账号1登录");
    respAgentResult.setResult(1);
    MessagePusher.pushMessage(session, (Message)respAgentResult);
  }
  
  @RequestMapping
  public void previewLogin(IoSession session, ReqLoginPreview preview) {
    RespStartLogin respStartLogin = new RespStartLogin();
    respStartLogin.setCookie("4526387");
    respStartLogin.setType("cmd_l_login_preview_player");
    MessagePusher.pushMessage(session, (Message)respStartLogin);
  }
}
