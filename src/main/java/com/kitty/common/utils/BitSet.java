package com.kitty.common.utils;

import java.util.Arrays;

public class BitSet
{
    private long[] BV;
    private Boolean[] bit;
    
    private void initiate() {
        for (int i = 1; i <= 32; ++i) {
            this.BV[i - 1] = (long)Math.pow(2.0, (double)(i - 1));
        }
    }
    
    public static BitSet NEW() {
        return new BitSet(0L);
    }
    
    public BitSet(long i32) {
        this.BV = new long[32];
        this.bit = new Boolean[32];
        this.initiate();
        this.set(i32);
    }
    
    private void setI32(int startPos, long i32) {
        for (int j = 1; j <= 32; ++j) {
            int k = 33 - j - 1;
            if (i32 >= this.BV[k]) {
                this.bit[startPos + k] = Boolean.valueOf(true);
                i32 -= this.BV[k];
            }
            else {
                this.bit[startPos + k] = Boolean.valueOf(false);
            }
        }
    }
    
    public void set(long i32) {
        long[] nums = { i32 };
        int startPos = 0;
        for (long num : nums) {
            this.setI32(startPos, num);
            startPos += 32;
        }
    }
    
    public void setBit(int index, Boolean value) {
        if (index >= 32) {
            index -= 32;
        }
        if (index < 0 || index > this.bit.length) {
            return;
        }
        this.bit[index] = value;
        return;
    }
    
    public void setBit(int index) {
        if (--index >= 32) {
            index -= 32;
        }
        if (index < 0 || index > this.bit.length) {
            return;
        }
        this.bit[index] = Boolean.valueOf(true);
        return;
    }
    
    public boolean isSet(int index) {
        return index >= 0 && index <= this.bit.length && (boolean)this.bit[index];
    }
    
    public int getI32() {
        int i32 = 0;
        int j = 0;
        for (Boolean boo : this.bit) {
            if (boo != null && (boolean)boo) {
                i32 = (int)((long)i32 + this.BV[j]);
            }
            ++j;
        }
        return i32;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        BitSet bitSet = (BitSet)o;
        return Arrays.equals(this.BV, bitSet.BV);
    }
}
