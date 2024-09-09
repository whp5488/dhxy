package com.kitty.system.game.fightnew.bean;

import java.util.Optional;
import com.kitty.system.game.fightnew.utils.FightUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.RandomUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysDataMonsterPeripheral;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.init.domain.SysDataMonster;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;

public class MonsterFightObject extends FightObject
{
    private List<FightObject> predictTargetList;
    private Integer selectMonsterSerial;
    private int monsterType;
    
    public MonsterFightObject(SysDataMonster monster, Integer pos, int monsterId) {
        this.selectMonsterSerial = Integer.valueOf(0);
        this.loadPeripheral(monster);
        int life = (int)Integer.valueOf((int)monster.getHp());
        int mana = (int)Integer.valueOf((int)monster.getMp());
        int speed = (int)Integer.valueOf((int)monster.getSp());
        int power = (int)Integer.valueOf((int)monster.getAp());
        this.setId(Integer.valueOf(monsterId));
        this.setHostId(monsterId);
        this.setName(DataGlobalFunc.dynamicName(monster.getName()));
        this.setRelive((int)monster.getGrade() / 1000);
        this.setLevel((int)monster.getGrade() % 1000);
        this.setLogic(LOGIC.LOGICTYPE_MONSTER.getValue());
        this.setSerial((int)monster.getSerial());
        this.setPos((int)pos);
        this.setIcon((int)monster.getShape());
        this.setSpeed(speed);
        this.setPhyPower(power);
        this.setMaxLife(life);
        this.setMaxMana(life);
        this.setCurrLife(life);
        this.setCurrMana(mana);
        this.setSkillIds(SkillFunc.initMonsterSkill(monster.getSkills(), monster.getJnSld(), monster.getGrade()));
        this.setPropertyMap(this.proFieldValues(monster));
        this.setMonsterType(monster.getMonsterType());
    }
    
    public void loadPeripheral(SysDataMonster monster) {
        SysDataMonsterPeripheral monsterPeripheral = (SysDataMonsterPeripheral)FightDataPool.monsterPeripheralMap.get(monster.getSerial());
        if (monsterPeripheral != null) {
            this.setWeaponId(monsterPeripheral.getWeaponId());
            this.setGem(monsterPeripheral.getGemNum());
            if (monsterPeripheral.getColor() == 1) {
                NutMap map = new NutMap();
                map.setv("a", Integer.valueOf(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(10))));
                map.setv("b", Integer.valueOf(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(10))));
                this.setColor(map.toString());
            }
        }
    }
    
    public MonsterFightObject(FightObject petObject, int objId, int serial, int monsterType) {
        this.selectMonsterSerial = Integer.valueOf(0);
        this.setWeaponId(petObject.getWeaponId());
        this.setGem(petObject.getGem());
        this.setColor(petObject.getColor());
        this.setId(Integer.valueOf(objId));
        this.setHostId(0);
        this.setName(petObject.getName());
        this.setLevel(petObject.getLevel());
        this.setLogic(LOGIC.LOGICTYPE_MONSTER.getValue());
        this.setSerial(serial);
        this.setPos(petObject.getPos());
        this.setIcon(petObject.getIcon());
        this.setSpeed(petObject.getSpeed());
        this.setPhyPower(petObject.getPhyPower());
        this.setMaxLife(petObject.getMaxLife());
        this.setMaxMana(petObject.getMaxMana());
        this.setCurrLife(petObject.getCurrLife());
        this.setCurrMana(petObject.getCurrMana());
        this.setSkillIds(petObject.getSkillIds());
        this.setPropertyMap(petObject.getPropertyMap());
        this.setMonsterType(monsterType);
    }
    
    public NutMap proFieldValues(SysDataMonster monster) {
        NutMap nutMap = new NutMap();
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_J.getDist()), monster.getWxJin());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_M.getDist()), monster.getWxMu());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_S.getDist()), monster.getWxShui());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_H.getDist()), monster.getWxHuo());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_T.getDist()), monster.getWxTu());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KJ.getDist()), Double.valueOf(monster.getConGold()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KM.getDist()), Double.valueOf(monster.getConWood()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KS.getDist()), Double.valueOf(monster.getConWater()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KH.getDist()), Double.valueOf(monster.getConFire()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KT.getDist()), Double.valueOf(monster.getConSoil()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WL_XS.getDist()), Double.valueOf(monster.getPdeFend()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), Double.valueOf(monster.getPPofang()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist()), Double.valueOf(monster.getPPofangPro()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist()), Double.valueOf(monster.getPFanZhen()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.SHOCK_RATIO.getDist()), Double.valueOf(monster.getPFanZhenPro()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()), Double.valueOf(monster.getPKuangBao()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.MISS_RATIO.getDist()), Double.valueOf(monster.getPLjPro()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist()), monster.getPLjTimes());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist()), Double.valueOf(monster.getPLjPro()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), Double.valueOf(monster.getPAcc()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.MISS_RATIO.getDist()), Double.valueOf(monster.getPSbl()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.S_KB_RATIO.getDist()), Double.valueOf(monster.getShuiOdd()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.H_KB_RATIO.getDist()), Double.valueOf(monster.getHuoOdd()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.F_KB_RATIO.getDist()), Double.valueOf(monster.getFengOdd()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.L_KB_RATIO.getDist()), Double.valueOf(monster.getLeiOdd()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.GH_KB_RATIO.getDist()), Double.valueOf(monster.getGuiHuoOdd()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.SS_KB_RATIO.getDist()), Double.valueOf(monster.getSanShiOdd()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_S.getDist()), Double.valueOf(monster.getRainEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_H.getDist()), Double.valueOf(monster.getFireEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_F.getDist()), Double.valueOf(monster.getWindEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_L.getDist()), Double.valueOf(monster.getThunderEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_SS.getDist()), Double.valueOf(monster.getSanshiEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_PJ.getDist()), Double.valueOf(monster.getBreakEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ATTACK.getDist()), Double.valueOf(monster.getAdAtkEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_DEF.getDist()), Double.valueOf(monster.getAdDefEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_SPEED.getDist()), Double.valueOf(monster.getAdSpdEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_QHS.getDist()), Double.valueOf(monster.getSweepEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZJ.getDist()), Double.valueOf(monster.getThudEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_HL.getDist()), Double.valueOf(monster.getDisorderEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_FY.getDist()), Double.valueOf(monster.getSealEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_D.getDist()), Double.valueOf(monster.getPoisonEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_HS.getDist()), Double.valueOf(monster.getSleepEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_YW.getDist()), Double.valueOf(monster.getForgetEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_MH.getDist()), Double.valueOf(monster.getCharmEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZY.getDist()), Double.valueOf(monster.getCureEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZS.getDist()), Double.valueOf(monster.getShockEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_GH.getDist()), Double.valueOf(monster.getGfireEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_S.getDist()), Double.valueOf(monster.getKShui()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_H.getDist()), Double.valueOf(monster.getKHuo()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_F.getDist()), Double.valueOf(monster.getKFeng()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_L.getDist()), Double.valueOf(monster.getKLei()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(monster.getKHunLuan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(monster.getKFengYin()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(monster.getKHunShui()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), Double.valueOf(monster.getKYiWang()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_DU.getDist()), Double.valueOf(monster.getKZhongDu()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_ZS.getDist()), Double.valueOf(monster.getKZhenShe()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(monster.getKGuiHuo()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_SS.getDist()), Double.valueOf(monster.getKSanShi()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_MH.getDist()), Double.valueOf(monster.getKShuaiRuo()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Double.valueOf(monster.getFkShui()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Double.valueOf(monster.getFkHuo()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Double.valueOf(monster.getFkFeng()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Double.valueOf(monster.getFkLei()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_D.getDist()), Double.valueOf(monster.getFkZhongDu()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_HL.getDist()), Double.valueOf(monster.getFkHunLuan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_ZS.getDist()), Double.valueOf(monster.getFkZhenShe()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Double.valueOf(monster.getFkGuiHuo()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist()), Double.valueOf(monster.getFkSanShi()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_FY.getDist()), Double.valueOf(monster.getFkFengYin()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_YW.getDist()), Double.valueOf(monster.getFkYiWang()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), Double.valueOf(monster.getFPdeFend()));
        return nutMap;
    }
    
    @Override
    public RoleSkill getSkillBy(Integer skillId) {
        if (String.valueOf(skillId).length() == 6) {
            String hostSkillId = String.valueOf(skillId).substring(0, 5);
            Optional<RoleSkill> optional = this.getSkillIds().stream().filter(skill -> skill.getSkillId().equals(Integer.valueOf(Integer.parseInt(hostSkillId)))).findFirst();
            if (optional.isPresent()) {
                return FightUtils.getPetRoleSkill((int)skillId);
            }
        }
        Optional<RoleSkill> optional2 = this.getSkillIds().stream().filter(skill -> skill.getSkillId().equals(skillId)).findFirst();
        if (optional2.isPresent()) {
            return (RoleSkill)optional2.get();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "MonsterFightObject{id=" + this.id + ", name='" + this.name + '\'' + '}';
    }
    
    public List<FightObject> getPredictTargetList() {
        return this.predictTargetList;
    }
    
    public void setPredictTargetList(List<FightObject> predictTargetList) {
        this.predictTargetList = predictTargetList;
    }
    
    public Integer getSelectMonsterSerial() {
        return this.selectMonsterSerial;
    }
    
    public void setSelectMonsterSerial(Integer selectMonsterSerial) {
        this.selectMonsterSerial = selectMonsterSerial;
    }
    
    public int getMonsterType() {
        return this.monsterType;
    }
    
    public void setMonsterType(int monsterType) {
        this.monsterType = monsterType;
    }
}
