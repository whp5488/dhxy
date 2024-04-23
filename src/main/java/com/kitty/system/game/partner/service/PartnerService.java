package com.kitty.system.game.partner.service;

import com.kitty.common.identity.IdentityType;
import com.kitty.common.thread.IdGenerator;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.partner.enter.PartnerFiedValue;
import com.kitty.system.game.partner.handler.FeedPartnerHandler;
import com.kitty.system.game.partner.handler.FeedPartnerHelper;
import com.kitty.system.game.partner.message.ReqSelectCurrentPartner;
import com.kitty.system.game.partner.message.RespPartnerList;
import com.kitty.system.game.partner.message.RespUpdatePartner;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import com.kitty.system.game.pet.message.RespPetInfo;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.domain.SkillBox;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysPartner;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.pool.PartDataPool;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerService {
  @Autowired
  private Dao dao;
  
  @Autowired
  private RoleService roleService;
  
  public Map<Integer, Partner> selectPartner(Role role) {
    List<Partner> list = this.dao.query(Partner.class, (Condition)Cnd.where("is_del", "!=", "-1").and("role_id", "=", role.getRoleId()));
    return (Map<Integer, Partner>)list.stream().collect(Collectors.toMap(Partner::getPartnerId, partner -> partner));
  }
  
  public ArrayList<Partner> selectPartnerForFight(Role role) {
    ArrayList<Partner> partners = new ArrayList<>();
    Map<Integer, Partner> partnerMap = (Map<Integer, Partner>)DataCache.PARTNER_MAP.get(role.getId());
    for (Map.Entry<Integer, Partner> entry : partnerMap.entrySet()) {
      Partner partner = entry.getValue();
      if (1 == partner.getIsFight())
        partners.add(partner); 
    } 
    if (partners.size() > 0)
      partners = (ArrayList<Partner>)partners.stream().sorted(Comparator.comparing(Partner::getPartnerOrder)).collect(Collectors.toList()); 
    return partners;
  }
  
  public ArrayList<Member> partner2Member(Role role) {
    ArrayList<Partner> partners = selectPartnerForFight(role);
    ArrayList<Member> members = new ArrayList<>();
    for (Partner partner : partners) {
      Member member = Member.valueOf(partner);
      members.add(member);
    } 
    return members;
  }
  
  public Partner addPartner(Role role, Integer partnerSerial) {
    SysPartner sysPartner = (SysPartner)PartnerDataPool.partnerMap.get(partnerSerial);
    if (sysPartner == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("未找到该伙伴信息"));
      return null;
    } 
    Map<Integer, Partner> integerPartnerMap = (Map<Integer, Partner>)DataCache.PARTNER_MAP.get(role.getId());
    Iterator<Partner> iterator = integerPartnerMap.values().iterator();
    while (iterator.hasNext()) {
      if (((Partner)iterator.next()).getPartnerType().equals(partnerSerial)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("你的队伍已经存在该伙伴，无需再次招募！"));
        return null;
      } 
    } 
    int needYl = sysPartner.getNeedYl();
    if (needYl > 0) {
      boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, needYl, "添加伙伴");
      if (!b) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("您当前无力支付招募的费用"));
        return null;
      } 
    } 
    Partner partner = initAsPartner(sysPartner);
    if (partner != null) {
      partner.setRoleId(role.getRoleId());
      partner.setIsRecruits((byte)1);
      Map<Integer, Partner> partnerMap = integerPartnerMap;
      int count = 0;
      for (Map.Entry<Integer, Partner> entry : partnerMap.entrySet()) {
        Partner valuePartner = entry.getValue();
        if (valuePartner.getIsFight() == 1)
          count++; 
      } 
      if (count < 4)
        partner.setIsFight((byte)1); 
      partnerMap.put(partner.getPartnerId(), partner);
      int partnerLv = 55;
      if (role.getLevel().intValue() >= partnerLv) {
        partner.setPartnerLv((short)partnerLv);
      } else {
        partner.setPartnerLv(role.getLevel().shortValue());
      } 
      partnerLevelChangeEvent(role, partner);
      partner.save();
      selectPartnerList(role);
      if (count < 4)
        excuteTeam(role); 
      return partner;
    } 
    MessagePusher.pushMessage(role, (Message)new RespMsg("未找到该伙伴信息"));
    return null;
  }
  
  public void combatPartner(Role role, ReqSelectCurrentPartner reqSelectCurrentPartner) {
    Map<Integer, Partner> partnerMap = (Map<Integer, Partner>)DataCache.PARTNER_MAP.get(role.getId());
    if (partnerMap == null || partnerMap.size() <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("未找到该伙伴信息"));
      return;
    } 
    Partner partner = partnerMap.get(Integer.valueOf(reqSelectCurrentPartner.getPartnerId()));
    if (partner != null) {
      List<PartnerFiedValue> list = new ArrayList<>();
      if (reqSelectCurrentPartner.getIsFight() == 1) {
        List<Integer> partnerFightList = (List<Integer>)partnerMap.entrySet().stream().filter(e -> (((Partner)e.getValue()).getIsFight() == 1 && ((Partner)e.getValue()).getIsRecruits() == 1)).map(Map.Entry::getKey).collect(Collectors.toList());
        if (partnerFightList.size() >= 4) {
          if (reqSelectCurrentPartner.getReplacePartnerId() <= 0)
            return; 
          Partner replacePartner = partnerMap.get(Integer.valueOf(reqSelectCurrentPartner.getReplacePartnerId()));
          if (replacePartner == null)
            return; 
          partner.setPartnerOrder(replacePartner.getPartnerOrder());
          replacePartner.setPartnerOrder((byte)0);
          replacePartner.setIsFight((byte)0);
          PartnerFiedValue replaceFiedValue = getFiedValue(replacePartner);
          list.add(replaceFiedValue);
        } else {
          partner.setPartnerOrder((byte)(partnerFightList.size() + 1));
        } 
      } 
      partner.setIsFight(reqSelectCurrentPartner.getIsFight());
      partner.save();
      PartnerFiedValue fiedValue = getFiedValue(partner);
      list.add(fiedValue);
      TeamService teamService = (TeamService)SpringUtils.getBean(TeamService.class);
      teamService.memberHandleThreadLocal(role, memberRole -> {
            if (role.getRoleId().equals(memberRole.getRoleId()))
              MessagePusher.pushMessage(memberRole, (Message)new RespPartnerList(list)); 
          });
      excuteTeam(role);
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("未找到该伙伴信息"));
    } 
  }
  
  public Partner initAsPartner(SysPartner sysPartner) {
    if (sysPartner != null)
      try {
        Partner partner = new Partner();
        partner.setId(Long.valueOf(IdGenerator.getNextId()));
        partner.setPartnerType(Integer.valueOf(sysPartner.getSerial()));
        partner.setIcon(Integer.valueOf(sysPartner.getIcon()));
        partner.setPartnerName(sysPartner.getName());
        partner.setPartnerId(Integer.valueOf(SpringUtils.getIdentityService().getNextId(IdentityType.PARTNER)));
        partner.setSex(Integer.valueOf(sysPartner.getSex()));
        partner.setRace(Integer.valueOf(sysPartner.getRace()));
        partner.setOperDefaultSkillId(sysPartner.getOperDefaultSkillId());
        partner.setIsRecruits((byte)0);
        calculateBase(partner);
        return partner;
      } catch (Exception e) {
        return null;
      }  
    return null;
  }
  
  public SkillBox excuteSkillBox(Partner partner, SysPartner sysPartner) {
    SkillBox box = new SkillBox();
    Map<Integer, SysSkill> skillMap = SkillDataPool.skillMap;
    List<Integer> skillIds = sysPartner.getSkills();
    if (skillIds != null && skillIds.size() > 0) {
      short lv = (short)(partner.getRebirth() * 1000 + partner.getPartnerLv());
      SysPartnerProperty sysPartnerProperty = (SysPartnerProperty)((Map)PartnerDataPool.partnerPropertyMap.get(partner.getPartnerType())).get(Short.valueOf(lv));
      Map<Integer, RoleSkill> skills = new HashMap<>();
      for (Integer skillSerial : skillIds) {
        SysSkill sysSkill = skillMap.get(skillSerial);
        if (sysSkill != null) {
          RoleSkill roleSkill = new RoleSkill();
          roleSkill.setSkillId(sysSkill.getSerial());
          roleSkill.setType(sysSkill.getAttr());
          if (sysPartnerProperty != null) {
            Map<Integer, Integer> extSkillInfo = sysPartnerProperty.getExtSkillInfo();
            Map<Integer, Integer> skillInfo = sysPartnerProperty.getSkillInfo();
            Integer ext = extSkillInfo.get(skillSerial);
            if (ext != null)
              roleSkill.setProficient(ext); 
            Integer next = skillInfo.get(skillSerial);
            if (next != null)
              roleSkill.setProficient(next); 
          } 
          skills.put(sysSkill.getSerial(), roleSkill);
        } 
      } 
      box.setSkills(skills);
    } 
    return box;
  }
  
  public void calculateBase(Partner partner) {
    Map<Short, SysPartnerProperty> sysPartnerPropertyMap = (Map<Short, SysPartnerProperty>)PartnerDataPool.partnerPropertyMap.get(partner.getPartnerType());
    int level = partner.getPartnerLv() + partner.getRebirth() * 1000;
    SysPartnerProperty sysPartnerProperty = sysPartnerPropertyMap.get(Short.valueOf((short)level));
    if (sysPartnerProperty == null)
      return; 
    partner.setCurrLife(Integer.valueOf(sysPartnerProperty.getHpMax()));
    partner.setMaxLife(Integer.valueOf(sysPartnerProperty.getHpMax()));
    SysPartner sysPartner = (SysPartner)PartnerDataPool.partnerMap.get(partner.getPartnerType());
    SkillBox skillBox = excuteSkillBox(partner, sysPartner);
    String skillJson = JsonUtils.object2String(skillBox);
    partner.setSkillBox(skillBox);
    partner.setSkillBoxJson(ZipUtil.compress(skillJson));
  }
  
  public void loadPartner(Role role, Partner partner) {
    partnerInfo(role, partner);
  }
  
  public void partnerInfo(Role role, Partner partner) {
    RespPetInfo petInfo = new RespPetInfo();
    BeanUtils.copyBeanProp(petInfo, partner);
  }
  
  public boolean addExp(Role role, Partner partner, long exp, boolean reason) {
    if (exp <= 0L)
      return false; 
    boolean flag = true;
    short level = partner.getPartnerLv();
    int partnerNextLevel = partner.getPartnerLv();
    int roleGrade = role.getRelive().intValue() * 1000 + role.getLevel().intValue();
    int partnerGrade = partner.getRebirth() * 1000 + partner.getPartnerLv();
    if (partnerGrade > roleGrade) {
      String msg = "伙伴等级不能超过人物等级";
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), msg, false));
      return false;
    } 
    if (partnerGrade >= 3250 && partner.getExp() >= 1500000000L) {
      String msg = String.format("%s伙伴已到达%d转的上限%d级", new Object[] { partner.getPartnerName(), Byte.valueOf(partner.getRebirth()), Short.valueOf(partner.getPartnerLv()) });
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), msg, false));
      return false;
    } 
    String exp_tips = "#<G>" + exp + "#点经验";
    long totalExp = (partner.getExp() + exp > 1500000000L) ? 1500000000L : (partner.getExp() + exp);
    int rebirth = partner.getRebirth();
    long needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_PARTNER.getValue(), rebirth, partnerNextLevel);
    if (totalExp >= needExp && partner.getRebirth() * 1000 + partner.getPartnerLv() < roleGrade) {
      long expForRebirthLevelNext = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_PARTNER.getValue(), rebirth, partnerNextLevel + 1);
      if (expForRebirthLevelNext <= 0L) {
        if (needExp == partner.getExp())
          return false; 
        partner.setExp(needExp);
        updateExp(role, partner);
        partner.save();
        return true;
      } 
      while (totalExp >= needExp) {
        totalExp -= needExp;
        level = (short)(level + 1);
        partner.setExp(totalExp);
        partnerGrade = partner.getRebirth() * 1000 + level;
        if (partnerGrade > roleGrade) {
          totalExp = (partner.getExp() + exp > 1500000000L) ? 1500000000L : (partner.getExp() + exp);
          partner.setExp(totalExp);
          flag = false;
        } 
        if (!flag) {
          if (partner.getExp() >= 1500000000L)
            MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "你的伙伴#<G>" + partner.getPartnerName() + "#等级已超过角色等级8级无法获得经验", true)); 
          updateExp(role, partner);
          break;
        } 
        partner.setPartnerLv(level);
        partnerLevelChangeEvent(role, partner);
        needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_PARTNER.getValue(), rebirth, partner.getPartnerLv());
      } 
    } else {
      partner.setExp(totalExp);
      updateExp(role, partner);
    } 
    partner.save();
    if (reason)
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "你的#<G>" + partner.getPartnerName() + "#获得了#<IR12>#" + exp_tips, true)); 
    return true;
  }
  
  public void updateExp(Role role, Partner partner) {
    RespUpdatePartner respUpdatePartner = new RespUpdatePartner();
    List<FiedValue> list = new ArrayList<>();
    list.add(new FiedValue("exp", (byte)4, String.valueOf(partner.getExp())));
    respUpdatePartner.setId(partner.getPartnerId().intValue());
    respUpdatePartner.setList(list);
    MessagePusher.pushMessage(role, (Message)respUpdatePartner);
  }
  
  public void partnerLevelChangeEvent(Role role, Partner partner) {
    calculateBase(partner);
    Map<Integer, Partner> partnerMap = (Map<Integer, Partner>)DataCache.PARTNER_MAP.get(role.getId());
    partnerMap.put(partner.getPartnerId(), partner);
    sendPartnerPanels(role, partner);
  }
  
  public void sendPartnerPanels(Role role, Partner partner) {
    RespUpdatePartner respUpdatePartner = new RespUpdatePartner();
    List<FiedValue> list = new ArrayList<>();
    respUpdatePartner.setId(partner.getPartnerId().intValue());
    list.add(new FiedValue("level", (byte)3, Short.valueOf(partner.getPartnerLv())));
    list.add(new FiedValue("exp", (byte)4, String.valueOf(partner.getExp())));
    list.add(new FiedValue("relive", (byte)3, Byte.valueOf(partner.getRebirth())));
    list.add(new FiedValue("isRecruit", (byte)1, Byte.valueOf(partner.getIsRecruits())));
    list.add(new FiedValue("isFight", (byte)1, Byte.valueOf(partner.getIsFight())));
    list.add(new FiedValue("dsId", (byte)3, Integer.valueOf(partner.getOperDefaultSkillId())));
    respUpdatePartner.setList(list);
    MessagePusher.pushMessage(role, (Message)respUpdatePartner);
  }
  
  public PartnerFiedValue getFiedValue(Partner partner) {
    PartnerFiedValue partnerFiedValue = new PartnerFiedValue();
    partnerFiedValue.setUid(partner.getPartnerId().intValue());
    partnerFiedValue.setTypeId(partner.getPartnerType().intValue());
    partnerFiedValue.setRelive(partner.getRebirth());
    partnerFiedValue.setLevel(partner.getPartnerLv());
    partnerFiedValue.setExp(String.valueOf(partner.getExp()));
    partnerFiedValue.setIsRecruit(partner.getIsRecruits());
    partnerFiedValue.setIsFight(partner.getIsFight());
    partnerFiedValue.setPartnerOrder(partner.getPartnerOrder());
    partnerFiedValue.setDsId(partner.getOperDefaultSkillId());
    return partnerFiedValue;
  }
  
  public PartnerFiedValue getFiedValue(SysPartner sysPartner) {
    PartnerFiedValue partnerFiedValue = new PartnerFiedValue();
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
  
  public void selectPartnerList(Role role) {
    List<Integer> sysPartnerList = (List<Integer>)PartnerDataPool.partnerMap.entrySet().stream().filter(map -> (((SysPartner)map.getValue()).getIsRecruits() == 1)).map(Map.Entry::getKey).collect(Collectors.toList());
    Map<Integer, Partner> partnerMap = (Map<Integer, Partner>)DataCache.PARTNER_MAP.get(role.getId());
    List<PartnerFiedValue> list = new ArrayList<>();
    if (partnerMap.size() <= sysPartnerList.size()) {
      for (Map.Entry<Integer, Partner> entry : partnerMap.entrySet()) {
        Partner partner = entry.getValue();
        if (sysPartnerList.contains(partner.getPartnerType())) {
          PartnerFiedValue fiedValue = getFiedValue(partner);
          list.add(fiedValue);
          sysPartnerList.remove(partner.getPartnerType());
        } 
      } 
      if (sysPartnerList.size() > 0)
        for (int i = sysPartnerList.size() - 1; i >= 0; i--) {
          Integer sysPartnerSerial = sysPartnerList.get(i);
          SysPartner sysPartner = (SysPartner)PartnerDataPool.partnerMap.get(sysPartnerSerial);
          if (sysPartner != null) {
            PartnerFiedValue fiedValue = getFiedValue(sysPartner);
            list.add(fiedValue);
            sysPartnerList.remove(i);
          } 
        }  
    } 
    MessagePusher.pushMessage(role, (Message)new RespPartnerList(list));
  }
  
  public void selectFightPartnerList(Role role) {
    ArrayList<Partner> partners = selectPartnerForFight(role);
    if (partners.size() <= 0)
      return; 
    List<PartnerFiedValue> list = new ArrayList<>();
    for (Partner partner : partners) {
      PartnerFiedValue fiedValue = getFiedValue(partner);
      list.add(fiedValue);
    } 
    MessagePusher.pushMessage(role, (Message)new RespPartnerList(list));
  }
  
  public void relive(Role role, int serial) {
    Partner partner = (Partner)((Map)DataCache.PARTNER_MAP.get(role.getId())).get(Integer.valueOf(serial));
    if (partner != null && partner.getRebirth() < 3 && partner.getRebirth() < role.getRelive().intValue()) {
      SysRbAttr sysRbAttr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf(partner.getRebirth() + 2));
      partner.setRebirth((byte)(partner.getRebirth() + 1));
      partner.setPartnerLv(sysRbAttr.getInitLv().shortValue());
      partnerLevelChangeEvent(role, partner);
      partner.save();
    } 
  }
  
  public void feedPartner(Role role, Partner partner, String type, int param) {
    FeedPartnerHandler handler = FeedPartnerHelper.INSTANCE.getHandler(type);
    if (handler == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("很抱歉，该功能暂未开放。"));
      return;
    } 
    String msg = handler.acceptFeed(partner, role, Integer.valueOf(param), type, new Object[0]);
    if (!Strings.isEmpty(msg))
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), msg, false)); 
  }
  
  private void excuteTeam(Role role) {
    TeamService teamService = (TeamService)SpringUtils.getBean(TeamService.class);
    Team team = teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      Member member = team.getList().get(0);
      if (member.getRoleId().equals(role.getRoleId())) {
        Map<Integer, Partner> partnerMap = (Map<Integer, Partner>)DataCache.PARTNER_MAP.get(role.getId());
        if (partnerMap == null || partnerMap.size() <= 0)
          return; 
        List<Partner> partners = (List<Partner>)partnerMap.values().stream().filter(obj -> (obj.getIsFight() == 1)).collect(Collectors.toList());
        ArrayList<Member> listPartnerMembers = new ArrayList<>();
        for (Partner p : partners) {
          Member partnerMember = Member.valueOf(p);
          listPartnerMembers.add(partnerMember);
        } 
        team.setPartner(listPartnerMembers);
        teamService.updateTeamList(role);
      } 
    } 
  }
  
  public Map<Integer, Partner> loadPartner(Role role) {
    Map<Integer, Partner> partnerMap = (Map<Integer, Partner>)DataCache.PARTNER_MAP.get(role.getId());
    if (partnerMap == null) {
      partnerMap = selectPartner(role);
      DataCache.PARTNER_MAP.put(role.getId(), partnerMap);
    } 
    return partnerMap;
  }
  
  public void changeDefaultSkill(Role role, int pId, int skillId) {
    Map<Integer, Partner> partnerMap = (Map<Integer, Partner>)DataCache.PARTNER_MAP.get(role.getId());
    Partner partner = null;
    for (Map.Entry<Integer, Partner> entry : partnerMap.entrySet()) {
      if (pId == ((Partner)entry.getValue()).getPartnerId().intValue()) {
        partner = entry.getValue();
        break;
      } 
    } 
    if (partner != null) {
      SkillBox skillBox = partner.getSkillBox();
      if (skillBox != null) {
        Map<Integer, RoleSkill> skills = skillBox.getSkills();
        RoleSkill roleSkill = skills.get(Integer.valueOf(skillId));
        if (roleSkill != null) {
          partner.setOperDefaultSkillId(roleSkill.getSkillId().intValue());
          partner.save();
        } 
      } 
    } 
    sendPartnerPanels(role, partner);
  }
}
