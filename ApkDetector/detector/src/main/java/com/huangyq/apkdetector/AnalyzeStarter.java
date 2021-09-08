package com.huangyq.apkdetector;

import com.huangyq.apkdetector.processor.*;
import com.huangyq.apkdetector.util.LogUtil;
import com.huangyq.apkdetector.util.Util;

public class AnalyzeStarter {
    private static AnalyzeStarter analyzeStarter;
    private boolean isProcessing = false;

    private AnalyzeStarter() {

    }

    public static AnalyzeStarter getInstance() {
        if (null == analyzeStarter) {
            synchronized (AnalyzeStarter.class) {
                if (null == analyzeStarter) {
                    analyzeStarter = new AnalyzeStarter();
                }
            }
        }
        return analyzeStarter;
    }

    /**
     * 开始检测，检测流程如下：
     * 1、处理配置参数
     * 2、反编译APK
     * 3、处理目标文件（包括目标接口文件、隐私相关代码和权限声明）
     * 4、处理AndroidManifest.xml
     * 5、处理smali源码
     * 6、输出结果
     *
     * @param args
     */
    public synchronized void startProcess(String[] args) {
        if (isProcessing) {
            LogUtil.e("is processing, can't start again");
            return;
        }

        isProcessing = true;
        long startAnalyzeTime = System.currentTimeMillis();
        LogUtil.start("Analyze");

        Context context = new Context();
        PdfProcessor pdfProcessor
                = new PdfProcessor();
        AnalyzeProcessor analyzeProcessor
                = new AnalyzeProcessor(pdfProcessor);
        ManifestProcessor manifestProcessor
                = new ManifestProcessor(analyzeProcessor);
        TargetProcessor targetProcessor
                = new TargetProcessor(manifestProcessor);
        DecompileProcessor decompileProcessor
                = new DecompileProcessor(targetProcessor);
        ArgumentsProcessor argumentsProcessor
                = new ArgumentsProcessor(decompileProcessor, args);

        boolean result = argumentsProcessor.process(context);
        long stopAnalyzeTime = System.currentTimeMillis();
        LogUtil.r("\nAnalyze result is " + (result ? "success" : "fail"));
        LogUtil.r("takes " + Util.getCostTime(startAnalyzeTime, stopAnalyzeTime));

        LogUtil.end("Analyze");
        isProcessing = false;
    }
}
