
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
    
    public static NutConcurrentMap WRAP(final Map<String, Object> map) {
        if (null == map) {
            return null;
        }
        if (map instanceof NutConcurrentMap) {
            return (NutConcurrentMap)map;
        }
        return new NutConcurrentMap(map);
    }
    
    public NutConcurrentMap() {
    }
    
    public NutConcurrentMap(final Map<String, Object> map) {
        this.putAll(map);
    }
    
    public NutConcurrentMap(final String json) {
        this.putAll((Map<? extends String, ?>)Lang.map(json));
    }
    
    public NutConcurrentMap(final String key, final Object value) {
        this.put(key, value);
    }
    
    public void setOrRemove(final String key, final Object v) {
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
    
    public static NutConcurrentMap WRAP(final String json) {
        return new NutConcurrentMap(json);
    }
    
    public boolean has(final String key) {
        return null != this.get(key);
    }
    
    public boolean is(final String key, final Object val) {
        final Object obj = this.get(key);
        return (null == obj && null == val) || (null != obj && null != val && obj.equals(val));
    }
    
    public NutConcurrentMap duplicate() {
        final NutConcurrentMap map = new NutConcurrentMap();
        map.putAll(this);
        return map;
    }
    
    public NutConcurrentMap pick(final String... keys) {
        if (keys.length == 0) {
            return new NutConcurrentMap();
        }
        final NutConcurrentMap re = new NutConcurrentMap();
        for (final String key : keys) {
            final Object val = this.get(key);
            if (null != val) {
                re.put(key, val);
            }
        }
        return re;
    }
    
    public NutConcurrentMap pickAndRemove(final String... keys) {
        if (keys.length == 0) {
            return new NutConcurrentMap();
        }
        final NutConcurrentMap re = new NutConcurrentMap();
        for (final String key : keys) {
            final Object val = this.remove(key);
            re.put(key, val);
        }
        return re;
    }
    
    public NutConcurrentMap pickBy(final String regex) {
        if (Strings.isBlank((CharSequence)regex)) {
            return this.duplicate();
        }
        final boolean isNot = regex.startsWith("!");
        final Pattern p = Regex.getPattern(isNot ? regex.substring(1) : regex);
        return this.pickBy(p, isNot);
    }
    
    public NutConcurrentMap pickBy(final Pattern p, final boolean isNot) {
        if (null == p) {
            return isNot ? this.duplicate() : new NutConcurrentMap();
        }
        final NutConcurrentMap re = new NutConcurrentMap();
        for (final Map.Entry<String, Object> en : this.entrySet()) {
            final String key = en.getKey();
            final boolean matched = p.matcher(key).find();
            if (matched) {
                if (isNot) {
                    continue;
                }
                re.put(key, en.getValue());
            }
            else {
                if (!isNot) {
                    continue;
                }
                re.put(key, en.getValue());
            }
        }
        return re;
    }
    
    public NutConcurrentMap pickAndRemoveBy(final String regex) {
        if (Strings.isBlank((CharSequence)regex)) {
            return new NutConcurrentMap();
        }
        final boolean isNot = regex.startsWith("!");
        final Pattern p = Pattern.compile(isNot ? regex.substring(1) : regex);
        return this.pickAndRemoveBy(p, isNot);
    }
    
    public NutConcurrentMap pickAndRemoveBy(final Pattern p, final boolean isNot) {
        if (null != p) {
            final NutConcurrentMap re = new NutConcurrentMap();
            final List<String> delKeys = new ArrayList<String>(this.size());
            for (final Map.Entry<String, Object> en : this.entrySet()) {
                final String key = en.getKey();
                final boolean matched = p.matcher(key).find();
                if (matched) {
                    if (isNot) {
                        continue;
                    }
                    delKeys.add(key);
                    re.put(key, en.getValue());
                }
                else {
                    if (!isNot) {
                        continue;
                    }
                    delKeys.add(key);
                    re.put(key, en.getValue());
                }
            }
            for (final String key2 : delKeys) {
                this.remove(key2);
            }
            return re;
        }
        if (isNot) {
            final NutConcurrentMap re = this.duplicate();
            this.clear();
            return re;
        }
        return new NutConcurrentMap();
    }
    
    public NutConcurrentMap omit(final String... keys) {
        final NutConcurrentMap re = new NutConcurrentMap();
        for (final Map.Entry<String, Object> en : this.entrySet()) {
            final String key = en.getKey();
            if (!Lang.contains((Object[])keys, (Object)key)) {
                re.put(key, en.getValue());
            }
        }
        return re;
    }
    
    public NutConcurrentMap putDefault(final String key, final Object dft) {
        if (!this.has(key)) {
            this.put(key, dft);
        }
        return this;
    }
    
    @Override
    public boolean containsValue(final Object value) {
        if (null == this._map) {
            return super.containsValue(value);
        }
        return super.containsValue(value) || this._map.containsValue(value);
    }
    
    @Override
    public boolean containsKey(final Object key) {
        if (null == this._map) {
            return super.containsKey(key);
        }
        return super.containsKey(key) || this._map.containsKey(key);
    }
    
    @Override
    public KeySetView<String, Object> keySet() {
        return super.keySet();
    }
    
    @Override
    public Collection<Object> values() {
        if (null == this._map) {
            return super.values();
        }
        final List<Object> vals = new LinkedList<Object>();
        for (final String key : this.keySet()) {
            vals.add(this.get(key));
        }
        return vals;
    }
    
    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        if (null == this._map) {
            return super.entrySet();
        }
        final HashSet<Map.Entry<String, Object>> vals = new HashSet<Map.Entry<String, Object>>();
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
    
    public NutConcurrentMap attach(final NutConcurrentMap map) {
        this._map = map;
        return this;
    }
    
    public NutConcurrentMap detach() {
        final NutConcurrentMap re = this._map;
        this._map = null;
        return re;
    }
    
    @Override
    public Object get(final Object key) {
        if (this._map == null) {
            return super.get(key);
        }
        if (super.containsKey(key)) {
            return super.get(key);
        }
        return this._map.get(key);
    }
    
    public Object get(final String key, final Object dft) {
        final Object v = this.get(key);
        return (null == v) ? dft : v;
    }
    
    public int getInt(final String key) {
        return this.getInt(key, -1);
    }
    
    public int getInt(final String key, final int dft) {
        final Object v = this.get(key);
        return (int)((null == v) ? dft : Castors.me().castTo(v, (Class)Integer.TYPE));
    }
    
    public float getFloat(final String key) {
        return this.getFloat(key, Float.NaN);
    }
    
    public float getFloat(final String key, final float dft) {
        final Object v = this.get(key);
        return (float)((null == v) ? dft : Castors.me().castTo(v, (Class)Float.TYPE));
    }
    
    public long getLong(final String key) {
        return this.getLong(key, -1L);
    }
    
    public long getLong(final String key, final long dft) {
        final Object v = this.get(key);
        return (long)((null == v) ? dft : Castors.me().castTo(v, (Class)Long.TYPE));
    }
    
    public double getDouble(final String key) {
        return this.getDouble(key, 0.0);
    }
    
    public double getDouble(final String key, final double dft) {
        final Object v = this.get(key);
        return (double)((null == v) ? dft : Castors.me().castTo(v, (Class)Double.TYPE));
    }
    
    public boolean getBoolean(final String key) {
        return this.getBoolean(key, false);
    }
    
    public boolean getBoolean(final String key, final boolean dft) {
        final Object v = this.get(key);
        return (boolean)((null == v) ? dft : Castors.me().castTo(v, (Class)Boolean.TYPE));
    }
    
    public String getString(final String key) {
        return this.getString(key, null);
    }
    
    public String getString(final String key, final String dft) {
        Object v = this.get(key);
        if (v == null) {
            return dft;
        }
        if (v instanceof CharSequence) {
            return v.toString();
        }
        if (v instanceof List) {
            v = ((List)v).iterator().next();
        }
        return (String)Castors.me().castTo(v, (Class)String.class);
    }
    
    public Date getTime(final String key) {
        return this.getTime(key, null);
    }
    
    public Date getTime(final String key, final Date dft) {
        final Object v = this.get(key);
        return (Date)((null == v) ? dft : Castors.me().castTo(v, (Class)Date.class));
    }
    
    public <T extends Enum<T>> T getEnum(final String key, final Class<T> classOfEnum) {
        final String s = this.getString(key);
        if (Strings.isBlank((CharSequence)s)) {
            return null;
        }
        return Enum.valueOf(classOfEnum, s);
    }
    
    public boolean isEnum(final String key, final Enum<?>... eus) {
        if (null == eus || eus.length == 0) {
            return false;
        }
        try {
            final Enum<?> v = this.getEnum(key, eus[0].getClass());
            for (final Enum<?> eu : eus) {
                if (!v.equals(eu)) {
                    return false;
                }
            }
            return true;
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    public <T> T getAs(final String key, final Class<T> classOfT) {
        return this.getAs(key, classOfT, (T)null);
    }
    
    public <T> T getAs(final String key, final Class<T> classOfT, final T dft) {
        final Object v = this.get(key);
        return (T)((null == v) ? dft : Castors.me().castTo(v, (Class)classOfT));
    }
    
    public <T> List<T> getAsList(final String key, final Class<T> eleType) {
        final Object v = this.get(key);
        if (null == v) {
            return null;
        }
        final List list = (List)v;
        final ListIterator it = list.listIterator();
        while (it.hasNext()) {
            final Object ele = it.next();
            if (null != ele && !eleType.isAssignableFrom(ele.getClass())) {
                final Object ele2 = Castors.me().castTo(ele, (Class)eleType);
                it.set(ele2);
            }
        }
        return list;
    }
    
    public <T> List<T> getList(final String key, final Class<T> eleType) {
        return this.getList(key, eleType, new ArrayList<T>());
    }
    
    public <T> List<T> getList(final String key, final Class<T> eleType, final List<T> dft) {
        final Object v = this.get(key);
        if (null == v) {
            return dft;
        }
        if (v instanceof CharSequence) {
            return Lang.list((T) new Object[] { Castors.me().castTo(v, eleType) });
        }
        final int len = Lang.eleSize(v);
        final List<T> list = new ArrayList<T>(len);
        Lang.each(v, (Each)new Each<Object>() {
            public void invoke(final int index, final Object ele, final int length) {
                list.add(Castors.me().castTo(ele, eleType));
            }
        });
        return list;
    }
    
    public <T> T[] getArray(final String key, final Class<T> eleType) {
        return this.getArray(key, eleType, (T[])Array.newInstance(eleType, 0));
    }
    
    public <T> T[] getArray(final String key, final Class<T> eleType, final T[] dft) {
        final Object v = this.get(key);
        if (null == v) {
            return dft;
        }
        if (v instanceof CharSequence) {
            return (T[])Lang.array(new Object[] { Castors.me().castTo(v, (Class)eleType) });
        }
        final int len = Lang.eleSize(v);
        final Object arr = Array.newInstance(eleType, len);
        final int[] i = { 0 };
        Lang.each(v, (Each)new Each<Object>() {
            public void invoke(final int index, final Object ele, final int length) {
                Array.set(arr, i[0]++, Castors.me().castTo(ele, eleType));
            }
        });
        return (T[])arr;
    }
    
    public NutConcurrentMap addv(final String key, final Object value) {
        final Object obj = this.get(key);
        if (null == obj) {
            this.put(key, value);
        }
        else if (obj instanceof List) {
            ((List)obj).add(value);
        }
        else {
            final List<Object> list = new LinkedList<Object>();
            list.add(obj);
            list.add(value);
            this.put(key, list);
        }
        return this;
    }
    
    public NutConcurrentMap addv2(final String key, final Object value) {
        List<Object> list = (List<Object>)this.get(key);
        if (null == list) {
            list = new LinkedList<Object>();
            this.put(key, list);
        }
        list.add(value);
        return this;
    }
    
    public <T> NutConcurrentMap pushTo(final String key, final T... values) {
        if (null != values && values.length > 0) {
            final Object v = this.get(key);
            if (null == v) {
                final List<Object> list = new LinkedList<Object>();
                for (final Object val : values) {
                    list.add(val);
                }
                this.put(key, list);
            }
            else if (v instanceof Collection) {
                for (final Object val2 : values) {
                    ((Collection)v).add(val2);
                }
            }
            else {
                final List<Object> list = new LinkedList<Object>();
                list.add(v);
                for (final Object val : values) {
                    list.add(val);
                }
                this.put(key, list);
            }
        }
        return this;
    }
    
    public NutConcurrentMap pushTo(final String key, final Collection<?> values) {
        if (null != values && values.size() > 0) {
            final Object v = this.get(key);
            if (null == v) {
                final List<Object> list = new LinkedList<Object>();
                list.addAll(values);
                this.put(key, list);
            }
            else if (v instanceof Collection) {
                ((Collection)v).addAll(values);
            }
            else {
                final List<Object> list = new LinkedList<Object>();
                list.add(v);
                list.addAll(values);
                this.put(key, list);
            }
        }
        return this;
    }
    
    @Deprecated
    public NutConcurrentMap putv(final String key, final Object value) {
        return this.addv(key, value);
    }
    
    public NutConcurrentMap setv(final String key, final Object value) {
        this.put(key, value);
        return this;
    }
    
    public void unset(final String key) {
        this.remove(key);
    }
    
    public NutBean setAll(final Map<String, Object> map) {
        this.putAll(map);
        return (NutBean)this;
    }
    
    public NutConcurrentMap setMap(final Map<?, ?> map, final boolean ignoreNullValue) {
        for (final Map.Entry<?, ?> en : map.entrySet()) {
            final Object key = en.getKey();
            final Object val = en.getValue();
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
    
    public NutConcurrentMap mergeWith(final Map<String, Object> map) {
        return this.mergeWith(map, false);
    }
    
    public NutConcurrentMap mergeWith(final Map<String, Object> map, final boolean onlyAbsent) {
        for (final Map.Entry<String, Object> en : map.entrySet()) {
            final String key = en.getKey();
            final Object val = en.getValue();
            if (null != key) {
                if (null == val) {
                    continue;
                }
                final Object myVal = this.get(key);
                if (null != myVal && myVal instanceof Map && val instanceof Map) {
                    final Map<String, Object> m0 = (Map<String, Object>)myVal;
                    final Map<String, Object> m2 = (Map<String, Object>)val;
                    final NutConcurrentMap m3 = WRAP(m0).mergeWith(m2, onlyAbsent);
                    if (m3 == m0) {
                        continue;
                    }
                    this.put(key, m3);
                }
                else if (onlyAbsent) {
                    this.setnx(key, val);
                }
                else {
                    this.put(key, val);
                }
            }
        }
        return this;
    }
    
    public NutConcurrentMap setnx(final String key, final Object val) {
        if (!this.containsKey(key)) {
            this.setv(key, val);
        }
        return this;
    }
    
    public NutConcurrentMap setnxAll(final Map<String, Object> map) {
        if (null != map && map.size() > 0) {
            for (final Map.Entry<String, Object> en : map.entrySet()) {
                this.setnx(en.getKey(), en.getValue());
            }
        }
        return this;
    }
    
    public <T> T getOrBorn(final String key, final Borning<T> factory) {
        T t = (T)this.get(key);
        if (t == null) {
            t = (T)factory.born(new Object[] { key });
            this.put(key, t);
        }
        return t;
    }
    
    public boolean match(final Map<String, Object> map) {
        if (null == map) {
            return false;
        }
        if (this.size() == 0) {
            return true;
        }
        for (final Map.Entry<String, Object> en : this.entrySet()) {
            final String key = en.getKey();
            final Object mtc = en.getValue();
            if (null == mtc) {
                if (map.containsKey(key)) {
                    return false;
                }
                continue;
            }
            else {
                final Object val = map.get(key);
                if (!this.__match_val(mtc, val)) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    private boolean __match_val(final Object mtc, final Object val) {
        final Mirror<?> mi = (Mirror<?>)Mirror.me(mtc);
        if (null == val) {
            return mi.isStringLike() && Strings.isEmpty((CharSequence)mtc.toString());
        }
        Pattern regex = mi.is(Pattern.class) ? ((Pattern)mtc) : null;
        if (mi.isStringLike()) {
            final String s = mtc.toString();
            if (!s.startsWith("^")) {
                final boolean[] re = { false };
                Lang.each(val, (Each)new Each<Object>() {
                    public void invoke(final int index, final Object ele, final int length) {
                        if (null != ele && ele.equals(s)) {
                            re[0] = true;
                            Lang.Break();
                        }
                    }
                });
                return re[0];
            }
            regex = Regex.getPattern(s);
        }
        if (null != regex) {
            final boolean[] re2 = { false };
            final Pattern REG = regex;
            Lang.each(val, (Each)new Each<Object>() {
                public void invoke(final int index, final Object ele, final int length) {
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
            Lang.each(val, (Each)new Each<Object>() {
                public void invoke(final int index, final Object ele, final int length) {
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
    
    public Object eval(final String el) {
        return El.eval(Lang.context(this), el);
    }
    
    public int evalInt(final String el) {
        final Object obj = El.eval(Lang.context(this), el);
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number)obj).intValue();
        }
        return Integer.parseInt(obj.toString());
    }
    
    public int intIncrement(final String key) {
        return this.intIncrement(key, 1);
    }
    
    public int intIncrement(final String key, final int number) {
        int val = this.getInt(key, 0);
        val += number;
        this.setv(key, val);
        return val;
    }
    
    public int intDecrement(final String key) {
        return this.intDecrement(key, 1);
    }
    
    public int intDecrement(final String key, final int number) {
        int val = this.getInt(key, 0);
        val -= number;
        this.setv(key, val);
        return val;
    }
    
    public long longIncrement(final String key) {
        return this.longIncrement(key, 1L);
    }
    
    public long longIncrement(final String key, final long number) {
        long val = this.getLong(key, 0L);
        val += number;
        this.setv(key, val);
        return val;
    }
    
    public long longDecrement(final String key) {
        return this.longDecrement(key, 1L);
    }
    
    public long longDecrement(final String key, final long number) {
        long val = this.getLong(key, 0L);
        val -= number;
        this.setv(key, val);
        return val;
    }
}
