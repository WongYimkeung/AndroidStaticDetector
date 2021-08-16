package com.huangyq.sdkdetector;

import com.huangyq.sdkdetector.util.LogUtil;

public class MainApplication {
    public static void main(String[] args) {
        LogUtil.setLogLevel(LogUtil.DEBUG);
        AnalyzeStarter.getInstance().startProcess(args);
    }
}
