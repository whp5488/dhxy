
package com.kitty.system.game.fightnew.messages;

import java.util.Collection;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import java.util.List;

public class RespCombats
{
    private List<FightMember> list;
    
    public List<FightMember> getList() {
        return this.list;
    }
    
    public void setList(final List<FightMember> list) {
        this.list = list;
    }
    
    public void addList(final List<FightMember> fightMembers) {
        if (fightMembers == null || fightMembers.size() < 0) {
            return;
        }
        if (this.list == null) {
            this.list = fightMembers;
        }
        else {
            this.list.addAll(fightMembers);
        }
    }
}
