
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
        this.itself = new ArrayList<MemberInfo>(5);
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
        final RespCaInfo other = (RespCaInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRound() != other.getRound()) {
            return false;
        }
        if (this.getCurrentSession() != other.getCurrentSession()) {
            return false;
        }
        final Object this$itself = this.getItself();
        final Object other$itself = other.getItself();
        Label_0091: {
            if (this$itself == null) {
                if (other$itself == null) {
                    break Label_0091;
                }
            }
            else if (this$itself.equals(other$itself)) {
                break Label_0091;
            }
            return false;
        }
        if (this.getGroup() != other.getGroup()) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespCaInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRound();
        result = result * 59 + this.getCurrentSession();
        final Object $itself = this.getItself();
        result = result * 59 + (($itself == null) ? 43 : $itself.hashCode());
        result = result * 59 + this.getGroup();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespCaInfo(round=" + this.getRound() + ", currentSession=" + this.getCurrentSession() + ", itself=" + this.getItself() + ", group=" + this.getGroup() + ", list=" + this.getList() + ")";
    }
}
