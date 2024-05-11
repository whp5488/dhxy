
package com.kitty.common.utils;

import java.util.Arrays;

public class BitSet
{
    private long[] BV;
    private Boolean[] bit;
    
    private void initiate() {
        for (int i = 1; i <= 32; ++i) {
            this.BV[i - 1] = (long)Math.pow(2.0, i - 1);
        }
    }
    
    public static BitSet NEW() {
        return new BitSet(0L);
    }
    
    public BitSet(final long i32) {
        this.BV = new long[32];
        this.bit = new Boolean[32];
        this.initiate();
        this.set(i32);
    }
    
    private void setI32(final int startPos, long i32) {
        for (int j = 1; j <= 32; ++j) {
            final int k = 33 - j - 1;
            if (i32 >= this.BV[k]) {
                this.bit[startPos + k] = true;
                i32 -= this.BV[k];
            }
            else {
                this.bit[startPos + k] = false;
            }
        }
    }
    
    public void set(final long i32) {
        final long[] nums = { i32 };
        int startPos = 0;
        for (final long num : nums) {
            this.setI32(startPos, num);
            startPos += 32;
        }
    }
    
    public void setBit(int index, final Boolean value) {
        if (index >= 32) {
            index -= 32;
        }
        if (index < 0 || index > this.bit.length) {
            return;
        }
        this.bit[index] = value;
    }
    
    public void setBit(int index) {
        if (--index >= 32) {
            index -= 32;
        }
        if (index < 0 || index > this.bit.length) {
            return;
        }
        this.bit[index] = true;
    }
    
    public boolean isSet(final int index) {
        return index >= 0 && index <= this.bit.length && this.bit[index];
    }
    
    public int getI32() {
        int i32 = 0;
        int j = 0;
        for (final Boolean boo : this.bit) {
            if (boo != null && boo) {
                i32 += (int)this.BV[j];
            }
            ++j;
        }
        return i32;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BitSet bitSet = (BitSet)o;
        return Arrays.equals(this.BV, bitSet.BV);
    }
}
