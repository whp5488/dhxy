package com.kitty.system.game.activity.message;

import java.util.ArrayList;
import com.kitty.system.game.activity.message.vo.CaFightInfo;
import com.kitty.system.game.activity.message.vo.MemberInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49182)
public class RespCaInfo extends Message
{
    private byte round;
    private byte currentSession;
    private List<MemberInfo> itself;
    private byte group;
    private List<CaFightInfo> list;
    
    public RespCaInfo() {
        this.round = 1;
        this.currentSession = 0;
        this.itself = new ArrayList(5);
    }
    
    public byte getRound() {
        return this.round;
    }
    
    public byte getCurrentSession() {
        return this.currentSession;
    }
    
    public List<MemberInfo> getItself() {
        return this.itself;
    }
    
    public byte getGroup() {
        return this.group;
    }
    
    public List<CaFightInfo> getList() {
        return this.list;
    }
    
    public void setRound(final byte round) {
        this.round = round;
    }
    
    public void setCurrentSession(final byte currentSession) {
        this.currentSession = currentSession;
    }
    
    public void setItself(final List<MemberInfo> itself) {
        this.itself = itself;
    }
    
    public void setGroup(final byte group) {
        this.group = group;
    }
    
    public void setList(final List<CaFightInfo> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespCaInfo)) {
            return false;
        }
        RespCaInfo other = (RespCaInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRound() != other.getRound()) {
            return false;
        }
        if (this.getCurrentSession() != other.getCurrentSession()) {
            return false;
        }
        Object this$itself = this.getItself();
        Object other$itself = other.getItself();
        if (!((this$itself != null) ? this$itself.equals(other$itself) : (other$itself == null))) {
            return false;
        }
        if (this.getGroup() != other.getGroup()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespCaInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRound();
        result = result * 59 + this.getCurrentSession();
        Object $itself = this.getItself();
        result = result * 59 + (($itself == null) ? 43 : $itself.hashCode());
        result = result * 59 + this.getGroup();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespCaInfo(round=" + (int)this.getRound() + ", currentSession=" + (int)this.getCurrentSession() + ", itself=" + this.getItself() + ", group=" + (int)this.getGroup() + ", list=" + this.getList() + ")";
    }
}
