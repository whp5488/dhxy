
package com.kitty.system.enums.war.wardef;

public enum STAR_PERCENT
{
    REN_M("男人", 318101, 0.05), 
    REN_W("女人", 318107, 0.13), 
    XIAN_M("男仙", 318103, 0.05), 
    XIAN_W("女仙", 318105, 0.2), 
    MO_M("男魔", 318110, 0.05), 
    MO_W("女魔", 318104, 0.25), 
    GUI_M("男鬼", 318102, 0.05), 
    GUI_W("女鬼", 318106, 0.12), 
    LONG_M("男龙", 318109, 0.05), 
    LONG_W("女龙", 318108, 0.05);
    
    private String name;
    private int serial;
    private double percent;
    
    public String getName() {
        return this.name;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public double getPercent() {
        return this.percent;
    }
    
    private STAR_PERCENT(final String name, final int serial, final double percent) {
        this.name = name;
        this.serial = serial;
        this.percent = percent;
    }
}
