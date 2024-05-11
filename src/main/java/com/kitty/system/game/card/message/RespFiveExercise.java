
package com.kitty.system.game.card.message;

import com.kitty.system.game.card.domain.CardExercise;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45219)
public class RespFiveExercise extends Message
{
    private List<FiedValue> list;
    
    public RespFiveExercise() {
        this.list = new ArrayList<FiedValue>();
    }
    
    public RespFiveExercise(final List<FiedValue> list) {
        this.list = new ArrayList<FiedValue>();
        this.list = list;
    }
    
    public RespFiveExercise(final CardExercise exercise) {
        this.list = new ArrayList<FiedValue>();
        final List<FiedValue> objects = new ArrayList<FiedValue>();
        objects.add(new FiedValue("grade", (byte)2, exercise.getFiveGrade()));
        objects.add(new FiedValue("goldGrade", (byte)2, exercise.getGoldGrade()));
        objects.add(new FiedValue("goldExp", (byte)3, exercise.getGoldExp()));
        objects.add(new FiedValue("woodGrade", (byte)2, exercise.getWoodGrade()));
        objects.add(new FiedValue("woodExp", (byte)3, exercise.getWoodExp()));
        objects.add(new FiedValue("waterGrade", (byte)2, exercise.getWaterGrade()));
        objects.add(new FiedValue("waterExp", (byte)3, exercise.getWaterExp()));
        objects.add(new FiedValue("fireGrade", (byte)2, exercise.getFireGrade()));
        objects.add(new FiedValue("fireExp", (byte)3, exercise.getFireExp()));
        objects.add(new FiedValue("soilGrade", (byte)2, exercise.getSoilGrade()));
        objects.add(new FiedValue("soilExp", (byte)3, exercise.getSoilExp()));
        objects.add(new FiedValue("useCount", (byte)2, exercise.getUseCount()));
        this.list = objects;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespFiveExercise)) {
            return false;
        }
        final RespFiveExercise other = (RespFiveExercise)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFiveExercise;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFiveExercise(list=" + this.getList() + ")";
    }
}
