package com.kitty.system.init.domain.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class MindDefine
{
    private int classify;
    private int serial;
    private List<MindDefineRange> list;
    private Map<Integer, List<MindDefineRange>> map;
    
    public MindDefine(int classify, int serial, List<MindDefineRange> list) {
        this.map = new HashMap();
        this.classify = classify;
        this.serial = serial;
        this.list = list;
    }
    
    public int getClassify() {
        return this.classify;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public List<MindDefineRange> getList() {
        return this.list;
    }
    
    public Map<Integer, List<MindDefineRange>> getMap() {
        return this.map;
    }
    
    public void setClassify(final int classify) {
        this.classify = classify;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setList(final List<MindDefineRange> list) {
        this.list = list;
    }
    
    public void setMap(final Map<Integer, List<MindDefineRange>> map) {
        this.map = map;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MindDefine)) {
            return false;
        }
        MindDefine other = (MindDefine)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getClassify() != other.getClassify()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        if (!((this$list != null) ? this$list.equals(other$list) : (other$list == null))) {
            return false;
        }
        Object this$map = this.getMap();
        Object other$map = other.getMap();
        return (this$map != null) ? this$map.equals(other$map) : (other$map == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MindDefine;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getClassify();
        result = result * 59 + this.getSerial();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        Object $map = this.getMap();
        result = result * 59 + (($map == null) ? 43 : $map.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "MindDefine(classify=" + this.getClassify() + ", serial=" + this.getSerial() + ", list=" + this.getList() + ", map=" + this.getMap() + ")";
    }
    
    public MindDefine(final int classify, final int serial, final List<MindDefineRange> list, final Map<Integer, List<MindDefineRange>> map) {
        this.map = new HashMap();
        this.classify = classify;
        this.serial = serial;
        this.list = list;
        this.map = map;
    }
    
    public MindDefine() {
        this.map = new HashMap();
    }
}
