package com.davtyan.filemanager.lib;

import java.util.HashMap;

public class ExtensionToIconMap {
    private final HashMap<String[], Integer> map;
    private final Integer defaultIcon;

    public ExtensionToIconMap(HashMap<String[], Integer> map, Integer defaultIcon) {
        this.map = map;
        this.defaultIcon = defaultIcon;
    }

    public int getIcon(String extension) {
        for (String[] key : map.keySet()) {
            if (contains(key, extension)) {
                return map.get(key);
            }
        }

        return defaultIcon;
    }

    private boolean contains(String[] array, String value) {
        for (String s : array) {
            if (s.equals(value)) {
                return true;
            }
        }

        return false;
    }
}
