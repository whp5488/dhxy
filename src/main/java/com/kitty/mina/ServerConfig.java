package com.kitty.mina;

import com.kitty.system.init.service.InitService;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class ServerConfig {
  @Autowired
  private Dao dao;
  
  @Autowired
  private InitService initService;
  
  @Value("${game.serial}")
  private int serial;
  
  @Value("${game.date}")
  private long serverDate;

  private int serverLevel = 70;

  public void init() {
    Sql sql = Sqls.create("SELECT MAX(relive * 1000 + level) FROM role GROUP BY role_id ORDER BY ANY_VALUE(exp) desc limit 1");
    sql = sql.setCallback(Sqls.callback.str());
    this.dao.execute(sql);
    if (sql.getString() != null) {
      int level = Integer.valueOf(sql.getString());
      if (level > this.serverLevel)
        this.serverLevel = level; 
    } 
    long time = this.initService.initDate();
    if (time > 0L)
      this.serverDate = time; 
  }
}
