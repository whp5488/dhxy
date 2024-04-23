package com.kitty.mina.filter;

import com.kitty.common.utils.ConcurrentHashSet;
import com.kitty.mina.ModuleCounter;
import com.kitty.mina.ModuleCounterSchedule;
import com.kitty.mina.message.Message;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.session.SessionProperties;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.validation.constraints.NotNull;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleCounterFilter extends IoFilterAdapter {
  private static final Logger log = LoggerFactory.getLogger(ModuleCounterFilter.class);
  
  public ModuleCounterFilter() {
    R_MUST_CMD.add(Integer.valueOf(4274));
    R_MUST_CMD.add(Integer.valueOf(12290));
  }
  
  public static final Integer G_IDLE_TIME = Integer.valueOf(3000);
  
  public static final String BAN_SHELL = "iptables -I INPUT -s %s -j DROP";
  
  public static final Set<String> G_BAN_IP = (Set<String>)new ConcurrentHashSet(100000);
  
  public static final AtomicInteger G_UNCHECKED_PLAYER = new AtomicInteger(0);
  
  public static final Integer G_MAX_UNCHECKED_CONN = Integer.valueOf(100);
  
  public static final Integer G_MIN_PACKET_COUNT = Integer.valueOf(5);
  
  public static final Integer R_MIN_MODULE_COUNT = Integer.valueOf(2);
  
  public static final Set<Integer> R_MUST_CMD = new HashSet<>();
  
  public static void banIp(@NotNull IoSession session) {
    String clientIP = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
    ModuleCounterSchedule.disconnected(session);
    log.error("登录次数异常,加入黑名单 {}", clientIP);
    G_BAN_IP.add(clientIP);
    execShell(String.format("iptables -I INPUT -s %s -j DROP", new Object[] { clientIP }));
  }
  
  public static void execShell(String shell) {
    try {
      Runtime.getRuntime().exec(shell);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object packet) {
    Message message = (Message)packet;
    nextFilter.messageReceived(session, message);
  }
  
  public static ModuleCounter getCounter(IoSession session) {
    SessionManager sessionMgr = SessionManager.INSTANCE;
    ModuleCounter counter = (ModuleCounter)sessionMgr.getSessionAttr(session, SessionProperties.MODULE_COUNTER, ModuleCounter.class);
    if (counter == null) {
      counter = new ModuleCounter();
      counter.setLoginTime(System.currentTimeMillis());
      session.setAttribute(SessionProperties.MODULE_COUNTER, counter);
      int count = G_UNCHECKED_PLAYER.incrementAndGet();
      if (!canConnect())
        log.error("[MODULE_COUNTER] 当前变更为不可连接,当前未合法数 {}", Integer.valueOf(count)); 
    } 
    return counter;
  }
  
  public static void dumpUnchecked() {}
  
  public static void dumpAll() {}
  
  private static int count(int cmd, ModuleCounter counter) {
    counter.setLastPackTime(System.currentTimeMillis());
    counter.getCount().getAndIncrement();
    Map<Integer, AtomicInteger> counterMap = counter.getCounterMap();
    if (!counterMap.containsKey(Integer.valueOf(cmd)))
      synchronized (counterMap) {
        if (!counterMap.containsKey(Integer.valueOf(cmd)))
          counterMap.put(Integer.valueOf(cmd), new AtomicInteger(0)); 
      }  
    return ((AtomicInteger)counterMap.get(Integer.valueOf(cmd))).incrementAndGet();
  }
  
  public static boolean rule(ModuleCounter counter) {
    Map<Integer, AtomicInteger> counterMap = counter.getCounterMap();
    if (counterMap.size() < R_MIN_MODULE_COUNT.intValue())
      return false; 
    if (counter.getCount().get() < G_MIN_PACKET_COUNT.intValue())
      return false; 
    long cmdCount = R_MUST_CMD.parallelStream().filter(e -> !counterMap.containsKey(e)).count();
    return (cmdCount == 0L);
  }
  
  public static boolean canConnect() {
    return (G_UNCHECKED_PLAYER.get() < G_MAX_UNCHECKED_CONN.intValue());
  }
}
