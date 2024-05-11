
package com.kitty.system.game.team.message;

import java.util.ArrayList;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61591)
public class RespClearnRequest extends Message
{
    private String type;
    private List<String> names;
    
    public RespClearnRequest() {
        this.names = new ArrayList<String>();
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public List<String> getNames() {
        return this.names;
    }
    
    public void setNames(final List<String> names) {
        this.names = names;
    }
}
