
package com.kitty.system.game.partner.service;

import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.partner.handler.FeedPartnerHandler;
import org.nutz.lang.Strings;
import com.kitty.system.game.partner.handler.FeedPartnerHelper;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.partner.message.RespUpdatePartner;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.pet.message.RespPetInfo;
import com.kitty.common.utils.ZipUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.HashMap;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.skill.domain.SkillBox;
import com.kitty.common.identity.IdentityType;
import com.kitty.common.thread.IdGenerator;
import com.kitty.system.game.partner.message.RespPartnerList;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.partner.enter.PartnerFiedValue;
import com.kitty.system.game.partner.message.ReqSelectCurrentPartner;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import com.kitty.system.init.domain.SysPartner;
import com.kitty.system.game.team.domain.Member;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.Comparator;
import com.kitty.mina.cache.DataCache;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.system.game.partner.domain.Partner;
import java.util.Map;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class PartnerService
{
    @Autowired
    private Dao dao;
    @Autowired
    private RoleService roleService;
    
    public Map<Integer, Partner> selectPartner(final Role role) {
        final List<Partner> list = this.dao.query(Partner.class, Cnd.where("is_del", "!=", (Object)"-1").and("role_id", "=", role.getRoleId()));
        return list.stream().collect(Collectors.toMap(Partner::getPartnerId, Partner -> Partner));
    }
    
    public ArrayList<Partner> selectPartnerForFight(final Role role) {
        ArrayList<Partner> partners = new ArrayList<Partner>();
        final Map<Integer, Partner> partnerMap = DataCache.PARTNER_MAP.get(role.getId());
        for (final Map.Entry<Integer, Partner> entry : partnerMap.entrySet()) {
            final Partner partner = entry.getValue();
            if (1 == partner.getIsFight()) {
                partners.add(partner);
            }
        }
        if (partners.size() > 0) {
            partners = (ArrayList<Partner>)partners.stream().sorted(Comparator.comparing(Partner::getPartnerOrder)).collect(Collectors.toList());
        }
        return partners;
    }
    
    public ArrayList<Member> partner2Member(final Role role) {
        final ArrayList<Partner> partners = this.selectPartnerForFight(role);
        final ArrayList<Member> members = new ArrayList<Member>();
        for (final Partner partner : partners) {
            final Member member = Member.valueOf(partner);
            members.add(member);
        }
        return members;
    }
    
    public Partner addPartner(final Role role, final Integer partnerSerial) {
        final SysPartner sysPartner = PartnerDataPool.partnerMap.get(partnerSerial);
        if (sysPartner == null) {
            MessagePusher.pushMessage(role, new RespMsg("未找到该伙伴信息"));
            return null;
        }
        final Map<Integer, Partner> integerPartnerMap = DataCache.PARTNER_MAP.get(role.getId());
        final Iterator<Partner> iterator = integerPartnerMap.values().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getPartnerType().equals(partnerSerial)) {
                MessagePusher.pushMessage(role, new RespMsg("你的队伍已经存在该伙伴，无需再次招募！"));
                return null;
            }
        }
        final int needYl = sysPartner.getNeedYl();
        if (needYl > 0) {
            final boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, needYl, "添加伙伴");
            if (!b) {
                MessagePusher.pushMessage(role, new RespMsg("您当前无力支付招募的费用"));
                return null;
            }
        }
        final Partner partner = this.initAsPartner(sysPartner);
        if (partner != null) {
            partner.setRoleId(role.getRoleId());
            partner.setIsRecruits((byte)1);
            final Map<Integer, Partner> partnerMap = integerPartnerMap;
            int count = 0;
            for (final Map.Entry<Integer, Partner> entry : partnerMap.entrySet()) {
                final Partner valuePartner = entry.getValue();
                if (valuePartner.getIsFight() == 1) {
                    ++count;
                }
            }
            if (count < 4) {
                partner.setIsFight((byte)1);
            }
            partnerMap.put(partner.getPartnerId(), partner);
            final int partnerLv = 55;
            if (role.getLevel() >= partnerLv) {
                partner.setPartnerLv((short)partnerLv);
            }
            else {
                partner.setPartnerLv(role.getLevel().shortValue());
            }
            this.partnerLevelChangeEvent(role, partner);
            partner.save();
            this.selectPartnerList(role);
            if (count < 4) {
                this.excuteTeam(role);
            }
            return partner;
        }
        MessagePusher.pushMessage(role, new RespMsg("未找到该伙伴信息"));
        return null;
    }
    
    public void combatPartner(final Role role, final ReqSelectCurrentPartner reqSelectCurrentPartner) {
        final Map<Integer, Partner> partnerMap = DataCache.PARTNER_MAP.get(role.getId());
        if (partnerMap == null || partnerMap.size() <= 0) {
            MessagePusher.pushMessage(role, new RespMsg("未找到该伙伴信息"));
            return;
        }
        final Partner partner = partnerMap.get(reqSelectCurrentPartner.getPartnerId());
        if (partner != null) {
            final List<PartnerFiedValue> list = new ArrayList<PartnerFiedValue>();
            if (reqSelectCurrentPartner.getIsFight() == 1) {
                final List<Integer> partnerFightList = partnerMap.entrySet().stream().filter(e -> e.getValue().getIsFight() == 1 && e.getValue().getIsRecruits() == 1).map(Map.Entry::getKey).collect(Collectors.toList());
                if (partnerFightList.size() >= 4) {
                    if (reqSelectCurrentPartner.getReplacePartnerId() <= 0) {
                        return;
                    }
                    final Partner replacePartner = partnerMap.get(reqSelectCurrentPartner.getReplacePartnerId());
                    if (replacePartner == null) {
                        return;
                    }
                    partner.setPartnerOrder(replacePartner.getPartnerOrder());
                    replacePartner.setPartnerOrder((byte)0);
                    replacePartner.setIsFight((byte)0);
                    final PartnerFiedValue replaceFiedValue = this.getFiedValue(replacePartner);
                    list.add(replaceFiedValue);
                }
                else {
                    partner.setPartnerOrder((byte)(partnerFightList.size() + 1));
                }
            }
            partner.setIsFight(reqSelectCurrentPartner.getIsFight());
            partner.save();
            final PartnerFiedValue fiedValue = this.getFiedValue(partner);
            list.add(fiedValue);
            final TeamService teamService = SpringUtils.getBean(TeamService.class);
            teamService.memberHandleThreadLocal(role, memberRole -> {
                if (role.getRoleId().equals(memberRole.getRoleId())) {
                    MessagePusher.pushMessage(memberRole, new RespPartnerList(list));
                }
                return;
            });
            this.excuteTeam(role);
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("未找到该伙伴信息"));
        }
    }
    
    public Partner initAsPartner(final SysPartner sysPartner) {
        if (sysPartner != null) {
            try {
                final Partner partner = new Partner();
                partner.setId(IdGenerator.getNextId());
                partner.setPartnerType(sysPartner.getSerial());
                partner.setIcon(sysPartner.getIcon());
                partner.setPartnerName(sysPartner.getName());
                partner.setPartnerId(SpringUtils.getIdentityService().getNextId(IdentityType.PARTNER));
                partner.setSex(sysPartner.getSex());
                partner.setRace(sysPartner.getRace());
                partner.setOperDefaultSkillId(sysPartner.getOperDefaultSkillId());
                partner.setIsRecruits((byte)0);
                this.calculateBase(partner);
                return partner;
            }
            catch (final Exception e) {
                return null;
            }
        }
        return null;
    }
    
    public SkillBox excuteSkillBox(final Partner partner, final SysPartner sysPartner) {
        final SkillBox box = new SkillBox();
        final Map<Integer, SysSkill> skillMap = SkillDataPool.skillMap;
        final List<Integer> skillIds = sysPartner.getSkills();
        if (skillIds != null && skillIds.size() > 0) {
            final short lv = (short)(partner.getRebirth() * 1000 + partner.getPartnerLv());
            final SysPartnerProperty sysPartnerProperty = (SysPartnerProperty)PartnerDataPool.partnerPropertyMap.get(partner.getPartnerType()).get(lv);
            final Map<Integer, RoleSkill> skills = new HashMap<Integer, RoleSkill>();
            for (final Integer skillSerial : skillIds) {
                final SysSkill sysSkill = skillMap.get(skillSerial);
                if (sysSkill != null) {
                    final RoleSkill roleSkill = new RoleSkill();
                    roleSkill.setSkillId(sysSkill.getSerial());
                    roleSkill.setType(sysSkill.getAttr());
                    if (sysPartnerProperty != null) {
                        final Map<Integer, Integer> extSkillInfo = sysPartnerProperty.getExtSkillInfo();
                        final Map<Integer, Integer> skillInfo = sysPartnerProperty.getSkillInfo();
                        final Integer ext = extSkillInfo.get(skillSerial);
                        if (ext != null) {
                            roleSkill.setProficient(ext);
                        }
                        final Integer next = skillInfo.get(skillSerial);
                        if (next != null) {
                            roleSkill.setProficient(next);
                        }
                    }
                    skills.put(sysSkill.getSerial(), roleSkill);
                }
            }
            box.setSkills(skills);
        }
        return box;
    }
    
    public void calculateBase(final Partner partner) {
        final Map<Short, SysPartnerProperty> sysPartnerPropertyMap = PartnerDataPool.partnerPropertyMap.get(partner.getPartnerType());
        final int level = partner.getPartnerLv() + partner.getRebirth() * 1000;
        final SysPartnerProperty sysPartnerProperty = sysPartnerPropertyMap.get((short)level);
        if (sysPartnerProperty == null) {
            return;
        }
        partner.setCurrLife(sysPartnerProperty.getHpMax());
        partner.setMaxLife(sysPartnerProperty.getHpMax());
        final SysPartner sysPartner = PartnerDataPool.partnerMap.get(partner.getPartnerType());
        final SkillBox skillBox = this.excuteSkillBox(partner, sysPartner);
        final String skillJson = JsonUtils.object2String(skillBox);
        partner.setSkillBox(skillBox);
        partner.setSkillBoxJson(ZipUtil.compress(skillJson));
    }
    
    public void loadPartner(final Role role, final Partner partner) {
        this.partnerInfo(role, partner);
    }
    
    public void partnerInfo(final Role role, final Partner partner) {
        final RespPetInfo petInfo = new RespPetInfo();
        BeanUtils.copyBeanProp(petInfo, partner);
    }
    
    public boolean addExp(final Role role, final Partner partner, final long exp, final boolean reason) {
        if (exp <= 0L) {
            return false;
        }
        boolean flag = true;
        short level = partner.getPartnerLv();
        final int partnerNextLevel = partner.getPartnerLv();
        final int roleGrade = role.getRelive() * 1000 + role.getLevel();
        int partnerGrade = partner.getRebirth() * 1000 + partner.getPartnerLv();
        if (partnerGrade > roleGrade) {
            final String msg = "伙伴等级不能超过人物等级";
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, false));
            return false;
        }
        if (partnerGrade >= 3250 && partner.getExp() >= 1500000000L) {
            final String msg = String.format("%s伙伴已到达%d转的上限%d级", partner.getPartnerName(), partner.getRebirth(), partner.getPartnerLv());
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, false));
            return false;
        }
        final String exp_tips = "#<G>" + exp + "#点经验";
        long totalExp = (partner.getExp() + exp > 1500000000L) ? 1500000000L : (partner.getExp() + exp);
        final int rebirth = partner.getRebirth();
        long needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_PARTNER.getValue(), rebirth, partnerNextLevel);
        if (totalExp >= needExp && partner.getRebirth() * 1000 + partner.getPartnerLv() < roleGrade) {
            final long expForRebirthLevelNext = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_PARTNER.getValue(), rebirth, partnerNextLevel + 1);
            if (expForRebirthLevelNext <= 0L) {
                if (needExp == partner.getExp()) {
                    return false;
                }
                partner.setExp(needExp);
                this.updateExp(role, partner);
                partner.save();
                return true;
            }
            else {
                while (totalExp >= needExp) {
                    totalExp -= needExp;
                    ++level;
                    partner.setExp(totalExp);
                    partnerGrade = partner.getRebirth() * 1000 + level;
                    if (partnerGrade > roleGrade) {
                        totalExp = ((partner.getExp() + exp > 1500000000L) ? 1500000000L : (partner.getExp() + exp));
                        partner.setExp(totalExp);
                        flag = false;
                    }
                    if (!flag) {
                        if (partner.getExp() >= 1500000000L) {
                            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "你的伙伴#<G>" + partner.getPartnerName() + "#等级已超过角色等级8级无法获得经验", true));
                        }
                        this.updateExp(role, partner);
                        break;
                    }
                    partner.setPartnerLv(level);
                    this.partnerLevelChangeEvent(role, partner);
                    needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_PARTNER.getValue(), rebirth, partner.getPartnerLv());
                }
            }
        }
        else {
            partner.setExp(totalExp);
            this.updateExp(role, partner);
        }
        partner.save();
        if (reason) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "你的#<G>" + partner.getPartnerName() + "#获得了#<IR12>#" + exp_tips, true));
        }
        return true;
    }
    
    public void updateExp(final Role role, final Partner partner) {
        final RespUpdatePartner respUpdatePartner = new RespUpdatePartner();
        final List<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("exp", (byte)4, String.valueOf(partner.getExp())));
        respUpdatePartner.setId(partner.getPartnerId());
        respUpdatePartner.setList(list);
        MessagePusher.pushMessage(role, respUpdatePartner);
    }
    
    public void partnerLevelChangeEvent(final Role role, final Partner partner) {
        this.calculateBase(partner);
        final Map<Integer, Partner> partnerMap = DataCache.PARTNER_MAP.get(role.getId());
        partnerMap.put(partner.getPartnerId(), partner);
        this.sendPartnerPanels(role, partner);
    }
    
    public void sendPartnerPanels(final Role role, final Partner partner) {
        final RespUpdatePartner respUpdatePartner = new RespUpdatePartner();
        final List<FiedValue> list = new ArrayList<FiedValue>();
        respUpdatePartner.setId(partner.getPartnerId());
        list.add(new FiedValue("level", (byte)3, partner.getPartnerLv()));
        list.add(new FiedValue("exp", (byte)4, String.valueOf(partner.getExp())));
        list.add(new FiedValue("relive", (byte)3, partner.getRebirth()));
        list.add(new FiedValue("isRecruit", (byte)1, partner.getIsRecruits()));
        list.add(new FiedValue("isFight", (byte)1, partner.getIsFight()));
        list.add(new FiedValue("dsId", (byte)3, partner.getOperDefaultSkillId()));
        respUpdatePartner.setList(list);
        MessagePusher.pushMessage(role, respUpdatePartner);
    }
    
    public PartnerFiedValue getFiedValue(final Partner partner) {
        final PartnerFiedValue partnerFiedValue = new PartnerFiedValue();
        partnerFiedValue.setUid(partner.getPartnerId());
        partnerFiedValue.setTypeId(partner.getPartnerType());
        partnerFiedValue.setRelive(partner.getRebirth());
        partnerFiedValue.setLevel(partner.getPartnerLv());
        partnerFiedValue.setExp(String.valueOf(partner.getExp()));
        partnerFiedValue.setIsRecruit(partner.getIsRecruits());
        partnerFiedValue.setIsFight(partner.getIsFight());
        partnerFiedValue.setPartnerOrder(partner.getPartnerOrder());
        partnerFiedValue.setDsId(partner.getOperDefaultSkillId());
        return partnerFiedValue;
    }
    
    public PartnerFiedValue getFiedValue(final SysPartner sysPartner) {
        final PartnerFiedValue partnerFiedValue = new PartnerFiedValue();
        partnerFiedValue.setUid(0);
        partnerFiedValue.setTypeId(sysPartner.getSerial());
        partnerFiedValue.setRelive((byte)0);
        partnerFiedValue.setLevel((short)0);
        partnerFiedValue.setExp(String.valueOf(0));
        partnerFiedValue.setIsRecruit((byte)0);
        partnerFiedValue.setIsFight((byte)0);
        partnerFiedValue.setDsId(sysPartner.getOperDefaultSkillId());
        return partnerFiedValue;
    }
    
    public void selectPartnerList(final Role role) {
        final List<Integer> sysPartnerList = PartnerDataPool.partnerMap.entrySet().stream().filter(map -> map.getValue().getIsRecruits() == 1).map(Map.Entry::getKey).collect(Collectors.toList());
        final Map<Integer, Partner> partnerMap = DataCache.PARTNER_MAP.get(role.getId());
        final List<PartnerFiedValue> list = new ArrayList<PartnerFiedValue>();
        if (partnerMap.size() <= sysPartnerList.size()) {
            for (final Map.Entry<Integer, Partner> entry : partnerMap.entrySet()) {
                final Partner partner = entry.getValue();
                if (sysPartnerList.contains(partner.getPartnerType())) {
                    final PartnerFiedValue fiedValue = this.getFiedValue(partner);
                    list.add(fiedValue);
                    sysPartnerList.remove(partner.getPartnerType());
                }
            }
            if (sysPartnerList.size() > 0) {
                for (int i = sysPartnerList.size() - 1; i >= 0; --i) {
                    final Integer sysPartnerSerial = sysPartnerList.get(i);
                    final SysPartner sysPartner = PartnerDataPool.partnerMap.get(sysPartnerSerial);
                    if (sysPartner != null) {
                        final PartnerFiedValue fiedValue = this.getFiedValue(sysPartner);
                        list.add(fiedValue);
                        sysPartnerList.remove(i);
                    }
                }
            }
        }
        MessagePusher.pushMessage(role, new RespPartnerList(list));
    }
    
    public void selectFightPartnerList(final Role role) {
        final ArrayList<Partner> partners = this.selectPartnerForFight(role);
        if (partners.size() <= 0) {
            return;
        }
        final List<PartnerFiedValue> list = new ArrayList<PartnerFiedValue>();
        for (final Partner partner : partners) {
            final PartnerFiedValue fiedValue = this.getFiedValue(partner);
            list.add(fiedValue);
        }
        MessagePusher.pushMessage(role, new RespPartnerList(list));
    }
    
    public void relive(final Role role, final int serial) {
        final Partner partner = (Partner)DataCache.PARTNER_MAP.get(role.getId()).get(serial);
        if (partner != null && partner.getRebirth() < 3 && partner.getRebirth() < role.getRelive()) {
            final SysRbAttr sysRbAttr = PartDataPool.rbAttrMap.get(partner.getRebirth() + 2);
            partner.setRebirth((byte)(partner.getRebirth() + 1));
            partner.setPartnerLv(sysRbAttr.getInitLv().shortValue());
            this.partnerLevelChangeEvent(role, partner);
            partner.save();
        }
    }
    
    public void feedPartner(final Role role, final Partner partner, final String type, final int param) {
        final FeedPartnerHandler handler = FeedPartnerHelper.INSTANCE.getHandler(type);
        if (handler == null) {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
            return;
        }
        final String msg = handler.acceptFeed(partner, role, param, type, new Object[0]);
        if (!Strings.isEmpty((CharSequence)msg)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, false));
        }
    }
    
    private void excuteTeam(final Role role) {
        final TeamService teamService = SpringUtils.getBean(TeamService.class);
        final Team team = teamService.getTeam(role.getRoleId());
        if (team != null) {
            final Member member = team.getList().get(0);
            if (member.getRoleId().equals(role.getRoleId())) {
                final Map<Integer, Partner> partnerMap = DataCache.PARTNER_MAP.get(role.getId());
                if (partnerMap == null || partnerMap.size() <= 0) {
                    return;
                }
                final List<Partner> partners = partnerMap.values().stream().filter(obj -> obj.getIsFight() == 1).collect(Collectors.toList());
                final ArrayList<Member> listPartnerMembers = new ArrayList<Member>();
                for (final Partner p : partners) {
                    final Member partnerMember = Member.valueOf(p);
                    listPartnerMembers.add(partnerMember);
                }
                team.setPartner(listPartnerMembers);
                teamService.updateTeamList(role);
            }
        }
    }
    
    public Map<Integer, Partner> loadPartner(final Role role) {
        Map<Integer, Partner> partnerMap = DataCache.PARTNER_MAP.get(role.getId());
        if (partnerMap == null) {
            partnerMap = this.selectPartner(role);
            DataCache.PARTNER_MAP.put(role.getId(), partnerMap);
        }
        return partnerMap;
    }
    
    public void changeDefaultSkill(final Role role, final int pId, final int skillId) {
        final Map<Integer, Partner> partnerMap = DataCache.PARTNER_MAP.get(role.getId());
        Partner partner = null;
        for (final Map.Entry<Integer, Partner> entry : partnerMap.entrySet()) {
            if (pId == entry.getValue().getPartnerId()) {
                partner = entry.getValue();
                break;
            }
        }
        if (partner != null) {
            final SkillBox skillBox = partner.getSkillBox();
            if (skillBox != null) {
                final Map<Integer, RoleSkill> skills = skillBox.getSkills();
                final RoleSkill roleSkill = skills.get(skillId);
                if (roleSkill != null) {
                    partner.setOperDefaultSkillId(roleSkill.getSkillId());
                    partner.save();
                }
            }
        }
        this.sendPartnerPanels(role, partner);
    }
}
