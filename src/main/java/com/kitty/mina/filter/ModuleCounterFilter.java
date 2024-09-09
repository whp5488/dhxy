package com.kitty.mina.filter;

import java.util.HashSet;
import com.kitty.common.utils.ConcurrentHashSet;
import org.slf4j.LoggerFactory;
import java.util.Map;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.ModuleCounter;
import com.kitty.mina.message.Message;
import org.apache.mina.core.filterchain.IoFilter;
import com.kitty.mina.ModuleCounterSchedule;
import java.net.InetSocketAddress;
import org.apache.mina.core.session.IoSession;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;
import org.slf4j.Logger;
import org.apache.mina.core.filterchain.IoFilterAdapter;

public class ModuleCounterFilter extends IoFilterAdapter
{
    private static final Logger log;
    public static final Integer G_IDLE_TIME;
    public static final String BAN_SHELL = "iptables -I INPUT -s %s -j DROP";
    public static final Set<String> G_BAN_IP;
    public static final AtomicInteger G_UNCHECKED_PLAYER;
    public static final Integer G_MAX_UNCHECKED_CONN;
    public static final Integer G_MIN_PACKET_COUNT;
    public static final Integer R_MIN_MODULE_COUNT;
    public static final Set<Integer> R_MUST_CMD;
    
    public ModuleCounterFilter() {
        ModuleCounterFilter.R_MUST_CMD.add(Integer.valueOf(4274));
        ModuleCounterFilter.R_MUST_CMD.add(Integer.valueOf(1833));
    }
    
    public static void banIp(IoSession session) {
        if (session == null) {
            return;
        }
        String clientIP = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
        ModuleCounterSchedule.disconnected(session);
        ModuleCounterFilter.log.error("登录次数异常,加入黑名单 {}", clientIP);
        ModuleCounterFilter.G_BAN_IP.add(clientIP);
        execShell(String.format("iptables -I INPUT -s %s -j DROP", clientIP));
        return;
    }
    
    public static void execShell(String shell) {
        try {
            Runtime.getRuntime().exec(shell);
        }
        catch (Exception e) {
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
            int count = ModuleCounterFilter.G_UNCHECKED_PLAYER.incrementAndGet();
            if (!canConnect()) {
                ModuleCounterFilter.log.error("[MODULE_COUNTER] 当前变更为不可连接,当前未合法数 {}", Integer.valueOf(count));
            }
        }
        return counter;
    }
    
    public static void dumpUnchecked() {
    }
    
    public static void dumpAll() {
    }
    
    private static int count(int cmd, ModuleCounter counter) {
        counter.setLastPackTime(System.currentTimeMillis());
        counter.getCount().getAndIncrement();
        Map<Integer, AtomicInteger> counterMap = counter.getCounterMap();
        if (!counterMap.containsKey(Integer.valueOf(cmd))) {
            synchronized (counterMap) {
                if (!counterMap.containsKey(Integer.valueOf(cmd))) {
                    counterMap.put(Integer.valueOf(cmd), new AtomicInteger(0));
                }
            }
        }
        return ((AtomicInteger)counterMap.get(Integer.valueOf(cmd))).incrementAndGet();
    }
    
    public static boolean rule(ModuleCounter counter) {
        Map<Integer, AtomicInteger> counterMap = counter.getCounterMap();
        if (counterMap.size() < (int)ModuleCounterFilter.R_MIN_MODULE_COUNT) {
            return false;
        }
        if (counter.getCount().get() < (long)(int)ModuleCounterFilter.G_MIN_PACKET_COUNT) {
            return false;
        }
        long cmdCount = ModuleCounterFilter.R_MUST_CMD.parallelStream().filter(e -> !counterMap.containsKey(e)).count();
        return cmdCount == 0L;
    }
    
    public static boolean canConnect() {
        return ModuleCounterFilter.G_UNCHECKED_PLAYER.get() < (int)ModuleCounterFilter.G_MAX_UNCHECKED_CONN;
    }
    
    static {
        log = LoggerFactory.getLogger(ModuleCounterFilter.class);
        G_IDLE_TIME = Integer.valueOf(3000);
        G_BAN_IP = new ConcurrentHashSet(100000);
        G_UNCHECKED_PLAYER = new AtomicInteger(0);
        G_MAX_UNCHECKED_CONN = Integer.valueOf(100);
        G_MIN_PACKET_COUNT = Integer.valueOf(5);
        R_MIN_MODULE_COUNT = Integer.valueOf(2);
        R_MUST_CMD = new HashSet();
    }
}
