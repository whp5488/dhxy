
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_73343 extends AcsrySkillProcessor
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
        if (SKILLATTR.SKILLATTR_ZHEN.getValue() == skill.getType() && effecter.isPlayer()) {
            double percent = -0.15;
            final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
            percent -= playerFightObject.getRole().getMj() / 100 * 0.01;
            this.excuteProperty(target, percent);
            target.getStatusMap().setv(String.valueOf(8007), (Object)2);
            target.getStatusMap().setv("8007perpetrator", (Object)("73343_" + effecter.getId()));
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 73343);
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
        if (status == null || 8007 != status) {
            return;
        }
        final String perpetrator = fightObject.getStatusMap().getString("8007perpetrator");
        if (perpetrator != null && perpetrator.contains(String.valueOf(73343))) {
            double percent = 0.15;
            final String[] splits = perpetrator.split("_");
            try {
                final FightObject effecter = fight.queryFightObject(Integer.parseInt(splits[1]));
                final PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
                percent += playerFightObject.getRole().getMj() / 100 * 0.01;
                this.excuteProperty(fightObject, percent);
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 73343);
                skillRecord.setT(fightObject.getId());
            }
            catch (final NumberFormatException e2) {
                e2.printStackTrace();
            }
        }
    }
    
    private void excuteProperty(final FightObject target, final double percent) {
        final String kf = String.valueOf(PRO_DICT_TYPE.K_F.getDist());
        final double kfValue = target.getPropertyMap().getDouble(kf, 0.0);
        target.getPropertyMap().setv(kf, (Object)(kfValue + percent));
        final String kl = String.valueOf(PRO_DICT_TYPE.K_L.getDist());
        final double klValue = target.getPropertyMap().getDouble(kf, 0.0);
        target.getPropertyMap().setv(kf, (Object)(klValue + percent));
        final String ks = String.valueOf(PRO_DICT_TYPE.K_S.getDist());
        final double ksValue = target.getPropertyMap().getDouble(kf, 0.0);
        target.getPropertyMap().setv(ks, (Object)(ksValue + percent));
        final String kh = String.valueOf(PRO_DICT_TYPE.K_H.getDist());
        final double khValue = target.getPropertyMap().getDouble(kh, 0.0);
        target.getPropertyMap().setv(kh, (Object)(khValue + percent));
        final String kgh = String.valueOf(PRO_DICT_TYPE.K_GH.getDist());
        final double kghValue = target.getPropertyMap().getDouble(kgh, 0.0);
        target.getPropertyMap().setv(kgh, (Object)(kghValue + percent));
        final String kss = String.valueOf(PRO_DICT_TYPE.K_SS.getDist());
        final double kssValue = target.getPropertyMap().getDouble(kss, 0.0);
        target.getPropertyMap().setv(kss, (Object)(kssValue + percent));
        final String kdu = String.valueOf(PRO_DICT_TYPE.K_DU.getDist());
        final double kduValue = target.getPropertyMap().getDouble(kdu, 0.0);
        target.getPropertyMap().setv(kdu, (Object)(kduValue - percent));
        final String wlxs = String.valueOf(PRO_DICT_TYPE.WL_XS.getDist());
        final double wlxsValue = target.getPropertyMap().getDouble(wlxs, 0.0);
        target.getPropertyMap().setv(wlxs, (Object)(wlxsValue + percent));
    }
}
