package com.kitty.common.start;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Service
public class ServerService {
  
  int state = 1;
  
  String url;
  
  @Value("${game.type}")
  byte type;
  
  @Value("${game.recharge}")
  int recharge;
  
  @Value("${game.exp_times}")
  int expTimes;
  
  @Value("${game.exp_times_level}")
  int expTimesLevel;
  
  @Value("${game.serverPort}")
  int port;
  
  @Value("${game.name}")
  String name;
  
  @Value("${game.marking}")
  String marking;
  
  @Value("${game.versions}")
  String versions;
  
  @Value("${engine.versions}")
  String engine;
  
  @Value("${game.ratio}")
  int ratio;

  private int open = 1;
  
  @Value("${login_server_url}")
  private String loginServerUrl;

  private String userName = "4kED4EItrU";

  private String passWord = "4kED4EItrU";
  
  @Value("${send.sendIp}")
  private String sendIp;
  
  @Value("${send.sendPort}")
  private int sendPort;

  private String sendName = "4kED4EItrU";

  private String sendPwd = "4kED4EItrU";
  
  ScheduledExecutorService scheduledExecutorService;

  public void init(Map<String, String> map, Map<String, String> map_) {
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(12);
    setScheduledExecutorService(scheduledExecutorService);
    String charge = map.get("charge");
    if (charge != null)
      this.url = charge; 
    versions(map_);
  }
  
  public void versions(Map<String, String> map_) {
    if (map_ != null) {
      this.versions = map_.get("gameVersion");
      this.engine = map_.get("engineVersion");
    } 
  }

}
