
package com.kitty.system.game.task.facade;

import com.kitty.system.game.task.message.ReqSubmitQuestionHall;
import com.kitty.system.game.task.message.ReqSubmitQuestionProvince;
import com.kitty.system.game.task.message.ReqSubmitQuestionCountry;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.task.message.ReqSubmitQuestion;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.task.service.QuestionService;
import org.springframework.stereotype.Controller;

@Controller
public class QuestionController
{
    @Autowired
    QuestionService questionService;
    
    @RequestMapping
    public void submitQuestionDls(final IoSession session, final ReqSubmitQuestion submitQuestion) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.questionService.submitQuestionDls(role, submitQuestion);
    }
    
    @RequestMapping
    public void submitQuestionXs(final IoSession session, final ReqSubmitQuestionCountry submitQuestionCountry) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.questionService.submitQuestionXs(role, submitQuestionCountry);
    }
    
    @RequestMapping
    public void submitQuestionSs(final IoSession session, final ReqSubmitQuestionProvince submitQuestionProvince) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.questionService.submitQuestionSs(role, submitQuestionProvince);
    }
    
    @RequestMapping
    public void submitQuestionDs(final IoSession session, final ReqSubmitQuestionHall submitQuestionHall) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.questionService.submitQuestionDs(role, submitQuestionHall);
    }
}
