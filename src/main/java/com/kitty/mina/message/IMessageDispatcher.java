package com.kitty.mina.message;

import org.apache.mina.core.session.IoSession;

public interface IMessageDispatcher {
  void dispatch(IoSession paramIoSession, Message paramMessage);
}
