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
        this.list = new ArrayList<>();
    }
    
    public RespFiveExercise(List<FiedValue> list) {
        this.list = new ArrayList<>();
        this.list = list;
    }
    
    public RespFiveExercise(CardExercise exercise) {
        this.list = new ArrayList<>();
        List<FiedValue> objects = new ArrayList<>();
        objects.add(new FiedValue("grade", (byte)2, Integer.valueOf(exercise.getFiveGrade())));
        objects.add(new FiedValue("goldGrade", (byte)2, Integer.valueOf(exercise.getGoldGrade())));
        objects.add(new FiedValue("goldExp", (byte)3, Integer.valueOf(exercise.getGoldExp())));
        objects.add(new FiedValue("woodGrade", (byte)2, Integer.valueOf(exercise.getWoodGrade())));
        objects.add(new FiedValue("woodExp", (byte)3, Integer.valueOf(exercise.getWoodExp())));
        objects.add(new FiedValue("waterGrade", (byte)2, Integer.valueOf(exercise.getWaterGrade())));
        objects.add(new FiedValue("waterExp", (byte)3, Integer.valueOf(exercise.getWaterExp())));
        objects.add(new FiedValue("fireGrade", (byte)2, Integer.valueOf(exercise.getFireGrade())));
        objects.add(new FiedValue("fireExp", (byte)3, Integer.valueOf(exercise.getFireExp())));
        objects.add(new FiedValue("soilGrade", (byte)2, Integer.valueOf(exercise.getSoilGrade())));
        objects.add(new FiedValue("soilExp", (byte)3, Integer.valueOf(exercise.getSoilExp())));
        objects.add(new FiedValue("useCount", (byte)2, Integer.valueOf(exercise.getUseCount())));
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
        RespFiveExercise other = (RespFiveExercise)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFiveExercise;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFiveExercise(list=" + this.getList() + ")";
    }
}
