package com.kitty.system.game.fightnew.book.pet.special;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_84006 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 84006);
    double percenta = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((5.0D + 0.13D * (effector.getRelive() * 4 + 1) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    double percentb = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((15.0D + 0.39D * (effector.getRelive() * 4 + 1) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    int time = 2;
    FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1018, PRO_DICT_TYPE.K_FY.getDist(), time, percenta);
    ImbueUtil.addOrUpdStatus(host.getImbueMap(), 1018, PRO_DICT_TYPE.K_FY.getDist(), time, percenta);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1018, PRO_DICT_TYPE.K_HS.getDist(), time, percenta);
    ImbueUtil.addOrUpdStatus(host.getImbueMap(), 1018, PRO_DICT_TYPE.K_HS.getDist(), time, percenta);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1018, PRO_DICT_TYPE.K_HL.getDist(), time, percenta);
    ImbueUtil.addOrUpdStatus(host.getImbueMap(), 1018, PRO_DICT_TYPE.K_HL.getDist(), time, percenta);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1018, PRO_DICT_TYPE.K_YW.getDist(), time, percenta);
    ImbueUtil.addOrUpdStatus(host.getImbueMap(), 1018, PRO_DICT_TYPE.K_YW.getDist(), time, percenta);
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1018, PRO_DICT_TYPE.K_F.getDist(), time, percentb);
    ImbueUtil.addOrUpdStatus(host.getImbueMap(), 1018, PRO_DICT_TYPE.K_F.getDist(), time, percentb);
    String k_f = String.valueOf(PRO_DICT_TYPE.K_F.getDist()) + 84006;
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1018, PRO_DICT_TYPE.K_L.getDist(), time, percentb);
    ImbueUtil.addOrUpdStatus(host.getImbueMap(), 1018, PRO_DICT_TYPE.K_L.getDist(), time, percentb);
    String k_l = String.valueOf(PRO_DICT_TYPE.K_L.getDist()) + 84006;
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1018, PRO_DICT_TYPE.K_S.getDist(), time, percentb);
    ImbueUtil.addOrUpdStatus(host.getImbueMap(), 1018, PRO_DICT_TYPE.K_S.getDist(), time, percentb);
    String k_s = String.valueOf(PRO_DICT_TYPE.K_S.getDist()) + 84006;
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1018, PRO_DICT_TYPE.K_H.getDist(), time, percentb);
    ImbueUtil.addOrUpdStatus(host.getImbueMap(), 1018, PRO_DICT_TYPE.K_H.getDist(), time, percentb);
    String k_h = String.valueOf(PRO_DICT_TYPE.K_H.getDist()) + 84006;
    ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1018, PRO_DICT_TYPE.WL_XS.getDist(), time, percentb);
    ImbueUtil.addOrUpdStatus(host.getImbueMap(), 1018, PRO_DICT_TYPE.WL_XS.getDist(), time, percentb);
    String wl_xs = String.valueOf(PRO_DICT_TYPE.WL_XS.getDist()) + 84006;
    effector.getStatusMap().setv(String.valueOf(1018), Integer.valueOf(time));
    host.getStatusMap().setv(String.valueOf(1018), Integer.valueOf(time));
    ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 84006, 0);
    FightObjectParam targetParam1 = ActionParamUtils.addStatusForTarget(effector, 2, 1018, time, 1);
    FightObjectParam targetParam2 = ActionParamUtils.addStatusForTarget(host, 2, 1018, time, 2);
    actionDetailParam.getTargetParamList().add(targetParam1);
    actionDetailParam.getTargetParamList().add(targetParam2);
    fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    skillRecord.setT(host.getId().intValue());
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
