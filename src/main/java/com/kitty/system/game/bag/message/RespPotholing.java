package com.kitty.system.game.bag.message;

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
        RespPotholing other = (RespPotholing)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        if (!((this$list != null) ? this$list.equals(other$list) : (other$list == null))) {
            return false;
        }
        if (this.getIndex() != other.getIndex()) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        return (this$id != null) ? this$id.equals(other$id) : (other$id == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPotholing;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getIndex();
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPotholing(list=" + this.getList() + ", index=" + this.getIndex() + ", id=" + this.getId() + ")";
    }
    
    public RespPotholing(final List<PotholingVo> list, final int index, final String id) {
        this.list = list;
        this.index = index;
        this.id = id;
    }
}
