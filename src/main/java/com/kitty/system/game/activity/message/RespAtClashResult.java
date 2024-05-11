
package com.kitty.system.game.activity.message;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45246)
public class RespAtClashResult extends Message
{
    private byte result;
    private List<FiedValue> list;
    private int mateSecond;
    
    public RespAtClashResult() {
        this.mateSecond = 0;
    }
    
    public byte getResult() {
        return this.result;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public int getMateSecond() {
        return this.mateSecond;
    }
    
    public void setResult(final byte result) {
        this.result = result;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    public void setMateSecond(final int mateSecond) {
        this.mateSecond = mateSecond;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespAtClashResult)) {
            return false;
        }
        final RespAtClashResult other = (RespAtClashResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getResult() != other.getResult()) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return this.getMateSecond() == other.getMateSecond();
            }
        }
        else if (this$list.equals(other$list)) {
            return this.getMateSecond() == other.getMateSecond();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespAtClashResult;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getResult();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getMateSecond();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespAtClashResult(result=" + this.getResult() + ", list=" + this.getList() + ", mateSecond=" + this.getMateSecond() + ")";
    }
}
