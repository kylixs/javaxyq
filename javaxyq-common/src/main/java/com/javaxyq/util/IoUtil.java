package com.javaxyq.util;

import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IoUtil {

    public static String DEL = System.getProperty("line.separator");

    public static PathMatchingResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

    @SneakyThrows
    public static String readTextFromLocalFs(String path) {
        return readText(new FileInputStream(path));
    }

    public static String readTextFromClasspath(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null)
            classLoader = IoUtil.class.getClassLoader();
        return readText(classLoader.getResourceAsStream(path));
    }

    public static String readText(InputStream is) {
        return new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining(DEL));
    }

    // Support pattern:
    //     file:/absolute_path/filename.ext
    //     file:relative_path/filename.ext
    //     classpath:path/filename.ext
    public static Resource loadResource(String path) {
        return RESOURCE_RESOLVER.getResource(path);
    }

    // Support pattern:
    //     file:/absolute_path/**/file*.ext
    //     file:relative_path/file*.ext
    //     classpath:**/file*.ext
    public static Resource[] loadResources(String path) {
        try {
            return RESOURCE_RESOLVER.getResources(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static File loadFile(String path) {
        return loadResource(path).getFile();
    }

    @SneakyThrows
    public static File[] loadFiles(String path) {
        Resource[] resources = loadResources(path);
        File[] files = new File[resources.length];
        for (int i = 0; i < resources.length; i++) {
            files[i] = resources[i].getFile();
        }
        return files;
    }
}
