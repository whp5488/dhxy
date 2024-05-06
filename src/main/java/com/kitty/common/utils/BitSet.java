package com.kitty.common.utils;

import java.util.Arrays;

public class BitSet {
  private long[] BV = new long[32];
  
  private Boolean[] bit = new Boolean[32];
  
  private void initiate() {
    for (int i = 1; i <= 32; i++)
      this.BV[i - 1] = (long)Math.pow(2.0D, (i - 1)); 
  }
  
  public BitSet(long i32) {
    initiate();
    set(i32);
  }
  
  public static BitSet NEW() {
    return new BitSet(0L);
  }
  
  private void setI32(int startPos, long i32) {
    for (int j = 1; j <= 32; j++) {
      int i = 33 - j - 1;
      if (i32 >= this.BV[i]) {
        this.bit[startPos + i] = Boolean.TRUE;
        i32 -= this.BV[i];
      } else {
        this.bit[startPos + i] = Boolean.FALSE;
      } 
    } 
  }
  
  public void set(long i32) {
    long[] nums = { i32 };
    int startPos = 0;
    for (long num : nums) {
      setI32(startPos, num);
      startPos += 32;
    } 
  }
  
  public void setBit(int index, Boolean value) {
    if (index >= 32)
      index -= 32; 
    if (index < 0 || index > this.bit.length)
      return; 
    this.bit[index] = value;
  }
  
  public void setBit(int index) {
    index--;
    if (index >= 32)
      index -= 32; 
    if (index < 0 || index > this.bit.length)
      return; 
    this.bit[index] = Boolean.TRUE;
  }
  
  public boolean isSet(int index) {
    if (index < 0 || index > this.bit.length)
      return false; 
    return this.bit[index];
  }
  
  public int getI32() {
    int i32 = 0;
    int i = 0;
    for (Boolean boo : this.bit) {
      if (boo != null && boo)
        i32 = (int)(i32 + this.BV[i]); 
      i++;
    } 
    return i32;
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    BitSet bitSet = (BitSet)o;
    return Arrays.equals(this.BV, bitSet.BV);
  }
}
