package com.kitty.system.game.chat.message;

import com.kitty.system.game.chat.message.vo.ChatInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 16482)
public class ReqChat extends Message
{
    private int type;
    private String msg;
    private List<ChatInfo> list;
    private String token;
    private String para;
    
    public ReqChat() {
    }
    
    public int getType() {
        return this.type;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public List<ChatInfo> getList() {
        return this.list;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setList(final List<ChatInfo> list) {
        this.list = list;
    }
    
    public void setToken(final String token) {
        this.token = token;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqChat)) {
            return false;
        }
        ReqChat other = (ReqChat)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        Object this$msg = this.getMsg();
        Object other$msg = other.getMsg();
        if (!((this$msg != null) ? this$msg.equals(other$msg) : (other$msg == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        if (!((this$list != null) ? this$list.equals(other$list) : (other$list == null))) {
            return false;
        }
        Object this$token = this.getToken();
        Object other$token = other.getToken();
        if (!((this$token != null) ? this$token.equals(other$token) : (other$token == null))) {
            return false;
        }
        Object this$para = this.getPara();
        Object other$para = other.getPara();
        return (this$para != null) ? this$para.equals(other$para) : (other$para == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqChat;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        Object $token = this.getToken();
        result = result * 59 + (($token == null) ? 43 : $token.hashCode());
        Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqChat(type=" + this.getType() + ", msg=" + this.getMsg() + ", list=" + this.getList() + ", token=" + this.getToken() + ", para=" + this.getPara() + ")";
    }
}
