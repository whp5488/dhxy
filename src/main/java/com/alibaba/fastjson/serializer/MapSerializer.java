package com.alibaba.fastjson.serializer;

import java.util.List;
import java.util.Iterator;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import java.io.IOException;
import java.lang.reflect.Type;

public class MapSerializer extends SerializeFilterable implements ObjectSerializer
{
    public static MapSerializer instance;
    private static final int NON_STRINGKEY_AS_STRING;
    
    public MapSerializer() {
    }
    
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        this.write(serializer, object, fieldName, fieldType, features, false);
    }
    
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features, boolean unwrapped) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }
        Map<?, ?> map = (Map)object;
        int mapSortFieldMask = SerializerFeature.MapSortField.mask;
        if ((out.features & mapSortFieldMask) != 0x0 || (features & mapSortFieldMask) != 0x0) {
            if ((map instanceof JSONObject)) {
                map = ((JSONObject)map).getInnerMap();
            }
            if (!(map instanceof SortedMap) && !(map instanceof LinkedHashMap)) {
                try {
                    map = new TreeMap(map);
                }
                catch (Exception ex) {}
            }
        }
        if (serializer.containsReference(object)) {
            serializer.writeReference(object);
            return;
        }
        SerialContext parent = serializer.context;
        serializer.setContext(parent, object, fieldName, 0);
        try {
            if (!unwrapped) {
                out.write(123);
            }
            serializer.incrementIndent();
            Class<?> preClazz = null;
            ObjectSerializer preWriter = null;
            boolean first = true;
            if (out.isEnabled(SerializerFeature.WriteClassName)) {
                String typeKey = serializer.config.typeKey;
                Class<?> mapClass = map.getClass();
                boolean containsKey = (mapClass == JSONObject.class || mapClass == HashMap.class || mapClass == LinkedHashMap.class) && map.containsKey(typeKey);
                if (!containsKey) {
                    out.writeFieldName(typeKey);
                    out.writeString(object.getClass().getName());
                    first = false;
                }
            }
            Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<?, ?> entry = null;
                try {
                    entry = (Map.Entry)iterator.next();
                }
                catch (Exception e) {
                    break;
                }
                if (entry == null) {
                    continue;
                }
                Object value = entry.getValue();
                Object entryKey = entry.getKey();
                List<PropertyPreFilter> preFilters = serializer.propertyPreFilters;
                if (preFilters != null && preFilters.size() > 0) {
                    if (entryKey == null || entryKey instanceof String) {
                        if (!this.applyName(serializer, object, (String)entryKey)) {
                            continue;
                        }
                    }
                    else if (entryKey.getClass().isPrimitive() || entryKey instanceof Number) {
                        String strKey = JSON.toJSONString(entryKey);
                        if (!this.applyName(serializer, object, strKey)) {
                            continue;
                        }
                    }
                }
                preFilters = this.propertyPreFilters;
                if (preFilters != null && preFilters.size() > 0) {
                    if (entryKey == null || entryKey instanceof String) {
                        if (!this.applyName(serializer, object, (String)entryKey)) {
                            continue;
                        }
                    }
                    else if (entryKey.getClass().isPrimitive() || entryKey instanceof Number) {
                        String strKey = JSON.toJSONString(entryKey);
                        if (!this.applyName(serializer, object, strKey)) {
                            continue;
                        }
                    }
                }
                List<PropertyFilter> propertyFilters = serializer.propertyFilters;
                if (propertyFilters != null && propertyFilters.size() > 0) {
                    if (entryKey == null || entryKey instanceof String) {
                        if (!this.apply(serializer, object, (String)entryKey, value)) {
                            continue;
                        }
                    }
                    else if (entryKey.getClass().isPrimitive() || entryKey instanceof Number) {
                        String strKey = JSON.toJSONString(entryKey);
                        if (!this.apply(serializer, object, strKey, value)) {
                            continue;
                        }
                    }
                }
                propertyFilters = this.propertyFilters;
                if (propertyFilters != null && propertyFilters.size() > 0) {
                    if (entryKey == null || entryKey instanceof String) {
                        if (!this.apply(serializer, object, (String)entryKey, value)) {
                            continue;
                        }
                    }
                    else if (entryKey.getClass().isPrimitive() || entryKey instanceof Number) {
                        String strKey = JSON.toJSONString(entryKey);
                        if (!this.apply(serializer, object, strKey, value)) {
                            continue;
                        }
                    }
                }
                List<NameFilter> nameFilters = serializer.nameFilters;
                if (nameFilters != null && nameFilters.size() > 0) {
                    if (entryKey == null || entryKey instanceof String) {
                        entryKey = this.processKey(serializer, object, (String)entryKey, value);
                    }
                    else if (entryKey.getClass().isPrimitive() || entryKey instanceof Number) {
                        String strKey = JSON.toJSONString(entryKey);
                        entryKey = this.processKey(serializer, object, strKey, value);
                    }
                }
                nameFilters = this.nameFilters;
                if (nameFilters != null && nameFilters.size() > 0) {
                    if (entryKey == null || entryKey instanceof String) {
                        entryKey = this.processKey(serializer, object, (String)entryKey, value);
                    }
                    else if (entryKey.getClass().isPrimitive() || entryKey instanceof Number) {
                        String strKey = JSON.toJSONString(entryKey);
                        entryKey = this.processKey(serializer, object, strKey, value);
                    }
                }
                if (entryKey == null || entryKey instanceof String) {
                    value = this.processValue(serializer, null, object, (String)entryKey, value);
                }
                else {
                    boolean objectOrArray = entryKey instanceof Map || entryKey instanceof Collection;
                    if (!objectOrArray) {
                        String strKey = JSON.toJSONString(entryKey);
                        value = this.processValue(serializer, null, object, strKey, value);
                    }
                }
                if (value == null && !out.isEnabled(SerializerFeature.WRITE_MAP_NULL_FEATURES)) {
                    continue;
                }
                if ((entryKey instanceof String)) {
                    String key = (String)entryKey;
                    if (!first) {
                        out.write(44);
                    }
                    if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                        serializer.println();
                    }
                    out.writeFieldName(key, true);
                }
                else {
                    if (!first) {
                        out.write(44);
                    }
                    if (out.isEnabled(MapSerializer.NON_STRINGKEY_AS_STRING) && !(entryKey instanceof Enum)) {
                        String strEntryKey = JSON.toJSONString(entryKey);
                        serializer.write(strEntryKey);
                    }
                    else {
                        serializer.write(entryKey);
                    }
                    out.write(58);
                }
                first = false;
                if (value == null) {
                    out.writeNull();
                }
                else {
                    Class<?> clazz = value.getClass();
                    if (clazz != preClazz) {
                        preClazz = clazz;
                        preWriter = serializer.getObjectWriter(clazz);
                    }
                    if (SerializerFeature.isEnabled(features, SerializerFeature.WriteClassName) && preWriter instanceof JavaBeanSerializer) {
                        Type valueType = null;
                        if ((fieldType instanceof ParameterizedType)) {
                            ParameterizedType parameterizedType = (ParameterizedType)fieldType;
                            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                            if (actualTypeArguments.length == 2) {
                                valueType = actualTypeArguments[1];
                            }
                        }
                        JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer)preWriter;
                        javaBeanSerializer.writeNoneASM(serializer, value, entryKey, valueType, features);
                    }
                    else {
                        preWriter.write(serializer, value, entryKey, null, features);
                    }
                }
            }
        }
        finally {
            serializer.context = parent;
        }
        serializer.decrementIdent();
        if (out.isEnabled(SerializerFeature.PrettyFormat) && map.size() > 0) {
            serializer.println();
        }
        if (!unwrapped) {
            out.write(125);
        }
        return;
    }
    
    static {
        MapSerializer.instance = new MapSerializer();
        NON_STRINGKEY_AS_STRING = SerializerFeature.of(new SerializerFeature[] { SerializerFeature.BrowserCompatible, SerializerFeature.WriteNonStringKeyAsString, SerializerFeature.BrowserSecure });
    }
}
