
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
import javax.validation.constraints.NotNull;
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
        ModuleCounterFilter.R_MUST_CMD.add(4274);
        ModuleCounterFilter.R_MUST_CMD.add(12290);
    }
    
    public static void banIp(@NotNull final IoSession session) {
        final String clientIP = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
        ModuleCounterSchedule.disconnected(session);
        ModuleCounterFilter.log.error("登录次数异常,加入黑名单 {}", (Object)clientIP);
        ModuleCounterFilter.G_BAN_IP.add(clientIP);
        execShell(String.format("iptables -I INPUT -s %s -j DROP", clientIP));
    }
    
    public static void execShell(final String shell) {
        try {
            Runtime.getRuntime().exec(shell);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public void messageReceived(final IoFilter.NextFilter nextFilter, final IoSession session, final Object packet) {
        final Message message = (Message)packet;
        nextFilter.messageReceived(session, (Object)message);
    }
    
    public static ModuleCounter getCounter(final IoSession session) {
        final SessionManager sessionMgr = SessionManager.INSTANCE;
        ModuleCounter counter = sessionMgr.getSessionAttr(session, SessionProperties.MODULE_COUNTER, ModuleCounter.class);
        if (counter == null) {
            counter = new ModuleCounter();
            counter.setLoginTime(System.currentTimeMillis());
            session.setAttribute((Object)SessionProperties.MODULE_COUNTER, (Object)counter);
            final int count = ModuleCounterFilter.G_UNCHECKED_PLAYER.incrementAndGet();
            if (!canConnect()) {
                ModuleCounterFilter.log.error("[MODULE_COUNTER] 当前变更为不可连接,当前未合法数 {}", (Object)count);
            }
        }
        return counter;
    }
    
    public static void dumpUnchecked() {
    }
    
    public static void dumpAll() {
    }
    
    private static int count(final int cmd, final ModuleCounter counter) {
        counter.setLastPackTime(System.currentTimeMillis());
        counter.getCount().getAndIncrement();
        final Map<Integer, AtomicInteger> counterMap = counter.getCounterMap();
        if (!counterMap.containsKey(cmd)) {
            synchronized (counterMap) {
                if (!counterMap.containsKey(cmd)) {
                    counterMap.put(cmd, new AtomicInteger(0));
                }
            }
        }
        return counterMap.get(cmd).incrementAndGet();
    }
    
    public static boolean rule(final ModuleCounter counter) {
        final Map<Integer, AtomicInteger> counterMap = counter.getCounterMap();
        if (counterMap.size() < ModuleCounterFilter.R_MIN_MODULE_COUNT) {
            return false;
        }
        if (counter.getCount().get() < ModuleCounterFilter.G_MIN_PACKET_COUNT) {
            return false;
        }
        final long cmdCount = ModuleCounterFilter.R_MUST_CMD.parallelStream().filter(e -> !counterMap.containsKey(e)).count();
        return cmdCount == 0L;
    }
    
    public static boolean canConnect() {
        return ModuleCounterFilter.G_UNCHECKED_PLAYER.get() < ModuleCounterFilter.G_MAX_UNCHECKED_CONN;
    }
    
    static {
        log = LoggerFactory.getLogger(ModuleCounterFilter.class);
        G_IDLE_TIME = 3000;
        G_BAN_IP = new ConcurrentHashSet<String>(100000);
        G_UNCHECKED_PLAYER = new AtomicInteger(0);
        G_MAX_UNCHECKED_CONN = 100;
        G_MIN_PACKET_COUNT = 5;
        R_MIN_MODULE_COUNT = 2;
        R_MUST_CMD = new HashSet<Integer>();
    }
}
