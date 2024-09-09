package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45240)
public class RespConfirm extends Message
{
    private String title;
    private String tips;
    private int downCount;
    private int onlyConfirm;
    private String confirmType;
    private String confirmText;
    private String cancelText;
    private int show_dlg_mode;
    private String countDownTips;
    private String para_str;
    private int no_close_btn;
    
    public RespConfirm() {
        this.downCount = 0;
        this.onlyConfirm = 0;
        this.confirmText = "确认";
        this.cancelText = "取消";
        this.para_str = "{}";
        this.no_close_btn = 0;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getTips() {
        return this.tips;
    }
    
    public int getDownCount() {
        return this.downCount;
    }
    
    public int getOnlyConfirm() {
        return this.onlyConfirm;
    }
    
    public String getConfirmType() {
        return this.confirmType;
    }
    
    public String getConfirmText() {
        return this.confirmText;
    }
    
    public String getCancelText() {
        return this.cancelText;
    }
    
    public int getShow_dlg_mode() {
        return this.show_dlg_mode;
    }
    
    public String getCountDownTips() {
        return this.countDownTips;
    }
    
    public String getPara_str() {
        return this.para_str;
    }
    
    public int getNo_close_btn() {
        return this.no_close_btn;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setTips(final String tips) {
        this.tips = tips;
    }
    
    public void setDownCount(final int downCount) {
        this.downCount = downCount;
    }
    
    public void setOnlyConfirm(final int onlyConfirm) {
        this.onlyConfirm = onlyConfirm;
    }
    
    public void setConfirmType(final String confirmType) {
        this.confirmType = confirmType;
    }
    
    public void setConfirmText(final String confirmText) {
        this.confirmText = confirmText;
    }
    
    public void setCancelText(final String cancelText) {
        this.cancelText = cancelText;
    }
    
    public void setShow_dlg_mode(final int show_dlg_mode) {
        this.show_dlg_mode = show_dlg_mode;
    }
    
    public void setCountDownTips(final String countDownTips) {
        this.countDownTips = countDownTips;
    }
    
    public void setPara_str(final String para_str) {
        this.para_str = para_str;
    }
    
    public void setNo_close_btn(final int no_close_btn) {
        this.no_close_btn = no_close_btn;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespConfirm)) {
            return false;
        }
        RespConfirm other = (RespConfirm)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$title = this.getTitle();
        Object other$title = other.getTitle();
        if (!((this$title != null) ? this$title.equals(other$title) : (other$title == null))) {
            return false;
        }
        Object this$tips = this.getTips();
        Object other$tips = other.getTips();
        if (!((this$tips != null) ? this$tips.equals(other$tips) : (other$tips == null))) {
            return false;
        }
        if (this.getDownCount() != other.getDownCount()) {
            return false;
        }
        if (this.getOnlyConfirm() != other.getOnlyConfirm()) {
            return false;
        }
        Object this$confirmType = this.getConfirmType();
        Object other$confirmType = other.getConfirmType();
        if (!((this$confirmType != null) ? this$confirmType.equals(other$confirmType) : (other$confirmType == null))) {
            return false;
        }
        Object this$confirmText = this.getConfirmText();
        Object other$confirmText = other.getConfirmText();
        if (!((this$confirmText != null) ? this$confirmText.equals(other$confirmText) : (other$confirmText == null))) {
            return false;
        }
        Object this$cancelText = this.getCancelText();
        Object other$cancelText = other.getCancelText();
        if (!((this$cancelText != null) ? this$cancelText.equals(other$cancelText) : (other$cancelText == null))) {
            return false;
        }
        if (this.getShow_dlg_mode() != other.getShow_dlg_mode()) {
            return false;
        }
        Object this$countDownTips = this.getCountDownTips();
        Object other$countDownTips = other.getCountDownTips();
        if (!((this$countDownTips != null) ? this$countDownTips.equals(other$countDownTips) : (other$countDownTips == null))) {
            return false;
        }
        Object this$para_str = this.getPara_str();
        Object other$para_str = other.getPara_str();
        return ((this$para_str != null) ? this$para_str.equals(other$para_str) : (other$para_str == null)) && this.getNo_close_btn() == other.getNo_close_btn();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespConfirm;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $title = this.getTitle();
        result = result * 59 + (($title == null) ? 43 : $title.hashCode());
        Object $tips = this.getTips();
        result = result * 59 + (($tips == null) ? 43 : $tips.hashCode());
        result = result * 59 + this.getDownCount();
        result = result * 59 + this.getOnlyConfirm();
        Object $confirmType = this.getConfirmType();
        result = result * 59 + (($confirmType == null) ? 43 : $confirmType.hashCode());
        Object $confirmText = this.getConfirmText();
        result = result * 59 + (($confirmText == null) ? 43 : $confirmText.hashCode());
        Object $cancelText = this.getCancelText();
        result = result * 59 + (($cancelText == null) ? 43 : $cancelText.hashCode());
        result = result * 59 + this.getShow_dlg_mode();
        Object $countDownTips = this.getCountDownTips();
        result = result * 59 + (($countDownTips == null) ? 43 : $countDownTips.hashCode());
        Object $para_str = this.getPara_str();
        result = result * 59 + (($para_str == null) ? 43 : $para_str.hashCode());
        result = result * 59 + this.getNo_close_btn();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespConfirm(title=" + this.getTitle() + ", tips=" + this.getTips() + ", downCount=" + this.getDownCount() + ", onlyConfirm=" + this.getOnlyConfirm() + ", confirmType=" + this.getConfirmType() + ", confirmText=" + this.getConfirmText() + ", cancelText=" + this.getCancelText() + ", show_dlg_mode=" + this.getShow_dlg_mode() + ", countDownTips=" + this.getCountDownTips() + ", para_str=" + this.getPara_str() + ", no_close_btn=" + this.getNo_close_btn() + ")";
    }
}
