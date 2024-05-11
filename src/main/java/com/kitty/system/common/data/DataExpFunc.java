
package com.kitty.system.common.data;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.item.ItemClassify;

public class DataExpFunc
{
    public static long calculateDls(final int grade, final int rei, final int type, final int multiple) {
        if (type == ItemClassify.HERO_EXP.getId()) {
            return BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 60.0 * 1.0 * 1.5).longValue() * multiple;
        }
        if (type == ItemClassify.PET_EXP.getId()) {
            return BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 60.0 * 1.0 * 1.2 * 1.5).longValue() * multiple;
        }
        return 0L;
    }
    
    public static long calculateKj(final int grade, final int rei, final int type, final int multiple) {
        if (type == ItemClassify.HERO_EXP.getId()) {
            return BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 3.5) / 30.0 / 60.0 * 1.0 * 1.8).longValue() * multiple;
        }
        if (type == ItemClassify.PET_EXP.getId()) {
            return BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 3.5) / 30.0 / 60.0 * 1.0 * 1.2 * 1.8).longValue() * multiple;
        }
        return 0L;
    }
}
