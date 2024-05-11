
package com.kitty.system.game.ranking.pojo;

import java.beans.ConstructorProperties;
import com.kitty.system.game.activity.model.user.ca.vo.CaCache;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import java.io.Serializable;

public class CaRank implements Serializable
{
    private int roleId;
    private String name;
    private int serial;
    private byte relive;
    private short level;
    private int score;
    private int rank;
    private String server;
    
    public CaRank(final CaRankRecord record) {
        this.roleId = record.getRoleId();
        this.name = record.getName();
        this.serial = record.getSerial();
        this.relive = (byte)(record.getGrade() / 1000);
        this.level = (short)(record.getGrade() % 1000);
        this.score = record.getScore();
        this.rank = record.getFullRank();
    }
    
    public CaRank(final CaCache caCache, final String server) {
        this.roleId = caCache.getRoleId();
        this.name = caCache.getName();
        this.serial = caCache.getSerial();
        this.relive = (byte)(caCache.getGrade() / 1000);
        this.level = (short)(caCache.getGrade() % 1000);
        this.score = caCache.getScore();
        this.server = server;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getRelive() {
        return this.relive;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public int getRank() {
        return this.rank;
    }
    
    public String getServer() {
        return this.server;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRelive(final byte relive) {
        this.relive = relive;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setRank(final int rank) {
        this.rank = rank;
    }
    
    public void setServer(final String server) {
        this.server = server;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CaRank)) {
            return false;
        }
        final CaRank other = (CaRank)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0078: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0078;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0078;
            }
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getRelive() != other.getRelive()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getScore() != other.getScore()) {
            return false;
        }
        if (this.getRank() != other.getRank()) {
            return false;
        }
        final Object this$server = this.getServer();
        final Object other$server = other.getServer();
        if (this$server == null) {
            if (other$server == null) {
                return true;
            }
        }
        else if (this$server.equals(other$server)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CaRank;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getRelive();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getScore();
        result = result * 59 + this.getRank();
        final Object $server = this.getServer();
        result = result * 59 + (($server == null) ? 43 : $server.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "CaRank(roleId=" + this.getRoleId() + ", name=" + this.getName() + ", serial=" + this.getSerial() + ", relive=" + this.getRelive() + ", level=" + this.getLevel() + ", score=" + this.getScore() + ", rank=" + this.getRank() + ", server=" + this.getServer() + ")";
    }
    
    @ConstructorProperties({ "roleId", "name", "serial", "relive", "level", "score", "rank", "server" })
    public CaRank(final int roleId, final String name, final int serial, final byte relive, final short level, final int score, final int rank, final String server) {
        this.roleId = roleId;
        this.name = name;
        this.serial = serial;
        this.relive = relive;
        this.level = level;
        this.score = score;
        this.rank = rank;
        this.server = server;
    }
    
    public CaRank() {
    }
}
