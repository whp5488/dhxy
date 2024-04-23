package com.kitty.system.game.pk.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.friend.message.vo.SearchFriend;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 45226)
public class RespSearchRole extends Message {
  private List<SearchFriend> list = new ArrayList<>();
}
