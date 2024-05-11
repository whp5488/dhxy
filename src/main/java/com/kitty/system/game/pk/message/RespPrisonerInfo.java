
package com.kitty.system.game.pk.message;

import java.util.ArrayList;
import com.kitty.system.game.pk.message.vo.PrisonerInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45230)
public class RespPrisonerInfo extends Message
{
    private List<PrisonerInfo> list;
    
    public RespPrisonerInfo() {
        this.list = new ArrayList<PrisonerInfo>();
    }
    
    public List<PrisonerInfo> getList() {
        return this.list;
    }
    
    public void setList(final List<PrisonerInfo> list) {
        this.list = list;
    }
}
