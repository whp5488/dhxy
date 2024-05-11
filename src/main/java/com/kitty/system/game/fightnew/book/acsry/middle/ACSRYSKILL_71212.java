
package com.kitty.system.game.fightnew.book.acsry.middle;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71212 extends AcsrySkillProcessor
{
    @Override
    public void attackOver(final Fight fight, final FightObject effecter, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        RoleSkill skill = null;
        Boolean isHit = false;
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
        }
        catch (final Exception e) {
            return;
        }
        if (skill == null) {
            return;
        }
        if (isHit == null || !isHit) {
            return;
        }
        if (SKILLATTR.SKILLATTR_POISON.getValue() == skill.getType() && target.getStatusValue(1005) > 0 && effecter.isPlayer()) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 71212);
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            if (playerFightObject.getAcsrySkillList().contains(String.valueOf(71213))) {
                return;
            }
            final double percenta = 0.2;
            final double percentb = 0.1;
            final String wl_xs = String.valueOf(PRO_DICT_TYPE.WL_XS.getDist());
            final double wl_xs_value = target.getPropertyMap().getDouble(wl_xs, 0.0);
            target.getPropertyMap().setv(wl_xs, (Object)(wl_xs_value - percenta));
            final String omit_k_f = String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist());
            final double omit_k_f_value = target.getPropertyMap().getDouble(omit_k_f, 0.0);
            target.getPropertyMap().setv(omit_k_f, (Object)(omit_k_f_value - percentb));
            final String omit_k_l = String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist());
            final double omit_k_l_value = target.getPropertyMap().getDouble(omit_k_l, 0.0);
            target.getPropertyMap().setv(omit_k_l, (Object)(omit_k_l_value - percenta));
            final String omit_k_s = String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist());
            final double omit_k_s_value = target.getPropertyMap().getDouble(omit_k_s, 0.0);
            target.getPropertyMap().setv(omit_k_s, (Object)(omit_k_s_value - percenta));
            final String omit_k_h = String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist());
            final double omit_k_h_value = target.getPropertyMap().getDouble(omit_k_h, 0.0);
            target.getPropertyMap().setv(omit_k_h, (Object)(omit_k_h_value - percenta));
            final String omit_k_gh = String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist());
            final double omit_k_gh_value = target.getPropertyMap().getDouble(omit_k_gh, 0.0);
            target.getPropertyMap().setv(omit_k_gh, (Object)(omit_k_gh_value - percenta));
            final String omit_k_ss = String.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist());
            final double omit_k_ss_value = target.getPropertyMap().getDouble(omit_k_ss, 0.0);
            target.getPropertyMap().setv(omit_k_ss, (Object)(omit_k_ss_value - percenta));
            target.getStatusMap().setv("1005perpetrator", (Object)("71212_" + effecter.getId()));
            skillRecord.setT(target.getId());
        }
    }
    
    @Override
    public void statusOver(final Fight fight, final FightObject fightObject, final Object... args) {
        Integer status = null;
        try {
            status = (Integer)args[0];
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if (status == null || 1005 != status) {
            return;
        }
        final String perpetrator = fightObject.getStatusMap().getString("1005perpetrator");
        if (perpetrator != null && perpetrator.contains(String.valueOf(71212))) {
            final double percenta = 0.2;
            final double percentb = 0.1;
            final String wl_xs = String.valueOf(PRO_DICT_TYPE.WL_XS.getDist());
            final double wl_xs_value = fightObject.getPropertyMap().getDouble(wl_xs, 0.0);
            fightObject.getPropertyMap().setv(wl_xs, (Object)(wl_xs_value + percenta));
            final String omit_k_f = String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist());
            final double omit_k_f_value = fightObject.getPropertyMap().getDouble(omit_k_f, 0.0);
            fightObject.getPropertyMap().setv(omit_k_f, (Object)(omit_k_f_value + percentb));
            final String omit_k_l = String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist());
            final double omit_k_l_value = fightObject.getPropertyMap().getDouble(omit_k_l, 0.0);
            fightObject.getPropertyMap().setv(omit_k_l, (Object)(omit_k_l_value + percenta));
            final String omit_k_s = String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist());
            final double omit_k_s_value = fightObject.getPropertyMap().getDouble(omit_k_s, 0.0);
            fightObject.getPropertyMap().setv(omit_k_s, (Object)(omit_k_s_value + percenta));
            final String omit_k_h = String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist());
            final double omit_k_h_value = fightObject.getPropertyMap().getDouble(omit_k_h, 0.0);
            fightObject.getPropertyMap().setv(omit_k_h, (Object)(omit_k_h_value + percenta));
            final String omit_k_gh = String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist());
            final double omit_k_gh_value = fightObject.getPropertyMap().getDouble(omit_k_gh, 0.0);
            fightObject.getPropertyMap().setv(omit_k_gh, (Object)(omit_k_gh_value + percenta));
            final String omit_k_ss = String.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist());
            final double omit_k_ss_value = fightObject.getPropertyMap().getDouble(omit_k_ss, 0.0);
            fightObject.getPropertyMap().setv(omit_k_ss, (Object)(omit_k_ss_value + percenta));
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 71212);
            skillRecord.setT(fightObject.getId());
        }
    }
}
