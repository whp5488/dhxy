package com.kitty.cxf.send.service;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.TelnetUtil;
import com.kitty.common.utils.TimeUtil;
import com.kitty.cxf.send.mapper.SendMapper;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class GameSendService {
  private final Logger logger = LoggerFactory.getLogger(GameSendService.class);
  
  @Autowired
  private ServerService serverService;
  
  @Async("asyncServiceExecutor")
  public void sendSeal(String id, String name, String cause, String unbind) {
    try {
      boolean telnet = TelnetUtil.telnet(this.serverService.getSendIp(), this.serverService.getSendPort(), 30);
      if (!telnet)
        this.logger.error("服务器配置错误或已关闭"); 
      Map<String, Object> map = new HashMap<>(4);
      map.put("id", id);
      map.put("n", name);
      map.put("c", cause);
      map.put("u", unbind);
      Map map1 = SendMapper.Client(map, "seal", this.serverService.getSendIp(), this.serverService.getSendPort(), this.serverService.getSendName(), this.serverService.getSendPwd());
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void sendClose(String cause) {
    try {
      boolean telnet = TelnetUtil.telnet(this.serverService.getSendIp(), this.serverService.getSendPort(), 30);
      if (!telnet)
        this.logger.error("服务器配置错误或已关闭"); 
      Map<String, Object> map = new HashMap<>(4);
      map.put("id", "-1");
      map.put("n", "系统关服 - " + this.serverService.getMarking());
      map.put("c", cause);
      map.put("u", TimeUtil.currentTime());
      Map map1 = SendMapper.Client(map, "seal", this.serverService.getSendIp(), this.serverService.getSendPort(), this.serverService.getSendName(), this.serverService.getSendPwd());
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public String sendSms(String sId, String smsMob, String code, int time) {
    try {
      Map<String, Object> map = new HashMap<>(4);
      map.put("id", sId);
      map.put("mob", smsMob);
      map.put("c", code);
      map.put("t", Integer.valueOf(time));
      Map client = SendMapper.Client(map, "sms", this.serverService.getSendIp(), this.serverService.getSendPort(), this.serverService.getSendName(), this.serverService.getSendPwd());
      return client.get("msg").toString();
    } catch (Exception e) {
      e.printStackTrace();
      return "系统异常";
    } 
  }
}
