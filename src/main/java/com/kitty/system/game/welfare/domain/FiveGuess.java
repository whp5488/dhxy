
package com.kitty.system.game.welfare.domain;

import com.kitty.common.db.DbRecordService;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("five_guess")
public class FiveGuess extends BaseEntity<Long>
{
    @Id(auto = false)
    @Column("id")
    private Long id;
    @Column("r_id")
    private long rId;
    @Column("money")
    private long money;
    @Column("amount")
    private int amount;
    @Column("choice")
    private short choice;
    @Column("prize")
    private short prize;
    @Column("token")
    private String token;
    @Column("count")
    private short count;
    
    public FiveGuess(final Long id, final long rId) {
        this.money = 0L;
        this.amount = 0;
        this.choice = 0;
        this.prize = 0;
        this.count = WelfareDataPool.FIVE_GUESS_COUNT;
        this.id = id;
        this.rId = rId;
    }
    
    public void clear() {
        this.amount = 0;
        this.choice = 0;
        this.prize = 0;
        this.token = null;
    }
    
    public void save() {
        DbRecordService.getInstance().add2Queue(this);
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public long getRId() {
        return this.rId;
    }
    
    public long getMoney() {
        return this.money;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public short getChoice() {
        return this.choice;
    }
    
    public short getPrize() {
        return this.prize;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public short getCount() {
        return this.count;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRId(final long rId) {
        this.rId = rId;
    }
    
    public void setMoney(final long money) {
        this.money = money;
    }
    
    public void setAmount(final int amount) {
        this.amount = amount;
    }
    
    public void setChoice(final short choice) {
        this.choice = choice;
    }
    
    public void setPrize(final short prize) {
        this.prize = prize;
    }
    
    public void setToken(final String token) {
        this.token = token;
    }
    
    public void setCount(final short count) {
        this.count = count;
    }
    
    public FiveGuess() {
        this.money = 0L;
        this.amount = 0;
        this.choice = 0;
        this.prize = 0;
        this.count = WelfareDataPool.FIVE_GUESS_COUNT;
    }
}
