
package com.kitty.system.game.fightnew.spectator.pool;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import com.kitty.system.game.fightnew.messages.RespExcuteFight;
import com.kitty.system.game.fightnew.messages.RespRoundStartParam;
import com.kitty.system.game.fightnew.model.SkillParam;
import com.kitty.system.game.fightnew.model.RefreshPet;
import com.kitty.system.game.fightnew.model.PetFightInParam;
import java.util.Map;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import java.util.List;

public class FightRoundParam
{
    private List<FightMember> fightMemberA;
    private List<FightMember> fightMemberB;
    private List<ExcuteFightParam> excuteFightParams;
    private Map<Integer, PetFightInParam> petFightInParamMap;
    private Map<Integer, List<RefreshPet>> petMap;
    private Map<Integer, List<Integer>> statusMap;
    private Map<Integer, List<SkillParam>> skillParamMap;
    private Map<Integer, RespRoundStartParam> respRoundStartParamMap;
    private RespExcuteFight respExcuteFight;
    
    public PetFightInParam getPetFightInParam(final int roleId) {
        if (this.petFightInParamMap != null && this.petFightInParamMap.get(roleId) != null) {
            return this.petFightInParamMap.get(roleId);
        }
        return null;
    }
    
    public List<RefreshPet> getRefreshPets(final int roleId) {
        if (this.petMap != null && this.petMap.get(roleId) != null) {
            return this.petMap.get(roleId);
        }
        return null;
    }
    
    public List<Integer> getStatusList(final int roleId) {
        if (this.statusMap != null && this.statusMap.get(roleId) != null) {
            return this.statusMap.get(roleId);
        }
        return null;
    }
    
    public List<SkillParam> getSkillParamList(final int roleId) {
        if (this.skillParamMap != null && this.skillParamMap.get(roleId) != null) {
            return this.skillParamMap.get(roleId);
        }
        return null;
    }
    
    public void addSkillParam(final int roleId, final List<SkillParam> skillParam) {
        if (this.skillParamMap == null) {
            this.skillParamMap = new HashMap<Integer, List<SkillParam>>();
        }
        this.skillParamMap.put(roleId, skillParam);
    }
    
    public void addPetFightInParam(final int roleId, final PetFightInParam petFightInParam) {
        if (this.petFightInParamMap == null) {
            this.petFightInParamMap = new HashMap<Integer, PetFightInParam>();
        }
        this.petFightInParamMap.put(roleId, petFightInParam);
    }
    
    public void addRefreshPet(final int roleId, final List<RefreshPet> refreshPets) {
        if (this.petMap == null) {
            this.petMap = new HashMap<Integer, List<RefreshPet>>();
        }
        this.petMap.put(roleId, refreshPets);
    }
    
    public void addStatus(final int roleId, final List<Integer> status) {
        if (this.statusMap == null) {
            this.statusMap = new HashMap<Integer, List<Integer>>();
        }
        this.statusMap.put(roleId, status);
    }
    
    public List<FightMember> getFightMemberA() {
        return this.fightMemberA;
    }
    
    public List<FightMember> getFightMemberB() {
        return this.fightMemberB;
    }
    
    public List<ExcuteFightParam> getExcuteFightParams() {
        return this.excuteFightParams;
    }
    
    public Map<Integer, PetFightInParam> getPetFightInParamMap() {
        return this.petFightInParamMap;
    }
    
    public Map<Integer, List<RefreshPet>> getPetMap() {
        return this.petMap;
    }
    
    public Map<Integer, List<Integer>> getStatusMap() {
        return this.statusMap;
    }
    
    public Map<Integer, List<SkillParam>> getSkillParamMap() {
        return this.skillParamMap;
    }
    
    public Map<Integer, RespRoundStartParam> getRespRoundStartParamMap() {
        return this.respRoundStartParamMap;
    }
    
    public RespExcuteFight getRespExcuteFight() {
        return this.respExcuteFight;
    }
    
    public void setFightMemberA(final List<FightMember> fightMemberA) {
        this.fightMemberA = fightMemberA;
    }
    
    public void setFightMemberB(final List<FightMember> fightMemberB) {
        this.fightMemberB = fightMemberB;
    }
    
    public void setExcuteFightParams(final List<ExcuteFightParam> excuteFightParams) {
        this.excuteFightParams = excuteFightParams;
    }
    
    public void setPetFightInParamMap(final Map<Integer, PetFightInParam> petFightInParamMap) {
        this.petFightInParamMap = petFightInParamMap;
    }
    
    public void setPetMap(final Map<Integer, List<RefreshPet>> petMap) {
        this.petMap = petMap;
    }
    
    public void setStatusMap(final Map<Integer, List<Integer>> statusMap) {
        this.statusMap = statusMap;
    }
    
    public void setSkillParamMap(final Map<Integer, List<SkillParam>> skillParamMap) {
        this.skillParamMap = skillParamMap;
    }
    
    public void setRespRoundStartParamMap(final Map<Integer, RespRoundStartParam> respRoundStartParamMap) {
        this.respRoundStartParamMap = respRoundStartParamMap;
    }
    
    public void setRespExcuteFight(final RespExcuteFight respExcuteFight) {
        this.respExcuteFight = respExcuteFight;
    }
    
    @ConstructorProperties({ "fightMemberA", "fightMemberB", "excuteFightParams", "petFightInParamMap", "petMap", "statusMap", "skillParamMap", "respRoundStartParamMap", "respExcuteFight" })
    public FightRoundParam(final List<FightMember> fightMemberA, final List<FightMember> fightMemberB, final List<ExcuteFightParam> excuteFightParams, final Map<Integer, PetFightInParam> petFightInParamMap, final Map<Integer, List<RefreshPet>> petMap, final Map<Integer, List<Integer>> statusMap, final Map<Integer, List<SkillParam>> skillParamMap, final Map<Integer, RespRoundStartParam> respRoundStartParamMap, final RespExcuteFight respExcuteFight) {
        this.fightMemberA = fightMemberA;
        this.fightMemberB = fightMemberB;
        this.excuteFightParams = excuteFightParams;
        this.petFightInParamMap = petFightInParamMap;
        this.petMap = petMap;
        this.statusMap = statusMap;
        this.skillParamMap = skillParamMap;
        this.respRoundStartParamMap = respRoundStartParamMap;
        this.respExcuteFight = respExcuteFight;
    }
    
    public FightRoundParam() {
    }
}
