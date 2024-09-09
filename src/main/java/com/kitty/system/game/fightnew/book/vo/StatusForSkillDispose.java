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
    public StatusForSkillDispose() {
    }
    
    public void disposePositiveStatus(FightObject target, int status, boolean isClear) {
        try {
            Class<?> classType = this.getClass();
            Object obj = classType.getConstructor(new Class[0]).newInstance(new Object[0]);
            if (isClear) {
                Method clearMethod = classType.getDeclaredMethod("clear" + status, FightObject.class);
                clearMethod.invoke(obj, target);
            }
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
        catch (NoSuchMethodException e4) {
            e4.printStackTrace();
        }
    }
    
    public List<Integer> disposeAbnormalStatus(FightObject target, boolean isClear) {
        List<Integer> listStatuss = new ArrayList<>();
        for (Integer status : StatusConst.getAnomalousStatus()) {
            if (target.getStatusValue((int)status) > 0 && isClear) {
                target.removeStatus((int)status);
                listStatuss.add(status);
            }
            else {
                continue;
            }
        }
        return listStatuss;
    }
    
    public List<Integer> disposeWuXinModification(FightObject target, boolean isClear) {
        List<Integer> listStatuss = new ArrayList<>();
        for (Integer status : StatusConst.getWuxingStatus()) {
            if (target.getStatusValue((int)status) > 0 && isClear) {
                target.removeStatus((int)status);
                this.clearWuxin(target, status);
                listStatuss.add(status);
            }
            else {
                continue;
            }
        }
        return listStatuss;
    }
    
    private void clearWuxin(FightObject obj, Integer status) {
        Map<Integer, Integer> wuxinPro = StatusConst.getWuxinPro();
        Integer pro = (Integer)wuxinPro.get(status);
        if (pro != null) {
            ImbueUtil.removeStatus(obj.getImbueMap(), (int)pro, (int)status);
        }
    }
    
    private void clear1008(FightObject target) {
        target.removeStatus(1008);
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.RISE_ATTACK.getDist());
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.HIT_RATIO.getDist());
    }
    
    private void clear1009(FightObject target) {
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
    
    private void clear1010(FightObject target) {
        target.removeStatus(1010);
        ImbueUtil.removePro(target.getImbueMap(), PRO_DICT_TYPE.RISE_SPEED.getDist());
    }
    
    private void clear1011(FightObject target) {
        target.removeStatus(1011);
    }
    
    private void clear1018(FightObject target) {
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
    
    private void clear1022(FightObject target) {
        int dict = 1022;
        target.removeStatus(dict);
        target.removeStatusSub(dict + "value");
    }
    
    private void clear1023(FightObject target) {
        target.removeStatus(1023);
        int maxLife = target.getStatusSubMap().getInt("1023_max_life");
        int maxMana = target.getStatusSubMap().getInt("1023_max_mana");
        target.removeStatusSub("1023_max_life");
        target.removeStatusSub("1023_max_mana");
        target.setCurrLife((target.getCurrLife() > maxLife) ? maxLife : target.getCurrLife());
        target.setCurrMana((target.getCurrMana() > maxMana) ? maxMana : target.getCurrMana());
        target.setMaxLife(maxLife);
        target.setMaxMana(maxMana);
    }
    
    private void clear2010(FightObject target) {
        target.removeStatus(2010);
        target.getStatusMap().remove("2010skill");
        target.getStatusMap().remove("2010percent");
        target.getStatusMap().remove("2010reducePercent");
    }
    
    private void clear2031(FightObject target) {
        target.removeStatus(2031);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_F.getDist(), 81003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_L.getDist(), 81003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_S.getDist(), 81003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_H.getDist(), 81003);
        ImbueUtil.removeSkill(target.getImbueMap(), PRO_DICT_TYPE.K_GH.getDist(), 81003);
    }
}
