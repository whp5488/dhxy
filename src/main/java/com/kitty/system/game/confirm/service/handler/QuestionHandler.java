
package com.kitty.system.game.confirm.service.handler;

import com.kitty.system.game.vo.Npc;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import java.io.Serializable;
import com.kitty.system.game.task.domain.vo.RecordQuestion;
import com.kitty.system.game.confirm.model.QuestionKjConfirm;
import com.kitty.system.game.confirm.message.ReqConfirmResult;
import com.kitty.system.game.role.domain.Role;
import javax.annotation.Resource;
import com.kitty.cache.RedisService;
import org.springframework.stereotype.Component;
import com.kitty.system.game.confirm.handler.ConfirmHandler;

@Component
public class QuestionHandler extends ConfirmHandler
{
    @Resource
    private RedisService redisService;
    private static final String select = "1";
    
    @Override
    public void handleConfirmResult(final Role role, final ReqConfirmResult reqConfirmResult) {
        final String[] split = reqConfirmResult.getSelect().split("-");
        final QuestionKjConfirm confirm = (QuestionKjConfirm)role.getConfirm();
        final byte type = confirm.getType();
        if ("1".equals(split[0])) {
            if (type == 0) {
                final RecordQuestion recordQuestion = this.redisService.get("country:" + role.getId(), RecordQuestion.class);
                if (recordQuestion != null && recordQuestion.getS() <= 0 && recordQuestion.getE() <= 0) {
                    SpringUtils.getBean(KejuTaskHandler.class).conductQuestionXs(role, recordQuestion, new Npc(DataCache.PLACE_NPC.get(1730001)), true);
                }
            }
            else if (type == 1) {
                final RecordQuestion recordQuestion = this.redisService.get("province:" + role.getId(), RecordQuestion.class);
                if (recordQuestion != null && recordQuestion.getS() <= 0 && recordQuestion.getE() <= 0) {
                    recordQuestion.setT(Byte.valueOf(split[1]));
                    SpringUtils.getBean(KejuTaskHandler.class).conductQuestionSs(role, recordQuestion, new Npc(DataCache.PLACE_NPC.get(1730002)), true);
                }
            }
        }
    }
}
