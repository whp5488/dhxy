package com.kitty.mina.session;

import org.apache.mina.core.session.AttributeKey;

public interface SessionProperties
{
    public static final AttributeKey CODEC_CONTEXT = new AttributeKey(SessionProperties.class, "CONTEXT_KEY");
    public static final AttributeKey DISTRIBUTE_KEY = new AttributeKey(SessionProperties.class, "DISTRIBUTE_KEY");
    public static final AttributeKey PLAYER_ID = new AttributeKey(SessionProperties.class, "PLAYER_ID");
    public static final AttributeKey PLAYER_UID = new AttributeKey(SessionProperties.class, "PLAYER_UID");
    public static final AttributeKey PLAYER = new AttributeKey(SessionProperties.class, "PLAYER");
    public static final AttributeKey PLAYER_NAME = new AttributeKey(SessionProperties.class, "PLAYER_NAME");
    public static final AttributeKey FLOOD = new AttributeKey(SessionProperties.class, "FLOOD");
    public static final AttributeKey MODULE_COUNTER = new AttributeKey(SessionProperties.class, "MODULE_COUNTER");
    public static final AttributeKey ACCOUNT_SID = new AttributeKey(SessionProperties.class, "ACCOUNT_SID");
    public static final AttributeKey CLIENT_STATUS = new AttributeKey(SessionProperties.class, "CLIENT_STATUS");
    public static final AttributeKey HEARTBEAT_MAP = new AttributeKey(SessionProperties.class, "HEARTBEAT_MAP");
}
