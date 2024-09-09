package com.kitty.system.game.train.message;

import java.util.Iterator;
import com.kitty.system.game.train.domain.vo.FieldSeal;
import com.kitty.system.game.train.domain.TrainSeal;
import java.util.ArrayList;
import com.kitty.system.game.vo.FieldSealProp;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20168)
public class RespTrainSeal extends Message
{
    private String id;
    private int bd;
    private int serial;
    private int position;
    private List<FieldSealProp> refine;
    private byte t;
    
    public RespTrainSeal() {
        this.bd = 0;
        this.refine = new ArrayList<>();
        this.t = 0;
    }
    
    public RespTrainSeal(TrainSeal seal, byte type) {
        this.bd = 0;
        this.refine = new ArrayList<>();
        this.t = 0;
        this.id = String.valueOf(seal.getId());
        this.bd = seal.getBd();
        this.serial = seal.getSerial();
        this.position = seal.getPosition();
        this.t = type;
        for (FieldSeal fieldSeal : seal.getRefine()) {
            this.refine.add(new FieldSealProp(fieldSeal.getT(), fieldSeal.getK(), fieldSeal.getV()));
        }
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getBd() {
        return this.bd;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public List<FieldSealProp> getRefine() {
        return this.refine;
    }
    
    public byte getT() {
        return this.t;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setBd(final int bd) {
        this.bd = bd;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPosition(final int position) {
        this.position = position;
    }
    
    public void setRefine(final List<FieldSealProp> refine) {
        this.refine = refine;
    }
    
    public void setT(final byte t) {
        this.t = t;
    }
}
