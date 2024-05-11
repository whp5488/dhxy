
package com.kitty.system.game.role.domain.vo;

import java.util.ArrayList;
import com.kitty.system.game.bag.domain.BagEquip;
import org.codehaus.jackson.annotate.JsonIgnore;
import com.kitty.system.game.role.domain.Goods;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class StorePara implements Serializable
{
    private int t;
    private int s;
    private int n;
    private String v;
    private List<FiedValue> rests;
    @JsonIgnore
    private List<Goods> goods;
    @JsonIgnore
    private List<BagEquip> equips;
    
    public StorePara() {
        this.rests = new ArrayList<FiedValue>();
        this.goods = new ArrayList<Goods>();
        this.equips = new ArrayList<BagEquip>();
    }
    
    public int getT() {
        return this.t;
    }
    
    public int getS() {
        return this.s;
    }
    
    public int getN() {
        return this.n;
    }
    
    public String getV() {
        return this.v;
    }
    
    public List<FiedValue> getRests() {
        return this.rests;
    }
    
    public List<Goods> getGoods() {
        return this.goods;
    }
    
    public List<BagEquip> getEquips() {
        return this.equips;
    }
    
    public void setT(final int t) {
        this.t = t;
    }
    
    public void setS(final int s) {
        this.s = s;
    }
    
    public void setN(final int n) {
        this.n = n;
    }
    
    public void setV(final String v) {
        this.v = v;
    }
    
    public void setRests(final List<FiedValue> rests) {
        this.rests = rests;
    }
    
    public void setGoods(final List<Goods> goods) {
        this.goods = goods;
    }
    
    public void setEquips(final List<BagEquip> equips) {
        this.equips = equips;
    }
}
