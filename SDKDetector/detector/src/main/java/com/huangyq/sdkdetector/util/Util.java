package com.huangyq.sdkdetector.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static String getCostTime(long startTime, long stopTime) {
        long costTime = (stopTime - startTime) / 1000;  // 计算花费时间，单位秒
        long h = costTime / (60 * 60);
        long m = (costTime - h * 60 * 60) / 60;
        long s = costTime - h * 60 * 60 - m * 60;
        long ms = (stopTime - startTime) % 1000;
        return h + " hour(s) " + m + " minute(s) " + s + " second(s) " + ms + " millisecond(s)";
    }

    public static String getPdfName(String name) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return name + "-" + simpleDateFormat.format(new Date()) + ".pdf";
    }
}
