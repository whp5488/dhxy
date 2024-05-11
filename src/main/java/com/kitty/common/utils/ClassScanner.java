
package com.kitty.common.utils;

import org.slf4j.LoggerFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.io.support.ResourcePatternResolver;
import java.util.HashSet;
import org.springframework.util.ClassUtils;
import org.springframework.core.io.ResourceLoader;
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
    
    public static Set<Class<?>> getClasses(final String scanPackage) {
        return getClasses(scanPackage, ClassScanner.EMPTY_FILTER);
    }
    
    public static Set<Class<?>> listAllSubclasses(final String scanPackage, final Class<?> parent) {
        return getClasses(scanPackage, clazz -> parent.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers()));
    }
    
    public static <A extends Annotation> Set<Class<?>> listClassesWithAnnotation(final String scanPackage, final Class<A> annotation) {
        return getClasses(scanPackage, clazz -> clazz.getAnnotation(annotation) != null);
    }
    
    public static Set<Class<?>> getClasses(final String pack, final Predicate<Class<?>> filter) {
        final ResourcePatternResolver patternResolver = (ResourcePatternResolver)new PathMatchingResourcePatternResolver();
        final MetadataReaderFactory metaFactory = (MetadataReaderFactory)new SimpleMetadataReaderFactory((ResourceLoader)patternResolver);
        final String path = ClassUtils.convertClassNameToResourcePath(pack);
        final String location = "classpath:" + path + "/**/*.class";
        final Set<Class<?>> result = new HashSet<Class<?>>();
        try {
            final Resource[] resources2;
            final Resource[] resources = resources2 = patternResolver.getResources(location);
            for (final Resource resource : resources2) {
                final MetadataReader metaReader = metaFactory.getMetadataReader(resource);
                if (resource.isReadable()) {
                    final String clazzName = metaReader.getClassMetadata().getClassName();
                    if (!clazzName.contains("$")) {
                        final Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(clazzName);
                        if (filter.test(clazz)) {
                            result.add(clazz);
                        }
                    }
                }
            }
        }
        catch (final Exception e) {
            ClassScanner.logger.error("", (Throwable)e);
        }
        return result;
    }
    
    static {
        ClassScanner.logger = LoggerFactory.getLogger(ClassScanner.class);
        EMPTY_FILTER = (clazz -> true);
    }
}
