package com.kitty.system.game.market.message;

import java.util.Iterator;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45104)
public class RespMarketGoodCard extends Message
{
    private String id;
    private int roleId;
    private int serial;
    private List<FieldProp> field;
    private List<FieldProp> artifice;
    private List<FiedValue> para;
    private byte status;
    private int endTime;
    
    public RespMarketGoodCard() {
        this.field = new ArrayList<>();
        this.artifice = new ArrayList<>();
        this.para = new ArrayList<>();
        this.status = 2;
    }
    
    public RespMarketGoodCard(String id, int roleId, int serial, List<FiedEquip> field, List<FiedEquip> artifice) {
        this.field = new ArrayList<>();
        this.artifice = new ArrayList<>();
        this.para = new ArrayList<>();
        this.status = 2;
        this.id = id;
        this.roleId = roleId;
        this.serial = serial;
        for (FiedEquip fiedEquip : field) {
            this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
        for (FiedEquip fiedEquip : artifice) {
            this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
    }
    
    public RespMarketGoodCard(String id, int roleId, int serial, List<FiedEquip> field, List<FiedEquip> artifice, List<FiedValue> para, byte status, int endTime) {
        this.field = new ArrayList<>();
        this.artifice = new ArrayList<>();
        this.para = new ArrayList<>();
        this.status = 2;
        this.id = id;
        this.roleId = roleId;
        this.serial = serial;
        this.para = para;
        this.status = status;
        this.endTime = endTime;
        for (FiedEquip fiedEquip : field) {
            this.field.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
        for (FiedEquip fiedEquip : artifice) {
            this.artifice.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
        }
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setField(final List<FieldProp> field) {
        this.field = field;
    }
    
    public void setArtifice(final List<FieldProp> artifice) {
        this.artifice = artifice;
    }
    
    public void setPara(final List<FiedValue> para) {
        this.para = para;
    }
    
    public void setStatus(final byte status) {
        this.status = status;
    }
    
    public void setEndTime(final int endTime) {
        this.endTime = endTime;
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public List<FieldProp> getField() {
        return this.field;
    }
    
    public List<FieldProp> getArtifice() {
        return this.artifice;
    }
    
    public List<FiedValue> getPara() {
        return this.para;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public int getEndTime() {
        return this.endTime;
    }
}
