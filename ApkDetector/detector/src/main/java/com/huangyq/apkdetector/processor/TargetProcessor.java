package com.huangyq.apkdetector.processor;

import com.huangyq.apkdetector.Context;
import com.huangyq.apkdetector.Processor;
import com.huangyq.apkdetector.info.MethodInfo;
import com.huangyq.apkdetector.info.PermissionInfo;
import com.huangyq.apkdetector.util.IOUtil;
import com.huangyq.apkdetector.util.LogUtil;
import com.huangyq.apkdetector.util.SmaliUtil;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static com.huangyq.apkdetector.Constant.*;

public class TargetProcessor implements Processor {
    private final Processor processor;
    private Context context;

    public TargetProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public boolean process(Context context) {
        this.context = context;

        LogUtil.start("TargetProcessor");
        processInterfaceFile(context.getInterfaceFile());
        processSourceJSON(context.getSourceFile());
        processPermissionJSON(context.getPermissionFile());
        LogUtil.end("TargetProcessor");

        return processor.process(this.context);
    }

    private void processInterfaceFile(String interfaceFile) {
        if (null == interfaceFile || interfaceFile.length() == 0) {
            // 没有配置interfaceFile，直接返回null
            return;
        }

        try {
            String path = findInterfaceFileSmaliPath(context.getDirName());
            if (null != path) {
                List<String> targetFileStringList = IOUtil.read(path);
                String smaliClass = "";

                for (String str : targetFileStringList) {
                    if (str.startsWith(STRING_SMALI_CLASS)) {
                        smaliClass = processClassString(str);
                    } else if (str.startsWith(STRING_SMALI_METHOD)) {
                        processMethodString(str, smaliClass);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理分包情况下的接口文件路径
     *
     * @param dirName 反编译文件夹名称
     * @return 接口文件路径
     */
    private String findInterfaceFileSmaliPath(String dirName) {
        File decompileDir = new File(dirName);
        if (!decompileDir.exists()) {
            return null;
        }

        File[] files = decompileDir.listFiles();
        if (null == files || files.length == 0) {
            return null;
        }

        String interfaceFileSmali = File.separator
                + context.getInterfaceFile() + STRING_SMALI_SUFFIX;
        for (File file : files) {
            if (file.isDirectory() && file.getName().startsWith(STRING_SMALI)) {
                File targetFile = new File(file.getPath() + interfaceFileSmali);
                if (targetFile.exists()) {
                    LogUtil.r(targetFile.getPath());
                    return targetFile.getPath();
                }
            }
        }

        return null;
    }

    /**
     * 获取smali代码的类名
     *
     * @param str smali代码
     */
    private String processClassString(String str) {
        // .class public interface abstract Lcom/huangyq/apkdetector/Processor;
        return SmaliUtil.splitStringGetLast(str, " ");
    }

    /**
     * 获取smali代码的方法信息
     *
     * @param str smali代码
     */
    private void processMethodString(String str, String smaliClass) {
        // .method public abstract init(Landroid/content/Context;)V
        String smaliMethod = SmaliUtil.splitStringGetLast(str, " ");
        String methodName = SmaliUtil.splitStringGetFirst(smaliMethod, "\\(");
        context.getMethodInfoList().add(new MethodInfo(
                methodName, smaliClass, smaliMethod, null));
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
                    String smaliMethod = SmaliUtil.splitStringGetLast(code, STRING_SMALI_CALL_SYMBOL);
                    String methodName = SmaliUtil.getMethodName(smaliMethod);
                    context.getMethodInfoList().add(new MethodInfo(
                            methodName, smaliClass, smaliMethod, description));
                } else {
                    context.getMethodInfoList().add(new MethodInfo(
                            code, "", code, description));
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
