
package com.kitty.system.game.fightnew.book.vo;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.Map;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import java.util.Iterator;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import com.kitty.system.game.fightnew.FightObject;

public class StatusForSkillDispose
{
    public void disposePositiveStatus(final FightObject target, final int status, final boolean isClear) {
        try {
            final Class<?> classType = this.getClass();
            final Object obj = classType.getConstructor(new Class[0]).newInstance();
            if (isClear) {
                final Method clearMethod = classType.getDeclaredMethod("clear" + status, FightObject.class);
                clearMethod.invoke(obj, target);
            }
        }
        catch (final InstantiationException e) {
            e.printStackTrace();
        }
        catch (final IllegalAccessException e2) {
            e2.printStackTrace();
        }
        catch (final InvocationTargetException e3) {
            e3.printStackTrace();
        }
        catch (final NoSuchMethodException e4) {
            e4.printStackTrace();
        }
    }
    
    public List<Integer> disposeAbnormalStatus(final FightObject target, final boolean isClear) {
        final List<Integer> listStatuss = new ArrayList<Integer>();
        for (final Integer status : StatusConst.getAnomalousStatus()) {
            if (target.getStatusValue(status) > 0 && isClear) {
                target.removeStatus(status);
                listStatuss.add(status);
            }
        }
        return listStatuss;
    }
    
    public List<Integer> disposeWuXinModification(final FightObject target, final boolean isClear) {
        final List<Integer> listStatuss = new ArrayList<Integer>();
        for (final Integer status : StatusConst.getWuxingStatus()) {
            if (target.getStatusValue(status) > 0 && isClear) {
                target.removeStatus(status);
                this.clearWuxin(target, status);
                listStatuss.add(status);
            }
        }
        return listStatuss;
    }
    
    private void clearWuxin(final FightObject obj, final Integer status) {
        final Map<Integer, Integer> wuxinPro = StatusConst.getWuxinPro();
        final Integer pro = wuxinPro.get(status);
        if (pro != null) {
            ImbueUtil.removeStatus(obj.getImbueMap(), pro, status);
        }
    }
    
    private void clear1008(final FightObject target) {
        target.removeStatus(1008);
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.RISE_ATTACK.getDist());
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.HIT_RATIO.getDist());
    }
    
    private void clear1009(final FightObject target) {
        target.removeStatus(1009);
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.K_FY.getDist());
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.K_HS.getDist());
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.K_HL.getDist());
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.K_YW.getDist());
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.K_F.getDist());
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.K_L.getDist());
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.K_S.getDist());
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.K_H.getDist());
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.WL_XS.getDist());
    }
    
    private void clear1010(final FightObject target) {
        target.removeStatus(1010);
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.RISE_SPEED.getDist());
    }
    
    private void clear1011(final FightObject target) {
        target.removeStatus(1011);
    }
    
    private void clear1018(final FightObject target) {
        target.removeStatus(1018);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_FY.getDist(), 84003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_HS.getDist(), 84003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_HL.getDist(), 84003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_YW.getDist(), 84003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_F.getDist(), 84003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_L.getDist(), 84003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_S.getDist(), 84003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_H.getDist(), 84003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.WL_XS.getDist(), 84003);
    }
    
    private void clear1022(final FightObject target) {
        final int dict = 1022;
        target.removeStatus(dict);
        target.removeStatusSub(dict + "value");
    }
    
    private void clear1023(final FightObject target) {
        target.removeStatus(1023);
        final int maxLife = target.getStatusSubMap().getInt("1023_max_life");
        final int maxMana = target.getStatusSubMap().getInt("1023_max_mana");
        target.removeStatusSub("1023_max_life");
        target.removeStatusSub("1023_max_mana");
        target.setCurrLife((target.getCurrLife() > maxLife) ? maxLife : target.getCurrLife());
        target.setCurrMana((target.getCurrMana() > maxMana) ? maxMana : target.getCurrMana());
        target.setMaxLife(maxLife);
        target.setMaxMana(maxMana);
    }
    
    private void clear2010(final FightObject target) {
        target.removeStatus(2010);
        target.getStatusMap().remove("2010skill");
        target.getStatusMap().remove("2010percent");
        target.getStatusMap().remove("2010reducePercent");
    }
    
    private void clear2031(final FightObject target) {
        target.removeStatus(2031);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_F.getDist(), 81003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_L.getDist(), 81003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_S.getDist(), 81003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_H.getDist(), 81003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_GH.getDist(), 81003);
    }
}
