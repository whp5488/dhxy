package com.kitty.listener.event;

import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.listener.EventType;
import java.util.List;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.BasePlayerEvent;

public class FightEndEvent extends BasePlayerEvent
{
    public static final byte WIN = 1;
    public static final byte FAIL = 2;
    public static final byte RUNAWAY = 3;
    public static final byte MAXROUND = 40;
    private Role role;
    private int fightId;
    private byte status;
    private int fightType;
    private NutMap reward;
    private int taskId;
    private int npcId;
    private int currPetId;
    private List<Role> rolesA;
    private List<Role> rolesB;
    
    public FightEndEvent(EventType evtType, Role role) {
        super(evtType, role);
    }
    
    public FightEndEvent(Fight fight, EventType evtType, Role role, int fightId, byte status, int fightType, NutMap reward, int taskId, int npcId, int currPetId) {
        super(evtType, role);
        this.role = role;
        this.fightId = fightId;
        this.status = status;
        this.fightType = fightType;
        this.reward = reward;
        this.taskId = taskId;
        this.npcId = npcId;
        this.currPetId = currPetId;
        this.rolesA = this.getRolesNew(fight.getListA());
        this.rolesB = this.getRolesNew(fight.getListB());
    }
    
    public FightEndEvent(Fight fight, EventType evtType, Role role, byte status, int currPetId, List<Role> rolesA, List<Role> rolesB) {
        super(evtType, role);
        this.role = role;
        this.fightId = fight.getId();
        this.status = status;
        this.fightType = fight.getType();
        this.reward = fight.getBossReward();
        this.taskId = fight.getTaskId();
        this.npcId = fight.getNpcId();
        this.currPetId = currPetId;
        this.rolesA = this.getRolesNew(fight.getListA());
        if (rolesA != null && rolesA.size() > 0) {
            this.rolesA.addAll(rolesA);
        }
        this.rolesB = this.getRolesNew(fight.getListB());
        if (rolesB != null && rolesB.size() > 0) {
            this.rolesB.addAll(rolesB);
        }
    }
    
    private List<Role> getRolesNew(List<FightObject> list) {
        List<Role> roles = new ArrayList<>();
        for (FightObject fightObject : list) {
            if (fightObject.isPlayer()) {
                roles.add(fightObject.getRole());
            }
            else {
                continue;
            }
        }
        return roles;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public int getFightId() {
        return this.fightId;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public int getFightType() {
        return this.fightType;
    }
    
    public NutMap getReward() {
        return this.reward;
    }
    
    public int getTaskId() {
        return this.taskId;
    }
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public int getCurrPetId() {
        return this.currPetId;
    }
    
    public List<Role> getRolesA() {
        return this.rolesA;
    }
    
    public List<Role> getRolesB() {
        return this.rolesB;
    }
}
