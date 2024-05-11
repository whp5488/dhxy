
package com.kitty.system.game.bag.message;

import java.beans.ConstructorProperties;
import com.kitty.system.game.bag.message.vo.PotholingVo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 33042)
public class RespPotholing extends Message
{
    private List<PotholingVo> list;
    private int index;
    private String id;
    
    public List<PotholingVo> getList() {
        return this.list;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setList(final List<PotholingVo> list) {
        this.list = list;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPotholing)) {
            return false;
        }
        final RespPotholing other = (RespPotholing)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        Label_0065: {
            if (this$list == null) {
                if (other$list == null) {
                    break Label_0065;
                }
            }
            else if (this$list.equals(other$list)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getIndex() != other.getIndex()) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null) {
            if (other$id == null) {
                return true;
            }
        }
        else if (this$id.equals(other$id)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPotholing;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getIndex();
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPotholing(list=" + this.getList() + ", index=" + this.getIndex() + ", id=" + this.getId() + ")";
    }
    
    @ConstructorProperties({ "list", "index", "id" })
    public RespPotholing(final List<PotholingVo> list, final int index, final String id) {
        this.list = list;
        this.index = index;
        this.id = id;
    }
}
