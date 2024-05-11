
package com.kitty.system.game.chat.message;

import com.kitty.system.game.vo.UnifiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 40962, cmd = 1)
public class RespRoleInfo extends Message
{
    private String uuId;
    private String type;
    private List<UnifiedValue> list;
    
    public String getUuId() {
        return this.uuId;
    }
    
    public void setUuId(final String uuId) {
        this.uuId = uuId;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public List<UnifiedValue> getList() {
        return this.list;
    }
    
    public void setList(final List<UnifiedValue> list) {
        this.list = list;
    }
}
