
package com.kitty.system.game.welfare.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41240)
public class RespNewDistrictSign extends Message
{
    private String json;
    private int day;
    
    public String getJson() {
        return this.json;
    }
    
    public int getDay() {
        return this.day;
    }
    
    public void setJson(final String json) {
        this.json = json;
    }
    
    public void setDay(final int day) {
        this.day = day;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespNewDistrictSign)) {
            return false;
        }
        final RespNewDistrictSign other = (RespNewDistrictSign)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$json = this.getJson();
        final Object other$json = other.getJson();
        if (this$json == null) {
            if (other$json == null) {
                return this.getDay() == other.getDay();
            }
        }
        else if (this$json.equals(other$json)) {
            return this.getDay() == other.getDay();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespNewDistrictSign;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $json = this.getJson();
        result = result * 59 + (($json == null) ? 43 : $json.hashCode());
        result = result * 59 + this.getDay();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespNewDistrictSign(json=" + this.getJson() + ", day=" + this.getDay() + ")";
    }
    
    @ConstructorProperties({ "json", "day" })
    public RespNewDistrictSign(final String json, final int day) {
        this.day = 1;
        this.json = json;
        this.day = day;
    }
    
    public RespNewDistrictSign() {
        this.day = 1;
    }
}
