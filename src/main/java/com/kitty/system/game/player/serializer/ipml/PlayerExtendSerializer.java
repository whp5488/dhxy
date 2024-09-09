package com.kitty.system.game.player.serializer.ipml;

import com.kitty.system.game.player.bean.PlayerExtendBox;
import org.apache.commons.lang3.StringUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.player.serializer.IPlayerPropSerializer;

public class PlayerExtendSerializer implements IPlayerPropSerializer
{
    public PlayerExtendSerializer() {
    }
    
    @Override
    public void serialize(Role player) {
        if (player.getExtendBox() != null) {
            String extendBoxJson = JsonUtils.object2String(player.getExtendBox());
            player.setExtendBoxJson(ZipUtil.compress(extendBoxJson));
        }
    }
    
    @Override
    public void deserialize(Role player) {
        String json = player.getExtendBoxJson();
        if (StringUtils.isNotEmpty(json)) {
            String content = ZipUtil.uncompress(json);
            player.setExtendBox((PlayerExtendBox)JsonUtils.string2Object(content, PlayerExtendBox.class));
        }
    }
    
    public static void main(String[] args) {
        String content = ZipUtil.uncompress("H4sIAAAAAAAAAFVSTW/bMAz9Lzr7IGdpm/g2rB8YsAEd0tswBLLN2kRlyZXoBVmQ/z5K9GbXBz9RJh+f+XhRkQyBqi7XQo0mmCHOZx+R0DtV6SLlBFVtCtWa84/Jk/mGA5IkDrH7bkZV/fxVKDORf8SupwOkr4rOI1OXTPCG1n5t89k0wstHpu1SZqn1ZrPfbQtV+5BaXoXrM1HAWvqYAM4cGh8gS2r8UBt6TtV7zeX6pmTCEejhfcLx+YN6oKzwosBGOCbi42tSmTXku7Y+xhGgPcben5b+j5IlE7BWVTtWaNxLMK2oqKczS3gK2ObwfT0bVYrwjcAnga3AjcCtwJ3ATmAvUOoZZ55yJipnpjJTZavQ0Wzb69s46+VBfZiCM36ZXjetgjqYZjXYPpmyxNa7bokIyYJMk7v1YMflm8NmVdfiqoOfXPvkLU9po9PDQsFQXDLihOkX0hIFYMPylr3gkNbnTu+3u/I2VZmmR/gNAzhaaeJU+8U74luhcHDi/QDeWTdZy+yNCITTPUZeqeafPzrbo7M7Opujszc6W6OzM2kZ2oDJ/rTJ2Ll7c/7PzGE9Hfi9vjngnyTt+heyEobpXgMAAA==");
        System.out.println(content);
    }
}
