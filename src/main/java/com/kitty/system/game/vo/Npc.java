package com.kitty.system.game.vo;

import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.system.init.domain.SysDataTaskNpc;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;
import lombok.Setter;
import org.nutz.lang.util.NutMap;

@Getter
@Setter
public class Npc implements Serializable {
  private int id;
  
  private int role;
  
  private int map;
  
  private int x;
  
  private int y;
  
  @ConstructorProperties({"id", "role", "map", "x", "y", "icon", "name", "title", "ns", "level", "warSerial", "createTime", "endTime", "roleInfo", "inFight", "fightId", "shipper", "classify", "talk", "type", "visible", "npcTypes", "finish", "mirror"})
  public Npc(int id, int role, int map, int x, int y, int icon, String name, String title, int ns, int level, int warSerial, int createTime, int endTime, NutMap roleInfo, AtomicBoolean inFight, int fightId, String shipper, int classify, String talk, int type, int visible, List<Integer> npcTypes, long finish, byte mirror) {
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

  private int icon = 1;
  
  private String name;
  
  private String title;
  
  private int ns;

  private int level = 0;

  private int warSerial = 0;

  private int createTime = Math.toIntExact(System.currentTimeMillis() / 1000L);

  private int endTime = -1;
  
  private NutMap roleInfo;

  private AtomicBoolean inFight = new AtomicBoolean();

  private int fightId = -1;
  
  private String shipper;

  private int classify = 0;
  
  private String talk;

  private int type = 0;

  private int visible = 0;

  private List<Integer> npcTypes = new ArrayList<>();

  private long finish = 0L;

  private byte mirror = 0;

  public boolean isInFight() {
    return this.inFight.get();
  }
  
  public void setInFight(boolean inFight) {
    this.inFight.set(inFight);
  }
  
  public boolean isShow() {
    if (this.endTime > 0 && Math.toIntExact(System.currentTimeMillis() / 1000L) > this.endTime)
      return false; 
    return true;
  }
  
  public Npc(SysNpcInfo npcInfo) {
    this.id = npcInfo.getSerial().intValue();
    this.map = npcInfo.getPos().getMapId().intValue();
    this.x = npcInfo.getPos().getX().intValue();
    this.y = npcInfo.getPos().getY().intValue();
    this.icon = npcInfo.getShape().intValue();
    this.name = npcInfo.getName();
    this.talk = npcInfo.getTalk();
    this.shipper = npcInfo.getShipper();
    this.npcTypes = npcInfo.getNpcTypes();
    this.classify = npcInfo.getClassify();
  }
  
  public Npc(SysDataTaskNpc npcInfo) {
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
  
  public Npc() {}
}
