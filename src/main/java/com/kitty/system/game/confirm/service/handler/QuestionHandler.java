package com.kitty.system.game.confirm.service.handler;

import com.kitty.cache.RedisService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.confirm.handler.ConfirmHandler;
import com.kitty.system.game.confirm.message.ReqConfirmResult;
import com.kitty.system.game.confirm.model.QuestionKjConfirm;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.domain.vo.RecordQuestion;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.vo.Npc;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class QuestionHandler extends ConfirmHandler {
  @Resource
  private RedisService redisService;
  
  private static final String select = "1";
  
  public void handleConfirmResult(Role role, ReqConfirmResult reqConfirmResult) {
    String[] split = reqConfirmResult.getSelect().split("-");
    QuestionKjConfirm confirm = (QuestionKjConfirm)role.getConfirm();
    byte type = confirm.getType();
    if ("1".equals(split[0]))
      if (type == 0) {
        RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("country:" + role.getId(), RecordQuestion.class);
        if (recordQuestion != null && recordQuestion.getS() <= 0 && recordQuestion.getE() <= 0) {
          ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).conductQuestionXs(role, recordQuestion, new Npc((SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(1730001))), true);
          return;
        } 
      } else if (type == 1) {
        RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("province:" + role.getId(), RecordQuestion.class);
        if (recordQuestion != null && recordQuestion.getS() <= 0 && recordQuestion.getE() <= 0) {
          recordQuestion.setT(Byte.valueOf(split[1]).byteValue());
          ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).conductQuestionSs(role, recordQuestion, new Npc((SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(1730002))), true);
          return;
        } 
      }  
  }
}
