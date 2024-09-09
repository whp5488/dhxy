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
    
    public QuestionController() {
    }
    
    @RequestMapping
    public void submitQuestionDls(IoSession session, ReqSubmitQuestion submitQuestion) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.questionService.submitQuestionDls(role, submitQuestion);
        return;
    }
    
    @RequestMapping
    public void submitQuestionXs(IoSession session, ReqSubmitQuestionCountry submitQuestionCountry) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.questionService.submitQuestionXs(role, submitQuestionCountry);
        return;
    }
    
    @RequestMapping
    public void submitQuestionSs(IoSession session, ReqSubmitQuestionProvince submitQuestionProvince) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.questionService.submitQuestionSs(role, submitQuestionProvince);
        return;
    }
    
    @RequestMapping
    public void submitQuestionDs(IoSession session, ReqSubmitQuestionHall submitQuestionHall) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.questionService.submitQuestionDs(role, submitQuestionHall);
        return;
    }
}
