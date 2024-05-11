
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
        this.result = 1;
        this.code = 9018;
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
        final RespOtherLogin other = (RespOtherLogin)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$result = this.getResult();
        final Object other$result = other.getResult();
        Label_0065: {
            if (this$result == null) {
                if (other$result == null) {
                    break Label_0065;
                }
            }
            else if (this$result.equals(other$result)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$code = this.getCode();
        final Object other$code = other.getCode();
        Label_0102: {
            if (this$code == null) {
                if (other$code == null) {
                    break Label_0102;
                }
            }
            else if (this$code.equals(other$code)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null) {
            if (other$content == null) {
                return true;
            }
        }
        else if (this$content.equals(other$content)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespOtherLogin;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $result = this.getResult();
        result = result * 59 + (($result == null) ? 43 : $result.hashCode());
        final Object $code = this.getCode();
        result = result * 59 + (($code == null) ? 43 : $code.hashCode());
        final Object $content = this.getContent();
        result = result * 59 + (($content == null) ? 43 : $content.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespOtherLogin(result=" + this.getResult() + ", code=" + this.getCode() + ", content=" + this.getContent() + ")";
    }
}
