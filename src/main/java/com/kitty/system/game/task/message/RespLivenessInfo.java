
package com.kitty.system.game.task.message;

import com.kitty.system.game.task.message.vo.ActivityData;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 36883)
public class RespLivenessInfo extends Message
{
    private List<ActivityData> list;
    private byte rewardStatus;
    
    public RespLivenessInfo() {
        this.rewardStatus = 0;
    }
    
    public RespLivenessInfo(final List<ActivityData> list, final byte rewardStatus) {
        this.rewardStatus = 0;
        this.list = list;
        this.rewardStatus = rewardStatus;
    }
    
    public List<ActivityData> getList() {
        return this.list;
    }
    
    public void setList(final List<ActivityData> list) {
        this.list = list;
    }
    
    public byte getRewardStatus() {
        return this.rewardStatus;
    }
    
    public void setRewardStatus(final byte rewardStatus) {
        this.rewardStatus = rewardStatus;
    }
}
