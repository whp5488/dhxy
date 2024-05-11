
package com.kitty.mina.message;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnore;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.system.game.fightnew.messages.old.RespCombatAddFriend;
import com.kitty.system.game.fightnew.messages.old.RespCombatAddOpponent;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.common.message.RespRoundDataEnd;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.enter.message.RespCombatFriends;
import com.kitty.system.game.chat.message.RespSyncMessage;
import com.kitty.system.game.enter.message.RespUpdate;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "module")
@JsonSubTypes({ @JsonSubTypes.Type(value = RespUpdate.class, name = "65527"), @JsonSubTypes.Type(value = RespSyncMessage.class, name = "64977"), @JsonSubTypes.Type(value = RespCombatFriends.class, name = "65019"), @JsonSubTypes.Type(value = RespCombatOpponets.class, name = "65017"), @JsonSubTypes.Type(value = RespFightCommandInfo.class, name = "36889"), @JsonSubTypes.Type(value = RespMsg.class, name = "8165"), @JsonSubTypes.Type(value = RespRoundDataEnd.class, name = "65000"), @JsonSubTypes.Type(value = RespNotifyMiscEx.class, name = "20481"), @JsonSubTypes.Type(value = RespEndCombat.class, name = "3581"), @JsonSubTypes.Type(value = RespCombatAddOpponent.class, name = "64993"), @JsonSubTypes.Type(value = RespCombatAddFriend.class, name = "64995") })
public abstract class Message implements Serializable
{
    @JsonIgnore
    @JsonProperty("module")
    public int getModule() {
        final MessageMeta annotation = this.getClass().getAnnotation(MessageMeta.class);
        if (annotation != null) {
            return annotation.module();
        }
        return 0;
    }
}
