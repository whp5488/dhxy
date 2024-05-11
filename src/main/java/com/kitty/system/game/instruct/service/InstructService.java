
package com.kitty.system.game.instruct.service;

import com.kitty.system.game.instruct.handler.MonsterHandler;
import com.kitty.system.game.instruct.handler.TianFuHandler;
import com.kitty.system.game.instruct.handler.PayHandler;
import com.kitty.system.game.instruct.handler.PartnerHandler;
import com.kitty.system.game.instruct.handler.CsHandler;
import com.kitty.system.game.instruct.handler.CurrencyHandler;
import com.kitty.system.game.instruct.handler.EquipHandler;
import com.kitty.system.game.instruct.handler.PetHandler;
import com.kitty.system.game.instruct.handler.SuitHandler;
import com.kitty.system.game.instruct.handler.ItemHandler;
import com.kitty.system.game.instruct.handler.ExpHandler;
import com.kitty.system.game.instruct.handler.EscHandler;
import com.kitty.system.game.instruct.handler.WarHandler;
import com.kitty.system.game.instruct.inter.NcFather;
import java.util.HashMap;
import org.springframework.stereotype.Service;

@Service
public class InstructService
{
    HashMap<String, NcFather> instruct;
    
    public InstructService() {
        this.instruct = new HashMap<String, NcFather>();
    }
    
    public void init() {
        this.instruct.put("@war", new WarHandler());
        this.instruct.put("@esc", new EscHandler());
        this.instruct.put("@exp", new ExpHandler());
        this.instruct.put("@item", new ItemHandler());
        this.instruct.put("@suit", new SuitHandler());
        this.instruct.put("@pet", new PetHandler());
        this.instruct.put("@equip", new EquipHandler());
        this.instruct.put("@currency", new CurrencyHandler());
        this.instruct.put("@cs", new CsHandler());
        this.instruct.put("@partner", new PartnerHandler());
        this.instruct.put("@pay", new PayHandler());
        this.instruct.put("@tianfu", new TianFuHandler());
        this.instruct.put("@monster", new MonsterHandler());
    }
    
    public HashMap<String, NcFather> getInstruct() {
        return this.instruct;
    }
}
