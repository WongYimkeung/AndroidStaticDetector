package com.huangyq.apkdetector.util;

import static com.huangyq.apkdetector.Constant.STRING_JAVA_SUFFIX;

public class SmaliUtil {
    public static String getFilePath(String str) {
        String path = splitStringGetLast(str, " ");
        path = path.replaceFirst("L", "");
        return path.replace(";", STRING_JAVA_SUFFIX);
    }

    public static String getFileName(String str) {
        String name = splitStringGetLast(str, " ");
        return name.replaceAll("\"", "");
    }

    public static String getFileNameByFilePath(String str) {
        String name = splitStringGetLast(str, "/");
        return name.replace(";", "");
    }

    public static String getCallPlace(String str) {
        return splitStringGetLast(str, " ");
    }

    public static String getLineNumber(String str) {
        return splitStringGetLast(str, " ");
    }

    public static String getMethodName(String str) {
        if (str.contains("(")) {
            return splitStringGetFirst(str, "\\(");
        }
        return splitStringGetFirst(str, ":");
    }

    public static String splitStringGetFirst(String str, String regex) {
        if (null == str || str.length() == 0) {
            return "";
        }
        String[] splitStr = str.split(regex);
        return splitStr[0].trim();
    }

    public static String splitStringGetLast(String str, String regex) {
        if (null == str || str.length() == 0) {
            return "";
        }
        String[] splitStr = str.split(regex);
        return splitStr[splitStr.length - 1].trim();
    }

    public static String replace(String str, String target, String replacement) {
        if (null == str || str.length() == 0) {
            return "";
        }
        return str.replace(target, replacement).trim();
    }
}
