package com.kitty.mina;

import org.nutz.dao.sql.Sql;
import org.nutz.dao.Sqls;
import org.springframework.beans.factory.annotation.Value;
import com.kitty.system.init.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig
{
    @Autowired
    private Dao dao;
    @Autowired
    private InitService initService;
    @Value("${game.serial}")
    private int serial;
    @Value("${game.date}")
    private long serverDate;
    private int serverLevel;
    
    public void init() {
        Sql sql = Sqls.create("SELECT MAX(relive * 1000 + level) FROM role GROUP BY role_id ORDER BY ANY_VALUE(exp) desc limit 1");
        sql = sql.setCallback(Sqls.callback.str());
        this.dao.execute(sql);
        if (sql.getString() != null) {
            int level = (int)Integer.valueOf(sql.getString());
            if (level > this.serverLevel) {
                this.serverLevel = level;
            }
        }
        long time = this.initService.initDate();
        if (time > 0L) {
            this.serverDate = time;
        }
    }
    
    public ServerConfig() {
        this.serverLevel = 70;
    }
    
    public Dao getDao() {
        return this.dao;
    }
    
    public InitService getInitService() {
        return this.initService;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public long getServerDate() {
        return this.serverDate;
    }
    
    public int getServerLevel() {
        return this.serverLevel;
    }
    
    public void setDao(final Dao dao) {
        this.dao = dao;
    }
    
    public void setInitService(final InitService initService) {
        this.initService = initService;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setServerDate(final long serverDate) {
        this.serverDate = serverDate;
    }
    
    public void setServerLevel(final int serverLevel) {
        this.serverLevel = serverLevel;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ServerConfig)) {
            return false;
        }
        ServerConfig other = (ServerConfig)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$dao = this.getDao();
        Object other$dao = other.getDao();
        if (!((this$dao != null) ? this$dao.equals(other$dao) : (other$dao == null))) {
            return false;
        }
        Object this$initService = this.getInitService();
        Object other$initService = other.getInitService();
        return ((this$initService != null) ? this$initService.equals(other$initService) : (other$initService == null)) && this.getSerial() == other.getSerial() && this.getServerDate() == other.getServerDate() && this.getServerLevel() == other.getServerLevel();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ServerConfig;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $dao = this.getDao();
        result = result * 59 + (($dao == null) ? 43 : $dao.hashCode());
        Object $initService = this.getInitService();
        result = result * 59 + (($initService == null) ? 43 : $initService.hashCode());
        result = result * 59 + this.getSerial();
        long $serverDate = this.getServerDate();
        result = result * 59 + (int)($serverDate >>> 32 ^ $serverDate);
        result = result * 59 + this.getServerLevel();
        return result;
    }
    
    @Override
    public String toString() {
        return "ServerConfig(dao=" + this.getDao() + ", initService=" + this.getInitService() + ", serial=" + this.getSerial() + ", serverDate=" + this.getServerDate() + ", serverLevel=" + this.getServerLevel() + ")";
    }
}
