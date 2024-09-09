package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81031 extends SkillProcessor
{
    public SKILL_81031() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81031);
        if (effector.getComeRound() > 0) {
            SysPetSkill petSkill = (SysPetSkill)args[1];
            if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, null, false, args)) {
                return null;
            }
            int round = 3;
            effector.getStatusMap().setv(String.valueOf(1011), Integer.valueOf(round));
            effector.getOtherMap().setv("cloakingSkill", Integer.valueOf(81031));
            this.changeProperty(effector, true);
            ActionParamUtils.addStatus(fight, effector, effector, 7, 81031, 2, 1011, round);
            skillRecord.setT((long)(int)effector.getId());
        }
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 0;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
    
    @Override
    public void statusClear(Fight fight, FightObject fightObject, int status, Object... args) {
        if (status == 1011) {
            int cloakingSkill = fightObject.getOtherMap().getInt("cloakingSkill");
            if (81031 == cloakingSkill) {
                fightObject.getOtherMap().unset("cloakingSkill");
                this.changeProperty(fightObject, false);
            }
        }
    }
    
    private void changeProperty(FightObject effector, boolean isAdd) {
        double pval = AcsryPetSkillUtil.getPval(81031, effector);
        if (!isAdd) {
            pval = -pval;
        }
        if (pval > 0.0) {
            String kf = String.valueOf(PRO_DICT_TYPE.K_F.getDist());
            double kfValue = effector.getPropertyMap().getDouble(kf, 0.0);
            effector.getPropertyMap().setv(kf, Double.valueOf(kfValue + pval));
            String kl = String.valueOf(PRO_DICT_TYPE.K_L.getDist());
            double klValue = effector.getPropertyMap().getDouble(kl, 0.0);
            effector.getPropertyMap().setv(kl, Double.valueOf(klValue + pval));
            String ks = String.valueOf(PRO_DICT_TYPE.K_S.getDist());
            double ksValue = effector.getPropertyMap().getDouble(ks, 0.0);
            effector.getPropertyMap().setv(ks, Double.valueOf(ksValue + pval));
            String kh = String.valueOf(PRO_DICT_TYPE.K_H.getDist());
            double khValue = effector.getPropertyMap().getDouble(kh, 0.0);
            effector.getPropertyMap().setv(kh, Double.valueOf(khValue + pval));
            String gh = String.valueOf(PRO_DICT_TYPE.K_GH.getDist());
            double ghValue = effector.getPropertyMap().getDouble(kh, 0.0);
            effector.getPropertyMap().setv(gh, Double.valueOf(ghValue + pval));
            String wl = String.valueOf(PRO_DICT_TYPE.WL_XS.getDist());
            double wlValue = effector.getPropertyMap().getDouble(wl, 0.0);
            effector.getPropertyMap().setv(wl, Double.valueOf(wlValue + pval));
        }
    }
}
