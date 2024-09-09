package com.kitty.system.enums.email;

public enum Sender
{
    TYPE_1(1, "系统"), 
    TYPE_2(2, "帮派"), 
    TYPE_90247(90247, "礼部");
    
    private int note;
    private String value;
    
    public static String getName(int note) {
        String name = "系统";
        Sender[] values = values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            Sender n = values[i];
            if (n.note == note) {
                name = n.getValue();
                break;
            }
            ++i;
        }
        return name;
    }
    
    public int getNote() {
        return this.note;
    }
    
    public String getValue() {
        return this.value;
    }
    
    private Sender(final int note, final String value) {
        this.note = note;
        this.value = value;
    }
}
