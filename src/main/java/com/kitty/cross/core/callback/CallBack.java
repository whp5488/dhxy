
package com.kitty.cross.core.callback;

import com.kitty.logs.LoggerUtils;
import java.util.concurrent.atomic.AtomicInteger;
import com.kitty.mina.message.Message;

public class CallBack
{
    private int index;
    private Message data;
    private static final AtomicInteger idFactory;
    
    public static int nextMsgId() {
        return CallBack.idFactory.getAndIncrement();
    }
    
    protected void doTimeOut() {
        LoggerUtils.error("回调方法已过期");
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public Message getData() {
        return this.data;
    }
    
    public void setData(final Message data) {
        this.data = data;
    }
    
    public boolean isDone() {
        return this.data != null;
    }
    
    static {
        idFactory = new AtomicInteger();
    }
}
