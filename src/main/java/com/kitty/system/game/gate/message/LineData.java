package com.kitty.system.game.gate.message;

public class LineData
{
    private String line_name;
    private int expect_time;
    
    public LineData() {
    }
    
    public String getLine_name() {
        return this.line_name;
    }
    
    public void setLine_name(String line_name) {
        this.line_name = line_name;
    }
    
    public int getExpect_time() {
        return this.expect_time;
    }
    
    public void setExpect_time(int expect_time) {
        this.expect_time = expect_time;
    }
}
