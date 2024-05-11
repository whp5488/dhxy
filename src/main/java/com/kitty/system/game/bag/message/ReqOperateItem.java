
package com.kitty.system.game.bag.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 30778)
public class ReqOperateItem extends Message
{
    private int pos;
    private int type;
    private int num;
    private String param;
    
    public int getPos() {
        return this.pos;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public String getParam() {
        return this.param;
    }
    
    public void setPos(final int pos) {
        this.pos = pos;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public void setParam(final String param) {
        this.param = param;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqOperateItem)) {
            return false;
        }
        final ReqOperateItem other = (ReqOperateItem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPos() != other.getPos()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getNum() != other.getNum()) {
            return false;
        }
        final Object this$param = this.getParam();
        final Object other$param = other.getParam();
        if (this$param == null) {
            if (other$param == null) {
                return true;
            }
        }
        else if (this$param.equals(other$param)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqOperateItem;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPos();
        result = result * 59 + this.getType();
        result = result * 59 + this.getNum();
        final Object $param = this.getParam();
        result = result * 59 + (($param == null) ? 43 : $param.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqOperateItem(pos=" + this.getPos() + ", type=" + this.getType() + ", num=" + this.getNum() + ", param=" + this.getParam() + ")";
    }
    
    @ConstructorProperties({ "pos", "type", "num", "param" })
    public ReqOperateItem(final int pos, final int type, final int num, final String param) {
        this.pos = -1;
        this.num = 0;
        this.pos = pos;
        this.type = type;
        this.num = num;
        this.param = param;
    }
    
    public ReqOperateItem() {
        this.pos = -1;
        this.num = 0;
    }
}
