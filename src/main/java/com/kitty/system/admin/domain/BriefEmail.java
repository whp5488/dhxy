
package com.kitty.system.admin.domain;

import com.kitty.system.game.email.domain.EmailSystemAccessory;
import java.util.List;
import java.io.Serializable;

public class BriefEmail implements Serializable
{
    private int rec;
    private String title;
    private String con;
    private Long time;
    private int sen;
    private List<EmailSystemAccessory> list;
    
    public int getRec() {
        return this.rec;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getCon() {
        return this.con;
    }
    
    public Long getTime() {
        return this.time;
    }
    
    public int getSen() {
        return this.sen;
    }
    
    public List<EmailSystemAccessory> getList() {
        return this.list;
    }
    
    public void setRec(final int rec) {
        this.rec = rec;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setCon(final String con) {
        this.con = con;
    }
    
    public void setTime(final Long time) {
        this.time = time;
    }
    
    public void setSen(final int sen) {
        this.sen = sen;
    }
    
    public void setList(final List<EmailSystemAccessory> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BriefEmail)) {
            return false;
        }
        final BriefEmail other = (BriefEmail)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRec() != other.getRec()) {
            return false;
        }
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        Label_0078: {
            if (this$title == null) {
                if (other$title == null) {
                    break Label_0078;
                }
            }
            else if (this$title.equals(other$title)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$con = this.getCon();
        final Object other$con = other.getCon();
        Label_0115: {
            if (this$con == null) {
                if (other$con == null) {
                    break Label_0115;
                }
            }
            else if (this$con.equals(other$con)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$time = this.getTime();
        final Object other$time = other.getTime();
        Label_0152: {
            if (this$time == null) {
                if (other$time == null) {
                    break Label_0152;
                }
            }
            else if (this$time.equals(other$time)) {
                break Label_0152;
            }
            return false;
        }
        if (this.getSen() != other.getSen()) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof BriefEmail;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRec();
        final Object $title = this.getTitle();
        result = result * 59 + (($title == null) ? 43 : $title.hashCode());
        final Object $con = this.getCon();
        result = result * 59 + (($con == null) ? 43 : $con.hashCode());
        final Object $time = this.getTime();
        result = result * 59 + (($time == null) ? 43 : $time.hashCode());
        result = result * 59 + this.getSen();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "BriefEmail(rec=" + this.getRec() + ", title=" + this.getTitle() + ", con=" + this.getCon() + ", time=" + this.getTime() + ", sen=" + this.getSen() + ", list=" + this.getList() + ")";
    }
}
