
package com.kitty.system.queue;

import com.kitty.logs.LoggerUtils;
import com.kitty.system.common.utils.SenderUtils;
import javax.annotation.PostConstruct;
import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.BlockingUniqueQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import com.kitty.system.game.chat.message.RespChat;
import java.util.concurrent.BlockingQueue;
import org.springframework.stereotype.Component;

@Component
public class BroadcastThread
{
    private static volatile BroadcastThread instance;
    private BlockingQueue<RespChat> queue;
    private final AtomicBoolean run;
    
    public BroadcastThread() {
        this.queue = new BlockingUniqueQueue<RespChat>();
        this.run = new AtomicBoolean(true);
    }
    
    public static BroadcastThread getInstance() {
        return BroadcastThread.instance;
    }
    
    @PostConstruct
    private void init() {
        new NamedThreadFactory("broadcast-service").newThread(new Worker()).start();
        BroadcastThread.instance = this;
    }
    
    public void add2Queue(final RespChat entity) {
        this.queue.add(entity);
    }
    
    public void add2Queue(final String msg) {
        final RespChat res = new RespChat();
        res.setMsg(msg);
        res.setType(19);
        this.queue.add(res);
    }
    
    private class Worker implements Runnable
    {
        @Override
        public void run() {
            while (BroadcastThread.this.run.get()) {
                try {
                    final RespChat take = BroadcastThread.this.queue.take();
                    SenderUtils.pushChatMessage(take);
                    Thread.sleep(10000L);
                }
                catch (final Exception e) {
                    LoggerUtils.error("系统播报异常", e);
                }
            }
        }
    }
}
