package com.kitty.common.utils;

import org.slf4j.LoggerFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.io.support.ResourcePatternResolver;
import java.util.HashSet;
import org.springframework.util.ClassUtils;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.function.Predicate;
import org.slf4j.Logger;

public class ClassScanner
{
    private static Logger logger;
    private static final Predicate<Class<?>> EMPTY_FILTER;
    
    public ClassScanner() {
    }
    
    public static Set<Class<?>> getClasses(String scanPackage) {
        return getClasses(scanPackage, ClassScanner.EMPTY_FILTER);
    }
    
    public static Set<Class<?>> listAllSubclasses(String scanPackage, Class<?> parent) {
        return getClasses(scanPackage, clazz -> parent.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers()));
    }
    
    public static <A extends Annotation> Set<Class<?>> listClassesWithAnnotation(String scanPackage, Class<A> annotation) {
        return getClasses(scanPackage, clazz -> clazz.getAnnotation(annotation) != null);
    }
    
    public static Set<Class<?>> getClasses(String pack, Predicate<Class<?>> filter) {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metaFactory = new SimpleMetadataReaderFactory(patternResolver);
        String path = ClassUtils.convertClassNameToResourcePath(pack);
        String location = "classpath:" + path + "/**/*.class";
        Set<Class<?>> result = new HashSet<>();
        try {
            Resource[] resources;
            for (Resource resource : resources = patternResolver.getResources(location)) {
                MetadataReader metaReader = metaFactory.getMetadataReader(resource);
                if (resource.isReadable()) {
                    String clazzName = metaReader.getClassMetadata().getClassName();
                    if (!clazzName.contains("$")) {
                        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(clazzName);
                        if (filter.test(clazz)) {
                            result.add(clazz);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            ClassScanner.logger.error("", e);
        }
        return result;
    }
    
    static {
        ClassScanner.logger = LoggerFactory.getLogger(ClassScanner.class);
        EMPTY_FILTER = (clazz -> true);
    }
}
