
package com.kitty.system.game.party.message;

import com.kitty.system.game.party.message.vo.PartyLogInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 9011)
public class RespPartyLog extends Message
{
    private List<PartyLogInfo> list;
    
    public void setList(final List<PartyLogInfo> list) {
        this.list = list;
    }
    
    public List<PartyLogInfo> getList() {
        return this.list;
    }
}
