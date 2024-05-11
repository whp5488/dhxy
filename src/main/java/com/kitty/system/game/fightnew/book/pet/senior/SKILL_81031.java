
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
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81031);
        if (effector.getComeRound() > 0) {
            final SysPetSkill petSkill = (SysPetSkill)args[1];
            if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, null, false, args)) {
                return null;
            }
            final int round = 3;
            effector.getStatusMap().setv(String.valueOf(1011), (Object)round);
            effector.getOtherMap().setv("cloakingSkill", (Object)81031);
            this.changeProperty(effector, true);
            ActionParamUtils.addStatus(fight, effector, effector, 7, 81031, 2, 1011, round);
            skillRecord.setT(effector.getId());
        }
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 0;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
    
    @Override
    public void statusClear(final Fight fight, final FightObject fightObject, final int status, final Object... args) {
        if (status == 1011) {
            final int cloakingSkill = fightObject.getOtherMap().getInt("cloakingSkill");
            if (81031 == cloakingSkill) {
                fightObject.getOtherMap().unset("cloakingSkill");
                this.changeProperty(fightObject, false);
            }
        }
    }
    
    private void changeProperty(final FightObject effector, final boolean isAdd) {
        double pval = AcsryPetSkillUtil.getPval(81031, effector);
        if (!isAdd) {
            pval = -pval;
        }
        if (pval > 0.0) {
            final String kf = String.valueOf(PRO_DICT_TYPE.K_F.getDist());
            final double kfValue = effector.getPropertyMap().getDouble(kf, 0.0);
            effector.getPropertyMap().setv(kf, (Object)(kfValue + pval));
            final String kl = String.valueOf(PRO_DICT_TYPE.K_L.getDist());
            final double klValue = effector.getPropertyMap().getDouble(kl, 0.0);
            effector.getPropertyMap().setv(kl, (Object)(klValue + pval));
            final String ks = String.valueOf(PRO_DICT_TYPE.K_S.getDist());
            final double ksValue = effector.getPropertyMap().getDouble(ks, 0.0);
            effector.getPropertyMap().setv(ks, (Object)(ksValue + pval));
            final String kh = String.valueOf(PRO_DICT_TYPE.K_H.getDist());
            final double khValue = effector.getPropertyMap().getDouble(kh, 0.0);
            effector.getPropertyMap().setv(kh, (Object)(khValue + pval));
            final String gh = String.valueOf(PRO_DICT_TYPE.K_GH.getDist());
            final double ghValue = effector.getPropertyMap().getDouble(kh, 0.0);
            effector.getPropertyMap().setv(gh, (Object)(ghValue + pval));
            final String wl = String.valueOf(PRO_DICT_TYPE.WL_XS.getDist());
            final double wlValue = effector.getPropertyMap().getDouble(wl, 0.0);
            effector.getPropertyMap().setv(wl, (Object)(wlValue + pval));
        }
    }
}
