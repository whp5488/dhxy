package com.kitty.system.game.fightnew.book.horse.two.normal;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_62011 extends HorseSkillProcessor
{
    public HORSESKILL_62011() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        double percentb = (7.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
        percentb = HorseSkillUtils.getMathRoundPercent(percentb);
        if (percentb > 0.0) {
            NutMap map = NutMap.NEW();
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Double.valueOf(percentb));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Double.valueOf(percentb));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Double.valueOf(percentb));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Double.valueOf(percentb));
            map.setv(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Double.valueOf(percentb));
            return map;
        }
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
        Integer hurt = (Integer)args[0];
        if (hurt == null) {
            return null;
        }
        double wx_m = target.getSelfAllTempPro(PRO_DICT_TYPE.WX_M.getDist());
        if (wx_m >= 50.0) {
            HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(62011));
            if (horseSkillParam == null) {
                return null;
            }
            RoleSkill skill = (RoleSkill)args[2];
            if (PETSKILLATTR.PETSKILLATTR_FIRE.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_WIND.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_THUNDER.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_WATER.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_TM.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_FG.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_QM.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == (int)skill.getType() || PETSKILLATTR.PETSKILLATTR_AIHAO.getValue() == (int)skill.getType()) {
                double percenta = (40.0 * HorseSkillUtils.getPercent(horseSkillParam) + 0.1) / 100.0;
                percenta = HorseSkillUtils.getMathRoundPercent(percenta);
                if (percenta != 0.0) {
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 62011);
                    skillRecord.setT((long)(int)target.getId());
                    NutMap map = NutMap.NEW();
                    int alterHurt = (int)((double)(int)hurt * percenta);
                    map.setv("alterHurt", Integer.valueOf(alterHurt));
                    return map;
                }
            }
        }
        return null;
    }
}
