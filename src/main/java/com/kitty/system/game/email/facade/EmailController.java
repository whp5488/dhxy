
package com.kitty.system.game.email.facade;

import com.kitty.system.game.email.message.ReqEmaiLPickUpGoods;
import com.kitty.system.game.email.message.ReqEmailDetail;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.email.message.ReqEmailList;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.email.service.EmailService;
import org.springframework.stereotype.Controller;

@Controller
public class EmailController
{
    @Autowired
    private EmailService emailService;
    
    @RequestMapping
    public void getEmailList(final IoSession session, final ReqEmailList reqEmailList) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.emailService.getEmailList(role);
    }
    
    @RequestMapping
    public void getEmailDetail(final IoSession session, final ReqEmailDetail reqEmailDetail) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final String emailIdStr = reqEmailDetail.getEmailId();
        final byte isSystem = reqEmailDetail.getIsSystem();
        if (emailIdStr != null) {
            try {
                final long emailId = Long.parseLong(emailIdStr);
                if (emailId > 0L) {
                    this.emailService.getEmailDetail(role, emailId, isSystem);
                }
            }
            catch (final NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
    
    @RequestMapping
    public void pickUpGoods(final IoSession session, final ReqEmaiLPickUpGoods reqEmaiLPickUpGoods) {
        final String emailIdStr = reqEmaiLPickUpGoods.getEmailId();
        long emailId = -1L;
        try {
            emailId = Long.parseLong(emailIdStr);
        }
        catch (final NumberFormatException e) {
            return;
        }
        final Role role = SessionUtils.getRoleBySession(session);
        if (emailId > 0L && role != null) {
            final byte type = reqEmaiLPickUpGoods.getType();
            if (type == 0) {
                this.emailService.deleteEmail(role, emailId);
            }
            else if (type == 1) {
                this.emailService.pickUpGoods(role, emailId);
            }
        }
    }
}
