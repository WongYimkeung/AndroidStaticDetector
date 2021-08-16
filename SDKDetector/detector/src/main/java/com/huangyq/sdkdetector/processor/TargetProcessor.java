package com.huangyq.sdkdetector.processor;

import com.huangyq.sdkdetector.Context;
import com.huangyq.sdkdetector.Processor;
import com.huangyq.sdkdetector.info.MethodInfo;
import com.huangyq.sdkdetector.util.IOUtil;
import com.huangyq.sdkdetector.util.LogUtil;
import com.huangyq.sdkdetector.util.SmaliUtil;

import java.io.FileNotFoundException;
import java.util.List;

import static com.huangyq.sdkdetector.Constant.*;

public class TargetProcessor implements Processor {
    private final Processor processor;
    private String description;
    private String smaliClass;
    private Context context;

    public TargetProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public boolean process(Context context) {
        this.context = context;

        LogUtil.start("TargetFileProcessor");
        try {
            // 注意要使用TargetFileSmaliPath进行解析
            List<String> targetFileStringList = IOUtil.read(context.getTargetFileSmaliPath());
            for (String str : targetFileStringList) {
                processTargetFileString(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            List<String> targetSourceStringList = IOUtil.read(context.getTargetSource());
            for (String str : targetSourceStringList) {
                processTargetSourceString(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LogUtil.end("TargetFileProcessor");

        return processor.process(this.context);
    }

    private void processTargetFileString(String str) {
        if (str.startsWith(STRING_SMALI_CLASS)) {
            processClassString(str);
        } else if (str.startsWith(STRING_SMALI_METHOD)) {
            processMethodString(str);
        }
    }

    /**
     * 获取smali代码的类名
     *
     * @param str smali代码
     */
    private void processClassString(String str) {
        // .class public interface abstract Lcom/huangyq/sdkdetector/Processor;
        smaliClass = SmaliUtil.splitStringGetLast(str, " ");
    }

    /**
     * 获取smali代码的方法信息
     *
     * @param str smali代码
     */
    private void processMethodString(String str) {
        // .method public abstract init(Landroid/content/Context;)V
        String targetString = SmaliUtil.splitStringGetLast(str, " ");
        String methodName = SmaliUtil.splitStringGetFirst(targetString, "\\(");
        context.getMethodInfoList().add(new MethodInfo(
                methodName, targetString, smaliClass, null));
    }

    private void processTargetSourceString(String str) {
        if (str.startsWith(STRING_DESCRIPTION_SYMBOL)) {
            processDescriptionString(str);
        } else {
            processCodeString(str);
        }
    }

    private void processDescriptionString(String str) {
        // # 注释代码
        if (null == description) {
            description = "";
        }
        description += SmaliUtil.replace(str, STRING_DESCRIPTION_SYMBOL, "");
    }

    private void processCodeString(String str) {
        String smaliCode, smaliClass;

        if (str.contains(STRING_SMALI_CALL_SYMBOL)) {
            smaliCode = SmaliUtil.splitStringGetLast(str, STRING_SMALI_CALL_SYMBOL);
            smaliClass = SmaliUtil.splitStringGetFirst(str, STRING_SMALI_CALL_SYMBOL);
            context.getMethodInfoList().add(new MethodInfo(
                    SmaliUtil.getMethodName(smaliCode), smaliCode, smaliClass, description));
        } else {
            smaliCode = str;
            context.getMethodInfoList().add(new MethodInfo(
                    "", smaliCode, "", description));
        }

        description = null; // 添加MethodInfo之后需要清空
    }
}
