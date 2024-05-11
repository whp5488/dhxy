
package com.kitty.system.game.market.message;

import com.kitty.system.game.market.message.vo.HorseSkillCard;
import com.kitty.system.game.market.message.vo.PetEquipCard;
import com.kitty.system.game.vo.FieldProp;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45105)
public class RespMarketPetCard extends Message
{
    private String id;
    private int roleId;
    private int serial;
    private List<FiedValue> fields;
    private List<PetSkill> skills;
    private List<FieldProp> proList;
    private List<PetEquipCard> equips;
    private List<HorseSkillCard> horseSkillCard;
    private byte status;
    private int endTime;
    
    public RespMarketPetCard() {
        this.status = 2;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setFields(final List<FiedValue> fields) {
        this.fields = fields;
    }
    
    public void setSkills(final List<PetSkill> skills) {
        this.skills = skills;
    }
    
    public void setProList(final List<FieldProp> proList) {
        this.proList = proList;
    }
    
    public void setEquips(final List<PetEquipCard> equips) {
        this.equips = equips;
    }
    
    public void setHorseSkillCard(final List<HorseSkillCard> horseSkillCard) {
        this.horseSkillCard = horseSkillCard;
    }
    
    public void setStatus(final byte status) {
        this.status = status;
    }
    
    public void setEndTime(final int endTime) {
        this.endTime = endTime;
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public List<FiedValue> getFields() {
        return this.fields;
    }
    
    public List<PetSkill> getSkills() {
        return this.skills;
    }
    
    public List<FieldProp> getProList() {
        return this.proList;
    }
    
    public List<PetEquipCard> getEquips() {
        return this.equips;
    }
    
    public List<HorseSkillCard> getHorseSkillCard() {
        return this.horseSkillCard;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public int getEndTime() {
        return this.endTime;
    }
}
