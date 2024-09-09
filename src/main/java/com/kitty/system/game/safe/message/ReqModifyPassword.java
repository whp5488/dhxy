package com.kitty.system.game.safe.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32830)
public class ReqModifyPassword extends Message
{
    private String oldPassword;
    private String newPassword;
    
    public ReqModifyPassword() {
    }
    
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
        ReqModifyPassword other = (ReqModifyPassword)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$oldPassword = this.getOldPassword();
        Object other$oldPassword = other.getOldPassword();
        if (!((this$oldPassword != null) ? this$oldPassword.equals(other$oldPassword) : (other$oldPassword == null))) {
            return false;
        }
        Object this$newPassword = this.getNewPassword();
        Object other$newPassword = other.getNewPassword();
        return (this$newPassword != null) ? this$newPassword.equals(other$newPassword) : (other$newPassword == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqModifyPassword;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $oldPassword = this.getOldPassword();
        result = result * 59 + (($oldPassword == null) ? 43 : $oldPassword.hashCode());
        Object $newPassword = this.getNewPassword();
        result = result * 59 + (($newPassword == null) ? 43 : $newPassword.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqModifyPassword(oldPassword=" + this.getOldPassword() + ", newPassword=" + this.getNewPassword() + ")";
    }
}
