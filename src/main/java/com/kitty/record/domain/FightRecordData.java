
package com.kitty.record.domain;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import org.nutz.dao.entity.annotation.Id;
import java.io.Serializable;

public class FightRecordData implements Serializable
{
    @Id
    private long id;
    private long createTime;
    private byte[] msgData;
    
    public long getId() {
        return this.id;
    }
    
    public long getCreateTime() {
        return this.createTime;
    }
    
    public byte[] getMsgData() {
        return this.msgData;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public void setCreateTime(final long createTime) {
        this.createTime = createTime;
    }
    
    public void setMsgData(final byte[] msgData) {
        this.msgData = msgData;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FightRecordData)) {
            return false;
        }
        final FightRecordData other = (FightRecordData)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getCreateTime() == other.getCreateTime() && Arrays.equals(this.getMsgData(), other.getMsgData());
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FightRecordData;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $id = this.getId();
        result = result * 59 + (int)($id >>> 32 ^ $id);
        final long $createTime = this.getCreateTime();
        result = result * 59 + (int)($createTime >>> 32 ^ $createTime);
        result = result * 59 + Arrays.hashCode(this.getMsgData());
        return result;
    }
    
    @Override
    public String toString() {
        return "FightRecordData(id=" + this.getId() + ", createTime=" + this.getCreateTime() + ", msgData=" + Arrays.toString(this.getMsgData()) + ")";
    }
    
    @ConstructorProperties({ "id", "createTime", "msgData" })
    public FightRecordData(final long id, final long createTime, final byte[] msgData) {
        this.id = id;
        this.createTime = createTime;
        this.msgData = msgData;
    }
    
    public FightRecordData() {
    }
}
