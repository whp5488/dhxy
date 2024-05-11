
package com.kitty.system.game.onlinemall.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 12219)
public class RespBuyResult extends Message
{
    private short result;
    private String barcode;
    
    public RespBuyResult(final short result, final String barcode) {
        this.result = result;
        this.barcode = barcode;
    }
    
    public void setResult(final short result) {
        this.result = result;
    }
    
    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }
    
    public short getResult() {
        return this.result;
    }
    
    public String getBarcode() {
        return this.barcode;
    }
}
