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
    
    public RespTeamBallot() {
    }
    
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
        RespTeamBallot other = (RespTeamBallot)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isTeamLeader() != other.isTeamLeader()) {
            return false;
        }
        Object this$msg = this.getMsg();
        Object other$msg = other.getMsg();
        if (!((this$msg != null) ? this$msg.equals(other$msg) : (other$msg == null))) {
            return false;
        }
        if (this.getEndTime() != other.getEndTime()) {
            return false;
        }
        Object this$title = this.getTitle();
        Object other$title = other.getTitle();
        if (!((this$title != null) ? this$title.equals(other$title) : (other$title == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$para = this.getPara();
        Object other$para = other.getPara();
        return (this$para != null) ? this$para.equals(other$para) : (other$para == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTeamBallot;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isTeamLeader() ? 79 : 97);
        Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + this.getEndTime();
        Object $title = this.getTitle();
        result = result * 59 + (($title == null) ? 43 : $title.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTeamBallot(isTeamLeader=" + this.isTeamLeader() + ", msg=" + this.getMsg() + ", endTime=" + this.getEndTime() + ", title=" + this.getTitle() + ", type=" + this.getType() + ", para=" + this.getPara() + ")";
    }
}
