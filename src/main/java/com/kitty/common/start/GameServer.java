package com.kitty.common.start;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.common.utils.spring.ScheduledService;
import com.kitty.mina.MessageDispatcher;
import com.kitty.mina.ServerSocketIoHandler;
import com.kitty.mina.codec.SerializerHelper;
import com.kitty.mina.filter.ChatMessageFilter;
import com.kitty.mina.filter.HeartbeatFilter;
import com.kitty.mina.filter.MessageTraceFilter;
import com.kitty.mina.filter.ModuleEntranceFilter;
import com.kitty.mina.message.IMessageDispatcher;
import com.kitty.mina.message.MessageFactory;
import com.kitty.mina.task.TaskHandlerContext;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.IoBufferAllocator;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GameServer implements CommandLineRunner {
  private final Logger logger = LoggerFactory.getLogger(GameServer.class);
  
  private static SocketAcceptor acceptor;
  
  @Autowired
  ServerService serverService;
  
  @Autowired
  ScheduledService scheduledService;
  
  public void run(String... var1) throws Exception {
    initGameServer();
  }
  
  private void initGameServer() throws Exception {
    this.logger.warn("Starting GameServer ......");
    MessageFactory.INSTANCE.initMessagePool("com.kitty");
    TaskHandlerContext.INSTANCE.initialize();
    IoBuffer.setUseDirectBuffer(false);
    IoBuffer.setAllocator((IoBufferAllocator)new SimpleBufferAllocator());
    Executor executor = Executors.newCachedThreadPool((ThreadFactory)new NamedThreadFactory("SOCKET-IO-PROCESSOR"));
    int coreSize = Runtime.getRuntime().availableProcessors();
    if (coreSize > 16)
      coreSize = 16; 
    SimpleIoProcessorPool<NioSession> processor = new SimpleIoProcessorPool(NioProcessor.class, executor, coreSize, null);
    acceptor = (SocketAcceptor)new NioSocketAcceptor((IoProcessor)processor);
    acceptor.setReuseAddress(true);
    acceptor.getSessionConfig().setAll((IoSessionConfig)getSessionConfig());
    DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
    filterChain.addLast("codec", (IoFilter)new ProtocolCodecFilter((ProtocolCodecFactory)SerializerHelper.getInstance().getCodecFactory()));
    filterChain.addLast("moduleEntrance", (IoFilter)new ModuleEntranceFilter());
    filterChain.addLast("msgTrace", (IoFilter)new MessageTraceFilter());
    filterChain.addLast("chat", (IoFilter)new ChatMessageFilter());
    filterChain.addLast("heartBeat", (IoFilter)new HeartbeatFilter());
    acceptor.setHandler((IoHandler)new ServerSocketIoHandler((IMessageDispatcher)MessageDispatcher.getInstance()));
    acceptor.setBacklog(10000);
    try {
      acceptor.setDefaultLocalAddress(new InetSocketAddress(this.serverService.getPort()));
      acceptor.bind();
    } catch (Exception e) {
      System.out.println("e = " + e);
    } 
    this.logger.warn("socket server start at port:{},", this.serverService.getPort());
    this.logger.warn("start GameServer success!!");
  }
  
  private SocketSessionConfig getSessionConfig() {
    DefaultSocketSessionConfig defaultSocketSessionConfig = new DefaultSocketSessionConfig();
    defaultSocketSessionConfig.setKeepAlive(false);
    defaultSocketSessionConfig.setTcpNoDelay(true);
    defaultSocketSessionConfig.setReuseAddress(true);
    defaultSocketSessionConfig.setReadBufferSize(2048);
    defaultSocketSessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, 60);
    defaultSocketSessionConfig.setSoLinger(0);
    return (SocketSessionConfig)defaultSocketSessionConfig;
  }
  
  public SocketAcceptor getAcceptor() {
    return acceptor;
  }
}
