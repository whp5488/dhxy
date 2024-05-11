
package com.kitty.system.game.ranking.pojo;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class MaterTeamRank implements Serializable
{
    private List<MaterRank> list;
    private int score;
    private int restsScore;
    
    public List<MaterRank> getList() {
        return this.list;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public int getRestsScore() {
        return this.restsScore;
    }
    
    public void setList(final List<MaterRank> list) {
        this.list = list;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setRestsScore(final int restsScore) {
        this.restsScore = restsScore;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MaterTeamRank)) {
            return false;
        }
        final MaterTeamRank other = (MaterTeamRank)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return this.getScore() == other.getScore() && this.getRestsScore() == other.getRestsScore();
            }
        }
        else if (this$list.equals(other$list)) {
            return this.getScore() == other.getScore() && this.getRestsScore() == other.getRestsScore();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MaterTeamRank;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getScore();
        result = result * 59 + this.getRestsScore();
        return result;
    }
    
    @Override
    public String toString() {
        return "MaterTeamRank(list=" + this.getList() + ", score=" + this.getScore() + ", restsScore=" + this.getRestsScore() + ")";
    }
    
    @ConstructorProperties({ "list", "score", "restsScore" })
    public MaterTeamRank(final List<MaterRank> list, final int score, final int restsScore) {
        this.list = new ArrayList<MaterRank>();
        this.list = list;
        this.score = score;
        this.restsScore = restsScore;
    }
    
    public MaterTeamRank() {
        this.list = new ArrayList<MaterRank>();
    }
}
