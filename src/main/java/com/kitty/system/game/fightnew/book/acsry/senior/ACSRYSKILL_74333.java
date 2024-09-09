package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_74333 extends AcsrySkillProcessor
{
    public ACSRYSKILL_74333() {
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        RoleSkill skill = null;
        Boolean isHit = Boolean.valueOf(false);
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
        }
        catch (Exception e) {
            return;
        }
        if (skill == null) {
            return;
        }
        if (isHit == null || !(boolean)isHit) {
            return;
        }
        if (SKILLATTR.SKILLATTR_YIWANG.getValue() == (int)skill.getType()) {
            double percent = -0.15;
            this.excuteProperty(target, percent);
            target.getStatusMap().setv("8009perpetrator", "74333_" + effecter.getId());
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 74333);
            skillRecord.setT((long)(int)target.getId());
        }
        return;
    }
    
    @Override
    public void statusOver(Fight fight, FightObject fightObject, Object... args) {
        Integer status = (Integer)args[0];
        if (status == null || 1006 != (int)status) {
            return;
        }
        String str = fightObject.getStatusMap().getString("8009perpetrator", null);
        if (str == null) {
            return;
        }
        double percent = 0.15;
        this.excuteProperty(fightObject, percent);
        fightObject.getStatusMap().unset("8009perpetrator");
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 74333);
        skillRecord.setT((long)(int)fightObject.getId());
        return;
    }
    
    private void excuteProperty(FightObject target, double percent) {
        String kf = String.valueOf(PRO_DICT_TYPE.K_F.getDist());
        double kfValue = target.getPropertyMap().getDouble(kf, 0.0);
        target.getPropertyMap().setv(kf, Double.valueOf(kfValue + percent));
        String kl = String.valueOf(PRO_DICT_TYPE.K_L.getDist());
        double klValue = target.getPropertyMap().getDouble(kl, 0.0);
        target.getPropertyMap().setv(kl, Double.valueOf(klValue + percent));
        String ks = String.valueOf(PRO_DICT_TYPE.K_S.getDist());
        double ksValue = target.getPropertyMap().getDouble(ks, 0.0);
        target.getPropertyMap().setv(ks, Double.valueOf(ksValue + percent));
        String kh = String.valueOf(PRO_DICT_TYPE.K_H.getDist());
        double khValue = target.getPropertyMap().getDouble(kh, 0.0);
        target.getPropertyMap().setv(kh, Double.valueOf(khValue + percent));
        String kgh = String.valueOf(PRO_DICT_TYPE.K_GH.getDist());
        double kghValue = target.getPropertyMap().getDouble(kgh, 0.0);
        target.getPropertyMap().setv(kgh, Double.valueOf(kghValue + percent));
        String kdu = String.valueOf(PRO_DICT_TYPE.K_DU.getDist());
        double kduValue = target.getPropertyMap().getDouble(kdu, 0.0);
        target.getPropertyMap().setv(kdu, Double.valueOf(kduValue + percent));
    }
}
