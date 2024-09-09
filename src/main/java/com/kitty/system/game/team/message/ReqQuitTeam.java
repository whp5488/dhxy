package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 26)
public class ReqQuitTeam extends Message
{
    public ReqQuitTeam() {
    }
}
