
package com.kitty.system.game.party.message;

import com.kitty.system.game.party.message.vo.RedBagInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32845)
public class RespPartyRedBagList extends Message
{
    private List<RedBagInfo> redBagInfoList;
    
    public void setRedBagInfoList(final List<RedBagInfo> redBagInfoList) {
        this.redBagInfoList = redBagInfoList;
    }
    
    public List<RedBagInfo> getRedBagInfoList() {
        return this.redBagInfoList;
    }
}
