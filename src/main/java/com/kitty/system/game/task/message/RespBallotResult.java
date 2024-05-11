
package com.kitty.system.game.task.message;

import com.kitty.system.game.task.message.vo.BallotInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45127)
public class RespBallotResult extends Message
{
    private short meTime;
    private List<BallotInfo> resultList;
    
    public RespBallotResult() {
        this.meTime = -1;
    }
    
    public short getMeTime() {
        return this.meTime;
    }
    
    public void setMeTime(final short meTime) {
        this.meTime = meTime;
    }
    
    public List<BallotInfo> getResultList() {
        return this.resultList;
    }
    
    public void setResultList(final List<BallotInfo> resultList) {
        this.resultList = resultList;
    }
}
