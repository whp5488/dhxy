
package com.kitty.system.game.gate;

import com.kitty.system.game.gate.message.ReqLoginPreview;
import com.kitty.system.game.gate.message.RespAgentResult;
import com.kitty.mina.cache.DataCache;
import org.apache.commons.lang3.time.StopWatch;
import org.nutz.lang.Strings;
import com.kitty.mina.session.SessionManager;
import com.kitty.system.game.gate.message.RespLoginPreview;
import com.kitty.system.game.gate.message.ReqLoginAccount;
import com.kitty.system.game.gate.message.ReqKickOffClient;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import com.kitty.system.game.gate.message.LineData;
import com.kitty.system.game.gate.message.ResplineData;
import com.kitty.system.game.gate.message.ReqlineData;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.account.domain.Account;
import com.kitty.system.game.role.message.RespStartLogin;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.gate.message.RespOtherLogin;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.account.service.AccountService;
import com.kitty.mina.filter.ModuleCounterFilter;
import com.kitty.system.game.role.message.ReqRequestLineInfo;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import com.kitty.common.start.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.gate.model.QueueUpService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class GateController
{
    Logger logger;
    @Autowired
    QueueUpService queueUpService;
    @Autowired
    ServerService serverService;
    
    public GateController() {
        this.logger = LoggerFactory.getLogger(GateController.class);
    }
    
    @RequestMapping
    public void loginToDist(final IoSession session, final ReqRequestLineInfo request) {
        final String sid = request.getAccountId();
        try {
            if (sid.length() != "00SgzcIo".length()) {
                ModuleCounterFilter.banIp(session);
                session.closeNow();
                return;
            }
            final Account account = SpringUtils.getBean(AccountService.class).getAccount(sid);
            if (account == null || !account.getId().equals(sid)) {
                final RespOtherLogin respOtherLogin = new RespOtherLogin();
                respOtherLogin.setContent("账号登录失败。（原因：9021）");
                respOtherLogin.setResult(0);
                MessagePusher.pushMessage(session, respOtherLogin);
                ModuleCounterFilter.banIp(session);
                this.logger.error("没有账号=={}", (Object)request.getAccountId());
                session.closeNow();
                return;
            }
            if (!SpringUtils.getRoleService().canLogin(account, session)) {
                this.logger.error("账号登录失败。（原因：9022）");
                session.closeNow();
                return;
            }
            session.setAttribute((Object)SessionProperties.ACCOUNT_SID, (Object)sid);
            final RespStartLogin respStartLogin = new RespStartLogin();
            respStartLogin.setCookie("4526387");
            respStartLogin.setType("normal");
            MessagePusher.pushMessage(session, respStartLogin);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping
    public void requestLineData(final IoSession session, final ReqlineData reqlineData) {
        final ResplineData resplineData = new ResplineData();
        resplineData.setDatas(new ArrayList<LineData>(Collections.singleton(new LineData())));
        final LineData lineData = new LineData();
        lineData.setLine_name(SpringUtils.getBean(ServerService.class).getName());
        lineData.setExpect_time(500);
        resplineData.getDatas().add(lineData);
        MessagePusher.pushMessage(session, resplineData);
    }
    
    @RequestMapping
    public void kickOffClient(final IoSession session, final ReqKickOffClient offClient) {
        this.logger.error("你已被强制下线");
    }
    
    @RequestMapping
    public void loginAccount(final IoSession session, final ReqLoginAccount reqLoginAccount) {
        if (this.serverService.getState() <= 0) {
            final RespOtherLogin respOtherLogin = new RespOtherLogin();
            respOtherLogin.setContent("游戏维护中");
            respOtherLogin.setResult(4);
            MessagePusher.pushMessage(session, respOtherLogin);
            return;
        }
        if ("cmd_l_login_preview_player".equals(reqLoginAccount.getType())) {
            final RespLoginPreview preview = new RespLoginPreview();
            preview.setCookie("4526387");
            preview.setSid(reqLoginAccount.getAccountId());
            MessagePusher.pushMessage(session, preview);
            session.closeNow();
            return;
        }
        final ServerService service = SpringUtils.getBean(ServerService.class);
        final String versions = service.getVersions();
        final String engine = service.getEngine();
        if (!versions.equals(reqLoginAccount.getGameV()) || !engine.equals(reqLoginAccount.getEngV())) {
            final RespOtherLogin respOtherLogin2 = new RespOtherLogin();
            respOtherLogin2.setContent("请更新游戏");
            respOtherLogin2.setResult(4);
            MessagePusher.pushMessage(session, respOtherLogin2);
            return;
        }
        final String sid = SessionManager.INSTANCE.getSessionAttr(session, SessionProperties.ACCOUNT_SID, String.class);
        if (Strings.isEmpty((CharSequence)sid)) {
            final RespOtherLogin respOtherLogin3 = new RespOtherLogin();
            respOtherLogin3.setContent("账号登录失败。#r(原因：9023)");
            respOtherLogin3.setResult(0);
            MessagePusher.pushMessage(session, respOtherLogin3);
            ModuleCounterFilter.banIp(session);
            session.closeNow();
            this.logger.error("没有账号异常==1111");
            return;
        }
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final LoginAuth request = new LoginAuth();
        DataCache.LOGIN_AUTH_MAP.put(sid, request);
        final RespAgentResult respAgentResult = new RespAgentResult();
        respAgentResult.setAuth_key(request.getAuthKey());
        respAgentResult.setMsg("允许a该账号1登录");
        respAgentResult.setResult(1);
        MessagePusher.pushMessage(session, respAgentResult);
    }
    
    @RequestMapping
    public void previewLogin(final IoSession session, final ReqLoginPreview preview) {
        final RespStartLogin respStartLogin = new RespStartLogin();
        respStartLogin.setCookie("4526387");
        respStartLogin.setType("cmd_l_login_preview_player");
        MessagePusher.pushMessage(session, respStartLogin);
    }
}
