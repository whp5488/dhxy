package com.kitty.system.game.skill.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.vo.FieldByte;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 8312)
public class RespSkillEffects extends Message {
  private List<FieldByte> list;

}
