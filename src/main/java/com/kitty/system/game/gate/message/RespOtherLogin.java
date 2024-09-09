package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45140)
public class RespOtherLogin extends Message
{
    private Integer result;
    private Integer code;
    private String content;
    
    public RespOtherLogin() {
        this.result = Integer.valueOf(1);
        this.code = Integer.valueOf(9018);
    }
    
    public Integer getResult() {
        return this.result;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public void setResult(final Integer result) {
        this.result = result;
    }
    
    public void setCode(final Integer code) {
        this.code = code;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespOtherLogin)) {
            return false;
        }
        RespOtherLogin other = (RespOtherLogin)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$result = this.getResult();
        Object other$result = other.getResult();
        if (!((this$result != null) ? this$result.equals(other$result) : (other$result == null))) {
            return false;
        }
        Object this$code = this.getCode();
        Object other$code = other.getCode();
        if (!((this$code != null) ? this$code.equals(other$code) : (other$code == null))) {
            return false;
        }
        Object this$content = this.getContent();
        Object other$content = other.getContent();
        return (this$content != null) ? this$content.equals(other$content) : (other$content == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespOtherLogin;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $result = this.getResult();
        result = result * 59 + (($result == null) ? 43 : $result.hashCode());
        Object $code = this.getCode();
        result = result * 59 + (($code == null) ? 43 : $code.hashCode());
        Object $content = this.getContent();
        result = result * 59 + (($content == null) ? 43 : $content.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespOtherLogin(result=" + this.getResult() + ", code=" + this.getCode() + ", content=" + this.getContent() + ")";
    }
}
