package com.huangyq.sdkdetector.processor;

import com.huangyq.sdkdetector.Context;
import com.huangyq.sdkdetector.Processor;
import com.huangyq.sdkdetector.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DecompileProcessor implements Processor {
    private static final int INT_VALUE_SUCCESS = 0;
    private final Processor processor;

    public DecompileProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public boolean process(Context context) {
        if (decompile(context.getApkPath())) {
            return processor.process(context);
        }
        return false;
    }

    public boolean decompile(String apkPath) {
        LogUtil.start("DecompileProcessor");
        boolean success = true;
        try {
            Process process = Runtime.getRuntime().exec("./apktool d " + apkPath);
            success = handleProcess(process);
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        LogUtil.end("DecompileProcessor");
        return success;
    }

    private boolean handleProcess(Process process) {
        try {
            String result;

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((result = reader.readLine()) != null) {
                LogUtil.r(result);
            }
            reader.close();

            int exitValue = process.exitValue();
            LogUtil.r("\nexitValue = " + exitValue);
            if (exitValue == INT_VALUE_SUCCESS) {
                return true;
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((result = errorReader.readLine()) != null) {
                LogUtil.e(result);
            }
            errorReader.close();
            return false;
        } catch (IllegalThreadStateException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
