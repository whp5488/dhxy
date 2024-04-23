package com.kitty.system.common.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.nutz.castor.Castors;
import org.nutz.el.El;
import org.nutz.lang.Each;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.lang.born.Borning;
import org.nutz.lang.util.NutBean;
import org.nutz.lang.util.Regex;
import org.nutz.lang.util.Region;

public class NutConcurrentMap extends ConcurrentHashMap<String, Object> implements NutBean {
  private static final long serialVersionUID = 1L;
  
  private NutConcurrentMap _map;
  
  public static NutConcurrentMap WRAP(Map<String, Object> map) {
    if (null == map)
      return null; 
    if (map instanceof NutConcurrentMap)
      return (NutConcurrentMap)map; 
    return new NutConcurrentMap(map);
  }
  
  public NutConcurrentMap() {}
  
  public NutConcurrentMap(Map<String, Object> map) {
    putAll(map);
  }
  
  public NutConcurrentMap(String json) {
    putAll((Map<? extends String, ?>)Lang.map(json));
  }
  
  public NutConcurrentMap(String key, Object value) {
    put(key, value);
  }
  
  public void setOrRemove(String key, Object v) {
    if (null == v) {
      remove(key);
    } else {
      put(key, v);
    } 
  }
  
  public static NutConcurrentMap NEW() {
    return new NutConcurrentMap();
  }
  
  public static NutConcurrentMap WRAP(String json) {
    return new NutConcurrentMap(json);
  }
  
  public boolean has(String key) {
    return (null != get(key));
  }
  
  public boolean is(String key, Object val) {
    Object obj = get(key);
    if (null == obj && null == val)
      return true; 
    if (null == obj || null == val)
      return false; 
    return obj.equals(val);
  }
  
  public NutConcurrentMap duplicate() {
    NutConcurrentMap map = new NutConcurrentMap();
    map.putAll(this);
    return map;
  }
  
  public NutConcurrentMap pick(String... keys) {
    if (keys.length == 0)
      return new NutConcurrentMap(); 
    NutConcurrentMap re = new NutConcurrentMap();
    for (String key : keys) {
      Object val = get(key);
      if (null != val)
        re.put(key, val); 
    } 
    return re;
  }
  
  public NutConcurrentMap pickAndRemove(String... keys) {
    if (keys.length == 0)
      return new NutConcurrentMap(); 
    NutConcurrentMap re = new NutConcurrentMap();
    for (String key : keys) {
      Object val = remove(key);
      re.put(key, val);
    } 
    return re;
  }
  
  public NutConcurrentMap pickBy(String regex) {
    if (Strings.isBlank(regex))
      return duplicate(); 
    boolean isNot = regex.startsWith("!");
    Pattern p = Regex.getPattern(isNot ? regex.substring(1) : regex);
    return pickBy(p, isNot);
  }
  
  public NutConcurrentMap pickBy(Pattern p, boolean isNot) {
    if (null == p)
      return isNot ? duplicate() : new NutConcurrentMap(); 
    NutConcurrentMap re = new NutConcurrentMap();
    for (Map.Entry<String, Object> en : entrySet()) {
      String key = en.getKey();
      boolean matched = p.matcher(key).find();
      if (matched) {
        if (!isNot)
          re.put(key, en.getValue()); 
        continue;
      } 
      if (isNot)
        re.put(key, en.getValue()); 
    } 
    return re;
  }
  
  public NutConcurrentMap pickAndRemoveBy(String regex) {
    if (Strings.isBlank(regex))
      return new NutConcurrentMap(); 
    boolean isNot = regex.startsWith("!");
    Pattern p = Pattern.compile(isNot ? regex.substring(1) : regex);
    return pickAndRemoveBy(p, isNot);
  }
  
  public NutConcurrentMap pickAndRemoveBy(Pattern p, boolean isNot) {
    if (null == p) {
      if (isNot) {
        NutConcurrentMap nutConcurrentMap = duplicate();
        clear();
        return nutConcurrentMap;
      } 
      return new NutConcurrentMap();
    } 
    NutConcurrentMap re = new NutConcurrentMap();
    List<String> delKeys = new ArrayList<>(size());
    for (Map.Entry<String, Object> en : entrySet()) {
      String key = en.getKey();
      boolean matched = p.matcher(key).find();
      if (matched) {
        if (!isNot) {
          delKeys.add(key);
          re.put(key, en.getValue());
        } 
        continue;
      } 
      if (isNot) {
        delKeys.add(key);
        re.put(key, en.getValue());
      } 
    } 
    for (String key : delKeys)
      remove(key); 
    return re;
  }
  
  public NutConcurrentMap omit(String... keys) {
    NutConcurrentMap re = new NutConcurrentMap();
    for (Map.Entry<String, Object> en : entrySet()) {
      String key = en.getKey();
      if (!Lang.contains((Object[])keys, key))
        re.put(key, en.getValue()); 
    } 
    return re;
  }
  
  public NutConcurrentMap putDefault(String key, Object dft) {
    if (!has(key))
      put(key, dft); 
    return this;
  }
  
  public boolean containsValue(Object value) {
    if (null == this._map)
      return super.containsValue(value); 
    return (super.containsValue(value) || this._map.containsValue(value));
  }
  
  public boolean containsKey(Object key) {
    if (null == this._map)
      return super.containsKey(key); 
    return (super.containsKey(key) || this._map.containsKey(key));
  }
  
  public ConcurrentHashMap.KeySetView<String, Object> keySet() {
    return super.keySet();
  }
  
  public Collection<Object> values() {
    if (null == this._map)
      return super.values(); 
    List<Object> vals = new LinkedList();
    for (String key : keySet())
      vals.add(get(key)); 
    return vals;
  }
  
  public Set<Map.Entry<String, Object>> entrySet() {
    if (null == this._map)
      return super.entrySet(); 
    HashSet<Map.Entry<String, Object>> vals = new HashSet<>();
    vals.addAll(this._map.entrySet());
    vals.addAll(super.entrySet());
    return vals;
  }
  
  public void clear() {
    super.clear();
    if (null != this._map)
      this._map.clear(); 
  }
  
  public NutConcurrentMap attach(NutConcurrentMap map) {
    this._map = map;
    return this;
  }
  
  public NutConcurrentMap detach() {
    NutConcurrentMap re = this._map;
    this._map = null;
    return re;
  }
  
  public Object get(Object key) {
    if (this._map == null)
      return super.get(key); 
    if (super.containsKey(key))
      return super.get(key); 
    return this._map.get(key);
  }
  
  public Object get(String key, Object dft) {
    Object v = get(key);
    return (null == v) ? dft : v;
  }
  
  public int getInt(String key) {
    return getInt(key, -1);
  }
  
  public int getInt(String key, int dft) {
    Object v = get(key);
    return (null == v) ? dft : ((Integer)Castors.me().castTo(v, int.class)).intValue();
  }
  
  public float getFloat(String key) {
    return getFloat(key, Float.NaN);
  }
  
  public float getFloat(String key, float dft) {
    Object v = get(key);
    return (null == v) ? dft : ((Float)Castors.me().castTo(v, float.class)).floatValue();
  }
  
  public long getLong(String key) {
    return getLong(key, -1L);
  }
  
  public long getLong(String key, long dft) {
    Object v = get(key);
    return (null == v) ? dft : ((Long)Castors.me().castTo(v, long.class)).longValue();
  }
  
  public double getDouble(String key) {
    return getDouble(key, 0.0D);
  }
  
  public double getDouble(String key, double dft) {
    Object v = get(key);
    return (null == v) ? dft : ((Double)Castors.me().castTo(v, double.class)).doubleValue();
  }
  
  public boolean getBoolean(String key) {
    return getBoolean(key, false);
  }
  
  public boolean getBoolean(String key, boolean dft) {
    Object v = get(key);
    return (null == v) ? dft : ((Boolean)Castors.me().castTo(v, boolean.class)).booleanValue();
  }
  
  public String getString(String key) {
    return getString(key, (String)null);
  }
  
  public String getString(String key, String dft) {
    Object v = get(key);
    if (v == null)
      return dft; 
    if (v instanceof CharSequence)
      return v.toString(); 
    if (v instanceof List)
      v = ((List)v).iterator().next(); 
    return (String)Castors.me().castTo(v, String.class);
  }
  
  public Date getTime(String key) {
    return getTime(key, (Date)null);
  }
  
  public Date getTime(String key, Date dft) {
    Object v = get(key);
    return (null == v) ? dft : (Date)Castors.me().castTo(v, Date.class);
  }
  
  public <T extends Enum<T>> T getEnum(String key, Class<T> classOfEnum) {
    String s = getString(key);
    if (Strings.isBlank(s))
      return null; 
    return Enum.valueOf(classOfEnum, s);
  }
  
  public boolean isEnum(String key, Enum<?>... eus) {
    if (null == eus || eus.length == 0)
      return false; 
    try {
      Enum<?> v = (Enum<?>)getEnum(key, eus[0].getClass());
      for (Enum<?> eu : eus) {
        if (!v.equals(eu))
          return false; 
      } 
      return true;
    } catch (Exception e) {
      return false;
    } 
  }
  
  public <T> T getAs(String key, Class<T> classOfT) {
    return getAs(key, classOfT, (T)null);
  }
  
  public <T> T getAs(String key, Class<T> classOfT, T dft) {
    Object v = get(key);
    return (null == v) ? dft : (T)Castors.me().castTo(v, classOfT);
  }
  
  public <T> List<T> getAsList(String key, Class<T> eleType) {
    Object v = get(key);
    if (null == v)
      return null; 
    List<T> list = (List)v;
    ListIterator<T> it = list.listIterator();
    while (it.hasNext()) {
      T ele = it.next();
      if (null != ele && !eleType.isAssignableFrom(ele.getClass())) {
        T ele2 = Castors.me().castTo(ele, eleType);
        it.set(ele2);
      } 
    } 
    return list;
  }
  
  public <T> List<T> getList(String key, Class<T> eleType) {
    return getList(key, eleType, new ArrayList<>());
  }
  
  public <T> List<T> getList(String key, final Class<T> eleType, List<T> dft) {
    Object v = get(key);
    if (null == v)
      return dft; 
    if (v instanceof CharSequence)
      return Lang.list((T) new Object[] { Castors.me().castTo(v, eleType) });
    int len = Lang.eleSize(v);
    final List<T> list = new ArrayList<>(len);
    Lang.each(v, new Each<Object>() {
          public void invoke(int index, Object ele, int length) {
            list.add(Castors.me().castTo(ele, eleType));
          }
        });
    return list;
  }
  
  public <T> T[] getArray(String key, Class<T> eleType) {
    return getArray(key, eleType, (T[])Array.newInstance(eleType, 0));
  }
  
  public <T> T[] getArray(String key, final Class<T> eleType, T[] dft) {
    Object v = get(key);
    if (null == v)
      return dft; 
    if (v instanceof CharSequence)
      return (T[])Lang.array(new Object[] { Castors.me().castTo(v, eleType) }); 
    int len = Lang.eleSize(v);
    final Object arr = Array.newInstance(eleType, len);
    final int[] i = { 0 };
    Lang.each(v, new Each<Object>() {
          public void invoke(int index, Object ele, int length) {
            i[0] = i[0] + 1;
            Array.set(arr, i[0], Castors.me().castTo(ele, eleType));
          }
        });
    return (T[])arr;
  }
  
  public NutConcurrentMap addv(String key, Object value) {
    Object obj = get(key);
    if (null == obj) {
      put(key, value);
    } else if (obj instanceof List) {
      ((List<Object>)obj).add(value);
    } else {
      List<Object> list = new LinkedList();
      list.add(obj);
      list.add(value);
      put(key, list);
    } 
    return this;
  }
  
  public NutConcurrentMap addv2(String key, Object value) {
    List<Object> list = (List<Object>)get(key);
    if (null == list) {
      list = new LinkedList();
      put(key, list);
    } 
    list.add(value);
    return this;
  }
  
  public <T> NutConcurrentMap pushTo(String key, T... values) {
    if (null != values && values.length > 0) {
      Object v = get(key);
      if (null == v) {
        List<Object> list = new LinkedList();
        for (T val : values)
          list.add(val); 
        put(key, list);
      } else if (v instanceof Collection) {
        for (T val : values)
          ((Collection<Object>)v).add(val); 
      } else {
        List<Object> list = new LinkedList();
        list.add(v);
        for (T val : values)
          list.add(val); 
        put(key, list);
      } 
    } 
    return this;
  }
  
  public NutConcurrentMap pushTo(String key, Collection<?> values) {
    if (null != values && values.size() > 0) {
      Object v = get(key);
      if (null == v) {
        List<Object> list = new LinkedList();
        list.addAll(values);
        put(key, list);
      } else if (v instanceof Collection) {
        ((Collection)v).addAll(values);
      } else {
        List<Object> list = new LinkedList();
        list.add(v);
        list.addAll(values);
        put(key, list);
      } 
    } 
    return this;
  }
  
  @Deprecated
  public NutConcurrentMap putv(String key, Object value) {
    return addv(key, value);
  }
  
  public NutConcurrentMap setv(String key, Object value) {
    put(key, value);
    return this;
  }
  
  public void unset(String key) {
    remove(key);
  }
  
  public NutBean setAll(Map<String, Object> map) {
    putAll(map);
    return this;
  }
  
  public NutConcurrentMap setMap(Map<?, ?> map, boolean ignoreNullValue) {
    for (Map.Entry<?, ?> en : map.entrySet()) {
      Object key = en.getKey();
      Object val = en.getValue();
      if (null == key)
        continue; 
      if (null == val && ignoreNullValue)
        continue; 
      put(key.toString(), val);
    } 
    return this;
  }
  
  public NutConcurrentMap mergeWith(Map<String, Object> map) {
    return mergeWith(map, false);
  }
  
  public NutConcurrentMap mergeWith(Map<String, Object> map, boolean onlyAbsent) {
    for (Map.Entry<String, Object> en : map.entrySet()) {
      String key = en.getKey();
      Object val = en.getValue();
      if (null == key || null == val)
        continue; 
      Object myVal = get(key);
      if (null != myVal && myVal instanceof Map && val instanceof Map) {
        Map<String, Object> m0 = (Map<String, Object>)myVal;
        Map<String, Object> m1 = (Map<String, Object>)val;
        NutConcurrentMap m2 = WRAP(m0).mergeWith(m1, onlyAbsent);
        if (m2 != m0)
          put(key, m2); 
        continue;
      } 
      if (onlyAbsent) {
        setnx(key, val);
        continue;
      } 
      put(key, val);
    } 
    return this;
  }
  
  public NutConcurrentMap setnx(String key, Object val) {
    if (!containsKey(key))
      setv(key, val); 
    return this;
  }
  
  public NutConcurrentMap setnxAll(Map<String, Object> map) {
    if (null != map && map.size() > 0)
      for (Map.Entry<String, Object> en : map.entrySet())
        setnx(en.getKey(), en.getValue());  
    return this;
  }
  
  public <T> T getOrBorn(String key, Borning<T> factory) {
    T t = (T)get(key);
    if (t == null) {
      t = (T)factory.born(new Object[] { key });
      put(key, t);
    } 
    return t;
  }
  
  public boolean match(Map<String, Object> map) {
    if (null == map)
      return false; 
    if (size() == 0)
      return true; 
    for (Map.Entry<String, Object> en : entrySet()) {
      String key = en.getKey();
      Object mtc = en.getValue();
      if (null == mtc) {
        if (map.containsKey(key))
          return false; 
        continue;
      } 
      Object val = map.get(key);
      if (!__match_val(mtc, val))
        return false; 
    } 
    return true;
  }
  
  private boolean __match_val(final Object mtc, Object val) {
    Mirror<?> mi = Mirror.me(mtc);
    if (null == val)
      return (mi.isStringLike() && Strings.isEmpty(mtc.toString())); 
    Pattern regex = mi.is(Pattern.class) ? (Pattern)mtc : null;
    if (mi.isStringLike()) {
      final String s = mtc.toString();
      if (s.startsWith("^")) {
        regex = Regex.getPattern(s);
      } else {
        final boolean[] re = new boolean[1];
        Lang.each(val, new Each<Object>() {
              public void invoke(int index, Object ele, int length) {
                if (null != ele && ele.equals(s)) {
                  re[0] = true;
                  Lang.Break();
                } 
              }
            });
        return re[0];
      } 
    } 
    if (null != regex) {
      final boolean[] re = new boolean[1];
      final Pattern REG = regex;
      Lang.each(val, new Each<Object>() {
            public void invoke(int index, Object ele, int length) {
              if (null != ele && REG.matcher(ele.toString()).matches()) {
                re[0] = true;
                Lang.Break();
              } 
            }
          });
      return re[0];
    } 
    if (mi.isSimple()) {
      final boolean[] re = new boolean[1];
      Lang.each(val, new Each<Object>() {
            public void invoke(int index, Object ele, int length) {
              if (null != ele && ele.equals(mtc)) {
                re[0] = true;
                Lang.Break();
              } 
            }
          });
      return re[0];
    } 
    if (mi.is(Region.class))
      throw Lang.noImplement(); 
    return false;
  }
  
  public Object eval(String el) {
    return El.eval(Lang.context(this), el);
  }
  
  public int evalInt(String el) {
    Object obj = El.eval(Lang.context(this), el);
    if (obj == null)
      return 0; 
    if (obj instanceof Number)
      return ((Number)obj).intValue(); 
    return Integer.parseInt(obj.toString());
  }
  
  public int intIncrement(String key) {
    return intIncrement(key, 1);
  }
  
  public int intIncrement(String key, int number) {
    int val = getInt(key, 0);
    val += number;
    setv(key, Integer.valueOf(val));
    return val;
  }
  
  public int intDecrement(String key) {
    return intDecrement(key, 1);
  }
  
  public int intDecrement(String key, int number) {
    int val = getInt(key, 0);
    val -= number;
    setv(key, Integer.valueOf(val));
    return val;
  }
  
  public long longIncrement(String key) {
    return longIncrement(key, 1L);
  }
  
  public long longIncrement(String key, long number) {
    long val = getLong(key, 0L);
    val += number;
    setv(key, Long.valueOf(val));
    return val;
  }
  
  public long longDecrement(String key) {
    return longDecrement(key, 1L);
  }
  
  public long longDecrement(String key, long number) {
    long val = getLong(key, 0L);
    val -= number;
    setv(key, Long.valueOf(val));
    return val;
  }
}
