
package com.kitty.common.start;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.transport.socket.nio.NioSession;
import java.util.concurrent.Executor;
import java.net.InetSocketAddress;
import org.apache.mina.core.service.IoHandler;
import com.kitty.mina.message.IMessageDispatcher;
import com.kitty.mina.ServerSocketIoHandler;
import com.kitty.mina.MessageDispatcher;
import com.kitty.mina.filter.HeartbeatFilter;
import com.kitty.mina.filter.ChatMessageFilter;
import com.kitty.mina.filter.MessageTraceFilter;
import com.kitty.mina.filter.ModuleEntranceFilter;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import com.kitty.mina.codec.SerializerHelper;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import java.nio.channels.spi.SelectorProvider;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.transport.socket.nio.NioProcessor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import com.kitty.common.thread.NamedThreadFactory;
import org.apache.mina.core.buffer.IoBufferAllocator;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.buffer.IoBuffer;
import com.kitty.mina.task.TaskHandlerContext;
import com.kitty.mina.message.MessageFactory;
import org.slf4j.LoggerFactory;
import com.kitty.common.utils.spring.ScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

@Component
public class GameServer implements CommandLineRunner
{
    private final Logger logger;
    private static SocketAcceptor acceptor;
    @Autowired
    ServerService serverService;
    @Autowired
    ScheduledService scheduledService;
    
    public GameServer() {
        this.logger = LoggerFactory.getLogger(GameServer.class);
    }
    
    public void run(final String... var1) throws Exception {
        this.initGameServer();
    }
    
    private void initGameServer() throws Exception {
        this.logger.warn("Starting GameServer ......");
        MessageFactory.INSTANCE.initMessagePool("com.kitty");
        TaskHandlerContext.INSTANCE.initialize();
        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator((IoBufferAllocator)new SimpleBufferAllocator());
        final Executor executor = Executors.newCachedThreadPool(new NamedThreadFactory("SOCKET-IO-PROCESSOR"));
        int coreSize = Runtime.getRuntime().availableProcessors();
        if (coreSize > 16) {
            coreSize = 16;
        }
        final SimpleIoProcessorPool<NioSession> processor = (SimpleIoProcessorPool<NioSession>)new SimpleIoProcessorPool(NioProcessor.class, executor, coreSize, null);
        (GameServer.acceptor = (SocketAcceptor)new NioSocketAcceptor((IoProcessor)processor)).setReuseAddress(true);
        GameServer.acceptor.getSessionConfig().setAll((IoSessionConfig)this.getSessionConfig());
        final DefaultIoFilterChainBuilder filterChain = GameServer.acceptor.getFilterChain();
        filterChain.addLast("codec", (IoFilter)new ProtocolCodecFilter((ProtocolCodecFactory)SerializerHelper.getInstance().getCodecFactory()));
        filterChain.addLast("moduleEntrance", (IoFilter)new ModuleEntranceFilter());
        filterChain.addLast("msgTrace", (IoFilter)new MessageTraceFilter());
        filterChain.addLast("chat", (IoFilter)new ChatMessageFilter());
        filterChain.addLast("heartBeat", (IoFilter)new HeartbeatFilter());
        GameServer.acceptor.setHandler((IoHandler)new ServerSocketIoHandler(MessageDispatcher.getInstance()));
        GameServer.acceptor.setBacklog(10000);
        try {
            GameServer.acceptor.setDefaultLocalAddress(new InetSocketAddress(this.serverService.getPort()));
            GameServer.acceptor.bind();
        }
        catch (final Exception e) {
            System.out.println("e = " + e);
        }
        this.logger.warn("socket server start at port:{},", this.serverService.getPort());
        this.logger.warn("start GameServer success!!");
    }
    
    private SocketSessionConfig getSessionConfig() {
        final SocketSessionConfig config = new DefaultSocketSessionConfig();
        config.setKeepAlive(false);
        config.setTcpNoDelay(true);
        config.setReuseAddress(true);
        config.setReadBufferSize(2048);
        config.setIdleTime(IdleStatus.BOTH_IDLE, 60);
        config.setSoLinger(0);
        return config;
    }
    
    public SocketAcceptor getAcceptor() {
        return GameServer.acceptor;
    }
}
