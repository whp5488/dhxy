
package com.kitty.system.game.fightnew.bean;

import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.enter.message.RespCombatFriends;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.partner.domain.Partner;
import java.util.Map;
import com.kitty.system.game.pet.domain.Pet;
import java.util.ArrayList;
import java.util.Iterator;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.team.domain.Member;
import java.util.List;

public class TeamPkFight extends Fight
{
    private List<Member> listA;
    private List<Member> listB;
    private static NutMap publicAutoSet;
    
    public TeamPkFight(final List<Member> listA, final List<Member> listB) {
        super(FIGHT_TYPE.PLAY.getValue());
        this.listA = listA;
        this.listB = listB;
        FightExecutorNewService.FIGHTS.put(this.getId(), this);
    }
    
    public TeamPkFight(final int type, final List<Member> listA, final List<Member> listB) {
        super(type);
        this.listA = listA;
        this.listB = listB;
        FightExecutorNewService.FIGHTS.put(this.getId(), this);
    }
    
    @Override
    protected void loadActors() {
        this.initRoleAndPetInfo(true);
        this.initRoleAndPetInfo(false);
    }
    
    @Override
    protected void notifyAndStart() {
        this.sendToSide(Boolean.TRUE);
        this.sendToSide(Boolean.FALSE);
        SpringUtils.getBean(FightNewService.class).whileSendStart(this);
        for (final FightObject fightObject : this.getAllFightObjects()) {
            if (fightObject.isPlayer()) {
                SpringUtils.getBean(FightNewService.class).autoFightOne(this, fightObject);
            }
        }
    }
    
    @Override
    public boolean isPkFight() {
        return true;
    }
    
    private void initRoleAndPetInfo(final boolean isA) {
        int index = 0;
        int petIndex = 5;
        List<FightObject> fightObjectList;
        List<Member> memberList;
        if (isA) {
            fightObjectList = this.getListA();
            memberList = this.listA;
        }
        else {
            fightObjectList = this.getListB();
            memberList = this.listB;
        }
        int customIndex = 0;
        final List<Member> partners = new ArrayList<Member>();
        for (final Member member : memberList) {
            if (!member.isInTeam()) {
                continue;
            }
            final Role teamRole = SpringUtils.getRoleService().getOnlinePlayer(member.getId());
            if (teamRole == null) {
                partners.add(member);
            }
            else {
                FightExecutorNewService.FIGHT_ROOMS.put(member.getRoleId(), this.getId());
                SpringUtils.getBean(FightNewService.class).fullLifeRoleAndPet(this, teamRole);
                this.fightNewService.checkAtFightStart(teamRole);
                final FightObject fightObject = new PlayerFightObject(teamRole, index, customIndex, isA);
                fightObjectList.add(fightObject);
                if (isA) {
                    final FightMember fightMember = fightObject.asFightMember();
                    this.getFightMemberA().add(fightMember);
                }
                else {
                    final FightMember fightMember = fightObject.asFightMember();
                    this.getFightMemberB().add(fightMember);
                }
                ++index;
                ++customIndex;
                final Pet pet = SpringUtils.getPetService().selectPet(teamRole).get(teamRole.getExtendBox().getCombatPet());
                if (pet != null) {
                    ++customIndex;
                    final PetFightObject petFightObj = new PetFightObject(pet, fightObject);
                    fightObjectList.add(petFightObj);
                    final FightMember petFightMember = petFightObj.asFightMember();
                    if (isA) {
                        this.getFightMemberA().add(petFightMember);
                    }
                    else {
                        this.getFightMemberB().add(petFightMember);
                    }
                    FightExecutorNewService.PETID_ROLEID.put(pet.getPetId(), member.getRoleId());
                    FightExecutorNewService.FIGHT_ROOMS.put(pet.getPetId(), this.getId());
                }
                ++petIndex;
            }
        }
        if (partners.size() > 0) {
            for (final Member member2 : partners) {
                if (!member2.isInTeam()) {
                    continue;
                }
                final Role teamRole2 = SpringUtils.getRoleService().getOnlinePlayer(member2.getRoleId());
                if (teamRole2 == null) {
                    continue;
                }
                final Map<Integer, Partner> partnerMap = DataCache.PARTNER_MAP.get(teamRole2.getId());
                if (partnerMap == null) {
                    continue;
                }
                if (partnerMap.size() <= 0) {
                    continue;
                }
                final Partner partner = partnerMap.get(member2.getSerial());
                if (partner == null) {
                    continue;
                }
                final FightObject hostFightObject = this.queryFightObject(member2.getRoleId());
                final FightObject partnerFightObject = new PartnerFightObject(partner, hostFightObject, index);
                fightObjectList.add(partnerFightObject);
                final FightMember partnerFightMember = partnerFightObject.asFightMember();
                if (isA) {
                    this.getFightMemberA().add(partnerFightMember);
                }
                else {
                    this.getFightMemberB().add(partnerFightMember);
                }
                ++index;
            }
        }
    }
    
    private void sendToSide(final boolean isA) {
        final RespCombatFriends respCombatFriends = new RespCombatFriends();
        final RespCombatOpponets respCombatOpponets = new RespCombatOpponets();
        List<Member> members;
        if (isA) {
            members = this.listA;
            respCombatFriends.setList(this.getFightMemberA());
            respCombatOpponets.setList(this.getFightMemberB());
        }
        else {
            members = this.listB;
            respCombatFriends.setList(this.getFightMemberB());
            respCombatOpponets.setList(this.getFightMemberA());
        }
        SpringUtils.getBean(FightNewService.class).sendStartCombat(this, respCombatFriends, respCombatOpponets, members);
    }
    
    static {
        (TeamPkFight.publicAutoSet = NutMap.NEW()).setv("type", 0);
        TeamPkFight.publicAutoSet.setv("action", (Object)1);
        TeamPkFight.publicAutoSet.setv("skillId", 0);
    }
}
