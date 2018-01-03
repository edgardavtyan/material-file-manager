package com.davtyan.filemanager.utils;

public class BoolUtils {
    public static int compare(boolean a, boolean b) {
        return ((a == b) ? 0 : (a ? 1 : -1));
    }
}
