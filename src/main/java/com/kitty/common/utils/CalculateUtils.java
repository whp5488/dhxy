package com.kitty.common.utils;

import java.math.BigDecimal;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CalculateUtils {
  public static BigDecimal calculate(String str) {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("js");
    Object result = null;
    try {
      result = engine.eval(str);
      return new BigDecimal(result.toString());
    } catch (ScriptException e) {
      e.printStackTrace();
      return new BigDecimal(0);
    } 
  }
}
