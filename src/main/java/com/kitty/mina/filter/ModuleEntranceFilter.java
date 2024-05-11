
package com.kitty.mina.filter;

import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.mina.message.MessageFactory;
import com.kitty.mina.message.Message;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;

public class ModuleEntranceFilter extends IoFilterAdapter
{
    public void messageReceived(final IoFilter.NextFilter nextFilter, final IoSession session, final Object packet) throws Exception {
        final Message message = (Message)packet;
        final String messageId = MessageFactory.INSTANCE.getMessageId(message.getClass());
        if (NetGateKeeper.getInstance().canVisit(messageId)) {
            nextFilter.messageReceived(session, (Object)message);
        }
        else {
            MessagePusher.pushMessage(session, new RespMsg("This function is to be developed(Code:" + messageId + ")..."));
        }
    }
}
