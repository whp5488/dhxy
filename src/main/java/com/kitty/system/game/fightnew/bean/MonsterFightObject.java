
package com.kitty.system.game.fightnew.bean;

import java.util.Optional;
import com.kitty.system.game.fightnew.utils.FightUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.RandomUtil;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysDataMonsterPeripheral;
import com.kitty.system.init.domain.vo.SkillParameter;
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
    
    public MonsterFightObject(final SysDataMonster monster, final Integer pos, final int monsterId) {
        this.selectMonsterSerial = 0;
        this.loadPeripheral(monster);
        final int life = monster.getHp();
        final int mana = monster.getMp();
        final int speed = monster.getSp();
        final int power = monster.getAp();
        this.setId(monsterId);
        this.setHostId(monsterId);
        this.setName(DataGlobalFunc.dynamicName(monster.getName()));
        this.setLevel(monster.getGrade() % 1000);
        this.setLogic(LOGIC.LOGICTYPE_MONSTER.getValue());
        this.setSerial(monster.getSerial());
        this.setPos(pos);
        this.setIcon(monster.getShape());
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
    
    public void loadPeripheral(final SysDataMonster monster) {
        final SysDataMonsterPeripheral monsterPeripheral = FightDataPool.monsterPeripheralMap.get(monster.getSerial());
        if (monsterPeripheral != null) {
            this.setWeaponId(monsterPeripheral.getWeaponId());
            this.setGem(monsterPeripheral.getGemNum());
            if (monsterPeripheral.getColor() == 1) {
                final NutMap map = new NutMap();
                map.setv("a", RandomUtil.round(0, 10));
                map.setv("b", RandomUtil.round(0, 10));
                this.setColor(map.toString());
            }
        }
    }
    
    public MonsterFightObject(final FightObject petObject, final int objId, final int serial, final int monsterType) {
        this.selectMonsterSerial = 0;
        this.setWeaponId(petObject.getWeaponId());
        this.setGem(petObject.getGem());
        this.setColor(petObject.getColor());
        this.setId(objId);
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
    
    public NutMap proFieldValues(final SysDataMonster monster) {
        final NutMap nutMap = new NutMap();
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_J.getDist()), monster.getWxJin());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_M.getDist()), monster.getWxMu());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_S.getDist()), monster.getWxShui());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_H.getDist()), (Object)monster.getWxHuo());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_T.getDist()), (Object)monster.getWxTu());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KJ.getDist()), (Object)monster.getConGold());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KM.getDist()), (Object)monster.getConWood());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KS.getDist()), (Object)monster.getConWater());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KH.getDist()), (Object)monster.getConFire());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KT.getDist()), (Object)monster.getConSoil());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WL_XS.getDist()), (Object)monster.getPdeFend());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), (Object)monster.getPPofang());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist()), (Object)monster.getPPofangPro());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist()), (Object)monster.getPFanZhen());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.SHOCK_RATIO.getDist()), (Object)monster.getPFanZhenPro());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()), (Object)monster.getPKuangBao());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.MISS_RATIO.getDist()), (Object)monster.getPLjPro());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist()), (Object)monster.getPLjTimes());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist()), (Object)monster.getPLjPro());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), (Object)monster.getPAcc());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.MISS_RATIO.getDist()), (Object)monster.getPSbl());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.S_KB_RATIO.getDist()), (Object)monster.getShuiOdd());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.H_KB_RATIO.getDist()), (Object)monster.getHuoOdd());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.F_KB_RATIO.getDist()), (Object)monster.getFengOdd());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.L_KB_RATIO.getDist()), (Object)monster.getLeiOdd());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.GH_KB_RATIO.getDist()), (Object)monster.getGuiHuoOdd());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.SS_KB_RATIO.getDist()), (Object)monster.getSanShiOdd());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_S.getDist()), (Object)monster.getRainEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_H.getDist()), (Object)monster.getFireEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_F.getDist()), (Object)monster.getWindEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_L.getDist()), (Object)monster.getThunderEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_SS.getDist()), (Object)monster.getSanshiEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_PJ.getDist()), (Object)monster.getBreakEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ATTACK.getDist()), (Object)monster.getAdAtkEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_DEF.getDist()), (Object)monster.getAdDefEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_SPEED.getDist()), (Object)monster.getAdSpdEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_QHS.getDist()), (Object)monster.getSweepEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZJ.getDist()), (Object)monster.getThudEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_HL.getDist()), (Object)monster.getDisorderEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_FY.getDist()), (Object)monster.getSealEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_D.getDist()), (Object)monster.getPoisonEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_HS.getDist()), (Object)monster.getSleepEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_YW.getDist()), (Object)monster.getForgetEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_MH.getDist()), (Object)monster.getCharmEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZY.getDist()), (Object)monster.getCureEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZS.getDist()), (Object)monster.getShockEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_GH.getDist()), (Object)monster.getGfireEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_S.getDist()), (Object)monster.getKShui());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_H.getDist()), (Object)monster.getKHuo());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_F.getDist()), (Object)monster.getKFeng());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_L.getDist()), (Object)monster.getKLei());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), (Object)monster.getKHunLuan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), (Object)monster.getKFengYin());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), (Object)monster.getKHunShui());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), (Object)monster.getKYiWang());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_DU.getDist()), (Object)monster.getKZhongDu());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_ZS.getDist()), (Object)monster.getKZhenShe());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), (Object)monster.getKGuiHuo());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_SS.getDist()), (Object)monster.getKSanShi());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_MH.getDist()), (Object)monster.getKShuaiRuo());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), (Object)monster.getFkShui());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), (Object)monster.getFkHuo());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), (Object)monster.getFkFeng());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), (Object)monster.getFkLei());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_D.getDist()), (Object)monster.getFkZhongDu());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_HL.getDist()), (Object)monster.getFkHunLuan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_ZS.getDist()), (Object)monster.getFkZhenShe());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), (Object)monster.getFkGuiHuo());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist()), (Object)monster.getFkSanShi());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_FY.getDist()), (Object)monster.getFkFengYin());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_YW.getDist()), (Object)monster.getFkYiWang());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), (Object)monster.getFPdeFend());
        return nutMap;
    }
    
    @Override
    public RoleSkill getSkillBy(final Integer skillId) {
        if (String.valueOf(skillId).length() == 6) {
            final String hostSkillId = String.valueOf(skillId).substring(0, 5);
            final Optional<RoleSkill> optional = this.getSkillIds().stream().filter(skill -> skill.getSkillId().equals(Integer.parseInt(hostSkillId))).findFirst();
            if (optional.isPresent()) {
                return FightUtils.getPetRoleSkill(skillId);
            }
        }
        final Optional<RoleSkill> optional2 = this.getSkillIds().stream().filter(skill -> skill.getSkillId().equals(skillId)).findFirst();
        if (optional2.isPresent()) {
            return optional2.get();
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
    
    public void setPredictTargetList(final List<FightObject> predictTargetList) {
        this.predictTargetList = predictTargetList;
    }
    
    public Integer getSelectMonsterSerial() {
        return this.selectMonsterSerial;
    }
    
    public void setSelectMonsterSerial(final Integer selectMonsterSerial) {
        this.selectMonsterSerial = selectMonsterSerial;
    }
    
    public int getMonsterType() {
        return this.monsterType;
    }
    
    public void setMonsterType(final int monsterType) {
        this.monsterType = monsterType;
    }
}
