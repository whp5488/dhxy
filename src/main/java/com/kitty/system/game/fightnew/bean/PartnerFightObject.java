
package com.kitty.system.game.fightnew.bean;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import com.kitty.system.init.domain.SysPartnerProperty;
import java.util.Map;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.service.CountUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.common.utils.PositionUtils;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.fightnew.FightObject;

public class PartnerFightObject extends FightObject
{
    private Partner partner;
    private FightObject master;
    private Integer fightSkillId;
    
    public PartnerFightObject(final Partner partner, final FightObject master, final int index) {
        this.master = master;
        this.setId(partner.getPartnerId());
        this.setIcon(partner.getIcon());
        this.setName(partner.getPartnerName());
        this.setSerial(partner.getPartnerType());
        this.setLevel(partner.getPartnerLv());
        this.setPos(PositionUtils.positions.get(index));
        this.setRace(partner.getRace());
        this.setMaxLife(partner.getMaxLife());
        this.setCurrLife(partner.getCurrLife());
        this.setLogic(LOGIC.LOGICTYPE_PARTNER.getValue());
        this.setHostId(master.getId());
        final List<RoleSkill> collect = partner.getSkillBox().getSkills().values().stream().collect(Collectors.toList());
        this.setSkillIds(collect);
        this.setSkillId(partner.getOperDefaultSkillId());
        this.setFightSkillId(partner.getOperDefaultSkillId());
        this.setAutoFight(1);
        this.setASide(master.isASide());
        this.setSpeed(CountUtils.countPartnerSpeed(partner));
        this.setPropertyMap(this.proFieldValues(partner));
    }
    
    private NutMap proFieldValues(final Partner partner) {
        final short level = partner.getPartnerLv();
        final Integer serial = partner.getPartnerType();
        final NutMap nutMap = new NutMap();
        final Map<Short, SysPartnerProperty> levelSysPartnerPropertyMap = PartnerDataPool.partnerPropertyMap.get(serial);
        if (levelSysPartnerPropertyMap == null || levelSysPartnerPropertyMap.size() <= 0) {
            return nutMap;
        }
        final SysPartnerProperty partnerProperty = levelSysPartnerPropertyMap.get(level);
        if (partnerProperty == null) {
            return nutMap;
        }
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_J.getDist()), (Object)partnerProperty.getGold());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_M.getDist()), (Object)partnerProperty.getWood());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_S.getDist()), (Object)partnerProperty.getWater());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_H.getDist()), (Object)partnerProperty.getFire());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_T.getDist()), (Object)partnerProperty.getSoil());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KJ.getDist()), (Object)partnerProperty.getConGold());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KM.getDist()), (Object)partnerProperty.getConWood());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KS.getDist()), (Object)partnerProperty.getConWater());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KH.getDist()), (Object)partnerProperty.getConFire());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KT.getDist()), (Object)partnerProperty.getConSoil());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WL_XS.getDist()), (Object)partnerProperty.getPhyDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), (Object)partnerProperty.getPhyDefNef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()), (Object)partnerProperty.getFatalRate());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist()), (Object)partnerProperty.getHitCombo());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist()), (Object)partnerProperty.getHitComboRate());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), (Object)partnerProperty.getHitRate());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.MISS_RATIO.getDist()), (Object)partnerProperty.getVoidRate());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist()), (Object)partnerProperty.getPhyDefNefRate());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.F_KB_RATIO.getDist()), (Object)partnerProperty.getWindRageOdds());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.L_KB_RATIO.getDist()), (Object)partnerProperty.getThunderRageOdds());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.S_KB_RATIO.getDist()), (Object)partnerProperty.getRainRageOdds());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.H_KB_RATIO.getDist()), (Object)partnerProperty.getFireRageOdds());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.SS_KB_RATIO.getDist()), (Object)partnerProperty.getShanshiRageOdds());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.GH_KB_RATIO.getDist()), (Object)partnerProperty.getGfireRageOdds());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_S.getDist()), (Object)partnerProperty.getRainEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_H.getDist()), (Object)partnerProperty.getFireEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_F.getDist()), (Object)partnerProperty.getWindEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_L.getDist()), (Object)partnerProperty.getThunderEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_SS.getDist()), (Object)partnerProperty.getSanshiEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_PJ.getDist()), (Object)partnerProperty.getBreakEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ATTACK.getDist()), (Object)partnerProperty.getAdAtkEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_DEF.getDist()), (Object)partnerProperty.getAdDefEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_SPEED.getDist()), (Object)partnerProperty.getAdSpdEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_QHS.getDist()), (Object)partnerProperty.getSweepEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZJ.getDist()), (Object)partnerProperty.getThudEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_HL.getDist()), (Object)partnerProperty.getDisorderEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_FY.getDist()), (Object)partnerProperty.getSealEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_D.getDist()), (Object)partnerProperty.getPoisonEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_HS.getDist()), (Object)partnerProperty.getSleepEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_YW.getDist()), (Object)partnerProperty.getForgetEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_MH.getDist()), (Object)partnerProperty.getCharmEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZY.getDist()), (Object)partnerProperty.getCureEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZS.getDist()), (Object)partnerProperty.getShockEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_GH.getDist()), (Object)partnerProperty.getGfireEhan());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_S.getDist()), (Object)partnerProperty.getRainDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_H.getDist()), (Object)partnerProperty.getFireDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_F.getDist()), (Object)partnerProperty.getWindDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_L.getDist()), (Object)partnerProperty.getThunderDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), (Object)partnerProperty.getDisorderDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), (Object)partnerProperty.getSealDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), (Object)partnerProperty.getSleepDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), (Object)partnerProperty.getForgetDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_DU.getDist()), (Object)partnerProperty.getPoisonDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_ZS.getDist()), (Object)partnerProperty.getShockDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), (Object)partnerProperty.getGfireDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_SS.getDist()), (Object)partnerProperty.getSanshiDef());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), (Object)partnerProperty.getRainDefNeg());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), (Object)partnerProperty.getFireDefNeg());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), (Object)partnerProperty.getWindDefNeg());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), (Object)partnerProperty.getThunderDefNeg());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_HL.getDist()), (Object)partnerProperty.getDisorderDefNeg());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_ZS.getDist()), (Object)partnerProperty.getShockDefNeg());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), (Object)partnerProperty.getGfireDefNeg());
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist()), (Object)partnerProperty.getSanshiDefNeg());
        return nutMap;
    }
    
    public Partner getPartner() {
        return this.partner;
    }
    
    public Integer getFightSkillId() {
        return this.fightSkillId;
    }
    
    public void setFightSkillId(final Integer fightSkillId) {
        this.fightSkillId = fightSkillId;
    }
}
