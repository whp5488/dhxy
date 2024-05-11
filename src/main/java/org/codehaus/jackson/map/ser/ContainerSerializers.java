
package org.codehaus.jackson.map.ser;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Collection;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import java.util.List;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;
import java.lang.reflect.ParameterizedType;
import org.codehaus.jackson.JsonNode;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerationException;
import java.io.IOException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.type.JavaType;

public final class ContainerSerializers
{
    private ContainerSerializers() {
    }
    
    public static ContainerSerializerBase<?> indexedListSerializer(final JavaType elemType, final boolean staticTyping, final TypeSerializer vts, final BeanProperty property, final JsonSerializer<Object> valueSerializer) {
        return new IndexedListSerializer(elemType, staticTyping, vts, property, valueSerializer);
    }
    
    public static ContainerSerializerBase<?> collectionSerializer(final JavaType elemType, final boolean staticTyping, final TypeSerializer vts, final BeanProperty property, final JsonSerializer<Object> valueSerializer) {
        return new CollectionSerializer(elemType, staticTyping, vts, property, valueSerializer);
    }
    
    public static ContainerSerializerBase<?> iteratorSerializer(final JavaType elemType, final boolean staticTyping, final TypeSerializer vts, final BeanProperty property) {
        return new IteratorSerializer(elemType, staticTyping, vts, property);
    }
    
    public static ContainerSerializerBase<?> iterableSerializer(final JavaType elemType, final boolean staticTyping, final TypeSerializer vts, final BeanProperty property) {
        return new IterableSerializer(elemType, staticTyping, vts, property);
    }
    
    public static JsonSerializer<?> enumSetSerializer(final JavaType enumType, final BeanProperty property) {
        return (JsonSerializer<?>)new EnumSetSerializer(enumType, property);
    }
    
    public abstract static class AsArraySerializer<T> extends ContainerSerializerBase<T> implements ResolvableSerializer
    {
        protected final boolean _staticTyping;
        protected final JavaType _elementType;
        protected final TypeSerializer _valueTypeSerializer;
        protected JsonSerializer<Object> _elementSerializer;
        protected final BeanProperty _property;
        protected PropertySerializerMap _dynamicSerializers;
        
        @Deprecated
        protected AsArraySerializer(final Class<?> cls, final JavaType et, final boolean staticTyping, final TypeSerializer vts, final BeanProperty property) {
            this(cls, et, staticTyping, vts, property, null);
        }
        
        protected AsArraySerializer(final Class<?> cls, final JavaType et, final boolean staticTyping, final TypeSerializer vts, final BeanProperty property, final JsonSerializer<Object> elementSerializer) {
            super(cls, false);
            this._elementType = et;
            this._staticTyping = (staticTyping || (et != null && et.isFinal()));
            this._valueTypeSerializer = vts;
            this._property = property;
            this._elementSerializer = elementSerializer;
            this._dynamicSerializers = PropertySerializerMap.emptyMap();
        }
        
        public final void serialize(final T value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeStartArray();
            this.serializeContents(value, jgen, provider);
            jgen.writeEndArray();
        }
        
        public final void serializeWithType(final T value, final JsonGenerator jgen, final SerializerProvider provider, final TypeSerializer typeSer) throws IOException, JsonGenerationException {
            typeSer.writeTypePrefixForArray((Object)value, jgen);
            this.serializeContents(value, jgen, provider);
            typeSer.writeTypeSuffixForArray((Object)value, jgen);
        }
        
        protected abstract void serializeContents(final T p0, final JsonGenerator p1, final SerializerProvider p2) throws IOException, JsonGenerationException;
        
        public JsonNode getSchema(final SerializerProvider provider, final Type typeHint) throws JsonMappingException {
            final ObjectNode o = this.createSchemaNode("array", true);
            JavaType contentType = null;
            if (typeHint != null) {
                final JavaType javaType = provider.constructType(typeHint);
                contentType = javaType.getContentType();
                if (contentType == null && typeHint instanceof ParameterizedType) {
                    final Type[] typeArgs = ((ParameterizedType)typeHint).getActualTypeArguments();
                    if (typeArgs.length == 1) {
                        contentType = provider.constructType(typeArgs[0]);
                    }
                }
            }
            if (contentType == null && this._elementType != null) {
                contentType = this._elementType;
            }
            if (contentType != null) {
                JsonNode schemaNode = null;
                if (contentType.getRawClass() != Object.class) {
                    final JsonSerializer<Object> ser = (JsonSerializer<Object>)provider.findValueSerializer(contentType, this._property);
                    if (ser instanceof SchemaAware) {
                        schemaNode = ((SchemaAware)ser).getSchema(provider, (Type)null);
                    }
                }
                if (schemaNode == null) {
                    schemaNode = JsonSchema.getDefaultSchemaNode();
                }
                o.put("items", schemaNode);
            }
            return (JsonNode)o;
        }
        
        public void resolve(final SerializerProvider provider) throws JsonMappingException {
            if (this._staticTyping && this._elementType != null && this._elementSerializer == null) {
                this._elementSerializer = (JsonSerializer<Object>)provider.findValueSerializer(this._elementType, this._property);
            }
        }
        
        protected final JsonSerializer<Object> _findAndAddDynamic(final PropertySerializerMap map, final Class<?> type, final SerializerProvider provider) throws JsonMappingException {
            final PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSerializer(type, provider, this._property);
            if (map != result.map) {
                this._dynamicSerializers = result.map;
            }
            return (JsonSerializer<Object>)result.serializer;
        }
        
        protected final JsonSerializer<Object> _findAndAddDynamic(final PropertySerializerMap map, final JavaType type, final SerializerProvider provider) throws JsonMappingException {
            final PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSerializer(type, provider, this._property);
            if (map != result.map) {
                this._dynamicSerializers = result.map;
            }
            return (JsonSerializer<Object>)result.serializer;
        }
    }
    
    @JacksonStdImpl
    public static class IndexedListSerializer extends AsArraySerializer<List<?>>
    {
        public IndexedListSerializer(final JavaType elemType, final boolean staticTyping, final TypeSerializer vts, final BeanProperty property, final JsonSerializer<Object> valueSerializer) {
            super(List.class, elemType, staticTyping, vts, property, valueSerializer);
        }
        
        public ContainerSerializerBase<?> _withValueTypeSerializer(final TypeSerializer vts) {
            return new IndexedListSerializer(this._elementType, this._staticTyping, vts, this._property, this._elementSerializer);
        }
        
        public void serializeContents(final List<?> value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
            if (this._elementSerializer != null) {
                this.serializeContentsUsing(value, jgen, provider, this._elementSerializer);
                return;
            }
            if (this._valueTypeSerializer != null) {
                this.serializeTypedContents(value, jgen, provider);
                return;
            }
            final int len = value.size();
            if (len == 0) {
                return;
            }
            int i = 0;
            try {
                PropertySerializerMap serializers = this._dynamicSerializers;
                while (i < len) {
                    Object elem = null;
                    try {
                        elem = value.get(i);
                    }
                    catch (final Exception e) {
                        break;
                    }
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    }
                    else {
                        final Class<?> cc = elem.getClass();
                        JsonSerializer<Object> serializer = (JsonSerializer<Object>)serializers.serializerFor(cc);
                        if (serializer == null) {
                            if (this._elementType.hasGenericTypes()) {
                                serializer = this._findAndAddDynamic(serializers, this._elementType.forcedNarrowBy(cc), provider);
                            }
                            else {
                                serializer = this._findAndAddDynamic(serializers, cc, provider);
                            }
                            serializers = this._dynamicSerializers;
                        }
                        serializer.serialize(elem, jgen, provider);
                    }
                    ++i;
                }
            }
            catch (final Exception e2) {
                this.wrapAndThrow(provider, e2, value, i);
            }
        }
        
        public void serializeContentsUsing(final List<?> value, final JsonGenerator jgen, final SerializerProvider provider, final JsonSerializer<Object> ser) throws IOException, JsonGenerationException {
            final int len = value.size();
            if (len == 0) {
                return;
            }
            final TypeSerializer typeSer = this._valueTypeSerializer;
            for (int i = 0; i < len; ++i) {
                Object elem = null;
                try {
                    elem = value.get(i);
                }
                catch (final Exception e) {
                    break;
                }
                try {
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    }
                    else if (typeSer == null) {
                        ser.serialize(elem, jgen, provider);
                    }
                    else {
                        ser.serializeWithType(elem, jgen, provider, typeSer);
                    }
                }
                catch (final Exception e) {
                    this.wrapAndThrow(provider, e, value, i);
                }
            }
        }
        
        public void serializeTypedContents(final List<?> value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
            final int len = value.size();
            if (len == 0) {
                return;
            }
            int i = 0;
            try {
                final TypeSerializer typeSer = this._valueTypeSerializer;
                PropertySerializerMap serializers = this._dynamicSerializers;
                while (i < len) {
                    final Object elem = value.get(i);
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    }
                    else {
                        final Class<?> cc = elem.getClass();
                        JsonSerializer<Object> serializer = (JsonSerializer<Object>)serializers.serializerFor(cc);
                        if (serializer == null) {
                            if (this._elementType.hasGenericTypes()) {
                                serializer = this._findAndAddDynamic(serializers, this._elementType.forcedNarrowBy(cc), provider);
                            }
                            else {
                                serializer = this._findAndAddDynamic(serializers, cc, provider);
                            }
                            serializers = this._dynamicSerializers;
                        }
                        serializer.serializeWithType(elem, jgen, provider, typeSer);
                    }
                    ++i;
                }
            }
            catch (final Exception e) {
                this.wrapAndThrow(provider, (Throwable)e, (Object)value, i);
            }
        }
    }
    
    @JacksonStdImpl
    public static class CollectionSerializer extends AsArraySerializer<Collection<?>>
    {
        @Deprecated
        public CollectionSerializer(final JavaType elemType, final boolean staticTyping, final TypeSerializer vts, final BeanProperty property) {
            this(elemType, staticTyping, vts, property, null);
        }
        
        public CollectionSerializer(final JavaType elemType, final boolean staticTyping, final TypeSerializer vts, final BeanProperty property, final JsonSerializer<Object> valueSerializer) {
            super(Collection.class, elemType, staticTyping, vts, property, valueSerializer);
        }
        
        public ContainerSerializerBase<?> _withValueTypeSerializer(final TypeSerializer vts) {
            return new CollectionSerializer(this._elementType, this._staticTyping, vts, this._property);
        }
        
        public void serializeContents(final Collection<?> value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
            if (this._elementSerializer != null) {
                this.serializeContentsUsing(value, jgen, provider, this._elementSerializer);
                return;
            }
            final Iterator<?> it = value.iterator();
            if (!it.hasNext()) {
                return;
            }
            PropertySerializerMap serializers = this._dynamicSerializers;
            final TypeSerializer typeSer = this._valueTypeSerializer;
            int i = 0;
            try {
                do {
                    final Object elem = it.next();
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    }
                    else {
                        final Class<?> cc = elem.getClass();
                        JsonSerializer<Object> serializer = (JsonSerializer<Object>)serializers.serializerFor(cc);
                        if (serializer == null) {
                            if (this._elementType.hasGenericTypes()) {
                                serializer = this._findAndAddDynamic(serializers, this._elementType.forcedNarrowBy(cc), provider);
                            }
                            else {
                                serializer = this._findAndAddDynamic(serializers, cc, provider);
                            }
                            serializers = this._dynamicSerializers;
                        }
                        if (typeSer == null) {
                            serializer.serialize(elem, jgen, provider);
                        }
                        else {
                            serializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                    }
                    ++i;
                } while (it.hasNext());
            }
            catch (final Exception e) {
                this.wrapAndThrow(provider, (Throwable)e, (Object)value, i);
            }
        }
        
        public void serializeContentsUsing(final Collection<?> value, final JsonGenerator jgen, final SerializerProvider provider, final JsonSerializer<Object> ser) throws IOException, JsonGenerationException {
            final Iterator<?> it = value.iterator();
            if (it.hasNext()) {
                final TypeSerializer typeSer = this._valueTypeSerializer;
                int i = 0;
                do {
                    final Object elem = it.next();
                    try {
                        if (elem == null) {
                            provider.defaultSerializeNull(jgen);
                        }
                        else if (typeSer == null) {
                            ser.serialize(elem, jgen, provider);
                        }
                        else {
                            ser.serializeWithType(elem, jgen, provider, typeSer);
                        }
                        ++i;
                    }
                    catch (final Exception e) {
                        this.wrapAndThrow(provider, (Throwable)e, (Object)value, i);
                    }
                } while (it.hasNext());
            }
        }
    }
    
    @JacksonStdImpl
    public static class IteratorSerializer extends AsArraySerializer<Iterator<?>>
    {
        public IteratorSerializer(final JavaType elemType, final boolean staticTyping, final TypeSerializer vts, final BeanProperty property) {
            super(Iterator.class, elemType, staticTyping, vts, property);
        }
        
        public ContainerSerializerBase<?> _withValueTypeSerializer(final TypeSerializer vts) {
            return new IteratorSerializer(this._elementType, this._staticTyping, vts, this._property);
        }
        
        public void serializeContents(final Iterator<?> value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
            if (value.hasNext()) {
                final TypeSerializer typeSer = this._valueTypeSerializer;
                JsonSerializer<Object> prevSerializer = null;
                Class<?> prevClass = null;
                do {
                    final Object elem = value.next();
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    }
                    else {
                        final Class<?> cc = elem.getClass();
                        JsonSerializer<Object> currSerializer;
                        if (cc == prevClass) {
                            currSerializer = prevSerializer;
                        }
                        else {
                            currSerializer = (prevSerializer = (JsonSerializer<Object>)provider.findValueSerializer(cc, this._property));
                            prevClass = cc;
                        }
                        if (typeSer == null) {
                            currSerializer.serialize(elem, jgen, provider);
                        }
                        else {
                            currSerializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                    }
                } while (value.hasNext());
            }
        }
    }
    
    @JacksonStdImpl
    public static class IterableSerializer extends AsArraySerializer<Iterable<?>>
    {
        public IterableSerializer(final JavaType elemType, final boolean staticTyping, final TypeSerializer vts, final BeanProperty property) {
            super(Iterable.class, elemType, staticTyping, vts, property);
        }
        
        public ContainerSerializerBase<?> _withValueTypeSerializer(final TypeSerializer vts) {
            return new IterableSerializer(this._elementType, this._staticTyping, vts, this._property);
        }
        
        public void serializeContents(final Iterable<?> value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
            final Iterator<?> it = value.iterator();
            if (it.hasNext()) {
                final TypeSerializer typeSer = this._valueTypeSerializer;
                JsonSerializer<Object> prevSerializer = null;
                Class<?> prevClass = null;
                do {
                    final Object elem = it.next();
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    }
                    else {
                        final Class<?> cc = elem.getClass();
                        JsonSerializer<Object> currSerializer;
                        if (cc == prevClass) {
                            currSerializer = prevSerializer;
                        }
                        else {
                            currSerializer = (prevSerializer = (JsonSerializer<Object>)provider.findValueSerializer(cc, this._property));
                            prevClass = cc;
                        }
                        if (typeSer == null) {
                            currSerializer.serialize(elem, jgen, provider);
                        }
                        else {
                            currSerializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                    }
                } while (it.hasNext());
            }
        }
    }
    
    public static class EnumSetSerializer extends AsArraySerializer<EnumSet<? extends Enum<?>>>
    {
        public EnumSetSerializer(final JavaType elemType, final BeanProperty property) {
            super(EnumSet.class, elemType, true, null, property);
        }
        
        public ContainerSerializerBase<?> _withValueTypeSerializer(final TypeSerializer vts) {
            return this;
        }
        
        public void serializeContents(final EnumSet<? extends Enum<?>> value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
            JsonSerializer<Object> enumSer = this._elementSerializer;
            for (final Enum<?> en : value) {
                if (enumSer == null) {
                    enumSer = (JsonSerializer<Object>)provider.findValueSerializer(en.getDeclaringClass(), this._property);
                }
                enumSer.serialize((Object)en, jgen, provider);
            }
        }
    }
}
