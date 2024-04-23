package com.kitty.cross.login.service;

import com.kitty.common.utils.ZipUtil;
import org.springframework.stereotype.Service;

@Service
public class PlayerRemoteTransferService {
  public void receiverPlayerData(String fromIp, int fromServer, String playerJson, String authJson) {
    String data = ZipUtil.uncompress(playerJson);
  }
}
