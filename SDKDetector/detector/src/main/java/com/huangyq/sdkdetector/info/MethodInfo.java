package com.huangyq.sdkdetector.info;

import java.util.ArrayList;
import java.util.List;

public class MethodInfo {
    String name;    // 方法名称
    String smaliCode;   // 方法的smali定义
    String smaliClass;  // 方法的smali所属类
    String description; // 方法的描述
    List<CallInfo> callInfoList = new ArrayList<>();    // 方法调用信息

    public MethodInfo(String name, String smaliCode, String smaliClass, String description) {
        this.name = name;
        this.smaliCode = smaliCode;
        this.smaliClass = smaliClass;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getSmaliCode() {
        return smaliCode;
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
            return smaliCode;
        }
        return smaliClass + "->" + smaliCode;
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
                ", smaliCode='" + smaliCode + '\'' +
                ", smaliClass='" + smaliClass + '\'' +
                ", description='" + description + '\'' +
                ", callInfoList=" + callInfoList +
                '}';
    }
}
