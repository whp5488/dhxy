
package com.kitty.common.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;

public class CalculateUtils
{
    public static BigDecimal calculate(final String str) {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine engine = manager.getEngineByName("js");
        Object result = null;
        try {
            result = engine.eval(str);
            return new BigDecimal(result.toString());
        }
        catch (final ScriptException e) {
            e.printStackTrace();
            return new BigDecimal(0);
        }
    }
}
