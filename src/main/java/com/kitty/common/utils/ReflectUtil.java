
package com.kitty.common.utils;

import java.lang.reflect.Array;

public class ReflectUtil
{
    public static Object newArray(final Class<?> clazz, final Class<?> wrapper, final int size) {
        final String name2;
        final String name = name2 = clazz.getName();
        switch (name2) {
            case "[Z": {
                return new boolean[size];
            }
            case "[B": {
                return new byte[size];
            }
            case "[C": {
                return new char[size];
            }
            case "[S": {
                return new short[size];
            }
            case "[I": {
                return new int[size];
            }
            case "[J": {
                return new long[size];
            }
            case "[F": {
                return new float[size];
            }
            case "D": {
                return new double[size];
            }
            default: {
                return Array.newInstance(wrapper, size);
            }
        }
    }
}
