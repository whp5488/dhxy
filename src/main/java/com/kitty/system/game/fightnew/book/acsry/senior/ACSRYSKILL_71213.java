package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;

public class ACSRYSKILL_71213 extends AcsrySkillProcessor {
  public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    RoleSkill skill = null;
    Boolean isHit = Boolean.valueOf(false);
    try {
      skill = (RoleSkill)args[0];
      isHit = (Boolean)args[1];
    } catch (Exception e) {
      return;
    } 
    if (skill == null)
      return; 
    if (isHit == null || !isHit.booleanValue())
      return; 
    if (SKILLATTR.SKILLATTR_POISON.getValue() == skill.getType().intValue() && target.getStatusValue(1005) > 0 && 
      effecter.isPlayer()) {
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 71213);
      double percenta = 0.2D;
      double percentb = 0.1D;
      Role role = playerFightObject.getRole();
      percenta += (role.getGg().intValue() / 100) * 0.02D;
      percentb += (role.getGg().intValue() / 100) * 0.01D;
      String wl_xs = String.valueOf(PRO_DICT_TYPE.WL_XS.getDist());
      double wl_xs_value = target.getPropertyMap().getDouble(wl_xs, 0.0D);
      target.getPropertyMap().setv(wl_xs, Double.valueOf(wl_xs_value - percenta));
      String omit_k_f = String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist());
      double omit_k_f_value = target.getPropertyMap().getDouble(omit_k_f, 0.0D);
      target.getPropertyMap().setv(omit_k_f, Double.valueOf(omit_k_f_value - percentb));
      String omit_k_l = String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist());
      double omit_k_l_value = target.getPropertyMap().getDouble(omit_k_l, 0.0D);
      target.getPropertyMap().setv(omit_k_l, Double.valueOf(omit_k_l_value - percenta));
      String omit_k_s = String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist());
      double omit_k_s_value = target.getPropertyMap().getDouble(omit_k_s, 0.0D);
      target.getPropertyMap().setv(omit_k_s, Double.valueOf(omit_k_s_value - percenta));
      String omit_k_h = String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist());
      double omit_k_h_value = target.getPropertyMap().getDouble(omit_k_h, 0.0D);
      target.getPropertyMap().setv(omit_k_h, Double.valueOf(omit_k_h_value - percenta));
      String omit_k_gh = String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist());
      double omit_k_gh_value = target.getPropertyMap().getDouble(omit_k_gh, 0.0D);
      target.getPropertyMap().setv(omit_k_gh, Double.valueOf(omit_k_gh_value - percenta));
      String omit_k_ss = String.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist());
      double omit_k_ss_value = target.getPropertyMap().getDouble(omit_k_ss, 0.0D);
      target.getPropertyMap().setv(omit_k_ss, Double.valueOf(omit_k_ss_value - percenta));
      target.getStatusMap().setv("1005perpetrator", "71213_" + effecter.getId());
      skillRecord.setT(target.getId().intValue());
    } 
  }
  
  public void statusOver(Fight fight, FightObject fightObject, Object... args) {
    Integer status = null;
    try {
      status = (Integer)args[0];
    } catch (Exception e) {
      e.printStackTrace();
    } 
    if (status == null || 1005 != status.intValue())
      return; 
    String perpetrator = fightObject.getStatusMap().getString("1005perpetrator");
    if (perpetrator != null && perpetrator.contains(String.valueOf(71213))) {
      String[] split = perpetrator.split("_");
      int serial = 0;
      try {
        serial = Integer.parseInt(split[1]);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      } 
      FightObject effecter = fight.queryFightObject(Integer.valueOf(serial));
      if (effecter == null || !effecter.isPlayer())
        return; 
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 71213);
      PlayerFightObject playerFightObject = (PlayerFightObject)effecter;
      Role role = playerFightObject.getRole();
      double percenta = 0.2D;
      double percentb = 0.1D;
      percenta += (role.getGg().intValue() / 100) * 0.02D;
      percentb += (role.getGg().intValue() / 100) * 0.01D;
      String wl_xs = String.valueOf(PRO_DICT_TYPE.WL_XS.getDist());
      double wl_xs_value = fightObject.getPropertyMap().getDouble(wl_xs, 0.0D);
      fightObject.getPropertyMap().setv(wl_xs, Double.valueOf(wl_xs_value + percenta));
      String omit_k_f = String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist());
      double omit_k_f_value = fightObject.getPropertyMap().getDouble(omit_k_f, 0.0D);
      fightObject.getPropertyMap().setv(omit_k_f, Double.valueOf(omit_k_f_value + percentb));
      String omit_k_l = String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist());
      double omit_k_l_value = fightObject.getPropertyMap().getDouble(omit_k_l, 0.0D);
      fightObject.getPropertyMap().setv(omit_k_l, Double.valueOf(omit_k_l_value + percenta));
      String omit_k_s = String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist());
      double omit_k_s_value = fightObject.getPropertyMap().getDouble(omit_k_s, 0.0D);
      fightObject.getPropertyMap().setv(omit_k_s, Double.valueOf(omit_k_s_value + percenta));
      String omit_k_h = String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist());
      double omit_k_h_value = fightObject.getPropertyMap().getDouble(omit_k_h, 0.0D);
      fightObject.getPropertyMap().setv(omit_k_h, Double.valueOf(omit_k_h_value + percenta));
      String omit_k_gh = String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist());
      double omit_k_gh_value = fightObject.getPropertyMap().getDouble(omit_k_gh, 0.0D);
      fightObject.getPropertyMap().setv(omit_k_gh, Double.valueOf(omit_k_gh_value + percenta));
      String omit_k_ss = String.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist());
      double omit_k_ss_value = fightObject.getPropertyMap().getDouble(omit_k_ss, 0.0D);
      fightObject.getPropertyMap().setv(omit_k_ss, Double.valueOf(omit_k_ss_value + percenta));
      skillRecord.setT(fightObject.getId().intValue());
    } 
  }
}
