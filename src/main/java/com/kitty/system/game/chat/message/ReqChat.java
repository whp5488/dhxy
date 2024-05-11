
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
        final ReqChat other = (ReqChat)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        Label_0078: {
            if (this$msg == null) {
                if (other$msg == null) {
                    break Label_0078;
                }
            }
            else if (this$msg.equals(other$msg)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        Label_0115: {
            if (this$list == null) {
                if (other$list == null) {
                    break Label_0115;
                }
            }
            else if (this$list.equals(other$list)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$token = this.getToken();
        final Object other$token = other.getToken();
        Label_0152: {
            if (this$token == null) {
                if (other$token == null) {
                    break Label_0152;
                }
            }
            else if (this$token.equals(other$token)) {
                break Label_0152;
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
        return other instanceof ReqChat;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        final Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        final Object $token = this.getToken();
        result = result * 59 + (($token == null) ? 43 : $token.hashCode());
        final Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqChat(type=" + this.getType() + ", msg=" + this.getMsg() + ", list=" + this.getList() + ", token=" + this.getToken() + ", para=" + this.getPara() + ")";
    }
}
