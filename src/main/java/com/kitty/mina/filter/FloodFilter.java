
package com.kitty.mina.filter;

import org.slf4j.LoggerFactory;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.FireWallConfig;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.FloodRecord;
import com.kitty.common.utils.NumberUtil;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.filterchain.IoFilter;
import org.slf4j.Logger;
import org.apache.mina.core.filterchain.IoFilterAdapter;

public class FloodFilter extends IoFilterAdapter
{
    private static Logger logger;
    
    public void messageReceived(final IoFilter.NextFilter nextFilter, final IoSession session, final Object message) throws Exception {
        final FloodRecord record = getFloodRecordBy(session);
        final long now = System.currentTimeMillis();
        final long currSecond = NumberUtil.intValue(now / 1000L);
        final long lastTime = record.getLastReceivedTime();
        final int lastSecond = NumberUtil.intValue(lastTime / 1000L);
        tryToResetFloodTimes(now, record);
        if (currSecond == lastSecond) {
            final int packageSum = record.addSecondReceivedPackage();
            if (isMessageTooFast(packageSum)) {
                final int floodTimes = record.addFloodTimes();
                if (isMeetFloodStandard(floodTimes)) {
                    FloodFilter.logger.info("session窗口期洪水记录超过上限");
                }
                record.setLastFloodTime(now);
                record.setReceivedPacksLastSecond(0);
            }
        }
        else {
            record.setReceivedPacksLastSecond(0);
        }
        record.setLastReceivedTime(now);
        nextFilter.messageReceived(session, message);
    }
    
    private static FloodRecord getFloodRecordBy(final IoSession session) {
        final SessionManager sessionMgr = SessionManager.INSTANCE;
        FloodRecord record = sessionMgr.getSessionAttr(session, SessionProperties.FLOOD, FloodRecord.class);
        if (record == null) {
            record = new FloodRecord();
            session.setAttribute((Object)SessionProperties.FLOOD, (Object)record);
        }
        return record;
    }
    
    private static void tryToResetFloodTimes(final long now, final FloodRecord record) {
        final FireWallConfig config = SpringUtils.getBean(FireWallConfig.class);
        final long diffTime = now - record.getLastFloodTime();
        if (NumberUtil.intValue(diffTime / 1000L) > config.getFloodWindowSeconds()) {
            record.setFloodTimes(0);
        }
    }
    
    private static boolean isMessageTooFast(final int packageSum) {
        final FireWallConfig config = SpringUtils.getBean(FireWallConfig.class);
        return packageSum >= config.getMaxPackagePerSecond();
    }
    
    private static boolean isMeetFloodStandard(final int floodTimes) {
        final FireWallConfig config = SpringUtils.getBean(FireWallConfig.class);
        return floodTimes > config.getMaxFloodTimes();
    }
    
    static {
        FloodFilter.logger = LoggerFactory.getLogger(FloodFilter.class);
    }
}
