package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 53727)
public class RespQuestionHallRank extends Message {
  public String toString() {
    return "RespQuestionHallRank(list=" + getList() + ")";
  }

  private List<String> list = new ArrayList<>();

}
