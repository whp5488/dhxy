package com.kitty.system.game.account.service;

import org.nutz.dao.Chain;
import com.kitty.common.mysql.TargetDataSource;
import org.nutz.dao.Cnd;
import com.kitty.system.game.account.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class AccountService
{
    @Autowired
    private Dao dao;
    
    public AccountService() {
    }
    
    @TargetDataSource(name = "ac")
    public Account getAccount(String id) {
        return (Account)this.dao.fetch(Account.class, Cnd.where("id", "=", id));
    }
    
    @TargetDataSource(name = "ac")
    public void updateAccountMobile(String id, String mobile) {
        this.dao.update(Account.class, Chain.make("mobile", mobile), Cnd.where("id", "=", id));
    }
}
