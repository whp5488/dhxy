
package com.kitty.system.game.gate.message;

import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45628)
public class ResplineData extends Message
{
    private List<LineData> datas;
    
    public List<LineData> getDatas() {
        return this.datas;
    }
    
    public void setDatas(final List<LineData> datas) {
        this.datas = datas;
    }
}
