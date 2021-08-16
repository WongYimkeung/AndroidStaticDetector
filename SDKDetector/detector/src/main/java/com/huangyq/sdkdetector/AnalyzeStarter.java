package com.huangyq.sdkdetector;

import com.huangyq.sdkdetector.processor.*;
import com.huangyq.sdkdetector.util.LogUtil;
import com.huangyq.sdkdetector.util.Util;

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

    public synchronized void startProcess(String[] args) {
        if (isProcessing) {
            LogUtil.e("is processing, can't start again");
            return;
        }

        isProcessing = true;
        long startAnalyzeTime = System.currentTimeMillis();
        LogUtil.r("\n=============== Analyze started ===============\n");

        Context context = new Context();
        PdfProcessor pdfProcessor
                = new PdfProcessor();
        AnalyzeProcessor analyzeProcessor
                = new AnalyzeProcessor(pdfProcessor);
        TargetProcessor targetProcessor
                = new TargetProcessor(analyzeProcessor);
        DecompileProcessor decompileProcessor
                = new DecompileProcessor(targetProcessor);
        ArgumentsProcessor argumentsProcessor
                = new ArgumentsProcessor(decompileProcessor, args);

        boolean result = argumentsProcessor.process(context);
        long stopAnalyzeTime = System.currentTimeMillis();
        LogUtil.r("\nAnalyze result is " + (result ? "success" : "fail"));
        LogUtil.r("takes " + Util.getCostTime(startAnalyzeTime, stopAnalyzeTime));

        LogUtil.r("=============== Analyze finished ===============\n");
        isProcessing = false;
    }
}
