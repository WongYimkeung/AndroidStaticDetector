package com.huangyq.sdkdetector.processor;

import com.huangyq.sdkdetector.Context;
import com.huangyq.sdkdetector.Processor;
import com.huangyq.sdkdetector.info.MethodInfo;
import com.huangyq.sdkdetector.info.PermissionInfo;
import com.huangyq.sdkdetector.util.IOUtil;
import com.huangyq.sdkdetector.util.LogUtil;
import com.huangyq.sdkdetector.util.SmaliUtil;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static com.huangyq.sdkdetector.Constant.*;

public class TargetProcessor implements Processor {
    private final Processor processor;
    private String smaliClass;
    private Context context;

    public TargetProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public boolean process(Context context) {
        this.context = context;

        LogUtil.start("TargetProcessor");
        try {
            // 注意要使用TargetFileSmaliPath进行解析
            String path = findTargetFileSmaliPath(context.getDirName());
            if (null != path) {
                List<String> targetFileStringList = IOUtil.read(path);
                for (String str : targetFileStringList) {
                    processTargetFileString(str);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        processSourceJSON(context.getSourceFile());
        processPermissionJSON(context.getPermissionFile());
        LogUtil.end("TargetProcessor");

        return processor.process(this.context);
    }

    /**
     * 处理分包情况下的接口文件路径
     *
     * @param dirName 反编译文件夹名称
     * @return 接口文件路径
     */
    private String findTargetFileSmaliPath(String dirName) {
        File decompileDir = new File(dirName);
        if (!decompileDir.exists()) {
            return null;
        }

        File[] files = decompileDir.listFiles();
        if (null == files || files.length == 0) {
            return null;
        }

        for (File file : files) {
            if (file.isDirectory() && file.getName().startsWith(STRING_SMALI)) {
                File targetFile = new File(file.getPath() + File.separator +
                        context.getTargetFileWithSmaliSuffix());
                if (targetFile.exists()) {
                    LogUtil.r(targetFile.getPath());
                    return targetFile.getPath();
                }
            }
        }

        return null;
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

    private void processSourceJSON(String sourceFile) {
        try {
            List<String> readList = IOUtil.read(sourceFile);
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : readList) {
                stringBuilder.append(str);
            }

            JSONArray source = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < source.length(); i++) {
                JSONObject jsonObject = source.getJSONObject(i);
                String code = jsonObject.getString("code");
                String description = jsonObject.getString("description");

                if (code.contains(STRING_SMALI_CALL_SYMBOL)) {
                    String smaliClass = SmaliUtil.splitStringGetFirst(code, STRING_SMALI_CALL_SYMBOL);
                    String smaliCode = SmaliUtil.splitStringGetLast(code, STRING_SMALI_CALL_SYMBOL);
                    String name = SmaliUtil.getMethodName(smaliCode);
                    context.getMethodInfoList().add(new MethodInfo(
                            name, smaliCode, smaliClass, description));
                } else {
                    context.getMethodInfoList().add(new MethodInfo(
                            code, code, "", description));
                }
            }
        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processPermissionJSON(String permissionFile) {
        try {
            List<String> readList = IOUtil.read(permissionFile);
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : readList) {
                stringBuilder.append(str);
            }

            JSONArray permission = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < permission.length(); i++) {
                JSONObject jsonObject = permission.getJSONObject(i);
                context.getPermissionInfoList().add(new PermissionInfo(
                        jsonObject.getString("name"),
                        jsonObject.getString("level"),
                        jsonObject.getString("description"),
                        jsonObject.getBoolean("isDeclared")));
            }
        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
