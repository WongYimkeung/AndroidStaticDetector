package com.huangyq.apkdetector.info;

import java.util.ArrayList;
import java.util.List;

public class MethodInfo {
    String name;    // 方法名称
    String smaliClass;  // 方法的smali所属类
    String smaliMethod; // 方法的smali定义
    String description; // 方法的描述
    List<CallInfo> callInfoList = new ArrayList<>();    // 方法调用信息

    public MethodInfo(String name, String smaliClass, String smaliMethod, String description) {
        this.name = name;
        this.smaliClass = smaliClass;
        this.smaliMethod = smaliMethod;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getSmaliMethod() {
        return smaliMethod;
    }

    public String getSmaliClass() {
        return smaliClass;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 获取smali代码中的调用代码
     * 如果类不存在，则格式：方法定义
     * 如果类存在，则格式：类->方法定义
     *
     * @return smali代码中的调用代码
     */
    public String getSmaliCallCode() {
        if (null == smaliClass || smaliClass.length() == 0) {
            return smaliMethod;
        }
        return smaliClass + "->" + smaliMethod;
    }

    public List<CallInfo> getCallInfoList() {
        return callInfoList;
    }

    public int getCallInfoSize() {
        return callInfoList.size();
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "name='" + name + '\'' +
                ", smaliClass='" + smaliClass + '\'' +
                ", smaliMethod='" + smaliMethod + '\'' +
                ", description='" + description + '\'' +
                ", callInfoList=" + callInfoList +
                '}';
    }
}
