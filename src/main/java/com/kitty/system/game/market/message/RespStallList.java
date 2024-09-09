package com.kitty.system.game.market.message;

import com.kitty.system.game.market.model.StallItem;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49183)
public class RespStallList extends Message
{
    private short totalPage;
    private short currPage;
    private List<StallItem> list;
    private String classify;
    
    public RespStallList() {
    }
    
    public void setTotalPage(final short totalPage) {
        this.totalPage = totalPage;
    }
    
    public void setCurrPage(final short currPage) {
        this.currPage = currPage;
    }
    
    public void setList(final List<StallItem> list) {
        this.list = list;
    }
    
    public void setClassify(final String classify) {
        this.classify = classify;
    }
    
    public short getTotalPage() {
        return this.totalPage;
    }
    
    public short getCurrPage() {
        return this.currPage;
    }
    
    public List<StallItem> getList() {
        return this.list;
    }
    
    public String getClassify() {
        return this.classify;
    }
}
