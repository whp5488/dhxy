
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
    private String userName;
    private String passWord;
    @Value("${send.sendIp}")
    private String sendIp;
    @Value("${send.sendPort}")
    private int sendPort;
    private String sendName;
    private String sendPwd;
    ScheduledExecutorService scheduledExecutorService;
    
    public void init(final Map<String, String> map, final Map<String, String> map_) {
        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(12);
        this.setScheduledExecutorService(scheduledExecutorService);
        final String charge = map.get("charge");
        if (charge != null) {
            this.url = charge;
        }
        this.versions(map_);
    }
    
    public void versions(final Map<String, String> map_) {
        if (map_ != null) {
            this.versions = map_.get("gameVersion");
            this.engine = map_.get("engineVersion");
        }
    }
    
    public ScheduledExecutorService getScheduledExecutorService() {
        return this.scheduledExecutorService;
    }
    
    public void setScheduledExecutorService(final ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }
    
    public ServerService() {
        this.state = 1;
        this.open = 1;
        this.userName = "4kED4EItrU";
        this.passWord = "4kED4EItrU";
        this.sendName = "4kED4EItrU";
        this.sendPwd = "4kED4EItrU";
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ServerService)) {
            return false;
        }
        final ServerService other = (ServerService)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getState() != other.getState()) {
            return false;
        }
        final Object this$url = this.getUrl();
        final Object other$url = other.getUrl();
        Label_0078: {
            if (this$url == null) {
                if (other$url == null) {
                    break Label_0078;
                }
            }
            else if (this$url.equals(other$url)) {
                break Label_0078;
            }
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getRecharge() != other.getRecharge()) {
            return false;
        }
        if (this.getExpTimes() != other.getExpTimes()) {
            return false;
        }
        if (this.getExpTimesLevel() != other.getExpTimesLevel()) {
            return false;
        }
        if (this.getPort() != other.getPort()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0180: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0180;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0180;
            }
            return false;
        }
        final Object this$marking = this.getMarking();
        final Object other$marking = other.getMarking();
        Label_0217: {
            if (this$marking == null) {
                if (other$marking == null) {
                    break Label_0217;
                }
            }
            else if (this$marking.equals(other$marking)) {
                break Label_0217;
            }
            return false;
        }
        final Object this$versions = this.getVersions();
        final Object other$versions = other.getVersions();
        Label_0254: {
            if (this$versions == null) {
                if (other$versions == null) {
                    break Label_0254;
                }
            }
            else if (this$versions.equals(other$versions)) {
                break Label_0254;
            }
            return false;
        }
        final Object this$engine = this.getEngine();
        final Object other$engine = other.getEngine();
        Label_0291: {
            if (this$engine == null) {
                if (other$engine == null) {
                    break Label_0291;
                }
            }
            else if (this$engine.equals(other$engine)) {
                break Label_0291;
            }
            return false;
        }
        if (this.getRatio() != other.getRatio()) {
            return false;
        }
        if (this.getOpen() != other.getOpen()) {
            return false;
        }
        final Object this$loginServerUrl = this.getLoginServerUrl();
        final Object other$loginServerUrl = other.getLoginServerUrl();
        Label_0354: {
            if (this$loginServerUrl == null) {
                if (other$loginServerUrl == null) {
                    break Label_0354;
                }
            }
            else if (this$loginServerUrl.equals(other$loginServerUrl)) {
                break Label_0354;
            }
            return false;
        }
        final Object this$userName = this.getUserName();
        final Object other$userName = other.getUserName();
        Label_0391: {
            if (this$userName == null) {
                if (other$userName == null) {
                    break Label_0391;
                }
            }
            else if (this$userName.equals(other$userName)) {
                break Label_0391;
            }
            return false;
        }
        final Object this$passWord = this.getPassWord();
        final Object other$passWord = other.getPassWord();
        Label_0428: {
            if (this$passWord == null) {
                if (other$passWord == null) {
                    break Label_0428;
                }
            }
            else if (this$passWord.equals(other$passWord)) {
                break Label_0428;
            }
            return false;
        }
        final Object this$sendIp = this.getSendIp();
        final Object other$sendIp = other.getSendIp();
        Label_0465: {
            if (this$sendIp == null) {
                if (other$sendIp == null) {
                    break Label_0465;
                }
            }
            else if (this$sendIp.equals(other$sendIp)) {
                break Label_0465;
            }
            return false;
        }
        if (this.getSendPort() != other.getSendPort()) {
            return false;
        }
        final Object this$sendName = this.getSendName();
        final Object other$sendName = other.getSendName();
        Label_0515: {
            if (this$sendName == null) {
                if (other$sendName == null) {
                    break Label_0515;
                }
            }
            else if (this$sendName.equals(other$sendName)) {
                break Label_0515;
            }
            return false;
        }
        final Object this$sendPwd = this.getSendPwd();
        final Object other$sendPwd = other.getSendPwd();
        Label_0552: {
            if (this$sendPwd == null) {
                if (other$sendPwd == null) {
                    break Label_0552;
                }
            }
            else if (this$sendPwd.equals(other$sendPwd)) {
                break Label_0552;
            }
            return false;
        }
        final Object this$scheduledExecutorService = this.getScheduledExecutorService();
        final Object other$scheduledExecutorService = other.getScheduledExecutorService();
        if (this$scheduledExecutorService == null) {
            if (other$scheduledExecutorService == null) {
                return true;
            }
        }
        else if (this$scheduledExecutorService.equals(other$scheduledExecutorService)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ServerService;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getState();
        final Object $url = this.getUrl();
        result = result * 59 + (($url == null) ? 43 : $url.hashCode());
        result = result * 59 + this.getType();
        result = result * 59 + this.getRecharge();
        result = result * 59 + this.getExpTimes();
        result = result * 59 + this.getExpTimesLevel();
        result = result * 59 + this.getPort();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $marking = this.getMarking();
        result = result * 59 + (($marking == null) ? 43 : $marking.hashCode());
        final Object $versions = this.getVersions();
        result = result * 59 + (($versions == null) ? 43 : $versions.hashCode());
        final Object $engine = this.getEngine();
        result = result * 59 + (($engine == null) ? 43 : $engine.hashCode());
        result = result * 59 + this.getRatio();
        result = result * 59 + this.getOpen();
        final Object $loginServerUrl = this.getLoginServerUrl();
        result = result * 59 + (($loginServerUrl == null) ? 43 : $loginServerUrl.hashCode());
        final Object $userName = this.getUserName();
        result = result * 59 + (($userName == null) ? 43 : $userName.hashCode());
        final Object $passWord = this.getPassWord();
        result = result * 59 + (($passWord == null) ? 43 : $passWord.hashCode());
        final Object $sendIp = this.getSendIp();
        result = result * 59 + (($sendIp == null) ? 43 : $sendIp.hashCode());
        result = result * 59 + this.getSendPort();
        final Object $sendName = this.getSendName();
        result = result * 59 + (($sendName == null) ? 43 : $sendName.hashCode());
        final Object $sendPwd = this.getSendPwd();
        result = result * 59 + (($sendPwd == null) ? 43 : $sendPwd.hashCode());
        final Object $scheduledExecutorService = this.getScheduledExecutorService();
        result = result * 59 + (($scheduledExecutorService == null) ? 43 : $scheduledExecutorService.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ServerService(state=" + this.getState() + ", url=" + this.getUrl() + ", type=" + this.getType() + ", recharge=" + this.getRecharge() + ", expTimes=" + this.getExpTimes() + ", expTimesLevel=" + this.getExpTimesLevel() + ", port=" + this.getPort() + ", name=" + this.getName() + ", marking=" + this.getMarking() + ", versions=" + this.getVersions() + ", engine=" + this.getEngine() + ", ratio=" + this.getRatio() + ", open=" + this.getOpen() + ", loginServerUrl=" + this.getLoginServerUrl() + ", userName=" + this.getUserName() + ", passWord=" + this.getPassWord() + ", sendIp=" + this.getSendIp() + ", sendPort=" + this.getSendPort() + ", sendName=" + this.getSendName() + ", sendPwd=" + this.getSendPwd() + ", scheduledExecutorService=" + this.getScheduledExecutorService() + ")";
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
