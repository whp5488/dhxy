package com.kitty.mina.filter;

import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.FireWallConfig;
import com.kitty.mina.FloodRecord;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.session.SessionProperties;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloodFilter extends IoFilterAdapter {
  private static Logger logger = LoggerFactory.getLogger(FloodFilter.class);
  
  public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
    FloodRecord record = getFloodRecordBy(session);
    long now = System.currentTimeMillis();
    long currSecond = NumberUtil.intValue(Long.valueOf(now / 1000L));
    long lastTime = record.getLastReceivedTime();
    int lastSecond = NumberUtil.intValue(Long.valueOf(lastTime / 1000L));
    tryToResetFloodTimes(now, record);
    if (currSecond == lastSecond) {
      int packageSum = record.addSecondReceivedPackage();
      if (isMessageTooFast(packageSum)) {
        int floodTimes = record.addFloodTimes();
        if (isMeetFloodStandard(floodTimes))
          logger.info("session窗口期洪水记录超过上限"); 
        record.setLastFloodTime(now);
        record.setReceivedPacksLastSecond(0);
      } 
    } else {
      record.setReceivedPacksLastSecond(0);
    } 
    record.setLastReceivedTime(now);
    nextFilter.messageReceived(session, message);
  }
  
  private static FloodRecord getFloodRecordBy(IoSession session) {
    SessionManager sessionMgr = SessionManager.INSTANCE;
    FloodRecord record = (FloodRecord)sessionMgr.getSessionAttr(session, SessionProperties.FLOOD, FloodRecord.class);
    if (record == null) {
      record = new FloodRecord();
      session.setAttribute(SessionProperties.FLOOD, record);
    } 
    return record;
  }
  
  private static void tryToResetFloodTimes(long now, FloodRecord record) {
    FireWallConfig config = (FireWallConfig)SpringUtils.getBean(FireWallConfig.class);
    long diffTime = now - record.getLastFloodTime();
    if (NumberUtil.intValue(Long.valueOf(diffTime / 1000L)) > config.getFloodWindowSeconds())
      record.setFloodTimes(0); 
  }
  
  private static boolean isMessageTooFast(int packageSum) {
    FireWallConfig config = (FireWallConfig)SpringUtils.getBean(FireWallConfig.class);
    return (packageSum >= config.getMaxPackagePerSecond());
  }
  
  private static boolean isMeetFloodStandard(int floodTimes) {
    FireWallConfig config = (FireWallConfig)SpringUtils.getBean(FireWallConfig.class);
    return (floodTimes > config.getMaxFloodTimes());
  }
}
