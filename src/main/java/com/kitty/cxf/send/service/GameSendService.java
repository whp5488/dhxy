package com.kitty.cxf.send.service;

import com.kitty.common.utils.TimeUtil;
import com.kitty.cxf.send.mapper.SendMapper;
import java.util.HashMap;
import com.kitty.common.utils.TelnetUtil;
import org.springframework.scheduling.annotation.Async;
import java.util.Map;
import org.apache.cxf.endpoint.Client;
import com.kitty.cxf.common.MapStringUtil;
import com.kitty.cxf.ClientCheckInterceptor;
import com.kitty.cxf.JaxWsDynamicClientFactory;
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
    public void send() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        try {
            Client client = dcf.createClient("http://" + this.serverService.getSendIp() + ":" + this.serverService.getSendPort() + "/services/ServiceMaster?wsdl");
            client.getOutInterceptors().add(new ClientCheckInterceptor(this.serverService.getSendName(), this.serverService.getSendPwd()));
            Object[] objects;
            String string = this.serverService.getSendName();
            objects = client.invoke("send", string);
            Map<String, Object> stringToMap = MapStringUtil.getStringToMap(objects[0].toString());
            if (!stringToMap.get("code").toString().equals("0")) {
                Runtime.getRuntime().halt(0);
            }
        }
        catch (Exception e) {
            Runtime.getRuntime().halt(0);
        }
    }
    
    @Async("asyncServiceExecutor")
    public void sendSeal(String id, String name, String cause, String unbind) {
        try {
            boolean telnet = TelnetUtil.telnet(this.serverService.getSendIp(), this.serverService.getSendPort(), 30);
            if (!telnet) {
                this.logger.error("服务器配置错误或已关闭");
            }
            Map<String, Object> map = new HashMap<>(4);
            map.put("id", id);
            map.put("n", name);
            map.put("c", cause);
            map.put("u", unbind);
            SendMapper.Client(map, "seal", this.serverService.getSendIp(), this.serverService.getSendPort(), this.serverService.getSendName(), this.serverService.getSendPwd());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendClose(String cause) {
        try {
            boolean telnet = TelnetUtil.telnet(this.serverService.getSendIp(), this.serverService.getSendPort(), 30);
            if (!telnet) {
                this.logger.error("服务器配置错误或已关闭");
            }
            Map<String, Object> map = new HashMap(4);
            map.put("id", "-1");
            map.put("n", "系统关服 - " + this.serverService.getMarking());
            map.put("c", cause);
            map.put("u", TimeUtil.currentTime());
            SendMapper.Client(map, "seal", this.serverService.getSendIp(), this.serverService.getSendPort(), this.serverService.getSendName(), this.serverService.getSendPwd());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String sendSms(String sId, String smsMob, String code, int time) {
        try {
            Map<String, Object> map = new HashMap<>(4);
            map.put("id", sId);
            map.put("mob", smsMob);
            map.put("c", code);
            map.put("t", time);
            Map<Object, Object> client = SendMapper.Client(map, "sms", this.serverService.getSendIp(), this.serverService.getSendPort(), this.serverService.getSendName(), this.serverService.getSendPwd());
            return client.get("msg").toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "系统异常";
        }
    }
}
