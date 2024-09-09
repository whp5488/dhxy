package com.kitty.system.game.pet.handler;

import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
import java.util.List;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.pet.message.RespPetEffctInfo;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import java.text.MessageFormat;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import java.util.ArrayList;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("GridHandler")
public class GridHandler extends FeedPetHandler
{
    private static final Integer GOODS_SERIAL;
    private static final String SUCCESS;
    private static final String FAIL = "聚魄丹不足{0}颗";
    private static final String ERROR;
    
    public GridHandler() {
    }
    
    @Override
    public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
        int need = 1;
        String msg = null;
        boolean flag = false;
        int pos = (int)param / 1000 - 1;
        int genre = (int)param % 1000;
        if (((PetSkill)pet.getSkills().get(pos)).getSerial() >= 0) {
            return "该技能格子已经解封了";
        }
        List<Integer> unset = new ArrayList<>();
        for (int i = 0; i < pet.getSkills().size(); ++i) {
            PetSkill petSkill = (PetSkill)pet.getSkills().get(i);
            if (petSkill.getSerial() >= 0 && petSkill.getType() != PETSKILLSUB.SUBTYPE_SPECIAL.getValue() && petSkill.getType() != PETSKILLSUB.SUB_TYPE_SOUL.getValue()) {
                unset.add(Integer.valueOf(i));
            }
        }
        if (need > 0) {
            if (genre == 1) {
                need = this.need(unset.size() + 1);
                msg = MessageFormat.format(GridHandler.SUCCESS, "#<P>[聚魄丹]#x" + need, pet.getPetName(), Integer.valueOf(unset.size() + 1));
                flag = true;
            }
            else if (genre == 2) {
                flag = this.ratio();
                if (flag) {
                    msg = MessageFormat.format(GridHandler.SUCCESS, "#<P>[聚魄丹]#", pet.getPetName(), Integer.valueOf(unset.size() + 1));
                    flag = true;
                }
                else {
                    msg = GridHandler.ERROR;
                }
            }
            Integer goods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, (int)GridHandler.GOODS_SERIAL, need, true, true, "开启技能格子");
            if (goods != null) {
                msg = MessageFormat.format("聚魄丹不足{0}颗", Integer.valueOf(need));
                flag = false;
            }
        }
        else {
            msg = "召唤兽技能格子已全部开启";
        }
        if (flag) {
            ((PetSkill)pet.getSkills().get(pos)).setSerial(0);
            pet.save();
            MessagePusher.pushMessage(role, new RespPetEffctInfo(role.getRoleId(), pet.getPetId(), pet.getSkills()));
            return msg;
        }
        return msg;
    }
    
    public int need(int index) {
        int need = -1;
        if (index == 2) {
            return 1;
        }
        if (index == 3) {
            return 2;
        }
        if (index == 4) {
            return 4;
        }
        if (index == 5) {
            return 6;
        }
        if (index == 6) {
            return 10;
        }
        if (index == 7) {
            return 15;
        }
        if (index == 8) {
            return 25;
        }
        if (index == 9) {
            return 35;
        }
        if (index == 10) {
            return 50;
        }
        if (index == 11) {
            return 100;
        }
        if (index == 12) {
            return 200;
        }
        return need;
    }
    
    public boolean ratio() {
        double pow = Math.pow(0.35, BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(RandomUtil.round(Integer.valueOf(4), Integer.valueOf(6)))));
        return SkillAi.canAffectOnPro(pow);
    }
    
    static {
        GOODS_SERIAL = Integer.valueOf(75024);
        SUCCESS = "消耗#<II" + GridHandler.GOODS_SERIAL + ">#{0}，你的#<G>{1}#成功开启#<G>第{2}技能格#";
        ERROR = "消耗#<II" + GridHandler.GOODS_SERIAL + ">#，开启失败，少侠再接再厉！";
    }
}
