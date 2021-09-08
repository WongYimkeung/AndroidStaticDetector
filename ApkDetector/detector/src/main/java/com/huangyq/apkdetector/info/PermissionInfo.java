package com.huangyq.apkdetector.info;

public class PermissionInfo {
    String name;    // 权限名称
    String level;   // 权限等级
    String description; // 权限说明
    boolean isDeclared; // 是否声明

    public PermissionInfo(String name, String level, String description, boolean isDeclared) {
        this.name = name;
        this.level = level;
        this.description = description;
        this.isDeclared = isDeclared;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeclared() {
        return isDeclared;
    }

    public void setDeclared(boolean declared) {
        isDeclared = declared;
    }

    @Override
    public String toString() {
        return "PermissionInfo{" +
                "name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", description='" + description + '\'' +
                ", isDeclared=" + isDeclared +
                '}';
    }
}
