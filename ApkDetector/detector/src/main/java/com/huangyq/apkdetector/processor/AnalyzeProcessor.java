package com.huangyq.apkdetector.processor;

import com.huangyq.apkdetector.Context;
import com.huangyq.apkdetector.Processor;
import com.huangyq.apkdetector.info.CallInfo;
import com.huangyq.apkdetector.info.MethodInfo;
import com.huangyq.apkdetector.util.IOUtil;
import com.huangyq.apkdetector.util.LogUtil;
import com.huangyq.apkdetector.util.SmaliUtil;

import java.io.*;
import java.util.List;

import static com.huangyq.apkdetector.Constant.*;

public class AnalyzeProcessor implements Processor {
    private final Processor processor;
    private Context context;
    private String filePath;
    private String fileName;
    private String callPlace;
    private String lineNumber;

    public AnalyzeProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public boolean process(Context context) {
        this.context = context;
        if (processSmaliPath(context.getDirName())) {
            return processor.process(context);
        }
        return false;
    }

    private boolean processSmaliPath(String dirName) {
        LogUtil.start("AnalyzeProcessor");
        File decompileDir = new File(dirName);
        if (!decompileDir.exists()) {
            LogUtil.e("dirName " + dirName + " is not exists");
            return false;
        }

        File[] fileList = decompileDir.listFiles();
        if (null == fileList || fileList.length == 0) {
            return false;
        }

        for (File file : fileList) {
            if (file.isDirectory() && file.getName().startsWith(STRING_SMALI)) {
                LogUtil.d("processSmaliPath " + file.getName());
                processDir(file);
            }
        }
        LogUtil.end("AnalyzeProcessor");
        return true;
    }

    private void processDir(File dir) {
        File[] files = dir.listFiles();
        if (null == files || files.length == 0) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                if (needToProcess(file.getPath())) {
                    processDir(file);
                }
            } else {
                processSmali(file);
            }
        }
    }

    private void processSmali(File smali) {
        List<String> smaliStringList = null;
        try {
            smaliStringList = IOUtil.read(smali);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (null == smaliStringList || smaliStringList.size() == 0) {
            return;
        }

        for (String str : smaliStringList) {
            if (str.startsWith(STRING_SMALI_CLASS)) {
                processClassString(str);
                // ???????????????.source?????????fileName?????????????????????.source?????????
                // ????????????filePath?????????fileName
                fileName = SmaliUtil.getFileNameByFilePath(filePath);
            } else if (str.startsWith(STRING_SMALI_METHOD)) {
                processMethodString(str);
            } else if (str.startsWith(STRING_SMALI_LINE)) {
                processLineString(str);
            } else {
                processString(str);
            }
        }
    }

    private void processClassString(String str) {
        filePath = SmaliUtil.getFilePath(str);
    }

    private void processSourceString(String str) {
        fileName = SmaliUtil.getFileName(str);
        // ??????????????????.source?????????????????????
        if (fileName.length() == 0) {
            fileName = SmaliUtil.getFileNameByFilePath(filePath);
        }
    }

    private void processMethodString(String str) {
        callPlace = SmaliUtil.getCallPlace(str);
    }

    private void processLineString(String str) {
        lineNumber = SmaliUtil.getLineNumber(str);
    }

    private void processString(String str) {
        for (MethodInfo methodInfo : context.getMethodInfoList()) {
            if (str.contains(methodInfo.getSmaliCallCode())) {
                methodInfo.getCallInfoList().add(new CallInfo(
                        filePath, fileName, callPlace, lineNumber, str));
                break;
            }
        }
    }

    private boolean needToProcess(String path) {
        boolean needToProcess = true;
        for (String excludeDir : context.getExcludeDirList()) {
            if (path.endsWith(excludeDir)) {
                needToProcess = false;
                break;
            }
        }
        return needToProcess;
    }
}
