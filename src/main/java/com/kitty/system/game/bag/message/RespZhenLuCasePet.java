
package com.kitty.system.game.bag.message;

import com.kitty.system.game.pet.domain.PetBase;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20681)
public class RespZhenLuCasePet extends Message
{
    private String id;
    private int serial;
    private int grow;
    private int grade;
    private int hp;
    private int mp;
    private int ap;
    private int sp;
    
    public RespZhenLuCasePet() {
    }
    
    public RespZhenLuCasePet(final Pet pet) {
        final PetBase petBase = pet.getPetBase();
        this.id = String.valueOf(pet.getPetId());
        this.serial = pet.getPetType();
        this.grow = petBase.getGrow_up();
        this.grade = petBase.getGrade_base();
        this.hp = petBase.getHp_base();
        this.mp = petBase.getMp_base();
        this.ap = petBase.getAp_base();
        this.sp = petBase.getSp_base();
    }
    
    public RespZhenLuCasePet(final String id, final int serial, final int grow, final int grade, final int hp, final int mp, final int ap, final int sp) {
        this.id = id;
        this.serial = serial;
        this.grow = grow;
        this.grade = grade;
        this.hp = hp;
        this.mp = mp;
        this.ap = ap;
        this.sp = sp;
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getGrow() {
        return this.grow;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public int getHp() {
        return this.hp;
    }
    
    public int getMp() {
        return this.mp;
    }
    
    public int getAp() {
        return this.ap;
    }
    
    public int getSp() {
        return this.sp;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setGrow(final int grow) {
        this.grow = grow;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    public void setHp(final int hp) {
        this.hp = hp;
    }
    
    public void setMp(final int mp) {
        this.mp = mp;
    }
    
    public void setAp(final int ap) {
        this.ap = ap;
    }
    
    public void setSp(final int sp) {
        this.sp = sp;
    }
}
