
package com.kitty.system.game.chat.message;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 16383)
public class RespChat extends Message
{
    private int type;
    private int roleId;
    private String roleName;
    private String msg;
    private int time;
    private String token;
    private List<FiedValue> list;
    private int friendId;
    private String partyName;
    private String para;
    
    public RespChat() {
        this.roleId = 0;
        this.roleName = "";
        this.time = new Long(System.currentTimeMillis() / 1000L).intValue();
        this.list = new ArrayList<FiedValue>();
        this.friendId = 0;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getRoleName() {
        return this.roleName;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public int getFriendId() {
        return this.friendId;
    }
    
    public String getPartyName() {
        return this.partyName;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setToken(final String token) {
        this.token = token;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    public void setFriendId(final int friendId) {
        this.friendId = friendId;
    }
    
    public void setPartyName(final String partyName) {
        this.partyName = partyName;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespChat)) {
            return false;
        }
        final RespChat other = (RespChat)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        final Object this$roleName = this.getRoleName();
        final Object other$roleName = other.getRoleName();
        Label_0091: {
            if (this$roleName == null) {
                if (other$roleName == null) {
                    break Label_0091;
                }
            }
            else if (this$roleName.equals(other$roleName)) {
                break Label_0091;
            }
            return false;
        }
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        Label_0128: {
            if (this$msg == null) {
                if (other$msg == null) {
                    break Label_0128;
                }
            }
            else if (this$msg.equals(other$msg)) {
                break Label_0128;
            }
            return false;
        }
        if (this.getTime() != other.getTime()) {
            return false;
        }
        final Object this$token = this.getToken();
        final Object other$token = other.getToken();
        Label_0178: {
            if (this$token == null) {
                if (other$token == null) {
                    break Label_0178;
                }
            }
            else if (this$token.equals(other$token)) {
                break Label_0178;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        Label_0215: {
            if (this$list == null) {
                if (other$list == null) {
                    break Label_0215;
                }
            }
            else if (this$list.equals(other$list)) {
                break Label_0215;
            }
            return false;
        }
        if (this.getFriendId() != other.getFriendId()) {
            return false;
        }
        final Object this$partyName = this.getPartyName();
        final Object other$partyName = other.getPartyName();
        Label_0265: {
            if (this$partyName == null) {
                if (other$partyName == null) {
                    break Label_0265;
                }
            }
            else if (this$partyName.equals(other$partyName)) {
                break Label_0265;
            }
            return false;
        }
        final Object this$para = this.getPara();
        final Object other$para = other.getPara();
        if (this$para == null) {
            if (other$para == null) {
                return true;
            }
        }
        else if (this$para.equals(other$para)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespChat;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getRoleId();
        final Object $roleName = this.getRoleName();
        result = result * 59 + (($roleName == null) ? 43 : $roleName.hashCode());
        final Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + this.getTime();
        final Object $token = this.getToken();
        result = result * 59 + (($token == null) ? 43 : $token.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getFriendId();
        final Object $partyName = this.getPartyName();
        result = result * 59 + (($partyName == null) ? 43 : $partyName.hashCode());
        final Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespChat(type=" + this.getType() + ", roleId=" + this.getRoleId() + ", roleName=" + this.getRoleName() + ", msg=" + this.getMsg() + ", time=" + this.getTime() + ", token=" + this.getToken() + ", list=" + this.getList() + ", friendId=" + this.getFriendId() + ", partyName=" + this.getPartyName() + ", para=" + this.getPara() + ")";
    }
}
