package com.kitty.cross.core.callback;

import com.kitty.mina.message.Message;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class CallBackService {
  private ConcurrentMap<Integer, CallBack> callbacks = new ConcurrentHashMap<>();
  
  public void fillCallBack(int index, Message message) {
    CallBack callBack = new CallBack();
    callBack.setIndex(index);
    callBack.setData(message);
    this.callbacks.put(index, callBack);
  }
  
  public CallBack removeCallBack(int index) {
    return this.callbacks.remove(index);
  }
}
