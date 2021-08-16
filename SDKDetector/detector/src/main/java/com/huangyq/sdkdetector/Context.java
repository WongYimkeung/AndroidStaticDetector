package com.huangyq.sdkdetector;

import com.huangyq.sdkdetector.info.MethodInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.huangyq.sdkdetector.Constant.STRING_SMALI;
import static com.huangyq.sdkdetector.Constant.STRING_SMALI_SUFFIX;

public class Context {
    private String apkPath;   // 待反编译Apk路径
    private String dirName;   // 反编译内容输出文件夹
    private String targetFile;  // 接口定义文件
    private String targetSource;    // 检测内容文件
    private final List<String> excludeDirList
            = new ArrayList<>(Arrays.asList("android", "androidx"));    // 不做检测的路径
    private final List<MethodInfo> methodInfoList = new ArrayList<>();

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(String targetFile) {
        this.targetFile = targetFile;
    }

    public String getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(String targetSource) {
        this.targetSource = targetSource;
    }

    public List<String> getExcludeDirList() {
        return excludeDirList;
    }

    public List<MethodInfo> getMethodInfoList() {
        return methodInfoList;
    }

    public String getSmaliPath() {
        return dirName + File.separator + STRING_SMALI + File.separator;
    }

    public String getTargetFileSmaliPath() {
        return getSmaliPath() + targetFile + STRING_SMALI_SUFFIX;
    }
}
