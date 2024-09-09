package com.kitty.system.game.gate;

import java.util.Random;

public class LoginAuth
{
    private int authKey;
    private int seed;
    private long uid;
    
    public LoginAuth() {
        Random random = new Random();
        this.authKey = random.nextInt(8888888) + 1111111;
        this.seed = random.nextInt(888888888) + 111111111;
    }
    
    public int getAuthKey() {
        return this.authKey;
    }
    
    public int getSeed() {
        return this.seed;
    }
    
    public long getUid() {
        return this.uid;
    }
    
    public void setAuthKey(final int authKey) {
        this.authKey = authKey;
    }
    
    public void setSeed(final int seed) {
        this.seed = seed;
    }
    
    public void setUid(final long uid) {
        this.uid = uid;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LoginAuth)) {
            return false;
        }
        LoginAuth other = (LoginAuth)o;
        return other.canEqual(this) && this.getAuthKey() == other.getAuthKey() && this.getSeed() == other.getSeed() && this.getUid() == other.getUid();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof LoginAuth;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getAuthKey();
        result = result * 59 + this.getSeed();
        long $uid = this.getUid();
        result = result * 59 + (int)($uid >>> 32 ^ $uid);
        return result;
    }
    
    @Override
    public String toString() {
        return "LoginAuth(authKey=" + this.getAuthKey() + ", seed=" + this.getSeed() + ", uid=" + this.getUid() + ")";
    }
}
