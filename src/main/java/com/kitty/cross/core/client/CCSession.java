package com.kitty.cross.core.client;

import org.apache.mina.core.future.ConnectFuture;
import java.net.InetSocketAddress;
import com.kitty.mina.message.Message;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import com.kitty.cross.core.codec.CrossSerializerHelper;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.apache.mina.core.session.IoSession;
import com.kitty.cross.core.server.CMessageDispatcher;
import java.util.concurrent.atomic.AtomicInteger;

public class CCSession
{
    private int id;
    private static AtomicInteger idFactory;
    private String ipAddr;
    private int port;
    private CMessageDispatcher dispatcher;
    private IoSession wrapper;
    
    public CCSession() {
    }
    
    public static CCSession valueOf(String ip, int port, CMessageDispatcher dispatcher) {
        CCSession cSession = new CCSession();
        cSession.ipAddr = ip;
        cSession.port = port;
        cSession.id = CCSession.idFactory.getAndIncrement();
        cSession.dispatcher = dispatcher;
        return cSession;
    }
    
    public void buildConnection() {
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(CrossSerializerHelper.getInstance().getCodecFactory()));
        connector.setHandler(new IoHandlerAdapter() {
            public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            }
            
            public void messageReceived(IoSession session, Object data) throws Exception {
                Message message = (Message)data;
                CCSession.this.dispatcher.clientDispatch(CCSession.this, message);
            }
        });
        System.out.println("开始连接跨服服务器端口" + this.port);
        ConnectFuture future = connector.connect(new InetSocketAddress(this.port));
        future.awaitUninterruptibly();
        IoSession session = future.getSession();
        this.wrapper = session;
    }
    
    public String getIpAddr() {
        return this.ipAddr;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public IoSession getWrapper() {
        return this.wrapper;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void sendMessage(Message message) {
        this.wrapper.write(message);
    }
    
    static {
        CCSession.idFactory = new AtomicInteger();
    }
}
