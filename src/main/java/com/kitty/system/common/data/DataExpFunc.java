package com.kitty.system.common.data;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.item.ItemClassify;

public class DataExpFunc {
  public static long calculateDls(int grade, int rei, int type, int multiple) {
    if (type == ItemClassify.HERO_EXP.getId())
      return BigDecimalUtil.decimalScaleDown(0, 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 60.0D * 1.0D * 1.5D).longValue() * multiple;
    if (type == ItemClassify.PET_EXP.getId())
      return BigDecimalUtil.decimalScaleDown(0, 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 60.0D * 1.0D * 1.2D * 1.5D).longValue() * multiple;
    return 0L;
  }
  
  public static long calculateKj(int grade, int rei, int type, int multiple) {
    if (type == ItemClassify.HERO_EXP.getId())
      return BigDecimalUtil.decimalScaleDown(0, 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 3.5D) / 30.0D / 60.0D * 1.0D * 1.8D).longValue() * multiple;
    if (type == ItemClassify.PET_EXP.getId())
      return BigDecimalUtil.decimalScaleDown(0, 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 3.5D) / 30.0D / 60.0D * 1.0D * 1.2D * 1.8D).longValue() * multiple;
    return 0L;
  }
}
