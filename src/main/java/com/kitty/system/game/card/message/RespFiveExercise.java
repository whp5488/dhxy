package com.kitty.system.game.card.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.card.domain.CardExercise;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 45219)
public class RespFiveExercise extends Message {
  public void setList(List<FiedValue> list) {
    this.list = list;
  }

  private List<FiedValue> list = new ArrayList<>();
  
  public List<FiedValue> getList() {
    return this.list;
  }
  
  public RespFiveExercise(List<FiedValue> list) {
    this.list = list;
  }
  
  public RespFiveExercise(CardExercise exercise) {
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
  
  public RespFiveExercise() {}
}
