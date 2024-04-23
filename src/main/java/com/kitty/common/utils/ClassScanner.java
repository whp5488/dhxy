package com.kitty.common.utils;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

public class ClassScanner {
  private static Logger logger = LoggerFactory.getLogger(ClassScanner.class);
  
  private static final Predicate<Class<?>> EMPTY_FILTER = clazz -> true;
  
  public static Set<Class<?>> getClasses(String scanPackage) {
    return getClasses(scanPackage, EMPTY_FILTER);
  }
  
  public static Set<Class<?>> listAllSubclasses(String scanPackage, Class<?> parent) {
    return getClasses(scanPackage, clazz -> 
        (parent.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())));
  }
  
  public static <A extends java.lang.annotation.Annotation> Set<Class<?>> listClassesWithAnnotation(String scanPackage, Class<A> annotation) {
    return getClasses(scanPackage, clazz -> (clazz.getAnnotation(annotation) != null));
  }
  
  public static Set<Class<?>> getClasses(String pack, Predicate<Class<?>> filter) {
    PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
    SimpleMetadataReaderFactory simpleMetadataReaderFactory = new SimpleMetadataReaderFactory((ResourceLoader)pathMatchingResourcePatternResolver);
    String path = ClassUtils.convertClassNameToResourcePath(pack);
    String location = "classpath:" + path + "/**/*.class";
    Set<Class<?>> result = new HashSet<>();
    try {
      Resource[] resources = pathMatchingResourcePatternResolver.getResources(location);
      for (Resource resource : resources) {
        MetadataReader metaReader = simpleMetadataReaderFactory.getMetadataReader(resource);
        if (resource.isReadable()) {
          String clazzName = metaReader.getClassMetadata().getClassName();
          if (!clazzName.contains("$")) {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(clazzName);
            if (filter.test(clazz))
              result.add(clazz); 
          } 
        } 
      } 
    } catch (Exception e) {
      logger.error("", e);
    } 
    return result;
  }
}
