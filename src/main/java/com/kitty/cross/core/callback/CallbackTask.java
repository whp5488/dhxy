
package com.kitty.cross.core.callback;

import java.util.concurrent.locks.Condition;
import com.kitty.cross.core.client.C2SSessionPoolFactory;
import java.util.concurrent.TimeUnit;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.concurrent.locks.ReentrantLock;
import com.kitty.cross.core.client.CCSession;
import com.kitty.mina.message.Message;
import java.util.concurrent.Callable;

public class CallbackTask implements Callable<Message>
{
    private CCSession session;
    private Message request;
    
    public static CallbackTask valueOf(final CCSession session, final Message message) {
        final CallbackTask task = new CallbackTask();
        task.request = message;
        task.session = session;
        return task;
    }
    
    @Override
    public Message call() throws Exception {
        try {
            final CReqCallBack reqCallBack = (CReqCallBack)this.request;
            final int index = CallBack.nextMsgId();
            reqCallBack.setIndex(index);
            reqCallBack.serialize();
            this.session.sendMessage(reqCallBack);
            final ReentrantLock lock = new ReentrantLock();
            final Condition condition = lock.newCondition();
            final long maxTimeOut = 5000L;
            final long startTime = System.currentTimeMillis();
            final CallBackService callBackService = SpringUtils.getCallBackService();
            while (System.currentTimeMillis() - startTime <= maxTimeOut) {
                final CallBack c = callBackService.removeCallBack(index);
                if (c != null) {
                    return c.getData();
                }
                lock.lockInterruptibly();
                try {
                    condition.await(10L, TimeUnit.MILLISECONDS);
                }
                catch (final Exception ex) {}
                finally {
                    lock.unlock();
                }
            }
        }
        finally {
            SpringUtils.getBean(C2SSessionPoolFactory.class).returnSession(this.session);
        }
        return null;
    }
}
