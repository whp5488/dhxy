package com.kitty.system.game.equip.helper;

import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.itempro.def.CONVERT_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.message.RespUpgradePreview;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysPurifyAttr;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class UpgradeHandler {
  protected Logger logger = LoggerFunction.UPGRADE_EQUIP.getLogger();
  
  protected IdWorker worker = (IdWorker)SpringUtils.getBean(IdWorker.class);
  
  protected EquipService equipService = (EquipService)SpringUtils.getBean(EquipService.class);
  
  protected BagService bagService = (BagService)SpringUtils.getBean(BagService.class);
  
  protected RoleService roleService = SpringUtils.getRoleService();
  
  protected ChatService chatService = (ChatService)SpringUtils.getBean(ChatService.class);
  
  static final String ROLE_VACANCY_NOT_ENOUGH = "很抱歉，你的包裹空位不足，请整理后再来吧。";
  
  static final String ERROR_TYPE = "操作的类型不正确";
  
  int getVacancy(Role role) {
    return this.bagService.getPos(role, false);
  }
  
  public EquipInfo getEquip(Role role, int pos, Long id) {
    EquipInfo equipInfo = new EquipInfo();
    BagEquip equip = new BagEquip();
    try {
      if (pos < 0) {
        boolean flag = true;
        List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
        for (WearEquip wearEquip : wearEquips) {
          if (wearEquip != null && wearEquip.getId() != null && wearEquip.getId().equals(id)) {
            flag = false;
            BeanUtils.copyBeanProp(equip, wearEquip);
          } 
        } 
        if (flag) {
          equip = null;
          equipInfo.setTips("身上没有穿戴该道具");
        } 
      } else {
        Map<Long, BagEquip> bagEquip = this.bagService.getBagEquip(role);
        equip = bagEquip.get(id);
        if (equip == null)
          equipInfo.setTips("背包没有该道具"); 
      } 
      if (equip != null && equip.getId() == null) {
        equip = null;
        equipInfo.setTips("获取装备信息错误");
        LoggerUtils.info("玩家[{}]获取装备信息错误：装备ID = {}，位置 = {}" + role.getRoleId(), new Object[] { id, Integer.valueOf(pos) });
      } 
      equipInfo.setEquip(equip);
    } catch (ConcurrentModificationException e) {
      e.getMessage();
    } 
    return equipInfo;
  }
  
  public void refresh(Role role, BagEquip equip, int pos, Long id, boolean flag) {
    try {
      if (pos < 0) {
        List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
        WearEquip wearEquip = new WearEquip();
        BeanUtils.copyBeanProp(wearEquip, equip);
        for (int i = 0; i < wearEquips.size(); i++) {
          WearEquip wear = wearEquips.get(i);
          if (wear.getId().equals(id)) {
            wearEquips.set(i, wearEquip);
            break;
          } 
        } 
        wearEquip.save();
        if (flag)
          this.equipService.changeEquip(role, equip, wearEquip, true); 
      } else {
        Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
        bagEquipMap.put(equip.getId(), equip);
        equip.save();
        if (flag)
          this.bagService.refreshRoleEquip(role, equip, false); 
      } 
    } catch (ConcurrentModificationException concurrentModificationException) {}
  }
  
  public void animation(Role role, BagEquip equip, BagEquip newEq, int level) {
    RespUpgradePreview preview = new RespUpgradePreview();
    List<FiedValue> listOne = new ArrayList<>();
    for (FiedEquip fiedEquip : equip.getField())
      listOne.add(new FiedValue(String.valueOf(fiedEquip.getType()), (byte)3, fiedEquip.getValue())); 
    List<FiedValue> listTwo = new ArrayList<>();
    for (FiedEquip fiedEquip : newEq.getField())
      listTwo.add(new FiedValue(String.valueOf(fiedEquip.getType()), (byte)3, fiedEquip.getValue())); 
    preview.setListOne(listOne);
    preview.setListTwo(listTwo);
    preview.setLevel(level);
    preview.setName(newEq.getName());
    preview.setSerial(newEq.getType());
    MessagePusher.pushMessage(role, (Message)preview);
  }
  
  public int getPj(int pj) {
    String value = String.valueOf(pj);
    if (value.length() >= 2)
      return Integer.valueOf(value.substring(1, 2)).intValue(); 
    return 0;
  }
  
  public int fieldSerial(List<FiedEquip> field) {
    int[] serial = { 0 };
    field.forEach(e -> {
          if (e.getType().equals(Integer.valueOf(6680))) {
            serial[0] = e.getValue().intValue();
            return;
          } 
        });
    return serial[0];
  }
  
  public BagEquip clearHistory(BagEquip equip) {
    equip.setRedoGeneralHistory(new ArrayList());
    equip.setRedoHighHistory(new ArrayList());
    equip.setRedoSuperHistory(new ArrayList());
    equip.setArtificeGeneralHistory(new ArrayList());
    equip.setArtificeHighHistory(new ArrayList());
    equip.setArtificeSuperHistory(new ArrayList());
    equip.setPurifyGeneralHistory(new ArrayList());
    equip.setPurifyHighHistory(new ArrayList());
    equip.setPurifySuperHistory(new ArrayList());
    return equip;
  }
  
  public FiedEquip getPurify(List<FiedEquip> field, int pj) {
    if (pj >= 5) {
      List<SysPurifyAttr> list = EquipDataPool.purify;
      for (FiedEquip fiedEquip : field) {
        SysPurifyAttr purifyAttr = list.stream().filter(e -> (fiedEquip.getType().intValue() - PRO_DICT_SERIAL.EQUIP_PRO.getSerial() == CONVERT_DICT.getDist(e.getDict()))).findFirst().orElse(null);
        if (purifyAttr != null)
          return fiedEquip; 
      } 
    } 
    return null;
  }
  
  public int expend(int type) {
    int count = 1;
    if (type == 2) {
      count = 5;
    } else if (type == 3) {
      count = 20;
    } 
    return count;
  }
  
  public BagEquip remoldSave(BagEquip equip, List<List<FiedEquip>> redo, int type) {
    if (redo.size() > 0)
      if (type == 1) {
        equip.setRedoGeneralHistory(redo.get(0));
      } else if (type == 2) {
        equip.setRedoHighHistory(redo);
      } else if (type == 3) {
        equip.setRedoSuperHistory(redo);
      }  
    return equip;
  }
  
  public BagEquip remoldPurifySave(BagEquip equip, List<List<FiedEquip>> redo, int type) {
    if (redo.size() > 0)
      if (type == 1) {
        equip.setPurifyGeneralHistory(redo.get(0));
      } else if (type == 2) {
        equip.setPurifyHighHistory(redo);
      } else if (type == 3) {
        equip.setPurifySuperHistory(redo);
      }  
    return equip;
  }
  
  public BagEquip refineSave(BagEquip equip, List<List<FiedEquip>> refine, int type) {
    if (refine.size() > 0)
      if (type == 1) {
        equip.setArtificeGeneralHistory(refine.get(0));
      } else if (type == 2) {
        equip.setArtificeHighHistory(refine);
      } else if (type == 3) {
        equip.setArtificeSuperHistory(refine);
      }  
    return equip;
  }
  
  public boolean deduct(Role role, Map<Integer, Integer> consume) {
    for (Integer key : consume.keySet()) {
      Integer amount = consume.get(key);
      if (key.equals(Const.RESTYPE_SILVER)) {
        boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, amount.intValue(), "装备打造实现类");
        if (!b)
          return false; 
        continue;
      } 
      if (key.equals(Const.RESTYPE_COIN)) {
        boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, amount.intValue(), "装备打造实现类");
        if (!b)
          return false; 
        continue;
      } 
      Integer integer = this.bagService.consumeBagGoods(role, key.intValue(), amount.intValue(), true, true, "装备打造");
      if (integer != null)
        return false; 
    } 
    return true;
  }
  
  public abstract String acceptUpgrade(Role paramRole, Integer paramInteger1, Integer paramInteger2, String paramString1, String paramString2);
}
