package com.kitty.mina.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.common.message.RespRoundDataEnd;
import com.kitty.system.game.chat.message.RespSyncMessage;
import com.kitty.system.game.enter.message.RespCombatFriends;
import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.game.fightnew.messages.old.RespCombatAddFriend;
import com.kitty.system.game.fightnew.messages.old.RespCombatAddOpponent;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "module")
@JsonSubTypes({@Type(value = RespUpdate.class, name = "65527"), @Type(value = RespSyncMessage.class, name = "64977"), @Type(value = RespCombatFriends.class, name = "65019"), @Type(value = RespCombatOpponets.class, name = "65017"), @Type(value = RespFightCommandInfo.class, name = "36889"), @Type(value = RespMsg.class, name = "8165"), @Type(value = RespRoundDataEnd.class, name = "65000"), @Type(value = RespNotifyMiscEx.class, name = "20481"), @Type(value = RespEndCombat.class, name = "3581"), @Type(value = RespCombatAddOpponent.class, name = "64993"), @Type(value = RespCombatAddFriend.class, name = "64995")})
public abstract class Message implements Serializable {
  @JsonIgnore
  @JsonProperty("module")
  public int getModule() {
    MessageMeta annotation = getClass().<MessageMeta>getAnnotation(MessageMeta.class);
    if (annotation != null)
      return annotation.module(); 
    return 0;
  }
}
