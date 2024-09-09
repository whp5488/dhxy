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
        this.list = new ArrayList<>();
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
        RespChat other = (RespChat)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        Object this$roleName = this.getRoleName();
        Object other$roleName = other.getRoleName();
        if (!((this$roleName != null) ? this$roleName.equals(other$roleName) : (other$roleName == null))) {
            return false;
        }
        Object this$msg = this.getMsg();
        Object other$msg = other.getMsg();
        if (!((this$msg != null) ? this$msg.equals(other$msg) : (other$msg == null))) {
            return false;
        }
        if (this.getTime() != other.getTime()) {
            return false;
        }
        Object this$token = this.getToken();
        Object other$token = other.getToken();
        if (!((this$token != null) ? this$token.equals(other$token) : (other$token == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        if (!((this$list != null) ? this$list.equals(other$list) : (other$list == null))) {
            return false;
        }
        if (this.getFriendId() != other.getFriendId()) {
            return false;
        }
        Object this$partyName = this.getPartyName();
        Object other$partyName = other.getPartyName();
        if (!((this$partyName != null) ? this$partyName.equals(other$partyName) : (other$partyName == null))) {
            return false;
        }
        Object this$para = this.getPara();
        Object other$para = other.getPara();
        return (this$para != null) ? this$para.equals(other$para) : (other$para == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespChat;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getRoleId();
        Object $roleName = this.getRoleName();
        result = result * 59 + (($roleName == null) ? 43 : $roleName.hashCode());
        Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + this.getTime();
        Object $token = this.getToken();
        result = result * 59 + (($token == null) ? 43 : $token.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getFriendId();
        Object $partyName = this.getPartyName();
        result = result * 59 + (($partyName == null) ? 43 : $partyName.hashCode());
        Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespChat(type=" + this.getType() + ", roleId=" + this.getRoleId() + ", roleName=" + this.getRoleName() + ", msg=" + this.getMsg() + ", time=" + this.getTime() + ", token=" + this.getToken() + ", list=" + this.getList() + ", friendId=" + this.getFriendId() + ", partyName=" + this.getPartyName() + ", para=" + this.getPara() + ")";
    }
}
