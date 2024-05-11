
package com.kitty.system.game.welfare.message;

import com.kitty.system.game.welfare.message.vo.LuckDrawInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45381)
public class RespLuckDrawInfo extends Message
{
    private List<LuckDrawInfo> list;
    private byte classify;
    
    public List<LuckDrawInfo> getList() {
        return this.list;
    }
    
    public byte getClassify() {
        return this.classify;
    }
    
    public void setList(final List<LuckDrawInfo> list) {
        this.list = list;
    }
    
    public void setClassify(final byte classify) {
        this.classify = classify;
    }
}
