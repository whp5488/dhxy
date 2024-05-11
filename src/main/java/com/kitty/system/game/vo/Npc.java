
package com.kitty.system.game.vo;

import java.beans.ConstructorProperties;
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
    
    public void setInFight(final boolean inFight) {
        this.inFight.set(inFight);
    }
    
    public boolean isShow() {
        return this.endTime <= 0 || Math.toIntExact(System.currentTimeMillis() / 1000L) <= this.endTime;
    }
    
    public Npc(final SysNpcInfo npcInfo) {
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
        this.npcTypes = new ArrayList<Integer>();
        this.finish = 0L;
        this.mirror = 0;
        this.id = npcInfo.getSerial();
        this.map = npcInfo.getPos().getMapId();
        this.x = npcInfo.getPos().getX();
        this.y = npcInfo.getPos().getY();
        this.icon = npcInfo.getShape();
        this.name = npcInfo.getName();
        this.talk = npcInfo.getTalk();
        this.shipper = npcInfo.getShipper();
        this.npcTypes = npcInfo.getNpcTypes();
        this.classify = npcInfo.getClassify();
    }
    
    public Npc(final SysDataTaskNpc npcInfo) {
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
        this.npcTypes = new ArrayList<Integer>();
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
    
    public Npc(final int id, final int role, final int map, final int x, final int y, final int icon, final String name, final String title, final int ns, final int type) {
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
        this.npcTypes = new ArrayList<Integer>();
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
    
    public Npc(final int id, final int role, final int map, final int x, final int y, final int icon, final String name, final String title, final int ns, final int level, final int warSerial, final int type) {
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
        this.npcTypes = new ArrayList<Integer>();
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Npc)) {
            return false;
        }
        final Npc other = (Npc)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getRole() != other.getRole()) {
            return false;
        }
        if (this.getMap() != other.getMap()) {
            return false;
        }
        if (this.getX() != other.getX()) {
            return false;
        }
        if (this.getY() != other.getY()) {
            return false;
        }
        if (this.getIcon() != other.getIcon()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0143: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0143;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0143;
            }
            return false;
        }
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        Label_0180: {
            if (this$title == null) {
                if (other$title == null) {
                    break Label_0180;
                }
            }
            else if (this$title.equals(other$title)) {
                break Label_0180;
            }
            return false;
        }
        if (this.getNs() != other.getNs()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getWarSerial() != other.getWarSerial()) {
            return false;
        }
        if (this.getCreateTime() != other.getCreateTime()) {
            return false;
        }
        if (this.getEndTime() != other.getEndTime()) {
            return false;
        }
        final Object this$roleInfo = this.getRoleInfo();
        final Object other$roleInfo = other.getRoleInfo();
        Label_0282: {
            if (this$roleInfo == null) {
                if (other$roleInfo == null) {
                    break Label_0282;
                }
            }
            else if (this$roleInfo.equals(other$roleInfo)) {
                break Label_0282;
            }
            return false;
        }
        final Object this$inFight = this.getInFight();
        final Object other$inFight = other.getInFight();
        Label_0319: {
            if (this$inFight == null) {
                if (other$inFight == null) {
                    break Label_0319;
                }
            }
            else if (this$inFight.equals(other$inFight)) {
                break Label_0319;
            }
            return false;
        }
        if (this.getFightId() != other.getFightId()) {
            return false;
        }
        final Object this$shipper = this.getShipper();
        final Object other$shipper = other.getShipper();
        Label_0369: {
            if (this$shipper == null) {
                if (other$shipper == null) {
                    break Label_0369;
                }
            }
            else if (this$shipper.equals(other$shipper)) {
                break Label_0369;
            }
            return false;
        }
        if (this.getClassify() != other.getClassify()) {
            return false;
        }
        final Object this$talk = this.getTalk();
        final Object other$talk = other.getTalk();
        Label_0419: {
            if (this$talk == null) {
                if (other$talk == null) {
                    break Label_0419;
                }
            }
            else if (this$talk.equals(other$talk)) {
                break Label_0419;
            }
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getVisible() != other.getVisible()) {
            return false;
        }
        final Object this$npcTypes = this.getNpcTypes();
        final Object other$npcTypes = other.getNpcTypes();
        if (this$npcTypes == null) {
            if (other$npcTypes == null) {
                return this.getFinish() == other.getFinish() && this.getMirror() == other.getMirror();
            }
        }
        else if (this$npcTypes.equals(other$npcTypes)) {
            return this.getFinish() == other.getFinish() && this.getMirror() == other.getMirror();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Npc;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getRole();
        result = result * 59 + this.getMap();
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getIcon();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $title = this.getTitle();
        result = result * 59 + (($title == null) ? 43 : $title.hashCode());
        result = result * 59 + this.getNs();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getWarSerial();
        result = result * 59 + this.getCreateTime();
        result = result * 59 + this.getEndTime();
        final Object $roleInfo = this.getRoleInfo();
        result = result * 59 + (($roleInfo == null) ? 43 : $roleInfo.hashCode());
        final Object $inFight = this.getInFight();
        result = result * 59 + (($inFight == null) ? 43 : $inFight.hashCode());
        result = result * 59 + this.getFightId();
        final Object $shipper = this.getShipper();
        result = result * 59 + (($shipper == null) ? 43 : $shipper.hashCode());
        result = result * 59 + this.getClassify();
        final Object $talk = this.getTalk();
        result = result * 59 + (($talk == null) ? 43 : $talk.hashCode());
        result = result * 59 + this.getType();
        result = result * 59 + this.getVisible();
        final Object $npcTypes = this.getNpcTypes();
        result = result * 59 + (($npcTypes == null) ? 43 : $npcTypes.hashCode());
        final long $finish = this.getFinish();
        result = result * 59 + (int)($finish >>> 32 ^ $finish);
        result = result * 59 + this.getMirror();
        return result;
    }
    
    @Override
    public String toString() {
        return "Npc(id=" + this.getId() + ", role=" + this.getRole() + ", map=" + this.getMap() + ", x=" + this.getX() + ", y=" + this.getY() + ", icon=" + this.getIcon() + ", name=" + this.getName() + ", title=" + this.getTitle() + ", ns=" + this.getNs() + ", level=" + this.getLevel() + ", warSerial=" + this.getWarSerial() + ", createTime=" + this.getCreateTime() + ", endTime=" + this.getEndTime() + ", roleInfo=" + this.getRoleInfo() + ", inFight=" + this.getInFight() + ", fightId=" + this.getFightId() + ", shipper=" + this.getShipper() + ", classify=" + this.getClassify() + ", talk=" + this.getTalk() + ", type=" + this.getType() + ", visible=" + this.getVisible() + ", npcTypes=" + this.getNpcTypes() + ", finish=" + this.getFinish() + ", mirror=" + this.getMirror() + ")";
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
        this.npcTypes = new ArrayList<Integer>();
        this.finish = 0L;
        this.mirror = 0;
    }
    
    @ConstructorProperties({ "id", "role", "map", "x", "y", "icon", "name", "title", "ns", "level", "warSerial", "createTime", "endTime", "roleInfo", "inFight", "fightId", "shipper", "classify", "talk", "type", "visible", "npcTypes", "finish", "mirror" })
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
        this.npcTypes = new ArrayList<Integer>();
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
