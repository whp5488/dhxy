
package com.kitty.system.game.party.message;

import com.kitty.system.game.party.message.vo.ActiviesInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32839)
public class RespRedBagSendInfo extends Message
{
    private short leftTimes;
    private short memberCount;
    private List<ActiviesInfo> activiesInfoList;
    
    public void setLeftTimes(final short leftTimes) {
        this.leftTimes = leftTimes;
    }
    
    public void setMemberCount(final short memberCount) {
        this.memberCount = memberCount;
    }
    
    public void setActiviesInfoList(final List<ActiviesInfo> activiesInfoList) {
        this.activiesInfoList = activiesInfoList;
    }
    
    public short getLeftTimes() {
        return this.leftTimes;
    }
    
    public short getMemberCount() {
        return this.memberCount;
    }
    
    public List<ActiviesInfo> getActiviesInfoList() {
        return this.activiesInfoList;
    }
}
