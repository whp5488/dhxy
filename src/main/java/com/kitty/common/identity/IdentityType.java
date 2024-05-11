
package com.kitty.common.identity;

public enum IdentityType
{
    PET(2), 
    PARTNER(3), 
    ROLE(5), 
    PARTY(6);
    
    int type;
    boolean save;
    int value;
    int addValue;
    
    private IdentityType(final int type) {
        this.type = type;
        this.save = Boolean.TRUE;
        this.installValue(type);
    }
    
    public int getType() {
        return this.type;
    }
    
    public boolean isSave() {
        return this.save;
    }
    
    public void installValue(final int type) {
        switch (type) {
            case 2: {
                this.addValue = 5000000;
                this.value = 600000000;
                break;
            }
            case 3: {
                this.addValue = 2400000;
                this.value = 200000000;
                break;
            }
            case 5: {
                this.addValue = 100000;
                this.value = 10001568;
                break;
            }
            case 6: {
                this.addValue = 10000;
                this.value = 335546;
                break;
            }
        }
    }
    
    public int gainValue(final int type) {
        int returValue = -1;
        for (final IdentityType n : values()) {
            if (n.type == type) {
                returValue = n.addValue;
                break;
            }
        }
        return returValue;
    }
    
    public int getAddValue() {
        return this.addValue;
    }
    
    public int getValue() {
        return this.value;
    }
}
