package com.kitty.system.enums.pet;

public enum ConciseType
{
    COMMON(Integer.valueOf(1), Integer.valueOf(1)), 
    HIGHER(Integer.valueOf(2), Integer.valueOf(5)), 
    SUPRA(Integer.valueOf(3), Integer.valueOf(17));
    
    private Integer note;
    private Integer value;
    
    public static Integer getValue(Integer note) {
        Integer value = ConciseType.COMMON.value;
        ConciseType[] values = values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            ConciseType concise = values[i];
            if (concise.getNote().equals(note)) {
                value = concise.getValue();
                break;
            }
            ++i;
        }
        return value;
    }
    
    public Integer getNote() {
        return this.note;
    }
    
    public Integer getValue() {
        return this.value;
    }
    
    private ConciseType(final Integer note, final Integer value) {
        this.note = note;
        this.value = value;
    }
}
