
package com.kitty.system.game.fightnew.messages;

import com.kitty.system.game.fightnew.messages.vo.FightObjectAttr;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4341)
public class RespFightObjectAttr extends Message
{
    private List<FightObjectAttr> list;
    
    public List<FightObjectAttr> getList() {
        return this.list;
    }
    
    public void setList(final List<FightObjectAttr> list) {
        this.list = list;
    }
}
