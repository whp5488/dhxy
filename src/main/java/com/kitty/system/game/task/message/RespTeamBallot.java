
package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45111)
public class RespTeamBallot extends Message
{
    private boolean isTeamLeader;
    private String msg;
    private int endTime;
    private String title;
    private String type;
    private String para;
    
    public boolean isTeamLeader() {
        return this.isTeamLeader;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public int getEndTime() {
        return this.endTime;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public void setTeamLeader(final boolean isTeamLeader) {
        this.isTeamLeader = isTeamLeader;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setEndTime(final int endTime) {
        this.endTime = endTime;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespTeamBallot)) {
            return false;
        }
        final RespTeamBallot other = (RespTeamBallot)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isTeamLeader() != other.isTeamLeader()) {
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
        if (this.getEndTime() != other.getEndTime()) {
            return false;
        }
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        Label_0128: {
            if (this$title == null) {
                if (other$title == null) {
                    break Label_0128;
                }
            }
            else if (this$title.equals(other$title)) {
                break Label_0128;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0165: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0165;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0165;
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
        return other instanceof RespTeamBallot;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isTeamLeader() ? 79 : 97);
        final Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + this.getEndTime();
        final Object $title = this.getTitle();
        result = result * 59 + (($title == null) ? 43 : $title.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTeamBallot(isTeamLeader=" + this.isTeamLeader() + ", msg=" + this.getMsg() + ", endTime=" + this.getEndTime() + ", title=" + this.getTitle() + ", type=" + this.getType() + ", para=" + this.getPara() + ")";
    }
}
