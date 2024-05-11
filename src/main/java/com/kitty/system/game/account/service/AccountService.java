
package com.kitty.system.game.account.service;

import org.nutz.dao.Chain;
import com.kitty.common.mysql.TargetDataSource;
import org.nutz.dao.Condition;
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
    
    @TargetDataSource(name = "ac")
    public Account getAccount(final String id) {
        return this.dao.fetch(Account.class, Cnd.where("id", "=", id));
    }
    
    @TargetDataSource(name = "ac")
    public void updateAccountMobile(final String id, final String mobile) {
        this.dao.update(Account.class, Chain.make("mobile", mobile), Cnd.where("id", "=", id));
    }
}
