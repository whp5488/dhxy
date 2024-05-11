
package com.kitty.system.game.safe.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32830)
public class ReqModifyPassword extends Message
{
    private String oldPassword;
    private String newPassword;
    
    public String getOldPassword() {
        return this.oldPassword;
    }
    
    public String getNewPassword() {
        return this.newPassword;
    }
    
    public void setOldPassword(final String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqModifyPassword)) {
            return false;
        }
        final ReqModifyPassword other = (ReqModifyPassword)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$oldPassword = this.getOldPassword();
        final Object other$oldPassword = other.getOldPassword();
        Label_0065: {
            if (this$oldPassword == null) {
                if (other$oldPassword == null) {
                    break Label_0065;
                }
            }
            else if (this$oldPassword.equals(other$oldPassword)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$newPassword = this.getNewPassword();
        final Object other$newPassword = other.getNewPassword();
        if (this$newPassword == null) {
            if (other$newPassword == null) {
                return true;
            }
        }
        else if (this$newPassword.equals(other$newPassword)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqModifyPassword;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $oldPassword = this.getOldPassword();
        result = result * 59 + (($oldPassword == null) ? 43 : $oldPassword.hashCode());
        final Object $newPassword = this.getNewPassword();
        result = result * 59 + (($newPassword == null) ? 43 : $newPassword.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqModifyPassword(oldPassword=" + this.getOldPassword() + ", newPassword=" + this.getNewPassword() + ")";
    }
}
