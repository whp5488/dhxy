package com.kitty.system.game.mind.message;

import java.util.ArrayList;
import com.kitty.system.game.mind.domain.Mind;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20302)
public class RespMind extends Message
{
    private int serial;
    private List<FieldProp> refine;
    
    public RespMind(Mind mind) {
        this.refine = new ArrayList<>();
        this.serial = mind.getSerial();
        this.refine = mind.getRefine();
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public List<FieldProp> getRefine() {
        return this.refine;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRefine(final List<FieldProp> refine) {
        this.refine = refine;
    }

    public RespMind(final int serial, final List<FieldProp> refine) {
        this.refine = new ArrayList<>();
        this.serial = serial;
        this.refine = refine;
    }
    
    public RespMind() {
        this.refine = new ArrayList<>();
    }
}
