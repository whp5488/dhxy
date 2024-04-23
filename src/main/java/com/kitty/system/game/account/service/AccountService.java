package com.kitty.system.game.account.service;

import com.kitty.common.mysql.TargetDataSource;
import com.kitty.system.game.account.domain.Account;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
  @Autowired
  private Dao dao;
  
  @TargetDataSource(name = "ac")
  public Account getAccount(String id) {
    return (Account)this.dao.fetch(Account.class, (Condition)Cnd.where("id", "=", id));
  }
  
  @TargetDataSource(name = "ac")
  public void updateAccountMobile(String id, String mobile) {
    this.dao.update(Account.class, Chain.make("mobile", mobile), (Condition)Cnd.where("id", "=", id));
  }
}
