package com.kitty.common.utils;

import java.lang.reflect.Array;

public class ReflectUtil
{
    public ReflectUtil() {
    }
    
    public static Object newArray(Class<?> clazz, Class<?> wrapper, int size) {
        String name = clazz.getName();
        int n = -1;
        switch (name.hashCode()) {
            case 2911: {
                if (name.equals("[Z")) {
                    n = 0;
                    break;
                }
                break;
            }
            case 2887: {
                if (name.equals("[B")) {
                    n = 1;
                    break;
                }
                break;
            }
            case 2888: {
                if (name.equals("[C")) {
                    n = 2;
                    break;
                }
                break;
            }
            case 2904: {
                if (name.equals("[S")) {
                    n = 3;
                    break;
                }
                break;
            }
            case 2894: {
                if (name.equals("[I")) {
                    n = 4;
                    break;
                }
                break;
            }
            case 2895: {
                if (name.equals("[J")) {
                    n = 5;
                    break;
                }
                break;
            }
            case 2891: {
                if (name.equals("[F")) {
                    n = 6;
                    break;
                }
                break;
            }
            case 68: {
                if (name.equals("D")) {
                    n = 7;
                    break;
                }
                break;
            }
        }
        switch (n) {
            case 0: {
                return new boolean[size];
            }
            case 1: {
                return new byte[size];
            }
            case 2: {
                return new char[size];
            }
            case 3: {
                return new short[size];
            }
            case 4: {
                return new int[size];
            }
            case 5: {
                return new long[size];
            }
            case 6: {
                return new float[size];
            }
            case 7: {
                return new double[size];
            }
            default: {
                return Array.newInstance(wrapper, size);
            }
        }
    }
}
