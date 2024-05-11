
package com.kitty.common.utils;

import java.util.ArrayList;
import java.util.List;

public class Split
{
    public List<String> StringSplit(final String str) {
        final String[] resu = str.split("\\|");
        final List list = new ArrayList();
        for (final String k : resu) {
            list.add(k);
        }
        return list;
    }
}
