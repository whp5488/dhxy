
package com.kitty.cross.login.service;

import com.kitty.common.utils.ZipUtil;
import org.springframework.stereotype.Service;

@Service
public class PlayerRemoteTransferService
{
    public void receiverPlayerData(final String fromIp, final int fromServer, final String playerJson, final String authJson) {
        final String data = ZipUtil.uncompress(playerJson);
    }
}
