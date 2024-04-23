package com.kitty.system.game.vo;

import com.kitty.system.game.vo.init.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ItemEasyInfo {
  private Integer serial;
  
  private String name;

  private Integer price = Integer.valueOf(0);

  private Integer reprice = Integer.valueOf(0);
  
  private Integer shape;

  private Integer costType = Integer.valueOf(0);

  private Integer canMerge = Integer.valueOf(1);

  private Integer batches = Integer.valueOf(0);

  private Integer value = Integer.valueOf(0);

  private Integer itemPJ = Integer.valueOf(1);

  private Integer isOpen = Integer.valueOf(0);

  private Integer type = Integer.valueOf(0);
  
  private List<Item> extra;

  private Integer cycle = Integer.valueOf(0);

  private Integer category = Integer.valueOf(0);

  private Integer useCntLimit = Integer.valueOf(-1);

  private Integer applyObjectSerial = Integer.valueOf(0);
  
  private String dict;

}
