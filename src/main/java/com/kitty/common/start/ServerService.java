package com.kitty.common.start;

import java.util.concurrent.Executors;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServerService
{
    int state;
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
    private int open;
    @Value("${login_server_url}")
    private String loginServerUrl;
    @Value("${cxf.username}")
    private String userName;
    @Value("${cxf.password}")
    private String passWord;
    @Value("${cxf.sendIp}")
    private String sendIp;
    @Value("${cxf.sendPort}")
    private int sendPort;
    @Value("${cxf.sendName}")
    private String sendName;
    @Value("${cxf.sendPwd}")
    private String sendPwd;
    ScheduledExecutorService scheduledExecutorService;
    
    public void init(Map<String, String> map, Map<String, String> map_) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(12);
        this.setScheduledExecutorService(scheduledExecutorService);
        String charge = (String)map.get("charge");
        if (charge != null) {
            this.url = charge;
        }
        this.versions(map_);
    }
    
    public void versions(Map<String, String> map_) {
        if (map_ != null) {
            this.versions = (String)map_.get("gameVersion");
            this.engine = (String)map_.get("engineVersion");
        }
    }
    
    public ScheduledExecutorService getScheduledExecutorService() {
        return this.scheduledExecutorService;
    }
    
    public void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }
    
    public ServerService() {
        this.state = 1;
        this.open = 1;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getRecharge() {
        return this.recharge;
    }
    
    public int getExpTimes() {
        return this.expTimes;
    }
    
    public int getExpTimesLevel() {
        return this.expTimesLevel;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getMarking() {
        return this.marking;
    }
    
    public String getVersions() {
        return this.versions;
    }
    
    public String getEngine() {
        return this.engine;
    }
    
    public int getRatio() {
        return this.ratio;
    }
    
    public int getOpen() {
        return this.open;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setRecharge(final int recharge) {
        this.recharge = recharge;
    }
    
    public void setExpTimes(final int expTimes) {
        this.expTimes = expTimes;
    }
    
    public void setExpTimesLevel(final int expTimesLevel) {
        this.expTimesLevel = expTimesLevel;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setMarking(final String marking) {
        this.marking = marking;
    }
    
    public void setVersions(final String versions) {
        this.versions = versions;
    }
    
    public void setEngine(final String engine) {
        this.engine = engine;
    }
    
    public void setRatio(final int ratio) {
        this.ratio = ratio;
    }
    
    public void setOpen(final int open) {
        this.open = open;
    }
    
    public void setLoginServerUrl(final String loginServerUrl) {
        this.loginServerUrl = loginServerUrl;
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
    }
    
    public void setPassWord(final String passWord) {
        this.passWord = passWord;
    }
    
    public void setSendIp(final String sendIp) {
        this.sendIp = sendIp;
    }
    
    public void setSendPort(final int sendPort) {
        this.sendPort = sendPort;
    }
    
    public void setSendName(final String sendName) {
        this.sendName = sendName;
    }
    
    public void setSendPwd(final String sendPwd) {
        this.sendPwd = sendPwd;
    }
    
    public int getState() {
        return this.state;
    }
    
    public void setState(final int state) {
        this.state = state;
    }
    
    public String getLoginServerUrl() {
        return this.loginServerUrl;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public String getPassWord() {
        return this.passWord;
    }
    
    public String getSendIp() {
        return this.sendIp;
    }
    
    public int getSendPort() {
        return this.sendPort;
    }
    
    public String getSendName() {
        return this.sendName;
    }
    
    public String getSendPwd() {
        return this.sendPwd;
    }
}
