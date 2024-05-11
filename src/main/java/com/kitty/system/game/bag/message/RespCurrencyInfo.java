
package com.kitty.system.game.bag.message;

import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 54977)
public class RespCurrencyInfo extends Message
{
    private String gold;
    private String silver;
    private String coin;
    private String guoScore;
    private String bracScore;
    private String naoScore;
    private String diScore;
    private String arenaScore;
    private String longScore;
    private String niceScore;
    private String helpScore;
    private String featsScore;
    private String chargeScore;
    private String boundGold;
    
    public RespCurrencyInfo(final Role role) {
        this.gold = String.valueOf(role.getGold());
        this.silver = String.valueOf(role.getSilver());
        this.coin = String.valueOf(role.getCoin());
        this.guoScore = String.valueOf(role.getExtendBox().getGuoScore());
        this.bracScore = String.valueOf(role.getExtendBox().getBracScore());
        this.naoScore = String.valueOf(role.getExtendBox().getNaoScore());
        this.diScore = String.valueOf(role.getExtendBox().getDiScore());
        this.arenaScore = String.valueOf(role.getExtendBox().getArenaScore());
        this.longScore = String.valueOf(role.getExtendBox().getLongScore());
        this.niceScore = String.valueOf(role.getExtendBox().getNiceScore());
        this.helpScore = String.valueOf(role.getExtendBox().getHelpScore());
        this.featsScore = String.valueOf(role.getExtendBox().getFeatsScore());
        this.chargeScore = String.valueOf(role.getExtendBox().getChargeScore());
        this.boundGold = String.valueOf(role.getExtendBox().getBoundGold());
    }
    
    public String getGold() {
        return this.gold;
    }
    
    public String getSilver() {
        return this.silver;
    }
    
    public String getCoin() {
        return this.coin;
    }
    
    public String getGuoScore() {
        return this.guoScore;
    }
    
    public String getBracScore() {
        return this.bracScore;
    }
    
    public String getNaoScore() {
        return this.naoScore;
    }
    
    public String getDiScore() {
        return this.diScore;
    }
    
    public String getArenaScore() {
        return this.arenaScore;
    }
    
    public String getLongScore() {
        return this.longScore;
    }
    
    public String getNiceScore() {
        return this.niceScore;
    }
    
    public String getHelpScore() {
        return this.helpScore;
    }
    
    public String getFeatsScore() {
        return this.featsScore;
    }
    
    public String getChargeScore() {
        return this.chargeScore;
    }
    
    public String getBoundGold() {
        return this.boundGold;
    }
    
    public void setGold(final String gold) {
        this.gold = gold;
    }
    
    public void setSilver(final String silver) {
        this.silver = silver;
    }
    
    public void setCoin(final String coin) {
        this.coin = coin;
    }
    
    public void setGuoScore(final String guoScore) {
        this.guoScore = guoScore;
    }
    
    public void setBracScore(final String bracScore) {
        this.bracScore = bracScore;
    }
    
    public void setNaoScore(final String naoScore) {
        this.naoScore = naoScore;
    }
    
    public void setDiScore(final String diScore) {
        this.diScore = diScore;
    }
    
    public void setArenaScore(final String arenaScore) {
        this.arenaScore = arenaScore;
    }
    
    public void setLongScore(final String longScore) {
        this.longScore = longScore;
    }
    
    public void setNiceScore(final String niceScore) {
        this.niceScore = niceScore;
    }
    
    public void setHelpScore(final String helpScore) {
        this.helpScore = helpScore;
    }
    
    public void setFeatsScore(final String featsScore) {
        this.featsScore = featsScore;
    }
    
    public void setChargeScore(final String chargeScore) {
        this.chargeScore = chargeScore;
    }
    
    public void setBoundGold(final String boundGold) {
        this.boundGold = boundGold;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespCurrencyInfo)) {
            return false;
        }
        final RespCurrencyInfo other = (RespCurrencyInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$gold = this.getGold();
        final Object other$gold = other.getGold();
        Label_0065: {
            if (this$gold == null) {
                if (other$gold == null) {
                    break Label_0065;
                }
            }
            else if (this$gold.equals(other$gold)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$silver = this.getSilver();
        final Object other$silver = other.getSilver();
        Label_0102: {
            if (this$silver == null) {
                if (other$silver == null) {
                    break Label_0102;
                }
            }
            else if (this$silver.equals(other$silver)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$coin = this.getCoin();
        final Object other$coin = other.getCoin();
        Label_0139: {
            if (this$coin == null) {
                if (other$coin == null) {
                    break Label_0139;
                }
            }
            else if (this$coin.equals(other$coin)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$guoScore = this.getGuoScore();
        final Object other$guoScore = other.getGuoScore();
        Label_0176: {
            if (this$guoScore == null) {
                if (other$guoScore == null) {
                    break Label_0176;
                }
            }
            else if (this$guoScore.equals(other$guoScore)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$bracScore = this.getBracScore();
        final Object other$bracScore = other.getBracScore();
        Label_0213: {
            if (this$bracScore == null) {
                if (other$bracScore == null) {
                    break Label_0213;
                }
            }
            else if (this$bracScore.equals(other$bracScore)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$naoScore = this.getNaoScore();
        final Object other$naoScore = other.getNaoScore();
        Label_0250: {
            if (this$naoScore == null) {
                if (other$naoScore == null) {
                    break Label_0250;
                }
            }
            else if (this$naoScore.equals(other$naoScore)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$diScore = this.getDiScore();
        final Object other$diScore = other.getDiScore();
        Label_0287: {
            if (this$diScore == null) {
                if (other$diScore == null) {
                    break Label_0287;
                }
            }
            else if (this$diScore.equals(other$diScore)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$arenaScore = this.getArenaScore();
        final Object other$arenaScore = other.getArenaScore();
        Label_0324: {
            if (this$arenaScore == null) {
                if (other$arenaScore == null) {
                    break Label_0324;
                }
            }
            else if (this$arenaScore.equals(other$arenaScore)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$longScore = this.getLongScore();
        final Object other$longScore = other.getLongScore();
        Label_0361: {
            if (this$longScore == null) {
                if (other$longScore == null) {
                    break Label_0361;
                }
            }
            else if (this$longScore.equals(other$longScore)) {
                break Label_0361;
            }
            return false;
        }
        final Object this$niceScore = this.getNiceScore();
        final Object other$niceScore = other.getNiceScore();
        Label_0398: {
            if (this$niceScore == null) {
                if (other$niceScore == null) {
                    break Label_0398;
                }
            }
            else if (this$niceScore.equals(other$niceScore)) {
                break Label_0398;
            }
            return false;
        }
        final Object this$helpScore = this.getHelpScore();
        final Object other$helpScore = other.getHelpScore();
        Label_0435: {
            if (this$helpScore == null) {
                if (other$helpScore == null) {
                    break Label_0435;
                }
            }
            else if (this$helpScore.equals(other$helpScore)) {
                break Label_0435;
            }
            return false;
        }
        final Object this$featsScore = this.getFeatsScore();
        final Object other$featsScore = other.getFeatsScore();
        Label_0472: {
            if (this$featsScore == null) {
                if (other$featsScore == null) {
                    break Label_0472;
                }
            }
            else if (this$featsScore.equals(other$featsScore)) {
                break Label_0472;
            }
            return false;
        }
        final Object this$chargeScore = this.getChargeScore();
        final Object other$chargeScore = other.getChargeScore();
        Label_0509: {
            if (this$chargeScore == null) {
                if (other$chargeScore == null) {
                    break Label_0509;
                }
            }
            else if (this$chargeScore.equals(other$chargeScore)) {
                break Label_0509;
            }
            return false;
        }
        final Object this$boundGold = this.getBoundGold();
        final Object other$boundGold = other.getBoundGold();
        if (this$boundGold == null) {
            if (other$boundGold == null) {
                return true;
            }
        }
        else if (this$boundGold.equals(other$boundGold)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespCurrencyInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $gold = this.getGold();
        result = result * 59 + (($gold == null) ? 43 : $gold.hashCode());
        final Object $silver = this.getSilver();
        result = result * 59 + (($silver == null) ? 43 : $silver.hashCode());
        final Object $coin = this.getCoin();
        result = result * 59 + (($coin == null) ? 43 : $coin.hashCode());
        final Object $guoScore = this.getGuoScore();
        result = result * 59 + (($guoScore == null) ? 43 : $guoScore.hashCode());
        final Object $bracScore = this.getBracScore();
        result = result * 59 + (($bracScore == null) ? 43 : $bracScore.hashCode());
        final Object $naoScore = this.getNaoScore();
        result = result * 59 + (($naoScore == null) ? 43 : $naoScore.hashCode());
        final Object $diScore = this.getDiScore();
        result = result * 59 + (($diScore == null) ? 43 : $diScore.hashCode());
        final Object $arenaScore = this.getArenaScore();
        result = result * 59 + (($arenaScore == null) ? 43 : $arenaScore.hashCode());
        final Object $longScore = this.getLongScore();
        result = result * 59 + (($longScore == null) ? 43 : $longScore.hashCode());
        final Object $niceScore = this.getNiceScore();
        result = result * 59 + (($niceScore == null) ? 43 : $niceScore.hashCode());
        final Object $helpScore = this.getHelpScore();
        result = result * 59 + (($helpScore == null) ? 43 : $helpScore.hashCode());
        final Object $featsScore = this.getFeatsScore();
        result = result * 59 + (($featsScore == null) ? 43 : $featsScore.hashCode());
        final Object $chargeScore = this.getChargeScore();
        result = result * 59 + (($chargeScore == null) ? 43 : $chargeScore.hashCode());
        final Object $boundGold = this.getBoundGold();
        result = result * 59 + (($boundGold == null) ? 43 : $boundGold.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespCurrencyInfo(gold=" + this.getGold() + ", silver=" + this.getSilver() + ", coin=" + this.getCoin() + ", guoScore=" + this.getGuoScore() + ", bracScore=" + this.getBracScore() + ", naoScore=" + this.getNaoScore() + ", diScore=" + this.getDiScore() + ", arenaScore=" + this.getArenaScore() + ", longScore=" + this.getLongScore() + ", niceScore=" + this.getNiceScore() + ", helpScore=" + this.getHelpScore() + ", featsScore=" + this.getFeatsScore() + ", chargeScore=" + this.getChargeScore() + ", boundGold=" + this.getBoundGold() + ")";
    }
}
