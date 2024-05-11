
package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53574)
public class ReqTempFriendState extends Message
{
    private String gid;
    private String distName;
    
    public String getGid() {
        return this.gid;
    }
    
    public String getDistName() {
        return this.distName;
    }
}
