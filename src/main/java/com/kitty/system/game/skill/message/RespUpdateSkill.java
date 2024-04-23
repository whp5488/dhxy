package com.kitty.system.game.skill.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.skill.message.vo.Skill;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 32747)
public class RespUpdateSkill extends Message {
  private int id;
  
  private List<Skill> list;

}
