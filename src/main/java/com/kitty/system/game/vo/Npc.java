package com.kitty.system.game.vo;

import com.kitty.system.init.domain.SysDataTaskNpc;
import java.util.ArrayList;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.nutz.lang.util.NutMap;
import java.io.Serializable;

public class Npc implements Serializable
{
    private int id;
    private int role;
    private int map;
    private int x;
    private int y;
    private int icon;
    private String name;
    private String title;
    private int ns;
    private int level;
    private int warSerial;
    private int createTime;
    private int endTime;
    private NutMap roleInfo;
    private AtomicBoolean inFight;
    private int fightId;
    private String shipper;
    private int classify;
    private String talk;
    private int type;
    private int visible;
    private List<Integer> npcTypes;
    private long finish;
    private byte mirror;
    
    public boolean isInFight() {
        return this.inFight.get();
    }
    
    public void setInFight(boolean inFight) {
        this.inFight.set(inFight);
    }
    
    public boolean isShow() {
        return this.endTime <= 0 || Math.toIntExact(System.currentTimeMillis() / 1000L) <= this.endTime;
    }
    
    public Npc(SysNpcInfo npcInfo) {
        this.icon = 1;
        this.level = 0;
        this.warSerial = 0;
        this.createTime = Math.toIntExact(System.currentTimeMillis() / 1000L);
        this.endTime = -1;
        this.inFight = new AtomicBoolean();
        this.fightId = -1;
        this.classify = 0;
        this.type = 0;
        this.visible = 0;
        this.npcTypes = new ArrayList<>();
        this.finish = 0L;
        this.mirror = 0;
        this.id = (int)npcInfo.getSerial();
        this.map = (int)npcInfo.getPos().getMapId();
        this.x = (int)npcInfo.getPos().getX();
        this.y = (int)npcInfo.getPos().getY();
        this.icon = (int)npcInfo.getShape();
        this.name = npcInfo.getName();
        this.talk = npcInfo.getTalk();
        this.shipper = npcInfo.getShipper();
        this.npcTypes = npcInfo.getNpcTypes();
        this.classify = npcInfo.getClassify();
    }
    
    public Npc(SysDataTaskNpc npcInfo) {
        this.icon = 1;
        this.level = 0;
        this.warSerial = 0;
        this.createTime = Math.toIntExact(System.currentTimeMillis() / 1000L);
        this.endTime = -1;
        this.inFight = new AtomicBoolean();
        this.fightId = -1;
        this.classify = 0;
        this.type = 0;
        this.visible = 0;
        this.npcTypes = new ArrayList<>();
        this.finish = 0L;
        this.mirror = 0;
        this.id = npcInfo.getSerial();
        this.map = npcInfo.getScene();
        this.x = npcInfo.getX();
        this.y = npcInfo.getY();
        this.icon = npcInfo.getShape();
        this.name = npcInfo.getName();
        this.talk = npcInfo.getTalk();
        this.npcTypes = npcInfo.getTypes();
    }
    
    public Npc(int id, int role, int map, int x, int y, int icon, String name, String title, int ns, int type) {
        this.icon = 1;
        this.level = 0;
        this.warSerial = 0;
        this.createTime = Math.toIntExact(System.currentTimeMillis() / 1000L);
        this.endTime = -1;
        this.inFight = new AtomicBoolean();
        this.fightId = -1;
        this.classify = 0;
        this.type = 0;
        this.visible = 0;
        this.npcTypes = new ArrayList<>();
        this.finish = 0L;
        this.mirror = 0;
        this.id = id;
        this.role = role;
        this.map = map;
        this.x = x;
        this.y = y;
        this.icon = icon;
        this.name = name;
        this.title = title;
        this.ns = ns;
        this.type = type;
    }
    
    public Npc(int id, int role, int map, int x, int y, int icon, String name, String title, int ns, int level, int warSerial, int type) {
        this.icon = 1;
        this.level = 0;
        this.warSerial = 0;
        this.createTime = Math.toIntExact(System.currentTimeMillis() / 1000L);
        this.endTime = -1;
        this.inFight = new AtomicBoolean();
        this.fightId = -1;
        this.classify = 0;
        this.type = 0;
        this.visible = 0;
        this.npcTypes = new ArrayList<>();
        this.finish = 0L;
        this.mirror = 0;
        this.id = id;
        this.role = role;
        this.map = map;
        this.x = x;
        this.y = y;
        this.icon = icon;
        this.name = name;
        this.title = title;
        this.ns = ns;
        this.level = level;
        this.warSerial = warSerial;
        this.type = type;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getRole() {
        return this.role;
    }
    
    public int getMap() {
        return this.map;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public int getNs() {
        return this.ns;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getWarSerial() {
        return this.warSerial;
    }
    
    public int getCreateTime() {
        return this.createTime;
    }
    
    public int getEndTime() {
        return this.endTime;
    }
    
    public NutMap getRoleInfo() {
        return this.roleInfo;
    }
    
    public AtomicBoolean getInFight() {
        return this.inFight;
    }
    
    public int getFightId() {
        return this.fightId;
    }
    
    public String getShipper() {
        return this.shipper;
    }
    
    public int getClassify() {
        return this.classify;
    }
    
    public String getTalk() {
        return this.talk;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getVisible() {
        return this.visible;
    }
    
    public List<Integer> getNpcTypes() {
        return this.npcTypes;
    }
    
    public long getFinish() {
        return this.finish;
    }
    
    public byte getMirror() {
        return this.mirror;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setRole(final int role) {
        this.role = role;
    }
    
    public void setMap(final int map) {
        this.map = map;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setNs(final int ns) {
        this.ns = ns;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setWarSerial(final int warSerial) {
        this.warSerial = warSerial;
    }
    
    public void setCreateTime(final int createTime) {
        this.createTime = createTime;
    }
    
    public void setEndTime(final int endTime) {
        this.endTime = endTime;
    }
    
    public void setRoleInfo(final NutMap roleInfo) {
        this.roleInfo = roleInfo;
    }
    
    public void setFightId(final int fightId) {
        this.fightId = fightId;
    }
    
    public void setShipper(final String shipper) {
        this.shipper = shipper;
    }
    
    public void setClassify(final int classify) {
        this.classify = classify;
    }
    
    public void setTalk(final String talk) {
        this.talk = talk;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setVisible(final int visible) {
        this.visible = visible;
    }
    
    public void setNpcTypes(final List<Integer> npcTypes) {
        this.npcTypes = npcTypes;
    }
    
    public void setFinish(final long finish) {
        this.finish = finish;
    }
    
    public void setMirror(final byte mirror) {
        this.mirror = mirror;
    }

    public Npc() {
        this.icon = 1;
        this.level = 0;
        this.warSerial = 0;
        this.createTime = Math.toIntExact(System.currentTimeMillis() / 1000L);
        this.endTime = -1;
        this.inFight = new AtomicBoolean();
        this.fightId = -1;
        this.classify = 0;
        this.type = 0;
        this.visible = 0;
        this.npcTypes = new ArrayList<>();
        this.finish = 0L;
        this.mirror = 0;
    }
    
    public Npc(final int id, final int role, final int map, final int x, final int y, final int icon, final String name, final String title, final int ns, final int level, final int warSerial, final int createTime, final int endTime, final NutMap roleInfo, final AtomicBoolean inFight, final int fightId, final String shipper, final int classify, final String talk, final int type, final int visible, final List<Integer> npcTypes, final long finish, final byte mirror) {
        this.icon = 1;
        this.level = 0;
        this.warSerial = 0;
        this.createTime = Math.toIntExact(System.currentTimeMillis() / 1000L);
        this.endTime = -1;
        this.inFight = new AtomicBoolean();
        this.fightId = -1;
        this.classify = 0;
        this.type = 0;
        this.visible = 0;
        this.npcTypes = new ArrayList<>();
        this.finish = 0L;
        this.mirror = 0;
        this.id = id;
        this.role = role;
        this.map = map;
        this.x = x;
        this.y = y;
        this.icon = icon;
        this.name = name;
        this.title = title;
        this.ns = ns;
        this.level = level;
        this.warSerial = warSerial;
        this.createTime = createTime;
        this.endTime = endTime;
        this.roleInfo = roleInfo;
        this.inFight = inFight;
        this.fightId = fightId;
        this.shipper = shipper;
        this.classify = classify;
        this.talk = talk;
        this.type = type;
        this.visible = visible;
        this.npcTypes = npcTypes;
        this.finish = finish;
        this.mirror = mirror;
    }
}
