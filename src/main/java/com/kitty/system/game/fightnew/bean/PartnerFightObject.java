package com.kitty.system.game.fightnew.bean;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import java.util.Map;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.service.CountUtils;
import java.util.stream.Collectors;
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
    
    public PartnerFightObject(Partner partner, FightObject master, int index) {
        this.master = master;
        this.setId(partner.getPartnerId());
        this.setIcon((int)partner.getIcon());
        this.setName(partner.getPartnerName());
        this.setSerial((int)partner.getPartnerType());
        this.setLevel((int)partner.getPartnerLv());
        this.setPos((int)(short)PositionUtils.positions.get(index));
        this.setRace((int)partner.getRace());
        this.setMaxLife((int)partner.getMaxLife());
        this.setCurrLife((int)partner.getCurrLife());
        this.setLogic(LOGIC.LOGICTYPE_PARTNER.getValue());
        this.setHostId((int)master.getId());
        List<RoleSkill> collect = (List)partner.getSkillBox().getSkills().values().stream().collect(Collectors.toList());
        this.setSkillIds(collect);
        this.setSkillId(partner.getOperDefaultSkillId());
        this.setFightSkillId(Integer.valueOf(partner.getOperDefaultSkillId()));
        this.setAutoFight(1);
        this.setASide(master.isASide());
        this.setSpeed(CountUtils.countPartnerSpeed(partner));
        this.setPropertyMap(this.proFieldValues(partner));
    }
    
    private NutMap proFieldValues(Partner partner) {
        short level = partner.getPartnerLv();
        Integer serial = partner.getPartnerType();
        NutMap nutMap = new NutMap();
        Map<Short, SysPartnerProperty> levelSysPartnerPropertyMap = (Map)PartnerDataPool.partnerPropertyMap.get(serial);
        if (levelSysPartnerPropertyMap == null || levelSysPartnerPropertyMap.size() <= 0) {
            return nutMap;
        }
        SysPartnerProperty partnerProperty = (SysPartnerProperty)levelSysPartnerPropertyMap.get(Short.valueOf(level));
        if (partnerProperty == null) {
            return nutMap;
        }
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_J.getDist()), Integer.valueOf(partnerProperty.getGold()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_M.getDist()), Integer.valueOf(partnerProperty.getWood()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_S.getDist()), Integer.valueOf(partnerProperty.getWater()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_H.getDist()), Integer.valueOf(partnerProperty.getFire()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WX_T.getDist()), Integer.valueOf(partnerProperty.getSoil()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KJ.getDist()), Double.valueOf(partnerProperty.getConGold()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KM.getDist()), Double.valueOf(partnerProperty.getConWood()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KS.getDist()), Double.valueOf(partnerProperty.getConWater()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KH.getDist()), Double.valueOf(partnerProperty.getConFire()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.Q_KT.getDist()), Double.valueOf(partnerProperty.getConSoil()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.WL_XS.getDist()), Double.valueOf(partnerProperty.getPhyDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), Double.valueOf(partnerProperty.getPhyDefNef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist()), Double.valueOf(partnerProperty.getFatalRate()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist()), Integer.valueOf(partnerProperty.getHitCombo()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist()), Double.valueOf(partnerProperty.getHitComboRate()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), Double.valueOf(partnerProperty.getHitRate()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.MISS_RATIO.getDist()), Double.valueOf(partnerProperty.getVoidRate()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist()), Double.valueOf(partnerProperty.getPhyDefNefRate()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.F_KB_RATIO.getDist()), Double.valueOf(partnerProperty.getWindRageOdds()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.L_KB_RATIO.getDist()), Double.valueOf(partnerProperty.getThunderRageOdds()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.S_KB_RATIO.getDist()), Double.valueOf(partnerProperty.getRainRageOdds()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.H_KB_RATIO.getDist()), Double.valueOf(partnerProperty.getFireRageOdds()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.SS_KB_RATIO.getDist()), Double.valueOf(partnerProperty.getShanshiRageOdds()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.GH_KB_RATIO.getDist()), Double.valueOf(partnerProperty.getGfireRageOdds()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_S.getDist()), Double.valueOf(partnerProperty.getRainEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_H.getDist()), Double.valueOf(partnerProperty.getFireEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_F.getDist()), Double.valueOf(partnerProperty.getWindEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_L.getDist()), Double.valueOf(partnerProperty.getThunderEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_SS.getDist()), Double.valueOf(partnerProperty.getSanshiEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_PJ.getDist()), Double.valueOf(partnerProperty.getBreakEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ATTACK.getDist()), Double.valueOf(partnerProperty.getAdAtkEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_DEF.getDist()), Double.valueOf(partnerProperty.getAdDefEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_SPEED.getDist()), Double.valueOf(partnerProperty.getAdSpdEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_QHS.getDist()), Double.valueOf(partnerProperty.getSweepEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZJ.getDist()), Double.valueOf(partnerProperty.getThudEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_HL.getDist()), Double.valueOf(partnerProperty.getDisorderEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_FY.getDist()), Double.valueOf(partnerProperty.getSealEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_D.getDist()), Double.valueOf(partnerProperty.getPoisonEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_HS.getDist()), Double.valueOf(partnerProperty.getSleepEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_YW.getDist()), Double.valueOf(partnerProperty.getForgetEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_MH.getDist()), Double.valueOf(partnerProperty.getCharmEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZY.getDist()), Double.valueOf(partnerProperty.getCureEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_ZS.getDist()), Double.valueOf(partnerProperty.getShockEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.RISE_GH.getDist()), Double.valueOf(partnerProperty.getGfireEhan()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_S.getDist()), Double.valueOf(partnerProperty.getRainDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_H.getDist()), Double.valueOf(partnerProperty.getFireDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_F.getDist()), Double.valueOf(partnerProperty.getWindDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_L.getDist()), Double.valueOf(partnerProperty.getThunderDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(partnerProperty.getDisorderDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(partnerProperty.getSealDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(partnerProperty.getSleepDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), Double.valueOf(partnerProperty.getForgetDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_DU.getDist()), Double.valueOf(partnerProperty.getPoisonDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_ZS.getDist()), Double.valueOf(partnerProperty.getShockDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_GH.getDist()), Double.valueOf(partnerProperty.getGfireDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.K_SS.getDist()), Double.valueOf(partnerProperty.getSanshiDef()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist()), Double.valueOf(partnerProperty.getRainDefNeg()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist()), Double.valueOf(partnerProperty.getFireDefNeg()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist()), Double.valueOf(partnerProperty.getWindDefNeg()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist()), Double.valueOf(partnerProperty.getThunderDefNeg()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_HL.getDist()), Double.valueOf(partnerProperty.getDisorderDefNeg()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_ZS.getDist()), Double.valueOf(partnerProperty.getShockDefNeg()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist()), Double.valueOf(partnerProperty.getGfireDefNeg()));
        nutMap.put(String.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist()), Double.valueOf(partnerProperty.getSanshiDefNeg()));
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
