package com.kitty.system.common.utils;

import org.nutz.el.El;
import org.nutz.lang.util.Region;
import org.nutz.lang.Mirror;
import org.nutz.lang.born.Borning;
import java.lang.reflect.Array;
import org.nutz.lang.Each;
import java.util.ListIterator;
import java.util.Date;
import org.nutz.castor.Castors;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedList;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.nutz.lang.util.Regex;
import org.nutz.lang.Strings;
import org.nutz.lang.Lang;
import java.util.Map;
import org.nutz.lang.util.NutBean;
import java.util.concurrent.ConcurrentHashMap;

public class NutConcurrentMap extends ConcurrentHashMap<String, Object> implements NutBean
{
    private static final long serialVersionUID = 1L;
    private NutConcurrentMap _map;
    
    public static NutConcurrentMap WRAP(Map<String, Object> map) {
        if (null == map) {
            return null;
        }
        if ((map instanceof NutConcurrentMap)) {
            return (NutConcurrentMap)map;
        }
        return new NutConcurrentMap(map);
    }
    
    public NutConcurrentMap() {
    }
    
    public NutConcurrentMap(Map<String, Object> map) {
        this.putAll(map);
    }
    
    public NutConcurrentMap(String json) {
        this.putAll(Lang.map(json));
    }
    
    public NutConcurrentMap(String key, Object value) {
        this.put(key, value);
    }
    
    public void setOrRemove(String key, Object v) {
        if (null == v) {
            this.remove(key);
        }
        else {
            this.put(key, v);
        }
    }
    
    public static NutConcurrentMap NEW() {
        return new NutConcurrentMap();
    }
    
    public static NutConcurrentMap WRAP(String json) {
        return new NutConcurrentMap(json);
    }
    
    public boolean has(String key) {
        return null != this.get(key);
    }
    
    public boolean is(String key, Object val) {
        Object obj = this.get(key);
        return (null == obj && null == val) || (null != obj && null != val && obj.equals(val));
    }
    
    public NutConcurrentMap duplicate() {
        NutConcurrentMap map = new NutConcurrentMap();
        map.putAll(this);
        return map;
    }
    
    public NutConcurrentMap pick(String... keys) {
        if (keys.length == 0) {
            return new NutConcurrentMap();
        }
        NutConcurrentMap re = new NutConcurrentMap();
        for (String key : keys) {
            Object val = this.get(key);
            if (null != val) {
                re.put(key, val);
            }
        }
        return re;
    }
    
    public NutConcurrentMap pickAndRemove(String... keys) {
        if (keys.length == 0) {
            return new NutConcurrentMap();
        }
        NutConcurrentMap re = new NutConcurrentMap();
        for (String key : keys) {
            Object val = this.remove(key);
            re.put(key, val);
        }
        return re;
    }
    
    public NutConcurrentMap pickBy(String regex) {
        if (Strings.isBlank(regex)) {
            return this.duplicate();
        }
        boolean isNot = regex.startsWith("!");
        Pattern p = Regex.getPattern(isNot ? regex.substring(1) : regex);
        return this.pickBy(p, isNot);
    }
    
    public NutConcurrentMap pickBy(Pattern p, boolean isNot) {
        if (null == p) {
            return isNot ? this.duplicate() : new NutConcurrentMap();
        }
        NutConcurrentMap re = new NutConcurrentMap();
        for (Map.Entry<String, Object> en : this.entrySet()) {
            String key = (String)en.getKey();
            boolean matched = p.matcher(key).find();
            if (matched) {
                if (!isNot) {
                    re.put(key, en.getValue());
                }
                else {
                    continue;
                }
            }
            else if (isNot) {
                re.put(key, en.getValue());
            }
            else {
                continue;
            }
        }
        return re;
    }
    
    public NutConcurrentMap pickAndRemoveBy(String regex) {
        if (Strings.isBlank(regex)) {
            return new NutConcurrentMap();
        }
        boolean isNot = regex.startsWith("!");
        Pattern p = Pattern.compile(isNot ? regex.substring(1) : regex);
        return this.pickAndRemoveBy(p, isNot);
    }
    
    public NutConcurrentMap pickAndRemoveBy(Pattern p, boolean isNot) {
        if (null == p) {
            if (isNot) {
                NutConcurrentMap re = this.duplicate();
                this.clear();
                return re;
            }
            return new NutConcurrentMap();
        }
        else {
            NutConcurrentMap re = new NutConcurrentMap();
            List<String> delKeys = new ArrayList(this.size());
            for (Map.Entry<String, Object> en : this.entrySet()) {
                String key = (String)en.getKey();
                boolean matched = p.matcher(key).find();
                if (matched) {
                    if (!isNot) {
                        delKeys.add(key);
                        re.put(key, en.getValue());
                    }
                    else {
                        continue;
                    }
                }
                else if (isNot) {
                    delKeys.add(key);
                    re.put(key, en.getValue());
                }
                else {
                    continue;
                }
            }
            for (String key2 : delKeys) {
                this.remove(key2);
            }
            return re;
        }
    }
    
    public NutConcurrentMap omit(String... keys) {
        NutConcurrentMap re = new NutConcurrentMap();
        for (Map.Entry<String, Object> en : this.entrySet()) {
            String key = (String)en.getKey();
            if (!Lang.contains(keys, key)) {
                re.put(key, en.getValue());
            }
            else {
                continue;
            }
        }
        return re;
    }
    
    public NutConcurrentMap putDefault(String key, Object dft) {
        if (!this.has(key)) {
            this.put(key, dft);
            return this;
        }
        return this;
    }
    
    @Override
    public boolean containsValue(Object value) {
        if (null == this._map) {
            return super.containsValue(value);
        }
        return super.containsValue(value) || this._map.containsValue(value);
    }
    
    @Override
    public boolean containsKey(Object key) {
        if (null == this._map) {
            return super.containsKey(key);
        }
        return super.containsKey(key) || this._map.containsKey(key);
    }
    
    @Override
    public ConcurrentHashMap.KeySetView<String, Object> keySet() {
        return super.keySet();
    }
    
    @Override
    public Collection<Object> values() {
        if (null == this._map) {
            return super.values();
        }
        List<Object> vals = new LinkedList();
        for (String key : this.keySet()) {
            vals.add(this.get(key));
        }
        return vals;
    }
    
    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        if (null == this._map) {
            return super.entrySet();
        }
        HashSet<Map.Entry<String, Object>> vals = new HashSet();
        vals.addAll(this._map.entrySet());
        vals.addAll(super.entrySet());
        return vals;
    }
    
    @Override
    public void clear() {
        super.clear();
        if (null != this._map) {
            this._map.clear();
        }
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
    
    @Override
    public Object get(Object key) {
        if (this._map == null) {
            return super.get(key);
        }
        if (super.containsKey(key)) {
            return super.get(key);
        }
        return this._map.get(key);
    }
    
    public Object get(String key, Object dft) {
        Object v = this.get(key);
        return (null == v) ? dft : v;
    }
    
    public int getInt(String key) {
        return this.getInt(key, -1);
    }
    
    public int getInt(String key, int dft) {
        Object v = this.get(key);
        return (null == v) ? dft : ((int)Castors.me().castTo(v, Integer.TYPE));
    }
    
    public float getFloat(String key) {
        return this.getFloat(key, Float.NaN);
    }
    
    public float getFloat(String key, float dft) {
        Object v = this.get(key);
        return (null == v) ? dft : ((float)Castors.me().castTo(v, Float.TYPE));
    }
    
    public long getLong(String key) {
        return this.getLong(key, -1L);
    }
    
    public long getLong(String key, long dft) {
        Object v = this.get(key);
        return (null == v) ? dft : ((long)Castors.me().castTo(v, Long.TYPE));
    }
    
    public double getDouble(String key) {
        return this.getDouble(key, 0.0);
    }
    
    public double getDouble(String key, double dft) {
        Object v = this.get(key);
        return (null == v) ? dft : ((double)Castors.me().castTo(v, Double.TYPE));
    }
    
    public boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }
    
    public boolean getBoolean(String key, boolean dft) {
        Object v = this.get(key);
        return (null == v) ? dft : ((boolean)Castors.me().castTo(v, Boolean.TYPE));
    }
    
    public String getString(String key) {
        return this.getString(key, null);
    }
    
    public String getString(String key, String dft) {
        Object v = this.get(key);
        if (v == null) {
            return dft;
        }
        if ((v instanceof CharSequence)) {
            return v.toString();
        }
        if ((v instanceof List)) {
            v = ((List)v).iterator().next();
        }
        return (String)Castors.me().castTo(v, String.class);
    }
    
    public Date getTime(String key) {
        return this.getTime(key, null);
    }
    
    public Date getTime(String key, Date dft) {
        Object v = this.get(key);
        return (null == v) ? dft : ((Date)Castors.me().castTo(v, Date.class));
    }
    
    public <T extends Enum<T>> T getEnum(String key, Class<T> classOfEnum) {
        String s = this.getString(key);
        if (Strings.isBlank(s)) {
            return null;
        }
        return Enum.valueOf(classOfEnum, s);
    }
    
    public boolean isEnum(String key, Enum<?>... eus) {
        if (null == eus || eus.length == 0) {
            return false;
        }
        try {
            Enum<?> v = this.getEnum(key, eus[0].getClass());
            int length = eus.length;
            int i = 0;
            while (i < length) {
                Enum<?> eu = eus[i];
                if (!v.equals(eu)) {
                    return false;
                }
                ++i;
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public <T> T getAs(String key, Class<T> classOfT) {
        return this.getAs(key, classOfT, null);
    }
    
    public <T> T getAs(String key, Class<T> classOfT, T dft) {
        Object v = this.get(key);
        return (null == v) ? dft : Castors.me().castTo(v, classOfT);
    }
    
    public <T> List<T> getAsList(String key, Class<T> eleType) {
        Object v = this.get(key);
        if (null == v) {
            return null;
        }
        List<T> list = (List)v;
        ListIterator<T> it = list.listIterator();
        while (it.hasNext()) {
            Object ele = it.next();
            if (null != ele && !eleType.isAssignableFrom(ele.getClass())) {
                T ele2 = Castors.me().castTo(ele, eleType);
                it.set(ele2);
            }
            else {
                continue;
            }
        }
        return list;
    }
    
    public <T> List<T> getList(String key, final Class<T> eleType) {
        return this.getList(key, eleType, new ArrayList<>());
    }
    
    public <T> List<T> getList(String key, final Class<T> eleType, List<T> dft) {
        Object v = this.get(key);
        if (null == v) {
            return dft;
        }
        if ((v instanceof CharSequence)) {
            return (List)Lang.list(new Object[] { Castors.me().castTo(v, eleType) });
        }
        int len = Lang.eleSize(v);
        final List<T> list = new ArrayList(len);
        Lang.each(v, new Each<Object>() {
            public void invoke(int index, Object ele, int length) {
                list.add(Castors.me().castTo(ele, eleType));
            }
        });
        return list;
    }
    
    public <T> T[] getArray(String key, Class<T> eleType) {
        return this.getArray(key, eleType, (T[])Array.newInstance(eleType, 0));
    }
    
    public <T> T[] getArray(String key, final Class<T> eleType, T[] dft) {
        Object v = this.get(key);
        if (null == v) {
            return dft;
        }
        if ((v instanceof CharSequence)) {
            return (T[])Lang.array(new Object[] { Castors.me().castTo(v, eleType) });
        }
        int len = Lang.eleSize(v);
        final Object arr = Array.newInstance(eleType, len);
        final int[] i = { 0 };
        Lang.each(v, new Each<Object>() {
            public void invoke(int index, Object ele, int length) {
                Array.set(arr, i[0]++, Castors.me().castTo(ele, eleType));
            }
        });
        return (T[])(Object[])arr;
    }
    
    public NutConcurrentMap addv(String key, Object value) {
        Object obj = this.get(key);
        if (null == obj) {
            this.put(key, value);
        }
        else if ((obj instanceof List)) {
            ((List)obj).add(value);
        }
        else {
            List<Object> list = new LinkedList();
            list.add(obj);
            list.add(value);
            this.put(key, list);
        }
        return this;
    }
    
    public NutConcurrentMap addv2(String key, Object value) {
        List<Object> list = (List)this.get(key);
        if (null == list) {
            list = new LinkedList();
            this.put(key, list);
        }
        list.add(value);
        return this;
    }
    
    public <T> NutConcurrentMap pushTo(String key, T... values) {
        if (null != values && values.length > 0) {
            Object v = this.get(key);
            if (null == v) {
                List<Object> list = new LinkedList();
                for (Object val : values) {
                    list.add(val);
                }
                this.put(key, list);
            }
            else if ((v instanceof Collection)) {
                for (Object val2 : values) {
                    ((Collection)v).add(val2);
                }
            }
            else {
                List<Object> list = new LinkedList();
                list.add(v);
                for (Object val : values) {
                    list.add(val);
                }
                this.put(key, list);
            }
        }
        return this;
    }
    
    public NutConcurrentMap pushTo(String key, Collection<?> values) {
        if (null != values && values.size() > 0) {
            Object v = this.get(key);
            if (null == v) {
                List<Object> list = new LinkedList();
                list.addAll(values);
                this.put(key, list);
            }
            else if ((v instanceof Collection)) {
                ((Collection)v).addAll(values);
            }
            else {
                List<Object> list = new LinkedList();
                list.add(v);
                list.addAll(values);
                this.put(key, list);
            }
        }
        return this;
    }
    
    @Deprecated
    public NutConcurrentMap putv(String key, Object value) {
        return this.addv(key, value);
    }
    
    public NutConcurrentMap setv(String key, Object value) {
        this.put(key, value);
        return this;
    }
    
    public void unset(String key) {
        this.remove(key);
    }
    
    public NutBean setAll(Map<String, Object> map) {
        this.putAll(map);
        return this;
    }
    
    public NutConcurrentMap setMap(Map<?, ?> map, boolean ignoreNullValue) {
        for (Map.Entry<?, ?> en : map.entrySet()) {
            Object key = en.getKey();
            Object val = en.getValue();
            if (null == key) {
                continue;
            }
            if (null == val && ignoreNullValue) {
                continue;
            }
            this.put(key.toString(), val);
        }
        return this;
    }
    
    public NutConcurrentMap mergeWith(Map<String, Object> map) {
        return this.mergeWith(map, false);
    }
    
    public NutConcurrentMap mergeWith(Map<String, Object> map, boolean onlyAbsent) {
        for (Map.Entry<String, Object> en : map.entrySet()) {
            String key = (String)en.getKey();
            Object val = en.getValue();
            if (null == key) {
                continue;
            }
            if (null == val) {
                continue;
            }
            Object myVal = this.get(key);
            if (null != myVal && myVal instanceof Map && val instanceof Map) {
                Map<String, Object> m0 = (Map)myVal;
                Map<String, Object> m2 = (Map)val;
                NutConcurrentMap m3 = WRAP(m0).mergeWith(m2, onlyAbsent);
                if (m3 != m0) {
                    this.put(key, m3);
                }
                else {
                    continue;
                }
            }
            else if (onlyAbsent) {
                this.setnx(key, val);
            }
            else {
                this.put(key, val);
            }
        }
        return this;
    }
    
    public NutConcurrentMap setnx(String key, Object val) {
        if (!this.containsKey(key)) {
            this.setv(key, val);
            return this;
        }
        return this;
    }
    
    public NutConcurrentMap setnxAll(Map<String, Object> map) {
        if (null != map && map.size() > 0) {
            for (Map.Entry<String, Object> en : map.entrySet()) {
                this.setnx((String)en.getKey(), en.getValue());
            }
            return this;
        }
        return this;
    }
    
    public <T> T getOrBorn(String key, Borning<T> factory) {
        T t = (T)this.get(key);
        if (t == null) {
            t = factory.born(new Object[] { key });
            this.put(key, t);
            return t;
        }
        return t;
    }
    
    public boolean match(Map<String, Object> map) {
        if (null == map) {
            return false;
        }
        if (this.size() == 0) {
            return true;
        }
        for (Map.Entry<String, Object> en : this.entrySet()) {
            String key = (String)en.getKey();
            Object mtc = en.getValue();
            if (null == mtc) {
                if (map.containsKey(key)) {
                    return false;
                }
                continue;
            }
            else {
                Object val = map.get(key);
                if (!this.__match_val(mtc, val)) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    private boolean __match_val(final Object mtc, Object val) {
        Mirror<?> mi = Mirror.me(mtc);
        if (null == val) {
            return mi.isStringLike() && Strings.isEmpty(mtc.toString());
        }
        Pattern regex = mi.is(Pattern.class) ? ((Pattern)mtc) : null;
        if (mi.isStringLike()) {
            final String s = mtc.toString();
            if (s.startsWith("^")) {
                regex = Regex.getPattern(s);
            }
            else {
                final boolean[] re = { false };
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
            final boolean[] re2 = { false };
            final Pattern REG = regex;
            Lang.each(val, new Each<Object>() {
                public void invoke(int index, Object ele, int length) {
                    if (null != ele && REG.matcher(ele.toString()).matches()) {
                        re2[0] = true;
                        Lang.Break();
                    }
                }
            });
            return re2[0];
        }
        if (mi.isSimple()) {
            final boolean[] re2 = { false };
            Lang.each(val, new Each<Object>() {
                public void invoke(int index, Object ele, int length) {
                    if (null != ele && ele.equals(mtc)) {
                        re2[0] = true;
                        Lang.Break();
                    }
                }
            });
            return re2[0];
        }
        if (mi.is(Region.class)) {
            throw Lang.noImplement();
        }
        return false;
    }
    
    public Object eval(String el) {
        return El.eval(Lang.context(this), el);
    }
    
    public int evalInt(String el) {
        Object obj = El.eval(Lang.context(this), el);
        if (obj == null) {
            return 0;
        }
        if ((obj instanceof Number)) {
            return ((Number)obj).intValue();
        }
        return Integer.parseInt(obj.toString());
    }
    
    public int intIncrement(String key) {
        return this.intIncrement(key, 1);
    }
    
    public int intIncrement(String key, int number) {
        int val = this.getInt(key, 0);
        val += number;
        this.setv(key, Integer.valueOf(val));
        return val;
    }
    
    public int intDecrement(String key) {
        return this.intDecrement(key, 1);
    }
    
    public int intDecrement(String key, int number) {
        int val = this.getInt(key, 0);
        val -= number;
        this.setv(key, Integer.valueOf(val));
        return val;
    }
    
    public long longIncrement(String key) {
        return this.longIncrement(key, 1L);
    }
    
    public long longIncrement(String key, long number) {
        long val = this.getLong(key, 0L);
        val += number;
        this.setv(key, Long.valueOf(val));
        return val;
    }
    
    public long longDecrement(String key) {
        return this.longDecrement(key, 1L);
    }
    
    public long longDecrement(String key, long number) {
        long val = this.getLong(key, 0L);
        val -= number;
        this.setv(key, Long.valueOf(val));
        return val;
    }
}
