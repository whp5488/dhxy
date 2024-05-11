
package com.kitty.system.game.role.message;

import com.kitty.system.game.role.domain.RoleInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61537)
public class RespExistedRoleList extends Message
{
    private int serverState;
    private List<RoleInfo> list;
    private int openServerTime;
    private int account_online;
    
    public RespExistedRoleList() {
        this.serverState = 1;
        this.account_online = 0;
    }
    
    public int getServerState() {
        return this.serverState;
    }
    
    public List<RoleInfo> getList() {
        return this.list;
    }
    
    public int getOpenServerTime() {
        return this.openServerTime;
    }
    
    public int getAccount_online() {
        return this.account_online;
    }
    
    public void setServerState(final int serverState) {
        this.serverState = serverState;
    }
    
    public void setList(final List<RoleInfo> list) {
        this.list = list;
    }
    
    public void setOpenServerTime(final int openServerTime) {
        this.openServerTime = openServerTime;
    }
    
    public void setAccount_online(final int account_online) {
        this.account_online = account_online;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespExistedRoleList)) {
            return false;
        }
        final RespExistedRoleList other = (RespExistedRoleList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getServerState() != other.getServerState()) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return this.getOpenServerTime() == other.getOpenServerTime() && this.getAccount_online() == other.getAccount_online();
            }
        }
        else if (this$list.equals(other$list)) {
            return this.getOpenServerTime() == other.getOpenServerTime() && this.getAccount_online() == other.getAccount_online();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespExistedRoleList;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getServerState();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getOpenServerTime();
        result = result * 59 + this.getAccount_online();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespExistedRoleList(serverState=" + this.getServerState() + ", list=" + this.getList() + ", openServerTime=" + this.getOpenServerTime() + ", account_online=" + this.getAccount_online() + ")";
    }
}
