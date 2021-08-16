package com.huangyq.sdkdetector.util;

public class LogUtil {
    public static final int DEBUG = 0;
    public static final int RELEASE = 1;

    private static int logLevel = DEBUG;

    public static void setLogLevel(int logLevel) {
        LogUtil.logLevel = logLevel;
    }

    public static void d(String msg) {
        if (DEBUG >= logLevel) {
            System.out.println(msg);
        }
    }

    public static void d(Object obj) {
        if (DEBUG >= logLevel) {
            System.out.println(obj);
        }
    }

    public static void r(String msg) {
        if (RELEASE >= logLevel) {
            System.out.println(msg);
        }
    }

    public static void r(Object obj) {
        if (RELEASE >= logLevel) {
            System.out.println(obj);
        }
    }

    public static void e(String msg) {
        if (RELEASE >= logLevel) {
            System.out.println("Error: " + msg);
        }
    }

    public static void e(Object obj) {
        if (RELEASE >= logLevel) {
            System.out.println("Error: " + obj);
        }
    }

    public static void start(String msg) {
        if (RELEASE >= logLevel) {
            System.out.println("\n+++++++++++++++ " + msg + " start +++++++++++++++\n");
        }
    }

    public static void end(String msg) {
        if (RELEASE >= logLevel) {
            System.out.println("\n--------------- " + msg + " end ---------------\n");
        }
    }
}
