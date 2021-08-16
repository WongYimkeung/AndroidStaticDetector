package com.huangyq.sdkdetector.processor;

import com.huangyq.sdkdetector.Context;
import com.huangyq.sdkdetector.Processor;
import com.huangyq.sdkdetector.util.LogUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static com.huangyq.sdkdetector.Constant.*;

public class ArgumentsProcessor implements Processor {
    private final Processor processor;
    private final String[] args;
    private Context context;

    public ArgumentsProcessor(Processor processor, String[] args) {
        this.processor = processor;
        this.args = args;
    }

    @Override
    public boolean process(Context context) {
        this.context = context;
        if (handleArguments(args)) {
            return processor.process(this.context);
        }
        return false;
    }

    public boolean handleArguments(String[] args) {
        if (null == args || args.length == 0) {
            throw new IllegalArgumentException("no argument found");
        }

        if (args.length == 1 && args[0].startsWith(KEY_CONFIG_FILE)) {
            String[] splitString = args[0].split("=");
            return handleConfigFile(splitString[1]);
        }

        boolean result = true;
        for (String arg : args) {
            LogUtil.d(arg);

            String[] splitString = arg.split("=");
            if (splitString.length < 2) {
                LogUtil.e("arg " + arg + "'s format is wrong");
                continue;
            }

            switch (splitString[0]) {
                case KEY_APK_PATH:
                    result = handleApkPath(splitString[1]);
                    break;
                case KEY_EXCLUDE_DIR:
                    handleExcludeDir(splitString[1]);
                    break;
                case KEY_TARGET_FILE:
                    handleTargetFile(splitString[1]);
                    break;
                case KEY_TARGET_SOURCE:
                    handleTargetSource(splitString[1]);
                    break;
                default:
                    LogUtil.e("unknown arg " + splitString[0]);
                    break;
            }
        }
        return result;
    }

    private boolean handleConfigFile(String configFile) {
        if (null == configFile || configFile.length() == 0) {
            LogUtil.e("configFile is null");
            return false;
        }

        boolean result = true;
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(configFile));
            if (properties.containsKey(KEY_APK_PATH)) {
                result = handleApkPath(properties.getProperty(KEY_APK_PATH));
            }
            if (properties.containsKey(KEY_EXCLUDE_DIR)) {
                handleExcludeDir(properties.getProperty(KEY_EXCLUDE_DIR));
            }
            if (properties.containsKey(KEY_TARGET_FILE)) {
                handleTargetFile(properties.getProperty(KEY_TARGET_FILE));
            }
            if (properties.containsKey(KEY_TARGET_SOURCE)) {
                handleTargetSource(properties.getProperty(KEY_TARGET_SOURCE));
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    private boolean handleApkPath(String apkPath) {
        if (null == apkPath || apkPath.length() == 0
                || !apkPath.endsWith(STRING_APK_SUFFIX)) {
            LogUtil.e("apkPath is illegal");
            return false;
        }

        File file = new File(apkPath);
        if (!file.exists()) {
            LogUtil.e("apkPath " + apkPath + " is not exists");
            return false;
        }

        context.setApkPath(apkPath);
        context.setDirName(file.getName().replace(STRING_APK_SUFFIX, ""));
        LogUtil.r("handleApkPath apkPath = " + context.getApkPath());
        LogUtil.r("handleApkPath dirName = " + context.getDirName());

        return true;
    }

    private void handleExcludeDir(String excludeDir) {
        String[] splitExcludeDir = excludeDir.split(",");
        for (String dir : splitExcludeDir) {
            context.getExcludeDirList().add(dir);
        }
        LogUtil.r("handleExcludeDir " + context.getExcludeDirList());
    }

    private void handleTargetFile(String targetFile) {
        context.setTargetFile(targetFile);
        LogUtil.r("handleTargetFile targetFile = " + context.getTargetFile());
        LogUtil.r("handleTargetFile targetFilePath = " + context.getTargetFileSmaliPath());
    }

    private void handleTargetSource(String targetSource) {
        context.setTargetSource(targetSource);
        LogUtil.r("handleTargetSource targetSource = " + context.getTargetSource());
    }
}
