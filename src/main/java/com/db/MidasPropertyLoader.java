package com.db;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MidasPropertyLoader {

    private final Properties properties;

    public MidasPropertyLoader(String fileName) throws Exception {
        String propertiesFile = fileName;
        ClassLoader loader = null;
        if (fileName.contains("classpath:")) {
            loader = Thread.currentThread().getContextClassLoader();
            propertiesFile = fileName.replaceFirst("classpath:/", "");
        }
        properties = PropertiesLoaderUtils.loadAllProperties(propertiesFile, loader);
    }


    public Map<String, String> getMap(String prefix) {
        Set<Object> keys = properties.keySet();
        List<String> prefixKeys = keys.stream().filter(key -> ((String) key).contains(prefix)).map(value -> (String) value).collect(toList());
        return toMap(prefixKeys);
    }

    public Map<String, Map<String, String>> getMapOfMap(String prefix) {
        Set<Object> keys = properties.keySet();
        List<String> prefixKeys = keys.stream().filter(key -> ((String) key).contains(prefix)).map(value -> (String) value).collect(toList());
        return toMapOfMap(prefixKeys);
    }

    private Map<String, String> toMap(List<String> prefixKeys) {
        return prefixKeys.stream().collect(Collectors.toMap(v -> v.substring(v.lastIndexOf(".") + 1, v.length()), v -> properties.getProperty(v)));
    }

    private Map<String, Map<String, String>> toMapOfMap(List<String> prefixKeys) {
        Map<String, Map<String, String>> newMap = prefixKeys.stream().collect(Collectors.toMap(key -> key.substring(key.indexOf("[") + 1, key.lastIndexOf("]")), key -> new HashMap<String, String>(), (m1, m2) -> m1));
        prefixKeys.stream().forEach(key -> populateMap(key, newMap));
        return newMap;
    }

    private void populateMap(String key, Map<String, Map<String, String>> newMap) {
        String outerMapKey = key.substring(key.indexOf("[") + 1, key.lastIndexOf("]"));
        int innerMapKeyValue = key.lastIndexOf("]") + 1;
        String innerMapKey = key.substring(key.lastIndexOf(".", innerMapKeyValue) + 1, key.length());
        Map<String, String> innerMap = newMap.get(outerMapKey);
        if (innerMap != null) {
            innerMap.put(innerMapKey, properties.getProperty(key));
        }


    }
}
