
package com.kitty.cxf.send.service;

import com.kitty.common.utils.TimeUtil;
import org.springframework.scheduling.annotation.Async;
import java.util.Map;
import com.kitty.cxf.send.mapper.SendMapper;
import java.util.HashMap;
import com.kitty.common.utils.TelnetUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.common.start.ServerService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class GameSendService
{
    private final Logger logger;
    @Autowired
    private ServerService serverService;
    
    public GameSendService() {
        this.logger = LoggerFactory.getLogger(GameSendService.class);
    }
    
    @Async("asyncServiceExecutor")
    public void sendSeal(final String id, final String name, final String cause, final String unbind) {
        try {
            final boolean telnet = TelnetUtil.telnet(this.serverService.getSendIp(), this.serverService.getSendPort(), 30);
            if (!telnet) {
                this.logger.error("服务器配置错误或已关闭");
            }
            final Map<String, Object> map = new HashMap<String, Object>(4);
            map.put("id", id);
            map.put("n", name);
            map.put("c", cause);
            map.put("u", unbind);
            SendMapper.Client(map, "seal", this.serverService.getSendIp(), this.serverService.getSendPort(), this.serverService.getSendName(), this.serverService.getSendPwd());
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendClose(final String cause) {
        try {
            final boolean telnet = TelnetUtil.telnet(this.serverService.getSendIp(), this.serverService.getSendPort(), 30);
            if (!telnet) {
                this.logger.error("服务器配置错误或已关闭");
            }
            final Map<String, Object> map = new HashMap<String, Object>(4);
            map.put("id", "-1");
            map.put("n", "系统关服 - " + this.serverService.getMarking());
            map.put("c", cause);
            map.put("u", TimeUtil.currentTime());
            SendMapper.Client(map, "seal", this.serverService.getSendIp(), this.serverService.getSendPort(), this.serverService.getSendName(), this.serverService.getSendPwd());
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public String sendSms(final String sId, final String smsMob, final String code, final int time) {
        try {
            final Map<String, Object> map = new HashMap<String, Object>(4);
            map.put("id", sId);
            map.put("mob", smsMob);
            map.put("c", code);
            map.put("t", time);
            final Map client = SendMapper.Client(map, "sms", this.serverService.getSendIp(), this.serverService.getSendPort(), this.serverService.getSendName(), this.serverService.getSendPwd());
            return client.get("msg").toString();
        }
        catch (final Exception e) {
            e.printStackTrace();
            return "系统异常";
        }
    }
}
