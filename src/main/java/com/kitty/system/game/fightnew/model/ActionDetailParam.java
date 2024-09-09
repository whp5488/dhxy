package com.kitty.system.game.fightnew.model;

import java.util.Iterator;
import java.util.Optional;
import com.kitty.system.game.vo.FieldByte;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import java.util.List;

public class ActionDetailParam
{
    private int type;
    private int skillSerial;
    private byte colorId;
    private int attackerId;
    private long roleId;
    private int targetId;
    private List<FightObjectParam> attackerParam;
    private List<FightObjectParam> targetParamList;
    private List<RefreshPet> petList;
    private List<CombatAddObject> combatAddObjects;
    private byte hfType;
    
    public ActionDetailParam(FightObject effector, int type, int skillSerial) {
        this.type = -1;
        this.skillSerial = -1;
        this.colorId = 0;
        this.targetParamList = new ArrayList<>();
        this.type = type;
        this.skillSerial = skillSerial;
        this.attackerId = (int)effector.getId();
        this.addHfType(effector, skillSerial);
    }
    
    public ActionDetailParam(FightObject effector, int type, int skillSerial, int targetId) {
        this.type = -1;
        this.skillSerial = -1;
        this.colorId = 0;
        this.targetParamList = new ArrayList<>();
        this.type = type;
        this.skillSerial = skillSerial;
        this.attackerId = (int)effector.getId();
        this.targetId = targetId;
        this.addHfType(effector, skillSerial);
    }
    
    public ActionDetailParam(int type, int skillSerial, int effectorId, int targetId) {
        this.type = -1;
        this.skillSerial = -1;
        this.colorId = 0;
        this.targetParamList = new ArrayList<>();
        this.type = type;
        this.skillSerial = skillSerial;
        this.attackerId = effectorId;
        this.targetId = targetId;
    }
    
    public void addHfType(FightObject effector, int skillSerial) {
        if (effector != null && skillSerial > 0) {
            Optional<RoleSkill> opt = effector.getSkillIds().stream().filter(obj -> obj.getSkillId().equals(Integer.valueOf(skillSerial))).findFirst();
            if (opt.isPresent()) {
                RoleSkill roleSkill = (RoleSkill)opt.get();
                if (roleSkill != null) {
                    List<FieldByte> effects = roleSkill.getEffects();
                    for (FieldByte fieldByte : effects) {
                        if ((byte)fieldByte.getV() == 2 && fieldByte.getK() != null) {
                            this.hfType = (byte)((int)fieldByte.getK() % 10);
                            break;
                        }
                        continue;
                    }
                }
            }
        }
    }
    
    public void addAttackerParam(FightObjectParam fightObjectParam) {
        if (this.attackerParam == null) {
            this.attackerParam = new ArrayList<>();
        }
        this.attackerParam.add(fightObjectParam);
    }
    
    public FightObjectParam gainAttackerParam() {
        if (this.attackerParam == null || this.attackerParam.size() <= 0) {
            this.attackerParam = new ArrayList<>();
            FightObjectParam fightObjectParam = new FightObjectParam();
            fightObjectParam.setType(this.type);
            fightObjectParam.setObjId(this.attackerId);
            this.addAttackerParam(fightObjectParam);
            return fightObjectParam;
        }
        FightObjectParam fightObjectParam = (FightObjectParam)this.attackerParam.get(0);
        return fightObjectParam;
    }
    
    public FightObjectParam gainTargetParam(int targetId, boolean isCreate) {
        FightObjectParam targetParam = null;
        int i = this.targetParamList.size() - 1;
        while (i >= 0) {
            FightObjectParam param = (FightObjectParam)this.targetParamList.get(i);
            if (param != null && targetId == param.getObjId()) {
                targetParam = param;
                break;
            }
            --i;
        }
        if (isCreate && targetParam == null) {
            targetParam = new FightObjectParam();
            int size = this.targetParamList.size();
            targetParam.setOrder(++size);
            targetParam.setObjId(targetId);
            this.targetParamList.add(targetParam);
            return targetParam;
        }
        return targetParam;
    }
    
    public FightObjectParam gainTargetParamZ(int targetId, boolean isCreate) {
        FightObjectParam targetParam = null;
        for (FightObjectParam param : this.targetParamList) {
            if (param != null && targetId == param.getObjId()) {
                targetParam = param;
                break;
            }
            continue;
        }
        if (isCreate && targetParam == null) {
            targetParam = new FightObjectParam();
            int size = this.targetParamList.size();
            targetParam.setOrder(++size);
            targetParam.setObjId(targetId);
            this.targetParamList.add(targetParam);
            return targetParam;
        }
        return targetParam;
    }
    
    public List<FightObjectParam> gainTargetParamList(int targetId, boolean isCreate) {
        List<FightObjectParam> targetParams = new ArrayList<>();
        for (FightObjectParam param : this.targetParamList) {
            if (param != null && targetId == param.getObjId()) {
                targetParams.add(param);
            }
            else {
                continue;
            }
        }
        if (isCreate && targetParams.size() <= 0) {
            FightObjectParam targetParam = new FightObjectParam();
            int size = this.targetParamList.size();
            targetParam.setOrder(++size);
            targetParam.setObjId(targetId);
            targetParams.add(targetParam);
            return targetParams;
        }
        return targetParams;
    }
    
    @Override
    public String toString() {
        return "ActionDetailParam{type=" + this.type + ", skillSerial=" + this.skillSerial + ", attackerId=" + this.attackerId + ", targetId=" + this.targetId + ", attackerParam=" + this.attackerParam + ", targetParamList=" + this.targetParamList + '}';
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setSkillSerial(final int skillSerial) {
        this.skillSerial = skillSerial;
    }
    
    public void setColorId(final byte colorId) {
        this.colorId = colorId;
    }
    
    public void setAttackerId(final int attackerId) {
        this.attackerId = attackerId;
    }
    
    public void setRoleId(final long roleId) {
        this.roleId = roleId;
    }
    
    public void setTargetId(final int targetId) {
        this.targetId = targetId;
    }
    
    public void setAttackerParam(final List<FightObjectParam> attackerParam) {
        this.attackerParam = attackerParam;
    }
    
    public void setTargetParamList(final List<FightObjectParam> targetParamList) {
        this.targetParamList = targetParamList;
    }
    
    public void setPetList(final List<RefreshPet> petList) {
        this.petList = petList;
    }
    
    public void setCombatAddObjects(final List<CombatAddObject> combatAddObjects) {
        this.combatAddObjects = combatAddObjects;
    }
    
    public void setHfType(final byte hfType) {
        this.hfType = hfType;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getSkillSerial() {
        return this.skillSerial;
    }
    
    public byte getColorId() {
        return this.colorId;
    }
    
    public int getAttackerId() {
        return this.attackerId;
    }
    
    public long getRoleId() {
        return this.roleId;
    }
    
    public int getTargetId() {
        return this.targetId;
    }
    
    public List<FightObjectParam> getAttackerParam() {
        return this.attackerParam;
    }
    
    public List<FightObjectParam> getTargetParamList() {
        return this.targetParamList;
    }
    
    public List<RefreshPet> getPetList() {
        return this.petList;
    }
    
    public List<CombatAddObject> getCombatAddObjects() {
        return this.combatAddObjects;
    }
    
    public byte getHfType() {
        return this.hfType;
    }
    
    public ActionDetailParam() {
        this.type = -1;
        this.skillSerial = -1;
        this.colorId = 0;
        this.targetParamList = new ArrayList<>();
    }
    
    public ActionDetailParam(final int type, final int skillSerial, final byte colorId, final int attackerId, final long roleId, final int targetId, final List<FightObjectParam> attackerParam, final List<FightObjectParam> targetParamList, final List<RefreshPet> petList, final List<CombatAddObject> combatAddObjects, final byte hfType) {
        this.type = -1;
        this.skillSerial = -1;
        this.colorId = 0;
        this.targetParamList = new ArrayList<>();
        this.type = type;
        this.skillSerial = skillSerial;
        this.colorId = colorId;
        this.attackerId = attackerId;
        this.roleId = roleId;
        this.targetId = targetId;
        this.attackerParam = attackerParam;
        this.targetParamList = targetParamList;
        this.petList = petList;
        this.combatAddObjects = combatAddObjects;
        this.hfType = hfType;
    }
}
