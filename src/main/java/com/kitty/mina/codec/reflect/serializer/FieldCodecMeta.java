package com.kitty.mina.codec.reflect.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public class FieldCodecMeta {
  private Field field;
  
  private Class<?> type;
  
  private Serializer serializer;
  
  private Class<?> wrapper;
  
  public static FieldCodecMeta valueOf(Field field, Serializer serializer) {
    FieldCodecMeta meta = new FieldCodecMeta();
    meta.field = field;
    Class<?> type = field.getType();
    meta.type = type;
    meta.serializer = serializer;
    if (Collection.class.isAssignableFrom(type)) {
      meta.wrapper = (Class)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
    } else if (type.isArray()) {
      meta.wrapper = type.getComponentType();
    } 
    return meta;
  }
  
  public Field getField() {
    return this.field;
  }
  
  public Class<?> getType() {
    return this.type;
  }
  
  public Serializer getSerializer() {
    return this.serializer;
  }
  
  public Class<?> getWrapper() {
    return this.wrapper;
  }
  
  public String toString() {
    return "FieldCodecMeta [field=" + this.field + ", type=" + this.type + ", serializer=" + this.serializer + ", wrapper=" + this.wrapper + "]";
  }
}
