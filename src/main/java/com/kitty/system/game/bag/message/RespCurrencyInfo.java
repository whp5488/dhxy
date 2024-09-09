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
    
    public RespCurrencyInfo(Role role) {
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
        RespCurrencyInfo other = (RespCurrencyInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$gold = this.getGold();
        Object other$gold = other.getGold();
        if (!((this$gold != null) ? this$gold.equals(other$gold) : (other$gold == null))) {
            return false;
        }
        Object this$silver = this.getSilver();
        Object other$silver = other.getSilver();
        if (!((this$silver != null) ? this$silver.equals(other$silver) : (other$silver == null))) {
            return false;
        }
        Object this$coin = this.getCoin();
        Object other$coin = other.getCoin();
        if (!((this$coin != null) ? this$coin.equals(other$coin) : (other$coin == null))) {
            return false;
        }
        Object this$guoScore = this.getGuoScore();
        Object other$guoScore = other.getGuoScore();
        if (!((this$guoScore != null) ? this$guoScore.equals(other$guoScore) : (other$guoScore == null))) {
            return false;
        }
        Object this$bracScore = this.getBracScore();
        Object other$bracScore = other.getBracScore();
        if (!((this$bracScore != null) ? this$bracScore.equals(other$bracScore) : (other$bracScore == null))) {
            return false;
        }
        Object this$naoScore = this.getNaoScore();
        Object other$naoScore = other.getNaoScore();
        if (!((this$naoScore != null) ? this$naoScore.equals(other$naoScore) : (other$naoScore == null))) {
            return false;
        }
        Object this$diScore = this.getDiScore();
        Object other$diScore = other.getDiScore();
        if (!((this$diScore != null) ? this$diScore.equals(other$diScore) : (other$diScore == null))) {
            return false;
        }
        Object this$arenaScore = this.getArenaScore();
        Object other$arenaScore = other.getArenaScore();
        if (!((this$arenaScore != null) ? this$arenaScore.equals(other$arenaScore) : (other$arenaScore == null))) {
            return false;
        }
        Object this$longScore = this.getLongScore();
        Object other$longScore = other.getLongScore();
        if (!((this$longScore != null) ? this$longScore.equals(other$longScore) : (other$longScore == null))) {
            return false;
        }
        Object this$niceScore = this.getNiceScore();
        Object other$niceScore = other.getNiceScore();
        if (!((this$niceScore != null) ? this$niceScore.equals(other$niceScore) : (other$niceScore == null))) {
            return false;
        }
        Object this$helpScore = this.getHelpScore();
        Object other$helpScore = other.getHelpScore();
        if (!((this$helpScore != null) ? this$helpScore.equals(other$helpScore) : (other$helpScore == null))) {
            return false;
        }
        Object this$featsScore = this.getFeatsScore();
        Object other$featsScore = other.getFeatsScore();
        if (!((this$featsScore != null) ? this$featsScore.equals(other$featsScore) : (other$featsScore == null))) {
            return false;
        }
        Object this$chargeScore = this.getChargeScore();
        Object other$chargeScore = other.getChargeScore();
        if (!((this$chargeScore != null) ? this$chargeScore.equals(other$chargeScore) : (other$chargeScore == null))) {
            return false;
        }
        Object this$boundGold = this.getBoundGold();
        Object other$boundGold = other.getBoundGold();
        return (this$boundGold != null) ? this$boundGold.equals(other$boundGold) : (other$boundGold == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespCurrencyInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $gold = this.getGold();
        result = result * 59 + (($gold == null) ? 43 : $gold.hashCode());
        Object $silver = this.getSilver();
        result = result * 59 + (($silver == null) ? 43 : $silver.hashCode());
        Object $coin = this.getCoin();
        result = result * 59 + (($coin == null) ? 43 : $coin.hashCode());
        Object $guoScore = this.getGuoScore();
        result = result * 59 + (($guoScore == null) ? 43 : $guoScore.hashCode());
        Object $bracScore = this.getBracScore();
        result = result * 59 + (($bracScore == null) ? 43 : $bracScore.hashCode());
        Object $naoScore = this.getNaoScore();
        result = result * 59 + (($naoScore == null) ? 43 : $naoScore.hashCode());
        Object $diScore = this.getDiScore();
        result = result * 59 + (($diScore == null) ? 43 : $diScore.hashCode());
        Object $arenaScore = this.getArenaScore();
        result = result * 59 + (($arenaScore == null) ? 43 : $arenaScore.hashCode());
        Object $longScore = this.getLongScore();
        result = result * 59 + (($longScore == null) ? 43 : $longScore.hashCode());
        Object $niceScore = this.getNiceScore();
        result = result * 59 + (($niceScore == null) ? 43 : $niceScore.hashCode());
        Object $helpScore = this.getHelpScore();
        result = result * 59 + (($helpScore == null) ? 43 : $helpScore.hashCode());
        Object $featsScore = this.getFeatsScore();
        result = result * 59 + (($featsScore == null) ? 43 : $featsScore.hashCode());
        Object $chargeScore = this.getChargeScore();
        result = result * 59 + (($chargeScore == null) ? 43 : $chargeScore.hashCode());
        Object $boundGold = this.getBoundGold();
        result = result * 59 + (($boundGold == null) ? 43 : $boundGold.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespCurrencyInfo(gold=" + this.getGold() + ", silver=" + this.getSilver() + ", coin=" + this.getCoin() + ", guoScore=" + this.getGuoScore() + ", bracScore=" + this.getBracScore() + ", naoScore=" + this.getNaoScore() + ", diScore=" + this.getDiScore() + ", arenaScore=" + this.getArenaScore() + ", longScore=" + this.getLongScore() + ", niceScore=" + this.getNiceScore() + ", helpScore=" + this.getHelpScore() + ", featsScore=" + this.getFeatsScore() + ", chargeScore=" + this.getChargeScore() + ", boundGold=" + this.getBoundGold() + ")";
    }
}
