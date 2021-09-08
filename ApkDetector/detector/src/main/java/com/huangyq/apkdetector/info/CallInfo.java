package com.huangyq.apkdetector.info;

public class CallInfo {
    String filePath;    // 文件路径
    String fileName;    // 文件名称
    String callPlace;   // 方法被调用的地方
    String lineNumber;  // 代码所在行号
    String lineContent; // 代码内容

    public CallInfo(String filePath, String fileName,
                    String callPlace, String lineNumber, String lineContent) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.callPlace = callPlace;
        this.lineNumber = lineNumber;
        this.lineContent = lineContent;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCallPlace() {
        return callPlace;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public String getLineContent() {
        return lineContent;
    }

    @Override
    public String toString() {
        return "CallInfo{" +
                "filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", callPlace='" + callPlace + '\'' +
                ", lineNumber='" + lineNumber + '\'' +
                ", lineContent='" + lineContent + '\'' +
                '}';
    }
}
