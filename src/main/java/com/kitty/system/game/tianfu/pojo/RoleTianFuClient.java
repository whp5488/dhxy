package com.kitty.system.game.tianfu.pojo;

import java.util.Map;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import com.kitty.system.game.tianfu.domain.RoleTianFu;

public class RoleTianFuClient
{
    private String id;
    private int typeId;
    private byte grade;
    private byte usePos;
    private int bd;
    private short count;
    private int storePos;
    
    public RoleTianFuClient(RoleTianFu roleTianFu) {
        this.id = String.valueOf(roleTianFu.getId());
        if (roleTianFu.getToIdentifyCeSerial() != 1) {
            this.typeId = roleTianFu.getToIdentifyCeSerial();
        }
        else {
            this.typeId = roleTianFu.getTianFuSerial();
        }
        this.grade = roleTianFu.getLevel();
        Map<Long, String> tianFuGridMap = TianFuUtils.getTianFuGridMap((RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(Long.valueOf(roleTianFu.getRoleId())));
        String tianFuName = (String)tianFuGridMap.get(roleTianFu.getId());
        Byte tianFuClientName = (Byte)TianFuUtils.server2clientGridMap.get(tianFuName);
        if (tianFuClientName != null && (byte)tianFuClientName > 0) {
            this.usePos = (byte)tianFuClientName;
        }
        else {
            this.usePos = 0;
        }
        this.bd = roleTianFu.getBd();
        this.count = roleTianFu.getCount();
        this.storePos = roleTianFu.getStorePos();
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getTypeId() {
        return this.typeId;
    }
    
    public byte getGrade() {
        return this.grade;
    }
    
    public byte getUsePos() {
        return this.usePos;
    }
    
    public int getBd() {
        return this.bd;
    }
    
    public short getCount() {
        return this.count;
    }
    
    public int getStorePos() {
        return this.storePos;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setTypeId(final int typeId) {
        this.typeId = typeId;
    }
    
    public void setGrade(final byte grade) {
        this.grade = grade;
    }
    
    public void setUsePos(final byte usePos) {
        this.usePos = usePos;
    }
    
    public void setBd(final int bd) {
        this.bd = bd;
    }
    
    public void setCount(final short count) {
        this.count = count;
    }
    
    public void setStorePos(final int storePos) {
        this.storePos = storePos;
    }
    
    public RoleTianFuClient() {
    }
}
