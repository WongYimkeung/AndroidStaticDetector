package com.huangyq.apkdetector;

import com.huangyq.apkdetector.info.MethodInfo;
import com.huangyq.apkdetector.info.PermissionInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.huangyq.apkdetector.Constant.STRING_ANDROID_MANIFEST;

public class Context {
    private String apkPath;   // 待反编译Apk路径
    private String dirName;   // 反编译内容输出文件夹
    private String sourceFile;  // 检测内容JSON文件
    private String interfaceFile;   // 接口定义Java文件
    private String permissionFile;  // 权限声明JSON文件
    private final List<String> excludeDirList
            = new ArrayList<>(Arrays.asList("android", "androidx"));    // 不做检测的路径
    private final List<MethodInfo> methodInfoList = new ArrayList<>();
    private final List<PermissionInfo> permissionInfoList = new ArrayList<>();

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

    public String getInterfaceFile() {
        return interfaceFile;
    }

    public void setInterfaceFile(String interfaceFile) {
        this.interfaceFile = interfaceFile;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getPermissionFile() {
        return permissionFile;
    }

    public void setPermissionFile(String permissionFile) {
        this.permissionFile = permissionFile;
    }

    public List<String> getExcludeDirList() {
        return excludeDirList;
    }

    public List<MethodInfo> getMethodInfoList() {
        return methodInfoList;
    }

    public List<PermissionInfo> getPermissionInfoList() {
        return permissionInfoList;
    }

    public String getAndroidManifestPath() {
        return dirName + File.separator + STRING_ANDROID_MANIFEST;
    }
}
